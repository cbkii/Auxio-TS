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

import android.content.Intent
import androidx.navigation.fragment.findNavController
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import org.oxycblt.auxio.BuildConfig
import org.oxycblt.auxio.R
import org.oxycblt.auxio.car.overlay.CarOverlayActivity
import org.oxycblt.auxio.car.overlay.CarOverlayPermissionActivity
import org.oxycblt.auxio.car.overlay.CarOverlaySettings
import org.oxycblt.auxio.headunit.BootStartupMode
import org.oxycblt.auxio.headunit.compat.HeadUnitCompatManager
import org.oxycblt.auxio.headunit.compat.NativePrivateIntegrationStatus
import org.oxycblt.auxio.headunit.overlay.CarOverlayContract
import org.oxycblt.auxio.headunit.topway.Ts18LauncherIntegrationMode
import org.oxycblt.auxio.playback.service.PlaybackChannelState
import org.oxycblt.auxio.playback.service.PlaybackNotificationChannel
import org.oxycblt.auxio.settings.BasePreferenceFragment
import org.oxycblt.auxio.ui.UISettings
import org.oxycblt.auxio.util.navigateSafe

@AndroidEntryPoint
class CarPreferenceFragment : BasePreferenceFragment(R.xml.preferences_car) {

    @Inject lateinit var uiSettings: UISettings

    override fun onResume() {
        super.onResume()
        refreshRuntimePreferences()
    }

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
            KEY_CAR_OVERLAY_LAUNCH_NOW -> setupCarOverlayLaunchNow(preference)
            KEY_CAR_OVERLAY_RESET_POSITION -> setupCarOverlayReset(preference)
            getString(R.string.set_key_ts18_fast_resume_status) ->
                setupTs18FastResumeStatus(preference)
            getString(R.string.set_key_head_unit_startup_mode) -> setupStartupMode(preference)
            getString(R.string.set_key_overlay_permission) -> setupOverlayPermission(preference)
            getString(R.string.set_key_launcher_integration) -> setupLauncherIntegration(preference)
            getString(R.string.set_key_ts18_launcher_integration_mode) ->
                setupLauncherIntegrationAdvancedOverride(preference)
            getString(R.string.set_key_playback_notification_access) ->
                setupPlaybackNotificationAccess(preference)
            "open_diagnostics" -> {
                preference.setOnPreferenceClickListener {
                    findNavController()
                        .navigateSafe(CarPreferenceFragmentDirections.diagnosticsPreferences())
                    true
                }
            }
        }
    }

    private fun refreshRuntimePreferences() {
        findPreference<Preference>(getString(R.string.set_key_head_unit_startup_mode))
            ?.let(::setupStartupMode)
        findPreference<Preference>(getString(R.string.set_key_overlay_permission))
            ?.let(::setupOverlayPermission)
        findPreference<Preference>(KEY_CAR_OVERLAY_ENABLED)?.let(::setupCarOverlayEnabled)
        findPreference<Preference>(getString(R.string.set_key_launcher_integration))
            ?.let(::setupLauncherIntegration)
        findPreference<Preference>(getString(R.string.set_key_playback_notification_access))
            ?.let(::setupPlaybackNotificationAccess)
    }

    private fun setupTs18FastResumeStatus(preference: Preference) {
        preference.summary = getString(R.string.set_ts18_fast_resume_status_desc)
        preference.setOnPreferenceClickListener(null)
    }

    private fun setupStartupMode(preference: Preference) {
        if (!BuildConfig.TOPWAY_COMPAT_ENABLED) {
            preference.isVisible = false
            return
        }
        val list = preference as? ListPreference ?: return
        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val current = BootStartupMode.resolve(prefs, requireContext()).persistedValue
        list.isPersistent = false
        list.value = current
        val currentIdx = list.findIndexOfValue(current)
        list.summary = list.entries?.getOrNull(currentIdx)
        list.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { pref, newValue ->
                val mode =
                    BootStartupMode.fromPersisted(newValue as String)
                        ?: return@OnPreferenceChangeListener false
                BootStartupMode.persist(prefs, requireContext(), mode)
                val lp = pref as? ListPreference
                val newIndex = lp?.findIndexOfValue(mode.persistedValue) ?: -1
                lp?.entries?.getOrNull(newIndex)?.let { lp.summary = it }
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
        val list = preference as? ListPreference ?: return
        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val current =
            Ts18LauncherIntegrationMode.resolveEffectiveMode(
                prefs = prefs,
                topwayProduct = BuildConfig.TOPWAY_COMPAT_ENABLED,
            )
        val standard =
            Ts18LauncherIntegrationMode.resolveStandardMode(
                prefs = prefs,
                topwayProduct = BuildConfig.TOPWAY_COMPAT_ENABLED,
            )
        list.isPersistent = false
        list.value = standard.name
        list.summary =
            when (current) {
                Ts18LauncherIntegrationMode.GenericDofunMedia ->
                    getString(R.string.set_launcher_integration_standard_generic)
                Ts18LauncherIntegrationMode.AndroidMediaSessionOnly ->
                    getString(R.string.set_launcher_integration_standard_android)
                else -> getString(R.string.set_launcher_integration_advanced_summary, current.name)
            }
        list.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { pref, newValue ->
                val mode =
                    Ts18LauncherIntegrationMode.fromPreference(newValue as String).takeIf {
                        it == Ts18LauncherIntegrationMode.GenericDofunMedia ||
                            it == Ts18LauncherIntegrationMode.AndroidMediaSessionOnly
                    } ?: return@OnPreferenceChangeListener false
                Ts18LauncherIntegrationMode.persistStandardMode(prefs, mode)
                (pref as? ListPreference)?.summary =
                    if (mode == Ts18LauncherIntegrationMode.GenericDofunMedia) {
                        getString(R.string.set_launcher_integration_standard_generic)
                    } else {
                        getString(R.string.set_launcher_integration_standard_android)
                    }
                true
            }
    }

    private fun setupLauncherIntegrationAdvancedOverride(preference: Preference) {
        val list = preference as? ListPreference ?: return
        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        list.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue ->
                val mode = Ts18LauncherIntegrationMode.fromPreference(newValue as String)
                if (mode.isStandardMode) {
                    Ts18LauncherIntegrationMode.persistStandardMode(prefs, mode)
                }
                findPreference<Preference>(getString(R.string.set_key_launcher_integration))
                    ?.let(::setupLauncherIntegration)
                true
            }
    }

    private fun setupPlaybackNotificationAccess(preference: Preference) {
        val snapshot = PlaybackNotificationChannel.inspect(requireContext())
        val importance =
            snapshot.importance?.toString()
                ?: getString(R.string.set_playback_channel_unknown_importance)
        val channelSummary =
            when (snapshot.state) {
                PlaybackChannelState.Usable ->
                    getString(R.string.set_playback_channel_usable, importance)
                PlaybackChannelState.Blocked ->
                    getString(R.string.set_playback_channel_blocked, importance)
                PlaybackChannelState.NotCreated ->
                    getString(R.string.set_playback_channel_not_created)
            }
        preference.summary =
            getString(
                R.string.set_playback_notification_access_summary,
                BuildConfig.APPLICATION_ID,
                statusSummary(snapshot.packageNotificationsEnabled),
                channelSummary,
                if (snapshot.publicationRequestedThisProcess) {
                    getString(R.string.set_status_yes)
                } else {
                    getString(R.string.set_status_no)
                },
            )
        preference.setOnPreferenceClickListener {
            startActivity(PlaybackNotificationChannel.settingsIntent(requireContext()))
            true
        }
    }

    private fun setupCarOverlayEnabled(preference: Preference) {
        if (!BuildConfig.TOPWAY_COMPAT_ENABLED) return

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
        if (!BuildConfig.TOPWAY_COMPAT_ENABLED) return
        preference.setOnPreferenceClickListener {
            CarOverlaySettings.resetPosition(requireContext())
            true
        }
    }

    private fun setupCarOverlayLaunchNow(preference: Preference) {
        preference.setOnPreferenceClickListener {
            startActivity(
                Intent(requireContext(), CarOverlayActivity::class.java)
                    .setAction(CarOverlayActivity.ACTION_LAUNCH_FLOATING_CONTROLS)
            )
            true
        }
    }

    private fun statusSummary(status: Boolean): String =
        if (status) getString(R.string.lbl_enabled) else getString(R.string.lbl_disabled)

    private companion object {
        const val KEY_CAR_OVERLAY_ENABLED = CarOverlayContract.KEY_ENABLED
        const val KEY_CAR_OVERLAY_LAUNCH_NOW = "car_overlay_launch_now"
        const val KEY_CAR_OVERLAY_RESET_POSITION = "car_overlay_reset_position"
    }
}
