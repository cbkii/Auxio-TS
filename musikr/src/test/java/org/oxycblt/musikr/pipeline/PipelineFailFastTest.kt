/*
 * Copyright (c) 2026 Auxio Project
 * PipelineFailFastTest.kt is part of Auxio.
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

package org.oxycblt.musikr.pipeline

import android.net.Uri
import java.util.concurrent.atomic.AtomicInteger
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith
import org.oxycblt.musikr.util.map
import org.oxycblt.musikr.util.mapParallel
import org.oxycblt.musikr.util.merge
import org.oxycblt.musikr.util.tryAsync
import org.oxycblt.musikr.util.tryAsyncWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Termination proofs for the shared pipeline concurrency helpers.
 *
 * Every test uses a bounded timeout: the defect being guarded against is a scan that never
 * terminates, so "did not throw" is not a sufficient assertion.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
class PipelineFailFastTest {
    @Test
    fun `producer failure before the first send closes the consumer`() = runBlocking {
        val failure = IllegalStateException("enumeration failed immediately")
        val output = Channel<Int>(2)
        val received = mutableListOf<Int>()

        val result =
            withTimeout(TIMEOUT_MS) {
                coroutineScope {
                    val producer = tryAsyncWith(output, Dispatchers.Default) { throw failure }
                    val consumer = tryAsync(Dispatchers.Default) { for (v in output) received += v }
                    merge(producer, consumer).await()
                }
            }

        assertTrue(result.isFailure)
        assertCausalFailure(failure, result.exceptionOrNull())
        assertTrue(received.isEmpty())
    }

    @Test
    fun `producer failure after several sends still terminates the consumer`() = runBlocking {
        val failure = IllegalStateException("enumeration failed late")
        val output = Channel<Int>(2)
        val received = AtomicInteger(0)

        val result =
            withTimeout(TIMEOUT_MS) {
                coroutineScope {
                    val producer =
                        tryAsyncWith(output, Dispatchers.Default) {
                            repeat(4) { index -> it.send(index) }
                            throw failure
                        }
                    val consumer =
                        tryAsync(Dispatchers.Default) {
                            for (unused in output) received.incrementAndGet()
                        }
                    merge(producer, consumer).await()
                }
            }

        assertTrue(result.isFailure)
        assertCausalFailure(failure, result.exceptionOrNull())
    }

    @Test
    fun `a failing worker cancels its siblings and closes the output`() = runBlocking {
        val failure = IllegalStateException("worker failed")
        val input = Channel<Int>(Channel.UNLIMITED)
        val output = Channel<Int>(Channel.UNLIMITED)
        repeat(64) { input.trySend(it) }
        input.close()

        val result =
            withTimeout(TIMEOUT_MS) {
                coroutineScope {
                    mapParallel(4, input, output, Dispatchers.Default) { value ->
                            if (value == 7) throw failure
                            value
                        }
                        .await()
                }
            }

        assertTrue(result.isFailure)
        assertCausalFailure(failure, result.exceptionOrNull())
        var closeCause: Throwable? = null
        while (true) {
            val next = output.receiveCatching()
            if (next.isClosed) {
                closeCause = next.exceptionOrNull()
                break
            }
        }
        assertCausalFailure(failure, closeCause)
    }

    @Test
    fun `a worker cancellation signal is reported as a stage failure`() = runBlocking {
        val failure = CancellationException("worker aborted its item")
        val input =
            Channel<Int>(1).apply {
                trySend(1)
                close()
            }
        val output = Channel<Int>(1)

        val result =
            withTimeout(TIMEOUT_MS) {
                coroutineScope {
                    mapParallel(2, input, output, Dispatchers.Default) { throw failure }.await()
                }
            }

        assertTrue(result.isFailure)
        assertCausalFailure(failure, result.exceptionOrNull())
    }

    @Test
    fun `an independently cancelled deferred fails the aggregate`() = runBlocking {
        val failure = CancellationException("stage cancelled independently")
        val stage = CompletableDeferred<Result<Unit>>()
        stage.cancel(failure)

        val result = withTimeout(TIMEOUT_MS) { coroutineScope { merge(stage).await() } }

        assertTrue(result.isFailure)
        assertCausalFailure(failure, result.exceptionOrNull())
    }

    @Test
    fun `a cancellation result fails the aggregate`() = runBlocking {
        val failure = CancellationException("stage returned cancellation failure")
        val stage = CompletableDeferred(Result.failure<Unit>(failure))

        val result = withTimeout(TIMEOUT_MS) { coroutineScope { merge(stage).await() } }

        assertTrue(result.isFailure)
        assertCausalFailure(failure, result.exceptionOrNull())
    }

    @Test
    fun `a failed consumer unblocks a producer suspended on back-pressure`() = runBlocking {
        val failure = IllegalStateException("consumer failed")
        // Capacity one guarantees the producer suspends on send well before the consumer fails.
        val stage = Channel<Int>(1)
        val output = Channel<Int>(1)
        val produced = AtomicInteger(0)

        val result =
            withTimeout(TIMEOUT_MS) {
                coroutineScope {
                    val producer =
                        tryAsyncWith(stage, Dispatchers.Default) {
                            repeat(1_000) { index ->
                                it.send(index)
                                produced.incrementAndGet()
                            }
                        }
                    val consumer =
                        mapParallel(2, stage, output, Dispatchers.Default) { value ->
                            if (value >= 3) throw failure
                            value
                        }
                    val drain = tryAsync(Dispatchers.Default) { for (unused in output) Unit }
                    merge(producer, consumer, drain).await()
                }
            }

        assertTrue(result.isFailure)
        assertCausalFailure(failure, result.exceptionOrNull())
        assertTrue(produced.get() < 1_000)
    }

    @Test
    fun `a failed upstream producer terminates a waiting downstream consumer`() = runBlocking {
        val failure = IllegalStateException("upstream failed while downstream waited")
        val stage = Channel<Int>(4)
        val output = Channel<Int>(4)

        val result =
            withTimeout(TIMEOUT_MS) {
                coroutineScope {
                    val consumer = mapParallel(2, stage, output, Dispatchers.Default) { it }
                    val drain = tryAsync(Dispatchers.Default) { for (unused in output) Unit }
                    val producer = tryAsyncWith(stage, Dispatchers.Default) { throw failure }
                    merge(producer, consumer, drain).await()
                }
            }

        assertTrue(result.isFailure)
        assertCausalFailure(failure, result.exceptionOrNull())
    }

    @Test
    fun `cancellation of the pipeline scope is not reported as an ordinary failure`() =
        runBlocking {
            val started = CompletableDeferred<Unit>()
            var observed: Throwable? = null

            withTimeout(TIMEOUT_MS) {
                val outer = launch {
                    try {
                        coroutineScope {
                            val stage = Channel<Int>(1)
                            val output = Channel<Int>(1)
                            val producer =
                                tryAsyncWith(stage, Dispatchers.Default) {
                                    started.complete(Unit)
                                    var index = 0
                                    while (true) it.send(index++)
                                }
                            val consumer = mapParallel(2, stage, output, Dispatchers.Default) { it }
                            val drain = tryAsync(Dispatchers.Default) { for (v in output) Unit }
                            merge(producer, consumer, drain).await().getOrThrow()
                        }
                    } catch (e: CancellationException) {
                        observed = e
                        throw e
                    }
                }
                started.await()
                outer.cancel()
                outer.join()
            }

            assertTrue(observed is CancellationException)
        }

    @Test
    fun `a successful stage forwards every item and closes its output`() = runBlocking {
        val input = Channel<Int>(Channel.UNLIMITED)
        val output = Channel<Int>(Channel.UNLIMITED)
        repeat(500) { input.trySend(it) }
        input.close()

        val result =
            withTimeout(TIMEOUT_MS) {
                coroutineScope { mapParallel(4, input, output, Dispatchers.Default) { it }.await() }
            }

        assertTrue(result.isSuccess)
        var count = 0
        while (true) {
            if (output.receiveCatching().isClosed) break
            count++
        }
        assertEquals(500, count)
    }

    @Test
    fun `an empty but successful stage closes its output normally`() = runBlocking {
        val input = Channel<Int>(1).apply { close() }
        val output = Channel<Int>(1)

        val result =
            withTimeout(TIMEOUT_MS) {
                coroutineScope { map(input, output, Dispatchers.Default) { it }.await() }
            }

        assertTrue(result.isSuccess)
        val closed = output.receiveCatching()
        assertTrue(closed.isClosed)
        assertFalse(closed.exceptionOrNull() != null)
    }
}

/** Fault injection covering the classification, cache read and hydration stages. */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
class ExploreStepFailFastTest {
    @Test
    fun `enumeration failure before any file reaches the caller`() = runBlocking {
        val failure = IllegalStateException("enumeration failed")
        val result = explore(ThrowingFS(failure))
        assertTrue(result.isFailure)
        assertCausalFailure(failure, result.exceptionOrNull())
    }

    @Test
    fun `enumeration failure after several files still terminates`() = runBlocking {
        val failure = IllegalStateException("enumeration failed late")
        val result = explore(EmittingThenFailingFS(failure, count = 8))
        assertTrue(result.isFailure)
        assertCausalFailure(failure, result.exceptionOrNull())
    }

    @Test
    fun `cache read failure terminates the scan with the causal exception`() = runBlocking {
        val failure = IllegalStateException("cache read failed")
        val result = explore(EmittingFS(count = 16), cache = ThrowingCache(failure))
        assertTrue(result.isFailure)
        assertCausalFailure(failure, result.exceptionOrNull())
    }

    @Test
    fun `cached row hydration failure terminates the scan`() = runBlocking {
        val failure = IllegalStateException("hydration failed")
        val result =
            explore(EmittingFS(count = 16), cache = HitCache, covers = ThrowingCovers(failure))
        assertTrue(result.isFailure)
        assertCausalFailure(failure, result.exceptionOrNull())
    }

    @Test
    fun `an empty source completes successfully`() = runBlocking {
        assertTrue(explore(EmittingFS(count = 0)).isSuccess)
    }

    @Test
    fun `a large source completes within a bounded channel capacity`() = runBlocking {
        assertTrue(explore(EmittingFS(count = 2_000)).isSuccess)
    }

    private suspend fun explore(
        fs: org.oxycblt.musikr.fs.FS,
        cache: org.oxycblt.musikr.cache.MutableCache = MissCache,
        covers: org.oxycblt.musikr.covers.MutableCovers<out org.oxycblt.musikr.covers.Cover> =
            NoCovers,
    ): Result<Unit> =
        withTimeout(TIMEOUT_MS) {
            coroutineScope {
                val config =
                    org.oxycblt.musikr.Config(
                        fs = fs,
                        storage = org.oxycblt.musikr.Storage(cache, covers, EmptyStoredPlaylists),
                        interpretation =
                            org.oxycblt.musikr.Interpretation(
                                org.oxycblt.musikr.tag.interpret.Naming.simple(),
                                org.oxycblt.musikr.tag.interpret.Separators.from(""),
                            ),
                        indexingWorkerCount = 4,
                    )
                val explored = Channel<Explored>(PipelinePolicy.BUFFER_CAPACITY)
                val drain = tryAsync(Dispatchers.Default) { for (unused in explored) Unit }
                val task = ExploreStep.from(config).explore(this, explored)
                merge(task, drain).await()
            }
        }
}

private fun assertCausalFailure(expected: Throwable, actual: Throwable?) {
    var cursor = actual
    while (cursor != null) {
        if (cursor === expected) return
        cursor = cursor.cause
    }
    fail(
        "Expected ${expected.javaClass.name}: ${expected.message} in failure cause chain, " +
            "but received ${actual?.javaClass?.name}: ${actual?.message}"
    )
}

private const val TIMEOUT_MS = 20_000L

private fun testFile(index: Int): org.oxycblt.musikr.fs.File =
    org.oxycblt.musikr.fs.File(
        uri = Uri.parse("content://test/song$index.mp3"),
        path =
            org.oxycblt.musikr.fs.Path(
                TestVolume,
                org.oxycblt.musikr.fs.Components.parseUnix("Music/song$index.mp3"),
            ),
        addedMs = TestAddedMs,
        modifiedMs = 0L,
        mimeType = "audio/mpeg",
        size = 1L,
        parent = null,
    )

private object TestVolume : org.oxycblt.musikr.fs.Volume.Internal {
    override val mediaStoreName: String? = null
    override val components = org.oxycblt.musikr.fs.Components.root()

    override fun resolveName(context: android.content.Context) = "test"

    override fun isAccessible() = true
}

private object TestAddedMs : org.oxycblt.musikr.fs.AddedMs {
    override suspend fun resolve(): Long = 0L
}

private class ThrowingFS(private val failure: Throwable) : org.oxycblt.musikr.fs.FS {
    override suspend fun explore(
        files: Channel<org.oxycblt.musikr.fs.File>
    ): Deferred<Result<Unit>> {
        files.close(failure)
        return CompletableDeferred(Result.failure(failure))
    }

    override fun track(): Flow<org.oxycblt.musikr.fs.FSUpdate> = emptyFlow()
}

private class EmittingFS(private val count: Int) : org.oxycblt.musikr.fs.FS {
    private val scope = CoroutineScope(Dispatchers.Default + kotlinx.coroutines.SupervisorJob())

    override suspend fun explore(
        files: Channel<org.oxycblt.musikr.fs.File>
    ): Deferred<Result<Unit>> =
        scope.tryAsyncWith(files, Dispatchers.Default) {
            repeat(count) { index -> it.send(testFile(index)) }
        }

    override fun track(): Flow<org.oxycblt.musikr.fs.FSUpdate> = emptyFlow()
}

private class EmittingThenFailingFS(private val failure: Throwable, private val count: Int) :
    org.oxycblt.musikr.fs.FS {
    private val scope = CoroutineScope(Dispatchers.Default + kotlinx.coroutines.SupervisorJob())

    override suspend fun explore(
        files: Channel<org.oxycblt.musikr.fs.File>
    ): Deferred<Result<Unit>> =
        scope.tryAsyncWith(files, Dispatchers.Default) {
            repeat(count) { index -> it.send(testFile(index)) }
            throw failure
        }

    override fun track(): Flow<org.oxycblt.musikr.fs.FSUpdate> = emptyFlow()
}

private object MissCache : org.oxycblt.musikr.cache.MutableCache {
    override suspend fun read(file: org.oxycblt.musikr.fs.File) =
        org.oxycblt.musikr.cache.CacheResult.Miss(file)

    override suspend fun snapshot(): List<org.oxycblt.musikr.cache.CachedFile> = emptyList()

    override suspend fun write(cachedFile: org.oxycblt.musikr.cache.CachedFile) = Unit

    override suspend fun cleanup(excluding: List<org.oxycblt.musikr.cache.CachedFile>) = Unit
}

private object HitCache : org.oxycblt.musikr.cache.MutableCache {
    override suspend fun read(file: org.oxycblt.musikr.fs.File) =
        org.oxycblt.musikr.cache.CacheResult.Hit(
            org.oxycblt.musikr.cache.CachedFile(
                file,
                org.oxycblt.musikr.cache.Audio(
                    org.oxycblt.musikr.metadata.Properties("audio/mpeg", 1L, 1, 44_100),
                    org.oxycblt.musikr.tag.parse.ParsedTags(durationMs = 1L),
                    "cover",
                ),
                0L,
            )
        )

    override suspend fun snapshot(): List<org.oxycblt.musikr.cache.CachedFile> = emptyList()

    override suspend fun write(cachedFile: org.oxycblt.musikr.cache.CachedFile) = Unit

    override suspend fun cleanup(excluding: List<org.oxycblt.musikr.cache.CachedFile>) = Unit
}

private class ThrowingCache(private val failure: Throwable) :
    org.oxycblt.musikr.cache.MutableCache {
    override suspend fun read(file: org.oxycblt.musikr.fs.File): Nothing = throw failure

    override suspend fun snapshot(): List<org.oxycblt.musikr.cache.CachedFile> = emptyList()

    override suspend fun write(cachedFile: org.oxycblt.musikr.cache.CachedFile) = Unit

    override suspend fun cleanup(excluding: List<org.oxycblt.musikr.cache.CachedFile>) = Unit
}

private object NoCovers : org.oxycblt.musikr.covers.MutableCovers<org.oxycblt.musikr.covers.Cover> {
    override suspend fun obtain(
        id: String
    ): org.oxycblt.musikr.covers.CoverResult<org.oxycblt.musikr.covers.Cover> =
        org.oxycblt.musikr.covers.CoverResult.Miss()

    override suspend fun create(
        file: org.oxycblt.musikr.fs.File,
        metadata: org.oxycblt.musikr.metadata.Metadata,
    ): org.oxycblt.musikr.covers.CoverResult<org.oxycblt.musikr.covers.Cover> =
        org.oxycblt.musikr.covers.CoverResult.Miss()

    override suspend fun cleanup(excluding: Collection<org.oxycblt.musikr.covers.Cover>) = Unit
}

private class ThrowingCovers(private val failure: Throwable) :
    org.oxycblt.musikr.covers.MutableCovers<org.oxycblt.musikr.covers.Cover> {
    override suspend fun obtain(id: String): Nothing = throw failure

    override suspend fun create(
        file: org.oxycblt.musikr.fs.File,
        metadata: org.oxycblt.musikr.metadata.Metadata,
    ): Nothing = throw failure

    override suspend fun cleanup(excluding: Collection<org.oxycblt.musikr.covers.Cover>) = Unit
}

private object EmptyStoredPlaylists : org.oxycblt.musikr.playlist.db.StoredPlaylists() {
    override suspend fun new(
        name: String,
        songs: List<org.oxycblt.musikr.Song>,
    ): org.oxycblt.musikr.playlist.PlaylistHandle = error("not used")

    override suspend fun read(): List<org.oxycblt.musikr.playlist.PlaylistFile> = emptyList()
}
