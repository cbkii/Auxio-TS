/*
 * Copyright (c) 2024 Auxio Project
 * CarOverlaySettingsPolicy.kt is part of Auxio.
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

/** Pure decision policy for the Topway car floating-controls settings surface. */
object CarOverlaySettingsPolicy {
    enum class EnableDecision {
        COMPLETED,
        PENDING_PERMISSION,
    }

    /**
     * Complete result of applying an enable/disable request to persisted overlay settings.
     *
     * [enabled] and [pendingEnable] are the values that should be written to the overlay
     * preferences. The service and permission flags describe the Android side effects that should
     * run after those persisted values are applied.
     */
    data class EnableResult(
        val decision: EnableDecision,
        val enabled: Boolean,
        val pendingEnable: Boolean,
        val launchPermissionFlow: Boolean,
        val startService: Boolean,
        val stopService: Boolean,
    ) {
        val completedImmediately: Boolean
            get() = decision == EnableDecision.COMPLETED
    }

    /** Decide how to persist and execute an overlay enable/disable request. */
    fun setEnabledDecision(requestedEnabled: Boolean, hasOverlayPermission: Boolean): EnableResult {
        if (requestedEnabled && !hasOverlayPermission) {
            return EnableResult(
                decision = EnableDecision.PENDING_PERMISSION,
                enabled = false,
                pendingEnable = true,
                launchPermissionFlow = true,
                startService = false,
                stopService = false,
            )
        }
        if (requestedEnabled) {
            return EnableResult(
                decision = EnableDecision.COMPLETED,
                enabled = true,
                pendingEnable = false,
                launchPermissionFlow = false,
                startService = true,
                stopService = false,
            )
        }
        return EnableResult(
            decision = EnableDecision.COMPLETED,
            enabled = false,
            pendingEnable = false,
            launchPermissionFlow = false,
            startService = false,
            stopService = true,
        )
    }

    /** True only when the overlay runtime is known to be alive and attached. */
    fun overlayLive(serviceCreated: Boolean, overlayAttached: Boolean): Boolean =
        serviceCreated && overlayAttached

    /** Decide whether a reset-position action should be delivered to the overlay service. */
    fun shouldSignalResetToService(
        enabled: Boolean,
        hasOverlayPermission: Boolean,
        overlayLive: Boolean,
    ): Boolean = enabled && hasOverlayPermission && overlayLive
}
