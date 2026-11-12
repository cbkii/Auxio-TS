/*
 * Copyright (c) 2026 Auxio Project
 * PlaybackStateManagerShuffleScopeTest.kt is part of Auxio.
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

package org.oxycblt.auxio.playback.state

import java.lang.reflect.Method
import java.lang.reflect.Proxy
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/** The real manager mirror against a holder double: shuffle scope must survive Fast Resume. */
@RunWith(RobolectricTestRunner::class)
class PlaybackStateManagerShuffleScopeTest {
    private var hint: ShuffleScope? = null
    private var rawQueue = RawQueue.nil()
    private var acceptReplace = true
    private lateinit var manager: PlaybackStateManagerImpl
    private val song: org.oxycblt.musikr.Song =
        Proxy.newProxyInstance(
            org.oxycblt.musikr.Song::class.java.classLoader,
            arrayOf(org.oxycblt.musikr.Song::class.java),
        ) { instance, method, args ->
            when (method.name) {
                "hashCode" -> System.identityHashCode(instance)
                "equals" -> instance === args?.firstOrNull()
                "toString" -> "SongTestProxy"
                else -> defaultValue(method)
            }
        } as org.oxycblt.musikr.Song

    private val holder: PlaybackStateHolder =
        Proxy.newProxyInstance(
            PlaybackStateHolder::class.java.classLoader,
            arrayOf(PlaybackStateHolder::class.java),
        ) { instance, method, args ->
            when (method.name) {
                "getShuffleScopeHint" -> hint
                "resolveQueue" -> rawQueue
                "getProgression" -> Progression.nil()
                "getRepeatMode" -> RepeatMode.NONE
                "replaceQueuePreservingCurrent" -> {
                    if (acceptReplace)
                        manager.ack(instance as PlaybackStateHolder, args!![2] as StateAck)
                    acceptReplace
                }
                "hashCode" -> System.identityHashCode(instance)
                "equals" -> instance === args?.firstOrNull()
                "toString" -> "HolderTestProxy"
                else -> defaultValue(method)
            }
        } as PlaybackStateHolder

    private fun newManager() =
        PlaybackStateManagerImpl().also {
            manager = it
            it.registerStateHolder(holder)
        }

    @Test
    fun rawFastResumeReportsThePersistedGenreScopeImmediately() {
        val manager = newManager()
        hint = ShuffleScope.GENRE

        manager.ack(holder, StateAck.NewPlayback)

        assertEquals(ShuffleScope.GENRE, manager.shuffleScope)
        assertTrue(manager.isShuffled)
    }

    @Test
    fun genreScopeSurvivesTheCanonicalHandoffAfterRawFastResume() {
        val manager = newManager()
        hint = ShuffleScope.GENRE
        manager.ack(holder, StateAck.NewPlayback)

        // Hydrated shuffled queue installed; the raw hint is gone.
        hint = null
        rawQueue = RawQueue(heap = listOf(song), shuffledMapping = listOf(0), heapIndex = 0)
        manager.ack(holder, StateAck.NewPlayback)

        assertEquals(ShuffleScope.GENRE, manager.shuffleScope)
        assertTrue(manager.isShuffled)
    }

    @Test
    fun rejectedQueueReplacementLeavesTheScopeUnchanged() {
        val manager = newManager()
        acceptReplace = false

        assertFalse(manager.replaceQueuePreservingCurrent(emptyList(), true, ShuffleScope.GENRE))
        assertEquals(ShuffleScope.OFF, manager.shuffleScope)
    }

    @Test
    fun acceptedQueueReplacementPublishesTheRequestedScope() {
        val manager = newManager()
        rawQueue = RawQueue(heap = listOf(song), shuffledMapping = listOf(0), heapIndex = 0)

        assertTrue(manager.replaceQueuePreservingCurrent(emptyList(), true, ShuffleScope.GENRE))
        assertEquals(ShuffleScope.GENRE, manager.shuffleScope)
    }

    private fun defaultValue(method: Method): Any? =
        when {
            method.returnType == java.lang.Boolean.TYPE -> false
            method.returnType == java.lang.Integer.TYPE -> 0
            method.returnType == java.lang.Long.TYPE -> 0L
            method.returnType == java.lang.Void.TYPE -> null
            else -> null
        }
}
