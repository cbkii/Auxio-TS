/*
 * Copyright (c) 2026 Auxio Project
 * DrivingStartupPolicy.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.oxycblt.auxio.music

import org.oxycblt.musikr.library.ArtworkPolicy
import org.oxycblt.musikr.library.LibraryDimensionPolicy
import org.oxycblt.musikr.library.MetadataProfile

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
            playbackActive ||
                isTopwayVariant ||
                scanPriority == ScanPriority.PLAYBACK_FIRST
        ) {
            MetadataProfile.LEAN
        } else {
            MetadataProfile.FULL
        }
    }

    fun artworkPolicy(profile: MetadataProfile): ArtworkPolicy =
        if (profile == MetadataProfile.LEAN) ArtworkPolicy.VISIBLE_ONLY
        else ArtworkPolicy.FULL_INDEXING

    fun dimensions(profile: MetadataProfile): LibraryDimensionPolicy =
        if (profile == MetadataProfile.LEAN) {
            LibraryDimensionPolicy(
                songIdentity = true,
                basicTags = true,
                albums = true,
                artists = true,
                genres = false,
                musicBrainz = false,
                replayGain = false,
                releaseTypes = false,
            )
        } else {
            LibraryDimensionPolicy(
                songIdentity = true,
                basicTags = true,
                albums = true,
                artists = true,
                genres = true,
                musicBrainz = true,
                replayGain = true,
                releaseTypes = true,
            )
        }

    fun shouldDeferFullEnrichment(playbackActive: Boolean): Boolean = playbackActive
}
