/*
 * Copyright (c) 2024 Auxio Project
 * CarOverlayActivity.kt is part of Auxio.
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
import android.os.Bundle
import org.oxycblt.auxio.headunit.overlay.FloatingOnlyStartupCoordinator
import timber.log.Timber as L

/** A no-display entry point for launching a fully initialised floating-only Auxio session. */
class CarOverlayActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val enabledImmediately = CarOverlaySettings.setEnabled(this, true, startRuntime = false)
        CarOverlayVisibilityHooks.isSuppressedByAuxioForeground = false

        if (enabledImmediately) {
            val result =
                FloatingOnlyStartupCoordinator.start(
                    this,
                    reason = "floating_launcher",
                    restorePlayback = true,
                )
            L.i("Floating launcher startup result: $result")
        } else {
            // Permission UI is the only visible surface allowed from this no-display entry. Start
            // the canonical service now so library/playback state is hydrated while the user grants
            // overlay permission; the permission activity will attach the overlay after approval.
            val playback =
                FloatingOnlyStartupCoordinator.startPlayback(
                    this,
                    reason = "floating_launcher_permission_pending",
                    restorePlayback = true,
                )
            L.i("Floating launcher awaiting overlay permission; playback=$playback")
        }
        finish()
    }

    companion object {
        const val ACTION_LAUNCH_FLOATING_CONTROLS =
            "org.oxycblt.auxio.action.LAUNCH_FLOATING_CONTROLS"
    }
}
