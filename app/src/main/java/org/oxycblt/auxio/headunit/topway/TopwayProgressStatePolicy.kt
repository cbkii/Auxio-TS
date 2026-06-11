/*
 * Copyright (c) 2024 Auxio Project
 * TopwayProgressStatePolicy.kt is part of Auxio.
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

data class TopwayProgressSnapshot(val progressMs: Long, val durationMs: Long)

object TopwayProgressStatePolicy {
    val CLEAR = TopwayProgressSnapshot(0L, 0L)

    fun active(progressMs: Long, durationMs: Long): TopwayProgressSnapshot? {
        if (durationMs <= 0L) return null
        return TopwayProgressSnapshot(progressMs.coerceIn(0L, durationMs), durationMs)
    }

    fun shouldPublish(
        next: TopwayProgressSnapshot,
        last: TopwayProgressSnapshot?,
        nowMs: Long,
        lastAtMs: Long,
        minIntervalMs: Long,
    ): Boolean {
        if (last == null) return true
        if (last == CLEAR) return true
        if (next.durationMs != last.durationMs) return true

        val elapsedMs = nowMs - lastAtMs
        if (elapsedMs >= minIntervalMs) return true
        if (elapsedMs < 0L) return false

        val expectedProgressMs = (last.progressMs + elapsedMs).coerceIn(0L, next.durationMs)
        val deviationMs = kotlin.math.abs(next.progressMs - expectedProgressMs)

        return deviationMs >= minIntervalMs
    }
}
