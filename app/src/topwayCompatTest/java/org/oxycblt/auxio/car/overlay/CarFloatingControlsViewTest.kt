/*
 * Copyright (c) 2026 Auxio Project
 * CarFloatingControlsViewTest.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.oxycblt.auxio.car.overlay

import android.content.Context
import android.widget.LinearLayout
import android.widget.TextView
import androidx.preference.PreferenceManager
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CarFloatingControlsViewTest {

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        PreferenceManager.getDefaultSharedPreferences(context).edit().clear().commit()
    }

    @Test
    fun disabledTickerLeavesExistingControlsAsOnlyRow() {
        val view = CarFloatingControlsView(context, NoOpCallbacks)

        assertEquals(1, view.childCount)
        assertEquals(5, (view.getChildAt(0) as LinearLayout).childCount)
    }

    @Test
    fun enabledTickerAddsEqualSizedRowAboveControls() {
        CarOverlayPrefs.from(context).showTrackTicker = true

        val view = CarFloatingControlsView(context, NoOpCallbacks)
        val ticker = view.getChildAt(0) as TextView
        val controls = view.getChildAt(1) as LinearLayout

        assertEquals(2, view.childCount)
        assertEquals(controls.layoutParams.width, ticker.layoutParams.width)
        assertEquals(controls.layoutParams.height, ticker.layoutParams.height)
        assertEquals(5, controls.childCount)
        assertTrue(ticker.isSelected)
    }

    private object NoOpCallbacks : CarFloatingControlsView.Callbacks {
        override fun onDrag(deltaX: Int, deltaY: Int) = Unit

        override fun onDragFinished(x: Int, y: Int) = Unit

        override fun onPrevious() = Unit

        override fun onPlayPause() = Unit

        override fun onNext() = Unit

        override fun onOpenAuxio() = Unit

        override fun onStopRequested() = Unit
    }
}
