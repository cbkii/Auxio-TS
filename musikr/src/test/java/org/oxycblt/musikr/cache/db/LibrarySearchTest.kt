/*
 * Copyright (c) 2026 Auxio Project
 * LibrarySearchTest.kt is part of Auxio.
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

package org.oxycblt.musikr.cache.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Executable coverage for the database-first song search primitive.
 *
 * The DAO tests run against a real Room [CacheDatabase] so that the `LIKE ... ESCAPE '\'` clause,
 * deterministic ordering, and offset paging are validated by SQLite itself rather than by
 * source-text inspection. The coordinator tests validate strict bounds, obsolete-query suppression,
 * and cooperative cancellation with in-memory fakes.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
class LibrarySearchTest {
    private lateinit var context: Context
    private var db: CacheDatabase? = null

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        context.deleteDatabase(DB_NAME)
        db =
            Room.databaseBuilder(context, CacheDatabase::class.java, DB_NAME)
                .allowMainThreadQueries()
                .build()
    }

    @After
    fun tearDown() {
        db?.close()
        context.deleteDatabase(DB_NAME)
    }

    private fun insertSong(
        uri: String,
        title: String,
        titleSort: String = title.lowercase(),
        artistSort: String? = null,
        albumSort: String? = null,
        available: Boolean = true,
    ) {
        fun sql(value: String?) = value?.let { "'${it.replace("'", "''")}'" } ?: "NULL"
        db!!.openHelper.writableDatabase.execSQL(
            "INSERT INTO LibrarySongData (stableUid, volumeId, uri, fileName, title, titleSort, " +
                "primaryArtistName, primaryArtistSort, albumName, albumSort, sizeBytes, " +
                "modifiedTimeMs, dateAddedMs, scanGeneration, metadataRevision, available) VALUES (" +
                "${sql(uri)}, 1, ${sql(uri)}, ${sql(title)}, ${sql(title)}, ${sql(titleSort)}, " +
                "NULL, ${sql(artistSort)}, NULL, ${sql(albumSort)}, 1, 1000, 2000, 0, 0, " +
                "${if (available) 1 else 0})"
        )
    }

    @Test
    fun `like wildcards from user input are escaped and match literally`() = runBlocking {
        insertSong("content://1", "50% Off", titleSort = "50% off")
        insertSong("content://2", "500 Miles", titleSort = "500 miles")

        val dao = db!!.libraryDao()
        val results = dao.searchSongs(LikeQuery.contains("50%"), limit = 10, offset = 0)

        assertEquals(listOf("content://1"), results.map { it.uri })
    }

    @Test
    fun `underscore wildcard from user input is escaped`() = runBlocking {
        insertSong("content://1", "a_b", titleSort = "a_b")
        insertSong("content://2", "axb", titleSort = "axb")

        val results =
            db!!.libraryDao().searchSongs(LikeQuery.contains("a_b"), limit = 10, offset = 0)

        assertEquals(listOf("content://1"), results.map { it.uri })
    }

    @Test
    fun `search matches artist and album sort columns`() = runBlocking {
        insertSong("content://1", "Track", titleSort = "track", artistSort = "prince")
        insertSong("content://2", "Track", titleSort = "track", albumSort = "purple rain")
        insertSong("content://3", "Track", titleSort = "track")

        val byArtist = db!!.libraryDao().searchSongs(LikeQuery.contains("prince"), 10, 0)
        val byAlbum = db!!.libraryDao().searchSongs(LikeQuery.contains("purple"), 10, 0)

        assertEquals(listOf("content://1"), byArtist.map { it.uri })
        assertEquals(listOf("content://2"), byAlbum.map { it.uri })
    }

    @Test
    fun `unavailable rows are excluded from search`() = runBlocking {
        insertSong("content://1", "Hidden Track", titleSort = "hidden track", available = false)

        val results = db!!.libraryDao().searchSongs(LikeQuery.contains("hidden"), 10, 0)

        assertTrue(results.isEmpty())
    }

    @Test
    fun `paging is deterministic across equal sort keys with no gaps or repeats`() = runBlocking {
        repeat(6) { insertSong("content://match/$it", "Song", titleSort = "song") }
        val dao = db!!.libraryDao()

        val page0 = dao.searchSongs(LikeQuery.contains("song"), limit = 2, offset = 0)
        val page1 = dao.searchSongs(LikeQuery.contains("song"), limit = 2, offset = 2)
        val page2 = dao.searchSongs(LikeQuery.contains("song"), limit = 2, offset = 4)

        val all = (page0 + page1 + page2).map { it.id }
        assertEquals(2, page0.size)
        assertEquals(6, all.size)
        assertEquals(all.sorted(), all)
        assertEquals(all.toSet().size, all.size)
    }

    @Test
    fun `blank query returns no rows without touching the database`() = runBlocking {
        var queried = false
        val searcher =
            LibrarySearcher { _, _, _ ->
                queried = true
                emptyList()
            }

        val result = searcher.search("   ")

        assertEquals(SearchResult.Results(emptyList()), result)
        assertFalse(queried)
    }

    @Test
    fun `non-positive limit returns no rows without touching the database`() = runBlocking {
        var queried = false
        val searcher =
            LibrarySearcher { _, _, _ ->
                queried = true
                emptyList()
            }

        assertEquals(SearchResult.Results(emptyList()), searcher.search("query", limit = 0))
        assertEquals(SearchResult.Results(emptyList()), searcher.search("query", limit = -1))
        assertFalse(queried)
    }

    @Test
    fun `oversized pages are capped and offset multiplication cannot overflow`() = runBlocking {
        var observedLimit = -1
        var observedOffset = -1
        val searcher =
            LibrarySearcher { _, limit, offset ->
                observedLimit = limit
                observedOffset = offset
                emptyList()
            }

        searcher.search("query", limit = Int.MAX_VALUE, page = Int.MAX_VALUE)

        assertEquals(LibrarySearcher.MAX_PAGE_SIZE, observedLimit)
        assertEquals(
            (Int.MAX_VALUE / LibrarySearcher.MAX_PAGE_SIZE) * LibrarySearcher.MAX_PAGE_SIZE,
            observedOffset,
        )
    }

    @Test
    fun `a newer query supersedes an older in-flight query`() = runBlocking {
        var triggeredNewer = false
        lateinit var searcher: LibrarySearcher
        searcher =
            LibrarySearcher { _, _, _ ->
                if (!triggeredNewer) {
                    triggeredNewer = true
                    searcher.search("newer")
                }
                listOf(SongListRow(1L, "u", "content://u", "t", null, null, null, null, null))
            }

        val older = searcher.search("older")

        assertTrue(older is SearchResult.Superseded)
    }

    @Test
    fun `cancelling a search coroutine abandons the query cooperatively`() = runBlocking {
        val started = CompletableDeferred<Unit>()
        val gate = CompletableDeferred<Unit>()
        val searcher =
            LibrarySearcher { _, _, _ ->
                started.complete(Unit)
                gate.await()
                emptyList()
            }

        val job = launch { searcher.search("query") }
        started.await()
        job.cancel()
        gate.complete(Unit)
        job.join()

        assertTrue(job.isCancelled)
    }

    private companion object {
        const val DB_NAME = "music_cache.db"
    }
}
