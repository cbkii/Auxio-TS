/*
 * Copyright (c) 2026 Auxio Project
 * IncrementalResultFailurePolicyTest.kt is part of Auxio.
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

package org.oxycblt.musikr

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.oxycblt.musikr.cache.IncrementalScanCommit
import org.oxycblt.musikr.library.MetadataProfile

class IncrementalResultFailurePolicyTest {
    @Test
    fun `unobserved sources become temporary failures without replacing real failures`() {
        val commit =
            commit(
                failedSources = mapOf("failed" to "TRUNCATED|limit"),
                unavailableSources = setOf("failed", "absent"),
            )

        val failures = IncrementalResultFailurePolicy.effectiveFailures(commit)

        assertEquals("TRUNCATED|limit", failures["failed"])
        assertTrue(failures.getValue("absent").startsWith("TEMPORARILY_UNAVAILABLE|"))
    }

    @Test
    fun `incomplete enrichment is visible as a partial optional result`() {
        val commit =
            commit(
                committedSources = setOf("source"),
                enrichmentOnly = true,
                enrichmentComplete = false,
            )

        val failures = IncrementalResultFailurePolicy.effectiveFailures(commit)

        assertTrue(failures.getValue("source").startsWith("ENRICHMENT_INCOMPLETE|"))
    }

    private fun commit(
        committedSources: Set<String> = emptySet(),
        unavailableSources: Set<String> = emptySet(),
        failedSources: Map<String, String> = emptyMap(),
        enrichmentOnly: Boolean = false,
        enrichmentComplete: Boolean = true,
    ) =
        IncrementalScanCommit(
            scanId = "scan",
            committedSources = committedSources,
            reusedSources = emptySet(),
            unavailableSources = unavailableSources,
            failedSources = failedSources,
            changedRows = 0,
            removedRows = 0,
            metadataProfile = MetadataProfile.FULL,
            enrichmentOnly = enrichmentOnly,
            enrichmentComplete = enrichmentComplete,
        )
}
