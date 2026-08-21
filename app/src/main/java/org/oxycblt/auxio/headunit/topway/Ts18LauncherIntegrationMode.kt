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

import android.content.SharedPreferences
import androidx.core.content.edit
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

    /** Publishes only the observed Topway/TW metadata and progress broadcasts. */
    val sendsTopwayBroadcasts: Boolean
        get() =
            this == TopwayBroadcastOnly ||
                this == TopwayBroadcastAndCommand ||
                this == AutoAllSafePaths

    /**
     * Publishes the public legacy Android media broadcasts used by VLC-compatible consumers.
     *
     * These are deliberately independent from [sendsTopwayBroadcasts]: the generic DoFun lane
     * should not acquire `com.tw.*` traffic merely to expose `com.android.music.*` compatibility.
     */
    val publishesLegacyAndroidMediaBroadcasts: Boolean
        get() = this == GenericDofunMedia || this == AutoAllSafePaths

    val handlesTopwayCommands: Boolean
        get() =
            this == TopwayCommandOnly ||
                this == TopwayBroadcastAndCommand ||
                this == AutoAllSafePaths

    val diagnosticsOnly: Boolean
        get() = this == DiagnosticsOnly

    /** Whether the standards-first generic DoFun lane is the sole selected integration mode. */
    val usesGenericDofunProfile: Boolean
        get() = this == GenericDofunMedia

    /**
     * Whether the canonical playback notification should use the three-action DoFun/VLC profile.
     *
     * `AutoAllSafePaths` is additive: it keeps this public Android presentation while also enabling
     * the separately gated Topway transports.
     */
    val usesGenericDofunNotificationProfile: Boolean
        get() = this == GenericDofunMedia || this == AutoAllSafePaths

    val bindsTopwayCommandService: Boolean
        get() = handlesTopwayCommands || diagnosticsOnly

    companion object {
        const val PREF_KEY = "auxio_ts18_launcher_integration_mode"

        /**
         * Historical one-time migration marker retained for preference compatibility.
         *
         * Older releases used this marker when `GenericDofunMedia` became the standards-first
         * default. Do not rename it: existing installations rely on it to avoid rewriting an
         * explicit launcher-mode choice.
         */
        const val PREF_GENERIC_DEFAULT_MIGRATED = "auxio_ts18_launcher_generic_default_migrated_v1"

        /**
         * Fresh TS18 installs use every currently observed safe compatibility surface.
         *
         * This is the closest safe equivalent to a vendor music proxy: the same Auxio playback
         * authority is exposed simultaneously through Android MediaSession/MediaBrowser,
         * conventional DoFun/VLC notification + legacy broadcasts, Topway metadata/progress
         * broadcasts, the observed Topway command broadcasts and the verified CommandService
         * callback lane. It does not add another player, queue, MediaSession or notification owner.
         */
        fun defaultFor(topwayProduct: Boolean): Ts18LauncherIntegrationMode =
            if (topwayProduct) AutoAllSafePaths else AndroidMediaSessionOnly

        fun default(): Ts18LauncherIntegrationMode = defaultFor(BuildConfig.TOPWAY_COMPAT_ENABLED)

        fun fromPreference(value: String?): Ts18LauncherIntegrationMode =
            entries.firstOrNull { it.name == value } ?: default()

        /**
         * Resolves the one-time default migration without overwriting an explicit selection.
         *
         * Preference provenance was not recorded by older releases, so any persisted valid mode
         * may have been deliberately selected. Only an absent preference adopts the current proxy-
         * grade default; every persisted valid mode is preserved, including `GenericDofunMedia`.
         */
        fun migrationDecision(
            persistedValue: String?,
            migrationComplete: Boolean,
            topwayProduct: Boolean,
        ): Ts18LauncherModeMigrationDecision {
            val parsed = entries.firstOrNull { it.name == persistedValue }
            if (!topwayProduct || migrationComplete) {
                return Ts18LauncherModeMigrationDecision(
                    mode = parsed ?: defaultFor(topwayProduct),
                    persistMode = null,
                    markComplete = false,
                )
            }

            val shouldAdoptCurrentDefault = parsed == null
            val currentDefault = defaultFor(topwayProduct)
            return Ts18LauncherModeMigrationDecision(
                mode = parsed ?: currentDefault,
                persistMode = if (shouldAdoptCurrentDefault) currentDefault else null,
                markComplete = true,
            )
        }

        /**
         * Resolve and atomically persist the shared migration decision for any runtime entry point.
         */
        fun resolveAndPersist(
            prefs: SharedPreferences,
            topwayProduct: Boolean,
        ): Ts18LauncherModeMigrationDecision {
            val decision =
                migrationDecision(
                    persistedValue = prefs.getString(PREF_KEY, null),
                    migrationComplete = prefs.getBoolean(PREF_GENERIC_DEFAULT_MIGRATED, false),
                    topwayProduct = topwayProduct,
                )
            if (decision.persistMode != null || decision.markComplete) {
                prefs.edit {
                    decision.persistMode?.let { putString(PREF_KEY, it.name) }
                    if (decision.markComplete) putBoolean(PREF_GENERIC_DEFAULT_MIGRATED, true)
                }
            }
            return decision
        }
    }
}

data class Ts18LauncherModeMigrationDecision(
    val mode: Ts18LauncherIntegrationMode,
    val persistMode: Ts18LauncherIntegrationMode?,
    val markComplete: Boolean,
)
