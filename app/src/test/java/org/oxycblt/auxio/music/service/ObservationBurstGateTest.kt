/*
 * Copyright (c) 2026 Auxio Project
 * ObservationBurstGateTest.kt is part of Auxio.
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

package org.oxycblt.auxio.music.service

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ObservationBurstGateTest {
    @Test
    fun `only latest observer event may request a scan`() {
        val gate = ObservationBurstGate()
        val first = gate.nextToken()
        val second = gate.nextToken()
        val third = gate.nextToken()

        assertFalse(gate.isLatest(first))
        assertFalse(gate.isLatest(second))
        assertTrue(gate.isLatest(third))
    }
}
