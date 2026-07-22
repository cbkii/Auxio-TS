/*
 * Copyright (c) 2026 Auxio Project
 * ConfiguredSourcePolicy.kt is part of Auxio.
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
import javax.inject.Inject
import javax.inject.Singleton
import org.oxycblt.auxio.music.locations.LocationMode

/** Immutable snapshot and policy for configured music sources. */
@Singleton
class ConfiguredSourcePolicy @Inject constructor(private val settings: MusicSettings) {

    val locationMode: LocationMode
        get() = settings.locationMode

    val isUsbConfigured: Boolean
        get() {
            val query = settings.safQuery
            return query.source.any { loc ->
                loc.uri.toString().contains("usbdisk") || loc.uri.toString().contains("media_rw")
            }
        }

    val allowUnconfiguredUsbDiscovery: Boolean
        get() = false

    fun getConfiguredRootsAsFiles(): Map<String, File> {
        val configuredUris =
            if (locationMode == LocationMode.DIRECT_FS) {
                settings.safQuery.source.map { it.uri.toString() }
            } else {
                // For SAF we can't easily map to raw File paths generically, but DirectFS will give
                // us the direct paths.
                settings.safQuery.source.mapNotNull {
                    if (it.uri.toString().startsWith("/")) it.uri.toString() else null
                }
            }

        return configuredUris.associate { path -> path to File(path.toString()) }
    }
}
