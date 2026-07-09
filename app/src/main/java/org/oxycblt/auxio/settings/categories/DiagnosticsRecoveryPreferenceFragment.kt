/*
 * Copyright (c) 2026 Auxio Project
 * DiagnosticsRecoveryPreferenceFragment.kt is part of Auxio.
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

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.preference.Preference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.oxycblt.auxio.BuildConfig
import org.oxycblt.auxio.R
import org.oxycblt.auxio.headunit.compat.HeadUnitCompatManager
import org.oxycblt.auxio.headunit.compat.NativePrivateIntegrationStatus
import org.oxycblt.auxio.headunit.root.RootStateHolder
import org.oxycblt.auxio.headunit.root.dofun.Ts18DofunIntegrationResolver
import org.oxycblt.auxio.headunit.ts18.Ts18SourceRepairStatePolicy
import org.oxycblt.auxio.settings.BasePreferenceFragment
import org.oxycblt.auxio.ui.UISettings

@AndroidEntryPoint
class DiagnosticsRecoveryPreferenceFragment :
    BasePreferenceFragment(R.xml.preferences_diagnostics) {

    @javax.inject.Inject lateinit var rootStateHolder: RootStateHolder
    @javax.inject.Inject lateinit var uiSettings: UISettings

    private lateinit var resolver: Ts18DofunIntegrationResolver
    private var lastReportStr: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resolver = Ts18DofunIntegrationResolver(requireContext(), rootStateHolder)
    }

    override fun onSetupPreference(preference: Preference) {
        when (preference.key) {
            getString(R.string.set_head_unit_compat_status) -> setupCompatStatus(preference)
            getString(R.string.set_key_ts18_source_repair_status) ->
                setupTs18SourceRepairStatus(preference)
            getString(R.string.set_key_root_fs_status) -> setupRootFsStatus(preference)
            getString(R.string.set_key_ts18_fast_resume_status) ->
                setupTs18FastResumeStatus(preference)
            "car_overlay_status" -> setupCarOverlayStatus(preference)
        }

        if (preference.key == "run_check") {
            preference.setOnPreferenceClickListener {
                runCheck()
                true
            }
        }

        if (preference.key == "export_report") {
            preference.setOnPreferenceClickListener {
                exportReport()
                true
            }
        }

        if (preference.key == "test_stock_disable") {
            preference.setOnPreferenceClickListener {
                showDisableStockConfirmation()
                true
            }
        }

        if (preference.key == "restore_stock") {
            preference.setOnPreferenceClickListener {
                restoreStock()
                true
            }
        }

        updateUiState()
    }

    private fun setupCompatStatus(preference: Preference) {
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
                    getString(R.string.set_head_unit_compat_native_not_enabled_requires_validation)
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

    private fun setupTs18FastResumeStatus(preference: Preference) {
        preference.summary = getString(R.string.set_ts18_fast_resume_status_desc)
        preference.setOnPreferenceClickListener(null)
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

    private fun setupCarOverlayStatus(preference: Preference) {
        if (!BuildConfig.TOPWAY_COMPAT_FLAVOR) {
            preference.isVisible = false
            return
        }
        try {
            val settingsClass = Class.forName("org.oxycblt.auxio.car.overlay.CarOverlaySettings")
            val instance = settingsClass.getDeclaredField("INSTANCE").get(null)
            val isEnabledMethod = settingsClass.getMethod("isEnabled", Context::class.java)
            val enabled = isEnabledMethod.invoke(instance, requireContext()) as Boolean
            preference.summary = if (enabled) "Granted" else "Not granted"
        } catch (e: ReflectiveOperationException) {
            preference.isVisible = false
        }
    }

    private fun statusSummary(status: Boolean): String =
        if (status) getString(R.string.lbl_enabled) else getString(R.string.lbl_disabled)

    private fun runCheck() {
        val pref = findPreference<Preference>("run_check")
        pref?.summary = "Running..."
        pref?.isEnabled = false

        viewLifecycleOwner.lifecycleScope.launch {
            val report = resolver.runIntegrationCheck()

            val sb = StringBuilder()
            sb.appendLine("Root state: ${report.rootState}")
            sb.appendLine("\nInstalled packages:")
            report.installedPackages.forEach { sb.appendLine("- $it") }

            sb.appendLine("\nProbes:")
            report.probeResults.forEach { (probe, result) ->
                sb.appendLine("--- ${probe.name} ---")
                sb.appendLine(result)
                sb.appendLine()
            }

            sb.appendLine("\nDetected Path: ${report.detectedPath}")
            sb.appendLine("Recommended Step: ${report.recommendedStep}")
            sb.appendLine("\n${report.bootClassification}")

            lastReportStr = sb.toString()

            pref?.summary =
                "Check complete. Path: ${report.detectedPath}. Root: ${report.rootState}. Packages: ${report.installedPackages.joinToString()}"
            pref?.isEnabled = true
            findPreference<Preference>("export_report")?.isEnabled = true
            updateUiState()
        }
    }

    private fun exportReport() {
        val report = lastReportStr ?: return
        val clipboard =
            requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("TS18 DoFun Report", report)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(requireContext(), "Report copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    private fun showDisableStockConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle("Disable stock com.tw.music?")
            .setMessage(
                "This will use root to disable the stock com.tw.music package for user 0. This is reversible. Do you want to continue?"
            )
            .setPositiveButton("Disable") { _, _ ->
                viewLifecycleOwner.lifecycleScope.launch {
                    val success = resolver.testStockSelectionDisabledUser0()
                    val msg =
                        if (success) "Disabled stock com.tw.music successfully"
                        else "Failed to disable stock com.tw.music"
                    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun restoreStock() {
        viewLifecycleOwner.lifecycleScope.launch {
            val success = resolver.restoreStockSelectionDisabledUser0()
            val msg =
                if (success) "Enabled stock com.tw.music successfully"
                else "Failed to enable stock com.tw.music"
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUiState() {
        val isRootAvailable = rootStateHolder.stateSnapshot() == RootStateHolder.State.Available
        findPreference<Preference>("test_stock_disable")?.isVisible = isRootAvailable
        findPreference<Preference>("restore_stock")?.isVisible = isRootAvailable
        findPreference<Preference>("export_report")?.isEnabled = lastReportStr != null
    }
}
