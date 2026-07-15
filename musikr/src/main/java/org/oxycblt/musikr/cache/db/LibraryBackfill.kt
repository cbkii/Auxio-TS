/*
 * Copyright (c) 2026 Auxio Project
 * LibraryBackfill.kt is part of Auxio.
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

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.withTransaction

/**
 * DAO for the bounded, restart-safe backfill of legacy [CachedFileData] rows into the normalized
 * library tables introduced by [CacheDatabase.MIGRATION_70_71].
 *
 * Resume/checkpoint state is implicit: a legacy row is considered migrated once a [LibrarySongData]
 * row with the same URI exists, so a rerun after cancellation or process death simply continues
 * with the remaining rows and never duplicates work.
 */
@Dao
internal interface LibraryBackfillDao {
    @Query(
        "SELECT COUNT(*) FROM CachedFileData " +
            "WHERE uri NOT IN (SELECT uri FROM LibrarySongData)"
    )
    suspend fun remainingLegacyRows(): Int

    @Query(
        "SELECT * FROM CachedFileData " +
            "WHERE uri NOT IN (SELECT uri FROM LibrarySongData) ORDER BY uri LIMIT :limit"
    )
    suspend fun unmigratedLegacyPage(limit: Int): List<CachedFileData>

    @Query("SELECT id FROM LibraryVolumeData WHERE stableSourceKey = :key LIMIT 1")
    suspend fun volumeIdByKey(key: String): Long?

    @Insert suspend fun insertVolume(volume: LibraryVolumeData): Long

    @Query("SELECT id FROM LibraryAlbumData WHERE title = :title LIMIT 1")
    suspend fun albumIdByTitle(title: String): Long?

    @Insert suspend fun insertAlbum(album: LibraryAlbumData): Long

    @Query("SELECT id FROM LibraryArtistData WHERE name = :name LIMIT 1")
    suspend fun artistIdByName(name: String): Long?

    @Insert suspend fun insertArtist(artist: LibraryArtistData): Long

    @Query("SELECT id FROM LibraryGenreData WHERE name = :name LIMIT 1")
    suspend fun genreIdByName(name: String): Long?

    @Insert suspend fun insertGenre(genre: LibraryGenreData): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSong(song: LibrarySongData): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSongArtists(refs: List<SongArtistCrossRefData>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSongGenres(refs: List<SongGenreCrossRefData>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAlbumArtists(refs: List<AlbumArtistCrossRefData>)
}

/**
 * Bounded, restart-safe backfill of the legacy flat cache into the normalized library model.
 *
 * Guarantees:
 * - legacy rows are read in bounded batches and written transactionally per batch;
 * - the legacy [CachedFileData] table is never deleted or altered by the backfill, so the last
 *   valid library remains available while backfill is incomplete;
 * - a rerun after cancellation or process death resumes with the remaining rows only;
 * - reruns never create duplicate rows (unique URI identity plus insert-or-ignore);
 * - progress is observable through the [run] callback without blocking playback or startup.
 */
internal class LibraryBackfill(private val db: CacheDatabase) {
    private val dao = db.backfillDao()

    /** Returns the number of legacy rows that still require backfill. */
    suspend fun remaining(): Int = dao.remainingLegacyRows()

    /**
     * Run the backfill until no unmigrated legacy rows remain. Each batch commits independently, so
     * cancellation between batches preserves all completed work.
     *
     * @return the total number of songs backfilled by this invocation.
     */
    suspend fun run(batchSize: Int = DEFAULT_BATCH_SIZE, onProgress: ((Int) -> Unit)? = null): Int {
        require(batchSize > 0) { "Backfill batch size must be positive" }
        var total = 0
        while (true) {
            val migrated =
                db.withTransaction {
                    val page = dao.unmigratedLegacyPage(batchSize)
                    if (page.isEmpty()) return@withTransaction 0
                    val volumeId = getOrCreateLegacyVolume()
                    page.forEach { backfillRow(volumeId, it) }
                    page.size
                }
            if (migrated == 0) break
            total += migrated
            onProgress?.invoke(total)
        }
        return total
    }


    /**
     * Run at most one bounded batch for startup projections. This intentionally does not drain the
     * full legacy cache; callers can publish Fast Browse/Search after this returns and continue the
     * remaining [run] work asynchronously.
     */
    suspend fun runOneBatch(batchSize: Int = DEFAULT_BATCH_SIZE): Int {
        require(batchSize > 0) { "Backfill batch size must be positive" }
        return db.withTransaction {
            val page = dao.unmigratedLegacyPage(batchSize)
            if (page.isEmpty()) return@withTransaction 0
            val volumeId = getOrCreateLegacyVolume()
            page.forEach { backfillRow(volumeId, it) }
            page.size
        }
    }

    private suspend fun getOrCreateLegacyVolume(): Long =
        dao.volumeIdByKey(LEGACY_SOURCE_KEY)
            ?: dao.insertVolume(
                LibraryVolumeData(
                    stableSourceKey = LEGACY_SOURCE_KEY,
                    displayName = "Legacy cache",
                    sourceType = "LEGACY_CACHE",
                    rootUri = null,
                    rootPath = null,
                    available = true,
                    lastCommittedGeneration = 0L,
                    pendingGeneration = null,
                    lastSuccessfulScanMs = null,
                    lastSeenMs = System.currentTimeMillis(),
                    configurationRevision = 0L,
                )
            )

    private suspend fun backfillRow(volumeId: Long, row: CachedFileData) {
        val uriText = row.uri.toString()
        val fileName = row.uri.lastPathSegment ?: uriText
        val title = row.name ?: fileName
        val titleSort = row.sortName ?: title
        val primaryArtistName = row.artistNames?.firstOrNull()
        val primaryArtistSort = row.artistSortNames?.firstOrNull() ?: primaryArtistName
        val albumId = row.albumName?.let { getOrCreateAlbum(it, row.albumSortName ?: it) }
        val songId =
            dao.insertSong(
                LibrarySongData(
                    stableUid = "legacy:$uriText",
                    volumeId = volumeId,
                    albumId = albumId,
                    uri = uriText,
                    relativePath = row.uri.path,
                    displayPath = row.uri.path,
                    fileName = fileName,
                    title = title,
                    titleSort = titleSort,
                    primaryArtistName = primaryArtistName,
                    primaryArtistSort = primaryArtistSort,
                    albumName = row.albumName,
                    albumSort = row.albumSortName ?: row.albumName,
                    trackNumber = row.track,
                    discNumber = row.disc,
                    durationMs = row.durationMs,
                    sizeBytes = 0L,
                    modifiedTimeMs = row.modifiedMs,
                    dateAddedMs = row.addedMs,
                    mimeType = row.mimeType,
                    codecHint = null,
                    embeddedArtworkRef = row.coverId,
                    externalArtworkRef = null,
                    replayGainTrack = row.replayGainTrackAdjustment,
                    replayGainAlbum = row.replayGainAlbumAdjustment,
                    musicBrainzIds = row.musicBrainzId,
                    scanGeneration = 0L,
                    metadataRevision = 0L,
                    available = true,
                )
            )
        // A conflicting URI means another rerun already migrated this row; skip relationships.
        if (songId == -1L) return

        val artistRefs = mutableListOf<SongArtistCrossRefData>()
        row.artistNames.orEmpty().forEachIndexed { index, name ->
            val artistId = getOrCreateArtist(name, row.artistSortNames?.getOrNull(index) ?: name)
            artistRefs += SongArtistCrossRefData(songId, artistId, ROLE_ARTIST, index)
        }
        row.albumArtistNames.orEmpty().forEachIndexed { index, name ->
            val artistId =
                getOrCreateArtist(name, row.albumArtistSortNames?.getOrNull(index) ?: name)
            artistRefs += SongArtistCrossRefData(songId, artistId, ROLE_ALBUM_ARTIST, index)
            if (albumId != null) {
                dao.insertAlbumArtists(listOf(AlbumArtistCrossRefData(albumId, artistId, index)))
            }
        }
        if (artistRefs.isNotEmpty()) dao.insertSongArtists(artistRefs)

        val genreRefs =
            row.genreNames.orEmpty().mapIndexed { index, name ->
                SongGenreCrossRefData(songId, getOrCreateGenre(name), index)
            }
        if (genreRefs.isNotEmpty()) dao.insertSongGenres(genreRefs)
    }

    private suspend fun getOrCreateAlbum(title: String, titleSort: String): Long =
        dao.albumIdByTitle(title)
            ?: dao.insertAlbum(
                LibraryAlbumData(
                    title = title,
                    titleSort = titleSort,
                    scanGeneration = 0L,
                    metadataRevision = 0L,
                    available = true,
                )
            )

    private suspend fun getOrCreateArtist(name: String, nameSort: String): Long =
        dao.artistIdByName(name)
            ?: dao.insertArtist(
                LibraryArtistData(
                    name = name,
                    nameSort = nameSort,
                    scanGeneration = 0L,
                    metadataRevision = 0L,
                    available = true,
                )
            )

    private suspend fun getOrCreateGenre(name: String): Long =
        dao.genreIdByName(name)
            ?: dao.insertGenre(
                LibraryGenreData(
                    name = name,
                    nameSort = name,
                    scanGeneration = 0L,
                    metadataRevision = 0L,
                    available = true,
                )
            )

    companion object {
        const val DEFAULT_BATCH_SIZE = 128
        internal const val LEGACY_SOURCE_KEY = "legacy-cache"
        private const val ROLE_ARTIST = "artist"
        private const val ROLE_ALBUM_ARTIST = "albumartist"
    }
}
