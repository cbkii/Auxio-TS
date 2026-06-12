/*
 * Copyright (c) 2026 Auxio Project
 * CompositeFS.kt is part of Auxio.
 */

package org.oxycblt.musikr.fs

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.merge
import org.oxycblt.musikr.util.tryAsync

class CompositeFS(private val delegates: List<FS>) : FS {
    override suspend fun explore(files: Channel<File>): Deferred<Result<Unit>> = coroutineScope {
        tryAsync(Dispatchers.IO) {
            delegates.forEach { delegate ->
                val delegateChannel = Channel<File>(Channel.UNLIMITED)
                val delegateTask = delegate.explore(delegateChannel)
                for (file in delegateChannel) {
                    files.send(file)
                }
                delegateTask.await().getOrThrow()
            }
            files.close()
        }
    }

    override fun track(): Flow<FSUpdate> = delegates.map { it.track() }.mergeOrEmpty()
}

object EmptyFS : FS {
    override suspend fun explore(files: Channel<File>): Deferred<Result<Unit>> = coroutineScope {
        tryAsync(Dispatchers.IO) {
            files.close()
        }
    }

    override fun track(): Flow<FSUpdate> = emptyFlow()
}

private fun List<Flow<FSUpdate>>.mergeOrEmpty(): Flow<FSUpdate> =
    when (size) {
        0 -> emptyFlow()
        1 -> first()
        else -> merge(*toTypedArray())
    }
