/*
 * Copyright (c) 2026 Auxio Project
 * HomePlayDeadlineGateTest.kt is part of Auxio.
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
    fun `focus admission consuming the remaining budget cannot commit playback`() {
        var now = 100L
        val gate = HomePlayDeadlineGate { now }
        gate.arm(200L)
        val deadline = requireNotNull(gate.expiresAt)
        assertTrue(gate.consumeIfReady(true))
        now = 200L // Simulated blocking audio-focus admission.
        assertFalse(gate.permitsCommit(deadline))
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
