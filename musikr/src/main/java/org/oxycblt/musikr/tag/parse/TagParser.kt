/*
 * Copyright (c) 2024 Auxio Project
 * TagParser.kt is part of Auxio.
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

package org.oxycblt.musikr.tag.parse

import org.oxycblt.musikr.library.LibraryDimensionPolicy
import org.oxycblt.musikr.library.MetadataProfile
import org.oxycblt.musikr.library.MetadataWorkPolicy
import org.oxycblt.musikr.metadata.Metadata

internal interface TagParser {
    fun parse(metadata: Metadata): ParsedTags

    companion object {
        fun new(
            profile: MetadataProfile = MetadataProfile.FULL,
            dimensions: LibraryDimensionPolicy =
                LibraryDimensionPolicy(
                    genres = true,
                    playlists = true,
                    detailedCollaborators = true,
                    albumArtists = true,
                    releaseTypes = true,
                    advancedDates = true,
                    replayGain = true,
                    musicBrainz = true,
                ),
        ): TagParser = ProfiledTagParser(MetadataWorkPolicy.forProfile(profile), dimensions)
    }
}

private class ProfiledTagParser(
    private val work: MetadataWorkPolicy,
    private val dimensions: LibraryDimensionPolicy,
) : TagParser {
    override fun parse(metadata: Metadata): ParsedTags {
        val compilation = work.readReleaseTypes && metadata.isCompilation()
        val rawArtistNames = metadata.artistNames() ?: metadata.composerNames() ?: emptyList()
        val rawArtistSortNames =
            metadata.artistSortNames() ?: metadata.composerSortNames() ?: emptyList()
        val rawAlbumArtistNames = metadata.albumArtistNames().orEmpty()
        val artistNames =
            if (work.expandMultipleArtists && dimensions.detailedCollaborators) {
                rawArtistNames
            } else {
                rawArtistNames.take(1)
            }
        val artistSortNames =
            if (work.expandMultipleArtists && dimensions.detailedCollaborators) {
                rawArtistSortNames
            } else {
                rawArtistSortNames.take(1)
            }
        val albumArtistNames =
            if (dimensions.albumArtists) {
                if (work.expandMultipleArtists) rawAlbumArtistNames else rawAlbumArtistNames.take(1)
            } else {
                emptyList()
            }

        return ParsedTags(
            durationMs = metadata.properties.durationMs,
            replayGainTrackAdjustment =
                metadata.replayGainTrackAdjustment().takeIf {
                    work.readReplayGain && dimensions.replayGain
                },
            replayGainAlbumAdjustment =
                metadata.replayGainAlbumAdjustment().takeIf {
                    work.readReplayGain && dimensions.replayGain
                },
            musicBrainzId =
                metadata.musicBrainzId().takeIf { work.readMusicBrainz && dimensions.musicBrainz },
            name = metadata.name(),
            sortName = metadata.sortName(),
            track = metadata.track(),
            disc = metadata.disc(),
            subtitle = metadata.subtitle(),
            date = metadata.date().takeIf { work.readDetailedDates && dimensions.advancedDates },
            albumMusicBrainzId =
                metadata.albumMusicBrainzId().takeIf {
                    work.readMusicBrainz && dimensions.musicBrainz
                },
            albumName = metadata.albumName(),
            albumSortName = metadata.albumSortName(),
            releaseTypes =
                if (work.readReleaseTypes && dimensions.releaseTypes) {
                    metadata.releaseTypes()
                        ?: listOf("compilation").takeIf { compilation }
                        ?: emptyList()
                } else {
                    emptyList()
                },
            artistMusicBrainzIds =
                if (work.readMusicBrainz && dimensions.musicBrainz) {
                    metadata.artistMusicBrainzIds()
                        ?: metadata.composerMusicBrainzIds()
                        ?: emptyList()
                } else {
                    emptyList()
                },
            artistNames = artistNames,
            artistSortNames = artistSortNames,
            albumArtistMusicBrainzIds =
                if (work.readMusicBrainz && dimensions.musicBrainz) {
                    metadata.albumArtistMusicBrainzIds().orEmpty()
                } else {
                    emptyList()
                },
            albumArtistNames =
                if (dimensions.albumArtists) {
                    albumArtistNames.ifEmpty {
                        listOf("Various Artists").takeIf { compilation }.orEmpty()
                    }
                } else {
                    emptyList()
                },
            albumArtistSortNames =
                if (dimensions.albumArtists) {
                    val values = metadata.albumArtistSortNames().orEmpty()
                    if (work.expandMultipleArtists) values else values.take(1)
                } else {
                    emptyList()
                },
            genreNames =
                if (work.readGenres && dimensions.genres) metadata.genreNames().orEmpty()
                else emptyList(),
        )
    }
}
