/*
 * Copyright (c) 2026 Auxio Project
 * CriticalJourneys.kt is part of Auxio.
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

package org.oxycblt.auxio.startupbenchmark

import android.content.ComponentName
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.view.KeyEvent
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.BySelector
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/** Shared critical user journeys for profile generation and macrobenchmarks. */
internal object CriticalJourneys {
    private const val UI_TIMEOUT_MS = 10_000L
    private const val AUDIO_TIMEOUT_MS = 10_000L
    private const val SETTLE_MS = 350L

    fun MacrobenchmarkScope.launchFastStart() {
        pressHome()
        startActivityAndWait()
        requireObject(By.res(BuildConfig.TARGET_PACKAGE, "home_layout"), "Fast Start home")
        device.waitForIdle()
    }

    fun MacrobenchmarkScope.exerciseProcessDeathRelaunch() {
        device.executeShellCommand("am force-stop ${BuildConfig.TARGET_PACKAGE}")
        launchFastStart()
    }

    fun MacrobenchmarkScope.exercisePlaybackControls() {
        check(device.pressKeyCode(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE)) {
            "Play/Pause media key was not dispatched"
        }
        device.waitForIdle()
        check(device.pressKeyCode(KeyEvent.KEYCODE_MEDIA_NEXT)) { "Next media key was not dispatched" }
        device.waitForIdle()
        check(device.pressKeyCode(KeyEvent.KEYCODE_MEDIA_PREVIOUS)) {
            "Previous media key was not dispatched"
        }
        device.waitForIdle()
    }

    fun MacrobenchmarkScope.exerciseQuickFind(query: String = "Fixture Track 00010") {
        clickRequired(By.res(BuildConfig.TARGET_PACKAGE, "action_search"), "Quick Find action")
        val input = requireObject(By.clazz("android.widget.EditText"), "Quick Find input")
        input.click()
        input.text = query
        device.waitForIdle()
        Thread.sleep(SETTLE_MS)
        clickRequired(By.textContains(query), "Quick Find result '$query'")
        waitForAudioPlayback()
    }

    fun MacrobenchmarkScope.exerciseUsbFolder() {
        val rootSelectors =
            listOf(
                By.textContains("usbdisk0"),
                By.textContains("USB 0"),
                By.textContains("USB0"),
                By.textContains("usbdisk1"),
                By.textContains("USB 1"),
            )
        clickAnyRequired(rootSelectors, "Fast Start USB root")
        device.waitForIdle()
        clickRequired(By.textStartsWith("▶"), "playable USB fixture")
        waitForAudioPlayback()
    }

    fun MacrobenchmarkScope.exercisePagedLibrary() {
        clickRequired(By.textContains("Albums"), "Albums tab")
        device.waitForIdle()
        val width = device.displayWidth
        val height = device.displayHeight
        repeat(2) {
            device.swipe(width / 2, height * 3 / 4, width / 2, height / 4, 20)
            device.waitForIdle()
        }
        clickFirstVisibleRow()
    }

    fun exerciseEarlyMediaBrowser() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val connected = CountDownLatch(1)
        val failed = CountDownLatch(1)
        lateinit var browser: MediaBrowserCompat
        browser =
            MediaBrowserCompat(
                context,
                ComponentName(BuildConfig.TARGET_PACKAGE, "org.oxycblt.auxio.AuxioService"),
                object : MediaBrowserCompat.ConnectionCallback() {
                    override fun onConnected() {
                        connected.countDown()
                    }

                    override fun onConnectionFailed() {
                        failed.countDown()
                    }

                    override fun onConnectionSuspended() {
                        failed.countDown()
                    }
                },
                null,
            )
        browser.connect()
        try {
            check(connected.await(UI_TIMEOUT_MS, TimeUnit.MILLISECONDS) && failed.count == 1L) {
                "MediaBrowser root did not connect before full-library hydration"
            }
            check(browser.isConnected) { "MediaBrowser reported disconnected after callback" }
            val childrenReady = CountDownLatch(1)
            var childrenCount = -1
            val callback =
                object : MediaBrowserCompat.SubscriptionCallback() {
                    override fun onChildrenLoaded(
                        parentId: String,
                        children: MutableList<MediaBrowserCompat.MediaItem>,
                    ) {
                        childrenCount = children.size
                        childrenReady.countDown()
                    }

                    override fun onChildrenLoaded(
                        parentId: String,
                        children: MutableList<MediaBrowserCompat.MediaItem>,
                        options: Bundle,
                    ) {
                        childrenCount = children.size
                        childrenReady.countDown()
                    }

                    override fun onError(parentId: String) {
                        childrenReady.countDown()
                    }
                }
            browser.subscribe(browser.root, callback)
            check(childrenReady.await(UI_TIMEOUT_MS, TimeUnit.MILLISECONDS)) {
                "MediaBrowser first page timed out"
            }
            check(childrenCount >= 0) { "MediaBrowser first page returned an error" }
            browser.unsubscribe(browser.root, callback)
        } finally {
            browser.disconnect()
        }
    }

    private fun MacrobenchmarkScope.waitForAudioPlayback() {
        val deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(AUDIO_TIMEOUT_MS)
        var lastState = ""
        while (System.nanoTime() < deadline) {
            lastState = device.executeShellCommand("dumpsys media_session")
            if (
                lastState.contains(BuildConfig.TARGET_PACKAGE) &&
                    (lastState.contains("state=3") || lastState.contains("state=PlaybackState {state=3"))
            ) {
                return
            }
            Thread.sleep(200)
        }
        error("Audio did not enter PLAYING state. media_session=${lastState.takeLast(2_000)}")
    }

    private fun MacrobenchmarkScope.clickFirstVisibleRow() {
        val candidates =
            listOf(
                By.res(BuildConfig.TARGET_PACKAGE, "item_root"),
                By.res(BuildConfig.TARGET_PACKAGE, "item_name"),
            )
        clickAnyRequired(candidates, "first paged library row")
    }

    private fun MacrobenchmarkScope.clickRequired(selector: BySelector, description: String) {
        requireObject(selector, description).click()
        device.waitForIdle()
    }

    private fun MacrobenchmarkScope.clickAnyRequired(
        selectors: List<BySelector>,
        description: String,
    ) {
        for (selector in selectors) {
            val candidate = device.wait(Until.findObject(selector), 1_500L)
            if (candidate != null) {
                candidate.click()
                device.waitForIdle()
                return
            }
        }
        error("Required UI object not found: $description")
    }

    private fun MacrobenchmarkScope.requireObject(
        selector: BySelector,
        description: String,
    ): UiObject2 =
        device.wait(Until.findObject(selector), UI_TIMEOUT_MS)
            ?: error("Required UI object not found: $description")
}
