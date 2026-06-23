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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import org.oxycblt.musikr.cache.CachedFile
import org.oxycblt.musikr.covers.Cover
import org.oxycblt.musikr.covers.CoverResult
import org.oxycblt.musikr.pipeline.EvaluateStep
import org.oxycblt.musikr.pipeline.ExploreStep
import org.oxycblt.musikr.pipeline.Explored
import org.oxycblt.musikr.pipeline.ExtractStep
import org.oxycblt.musikr.pipeline.Extracted
import org.oxycblt.musikr.pipeline.RawPlaylist
import org.oxycblt.musikr.pipeline.RawSong
import org.oxycblt.musikr.util.merge
import org.oxycblt.musikr.util.tryAsyncWith

/**
 * A highly opinionated, multi-threaded device music library.
 *
 * Use this to load music with [run].
 *
 * Note the following:
 * 1. Musikr's API surface is intended to be primarily "stateless", with side-effects mostly
 *    contained within [Storage]. It's your job to manage long-term state.
 * 2. There are no "defaults" in Musikr. You should think carefully about the parameters you are
 *    specifying and know consider they are desirable or not.
 * 3. Musikr is currently not extendable, so if you're embedding this elsewhere you should be ready
 *    to fork and modify the source code.
 */
interface Musikr {
    /**
     * Start loading music using the given config and the configuration provided earlier.
     *
     * @param onProgress Optional callback to receive progress on the current status of the music
     *   pipeline. Warning: These events will be rapid-fire.
     * @return A handle to the newly created library alongside further cleanup.
     */
    suspend fun run(onProgress: suspend (IndexingProgress) -> Unit = {}): LibraryResult

    companion object {
        /**
         * Create a new instance from the given configuration.
         *
         * @param context The context to use for loading resources.
         * @param config Side-effect laden storage for use within the music loader **and** when
         *   mutating [MutableLibrary]. You should take responsibility for managing their long-term
         *   state.
         * @param interpretation The configuration to use for interpreting certain vague tags. This
         *   should be configured by the user, if possible.
         */
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

        /**
         * Rebuild the last indexed library from persisted cache rows without exploring storage.
         *
         * This gives callers a fast startup path: the returned library can be displayed and used by
         * media/session code immediately while any explicit or first-run scan happens later.
         *
         * Cover hydration is intentionally skipped during cached startup to avoid blocking first UI
         * availability on cover storage I/O. Covers are referenced by ID and can be loaded lazily
         * by image loaders when needed.
         */
        suspend fun loadCached(context: Context, config: Config): MutableLibrary {
            val start = System.currentTimeMillis()
            val extracted = Channel<Extracted>(Channel.UNLIMITED)
            for (cachedFile in config.storage.cache.snapshot()) {
                cachedFile.toRawSongFast(config.storage)?.let { extracted.send(it) }
            }
            for (playlist in config.storage.storedPlaylists.read()) {
                extracted.send(RawPlaylist(playlist))
            }
            extracted.close()
            Log.d("Musikr", "Cached snapshot prepared in ${System.currentTimeMillis() - start}ms")
            return EvaluateStep.new(context, config, config.interpretation).evaluate(extracted)
        }

        /**
         * Fast conversion from [CachedFile] to [RawSong] that skips cover hydration (obtain). The
         * cover ID is preserved as a stub so image loaders can resolve it lazily.
         */
        private fun CachedFile.toRawSongFast(storage: Storage): RawSong? {
            val audio = audio ?: return null
            // Create a lightweight cover reference without doing I/O to verify it exists.
            // The image loader will call covers.obtain(id) lazily when the art is displayed.
            val cover = audio.coverId?.let { id -> LazyIdCover(id) }
            return RawSong(file, audio.properties, audio.tags, cover, addedMs)
        }

        private suspend fun CachedFile.toRawSong(storage: Storage): RawSong? {
            val audio = audio ?: return null
            val cover =
                when (val result = audio.coverId?.let { storage.covers.obtain(it) }) {
                    is CoverResult.Hit -> result.cover
                    else -> null
                }
            return RawSong(file, audio.properties, audio.tags, cover, addedMs)
        }
    }
}

/** Simple library handle returned by [Musikr.run]. */
interface LibraryResult {
    val library: MutableLibrary

    /**
     * Clean up expired resources. This should be done as soon as possible after music loading to
     * reduce storage use.
     *
     * This may have unexpected results if previous [Library]s are in circulation across your app,
     * so use it once you've fully updated your state.
     */
    suspend fun cleanup()
}

/** Music loading progress as reported by the music pipeline. */
sealed interface IndexingProgress {
    /**
     * Currently indexing and extracting tags from device music.
     *
     * @param explored The amount of music currently found from the given [Query].
     * @param loaded The amount of music that has had metadata extracted and parsed.
     */
    data class Songs(val loaded: Int, val explored: Int) : IndexingProgress

    /**
     * Currently creating the music graph alongside I/O finalization.
     *
     * There is no way to measure progress on these events.
     */
    data object Indeterminate : IndexingProgress
}

private class MusikrImpl(
    private val config: Config,
    private val exploreStep: ExploreStep,
    private val extractStep: ExtractStep,
    private val evaluateStep: EvaluateStep,
) : Musikr {
    override suspend fun run(onProgress: suspend (IndexingProgress) -> Unit) = coroutineScope {
        onProgress(IndexingProgress.Songs(0, 0))
        val start = System.currentTimeMillis()
        var explored = 0
        var loaded = 0
        val exploredChannel = Channel<Explored>(Channel.UNLIMITED)
        val exploredTask = exploreStep.explore(this, exploredChannel)
        val trackedExploredChannel = Channel<Explored>(Channel.UNLIMITED)
        val trackedExploredTask =
            tryAsyncWith(trackedExploredChannel, Dispatchers.Main) {
                var lastEmitMs = 0L
                for (item in exploredChannel) {
                    explored++
                    // Emitting per-item progress floods the main thread with state updates
                    // (notification/UI refreshes) on large libraries; throttle to a humane rate.
                    val now = System.currentTimeMillis()
                    if (now - lastEmitMs >= PROGRESS_INTERVAL_MS) {
                        lastEmitMs = now
                        onProgress(IndexingProgress.Songs(loaded, explored))
                    }
                    trackedExploredChannel.send(item)
                }
                onProgress(IndexingProgress.Songs(loaded, explored))
            }
        val extractedChannel = Channel<Extracted>(Channel.UNLIMITED)
        val extractedTask = extractStep.extract(this, trackedExploredChannel, extractedChannel)
        val trackedExtractedChannel = Channel<Extracted>(Channel.UNLIMITED)
        val trackedExtractedTask =
            tryAsyncWith(trackedExtractedChannel, Dispatchers.Main) {
                var lastEmitMs = 0L
                for (item in extractedChannel) {
                    loaded++
                    val now = System.currentTimeMillis()
                    if (now - lastEmitMs >= PROGRESS_INTERVAL_MS) {
                        lastEmitMs = now
                        onProgress(IndexingProgress.Songs(loaded, explored))
                    }
                    trackedExtractedChannel.send(item)
                }
                onProgress(IndexingProgress.Songs(loaded, explored))
                onProgress(IndexingProgress.Indeterminate)
            }
        val library = evaluateStep.evaluate(trackedExtractedChannel)
        merge(exploredTask, extractedTask, trackedExploredTask, trackedExtractedTask).await()
        Log.d("Musikr", "Indexing took ${System.currentTimeMillis() - start}ms")
        LibraryResultImpl(config, library)
    }

    private companion object {
        /**
         * Minimum interval between [IndexingProgress.Songs] emissions. Progress consumers update
         * notifications/UI on the main thread, so per-item emission floods weak head-unit CPUs
         * during large scans.
         */
        const val PROGRESS_INTERVAL_MS = 100L
    }
}

private class LibraryResultImpl(private val config: Config, override val library: MutableLibrary) :
    LibraryResult {
    override suspend fun cleanup() {
        config.storage.covers.cleanup(library.songs.mapNotNull { it.cover })
    }
}

/**
 * A lightweight [Cover] stub that only holds an ID for deferred resolution.
 *
 * Used during cached startup to avoid blocking library emission on cover I/O. Image loaders resolve
 * the actual cover data lazily via [Covers.obtain].
 */
internal class LazyIdCover(override val id: String) : Cover {
    override suspend fun open(): InputStream? = null

    override fun equals(other: Any?) = other is Cover && id == other.id

    override fun hashCode() = id.hashCode()
}
