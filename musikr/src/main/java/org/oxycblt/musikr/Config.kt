/*
 * Copyright (c) 2024 Auxio Project
 * Config.kt is part of Auxio.
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

package org.oxycblt.musikr

import org.oxycblt.musikr.cache.IncrementalScanPlan
import org.oxycblt.musikr.cache.MutableCache
import org.oxycblt.musikr.covers.Cover
import org.oxycblt.musikr.covers.MutableCovers
import org.oxycblt.musikr.fs.FS
import org.oxycblt.musikr.library.ArtworkPolicy
import org.oxycblt.musikr.library.LibraryDimensionPolicy
import org.oxycblt.musikr.library.MetadataProfile
import org.oxycblt.musikr.playlist.db.StoredPlaylists
import org.oxycblt.musikr.tag.interpret.Naming
import org.oxycblt.musikr.tag.interpret.Separators

data class Config(
    val fs: FS,
    val storage: Storage,
    val interpretation: Interpretation,
    val indexingWorkerCount: Int = 2,
    val metadataProfile: MetadataProfile = MetadataProfile.FULL,
    val dimensionPolicy: LibraryDimensionPolicy =
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
    val artworkPolicy: ArtworkPolicy = ArtworkPolicy.FULL_INDEXING,
    val scanPlan: IncrementalScanPlan? = null,
    /** Cover garbage collection is maintenance work, not part of ordinary incremental scans. */
    val cleanupCovers: Boolean = scanPlan == null,
)

/** Side-effect laden [Config] for use during music loading and [MutableLibrary] operation. */
data class Storage(
    /** Metadata cache used by exploration, extraction and generation commit. */
    val cache: MutableCache,
    /** Durable cover store. Expensive writes are gated by [Config.artworkPolicy]. */
    val covers: MutableCovers<out Cover>,
    /** User-created playlists loaded independently from source scanning. */
    val storedPlaylists: StoredPlaylists,
)

data class Interpretation(
    /** How to construct names from audio tags. */
    val naming: Naming,
    /** What separators delimit multi-value audio tags. */
    val separators: Separators,
)
