/*
 * Copyright (c) 2026 Auxio Project
 * DrivingStartupPolicyTest.kt is part of Auxio.
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
        assertTrue(dimensions.playlists)
        assertFalse(dimensions.detailedCollaborators)
        assertFalse(dimensions.genres)
        assertFalse(dimensions.musicBrainz)
        assertFalse(dimensions.replayGain)
        assertFalse(dimensions.releaseTypes)
        assertEquals(
            ArtworkPolicy.VISIBLE_ITEMS,
            DrivingStartupPolicy.artworkPolicy(MetadataProfile.LEAN),
        )
    }
}
