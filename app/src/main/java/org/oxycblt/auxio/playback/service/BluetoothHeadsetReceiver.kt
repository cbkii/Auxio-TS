/*
 * Copyright (c) 2022 Auxio Project
 * BluetoothHeadsetReceiver.kt is part of Auxio.
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

package org.oxycblt.auxio.playback.service

import android.bluetooth.BluetoothA2dp
import android.bluetooth.BluetoothProfile
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BadParcelableException
import androidx.core.content.ContextCompat
import org.oxycblt.auxio.AuxioService
import org.oxycblt.auxio.IntegerTable
import org.oxycblt.auxio.R
import org.oxycblt.auxio.headunit.topway.ExportedCommandRateLimiter
import org.oxycblt.auxio.headunit.topway.TopwayServiceBridge
import timber.log.Timber as L

/**
 * A [BroadcastReceiver] that starts music playback when a bluetooth headset is connected.
 *
 * @author seijikun, OxygenCobalt
 */
class BluetoothHeadsetReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED) return
        val newState =
            try {
                intent.getIntExtra(
                    BluetoothProfile.EXTRA_STATE,
                    BluetoothProfile.STATE_DISCONNECTED,
                )
            } catch (e: BadParcelableException) {
                L.w(e, "Ignoring malformed Bluetooth connection-state payload")
                return
            } catch (e: RuntimeException) {
                L.w(e, "Ignoring unreadable Bluetooth connection-state payload")
                return
            }
        if (newState != BluetoothProfile.STATE_CONNECTED) return
        if (
            !ExportedCommandRateLimiter.allow(
                key = "bluetooth-a2dp-connected",
                maxEvents = MAX_CONNECTION_EVENTS_PER_WINDOW,
                windowMs = CONNECTION_RATE_WINDOW_MS,
            )
        ) {
            L.w("Dropping excessive Bluetooth connection-state broadcasts")
            return
        }

        val sharedPreferences =
            androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
        val autoplayKey = context.getString(R.string.set_key_headset_autoplay)
        if (!sharedPreferences.getBoolean(autoplayKey, false)) return

        L.d("Bluetooth headset connected, initializing service")
        val serviceClass = TopwayServiceBridge.resolveCompatServiceClass(AuxioService::class.java)
        val serviceIntent =
            Intent(context, serviceClass)
                .setAction(AuxioService.ACTION_START)
                .putExtra(AuxioService.INTENT_KEY_START_ID, IntegerTable.START_ID_BLUETOOTH)
        try {
            ContextCompat.startForegroundService(context, serviceIntent)
        } catch (e: IllegalStateException) {
            L.w(e, "Unable to start Auxio after Bluetooth connection")
        } catch (e: SecurityException) {
            L.w(e, "Bluetooth-triggered service start rejected")
        } catch (e: RuntimeException) {
            L.w(e, "Bluetooth-triggered service start failed")
        }
    }

    private companion object {
        const val MAX_CONNECTION_EVENTS_PER_WINDOW = 4
        const val CONNECTION_RATE_WINDOW_MS = 5_000L
    }
}
