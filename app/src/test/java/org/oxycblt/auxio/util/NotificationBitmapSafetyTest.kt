package org.oxycblt.auxio.util

import android.graphics.Bitmap
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NotificationBitmapSafetyTest {
    @Test fun fallbackBitmapIsTs18SafeSize() {
        val bitmap = NotificationBitmapSafety.fallbackBitmap()
        assertEquals(NotificationBitmapSafety.FALLBACK_ICON_SIZE_PX, bitmap.width)
        assertEquals(NotificationBitmapSafety.FALLBACK_ICON_SIZE_PX, bitmap.height)
    }
    @Test fun tinyArtworkIsPaddedToSafeCanvas() {
        val sanitized = NotificationBitmapSafety.sanitize(Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888))
        assertNotNull(sanitized)
        assertTrue(sanitized!!.width >= NotificationBitmapSafety.MIN_ICON_SIZE_PX)
        assertTrue(sanitized.height >= NotificationBitmapSafety.MIN_ICON_SIZE_PX)
    }
    @Test fun oversizedArtworkIsBounded() {
        val sanitized = NotificationBitmapSafety.sanitize(Bitmap.createBitmap(2048, 1024, Bitmap.Config.ARGB_8888))
        assertNotNull(sanitized)
        assertTrue(maxOf(sanitized!!.width, sanitized.height) <= NotificationBitmapSafety.MAX_ICON_SIZE_PX)
    }
    @Test fun nullArtworkStaysNull() {
        assertEquals(null, NotificationBitmapSafety.sanitize(null))
    }
}
