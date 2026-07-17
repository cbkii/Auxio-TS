/*
 * Copyright (c) 2024 Auxio Project
 * MetadataExtractor.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.oxycblt.musikr.metadata

import android.content.ContentResolver
import android.content.Context
import android.media.MediaMetadataRetriever
import java.io.FileInputStream
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.oxycblt.musikr.fs.File
import org.oxycblt.musikr.library.MetadataProfile

internal interface MetadataExtractor {
    suspend fun extract(deviceFile: File): MetadataResult

    companion object {
        fun from(context: Context, profile: MetadataProfile = MetadataProfile.FULL): MetadataExtractor =
            when (profile) {
                MetadataProfile.LEAN -> LeanMetadataExtractor(context.contentResolver)
                MetadataProfile.FULL -> TagLibMetadataExtractor(context.contentResolver)
            }
    }
}

sealed interface MetadataResult {
    data class Success(val metadata: Metadata?) : MetadataResult

    data object NoMetadata : MetadataResult

    data object NotAudio : MetadataResult

    data object ProviderFailed : MetadataResult
}

/** Full TagLib path used only by restart-safe enrichment. */
private class TagLibMetadataExtractor(private val contentResolver: ContentResolver) :
    MetadataExtractor {
    override suspend fun extract(deviceFile: File): MetadataResult =
        withContext(Dispatchers.IO) {
            contentResolver.openFileDescriptor(deviceFile.uri, "r")?.use { fd ->
                val fis = FileInputStream(fd.fileDescriptor)
                TagLibJNI.open(deviceFile, fis).also { fis.close() }
            } ?: MetadataResult.ProviderFailed
        }
}

/**
 * Platform-only first pass that avoids TagLib relationship maps and embedded-art extraction.
 *
 * MediaMetadataRetriever supplies only the fields needed for immediate playback and deterministic
 * first-page browsing. Rich tags are deliberately left for the Full enrichment generation.
 */
private class LeanMetadataExtractor(private val contentResolver: ContentResolver) :
    MetadataExtractor {
    override suspend fun extract(deviceFile: File): MetadataResult =
        withContext(Dispatchers.IO) {
            val descriptor =
                try {
                    contentResolver.openAssetFileDescriptor(deviceFile.uri, "r")
                } catch (e: CancellationException) {
                    throw e
                } catch (_: Exception) {
                    null
                } ?: return@withContext MetadataResult.ProviderFailed

            descriptor.use { afd ->
                val retriever = MediaMetadataRetriever()
                try {
                    if (afd.length >= 0L) {
                        retriever.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                    } else {
                        retriever.setDataSource(afd.fileDescriptor)
                    }
                    val hasAudio =
                        retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_AUDIO)
                    if (hasAudio != null && !hasAudio.equals("yes", ignoreCase = true)) {
                        return@withContext MetadataResult.NotAudio
                    }

                    val tags = linkedMapOf<String, List<String>>()
                    fun add(key: String, metadataKey: Int) {
                        retriever
                            .extractMetadata(metadataKey)
                            ?.trim()
                            ?.takeIf { it.isNotEmpty() }
                            ?.let { tags[key] = listOf(it) }
                    }
                    add("TIT2", MediaMetadataRetriever.METADATA_KEY_TITLE)
                    add("TPE1", MediaMetadataRetriever.METADATA_KEY_ARTIST)
                    add("TPE2", MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST)
                    add("TALB", MediaMetadataRetriever.METADATA_KEY_ALBUM)
                    add("TRCK", MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER)
                    add("TPOS", MediaMetadataRetriever.METADATA_KEY_DISC_NUMBER)

                    val durationMs =
                        retriever
                            .extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                            ?.toLongOrNull()
                            ?.coerceAtLeast(0L) ?: 0L
                    val bitrateKbps =
                        retriever
                            .extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE)
                            ?.toIntOrNull()
                            ?.div(1000)
                            ?.coerceAtLeast(0) ?: 0
                    val mimeType =
                        retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE)
                            ?: deviceFile.mimeType
                    MetadataResult.Success(
                        Metadata(
                            id3v2 = tags,
                            xiph = emptyMap(),
                            mp4 = emptyMap(),
                            cover = null,
                            properties =
                                Properties(
                                    mimeType = mimeType,
                                    durationMs = durationMs,
                                    bitrateKbps = bitrateKbps,
                                    sampleRateHz = 0,
                                ),
                        )
                    )
                } catch (e: CancellationException) {
                    throw e
                } catch (_: RuntimeException) {
                    MetadataResult.NoMetadata
                } finally {
                    retriever.release()
                }
            }
        }
}
