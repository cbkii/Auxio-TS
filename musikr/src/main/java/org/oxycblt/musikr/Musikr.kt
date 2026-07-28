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
import java.util.concurrent.atomic.AtomicLong
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.oxycblt.musikr.cache.CachedFile
import org.oxycblt.musikr.cache.IncrementalCache
import org.oxycblt.musikr.cache.IncrementalScanCommit
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
                context,
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

    val failedSources: Map<String, String>
        get() = emptyMap()

    /** Delete only resources proven expired by the successfully published generation. */
    suspend fun cleanup()
}

sealed interface IndexingProgress {
    val phase: IndexingPhase
    val currentItem: String?

    data class Songs(
        val loaded: Int,
        val explored: Int,
        val evaluated: Int = 0,
        override val phase: IndexingPhase = IndexingPhase.DISCOVERING,
        override val currentItem: String? = null,
    ) : IndexingProgress

    data class Stage(override val phase: IndexingPhase, override val currentItem: String? = null) :
        IndexingProgress

    data object Indeterminate : IndexingProgress {
        override val phase = IndexingPhase.FINALISING
        override val currentItem: String? = null
    }
}

enum class IndexingPhase {
    PREPARING,
    DISCOVERING,
    EXTRACTING,
    EVALUATING,
    FINALISING,
}

/** No configured source completed, reused, or retained readable rows. */
class SourceScanFailureException(val failures: Map<String, String>) :
    IllegalStateException(
        "Every attempted music source failed: " +
            failures.entries.joinToString { (source, detail) -> "$source=$detail" }
    )

internal object SourceScanCommitPolicy {
    fun allAttemptedSourcesFailed(commit: IncrementalScanCommit): Boolean =
        commit.failedSources.isNotEmpty() &&
            commit.committedSources.isEmpty() &&
            commit.reusedSources.isEmpty()

    fun rejectsAsAuthoritativeEmpty(
        commit: IncrementalScanCommit,
        hasPreservedReadableRows: Boolean,
    ): Boolean = allAttemptedSourcesFailed(commit) && !hasPreservedReadableRows
}

private class MusikrImpl(
    private val context: Context,
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
            emitProgress(IndexingProgress.Stage(IndexingPhase.DISCOVERING))
            val start = System.currentTimeMillis()
            val explored = AtomicInteger(0)
            val loaded = AtomicInteger(0)
            val evaluated = AtomicInteger(0)
            val furthestPhase = AtomicInteger(IndexingPhase.DISCOVERING.ordinal)
            fun currentPhase(): IndexingPhase = IndexingPhase.entries[furthestPhase.get()]
            fun advancePhase(phase: IndexingPhase) {
                while (true) {
                    val current = furthestPhase.get()
                    if (
                        phase.ordinal <= current ||
                            furthestPhase.compareAndSet(current, phase.ordinal)
                    ) {
                        return
                    }
                }
            }
            fun progress(item: String? = null) =
                IndexingProgress.Songs(
                    loaded = loaded.get(),
                    explored = explored.get(),
                    evaluated = evaluated.get(),
                    phase = currentPhase(),
                    currentItem = item,
                )
            val exploredChannel = Channel<Explored>(PipelinePolicy.BUFFER_CAPACITY)
            val exploredTask = exploreStep.explore(this, exploredChannel)
            val lastExtractionEmitMs = AtomicLong(0L)
            val trackedExploredChannel = Channel<Explored>(PipelinePolicy.BUFFER_CAPACITY)
            val trackedExploredTask =
                tryAsyncWith(trackedExploredChannel, Dispatchers.Default) {
                    var lastEmitMs = 0L
                    for (item in exploredChannel) {
                        explored.incrementAndGet()
                        val now = System.currentTimeMillis()
                        if (
                            currentPhase() == IndexingPhase.DISCOVERING &&
                                now - lastEmitMs >= PipelinePolicy.PROGRESS_INTERVAL_MS
                        ) {
                            lastEmitMs = now
                            emitProgress(progress(item.displayName()))
                        }
                        trackedExploredChannel.send(item)
                    }
                    if (currentPhase() == IndexingPhase.DISCOVERING) {
                        emitProgress(progress())
                    }
                }
            val extractedChannel = Channel<Extracted>(PipelinePolicy.BUFFER_CAPACITY)
            val extractedTask =
                extractStep.extract(
                    this,
                    trackedExploredChannel,
                    extractedChannel,
                    onItemStarted = { item ->
                        advancePhase(IndexingPhase.EXTRACTING)
                        val now = System.currentTimeMillis()
                        val prior = lastExtractionEmitMs.get()
                        if (
                            now - prior >= PipelinePolicy.PROGRESS_INTERVAL_MS &&
                                lastExtractionEmitMs.compareAndSet(prior, now)
                        ) {
                            emitProgress(progress(item.displayName()))
                        }
                    },
                )
            val trackedExtractedChannel = Channel<Extracted>(PipelinePolicy.BUFFER_CAPACITY)
            val trackedExtractedTask =
                tryAsyncWith(trackedExtractedChannel, Dispatchers.Default) {
                    var lastEmitMs = 0L
                    for (item in extractedChannel) {
                        loaded.incrementAndGet()
                        advancePhase(IndexingPhase.EXTRACTING)
                        val now = System.currentTimeMillis()
                        if (
                            currentPhase() == IndexingPhase.EXTRACTING &&
                                now - lastEmitMs >= PipelinePolicy.PROGRESS_INTERVAL_MS
                        ) {
                            lastEmitMs = now
                            emitProgress(progress(item.displayName()))
                        }
                        trackedExtractedChannel.send(item)
                    }
                    if (currentPhase() == IndexingPhase.EXTRACTING) {
                        emitProgress(progress())
                    }
                }
            var lastEvaluationEmitMs = 0L
            var resultLibrary =
                evaluateStep.evaluate(
                    trackedExtractedChannel,
                    onItemStarted = { item ->
                        advancePhase(IndexingPhase.EVALUATING)
                        val now = System.currentTimeMillis()
                        if (now - lastEvaluationEmitMs >= PipelinePolicy.PROGRESS_INTERVAL_MS) {
                            lastEvaluationEmitMs = now
                            emitProgress(progress(item.displayName()))
                        }
                    },
                    onItemCompleted = { evaluated.incrementAndGet() },
                )
            merge(exploredTask, extractedTask, trackedExploredTask, trackedExtractedTask).await()
            emitProgress(IndexingProgress.Stage(IndexingPhase.FINALISING))

            val commit = if (plan != null) incremental?.commitScan() else null
            if (commit != null) {
                Log.d(
                    "Musikr",
                    "Committed ${commit.committedSources.size} source generation(s), " +
                        "${commit.changedRows} changed and ${commit.removedRows} removed rows",
                )
                if (SourceScanCommitPolicy.allAttemptedSourcesFailed(commit)) {
                    // A transient provider or mount failure may leave an older committed generation
                    // readable. Reload that generation instead of publishing the empty in-flight
                    // graph. When no readable rows remain, fail explicitly so callers preserve
                    // their
                    // previous library state and expose source recovery rather than confirmed
                    // empty.
                    val hasPreservedRows = config.storage.cache.snapshot().any { it.audio != null }
                    if (
                        SourceScanCommitPolicy.rejectsAsAuthoritativeEmpty(commit, hasPreservedRows)
                    ) {
                        throw SourceScanFailureException(commit.failedSources)
                    }
                    resultLibrary = Musikr.loadCached(context, config)
                }
            }
            Log.d("Musikr", "Indexing took ${System.currentTimeMillis() - start}ms")
            LibraryResultImpl(config, resultLibrary, commit?.failedSources.orEmpty())
        } catch (e: CancellationException) {
            abortIncremental(plan, incremental, e)
            throw e
        } catch (e: Throwable) {
            abortIncremental(plan, incremental, e)
            throw e
        }
    }

    private fun Explored.displayName(): String? =
        when (this) {
            is NewSong -> file.path.resolve(context)
            is RawSong -> file.path.resolve(context)
            is RawPlaylist -> file.name
            is NotAudio -> null
        }

    private fun Extracted.displayName(): String? =
        when (this) {
            is RawSong -> file.path.resolve(context)
            is RawPlaylist -> file.name
            is InvalidSong,
            is NotAudio -> null
        }

    private suspend fun abortIncremental(
        plan: org.oxycblt.musikr.cache.IncrementalScanPlan?,
        incremental: IncrementalCache?,
        original: Throwable,
    ) {
        if (plan == null || incremental == null) return
        try {
            withContext(NonCancellable) { incremental.abortScan(original) }
        } catch (abortFailure: Throwable) {
            original.addSuppressed(abortFailure)
        }
    }
}

private class LibraryResultImpl(
    private val config: Config,
    override val library: MutableLibrary,
    override val failedSources: Map<String, String>,
) : LibraryResult {
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
