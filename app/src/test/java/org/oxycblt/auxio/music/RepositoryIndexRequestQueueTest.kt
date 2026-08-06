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
    fun staleEnrichmentIsDiscardedAfterANewerSourceGeneration() {
        val enrichment =
            IndexRequest(
                IndexReason.METADATA_ENRICHMENT,
                withCache = true,
                metadataProfile = MetadataProfile.FULL,
                configurationGeneration = 9L,
            )

        // Optional lanes hold no attempt lease, so this is the only thing stopping a long
        // enrichment started under an older configuration from overwriting the library a newer
        // authoritative scan already committed.
        assertTrue(IndexRequestPolicy.isSupersededByNewerConfiguration(enrichment, 10L))
        assertFalse(IndexRequestPolicy.isSupersededByNewerConfiguration(enrichment, 9L))
        // A scan that somehow outran the persisted generation is not stale.
        assertFalse(IndexRequestPolicy.isSupersededByNewerConfiguration(enrichment, 8L))
        // Requests predating the durable checkpoint carry no generation and are left alone.
        assertFalse(
            IndexRequestPolicy.isSupersededByNewerConfiguration(
                enrichment.copy(configurationGeneration = null),
                10L,
            )
        )
        assertTrue(
            IndexRequestPolicy.isSupersededByNewerConfiguration(
                enrichment.copy(reason = IndexReason.USER_REFRESH),
                10L,
            )
        )
        assertTrue(
            IndexRequestPolicy.isSupersededByNewerConfiguration(
                enrichment.copy(reason = IndexReason.SOURCE_OBSERVER),
                10L,
            )
        )
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
    fun mountedSourceRecoveryRequiresAttemptAuthority() {
        val owner = SourceScanAttemptOwner("process", "service")
        val mounted =
            IndexRequest(
                reason = IndexReason.STORAGE_MOUNTED,
                withCache = true,
                configurationGeneration = 7L,
                sourceKeys = setOf("direct:usb"),
                attemptId = "mounted-attempt",
                attemptOwner = owner,
            )

        assertTrue(IndexRequestPolicy.requiresAttemptClaim(mounted))
        assertEquals(7L, IndexRequestPolicy.checkpointAuthority(mounted)?.generation)
        assertEquals("mounted-attempt", IndexRequestPolicy.checkpointAuthority(mounted)?.attemptId)
    }

    @Test
    fun retryFallsBackToNormalRefreshWithoutARetryableCheckpoint() {
        val configured = setOf("direct:internal", "direct:usb")
        val committed =
            SourceConfigurationCheckpoint(
                generation = 9L,
                state = SourceConfigurationCheckpoint.State.COMMITTED,
            )

        val withoutCheckpoint =
            requireNotNull(
                IndexRequestPolicy.sourceRetryRequest(
                    checkpoint = null,
                    currentGeneration = 9L,
                    configuredSourceKeys = configured,
                    hasRevision = true,
                )
            )
        val afterCommittedRefresh =
            requireNotNull(
                IndexRequestPolicy.sourceRetryRequest(
                    checkpoint = committed,
                    currentGeneration = 9L,
                    configuredSourceKeys = configured,
                    hasRevision = true,
                )
            )

        assertEquals(IndexReason.USER_REFRESH, withoutCheckpoint.reason)
        assertEquals(IndexReason.USER_REFRESH, afterCommittedRefresh.reason)
        assertEquals(configured, afterCommittedRefresh.sourceKeys)
        assertFalse(IndexRequestPolicy.requiresAttemptClaim(afterCommittedRefresh))
    }

    @Test
    fun retryableCheckpointStillCreatesAnAttemptClaimRequest() {
        val checkpoint =
            SourceConfigurationCheckpoint(
                generation = 11L,
                state = SourceConfigurationCheckpoint.State.FAILED_RETRYABLE,
                unresolvedSourceKeys = setOf("direct:usb"),
            )

        val retry =
            requireNotNull(
                IndexRequestPolicy.sourceRetryRequest(
                    checkpoint = checkpoint,
                    currentGeneration = 11L,
                    configuredSourceKeys = setOf("direct:internal", "direct:usb"),
                    hasRevision = true,
                )
            )

        assertEquals(IndexReason.USER_RETRY, retry.reason)
        assertEquals(setOf("direct:usb"), retry.sourceKeys)
        assertTrue(IndexRequestPolicy.requiresAttemptClaim(retry))
        assertNull(
            IndexRequestPolicy.sourceRetryRequest(
                checkpoint =
                    checkpoint.copy(state = SourceConfigurationCheckpoint.State.FAILED_FINAL),
                currentGeneration = 11L,
                configuredSourceKeys = setOf("direct:internal", "direct:usb"),
                hasRevision = true,
            )
        )
    }

    @Test
    fun mountedAttemptCannotBeDisplacedOrBroadenedByOrdinaryRefresh() {
        val mounted =
            IndexRequest(
                reason = IndexReason.STORAGE_MOUNTED,
                withCache = true,
                configurationGeneration = 12L,
                sourceKeys = setOf("direct:usb"),
            )

        val merged =
            IndexRequestPolicy.merge(
                IndexRequest(
                    reason = IndexReason.USER_REFRESH,
                    withCache = false,
                    configurationGeneration = 12L,
                ),
                mounted,
            )

        assertEquals(mounted, merged)
        assertEquals(
            mounted,
            IndexRequestPolicy.merge(
                mounted,
                IndexRequest(
                    reason = IndexReason.USER_REFRESH,
                    withCache = false,
                    configurationGeneration = 12L,
                ),
            ),
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

    @Test
    fun authoritativeInterruptionHasCheckpointAuthorityWhileNonAuthoritativeDoesNot() {
        val owner = SourceScanAttemptOwner("process", "service")
        val authoritative =
            IndexRequest(
                IndexReason.INITIAL_CONFIGURATION,
                withCache = false,
                configurationGeneration = 5L,
                attemptId = "attempt-1",
                attemptOwner = owner,
            )
        val nonAuthoritative =
            IndexRequest(IndexReason.USER_REFRESH, withCache = true, configurationGeneration = 5L)

        // Only an authoritative request has a checkpoint authority. A stale interruption using
        // an authoritative request will have its outcome rejected when the checkpoint completion
        // is rejected. A non-authoritative request records its outcome unconditionally because
        // it never writes to the checkpoint.
        assertNull(IndexRequestPolicy.checkpointAuthority(nonAuthoritative))
        val authority = IndexRequestPolicy.checkpointAuthority(authoritative)
        requireNotNull(authority)
        assertEquals(5L, authority.generation)
        assertEquals("attempt-1", authority.attemptId)
        assertTrue(IndexRequestPolicy.recordsSourceOutcome(nonAuthoritative))
        assertTrue(IndexRequestPolicy.recordsSourceOutcome(authoritative))
    }

    @Test
    fun nonAuthoritativeRefreshOutcomeIsAlwaysRecorded() {
        // A USER_REFRESH or SOURCE_OBSERVER request has no checkpoint authority. Its outcome is
        // recorded via recordsSourceOutcome regardless of any checkpoint state, because it never
        // holds a source-attempt lease.
        val refreshReasons =
            listOf(
                IndexReason.USER_REFRESH,
                IndexReason.SOURCE_OBSERVER,
                IndexReason.COMPATIBILITY_RECOVERY,
            )
        for (reason in refreshReasons) {
            val request = IndexRequest(reason, withCache = true, configurationGeneration = 3L)
            assertNull(IndexRequestPolicy.checkpointAuthority(request))
            assertTrue(IndexRequestPolicy.recordsSourceOutcome(request))
        }
    }
}
