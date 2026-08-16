/*
 * Copyright (c) 2026 Auxio Project
 * GeneratedPlaylistLibraryViewTest.kt is part of Auxio.
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

package org.oxycblt.musikr.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test
import org.oxycblt.musikr.Album
import org.oxycblt.musikr.Artist
import org.oxycblt.musikr.Genre
import org.oxycblt.musikr.Music
import org.oxycblt.musikr.MutableLibrary
import org.oxycblt.musikr.Playlist
import org.oxycblt.musikr.Song
import org.oxycblt.musikr.fs.Path

class GeneratedPlaylistLibraryViewTest {
    @Test
    fun `base-library access does not compile generated playlists`() {
        var compilations = 0
        val view =
            GeneratedPlaylistLibraryView(EmptyLibrary) {
                compilations++
                emptyList()
            }

        assertTrue(view.songs.isEmpty())
        assertTrue(view.albums.isEmpty())
        assertTrue(view.artists.isEmpty())
        assertTrue(view.genres.isEmpty())
        assertTrue(view.empty())
        assertSame(view, view.withGeneratedPlaylists(true))
        assertEquals(0, compilations)
    }

    @Test
    fun `playlist access compiles once and memoizes the projection`() {
        var compilations = 0
        val view =
            GeneratedPlaylistLibraryView(EmptyLibrary) {
                compilations++
                emptyList()
            }

        assertTrue(view.playlists.isEmpty())
        assertTrue(view.playlists.isEmpty())
        assertEquals(null, view.findPlaylistByName("missing"))
        assertEquals(1, compilations)
    }

    @Test
    fun `disabling generated playlists before access does not compile them`() {
        var compilations = 0
        val view =
            GeneratedPlaylistLibraryView(EmptyLibrary) {
                compilations++
                emptyList()
            }

        assertSame(EmptyLibrary, view.withGeneratedPlaylists(false))
        assertEquals(0, compilations)
    }

    private object EmptyLibrary : MutableLibrary {
        override val songs: Collection<Song> = emptyList()
        override val albums: Collection<Album> = emptyList()
        override val artists: Collection<Artist> = emptyList()
        override val genres: Collection<Genre> = emptyList()
        override val playlists: Collection<Playlist> = emptyList()

        override fun empty() = true

        override fun findSong(uid: Music.UID): Song? = null

        override fun findSongByPath(path: Path): Song? = null

        override fun findAlbum(uid: Music.UID): Album? = null

        override fun findArtist(uid: Music.UID): Artist? = null

        override fun findGenre(uid: Music.UID): Genre? = null

        override fun findPlaylist(uid: Music.UID): Playlist? = null

        override fun findPlaylistByName(name: String): Playlist? = null

        override suspend fun createPlaylist(name: String, songs: List<Song>): MutableLibrary = this

        override suspend fun renamePlaylist(playlist: Playlist, name: String): MutableLibrary = this

        override suspend fun addToPlaylist(
            playlist: Playlist,
            songs: List<Song>,
        ): MutableLibrary = this

        override suspend fun rewritePlaylist(
            playlist: Playlist,
            songs: List<Song>,
        ): MutableLibrary = this

        override suspend fun deletePlaylist(playlist: Playlist): MutableLibrary = this
    }
}
