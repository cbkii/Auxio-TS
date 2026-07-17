/*
 * Copyright (c) 2026 Auxio Project
 * DrivingStartupPolicy.kt is part of Auxio.
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

import org.oxycblt.musikr.library.ArtworkPolicy
import org.oxycblt.musikr.library.LibraryDimensionPolicy
import org.oxycblt.musikr.library.MetadataProfile
import org.oxycblt.musikr.library.defaultArtworkPolicy
import org.oxycblt.musikr.library.defaultDimensionPolicy

/** Understandable, user-controlled resource policy for the first driving minute. */
object DrivingStartupPolicy {
    fun metadataProfile(
        explicit: MetadataProfile?,
        scanPriority: ScanPriority,
        playbackActive: Boolean,
        isTopwayVariant: Boolean,
    ): MetadataProfile {
        if (explicit != null) return explicit
        return if (
            playbackActive || isTopwayVariant || scanPriority == ScanPriority.PLAYBACK_FIRST
        ) {
            MetadataProfile.LEAN
        } else {
            MetadataProfile.FULL
        }
    }

    fun artworkPolicy(profile: MetadataProfile): ArtworkPolicy = profile.defaultArtworkPolicy()

    fun dimensions(profile: MetadataProfile): LibraryDimensionPolicy =
        profile.defaultDimensionPolicy()

    fun shouldDeferFullEnrichment(playbackActive: Boolean): Boolean = playbackActive
}
