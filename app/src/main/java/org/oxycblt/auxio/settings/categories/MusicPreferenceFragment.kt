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
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import org.oxycblt.auxio.R
import org.oxycblt.auxio.headunit.root.RootStateHolder
import org.oxycblt.auxio.music.MusicViewModel
import org.oxycblt.auxio.settings.BasePreferenceFragment
import org.oxycblt.auxio.settings.RootDiagnosticsHelper
import org.oxycblt.auxio.settings.ui.WrappedDialogPreference
import org.oxycblt.auxio.util.PerfTimer
import org.oxycblt.auxio.util.StartupPerformanceReport
import org.oxycblt.auxio.util.navigateSafe
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
        if (preference.key == getString(R.string.set_key_root_fs_status)) {
            RootDiagnosticsHelper.setupRootFsStatus(
                requireContext(),
                preference,
                rootStateHolder,
                viewLifecycleOwner.lifecycleScope,
            )
        }
        if (preference.key == getString(R.string.set_key_ts18_source_repair_status)) {
            RootDiagnosticsHelper.setupTs18SourceRepairStatus(
                requireContext(),
                preference,
                viewLifecycleOwner.lifecycleScope,
            )
        }
    }
}
