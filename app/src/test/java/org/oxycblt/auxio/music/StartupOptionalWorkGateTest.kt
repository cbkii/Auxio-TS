/*
 * Copyright (c) 2026 Auxio Project
 * StartupOptionalWorkGateTest.kt is part of Auxio.
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

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class StartupOptionalWorkGateTest {
    @Test
    fun `optional work requires queue readiness and terminal restore`() = runBlocking {
        val readiness = StartupReadinessController()
        val gate = StartupOptionalWorkGate(readiness)
        val waiter = launch(start = CoroutineStart.UNDISPATCHED) { gate.awaitOpen() }

        readiness.publishCapability(StartupReadinessState.QueueReady)
        assertFalse(waiter.isCompleted)

        gate.onRestoreFinished()
        withTimeout(1_000L) { waiter.join() }
        assertTrue(waiter.isCompleted)
    }

    @Test
    fun `restore completion before queue readiness remains closed`() = runBlocking {
        val readiness = StartupReadinessController()
        val gate = StartupOptionalWorkGate(readiness)
        val waiter = launch(start = CoroutineStart.UNDISPATCHED) { gate.awaitOpen() }

        gate.onRestoreFinished()
        assertFalse(waiter.isCompleted)

        readiness.publishCapability(StartupReadinessState.QueueReady)
        withTimeout(1_000L) { waiter.join() }
        assertTrue(waiter.isCompleted)
    }
}
