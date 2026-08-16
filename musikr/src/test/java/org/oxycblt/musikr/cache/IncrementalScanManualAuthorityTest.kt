/*
 * Copyright (c) 2026 Auxio Project
 * IncrementalScanManualAuthorityTest.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.oxycblt.musikr.cache

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.oxycblt.musikr.fs.File
import org.oxycblt.musikr.fs.SourceFingerprintStrength
import org.oxycblt.musikr.fs.SourceSnapshot
import org.oxycblt.musikr.library.MetadataProfile

class IncrementalScanManualAuthorityTest {
    @Test
    fun `manual lean planning reuses age-only expired advisory source`() = runBlocking {
        val source = source()
        val cache = TestCache(expiredPlan(source, MetadataProfile.LEAN))

        val planned =
            cache.planScan(
                snapshots = listOf(source),
                force = false,
                metadataProfile = MetadataProfile.LEAN,
                configurationRevision = 1L,
                allowAdvisoryExpiry = false,
            )

        assertTrue(planned.scanSources.isEmpty())
        assertEquals(setOf(source.sourceKey), planned.reuseSourceKeys)
        assertTrue(planned.scanReasons.isEmpty())
        assertFalse(planned.enrichmentOnly)
    }

    @Test
    fun `manual full planning downgrades age-only expiry to optional enrichment`() = runBlocking {
        val source = source()
        val cache = TestCache(expiredPlan(source, MetadataProfile.FULL))

        val planned =
            cache.planScan(
                snapshots = listOf(source),
                force = false,
                metadataProfile = MetadataProfile.FULL,
                configurationRevision = 1L,
                allowAdvisoryExpiry = false,
            )

        assertEquals(listOf(source), planned.scanSources)
        assertEquals(
            SourceScanReason.METADATA_PROFILE_UPGRADE,
            planned.scanReasons[source.sourceKey],
        )
        assertTrue(planned.enrichmentOnly)
    }

    @Test
    fun `automatic planning retains advisory expiry validation`() = runBlocking {
        val source = source()
        val cache = TestCache(expiredPlan(source, MetadataProfile.LEAN))

        val planned =
            cache.planScan(
                snapshots = listOf(source),
                force = false,
                metadataProfile = MetadataProfile.LEAN,
                configurationRevision = 1L,
                allowAdvisoryExpiry = true,
            )

        assertEquals(listOf(source), planned.scanSources)
        assertEquals(
            SourceScanReason.ADVISORY_FINGERPRINT_EXPIRED,
            planned.scanReasons[source.sourceKey],
        )
    }

    private fun source() =
        SourceSnapshot(
            sourceKey = "direct:usb0",
            sourceType = "DIRECT_FS",
            rootUri = "file:///storage/usbdisk0",
            rootPath = "/storage/usbdisk0",
            available = true,
            fingerprint = "stable",
            fingerprintStrength = SourceFingerprintStrength.ADVISORY,
        )

    private fun expiredPlan(source: SourceSnapshot, profile: MetadataProfile) =
        IncrementalScanPlan(
            scanId = "scan",
            scanSources = listOf(source),
            reuseSourceKeys = emptySet(),
            unavailableSourceKeys = emptySet(),
            metadataProfile = profile,
            configurationRevision = 1L,
            force = false,
            scanReasons =
                mapOf(source.sourceKey to SourceScanReason.ADVISORY_FINGERPRINT_EXPIRED),
        )

    private class TestCache(private val plan: IncrementalScanPlan) : IncrementalCache {
        override suspend fun planScan(
            snapshots: List<SourceSnapshot>,
            force: Boolean,
            metadataProfile: MetadataProfile,
            configurationRevision: Long,
        ): IncrementalScanPlan = plan

        override suspend fun beginScan(plan: IncrementalScanPlan) = Unit

        override suspend fun markSeen(file: File, cachedFile: CachedFile?) = Unit

        override fun reusedCachedFiles(sourceKeys: Set<String>): Flow<CachedFile> = emptyFlow()

        override suspend fun stage(cachedFile: CachedFile): Boolean = true

        override suspend fun markSourceFailed(sourceKey: String, detail: String) = Unit

        override suspend fun commitScan(commitGuard: () -> Boolean): IncrementalScanCommit =
            error("Not used")

        override suspend fun abortScan(cause: Throwable?) = Unit

        override suspend fun invalidateSource(sourceKey: String?) = Unit

        override fun activePlan(): IncrementalScanPlan? = null
    }
}
