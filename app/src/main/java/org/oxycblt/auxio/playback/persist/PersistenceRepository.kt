/*
 * Copyright (c) 2023 Auxio Project
 * PersistenceRepository.kt is part of Auxio.
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
import androidx.room.withTransaction
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import org.oxycblt.auxio.music.MusicRepository
import org.oxycblt.auxio.playback.state.PlaybackStateManager
import org.oxycblt.musikr.MusicParent
import timber.log.Timber as L

/**
 * Minimal last-raw-item snapshot used as the foundation for the TS18 fast-resume path.
 *
 * This deliberately stores primitive public playback metadata only. It does not grant platform
 * signing, privileged com.tw.music identity, or private Topway access. Direct pre-library playback
 * remains a follow-up runtime step that requires TS18 device validation.
 */
data class FastResumeSnapshot(
    val uri: String,
    val path: String?,
    val title: String?,
    val artist: String?,
    val album: String?,
    val durationMs: Long,
    val positionMs: Long,
    val playing: Boolean,
    val savedAtMs: Long,
)

/**
 * Manages the persisted playback state in a structured manner.
 *
 * @author Alexander Capehart (OxygenCobalt)
 */
interface PersistenceRepository {
    /** Read primitive queue session without requiring a loaded Musikr library. */
    suspend fun readQueueSession(): QueueSessionEntity?

    /** Read a bounded primitive queue window without requiring a loaded Musikr library. */
    suspend fun readQueueWindow(
        sessionId: Long,
        startInclusive: Int,
        endExclusive: Int,
    ): List<QueueItemRefEntity>

    /** Read the previously persisted [PlaybackStateManager.SavedState]. */
    suspend fun readState(): PlaybackStateManager.SavedState?

    /**
     * Persist a new [PlaybackStateManager.SavedState].
     *
     * @param state The [PlaybackStateManager.SavedState] to persist.
     */
    suspend fun saveState(state: PlaybackStateManager.SavedState?): Boolean

    /** Read the last raw playable item snapshot used by the TS18 fast-resume foundation. */
    suspend fun readFastResumeSnapshot(): FastResumeSnapshot?

    /** Persist the last raw playable item snapshot used by the TS18 fast-resume foundation. */
    suspend fun saveFastResumeSnapshot(snapshot: FastResumeSnapshot?): Boolean
}

class PersistenceRepositoryImpl
@Inject
constructor(
    @ApplicationContext private val context: Context,
    private val database: PersistenceDatabase,
    private val playbackStateDao: PlaybackStateDao,
    private val queueDao: QueueDao,
    private val musicRepository: MusicRepository,
) : PersistenceRepository {
    private val fastResumePrefs by lazy {
        context.getSharedPreferences(FAST_RESUME_PREFS, Context.MODE_PRIVATE)
    }

    override suspend fun readQueueSession(): QueueSessionEntity? =
        try {
            queueDao.getQueueSession()
        } catch (e: Exception) {
            L.w(e, "Unable to read primitive queue session")
            null
        }

    override suspend fun readQueueWindow(
        sessionId: Long,
        startInclusive: Int,
        endExclusive: Int,
    ): List<QueueItemRefEntity> =
        try {
            queueDao.getQueueWindow(sessionId, startInclusive, endExclusive)
        } catch (e: Exception) {
            L.w(e, "Unable to read primitive queue window")
            emptyList()
        }

    override suspend fun readState(): PlaybackStateManager.SavedState? {
        val library = musicRepository.library?.takeIf { !it.empty() } ?: return null
        val playbackState: PlaybackState
        val heapItems: List<QueueHeapItem>
        val mappingItems: List<QueueShuffledMappingItem>
        try {
            playbackState = playbackStateDao.getState() ?: return null
            heapItems = queueDao.getHeap()
            mappingItems = queueDao.getShuffledMapping()
        } catch (e: Exception) {
            L.e("Unable read playback state")
            L.e(e.stackTraceToString())
            return null
        }

        val heap = heapItems.map { library.findSong(it.uid) }
        val shuffledMapping = mappingItems.map { it.index }
        val parent = playbackState.parentUid?.let { musicRepository.find(it) as? MusicParent }

        return PlaybackStateManager.SavedState(
            positionMs = playbackState.positionMs,
            repeatMode = playbackState.repeatMode,
            parent = parent,
            heap = heap,
            shuffledMapping = shuffledMapping,
            index = playbackState.index,
            songUid = playbackState.songUid,
            shuffleScope = playbackState.shuffleScope,
        )
    }

    override suspend fun saveState(state: PlaybackStateManager.SavedState?): Boolean {
        val playbackState =
            state?.let {
                PlaybackState(
                    id = 0,
                    index = it.index,
                    positionMs = it.positionMs,
                    repeatMode = it.repeatMode,
                    songUid = it.songUid,
                    parentUid = it.parent?.uid,
                    shuffleScope = it.shuffleScope,
                )
            }
        val heap =
            state?.heap?.mapIndexed { i, song -> QueueHeapItem(i, requireNotNull(song).uid) }
                ?: emptyList()
        val shuffledMapping =
            state?.shuffledMapping?.mapIndexed { i, index -> QueueShuffledMappingItem(i, index) }
                ?: emptyList()
        val orderedHeapIndexes =
            state?.shuffledMapping?.takeIf { it.isNotEmpty() } ?: heap.map { it.id }
        val currentLogicalPosition =
            orderedHeapIndexes.indexOf(state?.index ?: -1).takeIf { it >= 0 } ?: (state?.index ?: 0)
        val session =
            state?.let {
                QueueSessionEntity(
                    id = 1,
                    currentLogicalPosition = currentLogicalPosition,
                    positionMs = it.positionMs,
                    repeatMode = it.repeatMode,
                    shuffleScope = it.shuffleScope,
                    totalCount = heap.size,
                    revision = System.currentTimeMillis(),
                    updatedAtMs = System.currentTimeMillis(),
                )
            }
        val primitiveItems =
            if (state == null) {
                emptyList()
            } else {
                orderedHeapIndexes.mapIndexedNotNull { logicalPosition, heapIndex ->
                    val song = state.heap.getOrNull(heapIndex) ?: return@mapIndexedNotNull null
                    QueueItemRefEntity(
                        sessionId = 1,
                        logicalPosition = logicalPosition,
                        stableSongUid = song.uid,
                        uri = song.uri.toString(),
                        pathFallback = song.path.toString(),
                        titleFallback = song.name.raw,
                        artistFallback = song.artists.joinToString { it.name.raw },
                        albumFallback = song.album.name.raw,
                        durationMs = song.durationMs,
                    )
                }
            }

        return try {
            database.withTransaction {
                playbackStateDao.nukeState()
                queueDao.nukeHeap()
                queueDao.nukeShuffledMapping()
                queueDao.nukeQueueSessions()
                queueDao.nukeQueueItemRefs()
                if (playbackState != null && session != null) {
                    playbackStateDao.insertState(playbackState)
                    queueDao.insertHeap(heap)
                    queueDao.insertShuffledMapping(shuffledMapping)
                    queueDao.insertQueueSession(session)
                    queueDao.insertQueueItemRefs(primitiveItems)
                }
            }
            L.d("Successfully wrote playback state transaction")
            true
        } catch (e: Exception) {
            L.e("Unable to transactionally write playback state")
            L.e(e.stackTraceToString())
            false
        }
    }

    override suspend fun readFastResumeSnapshot(): FastResumeSnapshot? {
        return try {
            if (!fastResumePrefs.getBoolean(KEY_FAST_VALID, false)) return null
            val uri = fastResumePrefs.getString(KEY_FAST_URI, null) ?: return null
            FastResumeSnapshot(
                uri = uri,
                path = fastResumePrefs.getString(KEY_FAST_PATH, null),
                title = fastResumePrefs.getString(KEY_FAST_TITLE, null),
                artist = fastResumePrefs.getString(KEY_FAST_ARTIST, null),
                album = fastResumePrefs.getString(KEY_FAST_ALBUM, null),
                durationMs = fastResumePrefs.getLong(KEY_FAST_DURATION_MS, 0L),
                positionMs = fastResumePrefs.getLong(KEY_FAST_POSITION_MS, 0L),
                playing = fastResumePrefs.getBoolean(KEY_FAST_PLAYING, false),
                savedAtMs = fastResumePrefs.getLong(KEY_FAST_SAVED_AT_MS, 0L),
            )
        } catch (e: Exception) {
            L.w(e, "Unable to read TS18 fast-resume snapshot")
            null
        }
    }

    override suspend fun saveFastResumeSnapshot(snapshot: FastResumeSnapshot?): Boolean {
        return try {
            val editor = fastResumePrefs.edit()
            if (snapshot == null) {
                editor.clear().commit()
            } else {
                editor
                    .putBoolean(KEY_FAST_VALID, true)
                    .putString(KEY_FAST_URI, snapshot.uri)
                    .putString(KEY_FAST_PATH, snapshot.path)
                    .putString(KEY_FAST_TITLE, snapshot.title)
                    .putString(KEY_FAST_ARTIST, snapshot.artist)
                    .putString(KEY_FAST_ALBUM, snapshot.album)
                    .putLong(KEY_FAST_DURATION_MS, snapshot.durationMs)
                    .putLong(KEY_FAST_POSITION_MS, snapshot.positionMs)
                    .putBoolean(KEY_FAST_PLAYING, snapshot.playing)
                    .putLong(KEY_FAST_SAVED_AT_MS, snapshot.savedAtMs)
                    .commit()
            }
        } catch (e: Exception) {
            L.w(e, "Unable to save TS18 fast-resume snapshot")
            false
        }
    }

    private companion object {
        const val FAST_RESUME_PREFS = "ts18_fast_resume_snapshot"
        const val KEY_FAST_VALID = "valid"
        const val KEY_FAST_URI = "uri"
        const val KEY_FAST_PATH = "path"
        const val KEY_FAST_TITLE = "title"
        const val KEY_FAST_ARTIST = "artist"
        const val KEY_FAST_ALBUM = "album"
        const val KEY_FAST_DURATION_MS = "duration_ms"
        const val KEY_FAST_POSITION_MS = "position_ms"
        const val KEY_FAST_PLAYING = "playing"
        const val KEY_FAST_SAVED_AT_MS = "saved_at_ms"
    }
}
