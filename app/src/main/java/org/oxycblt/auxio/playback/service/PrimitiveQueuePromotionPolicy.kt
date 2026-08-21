/*
 * Copyright (c) 2026 Auxio Project
 * PrimitiveQueuePromotionPolicy.kt is part of Auxio.
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

package org.oxycblt.auxio.playback.service

import org.oxycblt.auxio.playback.persist.QueueDescriptor
import org.oxycblt.auxio.playback.persist.QueueItemRef
import org.oxycblt.auxio.playback.state.ShuffleScope

/**
 * Validates the persisted Fast Resume queue topology before it is promoted back to hydrated Songs.
 *
 * Primitive queue items are stored in logical playback order. [QueueItemRef.canonicalPosition]
 * points back to the unshuffled heap. Promotion must reconstruct both views rather than applying a
 * second shuffle to an already shuffled logical list.
 */
internal object PrimitiveQueuePromotionPolicy {
    data class Layout(
        val itemsByCanonicalPosition: List<QueueItemRef>,
        val shuffledMapping: List<Int>,
    ) {
        fun heapIndexForLogicalPosition(logicalPosition: Int): Int? =
            if (shuffledMapping.isEmpty()) {
                logicalPosition.takeIf { it in itemsByCanonicalPosition.indices }
            } else {
                shuffledMapping.getOrNull(logicalPosition)
            }
    }

    fun layout(descriptor: QueueDescriptor, items: List<QueueItemRef>): Layout? {
        if (!descriptor.hasCurrentItem || items.size != descriptor.totalCount) return null
        if (items.map { it.logicalPosition } != (0 until descriptor.totalCount).toList()) return null

        val byCanonical = arrayOfNulls<QueueItemRef>(descriptor.totalCount)
        for (item in items) {
            val canonical = item.canonicalPosition
            if (canonical !in byCanonical.indices || byCanonical[canonical] != null) return null
            byCanonical[canonical] = item
        }
        val canonicalItems = byCanonical.map { it ?: return null }
        val mapping =
            if (descriptor.shuffleScope == ShuffleScope.OFF) {
                emptyList()
            } else {
                items.map { it.canonicalPosition }
            }
        val currentHeapIndex =
            if (mapping.isEmpty()) descriptor.currentLogicalPosition
            else mapping.getOrNull(descriptor.currentLogicalPosition) ?: return null
        if (currentHeapIndex !in canonicalItems.indices) return null
        return Layout(canonicalItems, mapping)
    }
}
