/*
 * Copyright (c) 2026 Auxio Project
 * ArtworkContentIdentity.kt is part of Auxio.
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

package org.oxycblt.auxio.list.recycler

import org.oxycblt.musikr.covers.Cover
import org.oxycblt.musikr.covers.CoverCollection

/** Cheap, I/O-free artwork identity used by RecyclerView content comparisons. */
internal object ArtworkContentIdentity {
    fun sameCover(oldCover: Cover?, newCover: Cover?): Boolean = oldCover?.id == newCover?.id

    fun sameCoverCollection(oldCovers: CoverCollection, newCovers: CoverCollection): Boolean {
        val old = oldCovers.covers
        val new = newCovers.covers
        if (old.size != new.size) return false
        for (index in old.indices) {
            if (old[index].id != new[index].id) return false
        }
        return true
    }
}
