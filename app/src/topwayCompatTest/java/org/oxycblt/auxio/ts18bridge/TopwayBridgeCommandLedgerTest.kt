/*
 * Copyright (c) 2026 Auxio Project
 * TopwayBridgeCommandLedgerTest.kt is part of Auxio-TS.
 */

package org.oxycblt.auxio.ts18bridge

import org.junit.Assert.assertEquals
import org.junit.Test

class TopwayBridgeCommandLedgerTest {
    @Test
    fun `accepted id is recognised as an accepted duplicate`() {
        val ledger = TopwayBridgeCommandLedger(maxEntries = 4, ttlMs = 100L)
        assertEquals(
            TopwayBridgeCommandLedger.Reservation.RESERVED,
            ledger.reserve(generation = 7L, commandId = 1L, nowMs = 10L),
        )
        ledger.markAccepted(generation = 7L, commandId = 1L, nowMs = 20L)
        assertEquals(
            TopwayBridgeCommandLedger.Reservation.DUPLICATE_ACCEPTED,
            ledger.reserve(generation = 7L, commandId = 1L, nowMs = 30L),
        )
    }

    @Test
    fun `pending id is busy and release allows a retry`() {
        val ledger = TopwayBridgeCommandLedger(maxEntries = 4, ttlMs = 100L)
        assertEquals(
            TopwayBridgeCommandLedger.Reservation.RESERVED,
            ledger.reserve(7L, 1L, 10L),
        )
        assertEquals(
            TopwayBridgeCommandLedger.Reservation.BUSY,
            ledger.reserve(7L, 1L, 11L),
        )
        ledger.release(7L, 1L)
        assertEquals(
            TopwayBridgeCommandLedger.Reservation.RESERVED,
            ledger.reserve(7L, 1L, 12L),
        )
    }

    @Test
    fun `generation separates identical command ids`() {
        val ledger = TopwayBridgeCommandLedger(maxEntries = 4, ttlMs = 100L)
        assertEquals(
            TopwayBridgeCommandLedger.Reservation.RESERVED,
            ledger.reserve(7L, 1L, 10L),
        )
        assertEquals(
            TopwayBridgeCommandLedger.Reservation.RESERVED,
            ledger.reserve(8L, 1L, 11L),
        )
    }

    @Test
    fun `ledger is bounded and expired entries are pruned`() {
        val ledger = TopwayBridgeCommandLedger(maxEntries = 2, ttlMs = 20L)
        assertEquals(
            TopwayBridgeCommandLedger.Reservation.RESERVED,
            ledger.reserve(7L, 1L, 10L),
        )
        assertEquals(
            TopwayBridgeCommandLedger.Reservation.RESERVED,
            ledger.reserve(7L, 2L, 11L),
        )
        assertEquals(
            TopwayBridgeCommandLedger.Reservation.BUSY,
            ledger.reserve(7L, 3L, 12L),
        )
        assertEquals(
            TopwayBridgeCommandLedger.Reservation.RESERVED,
            ledger.reserve(7L, 3L, 40L),
        )
        assertEquals(1, ledger.size(40L))
    }
}
