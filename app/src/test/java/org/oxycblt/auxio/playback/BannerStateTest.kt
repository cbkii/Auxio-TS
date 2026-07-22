/*
 * Copyright (c) 2026 Auxio Project
 * BannerStateTest.kt is part of Auxio.
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

package org.oxycblt.auxio.playback

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.oxycblt.auxio.playback.state.RawPlaybackMetadata

class BannerStateTest {
    @Test
    fun rawFastResumeEnablesOnlySingleItemPlaybackCommands() {
        val state = BannerState.Raw(rawMetadata())

        assertTrue(state.playable)
        assertFalse(state.richQueueCommandsAvailable)
    }

    @Test
    fun restoringIdleAndUnavailableNeverExposeStalePlaybackCommands() {
        val states =
            listOf(
                BannerState.Restoring,
                BannerState.Idle,
                BannerState.Unavailable("restore-failed"),
            )

        states.forEach { state ->
            assertFalse(state.playable)
            assertFalse(state.richQueueCommandsAvailable)
        }
    }

    @Test
    fun idleAndRestoringAreStableDataObjects() {
        assertTrue(BannerState.Idle == BannerState.Idle)
        assertTrue(BannerState.Restoring == BannerState.Restoring)
    }

    private fun rawMetadata() =
        RawPlaybackMetadata(
            title = "Track",
            artist = "Artist",
            album = "Album",
            uriString = "file:///storage/emulated/0/Music/track.mp3",
            path = "/storage/emulated/0/Music/track.mp3",
            durationMs = 60_000,
            positionMs = 5_000,
            isPlaying = true,
            savedAtMs = 1,
        )
}
