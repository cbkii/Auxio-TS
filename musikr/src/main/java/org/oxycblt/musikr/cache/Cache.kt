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

/**
 * An immutable repository for cached song metadata.
 *
 * Since file opening and metadata extraction sends to be quite slow on Android, a cache allows
 * up-to-date metadata to be read from a local database, which tends to be far faster.
 *
 * This is a read-only interface for reading cached metadata and isn't expected by Musikr's public
 * API, however there might be some use in external cache diagnostics by the client. For writing,
 * see [MutableCache].
 */
interface Cache {
    /**
     * Read a [CachedFile] corresponding to the given [file] from the cache. This can result in
     * several outcomes represented by [CacheResult].
     *
     * @param file the [File] to read from the cache
     * @return a [CacheResult] representing the result of the operation.
     */
    suspend fun read(file: File): CacheResult

    /** Read every cached file without exploring storage. */
    suspend fun snapshot(): List<CachedFile>
}

/** A mutable repository for cached song metadata. */
interface MutableCache : Cache {
    /** Write one [CachedFile] to the cache. */
    suspend fun write(cachedFile: CachedFile)

    /**
     * Write several entries as one logical batch when the implementation can do so efficiently.
     * The default preserves compatibility for non-database caches.
     */
    suspend fun writeAll(cachedFiles: List<CachedFile>) {
        cachedFiles.forEach { write(it) }
    }

    suspend fun cleanup(excluding: List<CachedFile>)

    suspend fun populateNormalizedLibrary(): Int = 0

    suspend fun prepareStartupProjections(): Int = 0
}

/** A cached song entry containing the data needed by the rest of the loader. */
data class CachedFile(
    val file: File,
    val audio: Audio?,
    val addedMs: Long,
)

/** Actual audio metadata if a [CachedFile] is an audio file */
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
