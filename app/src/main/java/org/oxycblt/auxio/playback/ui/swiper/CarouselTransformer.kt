package org.oxycblt.auxio.playback.ui.swiper

import android.graphics.RectF
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.carousel.MaskableFrameLayout

class CarouselTransformer : ViewPager2.PageTransformer {

    override fun transformPage(page: View, position: Float) {
        val maskable = page as? MaskableFrameLayout ?: return
        val width = page.width.toFloat()
        val height = page.height.toFloat()

        if (width <= 0f || height <= 0f) {
            return
        }

        page.translationX = -position * width
        page.alpha = 1f

        val p =
            when {
                position < 0f -> -position
                position > 0f -> 1f - position
                else -> 0f
            }.coerceIn(0f, 1f)

        val gapEnterBreakpoint = GAP_BREAKPOINT
        val gapExitBreakpoint = 1f - GAP_BREAKPOINT
        val maxGap = width * 0.08f

        val gap =
            when {
                p < gapEnterBreakpoint -> maxGap * (p / gapEnterBreakpoint)
                p > gapExitBreakpoint -> maxGap * ((1f - p) / (1f - gapExitBreakpoint))
                else -> maxGap
            }

        val reveal =
            ((p - gapEnterBreakpoint) / (gapExitBreakpoint - gapEnterBreakpoint)).coerceIn(0f, 1f)

        val available = width - gap
        val incomingWidth = available * reveal
        val outgoingWidth = available - incomingWidth

        val rect =
            when {
                position < 0f -> RectF(0f, 0f, outgoingWidth, height)
                position > 0f -> RectF(width - incomingWidth, 0f, width, height)
                else -> RectF(0f, 0f, width, height)
            }

        maskable.setMaskRectF(rect)

        val content = page.getChildAt(0)
        val maskCenter = (rect.left + rect.right) / 2f
        val pageCenter = width / 2f
        content.translationX = (maskCenter - pageCenter) * PARALLAX
    }

    private companion object {
        const val GAP_BREAKPOINT = 0.18f
        const val PARALLAX = 0.2f
    }
}
