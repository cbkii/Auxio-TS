/*
 * Copyright (c) 2026 Auxio Project
 * BatchingMutableCacheTest.kt is part of Auxio.
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
import android.net.Uri
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
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
        db =
            Room.inMemoryDatabaseBuilder(context, CacheDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        cache = BatchingMutableCache.from(db)
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun activeScanWriteAllStagesPendingAndSeenRowsBeforeCommit() = runBlocking {
        val source = snapshot("v1")
        val plan = cache.planScan(listOf(source), false, MetadataProfile.LEAN, 1L)
        cache.beginScan(plan)
        val files =
            List(BatchingMutableCache.WRITE_BATCH_SIZE + 1) { index ->
                cachedFile("track-$index.mp3")
            }

        cache.writeAll(files)

        assertEquals(
            BatchingMutableCache.WRITE_BATCH_SIZE + 1,
            db.incrementalDao().pendingCount(plan.scanId, source.sourceKey),
        )
        assertEquals(
            BatchingMutableCache.WRITE_BATCH_SIZE + 1,
            db.incrementalDao().seenCount(plan.scanId, source.sourceKey),
        )

        cache.commitScan()
        assertEquals(
            BatchingMutableCache.WRITE_BATCH_SIZE + 1,
            db.incrementalLibraryDao().songCount(),
        )
    }

    @Test
    fun abortDropsStagedRowsWithoutPublishingThem() = runBlocking {
        val source = snapshot("v1")
        val plan = cache.planScan(listOf(source), false, MetadataProfile.LEAN, 1L)
        cache.beginScan(plan)
        cache.writeAll(listOf(cachedFile("alpha.mp3")))

        cache.abortScan(IllegalStateException("cancelled"))

        assertEquals(0, db.incrementalDao().pendingCount(plan.scanId, source.sourceKey))
        assertEquals(0, db.incrementalLibraryDao().songCount())
        assertNull(cache.activePlan())
    }

    @Test
    fun writeAllWithoutActiveScanPersistsLegacyRows() = runBlocking {
        val files = listOf(cachedFile("alpha.mp3"), cachedFile("beta.mp3"))

        cache.writeAll(files)

        assertNotNull(db.readDao().selectSongByUri(files[0].file.uri))
        assertNotNull(db.readDao().selectSongByUri(files[1].file.uri))
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
