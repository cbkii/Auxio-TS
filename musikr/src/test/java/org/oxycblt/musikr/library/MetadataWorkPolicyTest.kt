/*
 * Copyright (c) 2026 Auxio Project
 * MetadataWorkPolicyTest.kt is part of Auxio.
 */

package org.oxycblt.musikr.library

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MetadataWorkPolicyTest {
    @Test
    fun `lean profile disables every rich extractor dimension`() {
        val lean = MetadataWorkPolicy.forProfile(MetadataProfile.LEAN)

        assertTrue(lean.useLeanPlatformExtractor)
        assertFalse(lean.readMusicBrainz)
        assertFalse(lean.readReplayGain)
        assertFalse(lean.readGenres)
        assertFalse(lean.readReleaseTypes)
        assertFalse(lean.readDetailedDates)
        assertFalse(lean.expandMultipleArtists)
        assertFalse(lean.extractArtwork)
    }

    @Test
    fun `full profile enables rich enrichment dimensions`() {
        val full = MetadataWorkPolicy.forProfile(MetadataProfile.FULL)

        assertFalse(full.useLeanPlatformExtractor)
        assertTrue(full.readMusicBrainz)
        assertTrue(full.readReplayGain)
        assertTrue(full.readGenres)
        assertTrue(full.readReleaseTypes)
        assertTrue(full.readDetailedDates)
        assertTrue(full.expandMultipleArtists)
        assertTrue(full.extractArtwork)
    }
}
