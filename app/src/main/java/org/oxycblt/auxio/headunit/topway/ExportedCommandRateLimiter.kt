/*
 * Copyright (c) 2026 Auxio Project
 * ExportedCommandRateLimiter.kt is part of Auxio.
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

package org.oxycblt.auxio.headunit.topway

import android.os.SystemClock
import java.util.concurrent.ConcurrentHashMap

/** Small fixed-key limiter for exported receiver and service command surfaces. */
internal class EventRateLimiter(private val elapsedRealtime: () -> Long) {
    private data class Window(var startedAtMs: Long, var count: Int)

    private val windows = ConcurrentHashMap<String, Window>()

    fun allow(key: String, maxEvents: Int, windowMs: Long): Boolean {
        require(key.length <= MAX_KEY_LENGTH)
        require(maxEvents > 0)
        require(windowMs > 0)
        val window = windows.getOrPut(key) { Window(elapsedRealtime(), 0) }
        synchronized(window) {
            val now = elapsedRealtime()
            if (now < window.startedAtMs || now - window.startedAtMs >= windowMs) {
                window.startedAtMs = now
                window.count = 0
            }
            if (window.count >= maxEvents) return false
            window.count++
            return true
        }
    }

    private companion object {
        const val MAX_KEY_LENGTH = 128
    }
}

internal object ExportedCommandRateLimiter {
    private val delegate = EventRateLimiter(SystemClock::elapsedRealtime)

    fun allow(key: String, maxEvents: Int, windowMs: Long): Boolean =
        delegate.allow(key, maxEvents, windowMs)
}
