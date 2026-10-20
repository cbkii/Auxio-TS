package org.oxycblt.auxio.ui

import android.util.TypedValue
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.widget.FrameLayout
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
        val context = ContextThemeWrapper(RuntimeEnvironment.getApplication(), R.style.Theme_Auxio_App)
        val root =
            LayoutInflater.from(context).inflate(R.layout.view_toolbar, FrameLayout(context), false)
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
