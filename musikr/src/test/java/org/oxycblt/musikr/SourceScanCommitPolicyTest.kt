/*
 * Copyright (c) 2026 Auxio Project
 * SourceScanCommitPolicyTest.kt is part of Auxio.
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

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.oxycblt.musikr.cache.IncrementalScanCommit
import org.oxycblt.musikr.library.MetadataProfile

class SourceScanCommitPolicyTest {
    @Test
    fun allFailedSourcesCannotPublishAuthoritativeEmptyLibrary() {
        assertTrue(
            SourceScanCommitPolicy.rejectsAsAuthoritativeEmpty(
                commit(failed = mapOf("source" to "unreadable"))
            )
        )
    }

    @Test
    fun successfulEmptySourceMayPublishConfirmedEmptyLibrary() {
        assertFalse(
            SourceScanCommitPolicy.rejectsAsAuthoritativeEmpty(commit(committed = setOf("source")))
        )
    }

    @Test
    fun mixedFailureAndReusedGenerationRemainsReadable() {
        assertFalse(
            SourceScanCommitPolicy.rejectsAsAuthoritativeEmpty(
                commit(reused = setOf("internal"), failed = mapOf("usb" to "unmounted"))
            )
        )
    }

    private fun commit(
        committed: Set<String> = emptySet(),
        reused: Set<String> = emptySet(),
        failed: Map<String, String> = emptyMap(),
    ) =
        IncrementalScanCommit(
            scanId = "scan",
            committedSources = committed,
            reusedSources = reused,
            unavailableSources = emptySet(),
            failedSources = failed,
            changedRows = 0,
            removedRows = 0,
            metadataProfile = MetadataProfile.LEAN,
        )
}
