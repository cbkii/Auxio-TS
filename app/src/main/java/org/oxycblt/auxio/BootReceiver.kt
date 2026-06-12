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
import org.oxycblt.auxio.playback.PlaybackSettings
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
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

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) {
            L.w("Ignoring non-boot intent: ${intent.action}")
            return
        }

        if (!playbackSettings.autostartOnBoot) {
            L.d("Autostart disabled, ignoring boot")
            return
        }

        L.d("Autostart enabled, attempting to launch Auxio-TS on boot")

        // Prefer launching the Activity UI for head-unit use.
        // On Android 10+ background-start restrictions may block this in some contexts,
        // so fall back to starting the service.
        try {
            val activityIntent =
                Intent(context, MainActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(activityIntent)
            L.d("Started MainActivity from boot")
        } catch (e: Exception) {
            L.w("Cannot start Activity from boot, falling back to service start: $e")
            val serviceIntent =
                Intent(context, AuxioService::class.java)
                    .setAction(AuxioService.ACTION_START)
                    .putExtra(AuxioService.INTENT_KEY_START_ID, IntegerTable.START_ID_BOOT)
            context.startForegroundService(serviceIntent)
            L.d("Started AuxioService from boot")
        }
    }
}
