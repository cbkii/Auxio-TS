/*
 * Copyright (c) 2026 Auxio Project
 * CrossUidMetadataGapProbe.kt is part of Auxio.
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

import android.annotation.SuppressLint
import android.content.ComponentName
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.test.platform.app.InstrumentationRegistry
import java.io.File
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.oxycblt.auxio.headunit.topway.TopwayServiceBridge

/**
 * Test-APK-only fault injection on the existing Auxio session. Every Next still executes the real
 * callback/player; the source process publishes a 250 ms blank BUFFERING interval before forwarding
 * it. There is no fixture session/player or production hook. A separate UID observes the real
 * Binder publications. This probes a forced gap, not evidence that the repaired source naturally
 * emits one.
 */
internal object CrossUidMetadataGapProbe {
    @SuppressLint("PrivateApi", "BlockedPrivateApi")
    fun run() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val context = instrumentation.targetContext
        val ready = File(context.cacheDir, "pair-gap-ready")
        val stop = File(context.cacheDir, "pair-gap-stop")
        ready.delete()
        stop.delete()
        val connected = CountDownLatch(1)
        val failure = AtomicReference<Throwable?>()
        val gaps = AtomicInteger()
        val preparesInGap = AtomicInteger()
        val main = Handler(Looper.getMainLooper())
        var session: MediaSessionCompat? = null
        var original: MediaSessionCompat.Callback? = null
        var inGap = false
        lateinit var browser: MediaBrowserCompat
        instrumentation.runOnMainSync {
            browser =
                MediaBrowserCompat(
                    context,
                    ComponentName(
                        context,
                        TopwayServiceBridge.resolveCompatServiceClass(AuxioService::class.java),
                    ),
                    object : MediaBrowserCompat.ConnectionCallback() {
                        override fun onConnected() {
                            try {
                                // Same-process instrumentation only; the caller checks
                                // API29/debug/emulator. Match the actual browser token
                                // before touching a holder.
                                val thread =
                                    Class.forName("android.app.ActivityThread")
                                        .getDeclaredMethod("currentActivityThread")
                                        .invoke(null)
                                val services = field(thread, "mServices") as Map<*, *>
                                val service =
                                    services.values.filterIsInstance<AuxioService>().single {
                                        it.sessionToken == browser.sessionToken
                                    }
                                val fragment = field(service, "playbackFragment")
                                val holder = field(fragment, "sessionHolder")
                                val target = field(holder, "mediaSession") as MediaSessionCompat
                                val callback =
                                    field(holder, "mediaSessionInterface")
                                        as MediaSessionCompat.Callback
                                session = target
                                original = callback
                                target.setCallback(
                                    object : MediaSessionCompat.Callback() {
                                        override fun onPrepare() {
                                            if (inGap) preparesInGap.incrementAndGet()
                                            callback.onPrepare()
                                        }

                                        override fun onPause() = callback.onPause()

                                        override fun onSkipToNext() {
                                            check(!inGap) { "Overlapping probe commands" }
                                            val state =
                                                checkNotNull(target.controller.playbackState)
                                            inGap = true
                                            target.setMetadata(
                                                MediaMetadataCompat.Builder().build()
                                            )
                                            target.setPlaybackState(
                                                PlaybackStateCompat.Builder(state)
                                                    .setState(
                                                        PlaybackStateCompat.STATE_BUFFERING,
                                                        state.position,
                                                        0f,
                                                        SystemClock.elapsedRealtime(),
                                                    )
                                                    .setActiveQueueItemId(
                                                        MediaSessionCompat.QueueItem.UNKNOWN_ID
                                                            .toLong()
                                                    )
                                                    .build()
                                            )
                                            android.util.Log.i("TS18Transport", "pair-gap=BEGIN")
                                            main.postDelayed(
                                                {
                                                    inGap = false
                                                    gaps.incrementAndGet()
                                                    callback.onSkipToNext()
                                                    android.util.Log.i(
                                                        "TS18Transport",
                                                        "pair-gap=FORWARDED count=${gaps.get()}",
                                                    )
                                                },
                                                250L,
                                            )
                                        }
                                    },
                                    main,
                                )
                                ready.writeText("ready\n")
                            } catch (error: Throwable) {
                                failure.set(error)
                            } finally {
                                connected.countDown()
                            }
                        }

                        override fun onConnectionFailed() {
                            failure.set(AssertionError("Actual Auxio browser refused probe"))
                            connected.countDown()
                        }
                    },
                    null,
                )
            browser.connect()
        }
        try {
            assertTrue("Probe browser did not answer", connected.await(12, TimeUnit.SECONDS))
            failure.get()?.let { throw AssertionError("Probe setup failed", it) }
            val deadline = SystemClock.elapsedRealtime() + 60000L
            while (!stop.exists() && SystemClock.elapsedRealtime() < deadline) Thread.sleep(50L)
            assertTrue("Launcher did not finish the probe", stop.exists())
            assertEquals("Every real Next must cross the forced source gap", 4, gaps.get())
            assertEquals("Passive PREPARE crossed the gap", 0, preparesInGap.get())
        } finally {
            instrumentation.runOnMainSync {
                main.removeCallbacksAndMessages(null)
                session?.setCallback(original)
                browser.disconnect()
            }
            ready.delete()
            stop.delete()
        }
    }

    private fun field(instance: Any, name: String): Any {
        var type: Class<*>? = instance.javaClass
        while (type != null) {
            try {
                return checkNotNull(
                    type.getDeclaredField(name).apply { isAccessible = true }.get(instance)
                )
            } catch (_: NoSuchFieldException) {
                type = type.superclass
            }
        }
        error("Missing test field $name")
    }
}
