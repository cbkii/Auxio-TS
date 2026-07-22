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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
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
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin
import org.oxycblt.auxio.R
import org.oxycblt.auxio.util.getAttrColorCompat

/**
 * Smooth filled radial FFT visualizer for the Now Playing cover slot.
 *
 * The contour approach is adapted from `gauravk95/audio-visualizer-android`, Copyright 2018 Gaurav
 * Kumar, licensed under the Apache License 2.0. This implementation keeps Auxio's GPL header and
 * uses a single filled path without synthetic idle animation or view-owned freshness polling.
 */
class BlobVisualizer
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {
    private val spectrumMapper = FftSpectrumMapper()
    private val bands: FloatArray
        get() = spectrumMapper.bands

    private var pointsX = FloatArray(0)
    private var pointsY = FloatArray(0)
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
        refreshThemeColors()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        refreshThemeColors()
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
                        spectrumMapper.updateWaveform(state.frame)
                }
            }
        }
        invalidate()
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
        val baseRadius = size * 0.35f
        val globalEnv = spectrumMapper.globalEnvelope
        val baseRadiusWithEnv = baseRadius * (1f + 0.10f * globalEnv)
        val maxPeakExcursion = baseRadius * 0.18f
        outlinePaint.strokeWidth = max(2f, size * 0.008f)

        path.reset()
        val count = bands.size
        if (pointsX.size != count) {
            pointsX = FloatArray(count)
            pointsY = FloatArray(count)
        }

        for (index in 0 until count) {
            val radius = baseRadiusWithEnv + maxPeakExcursion * bands[index]
            pointsX[index] = cx + radius * COS_LOOKUP[index]
            pointsY[index] = cy + radius * SIN_LOOKUP[index]
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

    companion object {
        private const val STATUS_TEXT_SIZE_RATIO = 0.055f
        private const val STATUS_MAX_WIDTH_RATIO = 0.82f
        private val COS_LOOKUP =
            FloatArray(FftSpectrumMapper.DEFAULT_BAND_COUNT) { index ->
                cos(angleForBand(index)).toFloat()
            }
        private val SIN_LOOKUP =
            FloatArray(FftSpectrumMapper.DEFAULT_BAND_COUNT) { index ->
                sin(angleForBand(index)).toFloat()
            }

        private fun angleForBand(index: Int) =
            (index.toDouble() / FftSpectrumMapper.DEFAULT_BAND_COUNT.toDouble()) * 2.0 * PI -
                PI / 2.0
    }
}
