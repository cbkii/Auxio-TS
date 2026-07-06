package org.oxycblt.auxio.headunit.topway

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class Ts18LauncherIntegrationModeTest {
    @Test
    fun `mode gating matches launcher plan`() {
        assertFalse(Ts18LauncherIntegrationMode.Disabled.sendsTopwayBroadcasts)
        assertFalse(Ts18LauncherIntegrationMode.DiagnosticsOnly.handlesTopwayCommands)
        assertTrue(Ts18LauncherIntegrationMode.TopwayBroadcastOnly.sendsTopwayBroadcasts)
        assertFalse(Ts18LauncherIntegrationMode.TopwayBroadcastOnly.handlesTopwayCommands)
        assertTrue(Ts18LauncherIntegrationMode.TopwayCommandOnly.handlesTopwayCommands)
        assertFalse(Ts18LauncherIntegrationMode.TopwayCommandOnly.sendsTopwayBroadcasts)
        assertTrue(Ts18LauncherIntegrationMode.AutoAllSafePaths.sendsTopwayBroadcasts)
        assertTrue(Ts18LauncherIntegrationMode.AutoAllSafePaths.handlesTopwayCommands)
    }
}
