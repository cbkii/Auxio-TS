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
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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
import org.oxycblt.musikr.fs.SourceAwareFS
import org.oxycblt.musikr.fs.SourceFingerprintStrength
import org.oxycblt.musikr.fs.SourceIdentity
import org.oxycblt.musikr.fs.SourceSnapshot
import org.oxycblt.musikr.util.tryAsyncWith

class DirectFS(private val roots: List<Location.Opened>) : SourceAwareFS {
    private val sourceFailures = ConcurrentHashMap<String, String>()

    override suspend fun sourceSnapshots(): List<SourceSnapshot> =
        kotlinx.coroutines.withContext(Dispatchers.IO) {
            roots.groupBy(SourceIdentity::forLocation).map { (sourceKey, locations) ->
                val evaluated =
                    locations.map { location ->
                        val root = location.uri.path?.let(::JavaFile)
                        val allowed = root != null && isAllowedRoot(root)
                        val readable = allowed && listFilesSafe(requireNotNull(root)) != null
                        RootSnapshot(location, root, readable)
                    }
                val available = evaluated.isNotEmpty() && evaluated.all { it.readable }
                SourceSnapshot(
                    sourceKey = sourceKey,
                    sourceType = SOURCE_TYPE,
                    // A source key may cover more than one configured folder. The first path is
                    // display metadata only; the combined fingerprint below covers every root.
                    rootUri = locations.firstOrNull()?.uri?.toString(),
                    rootPath = evaluated.firstOrNull()?.root?.absolutePath,
                    available = available,
                    fingerprint =
                        if (available) {
                            combineRootFingerprints(
                                evaluated.map { requireNotNull(it.root) to it.location }
                            )
                        } else {
                            null
                        },
                    fingerprintStrength =
                        if (available) SourceFingerprintStrength.ADVISORY
                        else SourceFingerprintStrength.NONE,
                )
            }
        }

    override fun selectSources(sourceKeys: Set<String>): FS =
        DirectFS(roots.filter { SourceIdentity.forLocation(it) in sourceKeys })

    override fun drainSourceFailures(): Map<String, String> =
        sourceFailures.toMap().also { sourceFailures.clear() }

    override suspend fun explore(files: Channel<File>): Deferred<Result<Unit>> = coroutineScope {
        tryAsyncWith(files, Dispatchers.IO) { output -> exploreBounded(output) }
    }

    override fun track(): Flow<FSUpdate> = emptyFlow()

    private suspend fun exploreBounded(files: Channel<File>) = coroutineScope {
        val queue = Channel<DirectoryTask>(Channel.UNLIMITED)
        val activeTasks = AtomicInteger(0)
        val discoveredDirectories = AtomicInteger(0)
        val discoveredFiles = AtomicInteger(0)
        val visitedRoots = ConcurrentHashMap.newKeySet<String>()
        val visitedDirectories = ConcurrentHashMap.newKeySet<String>()

        val workers =
            List(DIRECTORY_WORKER_COUNT) {
                async(Dispatchers.IO) {
                    for (task in queue) {
                        try {
                            processDirectory(
                                task,
                                files,
                                queue,
                                activeTasks,
                                discoveredDirectories,
                                discoveredFiles,
                                visitedDirectories,
                            )
                        } finally {
                            if (activeTasks.decrementAndGet() == 0) {
                                queue.close()
                            }
                        }
                    }
                }
            }

        try {
            var enqueuedAtLeastOne = false
            for (location in roots) {
                val sourceKey = SourceIdentity.forLocation(location)
                if (location.uri.scheme != "file") {
                    recordFailure(
                        sourceKey,
                        "TEMPORARILY_UNAVAILABLE|Unsupported DirectFS URI ${location.uri}",
                    )
                    continue
                }
                val root = location.uri.path?.let(::JavaFile)
                val canonicalRoot = root?.let(::canonicalFileOrNull)
                if (
                    root == null || canonicalRoot == null || !isAllowedCanonicalRoot(canonicalRoot)
                ) {
                    recordFailure(
                        sourceKey,
                        "TEMPORARILY_UNAVAILABLE|Unsafe or missing DirectFS source ${location.uri}",
                    )
                    continue
                }

                val canonicalPath = canonicalRoot.absolutePath
                if (!visitedRoots.add(canonicalPath)) {
                    Log.d(TAG, "DirectFS skipped duplicate root $canonicalPath")
                    continue
                }

                if (!visitedDirectories.add(canonicalPath)) {
                    Log.d(TAG, "DirectFS skipped duplicate directory $canonicalPath")
                    continue
                }

                val task =
                    DirectoryTask(
                        directory = root,
                        canonicalRoot = canonicalRoot,
                        relativePath = location.path,
                        parent = null,
                        depth = 0,
                        sourceKey = sourceKey,
                        configuredRootTask = true,
                    )

                if (
                    enqueueDirectory(queue, activeTasks, discoveredDirectories, task) !=
                        EnqueueResult.LimitExceeded
                ) {
                    enqueuedAtLeastOne = true
                }
            }
            if (!enqueuedAtLeastOne) {
                queue.close()
            }
        } catch (e: Exception) {
            queue.close(e)
            throw e
        }
        workers.awaitAll()
    }

    private suspend fun processDirectory(
        task: DirectoryTask,
        files: Channel<File>,
        queue: Channel<DirectoryTask>,
        activeTasks: AtomicInteger,
        discoveredDirectories: AtomicInteger,
        discoveredFiles: AtomicInteger,
        visitedDirectories: MutableSet<String>,
    ) {
        if (discoveredFiles.get() >= MAX_VISITED_FILES) {
            recordFailure(
                task.sourceKey,
                "TRUNCATED|DirectFS file limit reached at ${task.directory.path}",
            )
            return
        }
        if (task.depth > MAX_DEPTH) {
            recordFailure(
                task.sourceKey,
                "TRUNCATED|DirectFS maximum depth exceeded at ${task.directory.path}",
            )
            return
        }
        if (!isWithinCanonicalRoot(task.directory, task.canonicalRoot)) {
            recordFailure(
                task.sourceKey,
                "DirectFS traversal left the configured source at ${task.directory.path}",
            )
            return
        }

        val entries = listFilesSafe(task.directory)
        if (entries == null) {
            val detail = "DirectFS directory is unavailable at ${task.directory.path}"
            if (task.configuredRootTask) {
                recordFailure(task.sourceKey, "TEMPORARILY_UNAVAILABLE|$detail")
            } else {
                Log.w(TAG, "Skipping unreadable child directory ${task.directory.path}")
            }
            return
        }

        val directoryDeferred = CompletableDeferred<Directory>()
        val children = mutableListOf<File>()
        try {
            for (entry in entries) {
                if (entry.isSymlink || entry.isDirectory) continue
                if (discoveredFiles.incrementAndGet() > MAX_VISITED_FILES) {
                    recordFailure(
                        task.sourceKey,
                        "TRUNCATED|DirectFS file limit reached at ${task.directory.path}",
                    )
                    break
                }
                val item = entry.javaFile
                val file =
                    File(
                        Uri.fromFile(item),
                        task.relativePath.file(entry.name),
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
        } finally {
            if (!directoryDeferred.isCompleted) {
                directoryDeferred.complete(
                    Directory(
                        Uri.fromFile(task.directory),
                        task.relativePath,
                        task.parent,
                        children,
                    )
                )
            }
        }

        for (entry in entries) {
            if (entry.isSymlink || !entry.isDirectory) continue
            if (!shouldDescendIntoDirectory(entry.name)) {
                Log.d(TAG, "DirectFS skipped noisy directory ${entry.javaFile.path}")
                continue
            }
            val item = entry.javaFile
            val canonicalChild = canonicalFileOrNull(item) ?: continue
            if (!isWithinCanonicalRoot(canonicalChild, task.canonicalRoot)) {
                Log.w(TAG, "DirectFS skipped an escaped directory at ${item.path}")
                continue
            }
            if (!visitedDirectories.add(canonicalChild.absolutePath)) {
                Log.d(TAG, "DirectFS skipped duplicate traversal of ${canonicalChild.absolutePath}")
                continue
            }

            val childTask =
                DirectoryTask(
                    directory = item,
                    canonicalRoot = task.canonicalRoot,
                    relativePath = task.relativePath.file(entry.name),
                    parent = directoryDeferred,
                    depth = task.depth + 1,
                    sourceKey = task.sourceKey,
                    configuredRootTask = false,
                )
            enqueueDirectory(queue, activeTasks, discoveredDirectories, childTask)
        }
    }

    private fun enqueueDirectory(
        queue: Channel<DirectoryTask>,
        activeTasks: AtomicInteger,
        discoveredDirectories: AtomicInteger,
        task: DirectoryTask,
    ): EnqueueResult {
        while (true) {
            val current = discoveredDirectories.get()
            if (current >= MAX_VISITED_DIRECTORIES) {
                recordFailure(
                    task.sourceKey,
                    "TRUNCATED|DirectFS directory limit reached at ${task.directory.path}",
                )
                return EnqueueResult.LimitExceeded
            }
            if (discoveredDirectories.compareAndSet(current, current + 1)) break
        }
        activeTasks.incrementAndGet()
        val result = queue.trySend(task)
        if (result.isSuccess) {
            return EnqueueResult.Enqueued
        }
        activeTasks.decrementAndGet()
        return EnqueueResult.LimitExceeded
    }

    private fun recordFailure(sourceKey: String, detail: String) {
        if (sourceFailures.putIfAbsent(sourceKey, detail) == null) {
            Log.w(TAG, detail)
        }
    }

    private fun combineRootFingerprints(roots: List<Pair<JavaFile, Location.Opened>>): String {
        val digest = MessageDigest.getInstance("SHA-256")
        roots
            .sortedBy { it.first.absolutePath }
            .forEach { (root, location) ->
                digest.update(location.uri.toString().toByteArray(Charsets.UTF_8))
                digest.update(0)
                digest.update(boundedFingerprint(root).toByteArray(Charsets.UTF_8))
                digest.update(0)
            }
        return digest.digest().joinToString("") { "%02x".format(it) }
    }

    private fun boundedFingerprint(root: JavaFile): String {
        val digest = MessageDigest.getInstance("SHA-256")
        fun update(value: String) = digest.update(value.toByteArray(Charsets.UTF_8))
        update(root.absolutePath)
        update("|${root.lastModified()}|${root.length()}|")
        listFilesSafe(root)
            .orEmpty()
            .asSequence()
            .filterNot { it.isSymlink }
            .sortedBy { it.name.lowercase(Locale.ROOT) }
            .take(FINGERPRINT_ENTRY_LIMIT)
            .forEach {
                update(
                    "${it.name}\u0000${it.isDirectory}\u0000${it.modifiedMs}\u0000${it.size}\u0000"
                )
            }
        return digest.digest().joinToString("") { "%02x".format(it) }
    }

    private fun listFilesSafe(directory: JavaFile): List<DirectEntry>? {
        val local =
            try {
                directory.listFiles()
            } catch (e: RuntimeException) {
                Log.d(TAG, "Direct listing unavailable for ${directory.path}", e)
                null
            }
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

        Log.w(TAG, "DirectFS source is unavailable or inaccessible: ${directory.path}")
        return null
    }

    private data class RootSnapshot(
        val location: Location.Opened,
        val root: JavaFile?,
        val readable: Boolean,
    )

    private data class DirectEntry(
        val javaFile: JavaFile,
        val name: String,
        val isDirectory: Boolean,
        val isSymlink: Boolean,
        val modifiedMs: Long,
        val size: Long,
    )

    private data class DirectoryTask(
        val directory: JavaFile,
        val canonicalRoot: JavaFile,
        val relativePath: Path,
        val parent: Deferred<Directory>?,
        val depth: Int,
        val sourceKey: String,
        val configuredRootTask: Boolean,
    )

    private enum class EnqueueResult {
        Enqueued,
        ProcessInline,
        LimitExceeded,
    }

    private fun getMimeType(file: JavaFile): String =
        MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension.lowercase())
            ?: "application/octet-stream"

    internal companion object {
        private const val TAG = "DirectFS"
        private const val SOURCE_TYPE = "DIRECT_FS"
        private const val MAX_DEPTH = 32
        private const val FINGERPRINT_ENTRY_LIMIT = 128
        internal const val DIRECTORY_WORKER_COUNT = 3
        internal const val MAX_PENDING_DIRECTORIES = 512
        internal const val MAX_VISITED_DIRECTORIES = 100_000
        internal const val MAX_VISITED_FILES = 50_000
        private const val QUEUE_POLL_INTERVAL_MS = 100L
        private val skippedDirectoryNames =
            setOf(
                "android",
                "download",
                "dcim",
                "pictures",
                "movies",
                ".zjinnova",
                ".tcfg",
                ".dfmusiclog",
            )

        private val protectedRoots =
            listOf("/", "/system", "/vendor", "/data", "/proc", "/sys", "/dev", "/acct", "/config")

        fun isSymbolicLinkCompat(file: JavaFile): Boolean =
            try {
                val stat = android.system.Os.lstat(file.absolutePath)
                android.system.OsConstants.S_ISLNK(stat.st_mode)
            } catch (_: Exception) {
                false
            }

        fun isAllowedRoot(file: JavaFile): Boolean =
            canonicalFileOrNull(file)?.let(::isAllowedCanonicalRoot) == true

        internal fun shouldDescendIntoDirectory(name: String): Boolean =
            name.isNotBlank() &&
                name != "." &&
                name != ".." &&
                !name.startsWith('.') &&
                name.lowercase(Locale.ROOT) !in skippedDirectoryNames

        internal fun isWithinCanonicalRoot(candidate: JavaFile, canonicalRoot: JavaFile): Boolean {
            var cursor = canonicalFileOrNull(candidate) ?: return false
            while (true) {
                if (cursor == canonicalRoot) return true
                cursor = cursor.parentFile ?: return false
            }
        }

        internal fun isExpectedRestrictedSharedStorageChild(
            directory: JavaFile,
            canonicalRoot: JavaFile,
        ): Boolean {
            if (canonicalRoot.path.trimEnd('/') != "/storage/emulated/0") return false
            val canonicalDirectory = canonicalFileOrNull(directory) ?: return false
            val rootPath = canonicalRoot.path.trimEnd('/')
            val relative = canonicalDirectory.path.removePrefix(rootPath).trimStart('/')
            return relative == "Android/data" ||
                relative.startsWith("Android/data/") ||
                relative == "Android/obb" ||
                relative.startsWith("Android/obb/")
        }

        private fun canonicalFileOrNull(file: JavaFile): JavaFile? =
            try {
                file.canonicalFile
            } catch (_: Exception) {
                null
            }

        private fun isAllowedCanonicalRoot(canonical: JavaFile): Boolean {
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
