/*
 * Copyright (c) 2026 Auxio Project
 * BatchingMutableCache.kt is part of Auxio.
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

package org.oxycblt.musikr.cache.db

import android.content.Context
import androidx.room.withTransaction
import org.oxycblt.musikr.cache.CachedFile
import org.oxycblt.musikr.cache.IncrementalCache
import org.oxycblt.musikr.cache.IncrementalScanCommit
import org.oxycblt.musikr.cache.MutableCache
import org.oxycblt.musikr.cache.StartupProjectionCache
import org.oxycblt.musikr.fs.File

/** Groups active scan cache writes into bounded Room transactions. */
class BatchingMutableCache
private constructor(private val db: CacheDatabase, private val inner: MutableDBCache) :
    MutableCache, StartupProjectionCache by inner, IncrementalCache by inner {
    private val pending = ArrayList<CachedFile>(WRITE_BATCH_SIZE)

    override suspend fun read(file: File) = inner.read(file)

    override suspend fun snapshot() = inner.snapshot()

    override suspend fun write(cachedFile: CachedFile) {
        if (inner.activePlan() == null) {
            inner.write(cachedFile)
            return
        }
        pending += cachedFile
        if (pending.size >= WRITE_BATCH_SIZE) flush()
    }

    override suspend fun writeAll(cachedFiles: List<CachedFile>) {
        if (inner.activePlan() == null) {
            cachedFiles.chunked(WRITE_BATCH_SIZE).forEach { batch ->
                db.withTransaction { batch.forEach { inner.write(it) } }
            }
            return
        }
        for (cachedFile in cachedFiles) write(cachedFile)
    }

    override suspend fun commitScan(commitGuard: () -> Boolean): IncrementalScanCommit {
        flush()
        return inner.commitScan(commitGuard)
    }

    override suspend fun abortScan(cause: Throwable?) {
        pending.clear()
        inner.abortScan(cause)
    }

    override suspend fun cleanup(excluding: List<CachedFile>) {
        flush()
        inner.cleanup(excluding)
    }

    override suspend fun populateNormalizedLibrary(): Int = inner.populateNormalizedLibrary()

    override suspend fun prepareStartupProjections(): Int = inner.prepareStartupProjections()

    private suspend fun flush() {
        if (pending.isEmpty()) return
        db.withTransaction { pending.forEach { inner.write(it) } }
        pending.clear()
    }

    companion object {
        internal const val WRITE_BATCH_SIZE = 128

        fun from(context: Context): BatchingMutableCache = from(CacheDatabase.from(context))

        internal fun from(db: CacheDatabase): BatchingMutableCache =
            BatchingMutableCache(db, MutableDBCache.from(db))
    }
}
