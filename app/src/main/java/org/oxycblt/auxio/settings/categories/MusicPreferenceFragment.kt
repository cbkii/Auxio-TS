/*
 * Copyright (c) 2023 Auxio Project
 * MusicPreferenceFragment.kt is part of Auxio.
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

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.SwitchPreferenceCompat
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.oxycblt.auxio.R
import org.oxycblt.auxio.headunit.prestart.EarlyPrestartSettings
import org.oxycblt.auxio.headunit.root.RootStateHolder
import org.oxycblt.auxio.music.MusicViewModel
import org.oxycblt.auxio.settings.BasePreferenceFragment
import org.oxycblt.auxio.settings.RootDiagnosticsHelper
import org.oxycblt.auxio.settings.ui.WrappedDialogPreference
import org.oxycblt.auxio.util.PerfTimer
import org.oxycblt.auxio.util.StartupPerformanceReport
import org.oxycblt.auxio.util.navigateSafe
import org.oxycblt.auxio.util.showToast
import timber.log.Timber as L

/**
 * "Content" settings.
 *
 * @author Alexander Capehart (OxygenCobalt)
 */
@AndroidEntryPoint
class MusicPreferenceFragment : BasePreferenceFragment(R.xml.preferences_music) {
    private val musicModel: MusicViewModel by viewModels()
    @Inject lateinit var rootStateHolder: RootStateHolder
    @Inject lateinit var earlyPrestartSettings: EarlyPrestartSettings

    override fun onOpenDialogPreference(preference: WrappedDialogPreference) {
        when (preference.key) {
            getString(R.string.set_key_separators) -> {
                L.d("Navigating to separator dialog")
                findNavController()
                    .navigateSafe(MusicPreferenceFragmentDirections.separatorsSettings())
            }
            getString(R.string.set_key_music_dirs) -> {
                L.d("Navigating to music locations dialog")
                findNavController()
                    .navigateSafe(MusicPreferenceFragmentDirections.musicLocationsSettings())
            }
        }
    }

    override fun onSetupPreference(preference: Preference) {
        if (preference.key == getString(R.string.set_key_reindex)) {
            preference.setOnPreferenceClickListener {
                musicModel.refresh()
                true
            }
        }
        if (preference.key == getString(R.string.set_key_rescan)) {
            preference.setOnPreferenceClickListener {
                AlertDialog.Builder(requireContext())
                    .setTitle(R.string.set_rescan)
                    .setMessage(R.string.set_rescan_desc)
                    .setPositiveButton(android.R.string.ok) { _, _ -> musicModel.rescan() }
                    .setNegativeButton(android.R.string.cancel, null)
                    .show()
                true
            }
        }
        if (preference.key == getString(R.string.set_key_performance_capture)) {
            preference.onPreferenceChangeListener =
                Preference.OnPreferenceChangeListener { _, newValue ->
                    PerfTimer.configure(newValue as? Boolean == true)
                    true
                }
        }
        if (preference.key == getString(R.string.set_key_export_startup_report)) {
            preference.setOnPreferenceClickListener {
                val report =
                    StartupPerformanceReport.render(
                        StartupPerformanceReport.CaptureContext(
                            authority = "user-started-settings-export"
                        )
                    )
                val shareIntent =
                    Intent(Intent.ACTION_SEND)
                        .setType("text/plain")
                        .putExtra(
                            Intent.EXTRA_SUBJECT,
                            getString(R.string.set_export_startup_report),
                        )
                        .putExtra(Intent.EXTRA_TEXT, report)
                try {
                    startActivity(
                        Intent.createChooser(
                            shareIntent,
                            getString(R.string.set_export_startup_report_chooser),
                        )
                    )
                } catch (e: ActivityNotFoundException) {
                    L.w(e, "No activity can share the startup performance report")
                }
                true
            }
        }
        if (preference.key == getString(R.string.set_key_cover_mode)) {
            L.d("Configuring cover mode setting")
            preference.onPreferenceChangeListener =
                Preference.OnPreferenceChangeListener { _, _ ->
                    L.d("Cover mode changed, reloading music")
                    musicModel.refresh()
                    true
                }
        }
        if (preference.key == getString(R.string.set_key_with_hidden)) {
            L.d("Configuring ignore hidden files setting")
            preference.onPreferenceChangeListener =
                Preference.OnPreferenceChangeListener { _, _ ->
                    L.d("Ignore hidden files setting changed, reloading music")
                    musicModel.refresh()
                    true
                }
        }
        if (preference.key == getString(R.string.set_key_ts18_system_source_filter)) {
            L.d("Configuring ts18 system source filter setting")
            preference.onPreferenceChangeListener =
                Preference.OnPreferenceChangeListener { _, _ ->
                    L.d("TS18 system source filter changed, reloading music")
                    musicModel.refresh()
                    true
                }
        }
        if (
            preference.key == getString(R.string.set_key_use_root_fs) &&
                preference is SwitchPreferenceCompat
        ) {
            preference.onPreferenceChangeListener =
                Preference.OnPreferenceChangeListener { _, newValue ->
                    val enabled = newValue as? Boolean == true
                    rootStateHolder.setUserEnabled(enabled)
                    if (!enabled) {
                        earlyPrestartSettings.enabled = false
                        findPreference<SwitchPreferenceCompat>(
                                getString(R.string.set_key_early_prestart)
                            )
                            ?.apply {
                                isChecked = false
                                isEnabled = false
                            }
                        refreshEarlyPrestartStatus()
                    } else {
                        probeRootFromSettings()
                    }
                    true
                }
        }
        if (preference.key == getString(R.string.set_key_root_fs_status)) {
            RootDiagnosticsHelper.setupRootFsStatus(
                requireContext(),
                preference,
                rootStateHolder,
                viewLifecycleOwner.lifecycleScope,
            )
        }
        if (
            preference.key == getString(R.string.set_key_early_prestart) &&
                preference is SwitchPreferenceCompat
        ) {
            setupEarlyPrestartSwitch(preference)
        }
        if (preference.key == getString(R.string.set_key_early_prestart_status)) {
            preference.summary = earlyPrestartSettings.summary()
            preference.setOnPreferenceClickListener {
                preference.summary = earlyPrestartSettings.summary()
                true
            }
        }
        if (preference.key == getString(R.string.set_key_ts18_source_repair_status)) {
            RootDiagnosticsHelper.setupTs18SourceRepairStatus(
                requireContext(),
                preference,
                viewLifecycleOwner.lifecycleScope,
            )
        }
    }

    private fun setupEarlyPrestartSwitch(preference: SwitchPreferenceCompat) {
        preference.isChecked = earlyPrestartSettings.enabled
        preference.isEnabled = rootStateHolder.isUserEnabled()
        preference.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue ->
                val enable = newValue as? Boolean == true
                if (!enable) {
                    earlyPrestartSettings.enabled = false
                    refreshEarlyPrestartStatus()
                    return@OnPreferenceChangeListener true
                }
                if (!rootStateHolder.isUserEnabled()) {
                    requireContext().showToast(R.string.set_early_prestart_requires_root)
                    return@OnPreferenceChangeListener false
                }

                // Keep the switch off until the bounded probe confirms the existing Magisk grant.
                preference.isEnabled = false
                viewLifecycleOwner.lifecycleScope.launch {
                    val state = withContext(Dispatchers.IO) { rootStateHolder.probeSync() }
                    val rootStillEnabled = rootStateHolder.isUserEnabled()
                    val available = rootStillEnabled && state == RootStateHolder.State.Available
                    earlyPrestartSettings.enabled = available
                    preference.isChecked = available
                    preference.isEnabled = rootStillEnabled
                    requireContext().showToast(rootStateMessage(state))
                    refreshRootStatus()
                    refreshEarlyPrestartStatus()
                }
                false
            }
    }

    private fun probeRootFromSettings() {
        viewLifecycleOwner.lifecycleScope.launch {
            val state = withContext(Dispatchers.IO) { rootStateHolder.probeSync() }
            if (!isAdded) return@launch
            requireContext().showToast(rootStateMessage(state))
            refreshRootStatus()
            findPreference<SwitchPreferenceCompat>(getString(R.string.set_key_early_prestart))
                ?.isEnabled = rootStateHolder.isUserEnabled()
            refreshEarlyPrestartStatus()
        }
    }

    private fun refreshRootStatus() {
        findPreference<Preference>(getString(R.string.set_key_root_fs_status))?.let { preference ->
            RootDiagnosticsHelper.setupRootFsStatus(
                requireContext(),
                preference,
                rootStateHolder,
                viewLifecycleOwner.lifecycleScope,
            )
        }
    }

    private fun refreshEarlyPrestartStatus() {
        findPreference<Preference>(getString(R.string.set_key_early_prestart_status))?.summary =
            earlyPrestartSettings.summary()
    }

    private fun rootStateMessage(state: RootStateHolder.State): Int =
        when (state) {
            RootStateHolder.State.Available -> R.string.recovery_root_granted
            RootStateHolder.State.Denied -> R.string.recovery_root_denied
            RootStateHolder.State.TimedOut -> R.string.recovery_root_timed_out
            RootStateHolder.State.Unknown,
            RootStateHolder.State.Unavailable,
            RootStateHolder.State.UnsupportedForVariant,
            RootStateHolder.State.DisabledByUser -> R.string.recovery_root_unavailable
        }
}
