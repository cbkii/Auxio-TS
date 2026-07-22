/*
 * Copyright (c) 2026 Auxio Project
 * VisualizerDisplayPolicyTest.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.oxycblt.auxio.playback.ui.visualizer

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertSame
import kotlin.test.assertTrue
import org.oxycblt.auxio.ui.UISettings

class VisualizerDisplayPolicyTest {
    @Test
    fun alwaysModeKeepsExplicitStartingAndUnavailableStatesVisible() {
        assertTrue(
            VisualizerDisplayPolicy.shouldShow(
                VisualizerState.Starting,
                UISettings.VisualizerMode.ALWAYS,
                hasArtwork = true,
            )
        )
        assertTrue(
            VisualizerDisplayPolicy.shouldShow(
                VisualizerState.Unavailable("capture failed"),
                UISettings.VisualizerMode.ALWAYS,
                hasArtwork = true,
            )
        )
        assertFalse(
            VisualizerDisplayPolicy.shouldShow(
                VisualizerState.Paused,
                UISettings.VisualizerMode.ALWAYS,
                hasArtwork = true,
            )
        )
    }

    @Test
    fun fallbackModeNeverReplacesExistingArtwork() {
        val live = live(receivedAt = 1_000)
        assertFalse(
            VisualizerDisplayPolicy.shouldShow(
                live,
                UISettings.VisualizerMode.FALLBACK,
                hasArtwork = true,
            )
        )
        assertTrue(
            VisualizerDisplayPolicy.shouldShow(
                live,
                UISettings.VisualizerMode.FALLBACK,
                hasArtwork = false,
            )
        )
    }

    @Test
    fun staleAndFutureFramesBecomeUnavailable() {
        val fresh = live(receivedAt = 1_000)
        assertSame(fresh, VisualizerDisplayPolicy.sanitizeLiveFrame(fresh, nowUptimeMs = 2_000))
        assertIs<VisualizerState.Unavailable>(
            VisualizerDisplayPolicy.sanitizeLiveFrame(fresh, nowUptimeMs = 3_000)
        )
        assertIs<VisualizerState.Unavailable>(
            VisualizerDisplayPolicy.sanitizeLiveFrame(fresh, nowUptimeMs = 900)
        )
    }

    private fun live(receivedAt: Long) =
        VisualizerState.Live(
            frame = byteArrayOf(1, 2, 3),
            samplingRate = 44_100_000,
            receivedAtUptimeMs = receivedAt,
            source = VisualizerState.FrameSource.FFT,
        )
}
