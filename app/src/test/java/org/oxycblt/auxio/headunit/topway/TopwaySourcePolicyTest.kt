/*
 * Copyright (c) 2026 Auxio Project
 * TopwaySourcePolicyTest.kt is part of Auxio.
 */

package org.oxycblt.auxio.headunit.topway

import org.junit.Assert.assertTrue
import org.junit.Test

class TopwaySourcePolicyTest {
    @Test
    fun ts18CandidatesCoverObservedUsbAndEmulatedPaths() {
        assertTrue(TopwaySourcePolicy.CANDIDATE_ROOTS.contains("/storage/usbdisk0"))
        assertTrue(TopwaySourcePolicy.CANDIDATE_ROOTS.contains("/storage/usbdisk0/Music"))
        assertTrue(TopwaySourcePolicy.CANDIDATE_ROOTS.contains("/storage/usbdisk0/Download"))
        assertTrue(TopwaySourcePolicy.CANDIDATE_ROOTS.contains("/storage/emulated/0/Music"))
        assertTrue(TopwaySourcePolicy.CANDIDATE_ROOTS.contains("/storage/emulated/0/Media"))
        assertTrue(TopwaySourcePolicy.CANDIDATE_ROOTS.contains("/storage/emulated/0/Download"))
    }
}
