/*
 * Copyright (c) 2026 Auxio Project
 * TopwayOverlayRestoreBridge.kt is part of Auxio.
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

package org.oxycblt.auxio.headunit.overlay

import android.app.Service
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.core.content.ContextCompat
import org.oxycblt.auxio.BuildConfig
import org.oxycblt.auxio.headunit.overlay.CarOverlayContract.ACTION_START
import org.oxycblt.auxio.headunit.overlay.CarOverlayContract.EXTRA_START_REASON
import timber.log.Timber as L

object TopwayOverlayRestoreBridge {
    private const val SERVICE_CLASS = "org.oxycblt.auxio.car.overlay.CarFloatingControlsService"

    fun requestOverlayRestore(context: Context): CarOverlayContract.OverlayRestoreResult {
        if (!BuildConfig.TOPWAY_COMPAT_FLAVOR)
            return CarOverlayContract.OverlayRestoreResult.UnsupportedBuild

        val prefs =
            androidx.preference.PreferenceManager.getDefaultSharedPreferences(
                context.applicationContext
            )
        if (!prefs.getBoolean(CarOverlayContract.KEY_ENABLED, false)) {
            return CarOverlayContract.OverlayRestoreResult.Disabled
        }

        if (!Settings.canDrawOverlays(context)) {
            return CarOverlayContract.OverlayRestoreResult.PermissionMissing
        }

        val reason = "boot_autostart"
        return tryStartOverlayService(context, reason)
    }

    private fun tryStartOverlayService(
        context: Context,
        reason: String,
    ): CarOverlayContract.OverlayRestoreResult {
        return try {
            val serviceClass = Class.forName(SERVICE_CLASS).asSubclass(Service::class.java)
            val serviceIntent =
                Intent(context, serviceClass)
                    .setAction(ACTION_START)
                    .putExtra(EXTRA_START_REASON, reason)
            ContextCompat.startForegroundService(context, serviceIntent)
            L.i("Requested direct Topway overlay foreground-service restore [$reason]")
            CarOverlayContract.OverlayRestoreResult.StartRequested
        } catch (e: ClassNotFoundException) {
            L.w(e, "Topway overlay service class is not present in this source set")
            CarOverlayContract.OverlayRestoreResult.StartRejected("ClassNotFoundException")
        } catch (e: ClassCastException) {
            L.w(e, "Topway overlay service class did not extend Service")
            CarOverlayContract.OverlayRestoreResult.StartRejected("ClassCastException")
        } catch (e: SecurityException) {
            L.w(e, "Direct Topway overlay foreground-service restore was denied")
            CarOverlayContract.OverlayRestoreResult.StartRejected("SecurityException")
        } catch (e: IllegalStateException) {
            L.w(e, "Direct Topway overlay foreground-service restore was rejected")
            CarOverlayContract.OverlayRestoreResult.StartRejected("IllegalStateException")
        } catch (e: RuntimeException) {
            L.w(e, "Direct Topway overlay foreground-service restore failed unexpectedly")
            CarOverlayContract.OverlayRestoreResult.StartRejected("RuntimeException")
        }
    }
}
