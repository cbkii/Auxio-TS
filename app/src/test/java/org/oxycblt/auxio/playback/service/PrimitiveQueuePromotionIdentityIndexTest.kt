/*
 * Copyright (c) 2026 Auxio Project
 * PrimitiveQueuePromotionIdentityIndexTest.kt is part of Auxio.
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

package org.oxycblt.auxio.playback.service

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class PrimitiveQueuePromotionIdentityIndexTest {
    @Test
    fun duplicateFallbackIdentityIsExcludedInsteadOfCollapsed() {
        val index =
            PrimitiveQueuePromotionIdentityIndex.uniqueBy(listOf(1, 2, 3)) { value ->
                if (value < 3) "duplicate" else "unique"
            }

        assertNull(index["duplicate"])
        assertEquals(3, index["unique"])
    }

    @Test
    fun identityRemainsAmbiguousAfterAdditionalDuplicates() {
        val index =
            PrimitiveQueuePromotionIdentityIndex.uniqueBy(listOf(1, 2, 3, 4)) { value ->
                if (value < 4) "duplicate" else "unique"
            }

        assertNull(index["duplicate"])
        assertEquals(mapOf("unique" to 4), index)
    }
}
