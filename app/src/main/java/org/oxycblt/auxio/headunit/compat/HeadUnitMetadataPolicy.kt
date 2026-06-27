/*
 * Copyright (c) 2024 Auxio Project
 * HeadUnitMetadataPolicy.kt is part of Auxio.
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

package org.oxycblt.auxio.headunit.compat

data class HeadUnitMetadataSnapshot(
    val displayTitle: String,
    val displaySubtitle: String,
    val artist: String,
    val albumArtist: String,
    val albumTitle: String,
    val displayDescription: String,
    val durationMs: Long,
    val mediaId: String,
    val mediaUri: String,
    val hasArtwork: Boolean,
    val artworkUri: String?,
)

object HeadUnitMetadataPolicy {
    fun fromRaw(
        title: CharSequence?,
        artist: CharSequence?,
        albumArtist: CharSequence?,
        albumTitle: CharSequence?,
        durationMs: Long?,
        mediaId: String?,
        mediaUri: String?,
        artworkUri: String?,
        hasArtwork: Boolean,
    ): HeadUnitMetadataSnapshot? {
        val safeTitle = title?.toString()?.trim().orEmpty()
        if (safeTitle.isBlank()) return null
        val safeArtist = artist?.toString()?.trim().orEmpty()
        val safeAlbumArtist = albumArtist?.toString()?.trim().orEmpty()
        val safeAlbum = albumTitle?.toString()?.trim().orEmpty()
        val subtitle =
            if (safeArtist.isNotBlank()) {
                if (safeAlbumArtist.isNotBlank() && safeArtist != safeAlbumArtist) {
                    "$safeArtist • $safeAlbumArtist"
                } else {
                    safeArtist
                }
            } else if (safeAlbumArtist.isNotBlank()) {
                safeAlbumArtist
            } else {
                ""
            }
        val description = if (safeAlbum.isNotBlank()) safeAlbum else subtitle
        return HeadUnitMetadataSnapshot(
            displayTitle = safeTitle,
            displaySubtitle = subtitle,
            artist = safeArtist,
            albumArtist = safeAlbumArtist,
            albumTitle = safeAlbum,
            displayDescription = description,
            durationMs = durationMs ?: 0L,
            mediaId = mediaId.orEmpty(),
            mediaUri = mediaUri.orEmpty(),
            hasArtwork = hasArtwork || !artworkUri.isNullOrBlank(),
            artworkUri = artworkUri?.takeIf { it.isNotBlank() },
        )
    }
}
