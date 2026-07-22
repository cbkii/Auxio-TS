/*
 * Copyright (c) 2026 Auxio Project
 * VisualizerDisplayPolicy.kt is part of Auxio.
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

package org.oxycblt.auxio.playback.ui.visualizer

import org.oxycblt.auxio.ui.UISettings

/** Pure visibility policy for artwork versus the visualizer surface. */
internal object VisualizerDisplayPolicy {
    fun shouldShowVisualizer(
        state: VisualizerState,
        mode: UISettings.VisualizerMode,
        hasArtwork: Boolean,
    ): Boolean =
        when (mode) {
            UISettings.VisualizerMode.OFF -> false
            UISettings.VisualizerMode.ALWAYS -> state.isVisibleVisualizerState
            UISettings.VisualizerMode.FALLBACK -> !hasArtwork && state.isVisibleVisualizerState
        }

    private val VisualizerState.isVisibleVisualizerState: Boolean
        get() = this !is VisualizerState.Disabled && this !is VisualizerState.Paused
}
