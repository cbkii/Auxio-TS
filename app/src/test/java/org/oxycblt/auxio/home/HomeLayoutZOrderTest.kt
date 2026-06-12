/*
 * Copyright (c) 2026 Auxio Project
 * HomeLayoutZOrderTest.kt is part of Auxio.
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

package org.oxycblt.auxio.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.test.core.app.ApplicationProvider
import androidx.viewpager2.widget.ViewPager2
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.oxycblt.auxio.R
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class HomeLayoutZOrderTest {
    @Test
    fun fragmentHome_shortcutsAreAbovePager() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        context.setTheme(R.style.Theme_Auxio)
        val inflater = LayoutInflater.from(context)
        val root = inflater.inflate(R.layout.fragment_home, null) as ViewGroup

        val homeContent = root.findViewById<FrameLayout>(R.id.home_content)
        val pager = root.findViewById<ViewPager2>(R.id.home_pager)
        val shortcuts = root.findViewById<View>(R.id.home_head_unit_shortcuts)

        val pagerIndex = homeContent.indexOfChild(pager)
        val shortcutsIndex = homeContent.indexOfChild(shortcuts)

        assertTrue(
            "Shortcuts ($shortcutsIndex) must be after Pager ($pagerIndex) in FrameLayout for top Z-order",
            shortcutsIndex > pagerIndex,
        )
    }
}
