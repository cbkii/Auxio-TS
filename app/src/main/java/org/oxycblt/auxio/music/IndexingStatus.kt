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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.oxycblt.auxio.music

enum class IndexingTerminalOutcome {
    SUCCESS,
    FAILED,
    CANCELLED,
    SERVICE_STOPPED,
    TIMED_OUT,
}

class IndexingInterruptedException(val outcome: IndexingTerminalOutcome) :
    IllegalStateException(
        when (outcome) {
            IndexingTerminalOutcome.CANCELLED -> "Music loading was cancelled"
            IndexingTerminalOutcome.SERVICE_STOPPED ->
                "Music loading stopped because the playback service was destroyed"
            IndexingTerminalOutcome.TIMED_OUT ->
                "Music loading stopped after reaching the 30-minute safety limit"
            IndexingTerminalOutcome.FAILED -> "Music loading failed"
            IndexingTerminalOutcome.SUCCESS -> "Music loading finished"
        }
    )

enum class IndexingWatchdogState {
    HEALTHY,
    STALLED,
    OVERDUE,
}

/** Pure elapsed/no-progress policy shared by the service and UI. */
object IndexingWatchdogPolicy {
    const val STALL_WARNING_MS = 60_000L
    const val MAX_SCAN_ELAPSED_MS = 30 * 60_000L

    fun classify(
        nowElapsedMs: Long,
        startedAtElapsedMs: Long,
        lastProgressAtElapsedMs: Long,
    ): IndexingWatchdogState {
        if (startedAtElapsedMs > 0L && nowElapsedMs - startedAtElapsedMs >= MAX_SCAN_ELAPSED_MS) {
            return IndexingWatchdogState.OVERDUE
        }
        if (
            lastProgressAtElapsedMs > 0L &&
                nowElapsedMs - lastProgressAtElapsedMs >= STALL_WARNING_MS
        ) {
            return IndexingWatchdogState.STALLED
        }
        return IndexingWatchdogState.HEALTHY
    }
}
