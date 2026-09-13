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

    fun isAuthoritative(outcome: SourceScanOutcome?): Boolean = outcome is SourceScanOutcome.Success

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
    ): RawCanonicalTarget? =
        when {
            hasPersistedQueue -> RawCanonicalTarget.PERSISTED_QUEUE
            allowAllSongsFallback -> RawCanonicalTarget.ALL_SONGS
            else -> null
        }

    fun descriptorForRawIntent(
        descriptor: QueueDescriptor?,
        skipDelta: Int,
        seekPositionMs: Long?,
    ): QueueDescriptor? {
        descriptor ?: return null
        val target =
            (descriptor.currentLogicalPosition.toLong() + skipDelta)
                .coerceIn(0L, (descriptor.totalCount - 1).coerceAtLeast(0).toLong())
                .toInt()
        val position = seekPositionMs ?: if (skipDelta != 0) 0L else descriptor.positionMs
        return descriptor.copy(
            currentLogicalPosition = target,
            positionMs = position.coerceAtLeast(0L),
        )
    }

    fun isSameReconciliation(
        activeGeneration: Long?,
        activeOutcome: SourceScanOutcome?,
        requestedGeneration: Long,
        requestedOutcome: SourceScanOutcome?,
    ): Boolean = activeGeneration == requestedGeneration && activeOutcome == requestedOutcome

    fun rawCurrentMatchesExpected(expectedUid: Music.UID?, resolvedRawUid: Music.UID?): Boolean =
        expectedUid != null && resolvedRawUid == expectedUid

    /**
     * A Fast Resume retry must perform a real source enumeration. IncrementalIndexPlanner maps
     * withCache=false to force=true, so this cannot collapse into the unchanged-source no-op path.
     */
    fun forcedRescanRequest(): IndexRequest =
        IndexRequest(reason = IndexReason.USER_REFRESH, withCache = false)
}
