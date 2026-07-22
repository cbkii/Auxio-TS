/*
 * Copyright (c) 2026 Auxio Project
 * VisualizerState.kt is part of Auxio.
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

sealed interface VisualizerState {
    /** Visualizer is disabled. */
    data object Disabled : VisualizerState

    /** Playback is paused, or no track is actively playing. */
    data object Paused : VisualizerState

    /** Waiting for a non-zero audio session. */
    data object AwaitingAudioSession : VisualizerState

    /** Recording permission has not yet been granted. */
    data object PermissionRequired : VisualizerState

    /** Recording permission was explicitly denied. */
    data object PermissionDenied : VisualizerState

    /** The capture session is starting up. */
    data object Starting : VisualizerState

    enum class FrameSource {
        FFT,
        WAVEFORM,
    }

    /** A fresh, non-silent audio frame has arrived. */
    data class Live(
        val frame: ByteArray,
        val samplingRate: Int,
        val receivedAtUptimeMs: Long,
        val source: FrameSource = FrameSource.FFT,
    ) : VisualizerState

    /** Permission, listener registration, construction, or bounded capture recovery failed. */
    data class Unavailable(val reason: String) : VisualizerState
}
