package org.oxycblt.musikr.cache.db

import android.content.Context
import androidx.room.withTransaction
import org.oxycblt.musikr.cache.CachedFile
import org.oxycblt.musikr.cache.IncrementalCache
import org.oxycblt.musikr.cache.MutableCache
import org.oxycblt.musikr.cache.StartupProjectionCache
import org.oxycblt.musikr.fs.File

/**
 * Transactional batching adapter for the Room cache.
 *
 * The underlying incremental store still owns generation visibility and reconciliation. This
 * adapter only groups write calls into bounded Room transactions so a cold scan does not pay one
 * durable commit/fsync per extracted track.
 */
class BatchingMutableCache
private constructor(
    private val db: CacheDatabase,
    private val inner: MutableDBCache,
) : MutableCache,
    StartupProjectionCache by inner,
    IncrementalCache by inner {
    override suspend fun read(file: File) = inner.read(file)

    override suspend fun snapshot() = inner.snapshot()

    override suspend fun write(cachedFile: CachedFile) = inner.write(cachedFile)

    override suspend fun writeAll(cachedFiles: List<CachedFile>) {
        cachedFiles.chunked(WRITE_BATCH_SIZE).forEach { batch ->
            db.withTransaction {
                for (cachedFile in batch) {
                    inner.write(cachedFile)
                }
            }
        }
    }

    override suspend fun cleanup(excluding: List<CachedFile>) = inner.cleanup(excluding)

    override suspend fun populateNormalizedLibrary(): Int = inner.populateNormalizedLibrary()

    override suspend fun prepareStartupProjections(): Int = inner.prepareStartupProjections()

    companion object {
        internal const val WRITE_BATCH_SIZE = 128

        fun from(context: Context): BatchingMutableCache = from(CacheDatabase.from(context))

        internal fun from(db: CacheDatabase): BatchingMutableCache =
            BatchingMutableCache(db, MutableDBCache.from(db))
    }
}
