/*
 * Copyright (c) 2026 Auxio Project
 * TopwaySeekPolicyConverterTest.kt is part of Auxio.
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
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class TopwaySeekPolicyConverterTest {
    @Test
    fun `explicit seek policies convert to milliseconds`() {
        assertDecision(1500L, TopwaySeekUnitPolicy.Milliseconds, 1500, 10_000L)
        assertDecision(2000L, TopwaySeekUnitPolicy.Seconds, 2, 10_000L)
        assertDecision(5000L, TopwaySeekUnitPolicy.Percent0To100, 50, 10_000L)
        assertDecision(2500L, TopwaySeekUnitPolicy.Permille0To1000, 250, 10_000L)
    }

    @Test
    fun `explicit policies clamp or ignore documented out of range values`() {
        assertDecision(10_000L, TopwaySeekUnitPolicy.Milliseconds, 50_000, 10_000L)
        assertDecision(10_000L, TopwaySeekUnitPolicy.Seconds, 50, 10_000L)
        assertIgnored(101, 10_000L, TopwaySeekUnitPolicy.Percent0To100, "outside 0..100")
        assertIgnored(1001, 10_000L, TopwaySeekUnitPolicy.Permille0To1000, "outside 0..1000")
    }

    @Test
    fun `auto maps normalized ranges before larger seconds and milliseconds fallbacks`() {
        assertDecision(
            5_000L,
            TopwaySeekUnitPolicy.Auto,
            50,
            10_000L,
            TopwaySeekUnitPolicy.Percent0To100,
        )
        assertDecision(
            2_500L,
            TopwaySeekUnitPolicy.Auto,
            250,
            10_000L,
            TopwaySeekUnitPolicy.Permille0To1000,
        )
        assertDecision(
            1_200_000L,
            TopwaySeekUnitPolicy.Auto,
            1200,
            2_000_000L,
            TopwaySeekUnitPolicy.Seconds,
        )
        assertDecision(
            10_000L,
            TopwaySeekUnitPolicy.Auto,
            12_000,
            10_000L,
            TopwaySeekUnitPolicy.Milliseconds,
        )
    }

    @Test
    fun `invalid auto seeks are ignored rather than clamped dangerously`() {
        assertIgnored(-5, 10_000L, TopwaySeekUnitPolicy.Auto, "negative")
        assertIgnored(50, 0L, TopwaySeekUnitPolicy.Auto, "unknown duration")
    }

    private fun assertDecision(
        expectedPositionMs: Long,
        policy: TopwaySeekUnitPolicy,
        raw: Any?,
        durationMs: Long,
        expectedUnit: TopwaySeekUnitPolicy = policy,
    ) {
        val decision = TopwaySeekPolicyConverter.convert(raw, durationMs, policy)
        assertEquals(expectedPositionMs, decision.positionMs)
        assertEquals(expectedUnit, decision.unit)
        assertTrue(decision.detail.contains("unit=$expectedUnit"))
    }

    private fun assertIgnored(
        raw: Any?,
        durationMs: Long,
        policy: TopwaySeekUnitPolicy,
        reason: String,
    ) {
        val decision = TopwaySeekPolicyConverter.convert(raw, durationMs, policy)
        assertNull(decision.positionMs)
        assertTrue(decision.detail.contains(reason))
    }
}
