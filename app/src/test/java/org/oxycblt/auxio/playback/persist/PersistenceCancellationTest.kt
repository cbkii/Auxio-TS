/*
 * Copyright (c) 2026 Auxio Project
 * PersistenceCancellationTest.kt is part of Auxio.
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
import androidx.test.core.app.ApplicationProvider
import kotlin.coroutines.cancellation.CancellationException
import kotlin.test.assertFailsWith
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.oxycblt.auxio.music.IndexingState
import org.oxycblt.auxio.music.MusicRepository
import org.oxycblt.auxio.music.StartupReadinessState
import org.oxycblt.auxio.playback.state.RepeatMode
import org.oxycblt.auxio.playback.state.ShuffleScope
import org.oxycblt.musikr.Library
import org.oxycblt.musikr.Music
import org.oxycblt.musikr.Playlist
import org.oxycblt.musikr.Song
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Focused coverage proving that queue-persistence methods rethrow [CancellationException] instead
 * of consuming it through their broad operational-exception fallbacks, while ordinary exceptions
 * still degrade to the safe fallback values.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
class PersistenceCancellationTest {
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

    private fun repository(queueDao: QueueDao): PersistenceRepositoryImpl =
        PersistenceRepositoryImpl(
            ApplicationProvider.getApplicationContext(),
            database,
            database.playbackStateDao(),
            queueDao,
            StubMusicRepository(),
        )

    private fun descriptor() =
        QueueDescriptor(
            sessionId = 1L,
            totalCount = 3,
            currentLogicalPosition = 0,
            positionMs = 0L,
            repeatMode = RepeatMode.NONE,
            shuffleScope = ShuffleScope.OFF,
            revision = 1L,
            updatedAtMs = 0L,
        )

    @Test
    fun `readQueueDescriptor rethrows cancellation`() {
        val repository = repository(ThrowingQueueDao { CancellationException("cancelled") })
        assertFailsWith<CancellationException> { runBlocking { repository.readQueueDescriptor() } }
    }

    @Test
    fun `readQueueDescriptor falls back to null on operational failure`() = runBlocking {
        val repository = repository(ThrowingQueueDao { IllegalStateException("db failure") })
        assertNull(repository.readQueueDescriptor())
    }

    @Test
    fun `readQueueWindow rethrows cancellation`() {
        val repository = repository(ThrowingQueueDao { CancellationException("cancelled") })
        assertFailsWith<CancellationException> {
            runBlocking { repository.readQueueWindow(descriptor(), 0, 3) }
        }
    }

    @Test
    fun `readQueueWindow falls back to null on operational failure`() = runBlocking {
        val repository = repository(ThrowingQueueDao { IllegalStateException("db failure") })
        assertNull(repository.readQueueWindow(descriptor(), 0, 3))
    }

    @Test
    fun `updateQueuePosition rethrows cancellation`() {
        val repository = repository(ThrowingQueueDao { CancellationException("cancelled") })
        assertFailsWith<CancellationException> {
            runBlocking { repository.updateQueuePosition(descriptor(), 0, 0L, RepeatMode.NONE) }
        }
    }

    @Test
    fun `enrichQueueItem rethrows cancellation`() {
        val repository = repository(ThrowingQueueDao { CancellationException("cancelled") })
        assertFailsWith<CancellationException> {
            runBlocking {
                repository.enrichQueueItem(
                    descriptor(),
                    0,
                    FastResumeSnapshot(
                        uri = "content://media/1",
                        path = null,
                        title = null,
                        artist = null,
                        album = null,
                        durationMs = 0L,
                        positionMs = 0L,
                        playing = false,
                        savedAtMs = 0L,
                    ),
                )
            }
        }
    }

    @Test
    fun `readAllQueueItems rethrows cancellation`() {
        val repository = repository(ThrowingQueueDao { CancellationException("cancelled") })
        assertFailsWith<CancellationException> {
            runBlocking { repository.readAllQueueItems(descriptor()) }
        }
    }

    @Test
    fun `readAllQueueItems falls back to empty list on operational failure`() = runBlocking {
        val repository = repository(ThrowingQueueDao { IllegalStateException("db failure") })
        assertEquals(emptyList<QueueItemRef>(), repository.readAllQueueItems(descriptor()))
    }

    @Test
    fun `mutateDescriptor-backed removeQueueItem rethrows cancellation`() {
        val repository = repository(ThrowingQueueDao { CancellationException("cancelled") })
        assertFailsWith<CancellationException> {
            runBlocking { repository.removeQueueItem(descriptor(), 0) }
        }
    }

    /** A [QueueDao] whose every method throws the provided exception. */
    private class ThrowingQueueDao(private val exception: () -> Throwable) : QueueDao {
        private fun fail(): Nothing = throw exception()

        override suspend fun getHeap(): List<QueueHeapItem> = fail()

        override suspend fun getShuffledMapping(): List<QueueShuffledMappingItem> = fail()

        override suspend fun nukeHeap() = fail()

        override suspend fun nukeShuffledMapping() = fail()

        override suspend fun insertHeap(heap: List<QueueHeapItem>) = fail()

        override suspend fun insertShuffledMapping(mapping: List<QueueShuffledMappingItem>) = fail()

        override suspend fun getQueueSession(): QueueSessionEntity? = fail()

        override suspend fun getQueueWindow(
            sessionId: Long,
            startInclusive: Int,
            endExclusive: Int,
        ): List<QueueItemRefEntity> = fail()

        override suspend fun getQueueItem(
            sessionId: Long,
            logicalPosition: Int,
        ): QueueItemRefEntity? = fail()

        override suspend fun countQueueItems(sessionId: Long): Int = fail()

        override suspend fun getAllQueueItems(sessionId: Long): List<QueueItemRefEntity> = fail()

        override suspend fun updateQueuePosition(
            sessionId: Long,
            logicalPosition: Int,
            positionMs: Long,
            repeatMode: RepeatMode,
            updatedAtMs: Long,
        ): Int = fail()

        override suspend fun enrichQueueItem(
            sessionId: Long,
            logicalPosition: Int,
            uri: String?,
            pathFallback: String?,
            titleFallback: String?,
            artistFallback: String?,
            albumFallback: String?,
            durationMs: Long,
        ): Int = fail()

        override suspend fun getQueueItemByCanonicalPosition(
            sessionId: Long,
            canonicalPosition: Int,
        ): QueueItemRefEntity? = fail()

        override suspend fun deleteQueueItem(sessionId: Long, logicalPosition: Int): Int = fail()

        override suspend fun offsetLogicalPositions(
            sessionId: Long,
            startInclusive: Int,
            endExclusive: Int,
            delta: Int,
        ): Int = fail()

        override suspend fun offsetCanonicalPositions(
            sessionId: Long,
            startInclusive: Int,
            endExclusive: Int,
            delta: Int,
        ): Int = fail()

        override suspend fun setLogicalPositionByCanonical(
            sessionId: Long,
            canonicalPosition: Int,
            logicalPosition: Int,
        ): Int = fail()

        override suspend fun setLogicalPosition(
            sessionId: Long,
            fromLogicalPosition: Int,
            toLogicalPosition: Int,
        ): Int = fail()

        override suspend fun updateQueueLayout(
            sessionId: Long,
            logicalPosition: Int,
            totalCount: Int,
            shuffleScope: ShuffleScope,
            revision: Long,
            updatedAtMs: Long,
        ): Int = fail()

        override suspend fun nukeQueueSessions() = fail()

        override suspend fun nukeQueueItemRefs() = fail()

        override suspend fun insertQueueSession(session: QueueSessionEntity) = fail()

        override suspend fun insertQueueItemRefs(items: List<QueueItemRefEntity>) = fail()
    }

    /** A no-op [MusicRepository] stub; persistence reads under test never consult the library. */
    private class StubMusicRepository : MusicRepository {
        override val library: Library? = null
        override val indexingState: IndexingState? = null
        override val startupReadinessState: StartupReadinessState =
            StartupReadinessState.CheckingCachedLibrary

        override fun addUpdateListener(listener: MusicRepository.UpdateListener) = Unit

        override fun removeUpdateListener(listener: MusicRepository.UpdateListener) = Unit

        override fun addIndexingListener(listener: MusicRepository.IndexingListener) = Unit

        override fun removeIndexingListener(listener: MusicRepository.IndexingListener) = Unit

        override fun addStartupReadinessListener(
            listener: MusicRepository.StartupReadinessListener
        ) = Unit

        override fun removeStartupReadinessListener(
            listener: MusicRepository.StartupReadinessListener
        ) = Unit

        override fun find(uid: Music.UID): Music? = null

        override suspend fun createPlaylist(name: String, songs: List<Song>) = Unit

        override suspend fun renamePlaylist(playlist: Playlist, name: String) = Unit

        override suspend fun addToPlaylist(songs: List<Song>, playlist: Playlist) = Unit

        override suspend fun rewritePlaylist(playlist: Playlist, songs: List<Song>) = Unit

        override suspend fun deletePlaylist(playlist: Playlist) = Unit

        override fun registerWorker(worker: MusicRepository.IndexingWorker) = Unit

        override fun unregisterWorker(worker: MusicRepository.IndexingWorker) = Unit

        override fun requestIndex(withCache: Boolean) = Unit

        override suspend fun startup(worker: MusicRepository.IndexingWorker) = Unit

        override suspend fun index(worker: MusicRepository.IndexingWorker, withCache: Boolean) =
            Unit
    }
}
