/*
 * Copyright (c) 2026 Auxio Project
 * GeneratedPlaylistLibraryView.kt is part of Auxio.
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

import org.oxycblt.musikr.Album
import org.oxycblt.musikr.Artist
import org.oxycblt.musikr.Genre
import org.oxycblt.musikr.Music
import org.oxycblt.musikr.MutableLibrary
import org.oxycblt.musikr.Playlist
import org.oxycblt.musikr.Song
import org.oxycblt.musikr.fs.Path

/**
 * Lightweight playlist projection over a base library.
 *
 * Song/album/artist/genre collections and lookup maps remain owned by [base]. Generated-playlist
 * compilation is lazy and memoized so merely enabling the feature or restoring the cached base
 * library does not immediately sort/group the whole song collection. The derived playlists are
 * built only when a playlist surface actually asks for them.
 *
 * User playlist mutations update the base and return a newly wrapped view; generated playlists
 * remain immutable and are reused across those playlist-only mutations.
 */
internal class GeneratedPlaylistLibraryView(
    private val base: MutableLibrary,
    generated: () -> Collection<Playlist>,
) : MutableLibrary {
    private val generatedPlaylists: Set<Playlist> by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        generated().toSet()
    }
    private val generatedByUid: Map<Music.UID, Playlist> by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        generatedPlaylists.associateBy { it.uid }
    }

    override val songs: Collection<Song>
        get() = base.songs

    override val albums: Collection<Album>
        get() = base.albums

    override val artists: Collection<Artist>
        get() = base.artists

    override val genres: Collection<Genre>
        get() = base.genres

    override val playlists: Collection<Playlist>
        get() =
            base.playlists.filter { it.origin != Playlist.Origin.GENERATED } + generatedPlaylists

    override fun empty() = base.empty()

    override fun findSong(uid: Music.UID) = base.findSong(uid)

    override fun findSongByPath(path: Path) = base.findSongByPath(path)

    override fun findAlbum(uid: Music.UID) = base.findAlbum(uid)

    override fun findArtist(uid: Music.UID) = base.findArtist(uid)

    override fun findGenre(uid: Music.UID) = base.findGenre(uid)

    override fun findPlaylist(uid: Music.UID) =
        generatedByUid[uid]
            ?: base.findPlaylist(uid)?.takeIf { it.origin != Playlist.Origin.GENERATED }

    override fun findPlaylistByName(name: String) =
        generatedPlaylists.find { it.name.raw == name }
            ?: base.findPlaylistByName(name)?.takeIf { it.origin != Playlist.Origin.GENERATED }

    override fun withGeneratedPlaylists(enabled: Boolean): MutableLibrary =
        if (enabled) this else base.withGeneratedPlaylists(false)

    override suspend fun createPlaylist(name: String, songs: List<Song>) =
        wrap(base.createPlaylist(name, songs))

    override suspend fun renamePlaylist(playlist: Playlist, name: String): MutableLibrary {
        if (playlist.origin == Playlist.Origin.GENERATED) return this
        return wrap(base.renamePlaylist(playlist, name))
    }

    override suspend fun addToPlaylist(playlist: Playlist, songs: List<Song>): MutableLibrary {
        if (playlist.origin == Playlist.Origin.GENERATED) return this
        return wrap(base.addToPlaylist(playlist, songs))
    }

    override suspend fun rewritePlaylist(playlist: Playlist, songs: List<Song>): MutableLibrary {
        if (playlist.origin == Playlist.Origin.GENERATED) return this
        return wrap(base.rewritePlaylist(playlist, songs))
    }

    override suspend fun deletePlaylist(playlist: Playlist): MutableLibrary {
        if (playlist.origin == Playlist.Origin.GENERATED) return this
        return wrap(base.deletePlaylist(playlist))
    }

    private fun wrap(nextBase: MutableLibrary) =
        GeneratedPlaylistLibraryView(nextBase.withGeneratedPlaylists(false)) { generatedPlaylists }
}
