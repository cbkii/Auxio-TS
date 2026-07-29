/*
 * Copyright (c) 2026 Auxio Project
 * CopyleftNoticeTreeTest.kt is part of Auxio.
 */

package org.oxycblt.auxio.util

import android.util.Log
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class CopyleftNoticeTreeTest {
    @Test
    fun `fork logger preserves diagnostic tag message and throwable`() {
        val tree = CopyleftNoticeTree()
        val throwable = IllegalStateException("source failure")

        val payload =
            tree.preservePayload(
                Log.ERROR,
                "AuxioCapture",
                "AUXIO_TS_CAPTURE_CANARY generation=42",
                throwable,
            )

        assertEquals(Log.ERROR, payload.priority)
        assertEquals("AuxioCapture", payload.tag)
        assertEquals("AUXIO_TS_CAPTURE_CANARY generation=42", payload.message)
        assertSame(throwable, payload.throwable)
    }
}
