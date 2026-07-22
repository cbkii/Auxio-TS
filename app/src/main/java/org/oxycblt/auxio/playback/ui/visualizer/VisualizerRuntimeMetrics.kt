/*
 * Copyright (c) 2026 Auxio Project
 * VisualizerRuntimeMetrics.kt is part of Auxio.
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

package org.oxycblt.auxio.playback.ui.visualizer

import java.util.concurrent.atomic.AtomicLong
import org.oxycblt.auxio.diagnostics.DiagnosticJournal

/** Allocation/copy counters emitted only while a bounded diagnostic session is active. */
internal class VisualizerRuntimeMetrics(
    private val journal: DiagnosticJournal?,
    private val reportIntervalMs: Long = DEFAULT_REPORT_INTERVAL_MS,
) {
    private val frameCount = AtomicLong()
    private val copiedBytes = AtomicLong()
    private val copyNanos = AtomicLong()
    private val suppressedWaveforms = AtomicLong()
    private val watchdogRetries = AtomicLong()
    private val lastReportMs = AtomicLong()

    fun recordFrame(bytes: Int, elapsedCopyNanos: Long, nowMs: Long) {
        if (journal?.hasActiveSession != true || bytes <= 0) return
        frameCount.incrementAndGet()
        copiedBytes.addAndGet(bytes.toLong())
        copyNanos.addAndGet(elapsedCopyNanos.coerceAtLeast(0L))
        maybeReport(nowMs)
    }

    fun recordSuppressedWaveform() {
        if (journal?.hasActiveSession == true) suppressedWaveforms.incrementAndGet()
    }

    fun recordWatchdogRetry() {
        if (journal?.hasActiveSession == true) watchdogRetries.incrementAndGet()
    }

    fun flush(reason: String, nowMs: Long) {
        report(reason, nowMs, force = true)
    }

    private fun maybeReport(nowMs: Long) {
        if (journal?.hasActiveSession != true) return
        val previous = lastReportMs.get()
        if (previous != 0L && nowMs - previous < reportIntervalMs) return
        if (!lastReportMs.compareAndSet(previous, nowMs)) return
        report("periodic", nowMs, force = false)
    }

    private fun report(reason: String, nowMs: Long, force: Boolean) {
        if (journal?.hasActiveSession != true) {
            if (force) reset()
            return
        }
        if (force) lastReportMs.set(nowMs)
        val frames = frameCount.getAndSet(0L)
        val bytes = copiedBytes.getAndSet(0L)
        val nanos = copyNanos.getAndSet(0L)
        val suppressed = suppressedWaveforms.getAndSet(0L)
        val retries = watchdogRetries.getAndSet(0L)
        if (frames == 0L && suppressed == 0L && retries == 0L) return
        val averageCopyMicros = if (frames > 0L) nanos / frames / NANOS_PER_MICROSECOND else 0L
        journal.log(
            DiagnosticJournal.CAT_SYSTEM,
            "Visualizer performance",
            "reason=$reason frames=$frames copiedBytes=$bytes avgCopyUs=$averageCopyMicros suppressedWaveforms=$suppressed retries=$retries",
        )
    }

    private fun reset() {
        frameCount.set(0L)
        copiedBytes.set(0L)
        copyNanos.set(0L)
        suppressedWaveforms.set(0L)
        watchdogRetries.set(0L)
        lastReportMs.set(0L)
    }

    private companion object {
        const val DEFAULT_REPORT_INTERVAL_MS = 10_000L
        const val NANOS_PER_MICROSECOND = 1_000L
    }
}
