/*
 * Copyright (c) 2026 Auxio Project
 * IndexingResourcePolicyTest.kt is part of Auxio.
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
import org.junit.Test

class IndexingResourcePolicyTest {
    private val policy = DefaultIndexingResourcePolicy

    @Test
    fun `topway playback-first playing uses one worker`() {
        assertEquals(1, policy.resolveWorkerCount(ScanPriority.PLAYBACK_FIRST, true, true, 8))
    }

    @Test
    fun `topway playback-first idle uses two workers`() {
        assertEquals(2, policy.resolveWorkerCount(ScanPriority.PLAYBACK_FIRST, false, true, 8))
    }

    @Test
    fun `balanced policy responds to playback activity`() {
        assertEquals(2, policy.resolveWorkerCount(ScanPriority.BALANCED, true, true, 8))
        assertEquals(3, policy.resolveWorkerCount(ScanPriority.BALANCED, false, true, 8))
    }

    @Test
    fun `fast scan clamps to available processors and topway cap`() {
        assertEquals(1, policy.resolveWorkerCount(ScanPriority.FAST_SCAN, false, true, 1))
        assertEquals(4, policy.resolveWorkerCount(ScanPriority.FAST_SCAN, false, true, 16))
    }

    @Test
    fun `standard build still clamps invalid processor counts`() {
        assertEquals(1, policy.resolveWorkerCount(ScanPriority.FAST_SCAN, false, false, 0))
    }
}
