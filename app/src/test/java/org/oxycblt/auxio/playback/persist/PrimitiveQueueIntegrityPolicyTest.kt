/*
 * Copyright (c) 2026 Auxio Project
 * PrimitiveQueueIntegrityPolicyTest.kt is part of Auxio.
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

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class PrimitiveQueueIntegrityPolicyTest {
    @Test
    fun exactPersistedCountAndCurrentPositionAreAccepted() {
        assertEquals(
            3,
            PrimitiveQueueIntegrityPolicy.validTotalCount(
                declaredCount = 3,
                actualCount = 3,
                currentPosition = 1,
            ),
        )
    }

    @Test
    fun rowCountMismatchIsRejectedInsteadOfSilentlyTruncated() {
        assertNull(
            PrimitiveQueueIntegrityPolicy.validTotalCount(
                declaredCount = 3,
                actualCount = 2,
                currentPosition = 1,
            )
        )
        assertNull(
            PrimitiveQueueIntegrityPolicy.validTotalCount(
                declaredCount = 2,
                actualCount = 3,
                currentPosition = 1,
            )
        )
    }

    @Test
    fun invalidCurrentPositionIsRejected() {
        assertNull(
            PrimitiveQueueIntegrityPolicy.validTotalCount(
                declaredCount = 3,
                actualCount = 3,
                currentPosition = 3,
            )
        )
    }
}
