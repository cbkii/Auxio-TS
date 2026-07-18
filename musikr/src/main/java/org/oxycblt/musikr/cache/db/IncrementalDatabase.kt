/*
 * Copyright (c) 2026 Auxio Project
 * IncrementalDatabase.kt is part of Auxio.
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
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import org.oxycblt.musikr.cache.StartupAlbumRow
import org.oxycblt.musikr.cache.StartupArtistRow
import org.oxycblt.musikr.cache.StartupFolderRow
import org.oxycblt.musikr.cache.StartupSongRow
import org.oxycblt.musikr.cache.StartupSummaryRow

/** Durable observation and committed-generation state for one independent source. */
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
    fun observed(snapshot: org.oxycblt.musikr.fs.SourceSnapshot): SourceLedgerData =
        copy(
            sourceType = snapshot.sourceType,
            rootUri = snapshot.rootUri,
            rootPath = snapshot.rootPath,
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

/** Changed cache metadata staged until the source generation commits. */
@Entity(
    primaryKeys = ["scanId", "sourceKey", "uri"],
    indices = [Index(value = ["scanId", "sourceKey"])],
)
internal data class PendingCachedFileData(
    val scanId: String,
    val sourceKey: String,
    val uri: String,
    val modifiedMs: Long,
    val addedMs: Long,
    val mimeType: String?,
    val properties: org.oxycblt.musikr.metadata.Properties?,
    val tags: org.oxycblt.musikr.tag.parse.ParsedTags?,
    val embeddedArtworkRef: String?,
)

/** Committed lightweight song projection for bounded readers. */
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

    @Query("DELETE FROM ScanSeenData WHERE sourceKey = :sourceKey")
    suspend fun deleteSeenForSource(sourceKey: String)

    @Query("DELETE FROM PendingCachedFileData WHERE scanId = :scanId")
    suspend fun deletePending(scanId: String)

    @Query("DELETE FROM PendingCachedFileData WHERE sourceKey = :sourceKey")
    suspend fun deletePendingForSource(sourceKey: String)

    @Query(
        "SELECT cache.*, song.sourceKey AS sourceKey, song.displayPath AS committedDisplayPath, song.sizeBytes AS committedSizeBytes, source.rootUri AS committedSourceUri, source.rootPath AS committedRootPath FROM CachedFileData cache INNER JOIN IndexedSongData song ON song.uri = cache.uri INNER JOIN SourceLedgerData source ON source.sourceKey = song.sourceKey AND source.lastCommittedGeneration = song.generation WHERE song.sourceKey IN (:sourceKeys) ORDER BY cache.uri LIMIT :limit OFFSET :offset"
    )
    suspend fun committedCachedPage(
        sourceKeys: Set<String>,
        limit: Int,
        offset: Int,
    ): List<CommittedCachedRow>

    @Query(
        "SELECT * FROM (" +
            "SELECT cache.*, song.sourceKey AS sourceKey, song.displayPath AS committedDisplayPath, song.sizeBytes AS committedSizeBytes, source.rootUri AS committedSourceUri, source.rootPath AS committedRootPath FROM CachedFileData cache INNER JOIN IndexedSongData song ON song.uri = cache.uri INNER JOIN SourceLedgerData source ON source.sourceKey = song.sourceKey AND source.lastCommittedGeneration = song.generation WHERE source.available = 1 " +
            "UNION ALL SELECT cache.*, 'legacy:' || cache.uri AS sourceKey, cache.uri AS committedDisplayPath, 0 AS committedSizeBytes, NULL AS committedSourceUri, NULL AS committedRootPath FROM CachedFileData cache WHERE NOT EXISTS (SELECT 1 FROM IndexedUriStateData state WHERE state.uri = cache.uri)) ORDER BY uri LIMIT :limit OFFSET :offset"
    )
    suspend fun compatibilityCachedPage(limit: Int, offset: Int): List<CommittedCachedRow>

    @Query("SELECT * FROM IndexedUriStateData WHERE sourceKey = :sourceKey AND uri = :uri LIMIT 1")
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
    suspend fun startupSongs(limit: Int, offset: Int): List<StartupSongRow>

    @Query(
        "SELECT id, stableUid, uri, displayPath, title, primaryArtistName, albumName, durationMs, embeddedArtworkRef, externalArtworkRef, available FROM (" +
            "SELECT song.id AS id, song.stableUid AS stableUid, song.uri AS uri, song.displayPath AS displayPath, song.title AS title, song.primaryArtistName AS primaryArtistName, song.albumName AS albumName, song.durationMs AS durationMs, song.artworkRef AS embeddedArtworkRef, NULL AS externalArtworkRef, source.available AS available, song.dateAddedMs AS ordering FROM IndexedSongData song INNER JOIN SourceLedgerData source ON source.sourceKey = song.sourceKey AND source.lastCommittedGeneration = song.generation " +
            "UNION ALL SELECT legacy.id, legacy.stableUid, legacy.uri, legacy.displayPath, legacy.title, legacy.primaryArtistName, legacy.albumName, legacy.durationMs, legacy.embeddedArtworkRef, legacy.externalArtworkRef, legacy.available, legacy.dateAddedMs FROM LibrarySongData legacy WHERE NOT EXISTS (SELECT 1 FROM IndexedUriStateData state WHERE state.uri = legacy.uri)) merged WHERE available = 1 ORDER BY ordering DESC, id LIMIT :limit OFFSET :offset"
    )
    suspend fun recentlyAdded(limit: Int, offset: Int): List<StartupSongRow>

    @Query(
        "SELECT id, stableUid, uri, displayPath, title, primaryArtistName, albumName, durationMs, embeddedArtworkRef, externalArtworkRef, available FROM (" +
            "SELECT song.id AS id, song.stableUid AS stableUid, song.uri AS uri, song.displayPath AS displayPath, song.title AS title, song.primaryArtistName AS primaryArtistName, song.albumName AS albumName, song.durationMs AS durationMs, song.artworkRef AS embeddedArtworkRef, NULL AS externalArtworkRef, source.available AS available, song.titleSort AS ordering FROM IndexedSongData song INNER JOIN SourceLedgerData source ON source.sourceKey = song.sourceKey AND source.lastCommittedGeneration = song.generation " +
            "UNION ALL SELECT legacy.id, legacy.stableUid, legacy.uri, legacy.displayPath, legacy.title, legacy.primaryArtistName, legacy.albumName, legacy.durationMs, legacy.embeddedArtworkRef, legacy.externalArtworkRef, legacy.available, legacy.titleSort FROM LibrarySongData legacy WHERE NOT EXISTS (SELECT 1 FROM IndexedUriStateData state WHERE state.uri = legacy.uri)) merged WHERE available = 1 AND (title LIKE '%' || :likeQuery || '%' OR primaryArtistName LIKE '%' || :likeQuery || '%' OR albumName LIKE '%' || :likeQuery || '%' OR displayPath LIKE '%' || :likeQuery || '%') ORDER BY ordering, id LIMIT :limit OFFSET :offset"
    )
    suspend fun quickFindSongs(likeQuery: String, limit: Int, offset: Int): List<StartupSongRow>

    @Query(
        "SELECT ROW_NUMBER() OVER (ORDER BY albumSort, albumName) AS syntheticId, albumName AS name, primaryArtistName, MIN(artworkRef) AS artworkRef, COUNT(*) AS songCount, MAX(dateAddedMs) AS dateAddedMs FROM IndexedSongData song INNER JOIN SourceLedgerData source ON source.sourceKey = song.sourceKey AND source.lastCommittedGeneration = song.generation WHERE source.available = 1 AND albumName IS NOT NULL GROUP BY albumSort, albumName, primaryArtistName ORDER BY albumSort, albumName LIMIT :limit OFFSET :offset"
    )
    suspend fun startupAlbums(limit: Int, offset: Int): List<StartupAlbumRow>

    @Query(
        "SELECT ROW_NUMBER() OVER (ORDER BY primaryArtistSort, primaryArtistName) AS syntheticId, primaryArtistName AS name, MIN(artworkRef) AS artworkRef, COUNT(*) AS songCount FROM IndexedSongData song INNER JOIN SourceLedgerData source ON source.sourceKey = song.sourceKey AND source.lastCommittedGeneration = song.generation WHERE source.available = 1 AND primaryArtistName IS NOT NULL GROUP BY primaryArtistSort, primaryArtistName ORDER BY primaryArtistSort, primaryArtistName LIMIT :limit OFFSET :offset"
    )
    suspend fun startupArtists(limit: Int, offset: Int): List<StartupArtistRow>

    @Query(
        "SELECT MIN(id) AS syntheticId, COALESCE(NULLIF(SUBSTR(displayPath, 1, LENGTH(displayPath) - LENGTH(fileName) - 1), ''), '/') AS displayPath, COUNT(*) AS songCount, MAX(dateAddedMs) AS dateAddedMs FROM IndexedSongData song INNER JOIN SourceLedgerData source ON source.sourceKey = song.sourceKey AND source.lastCommittedGeneration = song.generation WHERE source.available = 1 GROUP BY displayPath ORDER BY displayPath LIMIT :limit OFFSET :offset"
    )
    suspend fun startupFolders(limit: Int, offset: Int): List<StartupFolderRow>

    @Query(
        "SELECT COUNT(*) AS songCount, COUNT(DISTINCT albumName) AS albumCount, COUNT(DISTINCT primaryArtistName) AS artistCount FROM IndexedSongData song INNER JOIN SourceLedgerData source ON source.sourceKey = song.sourceKey AND source.lastCommittedGeneration = song.generation WHERE source.available = 1"
    )
    suspend fun summary(): StartupSummaryRow

    @Query("SELECT COUNT(*) FROM IndexedSongData") suspend fun songCount(): Int
}
