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
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.sin

/** A lightweight BlobVisualizer adapted from gauravk95/audio-visualizer-android. */
class BlobVisualizer
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {

    private var fftBytes: ByteArray? = null
    private val paint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            alpha = 150
            style = Paint.Style.FILL
        }
    private val path = Path()

    fun updateFft(bytes: ByteArray?) {
        if (bytes != null) {
            // Defensive copy so that modifications from the visualizer thread do not break our data
            fftBytes = bytes.copyOf()
        } else {
            fftBytes = null
        }
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()
        val cx = width / 2f
        val cy = height / 2f
        val baseRadius = minOf(width, height) / 3f

        path.reset()

        val data = fftBytes
        if (data == null || data.size < 2) {
            // Draw a base circle if no active data
            canvas.drawCircle(cx, cy, baseRadius, paint)
            return
        }

        val pointsCount = minOf(data.size, 128)
        val angleStep = (2.0 * Math.PI) / pointsCount

        for (i in 0 until pointsCount) {
            val magnitude = data[i].toInt() and 0xFF
            val r = baseRadius + (magnitude.toFloat() / 256f) * (baseRadius * 0.5f)

            val angle = i * angleStep
            val px = cx + (r * cos(angle)).toFloat()
            val py = cy + (r * sin(angle)).toFloat()

            if (i == 0) {
                path.moveTo(px, py)
            } else {
                path.lineTo(px, py)
            }
        }
        path.close()
        canvas.drawPath(path, paint)
    }
}
