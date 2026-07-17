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
import org.oxycblt.musikr.cache.IncrementalCache
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

/** Immutable cache view backed by the committed Room generations. */
class DBCache
private constructor(
    private val readDao: CacheReadDao,
    private val incrementalLibraryDao: IncrementalLibraryReadDao,
    private val incrementalStore: IncrementalScanStore,
) : Cache, StartupProjectionCache {
    override suspend fun read(file: File): CacheResult {
        val dbSong = readDao.selectSongByUri(file.uri)
        if (dbSong == null) {
            incrementalStore.markSeen(file)
            return CacheResult.Miss(file)
        }
        val cachedFile = dbSong.toCachedFile(file)
        if (dbSong.modifiedMs != file.modifiedMs || !incrementalStore.cachedProfileAccepts(file)) {
            incrementalStore.markSeen(file, cachedFile)
            return CacheResult.Stale(file, dbSong.addedMs)
        }
        incrementalStore.markSeen(file, cachedFile)
        return CacheResult.Hit(cachedFile)
    }

    override suspend fun snapshot(): List<CachedFile> {
        // Explicit compatibility bridge for rich screens not yet migrated to projections.
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
        incrementalLibraryDao
            .songsPage(limit.coerceIn(1, MAX_STARTUP_LIMIT), offset.coerceAtLeast(0))
            .map { it.toStartupSongRow() }

    override suspend fun recentlyAdded(limit: Int): List<StartupSongRow> =
        incrementalLibraryDao.recentlyAdded(limit.coerceIn(1, MAX_STARTUP_LIMIT)).map {
            it.toStartupSongRow()
        }

    override suspend fun albums(limit: Int, offset: Int): List<StartupSummaryRow> =
        incrementalLibraryDao
            .albumsPage(limit.coerceIn(1, MAX_STARTUP_LIMIT), offset.coerceAtLeast(0))
            .map { StartupSummaryRow(it.id.toString(), it.title) }

    override suspend fun artists(limit: Int, offset: Int): List<StartupSummaryRow> =
        incrementalLibraryDao
            .artistsPage(limit.coerceIn(1, MAX_STARTUP_LIMIT), offset.coerceAtLeast(0))
            .map { StartupSummaryRow(it.id.toString(), it.name) }

    override suspend fun quickSearchSongs(query: String, limit: Int): List<StartupSongRow> =
        if (query.isBlank()) {
            emptyList()
        } else {
            incrementalLibraryDao
                .searchSongs(LikeQuery.contains(query), limit.coerceIn(1, 10), 0)
                .map { it.toStartupSongRow() }
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
                    Properties(mimeType, durationMs ?: 0L, bitrateKbps ?: 0, sampleRateHz ?: 0),
                    ParsedTags(
                        musicBrainzId = musicBrainzId,
                        name = name,
                        sortName = sortName,
                        durationMs = durationMs ?: 0L,
                        track = track,
                        disc = disc,
                        subtitle = subtitle,
                        date = date,
                        albumMusicBrainzId = albumMusicBrainzId,
                        albumName = albumName,
                        albumSortName = albumSortName,
                        releaseTypes = releaseTypes.orEmpty(),
                        artistMusicBrainzIds = artistMusicBrainzIds.orEmpty(),
                        artistNames = artistNames.orEmpty(),
                        artistSortNames = artistSortNames.orEmpty(),
                        albumArtistMusicBrainzIds = albumArtistMusicBrainzIds.orEmpty(),
                        albumArtistNames = albumArtistNames.orEmpty(),
                        albumArtistSortNames = albumArtistSortNames.orEmpty(),
                        genreNames = genreNames.orEmpty(),
                        replayGainTrackAdjustment = replayGainTrackAdjustment,
                        replayGainAlbumAdjustment = replayGainAlbumAdjustment,
                    ),
                    coverId = coverId,
                )
            },
            addedMs = addedMs,
        )

    /** Best-effort synthetic file used only by compatibility hydration. */
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

        fun from(context: Context) = from(CacheDatabase.from(context))

        internal fun from(db: CacheDatabase): DBCache {
            val store = IncrementalScanStore(db, db.readDao(), db.writeDao(), db.incrementalDao())
            return DBCache(db.readDao(), db.incrementalLibraryDao(), store)
        }

        internal fun from(db: CacheDatabase, store: IncrementalScanStore) =
            DBCache(db.readDao(), db.incrementalLibraryDao(), store)
    }
}

/** Mutable cache with staged source generations and legacy compatibility APIs. */
class MutableDBCache
private constructor(
    private val inner: DBCache,
    private val writeDao: CacheWriteDao,
    private val backfill: LibraryBackfill,
    private val incrementalStore: IncrementalScanStore,
) : MutableCache, StartupProjectionCache by inner, IncrementalCache by incrementalStore {
    override suspend fun read(file: File) = inner.read(file)

    override suspend fun snapshot() = inner.snapshot()

    override suspend fun populateNormalizedLibrary(): Int = backfill.run()

    override suspend fun prepareStartupProjections(): Int =
        backfill.runOneBatch(STARTUP_SEED_BATCH_SIZE)

    override suspend fun write(cachedFile: CachedFile) {
        if (incrementalStore.stage(cachedFile)) return
        writeDao.updateSong(cachedFile.toCachedFileData())
    }

    override suspend fun cleanup(excluding: List<CachedFile>) {
        // Generation commit performs database-side reconciliation without a complete URI set.
        if (incrementalStore.activePlan() != null) return
        writeDao.deleteExcludingUris(excluding.mapTo(mutableSetOf()) { it.file.uri.toString() })
    }

    private fun CachedFile.toCachedFileData() =
        CachedFileData(
            uri = file.uri,
            modifiedMs = file.modifiedMs,
            addedMs = addedMs,
            mimeType = audio?.properties?.mimeType,
            durationMs = audio?.properties?.durationMs,
            bitrateKbps = audio?.properties?.bitrateKbps,
            sampleRateHz = audio?.properties?.sampleRateHz,
            musicBrainzId = audio?.tags?.musicBrainzId,
            name = audio?.tags?.name,
            sortName = audio?.tags?.sortName,
            track = audio?.tags?.track,
            disc = audio?.tags?.disc,
            subtitle = audio?.tags?.subtitle,
            date = audio?.tags?.date,
            albumMusicBrainzId = audio?.tags?.albumMusicBrainzId,
            albumName = audio?.tags?.albumName,
            albumSortName = audio?.tags?.albumSortName,
            releaseTypes = audio?.tags?.releaseTypes,
            artistMusicBrainzIds = audio?.tags?.artistMusicBrainzIds,
            artistNames = audio?.tags?.artistNames,
            artistSortNames = audio?.tags?.artistSortNames,
            albumArtistMusicBrainzIds = audio?.tags?.albumArtistMusicBrainzIds,
            albumArtistNames = audio?.tags?.albumArtistNames,
            albumArtistSortNames = audio?.tags?.albumArtistSortNames,
            genreNames = audio?.tags?.genreNames,
            replayGainTrackAdjustment = audio?.tags?.replayGainTrackAdjustment,
            replayGainAlbumAdjustment = audio?.tags?.replayGainAlbumAdjustment,
            coverId = audio?.coverId,
        )

    companion object {
        private const val STARTUP_SEED_BATCH_SIZE = 32

        fun from(context: Context): MutableDBCache = from(CacheDatabase.from(context))

        internal fun from(db: CacheDatabase): MutableDBCache {
            val store = IncrementalScanStore(db, db.readDao(), db.writeDao(), db.incrementalDao())
            return MutableDBCache(
                DBCache.from(db, store),
                db.writeDao(),
                LibraryBackfill(db),
                store,
            )
        }
    }
}
