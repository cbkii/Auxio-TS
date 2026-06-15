/*
 * Copyright (c) 2026 Auxio Project
 * DiagnosticsViewModel.kt is part of Auxio.
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
import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.oxycblt.auxio.headunit.topway.TopwaySourcePolicy
import org.oxycblt.auxio.music.MusicSettings
import org.oxycblt.auxio.music.locations.LocationMode
import org.oxycblt.musikr.fs.Location
import timber.log.Timber as L

@HiltViewModel
class DiagnosticsViewModel
@Inject
constructor(
    @ApplicationContext private val context: Context,
    private val repository: DiagnosticsRepository,
    private val journal: DiagnosticJournal,
    private val musicSettings: MusicSettings,
    private val diagSettings: DiagnosticsSettings,
    private val reportGenerator: DiagnosticReportGenerator,
    private val markerController: DiagnosticMarkerController,
) : ViewModel() {

    private val _automatedReport = MutableStateFlow<List<DiagnosticEntry>?>(null)
    val automatedReport: StateFlow<List<DiagnosticEntry>?> = _automatedReport.asStateFlow()

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    private val _noisyPaths = MutableStateFlow<List<String>>(emptyList())
    val noisyPaths: StateFlow<List<String>> = _noisyPaths.asStateFlow()

    val journalEvents = journal.events
    val isCaptureActive = repository.isCaptureActive

    // Guided Test State
    private val _guidedTestState = MutableStateFlow<GuidedTestState>(GuidedTestState.Idle)
    val guidedTestState: StateFlow<GuidedTestState> = _guidedTestState.asStateFlow()

    private val _countdown = MutableStateFlow(0)
    val countdown: StateFlow<Int> = _countdown.asStateFlow()

    sealed class GuidedTestState {
        object Idle : GuidedTestState()

        object Instructions : GuidedTestState()

        object CountingDown : GuidedTestState()

        object Capturing : GuidedTestState()

        object Questionnaire : GuidedTestState()

        data class Result(val report: String) : GuidedTestState()
    }

    fun runAutomatedDiagnostics() {
        viewModelScope.launch {
            _isGenerating.value = true
            try {
                val results = repository.runAutomatedChecks()
                _automatedReport.value = results
                _noisyPaths.value = withContext(Dispatchers.IO) { findNoisyPaths() }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                L.w(e, "Automated diagnostics failed")
                _noisyPaths.value = emptyList()
                _automatedReport.value =
                    listOf(
                        DiagnosticEntry(
                            "Automated diagnostics",
                            "Failed: ${e.message ?: e.javaClass.simpleName}",
                            EvidenceClassification.QUERY_FAILED,
                        )
                    )
            } finally {
                _isGenerating.value = false
            }
        }
    }

    fun startGuidedTest() {
        if (_guidedTestState.value != GuidedTestState.Idle) return
        _guidedTestState.value = GuidedTestState.Instructions
        beginGuidedCountdown()
    }

    fun beginGuidedCountdown() {
        if (
            _guidedTestState.value != GuidedTestState.Instructions &&
                _guidedTestState.value != GuidedTestState.CountingDown
        ) {
            return
        }
        viewModelScope.launch {
            _guidedTestState.value = GuidedTestState.CountingDown
            for (i in 120 downTo 0) {
                _countdown.value = i
                if (_guidedTestState.value != GuidedTestState.CountingDown) break
                delay(1000)
            }
            if (_guidedTestState.value == GuidedTestState.CountingDown) {
                actuallyBeginCapture()
            }
        }
    }

    private var guidedSessionId: String? = null
    private var guidedStartRequested = false

    fun actuallyBeginCapture(markerEnabled: Boolean = true) {
        if (guidedStartRequested || _guidedTestState.value == GuidedTestState.Capturing) return
        val sessionId = "guided-${UUID.randomUUID().toString().take(8)}"
        guidedSessionId = sessionId
        guidedStartRequested = true
        try {
            DiagnosticService.start(
                context,
                sessionId,
                GUIDED_CAPTURE_DURATION_MS,
                DiagnosticService.ORIGIN_GUIDED,
            )
        } catch (e: Exception) {
            guidedStartRequested = false
            guidedSessionId = null
            restoreDiagnosticMarker()
            _guidedTestState.value = GuidedTestState.Result("Guided capture failed: ${e.message}")
            return
        }

        viewModelScope.launch {
            repeat(20) {
                if (journal.activeSessionId == sessionId) {
                    if (markerEnabled) {
                        val marker = "MARKER-${UUID.randomUUID().toString().take(4)}"
                        markerController.publishMarker(marker)
                        journal.log(
                            DiagnosticJournal.CAT_TOPWAY_BROADCAST,
                            "Guided test marker set",
                            marker,
                        )
                    } else {
                        journal.log(DiagnosticJournal.CAT_TOPWAY_BROADCAST, "Guided marker skipped")
                    }
                    _guidedTestState.value = GuidedTestState.Capturing
                    guidedStartRequested = false
                    return@launch
                }
                delay(100)
            }
            guidedStartRequested = false
            guidedSessionId = null
            restoreDiagnosticMarker()
            _guidedTestState.value =
                GuidedTestState.Result(
                    "Guided capture failed: service did not confirm the session."
                )
        }
    }

    fun cancelGuidedTest() {
        restoreDiagnosticMarker()
        if (isCaptureActive.value) {
            DiagnosticService.stop(context, guidedSessionId)
        }
        guidedStartRequested = false
        guidedSessionId = null
        _guidedTestState.value = GuidedTestState.Idle
    }

    fun returnFromGuidedTest() {
        if (_guidedTestState.value == GuidedTestState.Capturing) {
            DiagnosticService.stop(context, guidedSessionId)

            restoreDiagnosticMarker()

            _guidedTestState.value = GuidedTestState.Questionnaire
        }
    }

    fun submitQuestionnaire(answers: Map<Int, Int>, otherTexts: Map<Int, String>) {
        viewModelScope.launch {
            val report =
                withContext(Dispatchers.Default) { correlateGuidedTest(answers, otherTexts) }
            _guidedTestState.value = GuidedTestState.Result(report)
            try {
                _automatedReport.value = repository.runAutomatedChecks()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                L.w(e, "Automated refresh after questionnaire failed")
            }
        }
    }

    private fun correlateGuidedTest(answers: Map<Int, Int>, otherTexts: Map<Int, String>): String {
        val sb = StringBuilder()
        sb.append("Timestamp: ${Date()}\n\n")

        sb.append("== User Confirmed Responses ==\n")
        answers.forEach { (q, a) ->
            val questionText =
                when (q) {
                    1 -> "Which app opened after tapping Music card?"
                    2 -> "Did controls affect Auxio?"
                    3 -> "Did title/artist change?"
                    4 -> "Did progress/seek change?"
                    5 -> "Did you see the diagnostic marker?"
                    else -> "Question $q"
                }
            val answerText =
                when (q to a) {
                    1 to 1 -> "Auxio-TS"
                    1 to 2 -> "Stock TW Music"
                    1 to 3 -> "Another app"
                    1 to 4 -> "No app opened"
                    1 to 5 -> "Not sure"
                    2 to 1 -> "All affected Auxio"
                    2 to 2 -> "Some affected Auxio"
                    2 to 3 -> "None affected"
                    2 to 4 -> "Could not tell"
                    3 to 1 -> "Matched Auxio"
                    3 to 2 -> "Matched stock"
                    3 to 3 -> "Changed, unknown source"
                    3 to 4 -> "Did not change"
                    3 to 5 -> "Could not tell"
                    4 to 1 -> "Yes, matched Auxio"
                    4 to 2 -> "Yes, did not match"
                    4 to 3 -> "No"
                    4 to 4 -> "No progress control"
                    4 to 5 -> "Could not tell"
                    5 to 1 -> "Yes, I saw DIAGNOSTIC MARKER"
                    5 to 2 -> "No, I saw real metadata"
                    5 to 3 -> "I saw something else"
                    5 to 4 -> "Not sure"
                    else -> "Option $a"
                }
            sb.append("$questionText: $answerText")
            otherTexts[q]?.let { sb.append(" (Other: $it)") }
            sb.append("\n")
        }

        sb.append("\n== Correlation Analysis ==\n")

        val sessionId = guidedSessionId
        if (sessionId == null) {
            sb.append("Guided session correlation unavailable: missing guided session ID.\n")
            return sb.toString()
        }
        val sessionEvents = journalEvents.value.filter { it.sessionId == sessionId }
        val cmds = sessionEvents.filter { it.category == DiagnosticJournal.CAT_TOPWAY_CMD }
        val broadcasts =
            sessionEvents.filter { it.category == DiagnosticJournal.CAT_TOPWAY_BROADCAST }

        if (cmds.isNotEmpty()) {
            sb.append("Observed ${cmds.size} Topway commands during capture.\n")
        } else {
            sb.append("No Topway commands observed by Auxio.\n")
        }

        if (broadcasts.isNotEmpty()) {
            sb.append("Observed ${broadcasts.size} metadata/progress broadcasts sent.\n")
        }

        val analysis =
            when {
                cmds.isNotEmpty() && answers[1] == 1 ->
                    "Auxio activity launched and Topway commands received. [Success]"
                cmds.isNotEmpty() && answers[1] == 2 ->
                    "Auxio received controls but the card opened stock music. [Partial/Ambiguous]"
                cmds.isEmpty() && answers[1] == 2 ->
                    "No Auxio events observed and user reports stock app opened. [Likely stock priority]"
                else -> "Inconclusive results."
            }
        sb.append("\nConclusion: $analysis\n")

        return sb.toString()
    }

    fun buildFullReport(): String {
        val guided = (guidedTestState.value as? GuidedTestState.Result)?.report
        return reportGenerator.generate(
            automatedReport.value ?: emptyList(),
            journalEvents.value,
            guided,
        )
    }

    fun startTimedCapture(minutes: Int) {
        val boundedMinutes = minutes.coerceIn(2, 15)
        val durationMs = boundedMinutes.toLong().times(60_000L)
        val sessionId = "timed-${boundedMinutes}m-${UUID.randomUUID().toString().take(8)}"
        DiagnosticService.start(context, sessionId, durationMs, DiagnosticService.ORIGIN_TIMED)
    }

    fun armBootCapture() {
        val id = "boot-${UUID.randomUUID().toString().take(8)}"
        diagSettings.armCapture(
            id,
            System.currentTimeMillis() + (24 * 60 * 60 * 1000L), // 24h
            DiagnosticService.ORIGIN_APP_START_FALLBACK,
            BOOT_CAPTURE_DURATION_MS,
        )
        journal.log(DiagnosticJournal.CAT_SYSTEM, "Capture armed for next start", "ID: $id")
    }

    fun disarmBootCapture() {
        diagSettings.clearArmedCapture()
        journal.log(DiagnosticJournal.CAT_SYSTEM, "Capture disarmed")
    }

    fun stopCapture() {
        restoreDiagnosticMarker()
        DiagnosticService.stop(context, guidedSessionId ?: journal.activeSessionId)
    }

    private fun restoreDiagnosticMarker() {
        markerController.restoreCurrentMetadata()
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

    fun discoverWritableDestinations(context: Context): List<File> {
        val out = linkedSetOf<File>()
        context.getExternalFilesDir(null)?.let { if (isWritableDirectory(it)) out += it }
        listOf(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
            )
            .filterTo(out) { isWritableDirectory(it) }
        TopwaySourcePolicy.discoverCandidateRoots().map(::File).filterTo(out) {
            isWritableDirectory(it)
        }
        return out.toList()
    }

    private fun isWritableDirectory(file: File): Boolean =
        try {
            file.exists() && file.isDirectory && file.canWrite()
        } catch (e: Exception) {
            L.w(e, "Destination is not writable: ${file.absolutePath}")
            false
        }

    fun saveReport(destination: File, report: String): File? {
        return try {
            if (!destination.exists() && !destination.mkdirs()) return null
            if (!destination.isDirectory || !destination.canWrite()) return null
            val stamp = SimpleDateFormat("yyyyMMdd-HHmmss", Locale.US).format(Date())
            val out = File(destination, "Auxio-TS-diagnostics-$stamp.txt")
            out.writeText(report, Charsets.UTF_8)
            out
        } catch (e: Exception) {
            L.w(e, "Failed to save diagnostics report to ${destination.absolutePath}")
            null
        }
    }

    private fun findNoisyPaths(): List<String> {
        val candidates = mutableListOf<String>()
        val roots = TopwaySourcePolicy.discoverCandidateRoots().map(::File)

        for (root in roots) {
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

    companion object {
        const val GUIDED_CAPTURE_DURATION_MS = 15 * 60 * 1000L
        const val BOOT_CAPTURE_DURATION_MS = 5 * 60 * 1000L
    }
}
