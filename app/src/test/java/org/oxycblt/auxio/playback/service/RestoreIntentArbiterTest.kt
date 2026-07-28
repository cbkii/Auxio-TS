/*
 * Copyright (c) 2026 Auxio Project
 * RestoreIntentArbiterTest.kt is part of Auxio.
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

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.oxycblt.auxio.playback.state.DeferredPlayback

class RestoreIntentArbiterTest {
    @Test
    fun `duplicate restore coalesces and latest play state wins`() {
        val arbiter = RestoreIntentArbiter()

        assertTrue(arbiter.begin(DeferredPlayback.RestoreState(play = false)))
        assertFalse(arbiter.begin(DeferredPlayback.RestoreState(play = true)))

        assertTrue(arbiter.snapshot().play)
    }

    @Test
    fun `seek and skip burst stay bounded with latest seek semantics`() {
        val arbiter = RestoreIntentArbiter(maxAbsoluteSkip = 3)
        arbiter.begin(DeferredPlayback.RestoreState(play = false))
        repeat(20) { arbiter.addSkip(1) }
        arbiter.updateSeek(9_000L)
        arbiter.addSkip(-1)

        val snapshot = arbiter.snapshot()
        assertEquals(2, snapshot.skipDelta)
        assertNull(snapshot.seekPositionMs)
    }

    @Test
    fun `finish atomically consumes pending intent`() {
        val fallback = DeferredPlayback.ShuffleAll()
        val arbiter = RestoreIntentArbiter()
        arbiter.begin(DeferredPlayback.RestoreState(play = true, fallback = fallback))
        arbiter.updateSeek(4_000L)

        val finished = arbiter.finish()

        assertEquals(fallback, finished.fallback)
        assertEquals(4_000L, finished.seekPositionMs)
        assertFalse(arbiter.snapshot().active)
    }
}
