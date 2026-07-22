/*
 * Copyright (c) 2026 Auxio Project
 * PrimitiveQueueAuthority.kt is part of Auxio.
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

import org.oxycblt.auxio.playback.persist.QueueDescriptor

/** Pure policy for accepting and bounding asynchronous primitive queue range reads. */
internal object PrimitiveQueueAuthority {
    data class RequestToken(val generation: Long, val sessionId: Long, val revision: Long)

    fun token(generation: Long, descriptor: QueueDescriptor) =
        RequestToken(generation, descriptor.sessionId, descriptor.revision)

    fun accepts(
        token: RequestToken,
        currentGeneration: Long,
        activeDescriptor: QueueDescriptor?,
        returnedDescriptor: QueueDescriptor,
    ): Boolean =
        token.generation == currentGeneration &&
            activeDescriptor?.sessionId == token.sessionId &&
            activeDescriptor.revision == token.revision &&
            returnedDescriptor.sessionId == token.sessionId &&
            returnedDescriptor.revision == token.revision

    fun mergeBounded(
        current: List<QueueDisplayItem>,
        incoming: List<QueueDisplayItem>,
        anchorGlobalPosition: Int,
        maximumItems: Int,
    ): List<QueueDisplayItem> {
        require(maximumItems > 0) { "maximumItems must be positive" }
        val merged = current.associateByTo(linkedMapOf()) { it.globalPosition }
        incoming.forEach { candidate ->
            val existing = merged[candidate.globalPosition]
            // A range read may contain an unresolved row while richer state is already visible.
            // Never downgrade a playable rich/primitive item to a non-targetable placeholder.
            if (candidate.editable || existing == null || !existing.editable) {
                merged[candidate.globalPosition] = candidate
            }
        }
        val sorted = merged.values.sortedBy { it.globalPosition }
        if (sorted.size <= maximumItems) return sorted

        val anchorIndex =
            sorted.indexOfFirst { it.globalPosition >= anchorGlobalPosition }.let { index ->
                if (index >= 0) index else sorted.lastIndex
            }
        val start =
            (anchorIndex - maximumItems / 2).coerceIn(0, sorted.size - maximumItems)
        return sorted.subList(start, start + maximumItems)
    }
}
