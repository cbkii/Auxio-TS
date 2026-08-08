/*
 * Copyright (c) 2026 Auxio Project
 * TopwayBridgeCommandLedgerTest.kt is part of Auxio.
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
