/*
 * Copyright (c) 2026 Auxio Project
 * InterruptionOutcomeRecordingPolicyTest.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.oxycblt.auxio.music

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class InterruptionOutcomeRecordingPolicyTest {
    private val authoritativeRequest =
        IndexRequest(
            reason = IndexReason.INITIAL_CONFIGURATION,
            withCache = false,
            configurationGeneration = 42L,
            attemptId = "attempt-xyz",
            attemptOwner = SourceScanAttemptOwner("process", "service"),
        )

    @Test
    fun `accepted authoritative interruption records outcome`() {
        assertTrue(
            IndexRequestPolicy.shouldRecordInterruptionOutcome(
                authoritativeRequest,
                durableCompletionAccepted = true,
            )
        )
    }

    @Test
    fun `rejected authoritative interruption preserves terminal outcome`() {
        assertFalse(
            IndexRequestPolicy.shouldRecordInterruptionOutcome(
                authoritativeRequest,
                durableCompletionAccepted = false,
            )
        )
    }

    @Test
    fun `non-authoritative interruption records without durable completion`() {
        val refresh = IndexRequest(IndexReason.USER_REFRESH, withCache = true)

        assertTrue(
            IndexRequestPolicy.shouldRecordInterruptionOutcome(
                refresh,
                durableCompletionAccepted = false,
            )
        )
    }
}
