/*
 * Copyright (c) 2026 Auxio Project
 * FastStartDirectFolderBrowser.kt is part of Auxio.
 */
package org.oxycblt.auxio.headunit.ts18

import java.io.File
import java.nio.file.DirectoryStream
import java.nio.file.Files
import java.util.Locale
import kotlin.coroutines.coroutineContext
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext

/**
 * Bounded one-level DirectFS browser for the startup/Fast Start surface.
 *
 * Only app-facing `/storage/usbdisk0` and `/storage/usbdisk1` roots are accepted. The browser
 * canonicalises trusted roots and candidates, rejects traversal/symlink escapes, processes a hard
 * bounded number of visible entries, and returns reconstructed `/storage/...` paths even when the
 * kernel canonical path resolves through `/mnt/media_rw/...` on TS18 firmware.
 */
class FastStartDirectFolderBrowser
@Inject
constructor() {
    constructor(roots: Map<String, File>) : this() {
        configuredRoots = roots
    }

    data class Page(val entries: List<Entry>, val truncated: Boolean)

    data class Entry(
        val path: String,
        val name: String,
        val directory: Boolean,
        val playable: Boolean,
    )

    private var configuredRoots: Map<String, File> = DEFAULT_ROOTS

    suspend fun usbRoots(limit: Int = DEFAULT_LIMIT): Page =
        withContext(Dispatchers.IO) {
            val validRoots =
                configuredRoots.keys.mapNotNull { root ->
                    resolveCandidate(root)?.takeIf { it.file.isDirectory }
                }
            val boundedLimit = limit.coerceIn(1, MAX_LIMIT)
            val entries =
                validRoots.take(boundedLimit).map { candidate ->
                    Entry(candidate.appPath, candidate.file.name, true, false)
                }
            Page(entries, truncated = validRoots.size > boundedLimit)
        }

    suspend fun browse(path: String, limit: Int = DEFAULT_LIMIT): Page =
        withContext(Dispatchers.IO) {
            val candidate = resolveCandidate(path) ?: return@withContext Page(emptyList(), false)
            val dir = candidate.file
            if (!dir.isDirectory) return@withContext Page(emptyList(), false)
            val boundedLimit = limit.coerceIn(1, MAX_LIMIT)
            val selected = ArrayList<Entry>(boundedLimit)
            var processed = 0
            var validCount = 0
            var truncated = false
            try {
                Files.newDirectoryStream(dir.toPath()).use {
                    stream: DirectoryStream<java.nio.file.Path> ->
                    for (childPath in stream) {
                        coroutineContext.ensureActive()
                        if (processed++ >= MAX_PROCESSED_ENTRIES) {
                            truncated = true
                            break
                        }
                        val child = childPath.toFile()
                        if (child.name.startsWith('.')) continue
                        val childCandidate =
                            resolveCandidate(candidate.appPath + "/" + child.name) ?: continue
                        validCount++
                        offerTop(selected, childCandidate.toEntry(), boundedLimit)
                        if (validCount > boundedLimit) truncated = true
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (_: Exception) {
                return@withContext Page(selected.sortedWith(ENTRY_ORDER), truncated)
            }
            selected.sortWith(ENTRY_ORDER)
            Page(selected, truncated)
        }

    private fun offerTop(selected: MutableList<Entry>, entry: Entry, limit: Int) {
        selected.add(entry)
        selected.sortWith(ENTRY_ORDER)
        if (selected.size > limit) selected.removeAt(selected.lastIndex)
    }

    private fun ResolvedCandidate.toEntry(): Entry =
        Entry(appPath, file.name, file.isDirectory, file.isFile && isAudioLike(file.name))

    internal fun resolveCandidate(rawPath: String): ResolvedCandidate? {
        val normalized = normalize(rawPath) ?: return null
        val (rootPath, rootFile) =
            configuredRoots.entries.firstOrNull { (appRoot, _) ->
                normalized == appRoot || normalized.startsWith("$appRoot/")
            } ?: return null
        val relative = normalized.removePrefix(rootPath).removePrefix("/")
        val candidate = if (relative.isEmpty()) rootFile else File(rootFile, relative)
        return try {
            val rootCanonical = rootFile.canonicalFile
            val candidateCanonical = candidate.canonicalFile
            if (!candidateCanonical.isInside(rootCanonical)) return null
            val appPath = buildString {
                append(rootPath)
                val relativeCanonical = candidateCanonical.relativeToOrNull(rootCanonical)?.path
                if (!relativeCanonical.isNullOrEmpty()) {
                    append('/')
                    append(relativeCanonical.replace(File.separatorChar, '/'))
                }
            }
            ResolvedCandidate(appPath, candidateCanonical)
        } catch (_: Exception) {
            null
        }
    }

    private fun File.isInside(root: File): Boolean {
        var cursor: File? = this
        while (cursor != null) {
            if (cursor == root) return true
            cursor = cursor.parentFile
        }
        return false
    }

    private fun normalize(path: String): String? {
        val clean = path.trim().replace('\\', '/').replace(Regex("/+$"), "")
        if (clean.isEmpty()) return null
        if (clean.startsWith(MEDIA_RW_USB_PREFIX)) return null
        return clean
    }

    private fun isAudioLike(name: String): Boolean =
        AUDIO_EXTENSIONS.any { name.endsWith(it, ignoreCase = true) }

    internal data class ResolvedCandidate(val appPath: String, val file: File)

    companion object {
        const val USB0 = "/storage/usbdisk0"
        const val USB1 = "/storage/usbdisk1"
        private const val MEDIA_RW_USB_PREFIX = "/mnt/media_rw/usbdisk"
        private const val DEFAULT_LIMIT = 30
        private const val MAX_LIMIT = 100
        private const val MAX_PROCESSED_ENTRIES = 512
        private val DEFAULT_ROOTS = mapOf(USB0 to File(USB0), USB1 to File(USB1))
        private val AUDIO_EXTENSIONS =
            listOf(".mp3", ".flac", ".m4a", ".ogg", ".opus", ".wav", ".aac")
        private val ENTRY_ORDER =
            compareBy<Entry>(
                { !it.directory },
                { it.name.lowercase(Locale.ROOT) },
                { it.path },
            )
    }
}
