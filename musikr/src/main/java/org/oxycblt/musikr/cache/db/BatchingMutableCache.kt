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

/** Adapts cache batches to bounded Room transactions and bulk DAO operations. */
class BatchingMutableCache
private constructor(
    private val db: CacheDatabase,
    private val inner: MutableDBCache,
    private val batchWriter: IncrementalBatchWriter,
) : MutableCache, StartupProjectionCache by inner, IncrementalCache by inner {
    override suspend fun read(file: File) = inner.read(file)

    override suspend fun snapshot() = inner.snapshot()

    override suspend fun write(cachedFile: CachedFile) {
        writeAll(listOf(cachedFile))
    }

    override suspend fun writeAll(cachedFiles: List<CachedFile>) {
        cachedFiles.chunked(WRITE_BATCH_SIZE).forEach { batch ->
            db.withTransaction {
                val plan = inner.activePlan()
                if (plan == null) {
                    batchWriter.writeLegacyAll(batch)
                } else {
                    batchWriter.writeLegacyAll(batchWriter.stageAll(plan, batch))
                }
            }
        }
    }

    override suspend fun commitScan(commitGuard: () -> Boolean): IncrementalScanCommit =
        inner.commitScan(commitGuard)

    override suspend fun abortScan(cause: Throwable?) {
        inner.abortScan(cause)
    }

    override suspend fun cleanup(excluding: List<CachedFile>) {
        inner.cleanup(excluding)
    }

    override suspend fun populateNormalizedLibrary(): Int = inner.populateNormalizedLibrary()

    override suspend fun prepareStartupProjections(): Int = inner.prepareStartupProjections()

    companion object {
        internal const val WRITE_BATCH_SIZE = 128

        fun from(context: Context): BatchingMutableCache = from(CacheDatabase.from(context))

        internal fun from(db: CacheDatabase): BatchingMutableCache =
            BatchingMutableCache(db, MutableDBCache.from(db), IncrementalBatchWriter(db))
    }
}
