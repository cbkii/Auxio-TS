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
