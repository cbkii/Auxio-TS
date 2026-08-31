/*
 * Copyright (c) 2026 Auxio Project
 * SeamlessQueueHandoffPolicyTest.kt is part of Auxio.
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

class SeamlessQueueHandoffPolicyTest {
    @Test
    fun canonicalNeighboursArePlannedAroundUntouchedCurrentSource() {
        val plan =
            requireNotNull(
                SeamlessQueueHandoffPolicy.plan(
                    originalItemCount = 5,
                    originalCurrentIndex = 2,
                    canonicalItemCount = 9,
                    targetCurrentIndex = 4,
                )
            )

        assertEquals(2, plan.originalCurrentIndex)
        assertEquals(5, plan.originalItemCount)
        assertEquals(9, plan.canonicalItemCount)
        assertEquals(4, plan.targetCurrentIndex)
        assertEquals(4, plan.prependCount)
        assertEquals(4, plan.appendCount)
    }

    @Test
    fun firstCanonicalItemNeedsNoPrepend() {
        val plan =
            requireNotNull(
                SeamlessQueueHandoffPolicy.plan(
                    originalItemCount = 3,
                    originalCurrentIndex = 1,
                    canonicalItemCount = 4,
                    targetCurrentIndex = 0,
                )
            )

        assertEquals(0, plan.prependCount)
        assertEquals(3, plan.appendCount)
    }

    @Test
    fun lastCanonicalItemNeedsNoAppend() {
        val plan =
            requireNotNull(
                SeamlessQueueHandoffPolicy.plan(
                    originalItemCount = 3,
                    originalCurrentIndex = 1,
                    canonicalItemCount = 4,
                    targetCurrentIndex = 3,
                )
            )

        assertEquals(3, plan.prependCount)
        assertEquals(0, plan.appendCount)
    }

    @Test
    fun invalidPlayerOrCanonicalBoundsFailClosed() {
        assertNull(
            SeamlessQueueHandoffPolicy.plan(
                originalItemCount = 0,
                originalCurrentIndex = 0,
                canonicalItemCount = 1,
                targetCurrentIndex = 0,
            )
        )
        assertNull(
            SeamlessQueueHandoffPolicy.plan(
                originalItemCount = 2,
                originalCurrentIndex = 2,
                canonicalItemCount = 1,
                targetCurrentIndex = 0,
            )
        )
        assertNull(
            SeamlessQueueHandoffPolicy.plan(
                originalItemCount = 1,
                originalCurrentIndex = 0,
                canonicalItemCount = 2,
                targetCurrentIndex = 2,
            )
        )
    }
}
