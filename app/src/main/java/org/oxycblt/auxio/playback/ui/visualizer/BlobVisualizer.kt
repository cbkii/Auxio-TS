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
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.SystemClock
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.R as AR
import androidx.core.graphics.ColorUtils
import com.google.android.material.R as MR
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin
import org.oxycblt.auxio.util.getAttrColorCompat

/** Strong high-contrast radial FFT visualizer for the Now Playing cover slot. */
class BlobVisualizer
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {

    private val spectrumMapper = FftSpectrumMapper()
    private val bands: FloatArray
        get() = spectrumMapper.bands

    private var hasValidFrame = false
    private var lastFrameAtMs = 0L
    private var idleInvalidateScheduled = false
    private val idleInvalidateRunnable = Runnable {
        idleInvalidateScheduled = false
        if (shouldAnimateIdlePulse()) {
            invalidate()
        }
    }

    private val scrimPaint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.argb(210, 0, 0, 0)
            style = Paint.Style.FILL
        }
    private val glowPaint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.argb(125, 0, 229, 255)
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
        }
    private val spikePaint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
        }
    private val corePaint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.argb(245, 255, 255, 255)
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
        }
    private val fillPaint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.argb(80, 0, 229, 255)
            style = Paint.Style.FILL
        }
    private val path = Path()

    init {
        refreshThemeColors()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        refreshThemeColors()
        if (shouldAnimateIdlePulse()) {
            scheduleIdleInvalidation()
        }
    }

    override fun onDetachedFromWindow() {
        cancelIdleInvalidation()
        super.onDetachedFromWindow()
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (visibility == VISIBLE) {
            if (shouldAnimateIdlePulse()) {
                scheduleIdleInvalidation()
            }
        } else {
            cancelIdleInvalidation()
        }
    }

    fun updateFft(bytes: ByteArray?) {
        if (bytes == null) {
            spectrumMapper.reset()
            hasValidFrame = false
            cancelIdleInvalidation()
        } else {
            spectrumMapper.update(bytes)
            hasValidFrame = bands.any { it > 0.01f }
            if (hasValidFrame) lastFrameAtMs = SystemClock.uptimeMillis()
        }
        invalidate()
    }

    private fun refreshThemeColors() {
        val primary = context.getAttrColorCompat(AR.attr.colorPrimary).defaultColor
        val secondary = context.getAttrColorCompat(MR.attr.colorSecondary).defaultColor
        val surface = context.getAttrColorCompat(MR.attr.colorSurface).defaultColor
        val surfaceIsLight = ColorUtils.calculateLuminance(surface) > 0.5

        // Keep the user's selected Material accent as the hue source, then lift it only as much as
        // needed for visibility on the intentionally dark playback backplate. This avoids the old
        // always-white visualizer while preserving TS18/head-unit contrast on bright album art.
        val spikeColor = ensureContrastOnDark(primary, MIN_SPIKE_CONTRAST)
        val glowColor = ensureContrastOnDark(secondary, MIN_GLOW_CONTRAST)
        val fillColor = ensureContrastOnDark(primary, MIN_FILL_CONTRAST)

        scrimPaint.color =
            if (surfaceIsLight) {
                Color.argb(218, 0, 0, 0)
            } else {
                Color.argb(224, 8, 8, 10)
            }
        spikePaint.color = spikeColor
        glowPaint.color = ColorUtils.setAlphaComponent(glowColor, 145)
        corePaint.color = ColorUtils.setAlphaComponent(spikeColor, 245)
        fillPaint.color = ColorUtils.setAlphaComponent(fillColor, 88)
    }

    private fun ensureContrastOnDark(color: Int, minimumContrast: Double): Int {
        var candidate = color
        var blend = 0.18f
        while (ColorUtils.calculateContrast(candidate, DARK_BACKPLATE_COLOR) < minimumContrast) {
            candidate = ColorUtils.blendARGB(color, Color.WHITE, blend.coerceAtMost(0.78f))
            if (blend >= 0.78f) break
            blend += 0.12f
        }
        return candidate
    }

    private fun shouldAnimateIdlePulse() =
        isAttachedToWindow && visibility == VISIBLE && (!hasValidFrame || isCurrentFrameStale())

    private fun isCurrentFrameStale() = SystemClock.uptimeMillis() - lastFrameAtMs > STALE_FRAME_MS

    private fun scheduleIdleInvalidation() {
        if (!idleInvalidateScheduled && shouldAnimateIdlePulse()) {
            idleInvalidateScheduled = true
            postDelayed(idleInvalidateRunnable, IDLE_INVALIDATE_MS)
        }
    }

    private fun cancelIdleInvalidation() {
        idleInvalidateScheduled = false
        removeCallbacks(idleInvalidateRunnable)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val size = minOf(width, height).toFloat()
        if (size <= 0f) return

        val cx = width / 2f
        val cy = height / 2f
        val baseRadius = size * 0.32f
        val maxSpike = size * 0.34f
        val backplateRadius = size * 0.49f
        val stale = !hasValidFrame || isCurrentFrameStale()

        canvas.drawCircle(cx, cy, backplateRadius, scrimPaint)

        val strokeWidth = max(4f, size * 0.018f)
        spikePaint.strokeWidth = strokeWidth
        corePaint.strokeWidth = max(3f, size * 0.012f)
        glowPaint.strokeWidth = strokeWidth * 2.6f

        path.reset()
        val count = bands.size
        val phase =
            if (stale) {
                (SystemClock.uptimeMillis() % LISTENING_PULSE_MS).toFloat() / LISTENING_PULSE_MS
            } else {
                0f
            }
        for (i in 0 until count) {
            // Waiting pulse is intentionally modest and regular: it shows the view is listening,
            // but avoids pretending to be audio-reactive after capture has failed or not started.
            val level =
                if (stale) {
                    0.1f + 0.08f * (1f + sin(((i.toFloat() / count) + phase) * 2f * PI).toFloat())
                } else {
                    bands[i]
                }
            val inner = baseRadius * 0.88f
            val outer = (baseRadius + maxSpike * level).coerceAtMost(size * 0.47f)
            val cosAngle = COS_LOOKUP[i]
            val sinAngle = SIN_LOOKUP[i]
            val sx = cx + inner * cosAngle
            val sy = cy + inner * sinAngle
            val ex = cx + outer * cosAngle
            val ey = cy + outer * sinAngle

            canvas.drawLine(sx, sy, ex, ey, glowPaint)
            canvas.drawLine(sx, sy, ex, ey, spikePaint)

            if (i == 0) path.moveTo(ex, ey) else path.lineTo(ex, ey)
        }
        path.close()
        canvas.drawPath(path, fillPaint)
        canvas.drawCircle(cx, cy, baseRadius * 0.82f, corePaint)

        if (stale) {
            scheduleIdleInvalidation()
        } else {
            cancelIdleInvalidation()
        }
    }

    companion object {
        private const val STALE_FRAME_MS = 700L
        private const val LISTENING_PULSE_MS = 1400L
        private const val IDLE_INVALIDATE_MS = 33L
        private const val DARK_BACKPLATE_COLOR = -0x1000000
        private const val MIN_SPIKE_CONTRAST = 4.5
        private const val MIN_GLOW_CONTRAST = 3.2
        private const val MIN_FILL_CONTRAST = 2.8
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
