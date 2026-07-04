/*
 * Copyright (c) 2023 Auxio Project
 * AudioPreferenceFragment.kt is part of Auxio.
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

package org.oxycblt.auxio.settings.categories

/**
 * Audio settings interface.
 *
 * @author Alexander Capehart (OxygenCobalt)
 */
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import org.oxycblt.auxio.BuildConfig
import org.oxycblt.auxio.R
import org.oxycblt.auxio.settings.BasePreferenceFragment
import org.oxycblt.auxio.settings.ui.WrappedDialogPreference
import org.oxycblt.auxio.util.navigateSafe
import timber.log.Timber as L

class AudioPreferenceFragment : BasePreferenceFragment(R.xml.preferences_audio) {

    override fun onSetupPreference(preference: Preference) {
        when (preference.key) {
            getString(R.string.set_key_autostart_floating_only) -> {
                if (!BuildConfig.TOPWAY_COMPAT_FLAVOR) {
                    preference.isVisible = false
                    return
                }

                val prefs =
                    androidx.preference.PreferenceManager.getDefaultSharedPreferences(
                        requireContext()
                    )
                val overlayEnabled = prefs.getBoolean("car_overlay_enabled", false)

                preference.summary =
                    if (overlayEnabled) {
                        getString(R.string.set_autostart_floating_only_desc)
                    } else {
                        getString(R.string.set_autostart_floating_only_desc_requires_overlay)
                    }
            }
        }
    }

    override fun onOpenDialogPreference(preference: WrappedDialogPreference) {
        if (preference.key == getString(R.string.set_key_pre_amp)) {
            L.d("Navigating to pre-amp dialog")
            findNavController().navigateSafe(AudioPreferenceFragmentDirections.preAmpSettings())
        }
    }
}
