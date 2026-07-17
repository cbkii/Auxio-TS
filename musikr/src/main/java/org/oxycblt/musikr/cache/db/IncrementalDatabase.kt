/*
 * Copyright (c) 2026 Auxio Project
 * IncrementalDatabase.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.oxycblt.musikr.cache.db

import android.net.Uri
import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.TypeConverters
import org.oxycblt.musikr.fs.SourceSnapshot
import org.oxycblt.musikr.tag.Date

/** Durable per-source observation and committed-generation ledger. */
@Entity
internal data class SourceLedgerData(
    @PrimaryKey val sourceKey: String,
    val sourceType: String,
    val rootUri: String?,
    val rootPath: String?,
    val fingerprint: String?,
    val fingerprintStrength: String,
    val available: Boolean,
    val lastSeenMs: Long,
    val lastCommittedGeneration: Long?,
    val pendingGeneration: Long?,
    val lastSuccessfulScanMs: Long?,
    val configurationRevision: Long,
    val invalidationVersion: Long,
    val committedInvalidationVersion: Long,
    val committedProfile: String?,
    val enrichmentRevision: Long,
    val incomplete: Boolean,
) {
    fun observed(snapshot: SourceSnapshot): SourceLedgerData =
        copy(
            sourceType = snapshot.sourceType,
            rootUri = snapshot.rootUri,
            rootPath = snapshot.rootPath,
            fingerprint = snapshot.fingerprint,
            fingerprintStrength = snapshot.fingerprintStrength.name,
            available = snapshot.available,
            lastSeenMs = snapshot.observedAtMs,
        )
}

/** Lifecycle record for one source generation. */
@Entity(primaryKeys = ["scanId", "sourceKey"])
internal data class SourceScanGenerationData(
    val scanId: String,
    val sourceKey: String,
    val generation: Long,
    val state: String,
    val startedAtMs: Long,
    val completedAtMs: Long?,
    val error: String?,
)

/** Every file discovered for a source, including unchanged cache hits. */
@Entity(
    primaryKeys = ["scanId", "sourceKey", "uri"],
    indices = [Index(value = ["scanId", "sourceKey"]), Index(value = ["uri"])],
)
internal data class ScanSeenData(
    val scanId: String,
    val sourceKey: String,
    val uri: String,
    val displayPath: String?,
    val fileName: String,
    val sizeBytes: Long,
    val modifiedTimeMs: Long,
    val dateAddedMs: Long,
    val mimeType: String?,
    val title: String,
    val titleSort: String,
    val primaryArtistName: String?,
    val primaryArtistSort: String?,
    val albumName: String?,
    val albumSort: String?,
    val trackNumber: Int?,
    val discNumber: Int?,
    val durationMs: Long?,
    val artworkRef: String?,
    val metadataProfile: String,
)

/** Changed metadata staged until every pipeline stage completes successfully. */
@Entity(
    primaryKeys = ["scanId", "sourceKey", "uri"],
    indices = [Index(value = ["scanId", "sourceKey"])],
)
@TypeConverters(CachedFileData.Converters::class)
internal data class PendingCachedFileData(
    val scanId: String,
    val sourceKey: String,
    val uri: String,
    val modifiedMs: Long,
    val addedMs: Long,
    val mimeType: String?,
    val durationMs: Long?,
    val bitrateKbps: Int?,
    val sampleRateHz: Int?,
    val musicBrainzId: String?,
    val name: String?,
    val sortName: String?,
    val track: Int?,
    val disc: Int?,
    val subtitle: String?,
    val date: Date?,
    val albumMusicBrainzId: String?,
    val albumName: String?,
    val albumSortName: String?,
    val releaseTypes: List<String>?,
    val artistMusicBrainzIds: List<String>?,
    val artistNames: List<String>?,
    val artistSortNames: List<String>?,
    val albumArtistMusicBrainzIds: List<String>?,
    val albumArtistNames: List<String>?,
    val albumArtistSortNames: List<String>?,
    val genreNames: List<String>?,
    val replayGainTrackAdjustment: Float?,
    val replayGainAlbumAdjustment: Float?,
    val coverId: String?,
) {
    fun toCachedFileData(): CachedFileData =
        CachedFileData(
            uri = Uri.parse(uri),
            modifiedMs = modifiedMs,
            addedMs = addedMs,
            mimeType = mimeType,
            durationMs = durationMs,
            bitrateKbps = bitrateKbps,
            sampleRateHz = sampleRateHz,
            musicBrainzId = musicBrainzId,
            name = name,
            sortName = sortName,
            track = track,
            disc = disc,
            subtitle = subtitle,
            date = date,
            albumMusicBrainzId = albumMusicBrainzId,
            albumName = albumName,
            albumSortName = albumSortName,
            releaseTypes = releaseTypes,
            artistMusicBrainzIds = artistMusicBrainzIds,
            artistNames = artistNames,
            artistSortNames = artistSortNames,
            albumArtistMusicBrainzIds = albumArtistMusicBrainzIds,
            albumArtistNames = albumArtistNames,
            albumArtistSortNames = albumArtistSortNames,
            genreNames = genreNames,
            replayGainTrackAdjustment = replayGainTrackAdjustment,
            replayGainAlbumAdjustment = replayGainAlbumAdjustment,
            coverId = coverId,
        )
}

/** Complete lightweight song projection for one committed source generation. */
@Entity(
    indices =
        [
            Index(value = ["sourceKey", "generation", "uri"], unique = true),
            Index(value = ["sourceKey", "generation", "titleSort"]),
            Index(value = ["stableUid"]),
            Index(value = ["uri"]),
            Index(value = ["dateAddedMs"]),
            Index(value = ["primaryArtistSort"]),
            Index(value = ["albumSort"]),
        ]
)
internal data class IndexedSongData(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sourceKey: String,
    val generation: Long,
    val stableUid: String,
    val uri: String,
    val displayPath: String?,
    val fileName: String,
    val title: String,
    val titleSort: String,
    val primaryArtistName: String?,
    val primaryArtistSort: String?,
    val albumName: String?,
    val albumSort: String?,
    val trackNumber: Int?,
    val discNumber: Int?,
    val durationMs: Long?,
    val sizeBytes: Long,
    val modifiedTimeMs: Long,
    val dateAddedMs: Long,
    val mimeType: String?,
    val artworkRef: String?,
    val metadataProfile: String,
    val enrichmentRevision: Long,
)

/** Prevents stale legacy rows from reappearing after a source generation removed them. */
@Entity(primaryKeys = ["sourceKey", "uri"], indices = [Index(value = ["uri"])])
internal data class IndexedUriStateData(
    val sourceKey: String,
    val uri: String,
    val available: Boolean,
    val lastGeneration: Long,
    val metadataProfile: String,
)

internal data class CommittedCachedRow(
    @Embedded val cache: CachedFileData,
    val sourceKey: String,
    val committedDisplayPath: String?,
    val committedSizeBytes: Long,
    val committedSourceUri: String?,
    val committedRootPath: String?,
)

@Dao
internal interface IncrementalScanDao {
    @Query("SELECT * FROM SourceLedgerData WHERE sourceKey = :sourceKey LIMIT 1")
    suspend fun sourceLedger(sourceKey: String): SourceLedgerData?

    @Query("SELECT * FROM SourceLedgerData") suspend fun sourceLedgers(): List<SourceLedgerData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSourceLedger(ledger: SourceLedgerData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertGeneration(generation: SourceScanGenerationData)

    @Query(
        "UPDATE SourceScanGenerationData SET state = :state, completedAtMs = :completedAtMs, error = :error WHERE scanId = :scanId AND sourceKey = :sourceKey"
    )
    suspend fun completeGeneration(
        scanId: String,
        sourceKey: String,
        state: String,
        completedAtMs: Long,
        error: String?,
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertSeen(row: ScanSeenData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPending(row: PendingCachedFileData)

    @Query(
        "SELECT * FROM PendingCachedFileData WHERE scanId = :scanId AND sourceKey = :sourceKey ORDER BY uri LIMIT :limit OFFSET :offset"
    )
    suspend fun pendingPage(
        scanId: String,
        sourceKey: String,
        limit: Int,
        offset: Int,
    ): List<PendingCachedFileData>

    @Query(
        "SELECT COUNT(*) FROM PendingCachedFileData WHERE scanId = :scanId AND sourceKey = :sourceKey"
    )
    suspend fun pendingCount(scanId: String, sourceKey: String): Int

    @Query(
        "INSERT OR REPLACE INTO IndexedSongData (sourceKey, generation, stableUid, uri, displayPath, fileName, title, titleSort, primaryArtistName, primaryArtistSort, albumName, albumSort, trackNumber, discNumber, durationMs, sizeBytes, modifiedTimeMs, dateAddedMs, mimeType, artworkRef, metadataProfile, enrichmentRevision) " +
            "SELECT sourceKey, :generation, sourceKey || ':' || uri, uri, displayPath, fileName, title, titleSort, primaryArtistName, primaryArtistSort, albumName, albumSort, trackNumber, discNumber, durationMs, sizeBytes, modifiedTimeMs, dateAddedMs, mimeType, artworkRef, metadataProfile, :enrichmentRevision FROM ScanSeenData WHERE scanId = :scanId AND sourceKey = :sourceKey"
    )
    suspend fun publishSeenSongs(
        scanId: String,
        sourceKey: String,
        generation: Long,
        enrichmentRevision: Long,
    )

    @Query(
        "INSERT OR REPLACE INTO IndexedUriStateData (sourceKey, uri, available, lastGeneration, metadataProfile) " +
            "SELECT sourceKey, uri, 1, :generation, metadataProfile FROM ScanSeenData WHERE scanId = :scanId AND sourceKey = :sourceKey"
    )
    suspend fun publishSeenUriStates(scanId: String, sourceKey: String, generation: Long)

    @Query(
        "INSERT OR REPLACE INTO IndexedUriStateData (sourceKey, uri, available, lastGeneration, metadataProfile) " +
            "SELECT sourceKey, uri, 0, :newGeneration, metadataProfile FROM IndexedSongData old WHERE sourceKey = :sourceKey AND generation = :oldGeneration AND NOT EXISTS (SELECT 1 FROM ScanSeenData seen WHERE seen.scanId = :scanId AND seen.sourceKey = old.sourceKey AND seen.uri = old.uri)"
    )
    suspend fun tombstoneMissingUris(
        scanId: String,
        sourceKey: String,
        oldGeneration: Long,
        newGeneration: Long,
    )

    @Query(
        "DELETE FROM CachedFileData WHERE uri IN (SELECT old.uri FROM IndexedSongData old WHERE old.sourceKey = :sourceKey AND old.generation = :oldGeneration AND NOT EXISTS (SELECT 1 FROM ScanSeenData seen WHERE seen.scanId = :scanId AND seen.sourceKey = old.sourceKey AND seen.uri = old.uri))"
    )
    suspend fun deleteMissingCachedRows(scanId: String, sourceKey: String, oldGeneration: Long): Int

    @Query("DELETE FROM IndexedSongData WHERE sourceKey = :sourceKey AND generation <> :generation")
    suspend fun deleteOlderIndexedRows(sourceKey: String, generation: Long)

    @Query("DELETE FROM ScanSeenData WHERE scanId = :scanId") suspend fun deleteSeen(scanId: String)

    @Query("DELETE FROM PendingCachedFileData WHERE scanId = :scanId")
    suspend fun deletePending(scanId: String)

    @Query(
        "SELECT cache.*, song.sourceKey AS sourceKey, song.displayPath AS committedDisplayPath, song.sizeBytes AS committedSizeBytes, source.rootUri AS committedSourceUri, source.rootPath AS committedRootPath FROM CachedFileData cache INNER JOIN IndexedSongData song ON song.uri = cache.uri INNER JOIN SourceLedgerData source ON source.sourceKey = song.sourceKey AND source.lastCommittedGeneration = song.generation WHERE song.sourceKey IN (:sourceKeys) ORDER BY cache.uri LIMIT :limit OFFSET :offset"
    )
    suspend fun committedCachedPage(
        sourceKeys: Set<String>,
        limit: Int,
        offset: Int,
    ): List<CommittedCachedRow>

    @Query(
        "SELECT * FROM IndexedUriStateData WHERE sourceKey = :sourceKey AND uri = :uri LIMIT 1"
    )
    suspend fun uriState(sourceKey: String, uri: String): IndexedUriStateData?

    @Query("UPDATE SourceLedgerData SET invalidationVersion = invalidationVersion + 1")
    suspend fun invalidateAllSources()

    @Query(
        "UPDATE SourceLedgerData SET invalidationVersion = invalidationVersion + 1 WHERE sourceKey = :sourceKey"
    )
    suspend fun invalidateSource(sourceKey: String)
}

/** Bounded queries over only committed generations, with a legacy fallback during migration. */
@Dao
internal interface IncrementalLibraryReadDao {
    @Query(
        "SELECT id, stableUid, uri, displayPath, title, primaryArtistName, albumName, durationMs, embeddedArtworkRef, externalArtworkRef, available FROM (" +
            "SELECT song.id AS id, song.stableUid AS stableUid, song.uri AS uri, song.displayPath AS displayPath, song.title AS title, song.primaryArtistName AS primaryArtistName, song.albumName AS albumName, song.durationMs AS durationMs, song.artworkRef AS embeddedArtworkRef, NULL AS externalArtworkRef, source.available AS available, song.titleSort AS ordering FROM IndexedSongData song INNER JOIN SourceLedgerData source ON source.sourceKey = song.sourceKey AND source.lastCommittedGeneration = song.generation " +
            "UNION ALL SELECT legacy.id, legacy.stableUid, legacy.uri, legacy.displayPath, legacy.title, legacy.primaryArtistName, legacy.albumName, legacy.durationMs, legacy.embeddedArtworkRef, legacy.externalArtworkRef, legacy.available, legacy.titleSort FROM LibrarySongData legacy WHERE NOT EXISTS (SELECT 1 FROM IndexedUriStateData state WHERE state.uri = legacy.uri)) merged WHERE available = 1 ORDER BY ordering, id LIMIT :limit OFFSET :offset"
    )
    suspend fun songsPage(limit: Int, offset: Int): List<SongListRow>

    @Query(
        "SELECT id, stableUid, uri, displayPath, title, primaryArtistName, albumName, durationMs, embeddedArtworkRef, externalArtworkRef, available FROM (" +
            "SELECT song.id AS id, song.stableUid AS stableUid, song.uri AS uri, song.displayPath AS displayPath, song.title AS title, song.primaryArtistName AS primaryArtistName, song.albumName AS albumName, song.durationMs AS durationMs, song.artworkRef AS embeddedArtworkRef, NULL AS externalArtworkRef, source.available AS available, song.dateAddedMs AS ordering FROM IndexedSongData song INNER JOIN SourceLedgerData source ON source.sourceKey = song.sourceKey AND source.lastCommittedGeneration = song.generation " +
            "UNION ALL SELECT legacy.id, legacy.stableUid, legacy.uri, legacy.displayPath, legacy.title, legacy.primaryArtistName, legacy.albumName, legacy.durationMs, legacy.embeddedArtworkRef, legacy.externalArtworkRef, legacy.available, legacy.dateAddedMs FROM LibrarySongData legacy WHERE NOT EXISTS (SELECT 1 FROM IndexedUriStateData state WHERE state.uri = legacy.uri)) merged WHERE available = 1 ORDER BY ordering DESC, id DESC LIMIT :limit"
    )
    suspend fun recentlyAdded(limit: Int): List<SongListRow>

    @Query(
        "SELECT MIN(id) AS id, albumName AS title, MIN(albumSort) AS titleSort FROM (" +
            "SELECT song.id, song.albumName, COALESCE(song.albumSort, song.albumName) AS albumSort, source.available FROM IndexedSongData song INNER JOIN SourceLedgerData source ON source.sourceKey = song.sourceKey AND source.lastCommittedGeneration = song.generation " +
            "UNION ALL SELECT legacy.id, legacy.albumName, COALESCE(legacy.albumSort, legacy.albumName), legacy.available FROM LibrarySongData legacy WHERE NOT EXISTS (SELECT 1 FROM IndexedUriStateData state WHERE state.uri = legacy.uri)) merged WHERE available = 1 AND albumName IS NOT NULL GROUP BY albumName ORDER BY titleSort, id LIMIT :limit OFFSET :offset"
    )
    suspend fun albumsPage(limit: Int, offset: Int): List<AlbumListRow>

    @Query(
        "SELECT MIN(id) AS id, primaryArtistName AS name, MIN(primaryArtistSort) AS nameSort FROM (" +
            "SELECT song.id, song.primaryArtistName, COALESCE(song.primaryArtistSort, song.primaryArtistName) AS primaryArtistSort, source.available FROM IndexedSongData song INNER JOIN SourceLedgerData source ON source.sourceKey = song.sourceKey AND source.lastCommittedGeneration = song.generation " +
            "UNION ALL SELECT legacy.id, legacy.primaryArtistName, COALESCE(legacy.primaryArtistSort, legacy.primaryArtistName), legacy.available FROM LibrarySongData legacy WHERE NOT EXISTS (SELECT 1 FROM IndexedUriStateData state WHERE state.uri = legacy.uri)) merged WHERE available = 1 AND primaryArtistName IS NOT NULL GROUP BY primaryArtistName ORDER BY nameSort, id LIMIT :limit OFFSET :offset"
    )
    suspend fun artistsPage(limit: Int, offset: Int): List<ArtistListRow>

    @Query(
        "SELECT id, stableUid, uri, displayPath, title, primaryArtistName, albumName, durationMs, embeddedArtworkRef, externalArtworkRef, available FROM (" +
            "SELECT song.id AS id, song.stableUid AS stableUid, song.uri AS uri, song.displayPath AS displayPath, song.title AS title, song.primaryArtistName AS primaryArtistName, song.albumName AS albumName, song.durationMs AS durationMs, song.artworkRef AS embeddedArtworkRef, NULL AS externalArtworkRef, source.available AS available, song.titleSort AS titleSort, song.primaryArtistSort AS primaryArtistSort, song.albumSort AS albumSort FROM IndexedSongData song INNER JOIN SourceLedgerData source ON source.sourceKey = song.sourceKey AND source.lastCommittedGeneration = song.generation " +
            "UNION ALL SELECT legacy.id, legacy.stableUid, legacy.uri, legacy.displayPath, legacy.title, legacy.primaryArtistName, legacy.albumName, legacy.durationMs, legacy.embeddedArtworkRef, legacy.externalArtworkRef, legacy.available, legacy.titleSort, legacy.primaryArtistSort, legacy.albumSort FROM LibrarySongData legacy WHERE NOT EXISTS (SELECT 1 FROM IndexedUriStateData state WHERE state.uri = legacy.uri)) merged WHERE available = 1 AND (titleSort LIKE :pattern ESCAPE '\\' OR primaryArtistSort LIKE :pattern ESCAPE '\\' OR albumSort LIKE :pattern ESCAPE '\\') ORDER BY titleSort, id LIMIT :limit OFFSET :offset"
    )
    suspend fun searchSongs(pattern: String, limit: Int, offset: Int): List<SongListRow>

    @Query(
        "SELECT COUNT(*) FROM (SELECT song.uri FROM IndexedSongData song INNER JOIN SourceLedgerData source ON source.sourceKey = song.sourceKey AND source.lastCommittedGeneration = song.generation WHERE source.available = 1 UNION ALL SELECT legacy.uri FROM LibrarySongData legacy WHERE legacy.available = 1 AND NOT EXISTS (SELECT 1 FROM IndexedUriStateData state WHERE state.uri = legacy.uri))"
    )
    suspend fun songCount(): Int
}
