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
import kotlin.test.assertTrue
import org.oxycblt.auxio.ui.UISettings

class VisualizerDisplayPolicyTest {
    @Test
    fun alwaysModeKeepsStartingAndUnavailableStatesVisible() {
        assertTrue(
            VisualizerDisplayPolicy.shouldShowVisualizer(
                VisualizerState.Starting,
                UISettings.VisualizerMode.ALWAYS,
                hasArtwork = true,
            )
        )
        assertTrue(
            VisualizerDisplayPolicy.shouldShowVisualizer(
                VisualizerState.Unavailable("capture failed"),
                UISettings.VisualizerMode.ALWAYS,
                hasArtwork = true,
            )
        )
    }

    @Test
    fun pausedAndDisabledStatesRestoreArtwork() {
        assertFalse(
            VisualizerDisplayPolicy.shouldShowVisualizer(
                VisualizerState.Paused,
                UISettings.VisualizerMode.ALWAYS,
                hasArtwork = false,
            )
        )
        assertFalse(
            VisualizerDisplayPolicy.shouldShowVisualizer(
                VisualizerState.Disabled,
                UISettings.VisualizerMode.ALWAYS,
                hasArtwork = false,
            )
        )
    }

    @Test
    fun fallbackModeNeverReplacesAvailableArtwork() {
        assertFalse(
            VisualizerDisplayPolicy.shouldShowVisualizer(
                VisualizerState.Starting,
                UISettings.VisualizerMode.FALLBACK,
                hasArtwork = true,
            )
        )
        assertTrue(
            VisualizerDisplayPolicy.shouldShowVisualizer(
                VisualizerState.Starting,
                UISettings.VisualizerMode.FALLBACK,
                hasArtwork = false,
            )
        )
    }

    @Test
    fun offModeAlwaysUsesArtwork() {
        assertFalse(
            VisualizerDisplayPolicy.shouldShowVisualizer(
                VisualizerState.Live(
                    frame = byteArrayOf(1, 2, 3),
                    samplingRate = 44_100,
                    receivedAtUptimeMs = 1,
                ),
                UISettings.VisualizerMode.OFF,
                hasArtwork = false,
            )
        )
    }
}
