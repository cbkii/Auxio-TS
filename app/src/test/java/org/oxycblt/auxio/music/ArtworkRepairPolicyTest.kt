/*
 * Copyright (c) 2026 Auxio Project
 * ArtworkRepairPolicyTest.kt is part of Auxio.
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
import org.oxycblt.auxio.image.CoverMode
import org.oxycblt.musikr.library.MetadataProfile

class ArtworkRepairPolicyTest {
    @Test
    fun `enabled artwork waits until full library is ready`() {
        assertFalse(
            ArtworkRepairPolicy.shouldRequest(CoverMode.OPTIMISED, StartupReadinessState.QueueReady)
        )
        assertTrue(
            ArtworkRepairPolicy.shouldRequest(
                CoverMode.OPTIMISED,
                StartupReadinessState.FullLibraryReady,
            )
        )
    }

    @Test
    fun `off never schedules artwork repair`() {
        assertFalse(
            ArtworkRepairPolicy.shouldRequest(
                CoverMode.OFF,
                StartupReadinessState.EnrichmentComplete,
            )
        )
    }

    @Test
    fun `as is mode also schedules repair`() {
        assertTrue(
            ArtworkRepairPolicy.shouldRequest(
                CoverMode.AS_IS,
                StartupReadinessState.FullLibraryReady,
            )
        )
    }

    @Test
    fun `only full cached unscoped metadata enrichment can satisfy compatibility repair`() {
        assertTrue(
            ArtworkRepairCompletionPolicy.isRepairRequest(
                IndexRequest(
                    reason = IndexReason.METADATA_ENRICHMENT,
                    withCache = true,
                    metadataProfile = MetadataProfile.FULL,
                    configurationGeneration = 3L,
                )
            )
        )
        assertFalse(
            ArtworkRepairCompletionPolicy.isRepairRequest(
                IndexRequest(
                    reason = IndexReason.USER_REFRESH,
                    withCache = true,
                    metadataProfile = MetadataProfile.FULL,
                    configurationGeneration = 3L,
                )
            )
        )
        assertFalse(
            ArtworkRepairCompletionPolicy.isRepairRequest(
                IndexRequest(
                    reason = IndexReason.METADATA_ENRICHMENT,
                    withCache = true,
                    metadataProfile = MetadataProfile.LEAN,
                    configurationGeneration = 3L,
                )
            )
        )
        assertFalse(
            ArtworkRepairCompletionPolicy.isRepairRequest(
                IndexRequest(
                    reason = IndexReason.METADATA_ENRICHMENT,
                    withCache = false,
                    metadataProfile = MetadataProfile.FULL,
                    configurationGeneration = 3L,
                )
            )
        )
        assertFalse(
            ArtworkRepairCompletionPolicy.isRepairRequest(
                IndexRequest(
                    reason = IndexReason.METADATA_ENRICHMENT,
                    withCache = true,
                    metadataProfile = MetadataProfile.FULL,
                    configurationGeneration = 3L,
                    sourceKeys = setOf("direct:usb0"),
                )
            )
        )
    }

    @Test
    fun `repair checkpoint is written only after a complete success`() {
        assertTrue(
            ArtworkRepairCompletionPolicy.isSuccessfulCompletion(IndexingTerminalOutcome.SUCCESS)
        )
        assertFalse(
            ArtworkRepairCompletionPolicy.isSuccessfulCompletion(
                IndexingTerminalOutcome.PARTIAL_SUCCESS
            )
        )
        assertFalse(
            ArtworkRepairCompletionPolicy.isSuccessfulCompletion(IndexingTerminalOutcome.FAILED)
        )
    }
}
