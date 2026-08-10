/*
 * Copyright (c) 2026 Auxio Project
 * ArtworkContentIdentity.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.oxycblt.auxio.list.recycler

import org.oxycblt.musikr.covers.Cover
import org.oxycblt.musikr.covers.CoverCollection

/** Cheap, I/O-free artwork identity used by RecyclerView content comparisons. */
internal object ArtworkContentIdentity {
    fun sameCover(oldCover: Cover?, newCover: Cover?): Boolean = oldCover?.id == newCover?.id

    fun sameCoverCollection(
        oldCovers: CoverCollection,
        newCovers: CoverCollection,
    ): Boolean = oldCovers.covers.map(Cover::id) == newCovers.covers.map(Cover::id)
}
