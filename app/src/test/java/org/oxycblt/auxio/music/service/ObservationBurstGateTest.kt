/*
 * Copyright (c) 2026 Auxio Project
 * ObservationBurstGateTest.kt is part of Auxio.
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
