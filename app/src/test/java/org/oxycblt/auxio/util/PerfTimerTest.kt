/*
 * Copyright (c) 2026 Auxio Project
 * PerfTimerTest.kt is part of Auxio.
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

package org.oxycblt.auxio.util

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PerfTimerTest {
    @After
    fun tearDown() {
        PerfTimer.clear()
        PerfTimer.configure(false)
    }

    @Test
    fun `capture is bounded to the newest 256 events`() {
        PerfTimer.configure(true)
        repeat(300) { PerfTimer.point("event-$it") }

        val events = PerfTimer.snapshot()
        assertEquals(256, events.size)
        assertEquals("event-44", events.first().label)
        assertEquals("event-299", events.last().label)
    }

    @Test
    fun `trace records duration and thread`() {
        PerfTimer.configure(true)
        PerfTimer.trace("work") { Unit }

        val event = PerfTimer.snapshot().last()
        assertEquals("work", event.label)
        assertTrue(event.durationMs != null && event.durationMs >= 0L)
        assertTrue(event.threadName.isNotBlank())
    }
}
