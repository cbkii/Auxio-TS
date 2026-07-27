/* Copyright (c) 2026 Auxio Project; GPL-3.0-or-later. */

package org.oxycblt.auxio.headunit.topway

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class Ts18LauncherIntegrationModeTest {
    @Test
    fun `default policy is explicit for both compatibility states`() {
        assertEquals(
            Ts18LauncherIntegrationMode.GenericDofunMedia,
            Ts18LauncherIntegrationMode.defaultFor(topwayCompatFlavor = true),
        )
        assertEquals(
            Ts18LauncherIntegrationMode.AndroidMediaSessionOnly,
            Ts18LauncherIntegrationMode.defaultFor(topwayCompatFlavor = false),
        )
    }

    @Test
    fun `mode gating matches launcher plan`() {
        assertTrue(Ts18LauncherIntegrationMode.GenericDofunMedia.usesGenericDofunProfile)
        assertFalse(Ts18LauncherIntegrationMode.GenericDofunMedia.sendsTopwayBroadcasts)
        assertFalse(Ts18LauncherIntegrationMode.GenericDofunMedia.handlesTopwayCommands)
        assertFalse(Ts18LauncherIntegrationMode.GenericDofunMedia.bindsTopwayCommandService)
        assertFalse(Ts18LauncherIntegrationMode.Disabled.sendsTopwayBroadcasts)
        assertFalse(Ts18LauncherIntegrationMode.DiagnosticsOnly.handlesTopwayCommands)
        assertTrue(Ts18LauncherIntegrationMode.TopwayBroadcastOnly.sendsTopwayBroadcasts)
        assertFalse(Ts18LauncherIntegrationMode.TopwayBroadcastOnly.handlesTopwayCommands)
        assertTrue(Ts18LauncherIntegrationMode.TopwayCommandOnly.handlesTopwayCommands)
        assertFalse(Ts18LauncherIntegrationMode.TopwayCommandOnly.sendsTopwayBroadcasts)
        assertTrue(Ts18LauncherIntegrationMode.AutoAllSafePaths.sendsTopwayBroadcasts)
        assertTrue(Ts18LauncherIntegrationMode.AutoAllSafePaths.handlesTopwayCommands)
        assertTrue(Ts18LauncherIntegrationMode.AutoAllSafePaths.bindsTopwayCommandService)
        assertTrue(Ts18LauncherIntegrationMode.DiagnosticsOnly.bindsTopwayCommandService)
    }

    @Test
    fun `unset topway preference adopts generic media once`() {
        val decision =
            Ts18LauncherIntegrationMode.migrationDecision(
                persistedValue = null,
                migrationComplete = false,
                topwayCompatFlavor = true,
            )
        assertEquals(Ts18LauncherIntegrationMode.GenericDofunMedia, decision.mode)
        assertEquals(Ts18LauncherIntegrationMode.GenericDofunMedia, decision.persistMode)
        assertTrue(decision.markComplete)
    }

    @Test
    fun `persisted all safe paths survives migration`() {
        val decision =
            Ts18LauncherIntegrationMode.migrationDecision(
                persistedValue = Ts18LauncherIntegrationMode.AutoAllSafePaths.name,
                migrationComplete = false,
                topwayCompatFlavor = true,
            )
        assertEquals(Ts18LauncherIntegrationMode.AutoAllSafePaths, decision.mode)
        assertNull(decision.persistMode)
        assertTrue(decision.markComplete)
    }

    @Test
    fun `explicit legacy fallback survives migration`() {
        val decision =
            Ts18LauncherIntegrationMode.migrationDecision(
                persistedValue = Ts18LauncherIntegrationMode.TopwayCommandOnly.name,
                migrationComplete = false,
                topwayCompatFlavor = true,
            )
        assertEquals(Ts18LauncherIntegrationMode.TopwayCommandOnly, decision.mode)
        assertNull(decision.persistMode)
        assertTrue(decision.markComplete)
    }

    @Test
    fun `completed or non Topway migration does not rewrite preferences`() {
        val completed =
            Ts18LauncherIntegrationMode.migrationDecision(
                persistedValue = Ts18LauncherIntegrationMode.AutoAllSafePaths.name,
                migrationComplete = true,
                topwayCompatFlavor = true,
            )
        assertEquals(Ts18LauncherIntegrationMode.AutoAllSafePaths, completed.mode)
        assertNull(completed.persistMode)
        assertFalse(completed.markComplete)

        val nonTopway =
            Ts18LauncherIntegrationMode.migrationDecision(
                persistedValue = null,
                migrationComplete = false,
                topwayCompatFlavor = false,
            )
        assertEquals(Ts18LauncherIntegrationMode.AndroidMediaSessionOnly, nonTopway.mode)
        assertNull(nonTopway.persistMode)
        assertFalse(nonTopway.markComplete)
    }
}
