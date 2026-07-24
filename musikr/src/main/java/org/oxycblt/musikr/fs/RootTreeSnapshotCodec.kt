/*
 * Copyright (c) 2026 Auxio Project
 * RootTreeSnapshotCodec.kt is part of Auxio.
 */
package org.oxycblt.musikr.fs

/** Strict parser for the fixed TSV emitted by the TS18 root storage snapshot command. */
object RootTreeSnapshotCodec {
    private const val DEFAULT_MAX_ENTRIES = 100_000

    fun parse(
        rootPath: String,
        text: String,
        maxEntries: Int = DEFAULT_MAX_ENTRIES,
    ): RootTreeSnapshot? {
        if (rootPath.isBlank() || maxEntries <= 0) return null
        val entries = ArrayList<RootTreeEntry>()
        for (line in text.lineSequence()) {
            if (line.isBlank()) continue
            if (entries.size >= maxEntries) return null
            val parts = line.split('\t', limit = 4)
            if (parts.size != 4) return null
            val type = parts[0]
            val relative = normaliseRelative(parts[3]) ?: return null
            val modifiedSeconds = parts[1].toLongOrNull() ?: return null
            val size = parts[2].toLongOrNull() ?: return null
            if (modifiedSeconds < 0L || size < 0L) return null
            entries +=
                RootTreeEntry(
                    relativePath = relative,
                    isDirectory = type == "d",
                    isSymlink = type == "l",
                    modifiedMs = modifiedSeconds * 1000L,
                    size = size,
                )
            if (type != "d" && type != "f" && type != "l") return null
        }
        return RootTreeSnapshot(rootPath = rootPath, entries = entries)
    }

    private fun normaliseRelative(value: String): String? {
        val clean = value.replace('\\', '/').trim('/')
        if (clean.isBlank() || clean.contains('\n') || clean.contains('\r') || clean.contains('\t')) {
            return null
        }
        val segments = clean.split('/')
        if (segments.any { it.isBlank() || it == "." || it == ".." }) return null
        return segments.joinToString("/")
    }
}
