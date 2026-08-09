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
    fun `mode gating keeps generic and Topway transports independent`() {
        with(Ts18LauncherIntegrationMode.GenericDofunMedia) {
            assertTrue(usesGenericDofunProfile)
            assertTrue(usesGenericDofunNotificationProfile)
            assertTrue(publishesLegacyAndroidMediaBroadcasts)
            assertFalse(sendsTopwayBroadcasts)
            assertFalse(handlesTopwayCommands)
            assertFalse(bindsTopwayCommandService)
        }

        with(Ts18LauncherIntegrationMode.TopwayBroadcastOnly) {
            assertFalse(usesGenericDofunNotificationProfile)
            assertFalse(publishesLegacyAndroidMediaBroadcasts)
            assertTrue(sendsTopwayBroadcasts)
            assertFalse(handlesTopwayCommands)
        }

        with(Ts18LauncherIntegrationMode.TopwayCommandOnly) {
            assertFalse(publishesLegacyAndroidMediaBroadcasts)
            assertTrue(handlesTopwayCommands)
            assertFalse(sendsTopwayBroadcasts)
            assertTrue(bindsTopwayCommandService)
        }

        with(Ts18LauncherIntegrationMode.AutoAllSafePaths) {
            assertFalse(usesGenericDofunProfile)
            assertTrue(usesGenericDofunNotificationProfile)
            assertTrue(publishesLegacyAndroidMediaBroadcasts)
            assertTrue(sendsTopwayBroadcasts)
            assertTrue(handlesTopwayCommands)
            assertTrue(bindsTopwayCommandService)
        }

        assertFalse(Ts18LauncherIntegrationMode.Disabled.sendsTopwayBroadcasts)
        assertFalse(Ts18LauncherIntegrationMode.Disabled.publishesLegacyAndroidMediaBroadcasts)
        assertFalse(Ts18LauncherIntegrationMode.DiagnosticsOnly.handlesTopwayCommands)
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
