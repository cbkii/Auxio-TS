/*
 * Copyright (c) 2026 Auxio Project
 * TopwayMusicEntryPolicy.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.oxycblt.auxio.car.overlay

import android.content.Intent

/** Pure routing policy for the fixed DoFun `com.tw.music.MusicActivity` component. */
object TopwayMusicEntryPolicy {
    enum class Route {
        FULL_PLAYER,
        FLOATING_CONTROLS_ONLY,
    }

    fun route(action: String?, hasData: Boolean, floatingOnly: Boolean): Route {
        if (action == Intent.ACTION_VIEW || hasData) return Route.FULL_PLAYER
        return if (floatingOnly) Route.FLOATING_CONTROLS_ONLY else Route.FULL_PLAYER
    }
}
