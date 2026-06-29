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
