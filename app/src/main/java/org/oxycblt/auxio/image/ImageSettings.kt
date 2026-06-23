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
            return CoverMode.fromIntCode(code)
                ?: if (code == Int.MIN_VALUE) CoverMode.OPTIMISED else migrateLegacy()
        }

    private fun migrateLegacy(): CoverMode {
        // Only legacy/unknown values that CoverMode.fromIntCode() cannot resolve reach this path.
        val migrated = CoverMode.OPTIMISED
        sharedPreferences.edit { putInt(getString(R.string.set_key_cover_mode), migrated.intCode) }
        return migrated
    }

    override val forceSquareCovers: Boolean
        get() = sharedPreferences.getBoolean(getString(R.string.set_key_square_covers), false)

    override fun migrate() {
        // Handle standard Auxio migrations, then ensure TS18 mode simplification
        val currentCode = sharedPreferences.getInt(getString(R.string.set_key_cover_mode), -1)
        if (CoverMode.fromIntCode(currentCode) == null && currentCode != -1) {
            migrateLegacy()
        }
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
