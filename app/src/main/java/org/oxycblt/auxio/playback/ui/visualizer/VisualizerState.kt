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
    enum class FailureReason {
        PERMISSION_DENIED,
        ZERO_SESSION,
        CONSTRUCTOR_REJECTED,
        CAPTURE_SIZE_RANGE_INVALID,
        LISTENER_REGISTRATION_REJECTED,
        ENABLE_FAILED,
        NO_CALLBACKS,
        FFT_ZERO_WITHOUT_WAVEFORM,
        SESSION_CHANGED,
        BACKEND_UNSUPPORTED,
        CAPTURE_STALLED,
    }

    data object Disabled : VisualizerState
    /** Compatibility name for disabled/hidden state used by existing UI paths. */
    data object Hidden : VisualizerState
    data object WaitingForPermission : VisualizerState
    data object WaitingForSession : VisualizerState
    data object Starting : VisualizerState
    data object WaitingForFrames : VisualizerState
    data class Retrying(val reason: FailureReason, val attempt: Int) : VisualizerState
    data class Live(val frame: ByteArray, val samplingRate: Int, val receivedAtUptimeMs: Long) :
        VisualizerState
    data class Failed(val reason: FailureReason, val detail: String? = null) : VisualizerState

}
