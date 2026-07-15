/*
 * Copyright (c) 2026 Auxio Project
 * StartupProjections.kt is part of Auxio.
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
