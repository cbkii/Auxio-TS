/*
 * Copyright (c) 2026 Auxio Project
 * CoverCleanupPolicyTest.kt is part of Auxio.
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

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Cover cleanup retains only the artwork of the library that was just published, so it may only run
 * behind proof that the publication is a complete authoritative view.
 */
class CoverCleanupPolicyTest {
    @Test
    fun `a complete authoritative success may reclaim expired covers`() {
        assertTrue(evaluate(outcome = SourceScanOutcome.Success(setOf("usb0"))).allowed)
    }

    @Test
    fun `an authoritative empty library may reclaim expired covers`() {
        // Proven-empty is still a complete view of the configured sources.
        assertTrue(evaluate(outcome = SourceScanOutcome.AuthoritativeEmpty(setOf("usb0"))).allowed)
    }

    @Test
    fun `an unpublished scan never reclaims covers`() {
        assertFalse(evaluate(published = false).allowed)
    }

    @Test
    fun `a lean publication never defines the retained cover set`() {
        // Rich extraction has not run yet, so the lean library does not reference every cover the
        // committed library still needs.
        assertFalse(evaluate(completeMetadata = false).allowed)
    }

    @Test
    fun `optional enrichment never owns destructive cleanup`() {
        assertFalse(evaluate(enrichmentOnly = true).allowed)
    }

    @Test
    fun `retained unresolved sources block cleanup`() {
        assertFalse(evaluate(unresolvedSourceKeys = setOf("usb1")).allowed)
    }

    @Test
    fun `unobserved sources block cleanup`() {
        // An unmounted USB volume keeps its last-known-good rows, and therefore its covers.
        assertFalse(evaluate(unavailableSourceKeys = setOf("usb1")).allowed)
    }

    @Test
    fun `partial and truncated outcomes block cleanup`() {
        assertFalse(
            evaluate(outcome = SourceScanOutcome.Partial(setOf("usb0"), emptySet())).allowed
        )
        assertFalse(
            evaluate(outcome = SourceScanOutcome.Truncated(setOf("usb0"), emptySet())).allowed
        )
    }

    @Test
    fun `every rejection explains itself`() {
        assertFalse(evaluate(published = false).reason.isBlank())
        assertFalse(evaluate(completeMetadata = false).reason.isBlank())
        assertFalse(evaluate(unresolvedSourceKeys = setOf("usb1")).reason.isBlank())
    }

    private fun evaluate(
        published: Boolean = true,
        outcome: SourceScanOutcome = SourceScanOutcome.Success(setOf("usb0")),
        unresolvedSourceKeys: Set<String> = emptySet(),
        unavailableSourceKeys: Set<String> = emptySet(),
        completeMetadata: Boolean = true,
        enrichmentOnly: Boolean = false,
    ) =
        CoverCleanupPolicy.evaluate(
            published = published,
            outcome = outcome,
            unresolvedSourceKeys = unresolvedSourceKeys,
            unavailableSourceKeys = unavailableSourceKeys,
            completeMetadata = completeMetadata,
            enrichmentOnly = enrichmentOnly,
        )
}
