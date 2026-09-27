/*
 * Copyright (c) 2024 Auxio Project
 * BootReceiver.kt is part of Auxio.
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

package org.oxycblt.auxio

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import org.oxycblt.auxio.diagnostics.DiagnosticJournal
import org.oxycblt.auxio.headunit.BootStartupMode
import org.oxycblt.auxio.headunit.overlay.FloatingOnlyStartupCoordinator
import org.oxycblt.auxio.headunit.topway.TopwayServiceBridge
import org.oxycblt.auxio.playback.PlaybackSettings
import org.oxycblt.auxio.playback.service.ForegroundServiceStartContract
import timber.log.Timber as L

/**
 * A [BroadcastReceiver] that launches Auxio-TS on device boot when the Autostart setting is
 * enabled. Intended for TS18/head-unit use where the device boots directly into the car
 * environment.
 *
 * @author Auxio-TS contributors
 */
@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {
    @Inject lateinit var playbackSettings: PlaybackSettings
    @Inject lateinit var journal: DiagnosticJournal

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) {
            L.w("Ignoring non-boot intent: ${intent.action}")
            return
        }

        val launchRoute = BootLaunchPolicy.route(BootStartupMode.resolve(context))
        if (launchRoute == BootLaunchPolicy.Route.DISABLED) {
            L.d("Autostart disabled, ignoring boot")
            return
        }

        L.d("Autostart enabled, attempting to launch Auxio-TS on boot")
        journal.log(DiagnosticJournal.CAT_BOOT, "Boot Received", "Autostart enabled")
        // Cached playback/session restoration is never blocked by an interactive su process.
        // Optional root storage preparation runs independently in the Magisk late-start service;
        // the app consumes its cached manifest and refreshes it only from an explicit source flow.
        journal.log(
            DiagnosticJournal.CAT_BOOT,
            "Root storage preparation",
            "parallel_magisk_late_start",
        )

        // Floating-only is a real headless Auxio launch, not an overlay-only shortcut. Start the
        // canonical playback/library service regardless of autoplay, then request the overlay and
        // return without ever creating MainActivity. START_ID_BOOT keeps autoplay semantics aligned
        // with a normal boot launch while the service still hydrates the canonical library when
        // autoplay is disabled.
        if (launchRoute == BootLaunchPolicy.Route.FLOATING_CONTROLS_ONLY) {
            journal.log(
                DiagnosticJournal.CAT_BOOT,
                "Floating-only autostart",
                "starting canonical service and overlay",
            )
            val result =
                FloatingOnlyStartupCoordinator.start(
                    context,
                    reason = "boot_receiver",
                    restorePlayback = true,
                )
            journal.log(
                DiagnosticJournal.CAT_BOOT,
                "Floating-only startup result",
                "playback=${result.playback} overlay=${result.overlay}",
            )
            if (
                result.overlay is
                    org.oxycblt.auxio.headunit.overlay.CarOverlayContract.OverlayRestoreResult.StartRequested ||
                    result.overlay is
                        org.oxycblt.auxio.headunit.overlay.CarOverlayContract.OverlayRestoreResult.AlreadyVisible
            ) {
                L.d("Floating-only boot requested canonical Auxio state and Topway overlay")
            } else {
                L.w("Floating-only boot could not display the overlay: ${result.overlay}")
            }
            return
        }

        // Background Ready and Full-player both initialise the one canonical playback/library
        // service. Background Ready always restores paused and never launches MainActivity.
        // Full-player keeps the historical presentation path while preserving playback ownership in
        // the service itself. Android 15+ may restrict BOOT_COMPLETED mediaPlayback foreground
        // starts for API 35+, so both paths remain fail-open.
        if (BootLaunchPolicy.shouldStartCanonicalServiceDirectly(launchRoute)) {
            journal.log(
                DiagnosticJournal.CAT_BOOT,
                "Playback restore path",
                "service=true autoplay=${playbackSettings.autoplayOnLaunch} route=$launchRoute",
            )
            try {
                val serviceClass =
                    TopwayServiceBridge.resolveCompatServiceClass(AuxioService::class.java)
                val serviceIntent =
                    Intent(context, serviceClass)
                        .setAction(AuxioService.ACTION_START)
                        .putExtra(
                            AuxioService.INTENT_KEY_START_ID,
                            if (launchRoute == BootLaunchPolicy.Route.BACKGROUND_READY) {
                                IntegerTable.START_ID_BACKGROUND_READY
                            } else {
                                IntegerTable.START_ID_BOOT
                            },
                        )
                ForegroundServiceStartContract.start(context, serviceIntent)
                L.d(
                    "Started AuxioService from boot [autoplay=${playbackSettings.autoplayOnLaunch}, route=$launchRoute]"
                )
            } catch (e: Exception) {
                L.w("Cannot start AuxioService from boot: $e")
                journal.log(DiagnosticJournal.CAT_BOOT, "Playback restore failed", e.toString())
            }
        }

        if (launchRoute == BootLaunchPolicy.Route.BACKGROUND_READY) {
            return
        }

        // Attempt to show the activity UI for head-unit use. Background activity starts may be
        // silently blocked on Android 10+ without throwing, so this is best-effort presentation.
        try {
            val activityIntent =
                Intent(context, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(activityIntent)
            L.d("Started MainActivity from boot")
        } catch (e: Exception) {
            L.w("Cannot start Activity from boot: $e")
        }
    }
}

/** Keeps the boot-only floating-controls preference out of manual launcher routing. */
internal object BootLaunchPolicy {
    enum class Route {
        DISABLED,
        BACKGROUND_READY,
        FLOATING_CONTROLS_ONLY,
        FULL_PLAYER,
    }

    fun route(mode: BootStartupMode): Route =
        when (mode) {
            BootStartupMode.DISABLED -> Route.DISABLED
            BootStartupMode.BACKGROUND_READY -> Route.BACKGROUND_READY
            BootStartupMode.FULL_PLAYER -> Route.FULL_PLAYER
            BootStartupMode.FLOATING_CONTROLS_ONLY -> Route.FLOATING_CONTROLS_ONLY
        }

    /** Whether this boot route requires Auxio's existing canonical service/library authority. */
    fun shouldStartCanonicalService(route: Route): Boolean = route != Route.DISABLED

    /** Whether BootReceiver itself owns the canonical service start for this route. */
    fun shouldStartCanonicalServiceDirectly(route: Route): Boolean =
        route == Route.FULL_PLAYER || route == Route.BACKGROUND_READY
}
