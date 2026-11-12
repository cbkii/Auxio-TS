/*
 * Copyright (c) 2026 Auxio Project
 * DeferredPlaybackColdControlPolicyTest.kt is part of Auxio.
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

package org.oxycblt.auxio.playback.state

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class DeferredPlaybackColdControlPolicyTest {
    @Test
    fun pauseOverridesPendingRestoreWithoutCreatingFallback() {
        val pending = DeferredPlayback.RestoreState(play = true)
        val result = DeferredPlaybackColdControlPolicy.applyPause(pending)
        assertEquals(DeferredPlayback.RestoreState(play = false), result)
    }

    @Test
    fun pauseDoesNotCreateOrReplaceNonRestoreWork() {
        assertEquals(null, DeferredPlaybackColdControlPolicy.applyPause(null))
        val shuffle = DeferredPlayback.ShuffleAll()
        assertSame(shuffle, DeferredPlaybackColdControlPolicy.applyPause(shuffle))
    }

    @Test
    fun boundedPlayJoinsAnUnboundedPlayAlreadyOwed() {
        val owed =
            DeferredPlayback.RestoreState(play = true, fallback = DeferredPlayback.ShuffleAll())
        val bounded = DeferredPlayback.RestoreState(play = true, deadlineElapsedMs = 200L)
        assertSame(owed, DeferredPlaybackColdControlPolicy.admit(owed, bounded))
    }

    @Test
    fun boundedPlayReplacesPassiveOrBoundedPendingWork() {
        val bounded = DeferredPlayback.RestoreState(play = true, deadlineElapsedMs = 200L)
        val passive = DeferredPlayback.RestoreState(play = false)
        val earlier = DeferredPlayback.RestoreState(play = true, deadlineElapsedMs = 100L)
        assertSame(bounded, DeferredPlaybackColdControlPolicy.admit(passive, bounded))
        assertSame(bounded, DeferredPlaybackColdControlPolicy.admit(earlier, bounded))
        assertSame(bounded, DeferredPlaybackColdControlPolicy.admit(null, bounded))
    }

    @Test
    fun newestUnboundedRequestStillWins() {
        val bounded = DeferredPlayback.RestoreState(play = true, deadlineElapsedMs = 200L)
        val explicit = DeferredPlayback.RestoreState(play = true)
        val shuffle = DeferredPlayback.ShuffleAll()
        assertSame(explicit, DeferredPlaybackColdControlPolicy.admit(bounded, explicit))
        assertSame(shuffle, DeferredPlaybackColdControlPolicy.admit(explicit, shuffle))
    }
}
