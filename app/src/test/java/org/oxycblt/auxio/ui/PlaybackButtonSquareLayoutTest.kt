/*
 * Copyright (c) 2026 Auxio Project
 * PlaybackButtonSquareLayoutTest.kt is part of Auxio.
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

import androidx.annotation.LayoutRes
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.oxycblt.auxio.R
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.xmlpull.v1.XmlPullParser

@RunWith(RobolectricTestRunner::class)
class PlaybackButtonSquareLayoutTest {
    @Test
    @Config(qualifiers = "w1280dp-h720dp-land")
    fun playbackBarControlsAreSquareInLandscape() {
        assertPlaybackButtonsDeclareSquareBounds(R.layout.fragment_playback_bar)
    }

    @Test
    @Config(qualifiers = "w320dp-h320dp-port")
    fun defaultPlaybackPanelControlsAreSquare() {
        assertPlaybackButtonsDeclareSquareBounds(R.layout.fragment_playback_panel)
    }

    @Test
    @Config(qualifiers = "w640dp-h360dp-land")
    fun h360PlaybackPanelControlsAreSquare() {
        assertPlaybackButtonsDeclareSquareBounds(R.layout.fragment_playback_panel)
    }

    @Test
    @Config(qualifiers = "w1280dp-h720dp-land")
    fun h520PlaybackPanelControlsAreSquareInLandscape() {
        assertPlaybackButtonsDeclareSquareBounds(R.layout.fragment_playback_panel)
    }

    @Test
    @Config(qualifiers = "w720dp-h1280dp-port")
    fun h520PlaybackPanelControlsAreSquareInPortrait() {
        assertPlaybackButtonsDeclareSquareBounds(R.layout.fragment_playback_panel)
    }

    private fun assertPlaybackButtonsDeclareSquareBounds(@LayoutRes layoutId: Int) {
        val resources =
            ApplicationProvider.getApplicationContext<android.content.Context>().resources
        val seenButtonIds = mutableSetOf<Int>()
        val parser = resources.getXml(layoutId)
        try {
            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                if (parser.eventType != XmlPullParser.START_TAG) continue

                val buttonId = parser.getAttributeResourceValue(ANDROID_NS, "id", NO_RESOURCE)
                if (buttonId !in PLAYBACK_BUTTON_IDS) continue

                seenButtonIds += buttonId
                val width =
                    parser.getAttributeResourceValue(ANDROID_NS, "layout_width", NO_RESOURCE)
                val height =
                    parser.getAttributeResourceValue(ANDROID_NS, "layout_height", NO_RESOURCE)
                val name = resources.getResourceEntryName(buttonId)

                assertTrue(
                    "Playback button $name must declare a resource width",
                    width != NO_RESOURCE,
                )
                assertTrue(
                    "Playback button $name must declare a resource height",
                    height != NO_RESOURCE,
                )
                assertEquals("Playback button $name must declare square bounds", height, width)
            }
        } finally {
            parser.close()
        }

        val missing = PLAYBACK_BUTTON_IDS.toSet() - seenButtonIds
        val missingNames = missing.map { resources.getResourceEntryName(it) }
        assertTrue("Missing playback buttons: $missingNames", missing.isEmpty())
    }

    private companion object {
        const val ANDROID_NS = "http://schemas.android.com/apk/res/android"
        const val NO_RESOURCE = 0

        val PLAYBACK_BUTTON_IDS =
            intArrayOf(
                R.id.playback_repeat,
                R.id.playback_skip_prev,
                R.id.playback_play_pause,
                R.id.playback_skip_next,
                R.id.playback_shuffle,
            )
    }
}
