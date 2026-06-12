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

/**
 * Deduplicates paths that represent the same physical location (aliases).
 */
object StoragePathAliasPolicy {

    /**
     * Identifies a group of aliases pointing to the same file or directory.
     */
    data class AliasGroup(
        val canonicalPath: String,
        val paths: List<String>
    )

    /**
     * Deduplicates a list of file paths.
     * Returns a list of paths where duplicates are removed.
     */
    fun deduplicatePaths(paths: List<String>): List<String> {
        return groupAliases(paths).map { it.paths.first() }
    }

    /**
     * Groups paths into alias groups.
     */
    fun groupAliases(paths: List<String>): List<AliasGroup> {
        val groups = mutableMapOf<String, MutableList<String>>()

        for (path in paths) {
            val canonical = try {
                File(path).canonicalPath
            } catch (e: Exception) {
                // Fallback to absolute path or normalized TS18 path
                normalizePath(path)
            }
            groups.getOrPut(canonical) { mutableListOf() }.add(path)
        }

        return groups.map { AliasGroup(it.key, it.value) }
    }

    /**
     * Deduplicates files based on metadata (size, modified time, canonical path).
     */
    fun <T> deduplicateFiles(
        files: List<T>,
        pathSelector: (T) -> String,
        sizeSelector: (T) -> Long,
        modifiedMsSelector: (T) -> Long
    ): List<T> {
        // Group by size and modified time as a quick filter, then by canonical path
        val deduped = mutableListOf<T>()
        val seen = mutableSetOf<String>()

        for (file in files) {
            val path = pathSelector(file)
            val size = sizeSelector(file)
            val modified = modifiedMsSelector(file)
            
            val canonical = try {
                File(path).canonicalPath
            } catch (e: Exception) {
                normalizePath(path)
            }
            
            // To be safe, we use canonical path + size to uniquely identify files.
            // Some file systems might report different modified times depending on access route,
            // so we just use canonicalPath + size for identity.
            val identity = "${canonical}_${size}"
            
            if (seen.add(identity)) {
                deduped.add(file)
            }
        }
        
        return deduped
    }

    private fun normalizePath(path: String): String {
        // Try to resolve known TS18 aliases if canonical path fails
        var normalized = path
        if (normalized.startsWith("/mnt/media_rw/usbdisk0")) {
            normalized = normalized.replaceFirst("/mnt/media_rw/usbdisk0", "/storage/usbdisk0")
        } else if (normalized.startsWith("/sdcard")) {
            normalized = normalized.replaceFirst("/sdcard", "/storage/emulated/0")
        }
        return normalized
    }
}
