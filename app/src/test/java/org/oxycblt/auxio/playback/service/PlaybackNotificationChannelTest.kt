/*
 * Copyright (c) 2026 Auxio Project
 * PlaybackNotificationChannelTest.kt is part of Auxio.
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

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.provider.Settings
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
class PlaybackNotificationChannelTest {
    private lateinit var context: Context
    private lateinit var notificationManager: NotificationManager

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.deleteNotificationChannel(PlaybackNotificationChannel.id)
    }

    @After
    fun tearDown() {
        notificationManager.deleteNotificationChannel(PlaybackNotificationChannel.id)
    }

    @Test
    fun `missing channel reports not created and opens app settings`() {
        val snapshot = PlaybackNotificationChannel.inspect(context)
        assertEquals(PlaybackChannelState.NotCreated, snapshot.state)

        val intent = PlaybackNotificationChannel.settingsIntent(context)
        assertEquals(Settings.ACTION_APP_NOTIFICATION_SETTINGS, intent.action)
        assertEquals(context.packageName, intent.getStringExtra(Settings.EXTRA_APP_PACKAGE))
    }

    @Test
    fun `existing channel reports importance and opens exact channel settings`() {
        notificationManager.createNotificationChannel(
            NotificationChannel(
                PlaybackNotificationChannel.id,
                "Playback",
                NotificationManager.IMPORTANCE_LOW,
            )
        )

        val snapshot = PlaybackNotificationChannel.inspect(context)
        assertEquals(PlaybackChannelState.Usable, snapshot.state)
        assertEquals(NotificationManager.IMPORTANCE_LOW, snapshot.importance)

        val intent = PlaybackNotificationChannel.settingsIntent(context)
        assertEquals(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS, intent.action)
        assertEquals(context.packageName, intent.getStringExtra(Settings.EXTRA_APP_PACKAGE))
        assertEquals(
            PlaybackNotificationChannel.id,
            intent.getStringExtra(Settings.EXTRA_CHANNEL_ID),
        )
    }

    @Test
    fun `zero importance is reported as blocked`() {
        notificationManager.createNotificationChannel(
            NotificationChannel(
                PlaybackNotificationChannel.id,
                "Playback",
                NotificationManager.IMPORTANCE_NONE,
            )
        )

        val snapshot = PlaybackNotificationChannel.inspect(context)
        assertEquals(PlaybackChannelState.Blocked, snapshot.state)
        assertTrue(snapshot.importance == NotificationManager.IMPORTANCE_NONE)
    }
}
