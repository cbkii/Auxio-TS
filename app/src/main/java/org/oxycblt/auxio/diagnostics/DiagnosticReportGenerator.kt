/*
 * Copyright (c) 2026 Auxio Project
 * DiagnosticReportGenerator.kt is part of Auxio.
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

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class DiagnosticReportGenerator @Inject constructor() {

    fun generate(
        automatedReport: List<DiagnosticEntry>,
        events: List<DiagnosticEvent>,
        guidedTestReport: String? = null,
    ): String {
        val sb = StringBuilder()
        val stamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date())

        sb.append("========================================\n")
        sb.append("   AUXIO-TS HEALTH DIAGNOSTICS REPORT\n")
        sb.append("========================================\n")
        sb.append("Generated: $stamp\n\n")

        if (guidedTestReport != null) {
            sb.append("== GUIDED INTEGRATION TEST ==\n")
            sb.append(guidedTestReport)
            sb.append("\n\n")
        }

        sb.append("== AUTOMATED DIAGNOSTICS ==\n")
        automatedReport.forEach { entry ->
            sb.append("${entry.name}:\n")
            sb.append("  Value:    ${entry.value}\n")
            sb.append("  Evidence: ${entry.evidence}\n")
            if (entry.detail != null) sb.append("  Detail:   ${entry.detail}\n")
            if (entry.primaryMethod != null) sb.append("  Method:   ${entry.primaryMethod}\n")
            if (entry.fallbackMethod != null) sb.append("  Fallback: ${entry.fallbackMethod}\n")
            if (entry.error != null) sb.append("  Error:    ${entry.error}\n")
            sb.append("\n")
        }

        sb.append("== EVENT JOURNAL (Timeline) ==\n")
        if (events.isEmpty()) {
            sb.append("No events observed during the active capture.\n\n")
        }
        events.forEach { event ->
            val time = SimpleDateFormat("HH:mm:ss.SSS", Locale.US).format(Date(event.wallTime))
            val session = if (event.sessionId != null) "[${event.sessionId}] " else ""
            sb.append("[$time] $session${event.category}: ${event.event}\n")
            if (event.detail != null) sb.append("  Detail: ${event.detail}\n")
            if (event.result != null) sb.append("  Result: ${event.result}\n")
            sb.append("  Source: ${event.evidence}\n\n")
        }

        sb.append("== EXTERNAL EVIDENCE GUIDANCE ==\n")
        sb.append("Some information is inaccessible from the normal app context.\n")
        sb.append("Use ADB shell for deeper inspection if available:\n")
        sb.append("  adb shell dumpsys media_session\n")
        sb.append("  adb shell logcat -v time | grep -iE 'tw.music|dofun|Auxio'\n")
        sb.append("\n[Report End]\n")

        return sb.toString()
    }
}
