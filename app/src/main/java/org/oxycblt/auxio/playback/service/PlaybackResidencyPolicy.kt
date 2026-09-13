/*
 * Copyright (c) 2026 Auxio Project
 * PlaybackResidencyPolicy.kt is part of Auxio.
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

/**
 * Pure lifecycle policy for keeping the canonical TS18 playback authority warm while paused.
 *
 * Residency never grants playback authority: it only decides whether the existing service/session
 * should be allowed to remain alive and prepared. Explicit user exit always wins.
 */
internal object PlaybackResidencyPolicy {
    fun shouldScheduleIdleStop(
        keepPlaybackReady: Boolean,
        isPlaying: Boolean,
        sessionOngoing: Boolean,
    ): Boolean = !keepPlaybackReady && !isPlaying && sessionOngoing

    fun shouldEndSessionOnTaskRemoved(
        keepPlaybackReady: Boolean,
        isPlaying: Boolean,
        exitOnTaskRemoval: Boolean,
    ): Boolean = exitOnTaskRemoval || (!keepPlaybackReady && !isPlaying)
}
