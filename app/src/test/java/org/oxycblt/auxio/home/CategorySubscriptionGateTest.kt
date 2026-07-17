/*
 * Copyright (c) 2026 Auxio Project
 * CategorySubscriptionGateTest.kt is part of Auxio.
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

package org.oxycblt.auxio.home

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.oxycblt.auxio.music.MusicType

class CategorySubscriptionGateTest {
    @Test
    fun `inactive invalidations conflate until category is activated`() {
        val gate = CategorySubscriptionGate(MusicType.SONGS)

        assertFalse(gate.invalidate(MusicType.ALBUMS))
        assertFalse(gate.invalidate(MusicType.ALBUMS))
        assertTrue(gate.activate(MusicType.ALBUMS))
        assertFalse(gate.activate(MusicType.ALBUMS))
    }

    @Test
    fun `active category invalidation executes immediately`() {
        val gate = CategorySubscriptionGate(MusicType.SONGS)

        assertTrue(gate.invalidate(MusicType.SONGS))
        assertTrue(gate.invalidate(MusicType.SONGS))
    }

    @Test
    fun `invalidate all leaves one pending refresh per inactive category`() {
        val gate = CategorySubscriptionGate(MusicType.SONGS)

        assertTrue(gate.invalidateAll())
        assertTrue(gate.activate(MusicType.GENRES))
        assertFalse(gate.activate(MusicType.GENRES))
    }
}
