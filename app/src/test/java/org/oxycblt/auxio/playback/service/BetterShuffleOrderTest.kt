/*
 * Copyright (c) 2026 Auxio Project
 * BetterShuffleOrderTest.kt is part of Auxio.
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

import androidx.media3.common.C
import org.junit.Assert.assertEquals
import org.junit.Test

class BetterShuffleOrderTest {
    @Test
    fun `test sequential insertion preserves intended play-next ordering`() {
        val order = BetterShuffleOrder(intArrayOf(0, 2, 1))
        val newOrder = order.cloneAndInsert(1, 2)
        assertEquals(5, newOrder.length)
        val indices = mutableSetOf<Int>()
        var current = newOrder.firstIndex
        while (current != C.INDEX_UNSET) {
            indices.add(current)
            current = newOrder.getNextIndex(current)
        }
        assertEquals(setOf(0, 1, 2, 3, 4), indices)
    }

    @Test
    fun `test cloneAndRemove`() {
        val order = BetterShuffleOrder(intArrayOf(0, 2, 1))
        val newOrder = order.cloneAndRemove(1, 2) // remove index 1
        assertEquals(2, newOrder.length)
        val indices = mutableSetOf<Int>()
        var current = newOrder.firstIndex
        while (current != C.INDEX_UNSET) {
            indices.add(current)
            current = newOrder.getNextIndex(current)
        }
        assertEquals(setOf(0, 1), indices)
    }
}
