/*
 * Copyright (c) 2026 Auxio Project
 * DiagnosticJournalTest.kt is part of Auxio.
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

package org.oxycblt.auxio.diagnostics

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DiagnosticJournalTest {

    private lateinit var journal: DiagnosticJournal

    @Before
    fun setup() {
        journal = DiagnosticJournal()
    }

    @Test
    fun `test event recording`() {
        journal.startSession("sess")
        journal.log("CAT", "Event", "Detail")
        val events = journal.events.value
        assertEquals(2, events.size)
        assertEquals("CAT", events[1].category)
        assertEquals("Event", events[1].event)
        assertEquals("Detail", events[1].detail)
    }

    @Test
    fun `test event pruning`() {
        journal.startSession("sess")
        // Log more than MAX_EVENT_COUNT (1000)
        for (i in 1..1100) {
            journal.log("CAT", "Event $i")
        }
        val events = journal.events.value
        assertEquals(1000, events.size)
        assertEquals("Event 101", events[0].event)
        assertEquals("Event 1100", events[999].event)
    }

    @Test
    fun `test session management`() {
        journal.startSession("sess1")
        journal.log("CAT", "Event")
        val events = journal.events.value
        // Session start event + log event
        assertTrue(events.any { it.category == "SESSION" && it.event == "Started" })
        assertEquals("sess1", events.last().sessionId)

        journal.endSession()
        journal.log("CAT", "Event2")
        val eventsAfter = journal.events.value
        assertEquals("Ended", eventsAfter.last().event)
        assertFalse(journal.hasActiveSession)
    }

    @Test
    fun `test clear`() {
        journal.startSession("sess")
        journal.log("CAT", "Event")
        journal.clear()
        assertEquals(0, journal.events.value.size)
    }

    @Test
    fun `test overlapping capture is prevented`() {
        assertTrue(journal.startSession("first"))
        assertFalse(journal.startSession("second"))
        journal.endSession()
        assertTrue(journal.startSession("third"))
    }
}
