/*
 * Copyright (c) 2026 Auxio Project
 * DiagnosticsFragment.kt is part of Auxio.
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

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.oxycblt.auxio.R
import org.oxycblt.auxio.util.collectImmediately
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class DiagnosticsFragment : Fragment() {

    private val viewModel: DiagnosticsViewModel by viewModels()

    private var reportContainer: LinearLayout? = null
    private var timelineContainer: LinearLayout? = null
    private var statusText: TextView? = null
    private var overlayContainer: ViewGroup? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_diagnostics, container, false)

        statusText = view.findViewById(R.id.diag_status)
        reportContainer = view.findViewById(R.id.report_container)
        timelineContainer = view.findViewById(R.id.timeline_container)
        overlayContainer = view.findViewById(R.id.overlay_container)

        view.findViewById<Button>(R.id.run_auto_btn).setOnClickListener {
            viewModel.runAutomatedDiagnostics()
        }
        view.findViewById<Button>(R.id.start_guided_btn).setOnClickListener {
            viewModel.startGuidedTest()
        }
        view.findViewById<Button>(R.id.start_capture_btn).setOnClickListener {
            viewModel.startTimedCapture(5)
        }
        view.findViewById<Button>(R.id.arm_boot_btn).setOnClickListener {
            viewModel.armBootCapture()
            Toast.makeText(requireContext(), "Capture armed for next start", Toast.LENGTH_SHORT).show()
        }
        view.findViewById<Button>(R.id.stop_capture_btn).setOnClickListener {
            viewModel.stopCapture()
        }
        view.findViewById<Button>(R.id.export_btn).setOnClickListener {
            exportReport()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = view.context

        collectImmediately(viewModel.isCaptureActive) { active ->
            statusText?.text = if (active) "CAPTURE ACTIVE" else "System Idle"
            statusText?.setTextColor(if (active) 0xFFFF0000.toInt() else 0xFF888888.toInt())
        }

        collectImmediately(viewModel.automatedReport) { report ->
            val container = reportContainer ?: return@collectImmediately
            container.removeAllViews()
            report?.forEach { entry ->
                val entryView = TextView(context).apply {
                    text = "${entry.name}: ${entry.value}\n[${entry.evidence}]"
                    setPadding(0, 8, 0, 8)
                    textSize = 14f
                    // Safe coloring for diagnostic text
                    val color = when(entry.evidence) {
                        EvidenceClassification.OBSERVED_BY_AUXIO -> 0xFF2E7D32.toInt() // Green
                        EvidenceClassification.USER_CONFIRMED -> 0xFF1976D2.toInt() // Blue
                        EvidenceClassification.PERMISSION_DENIED -> 0xFFD32F2F.toInt() // Red
                        EvidenceClassification.QUERY_FAILED -> 0xFFD32F2F.toInt() // Red
                        else -> 0xFF333333.toInt()
                    }
                    setTextColor(color)
                }
                container.addView(entryView)
            }
        }

        collectImmediately(viewModel.journalEvents) { events ->
            val container = timelineContainer ?: return@collectImmediately
            container.removeAllViews()
            events.takeLast(50).reversed().forEach { event ->
                val eventView = TextView(context).apply {
                    val time = SimpleDateFormat("HH:mm:ss", Locale.US).format(Date(event.wallTime))
                    text = "[$time] ${event.category}: ${event.event} - ${event.detail ?: ""}"
                    textSize = 12f
                    setPadding(0, 2, 0, 2)
                    typeface = android.graphics.Typeface.MONOSPACE
                }
                container.addView(eventView)
            }
        }

        collectImmediately(viewModel.guidedTestState) { state ->
            handleGuidedTestState(state)
        }
    }

    private fun handleGuidedTestState(state: DiagnosticsViewModel.GuidedTestState) {
        val container = overlayContainer ?: return
        container.removeAllViews()

        when (state) {
            DiagnosticsViewModel.GuidedTestState.Idle -> {
                container.visibility = View.GONE
            }
            DiagnosticsViewModel.GuidedTestState.Instructions -> {
                container.visibility = View.VISIBLE
                val view = layoutInflater.inflate(R.layout.layout_guided_instructions, container, true)
                view.findViewById<Button>(R.id.guided_begin_btn).setOnClickListener {
                    viewModel.beginGuidedCountdown()
                }
                view.findViewById<Button>(R.id.guided_cancel_btn).setOnClickListener {
                    viewModel.cancelGuidedTest()
                }
            }
            DiagnosticsViewModel.GuidedTestState.CountingDown -> {
                container.visibility = View.VISIBLE
                val view = layoutInflater.inflate(R.layout.layout_guided_instructions, container, true)
                val countdownText = view.findViewById<TextView>(R.id.guided_countdown_text)
                countdownText.visibility = View.VISIBLE

                collectImmediately(viewModel.countdown) { count ->
                    countdownText.text = "Starting in $count seconds..."
                }

                view.findViewById<Button>(R.id.guided_begin_btn).setOnClickListener {
                    viewModel.actuallyBeginCapture()
                }
                view.findViewById<Button>(R.id.guided_cancel_btn).setOnClickListener {
                    viewModel.cancelGuidedTest()
                }
            }
            DiagnosticsViewModel.GuidedTestState.Capturing -> {
                container.visibility = View.VISIBLE
                val view = layoutInflater.inflate(R.layout.layout_guided_instructions, container, true)
                view.findViewById<TextView>(R.id.guided_instructions_text).text =
                    "CAPTURE ACTIVE\n\nLeave Auxio now and complete the steps. Return when finished."
                view.findViewById<Button>(R.id.guided_begin_btn).visibility = View.GONE
                view.findViewById<Button>(R.id.guided_cancel_btn).setOnClickListener {
                    viewModel.cancelGuidedTest()
                }
            }
            DiagnosticsViewModel.GuidedTestState.Questionnaire -> {
                container.visibility = View.VISIBLE
                showQuestionnaireUI(container)
            }
            is DiagnosticsViewModel.GuidedTestState.Result -> {
                container.visibility = View.GONE
                AlertDialog.Builder(requireContext())
                    .setTitle("Guided Test Result")
                    .setMessage(state.report)
                    .setPositiveButton("OK") { _, _ -> viewModel.cancelGuidedTest() }
                    .show()
            }
        }
    }

    private fun showQuestionnaireUI(parent: ViewGroup) {
        val view = layoutInflater.inflate(R.layout.layout_guided_questionnaire, parent, true)
        val container = view.findViewById<LinearLayout>(R.id.questionnaire_container)
        val answers = mutableMapOf<Int, Int>()

        val questions = listOf(
            "1. Which app opened after tapping Music card?" to arrayOf("1. Auxio-TS", "2. Stock TW Music", "3. Another app", "4. No app", "5. Not sure"),
            "2. Did controls affect Auxio?" to arrayOf("1. All affected Auxio", "2. Some affected", "3. None", "4. Not sure"),
            "3. Did title/artist change?" to arrayOf("1. Matched Auxio", "2. Matched stock", "3. Changed, unknown", "4. No change", "5. Not sure"),
            "4. Did progress/seek change?" to arrayOf("1. Yes, matched Auxio", "2. Yes, but different", "3. No", "4. No control", "5. Not sure"),
            "5. Did you see the diagnostic marker?" to arrayOf("1. Yes, I saw DIAGNOSTIC MARKER", "2. No, real metadata", "3. Something else", "4. Not sure")
        )

        questions.forEachIndexed { index, (qText, options) ->
            val qNum = index + 1
            val tv = TextView(requireContext()).apply {
                text = qText
                textSize = 18f
                setPadding(0, 16, 0, 8)
            }
            container.addView(tv, container.childCount - 1)

            val rg = RadioGroup(requireContext())
            options.forEachIndexed { optIndex, optText ->
                val rb = RadioButton(requireContext()).apply {
                    text = optText
                    id = View.generateViewId()
                }
                rg.addView(rb)
                if (optIndex == options.size - 1) rb.isChecked = true // Default to unsure
            }
            rg.setOnCheckedChangeListener { group, checkedId ->
                val checked = group.findViewById<RadioButton>(checkedId)
                answers[qNum] = group.indexOfChild(checked) + 1
            }
            // Init with default
            answers[qNum] = options.size

            container.addView(rg, container.childCount - 1)
        }

        view.findViewById<Button>(R.id.questionnaire_submit_btn).setOnClickListener {
            viewModel.submitQuestionnaire(answers, emptyMap())
        }
    }

    private fun exportReport() {
        val context = context ?: return
        val report = viewModel.buildFullReport()
        viewLifecycleOwner.lifecycleScope.launch {
            val destinations = withContext(Dispatchers.IO) { viewModel.discoverWritableDestinations(context) }
            if (destinations.isEmpty()) {
                Toast.makeText(context, "No writable destinations found", Toast.LENGTH_LONG).show()
            } else {
                AlertDialog.Builder(context)
                    .setTitle("Save diagnostics report")
                    .setItems(destinations.map { it.absolutePath }.toTypedArray()) { _, which ->
                        viewLifecycleOwner.lifecycleScope.launch {
                            val saved = withContext(Dispatchers.IO) {
                                viewModel.saveReport(destinations[which], report)
                            }
                            Toast.makeText(context, saved?.let { "Saved: ${it.absolutePath}" } ?: "Save failed", Toast.LENGTH_LONG).show()
                        }
                    }.show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.returnFromGuidedTest()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        reportContainer = null
        timelineContainer = null
        statusText = null
        overlayContainer = null
    }

    private val Float.sp get() = this * resources.displayMetrics.scaledDensity
}
