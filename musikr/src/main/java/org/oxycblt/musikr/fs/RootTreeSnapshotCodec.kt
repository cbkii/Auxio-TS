/*
 * Copyright (c) 2026 Auxio Project
 * RootTreeSnapshotCodec.kt is part of Auxio.
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
            if (type != "d" && type != "f" && type != "l") return null
            val relative = validateRelative(parts[3]) ?: return null
            val modifiedSeconds = parts[1].toLongOrNull() ?: return null
            val size = parts[2].toLongOrNull() ?: return null
            if (modifiedSeconds < 0L || modifiedSeconds > Long.MAX_VALUE / 1000L || size < 0L) {
                return null
            }
            entries +=
                RootTreeEntry(
                    relativePath = relative,
                    isDirectory = type == "d",
                    isSymlink = type == "l",
                    modifiedMs = modifiedSeconds * 1000L,
                    size = size,
                )
        }
        return RootTreeSnapshot(rootPath = rootPath, entries = entries)
    }

    private fun validateRelative(value: String): String? {
        if (
            value.isEmpty() ||
                value.startsWith('/') ||
                value.endsWith('/') ||
                value.any { it.isISOControl() }
        ) {
            return null
        }
        val segments = value.split('/')
        if (segments.any { it.isEmpty() || it == "." || it == ".." }) return null
        return value
    }
}
