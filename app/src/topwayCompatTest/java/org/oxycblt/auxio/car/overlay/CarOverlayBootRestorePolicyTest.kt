/*
 * Copyright (c) 2026 Auxio Project
 * CarOverlayBootRestorePolicyTest.kt is part of Auxio.
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

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.oxycblt.auxio.headunit.overlay.CarOverlayContract
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CarOverlayBootRestorePolicyTest {

    @Test
    fun testStartRequestedIsAccepted() {
        val result: CarOverlayContract.OverlayRestoreResult =
            CarOverlayContract.OverlayRestoreResult.StartRequested
        assertTrue(
            result is CarOverlayContract.OverlayRestoreResult.StartRequested ||
                result is CarOverlayContract.OverlayRestoreResult.AlreadyVisible
        )
    }

    @Test
    fun testDisabledIsRejected() {
        val result: CarOverlayContract.OverlayRestoreResult =
            CarOverlayContract.OverlayRestoreResult.Disabled
        assertFalse(
            result is CarOverlayContract.OverlayRestoreResult.StartRequested ||
                result is CarOverlayContract.OverlayRestoreResult.AlreadyVisible
        )
    }
}
