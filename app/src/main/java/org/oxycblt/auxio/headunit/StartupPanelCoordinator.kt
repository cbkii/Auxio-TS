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
import org.oxycblt.auxio.BuildConfig
import org.oxycblt.auxio.music.StartupReadinessState
import org.oxycblt.auxio.playback.OpenPanel
import org.oxycblt.auxio.playback.PlaybackSettings
import org.oxycblt.auxio.playback.state.RestoreOutcome
import timber.log.Timber as L

/**
 * Coordinates and determines panel routing at startup, reconciling explicit intent destinations,
 * generic TS18 launch settings, and the underlying playback restoration outcome.
 *
 * @author Auxio-TS contributors
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
        EXPLICIT_INTENT,
        GENERIC_STARTUP,
    }

    private val activeRequest = MutableStateFlow<RouteRequest?>(null)

    // Tracked state inputs updated by fragments
    private val currentSong = MutableStateFlow<org.oxycblt.musikr.Song?>(null)
    private val restoreOutcome = MutableStateFlow(RestoreOutcome.NOT_REQUESTED)
    private val startupReadinessState =
        MutableStateFlow<StartupReadinessState>(StartupReadinessState.CheckingCachedLibrary)

    val routeDecision: StateFlow<RouteRequest?> =
        combine(activeRequest, startupReadinessState, restoreOutcome, currentSong) {
                request: RouteRequest?,
                readiness: StartupReadinessState,
                outcome: RestoreOutcome,
                song: org.oxycblt.musikr.Song? ->
                if (request == null) return@combine null

                if (
                    readiness == StartupReadinessState.CheckingCachedLibrary ||
                        readiness == StartupReadinessState.NeedsMusicSource
                ) {
                    return@combine null
                }

                // Exclude empty libraries from generic panel routing
                if (
                    request.priority == Priority.GENERIC_STARTUP &&
                        readiness == StartupReadinessState.EmptyLibrary
                ) {
                    L.d("Cancelling generic startup route: Library is empty")
                    return@combine null
                }

                // Await an outcome
                if (
                    outcome == RestoreOutcome.NOT_REQUESTED ||
                        outcome == RestoreOutcome.WAITING_FOR_PLAYER ||
                        outcome == RestoreOutcome.WAITING_FOR_LIBRARY
                ) {
                    return@combine null
                }

                // Do not route generic launches if there is no session to restore
                if (
                    request.priority == Priority.GENERIC_STARTUP &&
                        outcome == RestoreOutcome.NO_SAVED_SESSION
                ) {
                    L.d("Cancelling generic startup route: No saved session")
                    return@combine null
                }

                // If we're relying on a normal song, ensure we have one, unless it's raw
                // fast-resume
                if (outcome != RestoreOutcome.RAW_FAST_RESUME_ACTIVE && song == null) {
                    return@combine null
                }

                L.i(
                    "StartupPanelCoordinator fulfilled request: ${request.description} -> ${request.destination}"
                )
                request
            }
            .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun updateState(
        song: org.oxycblt.musikr.Song?,
        outcome: RestoreOutcome,
        readiness: StartupReadinessState,
    ) {
        currentSong.value = song
        restoreOutcome.value = outcome
        startupReadinessState.value = readiness
    }

    /** Provide an explicit launch route (e.g., from an Intent). Overrides generic routes. */
    fun requestExplicitRoute(destination: OpenPanel, description: String) {
        val newRequest =
            RouteRequest(UUID.randomUUID(), destination, Priority.EXPLICIT_INTENT, description)
        L.d("Requesting explicit route: $newRequest")
        activeRequest.value = newRequest
    }

    /** Trigger a generic startup route if settings and conditions allow. */
    fun requestGenericStartupRoute() {
        if (!playbackSettings.launchToPanel) {
            L.d("Generic startup route skipped: launchToPanel disabled")
            return
        }
        activeRequest.update { current ->
            if (current != null && current.priority >= Priority.EXPLICIT_INTENT) {
                L.d("Generic startup route suppressed by existing higher-priority route: $current")
                current
            } else {
                val destination =
                    if (BuildConfig.TOPWAY_COMPAT_FLAVOR) OpenPanel.PLAYBACK_QUEUE
                    else OpenPanel.PLAYBACK
                val newRequest =
                    RouteRequest(
                        UUID.randomUUID(),
                        destination,
                        Priority.GENERIC_STARTUP,
                        "Generic App Launch",
                    )
                L.d("Requesting generic startup route: $newRequest")
                newRequest
            }
        }
    }

    /**
     * Consume the specified route. This should be called by the UI when the layout has successfully
     * reached the target destination.
     */
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

    /** Cancel any pending routing, such as when the user explicitly navigates somewhere else. */
    fun cancelRouting() {
        if (activeRequest.value != null) {
            L.d("Active routing request cancelled")
            activeRequest.value = null
        }
    }
}
