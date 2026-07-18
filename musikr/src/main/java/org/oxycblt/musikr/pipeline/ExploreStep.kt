/*
 * Copyright (c) 2024 Auxio Project
 * ExploreStep.kt is part of Auxio.
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

import java.util.Locale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import org.oxycblt.musikr.Config
import org.oxycblt.musikr.Storage
import org.oxycblt.musikr.cache.CacheResult
import org.oxycblt.musikr.cache.CachedFile
import org.oxycblt.musikr.cache.IncrementalCache
import org.oxycblt.musikr.covers.CoverResult
import org.oxycblt.musikr.fs.FS
import org.oxycblt.musikr.fs.File
import org.oxycblt.musikr.fs.RootGate
import org.oxycblt.musikr.fs.SourceAwareFS
import org.oxycblt.musikr.library.MetadataProfile
import org.oxycblt.musikr.pipeline.shim.FilteredFS
import org.oxycblt.musikr.util.mapParallel
import org.oxycblt.musikr.util.merge
import org.oxycblt.musikr.util.tryAsync
import org.oxycblt.musikr.util.tryAsyncWith

internal interface ExploreStep {
    suspend fun explore(scope: CoroutineScope, explored: Channel<Explored>): Deferred<Result<Unit>>

    companion object {
        fun from(
            config: Config,
            noisyDirs: Set<String> = emptySet(),
            pathKeywords: List<String> = emptyList(),
            rootGate: RootGate? = null,
        ): ExploreStep =
            ExploreStepImpl(
                config.fs,
                config.storage,
                config.metadataProfile,
                config.scanPlan?.reuseSourceKeys.orEmpty(),
                noisyDirs,
                pathKeywords,
                rootGate,
                config.indexingWorkerCount,
            )
    }
}

private class ExploreStepImpl(
    private val fs: FS,
    private val storage: Storage,
    private val metadataProfile: MetadataProfile,
    private val reuseSourceKeys: Set<String>,
    private val noisyDirs: Set<String>,
    private val pathKeywords: List<String>,
    rootGate: RootGate?,
    workerCount: Int,
) : ExploreStep {
    private val parallelism = workerCount.coerceAtLeast(1)

    override suspend fun explore(
        scope: CoroutineScope,
        explored: Channel<Explored>,
    ): Deferred<Result<Unit>> {
        val filteredFs =
            if (noisyDirs.isNotEmpty() || pathKeywords.isNotEmpty()) {
                FilteredFS(fs, scope, noisyDirs, pathKeywords)
            } else {
                fs
            }
        val files = Channel<File>(PipelinePolicy.BUFFER_CAPACITY)
        val filesTask = filteredFs.explore(files)
        val sourceFailureTask =
            scope.tryAsync(Dispatchers.IO) {
                filesTask.await().getOrThrow()
                val incremental = storage.cache as? IncrementalCache
                val failures = (fs as? SourceAwareFS)?.drainSourceFailures().orEmpty()
                for ((sourceKey, detail) in failures) {
                    incremental?.markSourceFailed(sourceKey, detail)
                }
            }

        val classified = Channel<Classified>(PipelinePolicy.BUFFER_CAPACITY)
        val classifiedTask =
            scope.mapParallel(parallelism, files, classified, Dispatchers.IO) { file ->
                if (!FileClassification.isPotentialMusicFile(file)) {
                    return@mapParallel Finalized(NotAudio)
                }
                when (val cacheResult = storage.cache.read(file)) {
                    is CacheResult.Hit -> NeedsHydration(cacheResult.file)
                    is CacheResult.Stale -> Finalized(NewSong(cacheResult.file))
                    is CacheResult.Miss -> Finalized(NewSong(cacheResult.file))
                }
            }

        val finalized = Channel<Finalized>(PipelinePolicy.BUFFER_CAPACITY)
        val exploredTask =
            scope.mapParallel(parallelism, classified, finalized, Dispatchers.IO) { item ->
                when (item) {
                    is Finalized -> item
                    is NeedsHydration -> Finalized(item.cachedFile.toExplored())
                }
            }
        val playlists = Channel<Explored>(PipelinePolicy.BUFFER_CAPACITY)
        val playlistsTask =
            scope.tryAsyncWith(playlists, Dispatchers.IO) {
                for (playlist in storage.storedPlaylists.read()) it.send(RawPlaylist(playlist))
            }

        val mergeTask =
            scope.tryAsyncWith(explored, Dispatchers.Default) {
                for (item in finalized) it.send(item.explored)

                // Unchanged sources never touch their provider or metadata extractor. Their
                // complete committed cache rows are streamed into the compatibility graph in
                // bounded pages.
                val incremental = storage.cache as? IncrementalCache
                if (incremental != null && reuseSourceKeys.isNotEmpty()) {
                    incremental.reusedCachedFiles(reuseSourceKeys).collect { cached ->
                        it.send(cached.toExplored())
                    }
                }
                for (playlist in playlists) it.send(playlist)
            }

        return scope.merge(
            filesTask,
            sourceFailureTask,
            classifiedTask,
            exploredTask,
            playlistsTask,
            mergeTask,
        )
    }

    private suspend fun CachedFile.toExplored(): Explored {
        val audio = audio ?: return NotAudio
        val cover =
            if (metadataProfile == MetadataProfile.FULL) {
                when (val result = audio.coverId?.let { id -> storage.covers.obtain(id) }) {
                    is CoverResult.Hit -> result.cover
                    else -> null
                }
            } else {
                null
            }
        return RawSong(file, audio.properties, audio.tags, cover, addedMs)
    }

    private sealed interface Classified

    private data class NeedsHydration(val cachedFile: CachedFile) : Classified

    private data class Finalized(val explored: Explored) : Classified
}

internal object FileClassification {
    private val supportedAudioExtensions =
        setOf(
            "3gp",
            "aac",
            "alac",
            "amr",
            "ape",
            "dsf",
            "flac",
            "m4a",
            "m4b",
            "mid",
            "midi",
            "mka",
            "mp3",
            "mp4",
            "ogg",
            "opus",
            "wav",
            "wma",
        )

    private val playlistExtensions = setOf("m3u", "m3u8")
    private val playlistMimeTypes =
        setOf(
            "application/vnd.apple.mpegurl",
            "application/x-mpegurl",
            "audio/mpegurl",
            "audio/x-mpegurl",
        )
    private val applicationAudioMimeTypes =
        setOf(
            "application/flac",
            "application/ogg",
            "application/x-flac",
            "application/x-ogg",
        )
    private val genericMimeTypes = setOf("application/octet-stream", "binary/octet-stream")

    fun isPotentialMusicFile(file: File): Boolean =
        isPotentialMusicFileNameMime(file.uri.lastPathSegment.orEmpty(), file.mimeType)

    fun isPotentialMusicFileNameMime(fileName: String, mimeType: String?): Boolean {
        val extension =
            fileName.substringAfterLast('.', missingDelimiterValue = "").lowercase(Locale.ROOT)
        if (extension in playlistExtensions) return false

        val normalizedMime = mimeType?.trim()?.lowercase(Locale.ROOT).orEmpty()
        if (normalizedMime in playlistMimeTypes) return false
        if (normalizedMime.startsWith("audio/")) return true
        if (normalizedMime in applicationAudioMimeTypes) return true

        return (normalizedMime.isEmpty() || normalizedMime in genericMimeTypes) &&
            extension in supportedAudioExtensions
    }
}
