/*
 * Copyright (c) 2024 Auxio Project
 * CarOverlayPrefs.kt is part of Auxio.
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

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager

/**
 * SharedPreferences wrapper for car floating controls overlay configuration. Manages enablement,
 * position, opacity, visibility, display mode, and ticker width.
 *
 * Uses [PreferenceManager.getDefaultSharedPreferences] so runtime state and the AndroidX preference
 * UI share one source of truth.
 */
class CarOverlayPrefs private constructor(private val prefs: SharedPreferences) {
    enum class DisplayMode(
        val storageValue: String,
        val showsControls: Boolean,
        val showsTicker: Boolean,
    ) {
        CONTROLS("controls", showsControls = true, showsTicker = false),
        CONTROLS_AND_TICKER("controls_ticker", showsControls = true, showsTicker = true),
        TICKER_ONLY("ticker_only", showsControls = false, showsTicker = true);

        companion object {
            fun fromStorage(value: String?): DisplayMode =
                entries.firstOrNull { it.storageValue == value } ?: CONTROLS
        }
    }

    init {
        if (prefs.contains("car_overlay_suppressed_auxio_fg")) {
            prefs.edit().remove("car_overlay_suppressed_auxio_fg").apply()
        }
        if (!prefs.contains(KEY_DISPLAY_MODE) && prefs.contains(LEGACY_KEY_SHOW_TRACK_TICKER)) {
            val migratedMode =
                if (prefs.getBoolean(LEGACY_KEY_SHOW_TRACK_TICKER, false)) {
                    DisplayMode.CONTROLS_AND_TICKER
                } else {
                    DisplayMode.CONTROLS
                }
            prefs.edit {
                putString(KEY_DISPLAY_MODE, migratedMode.storageValue)
                remove(LEGACY_KEY_SHOW_TRACK_TICKER)
            }
        }
        if (!prefs.getBoolean(KEY_PERSISTENCE_DEFAULT_MIGRATED, false)) {
            // Persistent controls are the safety-first default. Clear the old default-on hide
            // preference once; users of later builds may still explicitly opt in if re-exposed.
            prefs.edit {
                putBoolean(KEY_HIDE_WHILE_AUXIO_FG, false)
                putBoolean(KEY_PERSISTENCE_DEFAULT_MIGRATED, true)
            }
        }
    }

    var enabled: Boolean
        get() = prefs.getBoolean(KEY_ENABLED, false)
        set(value) = prefs.edit { putBoolean(KEY_ENABLED, value) }

    /** Pending-enable flag: permission activity sets this when permission is being requested. */
    var pendingEnable: Boolean
        get() = prefs.getBoolean(KEY_PENDING_ENABLE, false)
        set(value) = prefs.edit { putBoolean(KEY_PENDING_ENABLE, value) }

    var hideWhileAuxioForeground: Boolean
        get() = prefs.getBoolean(KEY_HIDE_WHILE_AUXIO_FG, false)
        set(value) = prefs.edit { putBoolean(KEY_HIDE_WHILE_AUXIO_FG, value) }

    var displayMode: DisplayMode
        get() = DisplayMode.fromStorage(prefs.getString(KEY_DISPLAY_MODE, null))
        set(value) = prefs.edit { putString(KEY_DISPLAY_MODE, value.storageValue) }

    /** Width of ticker-only mode relative to the existing 100% controls/ticker row width. */
    var tickerWidthPercent: Int
        get() {
            val stored = prefs.getInt(KEY_TICKER_WIDTH_PERCENT, DEFAULT_TICKER_WIDTH_PERCENT)
            return if (stored in ALLOWED_TICKER_WIDTH_PERCENTS) {
                stored
            } else {
                DEFAULT_TICKER_WIDTH_PERCENT
            }
        }
        set(value) {
            val normalized =
                if (value in ALLOWED_TICKER_WIDTH_PERCENTS) value else DEFAULT_TICKER_WIDTH_PERCENT
            prefs.edit { putInt(KEY_TICKER_WIDTH_PERCENT, normalized) }
        }

    var positionX: Int
        get() = prefs.getInt(KEY_POSITION_X, DEFAULT_X)
        set(value) = prefs.edit { putInt(KEY_POSITION_X, value) }

    var positionY: Int
        get() = prefs.getInt(KEY_POSITION_Y, DEFAULT_Y)
        set(value) = prefs.edit { putInt(KEY_POSITION_Y, value) }

    val hasSavedPosition: Boolean
        get() = prefs.contains(KEY_POSITION_X) && prefs.contains(KEY_POSITION_Y)

    val hasOldDefaultPosition: Boolean
        get() =
            hasSavedPosition &&
                prefs.getInt(KEY_POSITION_X, DEFAULT_X) == OLD_DEFAULT_X &&
                prefs.getInt(KEY_POSITION_Y, DEFAULT_Y) == OLD_DEFAULT_Y

    var opacityPercent: Int
        get() = prefs.getInt(KEY_OPACITY, DEFAULT_OPACITY).coerceIn(MIN_OPACITY, MAX_OPACITY)
        set(value) = prefs.edit { putInt(KEY_OPACITY, value.coerceIn(MIN_OPACITY, MAX_OPACITY)) }

    fun resetPosition() {
        // Clear instead of writing a fixed X so the running service can re-center against the
        // current full physical display width. The property getters still expose the TS18 fallback
        // default for static callers.
        // Uses commit=true to ensure the reset is synchronous, avoiding stale reads when the
        // service immediately handles ACTION_RESET_POSITION.
        prefs.edit(commit = true) {
            remove(KEY_POSITION_X)
            remove(KEY_POSITION_Y)
        }
    }

    companion object {
        const val KEY_ENABLED = "car_overlay_enabled"
        const val KEY_DISPLAY_MODE = "car_overlay_display_mode"
        const val KEY_TICKER_WIDTH_PERCENT = "car_overlay_ticker_width_percent"
        private const val LEGACY_KEY_SHOW_TRACK_TICKER = "car_overlay_show_track_ticker"
        private const val KEY_PENDING_ENABLE = "car_overlay_pending_enable"
        private const val KEY_HIDE_WHILE_AUXIO_FG = "car_overlay_hide_auxio_fg"
        private const val KEY_PERSISTENCE_DEFAULT_MIGRATED =
            "car_overlay_persistence_default_migrated_v2"
        private const val KEY_POSITION_X = "car_overlay_x"
        private const val KEY_POSITION_Y = "car_overlay_y"
        private const val KEY_OPACITY = "car_overlay_opacity"

        // Top-center anchor for the full TS18 physical display (1280x720) using the current
        // estimated overlay width. Runtime placement re-centers from live display metrics when
        // available, but these constants keep first-read/default static behaviour top-edge safe.
        const val DEFAULT_X = 465
        const val DEFAULT_Y = 0
        const val DEFAULT_OPACITY = 90
        const val MIN_OPACITY = 30
        const val MAX_OPACITY = 100
        const val DEFAULT_TICKER_WIDTH_PERCENT = 100
        private const val OLD_DEFAULT_X = 437
        private const val OLD_DEFAULT_Y = 55
        private val ALLOWED_TICKER_WIDTH_PERCENTS = setOf(100, 150, 200, 250, 300)

        fun from(context: Context): CarOverlayPrefs {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
            return CarOverlayPrefs(prefs)
        }
    }
}
