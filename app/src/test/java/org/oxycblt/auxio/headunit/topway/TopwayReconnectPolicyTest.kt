/*
 * Copyright (c) 2026 Auxio Project
 * TopwayReconnectPolicyTest.kt is part of Auxio.
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
package org.oxycblt.auxio.headunit.topway

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TopwayReconnectPolicyTest {
    @Test
    fun `transient failures use bounded fast retry burst`() {
        var attempt = 0
        val expected = listOf(500L, 1_500L, 3_000L)

        expected.forEach { expectedDelay ->
            val decision = TopwayReconnectPolicy.next(attempt)
            assertEquals(expectedDelay, decision.delayMs)
            assertFalse(decision.cooldown)
            attempt = decision.nextAttempt
        }

        assertEquals(3, attempt)
    }

    @Test
    fun `exhausted burst rearms after quiet cooldown`() {
        val decision = TopwayReconnectPolicy.next(3)
        assertEquals(TopwayReconnectPolicy.COOLDOWN_DELAY_MS, decision.delayMs)
        assertEquals(0, decision.nextAttempt)
        assertTrue(decision.cooldown)

        val restarted = TopwayReconnectPolicy.next(decision.nextAttempt)
        assertEquals(500L, restarted.delayMs)
        assertFalse(restarted.cooldown)
    }

    @Test
    fun `negative attempt is treated as first retry`() {
        val decision = TopwayReconnectPolicy.next(-1)
        assertEquals(500L, decision.delayMs)
        assertEquals(1, decision.nextAttempt)
        assertFalse(decision.cooldown)
    }
}
