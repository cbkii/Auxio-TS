/*
 * Copyright (c) 2026 Auxio Project
 * StartupProjections.kt is part of Auxio.
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

package org.oxycblt.musikr.cache

/** Lightweight cached row suitable for launch UI, quick search, and bounded MediaBrowser output. */
data class StartupSongRow(
    val stableId: String,
    val uri: String,
    val directPath: String?,
    val title: String,
    val primaryArtist: String?,
    val album: String?,
    val durationMs: Long?,
    val artworkRef: String?,
    val available: Boolean,
)

/** Lightweight summary row for bounded startup category sections. */
data class StartupSummaryRow(val stableId: String, val title: String, val available: Boolean = true)

/** Bounded startup/search projection API backed by the normalized cache database when available. */
interface StartupProjectionCache {
    suspend fun firstSongs(limit: Int = 20, offset: Int = 0): List<StartupSongRow>

    suspend fun recentlyAdded(limit: Int = 20): List<StartupSongRow>

    suspend fun albums(limit: Int = 10, offset: Int = 0): List<StartupSummaryRow>

    suspend fun artists(limit: Int = 10, offset: Int = 0): List<StartupSummaryRow>

    suspend fun quickSearchSongs(query: String, limit: Int = 10): List<StartupSongRow>
}
