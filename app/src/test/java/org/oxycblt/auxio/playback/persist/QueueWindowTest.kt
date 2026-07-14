/*
 * Copyright (c) 2026 Auxio Project
 * QueueWindowTest.kt is part of Auxio.
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

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.oxycblt.auxio.playback.state.RepeatMode
import org.oxycblt.auxio.playback.state.ShuffleScope

class QueueWindowTest {
    @Test
    fun `window translates global and local positions`() {
        val window = window(total = 100, current = 51, start = 25, count = 51)

        assertEquals(26, window.currentLocalPosition)
        assertEquals(25, window.localToGlobal(0))
        assertEquals(75, window.localToGlobal(50))
        assertEquals(0, window.globalToLocal(25))
        assertEquals(50, window.globalToLocal(75))
        assertNull(window.globalToLocal(24))
        assertNull(window.localToGlobal(51))
    }

    @Test
    fun `initial policy remains bounded at beginning middle and end`() {
        assertEquals(0 until 51, QueueWindowPolicy.around(100, 0).asRange())
        assertEquals(25 until 76, QueueWindowPolicy.around(100, 50).asRange())
        assertEquals(49 until 100, QueueWindowPolicy.around(100, 99).asRange())
        assertEquals(0 until 5, QueueWindowPolicy.around(5, 3).asRange())
    }

    @Test
    fun `prefetch policy detects both logical boundaries`() {
        val middle = window(total = 200, current = 52, start = 50, count = 51)
        assertTrue(QueueWindowPolicy.shouldPrefetchBefore(middle, 52))
        assertFalse(QueueWindowPolicy.shouldPrefetchAfter(middle, 52))
        assertTrue(QueueWindowPolicy.shouldPrefetchAfter(middle, 98))

        val beginning = window(total = 40, current = 0, start = 0, count = 40)
        assertFalse(QueueWindowPolicy.shouldPrefetchBefore(beginning, 0))
        assertFalse(QueueWindowPolicy.shouldPrefetchAfter(beginning, 39))
    }

    private fun window(total: Int, current: Int, start: Int, count: Int): QueueWindow {
        val descriptor =
            QueueDescriptor(
                sessionId = 1L,
                totalCount = total,
                currentLogicalPosition = current,
                positionMs = 0L,
                repeatMode = RepeatMode.NONE,
                shuffleScope = ShuffleScope.OFF,
                revision = 1L,
                updatedAtMs = 1L,
            )
        return QueueWindow(
            descriptor = descriptor,
            startLogicalPosition = start,
            items =
                (start until start + count).map { logical ->
                    QueueItemRef(
                        logicalPosition = logical,
                        canonicalPosition = logical,
                        stableSongUid = null,
                        uri = "content://queue/$logical",
                        pathFallback = null,
                        titleFallback = "Track $logical",
                        artistFallback = null,
                        albumFallback = null,
                        durationMs = 1_000L,
                    )
                },
        )
    }

    private fun QueueWindowPolicy.Range.asRange() = startInclusive until endExclusive
}
