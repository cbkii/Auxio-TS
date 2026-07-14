/*
 * Copyright (c) 2026 Auxio Project
 * CacheMigrationAndBackfillTest.kt is part of Auxio.
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
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Executable Room migration and backfill coverage for the cache database.
 *
 * A real v70 database file is populated with legacy [CachedFileData] rows and then opened with Room
 * at version 71 through [CacheDatabase.MIGRATION_70_71]. Room validates every entity against the
 * migrated schema on open, giving genuine schema-equality verification between the entity
 * declarations and the migration SQL, rather than source-text inspection.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
class CacheMigrationAndBackfillTest {
    private lateinit var context: Context
    private var db: CacheDatabase? = null

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        context.deleteDatabase(DB_NAME)
    }

    @After
    fun tearDown() {
        db?.close()
        context.deleteDatabase(DB_NAME)
    }

    /** Create a legacy v70 database file with only the flat cache table. */
    private fun createLegacyDatabase(populate: (SupportSQLiteDatabase) -> Unit) {
        val helper =
            FrameworkSQLiteOpenHelperFactory()
                .create(
                    SupportSQLiteOpenHelper.Configuration.builder(context)
                        .name(DB_NAME)
                        .callback(
                            object : SupportSQLiteOpenHelper.Callback(70) {
                                override fun onCreate(db: SupportSQLiteDatabase) {
                                    db.execSQL(LEGACY_CACHED_FILE_DATA_DDL)
                                }

                                override fun onUpgrade(
                                    db: SupportSQLiteDatabase,
                                    oldVersion: Int,
                                    newVersion: Int,
                                ) = Unit
                            }
                        )
                        .build()
                )
        populate(helper.writableDatabase)
        helper.close()
    }

    private fun openMigrated(): CacheDatabase {
        val opened =
            Room.databaseBuilder(context, CacheDatabase::class.java, DB_NAME)
                .addMigrations(CacheDatabase.MIGRATION_70_71)
                .allowMainThreadQueries()
                .build()
        // Force open so the migration and Room's schema validation actually run.
        opened.openHelper.writableDatabase
        db = opened
        return opened
    }

    private fun insertLegacyRow(
        db: SupportSQLiteDatabase,
        uri: String,
        name: String? = "Song",
        albumName: String? = "Album",
        artistNames: String? = "Artist",
        genreNames: String? = "Genre",
    ) {
        fun sql(value: String?) = value?.let { "'${it.replace("'", "''")}'" } ?: "NULL"
        db.execSQL(
            "INSERT INTO CachedFileData (uri, modifiedMs, addedMs, mimeType, durationMs, " +
                "bitrateKbps, sampleRateHz, musicBrainzId, name, sortName, track, disc, " +
                "subtitle, date, albumMusicBrainzId, albumName, albumSortName, releaseTypes, " +
                "artistMusicBrainzIds, artistNames, artistSortNames, albumArtistMusicBrainzIds, " +
                "albumArtistNames, albumArtistSortNames, genreNames, " +
                "replayGainTrackAdjustment, replayGainAlbumAdjustment, coverId) VALUES (" +
                "'$uri', 1000, 2000, 'audio/mpeg', 180000, 320, 44100, NULL, ${sql(name)}, " +
                "${sql(name)}, 1, 1, NULL, NULL, NULL, ${sql(albumName)}, ${sql(albumName)}, " +
                "'', '', ${sql(artistNames)}, ${sql(artistNames)}, '', '', '', " +
                "${sql(genreNames)}, NULL, NULL, NULL)"
        )
    }

    @Test
    fun `migration preserves legacy rows with full and partial metadata`() = runBlocking {
        createLegacyDatabase { legacy ->
            insertLegacyRow(legacy, "content://media/1", name = "Full Song")
            // Partial metadata: no tags at all, only file state.
            legacy.execSQL(
                "INSERT INTO CachedFileData (uri, modifiedMs, addedMs, mimeType, durationMs, " +
                    "bitrateKbps, sampleRateHz, musicBrainzId, name, sortName, track, disc, " +
                    "subtitle, date, albumMusicBrainzId, albumName, albumSortName, " +
                    "releaseTypes, artistMusicBrainzIds, artistNames, artistSortNames, " +
                    "albumArtistMusicBrainzIds, albumArtistNames, albumArtistSortNames, " +
                    "genreNames, replayGainTrackAdjustment, replayGainAlbumAdjustment, " +
                    "coverId) VALUES ('content://media/2', 1000, 2000, NULL, NULL, NULL, " +
                    "NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, " +
                    "NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL)"
            )
        }
        val migrated = openMigrated()

        val all = migrated.readDao().selectAllSongs()
        assertEquals(2, all.size)
        val full = all.first { it.uri.toString() == "content://media/1" }
        assertEquals("Full Song", full.name)
        val partial = all.first { it.uri.toString() == "content://media/2" }
        assertNull(partial.name)
        assertNull(partial.mimeType)
    }

    @Test
    fun `migration creates normalized tables and expected indexes`() = runBlocking {
        createLegacyDatabase {}
        val migrated = openMigrated()
        val support = migrated.openHelper.writableDatabase

        val tables = mutableSetOf<String>()
        support.query("SELECT name FROM sqlite_master WHERE type = 'table'").use {
            while (it.moveToNext()) tables += it.getString(0)
        }
        listOf(
                "CachedFileData",
                "LibraryVolumeData",
                "LibrarySongData",
                "LibraryAlbumData",
                "LibraryArtistData",
                "LibraryGenreData",
                "SongArtistCrossRefData",
                "SongGenreCrossRefData",
                "AlbumArtistCrossRefData",
                "LibraryPlaylistData",
                "PlaylistItemData",
                "ScanGenerationData",
                "SourceStateData",
                "MetadataRevisionData",
            )
            .forEach { assertTrue("missing table $it", it in tables) }

        val indexes = mutableSetOf<String>()
        support.query("SELECT name FROM sqlite_master WHERE type = 'index'").use {
            while (it.moveToNext()) indexes += it.getString(0)
        }
        listOf(
                "index_LibrarySongData_uri",
                "index_LibrarySongData_volumeId_relativePath",
                "index_LibrarySongData_stableUid",
                "index_LibrarySongData_titleSort",
                "index_LibrarySongData_albumId_discNumber_trackNumber",
                "index_LibrarySongData_primaryArtistSort",
                "index_LibrarySongData_available",
                "index_SongArtistCrossRefData_artistId",
                "index_SongGenreCrossRefData_genreId",
                "index_AlbumArtistCrossRefData_artistId",
                "index_PlaylistItemData_songId",
            )
            .forEach { assertTrue("missing index $it", it in indexes) }
        // Redundant composite-primary-key-prefix indexes must not exist.
        listOf(
                "index_SongArtistCrossRefData_songId_artistId",
                "index_SongGenreCrossRefData_songId_genreId",
                "index_PlaylistItemData_playlistId_position",
            )
            .forEach { assertTrue("unexpected redundant index $it", it !in indexes) }
    }

    @Test
    fun `backfill populates normalized model and reruns are idempotent`() = runBlocking {
        createLegacyDatabase { legacy ->
            insertLegacyRow(legacy, "content://media/1", "Alpha", "Album A", "Artist X", "Rock")
            insertLegacyRow(legacy, "content://media/2", "Beta", "Album A", "Artist X", "Rock")
            insertLegacyRow(legacy, "content://media/3", "Gamma", "Album B", "Artist Y", null)
        }
        val migrated = openMigrated()
        val backfill = LibraryBackfill(migrated)

        assertEquals(3, backfill.remaining())
        assertEquals(3, backfill.run())
        assertEquals(0, backfill.remaining())
        assertEquals(3, migrated.libraryDao().songCount())
        assertEquals(2, migrated.libraryDao().albumsPage(10, 0).size)
        assertEquals(2, migrated.libraryDao().artistsPage(10, 0).size)

        // Rerun must be a no-op and must not duplicate any rows.
        assertEquals(0, backfill.run())
        assertEquals(3, migrated.libraryDao().songCount())
        assertEquals(2, migrated.libraryDao().albumsPage(10, 0).size)
    }

    @Test
    fun `interrupted backfill resumes safely without duplicates`() = runBlocking {
        createLegacyDatabase { legacy ->
            (1..5).forEach { insertLegacyRow(legacy, "content://media/$it", "Song $it") }
        }
        val migrated = openMigrated()
        val backfill = LibraryBackfill(migrated)

        // Simulate cancellation/process death after the first committed batch.
        try {
            backfill.run(batchSize = 2) { throw CancellationException("interrupted") }
        } catch (e: CancellationException) {
            // Expected.
        }
        val committed = migrated.libraryDao().songCount()
        assertEquals(2, committed)
        // Legacy data remains fully intact while backfill is incomplete.
        assertEquals(5, migrated.readDao().selectAllSongs().size)

        // Resume processes only the remainder; totals match with no duplicates.
        assertEquals(3, backfill.run(batchSize = 2))
        assertEquals(5, migrated.libraryDao().songCount())
        assertEquals(0, backfill.remaining())
    }

    @Test
    fun `backfill handles thousands of records in bounded batches`() = runBlocking {
        createLegacyDatabase { legacy ->
            legacy.execSQL("BEGIN TRANSACTION")
            (1..2000).forEach {
                insertLegacyRow(
                    legacy,
                    "content://media/$it",
                    "Song $it",
                    "Album ${it % 40}",
                    "Artist ${it % 25}",
                )
            }
            legacy.execSQL("COMMIT")
        }
        val migrated = openMigrated()
        val backfill = LibraryBackfill(migrated)

        var lastProgress = 0
        assertEquals(2000, backfill.run { lastProgress = it })
        assertEquals(2000, lastProgress)
        assertEquals(2000, migrated.libraryDao().songCount())
        assertEquals(40, migrated.libraryDao().albumsPage(100, 0).size)
        assertEquals(25, migrated.libraryDao().artistsPage(100, 0).size)
    }

    @Test
    fun `point lookup uses single-row query after migration`() = runBlocking {
        createLegacyDatabase { legacy -> insertLegacyRow(legacy, "content://media/1", "Alpha") }
        val migrated = openMigrated()
        val row = migrated.readDao().selectSongByUri(android.net.Uri.parse("content://media/1"))
        assertNotNull(row)
        assertEquals("Alpha", requireNotNull(row).name)
        assertNull(migrated.readDao().selectSongByUri(android.net.Uri.parse("content://media/9")))
    }

    private companion object {
        const val DB_NAME = "test_music_cache.db"

        /** The exact flat-cache schema Room generated for version 70. */
        const val LEGACY_CACHED_FILE_DATA_DDL =
            "CREATE TABLE IF NOT EXISTS `CachedFileData` (`uri` TEXT NOT NULL, " +
                "`modifiedMs` INTEGER NOT NULL, `addedMs` INTEGER NOT NULL, `mimeType` TEXT, " +
                "`durationMs` INTEGER, `bitrateKbps` INTEGER, `sampleRateHz` INTEGER, " +
                "`musicBrainzId` TEXT, `name` TEXT, `sortName` TEXT, `track` INTEGER, " +
                "`disc` INTEGER, `subtitle` TEXT, `date` TEXT, `albumMusicBrainzId` TEXT, " +
                "`albumName` TEXT, `albumSortName` TEXT, `releaseTypes` TEXT, " +
                "`artistMusicBrainzIds` TEXT, `artistNames` TEXT, `artistSortNames` TEXT, " +
                "`albumArtistMusicBrainzIds` TEXT, `albumArtistNames` TEXT, " +
                "`albumArtistSortNames` TEXT, `genreNames` TEXT, " +
                "`replayGainTrackAdjustment` REAL, `replayGainAlbumAdjustment` REAL, " +
                "`coverId` TEXT, PRIMARY KEY(`uri`))"
    }
}
