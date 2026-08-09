/*
 * Copyright (c) 2026 Auxio Project
 * Android10CompatibilitySmokeTest.kt is part of Auxio.
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

package org.oxycblt.auxio

import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.os.SystemClock
import android.support.v4.media.MediaBrowserCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import com.tw.music.MusicService
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.oxycblt.auxio.playback.service.ForegroundServiceStartContract

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 29, maxSdkVersion = 29)
class Android10CompatibilitySmokeTest {
    @Test
    fun applicationAndMainActivityReachResumedState() {
        assertEquals(29, Build.VERSION.SDK_INT)
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.moveToState(Lifecycle.State.RESUMED)
            scenario.onActivity { activity -> assertFalse(activity.isFinishing) }
        }
    }

    @Test
    fun legacyStorageAndCanonicalTopwayMediaComponentsArePresent() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        assertTrue(
            "Android 10 DirectFS requires legacy external-storage behaviour",
            Environment.isExternalStorageLegacy(),
        )

        val internalService =
            context.packageManager.resolveService(Intent(context, AuxioService::class.java), 0)
        assertNotNull(internalService)
        assertFalse(requireNotNull(internalService).serviceInfo.exported)

        val canonicalService =
            context.packageManager.resolveService(Intent(context, MusicService::class.java), 0)
        assertNotNull(canonicalService)
        assertTrue(requireNotNull(canonicalService).serviceInfo.exported)
    }

    @Test
    fun markedForegroundStartPromotesCanonicalServiceOnApi29() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val serviceIntent =
            ForegroundServiceStartContract.markRequired(
                Intent(context, MusicService::class.java)
                    .setAction(AuxioService.ACTION_START)
                    .putExtra(AuxioService.INTENT_KEY_START_ID, IntegerTable.START_ID_BOOT)
            )

        try {
            ContextCompat.startForegroundService(context, serviceIntent)
            val deadline = SystemClock.elapsedRealtime() + FOREGROUND_PROMOTION_WAIT_MS
            while (!AuxioService.isForeground && SystemClock.elapsedRealtime() < deadline) {
                SystemClock.sleep(FOREGROUND_PROMOTION_POLL_MS)
            }
            assertTrue(
                "Canonical playback service did not acquire foreground state on Android 10",
                AuxioService.isForeground,
            )
        } finally {
            context.stopService(serviceIntent)
        }
    }

    @Test
    fun mediaBrowserConnectsToCanonicalPlaybackService() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val context = instrumentation.targetContext
        val completed = CountDownLatch(1)
        val failure = AtomicReference<String?>()
        lateinit var browser: MediaBrowserCompat

        instrumentation.runOnMainSync {
            browser =
                MediaBrowserCompat(
                    context,
                    ComponentName(context, MusicService::class.java),
                    object : MediaBrowserCompat.ConnectionCallback() {
                        override fun onConnected() {
                            try {
                                assertTrue(browser.isConnected)
                                assertNotNull(browser.sessionToken)
                            } catch (error: AssertionError) {
                                failure.set(error.message ?: error.javaClass.simpleName)
                            } finally {
                                completed.countDown()
                            }
                        }

                        override fun onConnectionFailed() {
                            failure.set("MediaBrowser connection failed")
                            completed.countDown()
                        }

                        override fun onConnectionSuspended() {
                            failure.set("MediaBrowser connection suspended")
                            completed.countDown()
                        }
                    },
                    null,
                )
            browser.connect()
        }

        assertTrue("MediaBrowser callback timed out", completed.await(30, TimeUnit.SECONDS))
        instrumentation.runOnMainSync { browser.disconnect() }
        assertNull(failure.get(), failure.get())
    }

    private companion object {
        const val FOREGROUND_PROMOTION_WAIT_MS = 4_000L
        const val FOREGROUND_PROMOTION_POLL_MS = 20L
    }
}
