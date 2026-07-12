/*
 * Copyright (c) 2026 Auxio Project
 * OverlayLifecycleJournalTest.kt is part of Auxio.
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

package org.oxycblt.auxio.car.overlay

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class OverlayLifecycleJournalTest {

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        OverlayLifecycleJournal.init(context)
        OverlayLifecycleJournal.clear()
    }

    @Test
    fun testJournalAppends() {
        OverlayLifecycleJournal.log("test", true, true, false, true, "Success")
        val history = OverlayLifecycleJournal.getHistory()
        assert(history.contains("test"))
        assert(history.contains("Success"))
    }

    @Test
    fun testJournalBounds() {
        for (i in 0 until 70) {
            OverlayLifecycleJournal.log("trigger_$i", true, true, false, true, "res_$i")
        }

        val history = OverlayLifecycleJournal.getHistory()
        // Should only contain the latest 64
        assert(!history.contains("trigger_0"))
        assert(history.contains("trigger_69"))
    }
}
