/*
 * Copyright (c) 2026 Auxio Project
 * TS18HealthDiagnosticsFragment.kt is part of Auxio.
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

import android.os.Bundle
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
import dagger.hilt.android.AndroidEntryPoint
import org.oxycblt.auxio.R
import org.oxycblt.auxio.util.collectImmediately

@AndroidEntryPoint
class TS18HealthDiagnosticsFragment : Fragment() {

    private val viewModel: TS18HealthDiagnosticsViewModel by viewModels()

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
        val root =
            ScrollView(context).apply {
                layoutParams =
                    ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                    )
                isFillViewport = true
            }

        val view =
            LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(32, 32, 32, 32)
            }
        root.addView(view)

        val explanationText =
            TextView(context).apply {
                text = "Run a comprehensive TS18 health and integration diagnostics check."
                textSize = 16f
                setPadding(0, 0, 0, 32)
            }
        view.addView(explanationText)

        val runButton = Button(context).apply { text = "Run Diagnostics" }
        view.addView(runButton)

        val reportText =
            TextView(context).apply {
                textSize = 14f
                setPadding(0, 32, 0, 32)
                typeface = android.graphics.Typeface.MONOSPACE
                setTextIsSelectable(true)
            }
        view.addView(reportText)
        this.reportText = reportText

        val noisyPathsContainer =
            LinearLayout(context).apply { orientation = LinearLayout.VERTICAL }
        view.addView(noisyPathsContainer)
        this.noisyPathsContainer = noisyPathsContainer

        val buttonContainer = LinearLayout(context).apply { orientation = LinearLayout.HORIZONTAL }
        view.addView(buttonContainer)

        val exportButton =
            Button(context).apply {
                text = "Share report"
                visibility = View.GONE
            }
        buttonContainer.addView(exportButton)
        this.exportButton = exportButton

        val saveButton =
            Button(context).apply {
                text = "Save to file"
                visibility = View.GONE
            }
        buttonContainer.addView(saveButton)
        this.saveButton = saveButton

        runButton.setOnClickListener { viewModel.generateReport(context) }

        exportButton.setOnClickListener {
            val report = viewModel.reportState.value
            if (report != null) {
                val intent =
                    android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(android.content.Intent.EXTRA_TEXT, report)
                    }
                startActivity(
                    android.content.Intent.createChooser(intent, "Export TS18 Health Report")
                )
            }
        }

        saveButton.setOnClickListener { showSaveDestinationPicker() }

        return root
    }

    private fun showSaveDestinationPicker() {
        val context = context ?: return
        val destinations = viewModel.getWritableDestinations(context)
        if (destinations.isEmpty()) {
            Toast.makeText(context, "No writable destinations found.", Toast.LENGTH_SHORT).show()
            return
        }

        val names = destinations.map { it.absolutePath }.toTypedArray()
        AlertDialog.Builder(context)
            .setTitle("Select save destination")
            .setItems(names) { _, which ->
                viewModel.saveReportToFile(context, destinations[which])
            }
            .setNegativeButton(R.string.lbl_cancel, null)
            .show()
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

        collectImmediately(viewModel.saveResult) { result ->
            if (result != null) {
                Toast.makeText(context, result, Toast.LENGTH_LONG).show()
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
