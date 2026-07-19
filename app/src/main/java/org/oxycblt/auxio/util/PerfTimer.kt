/*
 * Copyright (c) 2026 Auxio Project
 * PerfTimer.kt is part of Auxio.
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

import android.os.Process
import android.os.SystemClock
import androidx.annotation.VisibleForTesting
import java.util.ArrayDeque
import java.util.concurrent.atomic.AtomicBoolean
import org.oxycblt.auxio.BuildConfig
import timber.log.Timber

/**
 * Low-overhead bounded performance instrumentation.
 *
 * Debug and benchmark builds record by default. Release builds record only after an explicit user
 * setting calls [configure]. The in-memory ring never writes files and is capped to [MAX_EVENTS].
 */
object PerfTimer {
    @PublishedApi internal const val TAG = "AuxioPerf"
    @PublishedApi internal const val NANOS_PER_MS = 1_000_000L
    private const val MAX_EVENTS = 256

    data class Event(
        val label: String,
        val elapsedRealtimeMs: Long,
        val durationMs: Long?,
        val threadName: String,
        val processId: Int,
    )

    private val explicitlyEnabled = AtomicBoolean(false)
    private val events = ArrayDeque<Event>(MAX_EVENTS)
    private val lock = Any()

    /** Enable or disable bounded detailed capture in non-debug builds. */
    fun configure(enabled: Boolean) {
        explicitlyEnabled.set(enabled)
        if (!enabled && !BuildConfig.DEBUG && !isBenchmarkBuild()) clear()
    }

    /** Whether instrumentation is currently active. */
    fun isEnabled(): Boolean = BuildConfig.DEBUG || isBenchmarkBuild() || explicitlyEnabled.get()

    @VisibleForTesting internal fun isExplicitlyConfigured(): Boolean = explicitlyEnabled.get()

    private fun isBenchmarkBuild(): Boolean = BuildConfig.BUILD_TYPE == "benchmark"

    /** Return a stable snapshot of the bounded in-memory event ring. */
    fun snapshot(): List<Event> = synchronized(lock) { events.toList() }

    /** Clear captured events. */
    fun clear() = synchronized(lock) { events.clear() }

    /** Records a timing for a block of code when capture is enabled. */
    inline fun <T> trace(label: String, block: () -> T): T {
        if (!isEnabled()) return block()
        val start = SystemClock.elapsedRealtimeNanos()
        return try {
            block()
        } finally {
            record(label, (SystemClock.elapsedRealtimeNanos() - start) / NANOS_PER_MS)
        }
    }

    /** Records wall elapsed time for a suspend block when capture is enabled. */
    suspend inline fun <T> traceSuspend(label: String, crossinline block: suspend () -> T): T {
        if (!isEnabled()) return block()
        val start = SystemClock.elapsedRealtimeNanos()
        return try {
            block()
        } finally {
            record(label, (SystemClock.elapsedRealtimeNanos() - start) / NANOS_PER_MS)
        }
    }

    /** Records a labelled point in time. */
    fun point(label: String) {
        if (!isEnabled()) return
        record(label, null)
    }

    @PublishedApi
    internal fun record(label: String, durationMs: Long?) {
        val event =
            Event(
                label = label,
                elapsedRealtimeMs = SystemClock.elapsedRealtime(),
                durationMs = durationMs,
                threadName = Thread.currentThread().name,
                processId = Process.myPid(),
            )
        synchronized(lock) {
            while (events.size >= MAX_EVENTS) events.removeFirst()
            events.addLast(event)
        }
        if (durationMs == null) {
            Timber.tag(TAG).d("POINT: $label at ${event.elapsedRealtimeMs}ms [${event.threadName}]")
        } else {
            Timber.tag(TAG).d("$label took ${durationMs}ms [${event.threadName}]")
        }
    }
}
