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

import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/** Release implementation: diagnostics remain compile-time compatible and perform no work. */
@Suppress("UNUSED_PARAMETER")
@Singleton
class DiagnosticJournal @Inject constructor() {
    val events: StateFlow<List<DiagnosticEvent>> = MutableStateFlow(emptyList())
    val activeSessionId: String? = null
    val hasActiveSession: Boolean = false

    fun configurePersistence(directory: File) = Unit

    fun snapshot(): List<DiagnosticEvent> = emptyList()

    fun persistedFiles(): List<File> = emptyList()

    fun log(
        category: String,
        event: String,
        detail: String? = null,
        result: String? = null,
        evidence: EvidenceClassification = EvidenceClassification.OBSERVED_BY_AUXIO,
    ) = Unit

    fun startSession(id: String): Boolean = false

    fun endSession(id: String? = null): Boolean = false

    fun clear() = Unit

    companion object {
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
