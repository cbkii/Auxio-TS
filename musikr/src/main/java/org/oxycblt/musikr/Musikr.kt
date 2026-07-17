/*
 * Copyright (c) 2024 Auxio Project
 * Musikr.kt is part of Auxio.
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
import android.util.Log
import java.io.InputStream
import java.util.concurrent.atomic.AtomicInteger
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.oxycblt.musikr.cache.CachedFile
import org.oxycblt.musikr.cache.IncrementalCache
import org.oxycblt.musikr.covers.Cover
import org.oxycblt.musikr.covers.CoverResult
import org.oxycblt.musikr.pipeline.EvaluateStep
import org.oxycblt.musikr.pipeline.ExploreStep
import org.oxycblt.musikr.pipeline.Explored
import org.oxycblt.musikr.pipeline.ExtractStep
import org.oxycblt.musikr.pipeline.Extracted
import org.oxycblt.musikr.pipeline.PipelinePolicy
import org.oxycblt.musikr.pipeline.RawPlaylist
import org.oxycblt.musikr.pipeline.RawSong
import org.oxycblt.musikr.util.merge
import org.oxycblt.musikr.util.tryAsyncWith

/** A highly opinionated, multi-threaded device music library. */
interface Musikr {
    suspend fun run(onProgress: suspend (IndexingProgress) -> Unit = {}): LibraryResult

    companion object {
        fun new(
            context: Context,
            config: Config,
            noisyDirs: Set<String> = emptySet(),
            pathKeywords: List<String> = emptyList(),
            rootGate: org.oxycblt.musikr.fs.RootGate? = null,
        ): Musikr =
            MusikrImpl(
                config,
                ExploreStep.from(config, noisyDirs, pathKeywords, rootGate),
                ExtractStep.from(context, config),
                EvaluateStep.new(context, config, config.interpretation),
            )

        /** Compatibility-only rich graph reconstruction from persisted cache rows. */
        suspend fun loadCached(context: Context, config: Config): MutableLibrary = coroutineScope {
            val start = System.currentTimeMillis()
            val extracted = Channel<Extracted>(PipelinePolicy.BUFFER_CAPACITY)
            val producer =
                async(Dispatchers.IO) {
                    try {
                        for (cachedFile in config.storage.cache.snapshot()) {
                            cachedFile.toRawSongFast(config.storage)?.let { extracted.send(it) }
                        }
                        for (playlist in config.storage.storedPlaylists.read()) {
                            extracted.send(RawPlaylist(playlist))
                        }
                        extracted.close()
                    } catch (e: Throwable) {
                        extracted.close(e)
                        throw e
                    }
                }
            val evaluator =
                async(Dispatchers.Default) {
                    EvaluateStep.new(context, config, config.interpretation).evaluate(extracted)
                }
            try {
                val library = evaluator.await()
                producer.await()
                Log.d(
                    "Musikr",
                    "Cached snapshot prepared in ${System.currentTimeMillis() - start}ms",
                )
                library
            } catch (e: Throwable) {
                extracted.close(e)
                producer.cancel()
                evaluator.cancel()
                throw e
            }
        }

        private fun CachedFile.toRawSongFast(storage: Storage): RawSong? {
            val audio = audio ?: return null
            val cover = audio.coverId?.let { id -> LazyIdCover(id, storage) }
            return RawSong(file, audio.properties, audio.tags, cover, addedMs)
        }
    }
}

interface LibraryResult {
    val library: MutableLibrary

    /** Delete only resources proven expired by the successfully published generation. */
    suspend fun cleanup()
}

sealed interface IndexingProgress {
    data class Songs(val loaded: Int, val explored: Int) : IndexingProgress

    data object Indeterminate : IndexingProgress
}

private class MusikrImpl(
    private val config: Config,
    private val exploreStep: ExploreStep,
    private val extractStep: ExtractStep,
    private val evaluateStep: EvaluateStep,
) : Musikr {
    override suspend fun run(onProgress: suspend (IndexingProgress) -> Unit) = coroutineScope {
        suspend fun emitProgress(progress: IndexingProgress) {
            withContext(Dispatchers.Main) { onProgress(progress) }
        }

        val incremental = config.storage.cache as? IncrementalCache
        val plan = config.scanPlan
        if (plan != null) {
            requireNotNull(incremental) { "An incremental scan plan requires an IncrementalCache" }
            incremental.beginScan(plan)
        }

        try {
            emitProgress(IndexingProgress.Songs(0, 0))
            val start = System.currentTimeMillis()
            val explored = AtomicInteger(0)
            val loaded = AtomicInteger(0)
            val exploredChannel = Channel<Explored>(PipelinePolicy.BUFFER_CAPACITY)
            val exploredTask = exploreStep.explore(this, exploredChannel)
            val trackedExploredChannel = Channel<Explored>(PipelinePolicy.BUFFER_CAPACITY)
            val trackedExploredTask =
                tryAsyncWith(trackedExploredChannel, Dispatchers.Default) {
                    var lastEmitMs = 0L
                    for (item in exploredChannel) {
                        val exploredCount = explored.incrementAndGet()
                        val now = System.currentTimeMillis()
                        if (now - lastEmitMs >= PipelinePolicy.PROGRESS_INTERVAL_MS) {
                            lastEmitMs = now
                            emitProgress(IndexingProgress.Songs(loaded.get(), exploredCount))
                        }
                        trackedExploredChannel.send(item)
                    }
                    emitProgress(IndexingProgress.Songs(loaded.get(), explored.get()))
                }
            val extractedChannel = Channel<Extracted>(PipelinePolicy.BUFFER_CAPACITY)
            val extractedTask = extractStep.extract(this, trackedExploredChannel, extractedChannel)
            val trackedExtractedChannel = Channel<Extracted>(PipelinePolicy.BUFFER_CAPACITY)
            val trackedExtractedTask =
                tryAsyncWith(trackedExtractedChannel, Dispatchers.Default) {
                    var lastEmitMs = 0L
                    for (item in extractedChannel) {
                        val loadedCount = loaded.incrementAndGet()
                        val now = System.currentTimeMillis()
                        if (now - lastEmitMs >= PipelinePolicy.PROGRESS_INTERVAL_MS) {
                            lastEmitMs = now
                            emitProgress(IndexingProgress.Songs(loadedCount, explored.get()))
                        }
                        trackedExtractedChannel.send(item)
                    }
                    emitProgress(IndexingProgress.Songs(loaded.get(), explored.get()))
                    emitProgress(IndexingProgress.Indeterminate)
                }
            val library = evaluateStep.evaluate(trackedExtractedChannel)
            merge(exploredTask, extractedTask, trackedExploredTask, trackedExtractedTask).await()

            val commit = if (plan != null) incremental?.commitScan() else null
            if (commit != null) {
                Log.d(
                    "Musikr",
                    "Committed ${commit.committedSources.size} source generation(s), " +
                        "${commit.changedRows} changed and ${commit.removedRows} removed rows",
                )
            }
            Log.d("Musikr", "Indexing took ${System.currentTimeMillis() - start}ms")
            LibraryResultImpl(config, library)
        } catch (e: CancellationException) {
            if (plan != null) incremental?.abortScan(e)
            throw e
        } catch (e: Throwable) {
            if (plan != null) incremental?.abortScan(e)
            throw e
        }
    }
}

private class LibraryResultImpl(private val config: Config, override val library: MutableLibrary) :
    LibraryResult {
    override suspend fun cleanup() {
        if (config.cleanupCovers) {
            config.storage.covers.cleanup(library.songs.mapNotNull { it.cover })
        }
    }
}

/** Lightweight ID-only cover reference resolved on first visible use. */
internal class LazyIdCover(override val id: String, private val storage: Storage) : Cover {
    private sealed interface Resolution {
        data object Unresolved : Resolution

        data object Missing : Resolution

        data class Found(val cover: Cover) : Resolution
    }

    private val resolutionMutex = Mutex()
    @Volatile private var resolution: Resolution = Resolution.Unresolved

    override suspend fun open(): InputStream? {
        val cover =
            when (val current = resolution) {
                is Resolution.Found -> current.cover
                Resolution.Missing -> null
                Resolution.Unresolved -> resolveOnce()
            } ?: return null
        return cover.open()
    }

    private suspend fun resolveOnce(): Cover? =
        resolutionMutex.withLock {
            when (val current = resolution) {
                is Resolution.Found -> current.cover
                Resolution.Missing -> null
                Resolution.Unresolved -> {
                    when (val result = storage.covers.obtain(id)) {
                        is CoverResult.Hit -> {
                            resolution = Resolution.Found(result.cover)
                            result.cover
                        }
                        is CoverResult.Miss -> {
                            resolution = Resolution.Missing
                            null
                        }
                    }
                }
            }
        }

    override fun equals(other: Any?) = other is Cover && id == other.id

    override fun hashCode() = id.hashCode()
}
