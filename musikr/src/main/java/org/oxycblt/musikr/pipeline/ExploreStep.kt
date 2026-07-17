/*
 * Copyright (c) 2024 Auxio Project
 * ExploreStep.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
import org.oxycblt.musikr.library.MetadataProfile
import org.oxycblt.musikr.pipeline.shim.FilteredFS
import org.oxycblt.musikr.playlist.m3u.M3U
import org.oxycblt.musikr.util.mapParallel
import org.oxycblt.musikr.util.merge
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
                config.scanPlan?.let { it.reuseSourceKeys + it.unavailableSourceKeys }.orEmpty(),
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

                // Unchanged sources never touch their provider or metadata extractor. Their complete
                // committed cache rows are streamed into the compatibility graph in bounded pages.
                val incremental = storage.cache as? IncrementalCache
                if (incremental != null && reuseSourceKeys.isNotEmpty()) {
                    incremental.reusedCachedFiles(reuseSourceKeys).collect { cached ->
                        it.send(cached.toExplored())
                    }
                }
                for (playlist in playlists) it.send(playlist)
            }

        return scope.merge(filesTask, classifiedTask, exploredTask, playlistsTask, mergeTask)
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
            "flac",
            "m4a",
            "m4b",
            "m4p",
            "mp3",
            "mp4",
            "oga",
            "ogg",
            "opus",
            "wav",
        )

    fun isPotentialMusicFile(file: File): Boolean {
        val name = file.path.name ?: file.uri.lastPathSegment?.substringAfterLast('/')
        return isPotentialMusicFileNameMime(name, file.mimeType)
    }

    fun isPotentialMusicFileNameMime(name: String?, mimeType: String?): Boolean {
        val normalisedMimeType = mimeType?.lowercase(Locale.US).orEmpty()
        if (normalisedMimeType == M3U.MIME_TYPE) return false
        if (normalisedMimeType.startsWith("audio/")) return true
        if (normalisedMimeType == "application/ogg" || normalisedMimeType == "application/x-ogg") {
            return true
        }
        if (normalisedMimeType != "application/octet-stream" && normalisedMimeType.isNotEmpty()) {
            return false
        }

        val extension =
            name
                ?.substringAfterLast('.', missingDelimiterValue = "")
                ?.lowercase(Locale.US)
                .orEmpty()
        return extension in supportedAudioExtensions
    }
}
