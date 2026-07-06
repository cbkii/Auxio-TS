/*
 * Copyright (c) 2026 Auxio Project
 * Ts18LauncherIntegrationMode.kt is part of Auxio.
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

package org.oxycblt.auxio.headunit.topway

import org.oxycblt.auxio.BuildConfig

enum class Ts18LauncherIntegrationMode {
    Disabled,
    AndroidMediaSessionOnly,
    TopwayBroadcastOnly,
    TopwayCommandOnly,
    TopwayBroadcastAndCommand,
    AutoAllSafePaths,
    DiagnosticsOnly;

    val sendsTopwayBroadcasts: Boolean
        get() =
            this == TopwayBroadcastOnly ||
                this == TopwayBroadcastAndCommand ||
                this == AutoAllSafePaths

    val handlesTopwayCommands: Boolean
        get() =
            this == TopwayCommandOnly ||
                this == TopwayBroadcastAndCommand ||
                this == AutoAllSafePaths

    val diagnosticsOnly: Boolean
        get() = this == DiagnosticsOnly

    companion object {
        const val PREF_KEY = "auxio_ts18_launcher_integration_mode"

        fun default(): Ts18LauncherIntegrationMode =
            if (BuildConfig.TOPWAY_COMPAT_FLAVOR) AutoAllSafePaths else AndroidMediaSessionOnly

        fun fromPreference(value: String?): Ts18LauncherIntegrationMode =
            entries.firstOrNull { it.name == value } ?: default()
    }
}
