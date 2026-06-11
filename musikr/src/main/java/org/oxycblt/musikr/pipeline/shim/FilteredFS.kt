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
) : FS {

    override suspend fun explore(files: Channel<File>): Deferred<Result<Unit>> {
        val delegateChannel = Channel<File>(Channel.UNLIMITED)
        val delegateTask = delegate.explore(delegateChannel)

        val filterTask =
            scope.tryAsync(Dispatchers.Default) {
                for (file in delegateChannel) {
                    val isNoisy = file.path.components.components.any { it in noisyDirs }
                    if (!isNoisy) {
                        files.send(file)
                    }
                }
            }

        return scope.tryAsync(Dispatchers.Default) {
            // delegateTask reports failures as Result values, so this task owns final channel
            // closure: close delegateChannel with any delegate cause, await filterTask, then
            // close downstream files with the first failure before surfacing it via getOrThrow().
            val delegateResult =
                try {
                    delegateTask.await()
                } catch (e: Throwable) {
                    delegateChannel.close(e)
                    val filterResult = filterTask.await()
                    files.close(filterResult.exceptionOrNull() ?: e)
                    filterResult.getOrThrow()
                    throw e
                }
            delegateChannel.close(delegateResult.exceptionOrNull())
            val filterResult = filterTask.await()
            val failure = filterResult.exceptionOrNull() ?: delegateResult.exceptionOrNull()
            files.close(failure)
            filterResult.getOrThrow()
            delegateResult.getOrThrow()
        }
    }

    override fun track(): Flow<FSUpdate> = delegate.track()
}
