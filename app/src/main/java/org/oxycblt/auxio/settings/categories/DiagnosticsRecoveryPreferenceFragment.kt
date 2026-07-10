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
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.oxycblt.auxio.BuildConfig
import org.oxycblt.auxio.R
import org.oxycblt.auxio.headunit.compat.HeadUnitCompatManager
import org.oxycblt.auxio.headunit.compat.NativePrivateIntegrationStatus
import org.oxycblt.auxio.headunit.root.RootStateHolder
import org.oxycblt.auxio.headunit.root.dofun.Ts18DofunIntegrationResolver
import org.oxycblt.auxio.settings.BasePreferenceFragment
import org.oxycblt.auxio.settings.RootDiagnosticsHelper
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
                RootDiagnosticsHelper.setupTs18SourceRepairStatus(
                    requireContext(),
                    preference,
                    viewLifecycleOwner.lifecycleScope,
                )
            getString(R.string.set_key_root_fs_status) ->
                RootDiagnosticsHelper.setupRootFsStatus(
                    requireContext(),
                    preference,
                    rootStateHolder,
                    viewLifecycleOwner.lifecycleScope,
                )
            getString(R.string.set_key_ts18_fast_resume_status) ->
                setupTs18FastResumeStatus(preference)
            getString(R.string.set_key_car_overlay_status) -> setupCarOverlayStatus(preference)
        }

        if (preference.key == getString(R.string.set_key_diagnostics_run_check)) {
            preference.setOnPreferenceClickListener {
                runCheck()
                true
            }
        }

        if (preference.key == getString(R.string.set_key_diagnostics_export_report)) {
            preference.setOnPreferenceClickListener {
                exportReport()
                true
            }
        }

        if (preference.key == getString(R.string.set_key_diagnostics_test_stock_disable)) {
            preference.setOnPreferenceClickListener {
                showDisableStockConfirmation()
                true
            }
        }

        if (preference.key == getString(R.string.set_key_diagnostics_restore_stock)) {
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

    private fun setupCarOverlayStatus(preference: Preference) {
        if (!BuildConfig.TOPWAY_COMPAT_FLAVOR) {
            preference.isVisible = false
            return
        }
        val enabled = android.provider.Settings.canDrawOverlays(requireContext())
        preference.summary =
            if (enabled) getString(R.string.set_diagnostics_granted)
            else getString(R.string.set_diagnostics_not_granted)
    }

    private fun statusSummary(status: Boolean): String =
        if (status) getString(R.string.lbl_enabled) else getString(R.string.lbl_disabled)

    private fun runCheck() {
        val pref = findPreference<Preference>(getString(R.string.set_key_diagnostics_run_check))
        pref?.summary = getString(R.string.set_diagnostics_running)
        pref?.isEnabled = false

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val report = withContext(Dispatchers.IO) { resolver.runIntegrationCheck() }
                val ctx = context ?: return@launch

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
                    ctx.getString(
                        R.string.set_diagnostics_check_complete_summary,
                        report.detectedPath,
                        report.rootState.toString(),
                        report.installedPackages.joinToString(),
                    )
                findPreference<Preference>(
                        ctx.getString(R.string.set_key_diagnostics_export_report)
                    )
                    ?.isEnabled = true
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                lastReportStr = null
                val ctx = context ?: return@launch
                pref?.summary = ctx.getString(R.string.set_diagnostics_check_failed)
                Toast.makeText(
                        ctx,
                        ctx.getString(R.string.set_diagnostics_check_failed),
                        Toast.LENGTH_SHORT,
                    )
                    .show()
            } finally {
                if (isAdded) {
                    pref?.isEnabled = true
                    updateUiState()
                }
            }
        }
    }

    private fun exportReport() {
        val report = lastReportStr ?: return
        val clipboard =
            requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip =
            ClipData.newPlainText(getString(R.string.set_diagnostics_clipboard_label), report)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(
                requireContext(),
                getString(R.string.set_diagnostics_copied),
                Toast.LENGTH_SHORT,
            )
            .show()
    }

    private fun showDisableStockConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.set_diagnostics_disable_title))
            .setMessage(getString(R.string.set_diagnostics_disable_message))
            .setPositiveButton(getString(R.string.set_diagnostics_disable_btn)) { _, _ ->
                val pref =
                    findPreference<Preference>(
                        getString(R.string.set_key_diagnostics_test_stock_disable)
                    )
                pref?.isEnabled = false
                viewLifecycleOwner.lifecycleScope.launch {
                    try {
                        val success =
                            withContext(Dispatchers.IO) {
                                resolver.testStockSelectionDisabledUser0()
                            }
                        val ctx = context ?: return@launch
                        val msg =
                            if (success) {
                                ctx.getString(R.string.set_diagnostics_disable_success)
                            } else {
                                ctx.getString(R.string.set_diagnostics_disable_failed)
                            }
                        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        if (e is CancellationException) throw e
                        val ctx = context ?: return@launch
                        Toast.makeText(
                                ctx,
                                ctx.getString(R.string.set_diagnostics_disable_failed),
                                Toast.LENGTH_SHORT,
                            )
                            .show()
                    } finally {
                        if (isAdded) {
                            pref?.isEnabled = true
                            updateUiState()
                        }
                    }
                }
            }
            .setNegativeButton(getString(R.string.set_diagnostics_cancel_btn), null)
            .show()
    }

    private fun restoreStock() {
        val pref = findPreference<Preference>(getString(R.string.set_key_diagnostics_restore_stock))
        pref?.isEnabled = false
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val success =
                    withContext(Dispatchers.IO) { resolver.restoreStockSelectionDisabledUser0() }
                val ctx = context ?: return@launch
                val msg =
                    if (success) {
                        ctx.getString(R.string.set_diagnostics_restore_success)
                    } else {
                        ctx.getString(R.string.set_diagnostics_restore_failed)
                    }
                Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                val ctx = context ?: return@launch
                Toast.makeText(
                        ctx,
                        ctx.getString(R.string.set_diagnostics_restore_failed),
                        Toast.LENGTH_SHORT,
                    )
                    .show()
            } finally {
                if (isAdded) {
                    pref?.isEnabled = true
                    updateUiState()
                }
            }
        }
    }

    private fun updateUiState() {
        val isRootAvailable = rootStateHolder.stateSnapshot() == RootStateHolder.State.Available
        findPreference<Preference>(getString(R.string.set_key_diagnostics_test_stock_disable))
            ?.isVisible = isRootAvailable
        findPreference<Preference>(getString(R.string.set_key_diagnostics_restore_stock))
            ?.isVisible = isRootAvailable
        findPreference<Preference>(getString(R.string.set_key_diagnostics_export_report))
            ?.isEnabled = lastReportStr != null
    }
}
