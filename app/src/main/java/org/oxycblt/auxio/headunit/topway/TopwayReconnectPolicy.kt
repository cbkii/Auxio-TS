/*
 * Copyright (c) 2026 Auxio Project
 * TopwayReconnectPolicy.kt is part of Auxio.
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

/** One bounded reconnect decision for a long-lived Topway integration endpoint. */
internal data class TopwayReconnectDecision(
    val delayMs: Long,
    val nextAttempt: Int,
    val cooldown: Boolean,
)

/**
 * Proxy-style reconnect policy: retry transient failures quickly, then keep a low-rate watcher.
 *
 * A TS18 service can appear after Auxio during boot/ACC wake or be recreated independently while
 * Auxio remains alive. Permanently exhausting a short retry list therefore turns a transient race
 * into a process-lifetime integration failure. This policy keeps each retry burst bounded while
 * re-arming after a quiet cooldown. It owns no thread/timer itself; callers still maintain exactly
 * one delayed runnable and cancel it at their lifecycle boundary.
 */
internal object TopwayReconnectPolicy {
    private val BURST_DELAYS_MS = longArrayOf(500L, 1_500L, 3_000L)
    const val COOLDOWN_DELAY_MS = 30_000L

    fun next(attempt: Int): TopwayReconnectDecision {
        val safeAttempt = attempt.coerceAtLeast(0)
        return if (safeAttempt < BURST_DELAYS_MS.size) {
            TopwayReconnectDecision(
                delayMs = BURST_DELAYS_MS[safeAttempt],
                nextAttempt = safeAttempt + 1,
                cooldown = false,
            )
        } else {
            TopwayReconnectDecision(delayMs = COOLDOWN_DELAY_MS, nextAttempt = 0, cooldown = true)
        }
    }
}
