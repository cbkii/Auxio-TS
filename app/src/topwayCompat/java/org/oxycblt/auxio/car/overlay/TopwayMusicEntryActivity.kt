/*
 * Copyright (c) 2026 Auxio Project
 * TopwayMusicEntryActivity.kt is part of Auxio.
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
import android.content.Intent
import android.os.Bundle
import androidx.preference.PreferenceManager
import org.oxycblt.auxio.MainActivity
import org.oxycblt.auxio.R
import timber.log.Timber as L

/** Routes the stock-name DoFun music component without exposing two competing launcher entries. */
class TopwayMusicEntryActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // FULL_PLAYER is the fail-safe task disposition until the requested route is resolved.
        var entryRoute = TopwayMusicEntryPolicy.Route.FULL_PLAYER
        try {
            entryRoute = resolveEntryRoute()
            routeEntry(entryRoute)
        } finally {
            finishEntry(entryRoute)
        }
    }

    private fun resolveEntryRoute(): TopwayMusicEntryPolicy.Route {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val floatingOnly =
            prefs.getBoolean(getString(R.string.set_key_autostart_floating_only), false)
        return TopwayMusicEntryPolicy.route(intent.action, intent.data != null, floatingOnly)
    }

    private fun routeEntry(entryRoute: TopwayMusicEntryPolicy.Route) {
        when (entryRoute) {
            TopwayMusicEntryPolicy.Route.FLOATING_CONTROLS_ONLY -> {
                L.i("Topway music entry routed to persistent floating controls")
                CarOverlayVisibilityHooks.isSuppressedByAuxioForeground = false
                if (
                    CarOverlaySettings.isEnabled(this) &&
                        CarOverlaySettings.hasOverlayPermission(this)
                ) {
                    CarFloatingControlsService.restoreIfEnabled(this, "topway_music_entry")
                } else if (CarOverlaySettings.hasOverlayPermission(this)) {
                    CarOverlaySettings.setEnabled(this, true)
                    CarFloatingControlsService.restoreIfEnabled(this, "topway_music_entry_enable")
                } else {
                    CarOverlaySettings.setEnabled(this, true)
                }
            }
            TopwayMusicEntryPolicy.Route.FULL_PLAYER -> {
                L.i("Topway music entry routed to full player action=${intent.action}")
                val fullPlayerIntent =
                    Intent(this, MainActivity::class.java).apply {
                        action = intent.action
                        if (intent.data != null || intent.type != null) {
                            setDataAndType(intent.data, intent.type)
                        }
                        addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        if (intent.flags and Intent.FLAG_GRANT_READ_URI_PERMISSION != 0) {
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                    }
                startActivity(fullPlayerIntent)
            }
        }
    }

    private fun finishEntry(entryRoute: TopwayMusicEntryPolicy.Route) {
        // Only floating-only launches own a transient task that should be removed from Recents.
        when (entryRoute) {
            TopwayMusicEntryPolicy.Route.FLOATING_CONTROLS_ONLY -> {
                if (isTaskRoot) {
                    finishAndRemoveTask()
                } else {
                    finish()
                }
            }
            TopwayMusicEntryPolicy.Route.FULL_PLAYER -> finish()
        }
    }
}
