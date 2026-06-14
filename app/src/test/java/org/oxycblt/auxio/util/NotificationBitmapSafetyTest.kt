/*
 * Copyright (c) 2026 Auxio Project
 * NotificationBitmapSafetyTest.kt is part of Auxio.
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

package org.oxycblt.auxio.util

import android.graphics.Bitmap
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NotificationBitmapSafetyTest {
    @Test
    fun fallbackBitmapIsTs18SafeSizeAndCached() {
        val bitmap = NotificationBitmapSafety.fallbackBitmap()
        assertEquals(NotificationBitmapSafety.FALLBACK_ICON_SIZE_PX, bitmap.width)
        assertEquals(NotificationBitmapSafety.FALLBACK_ICON_SIZE_PX, bitmap.height)
        assertSame(bitmap, NotificationBitmapSafety.fallbackBitmap())
    }

    @Test
    fun tinyArtworkIsPaddedToSafeCanvas() {
        val sanitized =
            NotificationBitmapSafety.sanitize(Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888))
        assertNotNull(sanitized)
        assertEquals(NotificationBitmapSafety.FALLBACK_ICON_SIZE_PX, sanitized!!.width)
        assertEquals(NotificationBitmapSafety.FALLBACK_ICON_SIZE_PX, sanitized.height)
    }

    @Test
    fun oversizedArtworkIsBounded() {
        val sanitized =
            NotificationBitmapSafety.sanitize(
                Bitmap.createBitmap(2048, 1024, Bitmap.Config.ARGB_8888)
            )
        assertNotNull(sanitized)
        assertTrue(
            maxOf(sanitized!!.width, sanitized.height) <= NotificationBitmapSafety.MAX_ICON_SIZE_PX
        )
    }

    @Test
    fun extremeAspectRatioArtworkIsPaddedWithoutDistortion() {
        val sanitized =
            NotificationBitmapSafety.sanitize(
                Bitmap.createBitmap(100, 2048, Bitmap.Config.ARGB_8888)
            )
        assertNotNull(sanitized)
        assertEquals(NotificationBitmapSafety.FALLBACK_ICON_SIZE_PX, sanitized!!.width)
        assertEquals(NotificationBitmapSafety.FALLBACK_ICON_SIZE_PX, sanitized.height)
    }

    @Test
    fun nullArtworkStaysNull() {
        assertEquals(null, NotificationBitmapSafety.sanitize(null))
    }
}
