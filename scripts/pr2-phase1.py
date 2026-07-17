#!/usr/bin/env python3
from pathlib import Path


def replace(path: str, old: str, new: str, count: int = 1) -> None:
    p = Path(path)
    text = p.read_text()
    if old not in text:
        raise SystemExit(f"missing patch anchor in {path}: {old[:120]!r}")
    p.write_text(text.replace(old, new, count))


cache_db = "musikr/src/main/java/org/oxycblt/musikr/cache/db/CacheDatabase.kt"
replace(
    cache_db,
    """            SourceStateData::class,\n            MetadataRevisionData::class,""",
    """            SourceStateData::class,\n            SourceLedgerData::class,\n            SourceScanGenerationData::class,\n            ScanSeenData::class,\n            PendingCachedFileData::class,\n            IndexedSongData::class,\n            IndexedUriStateData::class,\n            MetadataRevisionData::class,""",
)
replace(cache_db, "version = 71,", "version = 72,")
replace(
    cache_db,
    """    abstract fun libraryDao(): LibraryReadDao\n\n    abstract fun backfillDao(): LibraryBackfillDao""",
    """    abstract fun libraryDao(): LibraryReadDao\n\n    abstract fun incrementalLibraryDao(): IncrementalLibraryReadDao\n\n    abstract fun incrementalDao(): IncrementalScanDao\n\n    abstract fun backfillDao(): LibraryBackfillDao""",
)
replace(
    cache_db,
    """        fun from(context: Context) =\n            Room.databaseBuilder(""",
    """        val MIGRATION_71_72 =\n            Migration(71, 72) { database ->\n                database.execSQL(\n                    \"CREATE TABLE IF NOT EXISTS `SourceLedgerData` (`sourceKey` TEXT NOT NULL, `sourceType` TEXT NOT NULL, `rootUri` TEXT, `rootPath` TEXT, `fingerprint` TEXT, `fingerprintStrength` TEXT NOT NULL, `available` INTEGER NOT NULL, `lastSeenMs` INTEGER NOT NULL, `lastCommittedGeneration` INTEGER, `pendingGeneration` INTEGER, `lastSuccessfulScanMs` INTEGER, `configurationRevision` INTEGER NOT NULL, `invalidationVersion` INTEGER NOT NULL, `committedInvalidationVersion` INTEGER NOT NULL, `committedProfile` TEXT, `enrichmentRevision` INTEGER NOT NULL, `incomplete` INTEGER NOT NULL, PRIMARY KEY(`sourceKey`))\"\n                )\n                database.execSQL(\n                    \"CREATE TABLE IF NOT EXISTS `SourceScanGenerationData` (`scanId` TEXT NOT NULL, `sourceKey` TEXT NOT NULL, `generation` INTEGER NOT NULL, `state` TEXT NOT NULL, `startedAtMs` INTEGER NOT NULL, `completedAtMs` INTEGER, `error` TEXT, PRIMARY KEY(`scanId`, `sourceKey`))\"\n                )\n                database.execSQL(\n                    \"CREATE TABLE IF NOT EXISTS `ScanSeenData` (`scanId` TEXT NOT NULL, `sourceKey` TEXT NOT NULL, `uri` TEXT NOT NULL, `displayPath` TEXT, `fileName` TEXT NOT NULL, `sizeBytes` INTEGER NOT NULL, `modifiedTimeMs` INTEGER NOT NULL, `dateAddedMs` INTEGER NOT NULL, `mimeType` TEXT, `title` TEXT NOT NULL, `titleSort` TEXT NOT NULL, `primaryArtistName` TEXT, `primaryArtistSort` TEXT, `albumName` TEXT, `albumSort` TEXT, `trackNumber` INTEGER, `discNumber` INTEGER, `durationMs` INTEGER, `artworkRef` TEXT, `metadataProfile` TEXT NOT NULL, PRIMARY KEY(`scanId`, `sourceKey`, `uri`))\"\n                )\n                database.execSQL(\n                    \"CREATE INDEX IF NOT EXISTS `index_ScanSeenData_scanId_sourceKey` ON `ScanSeenData` (`scanId`, `sourceKey`)\"\n                )\n                database.execSQL(\n                    \"CREATE INDEX IF NOT EXISTS `index_ScanSeenData_uri` ON `ScanSeenData` (`uri`)\"\n                )\n                database.execSQL(\n                    \"CREATE TABLE IF NOT EXISTS `PendingCachedFileData` (`scanId` TEXT NOT NULL, `sourceKey` TEXT NOT NULL, `uri` TEXT NOT NULL, `modifiedMs` INTEGER NOT NULL, `addedMs` INTEGER NOT NULL, `mimeType` TEXT, `durationMs` INTEGER, `bitrateKbps` INTEGER, `sampleRateHz` INTEGER, `musicBrainzId` TEXT, `name` TEXT, `sortName` TEXT, `track` INTEGER, `disc` INTEGER, `subtitle` TEXT, `date` TEXT, `albumMusicBrainzId` TEXT, `albumName` TEXT, `albumSortName` TEXT, `releaseTypes` TEXT, `artistMusicBrainzIds` TEXT, `artistNames` TEXT, `artistSortNames` TEXT, `albumArtistMusicBrainzIds` TEXT, `albumArtistNames` TEXT, `albumArtistSortNames` TEXT, `genreNames` TEXT, `replayGainTrackAdjustment` REAL, `replayGainAlbumAdjustment` REAL, `coverId` TEXT, PRIMARY KEY(`scanId`, `sourceKey`, `uri`))\"\n                )\n                database.execSQL(\n                    \"CREATE INDEX IF NOT EXISTS `index_PendingCachedFileData_scanId_sourceKey` ON `PendingCachedFileData` (`scanId`, `sourceKey`)\"\n                )\n                database.execSQL(\n                    \"CREATE TABLE IF NOT EXISTS `IndexedSongData` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `sourceKey` TEXT NOT NULL, `generation` INTEGER NOT NULL, `stableUid` TEXT NOT NULL, `uri` TEXT NOT NULL, `displayPath` TEXT, `fileName` TEXT NOT NULL, `title` TEXT NOT NULL, `titleSort` TEXT NOT NULL, `primaryArtistName` TEXT, `primaryArtistSort` TEXT, `albumName` TEXT, `albumSort` TEXT, `trackNumber` INTEGER, `discNumber` INTEGER, `durationMs` INTEGER, `sizeBytes` INTEGER NOT NULL, `modifiedTimeMs` INTEGER NOT NULL, `dateAddedMs` INTEGER NOT NULL, `mimeType` TEXT, `artworkRef` TEXT, `metadataProfile` TEXT NOT NULL, `enrichmentRevision` INTEGER NOT NULL)\"\n                )\n                database.execSQL(\n                    \"CREATE UNIQUE INDEX IF NOT EXISTS `index_IndexedSongData_sourceKey_generation_uri` ON `IndexedSongData` (`sourceKey`, `generation`, `uri`)\"\n                )\n                database.execSQL(\n                    \"CREATE INDEX IF NOT EXISTS `index_IndexedSongData_sourceKey_generation_titleSort` ON `IndexedSongData` (`sourceKey`, `generation`, `titleSort`)\"\n                )\n                database.execSQL(\n                    \"CREATE INDEX IF NOT EXISTS `index_IndexedSongData_stableUid` ON `IndexedSongData` (`stableUid`)\"\n                )\n                database.execSQL(\n                    \"CREATE INDEX IF NOT EXISTS `index_IndexedSongData_uri` ON `IndexedSongData` (`uri`)\"\n                )\n                database.execSQL(\n                    \"CREATE INDEX IF NOT EXISTS `index_IndexedSongData_dateAddedMs` ON `IndexedSongData` (`dateAddedMs`)\"\n                )\n                database.execSQL(\n                    \"CREATE INDEX IF NOT EXISTS `index_IndexedSongData_primaryArtistSort` ON `IndexedSongData` (`primaryArtistSort`)\"\n                )\n                database.execSQL(\n                    \"CREATE INDEX IF NOT EXISTS `index_IndexedSongData_albumSort` ON `IndexedSongData` (`albumSort`)\"\n                )\n                database.execSQL(\n                    \"CREATE TABLE IF NOT EXISTS `IndexedUriStateData` (`sourceKey` TEXT NOT NULL, `uri` TEXT NOT NULL, `available` INTEGER NOT NULL, `lastGeneration` INTEGER NOT NULL, `metadataProfile` TEXT NOT NULL, PRIMARY KEY(`sourceKey`, `uri`))\"\n                )\n                database.execSQL(\n                    \"CREATE INDEX IF NOT EXISTS `index_IndexedUriStateData_uri` ON `IndexedUriStateData` (`uri`)\"\n                )\n            }\n\n        fun from(context: Context) =\n            Room.databaseBuilder(""",
)
replace(
    cache_db,
    ".addMigrations(MIGRATION_70_71)",
    ".addMigrations(MIGRATION_70_71, MIGRATION_71_72)",
)
replace(
    cache_db,
    """    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun updateSong(data: CachedFileData)\n\n    @Transaction""",
    """    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun updateSong(data: CachedFileData)\n\n    @Insert(onConflict = OnConflictStrategy.REPLACE)\n    suspend fun updateSongs(data: List<CachedFileData>)\n\n    @Transaction""",
)

incremental_db = "musikr/src/main/java/org/oxycblt/musikr/cache/db/IncrementalDatabase.kt"
replace(
    incremental_db,
    "import androidx.room.Entity\n",
    "import androidx.room.Embedded\nimport androidx.room.Entity\n",
)
replace(
    incremental_db,
    """@Dao\ninternal interface IncrementalScanDao {""",
    """internal data class CommittedCachedRow(\n    @Embedded val cache: CachedFileData,\n    val sourceKey: String,\n    val committedDisplayPath: String?,\n    val committedSizeBytes: Long,\n    val committedSourceUri: String?,\n    val committedRootPath: String?,\n)\n\n@Dao\ninternal interface IncrementalScanDao {""",
)
replace(
    incremental_db,
    """        \"SELECT cache.* FROM CachedFileData cache INNER JOIN IndexedSongData song ON song.uri = cache.uri INNER JOIN SourceLedgerData source ON source.sourceKey = song.sourceKey AND source.lastCommittedGeneration = song.generation WHERE song.sourceKey IN (:sourceKeys) ORDER BY cache.uri LIMIT :limit OFFSET :offset\"\n    )\n    suspend fun committedCachedPage(\n        sourceKeys: Set<String>,\n        limit: Int,\n        offset: Int,\n    ): List<CachedFileData>""",
    """        \"SELECT cache.*, song.sourceKey AS sourceKey, song.displayPath AS committedDisplayPath, song.sizeBytes AS committedSizeBytes, source.rootUri AS committedSourceUri, source.rootPath AS committedRootPath FROM CachedFileData cache INNER JOIN IndexedSongData song ON song.uri = cache.uri INNER JOIN SourceLedgerData source ON source.sourceKey = song.sourceKey AND source.lastCommittedGeneration = song.generation WHERE song.sourceKey IN (:sourceKeys) ORDER BY cache.uri LIMIT :limit OFFSET :offset\"\n    )\n    suspend fun committedCachedPage(\n        sourceKeys: Set<String>,\n        limit: Int,\n        offset: Int,\n    ): List<CommittedCachedRow>""",
)

migration_test = "musikr/src/test/java/org/oxycblt/musikr/cache/db/CacheMigrationAndBackfillTest.kt"
replace(
    migration_test,
    ".addMigrations(CacheDatabase.MIGRATION_70_71)",
    ".addMigrations(CacheDatabase.MIGRATION_70_71, CacheDatabase.MIGRATION_71_72)",
)
replace(
    migration_test,
    '                "MetadataRevisionData",\n',
    '                "MetadataRevisionData",\n                "SourceLedgerData",\n                "SourceScanGenerationData",\n                "ScanSeenData",\n                "PendingCachedFileData",\n                "IndexedSongData",\n                "IndexedUriStateData",\n',
)

# Remove this one-shot patch from the final branch diff.
Path("scripts/pr2-phase1.py").unlink()
Path(".github/workflows/pr2-phase1.yml").unlink()
