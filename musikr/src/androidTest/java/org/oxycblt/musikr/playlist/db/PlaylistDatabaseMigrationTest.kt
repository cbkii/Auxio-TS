/*
 * Copyright (c) 2026 Auxio Project
 * PlaylistDatabaseMigrationTest.kt is part of Auxio.
 */

package org.oxycblt.musikr.playlist.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.oxycblt.musikr.Music

@RunWith(AndroidJUnit4::class)
class PlaylistDatabaseMigrationTest {
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        context.deleteDatabase(DB_NAME)
    }

    @After
    fun tearDown() {
        context.deleteDatabase(DB_NAME)
    }

    @Test
    fun legacyUidDatabaseMigratesAndCrudTargetsCanonicalRows() = runBlocking {
        createV30Database(
            playlistRows = listOf(LEGACY_PLAYLIST to "Road Trip"),
            songRows = listOf(LEGACY_SONG_1),
            refs = listOf(Triple(1L, LEGACY_PLAYLIST, LEGACY_SONG_1)),
        )

        val database = PlaylistDatabase.from(context)
        try {
            val dao = database.playlistDao()
            val migrated = dao.readRawPlaylists().single()
            assertEquals(CANONICAL_PLAYLIST, migrated.playlistInfo.playlistUid.toString())
            assertEquals(listOf(CANONICAL_SONG_1), migrated.songs.map { it.songUid.toString() })
            assertEquals(CANONICAL_PLAYLIST, scalar(database, "SELECT playlistUid FROM PlaylistInfo"))
            assertEquals(CANONICAL_SONG_1, scalar(database, "SELECT songUid FROM PlaylistSong"))

            dao.replacePlaylistInfo(migrated.playlistInfo.copy(name = "Renamed"))
            assertEquals("Renamed", scalar(database, "SELECT name FROM PlaylistInfo"))

            val secondSong = requireNotNull(Music.UID.fromString(LEGACY_SONG_2))
            dao.insertPlaylistSongs(
                migrated.playlistInfo.playlistUid,
                listOf(PlaylistSong(secondSong)),
            )
            assertEquals(2L, count(database, "SELECT COUNT(*) FROM PlaylistSongCrossRef"))

            dao.replacePlaylistSongs(
                migrated.playlistInfo.playlistUid,
                listOf(PlaylistSong(secondSong)),
            )
            assertEquals(1L, count(database, "SELECT COUNT(*) FROM PlaylistSongCrossRef"))
            assertEquals(
                CANONICAL_SONG_2,
                scalar(database, "SELECT songUid FROM PlaylistSongCrossRef"),
            )

            dao.deletePlaylist(migrated.playlistInfo.playlistUid)
            assertEquals(0L, count(database, "SELECT COUNT(*) FROM PlaylistInfo"))
            assertEquals(0L, count(database, "SELECT COUNT(*) FROM PlaylistSongCrossRef"))
        } finally {
            database.close()
        }
    }

    @Test
    fun mixedLegacyAndCanonicalRowsMergeWithoutDroppingPlaylistContents() = runBlocking {
        createV30Database(
            playlistRows =
                listOf(
                    LEGACY_PLAYLIST to "Original name",
                    CANONICAL_PLAYLIST to "Renamed name",
                ),
            songRows = listOf(LEGACY_SONG_1, CANONICAL_SONG_1),
            refs =
                listOf(
                    Triple(1L, LEGACY_PLAYLIST, LEGACY_SONG_1),
                    Triple(2L, CANONICAL_PLAYLIST, CANONICAL_SONG_1),
                ),
        )

        val database = PlaylistDatabase.from(context)
        try {
            val playlists = database.playlistDao().readRawPlaylists()
            assertEquals(1, playlists.size)
            assertEquals("Renamed name", playlists.single().playlistInfo.name)
            assertEquals(CANONICAL_PLAYLIST, playlists.single().playlistInfo.playlistUid.toString())
            assertEquals(1L, count(database, "SELECT COUNT(*) FROM PlaylistInfo"))
            assertEquals(1L, count(database, "SELECT COUNT(*) FROM PlaylistSong"))
            // Cross-reference rows are intentionally not deduplicated: duplicate songs can be
            // meaningful user playlist content and migration must not silently discard them.
            assertEquals(2L, count(database, "SELECT COUNT(*) FROM PlaylistSongCrossRef"))
            assertEquals(
                0L,
                count(
                    database,
                    "SELECT COUNT(*) FROM PlaylistSongCrossRef " +
                        "WHERE playlistUid != '$CANONICAL_PLAYLIST' OR songUid != '$CANONICAL_SONG_1'",
                ),
            )
        } finally {
            database.close()
        }
    }

    @Test
    fun malformedUidAbortsWithoutDestroyingUserDatabase() {
        createV30Database(
            playlistRows = listOf("not-a-valid-music-uid" to "Keep me"),
            songRows = listOf(LEGACY_SONG_1),
            refs = listOf(Triple(1L, "not-a-valid-music-uid", LEGACY_SONG_1)),
        )

        val database = PlaylistDatabase.from(context)
        try {
            assertThrows(IllegalStateException::class.java) {
                database.openHelper.writableDatabase
            }
        } finally {
            database.close()
        }

        SQLiteDatabase.openDatabase(
                context.getDatabasePath(DB_NAME).path,
                null,
                SQLiteDatabase.OPEN_READONLY,
            )
            .use { raw ->
                assertEquals(30, raw.version)
                raw.rawQuery("SELECT playlistUid, name FROM PlaylistInfo", null).use { cursor ->
                    assertTrue(cursor.moveToFirst())
                    assertEquals("not-a-valid-music-uid", cursor.getString(0))
                    assertEquals("Keep me", cursor.getString(1))
                }
            }
    }

    private fun createV30Database(
        playlistRows: List<Pair<String, String>>,
        songRows: List<String>,
        refs: List<Triple<Long, String, String>>,
    ) {
        val file = context.getDatabasePath(DB_NAME)
        file.parentFile?.mkdirs()
        SQLiteDatabase.openOrCreateDatabase(file, null).use { db ->
            db.execSQL(
                "CREATE TABLE IF NOT EXISTS PlaylistInfo " +
                    "(playlistUid TEXT NOT NULL PRIMARY KEY, name TEXT NOT NULL)"
            )
            db.execSQL(
                "CREATE TABLE IF NOT EXISTS PlaylistSong " +
                    "(songUid TEXT NOT NULL PRIMARY KEY)"
            )
            db.execSQL(
                "CREATE TABLE IF NOT EXISTS PlaylistSongCrossRef " +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "playlistUid TEXT NOT NULL, songUid TEXT NOT NULL)"
            )
            db.execSQL(
                "CREATE INDEX IF NOT EXISTS index_PlaylistSongCrossRef_playlistUid " +
                    "ON PlaylistSongCrossRef (playlistUid)"
            )
            db.execSQL(
                "CREATE INDEX IF NOT EXISTS index_PlaylistSongCrossRef_songUid " +
                    "ON PlaylistSongCrossRef (songUid)"
            )
            playlistRows.forEach { (uid, name) ->
                db.execSQL(
                    "INSERT INTO PlaylistInfo(playlistUid, name) VALUES(?, ?)",
                    arrayOf(uid, name),
                )
            }
            songRows.forEach { uid ->
                db.execSQL("INSERT INTO PlaylistSong(songUid) VALUES(?)", arrayOf(uid))
            }
            refs.forEach { (id, playlistUid, songUid) ->
                db.execSQL(
                    "INSERT INTO PlaylistSongCrossRef(id, playlistUid, songUid) VALUES(?, ?, ?)",
                    arrayOf(id, playlistUid, songUid),
                )
            }
            db.version = 30
        }
    }

    private fun scalar(database: PlaylistDatabase, sql: String): String =
        database.openHelper.writableDatabase.query(sql).use { cursor ->
            assertTrue(cursor.moveToFirst())
            cursor.getString(0)
        }

    private fun count(database: PlaylistDatabase, sql: String): Long =
        database.openHelper.writableDatabase.query(sql).use { cursor ->
            assertTrue(cursor.moveToFirst())
            cursor.getLong(0)
        }

    private companion object {
        const val DB_NAME = "user_music.db"
        const val UUID_PLAYLIST = "00000000-0000-0000-0000-000000000001"
        const val UUID_SONG_1 = "00000000-0000-0000-0000-000000000101"
        const val UUID_SONG_2 = "00000000-0000-0000-0000-000000000102"
        const val LEGACY_PLAYLIST = "org.oxycblt.auxio:a107-$UUID_PLAYLIST"
        const val LEGACY_SONG_1 = "org.oxycblt.auxio:a10b-$UUID_SONG_1"
        const val LEGACY_SONG_2 = "org.oxycblt.auxio:a10b-$UUID_SONG_2"
        val CANONICAL_PLAYLIST = requireNotNull(Music.UID.fromString(LEGACY_PLAYLIST)).toString()
        val CANONICAL_SONG_1 = requireNotNull(Music.UID.fromString(LEGACY_SONG_1)).toString()
        val CANONICAL_SONG_2 = requireNotNull(Music.UID.fromString(LEGACY_SONG_2)).toString()
    }
}
