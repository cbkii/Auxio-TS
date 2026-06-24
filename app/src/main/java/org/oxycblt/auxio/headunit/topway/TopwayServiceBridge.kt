/*
 * Copyright (c) 2026 Auxio Project
 * TopwayServiceBridge.kt is part of Auxio.
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

package org.oxycblt.auxio.headunit.topway

import android.app.Service
import org.oxycblt.auxio.BuildConfig

/** Bridge for resolving Topway-compatible service components at runtime. */
object TopwayServiceBridge {
    private const val STOCK_MUSIC_SERVICE_CLASS = "com.tw.music.MusicService"

    /**
     * Resolves the appropriate service class for the current flavor.
     *
     * @param defaultClass The standard service class to return if not in Topway flavor.
     * @return The stock-compatible service class name if in Topway flavor, else defaultClass.
     */
    fun <T : Service> resolveCompatServiceClass(defaultClass: Class<T>): Class<*> {
        if (BuildConfig.TOPWAY_COMPAT_FLAVOR) {
            return try {
                Class.forName(STOCK_MUSIC_SERVICE_CLASS)
            } catch (e: ClassNotFoundException) {
                defaultClass
            }
        }
        return defaultClass
    }
}
