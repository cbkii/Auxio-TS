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
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin
import org.oxycblt.auxio.util.getAttrColorCompat

/**
 * Smooth filled radial FFT visualizer for the Now Playing cover slot.
 *
 * The contour approach is adapted from `gauravk95/audio-visualizer-android`, Copyright 2018
 * Gaurav Kumar, licensed under the Apache License 2.0. This implementation keeps Auxio's GPL
 * header and uses a single filled path without synthetic idle animation or view-owned freshness
 * polling.
 */
class BlobVisualizer
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {

    private val spectrumMapper = FftSpectrumMapper()
    private val bands: FloatArray
        get() = spectrumMapper.bands

    private val outlinePaint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
        }
    private val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }
    private val path = Path()
    private val pointsX = FloatArray(FftSpectrumMapper.DEFAULT_BAND_COUNT)
    private val pointsY = FloatArray(FftSpectrumMapper.DEFAULT_BAND_COUNT)

    init {
        refreshThemeColors()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        refreshThemeColors()
    }

    fun updateState(state: VisualizerState) {
        when (state) {
            is VisualizerState.Hidden,
            is VisualizerState.WaitingForFrames,
            is VisualizerState.Failed -> spectrumMapper.reset()
            is VisualizerState.Live ->
                spectrumMapper.update(state.frame, state.samplingRateMilliHertz)
        }
        postInvalidateOnAnimation()
    }

    private fun refreshThemeColors() {
        val primary = context.getAttrColorCompat(AR.attr.colorPrimary).defaultColor

        outlinePaint.color = ColorUtils.setAlphaComponent(primary, 200)
        fillPaint.color = ColorUtils.setAlphaComponent(primary, 140)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val size = minOf(width, height).toFloat()
        if (size <= 0f) return

        val cx = width / 2f
        val cy = height / 2f
        val baseRadius = size * 0.35f
        val baseRadiusWithEnvelope = baseRadius * (1f + 0.10f * spectrumMapper.globalEnvelope)
        val maxPeakExcursion = baseRadius * 0.18f

        outlinePaint.strokeWidth = max(2f, size * 0.008f)
        path.reset()

        for (i in bands.indices) {
            val radius = baseRadiusWithEnvelope + maxPeakExcursion * bands[i]
            pointsX[i] = cx + radius * COS_LOOKUP[i]
            pointsY[i] = cy + radius * SIN_LOOKUP[i]
        }

        if (bands.isNotEmpty()) {
            val last = bands.lastIndex
            path.moveTo((pointsX[last] + pointsX[0]) / 2f, (pointsY[last] + pointsY[0]) / 2f)

            for (i in bands.indices) {
                val next = (i + 1) % bands.size
                path.quadTo(
                    pointsX[i],
                    pointsY[i],
                    (pointsX[i] + pointsX[next]) / 2f,
                    (pointsY[i] + pointsY[next]) / 2f,
                )
            }
            path.close()
        }

        canvas.drawPath(path, fillPaint)
        canvas.drawPath(path, outlinePaint)
    }

    companion object {
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
