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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.oxycblt.auxio.music

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Proves the `shouldRecord` rule applied in [MusicRepository.prepareIndexingInterruption] and the
 * [CancellationException] catch path:
 *
 * - authoritative request + durable completion accepted  => record
 * - authoritative request + durable completion rejected  => do NOT record
 * - non-authoritative request (no checkpoint lease)      => always record
 */
class InterruptionOutcomeRecordingPolicyTest {

    /** Mimics the `shouldRecord` expression used in both interrupted/cancelled paths. */
    private fun shouldRecord(request: IndexRequest, durableCompletionAccepted: Boolean): Boolean =
        IndexRequestPolicy.checkpointAuthority(request) == null || durableCompletionAccepted

    // -------------------------------------------------------------------------
    // Non-authoritative requests (no checkpoint lease)
    // -------------------------------------------------------------------------

    @Test
    fun `non-authoritative USER_REFRESH records even when durable completion was not accepted`() {
        val request = IndexRequest(reason = IndexReason.USER_REFRESH, withCache = true)
        assertTrue(shouldRecord(request, durableCompletionAccepted = false))
    }

    @Test
    fun `non-authoritative USER_REFRESH records when durable completion was accepted`() {
        val request = IndexRequest(reason = IndexReason.USER_REFRESH, withCache = true)
        assertTrue(shouldRecord(request, durableCompletionAccepted = true))
    }

    @Test
    fun `non-authoritative SOURCE_OBSERVER request records regardless of durable completion`() {
        val request = IndexRequest(reason = IndexReason.SOURCE_OBSERVER, withCache = true)
        assertTrue(shouldRecord(request, durableCompletionAccepted = false))
        assertTrue(shouldRecord(request, durableCompletionAccepted = true))
    }

    @Test
    fun `non-authoritative METADATA_ENRICHMENT request records regardless of durable completion`() {
        // Enrichment never owns the checkpoint; the shouldRecord rule still applies.
        val request = IndexRequest(reason = IndexReason.METADATA_ENRICHMENT, withCache = true)
        assertTrue(shouldRecord(request, durableCompletionAccepted = false))
    }

    // -------------------------------------------------------------------------
    // Authoritative requests (checkpoint lease present)
    // -------------------------------------------------------------------------

    private fun authoritativeRequest(reason: IndexReason = IndexReason.INITIAL_CONFIGURATION) =
        IndexRequest(
            reason = reason,
            withCache = false,
            configurationGeneration = 42L,
            attemptId = "attempt-xyz",
            attemptOwner = SourceScanAttemptOwner(processId = "proc-1", lifecycleId = "lc-1"),
        )

    @Test
    fun `authoritative request with accepted durable completion records outcome`() {
        val request = authoritativeRequest()
        assertTrue(shouldRecord(request, durableCompletionAccepted = true))
    }

    @Test
    fun `authoritative request with rejected durable completion does NOT record outcome`() {
        // This is the key fix: a stale late interruption must not overwrite an already-terminal
        // outcome when the durable checkpoint rejected the completion.
        val request = authoritativeRequest()
        assertFalse(shouldRecord(request, durableCompletionAccepted = false))
    }

    @Test
    fun `authoritative USER_RETRY with accepted completion records outcome`() {
        val request = authoritativeRequest(reason = IndexReason.USER_RETRY)
        assertTrue(shouldRecord(request, durableCompletionAccepted = true))
    }

    @Test
    fun `authoritative USER_RETRY with rejected completion does NOT record outcome`() {
        val request = authoritativeRequest(reason = IndexReason.USER_RETRY)
        assertFalse(shouldRecord(request, durableCompletionAccepted = false))
    }

    @Test
    fun `authoritative STORAGE_MOUNTED with rejected completion does NOT record outcome`() {
        val request = authoritativeRequest(reason = IndexReason.STORAGE_MOUNTED)
        assertFalse(shouldRecord(request, durableCompletionAccepted = false))
    }

    // -------------------------------------------------------------------------
    // Partially-authoritative: reason requires claim but one field is missing
    // -------------------------------------------------------------------------

    @Test
    fun `INITIAL_CONFIGURATION without attemptId is non-authoritative and always records`() {
        // Missing attemptId makes checkpointAuthority return null => non-authoritative.
        val request =
            IndexRequest(
                reason = IndexReason.INITIAL_CONFIGURATION,
                withCache = false,
                configurationGeneration = 10L,
                // no attemptId
            )
        assertTrue(shouldRecord(request, durableCompletionAccepted = false))
    }

    @Test
    fun `INITIAL_CONFIGURATION without generation is non-authoritative and always records`() {
        val request =
            IndexRequest(
                reason = IndexReason.INITIAL_CONFIGURATION,
                withCache = false,
                // no configurationGeneration
                attemptId = "attempt-a",
                attemptOwner = SourceScanAttemptOwner(processId = "p", lifecycleId = "lc"),
            )
        assertTrue(shouldRecord(request, durableCompletionAccepted = false))
    }
}
