/*
 * Copyright (c) 2026 Auxio Project
 * PlaybackLayoutInflationTest.kt is part of Auxio.
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

package org.oxycblt.auxio.ui

import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.widget.FrameLayout
import org.junit.Test
import org.junit.runner.RunWith
import org.oxycblt.auxio.R
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

/**
 * Regression test for the release crash caused by MaterialButton inflation failures when
 * Material3Expressive icon button styles are combined with materialSizeOverlay inside a
 * MaterialButtonGroup. This test inflates the actual playback layouts under Theme.Auxio.App to
 * ensure no UnsupportedOperationException occurs during attribute resolution.
 *
 * See: fragment_playback_bar line #77 crash with WidthFixMaterialButton / MaterialButton.
 */
@RunWith(RobolectricTestRunner::class)
class PlaybackLayoutInflationTest {

    private fun themedInflater(): LayoutInflater {
        val context =
            ContextThemeWrapper(RuntimeEnvironment.getApplication(), R.style.Theme_Auxio_App)
        return LayoutInflater.from(context)
    }

    @Test
    @Config(qualifiers = "w1280dp-h720dp-land")
    fun inflatePlaybackBar_doesNotCrash() {
        val inflater = themedInflater()
        val parent = FrameLayout(inflater.context)
        // This is the exact crash path: inflating fragment_playback_bar with MaterialButtonGroup
        // containing WidthFixMaterialButton/RippleFixMaterialButton with size overlays.
        inflater.inflate(R.layout.fragment_playback_bar, parent, false)
    }

    @Test
    @Config(qualifiers = "w320dp-h320dp-port")
    fun inflatePlaybackPanel_default_doesNotCrash() {
        val inflater = themedInflater()
        val parent = FrameLayout(inflater.context)
        inflater.inflate(R.layout.fragment_playback_panel, parent, false)
    }

    @Test
    @Config(qualifiers = "w640dp-h360dp-land")
    fun inflatePlaybackPanel_h360dp_doesNotCrash() {
        val inflater = themedInflater()
        val parent = FrameLayout(inflater.context)
        inflater.inflate(R.layout.fragment_playback_panel, parent, false)
    }

    @Test
    @Config(qualifiers = "w720dp-h720dp-port")
    fun inflatePlaybackPanel_h520dp_doesNotCrash() {
        val inflater = themedInflater()
        val parent = FrameLayout(inflater.context)
        inflater.inflate(R.layout.fragment_playback_panel, parent, false)
    }

    @Test
    @Config(qualifiers = "w1280dp-h720dp-land")
    fun inflatePlaybackPanel_landscape_doesNotCrash() {
        val inflater = themedInflater()
        val parent = FrameLayout(inflater.context)
        inflater.inflate(R.layout.fragment_playback_panel, parent, false)
    }

    @Test
    @Config(qualifiers = "w412dp-h915dp-port")
    fun inflateDialogSort_doesNotCrash() {
        val inflater = themedInflater()
        val parent = FrameLayout(inflater.context)
        inflater.inflate(R.layout.dialog_sort, parent, false)
    }
}
