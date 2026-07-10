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

        mapper.update(createSineWaveFft(1000f), SAMPLE_RATE_MILLIHERTZ)
        mapper.update(createSineWaveFft(2000f), SAMPLE_RATE_MILLIHERTZ)

        assertSame(firstBands, mapper.bands)
    }

    @Test
    fun binMappingCacheReusesAndInvalidatesByConfiguration() {
        val mapper = FftSpectrumMapper(48)

        mapper.update(createSineWaveFft(1000f), SAMPLE_RATE_MILLIHERTZ)
        assertEquals(1, mapper.mappingRebuildCount)

        mapper.update(createSineWaveFft(2000f), SAMPLE_RATE_MILLIHERTZ)
        assertEquals(1, mapper.mappingRebuildCount)

        mapper.update(createSineWaveFft(1000f, size = 1024), SAMPLE_RATE_MILLIHERTZ)
        assertEquals(2, mapper.mappingRebuildCount)

        mapper.update(createSineWaveFft(1000f, size = 1024), 48_000_000)
        assertEquals(3, mapper.mappingRebuildCount)
    }

    @Test
    fun realImaginaryPairProducesNonZeroBand() {
        val mapper = FftSpectrumMapper(48)
        val fft = createSineWaveFft(1000f)

        mapper.update(fft, SAMPLE_RATE_MILLIHERTZ)

        assertTrue(mapper.bands.any { it > 0f })
    }

    @Test
    fun highEnergyFrameProducesStrongerBandsThanLowEnergyFrame() {
        val lowMapper = FftSpectrumMapper(48)
        val lowFft = createSineWaveFft(1000f, amplitude = 10)
        lowMapper.update(lowFft, SAMPLE_RATE_MILLIHERTZ)
        val low = lowMapper.bands.maxOrNull() ?: 0f

        val highMapper = FftSpectrumMapper(48)
        val highFft = createSineWaveFft(1000f, amplitude = 100)
        highMapper.update(highFft, SAMPLE_RATE_MILLIHERTZ)
        val high = highMapper.bands.maxOrNull() ?: 0f

        assertTrue(high > low)
    }

    @Test
    fun smoothingAndDecayAreDeterministicAndClamped() {
        val mapper = FftSpectrumMapper(48)
        mapper.update(createSineWaveFft(1000f, amplitude = 100), SAMPLE_RATE_MILLIHERTZ)
        val first = mapper.bands.maxOrNull() ?: 0f
        mapper.update(byteArrayOf(0, 0, 0, 0, 0, 0), SAMPLE_RATE_MILLIHERTZ)
        val decayed = mapper.bands.maxOrNull() ?: 0f

        assertTrue(first in 0f..1f)
        assertTrue(decayed in 0f..1f)
        assertTrue(decayed < first)
    }

    @Test
    fun malformedOddLengthFrameDoesNotCrashAndClamps() {
        val mapper = FftSpectrumMapper(48)
        mapper.update(byteArrayOf(0, 0, 127, 127, 64), SAMPLE_RATE_MILLIHERTZ)

        assertEquals(48, mapper.bands.size)
        assertTrue(mapper.bands.all { it in 0f..1f })
    }

    @Test
    fun framesStoppingDecaysEnvelope() {
        val mapper = FftSpectrumMapper(48)
        val fft = createSineWaveFft(1000f, amplitude = 120)

        mapper.update(fft, SAMPLE_RATE_MILLIHERTZ)
        val initialEnvelope = mapper.globalEnvelope
        assertTrue("Envelope should be > 0", initialEnvelope > 0f)

        mapper.update(byteArrayOf(0, 0, 0, 0), SAMPLE_RATE_MILLIHERTZ)
        val decayedEnvelope = mapper.globalEnvelope
        assertTrue("Envelope should decay", decayedEnvelope < initialEnvelope)
    }

    @Test
    fun invalidSamplingRateDecaysSafely() {
        val mapper = FftSpectrumMapper(48)
        mapper.update(createSineWaveFft(1000f), SAMPLE_RATE_MILLIHERTZ)
        val initialEnvelope = mapper.globalEnvelope

        mapper.update(createSineWaveFft(1000f), 0)

        assertTrue(mapper.globalEnvelope < initialEnvelope)
        assertTrue(mapper.bands.all { it in 0f..1f })
    }

    @Test
    fun frequencyRangeExclusionUsesPopulatedRepresentableBins() {
        val mapper = FftSpectrumMapper(48)

        // At 8192 bytes and 44.1 kHz, 10 Hz maps to a non-zero bin below the 40 Hz cutoff.
        mapper.update(createSineWaveFft(10f, size = 8192), SAMPLE_RATE_MILLIHERTZ)
        val lowFreqMax = mapper.bands.maxOrNull() ?: 0f

        mapper.reset()
        mapper.update(createSineWaveFft(20_000f, size = 8192), SAMPLE_RATE_MILLIHERTZ)
        val highFreqMax = mapper.bands.maxOrNull() ?: 0f

        mapper.reset()
        mapper.update(createSineWaveFft(1000f, size = 8192), SAMPLE_RATE_MILLIHERTZ)
        val validFreqMax = mapper.bands.maxOrNull() ?: 0f

        assertTrue("Low frequency should be excluded, was $lowFreqMax", lowFreqMax == 0f)
        assertTrue("High frequency should be excluded, was $highFreqMax", highFreqMax == 0f)
        assertTrue("Valid frequency should be included", validFreqMax > 0f)
    }

    private fun createSineWaveFft(
        frequencyHertz: Float,
        amplitude: Byte = 100,
        size: Int = 512,
    ): ByteArray {
        val fft = ByteArray(size)
        val resolutionHertz = SAMPLE_RATE_HERTZ / size
        val targetBin = (frequencyHertz / resolutionHertz).toInt()

        if (targetBin > 0 && targetBin * 2 + 1 < size) {
            val realIndex = targetBin * 2
            fft[realIndex] = amplitude
            fft[realIndex + 1] = 0
        }
        return fft
    }

    companion object {
        private const val SAMPLE_RATE_HERTZ = 44_100f
        private const val SAMPLE_RATE_MILLIHERTZ = 44_100_000
    }
}
