/*
 * Copyright (c) 2026 Auxio Project
 * PrimitiveQueueIntegrityPolicy.kt is part of Auxio.
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

package org.oxycblt.auxio.playback.persist

/** Fail-open descriptor validation used before Fast Resume trusts persisted queue topology. */
internal object PrimitiveQueueIntegrityPolicy {
    fun validTotalCount(declaredCount: Int, actualCount: Int, currentPosition: Int): Int? {
        if (declaredCount <= 0 || actualCount != declaredCount) return null
        if (currentPosition !in 0 until declaredCount) return null
        return declaredCount
    }
}
