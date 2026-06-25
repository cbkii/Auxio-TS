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

import android.app.Activity
import android.app.Application
import android.os.Bundle
import timber.log.Timber as L

/**
 * Application-level activity lifecycle callbacks that keep the car floating overlay converged with
 * user settings. New starts proactively restore the overlay when enabled, while the optional
 * "hide while Auxio foreground" setting still suppresses the overlay during Auxio UI use.
 */
class CarOverlayVisibilityHooks : Application.ActivityLifecycleCallbacks {

    private var startedActivityCount = 0

    override fun onActivityStarted(activity: Activity) {
        val previous = startedActivityCount
        startedActivityCount++
        if (previous == 0) {
            val prefs = CarOverlayPrefs.from(activity)
            if (!prefs.enabled) return

            if (prefs.hideWhileAuxioForeground) {
                L.d("Auxio entered foreground, signalling overlay to hide")
                CarFloatingControlsService.setAuxioForeground(activity, true)
            } else {
                L.d("Auxio entered foreground with overlay allowed, restoring overlay")
                CarFloatingControlsService.restoreIfEnabled(activity, "activity_started")
            }
        }
    }

    override fun onActivityStopped(activity: Activity) {
        if (activity.isChangingConfigurations) return

        val previous = startedActivityCount
        startedActivityCount = (startedActivityCount - 1).coerceAtLeast(0)
        if (previous == 1 && startedActivityCount == 0) {
            val prefs = CarOverlayPrefs.from(activity)
            if (!prefs.enabled) return

            L.d("Auxio entered background, restoring overlay")
            if (prefs.hideWhileAuxioForeground) {
                CarFloatingControlsService.setAuxioForeground(activity, false)
            } else {
                CarFloatingControlsService.restoreIfEnabled(activity, "activity_stopped")
            }
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        val prefs =
            try {
                CarOverlayPrefs.from(activity)
            } catch (e: RuntimeException) {
                L.w(e, "Unable to read overlay prefs during activity-created restore")
                return
            }
        if (prefs.enabled && !prefs.hideWhileAuxioForeground) {
            CarFloatingControlsService.restoreIfEnabled(activity, "activity_created")
        }
    }

    override fun onActivityResumed(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}

    companion object {
        fun register(application: Application) {
            application.registerActivityLifecycleCallbacks(CarOverlayVisibilityHooks())
            L.d("Car overlay visibility hooks registered")
        }
    }
}
