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
    GenericDofunMedia,
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

    val usesGenericMediaNotification: Boolean
        get() = this == GenericDofunMedia

    val bindsTopwayCommandService: Boolean
        get() = handlesTopwayCommands || diagnosticsOnly

    companion object {
        const val PREF_KEY = "auxio_ts18_launcher_integration_mode"
        const val PREF_GENERIC_DEFAULT_MIGRATED =
            "auxio_ts18_launcher_generic_default_migrated_v1"

        fun default(): Ts18LauncherIntegrationMode =
            if (BuildConfig.TOPWAY_COMPAT_FLAVOR) GenericDofunMedia else AndroidMediaSessionOnly

        fun fromPreference(value: String?): Ts18LauncherIntegrationMode =
            entries.firstOrNull { it.name == value } ?: default()

        /**
         * Resolves the one-time default migration without overwriting an explicitly selected mode.
         *
         * `AutoAllSafePaths` was the former recommended/default Topway mode. Existing users who
         * never chose a narrower mode should move to the standards-first generic media profile;
         * explicit broadcast, command, diagnostics, disabled, and Android-only selections remain
         * untouched.
         */
        fun migrationDecision(
            persistedValue: String?,
            migrationComplete: Boolean,
            topwayCompatFlavor: Boolean,
        ): Ts18LauncherModeMigrationDecision {
            val parsed = entries.firstOrNull { it.name == persistedValue }
            if (!topwayCompatFlavor || migrationComplete) {
                return Ts18LauncherModeMigrationDecision(
                    mode =
                        parsed
                            ?: if (topwayCompatFlavor) {
                                GenericDofunMedia
                            } else {
                                AndroidMediaSessionOnly
                            },
                    persistMode = null,
                    markComplete = false,
                )
            }

            val shouldAdoptGenericDefault = parsed == null || parsed == AutoAllSafePaths
            return Ts18LauncherModeMigrationDecision(
                mode =
                    if (shouldAdoptGenericDefault) {
                        GenericDofunMedia
                    } else {
                        requireNotNull(parsed)
                    },
                persistMode = if (shouldAdoptGenericDefault) GenericDofunMedia else null,
                markComplete = true,
            )
        }
    }
}

data class Ts18LauncherModeMigrationDecision(
    val mode: Ts18LauncherIntegrationMode,
    val persistMode: Ts18LauncherIntegrationMode?,
    val markComplete: Boolean,
)
