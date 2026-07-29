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
    const val FLAGS =
        MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or
            MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS or
            MediaSessionCompat.FLAG_HANDLES_QUEUE_COMMANDS

    fun initialPlaybackState(): PlaybackStateCompat =
        PlaybackStateCompat.Builder()
            .setActions(MediaSessionInterface.ACTIONS)
            .setState(PlaybackStateCompat.STATE_NONE, 0L, 0f, 0L)
            .build()

    /** Empty libraries remain explicitly stopped and, at the holder boundary, inactive. */
    fun emptyPlaybackState(): PlaybackStateCompat =
        PlaybackStateCompat.Builder()
            .setActions(MediaSessionInterface.ACTIONS)
            .setState(PlaybackStateCompat.STATE_STOPPED, 0L, 0f, 0L)
            .build()
}
