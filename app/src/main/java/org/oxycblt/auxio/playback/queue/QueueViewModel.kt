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

    /** Stable user-navigation intent captured before a bounded primitive range can move. */
    data class NavigationTarget
    internal constructor(val globalPosition: Int, internal val generation: Long)

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
        // PlaybackStateManager listener replay is intentionally conditional. Read the canonical
        // state under the manager's own monitor after registration so a volatile UI created during
        // Fast Resume cannot miss an already-active primitive/rich queue transition.
        synchronized(playbackManager) { synchronizeCurrentQueue() }
    }

    override fun onIndexMoved(index: Int) {
        L.d("Index moved, synchronizing and scrolling to new position")
        if (activePrimitiveWindow != null) {
            // PlaybackStateManager reports the primitive index in global logical coordinates while
            // this ViewModel exposes adapter-local coordinates for the bounded window. Do not
            // transiently publish a global index as a local pager position while the replacement
            // window is still arriving.
            val localIndex = _queue.value.indexOfFirst { it.globalPosition == index }
            if (localIndex < 0) {
                L.d("Primitive index $index is outside the loaded window; awaiting window update")
                return
            }
            _scrollTo.put(localIndex)
            _index.value = localIndex
            return
        }
        _scrollTo.put(index)
        _index.value = index
    }

    override fun onQueueChanged(queue: List<Song>, index: Int, change: QueueChange) {
        // Queue changed trivially due to item move -> Diff queue, stay at current index.
        L.d("Updating queue display")
        invalidateQueueAuthority()
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
        invalidateQueueAuthority()
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
        invalidateQueueAuthority()
        _queueInstructions.put(UpdateInstructions.Replace(0))
        _scrollTo.put(index)
        _queue.value = queue.toDisplayItems()
        _index.value = index
        _isInitialQueueLoaded.value = true
    }

    override fun onQueueWindowChanged(window: QueueWindow?) {
        replacePrimitiveWindow(window)
        _queueInstructions.put(UpdateInstructions.Replace(0))
        if (window == null) {
            // A primitive authority can disappear while raw playback or a hydrated queue remains.
            // Re-read the manager rather than leaving stale primitive rows in volatile UI state.
            synchronized(playbackManager) { synchronizeCurrentQueue() }
            return
        }
        L.d("Updating bounded primitive queue display")
        _queue.value = window.toDisplayItems()
        _index.value = window.currentLocalPosition
        _scrollTo.put(window.currentLocalPosition)
        _isInitialQueueLoaded.value = true
    }

    override fun onSessionEnded() {
        L.d("Playback session ended, clearing queue presentation authority")
        invalidateQueueAuthority()
        _queueInstructions.consume()
        _scrollTo.consume()
        _queue.value = emptyList()
        _index.value = -1
        _isInitialQueueLoaded.value = false
    }

    fun requestAdjacentRange(firstVisible: Int, lastVisible: Int) {
        val activeWindow = activePrimitiveWindow ?: return
        val loaded = _queue.value
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
        val requestToken = PrimitiveQueueAuthority.token(queueGeneration, activeWindow.descriptor)
        rangeJob =
            viewModelScope.launch {
                val window =
                    withContext(Dispatchers.IO) {
                        persistenceRepository.readQueueWindowAround(activeWindow.descriptor, anchor)
                    }
                if (window == null) {
                    if (requestToken.generation == queueGeneration) lastRequestedAnchor = null
                    return@launch
                }
                if (
                    !PrimitiveQueueAuthority.accepts(
                        requestToken,
                        currentGeneration = queueGeneration,
                        activeDescriptor = activePrimitiveWindow?.descriptor,
                        returnedDescriptor = window.descriptor,
                    )
                ) {
                    L.d("Discarding stale primitive range result")
                    return@launch
                }

                val newItems = window.toDisplayItems()
                val merged =
                    PrimitiveQueueAuthority.mergeBounded(
                        current = _queue.value,
                        incoming = newItems,
                        anchorGlobalPosition = anchor,
                        maximumItems = QueueWindowPolicy.MAX_LOADED_ITEMS,
                    )
                _queueInstructions.put(UpdateInstructions.Replace(0))
                _queue.value = merged
                _index.value =
                    merged.indexOfFirst {
                        it.globalPosition == window.descriptor.currentLogicalPosition
                    }
                _isInitialQueueLoaded.value = true
            }
    }

    override fun onCleared() {
        rangeJob?.cancel()
        playbackManager.removeListener(this)
        super.onCleared()
    }

    /** Return the stable logical position currently represented by [adapterIndex], if playable. */
    fun globalPositionAt(adapterIndex: Int): Int? =
        _queue.value.getOrNull(adapterIndex)?.takeIf { it.editable }?.globalPosition

    /** Capture a queue-navigation intent before asynchronous primitive prefetch can move the UI. */
    fun navigationTargetAt(adapterIndex: Int): NavigationTarget? =
        _queue.value
            .getOrNull(adapterIndex)
            ?.takeIf { it.editable }
            ?.let { NavigationTarget(it.globalPosition, queueGeneration) }

    /**
     * Start playing the queue item represented by [adapterIndex]. The logical position is resolved
     * immediately from the current display snapshot.
     */
    fun goto(adapterIndex: Int) {
        val target = navigationTargetAt(adapterIndex) ?: return
        goto(target)
    }

    /**
     * Execute a previously captured user-navigation intent. Primitive targets are validated against
     * the queue authority generation and logical bounds rather than current bounded display
     * membership, so a valid swipe is not dropped if prefetch trims that row before the posted
     * command executes.
     */
    fun goto(target: NavigationTarget) {
        if (target.generation != queueGeneration) {
            L.d("Ignoring stale queue navigation target ${target.globalPosition}")
            return
        }
        val primitiveWindow = activePrimitiveWindow
        if (primitiveWindow != null) {
            if (target.globalPosition !in 0 until primitiveWindow.descriptor.totalCount) return
            L.d("Going to logical position ${target.globalPosition} in primitive queue")
            playbackManager.goto(target.globalPosition)
            return
        }
        gotoGlobalPosition(target.globalPosition)
    }

    /**
     * Start playing a known logical queue position if it is still represented by a playable item in
     * the current bounded/rich queue snapshot.
     */
    fun gotoGlobalPosition(globalPosition: Int) {
        val item =
            _queue.value.firstOrNull { it.globalPosition == globalPosition && it.editable }
                ?: return
        L.d("Going to logical position ${item.globalPosition} in queue")
        playbackManager.goto(item.globalPosition)
    }

    /**
     * Remove a queue item at the given index.
     *
     * @param adapterIndex The index of the queue item to play. Does nothing if the index is out of
     *   range.
     */
    fun removeQueueDataItem(adapterIndex: Int) {
        val currentQueue = _queue.value
        if (adapterIndex !in currentQueue.indices) return
        val item = currentQueue[adapterIndex]
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
        val currentQueue = _queue.value
        if (adapterFrom !in currentQueue.indices || adapterTo !in currentQueue.indices) {
            return false
        }
        val from = currentQueue[adapterFrom]
        val to = currentQueue[adapterTo]
        if (!from.editable || !to.editable) return false
        L.d("Moving ${from.globalPosition} to ${to.globalPosition} in queue")
        playbackManager.moveQueueItem(from.globalPosition, to.globalPosition)
        return true
    }

    private fun synchronizeCurrentQueue() {
        val primitiveWindow = playbackManager.queueWindow
        if (primitiveWindow != null) {
            activePrimitiveWindow = primitiveWindow
            _queue.value = primitiveWindow.toDisplayItems()
            _index.value = primitiveWindow.currentLocalPosition
            _isInitialQueueLoaded.value = true
            return
        }

        val richQueue = playbackManager.queue
        if (richQueue.isNotEmpty()) {
            activePrimitiveWindow = null
            _queue.value = richQueue.toDisplayItems()
            _index.value = playbackManager.index
            _isInitialQueueLoaded.value = true
        } else {
            activePrimitiveWindow = null
            _queue.value = emptyList()
            _index.value = playbackManager.index
            _isInitialQueueLoaded.value = false
        }
    }

    private fun invalidateQueueAuthority() {
        queueGeneration++
        cancelPrimitiveRangeRequest()
        activePrimitiveWindow = null
    }

    private fun replacePrimitiveWindow(nextWindow: QueueWindow?) {
        val previousDescriptor = activePrimitiveWindow?.descriptor
        val nextDescriptor = nextWindow?.descriptor
        val sameAuthority =
            previousDescriptor != null &&
                nextDescriptor != null &&
                previousDescriptor.sessionId == nextDescriptor.sessionId &&
                previousDescriptor.revision == nextDescriptor.revision
        if (!sameAuthority) queueGeneration++
        cancelPrimitiveRangeRequest()
        activePrimitiveWindow = nextWindow
    }

    private fun cancelPrimitiveRangeRequest() {
        rangeJob?.cancel()
        rangeJob = null
        lastRequestedAnchor = null
    }

    private fun QueueWindow.toDisplayItems() =
        items.map { item ->
            QueueDisplayItem(globalPosition = item.logicalPosition, song = null, primitive = item)
        }

    private fun List<Song>.toDisplayItems() = mapIndexed { index, song ->
        QueueDisplayItem(globalPosition = index, song = song, primitive = null)
    }
}
