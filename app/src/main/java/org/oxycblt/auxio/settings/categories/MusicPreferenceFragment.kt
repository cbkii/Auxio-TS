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
import android.text.format.DateUtils
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.oxycblt.auxio.BuildConfig
import org.oxycblt.auxio.R
import org.oxycblt.auxio.headunit.root.RootStateHolder
import org.oxycblt.auxio.music.GeneratedPlaylistStatus
import org.oxycblt.auxio.music.MusicSettings
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
    @Inject lateinit var musicSettings: MusicSettings

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
        if (preference.key == getString(R.string.set_key_generated_playlists)) {
            preference.onPreferenceChangeListener =
                Preference.OnPreferenceChangeListener { _, newValue ->
                    findPreference<Preference>(
                            getString(R.string.set_key_refresh_generated_playlists)
                        )
                        ?.isEnabled = newValue as? Boolean == true
                    true
                }
        }
        if (preference.key == getString(R.string.set_key_refresh_generated_playlists)) {
            preference.isEnabled =
                preferenceManager.sharedPreferences?.getBoolean(
                    getString(R.string.set_key_generated_playlists),
                    false,
                ) == true
            preference.setOnPreferenceClickListener {
                musicModel.refreshGeneratedPlaylists()
                true
            }
        }
        if (preference.key == getString(R.string.set_key_generated_playlists_status)) {
            viewLifecycleOwner.lifecycleScope.launch {
                musicModel.generatedPlaylistStatus.collect { status ->
                    preference.summary =
                        getString(
                            when (status) {
                                GeneratedPlaylistStatus.OFF ->
                                    R.string.set_generated_playlists_status_off
                                GeneratedPlaylistStatus.WAITING_FOR_LIBRARY ->
                                    R.string.set_generated_playlists_status_waiting
                                GeneratedPlaylistStatus.GENERATING ->
                                    R.string.set_generated_playlists_status_generating
                                GeneratedPlaylistStatus.UP_TO_DATE ->
                                    R.string.set_generated_playlists_status_up_to_date
                                GeneratedPlaylistStatus.FAILED ->
                                    R.string.set_generated_playlists_status_failed
                            }
                        )
                }
            }
        }
        if (preference.key == getString(R.string.set_key_export_startup_report)) {
            preference.setOnPreferenceClickListener {
                val report =
                    StartupPerformanceReport.render(
                        StartupPerformanceReport.CaptureContext(
                            authority = "user-started-settings-export",
                            sourceState =
                                "mode=${musicSettings.locationMode};" +
                                    "generation=${musicSettings.sourceConfigurationGeneration};" +
                                    "checkpoint=" +
                                    "${musicSettings.sourceConfigurationCheckpoint?.diagnosticSummary()};" +
                                    "sources=${configuredSourcesDiagnosticSummary()}",
                            commit = BuildConfig.BUILD_COMMIT,
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
        if (preference.key == getString(R.string.set_key_use_root_fs)) {
            preference.onPreferenceChangeListener =
                Preference.OnPreferenceChangeListener { _, newValue ->
                    val enabled = newValue as? Boolean == true
                    rootStateHolder.setUserEnabled(enabled)
                    if (enabled) {
                        // This is the explicit consent action. Request the bounded Magisk grant
                        // here
                        // instead of surprising the user later from an ordinary source picker.
                        viewLifecycleOwner.lifecycleScope.launch {
                            val probed = withContext(Dispatchers.IO) { rootStateHolder.probeSync() }
                            findPreference<Preference>(getString(R.string.set_key_root_fs_status))
                                ?.summary =
                                RootDiagnosticsHelper.rootStatusSummary(requireContext(), probed)
                        }
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
        if (preference.key == getString(R.string.set_key_ts18_source_repair_status)) {
            updateSourceCheckpointSummary(preference)
            viewLifecycleOwner.lifecycleScope.launch {
                musicModel.indexingState.collect { updateSourceCheckpointSummary(preference) }
            }
        }
        if (preference.key == getString(R.string.set_key_retry_source_setup)) {
            preference.setOnPreferenceClickListener {
                musicModel.retrySourceConfiguration()
                true
            }
        }
    }

    private fun updateSourceCheckpointSummary(preference: Preference) {
        val checkpoint = musicSettings.sourceConfigurationCheckpoint
        val none = getString(R.string.set_source_checkpoint_none)
        val unavailable =
            musicSettings.configuredSourceSpecs
                .filter {
                    it.accessState !=
                        org.oxycblt.auxio.music.ConfiguredSourceSpec.AccessState.AVAILABLE
                }
                .joinToString { "${it.displayPath} (${it.accessState.name.lowercase()})" }
                .ifBlank { none }
        val checkpointText =
            checkpoint?.let { "${it.state.name.lowercase()} generation ${it.generation}" } ?: none
        findPreference<Preference>(getString(R.string.set_key_retry_source_setup))?.isEnabled =
            checkpoint?.let {
                it.state == org.oxycblt.auxio.music.SourceConfigurationCheckpoint.State.RUNNING ||
                    it.canClaim(org.oxycblt.auxio.music.SourceScanClaimReason.USER_RETRY)
            } == true
        val lastAttempt =
            checkpoint
                ?.lastAttemptAtMs
                ?.let {
                    DateUtils.getRelativeTimeSpanString(
                        it,
                        System.currentTimeMillis(),
                        DateUtils.SECOND_IN_MILLIS,
                    )
                }
                ?.toString() ?: none
        preference.summary =
            getString(
                R.string.set_source_checkpoint_summary,
                musicSettings.locationMode.name,
                musicSettings.configuredSourceCount,
                checkpointText,
                unavailable,
                lastAttempt,
                checkpoint?.lastOutcome ?: none,
            )
    }

    private fun configuredSourcesDiagnosticSummary(): String {
        val sources = musicSettings.configuredSourceSpecs
        val visible =
            sources.take(MAX_DIAGNOSTIC_SOURCES).map {
                "${it.sourceKey.take(MAX_DIAGNOSTIC_SOURCE_LENGTH)}:" +
                    "${it.accessState}:${it.origin}:${it.traversalScope}:" +
                    it.displayPath.take(MAX_DIAGNOSTIC_SOURCE_LENGTH)
            }
        val omitted = (sources.size - visible.size).coerceAtLeast(0)
        return if (omitted == 0) visible.toString() else "$visible;omitted=$omitted"
    }

    private companion object {
        const val MAX_DIAGNOSTIC_SOURCES = 8
        const val MAX_DIAGNOSTIC_SOURCE_LENGTH = 160
    }
}
