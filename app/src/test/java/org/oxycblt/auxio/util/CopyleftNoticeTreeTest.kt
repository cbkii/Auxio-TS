/*
 * Copyright (c) 2026 Auxio Project
 * CopyleftNoticeTreeTest.kt is part of Auxio.
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
