/*
 * Copyright (c) 2026 Auxio Project
 * StorageHealthFragment.kt is part of Auxio.
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
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.oxycblt.auxio.headunit.topway.TopwaySourcePolicy
import org.oxycblt.auxio.music.MusicSettings
import org.oxycblt.auxio.music.locations.LocationMode
import org.oxycblt.auxio.playback.state.PlaybackStateManager
import org.oxycblt.auxio.util.collectImmediately
import org.oxycblt.musikr.fs.Location
import org.oxycblt.musikr.fs.StoragePathAliasPolicy
import timber.log.Timber as L

@HiltViewModel
class StorageHealthViewModel
@Inject
constructor(
    private val musicSettings: MusicSettings,
    private val playbackManager: PlaybackStateManager,
) : ViewModel() {

    private val _reportState = MutableStateFlow<String?>(null)
    val reportState: StateFlow<String?> = _reportState.asStateFlow()

    private val _noisyPaths = MutableStateFlow<List<String>>(emptyList())
    val noisyPaths: StateFlow<List<String>> = _noisyPaths.asStateFlow()

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

    fun saveReport(destination: File, report: String): File? {
        return try {
            if (!destination.exists()) destination.mkdirs()
            if (!destination.isDirectory || !destination.canWrite()) return null
            val stamp = SimpleDateFormat("yyyyMMdd-HHmmss", Locale.US).format(java.util.Date())
            val out = File(destination, "Auxio-TS-health-$stamp.txt")
            out.writeText(report, Charsets.UTF_8)
            out
        } catch (e: Exception) {
            L.w(e, "Failed to save TS18 health report to ${destination.absolutePath}")
            null
        }
    }

    private fun buildReport(context: Context): String {
        val sb = java.lang.StringBuilder()
        val pm = context.packageManager
        val pkg = context.packageName
        val packageInfo = runCatching { pm.getPackageInfo(pkg, 0) }.getOrNull()
        sb.append("Auxio-TS Health Diagnostics Report\n")
        sb.append("Timestamp: ${java.util.Date()}\n\n")
        sb.append("== Device/platform ==\n")
        sb.append(
            "App package/version: $pkg / ${packageInfo?.versionName ?: "unknown"} (${packageInfo?.longVersionCode ?: -1})\n"
        )
        sb.append(
            "SDK / reported release: ${android.os.Build.VERSION.SDK_INT} / ${android.os.Build.VERSION.RELEASE}\n"
        )
        sb.append(
            "Release-SDK mismatch: ${android.os.Build.VERSION.RELEASE.startsWith("13") && android.os.Build.VERSION.SDK_INT == 29}\n"
        )
        sb.append(
            "Device/product/board/hardware: ${android.os.Build.DEVICE} / ${android.os.Build.PRODUCT} / ${android.os.Build.BOARD} / ${android.os.Build.HARDWARE}\n"
        )
        sb.append("Fingerprint: ${android.os.Build.FINGERPRINT}\n")
        val dm = context.resources.displayMetrics
        sb.append("Display: ${dm.widthPixels}x${dm.heightPixels} density=${dm.density} dpi=${dm.densityDpi}\n\n")

        sb.append("== Notifications/runtime ==\n")
        val nm = androidx.core.app.NotificationManagerCompat.from(context)
        sb.append("Notifications enabled: ${nm.areNotificationsEnabled()}\n")
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val sysNm =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as
                    android.app.NotificationManager
            sysNm.notificationChannels.filter { it.id.startsWith(pkg) }.forEach { ch ->
                sb.append("Channel: ${ch.id} importance=${ch.importance} name=${ch.name}\n")
            }
        }
        sb.append("Playback state: ${if (playbackManager.progression.isPlaying) "Playing" else "Paused/stopped"}\n")
        sb.append("Current song: ${playbackManager.currentSong?.name ?: "None"}\n")
        sb.append("Foreground service active: ${org.oxycblt.auxio.AuxioService.isForeground}\n")
        sb.append("Overlay permission: ${Settings.canDrawOverlays(context)}\n")
        sb.append(
            "Safe TS18 notification bitmap policy: active; fallback=${org.oxycblt.auxio.util.NotificationBitmapSafety.FALLBACK_ICON_SIZE_PX}px, min=${org.oxycblt.auxio.util.NotificationBitmapSafety.MIN_ICON_SIZE_PX}px\n"
        )
        sb.append("Privileged SystemUI/DoFun/kernel logs: Unavailable without privileged/system access\n\n")

        sb.append("== DoFun launcher integration ==\n")
        appendPackage(sb, context, "com.dofun.variety")
        appendResolvable(
            sb,
            context,
            "DoFun launcher",
            "com.dofun.variety",
            "com.dofun.overseasvariety.Launcher",
        )
        appendResolvable(
            sb,
            context,
            "DoFun NotifyService",
            "com.dofun.variety",
            "cn.cardoor.basic.media.NotifyService",
        )
        val home = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME)
        sb.append(
            "Default/available HOME: ${pm.resolveActivity(home, 0)?.activityInfo?.packageName ?: "unresolved"}\n"
        )
        appendResolvable(sb, context, "Auxio Topway MusicActivity", pkg, "com.tw.music.MusicActivity")
        sb.append("Private DoFun windows/listener internals: Unavailable without privileged/system access\n\n")

        sb.append("== Stock music app integration ==\n")
        appendPackage(sb, context, "com.tw.music")
        appendResolvable(sb, context, "Stock MusicActivity", "com.tw.music", "com.tw.music.MusicActivity")
        sb.append("Current Auxio variant package: $pkg\n")
        sb.append("Package conflict with stock com.tw.music: ${pkg == "com.tw.music"}\n\n")

        sb.append("== Storage and music sources ==\n")
        val candidates = TopwaySourcePolicy.discoverCandidateRoots()
        sb.append("Discovered readable source roots (bounded, non-recursive):\n")
        candidates.forEach { sb.append("  - $it\n") }
        sb.append("/mnt/media_rw accessible: ${File("/mnt/media_rw").canRead()}\n")
        sb.append("Music source mode: ${musicSettings.locationMode.name}\n")
        sb.append(
            "TS18 fast filter enabled: ${musicSettings.ts18SystemSourceFilter} keywords=${TopwaySourcePolicy.SYSTEM_SOURCE_PATH_KEYWORDS.joinToString()}\n"
        )
        sb.append("Persisted SAF permissions:\n")
        context.contentResolver.persistedUriPermissions.forEach { p ->
            sb.append("  - ${p.uri} read=${p.isReadPermission} write=${p.isWritePermission}\n")
        }
        sb.append("Selected SAF source paths (exact URI/path identity preserved by settings):\n")
        musicSettings.safQuery.source.forEach { location ->
            sb.append("  - ${location.uri} accessible=${location.path.volume.isAccessible()}\n")
        }
        sb.append(
            "MediaStore filter mode: ${musicSettings.mediaStoreQuery.mode.name} exclusions=${musicSettings.mediaStoreQuery.filtered.size}\n"
        )
        sb.append("MediaStore volumes (names only; counts skipped to avoid expensive launch-time scans):\n")
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            MediaStore.getExternalVolumeNames(context).forEach { sb.append("  - $it\n") }
        } else {
            sb.append("  - external\n")
        }

        val noisy = findNoisyPaths(candidates)
        _noisyPaths.value = noisy
        if (noisy.isNotEmpty()) {
            sb.append("Noisy/problem path candidates:\n")
            noisy.forEach { sb.append("  - $it\n") }
        }
        val aliases = StoragePathAliasPolicy.groupAliases(candidates).filter { it.paths.size > 1 }
        sb.append("Alias groups / duplicate-source risk:\n")
        if (aliases.isEmpty()) {
            sb.append("  none detected from bounded roots\n")
        } else {
            aliases.forEach { group -> group.paths.forEach { sb.append("  - $it\n") } }
        }
        sb.append("Temporary USB absence policy: selected paths are reported inaccessible, not rewritten or cleared by diagnostics.\n\n")

        sb.append("== Logs and collection limits ==\n")
        sb.append("Auxio app-owned crash/diagnostic files: see app files directory if present: ${context.filesDir.absolutePath}\n")
        sb.append(
            "SystemUI/DoFun/stock music/kernel logs: Unavailable without privileged/system access. Collect externally with adb bugreport/logcat/dmesg where available.\n"
        )
        return sb.toString()
    }

    private fun appendPackage(sb: StringBuilder, context: Context, packageName: String) {
        val pm = context.packageManager
        val info = runCatching { pm.getApplicationInfo(packageName, 0) }.getOrNull()
        val pkg = runCatching { pm.getPackageInfo(packageName, 0) }.getOrNull()
        val system = info?.flags?.and(android.content.pm.ApplicationInfo.FLAG_SYSTEM) != 0
        val source = info?.sourceDir ?: "unavailable"
        val privileged = source.contains("/priv-app/")
        sb.append(
            "$packageName installed=${info != null} enabled=${info?.enabled} version=${pkg?.versionName ?: "unknown"} system=$system privileged=$privileged source=$source\n"
        )
    }

    private fun appendResolvable(
        sb: StringBuilder,
        context: Context,
        label: String,
        packageName: String,
        className: String,
    ) {
        val intent = Intent().setClassName(packageName, className)
        val activity = context.packageManager.resolveActivity(intent, 0)
        val service = context.packageManager.resolveService(intent, 0)
        sb.append("$label ($packageName/$className) resolvable activity=${activity != null} service=${service != null}\n")
    }

    fun discoverWritableDestinations(context: Context): List<File> {
        val out = linkedSetOf<File>()
        context.getExternalFilesDir(null)?.let { if (ensureWritable(it)) out += it }
        listOf(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
            )
            .filterTo(out) { ensureWritable(it) }
        TopwaySourcePolicy.discoverCandidateRoots().map(::File).filterTo(out) { ensureWritable(it) }
        return out.toList()
    }

    private fun ensureWritable(file: File): Boolean =
        try {
            (file.exists() || file.mkdirs()) && file.isDirectory && file.canWrite()
        } catch (e: Exception) {
            L.w(e, "Destination is not writable: ${file.absolutePath}")
            false
        }

    private fun findNoisyPaths(
        baseRootPaths: List<String> = TopwaySourcePolicy.discoverCandidateRoots()
    ): List<String> {
        val candidates = mutableListOf<String>()
        val baseRoots = baseRootPaths.map(::File)

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

@AndroidEntryPoint
class StorageHealthFragment : Fragment() {

    private val viewModel: StorageHealthViewModel by viewModels()

    private var reportText: TextView? = null
    private var exportButton: Button? = null
    private var saveButton: Button? = null
    private var noisyPathsContainer: LinearLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val context = requireContext()
        val content =
            LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(32, 32, 32, 32)
            }
        val view = ScrollView(context).apply { addView(content) }

        val explanationText =
            TextView(context).apply {
                text = "Run a comprehensive on-demand TS18 health and integration diagnostics check."
                textSize = 16f
                setPadding(0, 0, 0, 32)
            }
        content.addView(explanationText)

        val runButton = Button(context).apply { text = "Run / refresh TS18 diagnostics" }
        content.addView(runButton)

        val reportText =
            TextView(context).apply {
                textSize = 14f
                setPadding(0, 32, 0, 32)
                typeface = android.graphics.Typeface.MONOSPACE
            }
        reportText.setTextIsSelectable(true)
        content.addView(reportText)
        this.reportText = reportText

        val noisyPathsContainer =
            LinearLayout(context).apply { orientation = LinearLayout.VERTICAL }
        content.addView(noisyPathsContainer)
        this.noisyPathsContainer = noisyPathsContainer

        val exportButton =
            Button(context).apply {
                text = "Copy / share report"
                visibility = View.GONE
            }
        content.addView(exportButton)
        val saveButton =
            Button(context).apply {
                text = "Save report"
                visibility = View.GONE
            }
        content.addView(saveButton)
        this.exportButton = exportButton
        this.saveButton = saveButton

        runButton.setOnClickListener { viewModel.generateReport(context) }

        exportButton.setOnClickListener {
            val report = viewModel.reportState.value
            if (report != null) {
                val clipboard =
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as
                        android.content.ClipboardManager
                clipboard.setPrimaryClip(
                    android.content.ClipData.newPlainText("Auxio-TS health", report)
                )
                // simple export via share intent
                val intent =
                    android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(android.content.Intent.EXTRA_TEXT, report)
                    }
                startActivity(
                    android.content.Intent.createChooser(intent, "Export Storage Health Report")
                )
            }
        }

        saveButton.setOnClickListener {
            val report = viewModel.reportState.value ?: return@setOnClickListener
            viewLifecycleOwner.lifecycleScope.launch {
                val destinations =
                    withContext(Dispatchers.IO) { viewModel.discoverWritableDestinations(context) }
                if (destinations.isEmpty()) {
                    Toast.makeText(context, "No writable destinations found", Toast.LENGTH_LONG).show()
                } else {
                    AlertDialog.Builder(context)
                        .setTitle("Save TS18 health report")
                        .setItems(destinations.map { it.absolutePath }.toTypedArray()) { _, which ->
                            viewLifecycleOwner.lifecycleScope.launch {
                                val saved =
                                    withContext(Dispatchers.IO) {
                                        viewModel.saveReport(destinations[which], report)
                                    }
                                Toast.makeText(
                                        context,
                                        saved?.let { "Saved: ${it.absolutePath}" } ?: "Save failed",
                                        Toast.LENGTH_LONG,
                                    )
                                    .show()
                            }
                        }
                        .show()
                }
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = view.context

        collectImmediately(viewModel.reportState) { report ->
            if (report != null) {
                reportText?.text = report
                exportButton?.visibility = View.VISIBLE
                saveButton?.visibility = View.VISIBLE
            }
        }

        collectImmediately(viewModel.noisyPaths) { paths ->
            val container = noisyPathsContainer ?: return@collectImmediately
            container.removeAllViews()
            if (paths.isNotEmpty()) {
                val title =
                    TextView(context).apply {
                        text = "Some folders are likely slowing scans. Click to exclude:"
                        textSize = 16f
                        setTypeface(null, android.graphics.Typeface.BOLD)
                    }
                container.addView(title)

                paths.forEach { path ->
                    val pathBtn =
                        Button(context).apply {
                            text = "Exclude $path"
                            setOnClickListener {
                                viewModel.excludePath(context, path)
                                Toast.makeText(context, "Excluded $path", Toast.LENGTH_SHORT).show()
                            }
                        }
                    container.addView(pathBtn)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        reportText = null
        exportButton = null
        saveButton = null
        noisyPathsContainer = null
    }
}
