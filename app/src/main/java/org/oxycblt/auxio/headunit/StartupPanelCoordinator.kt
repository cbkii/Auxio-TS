/*
 * Copyright (c) 2026 Auxio Project
 * StartupPanelCoordinator.kt is part of Auxio.
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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import org.oxycblt.auxio.music.StartupLibraryStatus
import org.oxycblt.auxio.music.StartupReadinessState
import org.oxycblt.auxio.playback.OpenPanel
import org.oxycblt.auxio.playback.PlaybackSettings
import org.oxycblt.auxio.playback.state.RestoreOutcome
import timber.log.Timber as L

/**
 * Owns startup-only panel routing independently from autoplay and playback ownership.
 *
 * A route remains pending through transient library, restore, and sheet states. Terminal policy
 * outcomes cancel it, while the UI consumes it only after reaching the requested final panel state.
 */
@HiltViewModel
class StartupPanelCoordinator @Inject constructor(private val playbackSettings: PlaybackSettings) :
    ViewModel() {
    data class RouteRequest(
        val token: UUID,
        val destination: OpenPanel,
        val priority: Priority,
        val description: String,
    )

    enum class Priority {
        GENERIC_STARTUP,
        EXPLICIT_INTENT,
    }

    sealed interface RouteEvaluation {
        data object Idle : RouteEvaluation

        data class Wait(val request: RouteRequest, val reason: String) : RouteEvaluation

        data class Cancel(val request: RouteRequest, val reason: String) : RouteEvaluation

        data class Render(val request: RouteRequest) : RouteEvaluation
    }

    private data class CoordinatorState(
        val hasPlayableItem: Boolean,
        val outcome: RestoreOutcome,
        val readiness: StartupReadinessState,
        val libraryStatus: StartupLibraryStatus,
    )

    private val activeRequest = MutableStateFlow<RouteRequest?>(null)
    private val coordinatorState =
        MutableStateFlow(
            CoordinatorState(
                hasPlayableItem = false,
                outcome = RestoreOutcome.NOT_REQUESTED,
                readiness = StartupReadinessState.ProcessVisible,
                libraryStatus = StartupLibraryStatus.Unknown,
            )
        )

    val routeEvaluation: StateFlow<RouteEvaluation> =
        combine(activeRequest, coordinatorState) { request, state ->
                evaluate(
                    request,
                    state.hasPlayableItem,
                    state.outcome,
                    state.readiness,
                    state.libraryStatus,
                )
            }
            .stateIn(viewModelScope, SharingStarted.Eagerly, RouteEvaluation.Idle)

    fun updateState(
        hasPlayableItem: Boolean,
        outcome: RestoreOutcome,
        readiness: StartupReadinessState,
        libraryStatus: StartupLibraryStatus,
    ) {
        coordinatorState.value =
            CoordinatorState(hasPlayableItem, outcome, readiness, libraryStatus)
    }

    /** Explicit launcher/deep-link navigation supersedes a generic startup request. */
    fun requestExplicitRoute(destination: OpenPanel, description: String) {
        val request =
            RouteRequest(UUID.randomUUID(), destination, Priority.EXPLICIT_INTENT, description)
        L.d("Requesting explicit route: $request")
        activeRequest.value = request
    }

    /** Request the user-configured generic cold-launch destination. */
    fun requestGenericStartupRoute() {
        if (!playbackSettings.launchToPanel) {
            L.d("Generic startup route skipped: launchToPanel disabled")
            return
        }
        activeRequest.update { current ->
            if (current?.priority == Priority.EXPLICIT_INTENT) {
                L.d("Generic startup route suppressed by explicit route: $current")
                current
            } else {
                RouteRequest(
                        UUID.randomUUID(),
                        genericDestination(),
                        Priority.GENERIC_STARTUP,
                        "Generic App Launch",
                    )
                    .also { L.d("Requesting generic startup route: $it") }
            }
        }
    }

    /** Consume only the still-matching route after its final panel state is rendered. */
    fun consumeRoute(token: UUID) {
        activeRequest.update { current ->
            if (current?.token == token) {
                L.d("Route consumed: ${current.description}")
                null
            } else {
                current
            }
        }
    }

    /** Cancel only the still-matching request after a terminal policy result. */
    fun cancelRoute(token: UUID, reason: String) {
        activeRequest.update { current ->
            if (current?.token == token) {
                L.d("Route cancelled [reason=$reason]: ${current.description}")
                null
            } else {
                current
            }
        }
    }

    /** Cancel startup routing after deliberate user navigation. */
    fun cancelRouting(reason: String = "manual-navigation") {
        activeRequest.update { current ->
            if (current != null) {
                L.d("Active route cancelled [reason=$reason]: ${current.description}")
            }
            null
        }
    }

    companion object {
        internal fun genericDestination(): OpenPanel = OpenPanel.PLAYBACK

        internal fun evaluate(
            request: RouteRequest?,
            hasPlayableItem: Boolean,
            outcome: RestoreOutcome,
            readiness: StartupReadinessState,
            libraryStatus: StartupLibraryStatus,
        ): RouteEvaluation {
            request ?: return RouteEvaluation.Idle

            if (request.priority == Priority.EXPLICIT_INTENT) {
                if (hasPlayableItem) return RouteEvaluation.Render(request)
                return when (outcome) {
                    RestoreOutcome.NO_SAVED_SESSION,
                    RestoreOutcome.FAILED,
                    RestoreOutcome.CANCELLED ->
                        RouteEvaluation.Cancel(request, "explicit-terminal-$outcome")
                    else -> RouteEvaluation.Wait(request, "explicit-awaiting-song")
                }
            }

            val terminalLibraryStatus =
                libraryStatus == StartupLibraryStatus.NeedsMusicSource ||
                    libraryStatus == StartupLibraryStatus.Empty ||
                    libraryStatus == StartupLibraryStatus.CacheUnavailable ||
                    libraryStatus == StartupLibraryStatus.SourceUnavailable
            val terminalRestore =
                outcome == RestoreOutcome.NOT_REQUESTED ||
                    outcome == RestoreOutcome.NO_SAVED_SESSION ||
                    outcome == RestoreOutcome.FAILED ||
                    outcome == RestoreOutcome.CANCELLED
            if (terminalLibraryStatus && terminalRestore && !hasPlayableItem) {
                return RouteEvaluation.Cancel(request, "library-terminal-$libraryStatus")
            }

            if (
                readiness == StartupReadinessState.ProcessVisible &&
                    libraryStatus == StartupLibraryStatus.Unknown
            ) {
                return RouteEvaluation.Wait(request, "library-checking")
            }

            return when (outcome) {
                RestoreOutcome.NOT_REQUESTED,
                RestoreOutcome.WAITING_FOR_PLAYER,
                RestoreOutcome.WAITING_FOR_LIBRARY ->
                    RouteEvaluation.Wait(request, "restore-transient-$outcome")
                RestoreOutcome.NO_SAVED_SESSION,
                RestoreOutcome.FAILED,
                RestoreOutcome.CANCELLED ->
                    RouteEvaluation.Cancel(request, "restore-terminal-$outcome")
                RestoreOutcome.RAW_FAST_RESUME_ACTIVE,
                RestoreOutcome.RESTORED_EXISTING_SESSION,
                RestoreOutcome.FALLBACK_QUEUE_CREATED ->
                    if (hasPlayableItem) {
                        RouteEvaluation.Render(request)
                    } else {
                        RouteEvaluation.Wait(request, "awaiting-playable-item")
                    }
            }
        }
    }
}
