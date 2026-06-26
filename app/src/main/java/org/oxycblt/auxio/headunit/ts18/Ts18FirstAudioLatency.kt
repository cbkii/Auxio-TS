/*
 * Copyright (c) 2026 Auxio-TS Project
 * Ts18FirstAudioLatency.kt is part of Auxio-TS.
 */

package org.oxycblt.auxio.headunit.ts18

import android.os.SystemClock
import timber.log.Timber as L

/** Lightweight local-only first-audio latency markers for TS18 validation. */
object Ts18FirstAudioLatency {
    private val startMs = SystemClock.elapsedRealtime()

    fun mark(stage: String) {
        val elapsed = SystemClock.elapsedRealtime() - startMs
        L.i("TS18 first-audio latency: ${stage} at ${elapsed}ms")
    }
}
