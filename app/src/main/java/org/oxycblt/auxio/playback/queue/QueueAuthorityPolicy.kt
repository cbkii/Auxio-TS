/*
 * Copyright (c) 2026 Auxio Project
 * QueueAuthorityPolicy.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.oxycblt.auxio.playback.queue

import org.oxycblt.auxio.playback.persist.QueueDescriptor
import org.oxycblt.auxio.playback.persist.QueueItemRef

/** Pure acceptance rules for asynchronous primitive queue-window results. */
internal object QueueAuthorityPolicy {
    data class Request(
        val generation: Long,
        val sessionId: Long,
        val revision: Long,
    )

    fun request(generation: Long, descriptor: QueueDescriptor) =
        Request(generation, descriptor.sessionId, descriptor.revision)

    fun accepts(
        request: Request,
        currentGeneration: Long,
        activeDescriptor: QueueDescriptor?,
        resultDescriptor: QueueDescriptor,
        resultItems: List<QueueItemRef>,
    ): Boolean =
        request.generation == currentGeneration &&
            activeDescriptor != null &&
            activeDescriptor.sessionId == request.sessionId &&
            activeDescriptor.revision == request.revision &&
            resultDescriptor.sessionId == request.sessionId &&
            resultDescriptor.revision == request.revision &&
            !hasMissingRows(resultItems)

    fun hasMissingRows(items: List<QueueItemRef>): Boolean =
        items.any { item ->
            item.stableSongUid == null &&
                item.uri.isNullOrBlank() &&
                item.pathFallback.isNullOrBlank()
        }
}
