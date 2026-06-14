package org.oxycblt.auxio.diagnostics

import org.junit.Assert.*
import org.junit.Test

class Ts18DiagnosticsPolicyTest {
    @Test fun journalIsBoundedAndPruned() {
        Ts18DiagnosticJournal.clear()
        repeat(650) { Ts18DiagnosticJournal.record("test", "event$it") }
        assertTrue(Ts18DiagnosticJournal.snapshot().size <= 600)
        assertFalse(Ts18DiagnosticJournal.snapshot().any { it.event == "event0" })
    }

    @Test fun overlappingCaptureIsPreventedAndStopAllowsNewCapture() {
        Ts18DiagnosticJournal.stopCapture("reset")
        val first = Ts18DiagnosticJournal.startCapture(60_000, "first")
        val second = Ts18DiagnosticJournal.startCapture(60_000, "second")
        assertNotNull(first)
        assertNull(second)
        Ts18DiagnosticJournal.stopCapture("manual")
        assertNotNull(Ts18DiagnosticJournal.startCapture(60_000, "third"))
        Ts18DiagnosticJournal.stopCapture("reset")
    }

    @Test fun guidedQuestionsAreNumberedAndPostReturnReady() {
        assertEquals(120, Ts18GuidedDoFunTest.COUNTDOWN_SECONDS)
        assertTrue(Ts18GuidedDoFunTest.instructions(null).contains("Return directly"))
        Ts18GuidedDoFunTest.questions.forEachIndexed { index, q ->
            assertTrue(q.startsWith("${index + 1}."))
            assertTrue(q.contains("1 ==>"))
            assertTrue(q.contains("I could not") || q.contains("not sure") || q.contains("did not enable"))
        }
    }

    @Test fun diagnosticFindingPreservesQueryFailureAsIndeterminate() {
        val finding = DiagnosticFinding("Package x", DiagnosticStatus.NOT_VISIBLE, DiagnosticEvidence.UNAVAILABLE, "getPackageInfo", "resolve", "not visible or absent; not treated as proven absent")
        assertEquals(DiagnosticStatus.NOT_VISIBLE, finding.status)
        assertTrue(finding.value.contains("not treated as proven absent"))
    }
}
