/*
 * Copyright (c) 2026 Auxio Project
 * CanonicalQueueStructure.kt is part of Auxio.
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

import org.oxycblt.auxio.playback.state.ShuffleScope
import org.oxycblt.musikr.Music

/** Exact, order-sensitive identity of a previously committed canonical queue structure. */
internal data class CanonicalQueueStructure(
    val heapUids: List<Music.UID>,
    val shuffleIndices: List<Int>,
    val parentUid: Music.UID?,
    val currentSongUid: Music.UID,
    val shuffleScope: ShuffleScope,
)
