/*
 * Copyright (c) 2024 Auxio Project
 * NullCovers.kt is part of Auxio.
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

package org.oxycblt.auxio.image.covers

import org.oxycblt.musikr.covers.CoverResult
import org.oxycblt.musikr.covers.MutableCovers
import org.oxycblt.musikr.covers.stored.CoverStorage
import org.oxycblt.musikr.fs.File
import org.oxycblt.musikr.metadata.Metadata

/**
 * Explicitly disabled artwork surface.
 *
 * OFF is a presentation/extraction choice, not authority to destroy the durable artwork cache.
 * Returning misses keeps new rows artwork-free while allowing the incremental cache to preserve an
 * existing cover ID, and the no-op cleanup makes OFF -> enabled reversible without clearing app
 * data or rescanning solely because the user temporarily hid artwork.
 */
class NullCovers(@Suppress("UNUSED_PARAMETER") storage: CoverStorage) :
    MutableCovers<org.oxycblt.musikr.covers.Cover> {
    override suspend fun obtain(id: String) = CoverResult.Miss<org.oxycblt.musikr.covers.Cover>()

    override suspend fun create(file: File, metadata: Metadata) =
        CoverResult.Miss<org.oxycblt.musikr.covers.Cover>()

    override suspend fun cleanup(excluding: Collection<org.oxycblt.musikr.covers.Cover>) = Unit
}
