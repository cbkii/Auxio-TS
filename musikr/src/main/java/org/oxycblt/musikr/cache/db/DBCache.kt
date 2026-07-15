/*
 * Copyright (c) 2025 Auxio Project
 * DBCache.kt is part of Auxio.
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

package org.oxycblt.musikr.cache.db

import android.content.Context
import org.oxycblt.musikr.cache.Audio
import org.oxycblt.musikr.cache.Cache
import org.oxycblt.musikr.cache.CacheResult
import org.oxycblt.musikr.cache.CachedFile
import org.oxycblt.musikr.cache.MutableCache
import org.oxycblt.musikr.cache.StartupProjectionCache
import org.oxycblt.musikr.cache.StartupSongRow
import org.oxycblt.musikr.cache.StartupSummaryRow
import org.oxycblt.musikr.fs.AddedMs
import org.oxycblt.musikr.fs.Components
import org.oxycblt.musikr.fs.File
import org.oxycblt.musikr.fs.Path
import org.oxycblt.musikr.fs.Volume
import org.oxycblt.musikr.metadata.Properties
import org.oxycblt.musikr.tag.parse.ParsedTags

/**
 * An immutable [Cache] backed by an internal Room database.
 *
 * Create an instance with [from].
 */
class DBCache private constructor(
    private val readDao: CacheReadDao,
    private val libraryDao: LibraryReadDao,
) : Cache, StartupProjectionCache {
    override suspend fun read(file: File): CacheResult {
        val dbSong = readDao.selectSongByUri(file.uri) ?: return CacheResult.Miss(file)
        if (dbSong.modifiedMs != file.modifiedMs) {
            return CacheResult.Stale(file, dbSong.addedMs)
        }
        return CacheResult.Hit(dbSong.toCachedFile(file))
    }

    override suspend fun snapshot(): List<CachedFile> {
        // Compatibility-only API for legacy full-library reconstruction. Startup/Fast Start
        // callers must use the bounded StartupProjectionCache methods below.
        val result = mutableListOf<CachedFile>()
        var offset = 0
        while (true) {
            val page = readDao.selectSongsPage(SNAPSHOT_PAGE_SIZE, offset)
            if (page.isEmpty()) break
            result += page.map { it.toCachedFile(it.toSyntheticFile()) }
            offset += page.size
        }
        return result
    }

    override suspend fun firstSongs(limit: Int, offset: Int): List<StartupSongRow> =
        libraryDao.songsPage(limit.coerceIn(1, MAX_STARTUP_LIMIT), offset.coerceAtLeast(0)).map {
            it.toStartupSongRow()
        }

    override suspend fun recentlyAdded(limit: Int): List<StartupSongRow> =
        libraryDao.recentlyAdded(limit.coerceIn(1, MAX_STARTUP_LIMIT)).map { it.toStartupSongRow() }

    override suspend fun albums(limit: Int, offset: Int): List<StartupSummaryRow> =
        libraryDao.albumsPage(limit.coerceIn(1, MAX_STARTUP_LIMIT), offset.coerceAtLeast(0)).map {
            StartupSummaryRow(it.id.toString(), it.title)
        }

    override suspend fun artists(limit: Int, offset: Int): List<StartupSummaryRow> =
        libraryDao.artistsPage(limit.coerceIn(1, MAX_STARTUP_LIMIT), offset.coerceAtLeast(0)).map {
            StartupSummaryRow(it.id.toString(), it.name)
        }

    override suspend fun quickSearchSongs(query: String, limit: Int): List<StartupSongRow> =
        if (query.isBlank()) {
            emptyList()
        } else {
            libraryDao.searchSongs(LikeQuery.contains(query), limit.coerceIn(1, 10), 0).map {
                it.toStartupSongRow()
            }
        }

    private fun SongListRow.toStartupSongRow() =
        StartupSongRow(
            stableId = stableUid,
            uri = uri,
            directPath = displayPath,
            title = title,
            primaryArtist = primaryArtistName,
            album = albumName,
            durationMs = durationMs,
            artworkRef = embeddedArtworkRef ?: externalArtworkRef,
            available = available,
        )

    private fun CachedFileData.toCachedFile(file: File) =
        CachedFile(
            file,
            mimeType?.let {
                Audio(
                    Properties(mimeType, durationMs!!, bitrateKbps!!, sampleRateHz!!),
                    ParsedTags(
                        musicBrainzId = musicBrainzId,
                        name = name,
                        sortName = sortName,
                        durationMs = durationMs,
                        track = track,
                        disc = disc,
                        subtitle = subtitle,
                        date = date,
                        albumMusicBrainzId = albumMusicBrainzId,
                        albumName = albumName,
                        albumSortName = albumSortName,
                        releaseTypes = releaseTypes!!,
                        artistMusicBrainzIds = artistMusicBrainzIds!!,
                        artistNames = artistNames!!,
                        artistSortNames = artistSortNames!!,
                        albumArtistMusicBrainzIds = albumArtistMusicBrainzIds!!,
                        albumArtistNames = albumArtistNames!!,
                        albumArtistSortNames = albumArtistSortNames!!,
                        genreNames = genreNames!!,
                        replayGainTrackAdjustment = replayGainTrackAdjustment,
                        replayGainAlbumAdjustment = replayGainAlbumAdjustment,
                    ),
                    coverId = coverId,
                )
            },
            addedMs = addedMs,
        )

    /**
     * Build a synthetic [File] from cached data without exploring storage.
     *
     * Best-effort metadata only:
     * - [File.path] is derived from the URI and may not correspond to a real filesystem path.
     * - [File.size] is unknown (set to 0); callers must not treat this as authoritative.
     * - [File.parent] is unknown (null); callers must not rely on it for folder navigation.
     */
    private fun CachedFileData.toSyntheticFile(): File {
        val pathText = uri.path ?: uri.lastPathSegment ?: uri.toString()
        return File(
            uri = uri,
            path = Path(Volume.ThirdParty(uri), Components.parseUnix(pathText)),
            addedMs =
                object : AddedMs {
                    override suspend fun resolve() = addedMs
                },
            modifiedMs = modifiedMs,
            mimeType = mimeType ?: "application/octet-stream",
            size = 0L,
            parent = null,
        )
    }

    companion object {
        private const val SNAPSHOT_PAGE_SIZE = 256
        private const val MAX_STARTUP_LIMIT = 100

        /**
         * Create a new instance of [DBCache] from the given [context].
         *
         * This instance should be a singleton, since it implicitly holds a Room database. As a
         * result, you should only create EITHER a [DBCache] or a [MutableDBCache].
         *
         * @param context The context to use to create the Room database.
         * @return A new instance of [DBCache].
         */
        fun from(context: Context) = from(CacheDatabase.from(context))

        internal fun from(db: CacheDatabase) = DBCache(db.readDao(), db.libraryDao())
    }
}

/**
 * A mutable [Cache] backed by an internal Room database.
 *
 * Create an instance with [from].
 */
class MutableDBCache
private constructor(
    private val inner: DBCache,
    private val writeDao: CacheWriteDao,
    private val backfill: LibraryBackfill,
) : MutableCache, StartupProjectionCache {
    override suspend fun read(file: File) = inner.read(file)

    override suspend fun snapshot() = inner.snapshot()

    override suspend fun populateNormalizedLibrary(): Int = backfill.run()

    override suspend fun firstSongs(limit: Int, offset: Int) = inner.firstSongs(limit, offset)

    override suspend fun recentlyAdded(limit: Int) = inner.recentlyAdded(limit)

    override suspend fun albums(limit: Int, offset: Int) = inner.albums(limit, offset)

    override suspend fun artists(limit: Int, offset: Int) = inner.artists(limit, offset)

    override suspend fun quickSearchSongs(query: String, limit: Int) = inner.quickSearchSongs(query, limit)

    override suspend fun write(cachedFile: CachedFile) {
        val dbSong =
            CachedFileData(
                uri = cachedFile.file.uri,
                modifiedMs = cachedFile.file.modifiedMs,
                addedMs = cachedFile.addedMs,
                mimeType = cachedFile.audio?.properties?.mimeType,
                durationMs = cachedFile.audio?.properties?.durationMs,
                bitrateKbps = cachedFile.audio?.properties?.bitrateKbps,
                sampleRateHz = cachedFile.audio?.properties?.sampleRateHz,
                musicBrainzId = cachedFile.audio?.tags?.musicBrainzId,
                name = cachedFile.audio?.tags?.name,
                sortName = cachedFile.audio?.tags?.sortName,
                track = cachedFile.audio?.tags?.track,
                disc = cachedFile.audio?.tags?.disc,
                subtitle = cachedFile.audio?.tags?.subtitle,
                date = cachedFile.audio?.tags?.date,
                albumMusicBrainzId = cachedFile.audio?.tags?.albumMusicBrainzId,
                albumName = cachedFile.audio?.tags?.albumName,
                albumSortName = cachedFile.audio?.tags?.albumSortName,
                releaseTypes = cachedFile.audio?.tags?.releaseTypes,
                artistMusicBrainzIds = cachedFile.audio?.tags?.artistMusicBrainzIds,
                artistNames = cachedFile.audio?.tags?.artistNames,
                artistSortNames = cachedFile.audio?.tags?.artistSortNames,
                albumArtistMusicBrainzIds = cachedFile.audio?.tags?.albumArtistMusicBrainzIds,
                albumArtistNames = cachedFile.audio?.tags?.albumArtistNames,
                albumArtistSortNames = cachedFile.audio?.tags?.albumArtistSortNames,
                genreNames = cachedFile.audio?.tags?.genreNames,
                replayGainTrackAdjustment = cachedFile.audio?.tags?.replayGainTrackAdjustment,
                replayGainAlbumAdjustment = cachedFile.audio?.tags?.replayGainAlbumAdjustment,
                coverId = cachedFile.audio?.coverId,
            )
        writeDao.updateSong(dbSong)
    }

    override suspend fun cleanup(excluding: List<CachedFile>) {
        writeDao.deleteExcludingUris(excluding.mapTo(mutableSetOf()) { it.file.uri.toString() })
    }

    companion object {
        /**
         * Create a new instance of [MutableDBCache] from the given [context].
         *
         * This instance should be a singleton, since it implicitly holds a Room database. As a
         * result, you should only create EITHER a [DBCache] or a [MutableDBCache].
         *
         * @param context The context to use to create the Room database.
         * @return A new instance of [MutableDBCache].
         */
        fun from(context: Context): MutableDBCache {
            val db = CacheDatabase.from(context)
            return MutableDBCache(DBCache.from(db), db.writeDao(), LibraryBackfill(db))
        }

        internal fun from(db: CacheDatabase): MutableDBCache =
            MutableDBCache(DBCache.from(db), db.writeDao(), LibraryBackfill(db))
    }
}
