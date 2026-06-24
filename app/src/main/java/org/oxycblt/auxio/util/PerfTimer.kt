/*
 * Copyright (c) 2026 Auxio Project
 * PerfTimer.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.oxycblt.auxio.util

import android.os.SystemClock
import org.oxycblt.auxio.BuildConfig
import timber.log.Timber

/**
 * A lightweight timing helper for development/debug diagnostics. Instruments key spans without
 * release overhead.
 */
object PerfTimer {
    @PublishedApi internal const val TAG = "AuxioPerf"
    @PublishedApi internal const val NANOS_PER_MS = 1_000_000L

    /** Records a timing for a block of code. Only active in debug builds. */
    inline fun <T> trace(label: String, block: () -> T): T {
        if (!BuildConfig.DEBUG) return block()

        val start = SystemClock.elapsedRealtimeNanos()
        return try {
            block()
        } finally {
            val elapsedMs = (SystemClock.elapsedRealtimeNanos() - start) / NANOS_PER_MS
            Timber.tag(TAG).d("$label took ${elapsedMs}ms")
        }
    }

    /**
     * Records wall elapsed time for a suspend block. The duration includes suspension time,
     * dispatcher wait, and I/O wait; it is not CPU time. Only active in debug builds.
     */
    suspend inline fun <T> traceSuspend(label: String, crossinline block: suspend () -> T): T {
        if (!BuildConfig.DEBUG) return block()

        val start = SystemClock.elapsedRealtimeNanos()
        return try {
            block()
        } finally {
            val elapsedMs = (SystemClock.elapsedRealtimeNanos() - start) / NANOS_PER_MS
            Timber.tag(TAG).d("$label took ${elapsedMs}ms")
        }
    }

    /** Records a point in time with a label. */
    fun point(label: String) {
        if (!BuildConfig.DEBUG) return
        Timber.tag(TAG).d("POINT: $label at ${SystemClock.elapsedRealtime()}ms")
    }
}
