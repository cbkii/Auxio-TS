/*
 * Copyright (c) 2026 Auxio Project
 * SourceAvailabilityOutcomePolicyTest.kt is part of Auxio.
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

package org.oxycblt.auxio.music

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SourceAvailabilityOutcomePolicyTest {
    @Test
    fun `retained rows do not turn an unobserved source into full success`() {
        val outcome =
            SourceScanOutcome.classify(
                configuredSourceKeys = setOf("usb0"),
                failedSources = mapOf("usb0" to "TEMPORARILY_UNAVAILABLE|preflight unavailable"),
                songCount = 1,
            )

        assertEquals(SourceScanOutcome.Partial(emptySet(), setOf("usb0")), outcome)
    }

    @Test
    fun `healthy sibling remains committed while unavailable source is unresolved`() {
        val outcome =
            SourceScanOutcome.classify(
                configuredSourceKeys = setOf("usb0", "usb1"),
                failedSources = mapOf("usb0" to "TEMPORARILY_UNAVAILABLE|preflight unavailable"),
                songCount = 1,
            )

        assertEquals(SourceScanOutcome.Partial(setOf("usb1"), setOf("usb0")), outcome)
    }

    @Test
    fun `all unavailable without readable rows remains retryable`() {
        val outcome =
            SourceScanOutcome.classify(
                configuredSourceKeys = setOf("usb0"),
                failedSources = mapOf("usb0" to "TEMPORARILY_UNAVAILABLE|preflight unavailable"),
                songCount = 0,
            )

        assertTrue(outcome is SourceScanOutcome.TemporarilyUnavailable)
        assertEquals(setOf("usb0"), outcome.unresolvedSourceKeys)
    }
}
