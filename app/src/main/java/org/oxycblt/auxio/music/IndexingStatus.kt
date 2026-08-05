/*
 * Copyright (c) 2026 Auxio Project
 * IndexingStatus.kt is part of Auxio.
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

package org.oxycblt.auxio.music

import org.oxycblt.musikr.IndexingPhase

enum class IndexingTerminalOutcome {
    SUCCESS,
    PARTIAL_SUCCESS,
    SOURCE_UNAVAILABLE,
    FAILED,
    CANCELLED,
    SERVICE_STOPPED,
    SUPERSEDED,
    TIMED_OUT,
}

class IndexingInterruptedException(val outcome: IndexingTerminalOutcome, detail: String? = null) :
    IllegalStateException(
        detail
            ?: when (outcome) {
                IndexingTerminalOutcome.CANCELLED -> "Music loading was cancelled"
                IndexingTerminalOutcome.SERVICE_STOPPED ->
                    "Music loading stopped because the playback service was destroyed"
                IndexingTerminalOutcome.SUPERSEDED ->
                    "Music loading was superseded by a newer source configuration"
                IndexingTerminalOutcome.TIMED_OUT ->
                    "Music loading stopped after its stage-aware safety limit"
                IndexingTerminalOutcome.SOURCE_UNAVAILABLE ->
                    "A configured music source is unavailable"
                IndexingTerminalOutcome.PARTIAL_SUCCESS ->
                    "Music loading completed with one or more unresolved sources"
                IndexingTerminalOutcome.FAILED -> "Music loading failed"
                IndexingTerminalOutcome.SUCCESS -> "Music loading finished"
            }
    )

enum class IndexingWatchdogState {
    HEALTHY,
    STALLED,
    NO_PROGRESS_TIMEOUT,
    OVERDUE,
}

enum class IndexingSourceScope {
    NARROW,
    WHOLE_VOLUME,
    MIXED,
    UNKNOWN,
}

data class IndexingWatchdogInput(
    val nowElapsedMs: Long,
    val startedAtElapsedMs: Long,
    val lastProgressAtElapsedMs: Long,
    val phase: IndexingPhase,
    val firstFileEmitted: Boolean,
    val sourceScope: IndexingSourceScope,
    val explored: Int = 0,
    val loaded: Int = 0,
    val evaluated: Int = 0,
    val currentItem: String? = null,
    val directFsDirectoriesVisited: Int? = null,
    val directFsEntriesInspected: Int? = null,
    val directFsFilesEmitted: Int? = null,
    val queuedDirectFsWork: Int? = null,
    val activeDirectFsEnumerators: Int? = null,
    val nonAuthoritativeWorkDeferred: Boolean = false,
)

data class IndexingWatchdogDecision(
    val state: IndexingWatchdogState,
    val noProgressMs: Long,
    val noProgressDeadlineMs: Long,
    val totalElapsedMs: Long,
    val shouldTerminate: Boolean,
    val detail: String,
)

/** Pure, independently testable elapsed/no-progress policy shared by the service and UI. */
object IndexingWatchdogPolicy {
    const val STALL_WARNING_MS = 60_000L
    const val MAX_SCAN_ELAPSED_MS = 30 * 60_000L
    const val NARROW_DISCOVERY_NO_PROGRESS_MS = 3 * 60_000L
    const val UNKNOWN_DISCOVERY_NO_PROGRESS_MS = 4 * 60_000L
    const val WHOLE_VOLUME_DISCOVERY_NO_PROGRESS_MS = 5 * 60_000L
    const val ACTIVE_STAGE_NO_PROGRESS_MS = 5 * 60_000L
    const val FINALISING_NO_PROGRESS_MS = 4 * 60_000L

    fun classify(input: IndexingWatchdogInput): IndexingWatchdogDecision {
        val totalElapsed = elapsed(input.nowElapsedMs, input.startedAtElapsedMs)
        val noProgress = elapsed(input.nowElapsedMs, input.lastProgressAtElapsedMs)
        val deadline = noProgressDeadline(input)
        if (totalElapsed >= MAX_SCAN_ELAPSED_MS) {
            return IndexingWatchdogDecision(
                state = IndexingWatchdogState.OVERDUE,
                noProgressMs = noProgress,
                noProgressDeadlineMs = deadline,
                totalElapsedMs = totalElapsed,
                shouldTerminate = true,
                detail =
                    "overall-cap phase=${input.phase} elapsedMs=$totalElapsed " +
                        "noProgressMs=$noProgress",
            )
        }
        if (noProgress >= deadline) {
            return IndexingWatchdogDecision(
                state = IndexingWatchdogState.NO_PROGRESS_TIMEOUT,
                noProgressMs = noProgress,
                noProgressDeadlineMs = deadline,
                totalElapsedMs = totalElapsed,
                shouldTerminate = true,
                detail =
                    "no-progress phase=${input.phase} firstFile=${input.firstFileEmitted} " +
                        "scope=${input.sourceScope} explored=${input.explored} " +
                        "loaded=${input.loaded} evaluated=${input.evaluated} " +
                        "item=${input.currentItem} directories=" +
                        "${input.directFsDirectoriesVisited} entries=" +
                        "${input.directFsEntriesInspected} files=" +
                        "${input.directFsFilesEmitted} queued=${input.queuedDirectFsWork} " +
                        "active=${input.activeDirectFsEnumerators} elapsedMs=$noProgress",
            )
        }
        val state =
            if (noProgress >= STALL_WARNING_MS) {
                IndexingWatchdogState.STALLED
            } else {
                IndexingWatchdogState.HEALTHY
            }
        return IndexingWatchdogDecision(
            state = state,
            noProgressMs = noProgress,
            noProgressDeadlineMs = deadline,
            totalElapsedMs = totalElapsed,
            shouldTerminate = false,
            detail =
                "phase=${input.phase} noProgressMs=$noProgress deadlineMs=$deadline " +
                    "deferred=${input.nonAuthoritativeWorkDeferred}",
        )
    }

    private fun noProgressDeadline(input: IndexingWatchdogInput): Long =
        when (input.phase) {
            IndexingPhase.PREPARING -> NARROW_DISCOVERY_NO_PROGRESS_MS
            IndexingPhase.DISCOVERING ->
                if (input.firstFileEmitted) {
                    ACTIVE_STAGE_NO_PROGRESS_MS
                } else {
                    when (input.sourceScope) {
                        IndexingSourceScope.NARROW -> NARROW_DISCOVERY_NO_PROGRESS_MS
                        IndexingSourceScope.WHOLE_VOLUME,
                        IndexingSourceScope.MIXED -> WHOLE_VOLUME_DISCOVERY_NO_PROGRESS_MS
                        IndexingSourceScope.UNKNOWN -> UNKNOWN_DISCOVERY_NO_PROGRESS_MS
                    }
                }
            IndexingPhase.EXTRACTING,
            IndexingPhase.EVALUATING -> ACTIVE_STAGE_NO_PROGRESS_MS
            IndexingPhase.FINALISING -> FINALISING_NO_PROGRESS_MS
        }

    private fun elapsed(nowMs: Long, baselineMs: Long): Long =
        if (baselineMs <= 0L || nowMs <= baselineMs) 0L else nowMs - baselineMs
}

/** Rejects stale progress/completion callbacks and more than one terminal callback per session. */
internal class IndexingSessionGate {
    private var activeSessionId: Long? = null

    fun begin(sessionId: Long) {
        activeSessionId = sessionId
    }

    fun isCurrent(sessionId: Long): Boolean = activeSessionId == sessionId

    fun complete(sessionId: Long): Boolean {
        if (activeSessionId != sessionId) return false
        activeSessionId = null
        return true
    }
}
