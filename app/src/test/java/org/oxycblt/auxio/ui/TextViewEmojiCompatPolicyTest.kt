/*
 * Copyright (c) 2026 Auxio Project
 * TextViewEmojiCompatPolicyTest.kt is part of Auxio.
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

import android.content.Context
import android.util.Xml
import android.view.ContextThemeWrapper
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.oxycblt.auxio.R
import org.robolectric.RobolectricTestRunner
import org.xmlpull.v1.XmlPullParser

@RunWith(RobolectricTestRunner::class)
class TextViewEmojiCompatPolicyTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun baseTextStyleDisablesEmojiCompatProcessing() {
        assertEmojiCompatDisabled(R.style.Widget_Auxio_TextView_Base)
    }

    @Test
    fun playbackMetadataStylesInheritDisabledEmojiCompatProcessing() {
        assertEmojiCompatDisabled(R.style.Widget_Auxio_TextView_Primary_Compact)
        assertEmojiCompatDisabled(R.style.Widget_Auxio_TextView_Secondary_Compact)
    }

    @Test
    fun playbackMetadataWithUnicodeAndEmojiLaysOutWithEmojiCompatDisabled() {
        val view = playbackSongTextView()
        val metadata = "Björk — Jóga 🎵 ❤️"

        assertFalse(view.isEmojiCompatEnabled)
        view.text = metadata
        view.measure(
            View.MeasureSpec.makeMeasureSpec(800, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
        )
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)

        val textLayout = requireNotNull(view.layout)
        assertTrue(textLayout.lineCount > 0)
        assertTrue(textLayout.getLineEnd(textLayout.lineCount - 1) == metadata.length)
    }

    private fun assertEmojiCompatDisabled(styleRes: Int) {
        val attributes =
            context.obtainStyledAttributes(
                styleRes,
                intArrayOf(androidx.appcompat.R.attr.emojiCompatEnabled),
            )
        try {
            assertTrue(
                "emojiCompatEnabled must remain explicit in the resolved style",
                attributes.hasValue(0),
            )
            assertFalse(attributes.getBoolean(0, true))
        } finally {
            attributes.recycle()
        }
    }

    private fun playbackSongTextView(): AppCompatTextView {
        val themed = ContextThemeWrapper(context, R.style.Theme_Auxio)
        val parser = themed.resources.getLayout(R.layout.fragment_playback_panel)
        try {
            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                if (parser.eventType != XmlPullParser.START_TAG) continue
                if (parser.getAttributeResourceValue(ANDROID_NS, "id", 0) != R.id.playback_song) {
                    continue
                }
                return AppCompatTextView(themed, Xml.asAttributeSet(parser))
            }
        } finally {
            parser.close()
        }
        error("playback_song was not present in fragment_playback_panel")
    }

    private companion object {
        const val ANDROID_NS = "http://schemas.android.com/apk/res/android"
    }
}
