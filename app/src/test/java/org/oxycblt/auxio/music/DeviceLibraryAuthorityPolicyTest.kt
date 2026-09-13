/*
 * Copyright (c) 2026 Auxio Project
 * DeviceLibraryAuthorityPolicyTest.kt is part of Auxio.
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

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DeviceLibraryAuthorityPolicyTest {
    @Test
    fun lateListenerReplaysAuthoritativeOutcomeForCurrentPublishedGeneration() {
        val success = SourceScanOutcome.Success(setOf("internal", "usb"))
        val snapshot =
            DeviceLibraryAuthorityPolicy.coherentSnapshot(
                currentGeneration = 7L,
                published = DeviceLibraryAuthority(7L, success),
            )

        assertEquals(7L, snapshot.generation)
        assertEquals(success, snapshot.sourceScanOutcome)
    }

    @Test
    fun generationAdvanceCannotReuseStaleSuccessfulOutcome() {
        val staleSuccess = SourceScanOutcome.Success(setOf("internal", "usb"))
        val snapshot =
            DeviceLibraryAuthorityPolicy.coherentSnapshot(
                currentGeneration = 8L,
                published = DeviceLibraryAuthority(7L, staleSuccess),
            )

        assertEquals(8L, snapshot.generation)
        assertNull(snapshot.sourceScanOutcome)
    }

    @Test
    fun currentPartialOutcomeRemainsNonAuthoritativeForLateListener() {
        val partial = SourceScanOutcome.Partial(setOf("internal"), setOf("usb"))
        val snapshot =
            DeviceLibraryAuthorityPolicy.coherentSnapshot(
                currentGeneration = 9L,
                published = DeviceLibraryAuthority(9L, partial),
            )

        assertEquals(9L, snapshot.generation)
        assertEquals(partial, snapshot.sourceScanOutcome)
    }
}
