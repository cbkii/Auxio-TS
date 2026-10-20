/*
 * Copyright (c) 2026 Auxio Project
 * LauncherPreparationTest.kt is part of Auxio.
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

package org.oxycblt.auxio.headunit

import android.content.Intent
import java.time.Duration
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowSystemClock

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
class LauncherPreparationTest {
    @Test
    fun exportedIntentCannotForgePreparationAndAnIssuedRequestIsSingleUse() {
        assertFalse(LauncherPreparation.consume(Intent("org.oxycblt.auxio.action.PREPARE_HOME")))
        val issued = LauncherPreparation.issue()
        val duplicate = Intent(issued)
        assertTrue(LauncherPreparation.consume(issued))
        assertFalse(LauncherPreparation.consume(duplicate))
    }

    @Test
    fun delayedRequestCannotBecomePreparationAfterItsLeaseExpires() {
        val issued = LauncherPreparation.issue()
        ShadowSystemClock.advanceBy(Duration.ofSeconds(11))
        assertFalse(LauncherPreparation.consume(issued))
    }
}
