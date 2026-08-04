/*
 * Copyright (c) 2026 Auxio Project
 * PipelineTrace.kt is part of Auxio.
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

package org.oxycblt.musikr.pipeline

import android.os.SystemClock
import android.util.Log
import java.util.concurrent.atomic.AtomicIntegerArray
import java.util.concurrent.atomic.AtomicLong

/** Bounded milestone transitions of one scan attempt. */
internal enum class PipelineStage {
    SCAN_STARTED,
    ENUMERATION_STARTED,
    FIRST_FILE_EMITTED,
    CLASSIFICATION_STARTED,
    CLASSIFICATION_COMPLETED,
    EXTRACTION_STARTED,
    EXTRACTION_COMPLETED,
    EVALUATION_STARTED,
    EVALUATION_COMPLETED,
    ENUMERATION_COMPLETED,
    PIPELINE_COMPLETED,
    PIPELINE_CANCELLED,
    PIPELINE_FAILED,
}

/**
 * Records one bounded log line per pipeline milestone so a future stall can be localised to the
 * exact stage that never advanced.
 *
 * Each milestone is emitted at most once per scan session, so the total release logging volume of a
 * scan is a small constant regardless of library size. Callers mark lifecycle transitions outside
 * per-item hot loops; item counters remain atomic because stages update them concurrently.
 */
internal class PipelineTrace(private val sessionId: Long) {
    private val startedAtMs = SystemClock.elapsedRealtime()
    private val emitted = AtomicIntegerArray(PipelineStage.entries.size)
    private val exploredCount = AtomicLong(0)
    private val loadedCount = AtomicLong(0)
    private val evaluatedCount = AtomicLong(0)

    fun countExplored(): Long = exploredCount.incrementAndGet()

    fun countLoaded(): Long = loadedCount.incrementAndGet()

    fun countEvaluated(): Long = evaluatedCount.incrementAndGet()

    /** Emits [stage] at most once. */
    fun mark(stage: PipelineStage, detail: String? = null, error: Throwable? = null) {
        if (!emitted.compareAndSet(stage.ordinal, 0, 1)) return
        val message = buildString {
            append("scan=")
            append(sessionId)
            append(" stage=")
            append(stage)
            append(" elapsedMs=")
            append(SystemClock.elapsedRealtime() - startedAtMs)
            append(" explored=")
            append(exploredCount.get())
            append(" loaded=")
            append(loadedCount.get())
            append(" evaluated=")
            append(evaluatedCount.get())
            if (detail != null) {
                append(" detail=")
                append(detail)
            }
            if (error != null) {
                append(" error=")
                append(error.javaClass.simpleName)
                append(":")
                append(error.message)
            }
        }
        if (error != null) {
            Log.w(TAG, message)
        } else {
            Log.i(TAG, message)
        }
    }

    private companion object {
        const val TAG = "MusikrPipeline"
    }
}
