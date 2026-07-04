/*
 * Copyright (c) 2026 Auxio Project
 * StartupPlaybackPolicyTest.kt is part of Auxio.
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

package org.oxycblt.auxio.playback

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.oxycblt.auxio.playback.state.DeferredPlayback

class StartupPlaybackPolicyTest {

    @Test
    fun `restoreActionForLaunch with autoplay disabled returns play false with paused ShuffleAll fallback`() {
        val action = StartupPlaybackPolicy.restoreActionForLaunch(autoplayOnLaunch = false)
        assertFalse(action.play)
        assertEquals(DeferredPlayback.ShuffleAll(play = false), action.fallback)
    }

    @Test
    fun `restoreActionForLaunch with autoplay enabled returns play true with ShuffleAll fallback`() {
        val action = StartupPlaybackPolicy.restoreActionForLaunch(autoplayOnLaunch = true)
        assertTrue(action.play)
        assertEquals(DeferredPlayback.ShuffleAll(play = true), action.fallback)
    }

    @Test
    fun `restoreActionForBoot with autoplay disabled returns play false with paused ShuffleAll fallback`() {
        val action = StartupPlaybackPolicy.restoreActionForBoot(autoplayOnLaunch = false)
        assertFalse(action.play)
        assertEquals(DeferredPlayback.ShuffleAll(play = false), action.fallback)
    }

    @Test
    fun `restoreActionForBoot with autoplay enabled returns play true with ShuffleAll fallback`() {
        val action = StartupPlaybackPolicy.restoreActionForBoot(autoplayOnLaunch = true)
        assertTrue(action.play)
        assertEquals(DeferredPlayback.ShuffleAll(play = true), action.fallback)
    }

    @Test
    fun shouldOpenPanelOnLaunch_falseWhenLibraryNull() {
        val result = StartupPlaybackPolicy.shouldOpenPanelOnLaunch(null)
        assertFalse(result)
    }

    @Test
    fun shouldOpenPanelOnLaunch_falseWhenLibraryEmpty() {
        val emptyLibrary =
            object : org.oxycblt.musikr.Library {
                override val songs = emptyList<org.oxycblt.musikr.Song>()
                override val albums = emptyList<org.oxycblt.musikr.Album>()
                override val artists = emptyList<org.oxycblt.musikr.Artist>()
                override val genres = emptyList<org.oxycblt.musikr.Genre>()
                override val playlists = emptyList<org.oxycblt.musikr.Playlist>()

                override fun empty() = true

                override fun findSong(uid: org.oxycblt.musikr.Music.UID) = null

                override fun findSongByPath(path: org.oxycblt.musikr.fs.Path) = null

                override fun findAlbum(uid: org.oxycblt.musikr.Music.UID) = null

                override fun findArtist(uid: org.oxycblt.musikr.Music.UID) = null

                override fun findGenre(uid: org.oxycblt.musikr.Music.UID) = null

                override fun findPlaylist(uid: org.oxycblt.musikr.Music.UID) = null

                override fun findPlaylistByName(name: String) = null
            }
        val result = StartupPlaybackPolicy.shouldOpenPanelOnLaunch(emptyLibrary)
        assertFalse(result)
    }
}
