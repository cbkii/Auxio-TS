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
 * imaginary pairs for bins 1 until n/2. This mapper ignores DC and Nyquist, groups component-pair
 * magnitudes into logarithmic bands, applies adaptive gain for quiet music, and maintains bounded
 * attack/release envelopes.
 *
 * [android.media.audiofx.Visualizer.OnDataCaptureListener.onFftDataCapture] supplies the sampling
 * rate in milliHertz, not Hertz. [update] preserves that platform unit explicitly and converts to
 * Hertz only while rebuilding the cached bin-to-band mapping.
 */
class FftSpectrumMapper(private val bandCount: Int = DEFAULT_BAND_COUNT) {
    init {
        require(bandCount > 0) { "bandCount must be positive" }
    }

    /** Current normalized band levels. Read-only by convention; mutated by [update] and [reset]. */
    val bands = FloatArray(bandCount)
    private val raw = FloatArray(bandCount)
    private val sumSquares = FloatArray(bandCount)
    private val sampleCounts = IntArray(bandCount)
    private var adaptivePeak = MIN_ADAPTIVE_PEAK

    /** Current global energy envelope for visualizer scaling. */
    var globalEnvelope = 0f
        private set

    private var cachedFftSize = -1
    private var cachedSamplingRateMilliHertz = -1
    private var binToBandMapping = IntArray(0)

    /** Number of mapping rebuilds, exposed internally for deterministic cache tests. */
    internal var mappingRebuildCount = 0
        private set

    private fun recalculateBinToBandMapping(fftSize: Int, samplingRateMilliHertz: Int) {
        cachedFftSize = fftSize
        cachedSamplingRateMilliHertz = samplingRateMilliHertz
        mappingRebuildCount++

        val usablePairCount = ((fftSize - 2) / 2).coerceAtLeast(0)
        binToBandMapping = IntArray(usablePairCount + 1) { EXCLUDED_BAND }
        if (usablePairCount == 0) return

        val samplingRateHertz = samplingRateMilliHertz / 1000f
        val frequencyResolutionHertz = samplingRateHertz / usablePairCount.toFloat() / 2f
        for (bin in 1..usablePairCount) {
            val frequencyHertz = bin * frequencyResolutionHertz
            if (frequencyHertz < MIN_FREQ_HZ || frequencyHertz > MAX_FREQ_HZ) continue

            val normalizedLogFrequency = (ln(frequencyHertz) - MIN_FREQ_LOG) / LOG_RANGE
            binToBandMapping[bin] =
                (normalizedLogFrequency * bandCount).toInt().coerceIn(0, bandCount - 1)
        }
    }

    /**
     * Update this mapper from one Android Visualizer FFT frame.
     *
     * @param fft Android Visualizer FFT bytes, or null to decay toward silence.
     * @param samplingRateMilliHertz sampling rate reported by Android Visualizer in milliHertz;
     *   `44_100_000` represents 44.1 kHz.
     */
    fun update(fft: ByteArray?, samplingRateMilliHertz: Int = DEFAULT_SAMPLING_RATE_MILLIHERTZ) {
        if (fft == null || fft.size < MIN_FFT_SIZE || samplingRateMilliHertz <= 0) {
            decayToSilence()
            return
        }

        val usablePairCount = ((fft.size - 2) / 2).coerceAtLeast(0)
        if (usablePairCount == 0) {
            decayToSilence()
            return
        }

        if (cachedSamplingRateMilliHertz != samplingRateMilliHertz || cachedFftSize != fft.size) {
            recalculateBinToBandMapping(fft.size, samplingRateMilliHertz)
        }

        sumSquares.fill(0f)
        sampleCounts.fill(0)

        var frameGlobalSumSquares = 0f
        var globalSampleCount = 0

        for (bin in 1..usablePairCount) {
            val band = binToBandMapping[bin]
            if (band == EXCLUDED_BAND) continue

            val pairIndex = bin * 2
            if (pairIndex + 1 >= fft.size) break

            val real = fft[pairIndex].toFloat()
            val imaginary = fft[pairIndex + 1].toFloat()
            val magnitude = sqrt(real * real + imaginary * imaginary)

            frameGlobalSumSquares += magnitude * magnitude
            globalSampleCount++
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
        const val DEFAULT_SAMPLING_RATE_MILLIHERTZ = 44_100_000

        private const val EXCLUDED_BAND = -1
        private const val MIN_FFT_SIZE = 4
        private const val MIN_FREQ_HZ = 40f
        private const val MAX_FREQ_HZ = 12_000f
        private val MIN_FREQ_LOG = ln(MIN_FREQ_HZ)
        private val MAX_FREQ_LOG = ln(MAX_FREQ_HZ)
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
