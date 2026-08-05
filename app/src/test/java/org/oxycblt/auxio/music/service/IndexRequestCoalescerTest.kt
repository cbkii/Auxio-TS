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
import org.junit.Assert.assertTrue
import org.junit.Test
import org.oxycblt.auxio.music.IndexReason
import org.oxycblt.auxio.music.IndexRequest
import org.oxycblt.auxio.music.ObservationMode
import org.oxycblt.musikr.library.MetadataProfile

class IndexRequestCoalescerTest {
    @Test
    fun `every reason pair resolves to the stronger authority`() {
        val strongestFirst =
            listOf(
                IndexReason.INITIAL_CONFIGURATION,
                IndexReason.USER_RETRY,
                IndexReason.USER_REFRESH,
                IndexReason.STORAGE_MOUNTED,
                IndexReason.SOURCE_OBSERVER,
                IndexReason.COMPATIBILITY_RECOVERY,
                IndexReason.METADATA_ENRICHMENT,
            )
        for (first in IndexReason.entries) {
            for (second in IndexReason.entries) {
                val merged =
                    IndexRequestCoalescer.merge(
                        IndexRequest(first, withCache = true, configurationGeneration = 9L),
                        IndexRequest(second, withCache = true, configurationGeneration = 9L),
                    )
                val expected =
                    if (strongestFirst.indexOf(first) <= strongestFirst.indexOf(second)) {
                        first
                    } else {
                        second
                    }
                assertEquals("$first + $second", expected, merged.reason)
            }
        }
    }

    @Test
    fun `cache bypass and full enrichment win a request burst`() {
        val first =
            IndexRequest(
                IndexReason.SOURCE_OBSERVER,
                withCache = true,
                metadataProfile = MetadataProfile.LEAN,
            )
        val merged =
            IndexRequestCoalescer.merge(
                first,
                IndexRequest(
                    IndexReason.USER_REFRESH,
                    withCache = false,
                    metadataProfile = MetadataProfile.FULL,
                ),
            )

        assertFalse(merged.withCache)
        assertEquals(MetadataProfile.FULL, merged.metadataProfile)
    }

    @Test
    fun `automatic requests remain automatic when coalesced`() {
        val merged =
            IndexRequestCoalescer.merge(
                IndexRequest(IndexReason.SOURCE_OBSERVER, withCache = true),
                IndexRequest(IndexReason.SOURCE_OBSERVER, withCache = true),
            )

        assertEquals(IndexRequest(IndexReason.SOURCE_OBSERVER, withCache = true), merged)
    }

    @Test
    fun `full enrichment cannot alter cache bypassing initial configuration`() {
        val initial =
            IndexRequest(
                IndexReason.INITIAL_CONFIGURATION,
                withCache = false,
                metadataProfile = MetadataProfile.LEAN,
                configurationGeneration = 11L,
            )

        val merged =
            IndexRequestCoalescer.merge(
                initial,
                IndexRequest(
                    IndexReason.METADATA_ENRICHMENT,
                    withCache = true,
                    metadataProfile = MetadataProfile.FULL,
                    configurationGeneration = 11L,
                ),
            )

        assertEquals(initial, merged)
    }

    @Test
    fun `mount bursts combine source keys within one generation`() {
        val merged =
            IndexRequestCoalescer.merge(
                IndexRequest(
                    IndexReason.STORAGE_MOUNTED,
                    withCache = true,
                    configurationGeneration = 3L,
                    sourceKeys = setOf("usb:a"),
                ),
                IndexRequest(
                    IndexReason.STORAGE_MOUNTED,
                    withCache = true,
                    configurationGeneration = 3L,
                    sourceKeys = setOf("usb:b"),
                ),
            )

        assertEquals(setOf("usb:a", "usb:b"), merged.sourceKeys)
    }

    @Test
    fun `requests from different generations never merge source scopes`() {
        val older =
            IndexRequest(
                IndexReason.USER_RETRY,
                withCache = false,
                configurationGeneration = 40L,
                sourceKeys = setOf("old"),
            )
        val newer =
            IndexRequest(
                IndexReason.SOURCE_OBSERVER,
                withCache = true,
                configurationGeneration = 41L,
                sourceKeys = setOf("new"),
            )

        val merged = IndexRequestCoalescer.merge(older, newer)

        assertEquals(newer, merged)
        assertTrue("old" !in requireNotNull(merged.sourceKeys))
    }

    @Test
    fun `replacement handoff is immediate while playback is idle`() {
        assertFalse(
            IndexReplacementHandoffPolicy.mustWaitForIdle(
                IndexRequest(
                    IndexReason.METADATA_ENRICHMENT,
                    withCache = true,
                    metadataProfile = MetadataProfile.FULL,
                ),
                playbackActive = false,
                observationMode = ObservationMode.WHEN_IDLE,
            )
        )
    }

    @Test
    fun `full replacement waits while playback is active`() {
        assertTrue(
            IndexReplacementHandoffPolicy.mustWaitForIdle(
                IndexRequest(
                    IndexReason.METADATA_ENRICHMENT,
                    withCache = true,
                    metadataProfile = MetadataProfile.FULL,
                ),
                playbackActive = true,
                observationMode = ObservationMode.CONTINUOUS,
            )
        )
    }

    @Test
    fun `when-idle observation defers even lean replacement work`() {
        assertTrue(
            IndexReplacementHandoffPolicy.mustWaitForIdle(
                IndexRequest(
                    IndexReason.SOURCE_OBSERVER,
                    withCache = true,
                    metadataProfile = MetadataProfile.LEAN,
                ),
                playbackActive = true,
                observationMode = ObservationMode.WHEN_IDLE,
            )
        )
    }

    @Test
    fun `old finally callback cannot clear a newer indexing job lease`() {
        val lease = IndexJobLease()
        val old = lease.begin()
        val newer = lease.begin()

        assertFalse(lease.complete(old))
        assertTrue(lease.complete(newer))
        assertFalse(lease.complete(newer))
    }
}
