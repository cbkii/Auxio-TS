package org.oxycblt.auxio.music

import org.junit.Assert.assertEquals
import org.junit.Test

class StartupReadinessTransitionsTest {
    @Test
    fun `startup readiness does not regress after fast data is ready`() {
        val current = StartupReadinessState.SearchReady
        assertEquals(
            current,
            StartupReadinessTransitions.advance(current, StartupReadinessState.ProcessVisible),
        )
    }

    @Test
    fun `startup readiness advances through full library and enrichment`() {
        assertEquals(
            StartupReadinessState.FullLibraryReady,
            StartupReadinessTransitions.advance(
                StartupReadinessState.SearchReady,
                StartupReadinessState.FullLibraryReady,
            ),
        )
        assertEquals(
            StartupReadinessState.EnrichmentComplete,
            StartupReadinessTransitions.advance(
                StartupReadinessState.FullLibraryReady,
                StartupReadinessState.EnrichmentComplete,
            ),
        )
    }
}
