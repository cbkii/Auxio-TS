package org.oxycblt.auxio.playback.service

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HomePlayDeadlineGateTest {
    @Test
    fun `restoration before deadline cannot play until decoder is ready and commits once`() {
        val gate = HomePlayDeadlineGate { 100L }
        gate.arm(200L)
        assertFalse(gate.consumeIfReady(false))
        assertTrue(gate.consumeIfReady(true))
        assertFalse(gate.consumeIfReady(true))
    }

    @Test
    fun `decoder readiness at or after expiry never commits late playback`() {
        var now = 100L
        val gate = HomePlayDeadlineGate { now }
        gate.arm(200L)
        now = 200L
        assertFalse(gate.consumeIfReady(true))
        gate.arm(199L)
        assertFalse(gate.consumeIfReady(true))
    }

    @Test
    fun `cancellation and stale expiry cannot revive or cancel replacement intent`() {
        var now = 100L
        val gate = HomePlayDeadlineGate { now }
        gate.arm(200L)
        gate.cancel()
        assertFalse(gate.consumeIfReady(true))
        gate.arm(400L)
        now = 250L
        gate.expire(200L)
        assertTrue(gate.consumeIfReady(true))
    }
}
