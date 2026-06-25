/*
 * Copyright (c) 2024 Auxio Project
 * CarOverlayBootReceiver.kt is part of Auxio.
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

package org.oxycblt.auxio.car.overlay

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import timber.log.Timber as L

/**
 * Restores the car floating controls overlay after public Android lifecycle broadcasts that are
 * commonly observed around TS18 boot, user unlock, package replacement, screen wake, and user
 * return. This remains Topway-compatible flavour only and uses the normal foreground-service path.
 *
 * Requires TS18 device validation: ACC sleep/wake may be exposed as screen/user-present only on
 * some firmware builds; vendor-private wake actions are intentionally not declared here.
 */
class CarOverlayBootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        if (action !in RESTORE_ACTIONS) return

        L.d("Car overlay restore broadcast: $action")
        CarFloatingControlsService.restoreIfEnabled(context, "receiver:$action")
    }

    companion object {
        val RESTORE_ACTIONS =
            setOf(
                Intent.ACTION_BOOT_COMPLETED,
                Intent.ACTION_USER_UNLOCKED,
                Intent.ACTION_MY_PACKAGE_REPLACED,
                Intent.ACTION_SCREEN_ON,
                Intent.ACTION_USER_PRESENT,
                ACTION_QUICKBOOT_POWERON,
                ACTION_HTC_QUICKBOOT_POWERON,
            )

        private const val ACTION_QUICKBOOT_POWERON = "android.intent.action.QUICKBOOT_POWERON"
        private const val ACTION_HTC_QUICKBOOT_POWERON = "com.htc.intent.action.QUICKBOOT_POWERON"
    }
}
