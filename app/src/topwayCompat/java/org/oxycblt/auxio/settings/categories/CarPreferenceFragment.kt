/*
 * Copyright (c) 2024 Auxio Project
 * CarPreferenceFragment.kt is part of Auxio.
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

import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import org.oxycblt.auxio.BuildConfig
import org.oxycblt.auxio.R
import org.oxycblt.auxio.car.overlay.CarOverlaySettings
import org.oxycblt.auxio.headunit.compat.HeadUnitCompatManager
import org.oxycblt.auxio.headunit.compat.NativePrivateIntegrationStatus
import org.oxycblt.auxio.settings.BasePreferenceFragment
import org.oxycblt.auxio.ui.UISettings
import org.oxycblt.auxio.util.navigateSafe

@AndroidEntryPoint
class CarPreferenceFragment : BasePreferenceFragment(R.xml.preferences_car) {

    @Inject lateinit var uiSettings: UISettings

    override fun onSetupPreference(preference: Preference) {
        when (preference.key) {
            getString(R.string.set_head_unit_compat_status) -> {
                val compatStatus =
                    HeadUnitCompatManager.currentStatus(
                        compatModeEnabled = uiSettings.headUnitLandscapeMode,
                        widgetMetadataPublishable = uiSettings.showHeadUnitAlbumArt,
                        shortcutCompatReady = uiSettings.showHeadUnitDashboardQuickAccess,
                        sessionCompatReady = uiSettings.headUnitLandscapeMode,
                    )
                val nativeStatusSummary =
                    when (compatStatus.nativePrivateIntegrationStatus) {
                        NativePrivateIntegrationStatus.NOT_ENABLED_REQUIRES_VALIDATION ->
                            getString(
                                R.string.set_head_unit_compat_native_not_enabled_requires_validation
                            )
                    }
                preference.summary =
                    getString(
                        R.string.set_head_unit_compat_status_summary,
                        statusSummary(compatStatus.compatModeEnabled),
                        statusSummary(compatStatus.androidFallbackActive),
                        statusSummary(compatStatus.widgetMetadataPublishable),
                        statusSummary(compatStatus.shortcutCompatReady),
                        statusSummary(compatStatus.sessionCompatReady),
                        nativeStatusSummary,
                    ) + "\n" + uiSettings.headUnitCompatStatusSummary
            }
            KEY_CAR_OVERLAY_ENABLED -> setupCarOverlayEnabled(preference)
            KEY_CAR_OVERLAY_RESET_POSITION -> setupCarOverlayReset(preference)
            getString(R.string.set_key_ts18_fast_resume_status) ->
                setupTs18FastResumeStatus(preference)
            getString(R.string.set_key_autostart_floating_only) ->
                setupAutostartFloatingOnly(preference)
            "open_diagnostics" -> {
                preference.setOnPreferenceClickListener {
                    findNavController()
                        .navigateSafe(CarPreferenceFragmentDirections.diagnosticsPreferences())
                    true
                }
            }
        }
    }

    private fun setupTs18FastResumeStatus(preference: Preference) {
        preference.summary = getString(R.string.set_ts18_fast_resume_status_desc)
        preference.setOnPreferenceClickListener(null)
    }

    private fun setupAutostartFloatingOnly(preference: Preference) {
        if (!BuildConfig.TOPWAY_COMPAT_FLAVOR) {
            preference.isVisible = false
            return
        }

        val overlayEnabled = CarOverlaySettings.isEnabled(requireContext())

        preference.summary =
            if (overlayEnabled) {
                getString(R.string.set_autostart_floating_only_desc)
            } else {
                getString(R.string.set_autostart_floating_only_desc_requires_overlay)
            }
    }

    private fun setupCarOverlayEnabled(preference: Preference) {
        if (!BuildConfig.TOPWAY_COMPAT_FLAVOR) return

        // Sync initial checked state from the actual source of truth.
        val currentlyEnabled = CarOverlaySettings.isEnabled(requireContext())
        (preference as? androidx.preference.TwoStatePreference)?.isChecked = currentlyEnabled

        preference.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { pref, newValue ->
                val result = CarOverlaySettings.setEnabled(requireContext(), newValue as Boolean)
                if (!result) {
                    // Permission needed — revert switch to unchecked.
                    (pref as? androidx.preference.TwoStatePreference)?.isChecked = false
                }
                // Update dependent preferences like autostart floating only
                val autostartFloatingOnly =
                    findPreference<Preference>(getString(R.string.set_key_autostart_floating_only))
                if (autostartFloatingOnly != null) {
                    setupAutostartFloatingOnly(autostartFloatingOnly)
                }
                result
            }
    }

    private fun setupCarOverlayReset(preference: Preference) {
        if (!BuildConfig.TOPWAY_COMPAT_FLAVOR) return
        preference.setOnPreferenceClickListener {
            CarOverlaySettings.resetPosition(requireContext())
            true
        }
    }

    private fun statusSummary(status: Boolean): String =
        if (status) getString(R.string.lbl_enabled) else getString(R.string.lbl_disabled)

    private companion object {
        const val KEY_CAR_OVERLAY_ENABLED = "car_overlay_enabled"
        const val KEY_CAR_OVERLAY_RESET_POSITION = "car_overlay_reset_position"
    }
}
