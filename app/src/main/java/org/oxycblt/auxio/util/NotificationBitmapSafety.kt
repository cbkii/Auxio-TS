/*
 * Copyright (c) 2026 Auxio Project
 * NotificationBitmapSafety.kt is part of Auxio.
 */
package org.oxycblt.auxio.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import kotlin.math.roundToInt
import timber.log.Timber as L

/** TS18-safe large-icon bitmap policy for RemoteViews/SystemUI renderers. */
object NotificationBitmapSafety {
    const val MIN_ICON_SIZE_PX = 96
    const val MAX_ICON_SIZE_PX = 512
    const val FALLBACK_ICON_SIZE_PX = 256

    private val paint = Paint(Paint.FILTER_BITMAP_FLAG or Paint.DITHER_FLAG)
    private var cachedFallback: Bitmap? = null

    fun sanitize(input: Bitmap?): Bitmap? {
        val source = input?.takeIf { !it.isRecycled && it.width > 0 && it.height > 0 } ?: return null
        var working = source.toSafeSoftwareBitmap() ?: return null

        if (maxOf(working.width, working.height) > MAX_ICON_SIZE_PX) {
            val scaled = working.scaleToMaxSide(MAX_ICON_SIZE_PX) ?: return null
            if (scaled !== working && working !== source && !working.isRecycled) {
                working.recycle()
            }
            working = scaled
        }

        if (minOf(working.width, working.height) < MIN_ICON_SIZE_PX) {
            val centered = working.centerInSafeCanvas() ?: return null
            if (centered !== working && working !== source && !working.isRecycled) {
                working.recycle()
            }
            working = centered
        }

        return working
    }

    @Synchronized
    fun fallbackBitmap(): Bitmap {
        val cached = cachedFallback
        if (cached != null && !cached.isRecycled) return cached
        return Bitmap.createBitmap(
                FALLBACK_ICON_SIZE_PX,
                FALLBACK_ICON_SIZE_PX,
                Bitmap.Config.ARGB_8888,
            )
            .also { cachedFallback = it }
    }

    private fun Bitmap.toSafeSoftwareBitmap(): Bitmap? {
        if (isRecycled) return null
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && config?.name == "HARDWARE") {
                copy(Bitmap.Config.ARGB_8888, false)
            } else {
                this
            }
        } catch (e: RuntimeException) {
            L.w(e, "Failed to convert notification artwork to a software bitmap")
            null
        }
    }

    private fun Bitmap.scaleToMaxSide(maxSide: Int): Bitmap? {
        val scale = maxSide.toFloat() / maxOf(width, height)
        val scaledWidth = (width * scale).roundToInt().coerceAtLeast(1)
        val scaledHeight = (height * scale).roundToInt().coerceAtLeast(1)
        return try {
            Bitmap.createScaledBitmap(this, scaledWidth, scaledHeight, true)
        } catch (e: RuntimeException) {
            L.w(e, "Failed to scale notification artwork bitmap")
            null
        }
    }

    private fun Bitmap.centerInSafeCanvas(): Bitmap? {
        return try {
            val out = Bitmap.createBitmap(
                FALLBACK_ICON_SIZE_PX,
                FALLBACK_ICON_SIZE_PX,
                Bitmap.Config.ARGB_8888,
            )
            val canvas = Canvas(out)
            canvas.drawColor(Color.TRANSPARENT)
            val scale = minOf(
                FALLBACK_ICON_SIZE_PX.toFloat() / width,
                FALLBACK_ICON_SIZE_PX.toFloat() / height,
            )
            val scaledWidth = width * scale
            val scaledHeight = height * scale
            val left = (FALLBACK_ICON_SIZE_PX - scaledWidth) / 2f
            val top = (FALLBACK_ICON_SIZE_PX - scaledHeight) / 2f
            canvas.drawBitmap(
                this,
                null,
                RectF(left, top, left + scaledWidth, top + scaledHeight),
                paint,
            )
            out
        } catch (e: RuntimeException) {
            L.w(e, "Failed to center notification artwork bitmap on safe canvas")
            null
        }
    }
}
