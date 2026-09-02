/*
 * Copyright (c) 2026 Auxio Project
 * PrimitiveQueueHandoffGateTest.kt is part of Auxio.
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
import kotlin.test.assertTrue

class PrimitiveQueueHandoffGateTest {
    private val key = PrimitiveQueueHandoffGate.Key(sessionId = 7L, revision = 11L)

    @Test
    fun libraryReadyInteractionWaitsWhileCanonicalPreparationRuns() {
        val gate = PrimitiveQueueHandoffGate()

        assertEquals(
            PrimitiveQueueHandoffGate.Decision.PREPARE,
            gate.requestHandoff(key, libraryReady = true),
        )
    }

    @Test
    fun preparedCanonicalQueueIsImmediatelyPromotable() {
        val gate = PrimitiveQueueHandoffGate()
        gate.onPrepared(key)

        assertTrue(gate.isPrepared(key))
        assertEquals(
            PrimitiveQueueHandoffGate.Decision.PROMOTE,
            gate.requestHandoff(key, libraryReady = true),
        )
    }

    @Test
    fun libraryNotReadyKeepsPrimitivePathFailOpen() {
        val gate = PrimitiveQueueHandoffGate()

        assertEquals(
            PrimitiveQueueHandoffGate.Decision.BYPASS,
            gate.requestHandoff(key, libraryReady = false),
        )
    }

    @Test
    fun failedHydrationBypassesForSameQueueRevision() {
        val gate = PrimitiveQueueHandoffGate()
        gate.onFailed(key)

        assertEquals(
            PrimitiveQueueHandoffGate.Decision.BYPASS,
            gate.requestHandoff(key, libraryReady = true),
        )
    }

    @Test
    fun newLibraryGenerationCanRetryFailedHydration() {
        val gate = PrimitiveQueueHandoffGate()
        gate.onFailed(key)
        gate.onLibraryChanged(key)

        assertEquals(
            PrimitiveQueueHandoffGate.Decision.PREPARE,
            gate.requestHandoff(key, libraryReady = true),
        )
    }

    @Test
    fun newLibraryGenerationInvalidatesPreparedHydration() {
        val gate = PrimitiveQueueHandoffGate()
        gate.onPrepared(key)
        gate.onLibraryChanged(key)

        assertEquals(
            PrimitiveQueueHandoffGate.Decision.PREPARE,
            gate.requestHandoff(key, libraryReady = true),
        )
    }

    @Test
    fun differentQueueRevisionDoesNotInheritOldFailure() {
        val gate = PrimitiveQueueHandoffGate()
        gate.onFailed(key)
        val newRevision = key.copy(revision = key.revision + 1)

        assertEquals(
            PrimitiveQueueHandoffGate.Decision.PREPARE,
            gate.requestHandoff(newRevision, libraryReady = true),
        )
    }
}
