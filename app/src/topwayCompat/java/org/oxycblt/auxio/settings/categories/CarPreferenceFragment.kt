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
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import org.oxycblt.auxio.BuildConfig
import org.oxycblt.auxio.R
import org.oxycblt.auxio.car.overlay.CarOverlayPermissionActivity
import org.oxycblt.auxio.car.overlay.CarOverlaySettings
import org.oxycblt.auxio.headunit.compat.HeadUnitCompatManager
import org.oxycblt.auxio.headunit.compat.NativePrivateIntegrationStatus
import org.oxycblt.auxio.headunit.overlay.CarOverlayContract
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
            getString(R.string.set_key_head_unit_startup_mode) -> setupStartupMode(preference)
            getString(R.string.set_key_overlay_permission) -> setupOverlayPermission(preference)
            getString(R.string.set_key_launcher_integration) -> setupLauncherIntegration(preference)
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

    private fun setupStartupMode(preference: Preference) {
        if (!BuildConfig.TOPWAY_COMPAT_FLAVOR) {
            preference.isVisible = false
            return
        }
        val list = preference as? ListPreference ?: return
        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val autostart = prefs.getBoolean(getString(R.string.set_key_autostart_on_boot), false)
        val floatingOnly =
            prefs.getBoolean(getString(R.string.set_key_autostart_floating_only), false)
        val current =
            when {
                !autostart -> STARTUP_NONE
                floatingOnly -> STARTUP_FLOATING_ONLY
                else -> STARTUP_OPEN_AUXIO
            }
        list.value = current
        val currentIdx = list.findIndexOfValue(current)
        list.summary = list.entries?.getOrNull(currentIdx) ?: ""
        list.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { pref, newValue ->
                val mode = newValue as String
                prefs
                    .edit()
                    .putBoolean(getString(R.string.set_key_autostart_on_boot), mode != STARTUP_NONE)
                    .putBoolean(
                        getString(R.string.set_key_autostart_floating_only),
                        mode == STARTUP_FLOATING_ONLY,
                    )
                    .apply()
                val lp = pref as? ListPreference
                val modeIdx = lp?.findIndexOfValue(mode) ?: -1
                lp?.entries?.getOrNull(modeIdx)?.let { lp.summary = it }
                true
            }
    }

    private fun setupOverlayPermission(preference: Preference) {
        val granted = CarOverlaySettings.hasOverlayPermission(requireContext())
        preference.summary =
            if (granted) getString(R.string.set_diagnostics_granted)
            else getString(R.string.set_floating_controls_permission_desc)
        preference.setOnPreferenceClickListener {
            startActivity(CarOverlayPermissionActivity.intent(requireContext()))
            true
        }
    }

    private fun setupLauncherIntegration(preference: Preference) {
        preference.setOnPreferenceClickListener {
            findNavController()
                .navigateSafe(CarPreferenceFragmentDirections.diagnosticsPreferences())
            true
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
                // Re-read the persisted source of truth because permission policy may reject
                // enable.
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
        const val KEY_CAR_OVERLAY_ENABLED = CarOverlayContract.KEY_ENABLED
        const val KEY_CAR_OVERLAY_RESET_POSITION = "car_overlay_reset_position"
        const val STARTUP_NONE = "none"
        const val STARTUP_OPEN_AUXIO = "open_auxio"
        const val STARTUP_FLOATING_ONLY = "floating_only"
    }
}
