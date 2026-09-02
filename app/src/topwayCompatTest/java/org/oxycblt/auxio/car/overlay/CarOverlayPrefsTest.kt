/*
 * Copyright (c) 2026 Auxio Project
 * CarOverlayPrefsTest.kt is part of Auxio.
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
import androidx.preference.PreferenceManager
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CarOverlayPrefsTest {

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        PreferenceManager.getDefaultSharedPreferences(context).edit().clear().commit()
    }

    @Test
    fun testMigrationRemovesSuppressedFlag() {
        val prefs =
            context.getSharedPreferences(context.packageName + "_preferences", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("car_overlay_suppressed_auxio_fg", true).commit()

        assertTrue(prefs.contains("car_overlay_suppressed_auxio_fg"))

        CarOverlayPrefs.from(context)

        assertFalse(prefs.contains("car_overlay_suppressed_auxio_fg"))
    }

    @Test
    fun displayModeDefaultsToControls() {
        assertEquals(
            CarOverlayPrefs.DisplayMode.CONTROLS,
            CarOverlayPrefs.from(context).displayMode,
        )
    }

    @Test
    fun legacyTickerEnabledMigratesToControlsAndTicker() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs.edit().putBoolean("car_overlay_show_track_ticker", true).commit()

        val overlayPrefs = CarOverlayPrefs.from(context)

        assertEquals(CarOverlayPrefs.DisplayMode.CONTROLS_AND_TICKER, overlayPrefs.displayMode)
        assertFalse(prefs.contains("car_overlay_show_track_ticker"))
        assertEquals(
            CarOverlayPrefs.DisplayMode.CONTROLS_AND_TICKER.storageValue,
            prefs.getString(CarOverlayPrefs.KEY_DISPLAY_MODE, null),
        )
    }

    @Test
    fun legacyTickerDisabledMigratesToControls() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs.edit().putBoolean("car_overlay_show_track_ticker", false).commit()

        assertEquals(
            CarOverlayPrefs.DisplayMode.CONTROLS,
            CarOverlayPrefs.from(context).displayMode,
        )
        assertFalse(prefs.contains("car_overlay_show_track_ticker"))
    }

    @Test
    fun tickerOnlyModeAndWidthPersist() {
        val overlayPrefs = CarOverlayPrefs.from(context)

        overlayPrefs.displayMode = CarOverlayPrefs.DisplayMode.TICKER_ONLY
        overlayPrefs.tickerWidthPercent = 250

        val restored = CarOverlayPrefs.from(context)
        assertEquals(CarOverlayPrefs.DisplayMode.TICKER_ONLY, restored.displayMode)
        assertEquals(250, restored.tickerWidthPercent)
    }

    @Test
    fun invalidTickerWidthFallsBackToOneHundredPercent() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs.edit().putInt(CarOverlayPrefs.KEY_TICKER_WIDTH_PERCENT, 175).commit()

        assertEquals(100, CarOverlayPrefs.from(context).tickerWidthPercent)
    }
}
