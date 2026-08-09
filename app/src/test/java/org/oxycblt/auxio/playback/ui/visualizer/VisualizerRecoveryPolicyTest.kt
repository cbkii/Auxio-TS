/*
 * Copyright (c) 2026 Auxio Project
 * VisualizerRecoveryPolicyTest.kt is part of Auxio.
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

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertSame
import kotlin.test.assertTrue

class VisualizerRecoveryPolicyTest {
    @Test
    fun firstFrameTimeoutIsMeasuredFromAttemptStart() {
        val tracker = VisualizerRecoveryTracker()
        tracker.beginAttempt(1_000L)

        assertFalse(tracker.isTimedOut(3_999L))
        assertTrue(tracker.isTimedOut(4_000L))
    }

    @Test
    fun usableFrameSwitchesWatchdogToLiveFrameClock() {
        val tracker = VisualizerRecoveryTracker()
        tracker.beginAttempt(1_000L)
        tracker.noteUsableFrame(3_500L)

        assertFalse(tracker.isTimedOut(5_999L))
        assertTrue(tracker.isTimedOut(6_000L))
    }

    @Test
    fun beginAttemptPreservesConsecutiveRetryBudget() {
        val tracker = VisualizerRecoveryTracker()
        tracker.beginAttempt(1_000L)
        assertTrue(tracker.consumeRetry(maxRetries = 1))

        tracker.beginAttempt(2_000L)

        assertEquals(1, tracker.consecutiveRetries)
        assertFalse(tracker.consumeRetry(maxRetries = 1))
    }

    @Test
    fun usableFrameResetsConsecutiveRetryBudget() {
        val tracker = VisualizerRecoveryTracker()
        tracker.beginAttempt(1_000L)
        assertTrue(tracker.consumeRetry(maxRetries = 1))
        assertEquals(1, tracker.consecutiveRetries)

        tracker.beginAttempt(2_000L)
        tracker.noteUsableFrame(2_100L)
        assertEquals(0, tracker.consecutiveRetries)

        assertTrue(tracker.consumeRetry(maxRetries = 1))
        assertEquals(1, tracker.consecutiveRetries)
    }

    @Test
    fun exhaustedConsecutiveRetryBudgetRemainsBoundedUntilRecovery() {
        val tracker = VisualizerRecoveryTracker()
        tracker.beginAttempt(1_000L)

        assertTrue(tracker.consumeRetry(maxRetries = 1))
        assertFalse(tracker.consumeRetry(maxRetries = 1))

        tracker.noteUsableFrame(1_100L)
        assertTrue(tracker.consumeRetry(maxRetries = 1))
    }

    @Test
    fun capturePolicyUsesFftPrimaryAndWaveformFallbackWithoutDualCapture() {
        val primary = VisualizerRecoveryPolicy.captureModeForAttempt(consecutiveRetries = 0)
        val fallback = VisualizerRecoveryPolicy.captureModeForAttempt(consecutiveRetries = 1)

        assertEquals(VisualizerCaptureMode.FFT, primary)
        assertTrue(primary.captureFft)
        assertFalse(primary.captureWaveform)

        assertEquals(VisualizerCaptureMode.WAVEFORM, fallback)
        assertFalse(fallback.captureFft)
        assertTrue(fallback.captureWaveform)

        for (mode in VisualizerCaptureMode.entries) {
            assertTrue(mode.captureFft xor mode.captureWaveform)
        }
    }

    @Test
    fun nonPositiveSamplingRatesAreNotUsableCaptureFrames() {
        assertFalse(VisualizerRecoveryPolicy.hasUsableSamplingRate(0))
        assertFalse(VisualizerRecoveryPolicy.hasUsableSamplingRate(-1))
        assertTrue(VisualizerRecoveryPolicy.hasUsableSamplingRate(44_100_000))
    }

    @Test
    fun retainedPauseResumeRequiresSameSessionInsideGracePeriod() {
        val pausedAt = 10_000L

        assertTrue(
            VisualizerRecoveryPolicy.canReusePausedSession(
                retainedSessionId = 42,
                requestedSessionId = 42,
                pausedAtUptimeMs = pausedAt,
                nowUptimeMs = pausedAt + VisualizerRecoveryPolicy.PAUSE_RETAIN_MS - 1,
            )
        )
        assertFalse(
            VisualizerRecoveryPolicy.canReusePausedSession(
                retainedSessionId = 42,
                requestedSessionId = 43,
                pausedAtUptimeMs = pausedAt,
                nowUptimeMs = pausedAt + 100,
            )
        )
        assertFalse(
            VisualizerRecoveryPolicy.canReusePausedSession(
                retainedSessionId = 42,
                requestedSessionId = 42,
                pausedAtUptimeMs = pausedAt,
                nowUptimeMs = pausedAt + VisualizerRecoveryPolicy.PAUSE_RETAIN_MS,
            )
        )
        assertFalse(
            VisualizerRecoveryPolicy.canReusePausedSession(
                retainedSessionId = null,
                requestedSessionId = 42,
                pausedAtUptimeMs = pausedAt,
                nowUptimeMs = pausedAt + 100,
            )
        )
    }

    @Test
    fun freshCachedLiveFrameIsPreserved() {
        val live =
            VisualizerState.Live(
                frame = byteArrayOf(1, 2, 3),
                samplingRate = 44_100_000,
                receivedAtUptimeMs = 10_000L,
            )

        assertSame(live, VisualizerRecoveryPolicy.sanitizeCachedState(live, 11_500L))
    }

    @Test
    fun staleCachedLiveFrameBecomesTransientStartingState() {
        val live =
            VisualizerState.Live(
                frame = byteArrayOf(1, 2, 3),
                samplingRate = 44_100_000,
                receivedAtUptimeMs = 10_000L,
            )

        val sanitized = VisualizerRecoveryPolicy.sanitizeCachedState(live, 11_501L)

        assertIs<VisualizerState.Starting>(sanitized)
    }

    @Test
    fun resetClearsTimingAndRetryState() {
        val tracker = VisualizerRecoveryTracker()
        tracker.beginAttempt(1_000L)
        tracker.consumeRetry(maxRetries = 1)
        tracker.noteUsableFrame(1_100L)
        tracker.consumeRetry(maxRetries = 1)

        tracker.reset()

        assertEquals(VisualizerRecoveryPolicy.UNSET_UPTIME_MS, tracker.attemptStartedAtUptimeMs)
        assertEquals(VisualizerRecoveryPolicy.UNSET_UPTIME_MS, tracker.lastUsableFrameAtUptimeMs)
        assertEquals(0, tracker.consecutiveRetries)
        assertFalse(tracker.isTimedOut(Long.MAX_VALUE))
    }
}
