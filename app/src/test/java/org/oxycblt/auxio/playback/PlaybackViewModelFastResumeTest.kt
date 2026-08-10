/*
 * Copyright (c) 2026 Auxio Project
 * PlaybackViewModelFastResumeTest.kt is part of Auxio.
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

package org.oxycblt.auxio.playback

import android.os.Looper
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.concurrent.TimeUnit
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.oxycblt.auxio.list.ListSettings
import org.oxycblt.auxio.playback.persist.QueueDescriptor
import org.oxycblt.auxio.playback.persist.QueueItemRef
import org.oxycblt.auxio.playback.persist.QueueWindow
import org.oxycblt.auxio.playback.state.PlaybackStateManager
import org.oxycblt.auxio.playback.state.Progression
import org.oxycblt.auxio.playback.state.RawPlaybackMetadata
import org.oxycblt.auxio.playback.state.RepeatMode
import org.oxycblt.auxio.playback.state.RestoreOutcome
import org.oxycblt.auxio.playback.state.ShuffleScope
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
@LooperMode(LooperMode.Mode.PAUSED)
class PlaybackViewModelFastResumeTest {
    @Test
    fun registrationGapIsClosedByPostRegistrationCanonicalSync() {
        val manager = FakePlaybackManager()
        manager.transitionDuringRegistration = true

        val model = newModel(manager.proxy)

        assertTrue(model.isPlaying.value)
        assertTrue(model.positionDs.value >= 120L)
        assertEquals("Fast song", model.rawPlaybackMetadata.value?.displayTitle)
        assertEquals(73, model.currentAudioSessionId.value)
        assertPositionAdvances(model)
    }

    @Test
    fun primitiveWindowTransitionReconcilesProgressionWithoutExtraProgressionCallback() {
        val manager = FakePlaybackManager()
        val model = newModel(manager.proxy)
        assertFalse(model.isPlaying.value)

        val window = primitiveWindow()
        manager.progression =
            Progression.from(isPlaying = true, isAdvancing = true, positionMs = 9000)
        manager.rawMetadata = newRawMetadata(positionMs = 9000)
        manager.window = window

        // PlaybackStateManager callbacks are synchronous under its monitor. Deliberately deliver
        // only QueueWindowChanged: the UI must not require a later play/pause/progression event to
        // discover the canonical Fast Resume state.
        synchronized(manager.proxy) { manager.listener?.onQueueWindowChanged(window) }

        assertTrue(model.isPlaying.value)
        assertTrue(model.positionDs.value >= 90L)
        assertEquals("Fast song", model.rawPlaybackMetadata.value?.displayTitle)
        assertPositionAdvances(model)
    }

    @Test
    fun rawMetadataCallbackUpdatesUiMirrorDirectly() {
        val manager = FakePlaybackManager()
        val model = newModel(manager.proxy)
        val updated = newRawMetadata(positionMs = 17_000, title = "Updated raw title")

        manager.listener?.onRawPlaybackMetadataChanged(updated)

        assertNotNull(model.rawPlaybackMetadata.value)
        assertEquals("Updated raw title", model.rawPlaybackMetadata.value?.displayTitle)
    }

    private fun assertPositionAdvances(model: PlaybackViewModel) {
        val before = model.positionDs.value
        shadowOf(Looper.getMainLooper()).idleFor(250, TimeUnit.MILLISECONDS)
        assertTrue(
            "Playing position must advance without another playback callback",
            model.positionDs.value > before,
        )
    }

    private fun newModel(manager: PlaybackStateManager) =
        PlaybackViewModel(
            playbackManager = manager,
            playbackSettings = playbackSettings(),
            commandFactory = interfaceProxy(),
            listSettings = interfaceProxy(),
        )

    private fun playbackSettings(): PlaybackSettings =
        interfaceProxy { method, _ ->
            when (method.name) {
                "getBarAction" -> ActionMode.NEXT
                else -> defaultValue(method)
            }
        }

    private class FakePlaybackManager {
        var progression: Progression = Progression.nil()
        var rawMetadata: RawPlaybackMetadata? = null
        var window: QueueWindow? = null
        var listener: PlaybackStateManager.Listener? = null
        var transitionDuringRegistration = false

        val proxy: PlaybackStateManager =
            interfaceProxy { method, args ->
                when (method.name) {
                    "getProgression" -> progression
                    "getRepeatMode" -> RepeatMode.NONE
                    "getParent" -> null
                    "getCurrentSong" -> null
                    "getRawPlaybackMetadata" -> rawMetadata
                    "getRestoreOutcome" -> RestoreOutcome.NOT_REQUESTED
                    "getQueue" -> emptyList<Any>()
                    "getQueueWindow" -> window
                    "getIndex" -> window?.descriptor?.currentLogicalPosition ?: -1
                    "isShuffled" -> false
                    "getShuffleScope" -> ShuffleScope.OFF
                    "getCurrentAudioSessionId" -> 73
                    "isAudioFocusHeld" -> true
                    "addListener" -> {
                        listener = args?.singleOrNull() as? PlaybackStateManager.Listener
                        if (transitionDuringRegistration) {
                            progression =
                                Progression.from(
                                    isPlaying = true,
                                    isAdvancing = true,
                                    positionMs = 12_000,
                                )
                            rawMetadata = newRawMetadata(positionMs = 12_000)
                        }
                        null
                    }
                    "removeListener" -> {
                        listener = null
                        null
                    }
                    else -> defaultValue(method)
                }
            }
    }

    private companion object {
        @Suppress("UNCHECKED_CAST")
        inline fun <reified T> interfaceProxy(
            noinline invocation: (Method, Array<out Any?>?) -> Any? = { method, _ ->
                defaultValue(method)
            }
        ): T =
            Proxy.newProxyInstance(
                T::class.java.classLoader,
                arrayOf(T::class.java),
            ) { proxy, method, args ->
                when (method.name) {
                    "toString" -> "${T::class.java.simpleName}TestProxy"
                    "hashCode" -> System.identityHashCode(proxy)
                    "equals" -> proxy === args?.firstOrNull()
                    else -> invocation(method, args)
                }
            } as T

        fun defaultValue(method: Method): Any? =
            when {
                method.returnType == java.lang.Boolean.TYPE -> false
                method.returnType == java.lang.Integer.TYPE -> 0
                method.returnType == java.lang.Long.TYPE -> 0L
                method.returnType == java.lang.Float.TYPE -> 0f
                method.returnType == java.lang.Double.TYPE -> 0.0
                method.returnType == java.lang.Void.TYPE -> null
                method.returnType.isEnum -> method.returnType.enumConstants?.firstOrNull()
                else -> null
            }

        fun newRawMetadata(positionMs: Long, title: String = "Fast song") =
            RawPlaybackMetadata(
                title = title,
                artist = "Fast artist",
                album = "Fast album",
                uriString = "file:///storage/usbdisk0/fast.flac",
                path = "/storage/usbdisk0/fast.flac",
                durationMs = 180_000L,
                positionMs = positionMs,
                isPlaying = true,
                savedAtMs = 1L,
            )

        fun primitiveWindow(): QueueWindow {
            val descriptor =
                QueueDescriptor(
                    sessionId = 5L,
                    totalCount = 1,
                    currentLogicalPosition = 0,
                    positionMs = 9000L,
                    repeatMode = RepeatMode.NONE,
                    shuffleScope = ShuffleScope.OFF,
                    revision = 2L,
                    updatedAtMs = 1L,
                )
            return QueueWindow(
                descriptor = descriptor,
                startLogicalPosition = 0,
                items =
                    listOf(
                        QueueItemRef(
                            logicalPosition = 0,
                            canonicalPosition = 0,
                            stableSongUid = null,
                            uri = "file:///storage/usbdisk0/fast.flac",
                            pathFallback = "/storage/usbdisk0/fast.flac",
                            titleFallback = "Fast song",
                            artistFallback = "Fast artist",
                            albumFallback = "Fast album",
                            durationMs = 180_000L,
                        )
                    ),
            )
        }
    }
}
