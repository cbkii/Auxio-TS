/*
 * Copyright (c) 2026 Auxio Project
 * MusikrCachedLoadTest.kt is part of Auxio.
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

package org.oxycblt.musikr

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.util.concurrent.atomic.AtomicInteger
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.oxycblt.musikr.cache.CacheResult
import org.oxycblt.musikr.cache.CachedFile
import org.oxycblt.musikr.cache.MutableCache
import org.oxycblt.musikr.covers.Cover
import org.oxycblt.musikr.covers.CoverResult
import org.oxycblt.musikr.covers.MutableCovers
import org.oxycblt.musikr.fs.FS
import org.oxycblt.musikr.fs.FSUpdate
import org.oxycblt.musikr.fs.File
import org.oxycblt.musikr.playlist.PlaylistFile
import org.oxycblt.musikr.playlist.PlaylistHandle
import org.oxycblt.musikr.playlist.db.StoredPlaylists
import org.oxycblt.musikr.tag.interpret.Naming
import org.oxycblt.musikr.tag.interpret.Separators
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
class MusikrCachedLoadTest {
    @Test
    fun `cached load consumes more items than the bounded channel capacity`() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val playlists = (0 until 300).map { playlist(it) }
        val config =
            Config(
                fs = NoOpFS,
                storage = Storage(EmptyCache, CountingCovers(), FakeStoredPlaylists(playlists)),
                interpretation = Interpretation(Naming.simple(), Separators.from("")),
                indexingWorkerCount = 1,
            )

        val library = withTimeout(10_000L) { Musikr.loadCached(context, config) }

        assertEquals(300, library.playlists.size)
    }

    @Test
    fun `lazy cover resolves a hit once for concurrent readers`() = runBlocking {
        val expected = ByteArrayCover("cover", byteArrayOf(1, 2, 3))
        val covers = CountingCovers(expected, delayMs = 25L)
        val storage = Storage(EmptyCache, covers, FakeStoredPlaylists(emptyList()))
        val lazy = LazyIdCover("cover", storage)

        val streams = (0 until 20).map { async { lazy.open() } }.awaitAll()

        assertEquals(1, covers.obtainCount.get())
        streams.forEach { stream ->
            assertEquals(listOf(1, 2, 3), stream!!.readBytes().map(Byte::toInt))
        }
    }

    @Test
    fun `lazy cover memoises a miss for the current library generation`() = runBlocking {
        val covers = CountingCovers(null)
        val storage = Storage(EmptyCache, covers, FakeStoredPlaylists(emptyList()))
        val lazy = LazyIdCover("missing", storage)

        assertNull(lazy.open())
        assertNull(lazy.open())

        assertEquals(1, covers.obtainCount.get())
    }

    @Test
    fun `new lazy cover instance can observe a later generation hit`() = runBlocking {
        val covers = CountingCovers(null)
        val storage = Storage(EmptyCache, covers, FakeStoredPlaylists(emptyList()))
        assertNull(LazyIdCover("cover", storage).open())

        val expected = ByteArrayCover("cover", byteArrayOf(7))
        covers.cover = expected
        val stream = LazyIdCover("cover", storage).open()

        assertEquals(expected.data.inputStream().read(), stream!!.read())
        assertEquals(2, covers.obtainCount.get())
    }

    private fun playlist(index: Int): PlaylistFile {
        val handle =
            object : PlaylistHandle {
                override val uid =
                    Music.UID.auxio(Music.UID.Item.PLAYLIST) {
                        update("playlist-$index".toByteArray())
                    }

                override suspend fun rename(name: String) = Unit

                override suspend fun add(songs: List<Song>) = Unit

                override suspend fun rewrite(songs: List<Song>) = Unit

                override suspend fun delete() = Unit
            }
        return PlaylistFile("Playlist $index", emptyList(), handle)
    }

    private object NoOpFS : FS {
        override suspend fun explore(files: Channel<File>): Deferred<Result<Unit>> =
            CompletableDeferred(Result.success(Unit))

        override fun track(): Flow<FSUpdate> = emptyFlow()
    }

    private object EmptyCache : MutableCache {
        override suspend fun read(file: File): CacheResult = CacheResult.Miss(file)

        override suspend fun snapshot(): List<CachedFile> = emptyList()

        override suspend fun write(cachedFile: CachedFile) = Unit

        override suspend fun cleanup(excluding: List<CachedFile>) = Unit
    }

    private class FakeStoredPlaylists(private val playlists: List<PlaylistFile>) :
        StoredPlaylists() {
        override suspend fun new(name: String, songs: List<Song>): PlaylistHandle =
            error("not used")

        override suspend fun read(): List<PlaylistFile> = playlists
    }

    private class CountingCovers(
        @Volatile var cover: Cover? = null,
        private val delayMs: Long = 0L,
    ) : MutableCovers<Cover> {
        val obtainCount = AtomicInteger(0)

        override suspend fun obtain(id: String): CoverResult<Cover> {
            obtainCount.incrementAndGet()
            if (delayMs > 0) delay(delayMs)
            return cover?.let { CoverResult.Hit(it) } ?: CoverResult.Miss()
        }

        override suspend fun create(
            file: File,
            metadata: org.oxycblt.musikr.metadata.Metadata,
        ): CoverResult<Cover> = CoverResult.Miss()

        override suspend fun cleanup(excluding: Collection<Cover>) = Unit
    }

    private data class ByteArrayCover(override val id: String, val data: ByteArray) : Cover {
        override suspend fun open(): InputStream = ByteArrayInputStream(data)
    }
}
