/*
 * Copyright (c) 2017 Auxio Project
 * BlobVisualizer.kt is part of Auxio.
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

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.R as AR
import androidx.core.graphics.ColorUtils
import com.google.android.material.R as MR
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin
import org.oxycblt.auxio.R
import org.oxycblt.auxio.util.getAttrColorCompat

/**
 * Smooth filled 24-point radial visualizer for the Now Playing cover slot.
 *
 * The contour approach is adapted from `gauravk95/audio-visualizer-android`, Copyright 2018 Gaurav
 * Kumar, licensed under the Apache License 2.0. This implementation keeps one filled quadratic
 * path, one outline, no synthetic idle animation, and no per-frame trigonometry or allocation.
 */
class BlobVisualizer
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {
    private val spectrumMapper = FftSpectrumMapper()
    private val bands: FloatArray
        get() = spectrumMapper.bands

    private var pointsX = FloatArray(FftSpectrumMapper.DEFAULT_BAND_COUNT)
    private var pointsY = FloatArray(FftSpectrumMapper.DEFAULT_BAND_COUNT)
    private val cosLookup = FloatArray(FftSpectrumMapper.DEFAULT_BAND_COUNT)
    private val sinLookup = FloatArray(FftSpectrumMapper.DEFAULT_BAND_COUNT)
    private var configuredTrackUid: String? = null
    private var configuredTrackDurationMs = Long.MIN_VALUE
    private var statusText: String? = null

    private val outlinePaint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
        }
    private val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }
    private val textPaint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textAlign = Paint.Align.CENTER
            style = Paint.Style.FILL
        }
    private val path = Path()

    init {
        recalculateAngles(phaseRadians = 0f, clockwise = true)
        refreshThemeColors()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        refreshThemeColors()
    }

    /**
     * Gives each track a stable visual orientation without changing the underlying frequency-band
     * boundaries. The expensive trigonometry runs only when a different track is bound.
     */
    fun configureTrack(uid: String, durationMs: Long) {
        if (configuredTrackUid == uid && configuredTrackDurationMs == durationMs) return

        configuredTrackUid = uid
        configuredTrackDurationMs = durationMs
        val seed = mix32(uid.hashCode() xor durationMs.hashCode())
        val clockwise = seed and 1 == 0
        val phaseUnit = ((seed ushr 8) and 0xFFFF) / 65_536f
        recalculateAngles(phaseUnit * TWO_PI, clockwise)
        spectrumMapper.reset()
        invalidate()
    }

    fun updateState(state: VisualizerState) {
        statusText =
            when (state) {
                VisualizerState.Disabled,
                VisualizerState.Paused -> null
                VisualizerState.AwaitingAudioSession,
                VisualizerState.Starting -> context.getString(R.string.lbl_visualizer_starting)
                VisualizerState.PermissionRequired ->
                    context.getString(R.string.lbl_visualizer_permission_required)
                VisualizerState.PermissionDenied ->
                    context.getString(R.string.lbl_visualizer_permission_denied)
                is VisualizerState.Unavailable ->
                    context.getString(R.string.lbl_visualizer_unavailable)
                is VisualizerState.Live -> null
            }
        contentDescription = statusText
        when (state) {
            VisualizerState.Disabled,
            VisualizerState.Paused,
            VisualizerState.AwaitingAudioSession,
            VisualizerState.PermissionRequired,
            VisualizerState.PermissionDenied,
            VisualizerState.Starting,
            is VisualizerState.Unavailable -> spectrumMapper.reset()
            is VisualizerState.Live -> {
                when (state.source) {
                    VisualizerState.FrameSource.FFT ->
                        spectrumMapper.update(state.frame, state.samplingRate)
                    VisualizerState.FrameSource.WAVEFORM ->
                        spectrumMapper.updateWaveform(state.frame, state.samplingRate)
                }
            }
        }
        invalidate()
    }

    private fun recalculateAngles(phaseRadians: Float, clockwise: Boolean) {
        val direction = if (clockwise) 1f else -1f
        for (index in cosLookup.indices) {
            val angularPosition = index.toFloat() / cosLookup.size.toFloat()
            val angle = phaseRadians + direction * angularPosition * TWO_PI - HALF_PI
            cosLookup[index] = cos(angle).toFloat()
            sinLookup[index] = sin(angle).toFloat()
        }
    }

    private fun refreshThemeColors() {
        val primary = context.getAttrColorCompat(AR.attr.colorPrimary).defaultColor
        outlinePaint.color = ColorUtils.setAlphaComponent(primary, 200)
        fillPaint.color = ColorUtils.setAlphaComponent(primary, 140)
        textPaint.color = context.getAttrColorCompat(MR.attr.colorOnSurface).defaultColor
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val size = minOf(width, height).toFloat()
        if (size <= 0f) return

        val cx = width / 2f
        val cy = height / 2f
        val baseRadius = size * BASE_RADIUS_RATIO
        val globalScale = GLOBAL_RADIUS_SCALE * spectrumMapper.globalEnvelope
        outlinePaint.strokeWidth = max(2f, size * OUTLINE_WIDTH_RATIO)

        path.reset()
        val count = bands.size
        if (pointsX.size != count) {
            pointsX = FloatArray(count)
            pointsY = FloatArray(count)
        }

        for (index in 0 until count) {
            val level = bands[index]
            val localScale =
                if (level >= 0f) {
                    OUTWARD_RADIUS_SCALE * level
                } else {
                    INWARD_RADIUS_SCALE * level
                }
            val radius = baseRadius * (1f + globalScale + localScale)
            pointsX[index] = cx + radius * cosLookup[index]
            pointsY[index] = cy + radius * sinLookup[index]
        }

        if (count > 0) {
            val firstMidX = (pointsX[count - 1] + pointsX[0]) / 2f
            val firstMidY = (pointsY[count - 1] + pointsY[0]) / 2f
            path.moveTo(firstMidX, firstMidY)
            for (index in 0 until count) {
                val nextIndex = (index + 1) % count
                val midX = (pointsX[index] + pointsX[nextIndex]) / 2f
                val midY = (pointsY[index] + pointsY[nextIndex]) / 2f
                path.quadTo(pointsX[index], pointsY[index], midX, midY)
            }
            path.close()
        }

        canvas.drawPath(path, fillPaint)
        canvas.drawPath(path, outlinePaint)
        statusText?.let { text ->
            textPaint.textSize = size * STATUS_TEXT_SIZE_RATIO
            val measured = textPaint.measureText(text)
            if (measured > width * STATUS_MAX_WIDTH_RATIO && measured > 0f) {
                textPaint.textSize *= (width * STATUS_MAX_WIDTH_RATIO) / measured
            }
            val baseline = cy - (textPaint.ascent() + textPaint.descent()) / 2f
            canvas.drawText(text, cx, baseline, textPaint)
        }
    }

    private fun mix32(input: Int): Int {
        var value = input
        value = value xor (value ushr 16)
        value *= -0x7A143595
        value = value xor (value ushr 13)
        value *= -0x3D4D51CB
        return value xor (value ushr 16)
    }

    companion object {
        private const val STATUS_TEXT_SIZE_RATIO = 0.055f
        private const val STATUS_MAX_WIDTH_RATIO = 0.82f

        private const val BASE_RADIUS_RATIO = 0.355f
        private const val GLOBAL_RADIUS_SCALE = 0.055f
        private const val OUTWARD_RADIUS_SCALE = 0.26f
        private const val INWARD_RADIUS_SCALE = 0.11f
        private const val OUTLINE_WIDTH_RATIO = 0.008f

        private const val TWO_PI = 6.2831855f
        private const val HALF_PI = 1.5707964f
    }
}
