/*
 * Copyright (c) 2026 Auxio Project
 * DirectFS.kt is part of Auxio.
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

package org.oxycblt.musikr.fs.direct

import android.net.Uri
import android.webkit.MimeTypeMap
import java.io.File as JavaFile
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.oxycblt.musikr.fs.*
import org.oxycblt.musikr.util.*

class DirectFS(private val roots: List<Location.Opened>, private val rootGate: RootGate? = null) :
    FS {
    override suspend fun explore(files: Channel<File>): Deferred<Result<Unit>> = coroutineScope {
        tryAsyncWith(files, Dispatchers.IO) {
            roots
                .map { location ->
                    val root =
                        location.uri.path
                            ?.takeIf { location.uri.scheme == "file" }
                            ?.let(::JavaFile)
                            ?.takeIf(::isAllowedRoot)
                            ?: return@map CompletableDeferred(Result.success(Unit))
                    exploreDirectoryImpl(root, location.path, null, files, 0)
                }
                .tryAwaitAll()
        }
    }

    override fun track(): Flow<FSUpdate> = emptyFlow()

    private fun CoroutineScope.exploreDirectoryImpl(
        directory: JavaFile,
        relativePath: Path,
        parent: Deferred<Directory>?,
        files: Channel<File>,
        depth: Int,
    ): Deferred<Result<Unit>> =
        tryAsync(Dispatchers.IO) {
            require(depth <= MAX_DEPTH) {
                "DirectFS traversal depth exceeded for ${directory.path}"
            }
            require(isAllowedRoot(directory)) {
                "DirectFS rejected protected root ${directory.path}"
            }
            val directoryDeferred = CompletableDeferred<Directory>()
            val children = mutableListOf<File>()
            val recursive = mutableListOf<Deferred<Result<Unit>>>()
            val list = listFilesSafe(directory)
            for (item in list) {
                if (item.name.startsWith(".")) continue
                if (java.nio.file.Files.isSymbolicLink(item.toPath())) continue
                val newPath = relativePath.file(item.name)
                if (item.isDirectory)
                    recursive.add(
                        exploreDirectoryImpl(item, newPath, directoryDeferred, files, depth + 1)
                    )
                else {
                    val file =
                        File(
                            Uri.fromFile(item),
                            newPath,
                            object : AddedMs {
                                override suspend fun resolve() = item.lastModified()
                            },
                            item.lastModified(),
                            getMimeType(item),
                            item.length(),
                            directoryDeferred,
                        )
                    children.add(file)
                    files.send(file)
                }
            }
            directoryDeferred.complete(
                Directory(Uri.fromFile(directory), relativePath, parent, children)
            )
            recursive.tryAwaitAll()
        }

    private fun listFilesSafe(directory: JavaFile): List<JavaFile> {
        val local = directory.listFiles()
        if (local != null) return local.toList()
        val rootList =
            rootGate?.runRootCommandSync("ls -1A ${shellQuote(directory.absolutePath)}")?.map {
                JavaFile(directory, it)
            }
        return rootList
            ?: throw IllegalStateException(
                "DirectFS root is unavailable or inaccessible: ${directory.path}"
            )
    }

    private fun getMimeType(file: JavaFile): String {
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension.lowercase())
            ?: "application/octet-stream"
    }

    internal companion object {
        private const val MAX_DEPTH = 32

        private val protectedRoots =
            listOf("/", "/system", "/vendor", "/data", "/proc", "/sys", "/dev", "/acct", "/config")

        fun shellQuote(value: String): String = "'${value.replace("'", "'\"'\"'")}'"

        fun isAllowedRoot(file: JavaFile): Boolean {
            val canonical =
                try {
                    file.canonicalFile
                } catch (_: Exception) {
                    return false
                }
            val path = canonical.path.trimEnd('/')
            if (path.isBlank()) return false
            if (protectedRoots.any { path == it.trimEnd('/') }) return false
            if (
                path.startsWith("/data/") ||
                    path.startsWith("/system/") ||
                    path.startsWith("/vendor/")
            ) {
                return false
            }
            return path.startsWith("/storage/") || path.startsWith("/mnt/media_rw/")
        }
    }
}
