/*
 * Copyright (c) 2026 Auxio Project
 * CarOverlayVisibilityHooksTest.kt is part of Auxio.
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

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CarOverlayVisibilityHooksTest {

    private lateinit var lifecycleOwner: TestLifecycleOwner
    private lateinit var hooks: CarOverlayVisibilityHooks

    class TestLifecycleOwner : LifecycleOwner {
        val lifecycleRegistry = LifecycleRegistry(this)
        override val lifecycle: Lifecycle
            get() = lifecycleRegistry
    }

    @Before
    fun setUp() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        hooks = CarOverlayVisibilityHooks(app)
        lifecycleOwner = TestLifecycleOwner()
        lifecycleOwner.lifecycleRegistry.addObserver(hooks)
        CarOverlayVisibilityHooks.isSuppressedByAuxioForeground = false
    }

    @Test
    fun testProcessStartSetsSuppressionIfHideEnabled() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val prefs = CarOverlayPrefs.from(app)
        prefs.enabled = true
        prefs.hideWhileAuxioForeground = true

        lifecycleOwner.lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)

        assertTrue(CarOverlayVisibilityHooks.isSuppressedByAuxioForeground)
    }

    @Test
    fun testProcessStopClearsSuppression() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val prefs = CarOverlayPrefs.from(app)
        prefs.enabled = true
        prefs.hideWhileAuxioForeground = true

        // Simulate going into foreground first
        lifecycleOwner.lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        assertTrue(CarOverlayVisibilityHooks.isSuppressedByAuxioForeground)

        // Then go into background
        lifecycleOwner.lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)

        assertFalse(CarOverlayVisibilityHooks.isSuppressedByAuxioForeground)
    }
}
