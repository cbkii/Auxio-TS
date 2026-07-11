/*
 * Copyright (c) 2026 Auxio Project
 * StartupPlaybackPolicy.kt is part of Auxio.
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

import org.oxycblt.auxio.playback.state.DeferredPlayback
import org.oxycblt.musikr.Library

/**
 * Pure policy functions for determining startup playback behavior based on user settings.
 *
 * @author Auxio-TS contributors
 */
object StartupPlaybackPolicy {

    /**
     * Determine the [DeferredPlayback] action for a generic app launch (no explicit intent action).
     *
     * @param autoplayOnLaunch Whether the autoplay-on-launch setting is enabled.
     * @return The appropriate [DeferredPlayback.RestoreState] action.
     */
    fun restoreActionForLaunch(autoplayOnLaunch: Boolean): DeferredPlayback.RestoreState =
        DeferredPlayback.RestoreState(
            play = autoplayOnLaunch,
            fallback = DeferredPlayback.ShuffleAll(play = autoplayOnLaunch),
        )

    /**
     * Determine the [DeferredPlayback] action for a boot-triggered service start (when Activity
     * launch was blocked by background-start restrictions).
     *
     * @param autoplayOnLaunch Whether the autoplay-on-launch setting is enabled.
     * @return The appropriate [DeferredPlayback.RestoreState] action.
     */
    fun restoreActionForBoot(autoplayOnLaunch: Boolean): DeferredPlayback.RestoreState =
        DeferredPlayback.RestoreState(
            play = autoplayOnLaunch,
            fallback = DeferredPlayback.ShuffleAll(play = autoplayOnLaunch),
        )

    /**
     * Determine whether the Now Playing panel should be opened on cold launch. We don't want to
     * open the panel if this is a first/setup launch where the library is missing/empty, as the
     * setup screen or library loader is more appropriate.
     */
    fun shouldOpenPanelOnLaunch(library: Library?): Boolean {
        return library != null && !library.empty()
    }

    fun startupRoute(
        input: StartupPanelInput,
    ): StartupPanelDecision =
        when {
            input.explicitDestination != null ->
                StartupPanelDecision.RequestRoute(
                    destination = input.explicitDestination,
                    origin = PanelRouteOrigin.EXPLICIT_INTENT,
                    priority = PanelRoutePriority.EXPLICIT,
                    waitForSong = input.explicitDestination != OpenPanel.MAIN,
                    reason = "explicit-destination",
                )
            !input.coldLaunch || input.restoredTask || input.userCancelled ->
                StartupPanelDecision.KeepCurrent("not-new-cold-launch")
            input.hasNormalSong ->
                StartupPanelDecision.RequestRoute(
                    destination = OpenPanel.PLAYBACK,
                    origin = PanelRouteOrigin.STARTUP_RESTORE,
                    priority = PanelRoutePriority.STARTUP,
                    waitForSong = false,
                    reason = "restored-normal-song",
                )
            input.libraryState == StartupLibraryRouteState.NEEDS_SOURCE ->
                StartupPanelDecision.KeepCurrent("needs-source")
            input.libraryState == StartupLibraryRouteState.EMPTY ->
                StartupPanelDecision.KeepCurrent("empty-library")
            input.libraryState == StartupLibraryRouteState.RECOVERY ->
                StartupPanelDecision.KeepCurrent("library-recovery")
            input.rawFastResumeActive ->
                StartupPanelDecision.RequestRoute(
                    destination = OpenPanel.PLAYBACK,
                    origin = PanelRouteOrigin.STARTUP_RESTORE,
                    priority = PanelRoutePriority.STARTUP,
                    waitForSong = true,
                    reason = "raw-fast-resume-awaiting-reconciliation",
                )
            input.topwayCompatFlavor && input.headUnitLandscapeMode ->
                StartupPanelDecision.RequestRoute(
                    destination = OpenPanel.PLAYBACK,
                    origin = PanelRouteOrigin.STARTUP_RESTORE,
                    priority = PanelRoutePriority.STARTUP,
                    waitForSong = true,
                    reason = "topway-cold-launch-awaiting-restore",
                )
            else -> StartupPanelDecision.KeepCurrent("standard-home-default")
        }
}

data class StartupPanelInput(
    val coldLaunch: Boolean,
    val restoredTask: Boolean,
    val topwayCompatFlavor: Boolean,
    val headUnitLandscapeMode: Boolean,
    val libraryState: StartupLibraryRouteState,
    val hasNormalSong: Boolean,
    val rawFastResumeActive: Boolean,
    val explicitDestination: OpenPanel? = null,
    val userCancelled: Boolean = false,
)

enum class StartupLibraryRouteState {
    CHECKING,
    READY_OR_UNKNOWN,
    NEEDS_SOURCE,
    EMPTY,
    RECOVERY,
}

enum class PanelRouteOrigin {
    EXPLICIT_INTENT,
    USER_ACTION,
    LAUNCHER,
    STARTUP_RESTORE,
}

enum class PanelRoutePriority(val value: Int) {
    STARTUP(10),
    LAUNCHER(20),
    EXPLICIT(30),
}

data class PanelRouteRequest(
    val id: Long,
    val destination: OpenPanel,
    val origin: PanelRouteOrigin,
    val priority: PanelRoutePriority,
    val waitForSong: Boolean,
    val reason: String,
)

sealed interface StartupPanelDecision {
    data class KeepCurrent(val reason: String) : StartupPanelDecision
    data class RequestRoute(
        val destination: OpenPanel,
        val origin: PanelRouteOrigin,
        val priority: PanelRoutePriority,
        val waitForSong: Boolean,
        val reason: String,
    ) : StartupPanelDecision
}
