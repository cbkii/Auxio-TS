/*
 * Copyright (c) 2026 Auxio Project
 * IncrementalScan.kt is part of Auxio.
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

package org.oxycblt.musikr.cache

import kotlinx.coroutines.flow.Flow
import org.oxycblt.musikr.fs.File
import org.oxycblt.musikr.fs.SourceSnapshot
import org.oxycblt.musikr.library.MetadataProfile

/** Immutable source-scoped work plan produced before recursive exploration. */
data class IncrementalScanPlan(
    val scanId: String,
    val scanSources: List<SourceSnapshot>,
    val reuseSourceKeys: Set<String>,
    val unavailableSourceKeys: Set<String>,
    val metadataProfile: MetadataProfile,
    val configurationRevision: Long,
    val force: Boolean,
    val scanReasons: Map<String, SourceScanReason> = emptyMap(),
    val removedSourceKeys: Set<String> = emptySet(),
    val enrichmentOnly: Boolean = false,
) {
    val scanSourceKeys: Set<String> = scanSources.mapTo(linkedSetOf()) { it.sourceKey }

    val hasWork: Boolean
        get() = scanSources.isNotEmpty() || removedSourceKeys.isNotEmpty()

    companion object {
        /** Single definition of the optional metadata-only lane. */
        fun isEnrichmentOnly(
            scanSources: List<SourceSnapshot>,
            removedSourceKeys: Set<String>,
            scanReasons: Map<String, SourceScanReason>,
        ): Boolean =
            scanSources.isNotEmpty() &&
                removedSourceKeys.isEmpty() &&
                scanSources.all {
                    scanReasons[it.sourceKey] == SourceScanReason.METADATA_PROFILE_UPGRADE
                }
    }
}

/** Result of atomically publishing all successful source generations in a scan. */
data class IncrementalScanCommit(
    val scanId: String,
    val committedSources: Set<String>,
    val reusedSources: Set<String>,
    val unavailableSources: Set<String>,
    val failedSources: Map<String, String>,
    val changedRows: Int,
    val removedRows: Int,
    val metadataProfile: MetadataProfile,
    val removedSources: Set<String> = emptySet(),
    val enrichmentOnly: Boolean = false,
    val enrichmentComplete: Boolean = true,
    val unresolvedItems: Int = 0,
)

/**
 * Optional durable scan protocol implemented by Room-backed caches.
 *
 * Writes made between [beginScan] and [commitScan] are staged and remain invisible to normal
 * readers. [abortScan] must preserve every previously committed generation.
 */
interface IncrementalCache {
    suspend fun planScan(
        snapshots: List<SourceSnapshot>,
        force: Boolean,
        metadataProfile: MetadataProfile,
        configurationRevision: Long,
    ): IncrementalScanPlan

    /**
     * Policy-aware planning entry point. Existing cache implementations retain automatic advisory
     * validation through [planScan]. In manual mode the maintained app passes false so age-only
     * advisory expiry cannot create a new source generation.
     *
     * If a standalone FULL plan contains only age-expired advisory sources, they are downgraded to
     * the optional metadata-enrichment lane so a genuine legacy artwork/metadata repair can still
     * run without acquiring source-membership authority. In any mixed authoritative/removal plan,
     * age-only sources are reused instead of being swept into the sibling source generation.
     */
    suspend fun planScan(
        snapshots: List<SourceSnapshot>,
        force: Boolean,
        metadataProfile: MetadataProfile,
        configurationRevision: Long,
        allowAdvisoryExpiry: Boolean,
    ): IncrementalScanPlan {
        val planned = planScan(snapshots, force, metadataProfile, configurationRevision)
        if (allowAdvisoryExpiry || force) return planned

        val expiredKeys =
            planned.scanReasons
                .filterValues { it == SourceScanReason.ADVISORY_FINGERPRINT_EXPIRED }
                .keys
        if (expiredKeys.isEmpty()) return planned

        val nonExpiredSources = planned.scanSources.filterNot { it.sourceKey in expiredKeys }
        if (
            metadataProfile == MetadataProfile.FULL &&
                nonExpiredSources.isEmpty() &&
                planned.removedSourceKeys.isEmpty()
        ) {
            val reasons =
                planned.scanReasons.mapValues { (sourceKey, reason) ->
                    if (sourceKey in expiredKeys) {
                        SourceScanReason.METADATA_PROFILE_UPGRADE
                    } else {
                        reason
                    }
                }
            return planned.copy(
                scanReasons = reasons,
                enrichmentOnly =
                    IncrementalScanPlan.isEnrichmentOnly(
                        scanSources = planned.scanSources,
                        removedSourceKeys = planned.removedSourceKeys,
                        scanReasons = reasons,
                    ),
            )
        }

        val retainedReasons = planned.scanReasons.filterKeys { it !in expiredKeys }
        return planned.copy(
            scanSources = nonExpiredSources,
            reuseSourceKeys = planned.reuseSourceKeys + expiredKeys,
            scanReasons = retainedReasons,
            enrichmentOnly =
                IncrementalScanPlan.isEnrichmentOnly(
                    scanSources = nonExpiredSources,
                    removedSourceKeys = planned.removedSourceKeys,
                    scanReasons = retainedReasons,
                ),
        )
    }

    suspend fun beginScan(plan: IncrementalScanPlan)

    /** Record a discovered file, even when its cached metadata is unchanged. */
    suspend fun markSeen(file: File, cachedFile: CachedFile? = null)

    /** Stream committed cached rows for sources intentionally skipped by the current plan. */
    fun reusedCachedFiles(sourceKeys: Set<String>): Flow<CachedFile>

    /** Stage changed metadata; returns false when no incremental scan is active. */
    suspend fun stage(cachedFile: CachedFile): Boolean

    /**
     * Record one enumerated item that became unavailable before metadata extraction.
     *
     * Implementations may carry a previously committed row forward into the pending generation; a
     * newly stale row with no committed predecessor is simply omitted. This uncertainty is
     * item-scoped and must not by itself fail the whole source.
     */
    suspend fun markItemUnavailable(file: File): Boolean = false

    /** Mark one source failed while allowing sibling source generations to commit. */
    suspend fun markSourceFailed(sourceKey: String, detail: String)

    /** Commit staged rows only while [commitGuard] owns the current scan. */
    suspend fun commitScan(commitGuard: () -> Boolean = { true }): IncrementalScanCommit

    suspend fun abortScan(cause: Throwable? = null)

    /** Persist an observer invalidation without performing an expensive read. */
    suspend fun invalidateSource(sourceKey: String? = null)

    fun activePlan(): IncrementalScanPlan?
}

internal val MetadataProfile.incrementalRank: Int
    get() =
        when (this) {
            MetadataProfile.LEAN -> 0
            MetadataProfile.FULL -> 1
        }
