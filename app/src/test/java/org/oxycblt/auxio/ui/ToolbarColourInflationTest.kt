/*
 * Copyright (c) 2026 Auxio Project
 * ToolbarColourInflationTest.kt is part of Auxio.
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

import android.util.TypedValue
import android.view.ContextThemeWrapper
import com.google.android.material.button.MaterialButton
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.oxycblt.auxio.R
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
class ToolbarColourInflationTest {
    @Test
    @Config(qualifiers = "w1280dp-h720dp-land-notnight")
    fun dayToolbarResolvesUncheckedIconColourWithoutInflationWarnings() = assertToolbarColours()

    @Test
    @Config(qualifiers = "w1280dp-h720dp-land-night")
    fun nightToolbarResolvesUncheckedIconColourWithoutInflationWarnings() = assertToolbarColours()

    private fun assertToolbarColours() {
        ShadowLog.clear()
        val context =
            ContextThemeWrapper(RuntimeEnvironment.getApplication(), R.style.Theme_Auxio_App)
        val root = AuxioToolbar(context)
        val button = root.findViewById<MaterialButton>(R.id.toolbar_navigation_button)
        val value = TypedValue()
        assertTrue(
            "Material's unchecked state token must survive the custom overlay",
            button.context.theme.resolveAttribute(
                com.google.android.material.R.attr.colorOnContainerUnchecked,
                value,
                true,
            ),
        )
        assertTrue(value.type in TypedValue.TYPE_FIRST_COLOR_INT..TypedValue.TYPE_LAST_COLOR_INT)
        assertTrue(
            "Framework fallback must not mask a ResourcesCompat colour-inflation failure",
            ShadowLog.getLogsForTag("ResourcesCompat").none {
                it.msg?.contains("Failed to inflate ColorStateList") == true
            },
        )
    }
}
