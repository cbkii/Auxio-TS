/*
 * Copyright (c) 2026 Auxio Project
 * StartupPerformanceReport.kt is part of Auxio.
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

package org.oxycblt.auxio.util

import android.os.SystemClock
import java.io.File
import org.oxycblt.auxio.BuildConfig

/** Builds a bounded, local-only startup evidence report after an explicit user capture. */
object StartupPerformanceReport {
    data class CaptureContext(
        val authority: String,
        val sourceState: String = "not-recorded",
        val fixtureSongCount: Int? = null,
        val commit: String? = null,
    )

    fun render(
        captureContext: CaptureContext,
        events: List<PerfTimer.Event> = PerfTimer.snapshot(),
        bootId: String? = readBootId(),
        reportElapsedRealtimeMs: Long = SystemClock.elapsedRealtime(),
    ): String {
        val boundedEvents = events.takeLast(MAX_REPORT_EVENTS)
        val origin = boundedEvents.firstOrNull()?.elapsedRealtimeMs
        return buildString {
            appendLine("Auxio-TS startup performance report")
            appendLine("authority=${captureContext.authority}")
            appendLine("applicationId=${BuildConfig.APPLICATION_ID}")
            appendLine("version=${BuildConfig.VERSION_NAME}")
            appendLine("variant=${BuildConfig.FLAVOR}${BuildConfig.BUILD_TYPE}")
            appendLine("commit=${captureContext.commit ?: "not-recorded"}")
            appendLine("bootId=${bootId ?: "unavailable"}")
            appendLine("reportElapsedRealtimeMs=$reportElapsedRealtimeMs")
            appendLine("sourceState=${captureContext.sourceState}")
            appendLine("fixtureSongCount=${captureContext.fixtureSongCount ?: "not-recorded"}")
            appendLine("eventCount=${boundedEvents.size}")
            appendLine()
            boundedEvents.forEachIndexed { index, event ->
                val relative = origin?.let { event.elapsedRealtimeMs - it } ?: 0L
                append(index.toString().padStart(3, '0'))
                append(" t+")
                append(relative)
                append("ms label=")
                append(escape(event.label))
                event.durationMs?.let { duration ->
                    append(" durationMs=")
                    append(duration)
                }
                append(" pid=")
                append(event.processId)
                append(" thread=")
                append(escape(event.threadName))
                appendLine()
            }
        }
    }

    internal fun readBootId(): String? =
        runCatching {
                File("/proc/sys/kernel/random/boot_id").readText(Charsets.UTF_8).trim().takeIf {
                    it.isNotEmpty()
                }
            }
            .getOrNull()

    private fun escape(value: String): String =
        value.replace("\\", "\\\\").replace("\n", "\\n").replace("\r", "\\r")

    private const val MAX_REPORT_EVENTS = 256
}
