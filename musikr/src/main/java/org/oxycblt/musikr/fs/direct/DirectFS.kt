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
import android.util.Log
import android.webkit.MimeTypeMap
import java.io.File as JavaFile
import java.security.MessageDigest
import java.util.Locale
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
import org.oxycblt.musikr.fs.SourceAwareFS
import org.oxycblt.musikr.fs.SourceFingerprintStrength
import org.oxycblt.musikr.fs.SourceIdentity
import org.oxycblt.musikr.fs.SourceSnapshot
import org.oxycblt.musikr.util.tryAsync
import org.oxycblt.musikr.util.tryAsyncWith
import org.oxycblt.musikr.util.tryAwaitAll

class DirectFS(private val roots: List<Location.Opened>, private val rootGate: RootGate? = null) :
    SourceAwareFS {
    override suspend fun sourceSnapshots(): List<SourceSnapshot> =
        kotlinx.coroutines.withContext(Dispatchers.IO) {
            roots.distinctBy(SourceIdentity::forLocation).map { location ->
                val root = location.uri.path?.let(::JavaFile)
                val allowed = root != null && isAllowedRoot(root)
                val accessible =
                    allowed &&
                        (root!!.isDirectory ||
                            runCatching { listFilesSafe(root).isNotEmpty() }.getOrDefault(false))
                SourceSnapshot(
                    sourceKey = SourceIdentity.forLocation(location),
                    sourceType = SOURCE_TYPE,
                    rootUri = location.uri.toString(),
                    rootPath = root?.absolutePath,
                    available = accessible,
                    fingerprint = root?.takeIf { accessible }?.let(::boundedFingerprint),
                    // FAT/direct trees expose no trustworthy generation token. A bounded sample
                    // avoids needless warm scans, but is refreshed periodically by the ledger.
                    fingerprintStrength =
                        if (accessible) SourceFingerprintStrength.ADVISORY
                        else SourceFingerprintStrength.NONE,
                )
            }
        }

    override fun selectSources(sourceKeys: Set<String>): FS =
        DirectFS(roots.filter { SourceIdentity.forLocation(it) in sourceKeys }, rootGate)

    override suspend fun explore(files: Channel<File>): Deferred<Result<Unit>> = coroutineScope {
        tryAsyncWith(files, Dispatchers.IO) {
            roots
                .map { location ->
                    if (location.uri.scheme != "file") {
                        Log.w(TAG, "Skipping non-file DirectFS source: ${location.uri}")
                        return@map CompletableDeferred(Result.success(Unit))
                    }
                    val root = location.uri.path?.let(::JavaFile)
                    if (root == null || !isAllowedRoot(root)) {
                        Log.w(TAG, "Skipping unsafe DirectFS source: ${location.uri}")
                        return@map CompletableDeferred(Result.success(Unit))
                    }
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
            if (depth > MAX_DEPTH || !isAllowedRoot(directory)) return@tryAsync
            val directoryDeferred = CompletableDeferred<Directory>()
            val children = mutableListOf<File>()
            val recursive = mutableListOf<Deferred<Result<Unit>>>()
            for (entry in listFilesSafe(directory)) {
                if (entry.name.startsWith(".") || entry.isSymlink) continue
                val item = entry.javaFile
                val newPath = relativePath.file(entry.name)
                if (entry.isDirectory) {
                    recursive.add(
                        exploreDirectoryImpl(item, newPath, directoryDeferred, files, depth + 1)
                    )
                } else {
                    val file =
                        File(
                            Uri.fromFile(item),
                            newPath,
                            object : AddedMs {
                                override suspend fun resolve() = entry.modifiedMs
                            },
                            entry.modifiedMs,
                            getMimeType(item),
                            entry.size,
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

    private fun boundedFingerprint(root: JavaFile): String {
        val digest = MessageDigest.getInstance("SHA-256")
        fun update(value: String) = digest.update(value.toByteArray(Charsets.UTF_8))
        update(root.absolutePath)
        update("|${root.lastModified()}|${root.length()}|")
        listFilesSafe(root)
            .asSequence()
            .filterNot { it.name.startsWith(".") || it.isSymlink }
            .sortedBy { it.name.lowercase(Locale.ROOT) }
            .take(FINGERPRINT_ENTRY_LIMIT)
            .forEach {
                update(
                    "${it.name}\u0000${it.isDirectory}\u0000${it.modifiedMs}\u0000${it.size}\u0000"
                )
            }
        return digest.digest().joinToString("") { "%02x".format(it) }
    }

    private fun listFilesSafe(directory: JavaFile): List<DirectEntry> {
        val local = directory.listFiles()
        if (local != null) {
            return local.map {
                DirectEntry(
                    javaFile = it,
                    name = it.name,
                    isDirectory = it.isDirectory,
                    isSymlink = isSymbolicLinkCompat(it),
                    modifiedMs = it.lastModified(),
                    size = it.length(),
                )
            }
        }
        val rootList =
            try {
                rootGate
                    ?.runRootCommandSync(buildRootListCommand(directory.absolutePath))
                    ?.mapNotNull { parseRootEntry(directory, it) }
            } catch (e: RuntimeException) {
                Log.w(TAG, "Root-assisted DirectFS listing failed for ${directory.path}", e)
                null
            }
        if (rootList != null) return rootList
        Log.w(TAG, "DirectFS root is unavailable or inaccessible: ${directory.path}")
        return emptyList()
    }

    private data class DirectEntry(
        val javaFile: JavaFile,
        val name: String,
        val isDirectory: Boolean,
        val isSymlink: Boolean,
        val modifiedMs: Long,
        val size: Long,
    )

    private fun parseRootEntry(parent: JavaFile, line: String): DirectEntry? {
        val parts = line.split('\t', limit = 5)
        if (parts.size != 5) return null
        val name = parts[4]
        if (name.isBlank() || name == "." || name == ".." || name.contains('/')) return null
        return DirectEntry(
            javaFile = JavaFile(parent, name),
            name = name,
            isDirectory = parts[0] == "d",
            isSymlink = parts[1] == "l",
            modifiedMs = (parts[2].toLongOrNull() ?: 0L) * 1000L,
            size = parts[3].toLongOrNull() ?: 0L,
        )
    }

    private fun getMimeType(file: JavaFile): String =
        MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension.lowercase())
            ?: "application/octet-stream"

    internal companion object {
        private const val TAG = "DirectFS"
        private const val SOURCE_TYPE = "DIRECT_FS"
        private const val MAX_DEPTH = 32
        private const val FINGERPRINT_ENTRY_LIMIT = 128

        private val protectedRoots =
            listOf("/", "/system", "/vendor", "/data", "/proc", "/sys", "/dev", "/acct", "/config")

        fun shellQuote(value: String): String = "'${value.replace("'", "'\"'\"'")}'"

        fun buildRootListCommand(directory: String): String {
            val quoted = shellQuote(directory)
            return "for p in $quoted/* $quoted/.*; do " +
                "[ -e \"\$p\" ] || continue; " +
                "b=\${p##*/}; [ \"\$b\" = . ] && continue; [ \"\$b\" = .. ] && continue; " +
                "t=f; [ -d \"\$p\" ] && t=d; [ -L \"\$p\" ] && t=l; " +
                "m=\$(stat -c %Y \"\$p\" 2>/dev/null || echo 0); " +
                "s=\$(stat -c %s \"\$p\" 2>/dev/null || echo 0); " +
                "printf '%s\t%s\t%s\t%s\t%s\n' \"\$t\" \"\$t\" \"\$m\" \"\$s\" \"\$b\"; " +
                "done"
        }

        fun isSymbolicLinkCompat(file: JavaFile): Boolean =
            try {
                val stat = android.system.Os.lstat(file.absolutePath)
                android.system.OsConstants.S_ISLNK(stat.st_mode)
            } catch (_: Exception) {
                false
            }

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
