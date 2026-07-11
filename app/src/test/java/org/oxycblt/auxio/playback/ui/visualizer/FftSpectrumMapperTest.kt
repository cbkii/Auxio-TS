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

        mapper.update(createSineWaveFft(1000f), 44100000)
        mapper.update(createSineWaveFft(2000f), 44100000)

        assertSame(firstBands, mapper.bands)
    }

    @Test
    fun realImaginaryPairProducesNonZeroBand() {
        val mapper = FftSpectrumMapper(48)
        val fft = createSineWaveFft(1000f) // 1 kHz, should fall in the 40-12000 range

        mapper.update(fft, 44100000)

        assertTrue(mapper.bands.any { it > 0f })
    }

    @Test
    fun highEnergyFrameProducesStrongerBandsThanLowEnergyFrame() {
        val lowMapper = FftSpectrumMapper(48)
        val lowFft = createSineWaveFft(1000f, amplitude = 10)
        lowMapper.update(lowFft, 44100000)
        val low = lowMapper.bands.maxOrNull() ?: 0f

        val highMapper = FftSpectrumMapper(48)
        val highFft = createSineWaveFft(1000f, amplitude = 100)
        highMapper.update(highFft, 44100000)
        val high = highMapper.bands.maxOrNull() ?: 0f

        assertTrue(high > low)
    }

    @Test
    fun smoothingAndDecayAreDeterministicAndClamped() {
        val mapper = FftSpectrumMapper(48)
        mapper.update(createSineWaveFft(1000f, amplitude = 100), 44100000)
        val first = mapper.bands.copyOf().maxOrNull() ?: 0f
        mapper.update(byteArrayOf(0, 0, 0, 0, 0, 0), 44100000)
        val decayed = mapper.bands.copyOf().maxOrNull() ?: 0f

        assertTrue(first in 0f..1f)
        assertTrue(decayed in 0f..1f)
        assertTrue(decayed < first)
    }

    @Test
    fun malformedOddLengthFrameDoesNotCrashAndClamps() {
        val mapper = FftSpectrumMapper(48)
        mapper.update(byteArrayOf(0, 0, 127, 127, 64), 44100000)

        assertEquals(48, mapper.bands.size)
        assertTrue(mapper.bands.all { it in 0f..1f })
    }

    @Test
    fun framesStoppingDecaysEnvelope() {
        val mapper = FftSpectrumMapper(48)
        val fft = createSineWaveFft(1000f, amplitude = 120)

        mapper.update(fft, 44100000)
        val initialEnvelope = mapper.globalEnvelope
        assertTrue("Envelope should be > 0", initialEnvelope > 0f)

        mapper.update(byteArrayOf(0, 0, 0, 0), 44100000) // "silence" or frame stopping
        val decayedEnvelope = mapper.globalEnvelope
        assertTrue("Envelope should decay", decayedEnvelope < initialEnvelope)
    }

    @Test
    fun frequencyRangeExclusion() {
        val mapper = FftSpectrumMapper(48)

        // Very low frequency (e.g. 10Hz) should be excluded
        mapper.update(createSineWaveFft(10f), 44100000)
        val lowFreqMax = mapper.bands.maxOrNull() ?: 0f

        mapper.reset()

        // Very high frequency (e.g. 20000Hz) should be excluded
        mapper.update(createSineWaveFft(20000f), 44100000)
        val highFreqMax = mapper.bands.maxOrNull() ?: 0f

        mapper.reset()

        // Valid frequency (e.g. 1000Hz)
        mapper.update(createSineWaveFft(1000f), 44100000)
        val validFreqMax = mapper.bands.maxOrNull() ?: 0f

        assertTrue(lowFreqMax == 0f)
        assertTrue(highFreqMax == 0f)
        assertTrue(validFreqMax > 0f)
    }

    private fun createSineWaveFft(freq: Float, amplitude: Byte = 100): ByteArray {
        val size = 512
        val fft = ByteArray(size)
        val sampleRate = 44100f
        val resolution = sampleRate / size
        val targetBin = (freq / resolution).toInt()

        // DC & Nyquist are ignored (indices 0, 1)
        if (targetBin > 0 && targetBin * 2 + 1 < size) {
            val realIndex = targetBin * 2
            val imagIndex = targetBin * 2 + 1
            fft[realIndex] = amplitude
            fft[imagIndex] = 0
        }
        return fft
    }
}
