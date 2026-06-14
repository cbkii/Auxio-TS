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

    interface Listener {
        fun onDiagnosticsSettingsChanged() {}
    }
}

@Singleton
class DiagnosticsSettingsImpl @Inject constructor(@ApplicationContext context: Context) :
    Settings.Impl<DiagnosticsSettings.Listener>(context), DiagnosticsSettings {

    override var armedBootCaptureId: String?
        get() = sharedPreferences.getString("diag_armed_id", null)
        set(value) =
            sharedPreferences.edit {
                putString("diag_armed_id", value)
                apply()
            }

    override var armedExpiryTime: Long
        get() = sharedPreferences.getLong("diag_armed_expiry", 0L)
        set(value) =
            sharedPreferences.edit {
                putLong("diag_armed_expiry", value)
                apply()
            }

    override fun onSettingChanged(key: String, listener: DiagnosticsSettings.Listener) {
        listener.onDiagnosticsSettingsChanged()
    }
}
