/* Copyright (c) 2026 Auxio Project */
package org.oxycblt.auxio.car.overlay

import android.content.Intent
import org.junit.Assert.assertEquals
import org.junit.Test

class TopwayMusicEntryPolicyTest {
    @Test
    fun mainEntryUsesFloatingControlsWhenConfigured() {
        assertEquals(
            TopwayMusicEntryPolicy.Route.FLOATING_CONTROLS_ONLY,
            TopwayMusicEntryPolicy.route(Intent.ACTION_MAIN, hasData = false, floatingOnly = true),
        )
    }

    @Test
    fun viewIntentAlwaysOpensFullPlayer() {
        assertEquals(
            TopwayMusicEntryPolicy.Route.FULL_PLAYER,
            TopwayMusicEntryPolicy.route(Intent.ACTION_VIEW, hasData = true, floatingOnly = true),
        )
    }

    @Test
    fun normalMainEntryOpensFullPlayer() {
        assertEquals(
            TopwayMusicEntryPolicy.Route.FULL_PLAYER,
            TopwayMusicEntryPolicy.route(Intent.ACTION_MAIN, hasData = false, floatingOnly = false),
        )
    }
}
