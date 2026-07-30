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
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog
import timber.log.Timber

@RunWith(RobolectricTestRunner::class)
class CopyleftNoticeTreeTest {
    private lateinit var tree: CopyleftNoticeTree

    @Before
    fun setup() {
        ShadowLog.clear()
        tree = CopyleftNoticeTree()
        Timber.plant(tree)
    }

    @After
    fun tearDown() {
        Timber.uproot(tree)
    }

    @Test
    fun `fork logger preserves diagnostic tag message and throwable`() {
        val throwable = IllegalStateException("source failure")

        Timber.tag("AuxioCapture").e(throwable, "AUXIO_TS_CAPTURE_CANARY generation=%d", 42)

        val logged = ShadowLog.getLogs().last()
        assertEquals(Log.ERROR, logged.type)
        assertEquals("AuxioCapture", logged.tag)
        assertTrue(logged.msg.startsWith("AUXIO_TS_CAPTURE_CANARY generation=42"))
        assertTrue(logged.msg.contains("java.lang.IllegalStateException: source failure"))
    }
}
