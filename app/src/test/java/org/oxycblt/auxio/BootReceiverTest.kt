/*
 * Copyright (c) 2026 Auxio Project
 * BootReceiverTest.kt is part of Auxio.
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

package org.oxycblt.auxio

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.oxycblt.auxio.headunit.overlay.FloatingOnlyStartupCoordinator

class BootReceiverTest {
    @Test
    fun `disabled autostart does not launch even when floating-only remains set`() {
        assertEquals(
            BootLaunchPolicy.Route.DISABLED,
            BootLaunchPolicy.route(autostartOnBoot = false, floatingOnly = true),
        )
    }

    @Test
    fun `floating-only autostart owns the overlay-only boot route`() {
        assertEquals(
            BootLaunchPolicy.Route.FLOATING_CONTROLS_ONLY,
            BootLaunchPolicy.route(autostartOnBoot = true, floatingOnly = true),
        )
    }

    @Test
    fun `normal autostart opens the full player`() {
        assertEquals(
            BootLaunchPolicy.Route.FULL_PLAYER,
            BootLaunchPolicy.route(autostartOnBoot = true, floatingOnly = false),
        )
    }

    @Test
    fun `headless floating startup requires both boot and floating-only preferences`() {
        assertTrue(
            FloatingOnlyStartupCoordinator.isConfigured(
                autostartOnBoot = true,
                floatingOnly = true,
            )
        )
        assertFalse(
            FloatingOnlyStartupCoordinator.isConfigured(
                autostartOnBoot = true,
                floatingOnly = false,
            )
        )
        assertFalse(
            FloatingOnlyStartupCoordinator.isConfigured(
                autostartOnBoot = false,
                floatingOnly = true,
            )
        )
    }
}
