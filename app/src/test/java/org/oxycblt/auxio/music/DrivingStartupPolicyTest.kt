/*
 * Copyright (c) 2026 Auxio Project
 * DrivingStartupPolicyTest.kt is part of Auxio.
 */

package org.oxycblt.auxio.music

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.oxycblt.musikr.library.ArtworkPolicy
import org.oxycblt.musikr.library.MetadataProfile

class DrivingStartupPolicyTest {
    @Test
    fun `Topway and active playback default to lean metadata`() {
        assertEquals(
            MetadataProfile.LEAN,
            DrivingStartupPolicy.metadataProfile(
                explicit = null,
                scanPriority = ScanPriority.BALANCED,
                playbackActive = false,
                isTopwayVariant = true,
            ),
        )
        assertEquals(
            MetadataProfile.LEAN,
            DrivingStartupPolicy.metadataProfile(
                explicit = null,
                scanPriority = ScanPriority.BALANCED,
                playbackActive = true,
                isTopwayVariant = false,
            ),
        )
    }

    @Test
    fun `explicit full profile is never downgraded`() {
        assertEquals(
            MetadataProfile.FULL,
            DrivingStartupPolicy.metadataProfile(
                explicit = MetadataProfile.FULL,
                scanPriority = ScanPriority.PLAYBACK_FIRST,
                playbackActive = true,
                isTopwayVariant = true,
            ),
        )
    }

    @Test
    fun `lean policy disables rich dimensions and eager artwork`() {
        val dimensions = DrivingStartupPolicy.dimensions(MetadataProfile.LEAN)
        assertTrue(dimensions.songIdentity)
        assertTrue(dimensions.basicTags)
        assertFalse(dimensions.genres)
        assertFalse(dimensions.musicBrainz)
        assertFalse(dimensions.replayGain)
        assertFalse(dimensions.releaseTypes)
        assertEquals(
            ArtworkPolicy.VISIBLE_ONLY,
            DrivingStartupPolicy.artworkPolicy(MetadataProfile.LEAN),
        )
    }
}
