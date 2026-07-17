/*
 * Copyright (c) 2021 Auxio Project
 * HomeViewModel.kt is part of Auxio.
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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.oxycblt.auxio.headunit.ts18.FastStartDirectFolderBrowser
import org.oxycblt.auxio.home.tabs.Tab
import org.oxycblt.auxio.list.ListSettings
import org.oxycblt.auxio.list.adapter.UpdateInstructions
import org.oxycblt.auxio.list.sort.Sort
import org.oxycblt.auxio.music.MusicType
import org.oxycblt.auxio.playback.PlaySong
import org.oxycblt.auxio.playback.PlaybackSettings
import org.oxycblt.auxio.util.Event
import org.oxycblt.auxio.util.MutableEvent
import org.oxycblt.auxio.util.PerfTimer
import org.oxycblt.musikr.Album
import org.oxycblt.musikr.Artist
import org.oxycblt.musikr.Genre
import org.oxycblt.musikr.Playlist
import org.oxycblt.musikr.Song
import org.oxycblt.musikr.cache.MutableCache
import org.oxycblt.musikr.cache.StartupProjectionCache
import org.oxycblt.musikr.cache.StartupSongRow
import timber.log.Timber as L

data class FastStartHomeState(
    val firstSongs: List<StartupSongRow> = emptyList(),
    val recentlyAdded: List<StartupSongRow> = emptyList(),
    val usbRoots: List<FastStartDirectFolderBrowser.Entry> = emptyList(),
    val loading: Boolean = true,
)

/**
 * The ViewModel for managing the tab data and lists of the home view.
 *
 * @author Alexander Capehart (OxygenCobalt)
 */
@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val listSettings: ListSettings,
    private val playbackSettings: PlaybackSettings,
    private val cache: MutableCache,
    private val fastFolderBrowser: FastStartDirectFolderBrowser,
    homeGeneratorFactory: HomeGenerator.Factory,
) : ViewModel(), HomeGenerator.Invalidator {
    private val _fastStartState = MutableStateFlow(FastStartHomeState())
    val fastStartState: StateFlow<FastStartHomeState> = _fastStartState
    private var fastStartJob: Job? = null

    private val _songList = MutableStateFlow(listOf<Song>())
    /** A list of [Song]s, sorted by the preferred [Sort] and filtered by [decadeFilter]. */
    val songList: StateFlow<List<Song>>
        get() = _songList

    private val _songInstructions = MutableEvent<UpdateInstructions>()
    /** Instructions for how to update [songList] in the UI. */
    val songInstructions: Event<UpdateInstructions>
        get() = _songInstructions

    /** The current [Sort] used for [songList]. */
    val songSort: Sort
        get() = listSettings.songSort

    /**
     * Unfiltered sorted songs from the library. Used by the head-unit decade chips to derive the
     * full decade list regardless of any active [decadeFilter].
     */
    private var _allSongs: List<Song> = emptyList()
        set(value) {
            field = value
            // Invalidate the derived year list so allSongYears recomputes lazily.
            _cachedSongYears = null
            // Invalidate the decade queue cache.
            _cachedSongsByDecade = null
        }

    private var _cachedSongYears: List<Int>? = null
    private var _cachedSongsByDecade: Map<Int, List<Song>>? = null

    private val songsByDecade: Map<Int, List<Song>>
        get() =
            _cachedSongsByDecade
                ?: GeneratedPlaylistPolicy.songsByDecade(_allSongs).also {
                    _cachedSongsByDecade = it
                }

    /**
     * All valid release years in the unfiltered library, for computing available decade chips. This
     * is always derived from the full library, independent of [decadeFilter]. Cached because it is
     * read on every home header rebind and deriving it walks the entire library.
     */
    val allSongYears: List<Int>
        get() =
            _cachedSongYears
                ?: _allSongs.mapNotNull { it.album.dates?.min?.year }.also { _cachedSongYears = it }

    /** `true` if the library has at least one song, independent of any active [decadeFilter]. */
    val hasAnySongs: Boolean
        get() = _allSongs.isNotEmpty()

    /**
     * Generate a decade playlist from the full unfiltered library. This intentionally ignores
     * [songList] so a currently active tab filter cannot shrink the playback queue.
     */
    fun songsForDecade(decade: Int): List<Song> = songsByDecade[decade].orEmpty()

    /** Generate a newest-first playlist from the full unfiltered library. */
    fun recentlyAddedSongs(): List<Song> = GeneratedPlaylistPolicy.recentlyAddedSongs(_allSongs)

    private val _decadeFilter = MutableStateFlow<Int?>(null)
    /**
     * The currently active decade filter, expressed as the first year of the decade (e.g. `1990`
     * for the 1990s). `null` means no filter is active.
     */
    val decadeFilter: StateFlow<Int?> = _decadeFilter

    /** The [PlaySong] instructions to use when playing a [Song]. */
    val playWith
        get() = playbackSettings.playInListWith

    private val _albumList = MutableStateFlow(listOf<Album>())
    /** A list of [Album]s, sorted by the preferred [Sort], to be shown in the home view. */
    val albumList: StateFlow<List<Album>>
        get() = _albumList

    private val _albumInstructions = MutableEvent<UpdateInstructions>()
    /** Instructions for how to update [albumList] in the UI. */
    val albumInstructions: Event<UpdateInstructions>
        get() = _albumInstructions

    /** The current [Sort] used for [albumList]. */
    val albumSort: Sort
        get() = listSettings.albumSort

    private val _artistList = MutableStateFlow(listOf<Artist>())
    /**
     * A list of [Artist]s, sorted by the preferred [Sort], to be shown in the home view. Note that
     * if "Hide collaborators" is on, this list will not include collaborator [Artist]s.
     */
    val artistList: MutableStateFlow<List<Artist>>
        get() = _artistList

    private val _artistInstructions = MutableEvent<UpdateInstructions>()
    /** Instructions for how to update [artistList] in the UI. */
    val artistInstructions: Event<UpdateInstructions>
        get() = _artistInstructions

    /** The current [Sort] used for [artistList]. */
    val artistSort: Sort
        get() = listSettings.artistSort

    private val _genreList = MutableStateFlow(listOf<Genre>())
    /** A list of [Genre]s, sorted by the preferred [Sort], to be shown in the home view. */
    val genreList: StateFlow<List<Genre>>
        get() = _genreList

    private val _genreInstructions = MutableEvent<UpdateInstructions>()
    /** Instructions for how to update [genreList] in the UI. */
    val genreInstructions: Event<UpdateInstructions>
        get() = _genreInstructions

    /** The current [Sort] used for [genreList]. */
    val genreSort: Sort
        get() = listSettings.genreSort

    private val _playlistList = MutableStateFlow(listOf<Playlist>())
    /** A list of [Playlist]s, sorted by the preferred [Sort], to be shown in the home view. */
    val playlistList: StateFlow<List<Playlist>>
        get() = _playlistList

    private val _empty = MutableStateFlow(false)
    val empty: StateFlow<Boolean>
        get() = _empty

    private val _playlistInstructions = MutableEvent<UpdateInstructions>()
    /** Instructions for how to update [genreList] in the UI. */
    val playlistInstructions: Event<UpdateInstructions>
        get() = _playlistInstructions

    /** The current [Sort] used for [genreList]. */
    val playlistSort: Sort
        get() = listSettings.playlistSort

    private val homeGenerator = homeGeneratorFactory.create(this)

    // Thread-safety: invalidateMusic is only called from dispatchLibraryChange which runs on
    // Dispatchers.Main, and viewModelScope.launch also dispatches on Main, so all map access
    // is confined to the main thread.
    /** Per-type invalidation jobs for cancellation of stale updates. */
    private val invalidationJobs = mutableMapOf<MusicType, Job>()

    /**
     * A list of [MusicType] corresponding to the current [Tab] configuration, excluding invisible
     * [Tab]s.
     */
    var currentTabTypes = homeGenerator.tabs()
        private set

    private val _currentTabType = MutableStateFlow(currentTabTypes[0])
    private val categorySubscriptions = CategorySubscriptionGate(currentTabTypes[0])
    /** The [MusicType] of the currently shown [Tab]. */
    val currentTabType: StateFlow<MusicType> = _currentTabType

    private val _shouldRecreate = MutableEvent<Unit>()
    /**
     * A marker to re-create all library tabs, usually initiated by a settings change. When this
     * flag is true, all tabs (and their respective ViewPager2 fragments) will be re-created from
     * scratch.
     */
    val recreateTabs: Event<Unit>
        get() = _shouldRecreate

    private val _isFastScrolling = MutableStateFlow(false)
    /** A marker for whether the user is fast-scrolling in the home view or not. */
    val isFastScrolling: StateFlow<Boolean> = _isFastScrolling

    private val _showOuter = MutableEvent<Outer>()
    val showOuter: Event<Outer>
        get() = _showOuter

    private val _chooseMusicLocations = MutableEvent<Unit>()
    val chooseMusicLocations: Event<Unit>
        get() = _chooseMusicLocations

    private var automaticSourceDialogStarted = false

    init {
        homeGenerator.attach()
        refreshFastStart()
    }

    /** Refresh the bounded pre-library rows after startup projections become available. */
    fun refreshFastStart(force: Boolean = false) {
        if (fastStartJob?.isActive == true) return
        if (!force && !_fastStartState.value.loading) return
        fastStartJob =
            viewModelScope.launch {
                try {
                    val projection = cache as? StartupProjectionCache
                    val state =
                        withContext(Dispatchers.IO) {
                            FastStartHomeState(
                                firstSongs =
                                    projection?.firstSongs(limit = 8).orEmpty().filter {
                                        it.available
                                    },
                                recentlyAdded =
                                    projection?.recentlyAdded(limit = 6).orEmpty().filter {
                                        it.available
                                    },
                                usbRoots = fastFolderBrowser.usbRoots(limit = 2).entries,
                                loading = false,
                            )
                        }
                    _fastStartState.value = state
                    if (
                        state.firstSongs.isNotEmpty() ||
                            state.recentlyAdded.isNotEmpty() ||
                            state.usbRoots.isNotEmpty()
                    ) {
                        PerfTimer.point("startup.fast_home_first_rows")
                    }
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    L.w(e, "Unable to load bounded Fast Start rows")
                    _fastStartState.value = FastStartHomeState(loading = false)
                } finally {
                    fastStartJob = null
                }
            }
    }

    override fun onCleared() {
        super.onCleared()
        fastStartJob?.cancel()
        homeGenerator.release()
    }

    override fun invalidateEmpty() {
        _empty.value = homeGenerator.empty()
    }

    override fun invalidateMusic(type: MusicType, instructions: UpdateInstructions) {
        // Expensive rich-category work is subscriber-driven. Inactive invalidations conflate into
        // one refresh when their tab becomes visible.
        if (!categorySubscriptions.invalidate(type)) return
        // Cancel any previous in-flight invalidation for this type to avoid stale
        // older jobs overwriting newer state (race-safe latest-wins semantics).
        invalidationJobs[type]?.cancel()
        val job =
            viewModelScope.launch {
                when (type) {
                    MusicType.SONGS -> {
                        _allSongs = homeGenerator.songs()
                        _songInstructions.put(instructions)
                        _songList.value = _allSongs.filteredByDecade(_decadeFilter.value)
                    }
                    MusicType.ALBUMS -> {
                        _albumInstructions.put(instructions)
                        _albumList.value = homeGenerator.albums()
                    }
                    MusicType.ARTISTS -> {
                        _artistInstructions.put(instructions)
                        _artistList.value = homeGenerator.artists()
                    }
                    MusicType.GENRES -> {
                        _genreInstructions.put(instructions)
                        _genreList.value = homeGenerator.genres()
                    }
                    MusicType.PLAYLISTS -> {
                        _playlistInstructions.put(instructions)
                        _playlistList.value = homeGenerator.playlists()
                    }
                }
            }
        invalidationJobs[type] = job
        job.invokeOnCompletion { if (invalidationJobs[type] === job) invalidationJobs.remove(type) }
    }

    override fun invalidateTabs() {
        currentTabTypes = homeGenerator.tabs()
        _shouldRecreate.put(Unit)
    }

    /**
     * Apply a new [Sort] to [songList].
     *
     * @param sort The [Sort] to apply.
     */
    fun applySongSort(sort: Sort) {
        listSettings.songSort = sort
    }

    /**
     * Apply or clear the decade filter on [songList].
     *
     * @param decade The first year of the decade to filter to (e.g. `1990`), or `null` to show all
     *   songs.
     */
    fun applyDecadeFilter(decade: Int?) {
        _decadeFilter.value = decade
        // Replace(0) replaces the entire list in one visual pass (starting from position 0),
        // which is smoother than Diff when the whole list content changes due to a new filter.
        _songInstructions.put(UpdateInstructions.Replace(0))
        _songList.value = _allSongs.filteredByDecade(decade)
    }

    private fun List<Song>.filteredByDecade(decade: Int?) =
        GeneratedPlaylistPolicy.filterSongsForDecadePreservingOrder(this, decade)

    /**
     * Apply a new [Sort] to [albumList].
     *
     * @param sort The [Sort] to apply.
     */
    fun applyAlbumSort(sort: Sort) {
        listSettings.albumSort = sort
    }

    /**
     * Apply a new [Sort] to [artistList].
     *
     * @param sort The [Sort] to apply.
     */
    fun applyArtistSort(sort: Sort) {
        listSettings.artistSort = sort
    }

    /**
     * Apply a new [Sort] to [genreList].
     *
     * @param sort The [Sort] to apply.
     */
    fun applyGenreSort(sort: Sort) {
        listSettings.genreSort = sort
    }

    /**
     * Apply a new [Sort] to [playlistList].
     *
     * @param sort The [Sort] to apply.
     */
    fun applyPlaylistSort(sort: Sort) {
        listSettings.playlistSort = sort
    }

    /**
     * Update [currentTabType] to reflect a new ViewPager2 position
     *
     * @param pagerPos The new position of the ViewPager2 instance.
     */
    fun synchronizeTabPosition(pagerPos: Int) {
        val next = currentTabTypes[pagerPos]
        L.d("Updating current tab to $next")
        _currentTabType.value = next
        if (categorySubscriptions.activate(next)) {
            invalidateMusic(next, UpdateInstructions.Replace(0))
        }
    }

    /**
     * Update whether the user is fast scrolling or not in the home view.
     *
     * @param isFastScrolling true if the user is currently fast scrolling, false otherwise.
     */
    fun setFastScrolling(isFastScrolling: Boolean) {
        L.d("Updating fast scrolling state: $isFastScrolling")
        _isFastScrolling.value = isFastScrolling
    }

    fun startChooseMusicLocations() {
        _chooseMusicLocations.put(Unit)
    }

    fun markAutomaticSourceDialogStarted(): Boolean {
        if (automaticSourceDialogStarted) return false
        automaticSourceDialogStarted = true
        return true
    }

    fun showSettings() {
        _showOuter.put(Outer.Settings)
    }

    fun showAbout() {
        _showOuter.put(Outer.About)
    }
}

sealed interface Outer {
    data object Settings : Outer

    data object About : Outer
}
