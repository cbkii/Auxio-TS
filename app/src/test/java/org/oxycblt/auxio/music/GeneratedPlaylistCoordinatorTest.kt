/*
 * Copyright (c) 2026 Auxio Project
 * GeneratedPlaylistCoordinatorTest.kt is part of Auxio.
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

import java.util.concurrent.atomic.AtomicInteger
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class GeneratedPlaylistCoordinatorTest {
    @Test
    fun `new library fingerprint cancels stale generation`() = runBlocking {
        val coordinator = openCoordinator()
        val firstStarted = CompletableDeferred<Unit>()
        val firstCancelled = CompletableDeferred<Unit>()
        val secondPublished = CompletableDeferred<Unit>()

        coordinator.request(enabled = true, fingerprint = "old", force = false) {
            firstStarted.complete(Unit)
            try {
                awaitCancellation()
            } finally {
                firstCancelled.complete(Unit)
            }
        }
        withTimeout(2_000L) { firstStarted.await() }

        coordinator.request(enabled = true, fingerprint = "new", force = false) {
            secondPublished.complete(Unit)
            true
        }

        withTimeout(2_000L) {
            firstCancelled.await()
            secondPublished.await()
            coordinator.status.first { it == GeneratedPlaylistStatus.UP_TO_DATE }
        }
        assertEquals(GeneratedPlaylistStatus.UP_TO_DATE, coordinator.status.value)
    }

    @Test
    fun `same active fingerprint coalesces`() = runBlocking {
        val coordinator = openCoordinator()
        val calls = AtomicInteger()
        val firstStarted = CompletableDeferred<Unit>()
        val release = CompletableDeferred<Unit>()

        val projection: suspend (Boolean) -> Boolean = {
            calls.incrementAndGet()
            firstStarted.complete(Unit)
            release.await()
            true
        }
        coordinator.request(
            enabled = true,
            fingerprint = "same",
            force = false,
            project = projection,
        )
        withTimeout(2_000L) { firstStarted.await() }
        coordinator.request(
            enabled = true,
            fingerprint = "same",
            force = false,
            project = projection,
        )
        release.complete(Unit)

        withTimeout(2_000L) {
            coordinator.status.first { it == GeneratedPlaylistStatus.UP_TO_DATE }
        }
        assertEquals(1, calls.get())
        assertTrue(coordinator.status.value == GeneratedPlaylistStatus.UP_TO_DATE)
    }

    private fun openCoordinator(): GeneratedPlaylistCoordinator {
        val readiness = StartupReadinessController()
        val gate = StartupOptionalWorkGate(readiness)
        readiness.publishCapability(StartupReadinessState.QueueReady)
        gate.onRestoreFinished()
        return GeneratedPlaylistCoordinator(gate)
    }
}
