/*
 * Copyright (c) 2026 Auxio Project
 * QueueWindow.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.oxycblt.auxio.playback.persist

import org.oxycblt.auxio.playback.state.RepeatMode
import org.oxycblt.auxio.playback.state.ShuffleScope
import org.oxycblt.musikr.Music

/** Persistent logical queue metadata that is independent of a hydrated Musikr library. */
data class QueueDescriptor(
    val sessionId: Long,
    val totalCount: Int,
    val currentLogicalPosition: Int,
    val positionMs: Long,
    val repeatMode: RepeatMode,
    val shuffleScope: ShuffleScope,
    val revision: Long,
    val updatedAtMs: Long,
) {
    val hasCurrentItem: Boolean
        get() = totalCount > 0 && currentLogicalPosition in 0 until totalCount
}

/** Primitive persisted queue item that remains useful before a [org.oxycblt.musikr.Song] exists. */
data class QueueItemRef(
    val logicalPosition: Int,
    val canonicalPosition: Int,
    val stableSongUid: Music.UID?,
    val uri: String?,
    val pathFallback: String?,
    val titleFallback: String?,
    val artistFallback: String?,
    val albumFallback: String?,
    val durationMs: Long,
) {
    val displayTitle: String
        get() =
            titleFallback?.takeIf { it.isNotBlank() }
                ?: pathFallback?.substringAfterLast('/')
                ?: uri?.substringAfterLast('/')
                ?: "Unavailable queue item"

    val displayArtist: String
        get() = artistFallback?.takeIf { it.isNotBlank() } ?: "USB audio"

    val hasPlayableReference: Boolean
        get() = !uri.isNullOrBlank() || !pathFallback.isNullOrBlank()
}

/** A bounded overlapping portion of the persistent logical queue. */
data class QueueWindow(
    val descriptor: QueueDescriptor,
    val startLogicalPosition: Int,
    val items: List<QueueItemRef>,
) {
    init {
        require(startLogicalPosition >= 0) { "Queue window start must be non-negative" }
        require(items.size <= descriptor.totalCount) { "Queue window exceeds logical queue size" }
        require(
            items.withIndex().all { (offset, item) ->
                item.logicalPosition == startLogicalPosition + offset
            }
        ) {
            "Queue window items must be contiguous and ordered"
        }
    }

    val endLogicalPositionExclusive: Int
        get() = startLogicalPosition + items.size

    val currentLocalPosition: Int
        get() = globalToLocal(descriptor.currentLogicalPosition) ?: -1

    val currentItem: QueueItemRef?
        get() = items.getOrNull(currentLocalPosition)

    fun containsGlobal(logicalPosition: Int): Boolean =
        logicalPosition in startLogicalPosition until endLogicalPositionExclusive

    fun globalToLocal(logicalPosition: Int): Int? =
        (logicalPosition - startLogicalPosition).takeIf { it in items.indices }

    fun localToGlobal(localPosition: Int): Int? =
        localPosition.takeIf { it in items.indices }?.plus(startLogicalPosition)
}

/** Named queue-window sizing policy used by persistence, player and UI callers. */
object QueueWindowPolicy {
    const val INITIAL_RADIUS = 25
    const val PREFETCH_DISTANCE = 5
    const val MAX_LOADED_ITEMS = 75

    data class Range(val startInclusive: Int, val endExclusive: Int) {
        val size: Int
            get() = (endExclusive - startInclusive).coerceAtLeast(0)
    }

    fun around(totalCount: Int, anchor: Int, radius: Int = INITIAL_RADIUS): Range {
        if (totalCount <= 0) return Range(0, 0)
        val safeAnchor = anchor.coerceIn(0, totalCount - 1)
        val desired = (radius * 2 + 1).coerceAtMost(totalCount)
        var start = (safeAnchor - radius).coerceAtLeast(0)
        var end = (start + desired).coerceAtMost(totalCount)
        start = (end - desired).coerceAtLeast(0)
        return Range(start, end)
    }

    fun shouldPrefetchBefore(window: QueueWindow, globalPosition: Int): Boolean =
        window.startLogicalPosition > 0 &&
            globalPosition - window.startLogicalPosition <= PREFETCH_DISTANCE

    fun shouldPrefetchAfter(window: QueueWindow, globalPosition: Int): Boolean =
        window.endLogicalPositionExclusive < window.descriptor.totalCount &&
            window.endLogicalPositionExclusive - 1 - globalPosition <= PREFETCH_DISTANCE
}
