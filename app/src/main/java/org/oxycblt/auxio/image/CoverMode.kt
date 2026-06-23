/*
 * Copyright (c) 2026 Auxio Project
 * CoverMode.kt is part of Auxio.
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

package org.oxycblt.auxio.image

import org.oxycblt.auxio.IntegerTable

/** Represents the options available for album cover loading. */
enum class CoverMode {
    OFF,
    OPTIMISED,
    AS_IS;

    val intCode: Int
        get() =
            when (this) {
                OFF -> IntegerTable.COVER_MODE_OFF
                OPTIMISED -> IntegerTable.COVER_MODE_BALANCED
                AS_IS -> IntegerTable.COVER_MODE_AS_IS
            }

    companion object {
        fun fromIntCode(intCode: Int) =
            when (intCode) {
                IntegerTable.COVER_MODE_OFF -> OFF
                IntegerTable.COVER_MODE_BALANCED -> OPTIMISED
                IntegerTable.COVER_MODE_AS_IS -> AS_IS
                else -> null
            }
    }
}
