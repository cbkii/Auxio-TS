/*
 * Copyright (c) 2024 Auxio Project
 * MenuEventFlowTest.kt is part of Auxio.
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

package org.oxycblt.auxio.list

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import org.oxycblt.auxio.util.MutableEvent

/**
 * Regression tests for [MutableEvent] behaviour used by menu/dialog event flow.
 *
 * Verifies that:
 * - Events can be posted and consumed correctly
 * - Consuming clears the value for subsequent posts
 * - Stale events do not permanently block new posts when consumed
 */
class MenuEventFlowTest {

    @Test
    fun `put sets value observable via flow`() {
        val event = MutableEvent<String>()
        assertNull(event.flow.value)

        event.put("first")
        assertEquals("first", event.flow.value)
    }

    @Test
    fun `consume returns value and clears flow`() {
        val event = MutableEvent<String>()
        event.put("item")

        val consumed = event.consume()
        assertEquals("item", consumed)
        assertNull(event.flow.value)
    }

    @Test
    fun `consume on empty event returns null`() {
        val event = MutableEvent<String>()
        assertNull(event.consume())
    }

    @Test
    fun `after consume new put succeeds`() {
        val event = MutableEvent<String>()
        event.put("first")
        event.consume()

        // New event should post successfully
        event.put("second")
        assertEquals("second", event.flow.value)
    }

    @Test
    fun `unconsumed event blocks subsequent puts at application layer`() {
        // This test documents the blocking behaviour in ListViewModel.openImpl:
        // if the flow value is non-null, a new put is rejected.
        // The fix for playback panel bypasses this by not using ListViewModel.menu.
        val event = MutableEvent<String>()
        event.put("first")

        // Simulating openImpl guard: check if non-null before putting
        val existing = event.flow.value
        assertNotNull(existing)

        // After consumer drains the event, new posts work
        event.consume()
        assertNull(event.flow.value)
        event.put("second")
        assertEquals("second", event.flow.value)
    }
}
