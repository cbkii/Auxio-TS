/*
 * Copyright (c) 2026 Auxio Project
 * CacheWriteDaoReconciliationTest.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.oxycblt.musikr.cache.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
class CacheWriteDaoReconciliationTest {
    private lateinit var database: CacheDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database =
            Room.inMemoryDatabaseBuilder(context, CacheDatabase::class.java)
                .allowMainThreadQueries()
                .build()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `legacy reconciliation pages without skipping rows while deleting`() = runBlocking {
        val writable = database.openHelper.writableDatabase
        writable.beginTransaction()
        try {
            val statement =
                writable.compileStatement(
                    "INSERT INTO CachedFileData (uri, modifiedMs, addedMs) VALUES (?, ?, ?)"
                )
            repeat(RECORD_COUNT) { index ->
                statement.clearBindings()
                statement.bindString(1, uri(index))
                statement.bindLong(2, index.toLong())
                statement.bindLong(3, index.toLong())
                statement.executeInsert()
            }
            writable.setTransactionSuccessful()
        } finally {
            writable.endTransaction()
        }

        val retained =
            setOf(
                uri(0),
                uri(CacheWriteDao.LEGACY_RECONCILIATION_PAGE_SIZE - 1),
                uri(CacheWriteDao.LEGACY_RECONCILIATION_PAGE_SIZE),
                uri(RECORD_COUNT - 1),
            )
        database.writeDao().deleteExcludingUris(retained)

        writable.query("SELECT uri FROM CachedFileData ORDER BY uri").use { cursor ->
            val actual = mutableSetOf<String>()
            while (cursor.moveToNext()) actual += cursor.getString(0)
            assertEquals(retained, actual)
        }
        assertTrue(RECORD_COUNT > CacheWriteDao.LEGACY_RECONCILIATION_PAGE_SIZE * 2)
    }

    private fun uri(index: Int): String =
        "content://media/external/audio/${index.toString().padStart(6, '0')}"

    private companion object {
        const val RECORD_COUNT = 1_200
    }
}
