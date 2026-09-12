/*
 * Copyright (c) 2026 Auxio Project
 * ArtistComposerFallbackTest.kt is part of Auxio.
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

package org.oxycblt.musikr.tag.parse

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.oxycblt.musikr.metadata.Metadata
import org.oxycblt.musikr.metadata.Properties
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [30])
class ArtistComposerFallbackTest {
    private val parser = TagParser.new()

    @Test
    fun composerIdentityIsUsedAsOneFallbackSet() {
        val tags =
            parser.parse(
                metadata(
                    mapOf(
                        "TCOM" to listOf("Composer Artist"),
                        "TSOC" to listOf("Composer Artist Sort"),
                        "TXXX:MUSICBRAINZ COMPOSER ID" to listOf("composer-artist-mbid"),
                    )
                )
            )

        assertEquals(listOf("Composer Artist"), tags.artistNames)
        assertEquals(listOf("Composer Artist Sort"), tags.artistSortNames)
        assertEquals(listOf("composer-artist-mbid"), tags.artistMusicBrainzIds)
    }

    @Test
    fun composerFieldsDoNotHybridizeWithPresentArtistName() {
        val tags =
            parser.parse(
                metadata(
                    mapOf(
                        "TPE1" to listOf("Primary Artist"),
                        "TCOM" to listOf("Composer Artist"),
                        "TSOC" to listOf("Composer Artist Sort"),
                        "TXXX:MUSICBRAINZ COMPOSER ID" to listOf("composer-artist-mbid"),
                    )
                )
            )

        assertEquals(listOf("Primary Artist"), tags.artistNames)
        assertEquals(emptyList<String>(), tags.artistSortNames)
        assertEquals(emptyList<String>(), tags.artistMusicBrainzIds)
    }

    private fun metadata(tags: Map<String, List<String>>) =
        Metadata(
            id3v2 = tags,
            xiph = emptyMap(),
            mp4 = emptyMap(),
            cover = null,
            properties = Properties("audio/mpeg", 1_000, 320, 44_100),
        )
}
