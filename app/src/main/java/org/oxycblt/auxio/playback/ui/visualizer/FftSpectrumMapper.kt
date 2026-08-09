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

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.floor
import kotlin.math.ln
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * Converts Android [android.media.audiofx.Visualizer] frames into a lightweight signed radial
 * contour.
 *
 * FFT magnitudes are grouped into non-empty perceptual bands, converted to log amplitude, and
 * detrended against log frequency so the common bass-to-treble slope of mastered music does not
 * become a fixed blob shape. A slowly adapting profile separates persistent track colour from
 * short-term spectral movement. The resulting bands are signed in `[-1, 1]`, allowing the renderer
 * to move both inward and outward around a near-circular neutral radius.
 *
 * Android Visualizer sampling rates are expressed in milliHertz. FFT bytes contain DC real at index
 * 0, Nyquist real at index 1, then real/imaginary pairs for bins 1 until `fft.size / 2`.
 */
class FftSpectrumMapper(private val bandCount: Int = DEFAULT_BAND_COUNT) {
    init {
        require(bandCount > 0) { "bandCount must be positive" }
    }

    /** Current signed contour values. Read-only by convention; mutated by updates and [reset]. */
    val bands = FloatArray(bandCount)

    /** Current global music-energy envelope used for modest whole-blob breathing. */
    var globalEnvelope = 0f
        private set

    internal var lastActivity = 0f
        private set

    private val raw = FloatArray(bandCount)
    private val logLevels = FloatArray(bandCount)
    private val normalizedSpectrum = FloatArray(bandCount)
    private val residual = FloatArray(bandCount)
    private val profile = FloatArray(bandCount)
    private val previousResidual = FloatArray(bandCount)
    private val target = FloatArray(bandCount)
    private val smoothed = FloatArray(bandCount)

    private val bandStartBins = IntArray(bandCount)
    private val bandEndBinsExclusive = IntArray(bandCount)
    private val bandLogFrequencies = FloatArray(bandCount)
    private val logFrequencyX = FloatArray(bandCount)
    private var logFrequencyDenominator = 1f

    private val waveformCoefficients = FloatArray(WAVEFORM_ANCHOR_COUNT)
    private val waveformAnchorLogFrequencies = FloatArray(WAVEFORM_ANCHOR_COUNT)
    private val waveformAnchorMagnitudes = FloatArray(WAVEFORM_ANCHOR_COUNT)

    private var cachedFftSize = -1
    private var cachedFftSamplingRate = -1
    private var cachedWaveformSize = -1
    private var cachedWaveformSamplingRate = -1
    private var waveformMappingValid = false
    private var mappingGeneration = 0

    private var adaptivePeak = MIN_ADAPTIVE_PEAK
    private var adaptiveFloor = 0f
    private var spectralSpread = MIN_SPECTRAL_SPREAD
    private var fluxPeak = MIN_FLUX_PEAK
    private var profileInitialized = false
    private var activeSource: InputSource? = null

    private enum class InputSource {
        FFT,
        WAVEFORM,
    }

    fun update(fft: ByteArray?, samplingRate: Int = DEFAULT_SAMPLING_RATE_MILLIHZ) {
        if (fft == null || fft.size < MIN_FFT_SIZE) {
            decayToSilence()
            return
        }

        val usablePairCount = ((fft.size - 2) / 2).coerceAtLeast(0)
        if (usablePairCount == 0) {
            decayToSilence()
            return
        }

        val sourceChanged = prepareSource(InputSource.FFT)
        if (cachedFftSize != fft.size || cachedFftSamplingRate != samplingRate) {
            if (!sourceChanged) resetDynamics()
            recalculateFftMapping(fft.size, samplingRate)
        }

        if (bandEndBinsExclusive[bandCount - 1] <= 0) {
            decayToSilence()
            return
        }

        for (band in 0 until bandCount) {
            val start = bandStartBins[band]
            val end = bandEndBinsExclusive[band]
            if (end <= start) {
                raw[band] = 0f
                continue
            }

            var sumSquares = 0f
            var sampleCount = 0
            for (bin in start until end) {
                val pairIndex = bin * 2
                if (pairIndex + 1 >= fft.size) break
                val real = fft[pairIndex].toFloat()
                val imaginary = fft[pairIndex + 1].toFloat()
                sumSquares += real * real + imaginary * imaginary
                sampleCount++
            }
            raw[band] = if (sampleCount > 0) sqrt(sumSquares / sampleCount) else 0f
        }

        var globalSumSquares = 0f
        var globalSampleCount = 0
        val globalStart = bandStartBins.first()
        val globalEnd = bandEndBinsExclusive.last()
        for (bin in globalStart until globalEnd) {
            val pairIndex = bin * 2
            if (pairIndex + 1 >= fft.size) break
            val real = fft[pairIndex].toFloat()
            val imaginary = fft[pairIndex + 1].toFloat()
            globalSumSquares += real * real + imaginary * imaginary
            globalSampleCount++
        }

        if (globalSampleCount == 0) {
            decayToSilence()
            return
        }
        applyRawLevels(sqrt(globalSumSquares / globalSampleCount))
    }

    /**
     * Converts real waveform capture into coarse frequency anchors with Goertzel filters, then
     * interpolates those anchors into the same perceptual contour. This fallback is used only when
     * the device supplies no usable FFT frames; it avoids treating consecutive time slices as fake
     * frequency bands.
     */
    fun updateWaveform(waveform: ByteArray?, samplingRate: Int = DEFAULT_SAMPLING_RATE_MILLIHZ) {
        if (waveform == null || waveform.size < MIN_WAVEFORM_SIZE) {
            decayToSilence()
            return
        }

        val sourceChanged = prepareSource(InputSource.WAVEFORM)
        if (cachedWaveformSize != waveform.size || cachedWaveformSamplingRate != samplingRate) {
            if (!sourceChanged) resetDynamics()
            recalculateWaveformMapping(waveform.size, samplingRate)
        }
        if (!waveformMappingValid) {
            decayToSilence()
            return
        }

        var frameSumSquares = 0f
        for (sample in waveform) {
            val centered = ((sample.toInt() and 0xFF) - WAVEFORM_CENTER).toFloat()
            frameSumSquares += centered * centered
        }

        for (anchor in 0 until WAVEFORM_ANCHOR_COUNT) {
            val coefficient = waveformCoefficients[anchor]
            var previous = 0f
            var previous2 = 0f
            for (sample in waveform) {
                val centered = ((sample.toInt() and 0xFF) - WAVEFORM_CENTER).toFloat()
                val current = centered + coefficient * previous - previous2
                previous2 = previous
                previous = current
            }
            val power =
                max(
                    0f,
                    previous2 * previous2 + previous * previous - coefficient * previous * previous2,
                )
            waveformAnchorMagnitudes[anchor] = sqrt(power) / waveform.size.toFloat()
        }

        for (band in 0 until bandCount) {
            val targetLogFrequency = bandLogFrequencies[band]
            var upper = 1
            while (
                upper < WAVEFORM_ANCHOR_COUNT - 1 &&
                    waveformAnchorLogFrequencies[upper] < targetLogFrequency
            ) {
                upper++
            }
            val lower = (upper - 1).coerceAtLeast(0)
            val lowerLog = waveformAnchorLogFrequencies[lower]
            val upperLog = waveformAnchorLogFrequencies[upper]
            val fraction =
                if (upperLog > lowerLog) {
                    ((targetLogFrequency - lowerLog) / (upperLog - lowerLog)).coerceIn(0f, 1f)
                } else {
                    0f
                }
            raw[band] =
                waveformAnchorMagnitudes[lower] +
                    (waveformAnchorMagnitudes[upper] - waveformAnchorMagnitudes[lower]) * fraction
        }

        applyRawLevels(sqrt(frameSumSquares / waveform.size.toFloat()))
    }

    fun reset() {
        resetDynamics()
        invalidateMappingCaches()
        activeSource = null
    }

    internal fun bandStartBinsForTest() = bandStartBins.copyOf()

    internal fun bandEndBinsExclusiveForTest() = bandEndBinsExclusive.copyOf()

    internal fun mappingGenerationForTest() = mappingGeneration

    private fun prepareSource(source: InputSource): Boolean {
        val sourceChanged = activeSource != source
        if (sourceChanged) {
            if (activeSource != null) resetDynamics()
            invalidateMappingCaches()
        }
        activeSource = source
        return sourceChanged
    }

    private fun invalidateMappingCaches() {
        cachedFftSize = -1
        cachedFftSamplingRate = -1
        cachedWaveformSize = -1
        cachedWaveformSamplingRate = -1
        waveformMappingValid = false
    }

    private fun recalculateFftMapping(fftSize: Int, samplingRate: Int) {
        cachedFftSize = fftSize
        cachedFftSamplingRate = samplingRate
        mappingGeneration++

        bandStartBins.fill(0)
        bandEndBinsExclusive.fill(0)

        val usablePairCount = ((fftSize - 2) / 2).coerceAtLeast(0)
        val sampleRateHz = samplingRate / 1000f
        if (usablePairCount == 0 || sampleRateHz <= 0f || fftSize <= 0) return

        val resolutionHz = sampleRateHz / fftSize.toFloat()
        val highestFrequency = min(MAX_FREQ_HZ, sampleRateHz / 2f)
        val minBin = ceil(MIN_FREQ_HZ / resolutionHz).toInt().coerceIn(1, usablePairCount)
        val maxBin =
            floor(highestFrequency / resolutionHz).toInt().coerceIn(minBin, usablePairCount)
        val availableBins = maxBin - minBin + 1
        if (availableBins <= 0) return

        val minFrequency = minBin * resolutionHz
        val maxExclusiveFrequency = (maxBin + 1) * resolutionHz
        val minMel = toMel(minFrequency)
        val maxMel = toMel(maxExclusiveFrequency)

        if (availableBins >= bandCount) {
            val edges = IntArray(bandCount + 1)
            edges[0] = minBin
            edges[bandCount] = maxBin + 1
            for (index in 1 until bandCount) {
                val fraction = index.toFloat() / bandCount.toFloat()
                val mel = minMel + (maxMel - minMel) * fraction
                val candidate = floor(fromMel(mel) / resolutionHz).toInt()
                val minimumAllowed = edges[index - 1] + 1
                val maximumAllowed = edges[bandCount] - (bandCount - index)
                edges[index] = candidate.coerceIn(minimumAllowed, maximumAllowed)
            }
            for (band in 0 until bandCount) {
                bandStartBins[band] = edges[band]
                bandEndBinsExclusive[band] = edges[band + 1]
            }
        } else {
            // Small non-standard capture sizes cannot provide one unique bin per visual point.
            // Reuse the nearest perceptual bin rather than introducing fixed zero holes.
            for (band in 0 until bandCount) {
                val fraction = (band + 0.5f) / bandCount.toFloat()
                val mel = minMel + (maxMel - minMel) * fraction
                val bin = (fromMel(mel) / resolutionHz).roundToInt().coerceIn(minBin, maxBin)
                bandStartBins[band] = bin
                bandEndBinsExclusive[band] = bin + 1
            }
        }

        for (band in 0 until bandCount) {
            val centerBin = (bandStartBins[band] + bandEndBinsExclusive[band] - 1) / 2f
            bandLogFrequencies[band] = ln(max(MIN_FREQ_HZ, centerBin * resolutionHz))
        }
        centerLogFrequencyCoordinates()
    }

    private fun recalculateWaveformMapping(waveformSize: Int, samplingRate: Int) {
        cachedWaveformSize = waveformSize
        cachedWaveformSamplingRate = samplingRate
        waveformMappingValid = false
        mappingGeneration++

        val sampleRateHz = samplingRate / 1000f
        val highestFrequency = min(MAX_FREQ_HZ, sampleRateHz * 0.45f)
        val lowestFrequency = min(MIN_FREQ_HZ, highestFrequency * 0.5f)
        if (sampleRateHz <= 0f || highestFrequency <= lowestFrequency) {
            waveformCoefficients.fill(0f)
            waveformAnchorMagnitudes.fill(0f)
            return
        }

        val minimumLog = ln(lowestFrequency)
        val maximumLog = ln(highestFrequency)
        for (anchor in 0 until WAVEFORM_ANCHOR_COUNT) {
            val fraction = anchor.toFloat() / (WAVEFORM_ANCHOR_COUNT - 1).toFloat()
            val logFrequency = minimumLog + (maximumLog - minimumLog) * fraction
            val frequency = exp(logFrequency)
            waveformAnchorLogFrequencies[anchor] = logFrequency
            waveformCoefficients[anchor] =
                (2.0 * cos(2.0 * PI * frequency / sampleRateHz)).toFloat()
        }

        val minimumMel = toMel(lowestFrequency)
        val maximumMel = toMel(highestFrequency)
        for (band in 0 until bandCount) {
            val fraction = (band + 0.5f) / bandCount.toFloat()
            val mel = minimumMel + (maximumMel - minimumMel) * fraction
            bandLogFrequencies[band] = ln(fromMel(mel))
        }
        centerLogFrequencyCoordinates()
        waveformMappingValid = true
    }

    private fun centerLogFrequencyCoordinates() {
        var mean = 0f
        for (value in bandLogFrequencies) mean += value
        mean /= bandCount.toFloat()

        var denominator = 0f
        for (index in 0 until bandCount) {
            val centered = bandLogFrequencies[index] - mean
            logFrequencyX[index] = centered
            denominator += centered * centered
        }
        logFrequencyDenominator = max(MIN_REGRESSION_DENOMINATOR, denominator)
    }

    private fun applyRawLevels(frameGlobalRms: Float) {
        var logMean = 0f
        for (index in 0 until bandCount) {
            val value = ln(1f + max(0f, raw[index]))
            logLevels[index] = value
            logMean += value
        }
        logMean /= bandCount.toFloat()

        var frameSpread = 0f
        for (index in 0 until bandCount) {
            frameSpread = max(frameSpread, abs(logLevels[index] - logMean))
        }
        spectralSpread =
            max(MIN_SPECTRAL_SPREAD, max(frameSpread, spectralSpread * SPECTRAL_SPREAD_DECAY))

        for (index in 0 until bandCount) {
            normalizedSpectrum[index] =
                ((logLevels[index] - logMean) / spectralSpread).coerceIn(-1f, 1f)
        }

        var slopeNumerator = 0f
        for (index in 0 until bandCount) {
            slopeNumerator += logFrequencyX[index] * normalizedSpectrum[index]
        }
        val slope = slopeNumerator / logFrequencyDenominator
        for (index in 0 until bandCount) {
            residual[index] = normalizedSpectrum[index] - slope * logFrequencyX[index]
        }

        if (bandCount > 1) {
            val seamDifference = residual[bandCount - 1] - residual[0]
            for (index in 0 until bandCount) {
                residual[index] -= seamDifference * index.toFloat() / (bandCount - 1).toFloat()
            }
        }

        var residualMean = 0f
        for (value in residual) residualMean += value
        residualMean /= bandCount.toFloat()
        for (index in 0 until bandCount) residual[index] -= residualMean

        val activity =
            if (!profileInitialized) {
                for (index in 0 until bandCount) {
                    profile[index] = residual[index]
                    previousResidual[index] = residual[index]
                }
                profileInitialized = true
                0f
            } else {
                var flux = 0f
                for (index in 0 until bandCount) {
                    flux += max(0f, residual[index] - previousResidual[index])
                    previousResidual[index] = residual[index]
                }
                flux /= bandCount.toFloat()
                fluxPeak = max(MIN_FLUX_PEAK, max(flux, fluxPeak * FLUX_PEAK_DECAY))
                (flux / fluxPeak).coerceIn(0f, 1f)
            }
        lastActivity = activity

        val profileRate =
            PROFILE_RATE_LOW_ACTIVITY +
                (PROFILE_RATE_HIGH_ACTIVITY - PROFILE_RATE_LOW_ACTIVITY) * activity
        for (index in 0 until bandCount) {
            profile[index] += (residual[index] - profile[index]) * profileRate
            val transient = residual[index] - profile[index]
            target[index] =
                (ABSOLUTE_SHAPE_WEIGHT * residual[index] + TRANSIENT_SHAPE_WEIGHT * transient)
                    .coerceIn(-1f, 1f)
        }

        if (bandCount == 1) {
            smoothed[0] = target[0]
        } else {
            for (index in 0 until bandCount) {
                val left = target[(index + bandCount - 1) % bandCount]
                val right = target[(index + 1) % bandCount]
                smoothed[index] =
                    NEIGHBOR_WEIGHT * left + CENTER_WEIGHT * target[index] + NEIGHBOR_WEIGHT * right
            }
        }

        val attack = ATTACK_LOW_ACTIVITY + (ATTACK_HIGH_ACTIVITY - ATTACK_LOW_ACTIVITY) * activity
        val release =
            RELEASE_LOW_ACTIVITY + (RELEASE_HIGH_ACTIVITY - RELEASE_LOW_ACTIVITY) * activity
        for (index in 0 until bandCount) {
            val current = bands[index]
            val next = smoothed[index]
            val changesDirection = current * next < 0f
            val growsInMagnitude = abs(next) > abs(current)
            val alpha = if (changesDirection || growsInMagnitude) attack else release
            bands[index] = (current + (next - current) * alpha).coerceIn(-1f, 1f)
        }

        adaptivePeak =
            max(MIN_ADAPTIVE_PEAK, max(frameGlobalRms, adaptivePeak * ADAPTIVE_PEAK_DECAY))
        val floorAlpha =
            if (frameGlobalRms < adaptiveFloor) GLOBAL_FLOOR_FALL_ALPHA else GLOBAL_FLOOR_RISE_ALPHA
        adaptiveFloor += (frameGlobalRms - adaptiveFloor) * floorAlpha
        val energy =
            ((frameGlobalRms - adaptiveFloor) / max(MIN_GLOBAL_RANGE, adaptivePeak - adaptiveFloor))
                .coerceIn(0f, 1f)
        val envelopeTarget =
            (GLOBAL_ENERGY_WEIGHT * energy + GLOBAL_ACTIVITY_WEIGHT * activity).coerceIn(0f, 1f)
        val envelopeAlpha = if (envelopeTarget > globalEnvelope) GLOBAL_ATTACK else GLOBAL_RELEASE
        globalEnvelope += (envelopeTarget - globalEnvelope) * envelopeAlpha
    }

    private fun resetDynamics() {
        bands.fill(0f)
        raw.fill(0f)
        logLevels.fill(0f)
        normalizedSpectrum.fill(0f)
        residual.fill(0f)
        profile.fill(0f)
        previousResidual.fill(0f)
        target.fill(0f)
        smoothed.fill(0f)
        waveformAnchorMagnitudes.fill(0f)
        adaptivePeak = MIN_ADAPTIVE_PEAK
        adaptiveFloor = 0f
        spectralSpread = MIN_SPECTRAL_SPREAD
        fluxPeak = MIN_FLUX_PEAK
        profileInitialized = false
        lastActivity = 0f
        globalEnvelope = 0f
    }

    private fun decayToSilence() {
        raw.fill(0f)
        lastActivity *= SILENCE_DECAY
        globalEnvelope *= SILENCE_DECAY
        for (index in 0 until bandCount) {
            bands[index] = (bands[index] * SILENCE_DECAY).coerceIn(-1f, 1f)
        }
    }

    private fun toMel(frequencyHz: Float) = ln(1f + frequencyHz / MEL_SCALE_HZ)

    private fun fromMel(mel: Float) = MEL_SCALE_HZ * (exp(mel) - 1f)

    companion object {
        const val DEFAULT_BAND_COUNT = 24

        private const val DEFAULT_SAMPLING_RATE_MILLIHZ = 44_100_000
        private const val MIN_FFT_SIZE = 4
        private const val MIN_WAVEFORM_SIZE = 16
        private const val WAVEFORM_CENTER = 128
        private const val WAVEFORM_ANCHOR_COUNT = 8

        private const val MIN_FREQ_HZ = 80f
        private const val MAX_FREQ_HZ = 12_000f
        private const val MEL_SCALE_HZ = 700f
        private const val MIN_REGRESSION_DENOMINATOR = 0.0001f

        private const val MIN_SPECTRAL_SPREAD = 0.12f
        private const val SPECTRAL_SPREAD_DECAY = 0.96f

        private const val MIN_FLUX_PEAK = 0.015f
        private const val FLUX_PEAK_DECAY = 0.95f

        private const val PROFILE_RATE_LOW_ACTIVITY = 0.014f
        private const val PROFILE_RATE_HIGH_ACTIVITY = 0.004f

        private const val ABSOLUTE_SHAPE_WEIGHT = 0.35f
        private const val TRANSIENT_SHAPE_WEIGHT = 0.90f

        private const val NEIGHBOR_WEIGHT = 0.12f
        private const val CENTER_WEIGHT = 0.76f

        internal const val ATTACK_LOW_ACTIVITY = 0.48f
        internal const val ATTACK_HIGH_ACTIVITY = 0.64f
        private const val RELEASE_LOW_ACTIVITY = 0.12f
        private const val RELEASE_HIGH_ACTIVITY = 0.21f

        private const val MIN_ADAPTIVE_PEAK = 18f
        private const val ADAPTIVE_PEAK_DECAY = 0.965f
        private const val GLOBAL_FLOOR_FALL_ALPHA = 0.08f
        private const val GLOBAL_FLOOR_RISE_ALPHA = 0.015f
        private const val GLOBAL_ENERGY_WEIGHT = 0.72f
        private const val GLOBAL_ACTIVITY_WEIGHT = 0.28f
        private const val GLOBAL_ATTACK = 0.42f
        private const val GLOBAL_RELEASE = 0.10f
        private const val MIN_GLOBAL_RANGE = 6f

        private const val SILENCE_DECAY = 0.84f
    }
}
