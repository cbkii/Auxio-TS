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

    /** Current global energy envelope for visualizer scaling. */
    var globalEnvelope = 0f
        private set

    private val sumSquares = FloatArray(bandCount)
    private val sampleCounts = IntArray(bandCount)

    fun update(fft: ByteArray?, samplingRate: Int = 44100000) {
        if (fft == null || fft.size < MIN_FFT_SIZE) {
            decayToSilence()
            return
        }

        val usablePairCount = ((fft.size - 2) / 2).coerceAtLeast(0)
        if (usablePairCount == 0) {
            decayToSilence()
            return
        }

        sumSquares.fill(0f)
        sampleCounts.fill(0)

        val freqResolution = (samplingRate / 1000f) / usablePairCount.toFloat() / 2f
        var frameGlobalSumSquares = 0f
        var globalSampleCount = 0

        for (bin in 1..usablePairCount) {
            val freq = bin * freqResolution
            if (freq < MIN_FREQ || freq > MAX_FREQ) continue

            val pairIndex = bin * 2
            if (pairIndex + 1 >= fft.size) break

            val real = fft[pairIndex].toFloat()
            val imaginary = fft[pairIndex + 1].toFloat()
            val magnitude = sqrt(real * real + imaginary * imaginary)

            frameGlobalSumSquares += magnitude * magnitude
            globalSampleCount++

            val normalizedLogFreq = (ln(freq) - MIN_FREQ_LOG) / LOG_RANGE
            val band = (normalizedLogFreq * bandCount).toInt().coerceIn(0, bandCount - 1)

            sumSquares[band] += magnitude * magnitude
            sampleCounts[band]++
        }

        if (globalSampleCount == 0) {
            decayToSilence()
            return
        }

        val frameGlobalRms = sqrt(frameGlobalSumSquares / globalSampleCount)
        adaptivePeak = max(MIN_ADAPTIVE_PEAK, max(frameGlobalRms, adaptivePeak * ADAPTIVE_DECAY))

        val normalizedGlobalEnvelope = (frameGlobalRms / adaptivePeak).coerceIn(0f, 1f)
        globalEnvelope = max(normalizedGlobalEnvelope, globalEnvelope * ENVELOPE_DECAY)

        for (i in bands.indices) {
            raw[i] =
                if (sampleCounts[i] > 0) {
                    sqrt(sumSquares[i] / sampleCounts[i])
                } else {
                    0f
                }
        }

        for (i in bands.indices) {
            val left2 = raw[(i - 2 + bandCount) % bandCount]
            val left1 = raw[(i - 1 + bandCount) % bandCount]
            val center = raw[i]
            val right1 = raw[(i + 1) % bandCount]
            val right2 = raw[(i + 2) % bandCount]

            val smoothedRaw =
                (left2 * 0.10f) +
                    (left1 * 0.22f) +
                    (center * 0.36f) +
                    (right1 * 0.22f) +
                    (right2 * 0.10f)

            val boosted = (smoothedRaw / adaptivePeak).coerceIn(0f, 1f).pow(RESPONSE_CURVE)

            if (boosted > bands[i]) {
                bands[i] += (boosted - bands[i]) * ATTACK
            } else {
                bands[i] *= RELEASE
            }
            bands[i] = bands[i].coerceIn(0f, 1f)
        }
    }

    fun reset() {
        bands.fill(0f)
        raw.fill(0f)
        adaptivePeak = MIN_ADAPTIVE_PEAK
        globalEnvelope = 0f
    }

    private fun decayToSilence() {
        raw.fill(0f)
        globalEnvelope *= ENVELOPE_DECAY
        for (i in bands.indices) {
            bands[i] = (bands[i] * SILENCE_DECAY).coerceIn(0f, 1f)
        }
    }

    companion object {
        const val DEFAULT_BAND_COUNT = 48
        private const val MIN_FFT_SIZE = 4
        private const val MIN_FREQ = 40f
        private const val MAX_FREQ = 12000f
        private val MIN_FREQ_LOG = ln(MIN_FREQ)
        private val MAX_FREQ_LOG = ln(MAX_FREQ)
        private val LOG_RANGE = MAX_FREQ_LOG - MIN_FREQ_LOG

        private const val MIN_ADAPTIVE_PEAK = 18f
        private const val ADAPTIVE_DECAY = 0.98f
        private const val ATTACK = 0.65f
        private const val RELEASE = 0.88f
        private const val SILENCE_DECAY = 0.84f
        private const val RESPONSE_CURVE = 0.70f
        private const val ENVELOPE_DECAY = 0.92f
    }
}
