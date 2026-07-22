/*
 * Copyright (c) 2026 Auxio Project
 * ExportedCommandRateLimiterTest.kt is part of Auxio.
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

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExportedCommandRateLimiterTest {
    @Test
    fun `allows bounded burst and resets after window`() {
        var now = 100L
        val limiter = EventRateLimiter { now }

        assertTrue(limiter.allow("media", maxEvents = 2, windowMs = 1000))
        assertTrue(limiter.allow("media", maxEvents = 2, windowMs = 1000))
        assertFalse(limiter.allow("media", maxEvents = 2, windowMs = 1000))

        now += 1000
        assertTrue(limiter.allow("media", maxEvents = 2, windowMs = 1000))
    }

    @Test
    fun `keeps independent windows per exported surface`() {
        val limiter = EventRateLimiter { 500L }

        assertTrue(limiter.allow("topway", maxEvents = 1, windowMs = 1000))
        assertFalse(limiter.allow("topway", maxEvents = 1, windowMs = 1000))
        assertTrue(limiter.allow("bluetooth", maxEvents = 1, windowMs = 1000))
    }

    @Test
    fun `monotonic clock rollback starts a fresh window`() {
        var now = 500L
        val limiter = EventRateLimiter { now }
        assertTrue(limiter.allow("media", maxEvents = 1, windowMs = 1000))
        assertFalse(limiter.allow("media", maxEvents = 1, windowMs = 1000))

        now = 100L
        assertTrue(limiter.allow("media", maxEvents = 1, windowMs = 1000))
    }
}
