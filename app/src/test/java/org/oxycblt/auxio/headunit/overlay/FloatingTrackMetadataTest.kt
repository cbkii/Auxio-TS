/*
 * Copyright (c) 2026 Auxio Project
 * FloatingTrackMetadataTest.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.oxycblt.auxio.headunit.overlay

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class FloatingTrackMetadataTest {

    @After
    fun tearDown() {
        FloatingTrackMetadataBus.clear()
    }

    @Test
    fun formatsArtistAndTitleWithRequestedSeparator() {
        val metadata = FloatingTrackMetadata.from("Artist", "Song title")

        assertEquals("Artist - Song title", metadata?.displayText)
    }

    @Test
    fun trimsValuesAndFallsBackToAvailableField() {
        assertEquals("Song title", FloatingTrackMetadata.from("  ", " Song title ")?.displayText)
        assertEquals("Artist", FloatingTrackMetadata.from(" Artist ", null)?.displayText)
        assertNull(FloatingTrackMetadata.from("  ", null))
    }

    @Test
    fun busImmediatelyDeliversCurrentMetadataAndDeduplicates() {
        val received = mutableListOf<FloatingTrackMetadata?>()
        val listener: (FloatingTrackMetadata?) -> Unit = received::add

        FloatingTrackMetadataBus.publish("Artist", "Song")
        FloatingTrackMetadataBus.addListener(listener)
        FloatingTrackMetadataBus.publish("Artist", "Song")
        FloatingTrackMetadataBus.publish("Artist", "Next")
        FloatingTrackMetadataBus.removeListener(listener)

        assertEquals(listOf("Artist - Song", "Artist - Next"), received.map { it?.displayText })
    }
}
