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

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import coil3.ImageLoader
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.oxycblt.auxio.R
import org.oxycblt.auxio.headunit.root.RootStateHolder
import org.oxycblt.auxio.headunit.ts18.Ts18SourceRepairStatePolicy
import org.oxycblt.auxio.music.MusicViewModel
import org.oxycblt.auxio.settings.BasePreferenceFragment
import org.oxycblt.auxio.settings.ui.WrappedDialogPreference
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
    @Inject lateinit var imageLoader: ImageLoader

    override fun onOpenDialogPreference(preference: WrappedDialogPreference) {
        if (preference.key == getString(R.string.set_key_separators)) {
            L.d("Navigating to separator dialog")
            findNavController().navigateSafe(MusicPreferenceFragmentDirections.separatorsSettings())
        }
    }

    override fun onSetupPreference(preference: Preference) {
        if (preference.key == getString(R.string.set_key_cover_mode)) {
            L.d("Configuring cover mode setting")
            preference.onPreferenceChangeListener =
                Preference.OnPreferenceChangeListener { _, _ ->
                    L.d("Cover mode changed, reloading music")
                    musicModel.refresh()
                    true
                }
        }
        if (preference.key == getString(R.string.set_key_square_covers)) {
            L.d("Configuring square cover setting")
            preference.onPreferenceChangeListener =
                Preference.OnPreferenceChangeListener { _, _ ->
                    L.d("Cover mode changed, resetting image memory cache")
                    imageLoader.memoryCache?.clear()
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
            setupRootFsStatus(preference)
        }
        if (preference.key == getString(R.string.set_key_ts18_source_repair_status)) {
            setupTs18SourceRepairStatus(preference)
        }
    }

    private fun setupRootFsStatus(preference: Preference) {
        val status = rootStateHolder.stateSnapshot()
        preference.summary = rootStatusSummary(status)

        preference.setOnPreferenceClickListener {
            val prefs =
                androidx.preference.PreferenceManager.getDefaultSharedPreferences(requireContext())
            val enabled = prefs.getBoolean(getString(R.string.set_key_use_root_fs), false)
            if (!enabled) {
                preference.summary = getString(R.string.set_root_fs_status_disabled)
                return@setOnPreferenceClickListener true
            }

            viewLifecycleOwner.lifecycleScope.launch {
                val probed = withContext(Dispatchers.IO) { rootStateHolder.probeSync() }
                preference.summary = rootStatusSummary(probed)
            }
            true
        }
    }

    private fun rootStatusSummary(state: RootStateHolder.State): String =
        when (state) {
            RootStateHolder.State.DisabledByUser -> getString(R.string.set_root_fs_status_disabled)
            RootStateHolder.State.UnsupportedForVariant ->
                getString(R.string.set_root_fs_status_unsupported)
            RootStateHolder.State.Unknown -> getString(R.string.set_root_fs_status_unknown)
            RootStateHolder.State.Available -> getString(R.string.set_root_fs_status_available)
            RootStateHolder.State.Denied -> getString(R.string.set_root_fs_status_denied)
            RootStateHolder.State.TimedOut -> getString(R.string.set_root_fs_status_timed_out)
            RootStateHolder.State.Unavailable -> getString(R.string.set_root_fs_status_unavailable)
        }

    private fun setupTs18SourceRepairStatus(preference: Preference) {
        preference.summary =
            getString(
                R.string.set_ts18_source_repair_status_summary,
                getString(R.string.set_ts18_source_repair_checking),
                "",
            )
        refreshTs18SourceRepairStatus(preference)
        preference.setOnPreferenceClickListener {
            refreshTs18SourceRepairStatus(preference)
            true
        }
    }

    private fun refreshTs18SourceRepairStatus(preference: Preference) {
        viewLifecycleOwner.lifecycleScope.launch {
            val states =
                withContext(Dispatchers.IO) { Ts18SourceRepairStatePolicy.classifyDirectPaths() }
            val summaryKind = Ts18SourceRepairStatePolicy.summarise(states)
            val stateText = sourceRepairKindText(summaryKind)
            val details =
                states.joinToString(separator = "\n") { state ->
                    "${state.path}: ${sourceRepairKindText(state.kind)}"
                }
            preference.summary =
                getString(R.string.set_ts18_source_repair_status_summary, stateText, details)
        }
    }

    private fun sourceRepairKindText(kind: Ts18SourceRepairStatePolicy.Kind): String =
        when (kind) {
            Ts18SourceRepairStatePolicy.Kind.ALL_SOURCES_READY ->
                getString(R.string.set_ts18_source_repair_ready)
            Ts18SourceRepairStatePolicy.Kind.MOUNT_MISSING ->
                getString(R.string.set_ts18_source_repair_mount_missing)
            Ts18SourceRepairStatePolicy.Kind.DIRECT_PATH_INACCESSIBLE ->
                getString(R.string.set_ts18_source_repair_direct_inaccessible)
            Ts18SourceRepairStatePolicy.Kind.SAF_PERMISSION_MISSING ->
                getString(R.string.set_ts18_source_repair_saf_permission_missing)
            Ts18SourceRepairStatePolicy.Kind.SAF_PROVIDER_FAILURE ->
                getString(R.string.set_ts18_source_repair_saf_provider_failure)
            Ts18SourceRepairStatePolicy.Kind.SOURCE_EMPTY ->
                getString(R.string.set_ts18_source_repair_source_empty)
            Ts18SourceRepairStatePolicy.Kind.SOURCE_CONTAINS_NO_SUPPORTED_AUDIO ->
                getString(R.string.set_ts18_source_repair_no_audio)
            Ts18SourceRepairStatePolicy.Kind.MIXED_MULTIPLE_VOLUME_STATE ->
                getString(R.string.set_ts18_source_repair_mixed)
            Ts18SourceRepairStatePolicy.Kind.UNKNOWN_FAILURE ->
                getString(R.string.set_ts18_source_repair_unknown)
        }
}
