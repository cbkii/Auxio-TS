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

/** A queue row backed either by a hydrated song or a valid primitive reference. */
data class QueueDisplayItem(
    val globalPosition: Int,
    val song: Song?,
    val primitive: QueueItemRef?,
) {
    val editable: Boolean
        get() = song != null || primitive?.hasPlayableReference == true
}

/**
 * Manages the visible queue while enforcing one explicit rich-or-primitive display authority.
 *
 * Every asynchronous primitive read captures a monotonic generation and descriptor. A newer rich
 * queue, primitive session, reorder, or mutation invalidates that read before it can publish.
 */
@HiltViewModel
class QueueViewModel
@Inject
constructor(
    private val playbackManager: PlaybackStateManager,
    private val persistenceRepository: PersistenceRepository,
) : ViewModel(), PlaybackStateManager.Listener {

    private val _queue = MutableStateFlow(listOf<QueueDisplayItem>())
    val queue: StateFlow<List<QueueDisplayItem>> = _queue

    private val _queueInstructions = MutableEvent<UpdateInstructions>()
    val queueInstructions: Event<UpdateInstructions> = _queueInstructions

    private val _scrollTo = MutableEvent<Int>()
    val scrollTo: Event<Int>
        get() = _scrollTo

    private val _index = MutableStateFlow(playbackManager.index)
    val index: StateFlow<Int>
        get() = _index

    private val _isInitialQueueLoaded = MutableStateFlow(false)
    val isInitialQueueLoaded: StateFlow<Boolean>
        get() = _isInitialQueueLoaded

    private var rangeJob: Job? = null
    private var lastRequestedAnchor: Int? = null
    private var queueGeneration = 0L
    private var activePrimitiveWindow: QueueWindow? = null

    init {
        playbackManager.addListener(this)
    }

    override fun onIndexMoved(index: Int) {
        L.d("Index moved, synchronizing and scrolling to new position")
        _scrollTo.put(index)
        _index.value = index
    }

    override fun onQueueChanged(queue: List<Song>, index: Int, change: QueueChange) {
        L.d("Updating rich queue display")
        beginRichAuthority()
        _queueInstructions.put(change.instructions)
        _queue.value = queue.toDisplayItems()
        _isInitialQueueLoaded.value = true
        if (change.type != QueueChange.Type.MAPPING) {
            _index.value = index
        }
    }

    override fun onQueueReordered(queue: List<Song>, index: Int, isShuffled: Boolean) {
        L.d("Rich queue reordered; replacing queue and position")
        beginRichAuthority()
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
        L.d("New rich playback; replacing queue and position")
        beginRichAuthority()
        _queueInstructions.put(UpdateInstructions.Replace(0))
        _scrollTo.put(index)
        _queue.value = queue.toDisplayItems()
        _index.value = index
        _isInitialQueueLoaded.value = true
    }

    override fun onQueueWindowChanged(window: QueueWindow?) {
        if (window == null) {
            if (activePrimitiveWindow != null) {
                L.d("Primitive queue authority ended")
                invalidatePendingRange()
                activePrimitiveWindow = null
            }
            return
        }
        if (window.hasMissingRows()) {
            L.w(
                "Rejecting incomplete primitive queue window " +
                    "session=${window.descriptor.sessionId} revision=${window.descriptor.revision}"
            )
            invalidatePendingRange()
            activePrimitiveWindow = null
            return
        }

        L.d("Updating bounded primitive queue display")
        invalidatePendingRange()
        activePrimitiveWindow = window
        _queueInstructions.put(UpdateInstructions.Replace(0))
        _queue.value = window.toDisplayItems()
        _index.value = window.currentLocalPosition
        _scrollTo.put(window.currentLocalPosition)
        _isInitialQueueLoaded.value = true
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

        val requestGeneration = queueGeneration
        val requestDescriptor = activeWindow.descriptor
        rangeJob =
            viewModelScope.launch {
                val window =
                    withContext(Dispatchers.IO) {
                        persistenceRepository.readQueueWindowAround(requestDescriptor, anchor)
                    } ?: return@launch

                val stillActive = activePrimitiveWindow
                if (
                    requestGeneration != queueGeneration ||
                        stillActive == null ||
                        stillActive.descriptor.sessionId != requestDescriptor.sessionId ||
                        stillActive.descriptor.revision != requestDescriptor.revision
                ) {
                    L.d("Discarding stale primitive range result")
                    return@launch
                }
                if (
                    window.descriptor.sessionId != requestDescriptor.sessionId ||
                        window.descriptor.revision != requestDescriptor.revision
                ) {
                    L.d("Discarding primitive range with mismatched session/revision")
                    return@launch
                }
                if (window.hasMissingRows()) {
                    L.w(
                        "Discarding incomplete primitive range " +
                            "session=${window.descriptor.sessionId} revision=${window.descriptor.revision}"
                    )
                    return@launch
                }

                val newItems = window.toDisplayItems()
                val mergedList =
                    (_queue.value + newItems)
                        .associateBy { it.globalPosition }
                        .values
                        .sortedBy { it.globalPosition }
                _queueInstructions.put(UpdateInstructions.Replace(0))
                _queue.value = mergedList
                _isInitialQueueLoaded.value = true
            }
    }

    override fun onCleared() {
        invalidatePendingRange()
        playbackManager.removeListener(this)
        super.onCleared()
    }

    fun goto(adapterIndex: Int) {
        val currentQueue = _queue.value
        val item = currentQueue.getOrNull(adapterIndex) ?: return
        if (!item.editable) return
        L.d("Going to logical position ${item.globalPosition} in queue")
        playbackManager.goto(item.globalPosition)
    }

    fun removeQueueDataItem(adapterIndex: Int) {
        val currentQueue = _queue.value
        val item = currentQueue.getOrNull(adapterIndex) ?: return
        if (!item.editable) return
        L.d("Removing item ${item.globalPosition} in queue")
        playbackManager.removeQueueItem(item.globalPosition)
    }

    fun moveQueueDataItems(adapterFrom: Int, adapterTo: Int): Boolean {
        val currentQueue = _queue.value
        val from = currentQueue.getOrNull(adapterFrom) ?: return false
        val to = currentQueue.getOrNull(adapterTo) ?: return false
        if (!from.editable || !to.editable) return false
        L.d("Moving ${from.globalPosition} to ${to.globalPosition} in queue")
        playbackManager.moveQueueItem(from.globalPosition, to.globalPosition)
        return true
    }

    private fun beginRichAuthority() {
        invalidatePendingRange()
        activePrimitiveWindow = null
    }

    private fun invalidatePendingRange() {
        queueGeneration++
        rangeJob?.cancel()
        rangeJob = null
        lastRequestedAnchor = null
    }

    private fun QueueWindow.hasMissingRows(): Boolean =
        items.any { item ->
            item.stableSongUid == null &&
                item.uri.isNullOrBlank() &&
                item.pathFallback.isNullOrBlank()
        }

    private fun QueueWindow.toDisplayItems() =
        items.map { item ->
            QueueDisplayItem(globalPosition = item.logicalPosition, song = null, primitive = item)
        }

    private fun List<Song>.toDisplayItems() =
        mapIndexed { index, song ->
            QueueDisplayItem(globalPosition = index, song = song, primitive = null)
        }
}
