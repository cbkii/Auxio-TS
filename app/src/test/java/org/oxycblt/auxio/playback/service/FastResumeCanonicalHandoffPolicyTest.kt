/*
 * Copyright (c) 2026 Auxio Project
 * FastResumeCanonicalHandoffPolicyTest.kt is part of Auxio.
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

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.oxycblt.auxio.music.IndexReason
import org.oxycblt.auxio.music.SourceScanOutcome
import org.oxycblt.auxio.playback.persist.QueueDescriptor
import org.oxycblt.auxio.playback.state.RepeatMode
import org.oxycblt.auxio.playback.state.ShuffleScope
import org.oxycblt.musikr.Music

class FastResumeCanonicalHandoffPolicyTest {
    @Test
    fun partialOrUnavailableLibraryCannotOwnCanonicalFastResumeQueue() {
        assertFalse(
            FastResumeCanonicalHandoffPolicy.isAuthoritative(
                SourceScanOutcome.Partial(setOf("internal"), setOf("usb"))
            )
        )
        assertFalse(
            FastResumeCanonicalHandoffPolicy.isAuthoritative(
                SourceScanOutcome.TemporarilyUnavailable(setOf("usb"))
            )
        )
        assertFalse(FastResumeCanonicalHandoffPolicy.isAuthoritative(null))
    }

    @Test
    fun completeSourceSuccessMayOwnCanonicalFastResumeQueue() {
        assertTrue(
            FastResumeCanonicalHandoffPolicy.isAuthoritative(
                SourceScanOutcome.Success(setOf("internal", "usb"))
            )
        )
    }

    @Test
    fun sourceAuthorityIsBoundToExactLibraryGenerationAndOutcome() {
        val success = SourceScanOutcome.Success(setOf("internal", "usb"))
        val partial = SourceScanOutcome.Partial(setOf("internal"), setOf("usb"))

        assertTrue(
            FastResumeCanonicalHandoffPolicy.isAuthoritative(
                generation = 7L,
                expectedGeneration = 7L,
                outcome = success,
                expectedOutcome = success,
            )
        )
        assertFalse(
            FastResumeCanonicalHandoffPolicy.isAuthoritative(
                generation = 6L,
                expectedGeneration = 7L,
                outcome = success,
                expectedOutcome = success,
            )
        )
        assertFalse(
            FastResumeCanonicalHandoffPolicy.isAuthoritative(
                generation = 7L,
                expectedGeneration = 7L,
                outcome = success,
                expectedOutcome = partial,
            )
        )
    }

    @Test
    fun persistedQueueAlwaysWinsOverAllSongsFallback() {
        assertEquals(
            FastResumeCanonicalHandoffPolicy.RawCanonicalTarget.PERSISTED_QUEUE,
            FastResumeCanonicalHandoffPolicy.rawCanonicalTarget(
                hasPersistedQueue = true,
                allowAllSongsFallback = true,
            ),
        )
        assertEquals(
            FastResumeCanonicalHandoffPolicy.RawCanonicalTarget.ALL_SONGS,
            FastResumeCanonicalHandoffPolicy.rawCanonicalTarget(
                hasPersistedQueue = false,
                allowAllSongsFallback = true,
            ),
        )
        assertNull(
            FastResumeCanonicalHandoffPolicy.rawCanonicalTarget(
                hasPersistedQueue = false,
                allowAllSongsFallback = false,
            )
        )
    }

    @Test
    fun rawDescriptorFoldsColdSkipAndSeekWithoutChangingQueueIdentity() {
        val descriptor =
            QueueDescriptor(
                sessionId = 42L,
                totalCount = 8,
                currentLogicalPosition = 3,
                positionMs = 9_000L,
                repeatMode = RepeatMode.ALL,
                shuffleScope = ShuffleScope.ALL,
                revision = 7L,
                updatedAtMs = 123L,
            )

        val skipped =
            requireNotNull(
                FastResumeCanonicalHandoffPolicy.descriptorForRawIntent(
                    descriptor = descriptor,
                    skipDelta = 2,
                    seekPositionMs = null,
                )
            )
        assertEquals(42L, skipped.sessionId)
        assertEquals(7L, skipped.revision)
        assertEquals(5, skipped.currentLogicalPosition)
        assertEquals(0L, skipped.positionMs)
        assertEquals(ShuffleScope.ALL, skipped.shuffleScope)

        val sought =
            requireNotNull(
                FastResumeCanonicalHandoffPolicy.descriptorForRawIntent(
                    descriptor = descriptor,
                    skipDelta = 0,
                    seekPositionMs = 4_321L,
                )
            )
        assertEquals(3, sought.currentLogicalPosition)
        assertEquals(4_321L, sought.positionMs)
    }

    @Test
    fun rawReconciliationSingleFlightRequiresExactGenerationAndOutcome() {
        val success = SourceScanOutcome.Success(setOf("internal", "usb"))
        val newerSuccess = SourceScanOutcome.Success(setOf("internal", "usb", "sd"))

        assertTrue(
            FastResumeCanonicalHandoffPolicy.isSameReconciliation(
                activeGeneration = 4L,
                activeOutcome = success,
                requestedGeneration = 4L,
                requestedOutcome = success,
            )
        )
        assertFalse(
            FastResumeCanonicalHandoffPolicy.isSameReconciliation(
                activeGeneration = 4L,
                activeOutcome = success,
                requestedGeneration = 5L,
                requestedOutcome = success,
            )
        )
        assertFalse(
            FastResumeCanonicalHandoffPolicy.isSameReconciliation(
                activeGeneration = 4L,
                activeOutcome = success,
                requestedGeneration = 4L,
                requestedOutcome = newerSuccess,
            )
        )
    }

    @Test
    fun persistedRawCurrentMustMatchExpectedCanonicalSong() {
        val expected =
            requireNotNull(Music.UID.fromString("uas00000000-0000-0000-0000-000000000001"))
        val other = requireNotNull(Music.UID.fromString("uas00000000-0000-0000-0000-000000000002"))

        assertTrue(FastResumeCanonicalHandoffPolicy.rawCurrentMatchesExpected(expected, expected))
        assertFalse(FastResumeCanonicalHandoffPolicy.rawCurrentMatchesExpected(expected, other))
        assertFalse(FastResumeCanonicalHandoffPolicy.rawCurrentMatchesExpected(expected, null))
        assertFalse(FastResumeCanonicalHandoffPolicy.rawCurrentMatchesExpected(null, expected))
    }

    @Test
    fun retryBuildsConcreteForcedUserRefresh() {
        val request = FastResumeCanonicalHandoffPolicy.forcedRescanRequest()

        assertEquals(IndexReason.USER_REFRESH, request.reason)
        assertFalse(request.withCache)
        assertNull(request.configurationGeneration)
        assertNull(request.sourceKeys)
    }

    @Test
    fun retryBudgetCoversThreeMinuteConvergenceWindow() {
        assertEquals(
            FastResumeCanonicalHandoffPolicy.RETRY_WINDOW_MS,
            FastResumeCanonicalHandoffPolicy.RETRY_INTERVAL_MS *
                FastResumeCanonicalHandoffPolicy.MAX_RETRY_ATTEMPTS,
        )
    }
}
