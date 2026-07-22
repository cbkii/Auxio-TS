/*
 * Copyright (c) 2026 Auxio Project
 * VisualizerDisplayPolicy.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.oxycblt.auxio.playback.ui.visualizer

import org.oxycblt.auxio.ui.UISettings

/** Pure visualiser display and frame-freshness rules shared by the cover pager and tests. */
internal object VisualizerDisplayPolicy {
    const val LIVE_FRAME_FRESHNESS_MS = 1_500L

    fun shouldShow(
        state: VisualizerState,
        mode: UISettings.VisualizerMode,
        hasArtwork: Boolean,
    ): Boolean =
        when (mode) {
            UISettings.VisualizerMode.OFF -> false
            UISettings.VisualizerMode.FALLBACK -> !hasArtwork && state is VisualizerState.Live
            UISettings.VisualizerMode.ALWAYS ->
                state !is VisualizerState.Disabled && state !is VisualizerState.Paused
        }

    fun sanitizeLiveFrame(
        state: VisualizerState,
        nowUptimeMs: Long,
        freshnessMs: Long = LIVE_FRAME_FRESHNESS_MS,
    ): VisualizerState {
        if (state !is VisualizerState.Live) return state
        val ageMs = nowUptimeMs - state.receivedAtUptimeMs
        return if (ageMs in 0..freshnessMs) {
            state
        } else {
            VisualizerState.Unavailable("Stale visualizer frame")
        }
    }
}
