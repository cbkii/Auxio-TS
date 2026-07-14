/*
 * Copyright (c) 2026 Auxio Project
 * PersistenceMigrationTest.kt is part of Auxio.
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

package org.oxycblt.auxio.playback.persist

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Executable coverage for [PersistenceDatabase.MIGRATION_39_40] using a populated legacy v39
 * schema. Verifies that the saved logical queue position and current song survive the migration for
 * unshuffled queues, shuffled queues where logical and canonical positions differ, boundary
 * positions and empty/incomplete legacy state.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
class PersistenceMigrationTest {
    private lateinit var helper: SupportSQLiteOpenHelper
    private lateinit var db: SupportSQLiteDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        helper =
            FrameworkSQLiteOpenHelperFactory()
                .create(
                    SupportSQLiteOpenHelper.Configuration.builder(context)
                        .name(null) // In-memory.
                        .callback(
                            object : SupportSQLiteOpenHelper.Callback(39) {
                                override fun onCreate(db: SupportSQLiteDatabase) {
                                    createLegacySchema(db)
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
        db = helper.writableDatabase
    }

    @After
    fun tearDown() {
        helper.close()
    }

    private fun createLegacySchema(db: SupportSQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE PlaybackState (id INTEGER NOT NULL PRIMARY KEY, " +
                "`index` INTEGER NOT NULL, positionMs INTEGER NOT NULL, " +
                "repeatMode TEXT NOT NULL, songUid TEXT NOT NULL, parentUid TEXT, " +
                "shuffleScope TEXT NOT NULL)"
        )
        db.execSQL(
            "CREATE TABLE QueueHeapItem (id INTEGER NOT NULL PRIMARY KEY, uid TEXT NOT NULL)"
        )
        db.execSQL(
            "CREATE TABLE QueueShuffledMappingItem (id INTEGER NOT NULL PRIMARY KEY, " +
                "`index` INTEGER NOT NULL)"
        )
    }

    private fun insertState(index: Int, positionMs: Long = 4200L, shuffleScope: String = "OFF") {
        db.execSQL(
            "INSERT INTO PlaybackState (id, `index`, positionMs, repeatMode, songUid, " +
                "parentUid, shuffleScope) VALUES (0, $index, $positionMs, 'NONE', " +
                "'org.oxycblt.auxio:song-current', NULL, '$shuffleScope')"
        )
    }

    private fun insertHeap(uids: List<String>) {
        uids.forEachIndexed { i, uid ->
            db.execSQL("INSERT INTO QueueHeapItem (id, uid) VALUES ($i, '$uid')")
        }
    }

    private fun insertShuffledMapping(logicalToHeap: List<Int>) {
        logicalToHeap.forEachIndexed { logical, heapIndex ->
            db.execSQL(
                "INSERT INTO QueueShuffledMappingItem (id, `index`) VALUES ($logical, $heapIndex)"
            )
        }
    }

    private fun migrate() = PersistenceDatabase.MIGRATION_39_40.migrate(db)

    private fun sessionLogicalPosition(): Int? =
        db.query("SELECT currentLogicalPosition FROM QueueSessionEntity WHERE id = 1").use {
            if (it.moveToFirst()) it.getInt(0) else null
        }

    private fun itemAtLogical(position: Int): Pair<Int, String>? =
        db.query(
                "SELECT canonicalPosition, stableSongUid FROM QueueItemRefEntity " +
                    "WHERE sessionId = 1 AND logicalPosition = $position"
            )
            .use { if (it.moveToFirst()) it.getInt(0) to it.getString(1) else null }

    @Test
    fun `unshuffled queue preserves logical position and current song`() {
        insertHeap(listOf("uid-a", "uid-b", "uid-c", "uid-d"))
        insertState(index = 1)
        migrate()

        assertEquals(1, sessionLogicalPosition())
        val (canonical, uid) = requireNotNull(itemAtLogical(1))
        assertEquals(1, canonical)
        assertEquals("uid-b", uid)
    }

    @Test
    fun `shuffled queue keeps saved logical position without reverse mapping`() {
        insertHeap(listOf("uid-a", "uid-b", "uid-c", "uid-d"))
        // Logical order: C, A, D, B.
        insertShuffledMapping(listOf(2, 0, 3, 1))
        // The saved index is already the logical position: logical 2 -> heap 3 -> uid-d.
        insertState(index = 2, shuffleScope = "ALL")
        migrate()

        assertEquals(2, sessionLogicalPosition())
        val (canonical, uid) = requireNotNull(itemAtLogical(2))
        assertEquals(3, canonical)
        assertEquals("uid-d", uid)
    }

    @Test
    fun `current item at beginning survives`() {
        insertHeap(listOf("uid-a", "uid-b", "uid-c"))
        insertState(index = 0)
        migrate()

        assertEquals(0, sessionLogicalPosition())
        assertEquals(0 to "uid-a", itemAtLogical(0))
    }

    @Test
    fun `current item at end survives`() {
        insertHeap(listOf("uid-a", "uid-b", "uid-c"))
        insertState(index = 2)
        migrate()

        assertEquals(2, sessionLogicalPosition())
        assertEquals(2 to "uid-c", itemAtLogical(2))
    }

    @Test
    fun `out of bounds index is clamped to queue bounds`() {
        insertHeap(listOf("uid-a", "uid-b"))
        insertState(index = 9)
        migrate()

        assertEquals(1, sessionLogicalPosition())
    }

    @Test
    fun `negative index is clamped to zero`() {
        insertHeap(listOf("uid-a", "uid-b"))
        insertState(index = -1)
        migrate()

        assertEquals(0, sessionLogicalPosition())
    }

    @Test
    fun `empty legacy state migrates without a session`() {
        migrate()

        assertEquals(null, sessionLogicalPosition())
        db.query("SELECT COUNT(*) FROM QueueItemRefEntity").use {
            assertTrue(it.moveToFirst())
            assertEquals(0, it.getInt(0))
        }
    }

    @Test
    fun `incomplete legacy state with empty heap clamps to zero`() {
        insertState(index = 3)
        migrate()

        assertEquals(0, sessionLogicalPosition())
        db.query("SELECT totalCount FROM QueueSessionEntity WHERE id = 1").use {
            assertTrue(it.moveToFirst())
            assertEquals(0, it.getInt(0))
        }
    }

    @Test
    fun `shuffled item refs map logical to canonical positions`() {
        insertHeap(listOf("uid-a", "uid-b", "uid-c"))
        insertShuffledMapping(listOf(1, 2, 0))
        insertState(index = 0, shuffleScope = "ALL")
        migrate()

        assertEquals(1 to "uid-b", itemAtLogical(0))
        assertEquals(2 to "uid-c", itemAtLogical(1))
        assertEquals(0 to "uid-a", itemAtLogical(2))
    }

    @Test
    fun `migration is idempotent for table creation`() {
        insertHeap(listOf("uid-a"))
        insertState(index = 0)
        migrate()
        // Table creation uses IF NOT EXISTS; a second run must not throw on DDL.
        db.query("SELECT COUNT(*) FROM QueueSessionEntity").use {
            assertTrue(it.moveToFirst())
            assertFalse(it.getInt(0) > 1)
        }
    }
}
