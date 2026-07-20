/*
 * Copyright (c) 2026 Auxio Project
 * FloatingTrackMetadata.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.oxycblt.auxio.headunit.overlay

/** Primitive track metadata safe to share with optional head-unit UI surfaces. */
data class FloatingTrackMetadata private constructor(
    val artist: String?,
    val title: String?,
) {
    val displayText: String
        get() =
            when {
                artist != null && title != null -> "$artist - $title"
                title != null -> title
                artist != null -> artist
                else -> ""
            }

    companion object {
        fun from(artist: String?, title: String?): FloatingTrackMetadata? {
            val normalizedArtist = artist?.trim()?.takeIf(String::isNotEmpty)
            val normalizedTitle = title?.trim()?.takeIf(String::isNotEmpty)
            if (normalizedArtist == null && normalizedTitle == null) return null
            return FloatingTrackMetadata(normalizedArtist, normalizedTitle)
        }
    }
}

/**
 * In-process metadata bus for the optional floating-controls ticker.
 *
 * This does not create another playback, MediaSession, notification, or command authority. It only
 * mirrors primitive text already owned by Auxio's canonical playback state. State transitions and
 * listener delivery are serialized so concurrent playback callbacks cannot publish stale text last.
 */
object FloatingTrackMetadataBus {
    private val lock = Any()
    private val listeners = mutableListOf<(FloatingTrackMetadata?) -> Unit>()
    private var currentMetadata: FloatingTrackMetadata? = null

    val current: FloatingTrackMetadata?
        get() = synchronized(lock) { currentMetadata }

    fun publish(artist: String?, title: String?) {
        val next = FloatingTrackMetadata.from(artist, title)
        synchronized(lock) {
            if (next == currentMetadata) return
            currentMetadata = next
            // Notify while holding the serializing lock so concurrent publishers cannot deliver an
            // older snapshot after a newer one. Listeners must remain bounded and non-blocking.
            listeners.toList().forEach { it(next) }
        }
    }

    fun clear() = publish(null, null)

    fun addListener(listener: (FloatingTrackMetadata?) -> Unit) {
        synchronized(lock) {
            if (!listeners.contains(listener)) {
                listeners.add(listener)
            }
            listener(currentMetadata)
        }
    }

    fun removeListener(listener: (FloatingTrackMetadata?) -> Unit) {
        synchronized(lock) { listeners.remove(listener) }
    }
}
