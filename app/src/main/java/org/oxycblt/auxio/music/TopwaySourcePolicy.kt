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

package org.oxycblt.auxio.music

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

    /** Checks if a given path is a valid and accessible candidate for music storage on TS18. */
    fun isAccessibleCandidate(path: String): Boolean {
        val file = File(path)
        return file.exists() && file.isDirectory && file.canRead()
    }

    /** Returns the first accessible candidate root from the priority list. */
    fun findFirstAccessibleCandidate(): String? {
        return CANDIDATE_ROOTS.firstOrNull { isAccessibleCandidate(it) }
    }
}
