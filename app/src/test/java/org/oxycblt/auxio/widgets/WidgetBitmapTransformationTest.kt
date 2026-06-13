/*
 * Copyright (c) 2026 Auxio Project
 * WidgetBitmapTransformationTest.kt is part of Auxio.
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

package org.oxycblt.auxio.widgets

import android.content.res.Resources
import android.graphics.Bitmap
import coil3.size.Size
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class WidgetBitmapTransformationTest {
    private fun maxBitmapArea(reduce: Float): Int {
        val metrics = Resources.getSystem().displayMetrics
        return (1.5 * metrics.widthPixels * metrics.heightPixels / reduce).toInt()
    }

    @Test
    fun transform_smallBitmap_isNotUpscaled() = runBlocking {
        val transformation = WidgetBitmapTransformation(3f)
        val small = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888)
        val result = transformation.transform(small, Size.ORIGINAL)
        // Upscaling a small cover would waste memory and CPU; the input must pass through.
        assertSame(small, result)
    }

    @Test
    fun transform_oversizedBitmap_isDownscaledToCap() = runBlocking {
        val reduce = 3f
        val cap = maxBitmapArea(reduce)
        val transformation = WidgetBitmapTransformation(reduce)
        // Build a square bitmap guaranteed to exceed the cap.
        var side = 1
        while (side * side <= cap) {
            side *= 2
        }
        val oversized = Bitmap.createBitmap(side, side, Bitmap.Config.ARGB_8888)
        val result = transformation.transform(oversized, Size.ORIGINAL)
        assertTrue(
            "result area ${result.width * result.height} must fit cap $cap",
            result.width * result.height <= cap,
        )
    }
}
