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
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.oxycblt.auxio.R
import org.robolectric.RobolectricTestRunner

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

    private fun assertEmojiCompatDisabled(styleRes: Int) {
        val attributes =
            context.obtainStyledAttributes(
                styleRes,
                intArrayOf(androidx.appcompat.R.attr.emojiCompatEnabled),
            )
        try {
            assertTrue("emojiCompatEnabled must remain explicit in the resolved style", attributes.hasValue(0))
            assertFalse(attributes.getBoolean(0, true))
        } finally {
            attributes.recycle()
        }
    }
}
