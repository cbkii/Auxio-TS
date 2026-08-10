/*
 * Copyright (c) 2026 Auxio Project
 * ArtworkContentIdentityTest.kt is part of Auxio.
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

package org.oxycblt.auxio.list.recycler

import java.io.ByteArrayInputStream
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.oxycblt.musikr.covers.Cover
import org.oxycblt.musikr.covers.CoverCollection

class ArtworkContentIdentityTest {
    @Test
    fun `null to populated cover is a content change`() {
        assertFalse(ArtworkContentIdentity.sameCover(null, TestCover("mcs:new")))
    }

    @Test
    fun `same stable cover id is unchanged without opening artwork`() {
        assertTrue(
            ArtworkContentIdentity.sameCover(
                TestCover("mcs:stable", failIfOpened = true),
                TestCover("mcs:stable", failIfOpened = true),
            )
        )
    }

    @Test
    fun `parent cover collection notices enrichment`() {
        val lean = CoverCollection.from(emptyList())
        val enriched = CoverCollection.from(listOf(TestCover("mcs:album")))

        assertFalse(ArtworkContentIdentity.sameCoverCollection(lean, enriched))
    }

    @Test
    fun `parent cover collection compares stable ids only`() {
        val old = CoverCollection.from(listOf(TestCover("mcs:a", true), TestCover("mcs:b", true)))
        val same = CoverCollection.from(listOf(TestCover("mcs:b", true), TestCover("mcs:a", true)))

        assertTrue(ArtworkContentIdentity.sameCoverCollection(old, same))
    }

    private data class TestCover(
        override val id: String,
        private val failIfOpened: Boolean = false,
    ) : Cover {
        override suspend fun open() =
            if (failIfOpened) {
                error("Artwork identity comparison must not perform I/O")
            } else {
                ByteArrayInputStream(byteArrayOf(1))
            }
    }
}
