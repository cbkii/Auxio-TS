/*
 * Copyright (c) 2026 Auxio Project
 * RawQueue.kt is part of Auxio.
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

package org.oxycblt.auxio.music

import org.oxycblt.musikr.Song

/** Utility for creating and manipulating raw song queues. */
object RawQueue {
    fun fromSongs(songs: List<Song?>, shuffle: Boolean, anchor: Song? = null): List<Song?> {
        if (!shuffle) return songs
        val pool = songs.toMutableList()
        if (anchor == null) {
            pool.shuffle()
            return pool
        }
        if (!pool.remove(anchor)) {
            pool.shuffle()
            return pool
        }
        pool.shuffle()
        pool.add(0, anchor)
        return pool
    }
}
