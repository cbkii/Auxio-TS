/*
 * Copyright (c) 2026 Auxio Project
 * PreparedVolumeIndexStore.kt is part of Auxio.
 */
package org.oxycblt.auxio.headunit.root.storage

import android.content.Context
import android.os.SystemClock
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import org.oxycblt.auxio.headunit.root.RootStateHolder
import org.oxycblt.auxio.headunit.root.RootStorageCommandPolicy

/** Source authority granted to a path after end-to-end validation. */
enum class SourceAuthority {
    APP_READABLE,
    PREPARED_ALIAS,
    ROOT_SNAPSHOT_ONLY,
    UNAVAILABLE,
}

data class SourceResolution(
    val requestedPath: String,
    val resolvedPath: String?,
    val authority: SourceAuthority,
    val detail: String,
)

data class PreparedVolumeRecord(
    val generationSeconds: Long,
    val volumeId: String,
    val rawPath: String,
    val appPath: String,
    val aliasPath: String,
    val selectedPath: String?,
    val state: String,
    val samplePath: String?,
)

/** Parser for the fixed Magisk helper TSV. */
object PreparedVolumeManifestCodec {
    private val volumeId = Regex("^usbdisk\\d+$", RegexOption.IGNORE_CASE)
    private val states = setOf("app_candidate", "alias_candidate", "raw_only", "unavailable")

    fun parse(text: String): List<PreparedVolumeRecord>? {
        val out = mutableListOf<PreparedVolumeRecord>()
        val seenIds = mutableSetOf<String>()
        for (line in text.lineSequence()) {
            if (line.isBlank() || line.startsWith("#")) continue
            val parts = line.split('\t', limit = 9)
            if (parts.size != 9 || parts[0] != MANIFEST_VERSION) return null
            val generation = parts[1].toLongOrNull() ?: return null
            val id = parts[2].lowercase()
            val raw = parts[3]
            val app = parts[4]
            val alias = parts[5]
            val selected = parts[6].takeUnless { it == "-" }
            val state = parts[7]
            val sample = parts[8].takeUnless { it == "-" }
            if (
                generation < 0L ||
                    !volumeId.matches(id) ||
                    !seenIds.add(id) ||
                    state !in states
            ) {
                return null
            }
            if (
                raw != "/mnt/media_rw/$id" ||
                    app != "/storage/$id" ||
                    alias != "/storage/auxio-root/$id" ||
                    !validPath(raw) ||
                    !validPath(app) ||
                    !validPath(alias)
            ) {
                return null
            }
            val selectedMatchesState =
                when (state) {
                    "app_candidate" -> selected == app
                    "alias_candidate" -> selected == alias
                    "raw_only", "unavailable" -> selected == null
                    else -> false
                }
            if (!selectedMatchesState) return null
            if (sample != null && (selected == null || !isWithin(sample, selected))) return null
            out +=
                PreparedVolumeRecord(
                    generationSeconds = generation,
                    volumeId = id,
                    rawPath = raw,
                    appPath = app,
                    aliasPath = alias,
                    selectedPath = selected,
                    state = state,
                    samplePath = sample,
                )
        }
        return out.sortedWith(
            compareByDescending<PreparedVolumeRecord> { it.generationSeconds }
                .thenBy { it.volumeId }
        )
    }

    private fun validPath(path: String): Boolean =
        path != "-" && RootStorageCommandPolicy.isAllowedStorageRoot(path)

    private fun isWithin(candidate: String, root: String): Boolean =
        candidate == root || candidate.startsWith(root.trimEnd('/') + "/")

    private const val MANIFEST_VERSION = "1"
}

/**
 * App-private cache and resolver for the Magisk-prepared TS18 volume manifest.
 *
 * The cache is loaded without `su`. Refreshes are explicit, serialized and coalesced, then accepted
 * only after strict parsing and an atomic app-private write.
 */
@Singleton
class PreparedVolumeIndexStore
@Inject
constructor(
    @ApplicationContext context: Context,
    private val rootStateHolder: RootStateHolder,
) {
    private val cacheDir = File(context.filesDir, "ts18-root-storage")
    private val cacheFile = File(cacheDir, "volumes.tsv")

    @Volatile private var records: List<PreparedVolumeRecord> = readCachedRecords()
    @Volatile private var lastRefreshElapsedMs = Long.MIN_VALUE

    fun cachedRecords(): List<PreparedVolumeRecord> = records

    fun cachedCandidatePaths(): List<String> = candidatePaths(records)

    /** Explicit/user-started refresh. This may perform the bounded Magisk consent probe. */
    @Synchronized
    fun refreshFromRootSync(force: Boolean = false): List<PreparedVolumeRecord> {
        if (!rootStateHolder.isUserEnabled()) return records
        val now = SystemClock.elapsedRealtime()
        if (!force && now - lastRefreshElapsedMs in 0 until MIN_REFRESH_INTERVAL_MS) {
            return records
        }
        lastRefreshElapsedMs = now
        val text = rootStateHolder.refreshPreparedVolumeManifestSync() ?: return records
        val parsed = PreparedVolumeManifestCodec.parse(text) ?: return records
        if (!writeAtomically(text)) return records
        records = parsed
        return records
    }

    /**
     * Resolve a requested path without invoking root when ordinary app access already works.
     *
     * Root preparation/snapshot work occurs only after the zero-root direct check fails.
     */
    fun resolveSourceSync(requestedPath: String): SourceResolution {
        val clean = requestedPath.replace('\\', '/').trimEnd('/').ifEmpty { "/" }
        if (!clean.startsWith("/mnt/media_rw/")) {
            val direct = SourceAuthorityValidator.classifyDirect(clean, preparedAlias = false)
            if (direct == SourceAuthority.APP_READABLE) {
                return SourceResolution(clean, clean, direct, "app_uid_direct_access_ok")
            }
        }

        var current = records
        var match = current.firstOrNull { belongsToRecord(clean, it) }
        if (rootStateHolder.isUserEnabled()) {
            current = refreshFromRootSync(force = match == null || clean.startsWith("/mnt/media_rw/"))
            match = current.firstOrNull { belongsToRecord(clean, it) }
        }
        val suffix = match?.let { suffixFor(clean, it) }.orEmpty()
        val candidates = linkedSetOf<String>()
        if (!clean.startsWith("/mnt/media_rw/")) candidates += clean
        if (match != null) {
            candidates += appendSuffix(match.appPath, suffix)
            match.selectedPath?.let { candidates += appendSuffix(it, suffix) }
            candidates += appendSuffix(match.aliasPath, suffix)
        }

        for (candidate in candidates) {
            if (candidate.startsWith("/mnt/media_rw/")) continue
            val prepared = candidate.startsWith("/storage/auxio-root/")
            val authority = SourceAuthorityValidator.classifyDirect(candidate, prepared) ?: continue
            return SourceResolution(clean, candidate, authority, "app_uid_resolved_access_ok")
        }

        val rawBacking =
            match?.let { appendSuffix(it.rawPath, suffix) }
                ?: clean.takeIf { it.startsWith("/mnt/media_rw/usbdisk") }
        if (rawBacking != null && rootStateHolder.isUserEnabled()) {
            val snapshot =
                rootStateHolder.snapshotTreeSync(
                    rawBacking,
                    maxDepth = ROOT_AUTHORITY_DEPTH,
                    timeoutMs = ROOT_AUTHORITY_TIMEOUT_MS,
                )
            if (snapshot != null) {
                return SourceResolution(
                    clean,
                    null,
                    SourceAuthority.ROOT_SNAPSHOT_ONLY,
                    "root_snapshot_without_app_uid_media_access",
                )
            }
        }
        return SourceResolution(
            clean,
            null,
            SourceAuthority.UNAVAILABLE,
            "no_valid_app_readable_path",
        )
    }

    private fun readCachedRecords(): List<PreparedVolumeRecord> =
        runCatching { PreparedVolumeManifestCodec.parse(cacheFile.readText()) }
            .getOrNull()
            .orEmpty()

    private fun writeAtomically(text: String): Boolean =
        runCatching {
                cacheDir.mkdirs()
                val temp = File(cacheDir, "volumes.tsv.tmp")
                temp.writeText(text)
                if (!temp.renameTo(cacheFile)) {
                    temp.copyTo(cacheFile, overwrite = true)
                    temp.delete()
                }
                true
            }
            .getOrDefault(false)

    private fun candidatePaths(values: List<PreparedVolumeRecord>): List<String> {
        val out = linkedSetOf<String>()
        values.forEach { record ->
            when (record.state) {
                "app_candidate" -> out += record.appPath
                "alias_candidate" -> {
                    out += record.appPath
                    out += record.aliasPath
                }
            }
        }
        return out.toList()
    }

    private fun belongsToRecord(path: String, record: PreparedVolumeRecord): Boolean =
        listOfNotNull(record.rawPath, record.appPath, record.aliasPath, record.selectedPath).any {
            path == it || path.startsWith(it.trimEnd('/') + "/")
        }

    private fun suffixFor(path: String, record: PreparedVolumeRecord): String {
        val root =
            listOfNotNull(record.rawPath, record.appPath, record.aliasPath, record.selectedPath)
                .filter { path == it || path.startsWith(it.trimEnd('/') + "/") }
                .maxByOrNull(String::length)
                ?: return ""
        return path.removePrefix(root).trimStart('/')
    }

    private fun appendSuffix(root: String, suffix: String): String =
        if (suffix.isBlank()) root else root.trimEnd('/') + "/" + suffix

    private companion object {
        const val MIN_REFRESH_INTERVAL_MS = 2_000L
        const val ROOT_AUTHORITY_DEPTH = 4
        const val ROOT_AUTHORITY_TIMEOUT_MS = 5_000L
    }
}
