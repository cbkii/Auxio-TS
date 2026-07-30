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

import java.io.File
import java.nio.file.Files
import org.json.JSONObject
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
    fun `stale owner cannot end active session`() {
        assertTrue(journal.startSession("newer"))
        assertFalse(journal.endSession("older"))
        assertTrue(journal.hasActiveSession)
        assertEquals("newer", journal.activeSessionId)
        assertTrue(journal.endSession("newer"))
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

    @Test
    fun `active session is persisted with terminal summary`() {
        val directory = Files.createTempDirectory("auxio-journal-test").toFile()
        try {
            val now = System.currentTimeMillis()
            repeat(12) { index ->
                File(directory, "session-seed-$index.jsonl").apply {
                    writeText("""{"seed":$index}""")
                    setLastModified(now - 10_000L - index)
                }
            }
            val interruptedMarker =
                File(directory, ".active-interrupted_campaign").apply {
                    writeText("interrupted campaign")
                }

            journal.configurePersistence(directory)
            journal.awaitPendingWrites()

            assertFalse(interruptedMarker.exists())
            val interruptedSummary = File(directory, "session-interrupted_campaign.summary.json")
            assertTrue(interruptedSummary.exists())
            assertTrue(interruptedSummary.readText().contains("\"outcome\":\"INTERRUPTED\""))
            assertTrue(journal.persistedFiles().size <= 10)

            assertTrue(journal.startSession("physical campaign"))
            journal.log("Indexing", "Progress", "phase=EXTRACTING explored=42")
            assertTrue(journal.endSession("physical campaign"))
            journal.awaitPendingWrites()

            val files = journal.persistedFiles()
            assertTrue(files.any { it.name.endsWith(".jsonl") })
            assertTrue(files.any { it.name.endsWith(".summary.json") })
            val events = files.first { it.name.endsWith(".jsonl") }.readText()
            assertTrue(events.contains("\"event\":\"Progress\""))
            assertTrue(events.contains("\"sessionId\":\"physical campaign\""))
            val summary = files.first { it.name.endsWith(".summary.json") }.readText()
            assertTrue(summary.contains("\"outcome\":\"ENDED\""))
        } finally {
            directory.deleteRecursively()
        }
    }

    @Test
    fun `strict JSON encoding preserves arbitrary control characters`() {
        val controls = buildString { (0x00..0x1F).forEach { append(it.toChar()) } }
        val original = "$controls quote=\" slash=\\"
        val encoded =
            DiagnosticJournal.toJsonLine(
                DiagnosticEvent(category = original, event = original, detail = original)
            )

        val decoded = JSONObject(encoded)
        assertEquals(original, decoded.getString("category"))
        assertEquals(original, decoded.getString("event"))
        assertEquals(original, decoded.getString("detail"))
        assertTrue(encoded.contains("\\u0000"))
        assertFalse(encoded.any { it.code < 0x20 })
    }

    @Test
    fun `path privacy filter removes configured and discovered path values`() {
        val source = "/storage/emulated/0/Music"
        val item = "$source/Artist Name/Track Name.mp3"
        val uri = "content://media/external/audio/media/42"
        val input =
            """
            {"detail":"phase=EXTRACTING item=$item","result":null}
            Detected Path: $source
            uri=$uri
            """
                .trimIndent()

        val filtered = DiagnosticBundleExporter.filterPathBearingText(input, listOf(source))

        assertFalse(filtered.contains(source))
        assertFalse(filtered.contains("Artist Name"))
        assertFalse(filtered.contains("Track Name.mp3"))
        assertFalse(filtered.contains(uri))
        assertTrue(filtered.contains("sha256:"))
    }
}
