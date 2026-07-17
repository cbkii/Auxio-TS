/*
 * Copyright (c) 2026 Auxio Project
 * StartupPanelCoordinatorTest.kt is part of Auxio.
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

package org.oxycblt.auxio.headunit

import java.util.UUID
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.oxycblt.auxio.music.StartupLibraryStatus
import org.oxycblt.auxio.music.StartupReadinessState
import org.oxycblt.auxio.playback.OpenPanel
import org.oxycblt.auxio.playback.state.RestoreOutcome

class StartupPanelCoordinatorTest {
    @Test
    fun `generic launch targets now playing`() {
        assertEquals(OpenPanel.PLAYBACK, StartupPanelCoordinator.genericDestination())
    }

    @Test
    fun `generic route waits for transient restore`() {
        assertTrue(
            evaluate(false, RestoreOutcome.WAITING_FOR_LIBRARY)
                is StartupPanelCoordinator.RouteEvaluation.Wait
        )
    }

    @Test
    fun `process visible readiness waits for library checking`() {
        assertTrue(
            evaluate(
                false,
                RestoreOutcome.WAITING_FOR_LIBRARY,
                StartupReadinessState.ProcessVisible,
            )
                is StartupPanelCoordinator.RouteEvaluation.Wait
        )
    }

    @Test
    fun `no saved session stays terminal even after a manual song appears`() {
        assertTrue(
            evaluate(true, RestoreOutcome.NO_SAVED_SESSION)
                is StartupPanelCoordinator.RouteEvaluation.Cancel
        )
    }

    @Test
    fun `raw resume renders when primitive playback is available`() {
        assertTrue(
            evaluate(false, RestoreOutcome.RAW_FAST_RESUME_ACTIVE)
                is StartupPanelCoordinator.RouteEvaluation.Wait
        )
        assertTrue(
            evaluate(true, RestoreOutcome.RAW_FAST_RESUME_ACTIVE)
                is StartupPanelCoordinator.RouteEvaluation.Render
        )
    }

    @Test
    fun `failed and cancelled restores terminate generic routing`() {
        listOf(RestoreOutcome.FAILED, RestoreOutcome.CANCELLED).forEach { outcome ->
            assertTrue(evaluate(false, outcome) is StartupPanelCoordinator.RouteEvaluation.Cancel)
        }
    }

    @Test
    fun `terminal source status cancels generic route after restore terminates`() {
        assertTrue(
            evaluate(
                false,
                RestoreOutcome.NO_SAVED_SESSION,
                StartupReadinessState.ProcessVisible,
                StartupLibraryStatus.NeedsMusicSource,
            )
                is StartupPanelCoordinator.RouteEvaluation.Cancel
        )
    }

    @Test
    fun `explicit queue renders only after a playable item exists`() {
        val request =
            StartupPanelCoordinator.RouteRequest(
                UUID.randomUUID(),
                OpenPanel.PLAYBACK_QUEUE,
                StartupPanelCoordinator.Priority.EXPLICIT_INTENT,
                "test explicit queue",
            )
        assertTrue(
            StartupPanelCoordinator.evaluate(
                request,
                false,
                RestoreOutcome.NOT_REQUESTED,
                StartupReadinessState.FullLibraryReady,
                StartupLibraryStatus.Usable,
            ) is StartupPanelCoordinator.RouteEvaluation.Wait
        )
        assertTrue(
            StartupPanelCoordinator.evaluate(
                request,
                true,
                RestoreOutcome.NOT_REQUESTED,
                StartupReadinessState.FullLibraryReady,
                StartupLibraryStatus.Usable,
            ) is StartupPanelCoordinator.RouteEvaluation.Render
        )
    }

    private fun evaluate(
        hasSong: Boolean,
        outcome: RestoreOutcome,
        readiness: StartupReadinessState = StartupReadinessState.FullLibraryReady,
        libraryStatus: StartupLibraryStatus = StartupLibraryStatus.Usable,
    ) =
        StartupPanelCoordinator.evaluate(
            StartupPanelCoordinator.RouteRequest(
                UUID.randomUUID(),
                OpenPanel.PLAYBACK,
                StartupPanelCoordinator.Priority.GENERIC_STARTUP,
                "test generic",
            ),
            hasSong,
            outcome,
            readiness,
            libraryStatus,
        )
}
