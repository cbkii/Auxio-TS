/*
 * Copyright (c) 2026 Auxio Project
 * DofunMediaCompatPolicyTest.kt is part of Auxio.
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

import android.app.NotificationManager
import android.view.KeyEvent
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.oxycblt.auxio.headunit.topway.Ts18LauncherIntegrationMode

class DofunMediaCompatPolicyTest {
    @Test
    fun `generic notification profile is retained by all safe paths`() {
        assertEquals(
            PlaybackNotificationProfile.GenericDofun,
            DofunMediaCompatPolicy.notificationProfile(
                Ts18LauncherIntegrationMode.GenericDofunMedia,
                topwayCompatFlavor = true,
            ),
        )
        assertEquals(
            PlaybackNotificationProfile.GenericDofun,
            DofunMediaCompatPolicy.notificationProfile(
                Ts18LauncherIntegrationMode.AutoAllSafePaths,
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
                Ts18LauncherIntegrationMode.TopwayBroadcastAndCommand,
                topwayCompatFlavor = true,
            ),
        )
    }

    @Test
    fun `non-private wrapper profiles use canonical media controls`() {
        assertTrue(
            DofunMediaCompatPolicy.usesCanonicalWidgetControls(
                Ts18LauncherIntegrationMode.GenericDofunMedia
            )
        )
        assertTrue(
            DofunMediaCompatPolicy.usesCanonicalWidgetControls(
                Ts18LauncherIntegrationMode.AndroidMediaSessionOnly
            )
        )
        assertFalse(
            DofunMediaCompatPolicy.usesCanonicalWidgetControls(
                Ts18LauncherIntegrationMode.AutoAllSafePaths
            )
        )
        assertFalse(
            DofunMediaCompatPolicy.usesCanonicalWidgetControls(
                Ts18LauncherIntegrationMode.TopwayCommandOnly
            )
        )
    }

    @Test
    fun `compact action indices are defensively copied`() {
        val first = DofunMediaCompatPolicy.compactActionIndices
        first[0] = 99
        assertArrayEquals(intArrayOf(0, 1, 2), DofunMediaCompatPolicy.compactActionIndices)
    }

    @Test
    fun `generic notification is previous play next while paused`() {
        val state = DofunMediaCompatPolicy.genericNotificationState(isPlaying = false)

        assertArrayEquals(intArrayOf(0, 1, 2), DofunMediaCompatPolicy.compactActionIndices)
        assertArrayEquals(
            intArrayOf(
                KeyEvent.KEYCODE_MEDIA_PREVIOUS,
                KeyEvent.KEYCODE_MEDIA_PLAY,
                KeyEvent.KEYCODE_MEDIA_NEXT,
            ),
            state.actionKeyCodes,
        )
        assertFalse(state.ongoing)
        assertEquals(KeyEvent.KEYCODE_MEDIA_STOP, state.deleteKeyCode)
    }

    @Test
    fun `generic notification is previous pause next while playing`() {
        val state = DofunMediaCompatPolicy.genericNotificationState(isPlaying = true)

        assertArrayEquals(
            intArrayOf(
                KeyEvent.KEYCODE_MEDIA_PREVIOUS,
                KeyEvent.KEYCODE_MEDIA_PAUSE,
                KeyEvent.KEYCODE_MEDIA_NEXT,
            ),
            state.actionKeyCodes,
        )
        assertTrue(state.ongoing)
        assertEquals(KeyEvent.KEYCODE_MEDIA_STOP, state.deleteKeyCode)
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
