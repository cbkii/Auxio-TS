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

/** Policy for identifying and prioritizing TS18-specific candidate music source roots. */
object TopwaySourcePolicy {

    /** Primary observed USB storage path on Topway TS18. */
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
     * TS18-specific removable USB candidate. Should only be used on Topway/TS18 builds and after
     * explicit validation.
     */
    val TS18_USB_CANDIDATES = listOf(USB_DISK_0)

    /** List of all candidate roots. */
    val CANDIDATE_ROOTS = SAFE_GENERIC_FALLBACKS + TS18_USB_CANDIDATES

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
    fun discoverCandidateRoots(): List<String> {
        val out = linkedSetOf<String>()
        SAFE_GENERIC_FALLBACKS.filterTo(out) { isAccessibleCandidate(it) }
        discoverChildren(File("/storage"), removableOnly = false).filterTo(out) { isAccessibleCandidate(it) }
        discoverChildren(File("/mnt/media_rw"), removableOnly = true).filterTo(out) { isAccessibleCandidate(it) }
        return out.toList()
    }

    private fun discoverChildren(root: File, removableOnly: Boolean): List<String> {
        val children = try { root.listFiles() } catch (e: Exception) { null } ?: return emptyList()
        return children.asSequence()
            .filter { it.isDirectory }
            .filter { !removableOnly || it.name.startsWith("usbdisk", ignoreCase = true) }
            .filter { it.name != "self" && it.name != "emulated" }
            .map { it.absolutePath }
            .sortedWith(compareBy<String> { !it.substringAfterLast('/').startsWith("usbdisk", true) }.thenBy { it })
            .toList()
    }

    /** Returns the first accessible candidate root from dynamic discovery. */
    fun findFirstAccessibleCandidate(): String? {
        return discoverCandidateRoots().firstOrNull()
    }
}
