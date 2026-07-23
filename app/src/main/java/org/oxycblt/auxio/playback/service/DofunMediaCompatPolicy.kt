/*
 * Copyright (c) 2026 Auxio Project
 * DofunMediaCompatPolicy.kt is part of Auxio.
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

import android.view.KeyEvent
import org.oxycblt.auxio.headunit.topway.Ts18LauncherIntegrationMode

/** Notification layouts available to the one canonical Auxio playback notification. */
enum class PlaybackNotificationProfile {
    RichAuxio,
    GenericDofun,
}

/** Pure policy for the Android-standard DoFun compatibility lane. */
object DofunMediaCompatPolicy {
    val compactActionIndices: IntArray = intArrayOf(0, 1, 2)

    fun notificationProfile(
        mode: Ts18LauncherIntegrationMode,
        topwayCompatFlavor: Boolean,
    ): PlaybackNotificationProfile =
        if (topwayCompatFlavor && mode.usesGenericMediaNotification) {
            PlaybackNotificationProfile.GenericDofun
        } else {
            PlaybackNotificationProfile.RichAuxio
        }

    fun genericActionKeyCodes(isPlaying: Boolean): IntArray =
        intArrayOf(
            KeyEvent.KEYCODE_MEDIA_PREVIOUS,
            if (isPlaying) KeyEvent.KEYCODE_MEDIA_PAUSE else KeyEvent.KEYCODE_MEDIA_PLAY,
            KeyEvent.KEYCODE_MEDIA_NEXT,
        )

    fun isColdStartPlayKey(keyCode: Int): Boolean =
        keyCode == KeyEvent.KEYCODE_MEDIA_PLAY ||
            keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE ||
            keyCode == KeyEvent.KEYCODE_HEADSETHOOK
}
