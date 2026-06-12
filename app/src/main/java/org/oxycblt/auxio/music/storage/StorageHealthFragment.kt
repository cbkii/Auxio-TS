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
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.oxycblt.auxio.R
import org.oxycblt.auxio.headunit.topway.TopwaySourcePolicy
import org.oxycblt.auxio.music.MusicSettings
import org.oxycblt.auxio.music.locations.LocationMode
import org.oxycblt.musikr.fs.Location

import org.oxycblt.musikr.fs.StoragePathAliasPolicy

@HiltViewModel
class StorageHealthViewModel @Inject constructor(
    private val musicSettings: MusicSettings
) : ViewModel() {

    private val _reportState = MutableStateFlow<String?>(null)
    val reportState: StateFlow<String?> = _reportState.asStateFlow()

    private val _noisyPaths = MutableStateFlow<List<String>>(emptyList())
    val noisyPaths: StateFlow<List<String>> = _noisyPaths.asStateFlow()

    fun generateReport(context: Context) {
        viewModelScope.launch {
            val report = withContext(Dispatchers.IO) {
                buildReport(context)
            }
            _reportState.value = report
        }
    }

    fun excludePath(context: Context, path: String) {
        if (musicSettings.locationMode == LocationMode.SAF) {
            val currentQuery = musicSettings.safQuery
            val newExclude = currentQuery.exclude + Location.Unopened.from(context, android.net.Uri.parse("file://$path"))!!
            musicSettings.safQuery = currentQuery.copy(exclude = newExclude)
        } else {
            val currentQuery = musicSettings.mediaStoreQuery
            val newFiltered = currentQuery.filtered + Location.Unopened.from(context, android.net.Uri.parse("file://$path"))!!
            musicSettings.mediaStoreQuery = currentQuery.copy(filtered = newFiltered)
        }
    }

    private fun buildReport(context: Context): String {
        val sb = java.lang.StringBuilder()
        sb.append("Auxio-TS Storage Health Report\n")
        sb.append("Timestamp: ${java.util.Date()}\n")
        sb.append("Android version / SDK: ${android.os.Build.VERSION.RELEASE} / ${android.os.Build.VERSION.SDK_INT}\n")
        sb.append("Device / board / manufacturer: ${android.os.Build.DEVICE} / ${android.os.Build.BOARD} / ${android.os.Build.MANUFACTURER}\n")
        
        val isTs18 = android.os.Build.DEVICE.lowercase().contains("s9863a1h10") || android.os.Build.BOARD.lowercase().contains("s9863a1h10")
        sb.append("TS18 detected: ${if (isTs18) "yes" else "no"}\n")

        val usbDisk0 = File("/storage/usbdisk0")
        sb.append("/storage/usbdisk0 exists/readable/listable: ${usbDisk0.exists()}/${usbDisk0.canRead()}/${usbDisk0.list() != null}\n")

        val mediaRw = File("/mnt/media_rw")
        sb.append("/mnt/media_rw exists/readable/listable: ${mediaRw.exists()}/${mediaRw.canRead()}/${mediaRw.list() != null}\n")

        sb.append("Music source mode: ${musicSettings.locationMode.name}\n")
        sb.append("TS18 fast filter enabled: ${musicSettings.ts18SystemSourceFilter}\n")
        sb.append("TS18 keywords: ${TopwaySourcePolicy.SYSTEM_SOURCE_PATH_KEYWORDS.joinToString()}\n")

        sb.append("Configured MediaStore filter mode: ${musicSettings.mediaStoreQuery.mode.name}\n")
        
        sb.append("MediaStore volumes and counts:\n")
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            val volumes = MediaStore.getExternalVolumeNames(context)
            for (vol in volumes) {
                sb.append("  Volume: $vol\n")
            }
        } else {
            sb.append("  Volume: external\n")
        }

        val noisy = findNoisyPaths()
        _noisyPaths.value = noisy
        if (noisy.isNotEmpty()) {
            sb.append("\nNoisy/problem path candidates:\n")
            noisy.forEach { sb.append("  - $it\n") }
        }

        val allPaths = mutableListOf<String>()
        if (musicSettings.locationMode == LocationMode.SAF) {
            musicSettings.safQuery.source.forEach { allPaths.add(it.path) }
        } else {
            // Find some media store paths via a quick scan or just mention that.
            // For now, we only deduplicate based on existing noisy/selected paths
            allPaths.addAll(noisy)
        }
        
        val aliases = StoragePathAliasPolicy.groupAliases(allPaths).filter { it.paths.size > 1 }
        if (aliases.isNotEmpty()) {
            sb.append("\nAlias groups detected:\n")
            aliases.forEach { group ->
                sb.append("  Possible duplicate aliases detected:\n")
                group.paths.forEach { path ->
                    sb.append("  - $path\n")
                }
                sb.append("  Action: treated as one file\n")
            }
        }

        return sb.toString()
    }

    private fun findNoisyPaths(): List<String> {
        val candidates = mutableListOf<String>()
        val baseRoots = listOf(File("/storage/emulated/0"), File("/storage/usbdisk0"))
        
        for (root in baseRoots) {
            if (root.exists() && root.isDirectory) {
                root.listFiles()?.forEach { file ->
                    if (file.isDirectory) {
                        if (TopwaySourcePolicy.NOISY_DIRS.contains(file.name) || file.name.startsWith(".")) {
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val context = requireContext()
        val view = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
        }

        val explanationText = TextView(context).apply {
            text = "Run a one-time check of storage, source filters, and exclusions."
            textSize = 16f
            setPadding(0, 0, 0, 32)
        }
        view.addView(explanationText)

        val runButton = Button(context).apply {
            text = "Run Storage Check"
        }
        view.addView(runButton)

        val reportText = TextView(context).apply {
            textSize = 14f
            setPadding(0, 32, 0, 32)
            typeface = android.graphics.Typeface.MONOSPACE
        }
        view.addView(reportText)

        val noisyPathsContainer = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
        }
        view.addView(noisyPathsContainer)

        val exportButton = Button(context).apply {
            text = "Export report"
            visibility = View.GONE
        }
        view.addView(exportButton)

        runButton.setOnClickListener {
            viewModel.generateReport(context)
        }

        exportButton.setOnClickListener {
            val report = viewModel.reportState.value
            if (report != null) {
                // simple export via copy to clipboard or share intent
                val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(android.content.Intent.EXTRA_TEXT, report)
                }
                startActivity(android.content.Intent.createChooser(intent, "Export Storage Health Report"))
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.reportState.collect { report ->
                if (report != null) {
                    reportText.text = report
                    exportButton.visibility = View.VISIBLE
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.noisyPaths.collect { paths ->
                noisyPathsContainer.removeAllViews()
                if (paths.isNotEmpty()) {
                    val title = TextView(context).apply {
                        text = "Some folders are likely slowing scans. Click to exclude:"
                        textSize = 16f
                        setTypeface(null, android.graphics.Typeface.BOLD)
                    }
                    noisyPathsContainer.addView(title)
                    
                    paths.forEach { path ->
                        val pathBtn = Button(context).apply {
                            text = "Exclude $path"
                            setOnClickListener {
                                viewModel.excludePath(context, path)
                                Toast.makeText(context, "Excluded $path", Toast.LENGTH_SHORT).show()
                            }
                        }
                        noisyPathsContainer.addView(pathBtn)
                    }
                }
            }
        }

        return view
    }
}