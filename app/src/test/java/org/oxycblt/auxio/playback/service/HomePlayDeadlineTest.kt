/*
 * Copyright (c) 2026 Auxio Project
 * HomePlayDeadlineTest.kt is part of Auxio.
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
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.oxycblt.auxio.playback.state.DeferredPlayback

class HomePlayDeadlineTest {
    @Test
    fun expiredPlayCannotActivateAudioOrFallbackAfterSlowRestore() {
        var now = 100L
        val arbiter = RestoreIntentArbiter(elapsedMs = { now })
        arbiter.begin(
            DeferredPlayback.RestoreState(
                play = true,
                fallback = DeferredPlayback.ShuffleAll(),
                deadlineElapsedMs = 1_000L,
            )
        )
        assertTrue(arbiter.snapshot().play)
        now = 1_000L
        val attached = arbiter.finish()
        assertFalse(attached.play)
        assertNull(attached.fallback)
    }

    @Test
    fun prepareCannotExtendDeadlineButNewExplicitPlayCanSupersedeIt() {
        var now = 100L
        val arbiter = RestoreIntentArbiter(elapsedMs = { now })
        arbiter.begin(DeferredPlayback.RestoreState(play = true, deadlineElapsedMs = 1_000L))
        arbiter.begin(DeferredPlayback.RestoreState(play = false))
        now = 1_001L
        assertFalse(arbiter.snapshot().play)
        arbiter.updatePlay(true)
        assertTrue(arbiter.finish().play)
    }

    @Test
    fun aNewBoundedPlayUsesItsOwnDeadlineAcrossTheSameRestore() {
        var now = 100L
        val arbiter = RestoreIntentArbiter(elapsedMs = { now })
        arbiter.begin(DeferredPlayback.RestoreState(play = true, deadlineElapsedMs = 1_000L))
        now = 900L
        arbiter.begin(DeferredPlayback.RestoreState(play = true, deadlineElapsedMs = 2_000L))
        now = 1_500L
        assertTrue(arbiter.snapshot().play)
        now = 2_001L
        assertFalse(arbiter.finish().play)
    }
}
