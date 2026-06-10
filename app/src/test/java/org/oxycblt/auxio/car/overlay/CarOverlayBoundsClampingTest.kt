/*
 * Copyright (c) 2024 Auxio Project
 * CarOverlayBoundsClampingTest.kt is part of Auxio.
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

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Tests overlay bounds clamping logic. Uses the same full-display algorithm as
 * CarFloatingControlsService but extracted here for pure unit testing without Android dependencies.
 */
class CarOverlayBoundsClampingTest {

    // Mirror the fallback constants from CarFloatingControlsService companion.
    private val overlayWidth = 350
    private val overlayHeight = 80
    private val defaultTopEdgeY = 0

    private data class Bounds(val width: Int = 1280, val height: Int = 720)

    /** Simplified clamp function matching the service implementation. */
    private fun clampPosition(x: Int, y: Int, bounds: Bounds = Bounds()): Pair<Int, Int> {
        val screenW = bounds.width.takeIf { it > 0 } ?: 1280
        val screenH = bounds.height.takeIf { it > 0 } ?: 720
        val minX = 0
        val minY = defaultTopEdgeY
        val maxX = (screenW - overlayWidth).coerceAtLeast(minX)
        val maxY = (screenH - overlayHeight).coerceAtLeast(minY)
        return x.coerceIn(minX, maxX) to y.coerceIn(minY, maxY)
    }

    private fun defaultTopCenterPosition(bounds: Bounds = Bounds()): Pair<Int, Int> {
        val screenW = bounds.width.takeIf { it > 0 } ?: 1280
        val x = ((screenW - overlayWidth) / 2).coerceAtLeast(0)
        return clampPosition(x, defaultTopEdgeY, bounds)
    }

    @Test
    fun `position within full-screen bounds is unchanged`() {
        val (x, y) = clampPosition(100, 100)
        assertEquals(100, x)
        assertEquals(100, y)
    }

    @Test
    fun `negative x is clamped to zero`() {
        val (x, _) = clampPosition(-50, 100)
        assertEquals(0, x)
    }

    @Test
    fun `top edge y zero is allowed`() {
        val (_, y) = clampPosition(100, 0)
        assertEquals(0, y)
    }

    @Test
    fun `negative y is clamped to top edge zero`() {
        val (_, y) = clampPosition(100, -10)
        assertEquals(0, y)
    }

    @Test
    fun `x beyond physical right edge is clamped`() {
        // maxX = 1280 - 350 = 930; no right-nav inset is subtracted.
        val (x, _) = clampPosition(1000, 100)
        assertEquals(930, x)
    }

    @Test
    fun `y beyond bottom is clamped`() {
        // maxY = 720 - 80 = 640
        val (_, y) = clampPosition(100, 700)
        assertEquals(640, y)
    }

    @Test
    fun `default position is top center at physical top edge on TS18`() {
        val (x, y) = defaultTopCenterPosition()
        assertEquals(465, x)
        assertEquals(0, y)
        assertTrue(x >= 0, "Default X must be non-negative")
        assertTrue(y >= 0, "Default Y must be at or below physical top edge")
        assertTrue(x <= 930, "Default X must be within physical right bound")
        assertTrue(y <= 640, "Default Y must be within bottom bound")
    }

    @Test
    fun `default position centers against full display width fallback`() {
        val (x, y) = defaultTopCenterPosition(Bounds(width = 1024, height = 600))
        assertEquals(337, x)
        assertEquals(0, y)
    }

    @Test
    fun `extremely large coordinates are clamped safely`() {
        val (x, y) = clampPosition(9999, 9999)
        assertEquals(930, x)
        assertEquals(640, y)
    }
}
