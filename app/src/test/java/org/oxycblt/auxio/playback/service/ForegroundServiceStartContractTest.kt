/*
 * Copyright (c) 2026 Auxio Project
 * ForegroundServiceStartContractTest.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
    fun unmarkedStart_doesNotRequireImmediatePromotion() {
        assertFalse(ForegroundServiceStartContract.requiresImmediatePromotion(Intent()))
        assertFalse(ForegroundServiceStartContract.requiresImmediatePromotion(null))
    }

    @Test
    fun foregroundStartMarker_preservesEveryColdPlaybackStartIdentity() {
        val startIds =
            listOf(
                IntegerTable.START_ID_BOOT,
                IntegerTable.START_ID_BLUETOOTH,
                IntegerTable.START_ID_MEDIA_BUTTON,
                IntegerTable.START_ID_TOPWAY,
                IntegerTable.START_ID_TASKER,
            )

        for (startId in startIds) {
            val intent =
                Intent()
                    .putExtra(AuxioService.INTENT_KEY_START_ID, startId)
                    .let(ForegroundServiceStartContract::markRequired)

            assertTrue(ForegroundServiceStartContract.requiresImmediatePromotion(intent))
            assertEquals(startId, intent.getIntExtra(AuxioService.INTENT_KEY_START_ID, -1))
        }
    }
}
