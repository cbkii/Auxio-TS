/*
 * Copyright (c) 2026 Auxio Project
 * TopwaySeekUnitPolicy.kt is part of Auxio.
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

enum class TopwaySeekUnitPolicy {
    Auto,
    Milliseconds,
    Seconds,
    Percent0To100,
    Permille0To1000,
}

data class TopwaySeekDecision(
    val positionMs: Long?,
    val unit: TopwaySeekUnitPolicy?,
    val detail: String,
)

object TopwaySeekPolicyConverter {
    fun convert(
        rawProgress: Any?,
        durationMs: Long?,
        policy: TopwaySeekUnitPolicy,
    ): TopwaySeekDecision {
        if (durationMs == null || durationMs <= 0L) {
            return TopwaySeekDecision(null, null, "ignored: unknown duration")
        }
        val raw =
            when (rawProgress) {
                is Int -> rawProgress.toLong()
                is Long -> rawProgress
                is Short -> rawProgress.toLong()
                is String -> rawProgress.toLongOrNull()
                else -> null
            } ?: return TopwaySeekDecision(null, null, "ignored: unsupported raw value")
        if (raw < 0L) {
            return TopwaySeekDecision(null, null, "ignored: negative raw value $raw")
        }
        val decision =
            if (policy == TopwaySeekUnitPolicy.Auto) chooseAuto(raw, durationMs) else policy
        val position =
            when (decision) {
                TopwaySeekUnitPolicy.Auto -> raw.coerceIn(0L, durationMs)
                TopwaySeekUnitPolicy.Milliseconds -> raw.coerceIn(0L, durationMs)
                TopwaySeekUnitPolicy.Seconds ->
                    raw.saturatingMultiply(1000L).coerceIn(0L, durationMs)
                TopwaySeekUnitPolicy.Percent0To100 -> {
                    if (raw > 100L) {
                        return TopwaySeekDecision(
                            null,
                            decision,
                            "ignored: percent raw value $raw outside 0..100",
                        )
                    }
                    durationMs.saturatingMultiply(raw) / 100L
                }
                TopwaySeekUnitPolicy.Permille0To1000 -> {
                    if (raw > 1000L) {
                        return TopwaySeekDecision(
                            null,
                            decision,
                            "ignored: permille raw value $raw outside 0..1000",
                        )
                    }
                    durationMs.saturatingMultiply(raw) / 1000L
                }
            }
        return TopwaySeekDecision(
            position,
            decision,
            "unit=$decision raw=$raw duration=$durationMs position=$position",
        )
    }

    /**
     * Auto is intentionally deterministic, not magical: small widget values are more likely to be
     * normalized seekbar values than useful millisecond offsets, while large values are treated as
     * seconds only when that can fit inside the known track duration; otherwise they are clamped as
     * milliseconds. Explicit settings are the authority when TS18 validation proves another unit.
     */
    private fun chooseAuto(value: Long, durationMs: Long): TopwaySeekUnitPolicy =
        when {
            value <= 100L -> TopwaySeekUnitPolicy.Percent0To100
            value <= 1000L -> TopwaySeekUnitPolicy.Permille0To1000
            value <= Long.MAX_VALUE / 1000L && value * 1000L <= durationMs ->
                TopwaySeekUnitPolicy.Seconds
            else -> TopwaySeekUnitPolicy.Milliseconds
        }

    private fun Long.saturatingMultiply(other: Long): Long =
        if (this != 0L && other > Long.MAX_VALUE / this) Long.MAX_VALUE else this * other
}
