package org.oxycblt.auxio.headunit.topway

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class TopwaySeekPolicyConverterTest {
    @Test
    fun `explicit seek policies convert to milliseconds`() {
        assertEquals(
            1500L,
            TopwaySeekPolicyConverter.convert(
                    1500,
                    10_000L,
                    TopwaySeekUnitPolicy.Milliseconds,
                )
                .positionMs,
        )
        assertEquals(
            2000L,
            TopwaySeekPolicyConverter.convert(2, 10_000L, TopwaySeekUnitPolicy.Seconds)
                .positionMs,
        )
        assertEquals(
            5000L,
            TopwaySeekPolicyConverter.convert(50, 10_000L, TopwaySeekUnitPolicy.Percent0To100)
                .positionMs,
        )
        assertEquals(
            2500L,
            TopwaySeekPolicyConverter.convert(
                    250,
                    10_000L,
                    TopwaySeekUnitPolicy.Permille0To1000,
                )
                .positionMs,
        )
    }

    @Test
    fun `auto is conservative and clamps impossible values`() {
        assertEquals(
            9000L,
            TopwaySeekPolicyConverter.convert(9000, 10_000L, TopwaySeekUnitPolicy.Auto).positionMs,
        )
        assertEquals(
            10_000L,
            TopwaySeekPolicyConverter.convert(12_000, 10_000L, TopwaySeekUnitPolicy.Auto)
                .positionMs,
        )
        assertEquals(
            0L,
            TopwaySeekPolicyConverter.convert(-5, 10_000L, TopwaySeekUnitPolicy.Auto).positionMs,
        )
    }

    @Test
    fun `seek is ignored without known duration`() {
        assertNull(
            TopwaySeekPolicyConverter.convert(50, 0L, TopwaySeekUnitPolicy.Percent0To100)
                .positionMs
        )
    }
}
