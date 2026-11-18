/*
 * Copyright (c) 2026 Auxio Project
 * MediaSessionInitializationPolicy.kt is part of Auxio.
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

import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat

/** Pure values installed before the canonical playback session becomes active. */
object MediaSessionInitializationPolicy {
    const val HOME_PLAY_V1 = "org.oxycblt.auxio.action.HOME_PLAY_V1"

    fun deadlineAction(label: String): PlaybackStateCompat.CustomAction =
        PlaybackStateCompat.CustomAction.Builder(
                HOME_PLAY_V1,
                label,
                org.oxycblt.auxio.R.drawable.ic_play_24,
            )
            .build()

    fun withDeadlineAction(
        builder: PlaybackStateCompat.Builder,
        label: String,
    ): PlaybackStateCompat.Builder = builder.addCustomAction(deadlineAction(label))

    const val FLAGS =
        MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or
            MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS or
            MediaSessionCompat.FLAG_HANDLES_QUEUE_COMMANDS

    fun initialPlaybackState(label: String): PlaybackStateCompat =
        withDeadlineAction(
                PlaybackStateCompat.Builder().setActions(MediaSessionInterface.ACTIONS),
                label,
            )
            .setState(PlaybackStateCompat.STATE_NONE, 0L, 0f, 0L)
            .build()

    /** Empty libraries remain explicitly stopped and, at the holder boundary, inactive. */
    fun emptyPlaybackState(label: String): PlaybackStateCompat =
        withDeadlineAction(
                PlaybackStateCompat.Builder().setActions(MediaSessionInterface.ACTIONS),
                label,
            )
            .setState(PlaybackStateCompat.STATE_STOPPED, 0L, 0f, 0L)
            .build()
}
