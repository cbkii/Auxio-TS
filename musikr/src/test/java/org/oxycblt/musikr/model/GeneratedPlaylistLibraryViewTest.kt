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

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertSame
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
    fun `heavy collections delegate to base by identity`() {
        val base = FakeLibrary()
        val view = GeneratedPlaylistLibraryView(base, emptyList())

        assertSame(base.songs, view.songs)
        assertSame(base.albums, view.albums)
        assertSame(base.artists, view.artists)
        assertSame(base.genres, view.genres)
    }

    @Test
    fun `user mutation wraps new base without cloning heavy collections`() = runBlocking {
        val next = FakeLibrary()
        val base = FakeLibrary().apply { mutationResult = next }
        val view = GeneratedPlaylistLibraryView(base, emptyList())

        val mutated = view.createPlaylist("Road", emptyList())

        assertSame(next.songs, mutated.songs)
        assertSame(next.albums, mutated.albums)
    }

    private class FakeLibrary : MutableLibrary {
        override val songs = emptySet<Song>()
        override val albums = emptySet<Album>()
        override val artists = emptySet<Artist>()
        override val genres = emptySet<Genre>()
        override val playlists = emptySet<Playlist>()
        var mutationResult: MutableLibrary = this

        override fun empty() = songs.isEmpty()

        override fun findSong(uid: Music.UID): Song? = null

        override fun findSongByPath(path: Path): Song? = null

        override fun findAlbum(uid: Music.UID): Album? = null

        override fun findArtist(uid: Music.UID): Artist? = null

        override fun findGenre(uid: Music.UID): Genre? = null

        override fun findPlaylist(uid: Music.UID): Playlist? = null

        override fun findPlaylistByName(name: String): Playlist? = null

        override suspend fun createPlaylist(name: String, songs: List<Song>) = mutationResult

        override suspend fun renamePlaylist(playlist: Playlist, name: String) = mutationResult

        override suspend fun addToPlaylist(playlist: Playlist, songs: List<Song>) = mutationResult

        override suspend fun rewritePlaylist(playlist: Playlist, songs: List<Song>) = mutationResult

        override suspend fun deletePlaylist(playlist: Playlist) = mutationResult
    }
}
