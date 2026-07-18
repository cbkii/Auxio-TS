/*
 * Copyright (c) 2026 Auxio Project
 * StartupPerformanceReportTest.kt is part of Auxio.
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

package org.oxycblt.auxio.util

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class StartupPerformanceReportTest {
    @Test
    fun `report includes authority context and relative timing`() {
        val events =
            listOf(
                PerfTimer.Event("process_start", 1_000, null, "main", 42),
                PerfTimer.Event("queue_ready", 1_125, 7, "DefaultDispatcher", 42),
            )

        val report =
            StartupPerformanceReport.render(
                captureContext =
                    StartupPerformanceReport.CaptureContext(
                        authority = "unit-test",
                        sourceState = "usb0-mounted,usb1-absent",
                        fixtureSongCount = 5_000,
                        commit = "abc123",
                    ),
                events = events,
                bootId = "boot-test",
            )

        assertContains(report, "authority=unit-test")
        assertContains(report, "bootId=boot-test")
        assertContains(report, "fixtureSongCount=5000")
        assertContains(report, "t+125ms label=queue_ready durationMs=7")
    }

    @Test
    fun `report is bounded to the timer ring size`() {
        val events =
            (0 until 300).map { index ->
                PerfTimer.Event("event_$index", index.toLong(), null, "thread", 1)
            }
        val report =
            StartupPerformanceReport.render(
                StartupPerformanceReport.CaptureContext(authority = "unit-test"),
                events,
                bootId = null,
            )

        assertContains(report, "eventCount=256")
        assertFalse(report.contains("label=event_0\n"))
        assertContains(report, "label=event_299")
        assertEquals(256, report.lineSequence().count { it.contains(" label=event_") })
    }
}
