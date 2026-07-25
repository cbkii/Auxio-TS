/*
 * Copyright (c) 2026 Auxio Project
 * RepositoryIndexRequestQueueTest.kt is part of Auxio.
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

package org.oxycblt.auxio.music

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.oxycblt.musikr.library.MetadataProfile

class RepositoryIndexRequestQueueTest {
    @Test
    fun coalescesToTheStrongestPendingRequest() {
        val queue = RepositoryIndexRequestQueue()

        queue.offer(RepositoryIndexRequest(withCache = true, metadataProfile = null))
        queue.offer(
            RepositoryIndexRequest(withCache = true, metadataProfile = MetadataProfile.LEAN)
        )
        queue.offer(
            RepositoryIndexRequest(withCache = false, metadataProfile = MetadataProfile.FULL)
        )

        val request = requireNotNull(queue.drain())
        assertTrue(!request.withCache)
        assertEquals(MetadataProfile.FULL, request.metadataProfile)
        assertNull(queue.drain())
    }

    @Test
    fun leanProfileSurvivesARequestWithoutAnExplicitProfile() {
        val queue = RepositoryIndexRequestQueue()

        queue.offer(
            RepositoryIndexRequest(withCache = true, metadataProfile = MetadataProfile.LEAN)
        )
        queue.offer(RepositoryIndexRequest(withCache = true, metadataProfile = null))

        assertEquals(MetadataProfile.LEAN, queue.drain()?.metadataProfile)
    }
}
