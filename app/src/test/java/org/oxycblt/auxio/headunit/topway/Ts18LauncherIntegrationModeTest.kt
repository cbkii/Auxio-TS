/*
 * Copyright (c) 2026 Auxio Project
 * Ts18LauncherIntegrationModeTest.kt is part of Auxio.
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

package org.oxycblt.auxio.headunit.topway

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.oxycblt.auxio.BuildConfig

class Ts18LauncherIntegrationModeTest {
    @Test
    fun `default follows build flavor`() {
        val expected =
            if (BuildConfig.TOPWAY_COMPAT_FLAVOR) {
                Ts18LauncherIntegrationMode.AutoAllSafePaths
            } else {
                Ts18LauncherIntegrationMode.AndroidMediaSessionOnly
            }
        assertTrue(Ts18LauncherIntegrationMode.default() == expected)
        assertTrue(Ts18LauncherIntegrationMode.fromPreference("not-a-mode") == expected)
    }

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
