/*
 * Copyright (c) 2026 Auxio Project
 * DirectFsTraversal.kt is part of Auxio.
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
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import org.oxycblt.musikr.fs.AddedMs
import org.oxycblt.musikr.fs.CanonicalSourcePolicy
import org.oxycblt.musikr.fs.Directory
import org.oxycblt.musikr.fs.File
import org.oxycblt.musikr.fs.Path

/** How one configured source finished, whatever the reason. */
internal enum class SourceCompletion {
    /** Every reachable directory below the root was enumerated and files were emitted. */
    COMPLETED,
    /** Every reachable directory below the root was enumerated but held no files. */
    COMPLETED_EMPTY,
    /** The root could not be listed right now, for example because a volume unmounted. */
    TEMPORARILY_UNAVAILABLE,
    /** The root exists but the app UID may not read it. */
    PERMISSION_REQUIRED,
    /** A documented safety limit stopped the traversal before it finished. */
    TRUNCATED,
    /** The scan was cancelled while this source was being traversed. */
    CANCELLED,
    /** The traversal of this source raised an unexpected error. */
    FAILED,
}

/** One configured root after normalisation, canonicalisation and overlap ordering. */
internal data class PreparedRoot(
    val sourceKey: String,
    val directory: JavaFile,
    val canonicalPath: String,
    val relativePath: Path,
    val scope: CanonicalSourcePolicy.Scope,
)

/** Documented per-scope traversal safety limits. */
internal data class TraversalBudget(val maxDirectories: Int, val maxFiles: Int)

/** Deterministic result of traversing one configured root. */
internal data class SourceTraversalResult(
    val sourceKey: String,
    val canonicalPath: String,
    val completion: SourceCompletion,
    val detail: String? = null,
)

/** A bounded record of one filesystem operation that took longer than expected. */
internal data class SlowOperationRecord(
    val path: String,
    val operation: String,
    val elapsedMs: Long,
    val sourceKey: String,
    val queuedDirectories: Int,
    val activeEnumerators: Int,
)

/** Deterministic, comparable measurements of one complete traversal. */
internal data class DirectFsTraversalMetrics(
    val directoriesVisited: Int,
    val filesEmitted: Int,
    val duplicateDirectoriesSuppressed: Int,
    val peakQueuedDirectories: Int,
    val activeEnumerators: Int,
    val elapsedMs: Long,
    val results: List<SourceTraversalResult>,
    val slowOperations: List<SlowOperationRecord>,
)

/** Tunables that production and tests share, so traversal behaviour is never test-only. */
internal data class DirectFsOptions(
    val maxDepth: Int = 32,
    val explicitBudget: TraversalBudget =
        TraversalBudget(DirectFS.MAX_VISITED_DIRECTORIES, DirectFS.MAX_VISITED_FILES),
    val wholeVolumeBudget: TraversalBudget =
        TraversalBudget(WHOLE_VOLUME_MAX_DIRECTORIES, DirectFS.MAX_VISITED_FILES),
    val slowOperationThresholdMs: Long = 750L,
    val maxSlowOperationRecords: Int = 32,
    val entryCancellationInterval: Int = 64,
    val isAllowedCanonicalPath: (String) -> Boolean = { DirectFsRootPolicy.isAllowedPath(it) },
) {
    internal companion object {
        val DEFAULT = DirectFsOptions()
    }
}

/**
 * A whole-volume root is nearly always an accidental or fallback selection, so it keeps a tighter
 * directory budget than a folder the user chose deliberately.
 */
internal const val WHOLE_VOLUME_MAX_DIRECTORIES = 20_000

/**
 * Single-coordinator DirectFS traversal.
 *
 * The previous design let several workers poll a shared blocking queue and infer global completion
 * from an atomic pending counter, which could not be verified from outside the workers. This
 * coordinator owns one explicit work queue per source and drains it on one enumerator, so
 * completion is structural: the source is finished when its queue is empty, and the whole traversal
 * is finished when the last source has been drained.
 *
 * Throughput comes from the downstream classification and extraction stages, which consume the file
 * channel while this coordinator enumerates. Slow head-unit storage is therefore never hit by
 * several concurrent `listFiles`/`lstat`/`canonicalFile` bursts.
 */
internal class DirectFsTraversal(
    private val roots: List<PreparedRoot>,
    private val options: DirectFsOptions = DirectFsOptions.DEFAULT,
) {
    /** Canonical directories already enumerated, shared by every configured root. */
    private val visited = HashSet<String>()

    private val results = mutableListOf<SourceTraversalResult>()
    private val slowOperations = ArrayDeque<SlowOperationRecord>()
    private var directoriesVisited = 0
    private var filesEmitted = 0
    private var duplicateDirectoriesSuppressed = 0
    private var peakQueuedDirectories = 0
    private var activeEnumerators = 0
    private var queuedDirectories = 0

    suspend fun explore(files: Channel<File>): DirectFsTraversalMetrics {
        val start = System.currentTimeMillis()
        for (root in roots) {
            currentCoroutineContext().ensureActive()
            traverseSource(root, files)
        }
        return DirectFsTraversalMetrics(
            directoriesVisited = directoriesVisited,
            filesEmitted = filesEmitted,
            duplicateDirectoriesSuppressed = duplicateDirectoriesSuppressed,
            peakQueuedDirectories = peakQueuedDirectories,
            activeEnumerators = activeEnumerators,
            elapsedMs = System.currentTimeMillis() - start,
            results = results.toList(),
            slowOperations = slowOperations.toList(),
        )
    }

    private suspend fun traverseSource(root: PreparedRoot, files: Channel<File>) {
        if (!visited.add(root.canonicalPath)) {
            duplicateDirectoriesSuppressed++
            record(
                root,
                SourceCompletion.COMPLETED,
                "Already traversed through an equivalent configured source",
            )
            return
        }
        val budget =
            when (root.scope) {
                CanonicalSourcePolicy.Scope.WHOLE_VOLUME -> options.wholeVolumeBudget
                CanonicalSourcePolicy.Scope.EXPLICIT -> options.explicitBudget
            }
        val queue = ArrayDeque<DirectoryTask>()
        queue.addLast(
            DirectoryTask(
                directory = root.directory,
                canonicalPath = root.canonicalPath,
                relativePath = root.relativePath,
                parent = null,
                depth = 0,
            )
        )
        var directories = 0
        var emitted = 0
        var truncation: String? = null
        var hardStop = false
        var unavailable: SourceTraversalResult? = null

        try {
            while (queue.isNotEmpty()) {
                currentCoroutineContext().ensureActive()
                queuedDirectories = queue.size
                peakQueuedDirectories = maxOf(peakQueuedDirectories, queuedDirectories)
                val task = queue.removeFirst()
                directoriesVisited++
                directories++

                val entries = enumerate(task, root)
                if (entries == null) {
                    if (task.depth == 0) {
                        unavailable = rootUnavailableResult(root)
                        break
                    }
                    if (!isRootStillReadable(root)) {
                        unavailable = rootUnavailableResult(root)
                        break
                    }
                    Log.w(TAG, "DirectFS skipped unreadable child directory ${task.directory.path}")
                    continue
                }

                val directoryDeferred = CompletableDeferred<Directory>()
                val children = mutableListOf<File>()
                var index = 0
                try {
                    for (entry in entries) {
                        if (++index % options.entryCancellationInterval == 0) {
                            currentCoroutineContext().ensureActive()
                        }
                        if (entry.isSymlink || entry.isDirectory) continue
                        if (emitted >= budget.maxFiles) {
                            truncation =
                                "DirectFS file limit (${budget.maxFiles}) reached at " +
                                    task.directory.path
                            hardStop = true
                            break
                        }
                        val file =
                            File(
                                Uri.fromFile(entry.javaFile),
                                task.relativePath.file(entry.name),
                                object : AddedMs {
                                    override suspend fun resolve() = entry.modifiedMs
                                },
                                entry.modifiedMs,
                                mimeTypeOf(entry.javaFile),
                                entry.size,
                                directoryDeferred,
                            )
                        children.add(file)
                        emitted++
                        filesEmitted++
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
                if (hardStop) break

                if (task.depth >= options.maxDepth) {
                    // Stop descending here, but keep draining the queue so sibling trees still
                    // complete. The source is reported as truncated once the queue is empty.
                    if (truncation == null) {
                        truncation =
                            "DirectFS maximum depth (${options.maxDepth}) reached at " +
                                task.directory.path
                    }
                    continue
                }

                for (entry in entries) {
                    if (entry.isSymlink || !entry.isDirectory) continue
                    if (!shouldDescendIntoDirectory(entry.name, root.scope)) {
                        Log.d(TAG, "DirectFS skipped noisy directory ${entry.javaFile.path}")
                        continue
                    }
                    val childCanonical = canonicalAppFacingPath(entry.javaFile)
                    if (
                        childCanonical == null || !isWithinRoot(childCanonical, root.canonicalPath)
                    ) {
                        Log.w(TAG, "DirectFS skipped an escaped directory at ${entry.javaFile.path}")
                        continue
                    }
                    if (!visited.add(childCanonical)) {
                        duplicateDirectoriesSuppressed++
                        continue
                    }
                    if (directories + queue.size >= budget.maxDirectories) {
                        truncation =
                            "DirectFS directory limit (${budget.maxDirectories}) reached at " +
                                entry.javaFile.path
                        hardStop = true
                        break
                    }
                    queue.addLast(
                        DirectoryTask(
                            directory = entry.javaFile,
                            canonicalPath = childCanonical,
                            relativePath = task.relativePath.file(entry.name),
                            parent = directoryDeferred,
                            depth = task.depth + 1,
                        )
                    )
                }
                if (hardStop) break
            }
        } catch (e: CancellationException) {
            record(root, SourceCompletion.CANCELLED, "Scan cancelled during ${root.canonicalPath}")
            throw e
        } catch (e: Exception) {
            // One broken source must never strand the remaining configured sources.
            Log.e(TAG, "DirectFS traversal failed for ${root.canonicalPath}", e)
            record(root, SourceCompletion.FAILED, e.message ?: e.javaClass.simpleName)
            return
        } finally {
            queuedDirectories = 0
        }

        when {
            unavailable != null -> results.add(unavailable)
            truncation != null -> record(root, SourceCompletion.TRUNCATED, truncation)
            emitted == 0 -> record(root, SourceCompletion.COMPLETED_EMPTY)
            else -> record(root, SourceCompletion.COMPLETED)
        }
    }

    private fun record(root: PreparedRoot, completion: SourceCompletion, detail: String? = null) {
        results.add(SourceTraversalResult(root.sourceKey, root.canonicalPath, completion, detail))
    }

    private fun rootUnavailableResult(root: PreparedRoot): SourceTraversalResult {
        val exists = runCatching { root.directory.exists() }.getOrDefault(false)
        return if (exists) {
            SourceTraversalResult(
                root.sourceKey,
                root.canonicalPath,
                SourceCompletion.PERMISSION_REQUIRED,
                "DirectFS source is not readable by the app: ${root.canonicalPath}",
            )
        } else {
            SourceTraversalResult(
                root.sourceKey,
                root.canonicalPath,
                SourceCompletion.TEMPORARILY_UNAVAILABLE,
                "DirectFS source is unavailable: ${root.canonicalPath}",
            )
        }
    }

    private fun isRootStillReadable(root: PreparedRoot): Boolean =
        runCatching { root.directory.isDirectory && root.directory.canRead() }.getOrDefault(false)

    /**
     * Lists one directory, recording the elapsed time of the blocking call.
     *
     * Returns `null` when the directory cannot be enumerated, which the caller turns into either a
     * source-level outcome or a skipped child.
     */
    private fun enumerate(task: DirectoryTask, root: PreparedRoot): List<DirectEntry>? {
        val start = System.currentTimeMillis()
        val listed =
            try {
                task.directory.listFiles()
            } catch (e: RuntimeException) {
                Log.d(TAG, "DirectFS listing failed for ${task.directory.path}", e)
                null
            }
        recordSlowOperation(task.directory.path, "listFiles", start, root.sourceKey)
        if (listed == null) return null
        activeEnumerators = 1
        val statStart = System.currentTimeMillis()
        val entries =
            listed.map { child ->
                DirectEntry(
                    javaFile = child,
                    name = child.name,
                    isDirectory = child.isDirectory,
                    isSymlink = isSymbolicLink(child, task.canonicalPath),
                    modifiedMs = child.lastModified(),
                    size = child.length(),
                )
            }
        recordSlowOperation(task.directory.path, "statEntries", statStart, root.sourceKey)
        activeEnumerators = 0
        return entries
    }

    private fun recordSlowOperation(
        path: String,
        operation: String,
        startMs: Long,
        sourceKey: String,
    ) {
        val elapsed = System.currentTimeMillis() - startMs
        if (elapsed < options.slowOperationThresholdMs) return
        if (slowOperations.size >= options.maxSlowOperationRecords) slowOperations.removeFirst()
        val record =
            SlowOperationRecord(
                path = path,
                operation = operation,
                elapsedMs = elapsed,
                sourceKey = sourceKey,
                queuedDirectories = queuedDirectories,
                activeEnumerators = activeEnumerators,
            )
        slowOperations.addLast(record)
        Log.w(TAG, "Slow DirectFS $operation took ${elapsed}ms at $path")
    }

    private data class DirectoryTask(
        val directory: JavaFile,
        val canonicalPath: String,
        val relativePath: Path,
        val parent: Deferred<Directory>?,
        val depth: Int,
    )

    private data class DirectEntry(
        val javaFile: JavaFile,
        val name: String,
        val isDirectory: Boolean,
        val isSymlink: Boolean,
        val modifiedMs: Long,
        val size: Long,
    )

    internal companion object {
        private const val TAG = "DirectFS"

        private val NOISY_DIRECTORY_NAMES =
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

        /**
         * Whether a child directory should be entered.
         *
         * An explicitly selected folder is scanned as the user asked: a folder named `Download`
         * inside `/storage/emulated/0/My Audio` is ordinary content, not platform noise. Only a
         * whole-volume root keeps the stronger name exclusions, because there those names really do
         * denote the platform's own media trees.
         */
        internal fun shouldDescendIntoDirectory(
            name: String,
            scope: CanonicalSourcePolicy.Scope,
        ): Boolean {
            if (name.isBlank() || name == "." || name == "..") return false
            if (name.startsWith('.')) return false
            if (scope == CanonicalSourcePolicy.Scope.EXPLICIT) return true
            return name.lowercase(java.util.Locale.ROOT) !in NOISY_DIRECTORY_NAMES
        }

        /** Whether [candidate] is the canonical root itself or below it. */
        internal fun isWithinRoot(candidate: String, canonicalRoot: String): Boolean =
            candidate == canonicalRoot || candidate.startsWith("$canonicalRoot/")

        /**
         * The canonical path of [file], expressed in the app-facing namespace.
         *
         * Canonicalisation resolves symbolic links and mount aliases, which is what makes repeated
         * or bind-mounted directories collapse onto one visited entry. The result is re-normalised
         * because canonicalisation can resolve an app-facing path back onto privileged backing
         * storage that the app UID may not open.
         */
        internal fun canonicalAppFacingPath(file: JavaFile): String? {
            val canonical =
                try {
                    file.canonicalPath
                } catch (_: Exception) {
                    return null
                }
            return CanonicalSourcePolicy.normalizePath(canonical) ?: canonical
        }

        /**
         * Whether [entry] is a symbolic link.
         *
         * The platform syscall is authoritative when it is available. Hosts without it fall back to
         * comparing the canonical path with the already canonical parent, which detects the same
         * escapes without any platform dependency.
         */
        internal fun isSymbolicLink(entry: JavaFile, parentCanonicalPath: String): Boolean {
            try {
                val stat = android.system.Os.lstat(entry.absolutePath)
                return android.system.OsConstants.S_ISLNK(stat.st_mode)
            } catch (_: Throwable) {
                // The syscall is unavailable on this host; use the canonical comparison below.
            }
            val canonical = canonicalAppFacingPath(entry) ?: return true
            return canonical != "$parentCanonicalPath/${entry.name}"
        }

        internal fun mimeTypeOf(file: JavaFile): String =
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension.lowercase())
                ?: "application/octet-stream"
    }
}
