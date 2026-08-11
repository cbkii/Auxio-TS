/*
 * Copyright (c) 2026 Auxio Project
 * PlaybackPagerProjectionTest.kt is part of Auxio.
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

package org.oxycblt.auxio.playback.ui.swiper

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.oxycblt.auxio.playback.persist.QueueItemRef
import org.oxycblt.auxio.playback.queue.QueueDisplayItem
import org.oxycblt.auxio.playback.state.RawPlaybackMetadata

class PlaybackPagerProjectionTest {
    @Test
    fun primitiveQueueProducesPlayablePagerAndPreservesGlobalPosition() {
        val first = primitive(logicalPosition = 40, uri = "file:///storage/usbdisk0/40.flac")
        val current = primitive(logicalPosition = 41, uri = "file:///storage/usbdisk0/41.flac")
        val state =
            PlaybackPagerProjection.project(
                queue =
                    listOf(QueueDisplayItem(40, null, first), QueueDisplayItem(41, null, current)),
                queueIndex = 1,
                rawMetadata = raw(uri = current.uri!!),
            )

        assertTrue(state.hasPlayablePage)
        assertEquals(2, state.items.size)
        assertEquals(1, state.activeIndex)
        assertEquals(41, state.items[state.activeIndex].globalPosition)
        assertNull(state.items[state.activeIndex].song)
    }

    @Test
    fun rawFallbackProducesExactlyOnePageWithoutFakeSong() {
        val metadata = raw(uri = "file:///storage/usbdisk0/raw.flac")
        val state =
            PlaybackPagerProjection.project(
                queue = emptyList(),
                queueIndex = -1,
                rawMetadata = metadata,
            )

        assertTrue(state.hasPlayablePage)
        assertEquals(1, state.items.size)
        assertEquals(0, state.activeIndex)
        assertTrue(state.items.single() is PlaybackPagerItem.Raw)
        assertNull(state.items.single().song)
    }

    @Test
    fun queueAuthorityWinsOverRawFallback() {
        val item = primitive(logicalPosition = 9, uri = "file:///storage/usbdisk0/queue.flac")
        val state =
            PlaybackPagerProjection.project(
                queue = listOf(QueueDisplayItem(9, null, item)),
                queueIndex = 0,
                rawMetadata = raw(uri = "file:///storage/usbdisk0/raw.flac"),
            )

        assertEquals(1, state.items.size)
        assertTrue(state.items.single() is PlaybackPagerItem.Primitive)
        assertEquals(9, state.items.single().globalPosition)
    }

    @Test
    fun rawAndPrimitiveSharedUriAreSameLogicalItemForHydrationTransition() {
        val uri = "file:///storage/usbdisk0/shared.flac"
        val primitive = PlaybackPagerItem.Primitive(3, primitive(3, uri))
        val raw = PlaybackPagerItem.Raw(raw(uri))

        assertTrue(PlaybackPagerItem.sameLogicalItem(raw, primitive))
        assertTrue(PlaybackPagerItem.sameLogicalItem(primitive, raw))
        assertEquals(raw.visualizerTrackKey, primitive.visualizerTrackKey)
    }

    @Test
    fun visualizerIdentityPrefersUriWhenHydratedStateAlsoHasUid() {
        val uriKey = "uri:file:///storage/usbdisk0/shared.flac"
        val raw = PlaybackPagerItem.Raw(raw(uri = uriKey.removePrefix("uri:")))
        val hydratedIdentity = linkedSetOf("uid:rich-song-123", uriKey)

        assertEquals(uriKey, raw.visualizerTrackKey)
        assertEquals(
            raw.visualizerTrackKey,
            selectVisualizerTrackKey(hydratedIdentity, fallback = "fallback"),
        )
    }

    @Test
    fun duplicateQueueReferencesAtDifferentLogicalPositionsStayDistinct() {
        val uri = "file:///storage/usbdisk0/duplicate.flac"
        val first = PlaybackPagerItem.Primitive(2, primitive(2, uri))
        val second = PlaybackPagerItem.Primitive(7, primitive(7, uri))

        assertFalse(PlaybackPagerItem.sameLogicalItem(first, second))
    }

    @Test
    fun emptyStateHasNoPlayablePage() {
        val state =
            PlaybackPagerProjection.project(
                queue = emptyList(),
                queueIndex = -1,
                rawMetadata = null,
            )

        assertFalse(state.hasPlayablePage)
        assertTrue(state.items.isEmpty())
        assertEquals(-1, state.activeIndex)
    }

    private fun primitive(logicalPosition: Int, uri: String) =
        QueueItemRef(
            logicalPosition = logicalPosition,
            canonicalPosition = logicalPosition,
            stableSongUid = null,
            uri = uri,
            pathFallback = "/storage/usbdisk0/$logicalPosition.flac",
            titleFallback = "Song $logicalPosition",
            artistFallback = "Artist",
            albumFallback = "Album",
            durationMs = 180_000L,
        )

    private fun raw(uri: String) =
        RawPlaybackMetadata(
            title = "Song",
            artist = "Artist",
            album = "Album",
            uriString = uri,
            path = "/storage/usbdisk0/song.flac",
            durationMs = 180_000L,
            positionMs = 42_000L,
            isPlaying = true,
            savedAtMs = 1L,
        )
}
