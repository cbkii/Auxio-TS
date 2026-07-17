/*
 * Copyright (c) 2026 Auxio Project
 * MetadataWorkPolicy.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.oxycblt.musikr.library

/**
 * Executable work gates for [MetadataProfile].
 *
 * Lean extraction is deliberately sufficient for identity, immediate playback, deterministic
 * sorting and a useful first row. Rich relationships, gain and artwork are separate restart-safe
 * enrichment work.
 */
data class MetadataWorkPolicy(
    val useLeanPlatformExtractor: Boolean,
    val readMusicBrainz: Boolean,
    val readReplayGain: Boolean,
    val readGenres: Boolean,
    val readReleaseTypes: Boolean,
    val readDetailedDates: Boolean,
    val expandMultipleArtists: Boolean,
    val extractArtwork: Boolean,
) {
    companion object {
        fun forProfile(profile: MetadataProfile): MetadataWorkPolicy =
            when (profile) {
                MetadataProfile.LEAN ->
                    MetadataWorkPolicy(
                        useLeanPlatformExtractor = true,
                        readMusicBrainz = false,
                        readReplayGain = false,
                        readGenres = false,
                        readReleaseTypes = false,
                        readDetailedDates = false,
                        expandMultipleArtists = false,
                        extractArtwork = false,
                    )
                MetadataProfile.FULL ->
                    MetadataWorkPolicy(
                        useLeanPlatformExtractor = false,
                        readMusicBrainz = true,
                        readReplayGain = true,
                        readGenres = true,
                        readReleaseTypes = true,
                        readDetailedDates = true,
                        expandMultipleArtists = true,
                        extractArtwork = true,
                    )
            }
    }
}
