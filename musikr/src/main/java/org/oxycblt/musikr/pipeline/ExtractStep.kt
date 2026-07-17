/*
 * Copyright (c) 2024 Auxio Project
 * ExtractStep.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
import org.oxycblt.musikr.cache.IncrementalCache
import org.oxycblt.musikr.cache.MutableCache
import org.oxycblt.musikr.covers.Cover
import org.oxycblt.musikr.covers.CoverResult
import org.oxycblt.musikr.covers.MutableCovers
import org.oxycblt.musikr.library.MetadataWorkPolicy
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
                MetadataExtractor.from(context, config.metadataProfile),
                TagParser.new(config.metadataProfile, config.dimensionPolicy),
                config.storage.cache,
                config.storage.covers,
                MetadataWorkPolicy.forProfile(config.metadataProfile).extractArtwork &&
                    config.artworkPolicy == org.oxycblt.musikr.library.ArtworkPolicy.FULL_INDEXING,
                config.indexingWorkerCount,
            )
    }
}

private class ExtractStepImpl(
    private val metadataExtractor: MetadataExtractor,
    private val tagParser: TagParser,
    private val cache: MutableCache,
    private val covers: MutableCovers<out Cover>,
    private val extractArtwork: Boolean,
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
                            if (extractArtwork) {
                                when (val result =
                                    covers.create(item.newSong.file, item.metadata)) {
                                    is CoverResult.Hit -> result.cover
                                    else -> null
                                }
                            } else {
                                null
                            }
                        NeedsCaching(
                            RawSong(
                                item.newSong.file,
                                item.metadata.properties,
                                tags,
                                cover,
                                item.newSong.file.addedMs.resolve() ?: addingMs,
                            )
                        )
                    }
                }
            }
        val finalizedTask =
            scope.tryAsyncWith(extracted, Dispatchers.IO) {
                // Legacy caches still require the complete exclusion list. Incremental caches record
                // every discovered row directly and reconcile missing rows in SQL at commit time.
                val legacyExclude =
                    mutableListOf<CachedFile>().takeUnless { cache is IncrementalCache }
                for (item in parsed) {
                    val result =
                        when (item) {
                            is Finalized -> {
                                if (item.extracted is RawSong) {
                                    legacyExclude?.add(item.extracted.toCachedFile())
                                }
                                item
                            }
                            is NeedsCaching -> {
                                val cachedFile = item.rawSong.toCachedFile()
                                cache.write(cachedFile)
                                legacyExclude?.add(cachedFile)
                                Finalized(item.rawSong)
                            }
                        }
                    it.send(result.extracted)
                }
                legacyExclude?.let { cache.cleanup(it) }
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
