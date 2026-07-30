/*
 * Copyright (c) 2026 Auxio Project
 * DiagnosticBundleExporter.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.oxycblt.auxio.diagnostics

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Process
import androidx.annotation.VisibleForTesting
import java.io.File
import java.io.OutputStream
import java.security.DigestOutputStream
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream
import org.oxycblt.auxio.BuildConfig
import org.oxycblt.auxio.music.MusicSettings
import org.oxycblt.auxio.util.StartupPerformanceReport

/** Debug-only deterministic, integrity-checked local bundle exporter. No upload is performed. */
object DiagnosticBundleExporter {
    data class Options(
        val hashPaths: Boolean = false,
        val redactDeviceIdentifiers: Boolean = false,
        val integrationReport: String? = null,
    )

    fun create(
        context: Context,
        journal: DiagnosticJournal,
        musicSettings: MusicSettings,
        options: Options = Options(),
    ): File {
        val outputDirectory =
            File(context.getExternalFilesDir(null) ?: context.filesDir, "diagnostics")
        check(outputDirectory.exists() || outputDirectory.mkdirs()) {
            "Unable to create ${outputDirectory.absolutePath}"
        }

        val timestamp = SimpleDateFormat("yyyyMMdd-HHmmss", Locale.US).format(Date())
        val output = uniqueOutput(File(outputDirectory, "auxio-ts-diagnostics-$timestamp.zip"))
        val partial = File(output.parentFile, "${output.name}.partial")
        partial.delete()

        val apk = File(context.applicationInfo.sourceDir)
        val apkSha256 = apk.takeIf(File::isFile)?.let(::sha256)
        val pathPrivacyFilter =
            if (options.hashPaths) {
                PathPrivacyFilter(
                    musicSettings.configuredSourceSpecs.flatMap {
                        listOf(it.displayPath, it.normalizedUri.toString(), it.sourceKey)
                    }
                )
            } else {
                null
            }
        fun privacyFiltered(value: String): String = pathPrivacyFilter?.filter(value) ?: value
        val expectedEntries = linkedSetOf<String>()
        val checksumLines = mutableListOf<String>()
        ZipOutputStream(partial.outputStream().buffered()).use { zip ->
            fun payload(name: String, write: (OutputStream) -> Unit) {
                expectedEntries += name
                checksumLines += "${zip.writeEntry(name, write)}  $name"
            }

            payload("manifest.json") {
                it.writeUtf8(manifest(context, journal, options, apkSha256))
            }
            payload("source-state.txt") {
                it.writeUtf8(sourceState(musicSettings, options.hashPaths))
            }
            payload("journal-current.jsonl") { outputStream ->
                journal.snapshot().forEach { event ->
                    outputStream.writeUtf8(
                        DiagnosticJournal.toJsonLine(event.filtered(pathPrivacyFilter)) + "\n"
                    )
                }
            }
            payload("performance.txt") {
                it.writeUtf8(
                    privacyFiltered(
                        StartupPerformanceReport.render(
                            StartupPerformanceReport.CaptureContext(
                                authority = "deterministic-diagnostic-bundle",
                                sourceState =
                                    musicSettings.sourceConfigurationCheckpoint.toString(),
                                commit = BuildConfig.BUILD_COMMIT,
                            )
                        )
                    )
                )
            }
            payload("threads.txt") { it.writeUtf8(privacyFiltered(threadDump())) }
            options.integrationReport?.let { report ->
                payload("integration-check.txt") { it.writeUtf8(privacyFiltered(report)) }
            }
            journal.persistedFiles().take(MAX_PERSISTED_FILES).forEachIndexed { index, file ->
                payload("sessions/${index.toString().padStart(2, '0')}-${file.name}") { output ->
                    if (pathPrivacyFilter == null) {
                        file.inputStream().buffered().use { input -> input.copyTo(output) }
                    } else {
                        output.writeUtf8(pathPrivacyFilter.filter(file.readText(Charsets.UTF_8)))
                    }
                }
            }
            expectedEntries += CHECKSUM_ENTRY
            zip.writeEntry(CHECKSUM_ENTRY) {
                it.writeUtf8(checksumLines.joinToString(separator = "\n", postfix = "\n"))
            }
        }
        validate(partial, expectedEntries)
        check(partial.renameTo(output)) { "Unable to finalise ${output.absolutePath}" }
        return output
    }

    private fun manifest(
        context: Context,
        journal: DiagnosticJournal,
        options: Options,
        apkSha256: String?,
    ): String {
        @Suppress("DEPRECATION")
        val packageInfo =
            context.packageManager.getPackageInfo(
                context.packageName,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    PackageManager.GET_SIGNING_CERTIFICATES
                } else {
                    PackageManager.GET_SIGNATURES
                },
            )
        @Suppress("DEPRECATION")
        val signatures =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.signingInfo?.apkContentsSigners.orEmpty()
            } else {
                packageInfo.signatures.orEmpty()
            }
        val signingDigests = signatures.map { sha256(it.toByteArray()) }.sorted()
        val redact = options.redactDeviceIdentifiers
        return buildString {
            append("{\n")
            append("  \"schema\": 1,\n")
            append("  \"createdWallTime\": ${System.currentTimeMillis()},\n")
            append("  \"applicationId\": ${DiagnosticJson.string(BuildConfig.APPLICATION_ID)},\n")
            append("  \"versionName\": ${DiagnosticJson.string(BuildConfig.VERSION_NAME)},\n")
            append("  \"versionCode\": ${BuildConfig.VERSION_CODE},\n")
            append("  \"flavor\": ${DiagnosticJson.string(BuildConfig.FLAVOR)},\n")
            append("  \"buildType\": ${DiagnosticJson.string(BuildConfig.BUILD_TYPE)},\n")
            append("  \"commit\": ${DiagnosticJson.string(BuildConfig.BUILD_COMMIT)},\n")
            append("  \"apkSha256\": ${DiagnosticJson.string(apkSha256)},\n")
            append(
                "  \"signingCertificateSha256\": ${DiagnosticJson.string(signingDigests.joinToString())},\n"
            )
            append("  \"uid\": ${Process.myUid()},\n")
            append("  \"pid\": ${Process.myPid()},\n")
            append("  \"activeSessionId\": ${DiagnosticJson.string(journal.activeSessionId)},\n")
            append("  \"hashPaths\": ${options.hashPaths},\n")
            append("  \"redactDeviceIdentifiers\": ${options.redactDeviceIdentifiers},\n")
            append("  \"sdkInt\": ${Build.VERSION.SDK_INT},\n")
            append(
                "  \"manufacturer\": ${DiagnosticJson.string(if (redact) "<redacted>" else Build.MANUFACTURER)},\n"
            )
            append(
                "  \"model\": ${DiagnosticJson.string(if (redact) "<redacted>" else Build.MODEL)},\n"
            )
            append(
                "  \"fingerprint\": ${DiagnosticJson.string(if (redact) "<redacted>" else Build.FINGERPRINT)}\n"
            )
            append("}\n")
        }
    }

    private fun sourceState(settings: MusicSettings, hashPaths: Boolean): String = buildString {
        appendLine("locationMode=${settings.locationMode}")
        appendLine("generation=${settings.sourceConfigurationGeneration}")
        appendLine("checkpoint=${settings.sourceConfigurationCheckpoint}")
        appendLine("libraryState=${settings.libraryState}")
        appendLine("lastScanFailed=${settings.lastScanFailed}")
        appendLine("configuredSourceCount=${settings.configuredSourceCount}")
        settings.configuredSourceSpecs.forEachIndexed { index, source ->
            val path =
                if (hashPaths) "sha256:${sha256(source.displayPath.toByteArray())}"
                else source.displayPath
            val uri =
                if (hashPaths) "sha256:${sha256(source.normalizedUri.toString().toByteArray())}"
                else source.normalizedUri.toString()
            val key =
                if (hashPaths) "sha256:${sha256(source.sourceKey.toByteArray())}"
                else source.sourceKey
            appendLine(
                "source[$index]=mode:${source.mode} access:${source.accessState} " +
                    "key:$key path:$path uri:$uri"
            )
        }
    }

    private fun threadDump(): String = buildString {
        Thread.getAllStackTraces()
            .entries
            .sortedBy { it.key.name }
            .take(MAX_THREADS)
            .forEach { (thread, stack) ->
                appendLine(
                    "\"${thread.name}\" id=${thread.id} state=${thread.state} daemon=${thread.isDaemon}"
                )
                stack.take(MAX_FRAMES_PER_THREAD).forEach { appendLine("  at $it") }
                appendLine()
            }
    }

    private fun DiagnosticEvent.filtered(pathPrivacyFilter: PathPrivacyFilter?): DiagnosticEvent =
        if (pathPrivacyFilter == null) {
            this
        } else {
            copy(
                sessionId = pathPrivacyFilter.filterNullable(sessionId),
                category = pathPrivacyFilter.filter(category),
                event = pathPrivacyFilter.filter(event),
                detail = pathPrivacyFilter.filterNullable(detail),
                result = pathPrivacyFilter.filterNullable(result),
            )
        }

    private class PathPrivacyFilter(knownValues: Collection<String>) {
        private val knownValues =
            knownValues.filter(String::isNotBlank).distinct().sortedByDescending(String::length)

        fun filter(value: String): String = filterPathBearingText(value, knownValues)

        fun filterNullable(value: String?): String? = value?.let(::filter)
    }

    @VisibleForTesting
    internal fun filterPathBearingText(value: String, knownValues: Collection<String>): String {
        var filtered = value
        knownValues
            .filter(String::isNotBlank)
            .distinct()
            .sortedByDescending(String::length)
            .forEach { known -> filtered = filtered.replace(known, hashPathValue(known)) }
        filtered =
            SENSITIVE_PATH_FIELD.replace(filtered) { match ->
                val prefix = match.groupValues[1]
                val raw = match.groupValues[2].trim()
                if (raw.isBlank()) prefix else prefix + hashPathValue(raw)
            }
        filtered = URI_LIKE.replace(filtered) { hashPathValue(it.value) }
        return ABSOLUTE_PATH.replace(filtered) { hashPathValue(it.value) }
    }

    private fun hashPathValue(value: String): String =
        if (value.startsWith("sha256:") && value.length == SHA256_LABEL_LENGTH) {
            value
        } else {
            "sha256:${sha256(value.toByteArray(Charsets.UTF_8))}"
        }

    private fun validate(file: File, expectedEntries: Set<String>) {
        ZipFile(file).use { zip ->
            val names = zip.entries().asSequence().map { it.name }.toSet()
            check(names == expectedEntries) {
                "Diagnostic bundle entries differ: expected=$expectedEntries actual=$names"
            }
            zip.entries().asSequence().forEach { entry ->
                zip.getInputStream(entry).use { input ->
                    val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                    while (input.read(buffer) != -1) Unit
                }
            }
        }
    }

    private fun uniqueOutput(preferred: File): File {
        if (!preferred.exists()) return preferred
        val stem = preferred.name.removeSuffix(".zip")
        for (index in 1..99) {
            val candidate = File(preferred.parentFile, "$stem-$index.zip")
            if (!candidate.exists()) return candidate
        }
        error("Too many diagnostic bundles named $stem")
    }

    private fun sha256(file: File): String =
        file.inputStream().buffered().use { input ->
            val digest = MessageDigest.getInstance("SHA-256")
            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
            while (true) {
                val count = input.read(buffer)
                if (count < 0) break
                digest.update(buffer, 0, count)
            }
            digest.digest().toHex()
        }

    private fun sha256(bytes: ByteArray): String =
        MessageDigest.getInstance("SHA-256").digest(bytes).toHex()

    private fun ByteArray.toHex(): String =
        joinToString("") { byte -> "%02x".format(byte.toInt() and 0xff) }

    private fun ZipOutputStream.writeEntry(name: String, write: (OutputStream) -> Unit): String {
        val digest = MessageDigest.getInstance("SHA-256")
        putNextEntry(ZipEntry(name).apply { time = 0L })
        try {
            DigestOutputStream(this, digest).also(write).flush()
        } finally {
            closeEntry()
        }
        return digest.digest().toHex()
    }

    private fun OutputStream.writeUtf8(value: String) {
        write(value.toByteArray(Charsets.UTF_8))
    }

    private const val MAX_PERSISTED_FILES = 10
    private const val MAX_THREADS = 200
    private const val MAX_FRAMES_PER_THREAD = 100
    private const val SHA256_LABEL_LENGTH = 71
    private const val CHECKSUM_ENTRY = "checksums.sha256"
    private val SENSITIVE_PATH_FIELD =
        Regex("""(?i)(\b(?:item|path|uri|sources?|detected\s+path)\s*[:=]\s*)([^\r\n"}]*)""")
    private val URI_LIKE = Regex("""(?i)\b(?:content|file|document|https?|ftp)://[^\s"\\},]+""")
    private val ABSOLUTE_PATH = Regex("""(?<![A-Za-z0-9:])/(?:[^\s"\\},]+)""")
}
