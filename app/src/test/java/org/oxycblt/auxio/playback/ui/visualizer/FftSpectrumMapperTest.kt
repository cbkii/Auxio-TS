/*
 * Copyright (c) 2026 Auxio Project
 * FftSpectrumMapperTest.kt is part of Auxio.
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

import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class FftSpectrumMapperTest {
    @Test
    fun nullEmptyAndShortFramesReturnSafeZeros() {
        val mapper = FftSpectrumMapper(8)

        mapper.update(null)
        assertTrue(mapper.bands.all { it == 0f })
        mapper.update(byteArrayOf())
        assertTrue(mapper.bands.all { it == 0f })
        mapper.update(byteArrayOf(1, 2, 3))
        assertTrue(mapper.bands.all { it == 0f })
    }

    @Test
    fun updateReusesInternalBandBuffer() {
        val mapper = FftSpectrumMapper(8)
        val firstBands = mapper.bands

        mapper.update(byteArrayOf(0, 0, 3, 4, 0, 0))
        mapper.update(byteArrayOf(0, 0, 6, 8, 0, 0))

        assertSame(firstBands, mapper.bands)
    }

    @Test
    fun realImaginaryPairProducesNonZeroBand() {
        val mapper = FftSpectrumMapper(8)
        val fft = byteArrayOf(0, 0, 3, 4, 0, 0)

        mapper.update(fft)

        assertTrue(mapper.bands.any { it > 0f })
    }

    @Test
    fun highEnergyFrameProducesStrongerBandsThanLowEnergyFrame() {
        val lowMapper = FftSpectrumMapper(8)
        lowMapper.update(byteArrayOf(0, 0, 4, 3, 0, 0))
        val low = lowMapper.bands.maxOrNull() ?: 0f
        val highMapper = FftSpectrumMapper(8)
        highMapper.update(byteArrayOf(0, 0, 80, 60, 0, 0))
        val high = highMapper.bands.maxOrNull() ?: 0f

        assertTrue(high > low)
    }

    @Test
    fun smoothingAndDecayAreDeterministicAndClamped() {
        val mapper = FftSpectrumMapper(8)
        mapper.update(byteArrayOf(0, 0, 100, 100, 0, 0))
        val first = mapper.bands.copyOf().maxOrNull() ?: 0f
        mapper.update(byteArrayOf(0, 0, 0, 0, 0, 0))
        val decayed = mapper.bands.copyOf().maxOrNull() ?: 0f

        assertTrue(first in 0f..1f)
        assertTrue(decayed in 0f..1f)
        assertTrue(decayed < first)
    }

    @Test
    fun malformedOddLengthFrameDoesNotCrashAndClamps() {
        val mapper = FftSpectrumMapper(8)
        mapper.update(byteArrayOf(0, 0, 127, 127, 64))

        assertEquals(8, mapper.bands.size)
        assertTrue(mapper.bands.all { it in 0f..1f })
    }
}
