/*
 * Copyright (c) 2026 Auxio Project
 * DirectFS.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.oxycblt.musikr.fs.direct

import android.net.Uri
import java.io.File as JavaFile
import java.net.URLConnection
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.oxycblt.musikr.fs.AddedMs
import org.oxycblt.musikr.fs.Directory
import org.oxycblt.musikr.fs.FS
import org.oxycblt.musikr.fs.FSUpdate
import org.oxycblt.musikr.fs.File
import org.oxycblt.musikr.fs.Location
import org.oxycblt.musikr.fs.Path
import org.oxycblt.musikr.util.tryAsyncWith

/** Direct java.io.File-backed source for OEM firmwares whose picker/DocumentsUI is unreliable. */
class DirectFS private constructor(private val query: Query) : FS {
    override suspend fun explore(files: Channel<File>): Deferred<Result<Unit>> = coroutineScope {
        tryAsyncWith(files, Dispatchers.IO) {
            query.source.forEach { location ->
                val root = JavaFile(location.uri.path ?: return@forEach)
                val rootState = DirectPathState.from(root)
                if (rootState != DirectPathState.ACCESSIBLE) {
                    throw DirectPathUnavailableException(root.absolutePath, rootState)
                }
                exploreDirectory(
                    root,
                    root.absolutePath,
                    location.path,
                    null,
                    query.exclude.mapTo(mutableSetOf()) { it.path },
                    files,
                )
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun track(): Flow<FSUpdate> = callbackFlow {
        // Android provides no normal-app, portable direct-path watcher for removable TS18 media.
        // Preserve correctness by relying on explicit rescans and mount/accessibility checks.
        awaitClose {}
    }

    private suspend fun exploreDirectory(
        directory: JavaFile,
        rootPath: String,
        path: Path,
        parent: Deferred<Directory>?,
        exclude: Set<Path>,
        files: Channel<File>,
    ) {
        if (path in exclude) return
        if (!query.withHidden && directory.name.startsWith(".")) return
        val directoryDeferred = CompletableDeferred<Directory>()
        val children = mutableListOf<File>()
        val listed =
            try {
                directory.listFiles()
            } catch (e: SecurityException) {
                throw DirectPathUnavailableException(
                    directory.absolutePath,
                    DirectPathState.INACCESSIBLE,
                    e,
                )
            } catch (e: RuntimeException) {
                throw DirectPathUnavailableException(
                    directory.absolutePath,
                    DirectPathState.INACCESSIBLE,
                    e,
                )
            }
                ?: throw DirectPathUnavailableException(
                    directory.absolutePath,
                    DirectPathState.INACCESSIBLE,
                )

        for (child in listed) {
            if (!query.withHidden && child.name.startsWith(".")) continue
            val childPath = path.file(child.name)
            if (childPath in exclude) continue
            if (child.isDirectory) {
                val isRootChild = directory.absolutePath == rootPath
                if (!isRootChild && !query.pathFilter.shouldDescend(child.absolutePath)) continue
                exploreDirectory(child, rootPath, childPath, directoryDeferred, exclude, files)
            } else if (child.isFile && query.pathFilter.shouldInclude(child.absolutePath)) {
                val file =
                    File(
                        uri = Uri.fromFile(child),
                        path = childPath,
                        addedMs = DirectAddedMs(child.lastModified()),
                        modifiedMs = child.lastModified(),
                        mimeType = URLConnection.guessContentTypeFromName(child.name) ?: "audio/*",
                        size = child.length(),
                        parent = directoryDeferred,
                    )
                children.add(file)
                files.send(file)
            }
        }
        directoryDeferred.complete(Directory(Uri.fromFile(directory), path, parent, children))
    }

    data class Query(
        val source: List<Location.Opened>,
        val exclude: List<Location.Unopened>,
        val withHidden: Boolean,
        val pathFilter: DirectPathFilter = DirectPathFilter.disabled(),
    )

    private class DirectAddedMs(private val modifiedMs: Long) : AddedMs {
        override suspend fun resolve(): Long = modifiedMs
    }

    companion object {
        fun from(query: Query) = DirectFS(query)
    }
}

data class DirectPathFilter(val enabled: Boolean, val keywords: Set<String>) {
    fun shouldDescend(path: String): Boolean =
        !enabled || isAllowedPath(path) || mayContainAllowedChild(path)

    fun shouldInclude(path: String): Boolean = !enabled || isAllowedPath(path)

    fun isAllowedPath(path: String): Boolean {
        if (!enabled) return true
        val normalized = path.lowercase()
        return keywords.any { normalized.contains(it) }
    }

    private fun mayContainAllowedChild(path: String): Boolean {
        val name = JavaFile(path).name.lowercase()
        return name in VOLUME_OR_STORAGE_ROOT_NAMES
    }

    companion object {
        private val DEFAULT_KEYWORDS = setOf("music", "download", "media")
        private val VOLUME_OR_STORAGE_ROOT_NAMES =
            setOf(
                "0",
                "sdcard",
                "emulated",
                "storage",
                "usbdisk0",
                "usbhost",
                "extsd",
                "extsd0",
                "extsd1",
                "extsd2",
            )

        fun ts18Default() = DirectPathFilter(enabled = true, keywords = DEFAULT_KEYWORDS)

        fun disabled() = DirectPathFilter(enabled = false, keywords = DEFAULT_KEYWORDS)
    }
}

enum class DirectPathState {
    ACCESSIBLE,
    MISSING,
    NOT_DIRECTORY,
    INACCESSIBLE;

    companion object {
        fun from(file: JavaFile): DirectPathState =
            try {
                when {
                    !file.exists() -> MISSING
                    !file.isDirectory -> NOT_DIRECTORY
                    !file.canRead() -> INACCESSIBLE
                    else -> ACCESSIBLE
                }
            } catch (e: SecurityException) {
                INACCESSIBLE
            } catch (e: RuntimeException) {
                INACCESSIBLE
            }
    }
}

class DirectPathUnavailableException(
    val path: String,
    val state: DirectPathState,
    cause: Throwable? = null,
) : Exception("Direct path $path is $state", cause)
