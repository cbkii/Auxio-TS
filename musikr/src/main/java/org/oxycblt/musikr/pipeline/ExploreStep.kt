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

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import org.oxycblt.musikr.Config
import org.oxycblt.musikr.Storage
import org.oxycblt.musikr.cache.CacheResult
import org.oxycblt.musikr.cache.CachedFile
import org.oxycblt.musikr.covers.CoverResult
import org.oxycblt.musikr.fs.FS
import org.oxycblt.musikr.fs.File
import org.oxycblt.musikr.fs.RootGate
import java.util.Locale
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
            ExploreStepImpl(config.fs, config.storage, noisyDirs, pathKeywords, rootGate)
    }
}

private class ExploreStepImpl(
    private val fs: FS,
    private val storage: Storage,
    private val noisyDirs: Set<String>,
    private val pathKeywords: List<String>,
    rootGate: RootGate?,
) : ExploreStep {
    override suspend fun explore(
        scope: CoroutineScope,
        explored: Channel<Explored>,
    ): Deferred<Result<Unit>> {
        val filteredFs =
            if (noisyDirs.isNotEmpty() || pathKeywords.isNotEmpty())
                FilteredFS(fs, scope, noisyDirs, pathKeywords)
            else fs
        val files = Channel<File>(Channel.UNLIMITED)
        val filesTask = filteredFs.explore(files)

        val classified = Channel<Classified>(Channel.UNLIMITED)
        val classifiedTask =
            scope.mapParallel(PARALLELISM, files, classified, Dispatchers.IO) { file ->
                if (!FileClassification.isPotentialMusicFile(file)) {
                    return@mapParallel Finalized(NotAudio)
                }
                when (val cacheResult = storage.cache.read(file)) {
                    is CacheResult.Hit -> NeedsHydration(cacheResult.file)
                    is CacheResult.Stale -> Finalized(NewSong(cacheResult.file))
                    is CacheResult.Miss -> Finalized(NewSong(cacheResult.file))
                }
            }

        val finalized = Channel<Finalized>(Channel.UNLIMITED)
        val exploredTask =
            scope.mapParallel(PARALLELISM, classified, finalized, Dispatchers.IO) { item ->
                when (item) {
                    is Finalized -> item
                    is NeedsHydration -> {
                        val audio = item.cachedFile.audio ?: return@mapParallel Finalized(NotAudio)
                        val coverId =
                            when (
                                val result = audio.coverId?.let { id -> storage.covers.obtain(id) }
                            ) {
                                is CoverResult.Hit -> result.cover
                                is CoverResult.Miss ->
                                    return@mapParallel Finalized(NewSong(item.cachedFile.file))
                                null -> null
                            }

                        Finalized(
                            RawSong(
                                item.cachedFile.file,
                                audio.properties,
                                audio.tags,
                                coverId,
                                item.cachedFile.addedMs,
                            )
                        )
                    }
                }
            }
        val playlists = Channel<Explored>(Channel.UNLIMITED)
        val playlistsTask =
            scope.tryAsyncWith(playlists, Dispatchers.IO) {
                for (playlist in storage.storedPlaylists.read()) {
                    val rawPlaylist = RawPlaylist(playlist)
                    it.send(rawPlaylist)
                }
            }

        val mergeTask =
            scope.tryAsyncWith(explored, Dispatchers.Main) {
                for (item in finalized) {
                    it.send(item.explored)
                }
                for (playlist in playlists) {
                    it.send(playlist)
                }
            }

        return scope.merge(filesTask, classifiedTask, exploredTask, playlistsTask, mergeTask)
    }

    private sealed interface Classified

    private data class NeedsHydration(val cachedFile: CachedFile) : Classified

    private data class Finalized(val explored: Explored) : Classified

    private companion object {
        const val PARALLELISM = 8
    }
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

    fun isPotentialMusicFileNameMime(name: String?, mimeType: String): Boolean {
        val normalisedMimeType = mimeType.lowercase(Locale.US)
        if (normalisedMimeType == M3U.MIME_TYPE) return false
        if (normalisedMimeType.startsWith("audio/")) return true
        if (normalisedMimeType == "application/ogg" || normalisedMimeType == "application/x-ogg") {
            return true
        }
        if (normalisedMimeType != "application/octet-stream") return false

        val extension =
            name
                ?.substringAfterLast('.', missingDelimiterValue = "")
                ?.lowercase(Locale.US)
                .orEmpty()
        return extension in supportedAudioExtensions
    }
}
