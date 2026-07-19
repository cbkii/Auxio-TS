/*
 * Copyright (c) 2026 Auxio Project
 * IndexRequestCoalescerTest.kt is part of Auxio.
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

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import org.oxycblt.musikr.library.MetadataProfile

class IndexRequestCoalescerTest {
    @Test
    fun `cache bypass and full enrichment win a request burst`() {
        val first = IndexRequest(withCache = true, metadataProfile = MetadataProfile.LEAN)
        val merged =
            IndexRequestCoalescer.merge(
                first,
                IndexRequest(withCache = false, metadataProfile = MetadataProfile.FULL),
            )

        assertFalse(merged.withCache)
        assertEquals(MetadataProfile.FULL, merged.metadataProfile)
    }

    @Test
    fun `automatic requests remain automatic when coalesced`() {
        val merged =
            IndexRequestCoalescer.merge(
                IndexRequest(withCache = true, metadataProfile = null),
                IndexRequest(withCache = true, metadataProfile = null),
            )

        assertEquals(IndexRequest(withCache = true, metadataProfile = null), merged)
    }
}
