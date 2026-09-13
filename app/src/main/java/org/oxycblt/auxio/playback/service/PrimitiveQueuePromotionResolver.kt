/*
 * Copyright (c) 2026 Auxio Project
 * PrimitiveQueuePromotionResolver.kt is part of Auxio.
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

package org.oxycblt.auxio.playback.service

import android.content.Context
import org.oxycblt.auxio.music.resolve
import org.oxycblt.auxio.music.resolveNames
import org.oxycblt.auxio.playback.persist.QueueItemRef
import org.oxycblt.musikr.Library
import org.oxycblt.musikr.Song

/**
 * Resolves persisted Fast Resume identities without guessing ambiguous media. Expensive path and
 * metadata projections are lazy and are only built when stable UID/URI/model-path matching misses.
 */
internal class PrimitiveQueuePromotionResolver(context: Context, private val library: Library) {
    private val appContext = context.applicationContext
    private val songs = library.songs

    private val songsByUri by
        lazy(LazyThreadSafetyMode.NONE) {
            PrimitiveQueuePromotionIdentityIndex.uniqueBy(songs) { song ->
                PrimitiveQueuePromotionIdentityIndex.normalizeUriIdentity(song.uri.toString())
                    .orEmpty()
            }
        }
    private val songsByModelPath by
        lazy(LazyThreadSafetyMode.NONE) {
            PrimitiveQueuePromotionIdentityIndex.uniqueBy(songs) { song ->
                PrimitiveQueuePromotionIdentityIndex.normalizePathIdentity(song.path.toString())
                    .orEmpty()
            }
        }
    private val songsByResolvedPath by
        lazy(LazyThreadSafetyMode.NONE) {
            PrimitiveQueuePromotionIdentityIndex.uniqueBy(songs) { song ->
                PrimitiveQueuePromotionIdentityIndex.normalizePathIdentity(resolvePath(song))
                    .orEmpty()
            }
        }
    private val songsByTitle by
        lazy(LazyThreadSafetyMode.NONE) {
            songs.groupBy { song ->
                PrimitiveQueuePromotionIdentityIndex.normalizeTextIdentity(resolveTitle(song))
                    .orEmpty()
            }
        }

    fun resolve(item: QueueItemRef): Song? {
        item.stableSongUid?.let(library::findSong)?.let {
            return it
        }

        PrimitiveQueuePromotionIdentityIndex.normalizeUriIdentity(item.uri)
            ?.let(songsByUri::get)
            ?.let {
                return it
            }

        val path = PrimitiveQueuePromotionIdentityIndex.normalizePathIdentity(item.pathFallback)
        path?.let(songsByModelPath::get)?.let {
            return it
        }
        path?.let(songsByResolvedPath::get)?.let {
            return it
        }

        val title =
            PrimitiveQueuePromotionIdentityIndex.normalizeTextIdentity(item.titleFallback)
                ?: return null
        if (item.durationMs <= 0L) return null
        val expectedArtist =
            PrimitiveQueuePromotionIdentityIndex.normalizeTextIdentity(item.artistFallback)
        val expectedAlbum =
            PrimitiveQueuePromotionIdentityIndex.normalizeTextIdentity(item.albumFallback)

        return songsByTitle[title]
            .orEmpty()
            .asSequence()
            .filter { song ->
                PrimitiveQueuePromotionIdentityIndex.durationMatches(
                    item.durationMs,
                    song.durationMs,
                )
            }
            .filter { song ->
                expectedArtist == null ||
                    PrimitiveQueuePromotionIdentityIndex.normalizeTextIdentity(
                        resolveArtist(song)
                    ) == expectedArtist
            }
            .filter { song ->
                expectedAlbum == null ||
                    PrimitiveQueuePromotionIdentityIndex.normalizeTextIdentity(
                        resolveAlbum(song)
                    ) == expectedAlbum
            }
            .distinct()
            .singleOrNull()
    }

    private fun resolvePath(song: Song): String? =
        runCatching { song.path.resolve(appContext) }.getOrNull()

    private fun resolveTitle(song: Song): String? =
        runCatching { song.name.resolve(appContext) }.getOrNull()

    private fun resolveArtist(song: Song): String? =
        runCatching { song.artists.resolveNames(appContext) }.getOrNull()

    private fun resolveAlbum(song: Song): String? =
        runCatching { song.album.name.resolve(appContext) }.getOrNull()
}
