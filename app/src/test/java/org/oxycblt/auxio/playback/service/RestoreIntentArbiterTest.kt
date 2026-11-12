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
    fun `expired bounded restore stays silent and drops fallback`() {
        var now = 100L
        val arbiter = RestoreIntentArbiter(elapsedMs = { now })
        arbiter.begin(
            DeferredPlayback.RestoreState(
                play = true,
                deadlineElapsedMs = 200L,
                fallback = DeferredPlayback.ShuffleAll(),
            )
        )
        assertTrue(arbiter.snapshot().play)
        now = 200L
        val committed = arbiter.finish()
        assertFalse(committed.play)
        assertNull(committed.fallback)
        assertEquals(200L, committed.deadlineElapsedMs)
        assertFalse(arbiter.snapshot().active)
    }

    @Test
    fun `cancel and explicit pause prevent stale bounded restore playback`() {
        val arbiter = RestoreIntentArbiter(elapsedMs = { 100L })
        arbiter.begin(DeferredPlayback.RestoreState(play = true, deadlineElapsedMs = 200L))
        arbiter.updatePlay(false)
        assertFalse(arbiter.finish().play)
        arbiter.begin(DeferredPlayback.RestoreState(play = true, deadlineElapsedMs = 200L))
        arbiter.cancel()
        assertFalse(arbiter.snapshot().active)
        assertFalse(arbiter.finish().play)
    }

    @Test
    fun `duplicate restore coalesces and play requests upgrade eventual intent`() {
        val arbiter = RestoreIntentArbiter()

        assertTrue(arbiter.begin(DeferredPlayback.RestoreState(play = false)))
        assertFalse(arbiter.begin(DeferredPlayback.RestoreState(play = true)))

        assertTrue(arbiter.snapshot().play)
        assertFalse(arbiter.begin(DeferredPlayback.RestoreState(play = false)))
        assertTrue(arbiter.snapshot().play)
    }

    @Test
    fun `prepare cannot downgrade pending play without explicit pause`() {
        val arbiter = RestoreIntentArbiter()

        arbiter.begin(DeferredPlayback.RestoreState(play = true))
        arbiter.begin(DeferredPlayback.RestoreState(play = false))
        assertTrue(arbiter.snapshot().play)

        assertTrue(arbiter.updatePlay(false))
        assertFalse(arbiter.snapshot().play)
    }

    @Test
    fun `seek and skip burst stay bounded with latest seek semantics`() {
        val arbiter = RestoreIntentArbiter(maxAbsoluteSkip = 3)
        arbiter.begin(DeferredPlayback.RestoreState(play = false, skipDelta = 1))
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

    @Test
    fun `initial restore action seeds cold skip intent`() {
        val arbiter = RestoreIntentArbiter()

        arbiter.begin(DeferredPlayback.RestoreState(play = false, skipDelta = -1))

        assertEquals(-1, arbiter.snapshot().skipDelta)
    }

    @Test
    fun `bounded Play joins an unbounded Play already owed and cannot expire it`() {
        var now = 100L
        val arbiter = RestoreIntentArbiter(elapsedMs = { now })
        val shuffle = DeferredPlayback.ShuffleAll()
        arbiter.begin(DeferredPlayback.RestoreState(play = true, fallback = shuffle))
        arbiter.begin(DeferredPlayback.RestoreState(play = true, deadlineElapsedMs = 200L))
        now = 5_000L
        val committed = arbiter.finish()
        assertTrue(committed.play)
        assertNull(committed.deadlineElapsedMs)
        assertEquals(shuffle, committed.fallback)
    }

    @Test
    fun `bounded Play still bounds a passive restore and a later explicit Play lifts the bound`() {
        var now = 100L
        val arbiter = RestoreIntentArbiter(elapsedMs = { now })
        arbiter.begin(DeferredPlayback.RestoreState(play = false))
        arbiter.begin(DeferredPlayback.RestoreState(play = true, deadlineElapsedMs = 200L))
        assertEquals(200L, arbiter.snapshot().deadlineElapsedMs)
        arbiter.begin(DeferredPlayback.RestoreState(play = true))
        now = 5_000L
        val committed = arbiter.finish()
        assertTrue(committed.play)
        assertNull(committed.deadlineElapsedMs)
    }

    @Test
    fun `a newer bounded Play replaces an earlier bounded Play`() {
        var now = 100L
        val arbiter = RestoreIntentArbiter(elapsedMs = { now })
        arbiter.begin(DeferredPlayback.RestoreState(play = true, deadlineElapsedMs = 200L))
        now = 250L
        arbiter.begin(DeferredPlayback.RestoreState(play = true, deadlineElapsedMs = 900L))
        assertTrue(arbiter.snapshot().play)
        assertEquals(900L, arbiter.snapshot().deadlineElapsedMs)
    }
}
