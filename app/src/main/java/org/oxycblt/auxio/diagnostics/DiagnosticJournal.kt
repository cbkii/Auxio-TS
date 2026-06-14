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

    private var currentSessionId: String? = null

    /** Records a new diagnostic event. */
    fun log(
        category: String,
        event: String,
        detail: String? = null,
        result: String? = null,
        evidence: EvidenceClassification = EvidenceClassification.OBSERVED_BY_AUXIO,
    ) {
        val entry =
            DiagnosticEvent(
                sessionId = currentSessionId,
                category = category,
                event = event,
                detail = detail,
                result = result,
                evidence = evidence,
            )

        synchronized(eventList) {
            eventList.add(entry)
            if (eventList.size > MAX_EVENT_COUNT) {
                eventList.removeAt(0)
            }
            _events.value = eventList.toList()
        }
    }

    /** Starts a new capture session identifier. */
    fun startSession(id: String): Boolean {
        if (currentSessionId != null) return false
        currentSessionId = id
        log("SESSION", "Started", "Session ID: $id")
        return true
    }

    /** Clears the current session identifier. */
    fun endSession() {
        if (currentSessionId == null) return
        log("SESSION", "Ended", "Session ID: $currentSessionId")
        currentSessionId = null
    }

    /**
     * Clears all recorded events.
     */
    fun clear() {
        synchronized(eventList) {
            eventList.clear()
            _events.value = emptyList()
        }
    }

    companion object {
        private const val MAX_EVENT_COUNT = 1000

        // Categories
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
