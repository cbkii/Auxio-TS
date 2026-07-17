/*
 * Copyright (c) 2026 Auxio Project
 * CategorySubscriptionGateTest.kt is part of Auxio.
 */

package org.oxycblt.auxio.home

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.oxycblt.auxio.music.MusicType

class CategorySubscriptionGateTest {
    @Test
    fun `inactive invalidations conflate until category is activated`() {
        val gate = CategorySubscriptionGate(MusicType.SONGS)

        assertFalse(gate.invalidate(MusicType.ALBUMS))
        assertFalse(gate.invalidate(MusicType.ALBUMS))
        assertTrue(gate.activate(MusicType.ALBUMS))
        assertFalse(gate.activate(MusicType.ALBUMS))
    }

    @Test
    fun `active category invalidation executes immediately`() {
        val gate = CategorySubscriptionGate(MusicType.SONGS)

        assertTrue(gate.invalidate(MusicType.SONGS))
        assertTrue(gate.invalidate(MusicType.SONGS))
    }

    @Test
    fun `invalidate all leaves one pending refresh per inactive category`() {
        val gate = CategorySubscriptionGate(MusicType.SONGS)

        assertTrue(gate.invalidateAll())
        assertTrue(gate.activate(MusicType.GENRES))
        assertFalse(gate.activate(MusicType.GENRES))
    }
}
