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
        val external =
            SourceSnapshot(
                sourceKey = "internal:external_primary",
                sourceType = "MEDIA_STORE",
                rootUri = "content://media/external_primary/audio/media",
                rootPath = "/storage/emulated/0",
                available = true,
                fingerprint = "v1",
                fingerprintStrength = SourceFingerprintStrength.ADVISORY,
            )
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
}
