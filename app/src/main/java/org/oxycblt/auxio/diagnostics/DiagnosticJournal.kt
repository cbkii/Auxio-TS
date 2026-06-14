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

import java.util.Collections
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Singleton journal for recording and retrieving diagnostic events. It is concurrency-safe and
 * automatically prunes old events.
 */
@Singleton
class DiagnosticJournal @Inject constructor() {

    private val _events = MutableStateFlow<List<DiagnosticEvent>>(emptyList())
    /** A flow of the latest diagnostic events. */
    val events: StateFlow<List<DiagnosticEvent>> = _events.asStateFlow()

    private val eventList = Collections.synchronizedList(mutableListOf<DiagnosticEvent>())

    @Volatile private var currentSessionId: String? = null

    val activeSessionId: String?
        get() = currentSessionId

    val hasActiveSession: Boolean
        get() = currentSessionId != null

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
            true
        }

    /** Clears the current session identifier if it still owns the active capture. */
    fun endSession(id: String? = null): Boolean =
        synchronized(eventList) {
            val sessionId = currentSessionId ?: return@synchronized false
            if (id != null && id != sessionId) return@synchronized false
            appendLocked(sessionId, CAT_SESSION, "Ended", "Session ID: $sessionId")
            currentSessionId = null
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
        eventList.add(
            DiagnosticEvent(
                sessionId = sessionId,
                category = category,
                event = event,
                detail = detail,
                result = result,
                evidence = evidence,
            )
        )
        while (eventList.size > MAX_EVENT_COUNT) eventList.removeAt(0)
        _events.value = eventList.toList()
    }

    /** Clears all recorded events. */
    fun clear() {
        synchronized(eventList) {
            eventList.clear()
            _events.value = emptyList()
        }
    }

    companion object {
        private const val MAX_EVENT_COUNT = 1000

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
        const val CAT_SYSTEM = "System"
        const val CAT_BOOT = "Boot"
    }
}
