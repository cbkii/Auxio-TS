/*
 * Copyright (c) 2026 Auxio Project
 * FilteredFSTest.kt is part of Auxio.
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

package org.oxycblt.musikr.pipeline.shim

import android.content.Context
import android.net.Uri
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.oxycblt.musikr.fs.AddedMs
import org.oxycblt.musikr.fs.Components
import org.oxycblt.musikr.fs.FS
import org.oxycblt.musikr.fs.FSUpdate
import org.oxycblt.musikr.fs.File
import org.oxycblt.musikr.fs.Path
import org.oxycblt.musikr.fs.Volume
import org.oxycblt.musikr.util.tryAsync

class FilteredFSTest {
    @Test
    fun exploreForwardsNonNoisyFilesAndClosesDownstream() = runTest {
        val fs = FilteredFS(EmittingFS(this, file("Music/song.mp3")), this, setOf("Android"))
        val output = Channel<File>(Channel.UNLIMITED)

        val result = withTimeout(TIMEOUT_MS) { fs.explore(output).await() }

        assertTrue(result.isSuccess)
        assertEquals("Music/song.mp3", output.receive().path.components.toString())
        assertTrue(output.receiveCatching().isClosed)
    }

    @Test
    fun exploreDropsNoisyFilesByPathComponent() = runTest {
        val fs =
            FilteredFS(
                EmittingFS(
                    this,
                    file("Music/Androids/song.mp3"),
                    file("Android/cache/noise.mp3"),
                    file("Music/song.mp3"),
                ),
                this,
                setOf("Android"),
            )
        val output = Channel<File>(Channel.UNLIMITED)

        val result = withTimeout(TIMEOUT_MS) { fs.explore(output).await() }

        assertTrue(result.isSuccess)
        assertEquals("Music/Androids/song.mp3", output.receive().path.components.toString())
        assertEquals("Music/song.mp3", output.receive().path.components.toString())
        assertTrue(output.receiveCatching().isClosed)
    }

    @Test
    fun exploreClosesDownstreamWhenDelegateReturnsFailure() = runTest {
        val failure = IllegalStateException("boom")
        val fs = FilteredFS(FailingFS(failure), this, setOf("Android"))
        val output = Channel<File>(Channel.UNLIMITED)

        val result = withTimeout(TIMEOUT_MS) { fs.explore(output).await() }

        assertTrue(result.isFailure)
        assertSameFailure(failure, result.exceptionOrNull())
        val downstreamResult = output.receiveCatching()
        assertTrue(downstreamResult.isClosed)
        assertSameFailure(failure, downstreamResult.exceptionOrNull())
    }

    @Test
    fun exploreClosesDownstreamWhenDelegateThrows() = runTest {
        val failure = IllegalArgumentException("thrown")
        val fs = FilteredFS(ThrowingFS(failure), this, setOf("Android"))
        val output = Channel<File>(Channel.UNLIMITED)

        val result = withTimeout(TIMEOUT_MS) { fs.explore(output).await() }

        assertTrue(result.isFailure)
        assertSameFailure(failure, result.exceptionOrNull())
        val downstreamResult = output.receiveCatching()
        assertTrue(downstreamResult.isClosed)
        assertSameFailure(failure, downstreamResult.exceptionOrNull())
    }

    @Test
    fun exploreDoesNotHangWhenDelegateLeavesChannelOpen() = runTest {
        val fs =
            FilteredFS(LeakyButCompletedFS(this, file("Music/song.mp3")), this, setOf("Android"))
        val output = Channel<File>(Channel.UNLIMITED)

        val result = withTimeout(TIMEOUT_MS) { fs.explore(output).await() }

        assertTrue(result.isSuccess)
        assertEquals("Music/song.mp3", output.receive().path.components.toString())
        assertTrue(output.receiveCatching().isClosed)
    }

    @Test
    fun exploreDoesNotMaskDelegateFailureAsSuccessfulEmptyScan() = runTest {
        val failure = IllegalStateException("unmounted")
        val fs = FilteredFS(FailingFS(failure), this, setOf("Android"))
        val output = Channel<File>(Channel.UNLIMITED)

        val result = withTimeout(TIMEOUT_MS) { fs.explore(output).await() }

        assertTrue(result.isFailure)
        assertTrue(output.receiveCatching().isClosed)
        assertSameFailure(failure, result.exceptionOrNull())
    }

    private class EmittingFS(private val scope: CoroutineScope, private vararg val files: File) :
        FS {
        override suspend fun explore(files: Channel<File>): Deferred<Result<Unit>> =
            scope.tryAsync(Dispatchers.Unconfined) {
                this@EmittingFS.files.forEach { files.send(it) }
                files.close()
            }

        override fun track(): Flow<FSUpdate> = emptyFlow()
    }

    private class FailingFS(private val failure: Throwable) : FS {
        override suspend fun explore(files: Channel<File>): Deferred<Result<Unit>> =
            CompletableDeferred<Result<Unit>>(Result.failure(failure))

        override fun track(): Flow<FSUpdate> = emptyFlow()
    }

    private class ThrowingFS(private val failure: Throwable) : FS {
        override suspend fun explore(files: Channel<File>): Deferred<Result<Unit>> =
            CompletableDeferred<Result<Unit>>().also { it.completeExceptionally(failure) }

        override fun track(): Flow<FSUpdate> = emptyFlow()
    }

    private class LeakyButCompletedFS(private val scope: CoroutineScope, private val file: File) :
        FS {
        override suspend fun explore(files: Channel<File>): Deferred<Result<Unit>> =
            scope.tryAsync(Dispatchers.Unconfined) { files.send(file) }

        override fun track(): Flow<FSUpdate> = emptyFlow()
    }

    private object TestVolume : Volume.Internal {
        override val mediaStoreName: String? = null
        override val components: Components = Components.root()

        override fun resolveName(context: Context) = "test"

        override fun isAccessible() = true
    }

    private object TestAddedMs : AddedMs {
        override suspend fun resolve(): Long? = null
    }

    private fun file(path: String) =
        File(
            uri = mockk<Uri>(relaxed = true),
            path = Path(TestVolume, Components.parseUnix(path)),
            addedMs = TestAddedMs,
            modifiedMs = 0,
            mimeType = "audio/mpeg",
            size = 1,
            parent = null,
        )

    private fun assertSameFailure(expected: Throwable, actual: Throwable?) {
        assertEquals(expected::class.java, actual?.javaClass)
        assertEquals(expected.message, actual?.message)
    }

    private companion object {
        const val TIMEOUT_MS = 1000L
    }
}
