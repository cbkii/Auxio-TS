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
            roots.map { location ->
                val path =
                    location.uri.path?.takeIf { location.uri.scheme == "file" }
                        ?: return@map CompletableDeferred(Result.success(Unit))
                exploreDirectoryImpl(JavaFile(path), location.path, null, files)
            }.tryAwaitAll()
        }
    }

    override fun track(): Flow<FSUpdate> = emptyFlow()

    private fun CoroutineScope.exploreDirectoryImpl(
        directory: JavaFile,
        relativePath: Path,
        parent: Deferred<Directory>?,
        files: Channel<File>,
    ): Deferred<Result<Unit>> =
        tryAsync(Dispatchers.IO) {
            val directoryDeferred = CompletableDeferred<Directory>()
            val children = mutableListOf<File>()
            val recursive = mutableListOf<Deferred<Result<Unit>>>()
            val list = listFilesSafe(directory)
            for (item in list) {
                if (item.name.startsWith(".")) continue
                val newPath = relativePath.file(item.name)
                if (item.isDirectory)
                    recursive.add(exploreDirectoryImpl(item, newPath, directoryDeferred, files))
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
        return directory.listFiles()?.toList()
            ?: rootGate?.runRootCommandSync("ls -1 \"${directory.absolutePath}\"")?.map {
                JavaFile(directory, it)
            }
            ?: emptyList()
    }

    private fun getMimeType(file: JavaFile): String {
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension.lowercase())
            ?: "application/octet-stream"
    }
}
