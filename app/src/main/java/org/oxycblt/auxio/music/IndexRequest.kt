/*
 * Copyright (c) 2026 Auxio Project
 * IndexRequest.kt is part of Auxio.
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

import org.oxycblt.musikr.library.MetadataProfile

enum class IndexReason {
    INITIAL_CONFIGURATION,
    USER_RETRY,
    USER_REFRESH,
    STORAGE_MOUNTED,
    SOURCE_OBSERVER,
    COMPATIBILITY_RECOVERY,
    METADATA_ENRICHMENT,
}

data class IndexRequest(
    val reason: IndexReason,
    val withCache: Boolean,
    val metadataProfile: MetadataProfile? = null,
    val configurationGeneration: Long? = null,
    val sourceKeys: Set<String>? = null,
    val attemptId: String? = null,
    val attemptOwner: SourceScanAttemptOwner? = null,
)

data class SourceScanAttemptAuthority(
    val generation: Long,
    val attemptId: String,
    val owner: SourceScanAttemptOwner,
)

/** Shared request semantics used on both sides of the repository/service attachment boundary. */
internal object IndexRequestPolicy {
    /**
     * Metadata enrichment reuses the generation that already committed the lean library, but it
     * does not own that source-configuration checkpoint. An interrupted optional enrichment must
     * therefore never regress a committed source generation back to pending.
     */
    fun requiresAttemptClaim(request: IndexRequest): Boolean =
        request.reason == IndexReason.INITIAL_CONFIGURATION ||
            request.reason == IndexReason.USER_RETRY ||
            request.reason == IndexReason.STORAGE_MOUNTED

    /** Whether this request may replace the compatibility outcome of a source scan. */
    fun recordsSourceOutcome(request: IndexRequest): Boolean =
        request.reason != IndexReason.METADATA_ENRICHMENT

    /**
     * Whether a result computed for [request] has been overtaken by a newer source configuration.
     *
     * Optional lanes — enrichment above all — run without a checkpoint lease, so nothing else stops
     * a long enrichment started under an older configuration from overwriting the library a newer
     * authoritative scan has already committed. A request that carries no generation predates the
     * durable checkpoint and is left alone.
     */
    fun isSupersededByNewerConfiguration(request: IndexRequest, currentGeneration: Long): Boolean {
        val requestGeneration = request.configurationGeneration ?: return false
        return requestGeneration < currentGeneration
    }

    fun checkpointAuthority(request: IndexRequest): SourceScanAttemptAuthority? {
        if (!requiresAttemptClaim(request)) return null
        return SourceScanAttemptAuthority(
            generation = request.configurationGeneration ?: return null,
            attemptId = request.attemptId ?: return null,
            owner = request.attemptOwner ?: return null,
        )
    }

    fun checkpointGeneration(request: IndexRequest): Long? =
        checkpointAuthority(request)?.generation

    /**
     * Whether an interruption or cancellation outcome should be recorded for [request].
     *
     * Non-authoritative requests (no checkpoint lease) always record so that non-source refresh
     * interruptions are visible. Authoritative requests record only when the durable checkpoint
     * completion was accepted; a rejected completion means the checkpoint was already terminal, and
     * a duplicate late interruption must not overwrite that outcome.
     */
    fun shouldRecordInterruptionOutcome(
        request: IndexRequest,
        durableCompletionAccepted: Boolean,
    ): Boolean = checkpointAuthority(request) == null || durableCompletionAccepted

    /** Builds a user retry without bypassing an active retryable source checkpoint. */
    fun sourceRetryRequest(
        checkpoint: SourceConfigurationCheckpoint?,
        currentGeneration: Long,
        configuredSourceKeys: Set<String>,
        hasRevision: Boolean,
        allowUnscopedSources: Boolean = false,
    ): IndexRequest? {
        if (
            checkpoint == null || checkpoint.state == SourceConfigurationCheckpoint.State.COMMITTED
        ) {
            if (configuredSourceKeys.isEmpty() && !allowUnscopedSources) return null
            return IndexRequest(
                reason = IndexReason.USER_REFRESH,
                withCache = true,
                configurationGeneration = currentGeneration,
                sourceKeys = configuredSourceKeys.takeIf { it.isNotEmpty() },
            )
        }
        if (
            checkpoint.state != SourceConfigurationCheckpoint.State.RUNNING &&
                !checkpoint.canClaim(SourceScanClaimReason.USER_RETRY)
        ) {
            return null
        }
        val retrySourceKeys = checkpoint.unresolvedSourceKeys.ifEmpty { configuredSourceKeys }
        if (retrySourceKeys.isEmpty() && !allowUnscopedSources) return null
        return IndexRequest(
            reason = IndexReason.USER_RETRY,
            withCache =
                checkpoint.state == SourceConfigurationCheckpoint.State.PARTIALLY_COMMITTED &&
                    hasRevision,
            configurationGeneration = checkpoint.generation,
            sourceKeys = retrySourceKeys.takeIf { it.isNotEmpty() },
        )
    }

    fun merge(current: IndexRequest?, incoming: IndexRequest): IndexRequest {
        if (current == null) return incoming
        val currentGeneration = current.configurationGeneration
        val incomingGeneration = incoming.configurationGeneration
        if (
            currentGeneration != null &&
                incomingGeneration != null &&
                currentGeneration != incomingGeneration
        ) {
            return if (incomingGeneration > currentGeneration) incoming else current
        }

        val currentRequiresClaim = requiresAttemptClaim(current)
        val incomingRequiresClaim = requiresAttemptClaim(incoming)
        if (currentRequiresClaim != incomingRequiresClaim) {
            return if (currentRequiresClaim) current else incoming
        }

        val primary =
            if (priority(incoming.reason) > priority(current.reason)) incoming else current
        val secondary = if (primary === current) incoming else current
        if (requiresAttemptClaim(primary)) {
            return if (requiresAttemptClaim(secondary)) {
                primary.copy(sourceKeys = mergeSourceKeys(primary.sourceKeys, secondary.sourceKeys))
            } else {
                primary
            }
        }
        return primary.copy(
            withCache = current.withCache && incoming.withCache,
            metadataProfile = strongerProfile(current.metadataProfile, incoming.metadataProfile),
            configurationGeneration = incomingGeneration ?: currentGeneration,
            sourceKeys = mergeSourceKeys(current.sourceKeys, incoming.sourceKeys),
        )
    }

    private fun priority(reason: IndexReason): Int =
        when (reason) {
            IndexReason.INITIAL_CONFIGURATION -> 700
            IndexReason.USER_RETRY -> 600
            IndexReason.USER_REFRESH -> 500
            IndexReason.STORAGE_MOUNTED -> 400
            IndexReason.SOURCE_OBSERVER -> 300
            IndexReason.COMPATIBILITY_RECOVERY -> 200
            IndexReason.METADATA_ENRICHMENT -> 100
        }

    private fun strongerProfile(
        first: MetadataProfile?,
        second: MetadataProfile?,
    ): MetadataProfile? =
        when {
            first == MetadataProfile.FULL || second == MetadataProfile.FULL -> MetadataProfile.FULL
            first == MetadataProfile.LEAN || second == MetadataProfile.LEAN -> MetadataProfile.LEAN
            else -> null
        }

    private fun mergeSourceKeys(first: Set<String>?, second: Set<String>?): Set<String>? =
        when {
            first == null || second == null -> null
            else -> first + second
        }
}
