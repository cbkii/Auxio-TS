/*
 * Copyright (c) 2026 Auxio Project
 * ArtworkEnrichmentRevisionTest.kt is part of Auxio.
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
import org.junit.Assert.assertFalse
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
class ArtworkEnrichmentRevisionTest {
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
    fun `legacy full ledger is re-enriched once for corrected artwork revision`() = runBlocking {
        val source = snapshot()
        val initial = store.planScan(listOf(source), false, MetadataProfile.LEAN, 1L)
        store.beginScan(initial)
        store.stage(cachedFile())
        store.commitScan()

        val legacy = requireNotNull(db.incrementalDao().sourceLedger(source.sourceKey))
        db.incrementalDao()
            .upsertSourceLedger(
                legacy.copy(
                    committedProfile = MetadataProfile.FULL.name,
                    enrichmentRevision = 1L,
                )
            )

        val repair = store.planScan(listOf(source), false, MetadataProfile.FULL, 1L)
        assertTrue(repair.hasWork)
        assertTrue(repair.enrichmentOnly)

        store.beginScan(repair)
        store.stage(cachedFile())
        store.commitScan()

        val repaired = requireNotNull(db.incrementalDao().sourceLedger(source.sourceKey))
        assertTrue(repaired.enrichmentRevision > 1L)
        assertFalse(store.planScan(listOf(source), false, MetadataProfile.FULL, 1L).hasWork)
    }

    private fun snapshot(): SourceSnapshot {
        val root = "/storage/usbdisk0"
        val volume = Volume.ThirdParty(Uri.parse("file://$root"))
        return SourceSnapshot(
            sourceKey = SourceIdentity.forVolume(volume),
            sourceType = "DIRECT_FS",
            rootUri = "file://$root",
            rootPath = root,
            available = true,
            fingerprint = "stable",
            fingerprintStrength = SourceFingerprintStrength.ADVISORY,
            observedAtMs = 1_000L,
        )
    }

    private fun cachedFile(): CachedFile {
        val root = "/storage/usbdisk0"
        val volume = Volume.ThirdParty(Uri.parse("file://$root"))
        val file =
            File(
                uri = Uri.parse("file://$root/alpha.mp3"),
                path = Path(volume, Components.parseUnix("alpha.mp3")),
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
