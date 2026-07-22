/*
 * Copyright (c) 2026 Auxio Project
 * CacheMigrationInstrumentedTest.kt is part of Auxio.
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

import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 29, maxSdkVersion = 29)
class CacheMigrationInstrumentedTest {
    @get:Rule
    val helper =
        MigrationTestHelper(InstrumentationRegistry.getInstrumentation(), CacheDatabase::class.java)

    @After
    fun deleteTestDatabases() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        context.deleteDatabase(DB_FROM_70)
        context.deleteDatabase(DB_FROM_71)
    }

    @Test
    fun migrate70To72PreservesLegacyCacheAndValidatesCurrentSchema() {
        helper.createDatabase(DB_FROM_70, 70).apply {
            execSQL(
                "INSERT INTO CachedFileData (uri, modifiedMs, addedMs) " +
                    "VALUES ('content://media/external/audio/1', 1000, 2000)"
            )
            close()
        }

        val migrated =
            helper.runMigrationsAndValidate(
                DB_FROM_70,
                72,
                true,
                CacheDatabase.MIGRATION_70_71,
                CacheDatabase.MIGRATION_71_72,
            )
        migrated
            .query(
                "SELECT modifiedMs, addedMs FROM CachedFileData " +
                    "WHERE uri = 'content://media/external/audio/1'"
            )
            .use { cursor ->
                assertTrue(cursor.moveToFirst())
                assertEquals(1000L, cursor.getLong(0))
                assertEquals(2000L, cursor.getLong(1))
            }
        migrated.close()
    }

    @Test
    fun migrate71To72PreservesSourceStateAndAddsGenerationLedger() {
        helper.createDatabase(DB_FROM_71, 71).apply {
            execSQL(
                "INSERT INTO SourceStateData " +
                    "(sourceKey, available, lastSeenMs, lastCommittedGeneration) " +
                    "VALUES ('DIRECT_FS:/storage/usbdisk0', 0, 1234, 7)"
            )
            close()
        }

        val migrated =
            helper.runMigrationsAndValidate(DB_FROM_71, 72, true, CacheDatabase.MIGRATION_71_72)
        migrated
            .query(
                "SELECT available, lastSeenMs, lastCommittedGeneration FROM SourceStateData " +
                    "WHERE sourceKey = 'DIRECT_FS:/storage/usbdisk0'"
            )
            .use { cursor ->
                assertTrue(cursor.moveToFirst())
                assertEquals(0, cursor.getInt(0))
                assertEquals(1234L, cursor.getLong(1))
                assertEquals(7L, cursor.getLong(2))
            }
        assertTablesExist(
            migrated,
            listOf(
                "SourceLedgerData",
                "SourceScanGenerationData",
                "ScanSeenData",
                "PendingCachedFileData",
                "IndexedSongData",
                "IndexedUriStateData",
            ),
        )
        migrated.close()
    }

    private fun assertTablesExist(
        database: androidx.sqlite.db.SupportSQLiteDatabase,
        tableNames: List<String>,
    ) {
        tableNames.forEach { tableName ->
            database
                .query(
                    "SELECT COUNT(*) FROM sqlite_master " + "WHERE type = 'table' AND name = ?",
                    arrayOf(tableName),
                )
                .use { cursor ->
                    assertTrue(cursor.moveToFirst())
                    assertEquals("Missing table $tableName", 1, cursor.getInt(0))
                }
        }
    }

    private companion object {
        const val DB_FROM_70 = "cache-migration-from-70.db"
        const val DB_FROM_71 = "cache-migration-from-71.db"
    }
}
