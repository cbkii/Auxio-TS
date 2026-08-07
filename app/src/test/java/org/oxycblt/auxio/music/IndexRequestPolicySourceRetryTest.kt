/*
 * Copyright (c) 2026 Auxio Project
 * IndexRequestPolicySourceRetryTest.kt is part of Auxio.
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
import org.junit.Assert.assertNull
import org.junit.Test

class IndexRequestPolicySourceRetryTest {
    @Test
    fun `provider-managed refresh may retry without explicit configured keys`() {
        val request =
            IndexRequestPolicy.sourceRetryRequest(
                checkpoint = null,
                currentGeneration = 7L,
                configuredSourceKeys = emptySet(),
                hasRevision = true,
                allowUnscopedSources = true,
            )

        requireNotNull(request)
        assertEquals(IndexReason.USER_REFRESH, request.reason)
        assertNull(request.sourceKeys)
    }

    @Test
    fun `explicit-root refresh still rejects empty configured keys`() {
        assertNull(
            IndexRequestPolicy.sourceRetryRequest(
                checkpoint = null,
                currentGeneration = 7L,
                configuredSourceKeys = emptySet(),
                hasRevision = true,
            )
        )
    }

    @Test
    fun `explicit-root retry drops stale checkpoint keys when roots are empty`() {
        val checkpoint =
            SourceConfigurationCheckpoint(
                generation = 7L,
                state = SourceConfigurationCheckpoint.State.FAILED_RETRYABLE,
                unresolvedSourceKeys = setOf("direct:stale"),
            )

        assertNull(
            IndexRequestPolicy.sourceRetryRequest(
                checkpoint = checkpoint,
                currentGeneration = 7L,
                configuredSourceKeys = emptySet(),
                hasRevision = true,
            )
        )
    }

    @Test
    fun `explicit-root retry intersects unresolved checkpoint keys with current roots`() {
        val checkpoint =
            SourceConfigurationCheckpoint(
                generation = 7L,
                state = SourceConfigurationCheckpoint.State.FAILED_RETRYABLE,
                unresolvedSourceKeys = setOf("direct:current", "direct:stale"),
            )

        val request =
            IndexRequestPolicy.sourceRetryRequest(
                checkpoint = checkpoint,
                currentGeneration = 7L,
                configuredSourceKeys = setOf("direct:current"),
                hasRevision = true,
            )

        requireNotNull(request)
        assertEquals(setOf("direct:current"), request.sourceKeys)
    }

    @Test
    fun `provider retry retains discovered unresolved keys without configured roots`() {
        val checkpoint =
            SourceConfigurationCheckpoint(
                generation = 7L,
                state = SourceConfigurationCheckpoint.State.FAILED_RETRYABLE,
                unresolvedSourceKeys = setOf("internal:external_primary"),
            )

        val request =
            IndexRequestPolicy.sourceRetryRequest(
                checkpoint = checkpoint,
                currentGeneration = 7L,
                configuredSourceKeys = emptySet(),
                hasRevision = true,
                allowUnscopedSources = true,
            )

        requireNotNull(request)
        assertEquals(setOf("internal:external_primary"), request.sourceKeys)
    }
}
