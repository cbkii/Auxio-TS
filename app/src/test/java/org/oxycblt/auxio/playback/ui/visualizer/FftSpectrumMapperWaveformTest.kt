/* Copyright (c) 2026 Auxio Project */
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
        val waveform = ByteArray(256) { index -> if (index % 16 < 8) 0x40.toByte() else 0xC0.toByte() }
        mapper.updateWaveform(waveform)
        assertTrue(mapper.globalEnvelope > 0f)
        assertTrue(mapper.bands.any { it > 0f })
        assertTrue(mapper.bands.all { it in 0f..1f })
    }
}
