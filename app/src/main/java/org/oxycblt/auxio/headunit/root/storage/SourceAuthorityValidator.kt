/*
 * Copyright (c) 2026 Auxio Project
 * SourceAuthorityValidator.kt is part of Auxio.
 */
package org.oxycblt.auxio.headunit.root.storage

import java.io.File
import java.io.FileInputStream
import java.util.ArrayDeque

/** End-to-end app-UID validation for a DirectFS source or Magisk-prepared alias. */
object SourceAuthorityValidator {
    private val audioExtensions =
        setOf("mp3", "flac", "m4a", "mp4", "wav", "ogg", "opus", "aac", "3gp", "amr", "wma")
    private const val MAX_DEPTH = 4
    private const val MAX_VISITED = 512

    fun classifyDirect(path: String, preparedAlias: Boolean): SourceAuthority? {
        val root = File(path)
        val canonicalRoot = runCatching { root.canonicalFile }.getOrNull() ?: return null
        val first = runCatching { root.listFiles() }.getOrNull() ?: return null
        if (!root.exists() || !root.isDirectory || !root.canRead()) return null

        val queue = ArrayDeque<Pair<File, Int>>()
        first.forEach { queue.add(it to 1) }
        var visited = 0
        var representative: File? = null
        while (queue.isNotEmpty() && visited < MAX_VISITED && representative == null) {
            val (candidate, depth) = queue.removeFirst()
            visited++
            val canonical = runCatching { candidate.canonicalFile }.getOrNull() ?: continue
            if (!isWithin(canonical, canonicalRoot)) continue
            if (candidate.isFile && candidate.extension.lowercase() in audioExtensions) {
                representative = candidate
            } else if (candidate.isDirectory && depth < MAX_DEPTH) {
                runCatching { candidate.listFiles() }
                    .getOrNull()
                    ?.forEach { queue.add(it to depth + 1) }
            }
        }

        val mediaFile = representative ?: return null
        val opened =
            runCatching {
                    FileInputStream(mediaFile).use { stream ->
                        stream.read()
                        true
                    }
                }
                .getOrDefault(false)
        if (!opened) return null
        return if (preparedAlias) SourceAuthority.PREPARED_ALIAS else SourceAuthority.APP_READABLE
    }

    private fun isWithin(candidate: File, root: File): Boolean {
        var cursor: File? = candidate
        while (cursor != null) {
            if (cursor == root) return true
            cursor = cursor.parentFile
        }
        return false
    }
}
