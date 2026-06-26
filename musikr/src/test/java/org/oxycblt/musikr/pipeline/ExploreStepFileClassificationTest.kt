/*
 * Copyright (c) 2026 Auxio Project
 * ExploreStepFileClassificationTest.kt is part of Auxio.
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

package org.oxycblt.musikr.pipeline

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExploreStepFileClassificationTest {
    @Test
    fun acceptsKnownAudioMimeTypes() {
        assertTrue(FileClassification.isPotentialMusicFileNameMime("song.mp3", "audio/mpeg"))
        assertTrue(FileClassification.isPotentialMusicFileNameMime("song.ogg", "application/ogg"))
        assertTrue(FileClassification.isPotentialMusicFileNameMime("song.oga", "application/x-ogg"))
    }

    @Test
    fun acceptsOctetStreamOnlyWhenAudioExtensionIsKnown() {
        assertTrue(
            FileClassification.isPotentialMusicFileNameMime(
                "usb-track.flac",
                "application/octet-stream",
            )
        )
        assertTrue(
            FileClassification.isPotentialMusicFileNameMime(
                "usb-track.M4A",
                "application/octet-stream",
            )
        )
        assertTrue(
            FileClassification.isPotentialMusicFileNameMime(
                "usb-track.opus",
                "application/octet-stream",
            )
        )
        assertFalse(
            FileClassification.isPotentialMusicFileNameMime(
                "album-art.jpg",
                "application/octet-stream",
            )
        )
        assertFalse(
            FileClassification.isPotentialMusicFileNameMime("readme", "application/octet-stream")
        )
    }

    @Test
    fun acceptsUnknownMimeTypesWhenAudioExtensionIsKnown() {
        assertTrue(FileClassification.isPotentialMusicFileNameMime("usb-track.mp3", null))
        assertTrue(FileClassification.isPotentialMusicFileNameMime("usb-track.flac", ""))
        assertFalse(FileClassification.isPotentialMusicFileNameMime("album-art.jpg", null))
        assertFalse(FileClassification.isPotentialMusicFileNameMime("readme", ""))
    }

    @Test
    fun rejectsKnownNonAudioMimeTypesAndPlaylists() {
        assertFalse(FileClassification.isPotentialMusicFileNameMime("cover.jpg", "image/jpeg"))
        assertFalse(
            FileClassification.isPotentialMusicFileNameMime("playlist.m3u", "audio/x-mpegurl")
        )
        assertFalse(FileClassification.isPotentialMusicFileNameMime("notes.txt", "text/plain"))
        assertFalse(
            FileClassification.isPotentialMusicFileNameMime(
                "installer.apk",
                "application/vnd.android.package-archive",
            )
        )
    }
}
