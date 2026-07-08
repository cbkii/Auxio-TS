/*
 * Copyright (c) 2026 Auxio Project
 * Ts18IntegrationResolverPreferenceFragment.kt is part of Auxio.
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
import androidx.preference.PreferenceFragmentCompat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.oxycblt.auxio.R
import org.oxycblt.auxio.headunit.root.RootStateHolder
import org.oxycblt.auxio.headunit.root.dofun.Ts18DofunIntegrationResolver

@AndroidEntryPoint
class Ts18IntegrationResolverPreferenceFragment : PreferenceFragmentCompat() {

    @javax.inject.Inject lateinit var rootStateHolder: RootStateHolder

    private lateinit var resolver: Ts18DofunIntegrationResolver
    private var lastReportStr: String? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences_ts18_integration_resolver, rootKey)
        resolver = Ts18DofunIntegrationResolver(requireContext(), rootStateHolder)

        findPreference<Preference>("run_check")?.setOnPreferenceClickListener {
            runCheck()
            true
        }

        findPreference<Preference>("export_report")?.setOnPreferenceClickListener {
            exportReport()
            true
        }

        findPreference<Preference>("test_stock_disable")?.setOnPreferenceClickListener {
            showDisableStockConfirmation()
            true
        }

        findPreference<Preference>("restore_stock")?.setOnPreferenceClickListener {
            restoreStock()
            true
        }

        updateUiState()
    }

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
