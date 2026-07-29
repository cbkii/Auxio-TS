/*
 * Copyright (c) 2026 Auxio Project
 * TemporaryDeviceValidationLab.kt is part of Auxio.
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

package org.oxycblt.auxio.diagnostics

import android.content.Intent
import android.os.Process
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.oxycblt.auxio.BuildConfig
import org.oxycblt.auxio.MainActivity
import org.oxycblt.auxio.car.overlay.CarFloatingControlsService
import org.oxycblt.auxio.car.overlay.CarOverlayActivity
import org.oxycblt.auxio.headunit.root.RootStateHolder
import org.oxycblt.auxio.headunit.root.dofun.Ts18DofunIntegrationResolver
import org.oxycblt.auxio.music.MusicSettings
import org.oxycblt.auxio.settings.categories.DiagnosticsRecoveryPreferenceFragment
import org.oxycblt.auxio.util.PerfTimer
import timber.log.Timber

/**
 * Debug-only TS18 evidence controls. Remove individual controls once their physical acceptance gate
 * is closed; this entire class is intentionally absent from release variants.
 */
object TemporaryDeviceValidationLab {
    @JvmStatic
    fun install(
        fragment: DiagnosticsRecoveryPreferenceFragment,
        journal: DiagnosticJournal,
        musicSettings: MusicSettings,
        rootStateHolder: RootStateHolder,
    ) {
        val context = fragment.requireContext()
        val applicationContext = context.applicationContext
        val screen = fragment.preferenceScreen ?: return
        val category =
            PreferenceCategory(context).apply {
                key = "temporary_device_validation_lab"
                title = "TEMPORARY — Device Validation Lab"
                summary =
                    "Debug builds only. Capture evidence, then remove each probe when its feature is confirmed."
            }
        screen.addPreference(category)

        category.add(
            context,
            "Start a fresh diagnostic campaign",
            "Closes the process session and starts a named, persisted device-test campaign.",
        ) {
            journal.endSession()
            val id = "campaign-${System.currentTimeMillis()}-${Process.myPid()}"
            journal.startSession(id)
            Timber.tag("AuxioCapture")
                .i(
                    "AUXIO_TS_CAPTURE_CANARY session=%s applicationId=%s version=%s(%d) variant=%s%s commit=%s",
                    id,
                    BuildConfig.APPLICATION_ID,
                    BuildConfig.VERSION_NAME,
                    BuildConfig.VERSION_CODE,
                    BuildConfig.FLAVOR,
                    BuildConfig.BUILD_TYPE,
                    BuildConfig.BUILD_COMMIT,
                )
            toast(context, "Campaign started: $id")
        }
        category.add(
            context,
            "End campaign and resume process journal",
            "Writes a terminal summary before continuing general process diagnostics.",
        ) {
            val ended = journal.activeSessionId
            journal.endSession()
            journal.startSession("process-resumed-${System.currentTimeMillis()}-${Process.myPid()}")
            toast(context, "Ended: ${ended ?: "no active campaign"}")
        }
        category.add(
            context,
            "Snapshot app/package/variant identity",
            "Records the installed version, UID, APK path, build variant, and commit.",
        ) {
            journal.log(
                DiagnosticJournal.CAT_SYSTEM,
                "Identity snapshot",
                "applicationId=${BuildConfig.APPLICATION_ID} version=${BuildConfig.VERSION_NAME}" +
                    "(${BuildConfig.VERSION_CODE}) variant=${BuildConfig.FLAVOR}${BuildConfig.BUILD_TYPE} " +
                    "commit=${BuildConfig.BUILD_COMMIT} uid=${Process.myUid()} " +
                    "apk=${context.applicationInfo.sourceDir}",
            )
            toast(context, "Identity snapshot recorded")
        }
        category.add(
            context,
            "Snapshot source and indexing checkpoint",
            "Records configured roots, access state, generation, checkpoint, and last scan outcome state.",
        ) {
            journal.log(
                DiagnosticJournal.CAT_INDEXING,
                "Source checkpoint snapshot",
                "mode=${musicSettings.locationMode} generation=${musicSettings.sourceConfigurationGeneration} " +
                    "checkpoint=${musicSettings.sourceConfigurationCheckpoint} " +
                    "libraryState=${musicSettings.libraryState} lastScanFailed=${musicSettings.lastScanFailed} " +
                    "sources=${musicSettings.configuredSourceSpecs}",
            )
            toast(context, "Source snapshot recorded")
        }
        category.add(
            context,
            "Capture own JVM thread snapshot",
            "Records thread counts immediately; the next saved bundle contains bounded stacks.",
        ) {
            val states = Thread.getAllStackTraces().keys.groupingBy { it.state }.eachCount()
            journal.log(
                DiagnosticJournal.CAT_SYSTEM,
                "JVM thread snapshot requested",
                "threadCount=${states.values.sum()} states=$states",
            )
            toast(context, "Thread marker recorded; save a bundle now")
        }
        category.add(
            context,
            "Launch full MusicActivity",
            "Explicitly opens the full player, independent of floating-only launcher routing.",
        ) {
            context.startActivity(
                Intent(context, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }
        category.add(
            context,
            "Launch Floating Controls only",
            "Explicitly launches only the overlay entry point.",
        ) {
            context.startActivity(
                Intent(context, CarOverlayActivity::class.java)
                    .setAction(CarOverlayActivity.ACTION_LAUNCH_FLOATING_CONTROLS)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }
        category.add(
            context,
            "Stop Floating Controls",
            "Stops the overlay service without opening the full player.",
        ) {
            CarFloatingControlsService.stop(context)
            journal.log(DiagnosticJournal.CAT_OVERLAY, "Debug lab stop requested")
        }
        category.add(
            context,
            "Run root/SAF/DoFun health probes",
            "Runs the existing bounded allowlisted integration checks and records their output.",
        ) {
            fragment.lifecycleScope.launch {
                val resolver = Ts18DofunIntegrationResolver(applicationContext, rootStateHolder)
                try {
                    val report = withContext(Dispatchers.IO) { resolver.runIntegrationCheck() }
                    journal.log(
                        DiagnosticJournal.CAT_SYSTEM,
                        "Root SAF DoFun health probe",
                        "root=${report.rootState} packages=${report.installedPackages} " +
                            "path=${report.detectedPath} recommendation=${report.recommendedStep} " +
                            "probes=${report.probeResults}",
                        result = "COMPLETED",
                    )
                    if (!fragment.isAdded) return@launch
                    val currentContext = fragment.context ?: return@launch
                    toast(currentContext, "Health probes completed")
                } catch (error: CancellationException) {
                    throw error
                } catch (error: Exception) {
                    journal.log(
                        DiagnosticJournal.CAT_SYSTEM,
                        "Root SAF DoFun health probe",
                        error.toString(),
                        result = "FAILED",
                    )
                    if (!fragment.isAdded) return@launch
                    val currentContext = fragment.context ?: return@launch
                    toast(currentContext, "Health probes failed: ${error.message}")
                }
            }
        }
        category.add(
            context,
            "Save validated diagnostic ZIP now",
            "Includes identity, sources, persisted journal, timings, stacks, and checksums.",
        ) {
            fragment.lifecycleScope.launch {
                val message =
                    try {
                        val file =
                            withContext(Dispatchers.IO) {
                                DiagnosticBundleExporter.create(
                                    applicationContext,
                                    journal,
                                    musicSettings,
                                )
                            }
                        "Saved: ${file.absolutePath}"
                    } catch (error: CancellationException) {
                        throw error
                    } catch (error: Exception) {
                        "Save failed: ${error.message}"
                    }
                if (!fragment.isAdded) return@launch
                val currentContext = fragment.context ?: return@launch
                toast(currentContext, message)
            }
        }
        category.add(
            context,
            "Clear in-memory performance ring",
            "Starts a clean bounded performance interval; persisted journal data is retained.",
        ) {
            PerfTimer.clear()
            PerfTimer.point("debug-lab-performance-capture-start")
            journal.log(DiagnosticJournal.CAT_SYSTEM, "Performance interval reset")
            toast(context, "Performance interval reset")
        }
    }

    private fun PreferenceCategory.add(
        context: android.content.Context,
        titleText: String,
        summaryText: String,
        action: () -> Unit,
    ) {
        addPreference(
            Preference(context).apply {
                title = titleText
                summary = summaryText
                setOnPreferenceClickListener {
                    action()
                    true
                }
            }
        )
    }

    private fun toast(context: android.content.Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}
