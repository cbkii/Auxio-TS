/*
 * Copyright (c) 2026 Auxio Project
 * IndexingSessionGateTest.kt is part of Auxio.
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

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class IndexingSessionGateTest {
    @Test
    fun `old terminal callback is rejected after newer session starts`() {
        val gate = IndexingSessionGate()
        gate.begin(1L)
        gate.begin(2L)

        assertFalse(gate.complete(1L))
        assertTrue(gate.isCurrent(2L))
        assertTrue(gate.complete(2L))
    }

    @Test
    fun `one indexing session accepts exactly one terminal callback`() {
        val gate = IndexingSessionGate()
        gate.begin(7L)

        assertTrue(gate.complete(7L))
        assertFalse(gate.complete(7L))
        assertFalse(gate.isCurrent(7L))
    }
}
