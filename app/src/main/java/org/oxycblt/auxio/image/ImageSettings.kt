/*
 * Copyright (c) 2023 Auxio Project
 * ImageSettings.kt is part of Auxio.
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

package org.oxycblt.auxio.image

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import org.oxycblt.auxio.IntegerTable
import org.oxycblt.auxio.R
import org.oxycblt.auxio.settings.Settings
import timber.log.Timber as L

/**
 * User configuration specific to image loading.
 *
 * @author Alexander Capehart (OxygenCobalt)
 */
interface ImageSettings : Settings<ImageSettings.Listener> {
    /** The strategy to use when loading album covers. */
    val coverMode: CoverMode
    /** Whether to force all album covers to have a 1:1 aspect ratio. */
    val forceSquareCovers: Boolean

    interface Listener {
        /** Called when [coverMode] changes. */
        fun onImageSettingsChanged() {}
    }
}

class ImageSettingsImpl @Inject constructor(@ApplicationContext context: Context) :
    Settings.Impl<ImageSettings.Listener>(context), ImageSettings {
    override val coverMode: CoverMode
        get() {
            val code =
                sharedPreferences.getInt(getString(R.string.set_key_cover_mode), Int.MIN_VALUE)
            // Reads are deliberately side-effect free. Unknown values fail open to enabled artwork;
            // durable normalization is owned exclusively by migrate().
            return CoverMode.fromIntCode(code) ?: CoverMode.OPTIMISED
        }

    override val forceSquareCovers: Boolean
        get() = sharedPreferences.getBoolean(getString(R.string.set_key_square_covers), false)

    override fun migrate() {
        // Preserve the original Auxio migration contract: OFF is restored only from an explicit
        // legacy "show covers = false" value. Legacy quality choices remain artwork-enabled.
        if (
            sharedPreferences.contains(OLD_KEY_SHOW_COVERS) ||
                sharedPreferences.contains(OLD_KEY_QUALITY_COVERS)
        ) {
            L.d("Migrating legacy cover visibility settings")
            val mode =
                if (!sharedPreferences.getBoolean(OLD_KEY_SHOW_COVERS, true)) {
                    CoverMode.OFF
                } else {
                    CoverMode.OPTIMISED
                }
            sharedPreferences.edit {
                putInt(getString(R.string.set_key_cover_mode), mode.intCode)
                remove(OLD_KEY_SHOW_COVERS)
                remove(OLD_KEY_QUALITY_COVERS)
            }
        }

        if (sharedPreferences.contains(OLD_KEY_COVER_MODE)) {
            L.d("Migrating legacy cover mode setting")
            val legacyCode = sharedPreferences.getInt(OLD_KEY_COVER_MODE, Int.MIN_VALUE)
            val mode = legacyMode(legacyCode)
            sharedPreferences.edit {
                putInt(getString(R.string.set_key_cover_mode), mode.intCode)
                remove(OLD_KEY_COVER_MODE)
            }
        }

        val currentCode =
            sharedPreferences.getInt(getString(R.string.set_key_cover_mode), Int.MIN_VALUE)
        if (currentCode != Int.MIN_VALUE && CoverMode.fromIntCode(currentCode) == null) {
            // Unknown current values must never silently become OFF. Normalize once here rather
            // than mutating durable state from the coverMode getter.
            sharedPreferences.edit {
                putInt(getString(R.string.set_key_cover_mode), CoverMode.OPTIMISED.intCode)
            }
        }
    }

    private fun legacyMode(code: Int): CoverMode =
        when (code) {
            IntegerTable.COVER_MODE_OFF -> CoverMode.OFF
            IntegerTable.COVER_MODE_AS_IS -> CoverMode.AS_IS
            IntegerTable.COVER_MODE_BALANCED,
            IntegerTable.COVER_MODE_HIGH_QUALITY,
            IntegerTable.COVER_MODE_SAVE_SPACE -> CoverMode.OPTIMISED
            else -> CoverMode.OPTIMISED
        }

    override fun onSettingChanged(key: String, listener: ImageSettings.Listener) {
        if (
            key == getString(R.string.set_key_cover_mode) ||
                key == getString(R.string.set_key_square_covers)
        ) {
            L.d("Dispatching image setting change")
            listener.onImageSettingsChanged()
        }
    }

    private companion object {
        const val OLD_KEY_SHOW_COVERS = "KEY_SHOW_COVERS"
        const val OLD_KEY_QUALITY_COVERS = "KEY_QUALITY_COVERS"
        const val OLD_KEY_COVER_MODE = "auxio_cover_mode"
    }
}
