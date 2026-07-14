/*
 * Copyright (c) 2026 Auxio Project
 * PerfTimerTest.kt is part of Auxio.
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
