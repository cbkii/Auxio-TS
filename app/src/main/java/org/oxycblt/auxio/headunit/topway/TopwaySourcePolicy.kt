/*
 * Copyright (c) 2026 Auxio Project
 * TopwaySourcePolicy.kt is part of Auxio.
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

package org.oxycblt.auxio.headunit.topway

import java.io.File
import java.net.URI
import java.util.ArrayDeque
import org.oxycblt.musikr.fs.RootGate
import timber.log.Timber as L

/** Policy for identifying and prioritizing TS18-specific candidate music source roots. */
object TopwaySourcePolicy {
    const val USB_DISK_0 = "/storage/usbdisk0"
    const val EMULATED_ROOT = "/storage/emulated/0"
    const val EMULATED_MUSIC = "$EMULATED_ROOT/Music"
    const val SDCARD_ROOT = "/sdcard"
    const val SDCARD_MUSIC = "$SDCARD_ROOT/Music"

    val SAFE_GENERIC_FALLBACKS = listOf(EMULATED_ROOT, EMULATED_MUSIC, SDCARD_ROOT, SDCARD_MUSIC)
    val TS18_USB_EXAMPLE_CANDIDATES = listOf(USB_DISK_0)

    @Deprecated("Use dynamic discoverCandidateRoots(); this is an observed example seed only")
    val TS18_USB_CANDIDATES = TS18_USB_EXAMPLE_CANDIDATES

    val CANDIDATE_ROOTS = SAFE_GENERIC_FALLBACKS + TS18_USB_EXAMPLE_CANDIDATES

    val NOISY_DIRS =
        setOf(
            "Android",
            "Download",
            "DCIM",
            "Pictures",
            "Movies",
            ".zjinnova",
            ".Tcfg",
            ".DFMusicLog",
        )

    val SYSTEM_SOURCE_PATH_KEYWORDS = listOf("music", "download", "media")

    private val BLOCKED_SOURCE_PREFIXES =
        listOf("/", "/system", "/vendor", "/data", "/proc", "/sys", "/dev")
    private val USB_DISK_SOURCE_REGEX =
        Regex("^/storage/usbdisk\\d+(/.*)?$", RegexOption.IGNORE_CASE)
    private val MEDIA_RW_USB_SOURCE_REGEX =
        Regex("^/mnt/media_rw/usbdisk\\d+(/.*)?$", RegexOption.IGNORE_CASE)
    private val STORAGE_UUID_SOURCE_REGEX = Regex("^/storage/[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}(/.*)?$")
    private val AUDIO_EXTENSIONS =
        setOf("mp3", "flac", "m4a", "mp4", "wav", "ogg", "opus", "aac", "3gp", "amr", "wma")

    private const val MAX_SCAN_DEPTH = 4
    private const val MAX_VISITED_FILES = 2500
    private const val MAX_CANDIDATES = 48
    private const val MAX_SCAN_ELAPSED_MS = 1200L

    internal data class FileEntry(val file: File, val isDirectory: Boolean, val isFile: Boolean)

    /** Bounded evidence for one explicit source-discovery operation. */
    data class DiscoveryEvidence(
        val configuredSourceCount: Int,
        val rootsWalked: Int,
        val visitedEntries: Int,
        val elapsedMs: Long,
        val usbProbeRequested: Boolean,
        val usbProbeReason: String,
    )

    data class DiscoveryResult(val candidates: List<String>, val evidence: DiscoveryEvidence)

    fun matchesSystemSourceFilter(fullPath: String): Boolean {
        val lower = fullPath.lowercase()
        return SYSTEM_SOURCE_PATH_KEYWORDS.any { lower.contains(it) }
    }

    fun isAccessibleCandidate(path: String): Boolean =
        try {
            File(path).let { it.exists() && it.isDirectory && it.canRead() }
        } catch (_: SecurityException) {
            false
        } catch (_: RuntimeException) {
            false
        }

    fun discoverCandidateRoots(): List<String> =
        discoverCandidateRoots(
            File("/storage"),
            File("/mnt/media_rw"),
            includeGenericFallbacks = true,
        )

    internal fun discoverCandidateRoots(
        storageRoot: File,
        mediaRwRoot: File,
        includeGenericFallbacks: Boolean = false,
    ): List<String> {
        val out = linkedSetOf<String>()
        if (includeGenericFallbacks) {
            SAFE_GENERIC_FALLBACKS.filterTo(out) { isAccessibleCandidate(it) }
        }
        discoverChildren(storageRoot, removableOnly = false).filterTo(out) {
            isAccessibleCandidate(it)
        }
        discoverChildren(mediaRwRoot, removableOnly = true).filterTo(out) {
            isAccessibleCandidate(it)
        }
        return preferAppFacingRoots(out).toList()
    }

    private fun discoverChildren(root: File, removableOnly: Boolean): List<String> {
        val children =
            try {
                root.listFiles()
            } catch (e: Exception) {
                L.w(e, "Failed to list candidate roots under ${root.absolutePath}")
                null
            } ?: return emptyList()
        return children
            .asSequence()
            .filter { it.isDirectory }
            .filter { !removableOnly || it.name.startsWith("usbdisk", ignoreCase = true) }
            .filter { it.name != "self" && it.name != "emulated" }
            .sortedWith(
                compareBy<File> { !it.name.startsWith("usbdisk", ignoreCase = true) }
                    .thenBy { it.absolutePath }
            )
            .map { it.absolutePath }
            .toList()
    }

    fun discoverMusicSourceCandidates(
        savedPaths: Collection<String> = emptyList(),
        mediaStoreParents: Collection<String> = emptyList(),
        storageRoots: Collection<String> = emptyList(),
        rootGate: RootGate? = null,
        allowUnconfiguredUsb: Boolean = false,
    ): List<String> =
        discoverMusicSourceCandidatesWithEvidence(
                savedPaths,
                mediaStoreParents,
                storageRoots,
                rootGate,
                allowUnconfiguredUsb,
            )
            .candidates

    /**
     * Performs one bounded candidate-discovery operation.
     *
     * Unconfigured removable roots are never walked unless the caller explicitly opts in. Saved
     * paths are normalised once and used as a set so `/mnt/media_rw/usbdiskN` and the app-facing
     * `/storage/usbdiskN` form cannot drift into different priority decisions.
     */
    fun discoverMusicSourceCandidatesWithEvidence(
        savedPaths: Collection<String> = emptyList(),
        mediaStoreParents: Collection<String> = emptyList(),
        storageRoots: Collection<String> = emptyList(),
        rootGate: RootGate? = null,
        allowUnconfiguredUsb: Boolean = false,
    ): DiscoveryResult {
        val startedAt = System.currentTimeMillis()
        val saved =
            savedPaths.mapNotNull(::normaliseCandidatePath).filter(::isAllowedSourceCandidate)
        val savedSet = saved.toSet()
        val media =
            mediaStoreParents
                .mapNotNull(::normaliseCandidatePath)
                .filter(::isAllowedSourceCandidate)
        val configuredUsb = saved.any(::isUsbCandidate)
        val shouldProbeUsb = allowUnconfiguredUsb || configuredUsb
        val usbProbeReason =
            when {
                allowUnconfiguredUsb -> "explicit-user-source-discovery"
                configuredUsb -> "configured-removable-source"
                else -> "disabled-no-configured-removable-source"
            }

        val candidates = mutableListOf<String>()
        candidates.addAll(SAFE_GENERIC_FALLBACKS)
        val normalisedStorageRoots =
            storageRoots.mapNotNull(::normaliseCandidatePath).filter(::isAllowedSourceCandidate)
        candidates.addAll(
            normalisedStorageRoots.filter { root ->
                !isUsbCandidate(root) || allowUnconfiguredUsb || root in savedSet
            }
        )

        val discoveredRoots = if (shouldProbeUsb) discoverCandidateRoots() else emptyList()
        candidates.addAll(
            discoveredRoots.filter { root ->
                !isUsbCandidate(root) || allowUnconfiguredUsb || root in savedSet
            }
        )

        val roots = preferAppFacingRoots(candidates).filter(::isAllowedSourceCandidate)
        val audioParents = linkedSetOf<String>()
        val deadline = startedAt + MAX_SCAN_ELAPSED_MS
        var visitedEntries = 0
        var rootsWalked = 0
        for (root in roots) {
            if (audioParents.size >= MAX_CANDIDATES || System.currentTimeMillis() > deadline) break
            rootsWalked++
            visitedEntries +=
                discoverAudioParents(File(root), audioParents, rootGate, deadline = deadline)
        }
        val musicFolders =
            roots.mapNotNull {
                musicChildIfAccessible(it) ?: it.takeIf { path -> path.endsWith("/Music", true) }
            }
        val usb = roots.filter(::isUsbCandidate)
        val generic = roots.filterNot(::isUsbCandidate)
        val ordered = linkedSetOf<String>()
        listOf(saved, media, audioParents.toList(), musicFolders, usb, generic).forEach { group ->
            group.filterTo(ordered, ::isAllowedSourceCandidate)
        }
        val result = ordered.take(MAX_CANDIDATES)
        val evidence =
            DiscoveryEvidence(
                configuredSourceCount = saved.size,
                rootsWalked = rootsWalked,
                visitedEntries = visitedEntries.coerceAtMost(MAX_VISITED_FILES * rootsWalked),
                elapsedMs = (System.currentTimeMillis() - startedAt).coerceAtLeast(0L),
                usbProbeRequested = shouldProbeUsb,
                usbProbeReason = usbProbeReason,
            )
        L.i(
            "TS18 source discovery candidates=${result.size} configured=${evidence.configuredSourceCount} " +
                "roots=${evidence.rootsWalked} visited=${evidence.visitedEntries} " +
                "elapsedMs=${evidence.elapsedMs} usbProbe=${evidence.usbProbeRequested} " +
                "reason=${evidence.usbProbeReason}"
        )
        return DiscoveryResult(result, evidence)
    }

    internal fun discoverAudioParents(
        root: File,
        out: LinkedHashSet<String>,
        rootGate: RootGate? = null,
        enforceSafeRoot: Boolean = true,
        deadline: Long = System.currentTimeMillis() + MAX_SCAN_ELAPSED_MS,
    ): Int {
        if (enforceSafeRoot && !isAllowedSourceCandidate(root.absolutePath)) return 0
        var visited = 0
        val queue = ArrayDeque<Pair<File, Int>>()
        queue.add(root to 0)
        while (queue.isNotEmpty()) {
            if (out.size >= MAX_CANDIDATES || visited >= MAX_VISITED_FILES) return visited
            if (System.currentTimeMillis() > deadline) return visited
            val (dir, depth) = queue.removeFirst()
            val children = listFilesSafe(dir, rootGate) ?: continue
            var containsAudio = false
            for (child in children) {
                visited++
                if (visited >= MAX_VISITED_FILES) break
                when {
                    child.isFile && child.file.extension.lowercase() in AUDIO_EXTENSIONS ->
                        containsAudio = true
                    child.isDirectory &&
                        depth < MAX_SCAN_DEPTH &&
                        shouldDescend(child.file, enforceSafeRoot) ->
                        queue.add(child.file to depth + 1)
                }
            }
            if (containsAudio && (!enforceSafeRoot || isAllowedSourceCandidate(dir.absolutePath))) {
                out.add(dir.absolutePath)
            }
        }
        return visited
    }

    private fun listFilesSafe(dir: File, rootGate: RootGate?): List<FileEntry>? {
        val direct =
            try {
                dir.listFiles()
            } catch (e: Exception) {
                L.w(e, "Cannot list music candidate directory ${dir.absolutePath}")
                null
            }
        if (direct != null) {
            return direct.map { FileEntry(it, isDirectory = it.isDirectory, isFile = it.isFile) }
        }
        return rootGate
            ?.runRootCommandSync(buildRootListCommand(dir.absolutePath), 1200)
            ?.mapNotNull { parseRootEntry(dir, it) }
    }

    fun canListRootBackedDirectory(path: String, rootGate: RootGate): Boolean =
        isAllowedSourceCandidate(path) && listFilesSafe(File(path), rootGate) != null

    private fun buildRootListCommand(directory: String): String {
        val escaped = directory.replace("'", "'\\''")
        return "for p in '$escaped'/* '$escaped'/.*; do " +
            "[ -e \"\$p\" ] || continue; " +
            "b=\${p##*/}; [ \"\$b\" = . ] && continue; [ \"\$b\" = .. ] && continue; " +
            "t=f; [ -d \"\$p\" ] && t=d; [ -L \"\$p\" ] && t=l; " +
            "m=\$(stat -c %Y \"\$p\" 2>/dev/null || echo 0); " +
            "s=\$(stat -c %s \"\$p\" 2>/dev/null || echo 0); " +
            "printf '%s\\t%s\\t%s\\t%s\\t%s\\n' \"\$t\" \"\$t\" \"\$m\" \"\$s\" \"\$b\"; " +
            "done"
    }

    internal fun parseRootEntry(parent: File, line: String): FileEntry? {
        val parts = line.split('\t')
        if (parts.size < 5) return null
        val type = parts[0]
        val name = parts[4]
        if (name == "." || name == ".." || name.contains('/')) return null
        val file = File(parent, name)
        return when (type) {
            "d" -> FileEntry(file, isDirectory = true, isFile = false)
            "f" -> FileEntry(file, isDirectory = false, isFile = true)
            else -> null
        }
    }

    private fun shouldDescend(dir: File, enforceSafeRoot: Boolean): Boolean {
        val name = dir.name
        if (name == "." || name == ".." || name.startsWith('.')) return false
        if (isNoisyDir(name)) return false
        val path = dir.absolutePath.replace('\\', '/')
        if (
            path.contains("/Android/", ignoreCase = true) ||
                path.endsWith("/Android", ignoreCase = true)
        ) {
            return false
        }
        return !enforceSafeRoot || isAllowedSourceCandidate(path)
    }

    internal fun isNoisyDir(name: String): Boolean =
        NOISY_DIRS.any { it.equals(name, ignoreCase = true) }

    private fun musicChildIfAccessible(root: String): String? =
        File(root, "Music").absolutePath.takeIf { isAccessibleCandidate(it) }

    internal fun normaliseCandidatePath(value: String): String? {
        val trimmed = value.trim()
        if (trimmed.isEmpty()) return null
        val path =
            when {
                trimmed.startsWith("file://") -> runCatching { URI(trimmed).path }.getOrNull()
                else -> trimmed
            } ?: return null
        val clean = path.replace('\\', '/').trimEnd('/')
        return if (MEDIA_RW_USB_SOURCE_REGEX.matches(clean)) {
            clean.replace("/mnt/media_rw/usbdisk", "/storage/usbdisk")
        } else {
            clean
        }
    }

    fun isAllowedSourceCandidate(path: String): Boolean {
        val clean = path.replace('\\', '/').trimEnd('/').ifEmpty { "/" }
        if (
            clean.isBlank() ||
                clean.contains("/../") ||
                clean.endsWith("/..") ||
                clean.contains("/./") ||
                clean.endsWith("/.")
        ) {
            return false
        }
        if (BLOCKED_SOURCE_PREFIXES.any { clean == it || (it != "/" && clean.startsWith("$it/")) }) {
            return false
        }
        val syntacticallyAllowed =
            clean == SDCARD_ROOT ||
                clean.startsWith("$SDCARD_ROOT/") ||
                clean == EMULATED_ROOT ||
                clean.startsWith("$EMULATED_ROOT/") ||
                USB_DISK_SOURCE_REGEX.matches(clean) ||
                MEDIA_RW_USB_SOURCE_REGEX.matches(clean) ||
                STORAGE_UUID_SOURCE_REGEX.matches(clean)
        if (!syntacticallyAllowed) return false
        val canonical =
            runCatching { File(clean).canonicalPath.replace('\\', '/').trimEnd('/') }.getOrNull()
        if (canonical != null && canonical != clean && canonical != "/") {
            return canonical == SDCARD_ROOT ||
                canonical.startsWith("$SDCARD_ROOT/") ||
                canonical == EMULATED_ROOT ||
                canonical.startsWith("$EMULATED_ROOT/") ||
                USB_DISK_SOURCE_REGEX.matches(canonical) ||
                MEDIA_RW_USB_SOURCE_REGEX.matches(canonical) ||
                STORAGE_UUID_SOURCE_REGEX.matches(canonical)
        }
        return true
    }

    internal fun isUsbCandidate(path: String): Boolean =
        USB_DISK_SOURCE_REGEX.matches(path) ||
            MEDIA_RW_USB_SOURCE_REGEX.matches(path) ||
            STORAGE_UUID_SOURCE_REGEX.matches(path)

    private fun preferAppFacingRoots(paths: Collection<String>): List<String> {
        val candidates = linkedSetOf<String>()
        for (path in paths) {
            normaliseCandidatePath(path)?.let(candidates::add)
        }
        return candidates.toList()
    }

    fun findFirstAccessibleCandidate(): String? = discoverCandidateRoots().firstOrNull()
}

/** Discovers removable USB storage volumes on TS18 devices for explicit source selection only. */
fun discoverUsbStorage(): List<String> {
    val storageRoot = File("/storage")
    if (!storageRoot.exists() || !storageRoot.isDirectory) return emptyList()
    return storageRoot
        .listFiles()
        ?.filter { it.isDirectory && it.name.startsWith("usbdisk", ignoreCase = true) }
        ?.map { it.absolutePath } ?: emptyList()
}
