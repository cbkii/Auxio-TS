/*
 * Copyright (c) 2026 Auxio Project
 * MediaSessionInitializationPolicyTest.kt is part of Auxio.
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

import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.car.app.mediaextensions.MetadataExtras
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.oxycblt.auxio.BuildConfig
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MediaSessionInitializationPolicyTest {
    @Test
    fun `initial flags expose media buttons transports and queue commands`() {
        val flags = MediaSessionInitializationPolicy.FLAGS

        assertTrue(flags and MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS != 0)
        assertTrue(flags and MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS != 0)
        assertTrue(flags and MediaSessionCompat.FLAG_HANDLES_QUEUE_COMMANDS != 0)
    }

    @Test
    fun `initial state is inactive but advertises canonical actions`() {
        val state = MediaSessionInitializationPolicy.initialPlaybackState()

        assertEquals(PlaybackStateCompat.STATE_NONE, state.state)
        assertEquals(0L, state.position)
        assertEquals(MediaSessionInterface.ACTIONS, state.actions)
    }

    @Test
    fun `empty ready state is explicitly stopped`() {
        val state = MediaSessionInitializationPolicy.emptyPlaybackState()

        assertEquals(PlaybackStateCompat.STATE_STOPPED, state.state)
        assertEquals(0L, state.position)
        assertEquals(MediaSessionInterface.ACTIONS, state.actions)
    }

    @Test
    fun `empty metadata publishes non-null values for every cleared vendor field`() {
        val metadata = MediaSessionHolder.emptyMetadata
        val stringKeys =
            listOf(
                MediaMetadataCompat.METADATA_KEY_TITLE,
                MediaMetadataCompat.METADATA_KEY_ARTIST,
                MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST,
                MediaMetadataCompat.METADATA_KEY_ALBUM,
                MediaMetadataCompat.METADATA_KEY_AUTHOR,
                MediaMetadataCompat.METADATA_KEY_COMPOSER,
                MediaMetadataCompat.METADATA_KEY_WRITER,
                MediaMetadataCompat.METADATA_KEY_GENRE,
                MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE,
                MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE,
                MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION,
                MediaMetadataCompat.METADATA_KEY_MEDIA_ID,
                MediaMetadataCompat.METADATA_KEY_MEDIA_URI,
                MediaMetadataCompat.METADATA_KEY_DATE,
                MediaMetadataCompat.METADATA_KEY_ART_URI,
                MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI,
                MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI,
                BuildConfig.APPLICATION_ID + ".metadata.PARENT",
                MetadataExtras.KEY_SUBTITLE_LINK_MEDIA_ID,
                MetadataExtras.KEY_DESCRIPTION_LINK_MEDIA_ID,
            )

        stringKeys.forEach {
            assertEquals("Expected non-null empty value for $it", "", metadata.getString(it))
        }
        assertEquals(0L, metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION))
        assertEquals(0L, metadata.getLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER))
        assertEquals(0L, metadata.getLong(MediaMetadataCompat.METADATA_KEY_DISC_NUMBER))
        assertEquals(0L, metadata.getLong(MediaMetadataCompat.METADATA_KEY_YEAR))
    }
}
