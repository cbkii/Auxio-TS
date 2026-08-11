/*
 * Copyright (c) 2026 Auxio Project
 * QueueViewModelFastResumeTest.kt is part of Auxio.
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

package org.oxycblt.auxio.playback.queue

import java.lang.reflect.Method
import java.lang.reflect.Proxy
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.oxycblt.auxio.playback.persist.PersistenceRepository
import org.oxycblt.auxio.playback.persist.QueueDescriptor
import org.oxycblt.auxio.playback.persist.QueueItemRef
import org.oxycblt.auxio.playback.persist.QueueWindow
import org.oxycblt.auxio.playback.state.PlaybackStateManager
import org.oxycblt.auxio.playback.state.Progression
import org.oxycblt.auxio.playback.state.RepeatMode
import org.oxycblt.auxio.playback.state.RestoreOutcome
import org.oxycblt.auxio.playback.state.ShuffleScope
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
@LooperMode(LooperMode.Mode.PAUSED)
class QueueViewModelFastResumeTest {
    @Test
    fun primitiveWindowUsesLocalAdapterIndexAndStableGlobalNavigationTarget() {
        val manager = FakePlaybackManager(window(start = 40, current = 41))
        val model = QueueViewModel(manager.proxy, interfaceProxy())

        assertEquals(listOf(40, 41, 42), model.queue.value.map { it.globalPosition })
        assertEquals(1, model.index.value)
        assertEquals(41, model.globalPositionAt(1))

        model.gotoGlobalPosition(41)
        assertEquals(41, manager.lastGoto)
        assertNull(model.globalPositionAt(99))
    }

    @Test
    fun primitiveIndexMovedUsesLocalAdapterCoordinates() {
        val manager = FakePlaybackManager(window(start = 40, current = 41))
        val model = QueueViewModel(manager.proxy, interfaceProxy())

        synchronized(manager.proxy) { manager.listener?.onIndexMoved(42) }

        assertEquals(2, model.index.value)
        assertEquals(2, model.scrollTo.consume())

        synchronized(manager.proxy) { manager.listener?.onIndexMoved(50) }
        assertEquals(2, model.index.value)
        assertNull(model.scrollTo.consume())
    }

    @Test
    fun capturedPrimitiveNavigationUsesAuthorityGenerationNotDisplayPosition() {
        val manager = FakePlaybackManager(window(start = 40, current = 41))
        val model = QueueViewModel(manager.proxy, interfaceProxy())
        val target = checkNotNull(model.navigationTargetAt(1))

        model.goto(target)
        assertEquals(41, manager.lastGoto)

        manager.lastGoto = null
        manager.window = window(start = 60, current = 61)
        synchronized(manager.proxy) { manager.listener?.onQueueWindowChanged(manager.window) }
        model.goto(target)

        assertNull(manager.lastGoto)
    }

    @Test
    fun primitiveAuthorityRemovalDoesNotLeaveStaleRows() {
        val manager = FakePlaybackManager(window(start = 40, current = 41))
        val model = QueueViewModel(manager.proxy, interfaceProxy())
        assertTrue(model.queue.value.isNotEmpty())

        manager.window = null
        synchronized(manager.proxy) { manager.listener?.onQueueWindowChanged(null) }

        assertTrue(model.queue.value.isEmpty())
        assertNull(model.globalPositionAt(0))
    }

    private class FakePlaybackManager(initialWindow: QueueWindow?) {
        var window: QueueWindow? = initialWindow
        var listener: PlaybackStateManager.Listener? = null
        var lastGoto: Int? = null

        val proxy: PlaybackStateManager =
            interfaceProxy { method, args ->
                when (method.name) {
                    "getProgression" -> Progression.nil()
                    "getRepeatMode" -> RepeatMode.NONE
                    "getParent" -> null
                    "getCurrentSong" -> null
                    "getRawPlaybackMetadata" -> null
                    "getRestoreOutcome" -> RestoreOutcome.NOT_REQUESTED
                    "getQueue" -> emptyList<Any>()
                    "getQueueWindow" -> window
                    "getIndex" -> window?.descriptor?.currentLogicalPosition ?: -1
                    "isShuffled" -> false
                    "getShuffleScope" -> ShuffleScope.OFF
                    "getCurrentAudioSessionId" -> null
                    "isAudioFocusHeld" -> false
                    "addListener" -> {
                        listener = args?.singleOrNull() as? PlaybackStateManager.Listener
                        null
                    }
                    "removeListener" -> {
                        listener = null
                        null
                    }
                    "goto" -> {
                        lastGoto = args?.singleOrNull() as? Int
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

        fun window(start: Int, current: Int): QueueWindow {
            val totalCount = 100
            val descriptor =
                QueueDescriptor(
                    sessionId = 7L,
                    totalCount = totalCount,
                    currentLogicalPosition = current,
                    positionMs = 12_000L,
                    repeatMode = RepeatMode.NONE,
                    shuffleScope = ShuffleScope.OFF,
                    revision = 3L,
                    updatedAtMs = 1L,
                )
            return QueueWindow(
                descriptor = descriptor,
                startLogicalPosition = start,
                items =
                    (start until start + 3).map { logicalPosition ->
                        QueueItemRef(
                            logicalPosition = logicalPosition,
                            canonicalPosition = logicalPosition,
                            stableSongUid = null,
                            uri = "file:///storage/usbdisk0/$logicalPosition.flac",
                            pathFallback = "/storage/usbdisk0/$logicalPosition.flac",
                            titleFallback = "Song $logicalPosition",
                            artistFallback = "Artist",
                            albumFallback = "Album",
                            durationMs = 180_000L,
                        )
                    },
            )
        }
    }
}
