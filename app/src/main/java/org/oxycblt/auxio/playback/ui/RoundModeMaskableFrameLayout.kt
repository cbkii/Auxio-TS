package org.oxycblt.auxio.playback.ui

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.google.android.material.carousel.MaskableFrameLayout
import com.google.android.material.shape.ShapeAppearanceModel
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import org.oxycblt.auxio.ui.UISettings

class RoundModeMaskableFrameLayout
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleRes: Int = -1) :
    MaskableFrameLayout(context, attrs, defStyleRes) {
    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface RoundModeMaskableFrameLayoutEntryPoint {
        fun uiSettings(): UISettings
    }

    private val uiSettings: UISettings by lazy {
        EntryPointAccessors.fromApplication<RoundModeMaskableFrameLayoutEntryPoint>(context.applicationContext)
            .uiSettings()
    }

    init {
        if (!uiSettings.roundMode) {
            shapeAppearanceModel = ShapeAppearanceModel.builder().build()
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.actionMasked == MotionEvent.ACTION_DOWN && !maskRectF.contains(event.x, event.y)) {
            return false
        }
        return super.dispatchTouchEvent(event)
    }
}
