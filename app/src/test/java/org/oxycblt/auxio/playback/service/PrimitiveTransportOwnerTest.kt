/*
 * Copyright (c) 2026 Auxio Project
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package org.oxycblt.auxio.playback.service

import android.app.Application
import android.content.Context
import android.net.Uri
import android.os.Looper
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.coroutines.Continuation
import kotlin.coroutines.intrinsics.COROUTINE_SUSPENDED
import kotlin.coroutines.resume
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.oxycblt.auxio.headunit.ts18.RawFastResumeItem
import org.oxycblt.auxio.image.ImageSettings
import org.oxycblt.auxio.music.*
import org.oxycblt.auxio.playback.PlaybackSettings
import org.oxycblt.auxio.playback.persist.*
import org.oxycblt.auxio.playback.replaygain.ReplayGainAudioProcessor
import org.oxycblt.auxio.playback.state.*
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.util.ReflectionHelpers

/** Actual playback owner; only the player and persistence/platform boundaries are doubles. */
@OptIn(UnstableApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29], application = Application::class)
class PrimitiveTransportOwnerTest {
    private lateinit var holder: ExoPlaybackStateHolder
    private val items = mutableListOf<MediaItem>()
    private var index = 0
    private var position = 2300L
    private var playing = true
    private var prepares = 0
    private var rememberPause = true
    private var descriptor =
        QueueDescriptor(17, 3, 0, 2300, RepeatMode.NONE, ShuffleScope.OFF, 1, 1)
    private var rows = (0..2).map(::item)
    @Volatile private var blockRead = false
    @Volatile private var waiting: Continuation<QueueWindow?>? = null
    private var readEntered = CountDownLatch(1)

    @Before
    fun setUp() {
        val context: Context = RuntimeEnvironment.getApplication()
        val manager = proxy<PlaybackStateManager>()
        val settings =
            proxy<PlaybackSettings> { method, _ ->
                when (method.name) {
                    "getRememberPause" -> rememberPause
                    "getRewindWithPrev" -> false
                    else -> defaultValue(method)
                }
            }
        val player =
            proxy<ExoPlayer> { method, args ->
                when (method.name) {
                    "getPlayWhenReady",
                    "isPlaying" -> playing
                    "setPlayWhenReady" -> {
                        playing = args!![0] as Boolean
                        null
                    }
                    "play" -> {
                        playing = true
                        null
                    }
                    "pause" -> {
                        playing = false
                        null
                    }
                    "getCurrentPosition" -> position
                    "getCurrentMediaItemIndex" -> index
                    "getCurrentMediaItem" -> items.getOrNull(index)
                    "getMediaItemCount" -> items.size
                    "getMediaItemAt" -> items[args!![0] as Int]
                    "getRepeatMode" -> Player.REPEAT_MODE_OFF
                    "getPlaybackState" -> Player.STATE_READY
                    "getAudioSessionId" -> 7
                    "setMediaItems" -> {
                        items.clear()
                        items.addAll((args!![0] as List<*>).filterIsInstance<MediaItem>())
                        index = 0
                        null
                    }
                    "seekTo" -> {
                        val value =
                            if (args!!.size == 2) {
                                index = args[0] as Int
                                args[1] as Long
                            } else args[0] as Long
                        position = if (value == C.TIME_UNSET) 0 else value
                        null
                    }
                    "prepare" -> {
                        prepares++
                        null
                    }
                    "removeMediaItems" -> {
                        val first = args!![0] as Int
                        val last = args[1] as Int
                        items.subList(first, last).clear()
                        if (index >= last) index -= last - first
                        null
                    }
                    "addMediaItems" -> {
                        val at = if (args!!.size == 2) args[0] as Int else items.size
                        val added = (args.last() as List<*>).filterIsInstance<MediaItem>()
                        items.addAll(at, added)
                        if (at <= index) index += added.size
                        null
                    }
                    "replaceMediaItem" -> {
                        items[args!![0] as Int] = args[1] as MediaItem
                        null
                    }
                    else -> defaultValue(method)
                }
            }
        val persistence =
            proxy<PersistenceRepository> { method, args ->
                when (method.name) {
                    "updateQueuePosition" -> true
                    "readAllQueueItems" -> rows
                    "reorderQueue" -> {
                        val order = (args!![1] as List<*>).filterIsInstance<Int>()
                        val currentCanonical =
                            rows[descriptor.currentLogicalPosition].canonicalPosition
                        rows =
                            order.mapIndexed { logical, canonical ->
                                rows
                                    .first { it.canonicalPosition == canonical }
                                    .copy(logicalPosition = logical)
                            }
                        descriptor =
                            descriptor.copy(
                                currentLogicalPosition = order.indexOf(currentCanonical),
                                shuffleScope = args[2] as ShuffleScope,
                                revision = descriptor.revision + 1,
                            )
                        descriptor
                    }
                    "readQueueWindowAround" -> {
                        if (blockRead) {
                            blockRead = false
                            @Suppress("UNCHECKED_CAST")
                            waiting = args!!.last() as Continuation<QueueWindow?>
                            readEntered.countDown()
                            COROUTINE_SUSPENDED
                        } else QueueWindow(args!![0] as QueueDescriptor, 0, rows)
                    }
                    "readQueueWindow" -> QueueWindow(args!![0] as QueueDescriptor, 0, rows)
                    else -> defaultValue(method)
                }
            }
        val readiness = StartupReadinessController()
        holder =
            ExoPlaybackStateHolder(
                context,
                player,
                manager,
                persistence,
                settings,
                proxy<PlaybackCommand.Factory>(),
                ReplayGainAudioProcessor(manager, settings),
                proxy<MusicRepository>(),
                proxy<ImageSettings>(),
                readiness,
                ConfiguredSourcePolicy(proxy<MusicSettings>()),
                StartupOptionalWorkGate(readiness),
            )
        ReflectionHelpers.setField(holder, "hasAudioFocus", true)
        installWindow(QueueWindow(descriptor, 0, rows))
    }

    @After
    fun tearDown() {
        holder.release()
    }

    @Test
    fun rememberPausePreservesPlayingAndPausedIntentThroughLocalNavigation() {
        holder.next()
        assertTrue(playing)
        assertEquals(1, index)
        holder.prev()
        assertTrue(playing)
        assertEquals(0, index)
        holder.goto(2)
        assertTrue(playing)
        assertEquals(2, index)
        holder.playing(false)
        holder.prev()
        assertFalse(playing)
        assertEquals(1, index)
        holder.next()
        assertFalse(playing)
        assertEquals(2, index)
    }

    @Test
    fun pauseDuringOutOfWindowReadCannotBeReplayedAsPlay() {
        installWindow(QueueWindow(descriptor, 0, rows.take(1)))
        blockRead = true
        holder.next()
        awaitRead()
        holder.playing(false)
        waiting!!.resume(QueueWindow(descriptor.copy(currentLogicalPosition = 1), 0, rows))
        drainUntil { holder.primitiveQueueWindow?.descriptor?.currentLogicalPosition == 1 }
        assertFalse(playing)
        assertEquals(1, index)
    }

    @Test
    fun seekSupersedesAnOlderOutOfWindowNavigation() {
        installWindow(QueueWindow(descriptor, 0, rows.take(1)))
        blockRead = true
        holder.next()
        awaitRead()
        holder.seekTo(6700L)
        waiting!!.resume(QueueWindow(descriptor.copy(currentLogicalPosition = 1), 0, rows))
        drainUntil { ReflectionHelpers.getField<Any?>(holder, "primitiveNavigationJob") == null }
        assertEquals(0, index)
        assertEquals(6700L, position)
        assertEquals(0, prepares)
    }

    @Test
    fun shuffleCommitKeepsCurrentSourceLatestPauseAndLatestPosition() {
        val current = items[index]
        blockRead = true
        holder.shuffled(true)
        awaitRead()
        holder.playing(false)
        holder.seekTo(4200L)
        waiting!!.resume(QueueWindow(descriptor, 0, rows))
        drainUntil { holder.primitiveQueueWindow?.descriptor?.shuffleScope == ShuffleScope.ALL }
        assertSame(current, items[index])
        assertEquals(4200L, position)
        assertFalse(playing)
        assertEquals(0, prepares)
    }

    @Test
    fun rawNextRetainsSoundAndCoalescesUntilTheOwningQueueIsAvailable() {
        ReflectionHelpers.setField(holder, "activePrimitiveWindow", null)
        val uri = Uri.parse("file:///music/0.mp3")
        ReflectionHelpers.setField(
            holder,
            "rawFastResumeItem",
            RawFastResumeItem(uri, uri.toString(), null, "0", "Artist", null, 10000, 0, 1),
        )
        holder.next()
        holder.next()
        assertTrue(playing)
        assertEquals(0, prepares)
        val pending = ReflectionHelpers.getField<Any>(holder, "rawFastResumePendingNavigation")
        assertEquals(2, ReflectionHelpers.getField<Int>(pending, "skipDelta").toInt())
    }

    private fun installWindow(window: QueueWindow) {
        ReflectionHelpers.setField(holder, "activePrimitiveWindow", window)
        items.clear()
        items.addAll(window.items.map { MediaItem.fromUri(it.uri!!) })
        index = window.currentLocalPosition
    }

    private fun awaitRead() {
        drainUntil { readEntered.count == 0L }
        assertTrue(readEntered.await(1, TimeUnit.SECONDS))
    }

    private fun drainUntil(done: () -> Boolean) {
        val deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(5)
        while (!done() && System.nanoTime() < deadline) {
            shadowOf(Looper.getMainLooper()).idle()
            Thread.yield()
        }
        assertTrue("Production owner did not finish within test deadline", done())
    }

    private fun item(at: Int) =
        QueueItemRef(at, at, null, "file:///music/$at.mp3", null, "$at", "Artist", null, 10000)

    private fun defaultValue(method: Method): Any? =
        when (method.returnType) {
            java.lang.Boolean.TYPE -> false
            java.lang.Integer.TYPE -> 0
            java.lang.Long.TYPE -> 0L
            java.lang.Float.TYPE -> 0f
            java.lang.Double.TYPE -> 0.0
            else -> null
        }

    private inline fun <reified T> proxy(
        noinline call: (Method, Array<out Any?>?) -> Any? = { m, _ -> defaultValue(m) }
    ): T =
        Proxy.newProxyInstance(T::class.java.classLoader, arrayOf(T::class.java)) {
            instance,
            method,
            args ->
            when (method.name) {
                "hashCode" -> System.identityHashCode(instance)
                "equals" -> instance === args?.firstOrNull()
                "toString" -> T::class.java.simpleName
                else -> call(method, args)
            }
        } as T
}
