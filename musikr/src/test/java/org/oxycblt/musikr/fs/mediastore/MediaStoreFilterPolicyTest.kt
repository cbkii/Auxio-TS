/*
 * Copyright (c) 2026 Auxio Project
 * MediaStoreFilterPolicyTest.kt is part of Auxio.
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

package org.oxycblt.musikr.fs.mediastore

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MediaStoreFilterPolicyTest {
    @Test
    fun standardVariantRetainsExcludeNonMusicSetting() {
        assertTrue(
            MediaStoreFilterPolicy.shouldRequireIsMusic(
                query(excludeNonMusic = true, relaxIsMusicHeuristic = false)
            )
        )
    }

    @Test
    fun relaxedHeuristicBypassesIsMusicConstraint() {
        assertFalse(
            MediaStoreFilterPolicy.shouldRequireIsMusic(
                query(excludeNonMusic = true, relaxIsMusicHeuristic = true)
            )
        )
    }

    @Test
    fun disabledExcludeNonMusicNeverAddsTheConstraint() {
        assertFalse(
            MediaStoreFilterPolicy.shouldRequireIsMusic(
                query(excludeNonMusic = false, relaxIsMusicHeuristic = false)
            )
        )
    }

    private fun query(excludeNonMusic: Boolean, relaxIsMusicHeuristic: Boolean) =
        MediaStore.Query(
            mode = MediaStore.FilterMode.EXCLUDE,
            filtered = emptyList(),
            excludeNonMusic = excludeNonMusic,
            relaxIsMusicHeuristic = relaxIsMusicHeuristic,
        )
}
