#!/usr/bin/env python3

from __future__ import annotations

from pathlib import Path
from textwrap import dedent


def replace_once(path: str, old: str, new: str) -> None:
    file = Path(path)
    text = file.read_text(encoding="utf-8")
    count = text.count(old)
    if count != 1:
        raise SystemExit(f"STOP: expected one match in {path}, found {count}")
    file.write_text(text.replace(old, new), encoding="utf-8", newline="\n")


def patch_direct_fs() -> None:
    path = "musikr/src/main/java/org/oxycblt/musikr/fs/direct/DirectFS.kt"
    replace_once(
        path,
        "import java.util.concurrent.atomic.AtomicInteger\n",
        "import java.util.concurrent.atomic.AtomicBoolean\n"
        "import java.util.concurrent.atomic.AtomicInteger\n",
    )

    old = dedent(
        '''\
        private suspend fun exploreBounded(files: Channel<File>) = coroutineScope {
            val queue = LinkedBlockingQueue<DirectoryTask>(MAX_PENDING_DIRECTORIES)
            val pending = AtomicInteger(0)
            val discoveredDirectories = AtomicInteger(0)

            roots.forEach { location ->
                val sourceKey = SourceIdentity.forLocation(location)
                if (location.uri.scheme != "file") {
                    recordFailure(sourceKey, "Unsupported DirectFS URI ${location.uri}")
                    return@forEach
                }
                val root = location.uri.path?.let(::JavaFile)
                val canonicalRoot = root?.let(::canonicalFileOrNull)
                if (root == null || canonicalRoot == null || !isAllowedCanonicalRoot(canonicalRoot)) {
                    recordFailure(sourceKey, "Unsafe or missing DirectFS source ${location.uri}")
                    return@forEach
                }
                enqueueDirectory(
                    queue,
                    pending,
                    discoveredDirectories,
                    DirectoryTask(
                        directory = root,
                        canonicalRoot = canonicalRoot,
                        relativePath = location.path,
                        parent = null,
                        depth = 0,
                        sourceKey = sourceKey,
                    ),
                )
            }

            List(DIRECTORY_WORKER_COUNT) {
                    async(Dispatchers.IO) {
                        while (isActive) {
                            val task = queue.poll(QUEUE_POLL_INTERVAL_MS, TimeUnit.MILLISECONDS)
                            if (task == null) {
                                if (pending.get() == 0) return@async
                                continue
                            }
                            try {
                                processDirectory(task, files, queue, pending, discoveredDirectories)
                            } finally {
                                pending.decrementAndGet()
                            }
                        }
                    }
                }
                .awaitAll()
        }

        private suspend fun processDirectory(
            task: DirectoryTask,
            files: Channel<File>,
            queue: LinkedBlockingQueue<DirectoryTask>,
            pending: AtomicInteger,
            discoveredDirectories: AtomicInteger,
        ) {
            if (task.depth > MAX_DEPTH) {
                recordFailure(
                    task.sourceKey,
                    "DirectFS maximum depth exceeded at ${task.directory.path}",
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
                recordFailure(
                    task.sourceKey,
                    "DirectFS source became unavailable at ${task.directory.path}",
                )
                return
            }

            val directoryDeferred = CompletableDeferred<Directory>()
            val children = mutableListOf<File>()
            try {
                for (entry in entries) {
                    if (entry.isSymlink) continue
                    val item = entry.javaFile
                    val newPath = task.relativePath.file(entry.name)
                    if (entry.isDirectory) {
                        if (!isWithinCanonicalRoot(item, task.canonicalRoot)) {
                            recordFailure(
                                task.sourceKey,
                                "DirectFS rejected an escaped directory at ${item.path}",
                            )
                            continue
                        }
                        enqueueDirectory(
                            queue,
                            pending,
                            discoveredDirectories,
                            DirectoryTask(
                                directory = item,
                                canonicalRoot = task.canonicalRoot,
                                relativePath = newPath,
                                parent = directoryDeferred,
                                depth = task.depth + 1,
                                sourceKey = task.sourceKey,
                            ),
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
        }

        private fun enqueueDirectory(
            queue: LinkedBlockingQueue<DirectoryTask>,
            pending: AtomicInteger,
            discoveredDirectories: AtomicInteger,
            task: DirectoryTask,
        ): Boolean {
            val discovered = discoveredDirectories.incrementAndGet()
            if (discovered > MAX_VISITED_DIRECTORIES) {
                discoveredDirectories.decrementAndGet()
                recordFailure(
                    task.sourceKey,
                    "DirectFS directory limit exceeded at ${task.directory.path}",
                )
                return false
            }
            pending.incrementAndGet()
            if (queue.offer(task)) return true
            pending.decrementAndGet()
            discoveredDirectories.decrementAndGet()
            recordFailure(
                task.sourceKey,
                "DirectFS pending-directory limit exceeded at ${task.directory.path}",
            )
            return false
        }

        private fun recordFailure(sourceKey: String, detail: String) {
            Log.w(TAG, detail)
            sourceFailures.putIfAbsent(sourceKey, detail)
        }
        '''
    ).replace("\n", "\n    ", 1).rstrip() + "\n"

    new = dedent(
        '''\
        private suspend fun exploreBounded(files: Channel<File>) = coroutineScope {
            val queue = LinkedBlockingQueue<DirectoryTask>(MAX_PENDING_DIRECTORIES)
            val pending = AtomicInteger(0)
            val discoveredDirectories = AtomicInteger(0)
            val seeding = AtomicBoolean(true)
            val workers =
                List(DIRECTORY_WORKER_COUNT) {
                    async(Dispatchers.IO) {
                        while (isActive) {
                            val task = queue.poll(QUEUE_POLL_INTERVAL_MS, TimeUnit.MILLISECONDS)
                            if (task == null) {
                                if (!seeding.get() && pending.get() == 0) return@async
                                continue
                            }
                            try {
                                processDirectory(task, files, queue, pending, discoveredDirectories)
                            } finally {
                                pending.decrementAndGet()
                            }
                        }
                    }
                }

            try {
                for (location in roots) {
                    val sourceKey = SourceIdentity.forLocation(location)
                    if (location.uri.scheme != "file") {
                        recordFailure(sourceKey, "Unsupported DirectFS URI ${location.uri}")
                        continue
                    }
                    val root = location.uri.path?.let(::JavaFile)
                    val canonicalRoot = root?.let(::canonicalFileOrNull)
                    if (
                        root == null ||
                            canonicalRoot == null ||
                            !isAllowedCanonicalRoot(canonicalRoot)
                    ) {
                        recordFailure(sourceKey, "Unsafe or missing DirectFS source ${location.uri}")
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
                        )
                    when (enqueueDirectory(queue, pending, discoveredDirectories, task)) {
                        EnqueueResult.Enqueued -> Unit
                        EnqueueResult.ProcessInline ->
                            processDirectory(task, files, queue, pending, discoveredDirectories)
                        EnqueueResult.LimitExceeded -> Unit
                    }
                }
            } finally {
                seeding.set(false)
            }
            workers.awaitAll()
        }

        private suspend fun processDirectory(
            task: DirectoryTask,
            files: Channel<File>,
            queue: LinkedBlockingQueue<DirectoryTask>,
            pending: AtomicInteger,
            discoveredDirectories: AtomicInteger,
        ) {
            if (task.depth > MAX_DEPTH) {
                recordFailure(
                    task.sourceKey,
                    "DirectFS maximum depth exceeded at ${task.directory.path}",
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
                recordFailure(
                    task.sourceKey,
                    "DirectFS source became unavailable at ${task.directory.path}",
                )
                return
            }

            val directoryDeferred = CompletableDeferred<Directory>()
            val children = mutableListOf<File>()
            try {
                for (entry in entries) {
                    if (entry.isSymlink || entry.isDirectory) continue
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
                val item = entry.javaFile
                if (!isWithinCanonicalRoot(item, task.canonicalRoot)) {
                    recordFailure(
                        task.sourceKey,
                        "DirectFS rejected an escaped directory at ${item.path}",
                    )
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
                    )
                when (enqueueDirectory(queue, pending, discoveredDirectories, childTask)) {
                    EnqueueResult.Enqueued -> Unit
                    EnqueueResult.ProcessInline ->
                        processDirectory(
                            childTask,
                            files,
                            queue,
                            pending,
                            discoveredDirectories,
                        )
                    EnqueueResult.LimitExceeded -> Unit
                }
            }
        }

        private fun enqueueDirectory(
            queue: LinkedBlockingQueue<DirectoryTask>,
            pending: AtomicInteger,
            discoveredDirectories: AtomicInteger,
            task: DirectoryTask,
        ): EnqueueResult {
            while (true) {
                val current = discoveredDirectories.get()
                if (current >= MAX_VISITED_DIRECTORIES) {
                    recordFailure(
                        task.sourceKey,
                        "DirectFS directory limit exceeded at ${task.directory.path}",
                    )
                    return EnqueueResult.LimitExceeded
                }
                if (discoveredDirectories.compareAndSet(current, current + 1)) break
            }
            pending.incrementAndGet()
            if (queue.offer(task)) return EnqueueResult.Enqueued
            pending.decrementAndGet()
            return EnqueueResult.ProcessInline
        }

        private fun recordFailure(sourceKey: String, detail: String) {
            if (sourceFailures.putIfAbsent(sourceKey, detail) == null) {
                Log.w(TAG, detail)
            }
        }
        '''
    ).replace("\n", "\n    ", 1).rstrip() + "\n"
    replace_once(path, old, new)

    replace_once(
        path,
        """    private fun listFilesSafe(directory: JavaFile): List<DirectEntry>? {\n        val local = directory.listFiles()\n""",
        """    private fun listFilesSafe(directory: JavaFile): List<DirectEntry>? {\n        val local =\n            try {\n                directory.listFiles()\n            } catch (e: RuntimeException) {\n                Log.d(TAG, \"Direct listing unavailable for ${directory.path}; trying root\", e)\n                null\n            }\n""",
    )
    replace_once(
        path,
        """    private data class DirectoryTask(\n        val directory: JavaFile,\n        val canonicalRoot: JavaFile,\n        val relativePath: Path,\n        val parent: Deferred<Directory>?,\n        val depth: Int,\n        val sourceKey: String,\n    )\n\n""",
        """    private data class DirectoryTask(\n        val directory: JavaFile,\n        val canonicalRoot: JavaFile,\n        val relativePath: Path,\n        val parent: Deferred<Directory>?,\n        val depth: Int,\n        val sourceKey: String,\n    )\n\n    private enum class EnqueueResult {\n        Enqueued,\n        ProcessInline,\n        LimitExceeded,\n    }\n\n""",
    )


def patch_root_listing() -> None:
    path = "app/src/main/java/org/oxycblt/auxio/headunit/root/RootStateHolder.kt"
    replace_once(
        path,
        """                if (lines.size > MAX_ROOT_LIST_LINES) {\n                    journal.log(\n                        DiagnosticJournal.CAT_STORAGE,\n                        \"Root listing line limit exceeded\",\n                        \"maxLines=$MAX_ROOT_LIST_LINES\",\n                    )\n                }\n                lines.take(MAX_ROOT_LIST_LINES)\n""",
        """                if (lines.size > MAX_ROOT_LIST_LINES) {\n                    journal.log(\n                        DiagnosticJournal.CAT_STORAGE,\n                        \"Root listing line limit exceeded\",\n                        \"maxLines=$MAX_ROOT_LIST_LINES\",\n                    )\n                    null\n                } else {\n                    lines\n                }\n""",
    )
    replace_once(
        path,
        """        const val ROOT_LIST_OUTPUT_BYTES = 512 * 1024\n        const val MAX_ROOT_LIST_LINES = 5_000\n""",
        """        const val ROOT_LIST_OUTPUT_BYTES = 16 * 1024 * 1024\n        const val MAX_ROOT_LIST_LINES = 50_000\n""",
    )


def patch_cover_provider() -> None:
    path = "app/src/main/java/org/oxycblt/auxio/image/CoverProvider.kt"
    replace_once(
        path,
        "import java.util.concurrent.ScheduledThreadPoolExecutor\n",
        "import java.util.concurrent.ScheduledFuture\n"
        "import java.util.concurrent.ScheduledThreadPoolExecutor\n",
    )
    old = dedent(
        '''\
        return try {
            writerExecutor.execute { writeCoverToPipe(id, pipe[1]) }
            pipe[0]
        } catch (e: RejectedExecutionException) {
            pipe[0].closeQuietly()
            pipe[1].closeQuietly()
            L.w("Cover-provider writer queue is full; rejecting request")
            null
        }
    }

    private fun writeCoverToPipe(id: String, writeSide: ParcelFileDescriptor) {
        val timedOut = AtomicBoolean(false)
        val timeoutFuture =
            try {
                transferTimeoutExecutor.schedule(
                    {
                        timedOut.set(true)
                        writeSide.closeQuietly()
                    },
                    COVER_TRANSFER_TIMEOUT_MS,
                    TimeUnit.MILLISECONDS,
                )
            } catch (e: RejectedExecutionException) {
                writeSide.closeQuietly()
                L.w(e, "Cover-provider timeout executor rejected request")
                return
            }

        try {
            ParcelFileDescriptor.AutoCloseOutputStream(writeSide).use { output ->
                val coverDescriptor = runBlocking {
                    withTimeoutOrNull(COVER_LOAD_TIMEOUT_MS) {
                        withContext(Dispatchers.IO) {
                            when (
                                val result =
                                    SettingCovers.immutable(requireNotNull(context)).obtain(id)
                            ) {
                                is CoverResult.Hit -> result.cover.fd()
                                else -> null
                            }
                        }
                    }
                }
                if (coverDescriptor == null) {
                    L.d("Cover-provider request missed or timed out: $id")
                    return
                }
                ParcelFileDescriptor.AutoCloseInputStream(coverDescriptor).use { input ->
                    if (!copyBounded(input, output, MAX_COVER_BYTES)) {
                        L.w("Cover-provider payload exceeded $MAX_COVER_BYTES bytes: $id")
                    }
                }
            }
        } catch (e: IOException) {
            if (timedOut.get()) {
                L.w("Cover-provider transfer timed out: $id")
            } else {
                L.w(e, "Cover-provider transfer failed: $id")
            }
        } catch (e: RuntimeException) {
            if (timedOut.get()) {
                L.w("Cover-provider transfer timed out: $id")
            } else {
                L.w(e, "Cover-provider request failed: $id")
            }
        } finally {
            timeoutFuture.cancel(false)
        }
    }
        '''
    ).replace("\n", "\n        ", 1).rstrip() + "\n"
    new = dedent(
        '''\
        val timedOut = AtomicBoolean(false)
        val timeoutFuture =
            try {
                transferTimeoutExecutor.schedule(
                    {
                        timedOut.set(true)
                        pipe[1].closeQuietly()
                    },
                    COVER_TRANSFER_TIMEOUT_MS,
                    TimeUnit.MILLISECONDS,
                )
            } catch (e: RejectedExecutionException) {
                pipe[0].closeQuietly()
                pipe[1].closeQuietly()
                L.w(e, "Cover-provider timeout executor rejected request")
                return null
            }

        return try {
            writerExecutor.execute { writeCoverToPipe(id, pipe[1], timedOut, timeoutFuture) }
            pipe[0]
        } catch (e: RejectedExecutionException) {
            timeoutFuture.cancel(false)
            pipe[0].closeQuietly()
            pipe[1].closeQuietly()
            L.w("Cover-provider writer queue is full; rejecting request")
            null
        }
    }

    private fun writeCoverToPipe(
        id: String,
        writeSide: ParcelFileDescriptor,
        timedOut: AtomicBoolean,
        timeoutFuture: ScheduledFuture<*>,
    ) {
        try {
            ParcelFileDescriptor.AutoCloseOutputStream(writeSide).use { output ->
                val coverDescriptor = runBlocking {
                    withTimeoutOrNull(COVER_LOAD_TIMEOUT_MS) {
                        withContext(Dispatchers.IO) {
                            when (
                                val result =
                                    SettingCovers.immutable(requireNotNull(context)).obtain(id)
                            ) {
                                is CoverResult.Hit -> result.cover.fd()
                                else -> null
                            }
                        }
                    }
                }
                if (coverDescriptor == null) {
                    L.d("Cover-provider request missed or timed out: $id")
                    return
                }
                val declaredSize = coverDescriptor.statSize
                if (declaredSize > MAX_COVER_BYTES) {
                    coverDescriptor.closeQuietly()
                    L.w("Cover-provider payload exceeded $MAX_COVER_BYTES bytes: $id")
                    return
                }
                ParcelFileDescriptor.AutoCloseInputStream(coverDescriptor).use { input ->
                    if (!copyBounded(input, output, MAX_COVER_BYTES)) {
                        L.w("Cover-provider payload exceeded $MAX_COVER_BYTES bytes: $id")
                    }
                }
            }
        } catch (e: IOException) {
            if (timedOut.get()) {
                L.w("Cover-provider transfer timed out: $id")
            } else {
                L.w(e, "Cover-provider transfer failed: $id")
            }
        } catch (e: RuntimeException) {
            if (timedOut.get()) {
                L.w("Cover-provider transfer timed out: $id")
            } else {
                L.w(e, "Cover-provider request failed: $id")
            }
        } finally {
            timeoutFuture.cancel(false)
        }
    }
        '''
    ).replace("\n", "\n        ", 1).rstrip() + "\n"
    replace_once(path, old, new)
    replace_once(
        path,
        """        private const val COVER_WRITER_QUEUE_SIZE = 8\n        private const val COVER_WRITER_KEEP_ALIVE_SECONDS = 30L\n        private const val COVER_LOAD_TIMEOUT_MS = 5_000L\n        private const val COVER_TRANSFER_TIMEOUT_MS = 10_000L\n        internal const val MAX_COVER_BYTES = 8L * 1024L * 1024L\n""",
        """        private const val COVER_WRITER_QUEUE_SIZE = 32\n        private const val COVER_WRITER_KEEP_ALIVE_SECONDS = 30L\n        private const val COVER_LOAD_TIMEOUT_MS = 5_000L\n        private const val COVER_TRANSFER_TIMEOUT_MS = 15_000L\n        internal const val MAX_COVER_BYTES = 32L * 1024L * 1024L\n""",
    )


def patch_visualizer_metrics() -> None:
    metrics = (
        "app/src/main/java/org/oxycblt/auxio/playback/ui/visualizer/"
        "VisualizerRuntimeMetrics.kt"
    )
    replace_once(
        metrics,
        """    private val lastReportMs = AtomicLong()\n\n    fun recordFrame""",
        """    private val lastReportMs = AtomicLong()\n\n    val isActive: Boolean\n        get() = journal?.hasActiveSession == true\n\n    fun recordFrame""",
    )

    coordinator = (
        "app/src/main/java/org/oxycblt/auxio/playback/ui/visualizer/"
        "VisualizerCoordinator.kt"
    )
    replace_once(
        coordinator,
        """                            if (generation == currentGeneration && currentSessionId == sessionId) {\n                                val copyStart = SystemClock.elapsedRealtimeNanos()\n                                val frame = waveform.copyOf()\n                                runtimeMetrics.recordFrame(\n                                    frame.size,\n                                    SystemClock.elapsedRealtimeNanos() - copyStart,\n                                    now,\n                                )\n""",
        """                            if (generation == currentGeneration && currentSessionId == sessionId) {\n                                val frame =\n                                    if (runtimeMetrics.isActive) {\n                                        val copyStart = SystemClock.elapsedRealtimeNanos()\n                                        waveform.copyOf().also { copy ->\n                                            runtimeMetrics.recordFrame(\n                                                copy.size,\n                                                SystemClock.elapsedRealtimeNanos() - copyStart,\n                                                now,\n                                            )\n                                        }\n                                    } else {\n                                        waveform.copyOf()\n                                    }\n""",
    )
    replace_once(
        coordinator,
        """                            if (generation == currentGeneration && currentSessionId == sessionId) {\n                                val copyStart = SystemClock.elapsedRealtimeNanos()\n                                val frame = fft.copyOf()\n                                runtimeMetrics.recordFrame(\n                                    frame.size,\n                                    SystemClock.elapsedRealtimeNanos() - copyStart,\n                                    now,\n                                )\n""",
        """                            if (generation == currentGeneration && currentSessionId == sessionId) {\n                                val frame =\n                                    if (runtimeMetrics.isActive) {\n                                        val copyStart = SystemClock.elapsedRealtimeNanos()\n                                        fft.copyOf().also { copy ->\n                                            runtimeMetrics.recordFrame(\n                                                copy.size,\n                                                SystemClock.elapsedRealtimeNanos() - copyStart,\n                                                now,\n                                            )\n                                        }\n                                    } else {\n                                        fft.copyOf()\n                                    }\n""",
    )


def add_direct_fs_regression_test() -> None:
    path = Path(
        "musikr/src/androidTest/java/org/oxycblt/musikr/fs/direct/"
        "DirectFSInstrumentedTest.kt"
    )
    if path.exists():
        raise SystemExit(f"STOP: test already exists: {path}")
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(
        dedent(
            '''\
            /*
             * Copyright (c) 2026 Auxio Project
             * DirectFSInstrumentedTest.kt is part of Auxio.
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
            import androidx.test.ext.junit.runners.AndroidJUnit4
            import androidx.test.platform.app.InstrumentationRegistry
            import java.io.File as JavaFile
            import kotlinx.coroutines.channels.Channel
            import kotlinx.coroutines.delay
            import kotlinx.coroutines.runBlocking
            import org.junit.Assert.assertEquals
            import org.junit.Assert.assertTrue
            import org.junit.Test
            import org.junit.runner.RunWith
            import org.oxycblt.musikr.fs.File as MusicFile
            import org.oxycblt.musikr.fs.Location

            @RunWith(AndroidJUnit4::class)
            class DirectFSInstrumentedTest {
                @Test
                fun boundedQueueFallsBackWithoutDroppingDirectories() = runBlocking {
                    val context = InstrumentationRegistry.getInstrumentation().targetContext
                    val parent = requireNotNull(context.getExternalFilesDir(null))
                    val root = JavaFile(parent, "directfs-queue-${System.nanoTime()}")
                    assertTrue(root.mkdirs())
                    val directoryCount = DirectFS.MAX_PENDING_DIRECTORIES * 2 + 1

                    try {
                        repeat(directoryCount) { index ->
                            val directory =
                                JavaFile(root, "album-${index.toString().padStart(5, '0')}")
                            assertTrue(directory.mkdir())
                            assertTrue(JavaFile(directory, "track-$index.mp3").createNewFile())
                        }

                        val rootUri = Uri.fromFile(root)
                        val location =
                            requireNotNull(Location.Unopened.from(context, rootUri).open(context))
                        val output = Channel<MusicFile>(Channel.RENDEZVOUS)
                        val directFs = DirectFS(listOf(location))
                        val exploration = directFs.explore(output)

                        // Hold consumers briefly so non-root workers block and the bounded
                        // directory queue deterministically reaches capacity.
                        delay(250)
                        val discovered = mutableListOf<MusicFile>()
                        for (file in output) discovered += file

                        assertTrue(exploration.await().isSuccess)
                        assertEquals(directoryCount, discovered.size)
                        assertTrue(directFs.drainSourceFailures().isEmpty())
                    } finally {
                        root.deleteRecursively()
                    }
                }
            }
            '''
        ),
        encoding="utf-8",
        newline="\n",
    )


def main() -> None:
    patch_direct_fs()
    patch_root_listing()
    patch_cover_provider()
    patch_visualizer_metrics()
    add_direct_fs_regression_test()


if __name__ == "__main__":
    main()
