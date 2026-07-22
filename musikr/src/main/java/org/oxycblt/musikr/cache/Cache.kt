/*
 * Copyright (c) 2024 Auxio Project
 * Cache.kt is part of Auxio.
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

package org.oxycblt.musikr.cache

import org.oxycblt.musikr.fs.File
import org.oxycblt.musikr.metadata.Properties
import org.oxycblt.musikr.tag.parse.ParsedTags

/** Durable, ordered generated-playlist projection tied to one committed library generation. */
data class GeneratedPlaylistSnapshot(
    val stableKey: String,
    val stableUid: String,
    val name: String,
    val generation: String,
    val orderedSongUids: List<String>,
)

/**
 * An immutable repository for cached song metadata.
 *
 * Since file opening and metadata extraction tends to be slow on Android, a cache allows up-to-date
 * metadata to be read from a local database, which tends to be far faster.
 */
interface Cache {
    suspend fun read(file: File): CacheResult

    /** Read every cached file without exploring storage. */
    suspend fun snapshot(): List<CachedFile>

    /** Read the last atomically committed generated-playlist projection. */
    suspend fun readGeneratedPlaylists(): List<GeneratedPlaylistSnapshot> = emptyList()
}

/** A mutable repository for cached song metadata. */
interface MutableCache : Cache {
    suspend fun write(cachedFile: CachedFile)

    suspend fun cleanup(excluding: List<CachedFile>)

    /** Atomically replace only generated definitions and membership after a successful scan. */
    suspend fun replaceGeneratedPlaylists(playlists: List<GeneratedPlaylistSnapshot>) {}

    /**
     * Populate the normalized library model from any remaining legacy cache rows in bounded,
     * restart-safe batches.
     */
    suspend fun populateNormalizedLibrary(): Int = 0

    /** Prepare at most one bounded batch of normalized rows for startup projections. */
    suspend fun prepareStartupProjections(): Int = 0
}

/** A cached song entry containing the data needed by the rest of the loader. */
data class CachedFile(
    val file: File,
    val audio: Audio?,
    /** The time the song was added to the cache. */
    val addedMs: Long,
)

/** Actual audio metadata if a [CachedFile] is an audio file. */
data class Audio(
    val properties: Properties,
    val tags: ParsedTags,
    val coverId: String?,
)

/** A result of a cache lookup. */
sealed interface CacheResult {
    data class Hit(val file: CachedFile) : CacheResult

    data class Miss(val file: File) : CacheResult

    data class Stale(val file: File, val addedMs: Long) : CacheResult
}
