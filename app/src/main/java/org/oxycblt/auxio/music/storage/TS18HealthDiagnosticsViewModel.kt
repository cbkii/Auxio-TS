/*
 * Copyright (c) 2026 Auxio Project
 * TS18HealthDiagnosticsViewModel.kt is part of Auxio.
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

package org.oxycblt.auxio.music.storage

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.oxycblt.auxio.BuildConfig
import org.oxycblt.auxio.headunit.topway.TopwaySourcePolicy
import org.oxycblt.auxio.music.MusicSettings
import org.oxycblt.auxio.music.locations.LocationMode
import org.oxycblt.auxio.playback.state.PlaybackStateManager
import org.oxycblt.musikr.fs.Location

@HiltViewModel
class TS18HealthDiagnosticsViewModel
@Inject
constructor(
    private val musicSettings: MusicSettings,
    private val playbackManager: PlaybackStateManager,
) : ViewModel() {

    private val _reportState = MutableStateFlow<String?>(null)
    val reportState: StateFlow<String?> = _reportState.asStateFlow()

    private val _noisyPaths = MutableStateFlow<List<String>>(emptyList())
    val noisyPaths: StateFlow<List<String>> = _noisyPaths.asStateFlow()

    private val _saveResult = MutableStateFlow<String?>(null)
    val saveResult: StateFlow<String?> = _saveResult.asStateFlow()

    fun generateReport(context: Context) {
        viewModelScope.launch {
            val report = withContext(Dispatchers.IO) { buildReport(context) }
            _reportState.value = report
        }
    }

    fun excludePath(context: Context, path: String) {
        val location =
            Location.Unopened.from(context, android.net.Uri.parse("file://$path")) ?: return
        if (musicSettings.locationMode == LocationMode.SAF) {
            val currentQuery = musicSettings.safQuery
            musicSettings.safQuery = currentQuery.copy(exclude = currentQuery.exclude + location)
        } else {
            val currentQuery = musicSettings.mediaStoreQuery
            musicSettings.mediaStoreQuery =
                currentQuery.copy(filtered = currentQuery.filtered + location)
        }
    }

    fun getWritableDestinations(context: Context): List<File> {
        val dests = mutableListOf<File>()

        // App external files
        context.getExternalFilesDir(null)?.let { dests.add(it) }

        // Downloads
        val downloads = File("/storage/emulated/0/Download")
        if (downloads.exists() && downloads.canWrite()) dests.add(downloads)

        // USB disks
        TopwaySourcePolicy.TS18_USB_CANDIDATES.forEach { path ->
            val disk = File(path)
            if (disk.exists() && disk.canWrite()) dests.add(disk)
        }

        return dests
    }

    fun saveReportToFile(context: Context, dest: File) {
        val report = _reportState.value ?: return
        viewModelScope.launch {
            val result =
                withContext(Dispatchers.IO) {
                    val timestamp = SimpleDateFormat("yyyyMMdd-HHmmss", Locale.US).format(Date())
                    val filename = "Auxio-TS-health-$timestamp.txt"

                    val file = File(dest, filename)
                    try {
                        file.writeText(report)
                        "Report saved to: ${file.absolutePath}"
                    } catch (e: Exception) {
                        "Failed to save report: ${e.message}"
                    }
                }
            _saveResult.value = result
        }
    }

    private fun buildReport(context: Context): String {
        val sb = java.lang.StringBuilder()
        sb.append("=== Auxio-TS TS18 Health Diagnostics ===\n")
        sb.append("Generated: ${java.util.Date()}\n\n")

        appendDeviceInfo(sb, context)
        appendRuntimeInfo(sb, context)
        appendDoFunInfo(sb, context)
        appendStockMusicInfo(sb, context)
        appendStorageInfo(sb, context)
        appendLogsInfo(sb, context)

        return sb.toString()
    }

    private fun appendDeviceInfo(sb: StringBuilder, context: Context) {
        sb.append("--- Device & Platform ---\n")
        sb.append("App Version: ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})\n")
        sb.append("Package Name: ${context.packageName}\n")
        sb.append("SDK Level: ${android.os.Build.VERSION.SDK_INT}\n")
        sb.append("Android Release: ${android.os.Build.VERSION.RELEASE}\n")
        sb.append("Device/Product: ${android.os.Build.DEVICE} / ${android.os.Build.PRODUCT}\n")
        sb.append("Board/Hardware: ${android.os.Build.BOARD} / ${android.os.Build.HARDWARE}\n")
        sb.append("Fingerprint: ${android.os.Build.FINGERPRINT}\n")

        val displayMetrics = context.resources.displayMetrics
        sb.append(
            "Display: ${displayMetrics.widthPixels}x${displayMetrics.heightPixels} (${displayMetrics.densityDpi}dpi)\n"
        )

        if (android.os.Build.VERSION.RELEASE == "13" && android.os.Build.VERSION.SDK_INT == 29) {
            sb.append(
                "Mismatch detected: Firmware reports Android 13 but SDK is 29 (Android 10).\n"
            )
        }
        sb.append("\n")
    }

    private fun appendRuntimeInfo(sb: StringBuilder, context: Context) {
        sb.append("--- Auxio Runtime ---\n")
        sb.append(
            "Playback State: ${if (playbackManager.progression.isPlaying) "Playing" else "Paused/Stopped"}\n"
        )
        sb.append("Current Song: ${playbackManager.currentSong?.name ?: "None"}\n")

        val nm = NotificationManagerCompat.from(context)
        sb.append("Notifications Enabled: ${nm.areNotificationsEnabled()}\n")

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            sb.append("Channels:\n")
            nm.notificationChannels.forEach { channel ->
                sb.append(
                    "  - ${channel.id}: importance=${channel.importance}, enabled=${channel.importance != NotificationManagerCompat.IMPORTANCE_NONE}\n"
                )
            }
        }

        sb.append("Overlay Permission: ${Settings.canDrawOverlays(context)}\n")
        sb.append("\n")
    }

    private fun appendDoFunInfo(sb: StringBuilder, context: Context) {
        sb.append("--- DoFun Launcher Integration ---\n")
        val pm = context.packageManager
        val dofunPkg = "com.dofun.variety"
        sb.append("DoFun Variety ($dofunPkg): ${isPackageInstalled(pm, dofunPkg)}\n")

        val launcherIntent = Intent(Intent.ACTION_MAIN).apply { addCategory(Intent.CATEGORY_HOME) }
        val resolveInfo = pm.resolveActivity(launcherIntent, PackageManager.MATCH_DEFAULT_ONLY)
        sb.append("Default Home: ${resolveInfo?.activityInfo?.packageName}\n")

        val notifyService = "cn.cardoor.basic.media.NotifyService"
        sb.append("Notification Listener: ${isComponentPresent(pm, dofunPkg, notifyService)}\n")
        sb.append("\n")
    }

    private fun appendStockMusicInfo(sb: StringBuilder, context: Context) {
        sb.append("--- Stock Music App ---\n")
        val pm = context.packageManager
        val stockPkg = "com.tw.music"
        sb.append("Stock Music ($stockPkg): ${isPackageInstalled(pm, stockPkg)}\n")
        if (isPackageInstalled(pm, stockPkg) == "Installed") {
            try {
                val ai = pm.getApplicationInfo(stockPkg, 0)
                sb.append("  Flags: ${ai.flags}\n")
                sb.append("  Enabled: ${ai.enabled}\n")
            } catch (e: Exception) {}
        }
        sb.append("\n")
    }

    private fun appendStorageInfo(sb: StringBuilder, context: Context) {
        sb.append("--- Storage & Music Sources ---\n")
        sb.append("Location Mode: ${musicSettings.locationMode}\n")

        if (musicSettings.locationMode == LocationMode.SAF) {
            sb.append("SAF Roots:\n")
            musicSettings.safQuery.source.forEach { sb.append("  - ${it.uri}\n") }
        } else {
            sb.append("MediaStore Filtering: ${musicSettings.mediaStoreQuery.mode}\n")
            musicSettings.mediaStoreQuery.filtered.forEach { sb.append("  - ${it.uri}\n") }
        }

        sb.append("Removable Storage Discovery:\n")
        val storage = File("/storage")
        if (storage.exists() && storage.isDirectory) {
            storage.listFiles()?.forEach { file ->
                sb.append(
                    "  - ${file.absolutePath}: exists=${file.exists()}, readable=${file.canRead()}, listable=${file.list() != null}\n"
                )
            }
        }

        val noisy = findNoisyPaths()
        _noisyPaths.value = noisy
        if (noisy.isNotEmpty()) {
            sb.append("Noisy Paths:\n")
            noisy.forEach { sb.append("  - $it\n") }
        }
        sb.append("\n")
    }

    private fun appendLogsInfo(sb: StringBuilder, context: Context) {
        sb.append("--- Recent Errors ---\n")
        // In a real app, we'd pull from a crash log or diagnostic buffer.
        // For now, we report accessibility of system logs.
        sb.append("System logs (logcat): Unavailable without privileged access\n")
        sb.append("\n")
    }

    private fun isPackageInstalled(pm: PackageManager, pkg: String): String {
        return try {
            pm.getPackageInfo(pkg, 0)
            "Installed"
        } catch (e: PackageManager.NameNotFoundException) {
            "Not Found"
        }
    }

    private fun isComponentPresent(pm: PackageManager, pkg: String, cls: String): String {
        return try {
            val info = pm.getServiceInfo(android.content.ComponentName(pkg, cls), 0)
            if (info != null) "Present" else "Absent"
        } catch (e: Exception) {
            "Absent"
        }
    }

    private fun findNoisyPaths(): List<String> {
        val candidates = mutableListOf<String>()
        val baseRoots =
            listOf(File("/storage/emulated/0")) +
                TopwaySourcePolicy.TS18_USB_CANDIDATES.map { File(it) }

        for (root in baseRoots) {
            if (root.exists() && root.isDirectory) {
                root.listFiles()?.forEach { file ->
                    if (file.isDirectory) {
                        if (
                            TopwaySourcePolicy.NOISY_DIRS.contains(file.name) ||
                                file.name.startsWith(".")
                        ) {
                            candidates.add(file.absolutePath)
                        }
                    }
                }
            }
        }
        return candidates
    }
}
