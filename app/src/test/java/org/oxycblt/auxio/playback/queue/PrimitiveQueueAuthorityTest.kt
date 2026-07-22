/*
 * Copyright (c) 2026 Auxio Project
 * PrimitiveQueueAuthorityTest.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.oxycblt.auxio.playback.queue

import java.lang.reflect.Proxy
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue
import org.oxycblt.auxio.playback.persist.QueueDescriptor
import org.oxycblt.auxio.playback.persist.QueueItemRef
import org.oxycblt.auxio.playback.state.RepeatMode
import org.oxycblt.auxio.playback.state.ShuffleScope
import org.oxycblt.musikr.Song

class PrimitiveQueueAuthorityTest {
    @Test
    fun staleGenerationCannotOverwriteNewerRichQueue() {
        val descriptor = descriptor(sessionId = 7, revision = 4)
        val token = PrimitiveQueueAuthority.token(generation = 10, descriptor)

        assertFalse(
            PrimitiveQueueAuthority.accepts(
                token,
                currentGeneration = 11,
                activeDescriptor = descriptor,
                returnedDescriptor = descriptor,
            )
        )
    }

    @Test
    fun staleSessionOrRevisionIsRejected() {
        val descriptor = descriptor(sessionId = 7, revision = 4)
        val token = PrimitiveQueueAuthority.token(generation = 10, descriptor)

        assertFalse(
            PrimitiveQueueAuthority.accepts(
                token,
                currentGeneration = 10,
                activeDescriptor = descriptor(sessionId = 8, revision = 4),
                returnedDescriptor = descriptor,
            )
        )
        assertFalse(
            PrimitiveQueueAuthority.accepts(
                token,
                currentGeneration = 10,
                activeDescriptor = descriptor,
                returnedDescriptor = descriptor(sessionId = 7, revision = 5),
            )
        )
    }

    @Test
    fun currentPrimitiveRangeIsAccepted() {
        val descriptor = descriptor(sessionId = 7, revision = 4)
        val token = PrimitiveQueueAuthority.token(generation = 10, descriptor)

        assertTrue(
            PrimitiveQueueAuthority.accepts(
                token,
                currentGeneration = 10,
                activeDescriptor = descriptor,
                returnedDescriptor = descriptor,
            )
        )
    }

    @Test
    fun mergedPrimitiveRangesRemainOrderedAndBounded() {
        val current = (20..29).map(::displayItem)
        val incoming = (10..24).map(::displayItem)

        val merged =
            PrimitiveQueueAuthority.mergeBounded(
                current = current,
                incoming = incoming,
                anchorGlobalPosition = 20,
                maximumItems = 12,
            )

        assertEquals(12, merged.size)
        assertEquals(merged.map { it.globalPosition }.sorted(), merged.map { it.globalPosition })
        assertTrue(merged.any { it.globalPosition == 20 })
        assertEquals(merged.map { it.globalPosition }.distinct(), merged.map { it.globalPosition })
    }

    @Test
    fun unresolvedIncomingRowCannotReplaceExistingRichSong() {
        val richSong = fakeSong()
        val rich = QueueDisplayItem(globalPosition = 20, song = richSong, primitive = null)

        val merged =
            PrimitiveQueueAuthority.mergeBounded(
                current = listOf(rich),
                incoming = listOf(unresolvedItem(20)),
                anchorGlobalPosition = 20,
                maximumItems = 10,
            )

        assertSame(richSong, merged.single().song)
        assertTrue(merged.single().editable)
    }

    @Test
    fun unresolvedIncomingRowCannotReplacePlayablePrimitive() {
        val playable = displayItem(20)

        val merged =
            PrimitiveQueueAuthority.mergeBounded(
                current = listOf(playable),
                incoming = listOf(unresolvedItem(20)),
                anchorGlobalPosition = 20,
                maximumItems = 10,
            )

        assertEquals(playable, merged.single())
        assertTrue(merged.single().editable)
    }

    private fun descriptor(sessionId: Long, revision: Long) =
        QueueDescriptor(
            sessionId = sessionId,
            totalCount = 100,
            currentLogicalPosition = 20,
            positionMs = 0,
            repeatMode = RepeatMode.NONE,
            shuffleScope = ShuffleScope.OFF,
            revision = revision,
            updatedAtMs = 1,
        )

    private fun displayItem(position: Int) =
        QueueDisplayItem(
            globalPosition = position,
            song = null,
            primitive =
                QueueItemRef(
                    logicalPosition = position,
                    canonicalPosition = position,
                    stableSongUid = null,
                    uri = "file:///storage/emulated/0/Music/$position.mp3",
                    pathFallback = "/storage/emulated/0/Music/$position.mp3",
                    titleFallback = "Track $position",
                    artistFallback = "Artist",
                    albumFallback = null,
                    durationMs = 1_000,
                ),
        )

    private fun unresolvedItem(position: Int) =
        QueueDisplayItem(
            globalPosition = position,
            song = null,
            primitive =
                QueueItemRef(
                    logicalPosition = position,
                    canonicalPosition = position,
                    stableSongUid = null,
                    uri = null,
                    pathFallback = null,
                    titleFallback = "Unavailable",
                    artistFallback = null,
                    albumFallback = null,
                    durationMs = 0,
                ),
        )

    private fun fakeSong(): Song =
        Proxy.newProxyInstance(
            Song::class.java.classLoader,
            arrayOf(Song::class.java),
        ) { _, method, _ ->
            when (method.name) {
                "toString" -> "FakeSong"
                "hashCode" -> 1
                "equals" -> false
                else -> null
            }
        } as Song
}
