/*
 * Copyright (c) 2026 Auxio Project
 * DiagnosticReportGeneratorTest.kt is part of Auxio.
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

import org.junit.Assert.assertTrue
import org.junit.Test

class DiagnosticReportGeneratorTest {

    @Test
    fun `test report generation`() {
        val generator = DiagnosticReportGenerator()
        val automated =
            listOf(
                DiagnosticEntry(
                    "Test Entry",
                    "Test Value",
                    EvidenceClassification.OBSERVED_BY_AUXIO,
                    detail = "Some detail",
                )
            )
        val events =
            listOf(
                DiagnosticEvent(category = "CAT", event = "EVT", detail = "DET", monotonicTime = 0L)
            )

        val report = generator.generate(automated, events, "GUIDED RESULTS")

        assertTrue(report.contains("AUXIO-TS HEALTH DIAGNOSTICS REPORT"))
        assertTrue(report.contains("GUIDED INTEGRATION TEST"))
        assertTrue(report.contains("GUIDED RESULTS"))
        assertTrue(report.contains("Test Entry"))
        assertTrue(report.contains("Test Value"))
        assertTrue(report.contains("Some detail"))
        assertTrue(report.contains("CAT: EVT"))
        assertTrue(report.contains("DET"))
    }
}
