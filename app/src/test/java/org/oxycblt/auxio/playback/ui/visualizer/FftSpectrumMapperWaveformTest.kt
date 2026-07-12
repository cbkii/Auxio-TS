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

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FftSpectrumMapperWaveformTest {
    @Test
    fun silentWaveformDoesNotCreateSyntheticMotion() {
        val mapper = FftSpectrumMapper(16)
        mapper.updateWaveform(ByteArray(256) { 0x80.toByte() })
        assertEquals(0f, mapper.globalEnvelope, 0.0001f)
        assertTrue(mapper.bands.all { it == 0f })
    }

    @Test
    fun realWaveformProducesBoundedBands() {
        val mapper = FftSpectrumMapper(16)
        val waveform =
            ByteArray(256) { index -> if (index % 16 < 8) 0x40.toByte() else 0xC0.toByte() }
        mapper.updateWaveform(waveform)
        assertTrue(mapper.globalEnvelope > 0f)
        assertTrue(mapper.bands.any { it > 0f })
        assertTrue(mapper.bands.all { it in 0f..1f })
    }
}
