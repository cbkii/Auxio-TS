/*
 * Copyright (c) 2026 Auxio Project
 * RestoreIntentArbiter.kt is part of Auxio.
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

import org.oxycblt.auxio.playback.state.DeferredPlayback

/**
 * Bounded command accumulator for the short interval while a persisted queue is restored.
 *
 * Only the compact user intent is retained: latest play/pause and seek win, skips are folded into a
 * bounded logical delta, and a newer fallback replaces an older one. The restore worker can use
 * [version] to re-check that it is not attaching a stale queue window.
 */
internal class RestoreIntentArbiter(private val maxAbsoluteSkip: Int = DEFAULT_MAX_ABSOLUTE_SKIP) {
    data class Snapshot(
        val active: Boolean,
        val play: Boolean,
        val skipDelta: Int,
        val seekPositionMs: Long?,
        val fallback: DeferredPlayback?,
        val version: Long,
    )

    private var active = false
    private var play = false
    private var skipDelta = 0
    private var seekPositionMs: Long? = null
    private var fallback: DeferredPlayback? = null
    private var version = 0L

    @Synchronized
    fun begin(action: DeferredPlayback.RestoreState): Boolean {
        val started = !active
        if (started) {
            active = true
            skipDelta = 0
            seekPositionMs = null
            fallback = null
        }
        play = action.play
        if (action.fallback != null) fallback = action.fallback
        version += 1
        return started
    }

    @Synchronized
    fun updatePlay(playing: Boolean): Boolean {
        if (!active) return false
        play = playing
        version += 1
        return true
    }

    @Synchronized
    fun updateSeek(positionMs: Long): Boolean {
        if (!active) return false
        seekPositionMs = positionMs.coerceAtLeast(0L)
        version += 1
        return true
    }

    @Synchronized
    fun addSkip(delta: Int): Boolean {
        if (!active) return false
        skipDelta =
            (skipDelta.toLong() + delta)
                .coerceIn(-maxAbsoluteSkip.toLong(), maxAbsoluteSkip.toLong())
                .toInt()
        seekPositionMs = null
        version += 1
        return true
    }

    @Synchronized
    fun snapshot(): Snapshot = Snapshot(active, play, skipDelta, seekPositionMs, fallback, version)

    @Synchronized
    fun finish(): Snapshot {
        val result = snapshot()
        clearLocked()
        return result
    }

    @Synchronized
    fun cancel(): Boolean {
        if (!active) return false
        clearLocked()
        return true
    }

    private fun clearLocked() {
        active = false
        play = false
        skipDelta = 0
        seekPositionMs = null
        fallback = null
        version += 1
    }

    companion object {
        const val DEFAULT_MAX_ABSOLUTE_SKIP = 10_000
    }
}
