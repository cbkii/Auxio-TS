package org.oxycblt.musikr.cache.db

import android.content.Context
import android.net.Uri
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.oxycblt.musikr.cache.CachedFile
import org.oxycblt.musikr.fs.AddedMs
import org.oxycblt.musikr.fs.Components
import org.oxycblt.musikr.fs.File
import org.oxycblt.musikr.fs.Path
import org.oxycblt.musikr.fs.SourceFingerprintStrength
import org.oxycblt.musikr.fs.SourceIdentity
import org.oxycblt.musikr.fs.SourceSnapshot
import org.oxycblt.musikr.fs.Volume
import org.oxycblt.musikr.library.MetadataProfile
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
class BatchingMutableCacheTest {
    private lateinit var db: CacheDatabase
    private lateinit var cache: BatchingMutableCache

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, CacheDatabase::class.java).allowMainThreadQueries().build()
        cache = BatchingMutableCache.from(db)
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun activeScanFlushesAtBatchBoundaryAndBeforeCommit() = runBlocking {
        val source = snapshot("v1")
        val plan = cache.planScan(listOf(source), false, MetadataProfile.LEAN, 1L)
        cache.beginScan(plan)

        repeat(BatchingMutableCache.WRITE_BATCH_SIZE - 1) { index ->
            cache.write(cachedFile("track-$index.mp3"))
        }
        assertEquals(0, db.incrementalDao().pendingCount(plan.scanId, source.sourceKey))

        cache.write(cachedFile("track-last-in-batch.mp3"))
        assertEquals(
            BatchingMutableCache.WRITE_BATCH_SIZE,
            db.incrementalDao().pendingCount(plan.scanId, source.sourceKey),
        )

        cache.write(cachedFile("track-remainder.mp3"))
        assertEquals(
            BatchingMutableCache.WRITE_BATCH_SIZE,
            db.incrementalDao().pendingCount(plan.scanId, source.sourceKey),
        )

        cache.commitScan()
        assertEquals(BatchingMutableCache.WRITE_BATCH_SIZE + 1, db.incrementalLibraryDao().songCount())
    }

    @Test
    fun abortDropsUnflushedRowsWithoutPublishingThem() = runBlocking {
        val source = snapshot("v1")
        val plan = cache.planScan(listOf(source), false, MetadataProfile.LEAN, 1L)
        cache.beginScan(plan)
        cache.write(cachedFile("alpha.mp3"))

        cache.abortScan(IllegalStateException("cancelled"))

        assertEquals(0, db.incrementalDao().pendingCount(plan.scanId, source.sourceKey))
        assertEquals(0, db.incrementalLibraryDao().songCount())
        assertNull(cache.activePlan())
    }

    private fun snapshot(fingerprint: String): SourceSnapshot {
        val root = "/storage/usbdisk0"
        val volume = Volume.ThirdParty(Uri.parse("file://$root"))
        return SourceSnapshot(
            sourceKey = SourceIdentity.forVolume(volume),
            sourceType = "DIRECT_FS",
            rootUri = "file://$root",
            rootPath = root,
            available = true,
            fingerprint = fingerprint,
            fingerprintStrength = SourceFingerprintStrength.ADVISORY,
            observedAtMs = 1_000L,
        )
    }

    private fun cachedFile(name: String): CachedFile {
        val root = "/storage/usbdisk0"
        val volume = Volume.ThirdParty(Uri.parse("file://$root"))
        val file =
            File(
                uri = Uri.parse("file://$root/$name"),
                path = Path(volume, Components.parseUnix(name)),
                addedMs = FixedAddedMs,
                modifiedMs = 1L,
                mimeType = "audio/mpeg",
                size = 100L,
                parent = null,
            )
        return CachedFile(file, audio = null, addedMs = 10L)
    }

    private object FixedAddedMs : AddedMs {
        override suspend fun resolve(): Long = 10L
    }
}
