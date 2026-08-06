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
    fun changingFrameSourceResetsTransientActivity() {
        val mapper = FftSpectrumMapper()
        val fft = ByteArray(512).apply { this[24] = 100 }
        mapper.update(fft, SAMPLE_RATE_MILLIHZ)
        fft[24] = 0
        fft[80] = 100
        mapper.update(fft, SAMPLE_RATE_MILLIHZ)
        assertTrue(mapper.lastActivity > 0f)

        mapper.updateWaveform(createToneWaveform(1000f), SAMPLE_RATE_MILLIHZ)

        assertEquals(0f, mapper.lastActivity, 0.0001f)
    }

    private fun createToneWaveform(frequencyHz: Float): ByteArray =
        ByteArray(512) { index ->
            val angle = 2.0 * PI * frequencyHz * index / SAMPLE_RATE_HZ
            val sample = 128 + (60.0 * sin(angle)).roundToInt()
            sample.coerceIn(0, 255).toByte()
        }

    private companion object {
        const val SAMPLE_RATE_HZ = 44_100f
        const val SAMPLE_RATE_MILLIHZ = 44_100_000
    }
}
