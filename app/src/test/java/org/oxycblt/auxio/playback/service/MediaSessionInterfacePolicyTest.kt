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

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

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
    fun `rapid repeated cold play requests are coalesced but retry remains bounded`() {
        assertTrue(MediaSessionInterface.shouldRequestColdRestore(Long.MIN_VALUE, nowMs = 100L))
        assertFalse(MediaSessionInterface.shouldRequestColdRestore(lastRequestAtMs = 100L, nowMs = 101L))
        assertTrue(
            MediaSessionInterface.shouldRequestColdRestore(
                lastRequestAtMs = 100L,
                nowMs = 5_100L,
            )
        )
        assertTrue(
            MediaSessionInterface.shouldRequestColdRestore(
                lastRequestAtMs = 10_000L,
                nowMs = 5L,
            )
        )
    }
}
