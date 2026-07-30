/*
 * Copyright (c) 2026 Auxio Project
 * DiagnosticJournal.kt is part of Auxio.
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

import androidx.annotation.VisibleForTesting
import java.io.File
import java.util.Collections
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Lightweight runtime event journal for boot, playback, widget, overlay, and Topway bridge
 * observability. This is not the abandoned in-app TS18 diagnostics UI/service/capture path;
 * external Magisk/service.d tooling reads logcat and app-visible runtime markers instead.
 */
@Singleton
class DiagnosticJournal @Inject constructor() {

    private val _events = MutableStateFlow<List<DiagnosticEvent>>(emptyList())
    /** A flow of the latest diagnostic events. */
    val events: StateFlow<List<DiagnosticEvent>> = _events.asStateFlow()

    private val eventList = Collections.synchronizedList(mutableListOf<DiagnosticEvent>())

    @Volatile private var currentSessionId: String? = null
    @Volatile private var persistenceDirectory: File? = null
    private var writesSinceLastPrune = 0
    private val persistenceExecutor: ExecutorService =
        Executors.newSingleThreadExecutor { runnable ->
            Thread(runnable, "AuxioDiagnosticJournal").apply { isDaemon = true }
        }

    val activeSessionId: String?
        get() = currentSessionId

    val hasActiveSession: Boolean
        get() = currentSessionId != null

    /**
     * Enables bounded process-death-safe storage. Recovery and pruning run on the journal worker so
     * application startup never blocks on diagnostic I/O.
     */
    fun configurePersistence(directory: File) {
        persistenceDirectory = directory
        persistenceExecutor.execute {
            if (!directory.exists() && !directory.mkdirs()) return@execute
            recoverInterruptedSessions(directory)
            prune(directory)
            writesSinceLastPrune = 0
        }
    }

    /** A stable memory snapshot for deterministic exports. */
    fun snapshot(): List<DiagnosticEvent> = synchronized(eventList) { eventList.toList() }

    /** Existing bounded session files, newest first. */
    fun persistedFiles(): List<File> =
        persistenceDirectory
            ?.listFiles { file ->
                file.isFile && (file.name.endsWith(".jsonl") || file.name.endsWith(".summary.json"))
            }
            ?.sortedByDescending { it.lastModified() }
            .orEmpty()

    @VisibleForTesting
    internal fun awaitPendingWrites(timeoutMs: Long = 5_000L) {
        persistenceExecutor.submit {}.get(timeoutMs, TimeUnit.MILLISECONDS)
    }

    /** Records a new diagnostic event for the active session, no-oping while inactive. */
    fun log(
        category: String,
        event: String,
        detail: String? = null,
        result: String? = null,
        evidence: EvidenceClassification = EvidenceClassification.OBSERVED_BY_AUXIO,
    ) {
        synchronized(eventList) {
            val sessionId = currentSessionId
            if (sessionId == null && category != CAT_SESSION) return
            appendLocked(sessionId, category, event, detail, result, evidence)
        }
    }

    /** Starts a new capture session identifier. */
    fun startSession(id: String): Boolean =
        synchronized(eventList) {
            if (currentSessionId != null) return@synchronized false
            currentSessionId = id
            appendLocked(id, CAT_SESSION, "Started", "Session ID: $id")
            persistSessionMarker(id, active = true)
            true
        }

    /** Clears the current session identifier if it still owns the active capture. */
    fun endSession(id: String? = null): Boolean =
        synchronized(eventList) {
            val sessionId = currentSessionId ?: return@synchronized false
            if (id != null && id != sessionId) return@synchronized false
            appendLocked(sessionId, CAT_SESSION, "Ended", "Session ID: $sessionId")
            currentSessionId = null
            persistSessionMarker(sessionId, active = false)
            persistSummary(sessionId, "ENDED")
            true
        }

    private fun appendLocked(
        sessionId: String?,
        category: String,
        event: String,
        detail: String? = null,
        result: String? = null,
        evidence: EvidenceClassification = EvidenceClassification.OBSERVED_BY_AUXIO,
    ) {
        val diagnosticEvent =
            DiagnosticEvent(
                sessionId = sessionId,
                category = category,
                event = event,
                detail = detail,
                result = result,
                evidence = evidence,
            )
        eventList.add(diagnosticEvent)
        while (eventList.size > MAX_EVENT_COUNT) eventList.removeAt(0)
        // Always update the Flow so that new subscribers (like the UI) receive the latest
        // event history.
        _events.value = eventList.toList()
        persist(diagnosticEvent)
    }

    /** Clears all recorded events. */
    fun clear() {
        synchronized(eventList) {
            eventList.clear()
            _events.value = emptyList()
        }
    }

    private fun persist(event: DiagnosticEvent) {
        val directory = persistenceDirectory ?: return
        val sessionId = event.sessionId ?: return
        persistenceExecutor.execute {
            if (!directory.exists() && !directory.mkdirs()) return@execute
            val file = File(directory, "session-${safeName(sessionId)}.jsonl")
            if (file.length() >= MAX_SESSION_BYTES) return@execute
            val persisted =
                runCatching { file.appendText(toJsonLine(event) + "\n", Charsets.UTF_8) }.isSuccess
            if (persisted && ++writesSinceLastPrune >= PRUNE_WRITE_INTERVAL) {
                prune(directory)
                writesSinceLastPrune = 0
            }
        }
    }

    private fun persistSessionMarker(sessionId: String, active: Boolean) {
        val directory = persistenceDirectory ?: return
        persistenceExecutor.execute {
            if (!directory.exists() && !directory.mkdirs()) return@execute
            val marker = File(directory, ".active-${safeName(sessionId)}")
            if (active) {
                runCatching { marker.writeText(sessionId, Charsets.UTF_8) }
            } else {
                marker.delete()
            }
        }
    }

    private fun persistSummary(sessionId: String, outcome: String) {
        val directory = persistenceDirectory ?: return
        persistenceExecutor.execute {
            if (!directory.exists() && !directory.mkdirs()) return@execute
            val target = File(directory, "session-${safeName(sessionId)}.summary.json")
            val partial = File(directory, "${target.name}.partial")
            val body =
                """{"schema":1,"sessionId":${DiagnosticJson.string(sessionId)},"outcome":${DiagnosticJson.string(outcome)},"wallTime":${System.currentTimeMillis()}}"""
            runCatching {
                partial.writeText(body + "\n", Charsets.UTF_8)
                if (!partial.renameTo(target)) {
                    target.writeText(body + "\n", Charsets.UTF_8)
                    partial.delete()
                }
            }
            prune(directory)
            writesSinceLastPrune = 0
        }
    }

    private fun recoverInterruptedSessions(directory: File) {
        directory
            .listFiles { file -> file.isFile && file.name.startsWith(".active-") }
            .orEmpty()
            .forEach { marker ->
                val sessionId =
                    runCatching { marker.readText(Charsets.UTF_8).trim() }
                        .getOrNull()
                        .orEmpty()
                        .ifBlank { marker.name.removePrefix(".active-") }
                val recovery =
                    DiagnosticEvent(
                        sessionId = sessionId,
                        category = CAT_SESSION,
                        event = "Interrupted",
                        detail = "Recovered an unfinished diagnostic session after process restart",
                        result = "INTERRUPTED",
                    )
                val eventFile = File(directory, "session-${safeName(sessionId)}.jsonl")
                if (eventFile.length() < MAX_SESSION_BYTES) {
                    runCatching {
                        eventFile.appendText(toJsonLine(recovery) + "\n", Charsets.UTF_8)
                    }
                }
                val target = File(directory, "session-${safeName(sessionId)}.summary.json")
                runCatching {
                    target.writeText(
                        """{"schema":1,"sessionId":${DiagnosticJson.string(sessionId)},"outcome":"INTERRUPTED","wallTime":${System.currentTimeMillis()}}""" +
                            "\n",
                        Charsets.UTF_8,
                    )
                }
                marker.delete()
            }
    }

    private fun prune(directory: File) {
        val files =
            directory
                .listFiles { file ->
                    file.isFile &&
                        (file.name.endsWith(".jsonl") || file.name.endsWith(".summary.json"))
                }
                ?.sortedByDescending { it.lastModified() }
                .orEmpty()
        var retainedBytes = 0L
        files.forEachIndexed { index, file ->
            val length = file.length()
            val keep =
                index < MAX_PERSISTED_FILES && length <= MAX_TOTAL_BYTES - retainedBytes
            if (keep) {
                retainedBytes += length
            } else {
                file.delete()
            }
        }
    }

    companion object {
        private const val MAX_EVENT_COUNT = 1000
        private const val MAX_SESSION_BYTES = 1_048_576L
        private const val MAX_TOTAL_BYTES = 5_242_880L
        private const val MAX_PERSISTED_FILES = 10
        private const val PRUNE_WRITE_INTERVAL = 64

        private fun safeName(value: String): String =
            value.replace(Regex("[^A-Za-z0-9._-]"), "_").take(120).ifBlank { "unknown" }

        /** Canonical serializer shared by persisted sessions and bundle exports. */
        @VisibleForTesting
        internal fun toJsonLine(event: DiagnosticEvent): String =
            with(event) {
                buildString {
                    append(
                        """{"schema":1,"wallTime":$wallTime,"monotonicTime":$monotonicTime"""
                    )
                    append(""","sessionId":${DiagnosticJson.string(sessionId)}""")
                    append(
                        ""","category":${DiagnosticJson.string(category)},"event":${DiagnosticJson.string(event.event)}"""
                    )
                    append(
                        ""","detail":${DiagnosticJson.string(detail)},"result":${DiagnosticJson.string(result)}"""
                    )
                    append(""","evidence":${DiagnosticJson.string(evidence.name)}}""")
                }
            }

        // Categories
        const val CAT_SESSION = "SESSION"
        const val CAT_LIFECYCLE = "Lifecycle"
        const val CAT_INTENT = "Intent"
        const val CAT_TOPWAY_CMD = "TopwayCmd"
        const val CAT_TOPWAY_BROADCAST = "TopwayBroadcast"
        const val CAT_PLAYBACK = "Playback"
        const val CAT_NOTIFICATION = "Notification"
        const val CAT_WIDGET = "Widget"
        const val CAT_OVERLAY = "Overlay"
        const val CAT_STORAGE = "Storage"
        const val CAT_INDEXING = "Indexing"
        const val CAT_SYSTEM = "System"
        const val CAT_BOOT = "Boot"
    }
}
