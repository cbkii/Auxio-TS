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
import androidx.core.content.pm.PackageInfoCompat
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
import org.oxycblt.auxio.diagnostics.Ts18DiagnosticJournal
import org.oxycblt.auxio.diagnostics.Ts18DiagnosticsCaptureService
import org.oxycblt.auxio.diagnostics.Ts18DiagnosticsReporter
import org.oxycblt.auxio.diagnostics.Ts18GuidedDoFunTest
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
            val report = withContext(Dispatchers.IO) { Ts18DiagnosticsReporter(context).buildAutomatedReport() }
            _reportState.value = report
        }
    }

    fun startTimedCapture(context: Context, minutes: Int) {
        Ts18DiagnosticsCaptureService.start(context, minutes * 60_000L, "manual ${minutes}m capture")
        _reportState.value = "Timed TS18 integration capture started for $minutes minutes. Return here and run automated diagnostics or save the report after testing."
    }

    fun armNextStart(context: Context) {
        val prefs = context.getSharedPreferences("ts18_diagnostics", Context.MODE_PRIVATE)
        val id = java.util.UUID.randomUUID().toString().take(8)
        prefs.edit().putString("armed_id", id).putLong("armed_until", System.currentTimeMillis() + 6 * 60 * 60_000L).apply()
        _reportState.value = "Armed one-shot next-start/reboot diagnostics capture $id. It expires in about 6 hours and self-disarms after first app start."
    }

    fun beginGuidedTest(context: Context, marker: Boolean) {
        val id = Ts18DiagnosticJournal.startCapture(2 * 60_000L, "guided DoFun integration test")
        val markerText = if (marker) "AUXIO-TS-${System.currentTimeMillis().toString().takeLast(5)}" else null
        markerText?.let { Ts18DiagnosticJournal.record("topway", "metadata_marker", it, "user_consented_marker_requested") }
        _reportState.value = """
            Guided DoFun capture active (${id ?: "existing capture"}). Leave Auxio once, complete the numbered DoFun sequence, then return once.

            ${Ts18GuidedDoFunTest.instructions(markerText)}

            After return, answer:

            ${Ts18GuidedDoFunTest.questions.joinToString("\n\n")}
        """.trimIndent()
    }

    fun finishGuidedReport(context: Context, answers: List<String>) {
        Ts18DiagnosticJournal.stopCapture("guided answers collected")
        _reportState.value = Ts18DiagnosticsReporter(context).render(
            "Guided DoFun Integration Test",
            emptyList(),
            Ts18DiagnosticJournal.snapshot(),
            answers.map { "User confirmed ==> $it" },
        )
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
        val versionCode = packageInfo?.let { PackageInfoCompat.getLongVersionCode(it) } ?: -1L
        sb.append("Auxio-TS Health Diagnostics Report\n")
        sb.append("Timestamp: ${java.util.Date()}\n\n")
        sb.append("== Device/platform ==\n")
        sb.append(
            "App package/version: $pkg / ${packageInfo?.versionName ?: "unknown"} ($versionCode)\n"
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
        sb.append(
            "Display: ${dm.widthPixels}x${dm.heightPixels} density=${dm.density} dpi=${dm.densityDpi}\n\n"
        )

        sb.append("== Notifications/runtime ==\n")
        val nm = androidx.core.app.NotificationManagerCompat.from(context)
        sb.append("Notifications enabled: ${nm.areNotificationsEnabled()}\n")
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val sysNm =
                context.getSystemService(Context.NOTIFICATION_SERVICE)
                    as android.app.NotificationManager
            sysNm.notificationChannels
                .filter { it.id.startsWith(pkg) }
                .forEach { ch ->
                    sb.append("Channel: ${ch.id} importance=${ch.importance} name=${ch.name}\n")
                }
        }
        sb.append(
            "Playback state: ${if (playbackManager.progression.isPlaying) "Playing" else "Paused/stopped"}\n"
        )
        sb.append("Current song: ${playbackManager.currentSong?.name ?: "None"}\n")
        sb.append("Foreground service active: ${org.oxycblt.auxio.AuxioService.isForeground}\n")
        sb.append("Overlay permission: ${Settings.canDrawOverlays(context)}\n")
        sb.append(
            "Safe TS18 notification bitmap policy: active; fallback=${org.oxycblt.auxio.util.NotificationBitmapSafety.FALLBACK_ICON_SIZE_PX}px, min=${org.oxycblt.auxio.util.NotificationBitmapSafety.MIN_ICON_SIZE_PX}px\n"
        )
        sb.append(
            "Privileged SystemUI/DoFun/kernel logs: Unavailable without privileged/system access\n\n"
        )

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
        appendResolvable(
            sb,
            context,
            "Auxio Topway MusicActivity",
            pkg,
            "com.tw.music.MusicActivity",
        )
        sb.append(
            "Private DoFun windows/listener internals: Unavailable without privileged/system access\n\n"
        )

        sb.append("== Stock music app integration ==\n")
        appendPackage(sb, context, "com.tw.music")
        appendResolvable(
            sb,
            context,
            "Stock MusicActivity",
            "com.tw.music",
            "com.tw.music.MusicActivity",
        )
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
        sb.append(
            "MediaStore volumes (names only; counts skipped to avoid expensive launch-time scans):\n"
        )
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
        sb.append(
            "Temporary USB absence policy: selected paths are reported inaccessible, not rewritten or cleared by diagnostics.\n\n"
        )

        sb.append("== Logs and collection limits ==\n")
        sb.append(
            "Auxio app-owned crash/diagnostic files: see app files directory if present: ${context.filesDir.absolutePath}\n"
        )
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
        sb.append(
            "$label ($packageName/$className) resolvable activity=${activity != null} service=${service != null}\n"
        )
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
                text =
                    "Run a comprehensive on-demand TS18 health and integration diagnostics check."
                textSize = 16f
                setPadding(0, 0, 0, 32)
            }
        content.addView(explanationText)

        fun heading(text: String) = TextView(context).apply { this.text = text; textSize = 18f; setTypeface(null, android.graphics.Typeface.BOLD); setPadding(0, 24, 0, 8) }
        content.addView(heading("Automated diagnostics"))
        val runButton = Button(context).apply { text = "Run automated diagnostics" }
        content.addView(runButton)

        content.addView(heading("Guided DoFun test"))
        val guidedInfo = TextView(context).apply { text = "One external phase only: read all numbered instructions, leave Auxio once, interact with DoFun, return once, then answer all numbered questions."; textSize = 15f }
        content.addView(guidedInfo)
        val guidedButton = Button(context).apply { text = "Run guided DoFun integration test" }
        content.addView(guidedButton)

        content.addView(heading("Timed capture"))
        val captureRow = LinearLayout(context).apply { orientation = LinearLayout.HORIZONTAL }
        listOf(2, 5, 10, 15).forEach { mins -> captureRow.addView(Button(context).apply { text = "Capture ${mins}m"; setOnClickListener { viewModel.startTimedCapture(context, mins) } }) }
        content.addView(captureRow)

        content.addView(heading("Next-start/reboot capture"))
        val armButton = Button(context).apply { text = "Arm diagnostics for next reboot or ACC wake" }
        content.addView(armButton)

        content.addView(heading("Latest results / event timeline"))

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

        val finishGuidedButton = Button(context).apply { text = "Finish guided test / enter answers"; visibility = View.VISIBLE }
        content.addView(finishGuidedButton)
        finishGuidedButton.setOnClickListener {
            val answerChoices = Ts18GuidedDoFunTest.questions.toTypedArray()
            val selected = BooleanArray(answerChoices.size)
            AlertDialog.Builder(context)
                .setTitle("Post-return guided answers")
                .setMultiChoiceItems(answerChoices, selected) { _, which, checked -> selected[which] = checked }
                .setPositiveButton("Save answers") { _, _ -> viewModel.finishGuidedReport(context, answerChoices.filterIndexed { i, _ -> selected[i] }) }
                .setNegativeButton("Cancel", null)
                .show()
        }

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
        guidedButton.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Guided DoFun integration test")
                .setMessage("Begin now or wait about ${Ts18GuidedDoFunTest.COUNTDOWN_SECONDS} seconds; capture is bounded and continues while Auxio is backgrounded.

${Ts18GuidedDoFunTest.instructions(null)}")
                .setPositiveButton("Begin now") { _, _ -> viewModel.beginGuidedTest(context, marker = false) }
                .setNegativeButton("Cancel", null)
                .setNeutralButton("Begin with marker") { _, _ -> viewModel.beginGuidedTest(context, marker = true) }
                .show()
        }
        armButton.setOnClickListener { viewModel.armNextStart(context) }

        exportButton.setOnClickListener {
            val report = viewModel.reportState.value
            if (report != null) {
                val clipboard =
                    context.getSystemService(Context.CLIPBOARD_SERVICE)
                        as android.content.ClipboardManager
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
                    Toast.makeText(context, "No writable destinations found", Toast.LENGTH_LONG)
                        .show()
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
