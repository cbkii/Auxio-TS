/*
 * Copyright (c) 2026 Auxio Project
 * QueueAuthorityPolicyTest.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.oxycblt.auxio.playback.queue

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.oxycblt.auxio.playback.persist.QueueDescriptor
import org.oxycblt.auxio.playback.persist.QueueItemRef
import org.oxycblt.auxio.playback.state.RepeatMode
import org.oxycblt.auxio.playback.state.ShuffleScope

class QueueAuthorityPolicyTest {
    @Test
    fun acceptsCurrentPrimitiveGenerationAndDescriptor() {
        val descriptor = descriptor(sessionId = 7, revision = 11)
        val request = QueueAuthorityPolicy.request(generation = 3, descriptor)

        assertTrue(
            QueueAuthorityPolicy.accepts(
                request,
                currentGeneration = 3,
                activeDescriptor = descriptor,
                resultDescriptor = descriptor,
                resultItems = listOf(item("file:///storage/emulated/0/Music/a.mp3")),
            )
        )
    }

    @Test
    fun rejectsResultAfterNewRichQueueChangesGeneration() {
        val descriptor = descriptor(sessionId = 7, revision = 11)
        val request = QueueAuthorityPolicy.request(generation = 3, descriptor)

        assertFalse(
            QueueAuthorityPolicy.accepts(
                request,
                currentGeneration = 4,
                activeDescriptor = descriptor,
                resultDescriptor = descriptor,
                resultItems = listOf(item("file:///storage/emulated/0/Music/a.mp3")),
            )
        )
    }

    @Test
    fun rejectsSessionOrRevisionReplacement() {
        val requested = descriptor(sessionId = 7, revision = 11)
        val request = QueueAuthorityPolicy.request(generation = 3, requested)

        assertFalse(
            QueueAuthorityPolicy.accepts(
                request,
                currentGeneration = 3,
                activeDescriptor = descriptor(sessionId = 8, revision = 11),
                resultDescriptor = requested,
                resultItems = listOf(item("file:///storage/emulated/0/Music/a.mp3")),
            )
        )
        assertFalse(
            QueueAuthorityPolicy.accepts(
                request,
                currentGeneration = 3,
                activeDescriptor = requested,
                resultDescriptor = descriptor(sessionId = 7, revision = 12),
                resultItems = listOf(item("file:///storage/emulated/0/Music/a.mp3")),
            )
        )
    }

    @Test
    fun rejectsSyntheticMissingRows() {
        val descriptor = descriptor(sessionId = 7, revision = 11)
        val request = QueueAuthorityPolicy.request(generation = 3, descriptor)
        val missing = item(null)

        assertFalse(
            QueueAuthorityPolicy.accepts(
                request,
                currentGeneration = 3,
                activeDescriptor = descriptor,
                resultDescriptor = descriptor,
                resultItems = listOf(missing),
            )
        )
        assertTrue(QueueAuthorityPolicy.hasMissingRows(listOf(missing)))
    }

    private fun descriptor(sessionId: Long, revision: Long) =
        QueueDescriptor(
            sessionId = sessionId,
            totalCount = 1,
            currentLogicalPosition = 0,
            positionMs = 0,
            repeatMode = RepeatMode.NONE,
            shuffleScope = ShuffleScope.OFF,
            revision = revision,
            updatedAtMs = 0,
        )

    private fun item(uri: String?) =
        QueueItemRef(
            logicalPosition = 0,
            canonicalPosition = 0,
            stableSongUid = null,
            uri = uri,
            pathFallback = null,
            titleFallback = null,
            artistFallback = null,
            albumFallback = null,
            durationMs = 0,
        )
}
