/*
 * Copyright (c) 2026 Auxio Project
 * StoragePathAliasPolicy.kt is part of Auxio.
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

import java.io.File

/** Deduplicates paths that represent the same physical location (aliases). */
object StoragePathAliasPolicy {

    /** Identifies a group of aliases pointing to the same file or directory. */
    data class AliasGroup(val canonicalPath: String, val paths: List<String>)

    /** Deduplicates a list of file paths. Returns a list of paths where duplicates are removed. */
    fun deduplicatePaths(paths: List<String>): List<String> {
        return groupAliases(paths).map { it.paths.first() }
    }

    /** Groups paths into alias groups. */
    fun groupAliases(paths: List<String>): List<AliasGroup> {
        val groups = mutableMapOf<String, MutableList<String>>()

        for (path in paths) {
            groups.getOrPut(canonicalize(path)) { mutableListOf() }.add(path)
        }

        return groups.map { AliasGroup(it.key, it.value) }
    }

    /** Deduplicates files using their canonical path and size as identity. */
    fun <T> deduplicateFiles(
        files: List<T>,
        pathSelector: (T) -> String,
        sizeSelector: (T) -> Long,
    ): List<T> {
        val deduped = mutableListOf<T>()
        val seen = mutableSetOf<String>()

        for (file in files) {
            // Different mount aliases can report different modified times for the same physical
            // file, so identity is based on the canonical path and size only.
            val identity = "${canonicalize(pathSelector(file))}_${sizeSelector(file)}"
            if (seen.add(identity)) {
                deduped.add(file)
            }
        }

        return deduped
    }

    /**
     * Resolves a path to a canonical form, first collapsing known TS18 mount aliases so that the
     * same physical file reached via different mount points produces the same identity.
     */
    fun canonicalize(path: String): String {
        val normalized = normalize(path)
        return try {
            File(normalized).canonicalPath
        } catch (e: Exception) {
            normalized
        }
    }

    /**
     * Collapses known TS18 mount aliases to a single canonical-looking string without performing
     * any disk I/O. Suitable for hot paths such as the MediaStore scan loop.
     */
    fun normalize(path: String): String {
        // canonicalPath does not throw for missing or unmounted paths, so alias collapsing must
        // happen explicitly rather than only in a failure fallback.
        return when {
            // Any USB port index (usbdisk0, usbdisk1, ...) is exposed under both mount roots.
            path.startsWith("/mnt/media_rw/usbdisk") ->
                "/storage/usbdisk" + path.removePrefix("/mnt/media_rw/usbdisk")
            path == "/sdcard" || path.startsWith("/sdcard/") ->
                "/storage/emulated/0" + path.removePrefix("/sdcard")
            else -> path
        }
    }
}
