/*
 * Copyright (c) 2026 Auxio Project
 * BenchmarkFixtureReceiver.kt is part of Auxio.
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

package org.oxycblt.auxio.benchmark

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteStatement
import java.io.File
import java.util.Locale
import kotlin.concurrent.thread

/** Benchmark-build-only receiver that transactionally seeds committed bounded-library rows. */
class BenchmarkFixtureReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != ACTION_SEED) {
            resultCode = Activity.RESULT_CANCELED
            return
        }
        val songCount = intent.getIntExtra(EXTRA_SONG_COUNT, DEFAULT_SONG_COUNT)
        if (songCount !in SUPPORTED_SONG_COUNTS) {
            resultCode = Activity.RESULT_CANCELED
            resultData = "Unsupported fixture size: $songCount"
            return
        }

        val pending = goAsync()
        thread(name = "auxio-benchmark-fixture") {
            try {
                seed(context.applicationContext, songCount)
                pending.resultCode = Activity.RESULT_OK
                pending.resultData = "Seeded $songCount committed rows"
            } catch (error: Throwable) {
                pending.resultCode = Activity.RESULT_CANCELED
                pending.resultData = error.stackTraceToString().take(MAX_RESULT_LENGTH)
            } finally {
                pending.finish()
            }
        }
    }

    private fun seed(context: Context, songCount: Int) {
        val databaseFile = context.getDatabasePath(DATABASE_NAME)
        require(databaseFile.isFile) {
            "${databaseFile.path} does not exist; launch the benchmark target once before seeding"
        }
        SQLiteDatabase.openDatabase(
                databaseFile.path,
                null,
                SQLiteDatabase.OPEN_READWRITE or SQLiteDatabase.NO_LOCALIZED_COLLATORS,
            )
            .use { database ->
                database.beginTransaction()
                try {
                    database.execSQL("DELETE FROM IndexedUriStateData")
                    database.execSQL("DELETE FROM IndexedSongData")
                    database.execSQL("DELETE FROM SourceLedgerData")
                    database.execSQL("DELETE FROM LibrarySongData")
                    insertLedgers(database)
                    insertSongs(database, songCount)
                    database.setTransactionSuccessful()
                } finally {
                    database.endTransaction()
                }
            }
        context
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putInt(KEY_SONG_COUNT, songCount)
            .putInt(KEY_SCHEMA_VERSION, FIXTURE_SCHEMA_VERSION)
            .putLong(KEY_SEED, FIXTURE_SEED)
            .apply()
    }

    private fun insertLedgers(database: SQLiteDatabase) {
        val statement =
            database.compileStatement(
                "INSERT OR REPLACE INTO SourceLedgerData " +
                    "(sourceKey, sourceType, rootUri, rootPath, fingerprint, fingerprintStrength, " +
                    "available, lastSeenMs, lastCommittedGeneration, pendingGeneration, " +
                    "lastSuccessfulScanMs, configurationRevision, invalidationVersion, " +
                    "committedInvalidationVersion, committedProfile, enrichmentRevision, incomplete) " +
                    "VALUES (?, ?, NULL, ?, ?, ?, 1, ?, ?, NULL, ?, 1, 1, 1, ?, 1, 0)"
            )
        try {
            SOURCE_KEYS.forEachIndexed { index, sourceKey ->
                statement.clearBindings()
                statement.bindString(1, sourceKey)
                statement.bindString(2, SOURCE_TYPE)
                statement.bindString(3, "/storage/usbdisk$index")
                statement.bindString(4, "fixture:$FIXTURE_SCHEMA_VERSION:$FIXTURE_SEED:$index")
                statement.bindString(5, "STRONG")
                statement.bindLong(6, FIXTURE_EPOCH_MS)
                statement.bindLong(7, FIXTURE_GENERATION)
                statement.bindLong(8, FIXTURE_EPOCH_MS)
                statement.bindString(9, "LEAN")
                statement.executeInsert()
            }
        } finally {
            statement.close()
        }
    }

    private fun insertSongs(database: SQLiteDatabase, songCount: Int) {
        val songStatement =
            database.compileStatement(
                "INSERT INTO IndexedSongData " +
                    "(sourceKey, generation, stableUid, uri, displayPath, fileName, title, titleSort, " +
                    "primaryArtistName, primaryArtistSort, albumName, albumSort, trackNumber, " +
                    "discNumber, durationMs, sizeBytes, modifiedTimeMs, dateAddedMs, mimeType, " +
                    "artworkRef, metadataProfile, enrichmentRevision) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NULL, ?, ?)"
            )
        val stateStatement =
            database.compileStatement(
                "INSERT OR REPLACE INTO IndexedUriStateData " +
                    "(sourceKey, uri, available, lastGeneration, metadataProfile) " +
                    "VALUES (?, ?, 1, ?, ?)"
            )
        try {
            repeat(songCount) { index ->
                val row = fixtureRow(index, songCount)
                bindSong(songStatement, row)
                songStatement.executeInsert()
                stateStatement.clearBindings()
                stateStatement.bindString(1, row.sourceKey)
                stateStatement.bindString(2, row.uri)
                stateStatement.bindLong(3, FIXTURE_GENERATION)
                stateStatement.bindString(4, "LEAN")
                stateStatement.executeInsert()
            }
        } finally {
            songStatement.close()
            stateStatement.close()
        }
    }

    private fun bindSong(statement: SQLiteStatement, row: FixtureRow) {
        statement.clearBindings()
        statement.bindString(1, row.sourceKey)
        statement.bindLong(2, FIXTURE_GENERATION)
        statement.bindString(3, row.stableUid)
        statement.bindString(4, row.uri)
        statement.bindString(5, row.displayPath)
        statement.bindString(6, row.fileName)
        statement.bindString(7, row.title)
        statement.bindString(8, row.title.lowercase(Locale.ROOT))
        statement.bindString(9, row.artist)
        statement.bindString(10, row.artist.lowercase(Locale.ROOT))
        statement.bindString(11, row.album)
        statement.bindString(12, row.album.lowercase(Locale.ROOT))
        statement.bindLong(13, row.trackNumber.toLong())
        statement.bindLong(14, 1)
        statement.bindLong(15, row.durationMs)
        statement.bindLong(16, row.sizeBytes)
        statement.bindLong(17, row.modifiedTimeMs)
        statement.bindLong(18, row.dateAddedMs)
        statement.bindString(19, "audio/flac")
        statement.bindString(20, "LEAN")
        statement.bindLong(21, 1)
    }

    private fun fixtureRow(index: Int, songCount: Int): FixtureRow {
        val sourceIndex = index % SOURCE_KEYS.size
        val albumCount = maxOf(1, songCount / 10)
        val artistCount = maxOf(1, songCount / 25)
        val folderCount = maxOf(2, songCount / 50)
        val fileName = "track-${index.toString().padStart(5, '0')}.flac"
        val displayPath =
            "/storage/usbdisk$sourceIndex/Music/folder-${index % folderCount}/$fileName"
        return FixtureRow(
            sourceKey = SOURCE_KEYS[sourceIndex],
            stableUid = "fixture-$FIXTURE_SEED-$index",
            uri = "file://$displayPath",
            displayPath = displayPath,
            fileName = fileName,
            title = "Fixture Track ${index.toString().padStart(5, '0')}",
            artist = "Fixture Artist ${index % artistCount}",
            album = "Fixture Album ${index % albumCount}",
            trackNumber = (index % 99) + 1,
            durationMs = 120_000L + ((index * 997L) % 240_000L),
            sizeBytes = 4_000_000L + index,
            modifiedTimeMs = FIXTURE_EPOCH_MS + index * 1_000L,
            dateAddedMs = FIXTURE_EPOCH_MS + index * 1_000L,
        )
    }

    private data class FixtureRow(
        val sourceKey: String,
        val stableUid: String,
        val uri: String,
        val displayPath: String,
        val fileName: String,
        val title: String,
        val artist: String,
        val album: String,
        val trackNumber: Int,
        val durationMs: Long,
        val sizeBytes: Long,
        val modifiedTimeMs: Long,
        val dateAddedMs: Long,
    )

    companion object {
        const val ACTION_SEED = "org.oxycblt.auxio.action.SEED_BENCHMARK_FIXTURE"
        const val EXTRA_SONG_COUNT = "song_count"
        private const val DATABASE_NAME = "music_cache.db"
        private const val PREFS_NAME = "auxio_benchmark_fixture"
        private const val KEY_SONG_COUNT = "song_count"
        private const val KEY_SCHEMA_VERSION = "schema_version"
        private const val KEY_SEED = "seed"
        private const val SOURCE_TYPE = "BENCHMARK_FIXTURE"
        private const val FIXTURE_SCHEMA_VERSION = 1
        private const val FIXTURE_SEED = 18_022_026L
        private const val FIXTURE_GENERATION = 1L
        private const val FIXTURE_EPOCH_MS = 1_700_000_000_000L
        private const val DEFAULT_SONG_COUNT = 5_000
        private const val MAX_RESULT_LENGTH = 8_000
        private val SUPPORTED_SONG_COUNTS = setOf(500, 5_000, 20_000)
        private val SOURCE_KEYS = listOf("direct:usb0", "direct:usb1")
    }
}
