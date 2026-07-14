/*
 * Copyright (c) 2024 Auxio Project
 * ExtractStep.kt is part of Auxio.
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

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import org.oxycblt.musikr.Config
import org.oxycblt.musikr.cache.Audio
import org.oxycblt.musikr.cache.CachedFile
import org.oxycblt.musikr.cache.MutableCache
import org.oxycblt.musikr.covers.Cover
import org.oxycblt.musikr.covers.CoverResult
import org.oxycblt.musikr.covers.MutableCovers
import org.oxycblt.musikr.metadata.Metadata
import org.oxycblt.musikr.metadata.MetadataExtractor
import org.oxycblt.musikr.metadata.MetadataResult
import org.oxycblt.musikr.tag.parse.TagParser
import org.oxycblt.musikr.util.mapParallel
import org.oxycblt.musikr.util.merge
import org.oxycblt.musikr.util.tryAsyncWith

internal interface ExtractStep {
    suspend fun extract(
        scope: CoroutineScope,
        explored: Channel<Explored>,
        extracted: Channel<Extracted>,
    ): Deferred<Result<Unit>>

    companion object {
        fun from(context: Context, config: Config): ExtractStep =
            ExtractStepImpl(
                MetadataExtractor.from(context),
                TagParser.new(),
                config.storage.cache,
                config.storage.covers,
                config.indexingWorkerCount,
            )
    }
}

private class ExtractStepImpl(
    private val metadataExtractor: MetadataExtractor,
    private val tagParser: TagParser,
    private val cache: MutableCache,
    private val covers: MutableCovers<out Cover>,
    workerCount: Int,
) : ExtractStep {
    private val parallelism = workerCount.coerceAtLeast(1)
    override suspend fun extract(
        scope: CoroutineScope,
        explored: Channel<Explored>,
        extracted: Channel<Extracted>,
    ): Deferred<Result<Unit>> {
        val addingMs = System.currentTimeMillis()
        val extract = Channel<ParsedExtractItem>(parallelism)
        val extractTask =
            scope.mapParallel(parallelism, explored, extract, Dispatchers.IO) { item ->
                when (item) {
                    is RawSong -> Finalized(item)
                    is RawPlaylist -> Finalized(item)
                    is NewSong -> {
                        when (val result = metadataExtractor.extract(item.file)) {
                            is MetadataResult.Success ->
                                result.metadata?.let { metadata -> NeedsParsing(item, metadata) }
                                    ?: Finalized(InvalidSong)
                            MetadataResult.NoMetadata -> Finalized(InvalidSong)
                            MetadataResult.NotAudio -> Finalized(NotAudio)
                            MetadataResult.ProviderFailed -> Finalized(InvalidSong)
                        }
                    }
                    is NotAudio -> Finalized(NotAudio)
                }
            }
        val parsed = Channel<ParsedCachingItem>(PipelinePolicy.BUFFER_CAPACITY)
        val parsedTask =
            scope.mapParallel(parallelism, extract, parsed, Dispatchers.IO) { item ->
                when (item) {
                    is Finalized -> item
                    is NeedsParsing -> {
                        val tags = tagParser.parse(item.metadata)
                        val cover =
                            when (val result = covers.create(item.newSong.file, item.metadata)) {
                                is CoverResult.Hit -> result.cover
                                else -> null
                            }
                        NeedsCaching(
                            RawSong(
                                item.newSong.file,
                                item.metadata.properties,
                                tags,
                                cover,
                                // The thing about date added is that it's resolution can
                                // actually be expensive in some modes (ex. saf backend), so
                                // we resolve this by moving date added extraction as an
                                // extraction operation rather than doing the redundant work
                                // during exploration (well, kind of, MediaStore's date
                                // added query is basically free, it's only saf that has
                                // it's slow hacky workaround that we must accommodate
                                // here.)
                                item.newSong.file.addedMs.resolve() ?: addingMs,
                            )
                        )
                    }
                }
            }
        val finalizedTask =
            scope.tryAsyncWith(extracted, Dispatchers.IO) {
                val exclude = mutableListOf<CachedFile>()
                for (item in parsed) {
                    val result =
                        when (item) {
                            is Finalized -> {
                                if (item.extracted is RawSong) {
                                    // Cache-hit songs have no CachedFile instance in scope
                                    // anymore; the conversion is a plain field copy, so
                                    // rebuilding it here is cheap.
                                    exclude.add(item.extracted.toCachedFile())
                                }
                                item
                            }
                            is NeedsCaching -> {
                                // Convert once and reuse for both the cache write and the
                                // cleanup exclude list to avoid duplicate per-song allocations.
                                val cachedFile = item.rawSong.toCachedFile()
                                cache.write(cachedFile)
                                exclude.add(cachedFile)
                                Finalized(item.rawSong)
                            }
                        }
                    it.send(result.extracted)
                }
                cache.cleanup(exclude)
            }

        return scope.merge(extractTask, parsedTask, finalizedTask)
    }

    private sealed interface ParsedExtractItem

    private data class NeedsParsing(val newSong: NewSong, val metadata: Metadata) :
        ParsedExtractItem

    private sealed interface ParsedCachingItem

    private data class NeedsCaching(val rawSong: RawSong) : ParsedCachingItem

    private data class Finalized(val extracted: Extracted) : ParsedExtractItem, ParsedCachingItem

    private fun RawSong.toCachedFile() =
        CachedFile(file, audio = Audio(properties, tags, cover?.id), addedMs)

}
