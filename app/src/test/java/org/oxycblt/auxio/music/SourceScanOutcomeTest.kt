/*
 * Copyright (c) 2026 Auxio Project
 * SourceScanOutcomeTest.kt is part of Auxio.
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

class SourceScanOutcomeTest {
    @Test
    fun `empty is authoritative only when every configured source enumerated`() {
        val success =
            SourceScanOutcome.classify(
                configuredSourceKeys = setOf("a", "b"),
                failedSources = emptyMap(),
                songCount = 0,
            )
        val unavailable =
            SourceScanOutcome.classify(
                configuredSourceKeys = setOf("a", "b"),
                failedSources = mapOf("b" to "TEMPORARILY_UNAVAILABLE|missing"),
                songCount = 0,
            )

        assertTrue(success is SourceScanOutcome.AuthoritativeEmpty)
        assertTrue(unavailable is SourceScanOutcome.Partial)
        assertEquals(setOf("b"), unavailable.unresolvedSourceKeys)
    }

    @Test
    fun `readable sibling yields partial result and unresolved key`() {
        val outcome =
            SourceScanOutcome.classify(
                configuredSourceKeys = setOf("healthy", "missing"),
                failedSources = mapOf("missing" to "TEMPORARILY_UNAVAILABLE|USB"),
                songCount = 12,
            )

        assertTrue(outcome is SourceScanOutcome.Partial)
        assertEquals(setOf("missing"), outcome.unresolvedSourceKeys)
    }

    @Test
    fun `permission and truncation retain distinct recovery outcomes`() {
        assertTrue(
            SourceScanOutcome.classify(
                setOf("saf"),
                mapOf("saf" to "PERMISSION_REQUIRED|revoked"),
                0,
            ) is SourceScanOutcome.PermissionRequired
        )
        assertTrue(
            SourceScanOutcome.classify(setOf("usb"), mapOf("usb" to "TRUNCATED|depth"), 3)
                is SourceScanOutcome.Truncated
        )
    }
}
