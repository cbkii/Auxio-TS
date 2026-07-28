/*
 * Copyright (c) 2026 Auxio Project
 * IndexRequestCoalescer.kt is part of Auxio.
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

import org.oxycblt.auxio.music.IndexRequest
import org.oxycblt.auxio.music.IndexRequestPolicy

/**
 * Collapses bursts into the single strongest pending indexing request.
 *
 * A cache-bypassing request cannot be weakened by a later cached request, and an explicit Full
 * enrichment request cannot be weakened by Lean or automatic policy. This keeps observer storms
 * bounded without losing required work. The caller starts this one queued request only after the
 * active indexing pass has completed.
 */
internal object IndexRequestCoalescer {
    fun merge(current: IndexRequest?, incoming: IndexRequest): IndexRequest {
        return IndexRequestPolicy.merge(current, incoming)
    }
}
