/*
 * Copyright (c) 2026 Auxio Project
 * CoverPagerAdapterNullableCompat.kt is part of Auxio.
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

package org.oxycblt.auxio.playback

import org.oxycblt.auxio.list.adapter.UpdateInstructions
import org.oxycblt.auxio.playback.ui.swiper.CoverPagerAdapter
import org.oxycblt.musikr.Song

/**
 * Resolves the binding-scoped cover adapter before applying a queue update.
 *
 * [PlaybackPanelFragment] clears its adapter in `onDestroyBinding`. A queue callback outside the
 * active binding is therefore a lifecycle error and must fail locally instead of dereferencing a
 * nullable receiver implicitly.
 */
internal fun CoverPagerAdapter?.update(newList: List<Song>, instructions: UpdateInstructions?) {
    checkNotNull(this) { "CoverPagerAdapter must exist while the playback-panel binding is active" }
        .update(newList, instructions)
}
