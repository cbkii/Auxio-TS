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
import org.junit.Assert.assertTrue
import org.junit.Test

class FftSpectrumMapperTest {
    @Test
    fun nullEmptyAndShortFramesReturnSafeZeros() {
        val mapper = FftSpectrumMapper(8)

        assertTrue(mapper.update(null).all { it == 0f })
        assertTrue(mapper.update(byteArrayOf()).all { it == 0f })
        assertTrue(mapper.update(byteArrayOf(1, 2, 3)).all { it == 0f })
    }

    @Test
    fun realImaginaryPairProducesNonZeroBand() {
        val mapper = FftSpectrumMapper(8)
        val fft = byteArrayOf(0, 0, 3, 4, 0, 0)

        val bands = mapper.update(fft)

        assertTrue(bands.any { it > 0f })
    }

    @Test
    fun highEnergyFrameProducesStrongerBandsThanLowEnergyFrame() {
        val low = FftSpectrumMapper(8).update(byteArrayOf(0, 0, 4, 3, 0, 0)).maxOrNull() ?: 0f
        val high = FftSpectrumMapper(8).update(byteArrayOf(0, 0, 80, 60, 0, 0)).maxOrNull() ?: 0f

        assertTrue(high > low)
    }

    @Test
    fun smoothingAndDecayAreDeterministicAndClamped() {
        val mapper = FftSpectrumMapper(8)
        val first = mapper.update(byteArrayOf(0, 0, 100, 100, 0, 0)).maxOrNull() ?: 0f
        val decayed = mapper.update(byteArrayOf(0, 0, 0, 0, 0, 0)).maxOrNull() ?: 0f

        assertTrue(first in 0f..1f)
        assertTrue(decayed in 0f..1f)
        assertTrue(decayed < first)
    }

    @Test
    fun malformedOddLengthFrameDoesNotCrashAndClamps() {
        val mapper = FftSpectrumMapper(8)
        val bands = mapper.update(byteArrayOf(0, 0, 127, 127, 64))

        assertEquals(8, bands.size)
        assertTrue(bands.all { it in 0f..1f })
    }
}
