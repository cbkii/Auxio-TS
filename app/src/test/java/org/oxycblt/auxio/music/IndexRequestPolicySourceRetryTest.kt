/*
 * Copyright (c) 2026 Auxio Project
 * IndexRequestPolicySourceRetryTest.kt is part of Auxio.
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
}
