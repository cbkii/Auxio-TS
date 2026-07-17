/*
 * Copyright (c) 2026 Auxio Project
 * IncrementalScanStoreTest.kt is part of Auxio.
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
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
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
class IncrementalScanStoreTest {
    private lateinit var db: CacheDatabase
    private lateinit var store: IncrementalScanStore

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db =
            Room.inMemoryDatabaseBuilder(context, CacheDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        store = IncrementalScanStore(db, db.readDao(), db.writeDao(), db.incrementalDao())
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun `unchanged source reuses committed generation without scanning`() = runBlocking {
        val source = snapshot("fingerprint-1")
        val first = store.planScan(listOf(source), false, MetadataProfile.LEAN, 1L)
        assertTrue(first.hasWork)
        store.beginScan(first)
        store.stage(cachedFile("alpha.mp3", modifiedMs = 1L))
        store.commitScan()

        val second = store.planScan(listOf(source), false, MetadataProfile.LEAN, 1L)
        assertFalse(second.hasWork)
        assertEquals(setOf(source.sourceKey), second.reuseSourceKeys)
        assertEquals(1, db.incrementalLibraryDao().songCount())
    }

    @Test
    fun `changed file publishes a new generation atomically`() = runBlocking {
        val first = store.planScan(listOf(snapshot("v1")), false, MetadataProfile.LEAN, 1L)
        store.beginScan(first)
        store.stage(cachedFile("alpha.mp3", modifiedMs = 1L))
        store.commitScan()

        val changed = store.planScan(listOf(snapshot("v2")), false, MetadataProfile.LEAN, 1L)
        assertTrue(changed.hasWork)
        store.beginScan(changed)
        store.stage(cachedFile("alpha.mp3", modifiedMs = 2L))
        store.commitScan()

        assertEquals(
            2L,
            db.readDao()
                .selectSongByUri(Uri.parse("file:///storage/usbdisk0/alpha.mp3"))
                ?.modifiedMs,
        )
        assertEquals(1, db.incrementalLibraryDao().songCount())
    }

    @Test
    fun `successful generation reconciles deleted files in database`() = runBlocking {
        val first = store.planScan(listOf(snapshot("v1")), false, MetadataProfile.LEAN, 1L)
        store.beginScan(first)
        store.stage(cachedFile("alpha.mp3", modifiedMs = 1L))
        store.commitScan()
        assertEquals(1, db.incrementalLibraryDao().songCount())

        val empty = store.planScan(listOf(snapshot("v2")), false, MetadataProfile.LEAN, 1L)
        store.beginScan(empty)
        store.commitScan()

        assertEquals(0, db.incrementalLibraryDao().songCount())
        assertNull(db.readDao().selectSongByUri(Uri.parse("file:///storage/usbdisk0/alpha.mp3")))
    }

    @Test
    fun `cancelled scan preserves last known good generation`() = runBlocking {
        val first = store.planScan(listOf(snapshot("v1")), false, MetadataProfile.LEAN, 1L)
        store.beginScan(first)
        store.stage(cachedFile("alpha.mp3", modifiedMs = 1L))
        store.commitScan()

        val interrupted = store.planScan(listOf(snapshot("v2")), false, MetadataProfile.FULL, 1L)
        store.beginScan(interrupted)
        store.stage(cachedFile("alpha.mp3", modifiedMs = 2L))
        store.abortScan(CancellationException("process stopped"))

        assertEquals(1, db.incrementalLibraryDao().songCount())
        assertEquals(
            1L,
            db.readDao()
                .selectSongByUri(Uri.parse("file:///storage/usbdisk0/alpha.mp3"))
                ?.modifiedMs,
        )
        assertTrue(db.incrementalDao().sourceLedger(snapshot("v1").sourceKey)?.incomplete == true)
    }

    @Test
    fun `temporary unmount never becomes deletion`() = runBlocking {
        val mounted = snapshot("v1")
        val first = store.planScan(listOf(mounted), false, MetadataProfile.LEAN, 1L)
        store.beginScan(first)
        store.stage(cachedFile("alpha.mp3", modifiedMs = 1L))
        store.commitScan()

        val absent = mounted.copy(available = false, fingerprint = null)
        val plan = store.planScan(listOf(absent), false, MetadataProfile.LEAN, 1L)

        assertFalse(plan.hasWork)
        assertEquals(setOf(mounted.sourceKey), plan.unavailableSourceKeys)
        assertEquals(1, db.readDao().selectAllSongs().size)
        assertFalse(db.incrementalDao().sourceLedger(mounted.sourceKey)?.available ?: true)
    }

    @Test
    fun `one failed source does not invalidate another source`() = runBlocking {
        val usb0 = snapshot("usb0", "/storage/usbdisk0")
        val usb1 = snapshot("usb1", "/storage/usbdisk1")
        val first = store.planScan(listOf(usb0, usb1), false, MetadataProfile.LEAN, 1L)
        store.beginScan(first)
        store.stage(cachedFile("alpha.mp3", 1L, "/storage/usbdisk0"))
        store.stage(cachedFile("beta.mp3", 1L, "/storage/usbdisk1"))
        store.commitScan()

        val next =
            store.planScan(
                listOf(usb0.copy(available = false, fingerprint = null), usb1),
                false,
                MetadataProfile.LEAN,
                1L,
            )
        assertEquals(setOf(usb0.sourceKey), next.unavailableSourceKeys)
        assertEquals(setOf(usb1.sourceKey), next.reuseSourceKeys)
        assertEquals(2, db.readDao().selectAllSongs().size)
    }

    private fun snapshot(fingerprint: String, root: String = "/storage/usbdisk0"): SourceSnapshot {
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

    private fun cachedFile(
        name: String,
        modifiedMs: Long,
        root: String = "/storage/usbdisk0",
    ): CachedFile {
        val volume = Volume.ThirdParty(Uri.parse("file://$root"))
        val file =
            File(
                uri = Uri.parse("file://$root/$name"),
                path = Path(volume, Components.parseUnix(name)),
                addedMs = FixedAddedMs,
                modifiedMs = modifiedMs,
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
