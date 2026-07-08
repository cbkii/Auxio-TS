/*
 * Copyright (c) 2026 Auxio Project
 * GeneratedPlaylistPolicyTest.kt is part of Auxio.
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

import android.net.Uri
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.oxycblt.auxio.list.sort.Sort
import org.oxycblt.musikr.Album
import org.oxycblt.musikr.Artist
import org.oxycblt.musikr.Genre
import org.oxycblt.musikr.Music
import org.oxycblt.musikr.Song
import org.oxycblt.musikr.covers.Cover
import org.oxycblt.musikr.covers.CoverCollection
import org.oxycblt.musikr.fs.Format
import org.oxycblt.musikr.fs.Path
import org.oxycblt.musikr.tag.Date
import org.oxycblt.musikr.tag.Disc
import org.oxycblt.musikr.tag.Name
import org.oxycblt.musikr.tag.ReleaseType
import org.oxycblt.musikr.tag.ReplayGainAdjustment
import org.oxycblt.musikr.tag.Token

class GeneratedPlaylistPolicyTest {
    @Test
    fun decadeChipAction_activeDecadeClearsFilter_inactiveDecadePlays() {
        assertEquals(
            GeneratedPlaylistPolicy.DecadeChipAction.CLEAR_FILTER,
            GeneratedPlaylistPolicy.decadeChipAction(activeDecade = 1990, tappedDecade = 1990),
        )
        assertEquals(
            GeneratedPlaylistPolicy.DecadeChipAction.PLAY_DECADE,
            GeneratedPlaylistPolicy.decadeChipAction(activeDecade = 1980, tappedDecade = 1990),
        )
        assertEquals(
            GeneratedPlaylistPolicy.DecadeChipAction.PLAY_DECADE,
            GeneratedPlaylistPolicy.decadeChipAction(activeDecade = null, tappedDecade = 1990),
        )
    }

    @Test
    fun sharedSorts_matchGeneratedPlaylistOrderingAndUiContext() {
        assertEquals(
            Sort(Sort.Mode.ByDate, Sort.Direction.DESCENDING),
            GeneratedPlaylistPolicy.decadePlaybackSort,
        )
        assertEquals(
            Sort(Sort.Mode.ByDateAdded, Sort.Direction.DESCENDING),
            GeneratedPlaylistPolicy.recentlyAddedSort,
        )
        assertNull(GeneratedPlaylistPolicy.recentlyAddedDecadeFilter)
    }

    @Test
    fun songsForDecade_includesOnlyMatchingYears_newestFirst() {
        val song1989 = testSong("1989", year = 1989, addedMs = 1)
        val song1990 = testSong("1990", year = 1990, addedMs = 2)
        val song1995 = testSong("1995", year = 1995, addedMs = 3)
        val song1999 = testSong("1999", year = 1999, addedMs = 4)
        val song2000 = testSong("2000", year = 2000, addedMs = 5)

        val result =
            GeneratedPlaylistPolicy.songsForDecade(
                listOf(song1990, song1989, song2000, song1995, song1999),
                1990,
            )

        assertEquals(listOf(song1999, song1995, song1990), result)
    }

    @Test
    fun songsForDecade_excludesUnknownAndOutOfRangeYears() {
        val unknown = testSong("unknown", year = null, addedMs = 1)
        val old = testSong("old", year = 1989, addedMs = 2)
        val next = testSong("next", year = 2000, addedMs = 3)

        val result = GeneratedPlaylistPolicy.songsForDecade(listOf(unknown, old, next), 1990)

        assertTrue(result.isEmpty())
    }

    @Test
    fun filterSongsForDecadePreservingOrder_nullDecadeReturnsFullVisibleList() {
        val first = testSong("first", year = 1995, addedMs = 10)
        val second = testSong("second", year = 2005, addedMs = 20)

        val result =
            GeneratedPlaylistPolicy.filterSongsForDecadePreservingOrder(listOf(first, second), null)

        assertEquals(listOf(first, second), result)
    }

    @Test
    fun filterSongsForDecadePreservingOrder_keepsVisibleListOrdering() {
        val first = testSong("first", year = 1995, addedMs = 10)
        val second = testSong("second", year = 1990, addedMs = 30)
        val third = testSong("third", year = 1999, addedMs = 20)

        val result =
            GeneratedPlaylistPolicy.filterSongsForDecadePreservingOrder(
                listOf(first, second, third),
                1990,
            )

        assertEquals(listOf(first, second, third), result)
    }

    @Test
    fun recentlyAddedSongs_ordersNewestFirst() {
        val oldest = testSong("oldest", year = 2000, addedMs = 10)
        val newest = testSong("newest", year = 2001, addedMs = 30)
        val middle = testSong("middle", year = 2002, addedMs = 20)

        val result = GeneratedPlaylistPolicy.recentlyAddedSongs(listOf(oldest, newest, middle))

        assertEquals(listOf(newest, middle, oldest), result)
    }

    @Test
    fun songsByDecade_groupsCorrectlyAndExcludesUnknown() {
        val unknown = testSong("unknown", year = null, addedMs = 1)
        val s1985 = testSong("1985", year = 1985, addedMs = 2)
        val s1989 = testSong("1989", year = 1989, addedMs = 3)
        val s1990 = testSong("1990", year = 1990, addedMs = 4)
        val s2005 = testSong("2005", year = 2005, addedMs = 5)

        val map = GeneratedPlaylistPolicy.songsByDecade(listOf(unknown, s1985, s1989, s1990, s2005))

        assertEquals(3, map.size)
        // 1980s bucket sorted newest-first
        assertEquals(listOf(s1989, s1985), map[1980])
        // 1990s bucket
        assertEquals(listOf(s1990), map[1990])
        // 2000s bucket
        assertEquals(listOf(s2005), map[2000])
    }

    @Test
    fun songsByDecade_emptyInputReturnsEmptyMap() {
        assertTrue(GeneratedPlaylistPolicy.songsByDecade(emptyList()).isEmpty())
    }

    @Test
    fun generatedPlaylists_returnEmptySafelyForEmptyLibrary() {
        assertTrue(GeneratedPlaylistPolicy.songsForDecade(emptyList(), 1990).isEmpty())
        assertTrue(GeneratedPlaylistPolicy.recentlyAddedSongs(emptyList()).isEmpty())
    }

    private fun testSong(label: String, year: Int?, addedMs: Long): Song =
        TestSong(label, TestAlbum("album-$label", year), addedMs)

    private class TestSong(label: String, override val album: Album, override val addedMs: Long) :
        Song {
        override val name = TestName(label)
        override val track: Int? = null
        override val disc: Disc? = null
        override val date: Date? = null
        override val uid: Music.UID
            get() = error("uid not needed")

        override val uri: Uri
            get() = error("uri not needed")

        override val path: Path
            get() = error("path not needed")

        override val format: Format
            get() = error("format not needed")

        override val size: Long
            get() = error("size not needed")

        override val durationMs: Long
            get() = error("duration not needed")

        override val bitrateKbps: Int
            get() = error("bitrate not needed")

        override val sampleRateHz: Int
            get() = error("sample rate not needed")

        override val replayGainAdjustment: ReplayGainAdjustment
            get() = error("replaygain not needed")

        override val modifiedMs: Long
            get() = error("modified not needed")

        override val cover: Cover? = null
        override val artists: List<Artist> = emptyList()
        override val genres: List<Genre> = emptyList()
    }

    private class TestAlbum(label: String, year: Int?) : Album {
        override val name = TestName(label)
        override val dates: Date.Range? = year?.let { Date.Range(Date.from(it)!!, Date.from(it)!!) }
        override val songs: Collection<Song>
            get() = emptyList()

        override val uid: Music.UID
            get() = error("uid not needed")

        override val releaseType: ReleaseType
            get() = error("release type not needed")

        override val covers: CoverCollection
            get() = error("covers not needed")

        override val durationMs: Long
            get() = error("duration not needed")

        override val addedMs: Long
            get() = error("added not needed")

        override val artists: List<Artist> = emptyList()
    }

    private class TestName(override val raw: String) : Name.Known() {
        override val sort: String? = null
        override val tokens: List<Token> = emptyList()

        override fun equals(other: Any?) = other is TestName && raw == other.raw

        override fun hashCode() = raw.hashCode()

        override fun toString() = raw
    }
}
