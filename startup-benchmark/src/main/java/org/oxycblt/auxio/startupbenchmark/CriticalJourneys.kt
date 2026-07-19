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
import android.os.Trace
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
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
    const val TRACE_QUICK_FIND_FIRST_RESULT = "auxio.quick_find_first_result"
    const val TRACE_SEARCH_RESULT_TO_FIRST_AUDIO = "auxio.search_result_to_first_audio"
    const val TRACE_SAVED_SESSION_TO_FIRST_AUDIO = "auxio.saved_session_to_first_audio"
    const val TRACE_NEXT_COMMAND_TO_NEXT_AUDIO = "auxio.next_command_to_next_audio"
    const val TRACE_USB0_FOLDER_TO_FIRST_AUDIO = "auxio.usb0_folder_to_first_audio"
    const val TRACE_USB1_FOLDER_TO_FIRST_AUDIO = "auxio.usb1_folder_to_first_audio"
    const val TRACE_FIRST_SONGS_PAGE = "auxio.first_songs_page"
    const val TRACE_FIRST_ALBUMS_PAGE = "auxio.first_albums_page"
    const val TRACE_MEDIA_BROWSER_FIRST_PAGE = "auxio.media_browser_first_page"

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

    /** Exercises the seeded primitive queue through the public media-key authority. */
    fun MacrobenchmarkScope.exercisePlaybackControls() {
        withMediaController { controller ->
            dispatchMediaKey(KeyEvent.KEYCODE_MEDIA_PLAY, "Play")
            waitForPlaybackState(controller, PlaybackStateCompat.STATE_PLAYING)

            dispatchMediaKey(KeyEvent.KEYCODE_MEDIA_PAUSE, "Pause")
            waitForPlaybackState(controller, PlaybackStateCompat.STATE_PAUSED)

            dispatchMediaKey(KeyEvent.KEYCODE_MEDIA_PLAY, "Play")
            waitForPlaybackState(controller, PlaybackStateCompat.STATE_PLAYING)
            val first = waitForMediaFingerprint(controller)

            traceSection(TRACE_NEXT_COMMAND_TO_NEXT_AUDIO) {
                dispatchMediaKey(KeyEvent.KEYCODE_MEDIA_NEXT, "Next")
                waitForMediaFingerprint(controller, excluded = first)
                waitForPlaybackState(controller, PlaybackStateCompat.STATE_PLAYING)
            }
            val second = waitForMediaFingerprint(controller, excluded = first)

            dispatchMediaKey(KeyEvent.KEYCODE_MEDIA_PREVIOUS, "Previous")
            waitForMediaFingerprint(controller, excluded = second)
            waitForPlaybackState(controller, PlaybackStateCompat.STATE_PLAYING)
        }
    }

    fun MacrobenchmarkScope.exerciseQuickFind(query: String = "Fixture Track 000") {
        clickRequired(By.res(BuildConfig.TARGET_PACKAGE, "action_search"), "Quick Find action")
        val input = requireObject(By.clazz("android.widget.EditText"), "Quick Find input")
        val result =
            traceSection(TRACE_QUICK_FIND_FIRST_RESULT) {
                input.click()
                input.text = query
                device.waitForIdle()
                Thread.sleep(SETTLE_MS)
                requireObject(
                    By.res(BuildConfig.TARGET_PACKAGE, "song_name").textStartsWith(query),
                    "Quick Find result beginning '$query'",
                )
            }
        traceSection(TRACE_SEARCH_RESULT_TO_FIRST_AUDIO) {
            result.click()
            device.waitForIdle()
            waitForAudioPlayback()
        }
        withMediaController { controller ->
            val first = waitForMediaFingerprint(controller)
            traceSection(TRACE_NEXT_COMMAND_TO_NEXT_AUDIO) {
                dispatchMediaKey(KeyEvent.KEYCODE_MEDIA_NEXT, "Next after Quick Find")
                waitForMediaFingerprint(controller, excluded = first)
                waitForPlaybackState(controller, PlaybackStateCompat.STATE_PLAYING)
            }
        }
    }

    fun MacrobenchmarkScope.exerciseSavedSessionResume() {
        traceSection(TRACE_SAVED_SESSION_TO_FIRST_AUDIO) {
            startActivityAndWait()
            dispatchMediaKey(KeyEvent.KEYCODE_MEDIA_PLAY, "Play saved session")
            waitForAudioPlayback()
        }
    }

    fun MacrobenchmarkScope.exerciseUsbFolder(sourceIndex: Int = 0) {
        require(sourceIndex in 0..1) { "Unsupported USB fixture index: $sourceIndex" }
        val traceName =
            if (sourceIndex == 0) TRACE_USB0_FOLDER_TO_FIRST_AUDIO
            else TRACE_USB1_FOLDER_TO_FIRST_AUDIO
        val rootSelectors =
            listOf(
                By.textContains("usbdisk$sourceIndex"),
                By.textContains("USB $sourceIndex"),
                By.textContains("USB$sourceIndex"),
            )
        traceSection(traceName) {
            clickAnyRequired(rootSelectors, "Fast Start USB $sourceIndex root")
            device.waitForIdle()
            clickRequired(By.textStartsWith("▶"), "playable USB $sourceIndex fixture")
            waitForAudioPlayback()
        }
    }

    fun MacrobenchmarkScope.exercisePagedLibrary() {
        traceSection(TRACE_FIRST_SONGS_PAGE) {
            clickRequired(By.textContains("Songs"), "Songs tab")
            requireObject(By.res(BuildConfig.TARGET_PACKAGE, "song_name"), "first paged Songs row")
            scrollPageTwice()
        }
        traceSection(TRACE_FIRST_ALBUMS_PAGE) {
            clickRequired(By.textContains("Albums"), "Albums tab")
            requireObject(
                By.res(BuildConfig.TARGET_PACKAGE, "parent_name"),
                "first paged Albums row",
            )
            scrollPageTwice()
        }
        requireObject(
                By.res(BuildConfig.TARGET_PACKAGE, "parent_name"),
                "visible Album row after paging",
            )
            .click()
        device.waitForIdle()
        clickRequired(By.res(BuildConfig.TARGET_PACKAGE, "song_name"), "first Album track")
        waitForAudioPlayback()
    }

    fun exerciseEarlyMediaBrowser() {
        traceSection(TRACE_MEDIA_BROWSER_FIRST_PAGE) {
            val context = InstrumentationRegistry.getInstrumentation().context
            val connected = CountDownLatch(1)
            val failed = CountDownLatch(1)
            lateinit var browser: MediaBrowserCompat
            browser =
                MediaBrowserCompat(
                    context,
                    serviceComponent(),
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
    }

    fun MacrobenchmarkScope.waitForAudioPlayback() {
        withMediaController { controller ->
            waitForPlaybackState(controller, PlaybackStateCompat.STATE_PLAYING)
            waitForMediaFingerprint(controller)
        }
    }

    private fun MacrobenchmarkScope.dispatchMediaKey(keyCode: Int, description: String) {
        check(device.pressKeyCode(keyCode)) { "$description media key was not dispatched" }
        device.waitForIdle()
    }

    private fun MacrobenchmarkScope.scrollPageTwice() {
        val width = device.displayWidth
        val height = device.displayHeight
        repeat(2) {
            device.swipe(width / 2, height * 3 / 4, width / 2, height / 4, 20)
            device.waitForIdle()
        }
    }

    private fun waitForPlaybackState(controller: MediaControllerCompat, expectedState: Int) {
        val deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(AUDIO_TIMEOUT_MS)
        var lastState = PlaybackStateCompat.STATE_NONE
        while (System.nanoTime() < deadline) {
            lastState = controller.playbackState?.state ?: PlaybackStateCompat.STATE_NONE
            if (lastState == expectedState) return
            Thread.sleep(100)
        }
        error("Playback state did not become $expectedState; last state=$lastState")
    }

    private fun waitForMediaFingerprint(
        controller: MediaControllerCompat,
        excluded: PlaybackFingerprint? = null,
    ): PlaybackFingerprint {
        val deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(AUDIO_TIMEOUT_MS)
        var last = PlaybackFingerprint(null, null, null)
        while (System.nanoTime() < deadline) {
            val metadata = controller.metadata
            last =
                PlaybackFingerprint(
                    mediaId = metadata?.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID),
                    mediaUri = metadata?.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI),
                    title = metadata?.description?.title?.toString(),
                )
            if (last.hasIdentity && last != excluded) return last
            Thread.sleep(100)
        }
        error("Media metadata did not change to a usable item; last=$last excluded=$excluded")
    }

    private inline fun <T> withMediaController(block: (MediaControllerCompat) -> T): T {
        val context = InstrumentationRegistry.getInstrumentation().context
        val connected = CountDownLatch(1)
        val failed = CountDownLatch(1)
        lateinit var browser: MediaBrowserCompat
        browser =
            MediaBrowserCompat(
                context,
                serviceComponent(),
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
                "MediaController connection failed"
            }
            check(browser.isConnected) { "MediaBrowser disconnected before controller creation" }
            return block(MediaControllerCompat(context, browser.sessionToken))
        } finally {
            browser.disconnect()
        }
    }

    private inline fun <T> traceSection(name: String, block: () -> T): T {
        Trace.beginSection(name)
        return try {
            block()
        } finally {
            Trace.endSection()
        }
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

    private fun serviceComponent() =
        ComponentName(BuildConfig.TARGET_PACKAGE, "org.oxycblt.auxio.AuxioService")

    private data class PlaybackFingerprint(
        val mediaId: String?,
        val mediaUri: String?,
        val title: String?,
    ) {
        val hasIdentity: Boolean
            get() = !mediaId.isNullOrBlank() || !mediaUri.isNullOrBlank() || !title.isNullOrBlank()
    }
}
