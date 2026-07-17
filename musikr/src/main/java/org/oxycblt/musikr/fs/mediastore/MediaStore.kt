/*
 * Copyright (c) 2025 Auxio Project
 * MediaStore.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.oxycblt.musikr.fs.mediastore

import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore as AOSPMediaStore
import androidx.core.database.getStringOrNull
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import org.oxycblt.musikr.fs.AddedMs
import org.oxycblt.musikr.fs.FS
import org.oxycblt.musikr.fs.FSUpdate
import org.oxycblt.musikr.fs.File
import org.oxycblt.musikr.fs.Location
import org.oxycblt.musikr.fs.SourceAwareFS
import org.oxycblt.musikr.fs.SourceFingerprintStrength
import org.oxycblt.musikr.fs.SourceIdentity
import org.oxycblt.musikr.fs.SourceSnapshot
import org.oxycblt.musikr.fs.StoragePathAliasPolicy
import org.oxycblt.musikr.fs.Volume
import org.oxycblt.musikr.fs.path.MediaStorePathInterpreter
import org.oxycblt.musikr.fs.path.VolumeManager
import org.oxycblt.musikr.fs.saf.contentResolverSafe
import org.oxycblt.musikr.fs.saf.useQuery
import org.oxycblt.musikr.fs.track.LocationObserver
import org.oxycblt.musikr.util.tryAsyncWith

/** MediaStore adapter with source-scoped, cheap invalidation planning. */
class MediaStore
private constructor(
    private val context: Context,
    private val volumeManager: VolumeManager,
    private val query: Query,
    private val selectedSourceKeys: Set<String>? = null,
) : SourceAwareFS {
    private val pathInterpreterFactory = MediaStorePathInterpreter.Factory.from(volumeManager)

    override suspend fun sourceSnapshots(): List<SourceSnapshot> =
        withContext(Dispatchers.IO) {
            val volumes = recognizedVolumes()
            volumeNames().mapNotNull { volumeName ->
                val volume = volumes.firstOrNull { it.mediaStoreName == volumeName }
                val sourceKey = volume?.let(SourceIdentity::forVolume) ?: "media-store:$volumeName"
                if (selectedSourceKeys != null && sourceKey !in selectedSourceKeys) {
                    return@mapNotNull null
                }
                val uri = contentUri(volumeName)
                try {
                    val version =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            AOSPMediaStore.getVersion(context, volumeName)
                        } else {
                            AOSPMediaStore.getVersion(context)
                        }
                    SourceSnapshot(
                        sourceKey = sourceKey,
                        sourceType = SOURCE_TYPE,
                        rootUri = uri.toString(),
                        rootPath = volume?.components?.unixString?.let { "/$it" },
                        available = version != null && volume?.isAccessible() != false,
                        fingerprint = version?.let { "$it:${query.hashCode()}" },
                        // Android 10 exposes an opaque volume version but not per-row generation
                        // counters. ContentObserver invalidations cover live changes and the ledger
                        // periodically refreshes advisory snapshots after process death.
                        fingerprintStrength =
                            if (version != null) SourceFingerprintStrength.ADVISORY
                            else SourceFingerprintStrength.NONE,
                    )
                } catch (e: Exception) {
                    android.util.Log.w(
                        TAG,
                        "Unable to read MediaStore version for volume $volumeName",
                        e,
                    )
                    SourceSnapshot(
                        sourceKey = sourceKey,
                        sourceType = SOURCE_TYPE,
                        rootUri = uri.toString(),
                        rootPath = volume?.components?.unixString?.let { "/$it" },
                        available = false,
                        fingerprint = null,
                        fingerprintStrength = SourceFingerprintStrength.NONE,
                    )
                }
            }
        }

    override fun selectSources(sourceKeys: Set<String>): FS =
        MediaStore(context, volumeManager, query, sourceKeys)

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun explore(files: Channel<File>): Deferred<Result<Unit>> = coroutineScope {
        tryAsyncWith(files, Dispatchers.IO) {
            val projection = BASE_PROJECTION + pathInterpreterFactory.projection
            val (selector, args) = buildSelector()
            val seenIdentities = mutableSetOf<String>()
            val volumes = recognizedVolumes()
            var anyVolumeSucceeded = false
            var lastVolumeError: Exception? = null

            for (volumeName in volumeNames()) {
                val volume = volumes.firstOrNull { it.mediaStoreName == volumeName }
                val sourceKey = volume?.let(SourceIdentity::forVolume) ?: "media-store:$volumeName"
                if (selectedSourceKeys != null && sourceKey !in selectedSourceKeys) continue
                val contentUri = contentUri(volumeName)
                try {
                    context.contentResolverSafe.useQuery(contentUri, projection, selector, args) {
                        cursor ->
                        val pathInterpreter = pathInterpreterFactory.wrap(cursor)
                        val idIndex =
                            cursor.getColumnIndexOrThrow(AOSPMediaStore.Audio.AudioColumns._ID)
                        val mimeTypeIndex =
                            cursor.getColumnIndexOrThrow(
                                AOSPMediaStore.Audio.AudioColumns.MIME_TYPE
                            )
                        val sizeIndex =
                            cursor.getColumnIndexOrThrow(AOSPMediaStore.Audio.AudioColumns.SIZE)
                        val dateAddedIndex =
                            cursor.getColumnIndexOrThrow(
                                AOSPMediaStore.Audio.AudioColumns.DATE_ADDED
                            )
                        val dateModifiedIndex =
                            cursor.getColumnIndexOrThrow(
                                AOSPMediaStore.Audio.AudioColumns.DATE_MODIFIED
                            )
                        while (cursor.moveToNext()) {
                            val path = pathInterpreter.extract() ?: continue
                            val id = cursor.getLong(idIndex)
                            val uri = Uri.withAppendedPath(contentUri, id.toString())
                            val mimeType = cursor.getStringOrNull(mimeTypeIndex) ?: "audio/*"
                            val size = cursor.getLong(sizeIndex)
                            val dateAdded = cursor.getLong(dateAddedIndex) * 1000
                            val dateModified = cursor.getLong(dateModifiedIndex) * 1000
                            val volumeComponents = path.volume.components
                            val pathIdentity =
                                if (volumeComponents != null) {
                                    StoragePathAliasPolicy.normalize(
                                        "/${volumeComponents.unixString}/${path.components.unixString}"
                                    )
                                } else {
                                    "${path.volume}/${path.components.unixString}"
                                }
                            if (!seenIdentities.add("${pathIdentity}_$size")) continue
                            it.send(
                                File(
                                    uri = uri,
                                    path = path,
                                    modifiedMs = dateModified,
                                    mimeType = mimeType,
                                    size = size,
                                    addedMs = ForwardDateAdded(dateAdded),
                                    parent = null,
                                )
                            )
                        }
                    }
                    anyVolumeSucceeded = true
                } catch (e: Exception) {
                    lastVolumeError = e
                    android.util.Log.e(TAG, "Failed to query volume: $volumeName", e)
                }
            }
            if (!anyVolumeSucceeded) lastVolumeError?.let { throw it }
        }
    }

    override fun track(): Flow<FSUpdate> = callbackFlow {
        val observer =
            LocationObserver(context, AOSPMediaStore.Audio.Media.EXTERNAL_CONTENT_URI) {
                trySend(FSUpdate.LocationChanged(null))
            }
        awaitClose { observer.release() }
    }

    private fun buildSelector(): Pair<String, Array<String>> {
        var selector = BASE_SELECTOR
        val args = mutableListOf<String>()
        if (query.excludeNonMusic) {
            selector += " AND ${AOSPMediaStore.Audio.AudioColumns.IS_MUSIC}=1"
        }
        if (query.useDefaultSystemFilter) {
            selector +=
                " AND (" +
                    "${AOSPMediaStore.Audio.AudioColumns.DATA} LIKE '%music%' OR " +
                    "${AOSPMediaStore.Audio.AudioColumns.DATA} LIKE '%download%' OR " +
                    "${AOSPMediaStore.Audio.AudioColumns.DATA} LIKE '%media%')"
        }
        when (query.mode) {
            FilterMode.INCLUDE -> {
                pathInterpreterFactory.createSelector(query.filtered.map { it.path })?.let {
                    selector += " AND (${it.template})"
                    args.addAll(it.args)
                }
            }
            FilterMode.EXCLUDE -> {
                pathInterpreterFactory.createSelector(query.filtered.map { it.path })?.let {
                    selector += " AND NOT (${it.template})"
                    args.addAll(it.args)
                }
            }
        }
        return selector to args.toTypedArray()
    }

    private fun recognizedVolumes(): List<Volume> =
        (volumeManager.getVolumes() + volumeManager.getInternalVolume()).distinct()

    private fun volumeNames(): Set<String> {
        val names = linkedSetOf<String>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            runCatching { names.addAll(AOSPMediaStore.getExternalVolumeNames(context)) }
                .onFailure { android.util.Log.e(TAG, "Failed to enumerate external volumes", it) }
        }
        if (names.isEmpty()) names += AOSPMediaStore.VOLUME_EXTERNAL
        return names
    }

    private fun contentUri(volumeName: String): Uri =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            runCatching { AOSPMediaStore.Audio.Media.getContentUri(volumeName) }
                .getOrDefault(AOSPMediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
        } else {
            AOSPMediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }

    data class Query(
        val mode: FilterMode,
        val filtered: List<Location.Unopened>,
        val excludeNonMusic: Boolean,
        val useDefaultSystemFilter: Boolean = false,
    )

    enum class FilterMode {
        INCLUDE,
        EXCLUDE,
    }

    private class ForwardDateAdded(val dateAdded: Long) : AddedMs {
        override suspend fun resolve() = dateAdded
    }

    companion object {
        private const val TAG = "MediaStore"
        private const val SOURCE_TYPE = "MEDIA_STORE"

        fun from(context: Context, query: Query) =
            MediaStore(context, VolumeManager.from(context), query)

        private const val BASE_SELECTOR = "NOT ${AOSPMediaStore.Audio.Media.SIZE}=0"

        private val BASE_PROJECTION =
            arrayOf(
                AOSPMediaStore.Audio.AudioColumns._ID,
                AOSPMediaStore.Audio.AudioColumns.DATE_ADDED,
                AOSPMediaStore.Audio.AudioColumns.DATE_MODIFIED,
                AOSPMediaStore.Audio.AudioColumns.SIZE,
                AOSPMediaStore.Audio.AudioColumns.MIME_TYPE,
            )
    }
}
