/*
 * Copyright (c) 2026 Auxio Project
 * Ts18FirstAudioLatency.kt is part of Auxio.
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

package org.oxycblt.auxio.headunit.ts18

import android.os.SystemClock
import org.oxycblt.auxio.util.PerfTimer
import timber.log.Timber as L

/** Lightweight local-only first-audio latency markers for TS18 validation. */
object Ts18FirstAudioLatency {
    private val startMs = SystemClock.elapsedRealtime()

    fun mark(stage: String) {
        val elapsed = SystemClock.elapsedRealtime() - startMs
        PerfTimer.point("startup.audio.$stage")
        L.i("TS18 first-audio latency: ${stage} at ${elapsed}ms")
    }
}
