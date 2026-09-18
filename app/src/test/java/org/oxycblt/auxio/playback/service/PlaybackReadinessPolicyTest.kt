/*
 * Copyright (c) 2026 Auxio Project
 * PlaybackReadinessPolicyTest.kt is part of Auxio.
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

import androidx.media3.common.Player
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.oxycblt.auxio.music.PlaybackReadinessState

class PlaybackReadinessPolicyTest {
    @Test
    fun readyPausedRequiresActualMedia3ReadyState() {
        assertEquals(
            PlaybackReadinessState.HOT_PAUSED,
            PlaybackReadinessPolicy.fromPlayer(
                playbackState = Player.STATE_READY,
                hasMediaItem = true,
                isPlaying = false,
            ),
        )
        assertNull(
            PlaybackReadinessPolicy.fromPlayer(
                playbackState = Player.STATE_BUFFERING,
                hasMediaItem = true,
                isPlaying = false,
            )
        )
    }

    @Test
    fun playingObservationWinsOverPreparedState() {
        assertEquals(
            PlaybackReadinessState.PLAYING,
            PlaybackReadinessPolicy.fromPlayer(
                playbackState = Player.STATE_READY,
                hasMediaItem = true,
                isPlaying = true,
            ),
        )
    }

    @Test
    fun readyPlayerReprojectsFromPlayingToHotPausedWithoutStateTransition() {
        assertEquals(
            PlaybackReadinessState.PLAYING,
            PlaybackReadinessPolicy.fromPlayer(
                playbackState = Player.STATE_READY,
                hasMediaItem = true,
                isPlaying = true,
            ),
        )
        assertEquals(
            PlaybackReadinessState.HOT_PAUSED,
            PlaybackReadinessPolicy.fromPlayer(
                playbackState = Player.STATE_READY,
                hasMediaItem = true,
                isPlaying = false,
            ),
        )
    }

    @Test
    fun emptyPlayerCannotClaimHotPaused() {
        assertNull(
            PlaybackReadinessPolicy.fromPlayer(
                playbackState = Player.STATE_READY,
                hasMediaItem = false,
                isPlaying = false,
            )
        )
    }
}
