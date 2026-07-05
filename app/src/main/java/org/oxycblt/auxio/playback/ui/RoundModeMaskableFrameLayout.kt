/*
 * Copyright (c) 2026 Auxio Project
 * RoundModeMaskableFrameLayout.kt is part of Auxio.
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

package org.oxycblt.auxio.playback.ui

import android.content.Context
import android.util.AttributeSet
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
        EntryPointAccessors.fromApplication<RoundModeMaskableFrameLayoutEntryPoint>(
            context.applicationContext
        )
            .uiSettings()
    }

    init {
        if (!uiSettings.roundMode) {
            shapeAppearanceModel = ShapeAppearanceModel.builder().build()
        }
    }
}
