/*
 * Copyright (c) 2026 Auxio Project
 * CategorySubscriptionGate.kt is part of Auxio.
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

package org.oxycblt.auxio.home

import org.oxycblt.auxio.music.MusicType

/**
 * Conflates invalidations for inactive categories.
 *
 * Source observation stays cheap, while a complete sort/query is permitted only for the visible
 * category. Multiple changes while inactive collapse into one refresh when the user opens it.
 */
internal class CategorySubscriptionGate(initial: MusicType) {
    private var active = initial
    private val dirty = MusicType.entries.toMutableSet()

    @Synchronized
    fun activate(type: MusicType): Boolean {
        active = type
        return dirty.remove(type)
    }

    @Synchronized
    fun invalidate(type: MusicType): Boolean {
        dirty += type
        return if (type == active) {
            dirty.remove(type)
            true
        } else {
            false
        }
    }

    @Synchronized
    fun invalidateAll(): Boolean {
        dirty += MusicType.entries
        dirty.remove(active)
        return true
    }

    @Synchronized fun activeType(): MusicType = active
}
