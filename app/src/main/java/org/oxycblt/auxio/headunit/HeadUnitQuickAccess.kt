/*
 * Copyright (c) 2026 Auxio Project
 * HeadUnitQuickAccess.kt is part of Auxio.
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

package org.oxycblt.auxio.headunit

/** Well-known name used to identify the persisted Favourites playlist. */
const val FAVOURITES_PLAYLIST_NAME = "Favourites"

enum class QuickPickAction {
    NOW_PLAYING,
    SHUFFLE_ALL,
    GENRES,
    ARTISTS,
    ALBUMS,
    PLAYLISTS,
    QUEUE,
    RECENTLY_ADDED,
    DECADES,
    FOLDERS,
    FAVOURITES,
    HEAD_UNIT_SETTINGS,
}

internal data class QuickPickItem(val action: QuickPickAction, val enabled: Boolean)

data class MetadataChipState(
    val genres: Boolean,
    val decades: Boolean,
    val folders: Boolean,
    val recentlyAdded: Boolean,
    val favourites: Boolean,
)

object HeadUnitQuickAccess {
    /**
     * Return the dashboard quick-pick items for tests that only need action/enabled state.
     *
     * The returned list mirrors [HeadUnitDashboardPolicy.entries] and is intentionally not a fixed
     * action set: unavailable content-specific actions, such as [QuickPickAction.FAVOURITES], may be
     * omitted entirely instead of being returned disabled.
     */
    internal fun quickPicks(
        hasLibraryContent: Boolean,
        hasFavourites: Boolean,
        isIndexing: Boolean,
    ): List<QuickPickItem> =
        HeadUnitDashboardPolicy.entries(
                HeadUnitDashboardState(
                    hasLibraryContent = hasLibraryContent,
                    hasFavourites = hasFavourites,
                    isIndexing = isIndexing,
                )
            )
            .map { entry -> QuickPickItem(entry.action, entry.enabled) }

    fun metadataChipState(
        genreCount: Int,
        decadeCount: Int,
        hasRecent: Boolean,
        hasFolders: Boolean,
        hasFavourites: Boolean,
    ): MetadataChipState =
        MetadataChipState(
            genres = genreCount > 0,
            decades = decadeCount > 0,
            folders = hasFolders,
            recentlyAdded = hasRecent,
            favourites = hasFavourites,
        )

    fun deriveDecades(years: List<Int>): List<Int> =
        years.filter { it in 1900..2099 }.map { (it / 10) * 10 }.distinct().sorted()
}
