/*
 * Copyright (c) 2026 Auxio Project
 * StartupPlaybackPolicyTest.kt is part of Auxio.
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

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.oxycblt.auxio.playback.state.DeferredPlayback

class StartupPlaybackPolicyTest {

    @Test
    fun `restoreActionForLaunch with autoplay disabled returns play false and no fallback`() {
        val action = StartupPlaybackPolicy.restoreActionForLaunch(autoplayOnLaunch = false)
        assertFalse(action.play)
        assertNull(action.fallback)
    }

    @Test
    fun `restoreActionForLaunch with autoplay enabled returns play true with ShuffleAll fallback`() {
        val action = StartupPlaybackPolicy.restoreActionForLaunch(autoplayOnLaunch = true)
        assertTrue(action.play)
        assertEquals(DeferredPlayback.ShuffleAll, action.fallback)
    }

    @Test
    fun `restoreActionForBoot with autoplay disabled returns play false and no fallback`() {
        val action = StartupPlaybackPolicy.restoreActionForBoot(autoplayOnLaunch = false)
        assertFalse(action.play)
        assertNull(action.fallback)
    }

    @Test
    fun `restoreActionForBoot with autoplay enabled returns play true with ShuffleAll fallback`() {
        val action = StartupPlaybackPolicy.restoreActionForBoot(autoplayOnLaunch = true)
        assertTrue(action.play)
        assertEquals(DeferredPlayback.ShuffleAll, action.fallback)
    }
}
