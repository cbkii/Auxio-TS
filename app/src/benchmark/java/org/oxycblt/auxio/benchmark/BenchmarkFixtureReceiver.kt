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
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteStatement
import android.util.Base64
import androidx.preference.PreferenceManager
import androidx.room.Room
import androidx.room.withTransaction
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.Locale
import kotlin.concurrent.thread
import kotlin.math.min
import kotlinx.coroutines.runBlocking
import org.oxycblt.auxio.IntegerTable
import org.oxycblt.auxio.R
import org.oxycblt.auxio.headunit.ts18.FastStartDirectFolderBrowser
import org.oxycblt.auxio.music.LibraryState
import org.oxycblt.auxio.playback.persist.PersistenceDatabase
import org.oxycblt.auxio.playback.persist.QueueItemRefEntity
import org.oxycblt.auxio.playback.persist.QueueSessionEntity
import org.oxycblt.auxio.playback.state.RepeatMode
import org.oxycblt.auxio.playback.state.ShuffleScope
import org.oxycblt.auxio.util.StartupPerformanceReport

/** Benchmark-build-only authority for deterministic fixtures and bounded timing export. */
class BenchmarkFixtureReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            ACTION_SEED -> seedAsync(context, intent)
            ACTION_REPORT -> exportReport(context)
            else -> {
                resultCode = Activity.RESULT_CANCELED
                resultData = "Unsupported benchmark action"
            }
        }
    }

    private fun seedAsync(context: Context, intent: Intent) {
        val songCount = intent.getIntExtra(EXTRA_SONG_COUNT, DEFAULT_SONG_COUNT)
        val sourceMode = intent.getStringExtra(EXTRA_SOURCE_MODE) ?: SOURCE_MODE_NORMAL
        if (songCount !in SUPPORTED_SONG_COUNTS || sourceMode !in SUPPORTED_SOURCE_MODES) {
            resultCode = Activity.RESULT_CANCELED
            resultData = "Unsupported fixture request: songs=$songCount sourceMode=$sourceMode"
            return
        }

        val pending = goAsync()
        thread(name = "auxio-benchmark-fixture") {
            try {
                seed(context.applicationContext, songCount, sourceMode)
                pending.resultCode = Activity.RESULT_OK
                pending.resultData =
                    "Seeded $songCount committed rows and primitive queue ($sourceMode)"
            } catch (error: Throwable) {
                pending.resultCode = Activity.RESULT_CANCELED
                pending.resultData = error.stackTraceToString().take(MAX_RESULT_LENGTH)
            } finally {
                pending.finish()
            }
        }
    }

    private fun exportReport(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val report =
            StartupPerformanceReport.render(
                StartupPerformanceReport.CaptureContext(
                    authority = "benchmark-ordered-broadcast",
                    sourceState = prefs.getString(KEY_SOURCE_MODE, "not-seeded") ?: "not-seeded",
                    fixtureSongCount = prefs.getInt(KEY_SONG_COUNT, 0).takeIf { it > 0 },
                )
            )
        resultCode = Activity.RESULT_OK
        resultData = Base64.encodeToString(report.toByteArray(Charsets.UTF_8), Base64.NO_WRAP)
    }

    private fun seed(context: Context, songCount: Int, sourceMode: String) {
        val databaseFile = context.getDatabasePath(DATABASE_NAME)
        require(databaseFile.isFile) {
            "${databaseFile.path} does not exist; launch the benchmark target once before seeding"
        }
        val playableFiles = preparePlayableFixtures(context, sourceMode)
        SQLiteDatabase.openDatabase(
                databaseFile.path,
                null,
                SQLiteDatabase.OPEN_READWRITE or SQLiteDatabase.NO_LOCALIZED_COLLATORS,
            )
            .use { database ->
                database.beginTransaction()
                try {
                    clearFixtureTables(database)
                    insertLedgers(database, sourceMode)
                    insertRepresentativeLibraryRows(database, songCount, playableFiles, sourceMode)
                    insertSongs(database, songCount, playableFiles, sourceMode)
                    database.setTransactionSuccessful()
                } finally {
                    database.endTransaction()
                }
            }
        seedPlaybackQueue(context, songCount, playableFiles)
        seedBenchmarkStartupPreferences(context)
        context
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putInt(KEY_SONG_COUNT, songCount)
            .putInt(KEY_SCHEMA_VERSION, FIXTURE_SCHEMA_VERSION)
            .putLong(KEY_SEED, FIXTURE_SEED)
            .putString(KEY_SOURCE_MODE, sourceMode)
            .commit()
    }

    private fun seedPlaybackQueue(context: Context, songCount: Int, playableFiles: Map<Int, File>) {
        val physicalFiles = playableFiles.values.toList()
        require(physicalFiles.isNotEmpty()) {
            "At least one playable benchmark fixture is required"
        }
        val database =
            Room.databaseBuilder(context, PersistenceDatabase::class.java, PLAYBACK_DATABASE_NAME)
                .addMigrations(
                    PersistenceDatabase.MIGRATION_27_32,
                    PersistenceDatabase.MIGRATION_32_38,
                    PersistenceDatabase.MIGRATION_38_39,
                    PersistenceDatabase.MIGRATION_39_40,
                )
                .build()
        try {
            runBlocking {
                database.withTransaction {
                    val playbackStateDao = database.playbackStateDao()
                    val queueDao = database.queueDao()
                    playbackStateDao.nukeState()
                    queueDao.nukeHeap()
                    queueDao.nukeShuffledMapping()
                    queueDao.nukeQueueItemRefs()
                    queueDao.nukeQueueSessions()

                    val anchor = PLAYBACK_ANCHOR_INDEX.coerceIn(0, songCount - 1)
                    queueDao.insertQueueSession(
                        QueueSessionEntity(
                            id = PLAYBACK_SESSION_ID,
                            currentLogicalPosition = anchor,
                            positionMs = 0L,
                            repeatMode = RepeatMode.NONE,
                            shuffleScope = ShuffleScope.OFF,
                            totalCount = songCount,
                            revision = FIXTURE_GENERATION,
                            updatedAtMs = FIXTURE_EPOCH_MS,
                        )
                    )

                    for (start in 0 until songCount step QUEUE_INSERT_BATCH_SIZE) {
                        val end = min(songCount, start + QUEUE_INSERT_BATCH_SIZE)
                        val items =
                            (start until end).map { logicalPosition ->
                                val row = fixtureRow(logicalPosition, songCount, playableFiles)
                                val physicalFile =
                                    physicalFiles[logicalPosition % physicalFiles.size]
                                QueueItemRefEntity(
                                    sessionId = PLAYBACK_SESSION_ID,
                                    logicalPosition = logicalPosition,
                                    canonicalPosition = logicalPosition,
                                    stableSongUid = null,
                                    uri = physicalFile.toURI().toString(),
                                    pathFallback = row.displayPath,
                                    titleFallback = row.title,
                                    artistFallback = row.artist,
                                    albumFallback = row.album,
                                    durationMs = WAVE_DURATION_SECONDS * 1_000L,
                                )
                            }
                        queueDao.insertQueueItemRefs(items)
                    }
                }
            }
        } finally {
            database.close()
        }
    }

    private fun seedBenchmarkStartupPreferences(context: Context) {
        check(
            PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(
                    context.getString(R.string.set_key_library_revision),
                    FIXTURE_LIBRARY_REVISION,
                )
                .putString(
                    context.getString(R.string.set_key_library_state),
                    LibraryState.USABLE.name,
                )
                .putBoolean(context.getString(R.string.set_key_library_last_scan_failed), false)
                .putInt(
                    context.getString(R.string.set_key_locations_mode),
                    IntegerTable.LOCATION_MODE_DIRECT_FS,
                )
                .putString(
                    context.getString(R.string.set_key_music_locations),
                    "file:///storage/usbdisk0;file:///storage/usbdisk1",
                )
                .commit()
        ) {
            "Unable to persist deterministic benchmark startup settings"
        }
    }

    private fun clearFixtureTables(database: SQLiteDatabase) {
        listOf(
                "PlaylistItemData",
                "SongArtistCrossRefData",
                "SongGenreCrossRefData",
                "AlbumArtistCrossRefData",
                "LibraryPlaylistData",
                "LibraryGenreData",
                "LibraryArtistData",
                "LibraryAlbumData",
                "LibrarySongData",
                "LibraryVolumeData",
                "IndexedUriStateData",
                "IndexedSongData",
                "SourceScanGenerationData",
                "ScanSeenData",
                "PendingCachedFileData",
                "SourceLedgerData",
            )
            .forEach { table -> database.execSQL("DELETE FROM $table") }
    }

    private fun insertLedgers(database: SQLiteDatabase, sourceMode: String) {
        SOURCE_KEYS.forEachIndexed { index, sourceKey ->
            val available = !(sourceMode == SOURCE_MODE_USB1_ABSENT && index == 1)
            val pending = sourceMode == SOURCE_MODE_PENDING && index == 0
            val values =
                ContentValues().apply {
                    put("sourceKey", sourceKey)
                    put("sourceType", SOURCE_TYPE)
                    putNull("rootUri")
                    put("rootPath", "/storage/usbdisk$index")
                    put("fingerprint", "fixture:$FIXTURE_SCHEMA_VERSION:$FIXTURE_SEED:$index")
                    put("fingerprintStrength", "AUTHORITATIVE")
                    put("available", available)
                    put("lastSeenMs", FIXTURE_EPOCH_MS)
                    put("lastCommittedGeneration", FIXTURE_GENERATION)
                    if (pending) put("pendingGeneration", FIXTURE_GENERATION + 1)
                    else putNull("pendingGeneration")
                    put("lastSuccessfulScanMs", FIXTURE_EPOCH_MS)
                    put("configurationRevision", 1)
                    put("invalidationVersion", 1)
                    put("committedInvalidationVersion", 1)
                    put("committedProfile", "LEAN")
                    put("enrichmentRevision", 1)
                    put("incomplete", pending)
                }
            check(database.insertOrThrow("SourceLedgerData", null, values) != -1L)
        }
    }

    private fun insertRepresentativeLibraryRows(
        database: SQLiteDatabase,
        songCount: Int,
        playableFiles: Map<Int, File>,
        sourceMode: String,
    ) {
        SOURCE_KEYS.forEachIndexed { index, sourceKey ->
            val available = sourceMode != SOURCE_MODE_USB1_ABSENT || index != 1
            val pending = sourceMode == SOURCE_MODE_PENDING && index == 0
            val values =
                ContentValues().apply {
                    put("stableSourceKey", sourceKey)
                    put("displayName", "USB $index")
                    put("sourceType", SOURCE_TYPE)
                    putNull("rootUri")
                    put("rootPath", "/storage/usbdisk$index")
                    put("available", available)
                    put("lastCommittedGeneration", FIXTURE_GENERATION)
                    if (pending) put("pendingGeneration", FIXTURE_GENERATION + 1)
                    else putNull("pendingGeneration")
                    put("lastSuccessfulScanMs", FIXTURE_EPOCH_MS)
                    put("lastSeenMs", FIXTURE_EPOCH_MS)
                    put("configurationRevision", 1)
                }
            database.insertOrThrow("LibraryVolumeData", null, values)
        }

        val albumCount = min(2_000, maxOf(1, songCount / 10))
        repeat(albumCount) { index ->
            insertNamedLibraryRow(database, "LibraryAlbumData", "title", "Fixture Album $index")
        }
        val artistCount = min(1_000, maxOf(1, songCount / 25))
        repeat(artistCount) { index ->
            insertNamedLibraryRow(database, "LibraryArtistData", "name", "Fixture Artist $index")
        }
        repeat(GENRE_COUNT) { index ->
            insertNamedLibraryRow(database, "LibraryGenreData", "name", "Fixture Genre $index")
        }
        repeat(PLAYLIST_COUNT) { index ->
            val values =
                ContentValues().apply {
                    put("stableUid", "fixture-playlist-$index")
                    put("name", "Fixture Playlist $index")
                    put("nameSort", "fixture playlist ${index.toString().padStart(2, '0')}")
                    putNull("sourceUri")
                    put("scanGeneration", FIXTURE_GENERATION)
                    put("metadataRevision", 1)
                    put("available", 1)
                }
            val playlistId = database.insertOrThrow("LibraryPlaylistData", null, values)
            repeat(PLAYLIST_ITEM_COUNT) { position ->
                val songIndex = (index * PLAYLIST_ITEM_COUNT + position) % songCount
                val row = fixtureRow(songIndex, songCount, playableFiles)
                database.insertOrThrow(
                    "PlaylistItemData",
                    null,
                    ContentValues().apply {
                        put("playlistId", playlistId)
                        put("position", position)
                        putNull("songId")
                        put("stableSongUid", row.stableUid)
                        put("uri", row.uri)
                        put("titleFallback", row.title)
                    },
                )
            }
        }
    }

    private fun insertNamedLibraryRow(
        database: SQLiteDatabase,
        table: String,
        nameColumn: String,
        value: String,
    ) {
        val sortColumn = if (nameColumn == "title") "titleSort" else "nameSort"
        database.insertOrThrow(
            table,
            null,
            ContentValues().apply {
                put(nameColumn, value)
                put(sortColumn, value.lowercase(Locale.ROOT))
                put("scanGeneration", FIXTURE_GENERATION)
                put("metadataRevision", 1)
                put("available", 1)
            },
        )
    }

    private fun insertSongs(
        database: SQLiteDatabase,
        songCount: Int,
        playableFiles: Map<Int, File>,
        sourceMode: String,
    ) {
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
                    "VALUES (?, ?, ?, ?, ?)"
            )
        try {
            repeat(songCount) { index ->
                val row = fixtureRow(index, songCount, playableFiles)
                val available =
                    sourceMode != SOURCE_MODE_USB1_ABSENT || row.sourceKey != SOURCE_KEYS[1]
                bindSong(songStatement, row)
                songStatement.executeInsert()
                stateStatement.clearBindings()
                stateStatement.bindString(1, row.sourceKey)
                stateStatement.bindString(2, row.uri)
                stateStatement.bindLong(3, if (available) 1 else 0)
                stateStatement.bindLong(4, FIXTURE_GENERATION)
                stateStatement.bindString(5, "LEAN")
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
        statement.bindString(19, row.mimeType)
        statement.bindString(20, "LEAN")
        statement.bindLong(21, 1)
    }

    private fun fixtureRow(index: Int, songCount: Int, playableFiles: Map<Int, File>): FixtureRow {
        val sourceIndex = index % SOURCE_KEYS.size
        val albumCount = maxOf(1, songCount / 10)
        val artistCount = maxOf(1, songCount / 25)
        val folderCount = maxOf(2, songCount / 50)
        val playableFile = playableFiles[index]
        val fileName = playableFile?.name ?: "track-${index.toString().padStart(5, '0')}.flac"
        val displayPath =
            if (playableFile != null) {
                "/storage/usbdisk$sourceIndex/$fileName"
            } else {
                "/storage/usbdisk$sourceIndex/Music/folder-${index % folderCount}/$fileName"
            }
        return FixtureRow(
            sourceKey = SOURCE_KEYS[sourceIndex],
            stableUid = "fixture-$FIXTURE_SEED-$index",
            uri = playableFile?.toURI()?.toString() ?: "file://$displayPath",
            displayPath = displayPath,
            fileName = fileName,
            title = "Fixture Track ${index.toString().padStart(5, '0')}",
            artist = "Fixture Artist ${index % artistCount}",
            album = "Fixture Album ${index % albumCount}",
            trackNumber = (index % 99) + 1,
            durationMs = 120_000L + ((index * 997L) % 240_000L),
            sizeBytes = playableFile?.length() ?: (4_000_000L + index),
            modifiedTimeMs = FIXTURE_EPOCH_MS + index * 1_000L,
            dateAddedMs = FIXTURE_EPOCH_MS + index * 1_000L,
            mimeType = if (playableFile != null) "audio/wav" else "audio/flac",
        )
    }

    private fun preparePlayableFixtures(context: Context, sourceMode: String): Map<Int, File> {
        val files = linkedMapOf<Int, File>()
        SOURCE_KEYS.indices.forEach { sourceIndex ->
            val root = FastStartDirectFolderBrowser.benchmarkRoot(context, sourceIndex)
            if (sourceMode == SOURCE_MODE_USB1_ABSENT && sourceIndex == 1) {
                check(!root.exists() || root.deleteRecursively()) {
                    "Unable to remove unavailable fixture root ${root.absolutePath}"
                }
                return@forEach
            }
            check(root.mkdirs() || root.isDirectory) { "Unable to create ${root.absolutePath}" }
            root.listFiles()?.forEach { child ->
                if (child.name.startsWith("benchmark-tone-")) child.delete()
            }
            val index = PLAYABLE_INDICES[sourceIndex]
            val file = File(root, "benchmark-tone-$sourceIndex.wav")
            writeSilenceWave(file)
            files[index] = file
        }
        return files
    }

    private fun writeSilenceWave(file: File) {
        val sampleCount = WAVE_SAMPLE_RATE * WAVE_DURATION_SECONDS
        val dataSize = sampleCount * 2
        val buffer = ByteBuffer.allocate(44 + dataSize).order(ByteOrder.LITTLE_ENDIAN)
        buffer.put("RIFF".toByteArray(Charsets.US_ASCII))
        buffer.putInt(36 + dataSize)
        buffer.put("WAVE".toByteArray(Charsets.US_ASCII))
        buffer.put("fmt ".toByteArray(Charsets.US_ASCII))
        buffer.putInt(16)
        buffer.putShort(1.toShort())
        buffer.putShort(1.toShort())
        buffer.putInt(WAVE_SAMPLE_RATE)
        buffer.putInt(WAVE_SAMPLE_RATE * 2)
        buffer.putShort(2.toShort())
        buffer.putShort(16.toShort())
        buffer.put("data".toByteArray(Charsets.US_ASCII))
        buffer.putInt(dataSize)
        repeat(sampleCount) { buffer.putShort(0.toShort()) }
        file.writeBytes(buffer.array())
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
        val mimeType: String,
    )

    companion object {
        const val ACTION_SEED = "org.oxycblt.auxio.action.SEED_BENCHMARK_FIXTURE"
        const val ACTION_REPORT = "org.oxycblt.auxio.action.EXPORT_BENCHMARK_REPORT"
        const val EXTRA_SONG_COUNT = "song_count"
        const val EXTRA_SOURCE_MODE = "source_mode"
        const val SOURCE_MODE_NORMAL = "normal"
        const val SOURCE_MODE_USB1_ABSENT = "usb1_absent"
        const val SOURCE_MODE_PENDING = "pending_generation"
        private const val DATABASE_NAME = "music_cache.db"
        private const val PLAYBACK_DATABASE_NAME = "playback_persistence.db"
        private const val PREFS_NAME = "auxio_benchmark_fixture"
        private const val KEY_SONG_COUNT = "song_count"
        private const val KEY_SCHEMA_VERSION = "schema_version"
        private const val KEY_SEED = "seed"
        private const val KEY_SOURCE_MODE = "source_mode"
        private const val SOURCE_TYPE = "BENCHMARK_FIXTURE"
        private const val FIXTURE_SCHEMA_VERSION = 2
        private const val FIXTURE_SEED = 18_022_026L
        private const val FIXTURE_GENERATION = 1L
        private const val FIXTURE_LIBRARY_REVISION = "00000000-0000-0000-0000-000000000002"
        private const val FIXTURE_EPOCH_MS = 1_700_000_000_000L
        private const val PLAYBACK_SESSION_ID = 1L
        private const val PLAYBACK_ANCHOR_INDEX = 10
        private const val QUEUE_INSERT_BATCH_SIZE = 500
        private const val DEFAULT_SONG_COUNT = 5_000
        private const val MAX_RESULT_LENGTH = 8_000
        private const val GENRE_COUNT = 20
        private const val PLAYLIST_COUNT = 12
        private const val PLAYLIST_ITEM_COUNT = 20
        private const val WAVE_SAMPLE_RATE = 8_000
        private const val WAVE_DURATION_SECONDS = 10
        private val SUPPORTED_SONG_COUNTS = setOf(500, 5_000, 20_000)
        private val SUPPORTED_SOURCE_MODES =
            setOf(SOURCE_MODE_NORMAL, SOURCE_MODE_USB1_ABSENT, SOURCE_MODE_PENDING)
        private val SOURCE_KEYS = listOf("direct:usb0", "direct:usb1")
        private val PLAYABLE_INDICES = listOf(10, 11)
    }
}
