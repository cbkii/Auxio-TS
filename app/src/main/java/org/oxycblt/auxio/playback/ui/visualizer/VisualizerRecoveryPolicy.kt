/*
 * Copyright (c) 2026 Auxio Project
 * VisualizerRecoveryPolicy.kt is part of Auxio.
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

import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

/** Allocation-free timing and presentation policy for the Android Visualizer capture lifecycle. */
internal object VisualizerRecoveryPolicy {
    const val WATCHDOG_INTERVAL_MS = 1_000L
    const val FIRST_FRAME_TIMEOUT_MS = 3_000L
    const val LIVE_FRAME_STALE_MS = 2_500L
    const val PAUSE_RETAIN_MS = 5_000L
    const val CACHED_LIVE_REPLAY_MS = 1_500L

    fun hasUsableSamplingRate(samplingRate: Int) = samplingRate > 0

    fun captureModeForAttempt(consecutiveRetries: Int): VisualizerCaptureMode =
        if (consecutiveRetries <= 0) VisualizerCaptureMode.FFT else VisualizerCaptureMode.WAVEFORM

    fun isCaptureTimedOut(
        attemptStartedAtUptimeMs: Long,
        lastUsableFrameAtUptimeMs: Long,
        nowUptimeMs: Long,
    ): Boolean {
        if (attemptStartedAtUptimeMs == UNSET_UPTIME_MS) return false
        val hasUsableFrame = lastUsableFrameAtUptimeMs != UNSET_UPTIME_MS
        val reference = if (hasUsableFrame) lastUsableFrameAtUptimeMs else attemptStartedAtUptimeMs
        val timeout = if (hasUsableFrame) LIVE_FRAME_STALE_MS else FIRST_FRAME_TIMEOUT_MS
        return nowUptimeMs >= reference && nowUptimeMs - reference >= timeout
    }

    fun canReusePausedSession(
        retainedSessionId: Int?,
        requestedSessionId: Int,
        pausedAtUptimeMs: Long,
        nowUptimeMs: Long,
    ): Boolean =
        retainedSessionId == requestedSessionId &&
            pausedAtUptimeMs != UNSET_UPTIME_MS &&
            nowUptimeMs >= pausedAtUptimeMs &&
            nowUptimeMs - pausedAtUptimeMs < PAUSE_RETAIN_MS

    fun sanitizeCachedState(state: VisualizerState, nowUptimeMs: Long): VisualizerState {
        if (state !is VisualizerState.Live) return state
        val ageMs = nowUptimeMs - state.receivedAtUptimeMs
        return if (ageMs in 0..CACHED_LIVE_REPLAY_MS) state else VisualizerState.Starting
    }

    const val UNSET_UPTIME_MS = -1L
}

internal enum class VisualizerCaptureMode {
    FFT,
    WAVEFORM;

    val captureFft: Boolean
        get() = this == FFT

    val captureWaveform: Boolean
        get() = this == WAVEFORM
}

/** Tracks consecutive recovery attempts separately from the lifetime of the UI coordinator. */
internal class VisualizerRecoveryTracker {
    private val attemptStartedAt = AtomicLong(VisualizerRecoveryPolicy.UNSET_UPTIME_MS)
    private val lastUsableFrameAt = AtomicLong(VisualizerRecoveryPolicy.UNSET_UPTIME_MS)
    private val retries = AtomicInteger()

    val attemptStartedAtUptimeMs: Long
        get() = attemptStartedAt.get()

    val lastUsableFrameAtUptimeMs: Long
        get() = lastUsableFrameAt.get()

    val consecutiveRetries: Int
        get() = retries.get()

    fun beginAttempt(nowUptimeMs: Long) {
        attemptStartedAt.set(nowUptimeMs)
        lastUsableFrameAt.set(VisualizerRecoveryPolicy.UNSET_UPTIME_MS)
    }

    fun noteUsableFrame(nowUptimeMs: Long) {
        attemptStartedAt.compareAndSet(VisualizerRecoveryPolicy.UNSET_UPTIME_MS, nowUptimeMs)
        lastUsableFrameAt.set(nowUptimeMs)
        if (retries.get() != 0) retries.set(0)
    }

    fun isTimedOut(nowUptimeMs: Long) =
        VisualizerRecoveryPolicy.isCaptureTimedOut(
            attemptStartedAt.get(),
            lastUsableFrameAt.get(),
            nowUptimeMs,
        )

    fun consumeRetry(maxRetries: Int): Boolean {
        while (true) {
            val current = retries.get()
            if (current >= maxRetries) return false
            if (retries.compareAndSet(current, current + 1)) return true
        }
    }

    fun reset() {
        attemptStartedAt.set(VisualizerRecoveryPolicy.UNSET_UPTIME_MS)
        lastUsableFrameAt.set(VisualizerRecoveryPolicy.UNSET_UPTIME_MS)
        retries.set(0)
    }
}
