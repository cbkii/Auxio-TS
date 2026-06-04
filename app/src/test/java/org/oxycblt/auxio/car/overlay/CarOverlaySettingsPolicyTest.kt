/*
 * Copyright (c) 2024 Auxio Project
 * CarOverlaySettingsPolicyTest.kt is part of Auxio.
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

package org.oxycblt.auxio.car.overlay

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CarOverlaySettingsPolicyTest {

    @Test
    fun `enable with permission completes immediately and starts service`() {
        assertEquals(
            CarOverlaySettingsPolicy.EnableResult(
                decision = CarOverlaySettingsPolicy.EnableDecision.COMPLETED,
                enabled = true,
                pendingEnable = false,
                launchPermissionFlow = false,
                startService = true,
                stopService = false,
            ),
            CarOverlaySettingsPolicy.setEnabledDecision(
                requestedEnabled = true,
                hasOverlayPermission = true,
            ),
        )
    }

    @Test
    fun `enable without permission keeps switch unchecked and records pending enable`() {
        assertEquals(
            CarOverlaySettingsPolicy.EnableResult(
                decision = CarOverlaySettingsPolicy.EnableDecision.PENDING_PERMISSION,
                enabled = false,
                pendingEnable = true,
                launchPermissionFlow = true,
                startService = false,
                stopService = false,
            ),
            CarOverlaySettingsPolicy.setEnabledDecision(
                requestedEnabled = true,
                hasOverlayPermission = false,
            ),
        )
    }

    @Test
    fun `disable always clears enabled and pending enable and stops service`() {
        val expected =
            CarOverlaySettingsPolicy.EnableResult(
                decision = CarOverlaySettingsPolicy.EnableDecision.COMPLETED,
                enabled = false,
                pendingEnable = false,
                launchPermissionFlow = false,
                startService = false,
                stopService = true,
            )
        assertEquals(
            expected,
            CarOverlaySettingsPolicy.setEnabledDecision(
                requestedEnabled = false,
                hasOverlayPermission = true,
            ),
        )
        assertEquals(
            expected,
            CarOverlaySettingsPolicy.setEnabledDecision(
                requestedEnabled = false,
                hasOverlayPermission = false,
            ),
        )
    }

    @Test
    fun `reset position signals service only when overlay is live`() {
        assertTrue(
            CarOverlaySettingsPolicy.shouldSignalResetToService(
                enabled = true,
                hasOverlayPermission = true,
                overlayLive = true,
            )
        )
        assertFalse(
            CarOverlaySettingsPolicy.shouldSignalResetToService(
                enabled = false,
                hasOverlayPermission = true,
                overlayLive = true,
            )
        )
        assertFalse(
            CarOverlaySettingsPolicy.shouldSignalResetToService(
                enabled = true,
                hasOverlayPermission = false,
                overlayLive = true,
            )
        )
        assertFalse(
            CarOverlaySettingsPolicy.shouldSignalResetToService(
                enabled = true,
                hasOverlayPermission = true,
                overlayLive = false,
            )
        )
    }

    @Test
    fun `overlay live requires created service and attached overlay`() {
        assertTrue(
            CarOverlaySettingsPolicy.overlayLive(serviceCreated = true, overlayAttached = true)
        )
        assertFalse(
            CarOverlaySettingsPolicy.overlayLive(serviceCreated = false, overlayAttached = true)
        )
        assertFalse(
            CarOverlaySettingsPolicy.overlayLive(serviceCreated = true, overlayAttached = false)
        )
        assertFalse(
            CarOverlaySettingsPolicy.overlayLive(serviceCreated = false, overlayAttached = false)
        )
    }
}
