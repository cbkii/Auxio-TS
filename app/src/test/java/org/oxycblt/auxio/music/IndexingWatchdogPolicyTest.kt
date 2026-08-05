/*
 * Copyright (c) 2026 Auxio Project
 * IndexingWatchdogPolicyTest.kt is part of Auxio.
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

package org.oxycblt.auxio.music

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.oxycblt.musikr.IndexingPhase

class IndexingWatchdogPolicyTest {
    @Test
    fun recentProgressIsHealthy() {
        val decision = classify(now = 90_000L, start = 10_000L, progress = 80_000L)

        assertEquals(IndexingWatchdogState.HEALTHY, decision.state)
        assertFalse(decision.shouldTerminate)
    }

    @Test
    fun minuteWithoutProgressIsAWarningOnly() {
        val decision =
            classify(
                now = 90_000L,
                start = 10_000L,
                progress = 90_000L - IndexingWatchdogPolicy.STALL_WARNING_MS,
            )

        assertEquals(IndexingWatchdogState.STALLED, decision.state)
        assertFalse(decision.shouldTerminate)
    }

    @Test
    fun zeroFileNarrowDiscoveryTerminatesAtBoundedDeadline() {
        val decision =
            classify(
                now = 1_000L + IndexingWatchdogPolicy.NARROW_DISCOVERY_NO_PROGRESS_MS,
                start = 1_000L,
                progress = 1_000L,
                phase = IndexingPhase.DISCOVERING,
                scope = IndexingSourceScope.NARROW,
            )

        assertEquals(IndexingWatchdogState.NO_PROGRESS_TIMEOUT, decision.state)
        assertTrue(decision.shouldTerminate)
        assertTrue(decision.detail.contains("phase=DISCOVERING"))
        assertTrue(decision.detail.contains("explored=0"))
    }

    @Test
    fun wholeVolumeDiscoveryGetsTheConservativeLongerDeadline() {
        val beforeDeadline =
            classify(
                now = IndexingWatchdogPolicy.UNKNOWN_DISCOVERY_NO_PROGRESS_MS,
                start = 1L,
                progress = 1L,
                phase = IndexingPhase.DISCOVERING,
                scope = IndexingSourceScope.WHOLE_VOLUME,
            )
        val atDeadline =
            classify(
                now = 1L + IndexingWatchdogPolicy.WHOLE_VOLUME_DISCOVERY_NO_PROGRESS_MS,
                start = 1L,
                progress = 1L,
                phase = IndexingPhase.DISCOVERING,
                scope = IndexingSourceScope.WHOLE_VOLUME,
            )

        assertEquals(IndexingWatchdogState.STALLED, beforeDeadline.state)
        assertFalse(beforeDeadline.shouldTerminate)
        assertEquals(IndexingWatchdogState.NO_PROGRESS_TIMEOUT, atDeadline.state)
        assertTrue(atDeadline.shouldTerminate)
    }

    @Test
    fun extractionWithoutProgressUsesTheActiveStageDeadline() {
        val decision =
            classify(
                now = 5_001L + IndexingWatchdogPolicy.ACTIVE_STAGE_NO_PROGRESS_MS,
                start = 1L,
                progress = 5_001L,
                phase = IndexingPhase.EXTRACTING,
                firstFile = true,
                explored = 200,
                loaded = 10,
            )

        assertEquals(IndexingWatchdogState.NO_PROGRESS_TIMEOUT, decision.state)
        assertEquals(
            IndexingWatchdogPolicy.ACTIVE_STAGE_NO_PROGRESS_MS,
            decision.noProgressDeadlineMs,
        )
    }

    @Test
    fun finalCommitHasItsOwnNoProgressDeadline() {
        val decision =
            classify(
                now = 2_000L + IndexingWatchdogPolicy.FINALISING_NO_PROGRESS_MS,
                start = 1_000L,
                progress = 2_000L,
                phase = IndexingPhase.FINALISING,
                firstFile = true,
            )

        assertEquals(IndexingWatchdogState.NO_PROGRESS_TIMEOUT, decision.state)
        assertTrue(decision.detail.contains("phase=FINALISING"))
    }

    @Test
    fun longScanWithContinuingProgressIsNotCancelled() {
        val now = IndexingWatchdogPolicy.MAX_SCAN_ELAPSED_MS - 1L
        val decision =
            classify(
                now = now,
                start = 1L,
                progress = now - 2_000L,
                phase = IndexingPhase.EVALUATING,
                firstFile = true,
                explored = 25_000,
                loaded = 24_000,
                evaluated = 23_000,
            )

        assertEquals(IndexingWatchdogState.HEALTHY, decision.state)
        assertFalse(decision.shouldTerminate)
    }

    @Test
    fun overallElapsedCapTakesPrecedenceOverRecentProgress() {
        val start = 50_000L
        val now = start + IndexingWatchdogPolicy.MAX_SCAN_ELAPSED_MS
        val decision =
            classify(
                now = now,
                start = start,
                progress = now - 1_000L,
                phase = IndexingPhase.EVALUATING,
                firstFile = true,
                explored = 50_000,
            )

        assertEquals(IndexingWatchdogState.OVERDUE, decision.state)
        assertTrue(decision.shouldTerminate)
        assertTrue(decision.detail.startsWith("overall-cap"))
    }

    @Test
    fun unsetElapsedBaselinesCannotCreateFalseTimeouts() {
        val decision =
            classify(
                now = IndexingWatchdogPolicy.MAX_SCAN_ELAPSED_MS + 10_000L,
                start = 0L,
                progress = 0L,
            )

        assertEquals(IndexingWatchdogState.HEALTHY, decision.state)
        assertFalse(decision.shouldTerminate)
        assertEquals(0L, decision.totalElapsedMs)
        assertEquals(0L, decision.noProgressMs)
    }

    private fun classify(
        now: Long,
        start: Long,
        progress: Long,
        phase: IndexingPhase = IndexingPhase.DISCOVERING,
        firstFile: Boolean = false,
        scope: IndexingSourceScope = IndexingSourceScope.NARROW,
        explored: Int = 0,
        loaded: Int = 0,
        evaluated: Int = 0,
    ) =
        IndexingWatchdogPolicy.classify(
            IndexingWatchdogInput(
                nowElapsedMs = now,
                startedAtElapsedMs = start,
                lastProgressAtElapsedMs = progress,
                phase = phase,
                firstFileEmitted = firstFile,
                sourceScope = scope,
                explored = explored,
                loaded = loaded,
                evaluated = evaluated,
            )
        )
}
