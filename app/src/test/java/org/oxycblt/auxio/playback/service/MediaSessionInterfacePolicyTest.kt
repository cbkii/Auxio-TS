/*
 * Copyright (c) 2026 Auxio Project
 * MediaSessionInterfacePolicyTest.kt is part of Auxio.
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
import android.support.v4.media.session.PlaybackStateCompat
import org.oxycblt.auxio.playback.state.DeferredPlayback

class MediaSessionInterfacePolicyTest {
    @Test
    fun `hydrated and raw sessions resume without starting another restore`() {
        assertTrue(
            MediaSessionInterface.shouldResumeExistingPlayback(
                hasCurrentSong = true,
                hasRawPlaybackMetadata = false,
            )
        )
        assertTrue(
            MediaSessionInterface.shouldResumeExistingPlayback(
                hasCurrentSong = false,
                hasRawPlaybackMetadata = true,
            )
        )
    }

    @Test
    fun `empty session requests saved-state restoration`() {
        assertFalse(
            MediaSessionInterface.shouldResumeExistingPlayback(
                hasCurrentSong = false,
                hasRawPlaybackMetadata = false,
            )
        )
    }

    @Test
    fun `standard prepare action is advertised`() {
        assertTrue((MediaSessionInterface.ACTIONS and PlaybackStateCompat.ACTION_PREPARE) != 0L)
    }

    @Test
    fun `cold skip fallback still requests playback`() {
        assertTrue(MediaSessionInterface.shouldPlayFallbackAfterColdRestore(play = true, skipDelta = 1))
        assertFalse(MediaSessionInterface.shouldPlayFallbackAfterColdRestore(play = false, skipDelta = 0))
        assertEquals(
            DeferredPlayback.ShuffleAll(play = true),
            MediaSessionInterface.fallbackForColdRestore(play = true, skipDelta = 1),
        )
        assertNull(MediaSessionInterface.fallbackForColdRestore(play = false, skipDelta = 0))
    }
}
