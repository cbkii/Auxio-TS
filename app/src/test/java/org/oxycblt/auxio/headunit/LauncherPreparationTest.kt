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
import android.os.SystemClock
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
class LauncherPreparationTest {
    @Test
    fun validPreparationStartsOnlyTheExistingBackgroundService() {
        val intent =
            Intent()
                .putExtra(
                    LauncherPreparationActivity.EXTRA_UNTIL,
                    SystemClock.elapsedRealtime() + 60_000L,
                )
        val controller =
            Robolectric.buildActivity(LauncherPreparationActivity::class.java, intent).create()
        assertTrue(controller.get().isFinishing)
        assertNull(shadowOf(controller.get()).nextStartedActivity)
        val service = shadowOf(controller.get()).nextStartedService
        org.junit.Assert.assertNotNull(service)
        org.junit.Assert.assertEquals(controller.get().packageName, service.component?.packageName)
        org.junit.Assert.assertEquals("com.tw.music.MusicService", service.component?.className)
        org.junit.Assert.assertEquals(org.oxycblt.auxio.AuxioService.ACTION_START, service.action)
        org.junit.Assert.assertEquals(
            org.oxycblt.auxio.IntegerTable.START_ID_BACKGROUND_READY,
            service.getIntExtra(org.oxycblt.auxio.AuxioService.INTENT_KEY_START_ID, -1),
        )
        controller.destroy()
    }

    @Test
    fun expiredTrampolineFinishesWithoutStartingThePlayer() {
        val intent =
            Intent()
                .putExtra(LauncherPreparationActivity.EXTRA_UNTIL, SystemClock.elapsedRealtime())
        val controller =
            Robolectric.buildActivity(LauncherPreparationActivity::class.java, intent).create()
        assertTrue(controller.get().isFinishing)
        assertNull(shadowOf(controller.get()).nextStartedActivity)
        assertNull(shadowOf(controller.get()).nextStartedService)
        controller.destroy()
    }
}
