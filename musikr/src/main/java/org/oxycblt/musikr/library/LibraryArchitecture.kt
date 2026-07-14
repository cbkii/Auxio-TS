/*
 * Copyright (c) 2026 Auxio Project
 * LibraryArchitecture.kt is part of Auxio.
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

package org.oxycblt.musikr.library

/** Indexing profile used by the database-first library pipeline. */
enum class MetadataProfile {
    /** Minimal metadata needed for playback, first-page browsing, and fast indexed search. */
    LEAN,

    /** Full relationship, ReplayGain, MusicBrainz, genre, and advanced artwork enrichment. */
    FULL,
}

/** Policy for artwork work. Artwork decoding must be lazy unless [FULL_INDEXING] is selected. */
enum class ArtworkPolicy {
    NONE,
    CURRENT_TRACK_ONLY,
    VISIBLE_ITEMS,
    FULL_INDEXING,
}

/** Optional library dimensions gated before parsing, indexing, and querying work is scheduled. */
data class LibraryDimensionPolicy(
    val genres: Boolean,
    val playlists: Boolean,
    val detailedCollaborators: Boolean,
    val albumArtists: Boolean,
    val releaseTypes: Boolean,
    val advancedDates: Boolean,
    val replayGain: Boolean,
    val musicBrainz: Boolean,
)

/** A stable file fingerprint. Routine scans must not hash whole audio files. */
data class LibraryFingerprint(
    val sourceKey: String,
    val relativePathOrUri: String,
    val sizeBytes: Long,
    val modifiedTimeMs: Long,
    val providerDocumentId: String? = null,
    val providerGeneration: String? = null,
)

/** Scan generation state. Visible queries use only the last committed generation. */
enum class ScanGenerationState {
    PENDING,
    COMMITTED,
    FAILED,
    CANCELLED,
}
