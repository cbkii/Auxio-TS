/*
 * Copyright (c) 2026 Auxio Project
 * RootDiagnosticsHelper.kt is part of Auxio.
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

package org.oxycblt.auxio.settings

import android.content.Context
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.preference.Preference
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.oxycblt.auxio.R
import org.oxycblt.auxio.headunit.root.RootStateHolder
import org.oxycblt.auxio.headunit.ts18.Ts18SourceRepairStatePolicy

object RootDiagnosticsHelper {

    fun setupRootFsStatus(
        context: Context,
        preference: Preference,
        rootStateHolder: RootStateHolder,
        lifecycleScope: LifecycleCoroutineScope,
    ) {
        val status = rootStateHolder.stateSnapshot()
        preference.summary = rootStatusSummary(context, status)

        preference.setOnPreferenceClickListener {
            val prefs = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
            val enabled = prefs.getBoolean(context.getString(R.string.set_key_use_root_fs), false)
            if (!enabled) {
                preference.summary = context.getString(R.string.set_root_fs_status_disabled)
                return@setOnPreferenceClickListener true
            }

            lifecycleScope.launch {
                preference.summary =
                    try {
                        val probed = withContext(Dispatchers.IO) { rootStateHolder.probeSync() }
                        rootStatusSummary(context, probed)
                    } catch (e: Exception) {
                        if (e is CancellationException) throw e
                        context.getString(R.string.set_root_fs_status_unavailable)
                    }
            }
            true
        }
    }

    private fun rootStatusSummary(context: Context, state: RootStateHolder.State): String =
        when (state) {
            RootStateHolder.State.DisabledByUser ->
                context.getString(R.string.set_root_fs_status_disabled)
            RootStateHolder.State.UnsupportedForVariant ->
                context.getString(R.string.set_root_fs_status_unsupported)
            RootStateHolder.State.Unknown -> context.getString(R.string.set_root_fs_status_unknown)
            RootStateHolder.State.Available ->
                context.getString(R.string.set_root_fs_status_available)
            RootStateHolder.State.Denied -> context.getString(R.string.set_root_fs_status_denied)
            RootStateHolder.State.TimedOut ->
                context.getString(R.string.set_root_fs_status_timed_out)
            RootStateHolder.State.Unavailable ->
                context.getString(R.string.set_root_fs_status_unavailable)
        }

    fun setupTs18SourceRepairStatus(
        context: Context,
        preference: Preference,
        lifecycleScope: LifecycleCoroutineScope,
    ) {
        preference.summary =
            context.getString(
                R.string.set_ts18_source_repair_status_summary,
                context.getString(R.string.set_ts18_source_repair_checking),
                "",
            )
        refreshTs18SourceRepairStatus(context, preference, lifecycleScope)
        preference.setOnPreferenceClickListener {
            refreshTs18SourceRepairStatus(context, preference, lifecycleScope)
            true
        }
    }

    private fun refreshTs18SourceRepairStatus(
        context: Context,
        preference: Preference,
        lifecycleScope: LifecycleCoroutineScope,
    ) {
        lifecycleScope.launch {
            val summary =
                try {
                    val states =
                        withContext(Dispatchers.IO) {
                            Ts18SourceRepairStatePolicy.classifyDirectPaths()
                        }
                    val summaryKind = Ts18SourceRepairStatePolicy.summarise(states)
                    val stateText = sourceRepairKindText(context, summaryKind)
                    val details =
                        states.joinToString(separator = "\n") { state ->
                            "${state.path}: ${sourceRepairKindText(context, state.kind)}"
                        }
                    context.getString(
                        R.string.set_ts18_source_repair_status_summary,
                        stateText,
                        details,
                    )
                } catch (e: Exception) {
                    if (e is CancellationException) throw e
                    context.getString(
                        R.string.set_ts18_source_repair_status_summary,
                        context.getString(R.string.set_ts18_source_repair_unknown),
                        "",
                    )
                }
            preference.summary =
                summary
        }
    }

    private fun sourceRepairKindText(
        context: Context,
        kind: Ts18SourceRepairStatePolicy.Kind,
    ): String =
        when (kind) {
            Ts18SourceRepairStatePolicy.Kind.ALL_SOURCES_READY ->
                context.getString(R.string.set_ts18_source_repair_ready)
            Ts18SourceRepairStatePolicy.Kind.MOUNT_MISSING ->
                context.getString(R.string.set_ts18_source_repair_mount_missing)
            Ts18SourceRepairStatePolicy.Kind.DIRECT_PATH_INACCESSIBLE ->
                context.getString(R.string.set_ts18_source_repair_direct_inaccessible)
            Ts18SourceRepairStatePolicy.Kind.SAF_PERMISSION_MISSING ->
                context.getString(R.string.set_ts18_source_repair_saf_permission_missing)
            Ts18SourceRepairStatePolicy.Kind.SAF_PROVIDER_FAILURE ->
                context.getString(R.string.set_ts18_source_repair_saf_provider_failure)
            Ts18SourceRepairStatePolicy.Kind.SOURCE_EMPTY ->
                context.getString(R.string.set_ts18_source_repair_source_empty)
            Ts18SourceRepairStatePolicy.Kind.SOURCE_CONTAINS_NO_SUPPORTED_AUDIO ->
                context.getString(R.string.set_ts18_source_repair_no_audio)
            Ts18SourceRepairStatePolicy.Kind.MIXED_MULTIPLE_VOLUME_STATE ->
                context.getString(R.string.set_ts18_source_repair_mixed)
            Ts18SourceRepairStatePolicy.Kind.UNKNOWN_FAILURE ->
                context.getString(R.string.set_ts18_source_repair_unknown)
        }
}
