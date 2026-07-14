/*
 * Copyright (c) 2024 Auxio Project
 * HomeGenerator.kt is part of Auxio.
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

package org.oxycblt.auxio.home

import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.oxycblt.auxio.home.tabs.Tab
import org.oxycblt.auxio.list.ListSettings
import org.oxycblt.auxio.list.adapter.UpdateInstructions
import org.oxycblt.auxio.list.sort.Sort
import org.oxycblt.auxio.music.MusicRepository
import org.oxycblt.auxio.music.MusicType
import org.oxycblt.musikr.Album
import org.oxycblt.musikr.Artist
import org.oxycblt.musikr.Genre
import org.oxycblt.musikr.Library
import org.oxycblt.musikr.Playlist
import org.oxycblt.musikr.Song
import timber.log.Timber as L

interface HomeGenerator {
    fun attach()

    fun release()

    fun empty(): Boolean

    suspend fun songs(): List<Song>

    suspend fun albums(): List<Album>

    suspend fun artists(): List<Artist>

    suspend fun genres(): List<Genre>

    suspend fun playlists(): List<Playlist>

    fun tabs(): List<MusicType>

    interface Invalidator {
        fun invalidateEmpty() {}

        fun invalidateMusic(type: MusicType, instructions: UpdateInstructions)

        fun invalidateTabs()
    }

    interface Factory {
        fun create(invalidator: Invalidator): HomeGenerator
    }
}

class HomeGeneratorFactoryImpl
@Inject
constructor(
    private val homeSettings: HomeSettings,
    private val listSettings: ListSettings,
    private val musicRepository: MusicRepository,
) : HomeGenerator.Factory {
    override fun create(invalidator: HomeGenerator.Invalidator): HomeGenerator =
        HomeGeneratorImpl(invalidator, homeSettings, listSettings, musicRepository)
}

private class HomeGeneratorImpl(
    private val invalidator: HomeGenerator.Invalidator,
    private val homeSettings: HomeSettings,
    private val listSettings: ListSettings,
    private val musicRepository: MusicRepository,
) : HomeGenerator, HomeSettings.Listener, ListSettings.Listener, MusicRepository.UpdateListener {
    override fun attach() {
        homeSettings.registerListener(this)
        listSettings.registerListener(this)
        musicRepository.addUpdateListener(this)
    }

    override fun onTabsChanged() {
        invalidator.invalidateTabs()
    }

    override fun onHideCollaboratorsChanged() {
        // Changes in the hide collaborator setting will change the artist contents
        // of the library, consider it a library update.
        L.d("Collaborator setting changed, forwarding update")
        invalidator.invalidateMusic(MusicType.ARTISTS, UpdateInstructions.Diff)
    }

    override fun onSongSortChanged() {
        super.onSongSortChanged()
        invalidator.invalidateMusic(MusicType.SONGS, UpdateInstructions.Replace(0))
    }

    override fun onAlbumSortChanged() {
        super.onAlbumSortChanged()
        invalidator.invalidateMusic(MusicType.ALBUMS, UpdateInstructions.Replace(0))
    }

    override fun onArtistSortChanged() {
        super.onArtistSortChanged()
        invalidator.invalidateMusic(MusicType.ARTISTS, UpdateInstructions.Replace(0))
    }

    override fun onGenreSortChanged() {
        super.onGenreSortChanged()
        invalidator.invalidateMusic(MusicType.GENRES, UpdateInstructions.Replace(0))
    }

    override fun onPlaylistSortChanged() {
        super.onPlaylistSortChanged()
        invalidator.invalidateMusic(MusicType.PLAYLISTS, UpdateInstructions.Replace(0))
    }

    override fun onMusicChanges(changes: MusicRepository.Changes) {
        invalidator.invalidateEmpty()

        val library = musicRepository.library
        if (changes.deviceLibrary && library != null) {
            L.d("Refreshing library")
            // Get the each list of items in the library to use as our list data.
            // Applying the preferred sorting to them.
            invalidator.invalidateMusic(MusicType.SONGS, UpdateInstructions.Diff)
            invalidator.invalidateMusic(MusicType.ALBUMS, UpdateInstructions.Diff)
            invalidator.invalidateMusic(MusicType.ARTISTS, UpdateInstructions.Diff)
            invalidator.invalidateMusic(MusicType.GENRES, UpdateInstructions.Diff)
        }

        if (changes.userLibrary && library != null) {
            L.d("Refreshing playlists")
            invalidator.invalidateMusic(MusicType.PLAYLISTS, UpdateInstructions.Diff)
        }
    }

    override fun release() {
        musicRepository.removeUpdateListener(this)
        listSettings.unregisterListener(this)
        homeSettings.unregisterListener(this)
    }

    override fun empty() = musicRepository.library?.empty() ?: true

    /**
     * A sorted list snapshot tied to the exact library instance, [Sort], and any extra setting that
     * influenced it. Re-sorting a large library is expensive on weak head-unit CPUs, and both the
     * home UI and MediaBrowser browse requests hit these methods repeatedly, so cache the result
     * and recompute only when an input actually changes.
     */
    private data class CachedList<T>(
        val library: Library,
        val generation: Long,
        val sort: Sort,
        val extra: Any?,
        val items: List<T>,
    )

    @Volatile private var cachedSongs: CachedList<Song>? = null
    @Volatile private var cachedAlbums: CachedList<Album>? = null
    @Volatile private var cachedArtists: CachedList<Artist>? = null
    @Volatile private var cachedGenres: CachedList<Genre>? = null
    @Volatile private var cachedPlaylists: CachedList<Playlist>? = null

    private inline fun <T> cachedOrCompute(
        cached: CachedList<T>?,
        sort: Sort,
        extra: Any?,
        label: String,
        compute: (Library) -> List<T>,
        store: (CachedList<T>) -> Unit,
    ): List<T> {
        val library = musicRepository.library ?: return emptyList()
        val hit = cached
        if (
            hit != null &&
                hit.generation == musicRepository.generation &&
                hit.sort == sort &&
                hit.extra == extra
        ) {
            return hit.items
        }
        val start = System.currentTimeMillis()
        val result = compute(library)
        L.d(
            "HomeGenerator.$label() sorted ${result.size} items in ${System.currentTimeMillis() - start}ms"
        )
        store(CachedList(library, musicRepository.generation, sort, extra, result))
        return result
    }

    override suspend fun songs() =
        withContext(Dispatchers.Default) {
            val songSort = listSettings.songSort
            cachedOrCompute(
                cachedSongs,
                songSort,
                null,
                "songs",
                { songSort.songs(it.songs) },
                { cachedSongs = it },
            )
        }

    override suspend fun albums() =
        withContext(Dispatchers.Default) {
            val albumSort = listSettings.albumSort
            cachedOrCompute(
                cachedAlbums,
                albumSort,
                null,
                "albums",
                { albumSort.albums(it.albums) },
                { cachedAlbums = it },
            )
        }

    override suspend fun artists() =
        withContext(Dispatchers.Default) {
            val artistSort = listSettings.artistSort
            val hideCollaborators = homeSettings.shouldHideCollaborators
            cachedOrCompute(
                cachedArtists,
                artistSort,
                hideCollaborators,
                "artists",
                { library ->
                    val sorted = artistSort.artists(library.artists)
                    if (hideCollaborators) {
                        sorted.filter { it.explicitAlbums.isNotEmpty() }
                    } else {
                        sorted
                    }
                },
                { cachedArtists = it },
            )
        }

    override suspend fun genres() =
        withContext(Dispatchers.Default) {
            val genreSort = listSettings.genreSort
            cachedOrCompute(
                cachedGenres,
                genreSort,
                null,
                "genres",
                { genreSort.genres(it.genres) },
                { cachedGenres = it },
            )
        }

    override suspend fun playlists() =
        withContext(Dispatchers.Default) {
            val playlistSort = listSettings.playlistSort
            cachedOrCompute(
                cachedPlaylists,
                playlistSort,
                null,
                "playlists",
                { playlistSort.playlists(it.playlists) },
                { cachedPlaylists = it },
            )
        }

    override fun tabs() = homeSettings.homeTabs.filterIsInstance<Tab.Visible>().map { it.type }
}
