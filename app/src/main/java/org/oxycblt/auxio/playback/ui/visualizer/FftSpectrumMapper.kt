/*
 * Copyright (c) 2026 Auxio Project
 * FftSpectrumMapper.kt is part of Auxio.
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

import kotlin.math.ln
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Converts Android [android.media.audiofx.Visualizer] FFT frames into stable radial bands.
 *
 * Android FFT bytes are laid out as DC real at index 0, Nyquist real at index 1, then real/
 * imaginary pairs for bins 1 until n/2. This mapper intentionally ignores DC/Nyquist for drawing so
 * constant offsets do not turn the visualizer into a static circle. It then groups component-pair
 * magnitudes into logarithmic-ish bands, applies adaptive gain for quiet music, and keeps a small
 * peak/decay envelope so the head-unit visual remains lively without flickering.
 */
class FftSpectrumMapper(private val bandCount: Int = DEFAULT_BAND_COUNT) {
    init {
        require(bandCount > 0) { "bandCount must be positive" }
    }

    /** Current normalized band levels. Read-only by convention; mutated by [update] and [reset]. */
    val bands = FloatArray(bandCount)
    private val peaks = FloatArray(bandCount)
    private val raw = FloatArray(bandCount)
    private var adaptivePeak = MIN_ADAPTIVE_PEAK

    fun update(fft: ByteArray?) {
        if (fft == null || fft.size < MIN_FFT_SIZE) {
            decayToSilence()
            return
        }

        val usablePairCount = ((fft.size - 2) / 2).coerceAtLeast(0)
        if (usablePairCount == 0) {
            decayToSilence()
            return
        }

        raw.fill(0f)
        var framePeak = 0f
        var contributingBins = 0

        for (bin in 1..usablePairCount) {
            val pairIndex = bin * 2
            if (pairIndex + 1 >= fft.size) break

            val real = fft[pairIndex].toFloat()
            val imaginary = fft[pairIndex + 1].toFloat()
            val magnitude = sqrt(real * real + imaginary * imaginary)
            if (magnitude <= 0f) continue

            val normalizedBin = (bin - 1).toFloat() / usablePairCount.toFloat()
            val band = frequencyBandFor(normalizedBin)
            raw[band] = max(raw[band], magnitude)
            framePeak = max(framePeak, magnitude)
            contributingBins++
        }

        if (contributingBins == 0 || framePeak <= 0f) {
            decayToSilence()
            return
        }

        adaptivePeak = max(MIN_ADAPTIVE_PEAK, max(framePeak, adaptivePeak * ADAPTIVE_DECAY))

        for (i in bands.indices) {
            val boosted = (raw[i] / adaptivePeak).coerceIn(0f, 1f).pow(RESPONSE_CURVE)
            val withFloor = if (boosted > 0f) max(boosted, ACTIVITY_FLOOR) else 0f
            val smoothed =
                if (withFloor > bands[i]) {
                    bands[i] + (withFloor - bands[i]) * ATTACK
                } else {
                    bands[i] * RELEASE
                }
            peaks[i] = max(smoothed, peaks[i] * PEAK_DECAY)
            bands[i] = max(smoothed, peaks[i] * PEAK_BLEND).coerceIn(0f, 1f)
        }
    }

    fun reset() {
        bands.fill(0f)
        peaks.fill(0f)
        raw.fill(0f)
        adaptivePeak = MIN_ADAPTIVE_PEAK
    }

    private fun decayToSilence() {
        raw.fill(0f)
        for (i in bands.indices) {
            peaks[i] *= PEAK_DECAY
            bands[i] = max(bands[i] * SILENCE_DECAY, peaks[i] * PEAK_BLEND).coerceIn(0f, 1f)
        }
    }

    private fun frequencyBandFor(normalizedBin: Float): Int {
        val curved = ln(1f + normalizedBin * LOG_WEIGHT) / ln(1f + LOG_WEIGHT)
        return min(bandCount - 1, (curved * bandCount).toInt())
    }

    companion object {
        const val DEFAULT_BAND_COUNT = 64
        private const val MIN_FFT_SIZE = 4
        private const val MIN_ADAPTIVE_PEAK = 18f
        private const val ADAPTIVE_DECAY = 0.96f
        private const val ATTACK = 0.72f
        private const val RELEASE = 0.82f
        private const val PEAK_DECAY = 0.9f
        private const val PEAK_BLEND = 0.72f
        private const val SILENCE_DECAY = 0.84f
        private const val ACTIVITY_FLOOR = 0.08f
        private const val RESPONSE_CURVE = 0.62f
        private const val LOG_WEIGHT = 7f
    }
}
