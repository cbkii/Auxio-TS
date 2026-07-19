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
) {
    val scanSourceKeys: Set<String> = scanSources.mapTo(linkedSetOf()) { it.sourceKey }

    val hasWork: Boolean
        get() = scanSources.isNotEmpty()
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

    suspend fun beginScan(plan: IncrementalScanPlan)

    /** Record a discovered file, even when its cached metadata is unchanged. */
    suspend fun markSeen(file: File, cachedFile: CachedFile? = null)

    /** Stream committed cached rows for sources intentionally skipped by the current plan. */
    fun reusedCachedFiles(sourceKeys: Set<String>): Flow<CachedFile>

    /** Stage changed metadata; returns false when no incremental scan is active. */
    suspend fun stage(cachedFile: CachedFile): Boolean

    /** Mark one source failed while allowing sibling source generations to commit. */
    suspend fun markSourceFailed(sourceKey: String, detail: String)

    suspend fun commitScan(): IncrementalScanCommit

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
