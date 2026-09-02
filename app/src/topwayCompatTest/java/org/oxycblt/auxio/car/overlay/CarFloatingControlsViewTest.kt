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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.oxycblt.auxio.car.overlay

import android.content.Context
import android.view.MotionEvent
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
        context.resources.displayMetrics.widthPixels = 1280
    }

    @Test
    fun controlsModeLeavesExistingControlsAsOnlyRow() {
        val view = CarFloatingControlsView(context, NoOpCallbacks)

        assertEquals(1, view.childCount)
        assertEquals(5, (view.getChildAt(0) as LinearLayout).childCount)
    }

    @Test
    fun controlsAndTickerAddsEqualSizedRowAboveControls() {
        CarOverlayPrefs.from(context).displayMode = CarOverlayPrefs.DisplayMode.CONTROLS_AND_TICKER

        val view = CarFloatingControlsView(context, NoOpCallbacks)
        val ticker = view.getChildAt(0) as TextView
        val controls = view.getChildAt(1) as LinearLayout

        assertEquals(2, view.childCount)
        assertEquals(controls.layoutParams.width, ticker.layoutParams.width)
        assertEquals(controls.layoutParams.height, ticker.layoutParams.height)
        assertEquals(5, controls.childCount)
        assertTrue(ticker.isSelected)
    }

    @Test
    fun tickerOnlyUsesConfiguredWidthAndNoControlsRow() {
        val baseline = CarFloatingControlsView(context, NoOpCallbacks)
        val baselineWidth = baseline.getChildAt(0).layoutParams.width
        val prefs = CarOverlayPrefs.from(context)
        prefs.displayMode = CarOverlayPrefs.DisplayMode.TICKER_ONLY
        prefs.tickerWidthPercent = 200

        val view = CarFloatingControlsView(context, NoOpCallbacks)
        val ticker = view.getChildAt(0) as TextView

        assertEquals(1, view.childCount)
        assertEquals(baselineWidth * 2, ticker.layoutParams.width)
        assertTrue(ticker.isClickable)
        assertTrue(ticker.isFocusable)
    }

    @Test
    fun tickerOnlyWidthIsCappedToPhysicalDisplayWidth() {
        val prefs = CarOverlayPrefs.from(context)
        prefs.displayMode = CarOverlayPrefs.DisplayMode.TICKER_ONLY
        prefs.tickerWidthPercent = 300
        context.resources.displayMetrics.widthPixels = 700

        val view = CarFloatingControlsView(context, NoOpCallbacks)
        val ticker = view.getChildAt(0) as TextView

        assertEquals(700, ticker.layoutParams.width)
    }

    @Test
    fun tickerActsAsDragSurfaceInTickerOnlyMode() {
        val prefs = CarOverlayPrefs.from(context)
        prefs.displayMode = CarOverlayPrefs.DisplayMode.TICKER_ONLY
        val callbacks = RecordingCallbacks()
        val ticker = CarFloatingControlsView(context, callbacks).getChildAt(0) as TextView

        ticker.dispatchTouchEvent(event(MotionEvent.ACTION_DOWN, 100f, 100f, 0L))
        ticker.dispatchTouchEvent(event(MotionEvent.ACTION_MOVE, 180f, 100f, 50L))
        ticker.dispatchTouchEvent(event(MotionEvent.ACTION_UP, 180f, 100f, 100L))

        assertTrue(callbacks.dragCalls > 0)
        assertEquals(1, callbacks.dragFinishedCalls)
    }

    private fun event(action: Int, x: Float, y: Float, time: Long): MotionEvent =
        MotionEvent.obtain(0L, time, action, x, y, 0)

    private class RecordingCallbacks : CarFloatingControlsView.Callbacks {
        var dragCalls = 0
        var dragFinishedCalls = 0

        override fun onDrag(deltaX: Int, deltaY: Int) {
            dragCalls++
        }

        override fun onDragFinished(x: Int, y: Int) {
            dragFinishedCalls++
        }

        override fun onPrevious() = Unit

        override fun onPlayPause() = Unit

        override fun onNext() = Unit

        override fun onOpenAuxio() = Unit

        override fun onStopRequested() = Unit
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
