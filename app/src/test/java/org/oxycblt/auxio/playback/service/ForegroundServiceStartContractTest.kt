/*
 * Copyright (c) 2026 Auxio Project
 * ForegroundServiceStartContractTest.kt is part of Auxio.
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

package org.oxycblt.auxio.playback.service

import android.content.Intent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.oxycblt.auxio.AuxioService
import org.oxycblt.auxio.IntegerTable
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
class ForegroundServiceStartContractTest {
    @Test
    fun unrelatedUnmarkedStart_doesNotRequireImmediatePromotion() {
        assertFalse(ForegroundServiceStartContract.requiresImmediatePromotion(Intent()))
        assertFalse(ForegroundServiceStartContract.requiresImmediatePromotion(null))
        assertFalse(
            ForegroundServiceStartContract.requiresImmediatePromotion(
                Intent().putExtra(AuxioService.INTENT_KEY_START_ID, IntegerTable.START_ID_ACTIVITY)
            )
        )
    }

    @Test
    fun knownExternalStartIdentities_failSafeToImmediatePromotion() {
        for (startId in externalStartIds) {
            val intent = Intent().putExtra(AuxioService.INTENT_KEY_START_ID, startId)
            assertTrue(
                "startId=$startId",
                ForegroundServiceStartContract.requiresImmediatePromotion(intent),
            )
        }
    }

    @Test
    fun foregroundStartMarker_preservesEveryColdPlaybackStartIdentity() {
        for (startId in externalStartIds) {
            val intent =
                Intent()
                    .putExtra(AuxioService.INTENT_KEY_START_ID, startId)
                    .let(ForegroundServiceStartContract::markRequired)

            assertTrue(ForegroundServiceStartContract.requiresImmediatePromotion(intent))
            assertEquals(startId, intent.getIntExtra(AuxioService.INTENT_KEY_START_ID, -1))
        }
    }

    private companion object {
        val externalStartIds =
            listOf(
                IntegerTable.START_ID_BOOT,
                IntegerTable.START_ID_BLUETOOTH,
                IntegerTable.START_ID_MEDIA_BUTTON,
                IntegerTable.START_ID_TOPWAY,
                IntegerTable.START_ID_TASKER,
            )
    }
}
