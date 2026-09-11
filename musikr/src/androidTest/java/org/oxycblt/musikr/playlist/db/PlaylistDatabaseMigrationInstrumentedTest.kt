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

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 29, maxSdkVersion = 29)
class PlaylistDatabaseMigrationInstrumentedTest {
    @Test
    fun migrate30To31CanonicalizesPlaylistUidsWithoutDroppingMalformedRows() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        context.deleteDatabase(DB_NAME)
        val helper = createVersion30Database(context)
        try {
            val db = helper.writableDatabase
            val uuid = "12345678-1234-1234-1234-123456789abc"
            val secondUuid = "87654321-4321-4321-4321-cba987654321"
            val legacy = "org.oxycblt.auxio:a107-$uuid"
            val canonical = "uap$uuid"
            val otherLegacy = "org.oxycblt.auxio:a107-$secondUuid"
            val otherCanonical = "uap$secondUuid"
            val malformed = "not-a-valid-uid"
            val song = "uas11111111-2222-3333-4444-555555555555"

            db.execSQL(
                "INSERT INTO PlaylistInfo (playlistUid, name) VALUES (?, ?)",
                arrayOf<Any?>(legacy, "Legacy duplicate"),
            )
            db.execSQL(
                "INSERT INTO PlaylistInfo (playlistUid, name) VALUES (?, ?)",
                arrayOf<Any?>(canonical, "Canonical wins"),
            )
            db.execSQL(
                "INSERT INTO PlaylistInfo (playlistUid, name) VALUES (?, ?)",
                arrayOf<Any?>(otherLegacy, "Legacy only"),
            )
            db.execSQL(
                "INSERT INTO PlaylistInfo (playlistUid, name) VALUES (?, ?)",
                arrayOf<Any?>(malformed, "Malformed retained"),
            )
            db.execSQL("INSERT INTO PlaylistSong (songUid) VALUES (?)", arrayOf<Any?>(song))
            for (playlistUid in listOf(legacy, canonical, otherLegacy, malformed)) {
                db.execSQL(
                    "INSERT INTO PlaylistSongCrossRef (playlistUid, songUid) VALUES (?, ?)",
                    arrayOf<Any?>(playlistUid, song),
                )
            }

            PlaylistDatabase.MIGRATION_30_31.migrate(db)

            val info = mutableMapOf<String, String>()
            db.query("SELECT playlistUid, name FROM PlaylistInfo").use { cursor ->
                while (cursor.moveToNext()) {
                    info[cursor.getString(0)] = cursor.getString(1)
                }
            }
            assertEquals("Canonical wins", info[canonical])
            assertEquals("Legacy only", info[otherCanonical])
            assertEquals("Malformed retained", info[malformed])
            assertEquals(3, info.size)

            val crossRefs = mutableListOf<String>()
            db.query("SELECT playlistUid FROM PlaylistSongCrossRef ORDER BY id").use { cursor ->
                while (cursor.moveToNext()) crossRefs += cursor.getString(0)
            }
            assertEquals(listOf(canonical, otherCanonical, malformed), crossRefs)
            assertTrue(crossRefs.none { it == legacy || it == otherLegacy })
        } finally {
            helper.close()
            context.deleteDatabase(DB_NAME)
        }
    }

    private fun createVersion30Database(context: Context): SupportSQLiteOpenHelper {
        val configuration =
            SupportSQLiteOpenHelper.Configuration.builder(context)
                .name(DB_NAME)
                .callback(
                    object : SupportSQLiteOpenHelper.Callback(30) {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            db.execSQL(
                                "CREATE TABLE PlaylistInfo (playlistUid TEXT NOT NULL PRIMARY KEY, name TEXT NOT NULL)"
                            )
                            db.execSQL(
                                "CREATE TABLE PlaylistSong (songUid TEXT NOT NULL PRIMARY KEY)"
                            )
                            db.execSQL(
                                "CREATE TABLE PlaylistSongCrossRef (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, playlistUid TEXT NOT NULL, songUid TEXT NOT NULL)"
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
        return FrameworkSQLiteOpenHelperFactory().create(configuration)
    }

    private companion object {
        const val DB_NAME = "playlist-uid-migration-30-31.db"
    }
}
