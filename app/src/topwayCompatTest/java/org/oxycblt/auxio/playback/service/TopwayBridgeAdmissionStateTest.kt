/*
 * Copyright (c) 2026 Auxio Project
 * TopwayBridgeAdmissionStateTest.kt is part of Auxio.
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

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TopwayBridgeAdmissionStateTest {
    @Test
    fun `running timeout wins over later acceptance`() {
        val state = TopwayBridgeAdmissionState()
        assertTrue(state.start())
        assertEquals(TopwayBridgeAdmissionResult.EXPIRED, state.expire())

        assertEquals(
            TopwayBridgeAdmissionResult.EXPIRED,
            state.complete(TopwayBridgeAdmissionResult.ACCEPTED),
        )
        assertEquals(TopwayBridgeAdmissionResult.EXPIRED, state.result())
    }

    @Test
    fun `running interruption wins over later acceptance`() {
        val state = TopwayBridgeAdmissionState()
        assertTrue(state.start())
        assertEquals(TopwayBridgeAdmissionResult.INTERRUPTED, state.interrupt())

        assertEquals(
            TopwayBridgeAdmissionResult.INTERRUPTED,
            state.complete(TopwayBridgeAdmissionResult.ACCEPTED),
        )
        assertEquals(TopwayBridgeAdmissionResult.INTERRUPTED, state.result())
    }

    @Test
    fun `accepted commit remains accepted at timeout boundary`() {
        val state = TopwayBridgeAdmissionState()
        assertTrue(state.start())
        assertEquals(
            TopwayBridgeAdmissionResult.ACCEPTED,
            state.complete(TopwayBridgeAdmissionResult.ACCEPTED),
        )

        assertEquals(TopwayBridgeAdmissionResult.ACCEPTED, state.expire())
        assertEquals(TopwayBridgeAdmissionResult.ACCEPTED, state.result())
    }

    @Test
    fun `accepted commit remains accepted if waiter is interrupted later`() {
        val state = TopwayBridgeAdmissionState()
        assertTrue(state.start())
        assertEquals(
            TopwayBridgeAdmissionResult.ACCEPTED,
            state.complete(TopwayBridgeAdmissionResult.ACCEPTED),
        )

        assertEquals(TopwayBridgeAdmissionResult.ACCEPTED, state.interrupt())
        assertEquals(TopwayBridgeAdmissionResult.ACCEPTED, state.result())
    }

    @Test
    fun `pending timeout prevents later execution`() {
        val state = TopwayBridgeAdmissionState()

        assertEquals(TopwayBridgeAdmissionResult.EXPIRED, state.expire())
        assertFalse(state.start())
        assertEquals(TopwayBridgeAdmissionResult.EXPIRED, state.result())
    }

    @Test
    fun `not ready completion can never become accepted`() {
        val state = TopwayBridgeAdmissionState()
        assertTrue(state.start())
        assertEquals(
            TopwayBridgeAdmissionResult.NOT_READY,
            state.complete(TopwayBridgeAdmissionResult.NOT_READY),
        )

        assertEquals(
            TopwayBridgeAdmissionResult.NOT_READY,
            state.complete(TopwayBridgeAdmissionResult.ACCEPTED),
        )
        assertEquals(TopwayBridgeAdmissionResult.NOT_READY, state.result())
    }
}
