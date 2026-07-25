/*
 * Copyright (c) 2026 Auxio Project
 * IncrementalIndexPlannerTest.kt is part of Auxio.
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

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test
import org.oxycblt.musikr.cache.CacheResult
import org.oxycblt.musikr.cache.CachedFile
import org.oxycblt.musikr.cache.IncrementalCache
import org.oxycblt.musikr.cache.IncrementalScanCommit
import org.oxycblt.musikr.cache.IncrementalScanPlan
import org.oxycblt.musikr.cache.MutableCache
import org.oxycblt.musikr.fs.FS
import org.oxycblt.musikr.fs.FSUpdate
import org.oxycblt.musikr.fs.File
import org.oxycblt.musikr.fs.SourceAwareFS
import org.oxycblt.musikr.fs.SourceFingerprintStrength
import org.oxycblt.musikr.fs.SourceSnapshot
import org.oxycblt.musikr.library.MetadataProfile

class IncrementalIndexPlannerTest {
    @Test
    fun unavailableAdvisorySnapshotStillAttemptsRealEnumeration() = runBlocking {
        val sourceKey = "third-party:file:///storage/emulated/0/Audio"
        val original =
            FakeSourceAwareFs(
                snapshots =
                    listOf(
                        SourceSnapshot(
                            sourceKey = sourceKey,
                            sourceType = "DIRECT_FS",
                            rootUri = "file:///storage/emulated/0/Audio",
                            rootPath = "/storage/emulated/0/Audio",
                            available = false,
                            fingerprint = null,
                            fingerprintStrength = SourceFingerprintStrength.NONE,
                        )
                    )
            )
        val cache = FakeIncrementalCache()

        val prepared =
            IncrementalIndexPlanner.prepare(
                fs = original,
                cache = cache,
                withCache = true,
                profile = MetadataProfile.LEAN,
                configurationRevision = 1L,
                legacyWriteOnly = { it },
            )

        val observed = requireNotNull(cache.plannedSnapshots).single()
        assertFalse(observed.available)
        assertNull(observed.fingerprint)
        assertEquals(SourceFingerprintStrength.NONE, observed.fingerprintStrength)
        assertEquals(setOf(sourceKey), original.selectedSourceKeys)
        assertEquals(setOf(sourceKey), prepared.plan?.scanSourceKeys)
        assertTrue(prepared.plan?.unavailableSourceKeys.orEmpty().isEmpty())
    }

    @Test
    fun brokenPreflightFallsBackToCompleteAdapterScan() = runBlocking {
        val original = FakeSourceAwareFs(preflightFailure = IllegalStateException("OEM probe"))
        val cache = FakeIncrementalCache()

        val prepared =
            IncrementalIndexPlanner.prepare(
                fs = original,
                cache = cache,
                withCache = true,
                profile = MetadataProfile.LEAN,
                configurationRevision = 1L,
                legacyWriteOnly = { it },
            )

        assertSame(original, prepared.fs)
        assertSame(cache, prepared.cache)
        assertNull(prepared.plan)
        assertNull(cache.plannedSnapshots)
    }

    private class FakeSourceAwareFs(
        private val snapshots: List<SourceSnapshot> = emptyList(),
        private val preflightFailure: RuntimeException? = null,
    ) : SourceAwareFS {
        var selectedSourceKeys: Set<String>? = null

        override suspend fun sourceSnapshots(): List<SourceSnapshot> {
            preflightFailure?.let { throw it }
            return snapshots
        }

        override fun selectSources(sourceKeys: Set<String>): FS {
            selectedSourceKeys = sourceKeys
            return this
        }

        override suspend fun explore(files: Channel<File>): Deferred<Result<Unit>> {
            files.close()
            return CompletableDeferred(Result.success(Unit))
        }

        override fun track(): Flow<FSUpdate> = emptyFlow()
    }

    private class FakeIncrementalCache : MutableCache, IncrementalCache {
        var plannedSnapshots: List<SourceSnapshot>? = null
        private var active: IncrementalScanPlan? = null

        override suspend fun planScan(
            snapshots: List<SourceSnapshot>,
            force: Boolean,
            metadataProfile: MetadataProfile,
            configurationRevision: Long,
        ): IncrementalScanPlan {
            plannedSnapshots = snapshots
            return IncrementalScanPlan(
                scanId = "scan",
                scanSources = snapshots.filter { it.available },
                reuseSourceKeys = emptySet(),
                unavailableSourceKeys =
                    snapshots.filterNot { it.available }.mapTo(linkedSetOf()) { it.sourceKey },
                metadataProfile = metadataProfile,
                configurationRevision = configurationRevision,
                force = force,
            )
        }

        override suspend fun beginScan(plan: IncrementalScanPlan) {
            active = plan
        }

        override suspend fun markSeen(file: File, cachedFile: CachedFile?) = Unit

        override fun reusedCachedFiles(sourceKeys: Set<String>): Flow<CachedFile> = emptyFlow()

        override suspend fun stage(cachedFile: CachedFile): Boolean = true

        override suspend fun markSourceFailed(sourceKey: String, detail: String) = Unit

        override suspend fun commitScan(): IncrementalScanCommit {
            val plan = requireNotNull(active)
            active = null
            return IncrementalScanCommit(
                scanId = plan.scanId,
                committedSources = plan.scanSourceKeys,
                reusedSources = plan.reuseSourceKeys,
                unavailableSources = plan.unavailableSourceKeys,
                failedSources = emptyMap(),
                changedRows = 0,
                removedRows = 0,
                metadataProfile = plan.metadataProfile,
            )
        }

        override suspend fun abortScan(cause: Throwable?) {
            active = null
        }

        override suspend fun invalidateSource(sourceKey: String?) = Unit

        override fun activePlan(): IncrementalScanPlan? = active

        override suspend fun read(file: File): CacheResult = CacheResult.Miss(file)

        override suspend fun snapshot(): List<CachedFile> = emptyList()

        override suspend fun write(cachedFile: CachedFile) = Unit

        override suspend fun cleanup(excluding: List<CachedFile>) = Unit
    }
}
