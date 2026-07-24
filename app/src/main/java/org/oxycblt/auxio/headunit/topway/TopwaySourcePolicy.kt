/*
 * Copyright (c) 2026 Auxio Project
 * TopwaySourcePolicy.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
    private val PREPARED_USB_SOURCE_REGEX =
        Regex("^/storage/auxio-root/usbdisk\\d+(/.*)?$", RegexOption.IGNORE_CASE)
    private val STORAGE_UUID_SOURCE_REGEX = Regex("^/storage/[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}(/.*)?$")
    private val AUDIO_EXTENSIONS =
        setOf("mp3", "flac", "m4a", "mp4", "wav", "ogg", "opus", "aac", "3gp", "amr", "wma")

    private const val MAX_SCAN_DEPTH = 4
    private const val MAX_VISITED_FILES = 2500
    private const val MAX_CANDIDATES = 48
    private const val MAX_SCAN_ELAPSED_MS = 1200L
    private const val ROOT_DISCOVERY_SNAPSHOT_TIMEOUT_MS = 5_000L
    private const val NANOS_PER_MILLISECOND = 1_000_000L

    private fun monotonicNowMs(): Long = System.nanoTime() / NANOS_PER_MILLISECOND

    internal data class FileEntry(val file: File, val isDirectory: Boolean, val isFile: Boolean)

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
        discoverChildren(File(storageRoot, "auxio-root"), removableOnly = true).filterTo(out) {
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
    ): List<String> {
        val saved =
            savedPaths.mapNotNull(::normaliseCandidatePath).filter(::isAllowedSourceCandidate)
        val media =
            mediaStoreParents
                .mapNotNull(::normaliseCandidatePath)
                .filter(::isAllowedSourceCandidate)
        val injectedRoots =
            storageRoots.mapNotNull(::normaliseCandidatePath).filter(::isAllowedSourceCandidate)
        val fallbackRoots = SAFE_GENERIC_FALLBACKS.filter(::isAllowedSourceCandidate)
        val optionalRoots = media + injectedRoots + fallbackRoots
        val authorisedOptionalRoots =
            if (allowUnconfiguredUsb) {
                optionalRoots
            } else {
                optionalRoots.filter { candidate -> isContainedByAny(candidate, saved) }
            }
        val discoveredRoots = if (allowUnconfiguredUsb) discoverCandidateRoots() else emptyList()
        val candidates = saved + authorisedOptionalRoots + discoveredRoots
        val displayOptionalRoots = preferAppFacingRoots(authorisedOptionalRoots)

        // Background/configured-only access never walks or returns an unconfigured root. The
        // explicit source picker is the sole caller that opts into new removable suggestions.
        val roots = preferAppFacingRoots(candidates).filter(::isAllowedSourceCandidate)
        val audioParents = linkedSetOf<String>()
        val deadlineElapsedMs = monotonicNowMs() + MAX_SCAN_ELAPSED_MS
        for (root in roots) {
            if (audioParents.size >= MAX_CANDIDATES) break
            if (monotonicNowMs() > deadlineElapsedMs) break
            discoverAudioParents(
                File(root),
                audioParents,
                rootGate,
                deadlineElapsedMs = deadlineElapsedMs,
            )
        }
        val musicFolders =
            roots.mapNotNull {
                musicChildIfAccessible(it) ?: it.takeIf { path -> path.endsWith("/Music", true) }
            }
        val usb = roots.filter(::isUsbCandidate)
        val generic = roots.filterNot(::isUsbCandidate)
        val ordered = linkedSetOf<String>()
        listOf(saved, displayOptionalRoots, audioParents.toList(), musicFolders, usb, generic)
            .forEach { group -> group.filterTo(ordered, ::isAllowedSourceCandidate) }
        L.i(
            "Discovered ${ordered.size} TS18 music source candidates " +
                "(explicitUsb=$allowUnconfiguredUsb, configured=${saved.size}, injected=${injectedRoots.size})"
        )
        return ordered.take(MAX_CANDIDATES)
    }

    internal fun discoverAudioParents(
        root: File,
        out: LinkedHashSet<String>,
        rootGate: RootGate? = null,
        enforceSafeRoot: Boolean = true,
        deadlineElapsedMs: Long = monotonicNowMs() + MAX_SCAN_ELAPSED_MS,
    ) {
        if (enforceSafeRoot && !isAllowedSourceCandidate(root.absolutePath)) return
        val directRootReadable = runCatching { root.listFiles() }.getOrNull() != null
        if (!directRootReadable && rootGate != null) {
            val remaining = (MAX_CANDIDATES - out.size).coerceAtLeast(0)
            if (remaining == 0) return
            val snapshot =
                rootGate.snapshotTreeSync(
                    root.absolutePath,
                    MAX_SCAN_DEPTH,
                    ROOT_DISCOVERY_SNAPSHOT_TIMEOUT_MS,
                ) ?: return
            snapshot.entries
                .asSequence()
                .filter { !it.isDirectory && !it.isSymlink }
                .filter { entry ->
                    entry.relativePath.substringAfterLast('.', "").lowercase() in AUDIO_EXTENSIONS
                }
                .map { entry -> entry.relativePath.substringBeforeLast('/', "") }
                .distinct()
                .take(remaining)
                .mapTo(out) { relative ->
                    if (relative.isBlank()) root.absolutePath else File(root, relative).absolutePath
                }
            return
        }
        val canonicalCache = mutableMapOf<String, File?>()
        val canonicalRoot =
            if (enforceSafeRoot) {
                canonicalFile(root, canonicalCache) ?: return
            } else {
                null
            }
        var visited = 0
        val queue = ArrayDeque<Pair<File, Int>>()
        queue.add(root to 0)
        while (queue.isNotEmpty()) {
            if (out.size >= MAX_CANDIDATES || visited >= MAX_VISITED_FILES) return
            if (monotonicNowMs() > deadlineElapsedMs) return
            val (dir, depth) = queue.removeFirst()
            if (
                canonicalRoot != null && !isWithinCanonicalRoot(dir, canonicalRoot, canonicalCache)
            ) {
                continue
            }
            val children = listFilesSafe(dir) ?: continue
            var containsAudio = false
            for (child in children) {
                visited++
                if (visited >= MAX_VISITED_FILES) break
                val isAudioFile =
                    child.isFile && child.file.extension.lowercase() in AUDIO_EXTENSIONS
                val isDirectory = child.isDirectory && depth < MAX_SCAN_DEPTH
                if (!isAudioFile && !isDirectory) continue
                if (
                    canonicalRoot != null &&
                        !isWithinCanonicalRoot(child.file, canonicalRoot, canonicalCache)
                ) {
                    continue
                }
                when {
                    isAudioFile -> containsAudio = true
                    isDirectory &&
                        shouldDescend(child.file, enforceSafeRoot, canonicalRoot, canonicalCache) ->
                        queue.add(child.file to depth + 1)
                }
            }
            if (
                containsAudio &&
                    (!enforceSafeRoot ||
                        (isAllowedSourceCandidate(dir.absolutePath) &&
                            canonicalRoot != null &&
                            isWithinCanonicalRoot(dir, canonicalRoot, canonicalCache)))
            ) {
                out.add(dir.absolutePath)
            }
        }
    }

    private fun listFilesSafe(dir: File): List<FileEntry>? {
        val direct =
            try {
                dir.listFiles()
            } catch (e: Exception) {
                L.w(e, "Cannot list music candidate directory ${dir.absolutePath}")
                null
            }
        return direct?.map { FileEntry(it, isDirectory = it.isDirectory, isFile = it.isFile) }
    }

    fun canListRootBackedDirectory(path: String, rootGate: RootGate): Boolean =
        isAllowedSourceCandidate(path) &&
            rootGate.snapshotTreeSync(
                path,
                MAX_SCAN_DEPTH,
                ROOT_DISCOVERY_SNAPSHOT_TIMEOUT_MS,
            ) != null

    private fun shouldDescend(
        dir: File,
        enforceSafeRoot: Boolean,
        canonicalRoot: File?,
        canonicalCache: MutableMap<String, File?>,
    ): Boolean {
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
        if (!enforceSafeRoot) return true
        return canonicalRoot != null &&
            isAllowedSourceCandidate(path) &&
            isWithinCanonicalRoot(dir, canonicalRoot, canonicalCache)
    }

    internal fun isNoisyDir(name: String): Boolean =
        NOISY_DIRS.any { it.equals(name, ignoreCase = true) }

    private fun musicChildIfAccessible(root: String): String? =
        File(root, "Music").absolutePath.takeIf { isAccessibleCandidate(it) }

    internal fun isWithinCanonicalRoot(candidate: File, canonicalRoot: File): Boolean =
        isWithinCanonicalRoot(candidate, canonicalRoot, mutableMapOf())

    private fun isWithinCanonicalRoot(
        candidate: File,
        canonicalRoot: File,
        canonicalCache: MutableMap<String, File?>,
    ): Boolean {
        var cursor: File? = canonicalFile(candidate, canonicalCache) ?: return false
        while (cursor != null) {
            if (cursor == canonicalRoot) return true
            cursor = cursor.parentFile
        }
        return false
    }

    private fun canonicalFile(file: File, canonicalCache: MutableMap<String, File?>): File? {
        val key = file.absolutePath
        if (canonicalCache.containsKey(key)) return canonicalCache[key]
        val canonical = runCatching { file.canonicalFile }.getOrNull()
        canonicalCache[key] = canonical
        return canonical
    }

    private fun isContainedByAny(
        candidatePath: String,
        configuredRoots: Collection<String>,
    ): Boolean {
        val canonicalCache = mutableMapOf<String, File?>()
        val candidate = canonicalFile(File(candidatePath), canonicalCache) ?: return false
        return configuredRoots.any { configuredPath ->
            val root = canonicalFile(File(configuredPath), canonicalCache) ?: return@any false
            var cursor: File? = candidate
            while (cursor != null) {
                if (cursor == root) return@any true
                cursor = cursor.parentFile
            }
            false
        }
    }

    private fun normaliseCandidatePath(value: String): String? {
        val trimmed = value.trim()
        if (trimmed.isEmpty()) return null
        val path =
            when {
                trimmed.startsWith("file:", ignoreCase = true) ->
                    runCatching { URI(trimmed).path }.getOrNull()
                else -> trimmed
            } ?: return null
        return path.replace('\\', '/').trimEnd('/').ifEmpty { "/" }
    }

    fun isAllowedSourceCandidate(path: String): Boolean {
        val clean = path.replace('\\', '/').trimEnd('/').ifEmpty { "/" }
        if (
            clean.isBlank() ||
                clean == "." ||
                clean.startsWith("./") ||
                clean.startsWith("../") ||
                clean.contains("/../") ||
                clean.endsWith("/..") ||
                clean.contains("/./") ||
                clean.endsWith("/.")
        ) {
            return false
        }
        if (
            BLOCKED_SOURCE_PREFIXES.any { clean == it || (it != "/" && clean.startsWith("$it/")) }
        ) {
            return false
        }
        val syntacticallyAllowed =
            clean == SDCARD_ROOT ||
                clean.startsWith("$SDCARD_ROOT/") ||
                clean == EMULATED_ROOT ||
                clean.startsWith("$EMULATED_ROOT/") ||
                USB_DISK_SOURCE_REGEX.matches(clean) ||
                MEDIA_RW_USB_SOURCE_REGEX.matches(clean) ||
                PREPARED_USB_SOURCE_REGEX.matches(clean) ||
                STORAGE_UUID_SOURCE_REGEX.matches(clean)
        if (!syntacticallyAllowed) return false
        val canonical =
            runCatching { File(clean).canonicalPath.replace('\\', '/').trimEnd('/') }.getOrNull()
        if (canonical != null && canonical != clean) {
            return canonical == SDCARD_ROOT ||
                canonical.startsWith("$SDCARD_ROOT/") ||
                canonical == EMULATED_ROOT ||
                canonical.startsWith("$EMULATED_ROOT/") ||
                USB_DISK_SOURCE_REGEX.matches(canonical) ||
                MEDIA_RW_USB_SOURCE_REGEX.matches(canonical) ||
                PREPARED_USB_SOURCE_REGEX.matches(canonical) ||
                STORAGE_UUID_SOURCE_REGEX.matches(canonical)
        }
        return true
    }

    private fun isUsbCandidate(path: String): Boolean =
        USB_DISK_SOURCE_REGEX.matches(path) ||
            MEDIA_RW_USB_SOURCE_REGEX.matches(path) ||
            PREPARED_USB_SOURCE_REGEX.matches(path) ||
            STORAGE_UUID_SOURCE_REGEX.matches(path)

    private fun preferAppFacingRoots(paths: Collection<String>): List<String> {
        val candidates = linkedSetOf<String>()
        val raw = mutableListOf<String>()
        for (path in paths) {
            val clean = path.replace('\\', '/').trimEnd('/')
            if (MEDIA_RW_USB_SOURCE_REGEX.matches(clean)) {
                val appFacing = clean.replace("/mnt/media_rw/usbdisk", "/storage/usbdisk")
                if (isAllowedSourceCandidate(appFacing) && isAccessibleCandidate(appFacing)) {
                    candidates.add(appFacing)
                }
                raw.add(clean)
            } else {
                candidates.add(clean)
            }
        }
        raw.forEach(candidates::add)
        return candidates.toList()
    }

    fun findFirstAccessibleCandidate(): String? = discoverCandidateRoots().firstOrNull()
}

/** Discovers removable USB storage volumes on TS18 devices. */
fun discoverUsbStorage(): List<String> {
    val storageRoot = File("/storage")
    if (!storageRoot.exists() || !storageRoot.isDirectory) return emptyList()
    return storageRoot
        .listFiles()
        ?.filter { it.isDirectory && it.name.startsWith("usbdisk", ignoreCase = true) }
        ?.map { it.absolutePath } ?: emptyList()
}
