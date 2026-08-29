/*
 * Copyright (c) 2026 Auxio Project
 * PrimitiveQueuePromotionGateTest.kt is part of Auxio.
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
import kotlin.test.assertTrue

class PrimitiveQueuePromotionGateTest {
    private val key = PrimitiveQueuePromotionGate.Key(sessionId = 7L, revision = 11L)

    @Test
    fun libraryPreparationAloneDoesNotPromoteCurrentTrack() {
        val gate = PrimitiveQueuePromotionGate()

        assertFalse(gate.onPrepared(key))
        assertTrue(gate.isPrepared(key))
    }

    @Test
    fun libraryNotReadyBypassesWithoutCreatingPromotionBoundary() {
        val gate = PrimitiveQueuePromotionGate()

        assertEquals(
            PrimitiveQueuePromotionGate.Decision.BYPASS,
            gate.requestBoundary(key, libraryReady = false),
        )
        assertFalse(gate.onPrepared(key))
    }

    @Test
    fun semanticBoundaryPromotesImmediatelyWhenCanonicalQueueIsPrepared() {
        val gate = PrimitiveQueuePromotionGate()
        gate.onPrepared(key)

        assertEquals(
            PrimitiveQueuePromotionGate.Decision.PROMOTE,
            gate.requestBoundary(key, libraryReady = true),
        )
    }

    @Test
    fun boundaryBeforePreparationIsRememberedAndPromotesWhenPreparationCompletes() {
        val gate = PrimitiveQueuePromotionGate()

        assertEquals(
            PrimitiveQueuePromotionGate.Decision.PREPARE,
            gate.requestBoundary(key, libraryReady = true),
        )
        assertTrue(gate.onPrepared(key))
    }

    @Test
    fun failedHydrationFailsOpenForTheSameQueueRevision() {
        val gate = PrimitiveQueuePromotionGate()
        gate.onFailed(key)

        assertEquals(
            PrimitiveQueuePromotionGate.Decision.BYPASS,
            gate.requestBoundary(key, libraryReady = true),
        )
    }

    @Test
    fun freshLibraryGenerationMayRetryPreviouslyFailedHydration() {
        val gate = PrimitiveQueuePromotionGate()
        gate.onFailed(key)
        gate.onLibraryChanged(key)

        assertEquals(
            PrimitiveQueuePromotionGate.Decision.PREPARE,
            gate.requestBoundary(key, libraryReady = true),
        )
    }

    @Test
    fun freshLibraryGenerationInvalidatesPreparedHydration() {
        val gate = PrimitiveQueuePromotionGate()
        gate.onPrepared(key)
        gate.onLibraryChanged(key)

        assertEquals(
            PrimitiveQueuePromotionGate.Decision.PREPARE,
            gate.requestBoundary(key, libraryReady = true),
        )
    }

    @Test
    fun successfulPreparationClearsFailureForSameQueueRevision() {
        val gate = PrimitiveQueuePromotionGate()
        gate.onFailed(key)
        gate.onPrepared(key)

        assertEquals(
            PrimitiveQueuePromotionGate.Decision.PROMOTE,
            gate.requestBoundary(key, libraryReady = true),
        )
    }

    @Test
    fun differentQueueRevisionDoesNotInheritOldFailure() {
        val gate = PrimitiveQueuePromotionGate()
        gate.onFailed(key)
        val newRevision = key.copy(revision = key.revision + 1)

        assertEquals(
            PrimitiveQueuePromotionGate.Decision.PREPARE,
            gate.requestBoundary(newRevision, libraryReady = true),
        )
    }
}
