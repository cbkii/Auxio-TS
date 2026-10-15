/*
 * Copyright (c) 2026 Auxio Project
 * CarOverlayEnableActivity.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * <https://www.gnu.org/licenses/>.
 */

package org.oxycblt.auxio.car.overlay

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import org.oxycblt.auxio.R
import org.oxycblt.auxio.headunit.overlay.FloatingOnlyStartupCoordinator

/** Explicit user consent boundary for re-enabling a disabled overlay from an exported launcher. */
class CarOverlayEnableActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AlertDialog.Builder(this)
            .setTitle(R.string.car_overlay_launcher_title)
            .setMessage(R.string.car_overlay_launcher_enable_prompt)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                // The actual confirmation click is required; onCreate must have no side effects.
                val enabled = CarOverlaySettings.setEnabled(this, true, startRuntime = false)
                if (enabled) {
                    FloatingOnlyStartupCoordinator.start(
                        this,
                        reason = "floating_launcher_confirmed",
                        restorePlayback = true,
                    )
                } else {
                    // Permission screen is now pending after an explicit user confirmation.
                    FloatingOnlyStartupCoordinator.startPlayback(
                        this,
                        reason = "floating_launcher_confirmed_permission_pending",
                        restorePlayback = true,
                    )
                }
            }
            .setNegativeButton(android.R.string.cancel, null)
            .setOnDismissListener { finish() }
            .show()
    }

    companion object {
        fun intent(context: Context): Intent = Intent(context, CarOverlayEnableActivity::class.java)
    }
}
