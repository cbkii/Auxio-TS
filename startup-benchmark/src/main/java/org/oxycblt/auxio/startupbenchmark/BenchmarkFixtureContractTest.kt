/*
 * Copyright (c) 2026 Auxio Project
 * BenchmarkFixtureContractTest.kt is part of Auxio.
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

import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.Test

/**
 * Verifies fixture scale, source separation and deterministic generation on the benchmark device.
 */
class BenchmarkFixtureContractTest {
    @Test
    fun fixtureScalesAreDeterministic() {
        val expected =
            mapOf(
                500 to "0bc9ccaa3e61215eb373b44679d6b3932a1ec4fd5d3dece760cf10c625c4a24b",
                5_000 to "08ddcc9c2a9144e81fbd33b83f7cc17ac515bfccceb3a0fbb446fd530f1c1208",
                20_000 to "b90250105193230ed17a2946a75e34cf494c698beb2dbfa60857c674861cee18",
            )

        expected.forEach { (songCount, checksum) ->
            val spec = BenchmarkFixtures.spec(songCount)
            assertEquals(songCount, spec.songCount)
            assertEquals(2, spec.sourceCount)
            assertEquals(checksum, BenchmarkFixtures.checksum(songCount))
        }
    }

    @Test
    fun fixturesRepresentIndependentUsbVolumes() {
        val songs = BenchmarkFixtures.songs(500).toList()
        assertEquals(500, songs.map { it.stableUid }.toSet().size)
        assertEquals(BenchmarkFixtures.sourceKeys.toSet(), songs.map { it.sourceKey }.toSet())
        assertTrue(songs.any { it.displayPath.startsWith("/storage/usbdisk0/") })
        assertTrue(songs.any { it.displayPath.startsWith("/storage/usbdisk1/") })
    }
}
