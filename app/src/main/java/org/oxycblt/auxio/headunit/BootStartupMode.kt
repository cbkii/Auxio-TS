/*
 * Copyright (c) 2026 Auxio Project
 * BootStartupMode.kt is part of Auxio.
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

package org.oxycblt.auxio.headunit

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import org.oxycblt.auxio.R

enum class BootStartupMode(val persistedValue: String) {
    DISABLED("none"),
    BACKGROUND_READY("background_ready"),
    FULL_PLAYER("open_auxio"),
    FLOATING_CONTROLS_ONLY("floating_only");

    companion object {
        fun fromPersisted(value: String?): BootStartupMode? =
            entries.firstOrNull { it.persistedValue == value }

        fun resolve(context: Context): BootStartupMode =
            resolve(
                PreferenceManager.getDefaultSharedPreferences(context.applicationContext),
                context,
            )

        fun resolve(preferences: SharedPreferences, context: Context): BootStartupMode {
            val explicit =
                fromPersisted(
                    preferences.getString(
                        context.getString(R.string.set_key_head_unit_startup_mode),
                        null,
                    )
                )
            if (explicit != null) return explicit

            val legacy =
                when {
                    !preferences.getBoolean(
                        context.getString(R.string.set_key_autostart_on_boot),
                        false,
                    ) -> DISABLED
                    preferences.getBoolean(
                        context.getString(R.string.set_key_autostart_floating_only),
                        false,
                    ) -> FLOATING_CONTROLS_ONLY
                    else -> FULL_PLAYER
                }
            persist(preferences, context, legacy)
            return legacy
        }

        fun persist(preferences: SharedPreferences, context: Context, mode: BootStartupMode) {
            preferences.edit {
                putString(
                    context.getString(R.string.set_key_head_unit_startup_mode),
                    mode.persistedValue,
                )
                putBoolean(context.getString(R.string.set_key_autostart_on_boot), mode != DISABLED)
                putBoolean(
                    context.getString(R.string.set_key_autostart_floating_only),
                    mode == FLOATING_CONTROLS_ONLY,
                )
            }
        }
    }
}
