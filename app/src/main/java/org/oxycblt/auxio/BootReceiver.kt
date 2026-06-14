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
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import org.oxycblt.auxio.diagnostics.DiagnosticJournal
import org.oxycblt.auxio.diagnostics.DiagnosticService
import org.oxycblt.auxio.diagnostics.DiagnosticsSettings
import org.oxycblt.auxio.playback.PlaybackSettings
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
    @Inject lateinit var diagnosticsSettings: DiagnosticsSettings

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) {
            L.w("Ignoring non-boot intent: ${intent.action}")
            return
        }

        if (!playbackSettings.autostartOnBoot) {
            L.d("Autostart disabled, ignoring boot")
            return
        }

        val armedCaptureId = diagnosticsSettings.armedBootCaptureId
        val armedExpiry = diagnosticsSettings.armedExpiryTime
        if (armedCaptureId != null) {
            diagnosticsSettings.armedBootCaptureId = null
            diagnosticsSettings.armedExpiryTime = 0L
            if (System.currentTimeMillis() <= armedExpiry) {
                DiagnosticService.start(context, armedCaptureId)
                journal.log(
                    DiagnosticJournal.CAT_BOOT,
                    "Boot capture started",
                    "Session: $armedCaptureId",
                )
            } else {
                L.d("Expired armed boot capture ignored")
            }
        }

        L.d("Autostart enabled, attempting to launch Auxio-TS on boot")
        journal.log(DiagnosticJournal.CAT_BOOT, "Boot Received", "Autostart enabled")

        // When autoplay is enabled, start the playback service first so that music can begin
        // even if the background activity start is blocked. The service start is only performed
        // for autoplay because a foreground service that does not promptly begin playback (and
        // therefore never posts a media notification) would be killed by the system. On Android
        // 14+ a mediaPlayback foreground service started from BOOT_COMPLETED is rejected, so the
        // start is wrapped to degrade gracefully instead of crashing the receiver.
        if (playbackSettings.autoplayOnLaunch) {
            try {
                val serviceIntent =
                    Intent(context, AuxioService::class.java)
                        .setAction(AuxioService.ACTION_START)
                        .putExtra(AuxioService.INTENT_KEY_START_ID, IntegerTable.START_ID_BOOT)
                ContextCompat.startForegroundService(context, serviceIntent)
                L.d("Started AuxioService from boot")
            } catch (e: Exception) {
                L.w("Cannot start AuxioService from boot: $e")
            }
        }

        // Attempt to show the activity UI for head-unit use. Background activity starts may be
        // silently blocked on Android 10+ without throwing, so this is best-effort only.
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
