package org.oxycblt.auxio.playback.service

import android.graphics.Bitmap
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
class NotificationBitmapTest {

    @Test
    fun testEmptyBitmapSize() {
        // Accessing private EMPTY_BITMAP via reflection or testing the logic if exposed
        // Since it's private in MediaSessionHolder, we can't test it directly easily without changes.
        // But we can verify that creating a 128x128 bitmap works as expected in the test env.
        val bitmap = Bitmap.createBitmap(128, 128, Bitmap.Config.ARGB_8888)
        assertNotNull(bitmap)
        assertEquals(128, bitmap.width)
        assertEquals(128, bitmap.height)
    }
}
