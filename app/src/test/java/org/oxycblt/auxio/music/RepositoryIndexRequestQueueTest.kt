/*
 * Copyright (c) 2026 Auxio Project
 * RepositoryIndexRequestQueueTest.kt is part of Auxio.
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
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.oxycblt.musikr.library.MetadataProfile

class RepositoryIndexRequestQueueTest {
    @Test
    fun coalescesToTheStrongestPendingRequest() {
        val queue = RepositoryIndexRequestQueue()

        queue.offer(IndexRequest(IndexReason.USER_REFRESH, withCache = true))
        queue.offer(
            IndexRequest(
                IndexReason.SOURCE_OBSERVER,
                withCache = true,
                metadataProfile = MetadataProfile.LEAN,
            )
        )
        queue.offer(
            IndexRequest(
                IndexReason.USER_RETRY,
                withCache = false,
                metadataProfile = MetadataProfile.FULL,
                configurationGeneration = 4L,
            )
        )

        val request = requireNotNull(queue.drain())
        assertFalse(request.withCache)
        assertEquals(MetadataProfile.FULL, request.metadataProfile)
        assertNull(queue.drain())
    }

    @Test
    fun cacheBypassCannotBeWeakenedByALaterCachedRefresh() {
        val queue = RepositoryIndexRequestQueue()

        queue.offer(
            IndexRequest(
                IndexReason.INITIAL_CONFIGURATION,
                withCache = false,
                metadataProfile = MetadataProfile.LEAN,
                configurationGeneration = 2L,
            )
        )
        queue.offer(IndexRequest(IndexReason.USER_REFRESH, withCache = true))

        val request = requireNotNull(queue.drain())
        assertFalse(request.withCache)
        assertEquals(MetadataProfile.LEAN, request.metadataProfile)
    }

    @Test
    fun leanProfileSurvivesARequestWithoutAnExplicitProfile() {
        val queue = RepositoryIndexRequestQueue()

        queue.offer(
            IndexRequest(
                IndexReason.USER_REFRESH,
                withCache = true,
                metadataProfile = MetadataProfile.LEAN,
            )
        )
        queue.offer(IndexRequest(IndexReason.SOURCE_OBSERVER, withCache = true))

        assertEquals(MetadataProfile.LEAN, queue.drain()?.metadataProfile)
    }

    @Test
    fun newerConfigurationSupersedesOlderPendingWork() {
        val queue = RepositoryIndexRequestQueue()
        queue.offer(
            IndexRequest(
                IndexReason.INITIAL_CONFIGURATION,
                withCache = false,
                configurationGeneration = 7L,
            )
        )
        queue.offer(
            IndexRequest(
                IndexReason.INITIAL_CONFIGURATION,
                withCache = false,
                configurationGeneration = 8L,
            )
        )

        assertEquals(8L, queue.drain()?.configurationGeneration)
    }

    @Test
    fun metadataEnrichmentDoesNotOwnTheCommittedSourceCheckpoint() {
        val owner = SourceScanAttemptOwner("process", "service")
        val enrichment =
            IndexRequest(
                IndexReason.METADATA_ENRICHMENT,
                withCache = true,
                metadataProfile = MetadataProfile.FULL,
                configurationGeneration = 9L,
                attemptId = "attempt",
                attemptOwner = owner,
            )

        assertNull(IndexRequestPolicy.checkpointAuthority(enrichment))
        assertFalse(IndexRequestPolicy.recordsSourceOutcome(enrichment))
        assertEquals(
            9L,
            IndexRequestPolicy.checkpointGeneration(
                enrichment.copy(reason = IndexReason.INITIAL_CONFIGURATION)
            ),
        )
    }

    @Test
    fun sourceRequestsRemainAllowedToRecordSourceOutcome() {
        assertTrue(
            IndexRequestPolicy.recordsSourceOutcome(
                IndexRequest(IndexReason.USER_REFRESH, withCache = true)
            )
        )
        assertTrue(
            IndexRequestPolicy.recordsSourceOutcome(
                IndexRequest(
                    IndexReason.INITIAL_CONFIGURATION,
                    withCache = false,
                    configurationGeneration = 7L,
                )
            )
        )
    }

    @Test
    fun enrichmentMergedWhileSourceScanRunsCannotReplaceItsAttemptAuthority() {
        val owner = SourceScanAttemptOwner("process", "service")
        val source =
            IndexRequest(
                IndexReason.INITIAL_CONFIGURATION,
                withCache = false,
                metadataProfile = MetadataProfile.LEAN,
                configurationGeneration = 10L,
                sourceKeys = setOf("direct:internal"),
                attemptId = "source-attempt",
                attemptOwner = owner,
            )
        val merged =
            IndexRequestPolicy.merge(
                source,
                IndexRequest(
                    IndexReason.METADATA_ENRICHMENT,
                    withCache = true,
                    metadataProfile = MetadataProfile.FULL,
                    configurationGeneration = 10L,
                ),
            )

        assertEquals(source, merged)
        assertEquals("source-attempt", IndexRequestPolicy.checkpointAuthority(merged)?.attemptId)
    }
}
