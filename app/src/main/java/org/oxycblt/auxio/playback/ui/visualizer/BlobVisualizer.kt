/*
 * Copyright (c) 2018 Auxio Project
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
import android.media.audiofx.Visualizer
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.math.cos
import kotlin.math.sin
import timber.log.Timber as L

/**
 * A lightweight port of BlobVisualizer adapted for Auxio-TS.
 *
 * It uses the Android Visualizer API to render a blob-like shape based on the audio FFT data.
 */
class BlobVisualizer
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {

    private var visualizer: Visualizer? = null
    private val paint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, android.R.color.darker_gray)
        }
    private val path = Path()
    private var fftBytes: ByteArray? = null

    private var radius = 0f
    private var points = 0
    private var angleStep = 0f

    private var isVisualizerEnabled = false

    fun setColor(color: Int) {
        paint.color = color
        invalidate()
    }

    fun setAudioSessionId(audioSessionId: Int) {
        if (audioSessionId == 0) return

        release()
        try {
            visualizer =
                Visualizer(audioSessionId).apply {
                    captureSize = Visualizer.getCaptureSizeRange()[1]
                    setDataCaptureListener(
                        object : Visualizer.OnDataCaptureListener {
                            override fun onWaveFormDataCapture(
                                visualizer: Visualizer?,
                                waveform: ByteArray?,
                                samplingRate: Int,
                            ) {
                                // Not used
                            }

                            override fun onFftDataCapture(
                                visualizer: Visualizer?,
                                fft: ByteArray?,
                                samplingRate: Int,
                            ) {
                                fftBytes = fft?.copyOf()
                                invalidate()
                            }
                        },
                        Visualizer.getMaxCaptureRate() / 2,
                        false,
                        true,
                    )
                    enabled = isVisualizerEnabled
                }
        } catch (e: Exception) {
            L.w(e, "Failed to initialize Visualizer for audioSessionId $audioSessionId")
            visualizer = null
        }
    }

    fun enableVisualizer(enable: Boolean) {
        isVisualizerEnabled = enable
        try {
            visualizer?.enabled = enable
        } catch (e: Exception) {
            L.w(e, "Failed to enable/disable visualizer")
        }
        if (!enable) {
            fftBytes = null
            invalidate()
        }
    }

    fun release() {
        try {
            visualizer?.release()
        } catch (e: Exception) {
            L.w(e, "Failed to release visualizer")
        }
        visualizer = null
        fftBytes = null
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        radius = (minOf(w, h) / 2f) * 0.7f
        points = 64 // Use a reasonable number of points for smoothness vs performance
        angleStep = (2 * Math.PI / points).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val w = width.toFloat()
        val h = height.toFloat()
        val cx = w / 2f
        val cy = h / 2f

        path.reset()

        val fft = fftBytes

        if (fft == null || fft.isEmpty()) {
            // Draw a basic circle if no data
            canvas.drawCircle(cx, cy, radius, paint)
            return
        }

        // Draw the blob
        for (i in 0 until points) {
            val angle = i * angleStep

            // Extract magnitude from FFT data
            // Simple mapping from fft data to radius offset
            val fftIndex = (i * fft.size / points).coerceIn(0, fft.size - 2)
            val rfk = fft[fftIndex]
            val ifk = fft[fftIndex + 1]
            val magnitude = Math.hypot(rfk.toDouble(), ifk.toDouble()).toFloat()

            val r = radius + (magnitude * 0.5f).coerceAtMost(radius * 0.3f)

            val x = cx + r * cos(angle.toDouble()).toFloat()
            val y = cy + r * sin(angle.toDouble()).toFloat()

            if (i == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }
        path.close()

        canvas.drawPath(path, paint)
    }
}
