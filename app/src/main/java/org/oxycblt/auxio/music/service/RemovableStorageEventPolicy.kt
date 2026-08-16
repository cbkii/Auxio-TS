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

    /** Manual library mode treats a remount as availability only, never scan authority. */
    fun allowsAutomaticMountedRefresh(observationMode: ObservationMode): Boolean =
        observationMode != ObservationMode.MANUAL

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
