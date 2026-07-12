/*
 * Copyright (c) 2026 Auxio Project
 * TopwayMusicEntryPolicyTest.kt is part of Auxio.
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

package org.oxycblt.auxio.car.overlay

import android.content.Intent
import org.junit.Assert.assertEquals
import org.junit.Test

class TopwayMusicEntryPolicyTest {
    @Test
    fun mainEntryUsesFloatingControlsWhenConfigured() {
        assertEquals(
            TopwayMusicEntryPolicy.Route.FLOATING_CONTROLS_ONLY,
            TopwayMusicEntryPolicy.route(Intent.ACTION_MAIN, hasData = false, floatingOnly = true),
        )
    }

    @Test
    fun viewIntentAlwaysOpensFullPlayer() {
        assertEquals(
            TopwayMusicEntryPolicy.Route.FULL_PLAYER,
            TopwayMusicEntryPolicy.route(Intent.ACTION_VIEW, hasData = true, floatingOnly = true),
        )
    }

    @Test
    fun dataIntentWithoutViewActionOpensFullPlayer() {
        assertEquals(
            TopwayMusicEntryPolicy.Route.FULL_PLAYER,
            TopwayMusicEntryPolicy.route(action = null, hasData = true, floatingOnly = true),
        )
    }

    @Test
    fun normalMainEntryOpensFullPlayer() {
        assertEquals(
            TopwayMusicEntryPolicy.Route.FULL_PLAYER,
            TopwayMusicEntryPolicy.route(Intent.ACTION_MAIN, hasData = false, floatingOnly = false),
        )
    }
}
