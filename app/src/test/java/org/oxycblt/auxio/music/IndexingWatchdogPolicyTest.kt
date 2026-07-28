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
import org.junit.Test

class IndexingWatchdogPolicyTest {
    @Test
    fun recentProgressIsHealthy() {
        assertEquals(
            IndexingWatchdogState.HEALTHY,
            IndexingWatchdogPolicy.classify(
                nowElapsedMs = 90_000L,
                startedAtElapsedMs = 10_000L,
                lastProgressAtElapsedMs = 80_000L,
            ),
        )
    }

    @Test
    fun minuteWithoutProgressIsStalled() {
        assertEquals(
            IndexingWatchdogState.STALLED,
            IndexingWatchdogPolicy.classify(
                nowElapsedMs = 90_000L,
                startedAtElapsedMs = 10_000L,
                lastProgressAtElapsedMs = 90_000L - IndexingWatchdogPolicy.STALL_WARNING_MS,
            ),
        )
    }

    @Test
    fun unsetStartBaselineCannotBecomeOverdue() {
        assertEquals(
            IndexingWatchdogState.HEALTHY,
            IndexingWatchdogPolicy.classify(
                nowElapsedMs = IndexingWatchdogPolicy.MAX_SCAN_ELAPSED_MS + 10_000L,
                startedAtElapsedMs = 0L,
                lastProgressAtElapsedMs = IndexingWatchdogPolicy.MAX_SCAN_ELAPSED_MS + 9_000L,
            ),
        )
    }

    @Test
    fun unsetProgressBaselineCannotBecomeStalled() {
        assertEquals(
            IndexingWatchdogState.HEALTHY,
            IndexingWatchdogPolicy.classify(
                nowElapsedMs = 120_000L,
                startedAtElapsedMs = 60_000L,
                lastProgressAtElapsedMs = 0L,
            ),
        )
    }

    @Test
    fun maximumElapsedTimeTakesPrecedenceOverRecentProgress() {
        assertEquals(
            IndexingWatchdogState.OVERDUE,
            IndexingWatchdogPolicy.classify(
                nowElapsedMs = IndexingWatchdogPolicy.MAX_SCAN_ELAPSED_MS + 50_000L,
                startedAtElapsedMs = 50_000L,
                lastProgressAtElapsedMs = IndexingWatchdogPolicy.MAX_SCAN_ELAPSED_MS + 49_000L,
            ),
        )
    }
}
