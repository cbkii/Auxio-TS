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
    val unit: TopwaySeekUnitPolicy,
    val detail: String,
)

object TopwaySeekPolicyConverter {
    fun convert(
        rawProgress: Any?,
        durationMs: Long?,
        policy: TopwaySeekUnitPolicy,
    ): TopwaySeekDecision {
        if (durationMs == null || durationMs <= 0L) {
            return TopwaySeekDecision(null, policy, "ignored: unknown duration")
        }
        val raw =
            when (rawProgress) {
                is Int -> rawProgress.toLong()
                is Long -> rawProgress
                is Short -> rawProgress.toLong()
                is String -> rawProgress.toLongOrNull()
                else -> null
            } ?: return TopwaySeekDecision(null, policy, "ignored: unsupported raw value")
        val value = raw.coerceAtLeast(0L)
        val chosen =
            if (policy == TopwaySeekUnitPolicy.Auto) chooseAuto(value, durationMs) else policy
        val position = when (chosen) {
            TopwaySeekUnitPolicy.Auto -> value
            TopwaySeekUnitPolicy.Milliseconds -> value
            TopwaySeekUnitPolicy.Seconds -> value.saturatingMultiply(1000L)
            TopwaySeekUnitPolicy.Percent0To100 -> durationMs.saturatingMultiply(value) / 100L
            TopwaySeekUnitPolicy.Permille0To1000 -> durationMs.saturatingMultiply(value) / 1000L
        }.coerceIn(0L, durationMs)
        return TopwaySeekDecision(
            position,
            chosen,
            "raw=$raw duration=$durationMs position=$position",
        )
    }

    private fun chooseAuto(value: Long, durationMs: Long): TopwaySeekUnitPolicy =
        when {
            value <= durationMs -> TopwaySeekUnitPolicy.Milliseconds
            value <= Long.MAX_VALUE / 1000L && value * 1000L <= durationMs ->
                TopwaySeekUnitPolicy.Seconds
            value in 0L..100L -> TopwaySeekUnitPolicy.Percent0To100
            value in 0L..1000L -> TopwaySeekUnitPolicy.Permille0To1000
            else -> TopwaySeekUnitPolicy.Milliseconds
        }

    private fun Long.saturatingMultiply(other: Long): Long =
        if (this != 0L && other > Long.MAX_VALUE / this) Long.MAX_VALUE else this * other
}
