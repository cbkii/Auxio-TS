/*
 * Copyright (c) 2026 Auxio Project
 * FastResumeCanonicalHandoffPolicy.kt is part of Auxio.
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

package org.oxycblt.auxio.playback.service

import org.oxycblt.auxio.music.IndexReason
import org.oxycblt.auxio.music.IndexRequest
import org.oxycblt.auxio.music.SourceScanOutcome
import org.oxycblt.auxio.playback.persist.QueueDescriptor
import org.oxycblt.musikr.Music

/** Liveness and source-authority contract for temporary Fast Resume playback. */
internal object FastResumeCanonicalHandoffPolicy {
    const val RETRY_INTERVAL_MS = 30_000L
    const val RETRY_WINDOW_MS = 180_000L
    const val MAX_RETRY_ATTEMPTS = 6

    enum class RawCanonicalTarget {
        PERSISTED_QUEUE,
        ALL_SONGS,
    }

    /**
     * Whether the last source enumeration covered every configured source. This is required only
     * when Fast Resume must construct a *new* queue membership (the All Songs fallback), and it
     * decides whether a failed hydration justifies a forced rescan.
     */
    fun isAuthoritative(outcome: SourceScanOutcome?): Boolean = outcome is SourceScanOutcome.Success

    /**
     * Whether the published device library may be used to hydrate the *persisted* Fast Resume
     * queue.
     *
     * TS18 cold starts publish the committed cached library without enumerating sources (Topway
     * startup deliberately skips the scan), so no [SourceScanOutcome] exists for this process.
     * Waiting for [SourceScanOutcome.Success] therefore delayed the handoff until the bounded retry
     * forced a full rescan. Hydration itself is non-lossy (every persisted item, including the
     * current one, must resolve) and identity-checked, so a coherent library snapshot is sufficient
     * even when unrelated sources are unresolved: an item on an absent source simply fails
     * hydration and primitive authority is retained.
     */
    fun mayHydratePersistedQueue(
        generation: Long,
        expectedGeneration: Long,
        outcome: SourceScanOutcome?,
        expectedOutcome: SourceScanOutcome?,
    ): Boolean =
        generation == expectedGeneration && mayHydratePersistedQueue(outcome, expectedOutcome)

    /** As above, for callers that already hold the current library generation. */
    fun mayHydratePersistedQueue(
        outcome: SourceScanOutcome?,
        expectedOutcome: SourceScanOutcome?,
    ): Boolean = outcome == expectedOutcome && outcome !is SourceScanOutcome.AuthoritativeEmpty

    /**
     * A full forced rescan is only justified when hydration has already failed against the current
     * library generation and that library was not produced by a complete source enumeration.
     */
    fun shouldForceRescan(
        hydrationFailedForCurrentGeneration: Boolean,
        libraryAvailable: Boolean,
        outcome: SourceScanOutcome?,
    ): Boolean =
        outcome !is SourceScanOutcome.AuthoritativeEmpty &&
            (!libraryAvailable ||
                (hydrationFailedForCurrentGeneration && !isAuthoritative(outcome)))

    fun isAuthoritative(
        generation: Long,
        expectedGeneration: Long,
        outcome: SourceScanOutcome?,
        expectedOutcome: SourceScanOutcome?,
    ): Boolean =
        generation == expectedGeneration && outcome == expectedOutcome && isAuthoritative(outcome)

    /**
     * Raw playback may use All Songs only when no persisted queue is available and the caller
     * explicitly owns that fallback (direct-open or a restore with no saved queue).
     */
    fun rawCanonicalTarget(
        hasPersistedQueue: Boolean,
        allowAllSongsFallback: Boolean,
        libraryAuthoritative: Boolean = true,
    ): RawCanonicalTarget? =
        when {
            hasPersistedQueue -> RawCanonicalTarget.PERSISTED_QUEUE
            // A new All Songs membership must not be built from an incompletely enumerated library.
            allowAllSongsFallback && libraryAuthoritative -> RawCanonicalTarget.ALL_SONGS
            else -> null
        }

    fun descriptorForRawIntent(
        descriptor: QueueDescriptor?,
        skipDelta: Int,
        seekPositionMs: Long?,
    ): QueueDescriptor? {
        descriptor ?: return null
        // Keep raw identity pinned to the persisted current item. A cold skip is replayed only
        // after that item has been reconciled into the complete canonical queue.
        val position =
            if (skipDelta == 0) seekPositionMs ?: descriptor.positionMs else descriptor.positionMs
        return descriptor.copy(positionMs = position.coerceAtLeast(0L))
    }

    fun targetLogicalPosition(currentLogicalPosition: Int, totalCount: Int, skipDelta: Int): Int {
        if (totalCount <= 0) return 0
        return (currentLogicalPosition.toLong() + skipDelta)
            .coerceIn(0L, (totalCount - 1).toLong())
            .toInt()
    }

    fun isSameReconciliation(
        activeGeneration: Long?,
        activeOutcome: SourceScanOutcome?,
        requestedGeneration: Long,
        requestedOutcome: SourceScanOutcome?,
    ): Boolean = activeGeneration == requestedGeneration && activeOutcome == requestedOutcome

    fun rawCurrentMatchesExpected(expectedUid: Music.UID?, resolvedRawUid: Music.UID?): Boolean =
        expectedUid != null && resolvedRawUid == expectedUid

    fun suppressCanonicalPersistence(rawAuthorityActive: Boolean): Boolean = rawAuthorityActive

    /**
     * A Fast Resume retry must perform a real source enumeration. IncrementalIndexPlanner maps
     * withCache=false to force=true, so this cannot collapse into the unchanged-source no-op path.
     */
    fun forcedRescanRequest(): IndexRequest =
        IndexRequest(reason = IndexReason.USER_REFRESH, withCache = false)
}
