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
import org.oxycblt.auxio.playback.state.ShuffleScope
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
    /** Read primitive queue session metadata without requiring a loaded Musikr library. */
    suspend fun readQueueDescriptor(): QueueDescriptor?

    /** Read a bounded primitive queue window without requiring a loaded Musikr library. */
    suspend fun readQueueWindow(
        descriptor: QueueDescriptor,
        startInclusive: Int,
        endExclusive: Int,
    ): QueueWindow?

    /** Read the named initial window around [anchorLogicalPosition]. */
    suspend fun readQueueWindowAround(
        descriptor: QueueDescriptor,
        anchorLogicalPosition: Int = descriptor.currentLogicalPosition,
    ): QueueWindow?

    /** Persist the current logical/seek/repeat position without rewriting the queue. */
    suspend fun updateQueuePosition(
        descriptor: QueueDescriptor,
        logicalPosition: Int,
        positionMs: Long,
        repeatMode: org.oxycblt.auxio.playback.state.RepeatMode,
    ): Boolean

    /** Fill missing primitive playback metadata without replacing logical queue order. */
    suspend fun enrichQueueItem(
        descriptor: QueueDescriptor,
        logicalPosition: Int,
        snapshot: FastResumeSnapshot,
    ): Boolean

    /** Read the complete primitive order only for an explicit queue-reorder operation. */
    suspend fun readAllQueueItems(descriptor: QueueDescriptor): List<QueueItemRef>

    /** Insert primitive items at a logical position and return the committed descriptor. */
    suspend fun insertQueueItems(
        descriptor: QueueDescriptor,
        logicalPosition: Int,
        items: List<QueueItemRef>,
    ): QueueDescriptor?

    /** Remove one primitive item and return the committed descriptor. */
    suspend fun removeQueueItem(
        descriptor: QueueDescriptor,
        logicalPosition: Int,
    ): QueueDescriptor?

    /** Move one primitive item and return the committed descriptor. */
    suspend fun moveQueueItem(
        descriptor: QueueDescriptor,
        fromLogicalPosition: Int,
        toLogicalPosition: Int,
    ): QueueDescriptor?

    /** Persist an exact logical order expressed as canonical positions. */
    suspend fun reorderQueue(
        descriptor: QueueDescriptor,
        canonicalOrder: List<Int>,
        shuffleScope: ShuffleScope,
    ): QueueDescriptor?

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

    override suspend fun readQueueDescriptor(): QueueDescriptor? =
        try {
            val session = queueDao.getQueueSession() ?: return null
            val actualCount = queueDao.countQueueItems(session.id)
            if (actualCount <= 0) return null
            val totalCount = minOf(session.totalCount, actualCount).coerceAtLeast(0)
            if (totalCount == 0) return null
            QueueDescriptor(
                sessionId = session.id,
                totalCount = totalCount,
                currentLogicalPosition =
                    session.currentLogicalPosition.coerceIn(0, totalCount - 1),
                positionMs = session.positionMs.coerceAtLeast(0L),
                repeatMode = session.repeatMode,
                shuffleScope = session.shuffleScope,
                revision = session.revision,
                updatedAtMs = session.updatedAtMs,
            )
        } catch (e: Exception) {
            L.w(e, "Unable to read primitive queue session")
            null
        }

    override suspend fun readQueueWindow(
        descriptor: QueueDescriptor,
        startInclusive: Int,
        endExclusive: Int,
    ): QueueWindow? =
        try {
            val safeStart = startInclusive.coerceIn(0, descriptor.totalCount)
            val safeEnd = endExclusive.coerceIn(safeStart, descriptor.totalCount)
            if (safeStart == safeEnd) return null
            val rows = queueDao.getQueueWindow(descriptor.sessionId, safeStart, safeEnd)
            val rowsByPosition = rows.associateBy { it.logicalPosition }
            val items =
                (safeStart until safeEnd).map { logicalPosition ->
                    rowsByPosition[logicalPosition]?.toDomain()
                        ?: QueueItemRef(
                            logicalPosition = logicalPosition,
                            canonicalPosition = logicalPosition,
                            stableSongUid = null,
                            uri = null,
                            pathFallback = null,
                            titleFallback = null,
                            artistFallback = null,
                            albumFallback = null,
                            durationMs = 0L,
                        )
                }
            QueueWindow(descriptor, safeStart, items)
        } catch (e: Exception) {
            L.w(e, "Unable to read primitive queue window")
            null
        }

    override suspend fun readQueueWindowAround(
        descriptor: QueueDescriptor,
        anchorLogicalPosition: Int,
    ): QueueWindow? {
        val range =
            QueueWindowPolicy.around(
                descriptor.totalCount,
                anchorLogicalPosition,
            )
        return readQueueWindow(descriptor, range.startInclusive, range.endExclusive)
    }

    override suspend fun updateQueuePosition(
        descriptor: QueueDescriptor,
        logicalPosition: Int,
        positionMs: Long,
        repeatMode: org.oxycblt.auxio.playback.state.RepeatMode,
    ): Boolean =
        try {
            queueDao.updateQueuePosition(
                sessionId = descriptor.sessionId,
                logicalPosition = logicalPosition.coerceIn(0, descriptor.totalCount - 1),
                positionMs = positionMs.coerceAtLeast(0L),
                repeatMode = repeatMode,
                updatedAtMs = System.currentTimeMillis(),
            ) == 1
        } catch (e: Exception) {
            L.w(e, "Unable to update primitive queue position")
            false
        }

    override suspend fun enrichQueueItem(
        descriptor: QueueDescriptor,
        logicalPosition: Int,
        snapshot: FastResumeSnapshot,
    ): Boolean =
        try {
            queueDao.enrichQueueItem(
                sessionId = descriptor.sessionId,
                logicalPosition = logicalPosition,
                uri = snapshot.uri.takeIf { it.isNotBlank() },
                pathFallback = snapshot.path,
                titleFallback = snapshot.title,
                artistFallback = snapshot.artist,
                albumFallback = snapshot.album,
                durationMs = snapshot.durationMs.coerceAtLeast(0L),
            ) == 1
        } catch (e: Exception) {
            L.w(e, "Unable to enrich primitive queue item")
            false
        }

    override suspend fun readAllQueueItems(descriptor: QueueDescriptor): List<QueueItemRef> =
        try {
            queueDao.getAllQueueItems(descriptor.sessionId).map { it.toDomain() }
        } catch (e: Exception) {
            L.w(e, "Unable to read complete primitive queue for explicit reorder")
            emptyList()
        }

    override suspend fun insertQueueItems(
        descriptor: QueueDescriptor,
        logicalPosition: Int,
        items: List<QueueItemRef>,
    ): QueueDescriptor? {
        if (items.isEmpty()) return descriptor
        return mutateDescriptor("insert primitive queue items") { session ->
            val at = logicalPosition.coerceIn(0, session.totalCount)
            val count = items.size
            moveLogicalRangeToOffset(session.id, at, session.totalCount, count)
            val refs =
                items.mapIndexed { index, item ->
                    QueueItemRefEntity(
                        sessionId = session.id,
                        logicalPosition = at + index,
                        canonicalPosition = session.totalCount + index,
                        stableSongUid = item.stableSongUid,
                        uri = item.uri,
                        pathFallback = item.pathFallback,
                        titleFallback = item.titleFallback,
                        artistFallback = item.artistFallback,
                        albumFallback = item.albumFallback,
                        durationMs = item.durationMs.coerceAtLeast(0L),
                    )
                }
            queueDao.insertQueueItemRefs(refs)
            val current =
                if (at <= session.currentLogicalPosition) session.currentLogicalPosition + count
                else session.currentLogicalPosition
            commitLayout(
                session = session,
                currentLogicalPosition = current,
                totalCount = session.totalCount + count,
                shuffleScope = session.shuffleScope,
            )
        }
    }

    override suspend fun removeQueueItem(
        descriptor: QueueDescriptor,
        logicalPosition: Int,
    ): QueueDescriptor? =
        mutateDescriptor("remove primitive queue item") { session ->
            if (logicalPosition !in 0 until session.totalCount) return@mutateDescriptor null
            val removed = queueDao.getQueueItem(session.id, logicalPosition)
                ?: return@mutateDescriptor null
            if (queueDao.deleteQueueItem(session.id, logicalPosition) != 1) {
                return@mutateDescriptor null
            }
            moveLogicalRangeToOffset(
                session.id,
                logicalPosition + 1,
                session.totalCount,
                -1,
            )
            moveCanonicalRangeToOffset(
                session.id,
                removed.canonicalPosition + 1,
                session.totalCount,
                -1,
            )
            val newCount = session.totalCount - 1
            if (newCount == 0) {
                queueDao.nukeQueueSessions()
                return@mutateDescriptor null
            }
            val current =
                when {
                    logicalPosition < session.currentLogicalPosition ->
                        session.currentLogicalPosition - 1
                    logicalPosition == session.currentLogicalPosition ->
                        logicalPosition.coerceAtMost(newCount - 1)
                    else -> session.currentLogicalPosition
                }
            commitLayout(session, current, newCount, session.shuffleScope)
        }

    override suspend fun moveQueueItem(
        descriptor: QueueDescriptor,
        fromLogicalPosition: Int,
        toLogicalPosition: Int,
    ): QueueDescriptor? =
        mutateDescriptor("move primitive queue item") { session ->
            if (
                fromLogicalPosition !in 0 until session.totalCount ||
                    toLogicalPosition !in 0 until session.totalCount
            ) {
                return@mutateDescriptor null
            }
            if (fromLogicalPosition == toLogicalPosition) {
                return@mutateDescriptor session.toDescriptor()
            }
            check(queueDao.setLogicalPosition(session.id, fromLogicalPosition, TEMP_POSITION) == 1)
            if (fromLogicalPosition < toLogicalPosition) {
                moveLogicalRangeToOffset(
                    session.id,
                    fromLogicalPosition + 1,
                    toLogicalPosition + 1,
                    -1,
                )
            } else {
                moveLogicalRangeToOffset(
                    session.id,
                    toLogicalPosition,
                    fromLogicalPosition,
                    1,
                )
            }
            check(queueDao.setLogicalPosition(session.id, TEMP_POSITION, toLogicalPosition) == 1)
            val current =
                when {
                    session.currentLogicalPosition == fromLogicalPosition -> toLogicalPosition
                    fromLogicalPosition < session.currentLogicalPosition &&
                        session.currentLogicalPosition <= toLogicalPosition ->
                        session.currentLogicalPosition - 1
                    toLogicalPosition <= session.currentLogicalPosition &&
                        session.currentLogicalPosition < fromLogicalPosition ->
                        session.currentLogicalPosition + 1
                    else -> session.currentLogicalPosition
                }
            commitLayout(session, current, session.totalCount, session.shuffleScope)
        }

    override suspend fun reorderQueue(
        descriptor: QueueDescriptor,
        canonicalOrder: List<Int>,
        shuffleScope: ShuffleScope,
    ): QueueDescriptor? =
        mutateDescriptor("reorder primitive queue") { session ->
            val rows = queueDao.getAllQueueItems(session.id)
            if (
                rows.size != session.totalCount ||
                    canonicalOrder.size != rows.size ||
                    canonicalOrder.toSet() != rows.map { it.canonicalPosition }.toSet()
            ) {
                return@mutateDescriptor null
            }
            val currentCanonical =
                rows.firstOrNull { it.logicalPosition == session.currentLogicalPosition }
                    ?.canonicalPosition ?: return@mutateDescriptor null
            queueDao.offsetLogicalPositions(
                session.id,
                0,
                session.totalCount,
                POSITION_OFFSET,
            )
            canonicalOrder.forEachIndexed { logical, canonical ->
                check(
                    queueDao.setLogicalPositionByCanonical(session.id, canonical, logical) == 1
                )
            }
            val current = canonicalOrder.indexOf(currentCanonical)
            commitLayout(session, current, session.totalCount, shuffleScope)
        }

    private suspend fun mutateDescriptor(
        operation: String,
        block: suspend (QueueSessionEntity) -> QueueDescriptor?,
    ): QueueDescriptor? =
        try {
            database.withTransaction {
                val session = queueDao.getQueueSession() ?: return@withTransaction null
                block(session)
            }
        } catch (e: Exception) {
            L.w(e, "Unable to $operation")
            null
        }

    private suspend fun moveLogicalRangeToOffset(
        sessionId: Long,
        startInclusive: Int,
        endExclusive: Int,
        finalDelta: Int,
    ) {
        if (startInclusive >= endExclusive) return
        queueDao.offsetLogicalPositions(
            sessionId,
            startInclusive,
            endExclusive,
            POSITION_OFFSET,
        )
        queueDao.offsetLogicalPositions(
            sessionId,
            startInclusive + POSITION_OFFSET,
            endExclusive + POSITION_OFFSET,
            finalDelta - POSITION_OFFSET,
        )
    }

    private suspend fun moveCanonicalRangeToOffset(
        sessionId: Long,
        startInclusive: Int,
        endExclusive: Int,
        finalDelta: Int,
    ) {
        if (startInclusive >= endExclusive) return
        queueDao.offsetCanonicalPositions(
            sessionId,
            startInclusive,
            endExclusive,
            POSITION_OFFSET,
        )
        queueDao.offsetCanonicalPositions(
            sessionId,
            startInclusive + POSITION_OFFSET,
            endExclusive + POSITION_OFFSET,
            finalDelta - POSITION_OFFSET,
        )
    }

    private suspend fun commitLayout(
        session: QueueSessionEntity,
        currentLogicalPosition: Int,
        totalCount: Int,
        shuffleScope: ShuffleScope,
    ): QueueDescriptor? {
        val now = System.currentTimeMillis()
        val revision = maxOf(now, session.revision + 1)
        val safeCurrent = currentLogicalPosition.coerceIn(0, (totalCount - 1).coerceAtLeast(0))
        if (
            queueDao.updateQueueLayout(
                sessionId = session.id,
                logicalPosition = safeCurrent,
                totalCount = totalCount,
                shuffleScope = shuffleScope,
                revision = revision,
                updatedAtMs = now,
            ) != 1
        ) {
            return null
        }
        return QueueDescriptor(
            sessionId = session.id,
            totalCount = totalCount,
            currentLogicalPosition = safeCurrent,
            positionMs = session.positionMs,
            repeatMode = session.repeatMode,
            shuffleScope = shuffleScope,
            revision = revision,
            updatedAtMs = now,
        )
    }

    private fun QueueSessionEntity.toDescriptor() =
        QueueDescriptor(
            sessionId = id,
            totalCount = totalCount,
            currentLogicalPosition = currentLogicalPosition,
            positionMs = positionMs,
            repeatMode = repeatMode,
            shuffleScope = shuffleScope,
            revision = revision,
            updatedAtMs = updatedAtMs,
        )

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
            state?.index?.coerceIn(0, (orderedHeapIndexes.size - 1).coerceAtLeast(0)) ?: 0
        val session =
            state?.let {
                QueueSessionEntity(
                    id = 1,
                    currentLogicalPosition = currentLogicalPosition,
                    positionMs = it.positionMs,
                    repeatMode = it.repeatMode,
                    shuffleScope = it.shuffleScope,
                    totalCount = orderedHeapIndexes.size,
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
                        canonicalPosition = heapIndex,
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

    private fun QueueItemRefEntity.toDomain() =
        QueueItemRef(
            logicalPosition = logicalPosition,
            canonicalPosition = canonicalPosition,
            stableSongUid = stableSongUid,
            uri = uri,
            pathFallback = pathFallback,
            titleFallback = titleFallback,
            artistFallback = artistFallback,
            albumFallback = albumFallback,
            durationMs = durationMs.coerceAtLeast(0L),
        )

    private companion object {
        const val POSITION_OFFSET = 1_000_000_000
        const val TEMP_POSITION = -1
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
