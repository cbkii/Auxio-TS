/*
 * Copyright (c) 2026 Auxio Project
 * SourceAuthorityScopePolicyTest.kt is part of Auxio.
 */

package org.oxycblt.auxio.music

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.oxycblt.auxio.music.locations.LocationMode
import org.oxycblt.musikr.cache.IncrementalScanPlan
import org.oxycblt.musikr.fs.SourceFingerprintStrength
import org.oxycblt.musikr.fs.SourceSnapshot
import org.oxycblt.musikr.library.MetadataProfile

class SourceAuthorityScopePolicyTest {
    @Test
    fun `empty explicit keys become unscoped provider discovery`() {
        assertNull(SourceAuthorityScopePolicy.normalizeRequestedSourceKeys(emptySet()))
        assertEquals(
            setOf("direct:a"),
            SourceAuthorityScopePolicy.normalizeRequestedSourceKeys(setOf("direct:a")),
        )
    }

    @Test
    fun `only explicit-root backends may authorise an empty configured set`() {
        assertTrue(
            SourceAuthorityScopePolicy.allowExplicitEmptySourceSet(
                LocationMode.SAF,
                hasCheckpointAuthority = true,
                originalRequestedSourceKeys = emptySet(),
                configuredSourceKeys = emptySet(),
            )
        )
        assertFalse(
            SourceAuthorityScopePolicy.allowExplicitEmptySourceSet(
                LocationMode.MEDIA_STORE,
                hasCheckpointAuthority = true,
                originalRequestedSourceKeys = emptySet(),
                configuredSourceKeys = emptySet(),
            )
        )
    }

    @Test
    fun `mediastore attempted keys come from observed provider plan`() {
        val external = externalPrimarySnapshot()
        val plan =
            IncrementalScanPlan(
                scanId = "scan",
                scanSources = listOf(external),
                reuseSourceKeys = emptySet(),
                unavailableSourceKeys = emptySet(),
                metadataProfile = MetadataProfile.LEAN,
                configurationRevision = 1L,
                force = true,
            )

        assertEquals(
            setOf(external.sourceKey),
            SourceAuthorityScopePolicy.effectiveAttemptedSourceKeys(
                LocationMode.MEDIA_STORE,
                requestedSourceKeys = null,
                configuredSourceKeys = emptySet(),
                plan = plan,
            ),
        )
    }

    @Test
    fun `unscoped mediastore retry clears a reused provider source from unresolved scope`() {
        val external = externalPrimarySnapshot()
        val plan =
            IncrementalScanPlan(
                scanId = "retry",
                scanSources = emptyList(),
                reuseSourceKeys = setOf(external.sourceKey),
                unavailableSourceKeys = emptySet(),
                metadataProfile = MetadataProfile.LEAN,
                configurationRevision = 2L,
                force = false,
            )
        val attempted =
            SourceAuthorityScopePolicy.effectiveAttemptedSourceKeys(
                LocationMode.MEDIA_STORE,
                requestedSourceKeys = null,
                configuredSourceKeys = emptySet(),
                plan = plan,
            )

        assertEquals(setOf(external.sourceKey), attempted)
        assertTrue((setOf(external.sourceKey) - attempted).isEmpty())
    }

    private fun externalPrimarySnapshot() =
        SourceSnapshot(
            sourceKey = "internal:external_primary",
            sourceType = "MEDIA_STORE",
            rootUri = "content://media/external_primary/audio/media",
            rootPath = "/storage/emulated/0",
            available = true,
            fingerprint = "v1",
            fingerprintStrength = SourceFingerprintStrength.ADVISORY,
        )
}
