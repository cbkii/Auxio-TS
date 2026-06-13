/*
 * Copyright (c) 2026 Auxio Project
 * NotificationBitmapSafety.kt is part of Auxio.
 */
package org.oxycblt.auxio.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import kotlin.math.min

/** TS18-safe large-icon bitmap policy for RemoteViews/SystemUI renderers. */
object NotificationBitmapSafety {
    const val MIN_ICON_SIZE_PX = 96
    const val MAX_ICON_SIZE_PX = 512
    const val FALLBACK_ICON_SIZE_PX = 256

    fun sanitize(input: Bitmap?): Bitmap? {
        if (input == null || input.isRecycled || input.width <= 0 || input.height <= 0) return null
        val maxSide = maxOf(input.width, input.height)
        val minSide = minOf(input.width, input.height)
        return when {
            minSide < MIN_ICON_SIZE_PX -> centerInSafeCanvas(input)
            maxSide > MAX_ICON_SIZE_PX -> scaleDown(input)
            input.config == Bitmap.Config.HARDWARE -> input.copy(Bitmap.Config.ARGB_8888, false)
            else -> input
        }
    }

    fun fallbackBitmap(): Bitmap = Bitmap.createBitmap(FALLBACK_ICON_SIZE_PX, FALLBACK_ICON_SIZE_PX, Bitmap.Config.ARGB_8888)

    private fun centerInSafeCanvas(input: Bitmap): Bitmap {
        val out = fallbackBitmap()
        val canvas = Canvas(out)
        canvas.drawColor(Color.TRANSPARENT)
        val scale = min(FALLBACK_ICON_SIZE_PX.toFloat() / input.width, FALLBACK_ICON_SIZE_PX.toFloat() / input.height)
        val width = input.width * scale
        val height = input.height * scale
        val left = (FALLBACK_ICON_SIZE_PX - width) / 2f
        val top = (FALLBACK_ICON_SIZE_PX - height) / 2f
        canvas.drawBitmap(input, null, android.graphics.RectF(left, top, left + width, top + height), Paint(Paint.FILTER_BITMAP_FLAG))
        return out
    }

    private fun scaleDown(input: Bitmap): Bitmap {
        val scale = MAX_ICON_SIZE_PX.toFloat() / maxOf(input.width, input.height)
        val width = (input.width * scale).toInt().coerceAtLeast(MIN_ICON_SIZE_PX)
        val height = (input.height * scale).toInt().coerceAtLeast(MIN_ICON_SIZE_PX)
        return Bitmap.createScaledBitmap(input, width, height, true)
    }
}
