/*
 * Copyright (c) 2026 Auxio Project
 * MediaButtonIntentFactoryTest.kt is part of Auxio.
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

package org.oxycblt.auxio.playback.service

import android.content.Context
import android.content.Intent
import android.view.KeyEvent
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.oxycblt.auxio.AuxioService
import org.oxycblt.auxio.IntegerTable
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
class MediaButtonIntentFactoryTest {
    private val context: Context = ApplicationProvider.getApplicationContext()

    @Test
    fun `receiver intent targets canonical media button receiver`() {
        val intent =
            MediaButtonIntentFactory.receiverIntent(context, KeyEvent.KEYCODE_MEDIA_PREVIOUS)

        assertEquals(Intent.ACTION_MEDIA_BUTTON, intent.action)
        assertEquals(MediaButtonReceiver::class.java.name, intent.component?.className)
        assertKeyEvent(intent, KeyEvent.KEYCODE_MEDIA_PREVIOUS)
    }

    @Test
    fun `service intent targets canonical playback service with media start id`() {
        val intent =
            MediaButtonIntentFactory.serviceIntent(
                context,
                AuxioService::class.java,
                KeyEvent.KEYCODE_MEDIA_NEXT,
            )

        assertEquals(Intent.ACTION_MEDIA_BUTTON, intent.action)
        assertEquals(AuxioService::class.java.name, intent.component?.className)
        assertEquals(
            IntegerTable.START_ID_MEDIA_BUTTON,
            intent.getIntExtra(AuxioService.INTENT_KEY_START_ID, -1),
        )
        assertKeyEvent(intent, KeyEvent.KEYCODE_MEDIA_NEXT)
    }

    @Suppress("DEPRECATION")
    private fun assertKeyEvent(intent: Intent, expectedKeyCode: Int) {
        val event = intent.getParcelableExtra<KeyEvent>(Intent.EXTRA_KEY_EVENT)
        assertNotNull(event)
        assertEquals(KeyEvent.ACTION_DOWN, event?.action)
        assertEquals(expectedKeyCode, event?.keyCode)
    }
}
