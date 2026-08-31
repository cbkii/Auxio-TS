/*
 * Copyright (c) 2026 Auxio Project
 * PrimitiveQueuePromotionPolicyTest.kt is part of Auxio.
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

package org.oxycblt.auxio.playback.service

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.oxycblt.auxio.playback.persist.QueueDescriptor
import org.oxycblt.auxio.playback.persist.QueueItemRef
import org.oxycblt.auxio.playback.state.RepeatMode
import org.oxycblt.auxio.playback.state.ShuffleScope

class PrimitiveQueuePromotionPolicyTest {
    @Test
    fun unshuffledLogicalOrderBecomesCanonicalHeap() {
        val layout =
            PrimitiveQueuePromotionPolicy.layout(
                descriptor(shuffleScope = ShuffleScope.OFF),
                listOf(item(0, 0), item(1, 1), item(2, 2)),
            )

        requireNotNull(layout)
        assertEquals(listOf(0, 1, 2), layout.itemsByCanonicalPosition.map { it.canonicalPosition })
        assertEquals(emptyList(), layout.shuffledMapping)
        assertEquals(1, layout.heapIndexForLogicalPosition(1))
    }

    @Test
    fun unshuffledLogicalQueueEditsAreNotUndoneDuringPromotion() {
        val layout =
            PrimitiveQueuePromotionPolicy.layout(
                descriptor(shuffleScope = ShuffleScope.OFF),
                listOf(item(0, 2), item(1, 0), item(2, 1)),
            )

        requireNotNull(layout)
        assertEquals(listOf(2, 0, 1), layout.itemsByCanonicalPosition.map { it.canonicalPosition })
        assertEquals(emptyList(), layout.shuffledMapping)
        assertEquals(1, layout.heapIndexForLogicalPosition(1))
    }

    @Test
    fun shuffledLogicalOrderReconstructsCanonicalHeapAndMapping() {
        val layout =
            PrimitiveQueuePromotionPolicy.layout(
                descriptor(shuffleScope = ShuffleScope.ALL),
                listOf(item(0, 2), item(1, 0), item(2, 1)),
            )

        requireNotNull(layout)
        assertEquals(listOf(0, 1, 2), layout.itemsByCanonicalPosition.map { it.canonicalPosition })
        assertEquals(listOf(2, 0, 1), layout.shuffledMapping)
        assertEquals(0, layout.heapIndexForLogicalPosition(1))
    }

    @Test
    fun genreShufflePreservesPersistedShuffleMapping() {
        val layout =
            PrimitiveQueuePromotionPolicy.layout(
                descriptor(shuffleScope = ShuffleScope.GENRE),
                listOf(item(0, 1), item(1, 2), item(2, 0)),
            )

        requireNotNull(layout)
        assertEquals(listOf(1, 2, 0), layout.shuffledMapping)
    }

    @Test
    fun incompleteOrAmbiguousQueueFailsClosed() {
        assertNull(
            PrimitiveQueuePromotionPolicy.layout(
                descriptor(shuffleScope = ShuffleScope.OFF),
                listOf(item(0, 0), item(1, 1)),
            )
        )
        assertNull(
            PrimitiveQueuePromotionPolicy.layout(
                descriptor(shuffleScope = ShuffleScope.ALL),
                listOf(item(0, 0), item(1, 0), item(2, 2)),
            )
        )
    }

    @Test
    fun unshuffledHydrationDropsMissingNonCurrentItemsAndRemapsCurrent() {
        val layout =
            requireNotNull(
                PrimitiveQueuePromotionPolicy.layout(
                    descriptor(shuffleScope = ShuffleScope.OFF),
                    listOf(item(0, 0), item(1, 1), item(2, 2)),
                )
            )

        val hydrated =
            requireNotNull(
                PrimitiveQueuePromotionPolicy.hydratedLayout(
                    layout = layout,
                    currentLogicalPosition = 1,
                    resolvedHeapIndices = setOf(1, 2),
                )
            )

        assertEquals(listOf(1, 2), hydrated.keptHeapIndices)
        assertEquals(emptyList(), hydrated.shuffledMapping)
        assertEquals(0, hydrated.currentHeapIndex)
        assertEquals(1, hydrated.droppedCount)
    }

    @Test
    fun shuffledHydrationCompactsPersistedMappingAroundCurrentSong() {
        val layout =
            requireNotNull(
                PrimitiveQueuePromotionPolicy.layout(
                    descriptor(shuffleScope = ShuffleScope.ALL),
                    listOf(item(0, 2), item(1, 0), item(2, 1)),
                )
            )

        val hydrated =
            requireNotNull(
                PrimitiveQueuePromotionPolicy.hydratedLayout(
                    layout = layout,
                    currentLogicalPosition = 1,
                    resolvedHeapIndices = setOf(0, 2),
                )
            )

        assertEquals(listOf(0, 2), hydrated.keptHeapIndices)
        assertEquals(listOf(1, 0), hydrated.shuffledMapping)
        assertEquals(0, hydrated.currentHeapIndex)
        assertEquals(1, hydrated.droppedCount)
    }

    @Test
    fun missingCurrentSongFailsOpenInsteadOfJumpingToAnotherItem() {
        val layout =
            requireNotNull(
                PrimitiveQueuePromotionPolicy.layout(
                    descriptor(shuffleScope = ShuffleScope.ALL),
                    listOf(item(0, 2), item(1, 0), item(2, 1)),
                )
            )

        assertNull(
            PrimitiveQueuePromotionPolicy.hydratedLayout(
                layout = layout,
                currentLogicalPosition = 1,
                resolvedHeapIndices = setOf(1, 2),
            )
        )
    }

    private fun descriptor(shuffleScope: ShuffleScope) =
        QueueDescriptor(
            sessionId = 11L,
            totalCount = 3,
            currentLogicalPosition = 1,
            positionMs = 12_345L,
            repeatMode = RepeatMode.ALL,
            shuffleScope = shuffleScope,
            revision = 4L,
            updatedAtMs = 5L,
        )

    private fun item(logical: Int, canonical: Int) =
        QueueItemRef(
            logicalPosition = logical,
            canonicalPosition = canonical,
            stableSongUid = null,
            uri = "file:///storage/usbdisk0/$canonical.flac",
            pathFallback = "/storage/usbdisk0/$canonical.flac",
            titleFallback = "Song $canonical",
            artistFallback = "Artist",
            albumFallback = "Album",
            durationMs = 180_000L,
        )
}
