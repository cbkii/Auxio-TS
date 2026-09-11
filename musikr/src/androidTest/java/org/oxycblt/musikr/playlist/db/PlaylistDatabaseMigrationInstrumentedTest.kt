/*
 * Copyright (c) 2026 Auxio Project
 * PlaylistDatabaseMigrationInstrumentedTest.kt is part of Auxio.
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

package org.oxycblt.musikr.playlist.db

import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 29, maxSdkVersion = 29)
class PlaylistDatabaseMigrationInstrumentedTest {
    private lateinit var helper: SupportSQLiteOpenHelper

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        context.deleteDatabase(DB_NAME)
        helper =
            FrameworkSQLiteOpenHelperFactory().create(
                SupportSQLiteOpenHelper.Configuration.builder(context)
                    .name(DB_NAME)
                    .callback(
                        object : SupportSQLiteOpenHelper.Callback(30) {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                db.execSQL(
                                    "CREATE TABLE PlaylistInfo " +
                                        "(playlistUid TEXT NOT NULL PRIMARY KEY, name TEXT NOT NULL)"
                                )
                                db.execSQL(
                                    "CREATE TABLE PlaylistSongCrossRef " +
                                        "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                                        "playlistUid TEXT NOT NULL, songUid TEXT NOT NULL)"
                                )
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
        helper.writableDatabase
    }

    @After
    fun tearDown() {
        helper.close()
        InstrumentationRegistry.getInstrumentation().targetContext.deleteDatabase(DB_NAME)
    }

    @Test
    fun migrate30To31CanonicalizesLegacyPlaylistUidsAndPreservesRows() {
        val db = helper.writableDatabase
        db.execSQL(
            "INSERT INTO PlaylistInfo (playlistUid, name) VALUES (?, ?)",
            arrayOf(LEGACY_PLAYLIST_UID, "Legacy playlist"),
        )
        db.execSQL(
            "INSERT INTO PlaylistInfo (playlistUid, name) VALUES (?, ?)",
            arrayOf(CANONICAL_PLAYLIST_UID_2, "Canonical playlist"),
        )
        db.execSQL(
            "INSERT INTO PlaylistSongCrossRef (playlistUid, songUid) VALUES (?, ?)",
            arrayOf(LEGACY_PLAYLIST_UID, CANONICAL_SONG_UID),
        )
        db.execSQL(
            "INSERT INTO PlaylistSongCrossRef (playlistUid, songUid) VALUES (?, ?)",
            arrayOf(CANONICAL_PLAYLIST_UID_2, CANONICAL_SONG_UID),
        )

        PlaylistDatabase.MIGRATION_30_31.migrate(db)

        db.query("SELECT playlistUid, name FROM PlaylistInfo ORDER BY name").use { cursor ->
            assertTrue(cursor.moveToFirst())
            assertEquals(CANONICAL_PLAYLIST_UID_2, cursor.getString(0))
            assertEquals("Canonical playlist", cursor.getString(1))
            assertTrue(cursor.moveToNext())
            assertEquals(CANONICAL_PLAYLIST_UID_1, cursor.getString(0))
            assertEquals("Legacy playlist", cursor.getString(1))
            assertEquals(2, cursor.count)
        }
        db.query("SELECT playlistUid, songUid FROM PlaylistSongCrossRef ORDER BY id").use { cursor ->
            assertTrue(cursor.moveToFirst())
            assertEquals(CANONICAL_PLAYLIST_UID_1, cursor.getString(0))
            assertEquals(CANONICAL_SONG_UID, cursor.getString(1))
            assertTrue(cursor.moveToNext())
            assertEquals(CANONICAL_PLAYLIST_UID_2, cursor.getString(0))
            assertEquals(CANONICAL_SONG_UID, cursor.getString(1))
            assertEquals(2, cursor.count)
        }
    }

    @Test
    fun migrate30To31RejectsMalformedUidBeforeRewritingRows() {
        val db = helper.writableDatabase
        db.execSQL(
            "INSERT INTO PlaylistInfo (playlistUid, name) VALUES (?, ?)",
            arrayOf("not-a-valid-uid", "Broken playlist"),
        )

        try {
            PlaylistDatabase.MIGRATION_30_31.migrate(db)
            fail("Expected malformed playlist UID to abort migration")
        } catch (expected: IllegalArgumentException) {
            assertTrue(expected.message.orEmpty().contains("invalid UID"))
        }

        db.query("SELECT playlistUid, name FROM PlaylistInfo").use { cursor ->
            assertTrue(cursor.moveToFirst())
            assertEquals("not-a-valid-uid", cursor.getString(0))
            assertEquals("Broken playlist", cursor.getString(1))
            assertEquals(1, cursor.count)
        }
    }

    private companion object {
        const val DB_NAME = "playlist-uid-migration.db"
        const val LEGACY_PLAYLIST_UID =
            "org.oxycblt.auxio:a107-00000000-0000-0000-0000-000000000001"
        const val CANONICAL_PLAYLIST_UID_1 = "uap00000000-0000-0000-0000-000000000001"
        const val CANONICAL_PLAYLIST_UID_2 = "uap00000000-0000-0000-0000-000000000002"
        const val CANONICAL_SONG_UID = "uas00000000-0000-0000-0000-000000000010"
    }
}
