/*
 * Copyright (c) 2026 Auxio Project
 * IndexReplacementHandoffPolicyTest.kt is part of Auxio.
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

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.oxycblt.auxio.music.IndexReason
import org.oxycblt.auxio.music.IndexRequest
import org.oxycblt.auxio.music.ObservationMode
import org.oxycblt.musikr.library.MetadataProfile

class IndexReplacementHandoffPolicyTest {
    @Test
    fun `bounded metadata enrichment does not wait for playback idle`() {
        assertFalse(
            IndexReplacementHandoffPolicy.mustWaitForIdle(
                request =
                    IndexRequest(
                        reason = IndexReason.METADATA_ENRICHMENT,
                        withCache = true,
                        metadataProfile = MetadataProfile.FULL,
                    ),
                playbackActive = true,
                observationMode = ObservationMode.MANUAL,
            )
        )
    }

    @Test
    fun `ordinary full work still waits while playback is active`() {
        assertTrue(
            IndexReplacementHandoffPolicy.mustWaitForIdle(
                request =
                    IndexRequest(
                        reason = IndexReason.USER_REFRESH,
                        withCache = true,
                        metadataProfile = MetadataProfile.FULL,
                    ),
                playbackActive = true,
                observationMode = ObservationMode.MANUAL,
            )
        )
    }

    @Test
    fun `when idle observation still waits for ordinary work`() {
        assertTrue(
            IndexReplacementHandoffPolicy.mustWaitForIdle(
                request = IndexRequest(IndexReason.SOURCE_OBSERVER, withCache = true),
                playbackActive = true,
                observationMode = ObservationMode.WHEN_IDLE,
            )
        )
    }

    @Test
    fun `nothing waits when playback is already idle`() {
        assertFalse(
            IndexReplacementHandoffPolicy.mustWaitForIdle(
                request =
                    IndexRequest(
                        reason = IndexReason.USER_REFRESH,
                        withCache = true,
                        metadataProfile = MetadataProfile.FULL,
                    ),
                playbackActive = false,
                observationMode = ObservationMode.WHEN_IDLE,
            )
        )
    }
}
