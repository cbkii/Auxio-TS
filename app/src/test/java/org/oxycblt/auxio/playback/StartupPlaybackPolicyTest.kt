/*
 * Copyright (c) 2026 Auxio Project
 * StartupPlaybackPolicyTest.kt is part of Auxio.
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

package org.oxycblt.auxio.playback

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.oxycblt.auxio.playback.state.RestoreOutcome
import org.oxycblt.auxio.playback.state.RestoreProgress

class StartupPlaybackPolicyTest {
    @Test
    fun `autoplay changes restore play flag but not route eligibility`() {
        assertFalse(StartupPlaybackPolicy.restoreActionForLaunch(false).play)
        assertTrue(StartupPlaybackPolicy.restoreActionForLaunch(true).play)
        assertTrue(
            StartupPlaybackPolicy.startupRoute(input(hasNormalSong = true))
                is StartupPanelDecision.RequestRoute
        )
    }

    @Test
    fun `Topway cold launch waits for its tracked restore`() {
        val request =
            StartupPlaybackPolicy.startupRoute(input()) as StartupPanelDecision.RequestRoute
        assertEquals(OpenPanel.PLAYBACK, request.destination)
        assertTrue(request.waitForSong)
        assertTrue(request.restoreBound)
    }

    @Test
    fun `standard flavor preserves home by default`() {
        val kept =
            StartupPlaybackPolicy.startupRoute(
                input(
                    launchToPanel = false,
                    topwayCompatFlavor = false,
                    headUnitLandscapeMode = false,
                )
            ) as StartupPanelDecision.KeepCurrent
        assertEquals("launch-to-panel-disabled", kept.reason)
    }

    @Test
    fun `warm return without restored-task flag preserves current UI`() {
        val kept =
            StartupPlaybackPolicy.startupRoute(input(coldLaunch = false))
                as StartupPanelDecision.KeepCurrent
        assertEquals("not-new-cold-launch", kept.reason)
    }

    @Test
    fun `explicit queue wins over generic startup`() {
        val request =
            StartupPlaybackPolicy.startupRoute(
                input(explicitDestination = OpenPanel.PLAYBACK_QUEUE)
            ) as StartupPanelDecision.RequestRoute
        assertEquals(PanelRoutePriority.EXPLICIT, request.priority)
        assertFalse(request.restoreBound)
    }

    @Test
    fun `first setup empty and recovery never route generically`() {
        listOf(
                StartupLibraryRouteState.NEEDS_SOURCE,
                StartupLibraryRouteState.EMPTY,
                StartupLibraryRouteState.RECOVERY,
            )
            .forEach { state ->
                assertTrue(
                    StartupPlaybackPolicy.startupRoute(input(libraryState = state))
                        is StartupPanelDecision.KeepCurrent
                )
            }
    }

    @Test
    fun `library ready then restored song fulfils request`() {
        assertTrue(decide(RestoreOutcome.WAITING_FOR_PLAYER) is PendingPanelRouteDecision.Wait)
        assertTrue(
            decide(RestoreOutcome.RESTORED_EXISTING_SESSION, hasNormalSong = true)
                is PendingPanelRouteDecision.Apply
        )
    }

    @Test
    fun `restored song before readiness also fulfils request`() {
        assertTrue(
            decide(
                RestoreOutcome.RESTORED_EXISTING_SESSION,
                hasNormalSong = true,
                libraryState = StartupLibraryRouteState.CHECKING,
            )
                is PendingPanelRouteDecision.Apply
        )
    }

    @Test
    fun `no-session terminal state cannot be fulfilled by later manual song`() {
        assertTrue(decide(RestoreOutcome.NO_SAVED_SESSION) is PendingPanelRouteDecision.Cancel)
        assertTrue(
            decide(RestoreOutcome.NO_SAVED_SESSION, hasNormalSong = true)
                is PendingPanelRouteDecision.Cancel
        )
    }

    @Test
    fun `raw resume waits until normal-song reconciliation`() {
        assertTrue(
            decide(RestoreOutcome.RAW_FAST_RESUME_ACTIVE, rawFastResumeActive = true)
                is PendingPanelRouteDecision.Wait
        )
        assertTrue(
            decide(RestoreOutcome.RESTORED_EXISTING_SESSION, hasNormalSong = true)
                is PendingPanelRouteDecision.Apply
        )
    }

    @Test
    fun `stale restore token cannot fulfil newer route`() {
        val decision =
            StartupPlaybackPolicy.pendingRouteDecision(
                pendingInput(
                    restoreProgress = RestoreProgress(8, RestoreOutcome.RESTORED_EXISTING_SESSION),
                    hasNormalSong = true,
                )
            ) as PendingPanelRouteDecision.Cancel
        assertEquals("restore-request-superseded", decision.reason)
    }

    @Test
    fun `configuration recreation retains same pending decision`() {
        val state = pendingInput(RestoreProgress(7, RestoreOutcome.WAITING_FOR_LIBRARY))
        assertEquals(
            StartupPlaybackPolicy.pendingRouteDecision(state),
            StartupPlaybackPolicy.pendingRouteDecision(state),
        )
    }

    @Test
    fun `failed and cancelled restores terminate route`() {
        listOf(RestoreOutcome.FAILED, RestoreOutcome.CANCELLED).forEach { outcome ->
            assertTrue(decide(outcome) is PendingPanelRouteDecision.Cancel)
        }
    }

    private fun input(
        coldLaunch: Boolean = true,
        launchToPanel: Boolean = true,
        topwayCompatFlavor: Boolean = true,
        headUnitLandscapeMode: Boolean = true,
        libraryState: StartupLibraryRouteState = StartupLibraryRouteState.READY_OR_UNKNOWN,
        hasNormalSong: Boolean = false,
        explicitDestination: OpenPanel? = null,
    ) =
        StartupPanelInput(
            coldLaunch = coldLaunch,
            restoredTask = false,
            launchToPanel = launchToPanel,
            topwayCompatFlavor = topwayCompatFlavor,
            headUnitLandscapeMode = headUnitLandscapeMode,
            libraryState = libraryState,
            hasNormalSong = hasNormalSong,
            rawFastResumeActive = false,
            explicitDestination = explicitDestination,
        )

    private fun route() =
        PanelRouteRequest(
            id = 11,
            destination = OpenPanel.PLAYBACK,
            origin = PanelRouteOrigin.STARTUP_RESTORE,
            priority = PanelRoutePriority.STARTUP,
            waitForSong = true,
            reason = "test",
            restoreRequestId = 7,
        )

    private fun pendingInput(
        restoreProgress: RestoreProgress?,
        hasNormalSong: Boolean = false,
        rawFastResumeActive: Boolean = false,
        libraryState: StartupLibraryRouteState = StartupLibraryRouteState.READY_OR_UNKNOWN,
    ) =
        PendingPanelRouteInput(
            route = route(),
            libraryState = libraryState,
            hasNormalSong = hasNormalSong,
            rawFastResumeActive = rawFastResumeActive,
            restoreProgress = restoreProgress,
        )

    private fun decide(
        outcome: RestoreOutcome,
        hasNormalSong: Boolean = false,
        rawFastResumeActive: Boolean = false,
        libraryState: StartupLibraryRouteState = StartupLibraryRouteState.READY_OR_UNKNOWN,
    ) =
        StartupPlaybackPolicy.pendingRouteDecision(
            pendingInput(
                restoreProgress = RestoreProgress(7, outcome),
                hasNormalSong = hasNormalSong,
                rawFastResumeActive = rawFastResumeActive,
                libraryState = libraryState,
            )
        )
}
