/*
 * Copyright (c) 2026 Auxio Project
 * PreparedVolumeIndexStore.kt is part of Auxio.
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

package org.oxycblt.auxio.headunit.root.storage

import android.content.Context
import android.os.SystemClock
import android.util.AtomicFile
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
            if (generation < 0L || !volumeId.matches(id) || !seenIds.add(id) || state !in states) {
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
                    "raw_only",
                    "unavailable" -> selected == null
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
 * The cache is loaded without `su`. Resolution is authority- and cost-aware: a cached prepared
 * record may lead because its representative-file hint can be validated in O(1), while an actual
 * root process leads when the enabled capability can reduce raw/prepared/removable recovery cost.
 * Normal app-readable paths remain available without a blanket ordering rule.
 */
@Singleton
class PreparedVolumeIndexStore
@Inject
constructor(@ApplicationContext context: Context, private val rootStateHolder: RootStateHolder) {
    private val cacheDir = File(context.filesDir, "ts18-root-storage")
    private val cacheFile = File(cacheDir, "volumes.tsv")
    private val atomicCacheFile = AtomicFile(cacheFile)

    @Volatile private var records: List<PreparedVolumeRecord> = readCachedRecords()
    @Volatile private var lastRefreshElapsedMs = Long.MIN_VALUE

    fun cachedRecords(): List<PreparedVolumeRecord> = records

    fun cachedCandidatePaths(): List<String> = candidatePaths(records)

    /** Explicit/user-started refresh through the consented bounded root gate. */
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
     * Resolve one source using the lowest expected-cost authority that is available or consented.
     */
    fun resolveSourceSync(requestedPath: String): SourceResolution {
        val clean = requestedPath.replace('\\', '/').trimEnd('/').ifEmpty { "/" }
        if (!RootStorageCommandPolicy.isAllowedCanonicalStorageRoot(clean)) {
            return SourceResolution(clean, null, SourceAuthority.UNAVAILABLE, "unsafe_storage_path")
        }
        val rootEnabled = rootStateHolder.isUserEnabled()
        var rootAvailable = rootStateHolder.stateSnapshot() == RootStateHolder.State.Available
        var current = records
        var match = current.firstOrNull { belongsToRecord(clean, it) }
        var refreshed = false

        fun refreshMatch(force: Boolean): PreparedVolumeRecord? {
            current = refreshFromRootSync(force)
            refreshed = true
            match = current.firstOrNull { belongsToRecord(clean, it) }
            return match
        }

        val order =
            RootStorageAccelerationPolicy.choose(
                requestedPath = clean,
                rootEnabled = rootEnabled,
                rootAvailable = rootAvailable,
                hasCachedRecord = match != null,
            )

        when (order) {
            RootStorageResolutionOrder.CACHED_ROOT_METADATA_FIRST -> {
                resolveFromRecord(clean, match, "cached_root_metadata")?.let {
                    return it
                }
                if (rootAvailable) {
                    resolveFromRecord(clean, refreshMatch(force = true), "refreshed_root_metadata")
                        ?.let {
                            return it
                        }
                }
                resolveDirect(clean)?.let {
                    return it
                }
            }
            RootStorageResolutionOrder.REFRESHED_ROOT_METADATA_FIRST -> {
                resolveFromRecord(clean, refreshMatch(force = true), "refreshed_root_metadata")
                    ?.let {
                        return it
                    }
                resolveDirect(clean)?.let {
                    return it
                }
            }
            RootStorageResolutionOrder.DIRECT_FIRST -> {
                resolveDirect(clean)?.let {
                    return it
                }
            }
        }

        // This is an explicit source-resolution flow and the user has already enabled root storage.
        // After cheaper cached/direct attempts miss, a bounded consent probe restores acceleration
        // after process restart without imposing root work on cache restoration or first audio.
        if (rootEnabled && RootStorageAccelerationPolicy.isRemovablePath(clean)) {
            if (!rootAvailable) {
                rootAvailable = rootStateHolder.probeSync() == RootStateHolder.State.Available
            }
            if (rootAvailable && !refreshed) {
                resolveFromRecord(
                        clean,
                        refreshMatch(force = match == null),
                        "root_after_initial_miss",
                    )
                    ?.let {
                        return it
                    }
            }
        }

        val suffix = match?.let { suffixFor(clean, it) }.orEmpty()
        val rawBacking =
            match?.let { appendSuffix(it.rawPath, suffix) }
                ?: clean.takeIf { it.startsWith("/mnt/media_rw/usbdisk") }
        if (rawBacking != null && rootEnabled && rootAvailable) {
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

    private fun resolveDirect(clean: String): SourceResolution? {
        if (
            clean.startsWith("/mnt/media_rw/") ||
                !RootStorageCommandPolicy.isAllowedCanonicalStorageRoot(clean)
        ) {
            return null
        }
        val prepared = clean.startsWith("/storage/auxio-root/")
        val authority = SourceAuthorityValidator.classifyDirect(clean, prepared) ?: return null
        return SourceResolution(clean, clean, authority, "bounded_direct_validation_ok")
    }

    private fun resolveFromRecord(
        requestedPath: String,
        record: PreparedVolumeRecord?,
        detailPrefix: String,
    ): SourceResolution? {
        record ?: return null
        val suffix = suffixFor(requestedPath, record)
        for (candidate in candidatePaths(record, suffix)) {
            if (!RootStorageCommandPolicy.isAllowedCanonicalStorageRoot(candidate)) continue
            val prepared = candidate.startsWith("/storage/auxio-root/")
            val representative = representativeForCandidate(record, candidate, suffix)
            val authority =
                SourceAuthorityValidator.classifyDirect(
                    path = candidate,
                    preparedAlias = prepared,
                    representativePath = representative,
                ) ?: continue
            val detail =
                if (representative != null) {
                    "${detailPrefix}_representative_open_ok"
                } else {
                    "${detailPrefix}_bounded_walk_ok"
                }
            return SourceResolution(requestedPath, candidate, authority, detail)
        }
        return null
    }

    private fun candidatePaths(record: PreparedVolumeRecord, suffix: String): List<String> {
        val roots = linkedSetOf<String>()
        record.selectedPath?.let(roots::add)
        roots += record.appPath
        if (record.state == "alias_candidate") roots += record.aliasPath
        return roots.map { appendSuffix(it, suffix) }
    }

    private fun representativeForCandidate(
        record: PreparedVolumeRecord,
        candidatePath: String,
        suffix: String,
    ): String? {
        val selectedRoot = record.selectedPath ?: return null
        val sample = record.samplePath ?: return null
        if (sample != selectedRoot && !sample.startsWith(selectedRoot.trimEnd('/') + "/"))
            return null
        val relativeSample = sample.removePrefix(selectedRoot).trimStart('/')
        val tail =
            if (suffix.isBlank()) {
                relativeSample
            } else {
                if (relativeSample != suffix && !relativeSample.startsWith("$suffix/")) return null
                relativeSample.removePrefix(suffix).trimStart('/')
            }
        return appendSuffix(candidatePath, tail)
    }

    private fun readCachedRecords(): List<PreparedVolumeRecord> =
        runCatching {
                val text = atomicCacheFile.readFully().toString(Charsets.UTF_8)
                PreparedVolumeManifestCodec.parse(text)
            }
            .getOrNull()
            .orEmpty()

    private fun writeAtomically(text: String): Boolean {
        if (!cacheDir.isDirectory && !cacheDir.mkdirs()) return false
        val output =
            try {
                atomicCacheFile.startWrite()
            } catch (_: Exception) {
                return false
            }
        return try {
            output.write(text.toByteArray(Charsets.UTF_8))
            atomicCacheFile.finishWrite(output)
            true
        } catch (_: Exception) {
            runCatching { atomicCacheFile.failWrite(output) }
            false
        }
    }

    private fun candidatePaths(values: List<PreparedVolumeRecord>): List<String> {
        val out = linkedSetOf<String>()
        values.forEach { record ->
            record.selectedPath?.let(out::add)
            when (record.state) {
                "app_candidate" -> out += record.appPath
                "alias_candidate" -> {
                    out += record.aliasPath
                    out += record.appPath
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
                .maxByOrNull(String::length) ?: return ""
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
