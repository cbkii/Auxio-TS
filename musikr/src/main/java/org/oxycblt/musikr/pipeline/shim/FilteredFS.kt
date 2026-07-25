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

import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import org.oxycblt.musikr.fs.FS
import org.oxycblt.musikr.fs.FSUpdate
import org.oxycblt.musikr.fs.File
import org.oxycblt.musikr.util.tryAsync

/** Compatibility wrapper that preserves bounded channel and failure propagation. */
internal class FilteredFS(
    private val delegate: FS,
    private val scope: CoroutineScope,
    @Suppress("UNUSED_PARAMETER") noisyDirs: Set<String> = emptySet(),
    @Suppress("UNUSED_PARAMETER") pathKeywords: List<String> = emptyList(),
) : FS {
    override suspend fun explore(files: Channel<File>): Deferred<Result<Unit>> {
        val delegateChannel =
            Channel<File>(org.oxycblt.musikr.pipeline.PipelinePolicy.BUFFER_CAPACITY)
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

        val forwardingTask =
            scope.tryAsync(EmptyCoroutineContext) {
                try {
                    // The selected MediaStore query, SAF tree, or canonical DirectFS root is the
                    // source authority. Do not guess from child folder names: users may keep valid
                    // audio anywhere below /storage/emulated/0 or removable storage. Musikr's
                    // MIME/extension classifier rejects non-audio rows after this bounded forward.
                    for (file in delegateChannel) files.send(file)
                    files.close()
                } catch (t: Throwable) {
                    delegateChannel.close(t)
                    delegateTask.cancel(CancellationException("FilteredFS forwarding failed", t))
                    files.close(t)
                    throw t
                }
            }

        return scope.tryAsync(EmptyCoroutineContext) {
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

                val forwardingResult = forwardingTask.await()
                forwardingResult.getOrThrow()
                delegateResult.getOrThrow()
            } finally {
                delegateChannel.cancel()
            }
        }
    }

    override fun track(): Flow<FSUpdate> = delegate.track()
}
