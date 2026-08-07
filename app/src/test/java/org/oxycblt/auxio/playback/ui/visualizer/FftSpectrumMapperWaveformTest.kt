/*
 * Copyright (c) 2026 Auxio Project
 * FftSpectrumMapperWaveformTest.kt is part of Auxio.
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

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.sin
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FftSpectrumMapperWaveformTest {
    @Test
    fun silentWaveformDoesNotCreateSyntheticMotion() {
        val mapper = FftSpectrumMapper()

        mapper.updateWaveform(ByteArray(512) { 0x80.toByte() }, SAMPLE_RATE_MILLIHZ)

        assertEquals(0f, mapper.globalEnvelope, 0.0001f)
        assertTrue(mapper.bands.all { it == 0f })
    }

    @Test
    fun realToneProducesBoundedFrequencyContour() {
        val mapper = FftSpectrumMapper()

        mapper.updateWaveform(createToneWaveform(1000f), SAMPLE_RATE_MILLIHZ)

        assertTrue(mapper.globalEnvelope > 0f)
        assertTrue(mapper.bands.any { abs(it) > 0.001f })
        assertTrue(mapper.bands.all { it in -1f..1f })
    }

    @Test
    fun waveformMappingIsCachedForStableCaptureConfiguration() {
        val mapper = FftSpectrumMapper()
        val waveform = createToneWaveform(1000f)

        mapper.updateWaveform(waveform, SAMPLE_RATE_MILLIHZ)
        val firstGeneration = mapper.mappingGenerationForTest()
        mapper.updateWaveform(waveform, SAMPLE_RATE_MILLIHZ)

        assertEquals(firstGeneration, mapper.mappingGenerationForTest())
    }

    @Test
    fun waveformConfigurationChangeResetsTransientActivity() {
        val mapper = FftSpectrumMapper()

        mapper.updateWaveform(createToneWaveform(1000f), SAMPLE_RATE_MILLIHZ)
        mapper.updateWaveform(createToneWaveform(6000f), SAMPLE_RATE_MILLIHZ)
        assertTrue(mapper.lastActivity > 0f)

        mapper.updateWaveform(
            createToneWaveform(6000f, sampleRateHz = 48_000f),
            48_000_000,
        )

        assertEquals(0f, mapper.lastActivity, 0.0001f)
    }

    @Test
    fun changingFrameSourceResetsTransientActivity() {
        val mapper = FftSpectrumMapper()
        val fft = createToneFft()
        mapper.update(fft, SAMPLE_RATE_MILLIHZ)
        fft[24] = 0
        fft[80] = 100
        mapper.update(fft, SAMPLE_RATE_MILLIHZ)
        assertTrue(mapper.lastActivity > 0f)

        mapper.updateWaveform(createToneWaveform(1000f), SAMPLE_RATE_MILLIHZ)

        assertEquals(0f, mapper.lastActivity, 0.0001f)
    }

    @Test
    fun sourceChangesAndResetRebuildSharedFrequencyCoordinates() {
        val mapper = FftSpectrumMapper()
        val fft = createToneFft()
        val waveform = createToneWaveform(1000f)

        mapper.update(fft, SAMPLE_RATE_MILLIHZ)
        val firstGeneration = mapper.mappingGenerationForTest()

        mapper.updateWaveform(waveform, SAMPLE_RATE_MILLIHZ)
        assertEquals(firstGeneration + 1, mapper.mappingGenerationForTest())

        mapper.update(fft, SAMPLE_RATE_MILLIHZ)
        assertEquals(firstGeneration + 2, mapper.mappingGenerationForTest())

        mapper.reset()
        mapper.update(fft, SAMPLE_RATE_MILLIHZ)
        assertEquals(firstGeneration + 3, mapper.mappingGenerationForTest())
    }

    @Test
    fun unusableWaveformMappingDecaysWithoutReusingStaleCoordinates() {
        val mapper = FftSpectrumMapper()
        val waveform = createToneWaveform(1000f)

        mapper.updateWaveform(waveform, SAMPLE_RATE_MILLIHZ)
        val initialContour = mapper.bands.maxOf { abs(it) }
        val initialEnvelope = mapper.globalEnvelope
        val validGeneration = mapper.mappingGenerationForTest()

        mapper.updateWaveform(waveform, 0)
        val invalidGeneration = mapper.mappingGenerationForTest()

        assertEquals(validGeneration + 1, invalidGeneration)
        assertTrue(mapper.bands.maxOf { abs(it) } < initialContour)
        assertTrue(mapper.globalEnvelope < initialEnvelope)

        val firstDecayContour = mapper.bands.maxOf { abs(it) }
        val firstDecayEnvelope = mapper.globalEnvelope
        mapper.updateWaveform(waveform, 0)

        assertEquals(invalidGeneration, mapper.mappingGenerationForTest())
        assertTrue(mapper.bands.maxOf { abs(it) } < firstDecayContour)
        assertTrue(mapper.globalEnvelope < firstDecayEnvelope)
    }

    private fun createToneFft(): ByteArray = ByteArray(512).apply { this[24] = 100 }

    private fun createToneWaveform(
        frequencyHz: Float,
        sampleRateHz: Float = SAMPLE_RATE_HZ,
    ): ByteArray =
        ByteArray(512) { index ->
            val angle = 2.0 * PI * frequencyHz * index / sampleRateHz
            val sample = 128 + (60.0 * sin(angle)).roundToInt()
            sample.coerceIn(0, 255).toByte()
        }

    private companion object {
        const val SAMPLE_RATE_HZ = 44_100f
        const val SAMPLE_RATE_MILLIHZ = 44_100_000
    }
}
