/*
 * Copyright (c) 2024 Auxio Project
 * TopwayMusicBridgeReceiver.kt is part of Auxio.
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

package org.oxycblt.auxio.headunit.topway

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import org.oxycblt.auxio.AuxioService
import org.oxycblt.auxio.IntegerTable
import org.oxycblt.auxio.diagnostics.DiagnosticJournal
import org.oxycblt.auxio.playback.service.ForegroundServiceStartContract
import timber.log.Timber as L

/**
 * Narrow exported bridge receiver for Topway-compatible actions when Auxio is cold.
 *
 * It is exported so TS18/iLauncher can deliver the source-backed Topway actions, but it accepts
 * only the allowlisted actions from [TopwayMusicContract] and immediately delegates to Auxio's
 * service-specific Topway path. It must never route through generic media-button restore logic.
 */
@AndroidEntryPoint
class TopwayMusicBridgeReceiver : BroadcastReceiver() {
    @Inject lateinit var journal: DiagnosticJournal
    @Inject lateinit var coordinator: TopwayLauncherIntegrationCoordinator

    override fun onReceive(context: Context, intent: Intent?) {
        val action = intent?.action ?: return
        if (!TopwayMusicContract.isIncomingAction(action)) {
            L.w("Ignoring unsupported Topway bridge action: $action")
            return
        }
        if (intent.clipData != null) {
            L.w("Ignoring Topway bridge action carrying ClipData: $action")
            return
        }
        if (
            !ExportedCommandRateLimiter.allow(
                key = "topway",
                maxEvents = MAX_TOPWAY_EVENTS_PER_WINDOW,
                windowMs = TOPWAY_RATE_WINDOW_MS,
            )
        ) {
            L.w("Dropping excessive Topway bridge action: $action")
            return
        }

        journal.log(DiagnosticJournal.CAT_TOPWAY_CMD, "Incoming Intent", action)
        if (!coordinator.mode.handlesTopwayCommands) {
            journal.log(
                DiagnosticJournal.CAT_TOPWAY_CMD,
                "Ignored due to mode",
                action,
                coordinator.mode.name,
            )
            L.d("Ignoring Topway bridge action in ${coordinator.mode.name}: $action")
            return
        }

        val serviceClass =
            if (org.oxycblt.auxio.BuildConfig.TOPWAY_COMPAT_FLAVOR) {
                try {
                    Class.forName("com.tw.music.MusicService")
                } catch (e: ClassNotFoundException) {
                    L.d(e, "Topway wrapper service not found, falling back to AuxioService")
                    AuxioService::class.java
                }
            } else {
                AuxioService::class.java
            }
        val serviceIntent = Intent(context, serviceClass).setAction(action)
        val extras =
            TopwayBridgeExtrasPolicy.sanitizeIncomingExtras(
                TopwayBridgeExtrasPolicy.safelyExtractIncomingExtras(
                    intent,
                    javaClass.classLoader,
                    source = "TopwayMusicBridgeReceiver",
                )
            )
        extras.cmd?.let { serviceIntent.putExtra(TopwayMusicContract.EXTRA_CMD, it) }
        extras.widgetProgress?.let {
            serviceIntent.putExtra(TopwayMusicContract.EXTRA_WIDGET_PROGRESS, it)
        }
        serviceIntent.putExtra(AuxioService.INTENT_KEY_START_ID, IntegerTable.START_ID_TOPWAY)
        ForegroundServiceStartContract.markRequired(serviceIntent)
        try {
            ContextCompat.startForegroundService(context, serviceIntent)
        } catch (e: IllegalStateException) {
            L.w(e, "Unable to start Auxio for Topway action due to service state")
        } catch (e: SecurityException) {
            L.w(e, "Unable to start Auxio for Topway action due to security policy")
        } catch (e: RuntimeException) {
            L.w(e, "Unable to start Auxio for malformed Topway action")
        }
    }

    private companion object {
        const val MAX_TOPWAY_EVENTS_PER_WINDOW = 24
        const val TOPWAY_RATE_WINDOW_MS = 1_000L
    }
}
