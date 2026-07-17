/*
 * Copyright (c) 2026 Auxio Project
 * MetadataWorkPolicyTest.kt is part of Auxio.
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

package org.oxycblt.musikr.library

import org.junit.Assert.assertEquals
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
    fun `lean profile defaults cannot request full dimensions or artwork`() {
        val dimensions = MetadataProfile.LEAN.defaultDimensionPolicy()
        assertFalse(dimensions.genres)
        assertFalse(dimensions.detailedCollaborators)
        assertFalse(dimensions.albumArtists)
        assertFalse(dimensions.replayGain)
        assertFalse(dimensions.musicBrainz)
        assertEquals(ArtworkPolicy.VISIBLE_ITEMS, MetadataProfile.LEAN.defaultArtworkPolicy())
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
