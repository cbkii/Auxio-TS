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
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.oxycblt.musikr.fs.AddedMs
import org.oxycblt.musikr.fs.Directory
import org.oxycblt.musikr.fs.FS
import org.oxycblt.musikr.fs.FSUpdate
import org.oxycblt.musikr.fs.File
import org.oxycblt.musikr.fs.Location
import org.oxycblt.musikr.fs.Path
import org.oxycblt.musikr.fs.RootGate
import org.oxycblt.musikr.util.tryAsync
import org.oxycblt.musikr.util.tryAsyncWith
import org.oxycblt.musikr.util.tryAwaitAll

/**
 * A direct filesystem [FS] implementation that uses [java.io.File] and optionally [RootGate].
 *
 * This is used as the primary strategy for TS18 head units where SAF/MediaStore may be unreliable
 * or absent, but direct filesystem access (or root-assisted access) is available.
 */
class DirectFS(private val roots: List<Location.Opened>, private val rootGate: RootGate? = null) :
    FS {

    override suspend fun explore(files: Channel<File>): Deferred<Result<Unit>> = coroutineScope {
        tryAsyncWith(files, Dispatchers.IO) {
            roots
                .map { location ->
                    exploreDirectoryImpl(
                        JavaFile(
                            location.uri.path
                                ?: return@map CompletableDeferred(Result.success(Unit))
                        ),
                        location.path,
                        null,
                        files,
                    )
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
    ): Deferred<Result<Unit>> =
        tryAsync(Dispatchers.IO) {
            val directoryDeferred = CompletableDeferred<Directory>()
            val children = mutableListOf<File>()
            val recursive = mutableListOf<Deferred<Result<Unit>>>()

            val list = listFilesSafe(directory).getOrElse { throw it }
            for (item in list) {
                if (item.name.startsWith(".")) continue

                val newPath = relativePath.file(item.name)
                if (item.isDirectory) {
                    recursive.add(
                        exploreDirectoryImpl(item.javaFile, newPath, directoryDeferred, files)
                    )
                } else {
                    val file =
                        File(
                            uri = Uri.fromFile(item.javaFile),
                            path = newPath,
                            addedMs =
                                ConstantAddedMs(
                                    item.modifiedMs
                                ), // Inexact, but best available for direct FS
                            modifiedMs = item.modifiedMs,
                            mimeType = getMimeType(item.javaFile),
                            size = item.size,
                            parent = directoryDeferred,
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

    private fun listFilesSafe(directory: JavaFile): Result<List<DirectEntry>> {
        val normal =
            try {
                directory.listFiles()?.toList()
            } catch (e: Exception) {
                null
            }
        if (normal != null) {
            return Result.success(
                normal.map {
                    DirectEntry(
                        javaFile = it,
                        name = it.name,
                        isDirectory = it.isDirectory,
                        size = it.length(),
                        modifiedMs = it.lastModified(),
                    )
                }
            )
        }

        if (rootGate == null) {
            return Result.failure(
                SecurityException("DirectFS cannot list ${directory.absolutePath}")
            )
        }

        val command =
            DirectFsRootPolicy.buildRootListCommand(directory.absolutePath).getOrElse {
                return Result.failure(it)
            }
        val lines =
            rootGate.runRootCommandSync(command, ROOT_LIST_TIMEOUT_MS)
                ?: return Result.failure(
                    SecurityException(
                        "Root-assisted DirectFS listing failed for ${directory.absolutePath}"
                    )
                )
        return Result.success(lines.mapNotNull { parseRootEntry(directory, it) })
    }

    private fun parseRootEntry(parent: JavaFile, line: String): DirectEntry? {
        val parts = line.split('\t', limit = 4)
        if (parts.size != 4) return null
        val name = parts[3]
        if (name.isBlank() || name == "." || name == ".." || name.contains('/')) return null
        return DirectEntry(
            javaFile = JavaFile(parent, name),
            name = name,
            isDirectory = parts[0] == "d",
            size = parts[1].toLongOrNull() ?: 0L,
            modifiedMs = (parts[2].toLongOrNull() ?: 0L) * 1000L,
        )
    }

    private fun getMimeType(file: JavaFile): String {
        val extension = file.extension.lowercase()
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
            ?: "application/octet-stream"
    }

    private class ConstantAddedMs(private val time: Long) : AddedMs {
        override suspend fun resolve(): Long = time
    }

    private data class DirectEntry(
        val javaFile: JavaFile,
        val name: String,
        val isDirectory: Boolean,
        val size: Long,
        val modifiedMs: Long,
    )

    private companion object {
        const val ROOT_LIST_TIMEOUT_MS = 1500L
    }
}

internal object DirectFsRootPolicy {
    fun buildRootListCommand(path: String): Result<String> {
        if (!isAllowedRootPath(path)) {
            return Result.failure(SecurityException("Rejected unsafe DirectFS root path: $path"))
        }
        val quoted = shellQuote(path)
        return Result.success(
            "for p in $quoted/* $quoted/.[!.]* $quoted/..?*; do " +
                "[ -e \"\$p\" ] || continue; " +
                "if [ -d \"\$p\" ]; then t=d; else t=f; fi; " +
                "s=\$(stat -c %s \"\$p\" 2>/dev/null || echo 0); " +
                "m=\$(stat -c %Y \"\$p\" 2>/dev/null || echo 0); " +
                "printf '%s\\t%s\\t%s\\t%s\\n' \"\$t\" \"\$s\" \"\$m\" \"\${p##*/}\"; " +
                "done"
        )
    }

    fun isAllowedRootPath(path: String): Boolean =
        path.startsWith("/storage/") &&
            path != "/storage/" &&
            path != "/storage/." &&
            path != "/storage/.." &&
            !path.contains('\n') &&
            !path.contains("/../") &&
            !path.endsWith("/..")

    fun shellQuote(value: String): String = buildString {
        append('\'')
        append(value.replace("'", "'\"'\"'"))
        append('\'')
    }
}
