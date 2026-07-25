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

/** A wrapper [FS] that rejects protected paths without guessing where users store music. */
internal class FilteredFS(
    private val delegate: FS,
    private val scope: CoroutineScope,
    @Suppress("UNUSED_PARAMETER") noisyDirs: Set<String>,
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

        val filterTask =
            scope.tryAsync(EmptyCoroutineContext) {
                try {
                    for (file in delegateChannel) {
                        val componentsLower = file.path.components.components.map { it.lowercase() }
                        val fullPathStr = file.path.toString().lowercase()

                        // Hard-deny protected roots only when they appear near the root, not as an
                        // arbitrary user folder name. Android app-runtime paths used by Auxio and
                        // the dedicated Topway identities remain exempt.
                        val isProtected =
                            componentsLower.withIndex().any { (idx, comp) ->
                                val isProtectedName =
                                    comp in
                                        setOf("android", "data", "system", "vendor", "proc", "dev")
                                val isSafeRuntime =
                                    fullPathStr.contains("org.oxycblt.auxio") ||
                                        fullPathStr.contains("com.tw.music") ||
                                        fullPathStr.contains("com.tw.media") ||
                                        fullPathStr.contains("com.dofun.variety")
                                isProtectedName && idx <= 3 && !isSafeRuntime
                            }
                        if (isProtected) continue

                        // A configured source is authoritative. Do not discard valid audio merely
                        // because it lives below Download, DCIM, Movies, Pictures, or a folder whose
                        // name lacks "music". This is essential for arbitrary
                        // /storage/emulated/0/* and removable-storage layouts.
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
