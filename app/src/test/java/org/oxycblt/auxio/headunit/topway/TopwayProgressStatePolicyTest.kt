/*
 * Copyright (c) 2024 Auxio Project
 * TopwayProgressStatePolicyTest.kt is part of Auxio.
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

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class TopwayProgressStatePolicyTest {
    @Test
    fun `active progress clamps values and rejects unknown duration`() {
        assertEquals(
            TopwayProgressSnapshot(0L, 5_000L),
            TopwayProgressStatePolicy.active(-1L, 5_000L),
        )
        assertEquals(
            TopwayProgressSnapshot(5_000L, 5_000L),
            TopwayProgressStatePolicy.active(9_000L, 5_000L),
        )
        assertNull(TopwayProgressStatePolicy.active(1_000L, 0L))
        assertNull(TopwayProgressStatePolicy.active(1_000L, -1L))
    }

    @Test
    fun `publish policy allows first snapshot`() {
        val next = TopwayProgressSnapshot(1_000L, 5_000L)
        assertTrue(TopwayProgressStatePolicy.shouldPublish(next, null, 10_000L, 0L, 1_000L))
    }

    @Test
    fun `publish policy allows transition from CLEAR`() {
        val next = TopwayProgressSnapshot(1_000L, 5_000L)
        assertTrue(TopwayProgressStatePolicy.shouldPublish(next, TopwayProgressStatePolicy.CLEAR, 10_000L, 9_900L, 1_000L))
    }

    @Test
    fun `publish policy allows duration changes`() {
        val last = TopwayProgressSnapshot(1_000L, 5_000L)
        val next = TopwayProgressSnapshot(1_100L, 6_000L)
        assertTrue(TopwayProgressStatePolicy.shouldPublish(next, last, 10_100L, 10_000L, 1_000L))
    }

    @Test
    fun `publish policy suppresses normal progress within interval`() {
        val last = TopwayProgressSnapshot(1_000L, 5_000L)
        val next = TopwayProgressSnapshot(1_100L, 5_000L)
        assertFalse(TopwayProgressStatePolicy.shouldPublish(next, last, 10_100L, 10_000L, 1_000L))
    }

    @Test
    fun `publish policy allows normal progress after interval`() {
        val last = TopwayProgressSnapshot(1_000L, 5_000L)
        val next = TopwayProgressSnapshot(2_100L, 5_000L)
        assertTrue(TopwayProgressStatePolicy.shouldPublish(next, last, 11_100L, 10_000L, 1_000L))
    }

    @Test
    fun `publish policy allows seeks within interval`() {
        val last = TopwayProgressSnapshot(1_000L, 5_000L)
        val next = TopwayProgressSnapshot(3_000L, 5_000L)
        assertTrue(TopwayProgressStatePolicy.shouldPublish(next, last, 10_100L, 10_000L, 1_000L))
    }

    @Test
    fun `publish policy handles negative elapsed time conservatively`() {
        val last = TopwayProgressSnapshot(1_000L, 5_000L)
        val next = TopwayProgressSnapshot(1_100L, 5_000L)
        assertFalse(TopwayProgressStatePolicy.shouldPublish(next, last, 9_000L, 10_000L, 1_000L))
    }
}
