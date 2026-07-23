/*
 * Copyright (c) 2026 Auxio Project
 * MediaSessionInterfacePolicyTest.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
}
