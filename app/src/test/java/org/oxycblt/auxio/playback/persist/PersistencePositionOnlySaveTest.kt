/*
 * Copyright (c) 2026 Auxio Project
 * PersistencePositionOnlySaveTest.kt is part of Auxio.
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
import androidx.room.Room
import androidx.room.withTransaction
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.oxycblt.auxio.playback.state.RepeatMode
import org.oxycblt.auxio.playback.state.ShuffleScope
import org.oxycblt.musikr.Music
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
class PersistencePositionOnlySaveTest {
    private lateinit var database: PersistenceDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database =
            Room.inMemoryDatabaseBuilder(context, PersistenceDatabase::class.java)
                .allowMainThreadQueries()
                .build()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun positionOnlySavePreservesHeapAndPrimitiveMediaRows() = runBlocking {
        val uid = checkNotNull(Music.UID.fromString("uas00000000-0000-0000-0000-000000000001"))
        val legacyDao = database.playbackStateDao()
        val queueDao = database.queueDao()
        legacyDao.insertState(
            PlaybackState(
                id = 0,
                index = 0,
                positionMs = 0L,
                repeatMode = RepeatMode.NONE,
                songUid = uid,
                parentUid = null,
                shuffleScope = ShuffleScope.OFF,
            )
        )
        queueDao.insertHeap(listOf(QueueHeapItem(0, uid)))
        queueDao.insertQueueSession(
            QueueSessionEntity(
                id = 1L,
                currentLogicalPosition = 0,
                positionMs = 0L,
                repeatMode = RepeatMode.NONE,
                shuffleScope = ShuffleScope.OFF,
                totalCount = 1,
                revision = 8L,
                updatedAtMs = 0L,
            )
        )
        queueDao.insertQueueItemRefs(
            listOf(
                QueueItemRefEntity(
                    sessionId = 1L,
                    logicalPosition = 0,
                    canonicalPosition = 0,
                    stableSongUid = uid,
                    uri = "file:///storage/usbdisk0/one.flac",
                    pathFallback = "/storage/usbdisk0/one.flac",
                    titleFallback = "One",
                    artistFallback = "Artist",
                    albumFallback = "Album",
                    durationMs = 50_000L,
                )
            )
        )
        val originalItems = queueDao.getAllQueueItems(1L)
        val originalHeap = queueDao.getHeap()

        database.withTransaction {
            assertEquals(
                1,
                queueDao.updateQueuePosition(
                    sessionId = 1L,
                    expectedRevision = 8L,
                    logicalPosition = 0,
                    positionMs = 24_000L,
                    repeatMode = RepeatMode.NONE,
                    updatedAtMs = 100L,
                ),
            )
            assertEquals(
                1,
                legacyDao.updateProgress(
                    logicalPosition = 0,
                    positionMs = 24_000L,
                    repeatMode = RepeatMode.NONE,
                ),
            )
        }

        assertEquals(24_000L, legacyDao.getState()?.positionMs)
        assertEquals(24_000L, queueDao.getQueueSession()?.positionMs)
        assertEquals(8L, queueDao.getQueueSession()?.revision)
        assertEquals(originalItems, queueDao.getAllQueueItems(1L))
        assertEquals(originalHeap, queueDao.getHeap())
    }
}
