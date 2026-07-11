/*
 * Copyright (c) 2026 Auxio Project
 * CarOverlaySettingsTest.kt is part of Auxio.
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

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CarOverlaySettingsTest {

    private lateinit var context: Context
    private lateinit var prefs: CarOverlayPrefs

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        prefs = CarOverlayPrefs.from(context)
    }

    @Test
    fun testDisableSettingsClearsState() {
        prefs.enabled = true
        CarOverlaySettingsPolicy.setEnabledDecision(false, true)

        // This is pure policy testing, no side effects
        val result = CarOverlaySettingsPolicy.setEnabledDecision(false, true)
        assertFalse(result.enabled)
        assertTrue(result.stopService)
        assertFalse(result.startService)
    }

    @Test
    fun testEnableWithoutPermissionCreatesPendingState() {
        prefs.enabled = false
        val result = CarOverlaySettingsPolicy.setEnabledDecision(true, false)
        assertFalse(result.enabled)
        assertTrue(result.pendingEnable)
        assertTrue(result.launchPermissionFlow)
        assertFalse(result.startService)
    }

    @Test
    fun testEnableWithPermissionCompletes() {
        prefs.enabled = false
        val result = CarOverlaySettingsPolicy.setEnabledDecision(true, true)
        assertTrue(result.enabled)
        assertFalse(result.pendingEnable)
        assertFalse(result.launchPermissionFlow)
        assertTrue(result.startService)
    }
}
