/*
 * Copyright (c) 2022 Auxio Project
 * QueueViewModel.kt is part of Auxio.
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

package org.oxycblt.auxio.playback.queue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.oxycblt.auxio.list.adapter.UpdateInstructions
import org.oxycblt.auxio.playback.persist.PersistenceRepository
import org.oxycblt.auxio.playback.persist.QueueItemRef
import org.oxycblt.auxio.playback.persist.QueueWindow
import org.oxycblt.auxio.playback.persist.QueueWindowPolicy
import org.oxycblt.auxio.playback.state.PlaybackStateManager
import org.oxycblt.auxio.playback.state.QueueChange
import org.oxycblt.auxio.util.Event
import org.oxycblt.auxio.util.MutableEvent
import org.oxycblt.musikr.MusicParent
import org.oxycblt.musikr.Song
import timber.log.Timber as L

/**
 * A [ViewModel] that manages the current queue state and allows navigation through the queue.
 *
 * @author Alexander Capehart (OxygenCobalt)
 */
data class QueueDisplayItem(
    val globalPosition: Int,
    val song: Song?,
    val primitive: QueueItemRef?,
) {
    val editable: Boolean
        get() = song != null || primitive?.hasPlayableReference == true
}

@HiltViewModel
class QueueViewModel
@Inject
constructor(
    private val playbackManager: PlaybackStateManager,
    private val persistenceRepository: PersistenceRepository,
) : ViewModel(), PlaybackStateManager.Listener {

    private val _queue = MutableStateFlow(listOf<QueueDisplayItem>())
    /** The currently loaded queue range. */
    val queue: StateFlow<List<QueueDisplayItem>> = _queue
    private val _queueInstructions = MutableEvent<UpdateInstructions>()
    /** Instructions for how to update [queue] in the UI. */
    val queueInstructions: Event<UpdateInstructions> = _queueInstructions
    private val _scrollTo = MutableEvent<Int>()
    /** Controls whether the queue should be force-scrolled to a particular location. */
    val scrollTo: Event<Int>
        get() = _scrollTo

    private val _index = MutableStateFlow(playbackManager.index)
    /** The index of the currently playing song in the queue. */
    val index: StateFlow<Int>
        get() = _index

    private val _isInitialQueueLoaded = MutableStateFlow(false)
    private var rangeJob: Job? = null
    private var lastRequestedAnchor: Int? = null
    private var queueGeneration = 0L
    private var activePrimitiveWindow: QueueWindow? = null
    val isInitialQueueLoaded: StateFlow<Boolean>
        get() = _isInitialQueueLoaded

    init {
        playbackManager.addListener(this)
    }

    override fun onIndexMoved(index: Int) {
        L.d("Index moved, synchronizing and scrolling to new position")
        _scrollTo.put(index)
        _index.value = index
    }

    override fun onQueueChanged(queue: List<Song>, index: Int, change: QueueChange) {
        // Queue changed trivially due to item mo -> Diff queue, stay at current index.
        L.d("Updating queue display")
        queueGeneration++
        rangeJob?.cancel()
        activePrimitiveWindow = null
        _queueInstructions.put(change.instructions)
        _queue.value = queue.toDisplayItems()
        _isInitialQueueLoaded.value = true
        if (change.type != QueueChange.Type.MAPPING) {
            // Index changed, make sure it remains updated without actually scrolling to it.
            L.d("Index changed with queue, synchronizing new position")
            _index.value = index
        }
    }

    override fun onQueueReordered(queue: List<Song>, index: Int, isShuffled: Boolean) {
        // Queue changed completely -> Replace queue, update index
        L.d("Queue changed completely, replacing queue and position")
        queueGeneration++
        rangeJob?.cancel()
        activePrimitiveWindow = null
        _queueInstructions.put(UpdateInstructions.Replace(0))
        _scrollTo.put(index)
        _queue.value = queue.toDisplayItems()
        _index.value = index
        _isInitialQueueLoaded.value = true
    }

    override fun onNewPlayback(
        parent: MusicParent?,
        queue: List<Song>,
        index: Int,
        isShuffled: Boolean,
    ) {
        // Entirely new queue -> Replace queue, update index
        L.d("New playback, replacing queue and position")
        queueGeneration++
        rangeJob?.cancel()
        activePrimitiveWindow = null
        _queueInstructions.put(UpdateInstructions.Replace(0))
        _scrollTo.put(index)
        _queue.value = queue.toDisplayItems()
        _index.value = index
        _isInitialQueueLoaded.value = true
    }

    override fun onQueueWindowChanged(window: QueueWindow?) {
        if (window == null) return
        L.d("Updating bounded primitive queue display")
        queueGeneration++
        rangeJob?.cancel()
        activePrimitiveWindow = window
        _queueInstructions.put(UpdateInstructions.Replace(0))
        _queue.value =
            window.items.map { item ->
                QueueDisplayItem(
                    globalPosition = item.logicalPosition,
                    song = null,
                    primitive = item,
                )
            }
        _index.value = window.currentLocalPosition
        _scrollTo.put(window.currentLocalPosition)
        _isInitialQueueLoaded.value = true
    }

    fun requestAdjacentRange(firstVisible: Int, lastVisible: Int) {
        val activeWindow = activePrimitiveWindow ?: return
        val loaded = queue.value
        if (loaded.isEmpty() || rangeJob?.isActive == true) return
        val anchor =
            when {
                firstVisible in 0..QueueWindowPolicy.PREFETCH_DISTANCE ->
                    loaded.first().globalPosition
                lastVisible >= loaded.lastIndex - QueueWindowPolicy.PREFETCH_DISTANCE ->
                    loaded.last().globalPosition
                else -> return
            }
        if (anchor == lastRequestedAnchor) return
        lastRequestedAnchor = anchor
        val requestGen = queueGeneration
        rangeJob =
            viewModelScope.launch {
                val window =
                    withContext(Dispatchers.IO) {
                        persistenceRepository.readQueueWindowAround(activeWindow.descriptor, anchor)
                    } ?: return@launch
                if (requestGen != queueGeneration) {
                    L.d(
                        "Discarding stale primitive range result (generation $requestGen != $queueGeneration)"
                    )
                    return@launch
                }
                if (
                    window.descriptor.sessionId != activeWindow.descriptor.sessionId ||
                        window.descriptor.revision != activeWindow.descriptor.revision
                ) {
                    L.d("Discarding stale primitive range result (session/revision mismatch)")
                    return@launch
                }
                val currentItems = _queue.value
                val newItems =
                    window.items.map { item -> QueueDisplayItem(item.logicalPosition, null, item) }
                val mergedMap = currentItems.associateBy { it.globalPosition }.toMutableMap()
                for (item in newItems) {
                    mergedMap[item.globalPosition] = item
                }
                val mergedList = mergedMap.values.sortedBy { it.globalPosition }
                _queueInstructions.put(UpdateInstructions.Replace(0))
                _queue.value = mergedList
                _isInitialQueueLoaded.value = true
            }
    }

    override fun onCleared() {
        super.onCleared()
        playbackManager.removeListener(this)
    }

    /**
     * Start playing the the queue item at the given index.
     *
     * @param adapterIndex The index of the queue item to play. Does nothing if the index is out of
     *   range.
     */
    fun goto(adapterIndex: Int) {
        if (adapterIndex !in queue.value.indices) {
            return
        }
        val item = queue.value[adapterIndex]
        if (!item.editable) return
        val globalPosition = item.globalPosition
        L.d("Going to logical position $globalPosition in queue")
        playbackManager.goto(globalPosition)
    }

    /**
     * Remove a queue item at the given index.
     *
     * @param adapterIndex The index of the queue item to play. Does nothing if the index is out of
     *   range.
     */
    fun removeQueueDataItem(adapterIndex: Int) {
        if (adapterIndex !in queue.value.indices) {
            return
        }
        val item = queue.value[adapterIndex]
        if (!item.editable) return
        L.d("Removing item ${item.globalPosition} in queue")
        playbackManager.removeQueueItem(item.globalPosition)
    }

    /**
     * Move a queue item from one index to another index.
     *
     * @param adapterFrom The index of the queue item to move.
     * @param adapterTo The destination index for the queue item.
     * @return true if the items were moved, false otherwise.
     */
    fun moveQueueDataItems(adapterFrom: Int, adapterTo: Int): Boolean {
        if (adapterFrom !in queue.value.indices || adapterTo !in queue.value.indices) {
            return false
        }
        val from = queue.value[adapterFrom]
        val to = queue.value[adapterTo]
        if (!from.editable || !to.editable) return false
        L.d("Moving ${from.globalPosition} to ${to.globalPosition} in queue")
        playbackManager.moveQueueItem(from.globalPosition, to.globalPosition)
        return true
    }

    private fun List<Song>.toDisplayItems() = mapIndexed { index, song ->
        QueueDisplayItem(globalPosition = index, song = song, primitive = null)
    }
}
