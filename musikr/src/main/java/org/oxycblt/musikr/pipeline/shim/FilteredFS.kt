/*
 * Copyright (c) 2026 Auxio Project
 * FilteredFS.kt is part of Auxio.
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

package org.oxycblt.musikr.pipeline.shim

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import org.oxycblt.musikr.fs.FS
import org.oxycblt.musikr.fs.FSUpdate
import org.oxycblt.musikr.fs.File
import org.oxycblt.musikr.util.tryAsync

/** A wrapper [FS] that filters files based on their path components. */
internal class FilteredFS(
    private val delegate: FS,
    private val scope: CoroutineScope,
    private val noisyDirs: Set<String>,
    private val pathKeywords: List<String> = emptyList(),
) : FS {

    override suspend fun explore(files: Channel<File>): Deferred<Result<Unit>> {
        val delegateChannel = Channel<File>(Channel.UNLIMITED)
        val delegateTask =
            try {
                delegate.explore(delegateChannel)
            } catch (e: CancellationException) {
                throw e
            } catch (t: Throwable) {
                delegateChannel.close(t)
                files.close(t)
                return CompletableDeferred(Result.failure(t))
            }

        val filterTask =
            scope.tryAsync(Dispatchers.Default) {
                try {
                    for (file in delegateChannel) {
                        val isNoisy = file.path.components.components.any { it in noisyDirs }
                        if (isNoisy) continue
                        // If pathKeywords are configured, require the full path to contain
                        // at least one keyword (case-insensitive). This prevents scanning
                        // huge irrelevant directory trees on TS18.
                        if (pathKeywords.isNotEmpty()) {
                            val fullPath = file.path.toString().lowercase()
                            if (pathKeywords.none { fullPath.contains(it) }) continue
                        }
                        files.send(file)
                    }
                    files.close()
                } catch (t: Throwable) {
                    delegateChannel.close(t)
                    delegateTask.cancel(CancellationException("FilteredFS forwarding failed", t))
                    files.close(t)
                    throw t
                }
            }

        return scope.tryAsync(Dispatchers.Default) {
            try {
                val delegateResult =
                    try {
                        delegateTask.await()
                    } catch (e: CancellationException) {
                        throw e
                    } catch (t: Throwable) {
                        Result.failure(t)
                    }

                // FS implementations normally close their output, but FilteredFS also owns this
                // private delegate channel so a completed-but-leaky delegate cannot deadlock the
                // classifier pipeline. Preserve failure causes on both the private and downstream
                // channels where kotlinx.coroutines channels support them.
                delegateChannel.close(delegateResult.exceptionOrNull())

                val filterResult = filterTask.await()
                filterResult.getOrThrow()
                delegateResult.getOrThrow()
            } finally {
                delegateChannel.cancel()
            }
        }
    }

    override fun track(): Flow<FSUpdate> = delegate.track()
}
