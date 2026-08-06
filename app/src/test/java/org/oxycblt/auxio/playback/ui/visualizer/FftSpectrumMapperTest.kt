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

import kotlin.math.abs
import kotlin.math.pow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class FftSpectrumMapperTest {
    @Test
    fun defaultMapperUsesTwentyFourPoints() {
        assertEquals(24, FftSpectrumMapper().bands.size)
    }

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
        val mapper = FftSpectrumMapper()
        val firstBands = mapper.bands

        mapper.update(createSineWaveFft(1000f), SAMPLE_RATE_MILLIHZ)
        mapper.update(createSineWaveFft(2000f), SAMPLE_RATE_MILLIHZ)

        assertSame(firstBands, mapper.bands)
    }

    @Test
    fun commonCaptureMappingsPopulateEveryBandWithoutOverlap() {
        assertCompleteMapping(SAMPLE_RATE_MILLIHZ)
        assertCompleteMapping(48_000_000)
    }

    @Test
    fun mappingIsCachedUntilCaptureConfigurationChanges() {
        val mapper = FftSpectrumMapper()
        val frame = createBroadbandFft()

        mapper.update(frame, SAMPLE_RATE_MILLIHZ)
        val firstGeneration = mapper.mappingGenerationForTest()
        mapper.update(frame, SAMPLE_RATE_MILLIHZ)
        assertEquals(firstGeneration, mapper.mappingGenerationForTest())

        mapper.update(frame, 48_000_000)
        assertEquals(firstGeneration + 1, mapper.mappingGenerationForTest())
    }

    @Test
    fun smallCaptureReusesNearestBinsInsteadOfLeavingEmptyRanges() {
        val mapper = FftSpectrumMapper()
        mapper.update(createBroadbandFft(size = 32), SAMPLE_RATE_MILLIHZ)

        val starts = mapper.bandStartBinsForTest()
        val ends = mapper.bandEndBinsExclusiveForTest()
        assertEquals(24, starts.size)
        assertTrue(starts.indices.all { ends[it] > starts[it] })
    }

    @Test
    fun realImaginaryPairProducesBoundedSignedMovement() {
        val mapper = FftSpectrumMapper()

        mapper.update(createSineWaveFft(1000f), SAMPLE_RATE_MILLIHZ)

        assertTrue(mapper.bands.any { abs(it) > 0.001f })
        assertTrue(mapper.bands.all { it in -1f..1f })
    }

    @Test
    fun higherEnergyFrameProducesStrongerGlobalEnvelope() {
        val lowMapper = FftSpectrumMapper()
        lowMapper.update(createSineWaveFft(1000f, amplitude = 10), SAMPLE_RATE_MILLIHZ)

        val highMapper = FftSpectrumMapper()
        highMapper.update(createSineWaveFft(1000f, amplitude = 100), SAMPLE_RATE_MILLIHZ)

        assertTrue(highMapper.globalEnvelope > lowMapper.globalEnvelope)
    }

    @Test
    fun linearSpectralTiltIsFlatterThanLocalizedFrequencyBump() {
        val tiltedMapper = FftSpectrumMapper()
        tiltedMapper.update(createTiltedSpectrumFft(addBump = false), SAMPLE_RATE_MILLIHZ)
        val tiltedRange = tiltedMapper.bands.maxOf { abs(it) }

        val bumpedMapper = FftSpectrumMapper()
        bumpedMapper.update(createTiltedSpectrumFft(addBump = true), SAMPLE_RATE_MILLIHZ)
        val bumpedRange = bumpedMapper.bands.maxOf { abs(it) }

        assertTrue("Linear tilt should settle close to a circle", tiltedRange < 0.16f)
        assertTrue("A local spectral feature should survive detrending", bumpedRange > tiltedRange)
    }

    @Test
    fun invalidFramesDecaySignedContourAndEnvelope() {
        val mapper = FftSpectrumMapper()
        mapper.update(createSineWaveFft(1000f, amplitude = 120), SAMPLE_RATE_MILLIHZ)
        val firstContour = mapper.bands.maxOf { abs(it) }
        val firstEnvelope = mapper.globalEnvelope

        mapper.update(byteArrayOf(0, 0, 0), SAMPLE_RATE_MILLIHZ)

        assertTrue(mapper.bands.maxOf { abs(it) } < firstContour)
        assertTrue(mapper.globalEnvelope < firstEnvelope)
    }

    @Test
    fun frequenciesOutsideSupportedRangeDoNotCreateMovement() {
        val mapper = FftSpectrumMapper()

        mapper.update(createSineWaveFft(20_000f), SAMPLE_RATE_MILLIHZ)

        assertTrue(mapper.bands.all { it == 0f })
        assertEquals(0f, mapper.globalEnvelope, 0.0001f)
    }

    private fun assertCompleteMapping(samplingRate: Int) {
        val mapper = FftSpectrumMapper()
        mapper.update(createBroadbandFft(), samplingRate)

        val starts = mapper.bandStartBinsForTest()
        val ends = mapper.bandEndBinsExclusiveForTest()
        assertEquals(24, starts.size)
        assertEquals(24, ends.size)
        for (index in starts.indices) {
            assertTrue("Band $index must contain at least one bin", ends[index] > starts[index])
            if (index > 0) {
                assertEquals(
                    "Band ranges must not overlap or leave holes",
                    ends[index - 1],
                    starts[index],
                )
            }
        }
    }

    private fun createBroadbandFft(size: Int = 512): ByteArray {
        val fft = ByteArray(size)
        for (bin in 1 until size / 2) {
            val pairIndex = bin * 2
            if (pairIndex + 1 >= size) break
            fft[pairIndex] = (24 + bin % 80).toByte()
        }
        return fft
    }

    private fun createTiltedSpectrumFft(addBump: Boolean): ByteArray {
        val size = 512
        val fft = ByteArray(size)
        val resolution = SAMPLE_RATE_HZ / size
        for (bin in 1 until size / 2) {
            val frequency = bin * resolution
            if (frequency !in 80f..12_000f) continue
            val tiltedAmplitude = (121f * (frequency / 86.1328125f).pow(-0.25f) - 1f).toInt()
            val bump = if (addBump && frequency in 850f..1150f) 55 else 0
            val amplitude = (tiltedAmplitude + bump).coerceIn(1, 127)
            val pairIndex = bin * 2
            fft[pairIndex] = amplitude.toByte()
        }
        return fft
    }

    private fun createSineWaveFft(freq: Float, amplitude: Byte = 100): ByteArray {
        val size = 512
        val fft = ByteArray(size)
        val resolution = SAMPLE_RATE_HZ / size
        val targetBin = (freq / resolution).toInt()

        if (targetBin > 0 && targetBin * 2 + 1 < size) {
            val realIndex = targetBin * 2
            fft[realIndex] = amplitude
        }
        return fft
    }

    private companion object {
        const val SAMPLE_RATE_HZ = 44_100f
        const val SAMPLE_RATE_MILLIHZ = 44_100_000
    }
}
