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
import java.util.ArrayDeque
import kotlin.system.measureTimeMillis
import timber.log.Timber as L

/** Policy for identifying and prioritizing TS18-specific candidate music source roots. */
object TopwaySourcePolicy {

    /**
     * Primary observed USB storage path on captured Topway TS18 evidence; not a discovery limit.
     */
    const val USB_DISK_0 = "/storage/usbdisk0"

    /** Observed primary shared music directory on TS18. */
    const val EMULATED_MUSIC = "/storage/emulated/0/Music"

    /** Legacy/alias path for emulated music. */
    const val SDCARD_MUSIC = "/sdcard/Music"

    /**
     * Safe generic shared-storage fallbacks. These are preferred over device-specific paths when
     * SAF picker is unavailable.
     */
    val SAFE_GENERIC_FALLBACKS = listOf(EMULATED_MUSIC, SDCARD_MUSIC)

    /**
     * Static example USB candidate from captured TS18 evidence. Runtime code must use
     * [discoverCandidateRoots] so `/storage/usbdisk1`, `/storage/usbdisk2`, and
     * `/mnt/media_rw/usbdiskN` mounts are not missed.
     */
    val TS18_USB_EXAMPLE_CANDIDATES = listOf(USB_DISK_0)

    /** Backwards-compatible alias for docs/tests; example seeds only, not a complete allowlist. */
    @Deprecated("Use dynamic discoverCandidateRoots(); this is an observed example seed only")
    val TS18_USB_CANDIDATES = TS18_USB_EXAMPLE_CANDIDATES

    /** Static example candidate roots. Runtime selection should call [discoverCandidateRoots]. */
    val CANDIDATE_ROOTS = SAFE_GENERIC_FALLBACKS + TS18_USB_EXAMPLE_CANDIDATES

    /** Directories that are known to be noisy or irrelevant on TS18 and should be skipped. */
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

    /**
     * Keywords for default System source path filtering on TS18. When enabled, only MediaStore
     * results whose full path contains one of these keywords (case-insensitive) will be included.
     * This prevents scanning huge irrelevant USB root directories on TS18.
     */
    val SYSTEM_SOURCE_PATH_KEYWORDS = listOf("music", "download", "media")

    /**
     * Checks if a path matches the default TS18 system source filter. Returns true if the full path
     * contains at least one keyword (case-insensitive).
     */
    fun matchesSystemSourceFilter(fullPath: String): Boolean {
        val lower = fullPath.lowercase()
        return SYSTEM_SOURCE_PATH_KEYWORDS.any { lower.contains(it) }
    }

    /** Checks if a given path is a valid and accessible candidate for music storage on TS18. */
    fun isAccessibleCandidate(path: String): Boolean {
        return try {
            val file = File(path)
            file.exists() && file.isDirectory && file.canRead()
        } catch (e: SecurityException) {
            false
        } catch (e: RuntimeException) {
            false
        }
    }

    /**
     * Discovers currently existing, readable source roots without recursively traversing them.
     * Preserves each returned path exactly as discovered so UI selection can persist it unchanged.
     */
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
        return out.toList()
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

    /** Audio extensions that Auxio/Musikr can index from direct filesystem sources. */
    private val AUDIO_EXTENSIONS =
        setOf("mp3", "flac", "m4a", "mp4", "wav", "ogg", "opus", "aac", "3gp", "amr", "wma")

    /** Safe scan bounds for TS18 modal candidate discovery. */
    private const val MAX_SCAN_DEPTH = 4
    private const val MAX_VISITED_FILES = 2500
    private const val MAX_CANDIDATES = 48
    private const val MAX_SCAN_ELAPSED_MS = 1200L

    /** Returns directories that actually contain audio files, followed by safe fallback roots. */
    fun discoverMusicSourceCandidates(savedPaths: Collection<String> = emptyList()): List<String> {
        val out = linkedSetOf<String>()
        savedPaths.mapNotNull(::normaliseCandidatePath).filterTo(out, ::isAllowedSourceCandidate)

        val roots = discoverCandidateRoots()
        roots.map { musicChildIfAccessible(it) ?: it }.filterTo(out, ::isAllowedSourceCandidate)

        val elapsed = measureTimeMillis {
            for (root in roots) {
                if (out.size >= MAX_CANDIDATES) break
                discoverAudioParents(File(root), out)
            }
        }
        L.i("Discovered ${out.size} TS18 music source candidates in ${elapsed}ms")
        return out.sortedWith(candidateComparator()).take(MAX_CANDIDATES)
    }

    private fun discoverAudioParents(root: File, out: LinkedHashSet<String>) {
        if (!isAllowedSourceCandidate(root.absolutePath)) return
        val started = System.currentTimeMillis()
        var visited = 0
        val queue = ArrayDeque<Pair<File, Int>>()
        queue.add(root to 0)
        while (!queue.isEmpty()) {
            if (out.size >= MAX_CANDIDATES || visited >= MAX_VISITED_FILES) return
            if (System.currentTimeMillis() - started > MAX_SCAN_ELAPSED_MS) return
            val (dir, depth) = queue.removeFirst()
            val children =
                try {
                    dir.listFiles()
                } catch (e: Exception) {
                    L.w(e, "Cannot list music candidate directory ${dir.absolutePath}")
                    null
                } ?: continue
            var containsAudio = false
            for (child in children) {
                visited++
                if (visited >= MAX_VISITED_FILES) break
                if (child.isFile && child.extension.lowercase() in AUDIO_EXTENSIONS) {
                    containsAudio = true
                } else if (child.isDirectory && depth < MAX_SCAN_DEPTH && shouldDescend(child)) {
                    queue.add(child to depth + 1)
                }
            }
            if (containsAudio && isAllowedSourceCandidate(dir.absolutePath)) {
                out.add(dir.absolutePath)
            }
        }
    }

    private fun shouldDescend(dir: File): Boolean {
        val name = dir.name
        if (name == "." || name == ".." || name.startsWith('.')) return false
        if (name in NOISY_DIRS) return false
        val path = dir.absolutePath.replace('\\', '/')
        if ("/Android/" in path || path.endsWith("/Android")) return false
        return isAllowedSourceCandidate(path)
    }

    private fun musicChildIfAccessible(root: String): String? {
        val child = File(root, "Music")
        return child.absolutePath.takeIf { isAccessibleCandidate(it) }
    }

    private fun normaliseCandidatePath(value: String): String? {
        val trimmed = value.trim()
        if (trimmed.isEmpty()) return null
        return when {
            trimmed.startsWith("file://") -> runCatching { java.net.URI(trimmed).path }.getOrNull()
            else -> trimmed
        }
    }

    fun isAllowedSourceCandidate(path: String): Boolean {
        val clean = path.replace('\\', '/').trimEnd('/')
        if (
            clean.isBlank() ||
                clean.contains("/../") ||
                clean.endsWith("/..") ||
                clean.contains("/./") ||
                clean.endsWith("/.")
        ) {
            return false
        }
        val blocked = listOf("/", "/system", "/vendor", "/data", "/proc", "/sys", "/dev")
        if (blocked.any { clean == it || clean.startsWith("$it/") }) return false
        return clean == "/sdcard" ||
            clean.startsWith("/sdcard/") ||
            clean == "/storage/emulated/0" ||
            clean.startsWith("/storage/emulated/0/") ||
            Regex("^/storage/usbdisk\\d+(/.*)?$", RegexOption.IGNORE_CASE).matches(clean) ||
            Regex("^/mnt/media_rw/usbdisk\\d+(/.*)?$", RegexOption.IGNORE_CASE).matches(clean)
    }

    private fun candidateComparator(): Comparator<String> =
        compareBy<String> { !it.endsWith("/Music", ignoreCase = true) }
            .thenBy { !it.contains("/Music/", ignoreCase = true) }
            .thenBy {
                !(it.startsWith("/storage/usbdisk") || it.startsWith("/mnt/media_rw/usbdisk"))
            }
            .thenBy { it.length }
            .thenBy { it }

    /** Returns the first accessible candidate root from dynamic discovery. */
    fun findFirstAccessibleCandidate(): String? {
        return discoverCandidateRoots().firstOrNull()
    }
}

/** Discovers removable USB storage volumes on TS18 devices. */
fun discoverUsbStorage(): List<String> {
    val storageRoot = java.io.File("/storage")
    if (!storageRoot.exists() || !storageRoot.isDirectory) return emptyList()
    return storageRoot
        .listFiles()
        ?.filter { it.isDirectory && it.name.startsWith("usbdisk", ignoreCase = true) }
        ?.map { it.absolutePath } ?: emptyList()
}
