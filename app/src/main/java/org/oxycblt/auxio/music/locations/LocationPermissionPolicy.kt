/*
 * Copyright (c) 2026 Auxio Project
 * LocationPermissionPolicy.kt is part of Auxio.
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

package org.oxycblt.auxio.music.locations

/** Storage-permission policy for source selection and validation. */
internal object LocationPermissionPolicy {
    fun isSourceUiEnabled(mode: LocationMode, hasStoragePermission: Boolean): Boolean =
        mode != LocationMode.MEDIA_STORE || hasStoragePermission

    fun requiresStoragePermission(mode: LocationMode, path: String?): Boolean =
        when (mode) {
            LocationMode.MEDIA_STORE -> true
            LocationMode.SAF -> false
            LocationMode.DIRECT_FS -> path?.let(::isInternalSharedStoragePath) == true
        }

    fun isInternalSharedStoragePath(path: String): Boolean {
        val clean = path.replace('\\', '/').trimEnd('/')
        return clean == "/sdcard" ||
            clean.startsWith("/sdcard/") ||
            clean == "/storage/emulated/0" ||
            clean.startsWith("/storage/emulated/0/")
    }
}
