/*
 * Copyright (c) 2026 Auxio Project
 * MediaSessionInitializationPolicyTest.kt is part of Auxio.
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

import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MediaSessionInitializationPolicyTest {
    @Test
    fun `initial flags expose media buttons transports and queue commands`() {
        val flags = MediaSessionInitializationPolicy.FLAGS

        assertTrue(flags and MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS != 0)
        assertTrue(flags and MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS != 0)
        assertTrue(flags and MediaSessionCompat.FLAG_HANDLES_QUEUE_COMMANDS != 0)
    }

    @Test
    fun `initial state is inactive but advertises canonical actions`() {
        val state = MediaSessionInitializationPolicy.initialPlaybackState()

        assertEquals(PlaybackStateCompat.STATE_NONE, state.state)
        assertEquals(0L, state.position)
        assertEquals(MediaSessionInterface.ACTIONS, state.actions)
    }
}
