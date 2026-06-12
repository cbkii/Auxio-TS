/*
 * Copyright (c) 2026 Auxio Project
 * TopwaySourcePolicyFilterTest.kt is part of Auxio.
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

package org.oxycblt.auxio.headunit.topway

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TopwaySourcePolicyFilterTest {

    @Test
    fun matchesSystemSourceFilter_musicInPath_matches() {
        assertTrue(TopwaySourcePolicy.matchesSystemSourceFilter("/storage/usbdisk0/Music/song.mp3"))
    }

    @Test
    fun matchesSystemSourceFilter_downloadInPath_matches() {
        assertTrue(
            TopwaySourcePolicy.matchesSystemSourceFilter("/storage/emulated/0/Download/track.mp3")
        )
    }

    @Test
    fun matchesSystemSourceFilter_mediaInPath_matches() {
        assertTrue(
            TopwaySourcePolicy.matchesSystemSourceFilter("/storage/emulated/0/Media/audio.ogg")
        )
    }

    @Test
    fun matchesSystemSourceFilter_caseInsensitive_matches() {
        assertTrue(TopwaySourcePolicy.matchesSystemSourceFilter("/storage/usbdisk0/MUSIC/song.mp3"))
        assertTrue(
            TopwaySourcePolicy.matchesSystemSourceFilter("/storage/emulated/0/Downloads/track.mp3")
        )
        assertTrue(
            TopwaySourcePolicy.matchesSystemSourceFilter("/mnt/usbhost/My Music Archive/song.flac")
        )
    }

    @Test
    fun matchesSystemSourceFilter_noKeyword_rejected() {
        assertFalse(TopwaySourcePolicy.matchesSystemSourceFilter("/storage/usbdisk0/Videos/v.mp4"))
    }

    @Test
    fun matchesSystemSourceFilter_androidDataPath_rejected() {
        assertFalse(
            TopwaySourcePolicy.matchesSystemSourceFilter("/storage/emulated/0/Android/data/cache")
        )
    }

    @Test
    fun matchesSystemSourceFilter_randomRoot_rejected() {
        assertFalse(TopwaySourcePolicy.matchesSystemSourceFilter("/storage/usbdisk0/DCIM/img.jpg"))
    }

    @Test
    fun systemSourcePathKeywords_containsExpectedEntries() {
        assertTrue(TopwaySourcePolicy.SYSTEM_SOURCE_PATH_KEYWORDS.contains("music"))
        assertTrue(TopwaySourcePolicy.SYSTEM_SOURCE_PATH_KEYWORDS.contains("download"))
        assertTrue(TopwaySourcePolicy.SYSTEM_SOURCE_PATH_KEYWORDS.contains("media"))
    }
}
