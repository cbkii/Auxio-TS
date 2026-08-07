/*
 * Copyright (c) 2026 Auxio Project
 * TopwayBridgeAdmissionStateTest.kt is part of Auxio-TS.
 */

package org.oxycblt.auxio.playback.service

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TopwayBridgeAdmissionStateTest {
    @Test
    fun `running timeout wins over late acceptance`() {
        val state = TopwayBridgeAdmissionState()
        assertTrue(state.start())

        assertEquals(TopwayBridgeAdmissionResult.EXPIRED, state.expire())
        state.complete(TopwayBridgeAdmissionResult.ACCEPTED)

        assertEquals(TopwayBridgeAdmissionResult.EXPIRED, state.result())
    }

    @Test
    fun `running interruption wins over late acceptance`() {
        val state = TopwayBridgeAdmissionState()
        assertTrue(state.start())

        assertEquals(TopwayBridgeAdmissionResult.INTERRUPTED, state.interrupt())
        state.complete(TopwayBridgeAdmissionResult.ACCEPTED)

        assertEquals(TopwayBridgeAdmissionResult.INTERRUPTED, state.result())
    }

    @Test
    fun `accepted completion remains accepted at timeout boundary`() {
        val state = TopwayBridgeAdmissionState()
        assertTrue(state.start())
        state.complete(TopwayBridgeAdmissionResult.ACCEPTED)

        assertEquals(TopwayBridgeAdmissionResult.ACCEPTED, state.expire())
        assertEquals(TopwayBridgeAdmissionResult.ACCEPTED, state.result())
    }

    @Test
    fun `accepted completion remains accepted if waiter is interrupted later`() {
        val state = TopwayBridgeAdmissionState()
        assertTrue(state.start())
        state.complete(TopwayBridgeAdmissionResult.ACCEPTED)

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
        state.complete(TopwayBridgeAdmissionResult.NOT_READY)
        state.complete(TopwayBridgeAdmissionResult.ACCEPTED)

        assertEquals(TopwayBridgeAdmissionResult.NOT_READY, state.result())
    }
}
