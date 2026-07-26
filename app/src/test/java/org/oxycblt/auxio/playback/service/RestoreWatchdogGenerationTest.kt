/*
 * Copyright (c) 2026 Auxio Project
 * RestoreWatchdogGenerationTest.kt is part of Auxio.
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

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RestoreWatchdogGenerationTest {
    @Test
    fun `replacement invalidates the previous watchdog`() {
        val generation = RestoreWatchdogGeneration()
        val first = generation.next()
        val second = generation.next()

        assertFalse(generation.isCurrent(first))
        assertTrue(generation.isCurrent(second))
    }

    @Test
    fun `cancellation invalidates the current watchdog`() {
        val generation = RestoreWatchdogGeneration()
        val current = generation.next()

        generation.invalidate()

        assertFalse(generation.isCurrent(current))
    }
}
