/*
 * Copyright (c) 2026 Auxio Project
 * RemovableStorageEventPolicy.kt is part of Auxio.
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

package org.oxycblt.auxio.music.service

import org.oxycblt.auxio.music.ConfiguredSourceSpec
import org.oxycblt.auxio.music.ObservationMode
import org.oxycblt.auxio.music.locations.LocationMode

internal object RemovableStorageEventPolicy {
    val settleDelaysMs = longArrayOf(500L, 1_500L, 3_000L)

    /**
         * Determines whether mounted-storage events may trigger an automatic refresh.
         *
         * @return `true` if automatic mounted-storage refresh is permitted, `false` for manual observation mode.
         */
    fun allowsAutomaticMountedRefresh(observationMode: ObservationMode): Boolean =
        observationMode != ObservationMode.MANUAL

    /**
     * Finds direct-filesystem source keys whose configured paths overlap the mounted path.
     *
     * @param mountedPath The absolute mounted path to match.
     * @param specs The configured source specifications to search.
     * @return Matching source keys in insertion order, without duplicates; an empty set if the path is absent or invalid.
     */
    fun matchingSourceKeys(mountedPath: String?, specs: List<ConfiguredSourceSpec>): Set<String> {
        val root = mountedPath?.trimEnd('/')?.takeIf { it.startsWith("/") } ?: return emptySet()
        return specs
            .asSequence()
            .filter { it.mode == LocationMode.DIRECT_FS }
            .filter {
                val configured = it.normalizedUri.path?.trimEnd('/').orEmpty()
                configured == root ||
                    configured.startsWith("$root/") ||
                    root.startsWith("$configured/")
            }
            .mapTo(linkedSetOf()) { it.sourceKey }
    }
}
