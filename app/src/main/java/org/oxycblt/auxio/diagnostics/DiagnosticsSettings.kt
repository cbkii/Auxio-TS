/*
 * Copyright (c) 2026 Auxio Project
 * DiagnosticsSettings.kt is part of Auxio.
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

package org.oxycblt.auxio.diagnostics

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import org.oxycblt.auxio.settings.Settings

interface DiagnosticsSettings : Settings<DiagnosticsSettings.Listener> {
    var armedBootCaptureId: String?
    var armedExpiryTime: Long
    var armedCaptureOrigin: String?
    var armedDurationMs: Long

    fun clearArmedCapture()

    interface Listener {
        fun onDiagnosticsSettingsChanged() {}
    }
}

@Singleton
class DiagnosticsSettingsImpl @Inject constructor(@ApplicationContext context: Context) :
    Settings.Impl<DiagnosticsSettings.Listener>(context), DiagnosticsSettings {

    override var armedBootCaptureId: String?
        get() = sharedPreferences.getString(KEY_ARMED_ID, null)
        set(value) = sharedPreferences.edit { putString(KEY_ARMED_ID, value) }

    override var armedExpiryTime: Long
        get() = sharedPreferences.getLong(KEY_ARMED_EXPIRY, 0L)
        set(value) = sharedPreferences.edit { putLong(KEY_ARMED_EXPIRY, value) }

    override var armedCaptureOrigin: String?
        get() = sharedPreferences.getString(KEY_ARMED_ORIGIN, null)
        set(value) = sharedPreferences.edit { putString(KEY_ARMED_ORIGIN, value) }

    override var armedDurationMs: Long
        get() = sharedPreferences.getLong(KEY_ARMED_DURATION_MS, DEFAULT_ARMED_DURATION_MS)
        set(value) = sharedPreferences.edit { putLong(KEY_ARMED_DURATION_MS, value) }

    override fun clearArmedCapture() {
        sharedPreferences.edit {
            remove(KEY_ARMED_ID)
            remove(KEY_ARMED_EXPIRY)
            remove(KEY_ARMED_ORIGIN)
            remove(KEY_ARMED_DURATION_MS)
        }
    }

    override fun onSettingChanged(key: String, listener: DiagnosticsSettings.Listener) {
        if (key in DIAGNOSTIC_KEYS) listener.onDiagnosticsSettingsChanged()
    }

    private companion object {
        const val KEY_ARMED_ID = "diag_armed_id"
        const val KEY_ARMED_EXPIRY = "diag_armed_expiry"
        const val KEY_ARMED_ORIGIN = "diag_armed_origin"
        const val KEY_ARMED_DURATION_MS = "diag_armed_duration_ms"
        const val DEFAULT_ARMED_DURATION_MS = 5 * 60 * 1000L

        val DIAGNOSTIC_KEYS =
            setOf(KEY_ARMED_ID, KEY_ARMED_EXPIRY, KEY_ARMED_ORIGIN, KEY_ARMED_DURATION_MS)
    }
}
