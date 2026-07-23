/*
 * Copyright (c) 2026 Auxio Project
 * DofunMediaCompatPolicyTest.kt is part of Auxio.
 */

package org.oxycblt.auxio.playback.service

import android.app.NotificationManager
import android.view.KeyEvent
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test
import org.oxycblt.auxio.headunit.topway.Ts18LauncherIntegrationMode

class DofunMediaCompatPolicyTest {
    @Test
    fun `generic profile is isolated to topway generic mode`() {
        assertEquals(
            PlaybackNotificationProfile.GenericDofun,
            DofunMediaCompatPolicy.notificationProfile(
                Ts18LauncherIntegrationMode.GenericDofunMedia,
                topwayCompatFlavor = true,
            ),
        )
        assertEquals(
            PlaybackNotificationProfile.RichAuxio,
            DofunMediaCompatPolicy.notificationProfile(
                Ts18LauncherIntegrationMode.GenericDofunMedia,
                topwayCompatFlavor = false,
            ),
        )
        assertEquals(
            PlaybackNotificationProfile.RichAuxio,
            DofunMediaCompatPolicy.notificationProfile(
                Ts18LauncherIntegrationMode.AutoAllSafePaths,
                topwayCompatFlavor = true,
            ),
        )
    }

    @Test
    fun `generic actions are conventional previous play pause next`() {
        assertArrayEquals(intArrayOf(0, 1, 2), DofunMediaCompatPolicy.compactActionIndices)
        assertArrayEquals(
            intArrayOf(
                KeyEvent.KEYCODE_MEDIA_PREVIOUS,
                KeyEvent.KEYCODE_MEDIA_PLAY,
                KeyEvent.KEYCODE_MEDIA_NEXT,
            ),
            DofunMediaCompatPolicy.genericActionKeyCodes(isPlaying = false),
        )
        assertArrayEquals(
            intArrayOf(
                KeyEvent.KEYCODE_MEDIA_PREVIOUS,
                KeyEvent.KEYCODE_MEDIA_PAUSE,
                KeyEvent.KEYCODE_MEDIA_NEXT,
            ),
            DofunMediaCompatPolicy.genericActionKeyCodes(isPlaying = true),
        )
    }

    @Test
    fun `playback channel classification fails closed`() {
        assertEquals(
            PlaybackChannelState.Blocked,
            PlaybackNotificationChannel.classify(
                notificationsEnabled = false,
                channelExists = true,
                importance = NotificationManager.IMPORTANCE_LOW,
            ),
        )
        assertEquals(
            PlaybackChannelState.NotCreated,
            PlaybackNotificationChannel.classify(
                notificationsEnabled = true,
                channelExists = false,
                importance = null,
            ),
        )
        assertEquals(
            PlaybackChannelState.Blocked,
            PlaybackNotificationChannel.classify(
                notificationsEnabled = true,
                channelExists = true,
                importance = NotificationManager.IMPORTANCE_NONE,
            ),
        )
        assertEquals(
            PlaybackChannelState.Usable,
            PlaybackNotificationChannel.classify(
                notificationsEnabled = true,
                channelExists = true,
                importance = NotificationManager.IMPORTANCE_LOW,
            ),
        )
    }
}
