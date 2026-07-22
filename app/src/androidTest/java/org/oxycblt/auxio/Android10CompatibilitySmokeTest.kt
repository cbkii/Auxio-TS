/*
 * Copyright (c) 2026 Auxio Project
 * Android10CompatibilitySmokeTest.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.oxycblt.auxio

import android.content.ComponentName
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.os.Build
import android.support.v4.media.MediaBrowserCompat
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
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
    fun legacyStorageAndMediaComponentsArePresent() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val appInfo = context.packageManager.getApplicationInfo(context.packageName, 0)
        assertTrue(
            "Android 10 DirectFS requires requestLegacyExternalStorage",
            appInfo.flags and ApplicationInfo.FLAG_REQUEST_LEGACY_EXTERNAL_STORAGE != 0,
        )

        val service =
            context.packageManager.resolveService(
                Intent(context, AuxioService::class.java),
                0,
            )
        assertNotNull(service)
        assertTrue(requireNotNull(service).serviceInfo.exported)
    }

    @Test
    fun mediaBrowserConnectsToCanonicalPlaybackService() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val context = instrumentation.targetContext
        val completed = CountDownLatch(1)
        val failure = AtomicReference<String?>()
        lateinit var browser: MediaBrowserCompat
        browser =
            MediaBrowserCompat(
                context,
                ComponentName(context, AuxioService::class.java),
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

        instrumentation.runOnMainSync { browser.connect() }
        assertTrue("MediaBrowser callback timed out", completed.await(30, TimeUnit.SECONDS))
        instrumentation.runOnMainSync { browser.disconnect() }
        assertNull(failure.get(), failure.get())
    }
}
