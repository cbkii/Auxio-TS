/*
 * Copyright (c) 2024 Auxio Project
 * HeadUnitMetadataPolicyTest.kt is part of Auxio.
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

package org.oxycblt.auxio.headunit.compat

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class HeadUnitMetadataPolicyTest {
    @Test
    fun fromRaw_without_title_returns_null() {
        assertNull(
            HeadUnitMetadataPolicy.fromRaw(null, "a", "b", "c", 1L, "id", "uri", null, false)
        )
    }

    @Test
    fun fromRaw_builds_consistent_snapshot() {
        val s =
            HeadUnitMetadataPolicy.fromRaw(
                "Track",
                "Artist",
                "Album Artist",
                "Album",
                1000L,
                "1",
                "u",
                "art",
                true,
            )!!
        assertEquals("Track", s.displayTitle)
        assertTrue(s.displaySubtitle.contains("Artist"))
        assertEquals("Album", s.displayDescription)
    }

    @Test
    fun fromRaw_deduplicates_artist_subtitle_and_handles_blank_artwork_uri() {
        val s =
            HeadUnitMetadataPolicy.fromRaw(
                "Track",
                "Artist",
                "Artist",
                "",
                1L,
                "id",
                "uri",
                "  ",
                false,
            )!!
        assertEquals("Artist", s.displaySubtitle)
        assertEquals("Artist", s.displayDescription)
        assertFalse(s.hasArtwork)
    }

    @Test
    fun fromRaw_accepts_char_sequence_widget_metadata() {
        val s =
            HeadUnitMetadataPolicy.fromRaw(
                StringBuilder(" Track "),
                StringBuilder(" Artist "),
                StringBuilder(" Artist "),
                StringBuilder(" Album "),
                1000L,
                "1",
                "u",
                null,
                false,
            )!!
        assertEquals("Track", s.displayTitle)
        assertEquals("Artist", s.displaySubtitle)
        assertEquals("Album", s.displayDescription)
    }

    @Test
    fun fromRaw_clamps_duration_hashes_oversized_ids_and_rejects_oversized_uris() {
        val oversized = "x".repeat(5000)
        val s =
            HeadUnitMetadataPolicy.fromRaw(
                oversized,
                oversized,
                null,
                null,
                -123L,
                oversized,
                oversized,
                oversized,
                false,
            )!!

        assertEquals(512, s.displayTitle.length)
        assertEquals(512, s.artist.length)
        assertTrue(s.mediaId.startsWith("sha256:"))
        assertEquals(71, s.mediaId.length)
        assertTrue(s.mediaId.removePrefix("sha256:").matches(Regex("[0-9a-f]{64}")))
        assertEquals("", s.mediaUri)
        assertNull(s.artworkUri)
        assertTrue(!s.hasArtwork)
        assertEquals(0L, s.durationMs)
    }

    @Test
    fun fromRaw_preserves_valid_uris_without_truncation() {
        val mediaUri = "content://media/external/audio/media/42"
        val artworkUri = "content://org.oxycblt.auxio.covers/42"
        val s =
            HeadUnitMetadataPolicy.fromRaw(
                "Track",
                "Artist",
                null,
                null,
                1L,
                "stable-id",
                mediaUri,
                artworkUri,
                false,
            )!!

        assertEquals("stable-id", s.mediaId)
        assertEquals(mediaUri, s.mediaUri)
        assertEquals(artworkUri, s.artworkUri)
        assertTrue(s.hasArtwork)
    }
}
