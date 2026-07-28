/*
 * Copyright (c) 2026 Auxio Project
 * IndexingNotificationUpdateGateTest.kt is part of Auxio.
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
import org.oxycblt.auxio.music.IndexingState
import org.oxycblt.musikr.IndexingPhase
import org.oxycblt.musikr.IndexingProgress

class IndexingNotificationUpdateGateTest {
    @Test
    fun identicalProgressIsSuppressedWithinOneSession() {
        val gate = IndexingNotificationUpdateGate(minProgressUpdateMs = 3_000L)
        val state =
            IndexingState.Indexing(
                progress = IndexingProgress.Songs(loaded = 10, explored = 20),
                sessionId = 1L,
            )

        assertTrue(gate.shouldRefresh(state, nowElapsedMs = 10_000L))
        assertFalse(gate.shouldRefresh(state, nowElapsedMs = 14_000L))
    }

    @Test
    fun newSessionAlwaysResetsProgressDeduplication() {
        val gate = IndexingNotificationUpdateGate(minProgressUpdateMs = 3_000L)
        val first =
            IndexingState.Indexing(
                progress = IndexingProgress.Songs(loaded = 10, explored = 20),
                sessionId = 1L,
            )
        val replacement = first.copy(sessionId = 2L)

        assertTrue(gate.shouldRefresh(first, nowElapsedMs = 10_000L))
        assertTrue(gate.shouldRefresh(replacement, nowElapsedMs = 10_001L))
    }

    @Test
    fun changedProgressIsThrottledWithinTheUpdateWindow() {
        val gate = IndexingNotificationUpdateGate(minProgressUpdateMs = 3_000L)
        val first =
            IndexingState.Indexing(
                progress = IndexingProgress.Songs(loaded = 10, explored = 20),
                sessionId = 1L,
            )
        val changed = first.copy(progress = IndexingProgress.Songs(loaded = 11, explored = 21))

        assertTrue(gate.shouldRefresh(first, nowElapsedMs = 10_000L))
        assertFalse(gate.shouldRefresh(changed, nowElapsedMs = 11_000L))
        assertTrue(gate.shouldRefresh(changed, nowElapsedMs = 13_000L))
    }

    @Test
    fun phaseTransitionAlwaysRefreshesImmediately() {
        val gate = IndexingNotificationUpdateGate(minProgressUpdateMs = 3_000L)
        val songs =
            IndexingState.Indexing(
                progress = IndexingProgress.Songs(loaded = 10, explored = 20),
                sessionId = 1L,
            )
        val finalising = songs.copy(progress = IndexingProgress.Stage(IndexingPhase.FINALISING))

        assertTrue(gate.shouldRefresh(songs, nowElapsedMs = 10_000L))
        assertTrue(gate.shouldRefresh(finalising, nowElapsedMs = 10_100L))
        assertTrue(gate.shouldRefresh(songs, nowElapsedMs = 10_200L))
    }
}
