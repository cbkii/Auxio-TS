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
import java.util.concurrent.atomic.AtomicInteger

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

    private const val UI_TIMEOUT_MS = 20_000L
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

    /** Exercises the seeded primitive queue through the public MediaSession media-key authority. */
    fun MacrobenchmarkScope.exercisePlaybackControls() {
        withMediaController { controller ->
            dispatchMediaKey(
                controller,
                KeyEvent.KEYCODE_MEDIA_PLAY,
                "Play",
                PlaybackStateCompat.STATE_PLAYING,
            )

            dispatchMediaKey(
                controller,
                KeyEvent.KEYCODE_MEDIA_PAUSE,
                "Pause",
                PlaybackStateCompat.STATE_PAUSED,
            )

            dispatchMediaKey(
                controller,
                KeyEvent.KEYCODE_MEDIA_PLAY,
                "Play",
                PlaybackStateCompat.STATE_PLAYING,
            )
            val first = waitForMediaFingerprint(controller)

            traceSection(TRACE_NEXT_COMMAND_TO_NEXT_AUDIO) {
                dispatchMediaKey(
                    controller,
                    KeyEvent.KEYCODE_MEDIA_NEXT,
                    "Next",
                    PlaybackStateCompat.STATE_PLAYING,
                )
                waitForMediaFingerprint(controller, excluded = first)
            }
            val second = waitForMediaFingerprint(controller, excluded = first)

            dispatchMediaKey(
                controller,
                KeyEvent.KEYCODE_MEDIA_PREVIOUS,
                "Previous",
                PlaybackStateCompat.STATE_PLAYING,
            )
            waitForMediaFingerprint(controller, excluded = second)
        }
    }

    fun MacrobenchmarkScope.exerciseQuickFind(query: String = "Fixture Track 00010") {
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
            result.clickClickableAncestor("Quick Find result beginning '$query'")
            device.waitForIdle()
            waitForAudioPlayback()
        }
        withMediaController { controller ->
            val first = waitForMediaFingerprint(controller)
            traceSection(TRACE_NEXT_COMMAND_TO_NEXT_AUDIO) {
                dispatchMediaKey(
                    controller,
                    KeyEvent.KEYCODE_MEDIA_NEXT,
                    "Next after Quick Find",
                    PlaybackStateCompat.STATE_PLAYING,
                )
                waitForMediaFingerprint(controller, excluded = first)
            }
        }
    }

    fun MacrobenchmarkScope.exerciseSavedSessionResume() {
        traceSection(TRACE_SAVED_SESSION_TO_FIRST_AUDIO) {
            startActivityAndWait()
            withMediaController { controller ->
                dispatchMediaKey(
                    controller,
                    KeyEvent.KEYCODE_MEDIA_PLAY,
                    "Play saved session",
                    PlaybackStateCompat.STATE_PLAYING,
                )
                waitForMediaFingerprint(controller)
            }
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
            clickAnyAtCenterRequired(rootSelectors, "Fast Start USB $sourceIndex root")
            device.waitForIdle()
            clickAtCenterRequired(By.textStartsWith("▶"), "playable USB $sourceIndex fixture")
            waitForAudioPlayback()
        }
    }

    fun MacrobenchmarkScope.exercisePagedLibrary() {
        traceSection(TRACE_FIRST_SONGS_PAGE) {
            clickAtCenterRequired(By.textContains("Songs"), "Songs tab")
            requireObject(By.res(BuildConfig.TARGET_PACKAGE, "song_name"), "first paged Songs row")
            scrollPageTwice()
        }
        traceSection(TRACE_FIRST_ALBUMS_PAGE) {
            clickAtCenterRequired(By.textContains("Albums"), "Albums tab")
            requireObject(
                By.res(BuildConfig.TARGET_PACKAGE, "parent_name"),
                "first paged Albums row",
            )
            scrollPageTwice()
        }
        clickRequired(
            By.res(BuildConfig.TARGET_PACKAGE, "parent_name"),
            "visible Album row after paging",
        )
        clickRequired(By.res(BuildConfig.TARGET_PACKAGE, "song_name"), "first Album track")
        waitForAudioPlayback()
    }

    fun exerciseEarlyMediaBrowser() {
        traceSection(TRACE_MEDIA_BROWSER_FIRST_PAGE) {
            withConnectedBrowser(
                connectionFailureMessage =
                    "MediaBrowser root did not connect before full-library hydration",
                disconnectedMessage = "MediaBrowser reported disconnected after callback",
            ) { browser ->
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
                runOnMainSync { browser.subscribe(browser.root, callback) }
                try {
                    check(childrenReady.await(UI_TIMEOUT_MS, TimeUnit.MILLISECONDS)) {
                        "MediaBrowser first page timed out"
                    }
                    check(childrenCount >= 0) { "MediaBrowser first page returned an error" }
                } finally {
                    runOnMainSync { browser.unsubscribe(browser.root, callback) }
                }
            }
        }
    }

    fun MacrobenchmarkScope.waitForAudioPlayback() {
        withMediaController { controller ->
            waitForPlaybackState(controller, PlaybackStateCompat.STATE_PLAYING)
            waitForMediaFingerprint(controller)
        }
    }

    private fun MacrobenchmarkScope.dispatchMediaKey(
        controller: MediaControllerCompat,
        keyCode: Int,
        description: String,
        expectedState: Int,
    ) {
        val observedState = CountDownLatch(1)
        val lastState =
            AtomicInteger(controller.playbackState?.state ?: PlaybackStateCompat.STATE_NONE)
        val callback =
            object : MediaControllerCompat.Callback() {
                override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
                    val currentState = state?.state ?: PlaybackStateCompat.STATE_NONE
                    lastState.set(currentState)
                    if (currentState == expectedState) observedState.countDown()
                }
            }
        runOnMainSync { controller.registerCallback(callback) }
        try {
            // UiDevice.pressKeyCode() proved nondeterministic on hosted managed emulators: the key
            // was injected into the device but was not routed to Auxio's active session.
            // Dispatching it through the explicitly connected controller keeps the journey scoped
            // to the target session without bypassing MediaSessionCompat.Callback.
            val accepted = mainThreadValue {
                controller.dispatchMediaButtonEvent(KeyEvent(KeyEvent.ACTION_DOWN, keyCode))
            }
            check(accepted) { "$description media key was rejected by the connected MediaSession" }
            val currentState = controller.playbackState?.state ?: PlaybackStateCompat.STATE_NONE
            lastState.set(currentState)
            val reachedExpectedState =
                currentState == expectedState ||
                    observedState.await(AUDIO_TIMEOUT_MS, TimeUnit.MILLISECONDS)
            check(reachedExpectedState) {
                "$description did not reach playback state $expectedState; " +
                    "last state=${lastState.get()}"
            }
        } finally {
            runOnMainSync { controller.unregisterCallback(callback) }
        }
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

    private inline fun <T> withMediaController(block: (MediaControllerCompat) -> T): T =
        withConnectedBrowser(
            connectionFailureMessage = "MediaController connection failed",
            disconnectedMessage = "MediaBrowser disconnected before controller creation",
        ) { browser ->
            val context = InstrumentationRegistry.getInstrumentation().context
            val controller = mainThreadValue {
                MediaControllerCompat(context, browser.sessionToken)
            }
            block(controller)
        }

    private inline fun <T> withConnectedBrowser(
        connectionFailureMessage: String,
        disconnectedMessage: String,
        block: (MediaBrowserCompat) -> T,
    ): T {
        val context = InstrumentationRegistry.getInstrumentation().context
        val connected = CountDownLatch(1)
        val failed = CountDownLatch(1)
        val browser = mainThreadValue {
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
        }
        runOnMainSync { browser.connect() }
        try {
            check(connected.await(UI_TIMEOUT_MS, TimeUnit.MILLISECONDS) && failed.count == 1L) {
                connectionFailureMessage
            }
            check(browser.isConnected) { disconnectedMessage }
            return block(browser)
        } finally {
            runOnMainSync { browser.disconnect() }
        }
    }

    private fun runOnMainSync(block: () -> Unit) {
        InstrumentationRegistry.getInstrumentation().runOnMainSync { block() }
    }

    private fun <T> mainThreadValue(block: () -> T): T {
        var result: Result<T>? = null
        runOnMainSync { result = runCatching(block) }
        return requireNotNull(result) { "Main-thread benchmark operation did not complete" }
            .getOrThrow()
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
        requireObject(selector, description).clickClickableAncestor(description)
        device.waitForIdle()
    }

    private fun MacrobenchmarkScope.clickAtCenterRequired(
        selector: BySelector,
        description: String,
    ) {
        val center = requireObject(selector, description).visibleCenter
        check(device.click(center.x, center.y)) { "Coordinate click failed: $description" }
        device.waitForIdle()
    }

    private fun MacrobenchmarkScope.clickAnyAtCenterRequired(
        selectors: List<BySelector>,
        description: String,
    ) {
        for (selector in selectors) {
            val candidate = device.wait(Until.findObject(selector), 1_500L) ?: continue
            val center = candidate.visibleCenter
            if (device.click(center.x, center.y)) {
                device.waitForIdle()
                return
            }
        }
        error("Required UI object not found or coordinate click failed: $description")
    }

    private fun MacrobenchmarkScope.clickAnyRequired(
        selectors: List<BySelector>,
        description: String,
    ) {
        for (selector in selectors) {
            val candidate = device.wait(Until.findObject(selector), 1_500L)
            if (candidate != null) {
                candidate.clickClickableAncestor(description)
                device.waitForIdle()
                return
            }
        }
        error("Required UI object not found: $description")
    }

    private fun UiObject2.clickClickableAncestor(description: String) {
        var candidate: UiObject2? = this
        var depth = 0
        while (candidate != null && depth < 8) {
            if (candidate.isClickable) {
                candidate.click()
                return
            }
            candidate = candidate.parent
            depth += 1
        }
        error("Required UI object has no clickable ancestor: $description")
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
