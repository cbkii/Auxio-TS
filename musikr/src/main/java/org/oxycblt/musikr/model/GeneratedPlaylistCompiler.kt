/*
 * Copyright (c) 2026 Auxio Project
 * GeneratedPlaylistCompiler.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.oxycblt.musikr.model

import java.nio.charset.StandardCharsets
import org.oxycblt.musikr.Music

/** Deterministic, side-effect-free definitions for read-only generated playlists. */
internal object GeneratedPlaylistCompiler {
    const val RECENTLY_ADDED_ID = "recently-added"
    const val RECENTLY_ADDED_NAME = "Recently added"
    const val RECENTLY_ADDED_LIMIT = 500

    data class Entry<T>(
        val value: T,
        val stableKey: String,
        val addedMs: Long,
        val year: Int?,
        val albumSort: String,
        val disc: Int,
        val track: Int,
        val titleSort: String,
    )

    data class Definition<T>(val id: String, val name: String, val values: List<T>)

    fun <T> compile(entries: Collection<Entry<T>>): List<Definition<T>> {
        if (entries.isEmpty()) return emptyList()
        val definitions = mutableListOf<Definition<T>>()

        val recent =
            entries
                .sortedWith(
                    compareByDescending<Entry<T>> { it.addedMs }
                        .thenByDescending { it.year ?: Int.MIN_VALUE }
                        .thenBy { it.albumSort }
                        .thenBy { it.disc }
                        .thenBy { it.track }
                        .thenBy { it.titleSort }
                        .thenBy { it.stableKey }
                )
                .take(RECENTLY_ADDED_LIMIT)
                .map { it.value }
        if (recent.isNotEmpty()) {
            definitions += Definition(RECENTLY_ADDED_ID, RECENTLY_ADDED_NAME, recent)
        }

        entries
            .filter { it.year != null }
            .groupBy { requireNotNull(it.year) / 10 * 10 }
            .toSortedMap(compareByDescending { it })
            .forEach { (decade, decadeEntries) ->
                val songs =
                    decadeEntries
                        .sortedWith(
                            compareByDescending<Entry<T>> { requireNotNull(it.year) }
                                .thenByDescending { it.addedMs }
                                .thenBy { it.albumSort }
                                .thenBy { it.disc }
                                .thenBy { it.track }
                                .thenBy { it.titleSort }
                                .thenBy { it.stableKey }
                        )
                        .map { it.value }
                definitions += Definition("decade:$decade", "${decade}s", songs)
            }
        return definitions
    }

    fun stableUid(id: String): Music.UID =
        Music.UID.auxio(Music.UID.Item.PLAYLIST) {
            update("generated-playlist:$id".toByteArray(StandardCharsets.UTF_8))
        }
}
