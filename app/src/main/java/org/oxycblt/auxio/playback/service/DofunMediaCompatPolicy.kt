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

/** Pure state required to build the conventional generic playback notification. */
data class GenericPlaybackNotificationState(
    val actionKeyCodes: IntArray,
    val ongoing: Boolean,
    val deleteKeyCode: Int,
)

/** Pure policy for the Android-standard DoFun compatibility lane. */
object DofunMediaCompatPolicy {
    /** Return a defensive array so notification callers cannot mutate process-wide policy. */
    val compactActionIndices: IntArray
        get() = intArrayOf(0, 1, 2)

    fun notificationProfile(
        mode: Ts18LauncherIntegrationMode,
        topwayCompatFlavor: Boolean,
    ): PlaybackNotificationProfile =
        if (topwayCompatFlavor && mode.usesGenericDofunNotificationProfile) {
            PlaybackNotificationProfile.GenericDofun
        } else {
            PlaybackNotificationProfile.RichAuxio
        }

    /**
     * Stock-name wrapper controls must remain live for the explicit Topway transport modes,
     * including broadcast-only mode. Generic and Android-only profiles route their wrapper controls
     * through the canonical media-button service path. `AutoAllSafePaths` keeps Topway wrapper
     * controls while its canonical playback notification independently uses the generic three-action
     * DoFun profile.
     */
    fun usesCanonicalWidgetControls(mode: Ts18LauncherIntegrationMode): Boolean =
        mode.usesGenericDofunProfile || mode == Ts18LauncherIntegrationMode.AndroidMediaSessionOnly

    /** Republish current public legacy state only on a disabled -> enabled mode transition. */
    fun shouldRepublishLegacyAndroidMediaBroadcasts(
        previousMode: Ts18LauncherIntegrationMode,
        newMode: Ts18LauncherIntegrationMode,
    ): Boolean =
        !previousMode.publishesLegacyAndroidMediaBroadcasts &&
            newMode.publishesLegacyAndroidMediaBroadcasts

    fun genericNotificationState(isPlaying: Boolean): GenericPlaybackNotificationState =
        GenericPlaybackNotificationState(
            actionKeyCodes =
                intArrayOf(
                    KeyEvent.KEYCODE_MEDIA_PREVIOUS,
                    if (isPlaying) {
                        KeyEvent.KEYCODE_MEDIA_PAUSE
                    } else {
                        KeyEvent.KEYCODE_MEDIA_PLAY
                    },
                    KeyEvent.KEYCODE_MEDIA_NEXT,
                ),
            ongoing = isPlaying,
            deleteKeyCode = KeyEvent.KEYCODE_MEDIA_STOP,
        )

    fun isColdStartPlayKey(keyCode: Int): Boolean =
        keyCode == KeyEvent.KEYCODE_MEDIA_PLAY ||
            keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE ||
            keyCode == KeyEvent.KEYCODE_HEADSETHOOK
}
