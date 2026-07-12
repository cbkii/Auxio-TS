/*
 * Copyright (c) 2024 Auxio Project
 * CarOverlayVisibilityHooks.kt is part of Auxio.
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

import android.app.Application
import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import timber.log.Timber as L

/**
 * Application-level process lifecycle callbacks that keep the car floating overlay converged with
 * user settings. New starts proactively restore the overlay when enabled, while the optional "hide
 * while Auxio foreground" setting suppresses the overlay during Auxio UI use.
 */
class CarOverlayVisibilityHooks(private val applicationContext: Context) :
    DefaultLifecycleObserver {

    override fun onStart(owner: LifecycleOwner) {
        val prefs = readPrefs(applicationContext) ?: return
        if (!prefs.enabled) return

        if (prefs.hideWhileAuxioForeground) {
            isSuppressedByAuxioForeground = true
            L.d("Auxio entered foreground, signalling overlay to hide")
            CarFloatingControlsService.setAuxioForeground(applicationContext, true)
        } else {
            isSuppressedByAuxioForeground = false
            L.d("Auxio entered foreground with overlay allowed, restoring overlay")
            CarFloatingControlsService.restoreIfEnabled(applicationContext, "process_started")
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        isSuppressedByAuxioForeground = false
        val prefs = readPrefs(applicationContext) ?: return
        if (!prefs.enabled) return

        L.d("Auxio entered background, restoring overlay")
        if (prefs.hideWhileAuxioForeground) {
            CarFloatingControlsService.setAuxioForeground(applicationContext, false)
        } else {
            CarFloatingControlsService.restoreIfEnabled(applicationContext, "process_stopped")
        }
    }

    private fun readPrefs(context: Context): CarOverlayPrefs? =
        try {
            CarOverlayPrefs.from(context)
        } catch (e: RuntimeException) {
            L.w(e, "Unable to read overlay prefs during lifecycle restore")
            null
        }

    companion object {
        @Volatile
        var isSuppressedByAuxioForeground: Boolean = false
            internal set

        @Volatile private var isRegistered = false

        fun register(application: Application) {
            if (isRegistered) return
            isRegistered = true
            ProcessLifecycleOwner.get()
                .lifecycle
                .addObserver(CarOverlayVisibilityHooks(application))
            L.d("Car overlay visibility hooks registered")
        }
    }
}
