/*
 * Copyright (c) 2026 Auxio Project
 * StartupScanAuthorityPolicy.kt is part of Auxio.
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

package org.oxycblt.auxio.music.service

/** Origin of a service startup request. */
enum class StartupScanOrigin {
    USER_VISIBLE,
    BACKGROUND,
    EARLY_PRESTART;

    internal val priority: Int
        get() = if (this == USER_VISIBLE) 1 else 0

    companion object {
        fun merge(current: StartupScanOrigin?, next: StartupScanOrigin): StartupScanOrigin =
            if (current == null || next.priority > current.priority) next else current
    }
}

/**
 * Compatibility-boundary policy that maps service origin and build flavour to scan authority.
 *
 * The shared music startup core receives only the resulting boolean authority. This keeps Topway
 * boot/ACC restrictions outside generic source/cache policy while preserving standard Android's
 * historical automatic first-start behaviour.
 */
object StartupScanAuthorityPolicy {
    fun allowAutomaticScan(
        topwayCompatFlavor: Boolean,
        origin: StartupScanOrigin,
    ): Boolean = !topwayCompatFlavor || origin == StartupScanOrigin.USER_VISIBLE
}
