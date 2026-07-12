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
import org.oxycblt.auxio.playback.state.RestoreOutcome
import org.oxycblt.auxio.playback.state.RestoreProgress
import org.oxycblt.musikr.Library

/**
 * Pure policy functions for determining startup playback and panel behavior.
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
     * Determine whether the Now Playing panel should be opened on cold launch.
     *
     * A missing or intentionally empty library must remain on setup/home rather than showing an
     * empty playback surface.
     */
    fun shouldOpenPanelOnLaunch(library: Library?): Boolean {
        return library != null && !library.empty()
    }

    /**
     * Select the durable panel request for a new Activity launch.
     *
     * Branch ordering is explicit destination, non-cold/restored/cancelled task, independent
     * launch-to-panel preference, already-renderable normal song, terminal library gates, raw
     * fast-resume, then a restore-bound request. [PanelRouteRequest.waitForSong] means the current
     * panel cannot safely render until a normal Musikr song exists;
     * [StartupPanelDecision.RequestRoute.restoreBound] means the route must also match a terminal
     * restore outcome so a later manual song cannot be mistaken for startup restoration.
     *
     * @param input Typed launch, library, playback, flavor, and preference state.
     * @return A route request or a reason-coded decision to preserve the current destination.
     */
    fun startupRoute(input: StartupPanelInput): StartupPanelDecision =
        when {
            input.explicitDestination != null ->
                StartupPanelDecision.RequestRoute(
                    destination = input.explicitDestination,
                    origin = PanelRouteOrigin.EXPLICIT_INTENT,
                    priority = PanelRoutePriority.EXPLICIT,
                    waitForSong = input.explicitDestination != OpenPanel.MAIN,
                    restoreBound = false,
                    reason = "explicit-destination",
                )
            !input.coldLaunch || input.restoredTask || input.userCancelled ->
                StartupPanelDecision.KeepCurrent("not-new-cold-launch")
            !input.launchToPanel -> StartupPanelDecision.KeepCurrent("launch-to-panel-disabled")
            input.hasNormalSong ->
                StartupPanelDecision.RequestRoute(
                    destination = OpenPanel.PLAYBACK,
                    origin = PanelRouteOrigin.STARTUP_RESTORE,
                    priority = PanelRoutePriority.STARTUP,
                    waitForSong = false,
                    restoreBound = false,
                    reason = "existing-normal-session",
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
                    restoreBound = true,
                    reason = "raw-fast-resume-awaiting-reconciliation",
                )
            else ->
                StartupPanelDecision.RequestRoute(
                    destination = OpenPanel.PLAYBACK,
                    origin = PanelRouteOrigin.STARTUP_RESTORE,
                    priority = PanelRoutePriority.STARTUP,
                    waitForSong = true,
                    restoreBound = true,
                    reason =
                        if (input.topwayCompatFlavor && input.headUnitLandscapeMode) {
                            "topway-cold-launch-awaiting-restore"
                        } else {
                            "launch-to-panel-awaiting-restore"
                        },
                )
        }

    /**
     * Decide whether a durable request should render, remain pending, or be cancelled.
     *
     * Restore-bound requests are keyed to [RestoreProgress.requestId]. Terminal no-session,
     * failure, cancellation, stale-token, and unusable-library outcomes cancel the route. This is
     * what prevents the first later manual playback from being treated as a startup restore.
     * Explicit routes are not restore-bound but still wait for a normal song when their destination
     * requires one.
     */
    fun pendingRouteDecision(input: PendingPanelRouteInput): PendingPanelRouteDecision {
        val route = input.route
        if (route.origin != PanelRouteOrigin.STARTUP_RESTORE) {
            if (!route.waitForSong || input.hasNormalSong) {
                return PendingPanelRouteDecision.Apply("explicit-route-renderable")
            }
            if (input.rawFastResumeActive) {
                return PendingPanelRouteDecision.Wait("explicit-route-waiting-for-song")
            }
            return if (input.libraryState.isTerminalWithoutLibrary()) {
                PendingPanelRouteDecision.Cancel("explicit-route-no-renderable-song")
            } else {
                PendingPanelRouteDecision.Wait("explicit-route-waiting-for-song")
            }
        }

        val restoreRequestId = route.restoreRequestId
        if (restoreRequestId == null) {
            return if (input.hasNormalSong) {
                PendingPanelRouteDecision.Apply("existing-session-renderable")
            } else {
                PendingPanelRouteDecision.Cancel("startup-route-missing-restore-token")
            }
        }

        val progress =
            input.restoreProgress
                ?: return PendingPanelRouteDecision.Wait("restore-outcome-not-reported")
        if (progress.requestId != restoreRequestId) {
            return PendingPanelRouteDecision.Cancel("restore-request-superseded")
        }

        if (
            !input.hasNormalSong &&
                !input.rawFastResumeActive &&
                input.libraryState.isTerminalWithoutLibrary()
        ) {
            return PendingPanelRouteDecision.Cancel(
                "library-terminal-${input.libraryState.name.lowercase()}"
            )
        }

        return when (progress.outcome) {
            RestoreOutcome.NOT_REQUESTED,
            RestoreOutcome.WAITING_FOR_PLAYER,
            RestoreOutcome.WAITING_FOR_LIBRARY ->
                PendingPanelRouteDecision.Wait("restore-${progress.outcome.name.lowercase()}")
            RestoreOutcome.RAW_FAST_RESUME_ACTIVE ->
                if (input.hasNormalSong) {
                    PendingPanelRouteDecision.Apply("raw-resume-reconciled-song")
                } else {
                    PendingPanelRouteDecision.Wait("raw-fast-resume-awaiting-reconciliation")
                }
            RestoreOutcome.RESTORED_EXISTING_SESSION,
            RestoreOutcome.FALLBACK_QUEUE_CREATED ->
                if (input.hasNormalSong) {
                    PendingPanelRouteDecision.Apply("restore-${progress.outcome.name.lowercase()}")
                } else {
                    PendingPanelRouteDecision.Cancel("restore-terminal-without-normal-song")
                }
            RestoreOutcome.NO_SAVED_SESSION -> PendingPanelRouteDecision.Cancel("no-saved-session")
            RestoreOutcome.FAILED -> PendingPanelRouteDecision.Cancel("restore-failed")
            RestoreOutcome.CANCELLED -> PendingPanelRouteDecision.Cancel("restore-cancelled")
        }
    }

    private fun StartupLibraryRouteState.isTerminalWithoutLibrary() =
        this == StartupLibraryRouteState.NEEDS_SOURCE ||
            this == StartupLibraryRouteState.EMPTY ||
            this == StartupLibraryRouteState.RECOVERY
}

/** Typed input for selecting the initial panel route. */
data class StartupPanelInput(
    val coldLaunch: Boolean,
    val restoredTask: Boolean,
    val launchToPanel: Boolean,
    val topwayCompatFlavor: Boolean,
    val headUnitLandscapeMode: Boolean,
    val libraryState: StartupLibraryRouteState,
    val hasNormalSong: Boolean,
    val rawFastResumeActive: Boolean,
    val explicitDestination: OpenPanel? = null,
    val userCancelled: Boolean = false,
)

/** Library readiness categories relevant to startup panel routing. */
enum class StartupLibraryRouteState {
    CHECKING,
    READY_OR_UNKNOWN,
    NEEDS_SOURCE,
    EMPTY,
    RECOVERY,
}

/** Source of a durable panel request. */
enum class PanelRouteOrigin {
    EXPLICIT_INTENT,
    USER_ACTION,
    LAUNCHER,
    STARTUP_RESTORE,
}

/** Higher-valued requests supersede lower-valued requests. */
enum class PanelRoutePriority(val value: Int) {
    STARTUP(10),
    LAUNCHER(20),
    EXPLICIT(30),
}

/** Durable request retained until the fragment renders or explicitly cancels it. */
data class PanelRouteRequest(
    val id: Long,
    val destination: OpenPanel,
    val origin: PanelRouteOrigin,
    val priority: PanelRoutePriority,
    val waitForSong: Boolean,
    val reason: String,
    val restoreRequestId: Long? = null,
)

/** Result of selecting a route for a new launch. */
sealed interface StartupPanelDecision {
    /** Keep setup/home/current UI for the supplied reason. */
    data class KeepCurrent(val reason: String) : StartupPanelDecision

    /** Record a durable route with the supplied priority and render preconditions. */
    data class RequestRoute(
        val destination: OpenPanel,
        val origin: PanelRouteOrigin,
        val priority: PanelRoutePriority,
        val waitForSong: Boolean,
        val restoreBound: Boolean,
        val reason: String,
    ) : StartupPanelDecision
}

/** Current inputs used to evaluate an already-recorded durable route. */
data class PendingPanelRouteInput(
    val route: PanelRouteRequest,
    val libraryState: StartupLibraryRouteState,
    val hasNormalSong: Boolean,
    val rawFastResumeActive: Boolean,
    val restoreProgress: RestoreProgress?,
)

/** Rendering decision for a pending route. */
sealed interface PendingPanelRouteDecision {
    val reason: String

    /** Apply the route to the current layout. */
    data class Apply(override val reason: String) : PendingPanelRouteDecision

    /** Retain the route until another bounded state transition occurs. */
    data class Wait(override val reason: String) : PendingPanelRouteDecision

    /** Remove the route because it is terminal, stale, or superseded. */
    data class Cancel(override val reason: String) : PendingPanelRouteDecision
}
