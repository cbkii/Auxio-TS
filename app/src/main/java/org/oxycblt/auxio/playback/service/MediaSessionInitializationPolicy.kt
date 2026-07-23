/*
 * Copyright (c) 2026 Auxio Project
 * MediaSessionInitializationPolicy.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
}
