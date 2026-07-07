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
    private val bands = FloatArray(FftSpectrumMapper.DEFAULT_BAND_COUNT)
    private var hasValidFrame = false
    private var lastFrameAtMs = 0L

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
    }

    fun updateFft(bytes: ByteArray?) {
        if (bytes == null) {
            spectrumMapper.reset()
            bands.fill(0f)
            hasValidFrame = false
        } else {
            val next = spectrumMapper.update(bytes)
            System.arraycopy(next, 0, bands, 0, minOf(next.size, bands.size))
            hasValidFrame = bands.any { it > 0.01f }
            if (hasValidFrame) lastFrameAtMs = SystemClock.uptimeMillis()
        }
        invalidate()
    }

    private fun refreshThemeColors() {
        val primary = context.getAttrColorCompat(MR.attr.colorPrimary).defaultColor
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

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val size = minOf(width, height).toFloat()
        if (size <= 0f) return

        val cx = width / 2f
        val cy = height / 2f
        val baseRadius = size * 0.32f
        val maxSpike = size * 0.34f
        val backplateRadius = size * 0.49f
        val now = SystemClock.uptimeMillis()
        val stale = !hasValidFrame || now - lastFrameAtMs > STALE_FRAME_MS

        canvas.drawCircle(cx, cy, backplateRadius, scrimPaint)

        val strokeWidth = max(4f, size * 0.018f)
        spikePaint.strokeWidth = strokeWidth
        corePaint.strokeWidth = max(3f, size * 0.012f)
        glowPaint.strokeWidth = strokeWidth * 2.6f

        path.reset()
        val count = bands.size
        val phase = if (stale) (now % LISTENING_PULSE_MS).toFloat() / LISTENING_PULSE_MS else 0f
        for (i in 0 until count) {
            val angle = (i.toDouble() / count.toDouble()) * 2.0 * PI - PI / 2.0
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
            val sx = cx + (inner * cos(angle)).toFloat()
            val sy = cy + (inner * sin(angle)).toFloat()
            val ex = cx + (outer * cos(angle)).toFloat()
            val ey = cy + (outer * sin(angle)).toFloat()

            canvas.drawLine(sx, sy, ex, ey, glowPaint)
            canvas.drawLine(sx, sy, ex, ey, spikePaint)

            if (i == 0) path.moveTo(ex, ey) else path.lineTo(ex, ey)
        }
        path.close()
        canvas.drawPath(path, fillPaint)
        canvas.drawCircle(cx, cy, baseRadius * 0.82f, corePaint)

        if (stale && visibility == VISIBLE) {
            postInvalidateDelayed(IDLE_INVALIDATE_MS)
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
    }
}
