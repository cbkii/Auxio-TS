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
 * Regression tests for layout inflation under [R.style.Theme_Auxio_App].
 *
 * The v4.0.19/v4.0.20 release crash was caused by MaterialButton inflation failures when
 * Material3Expressive icon button styles were combined with materialSizeOverlay inside a
 * MaterialButtonGroup. These tests inflate all layouts containing MaterialButtonGroup,
 * WidthFixMaterialButton, RippleFixMaterialButton, or custom Auxio button styles to ensure no
 * UnsupportedOperationException occurs during attribute resolution.
 *
 * Coverage includes: playback bar/panel (all qualifiers), dialogs with button groups, toolbar,
 * detail layouts, home list, and main activity layouts.
 */
@RunWith(RobolectricTestRunner::class)
class PlaybackLayoutInflationTest {

    private fun themedInflater(): LayoutInflater {
        val context =
            ContextThemeWrapper(RuntimeEnvironment.getApplication(), R.style.Theme_Auxio_App)
        return LayoutInflater.from(context)
    }

    // --- Playback bar ---

    @Test
    @Config(qualifiers = "w1280dp-h720dp-land")
    fun inflatePlaybackBar_doesNotCrash() {
        val inflater = themedInflater()
        val parent = FrameLayout(inflater.context)
        inflater.inflate(R.layout.fragment_playback_bar, parent, false)
    }

    // --- Playback panel (all resource qualifiers) ---

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

    // --- Dialogs with MaterialButtonGroup ---

    @Test
    @Config(qualifiers = "w412dp-h915dp-port")
    fun inflateDialogSort_doesNotCrash() {
        val inflater = themedInflater()
        val parent = FrameLayout(inflater.context)
        inflater.inflate(R.layout.dialog_sort, parent, false)
    }

    @Test
    @Config(qualifiers = "w412dp-h915dp-port")
    fun inflateDialogMusicLocations_doesNotCrash() {
        val inflater = themedInflater()
        val parent = FrameLayout(inflater.context)
        inflater.inflate(R.layout.dialog_music_locations, parent, false)
    }

    @Test
    @Config(qualifiers = "w412dp-h915dp-port")
    fun inflateDialogPlaylistExport_doesNotCrash() {
        val inflater = themedInflater()
        val parent = FrameLayout(inflater.context)
        inflater.inflate(R.layout.dialog_playlist_export, parent, false)
    }

    // --- Toolbar (contains MaterialButtonGroup) ---

    @Test
    @Config(qualifiers = "w412dp-h915dp-port")
    fun inflateViewToolbar_doesNotCrash() {
        val inflater = themedInflater()
        val parent = FrameLayout(inflater.context)
        inflater.inflate(R.layout.view_toolbar, parent, false)
    }

    // --- Detail layouts (all qualifiers, contain MaterialButtonGroup) ---

    @Test
    @Config(qualifiers = "w320dp-h320dp-port")
    fun inflateFragmentDetail_default_doesNotCrash() {
        val inflater = themedInflater()
        val parent = FrameLayout(inflater.context)
        inflater.inflate(R.layout.fragment_detail, parent, false)
    }

    @Test
    @Config(qualifiers = "w640dp-h360dp-land")
    fun inflateFragmentDetail_h360dp_doesNotCrash() {
        val inflater = themedInflater()
        val parent = FrameLayout(inflater.context)
        inflater.inflate(R.layout.fragment_detail, parent, false)
    }

    @Test
    @Config(qualifiers = "w720dp-h480dp-port")
    fun inflateFragmentDetail_h480dp_doesNotCrash() {
        val inflater = themedInflater()
        val parent = FrameLayout(inflater.context)
        inflater.inflate(R.layout.fragment_detail, parent, false)
    }

    @Test
    @Config(qualifiers = "w600dp-h900dp-port")
    fun inflateFragmentDetail_w600dp_doesNotCrash() {
        val inflater = themedInflater()
        val parent = FrameLayout(inflater.context)
        inflater.inflate(R.layout.fragment_detail, parent, false)
    }

    @Test
    @Config(qualifiers = "w720dp-h900dp-port")
    fun inflateFragmentDetail_sw600dp_doesNotCrash() {
        val inflater = themedInflater()
        val parent = FrameLayout(inflater.context)
        inflater.inflate(R.layout.fragment_detail, parent, false)
    }

    // --- Home list (contains RippleFixMaterialButton) ---

    @Test
    @Config(qualifiers = "w412dp-h915dp-port")
    fun inflateFragmentHomeList_doesNotCrash() {
        val inflater = themedInflater()
        val parent = FrameLayout(inflater.context)
        inflater.inflate(R.layout.fragment_home_list, parent, false)
    }

    // --- Main activity layouts ---

    @Test
    @Config(qualifiers = "w412dp-h915dp-port")
    fun inflateFragmentMain_doesNotCrash() {
        val inflater = themedInflater()
        val parent = FrameLayout(inflater.context)
        inflater.inflate(R.layout.fragment_main, parent, false)
    }

    @Test
    @Config(qualifiers = "w720dp-h900dp-land")
    fun inflateFragmentMain_w720dp_doesNotCrash() {
        val inflater = themedInflater()
        val parent = FrameLayout(inflater.context)
        inflater.inflate(R.layout.fragment_main, parent, false)
    }
}
