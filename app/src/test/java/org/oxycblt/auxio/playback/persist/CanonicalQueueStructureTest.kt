/*
 * Copyright (c) 2026 Auxio Project
 * CanonicalQueueStructureTest.kt is part of Auxio.
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

package org.oxycblt.auxio.playback.persist

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.oxycblt.auxio.playback.state.ShuffleScope
import org.oxycblt.musikr.Music

class CanonicalQueueStructureTest {
    private val first =
        checkNotNull(Music.UID.fromString("uas00000000-0000-0000-0000-000000000001"))
    private val second =
        checkNotNull(Music.UID.fromString("uas00000000-0000-0000-0000-000000000002"))

    private fun structure(
        heap: List<Music.UID> = listOf(first, second),
        mapping: List<Int> = emptyList(),
    ) =
        CanonicalQueueStructure(
            heapUids = heap,
            shuffleIndices = mapping,
            parentUid = null,
            currentSongUid = first,
            shuffleScope = ShuffleScope.OFF,
        )

    @Test
    fun identicalQueueStructuresPermitPositionOnlyPersistence() {
        assertEquals(structure(), structure())
    }

    @Test
    fun orderAndShuffleChangesInvalidatePositionOnlyPersistence() {
        assertNotEquals(structure(), structure(heap = listOf(second, first)))
        assertNotEquals(structure(), structure(mapping = listOf(1, 0)))
    }
}
