/*
 * Copyright (c) 2026 Auxio Project
 * TopwayOverlayRestoreBridge.kt is part of Auxio.
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

package org.oxycblt.auxio.headunit.overlay

import android.content.Context
import android.content.Intent
import org.oxycblt.auxio.BuildConfig

object TopwayOverlayRestoreBridge {
    fun requestOverlayRestore(context: Context): Boolean {
        if (!BuildConfig.TOPWAY_COMPAT_FLAVOR) return false
        val overlayIntent = Intent("org.oxycblt.auxio.car.overlay.ACTION_RESTORE_OVERLAY")
        overlayIntent.setPackage(context.packageName)
        context.sendBroadcast(overlayIntent)
        return true
    }
}
