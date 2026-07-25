/*
 * Copyright (c) 2026 Auxio Project
 * RepositoryIndexRequestQueue.kt is part of Auxio.
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

package org.oxycblt.auxio.music

import org.oxycblt.musikr.library.MetadataProfile

internal data class RepositoryIndexRequest(
    val withCache: Boolean,
    val metadataProfile: MetadataProfile?,
) {
    fun dispatch(worker: MusicRepository.IndexingWorker) {
        if (metadataProfile != null) {
            worker.requestIndex(withCache, metadataProfile)
        } else {
            worker.requestIndex(withCache)
        }
    }
}

/** Coalesces requests received before the service indexing worker is attached. */
internal class RepositoryIndexRequestQueue {
    private var pending: RepositoryIndexRequest? = null

    fun offer(request: RepositoryIndexRequest) {
        pending = merge(pending, request)
    }

    fun drain(): RepositoryIndexRequest? = pending.also { pending = null }

    private fun merge(
        current: RepositoryIndexRequest?,
        incoming: RepositoryIndexRequest,
    ): RepositoryIndexRequest {
        if (current == null) return incoming
        return RepositoryIndexRequest(
            // A requested cache bypass must never be weakened by a later cached refresh.
            withCache = current.withCache && incoming.withCache,
            metadataProfile = strongerProfile(current.metadataProfile, incoming.metadataProfile),
        )
    }

    private fun strongerProfile(
        first: MetadataProfile?,
        second: MetadataProfile?,
    ): MetadataProfile? =
        when {
            first == MetadataProfile.FULL || second == MetadataProfile.FULL -> MetadataProfile.FULL
            first == MetadataProfile.LEAN || second == MetadataProfile.LEAN -> MetadataProfile.LEAN
            else -> null
        }
}
