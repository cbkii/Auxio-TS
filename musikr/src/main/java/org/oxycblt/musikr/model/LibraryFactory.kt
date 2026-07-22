/*
 * Copyright (c) 2024 Auxio Project
 * LibraryFactory.kt is part of Auxio.
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
import org.oxycblt.musikr.graph.AlbumVertex
import org.oxycblt.musikr.graph.ArtistVertex
import org.oxycblt.musikr.graph.GenreVertex
import org.oxycblt.musikr.graph.MusicGraph
import org.oxycblt.musikr.graph.PlaylistVertex
import org.oxycblt.musikr.graph.SongVertex
import org.oxycblt.musikr.graph.Vertex
import org.oxycblt.musikr.playlist.PlaylistHandle
import org.oxycblt.musikr.playlist.db.StoredPlaylistHandle
import org.oxycblt.musikr.playlist.db.StoredPlaylists
import org.oxycblt.musikr.playlist.interpret.PlaylistInterpreter
import org.oxycblt.musikr.playlist.interpret.PrePlaylistInfo
import org.oxycblt.musikr.tag.Name
import org.oxycblt.musikr.tag.Token

internal interface LibraryFactory {
    fun create(
        graph: MusicGraph,
        storedPlaylists: StoredPlaylists,
        playlistInterpreter: PlaylistInterpreter,
    ): MutableLibrary

    companion object {
        fun new(): LibraryFactory = LibraryFactoryImpl()
    }
}

private class LibraryFactoryImpl : LibraryFactory {
    override fun create(
        graph: MusicGraph,
        storedPlaylists: StoredPlaylists,
        playlistInterpreter: PlaylistInterpreter,
    ): MutableLibrary {
        val songs =
            graph.songVertex.mapTo(mutableSetOf()) { vertex ->
                SongImpl(SongVertexCore(vertex)).also { vertex.tag = it }
            }
        val albums =
            graph.albumVertex.mapTo(mutableSetOf()) { vertex ->
                AlbumImpl(AlbumVertexCore(vertex)).also { vertex.tag = it }
            }
        val artists =
            graph.artistVertex.mapTo(mutableSetOf()) { vertex ->
                ArtistImpl(ArtistVertexCore(vertex)).also { vertex.tag = it }
            }
        val genres =
            graph.genreVertex.mapTo(mutableSetOf()) { vertex ->
                GenreImpl(GenreVertexCore(vertex)).also { vertex.tag = it }
            }
        val playlists =
            graph.playlistVertex.mapTo(mutableSetOf()) { vertex ->
                PlaylistImpl(PlaylistVertexCore(vertex))
            }

        // Generated playlists are deterministic projections of this one committed rich library.
        // They are rebuilt after both cached and scanned graph construction, so there is no second
        // playlist database or partially committed generated state.
        val generatedDefinitions =
            GeneratedPlaylistCompiler.compile(
                songs.map { song ->
                    GeneratedPlaylistCompiler.Entry(
                        value = song,
                        stableKey = song.uid.toString(),
                        addedMs = song.addedMs,
                        year = song.album.dates?.min?.year,
                        albumSort = song.album.name.sortKey(),
                        disc = song.disc?.number ?: 0,
                        track = song.track ?: 0,
                        titleSort = song.name.sort ?: song.name.raw,
                    )
                }
            )
        generatedDefinitions.forEach { definition ->
            playlists +=
                PlaylistImpl(
                    GeneratedPlaylistCore(
                        id = definition.id,
                        displayName = definition.name,
                        songs = definition.values,
                    )
                )
        }

        return LibraryImpl(
            songs,
            albums,
            artists,
            genres,
            playlists,
            storedPlaylists,
            playlistInterpreter,
        )
    }

    private class SongVertexCore(private val vertex: SongVertex) : SongCore {
        override val preSong = vertex.preSong

        override fun resolveAlbum(): Album = tag(vertex.albumVertex)

        override fun resolveArtists(): List<Artist> = vertex.artistVertices.map { tag(it) }

        override fun resolveGenres(): List<Genre> = vertex.genreVertices.map { tag(it) }
    }

    private class AlbumVertexCore(private val vertex: AlbumVertex) : AlbumCore {
        override val preAlbum = vertex.preAlbum

        override val songs: Set<Song> = vertex.songVertices.mapTo(mutableSetOf()) { tag(it) }

        override fun resolveArtists(): List<Artist> = vertex.artistVertices.map { tag(it) }
    }

    private class ArtistVertexCore(private val vertex: ArtistVertex) : ArtistCore {
        override val preArtist = vertex.preArtist

        override val songs: Set<Song> = vertex.songVertices.mapTo(mutableSetOf()) { tag(it) }

        override val albums: Set<Album> = vertex.albumVertices.mapTo(mutableSetOf()) { tag(it) }

        override fun resolveGenres(): Set<Genre> =
            vertex.genreVertices.mapTo(mutableSetOf()) { tag(it) }
    }

    private class GenreVertexCore(vertex: GenreVertex) : GenreCore {
        override val preGenre = vertex.preGenre

        override val songs: Set<Song> = vertex.songVertices.mapTo(mutableSetOf()) { tag(it) }

        override val artists: Set<Artist> = vertex.artistVertices.mapTo(mutableSetOf()) { tag(it) }
    }

    private class PlaylistVertexCore(vertex: PlaylistVertex) : PlaylistCore {
        override val origin =
            if (vertex.prePlaylist.handle is StoredPlaylistHandle) {
                Playlist.Origin.USER
            } else {
                Playlist.Origin.IMPORTED
            }
        override val prePlaylist = vertex.prePlaylist

        override val songs: List<Song> =
            vertex.songVertices.mapNotNull { songVertex -> songVertex?.let { tag(it) } }
    }

    /** Defensive no-op handle; every public mutation path also rejects GENERATED origin. */
    private class GeneratedPlaylistHandle(override val uid: Music.UID) : PlaylistHandle {
        override suspend fun rename(name: String) = Unit

        override suspend fun add(songs: List<Song>) = Unit

        override suspend fun rewrite(songs: List<Song>) = Unit

        override suspend fun delete() = Unit
    }

    private class GeneratedPrePlaylistInfo(
        override val name: Name.Known,
        override val rawName: String,
        override val handle: PlaylistHandle,
    ) : PrePlaylistInfo

    private class GeneratedPlaylistCore(
        id: String,
        displayName: String,
        override val songs: List<Song>,
    ) : PlaylistCore {
        override val origin = Playlist.Origin.GENERATED
        override val prePlaylist: PrePlaylistInfo

        init {
            val uid = GeneratedPlaylistCompiler.stableUid(id)
            val tokens =
                listOf(
                    Token(
                        java.text.Collator.getInstance().getCollationKey(displayName),
                        Token.Type.LEXICOGRAPHIC,
                    )
                )
            val name =
                object : Name.Known() {
                    override val raw: String = displayName
                    override val sort: String = displayName
                    override val tokens: List<Token> = tokens

                    override fun hashCode() = displayName.hashCode()

                    override fun equals(other: Any?) = other is Name.Known && raw == other.raw
                }
            prePlaylist = GeneratedPrePlaylistInfo(name, displayName, GeneratedPlaylistHandle(uid))
        }
    }

    private companion object {
        fun Name.sortKey(): String = (this as? Name.Known)?.let { it.sort ?: it.raw }.orEmpty()

        private inline fun <reified T : Music> tag(vertex: Vertex): T {
            val tag = vertex.tag
            check(tag is T) { "Dead Vertex Detected: $vertex" }
            return tag
        }
    }
}
