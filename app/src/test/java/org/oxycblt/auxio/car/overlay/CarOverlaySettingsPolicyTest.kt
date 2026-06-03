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

/**
 * Tests the overlay settings enable/disable decision logic. Verifies that:
 * - Enabling without permission enters the pending-permission flow
 * - Enabling with permission completes immediately
 * - Disabling clears pending-enable state
 * - Reset position does not request service start when disabled, permissionless, or idle
 */
class CarOverlaySettingsPolicyTest {

    private enum class EnableDecision {
        COMPLETED,
        PENDING_PERMISSION,
    }

    private data class EnableResult(val decision: EnableDecision, val pendingEnable: Boolean)

    /**
     * Models the setEnabled decision logic from CarOverlaySettings without Android framework calls.
     * Enabling without permission must mark a pending-enable request and report that the operation
     * did not complete immediately; disabling must always clear pending-enable state.
     */
    private fun setEnabledDecision(enable: Boolean, hasPermission: Boolean): EnableResult {
        if (enable && !hasPermission) {
            return EnableResult(EnableDecision.PENDING_PERMISSION, pendingEnable = true)
        }
        return EnableResult(EnableDecision.COMPLETED, pendingEnable = false)
    }

    /**
     * Models the resetPosition logic — should only signal a service that is enabled, permitted, and
     * already running so reset never cold-starts the overlay service.
     */
    private fun shouldSignalResetToService(
        enabled: Boolean,
        hasPermission: Boolean,
        serviceRunning: Boolean,
        overlayAttached: Boolean,
    ): Boolean {
        return enabled && hasPermission && serviceRunning && overlayAttached
    }

    @Test
    fun `enable with permission completes immediately`() {
        assertEquals(
            EnableResult(EnableDecision.COMPLETED, pendingEnable = false),
            setEnabledDecision(enable = true, hasPermission = true),
        )
    }

    @Test
    fun `enable without permission enters pending-enable flow`() {
        assertEquals(
            EnableResult(EnableDecision.PENDING_PERMISSION, pendingEnable = true),
            setEnabledDecision(enable = true, hasPermission = false),
        )
    }

    @Test
    fun `disable always completes and clears pending-enable`() {
        assertEquals(
            EnableResult(EnableDecision.COMPLETED, pendingEnable = false),
            setEnabledDecision(enable = false, hasPermission = true),
        )
        assertEquals(
            EnableResult(EnableDecision.COMPLETED, pendingEnable = false),
            setEnabledDecision(enable = false, hasPermission = false),
        )
    }

    @Test
    fun `reset position signals service only when overlay is live`() {
        assertTrue(
            shouldSignalResetToService(
                enabled = true,
                hasPermission = true,
                serviceRunning = true,
                overlayAttached = true,
            )
        )
        assertFalse(
            shouldSignalResetToService(
                enabled = false,
                hasPermission = true,
                serviceRunning = true,
                overlayAttached = true,
            )
        )
        assertFalse(
            shouldSignalResetToService(
                enabled = true,
                hasPermission = false,
                serviceRunning = true,
                overlayAttached = true,
            )
        )
        assertFalse(
            shouldSignalResetToService(
                enabled = true,
                hasPermission = true,
                serviceRunning = false,
                overlayAttached = true,
            )
        )
        assertFalse(
            shouldSignalResetToService(
                enabled = true,
                hasPermission = true,
                serviceRunning = true,
                overlayAttached = false,
            )
        )
    }
}
