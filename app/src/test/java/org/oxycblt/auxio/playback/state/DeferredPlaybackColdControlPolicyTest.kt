/*
 * Copyright (c) 2026 Auxio Project
 * DeferredPlaybackColdControlPolicyTest.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
}
