/*
 * Copyright (c) 2026 Auxio Project
 * GeneratedPlaylistPolicy.kt is part of Auxio.
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

import org.oxycblt.auxio.list.sort.Sort
import org.oxycblt.musikr.Song

/** Pure generated-playlist helpers for home/head-unit quick-access pills. */
object GeneratedPlaylistPolicy {
    enum class DecadeChipAction {
        PLAY_DECADE,
        CLEAR_FILTER,
    }

    /**
     * Shared date-descending sort for generated decade queues and matching Songs-tab context.
     *
     * Generated decade queues and the visible Songs tab both rely on [Sort.songs] so they share
     * the same stable multi-pass ordering and tie-breaking pipeline.
     */
    val decadePlaybackSort = Sort(Sort.Mode.ByDate, Sort.Direction.DESCENDING)

    /**
     * Shared newest-first sort for generated Recently Added queues and matching Songs-tab context.
     *
     * As with [decadePlaybackSort], callers rely on [Sort.songs] so generated queues and the
     * visible Songs tab stay aligned for equal date-added values.
     */
    val recentlyAddedSort = Sort(Sort.Mode.ByDateAdded, Sort.Direction.DESCENDING)

    /** The visual decade filter state to use when opening the full Recently Added queue. */
    val recentlyAddedDecadeFilter: Int? = null

    fun decadeChipAction(activeDecade: Int?, tappedDecade: Int): DecadeChipAction =
        if (activeDecade == tappedDecade) {
            DecadeChipAction.CLEAR_FILTER
        } else {
            DecadeChipAction.PLAY_DECADE
        }

    /** Return true when [song]'s album release-year metadata falls inside [decade]. */
    fun isInDecade(song: Song, decade: Int): Boolean {
        val year = song.album.dates?.min?.year ?: return false
        return year >= decade && year < decade + 10
    }

    /**
     * Return all songs whose album release-year metadata falls inside [decade], sorted newest-first.
     *
     * This must match the visible Songs tab ordering for [decadePlaybackSort], so it delegates to
     * [Sort.songs] rather than introducing local secondary keys.
     */
    fun songsForDecade(songs: Collection<Song>, decade: Int): List<Song> =
        decadePlaybackSort.songs(songs.filter { song -> isInDecade(song, decade) })

    /**
     * Filter songs by [decade] while preserving the caller's existing order. This is for visible
     * browsing lists, not generated playback queues.
     */
    fun filterSongsForDecadePreservingOrder(songs: List<Song>, decade: Int?): List<Song> =
        if (decade == null) songs else songs.filter { song -> isInDecade(song, decade) }

    /**
     * Return all songs sorted newest-first by the same date-added semantics as the Songs tab.
     *
     * This delegates to [Sort.songs] so generated Recently Added playback queues share the UI
     * ordering and tie-breaking behaviour.
     */
    fun recentlyAddedSongs(songs: Collection<Song>): List<Song> = recentlyAddedSort.songs(songs)
}
