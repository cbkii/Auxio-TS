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

import java.nio.ByteBuffer
import java.util.UUID
import org.oxycblt.musikr.Album
import org.oxycblt.musikr.Artist
import org.oxycblt.musikr.Genre
import org.oxycblt.musikr.Music
import org.oxycblt.musikr.MutableLibrary
import org.oxycblt.musikr.Song
import org.oxycblt.musikr.graph.AlbumVertex
import org.oxycblt.musikr.graph.ArtistVertex
import org.oxycblt.musikr.graph.GenreVertex
import org.oxycblt.musikr.graph.MusicGraph
import org.oxycblt.musikr.graph.PlaylistVertex
import org.oxycblt.musikr.graph.SongVertex
import org.oxycblt.musikr.graph.Vertex
import org.oxycblt.musikr.playlist.PlaylistHandle
import org.oxycblt.musikr.playlist.db.StoredPlaylists
import org.oxycblt.musikr.playlist.interpret.PlaylistInterpreter
import org.oxycblt.musikr.playlist.interpret.PrePlaylistInfo
import org.oxycblt.musikr.tag.Name

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

private class LibraryFactoryImpl() : LibraryFactory {
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

        // Generate Recently Added playlist
        val recentlyAddedSongs =
            songs
                .sortedWith(
                    compareByDescending<Song> { it.addedMs }
                        .thenBy { it.album.dates?.min?.year ?: 0 }
                        .thenBy {
                            (it.album.name as? Name.Known)?.sort
                                ?: (it.album.name as? Name.Known)?.raw
                                ?: ""
                        }
                        .thenBy { it.disc?.number ?: 0 }
                        .thenBy { it.track ?: 0 }
                        .thenBy {
                            (it.name as? Name.Known)?.sort ?: (it.name as? Name.Known)?.raw ?: ""
                        }
                )
                .take(500)

        if (recentlyAddedSongs.isNotEmpty()) {
            playlists.add(
                PlaylistImpl(
                    GeneratedPlaylistCore("recently-added", "Recently added", recentlyAddedSongs)
                )
            )
        }

        // Generate Decade playlists
        val decadeMap = mutableMapOf<Int, MutableList<Song>>()
        for (song in songs) {
            val year = song.album.dates?.min?.year ?: continue
            val decade = (year / 10) * 10
            decadeMap.getOrPut(decade) { mutableListOf() }.add(song)
        }

        for ((decade, decadeSongs) in decadeMap) {
            val sortedDecadeSongs =
                decadeSongs.sortedWith(
                    compareByDescending<Song> { it.album.dates?.min?.year ?: 0 }
                        .thenByDescending { it.addedMs }
                        .thenBy {
                            (it.album.name as? Name.Known)?.sort
                                ?: (it.album.name as? Name.Known)?.raw
                                ?: ""
                        }
                        .thenBy { it.disc?.number ?: 0 }
                        .thenBy { it.track ?: 0 }
                        .thenBy {
                            (it.name as? Name.Known)?.sort ?: (it.name as? Name.Known)?.raw ?: ""
                        }
                )
            playlists.add(
                PlaylistImpl(
                    GeneratedPlaylistCore("decade:$decade", "${decade}s", sortedDecadeSongs)
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
            org.oxycblt.musikr.Playlist.Origin
                .USER // Imported playlists might also go through this path, will refine in next
        // step if needed
        override val prePlaylist = vertex.prePlaylist

        override val songs: List<Song> =
            vertex.songVertices.mapNotNull { vertex -> vertex?.let { tag(it) } }
    }

    private class GeneratedPlaylistHandle(override val uid: Music.UID) : PlaylistHandle {
        override suspend fun rename(name: String) {}

        override suspend fun add(songs: List<Song>) {}

        override suspend fun rewrite(songs: List<Song>) {}

        override suspend fun delete() {}
    }

    private class GeneratedPrePlaylistInfo(
        override val name: Name.Known,
        override val rawName: String?,
        override val handle: PlaylistHandle,
    ) : PrePlaylistInfo

    private class GeneratedPlaylistCore(
        val id: String,
        val displayName: String,
        override val songs: List<Song>,
    ) : PlaylistCore {
        override val origin = org.oxycblt.musikr.Playlist.Origin.GENERATED
        override val prePlaylist: PrePlaylistInfo

        init {
            val digest =
                java.security.MessageDigest.getInstance("MD5").digest("generated:$id".toByteArray())
            val buffer = ByteBuffer.wrap(digest)
            val uuid = UUID(buffer.long, buffer.long)

            val uid =
                Music.UID.auxio(Music.UID.Item.PLAYLIST) { update(uuid.toString().toByteArray()) }
            val handle = GeneratedPlaylistHandle(uid)
            val nameTokens =
                listOf(
                    org.oxycblt.musikr.tag.Token(
                        java.text.Collator.getInstance().getCollationKey(displayName),
                        org.oxycblt.musikr.tag.Token.Type.LEXICOGRAPHIC,
                    )
                )

            val name =
                object : Name.Known() {
                    override val raw: String = displayName
                    override val sort: String? = null
                    override val tokens: List<org.oxycblt.musikr.tag.Token> = nameTokens

                    override fun hashCode() = displayName.hashCode()

                    override fun equals(other: Any?) = other is Name.Known && raw == other.raw
                }

            prePlaylist = GeneratedPrePlaylistInfo(name, displayName, handle)
        }
    }

    private companion object {
        private inline fun <reified T : Music> tag(vertex: Vertex): T {
            val tag = vertex.tag
            check(tag is T) { "Dead Vertex Detected: $vertex" }
            return tag
        }
    }
}
