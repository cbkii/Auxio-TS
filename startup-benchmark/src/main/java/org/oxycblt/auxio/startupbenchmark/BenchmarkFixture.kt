/*
 * Copyright (c) 2026 Auxio Project
 * BenchmarkFixture.kt is part of Auxio.
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

import java.security.MessageDigest
import java.util.Locale

/** Deterministic logical fixtures used by startup and first-minute benchmarks. */
internal object BenchmarkFixtures {
    const val SCHEMA_VERSION = 1
    const val SEED = 18_022_026L
    val supportedSongCounts = setOf(500, 5_000, 20_000)
    val sourceKeys = listOf("direct:usb0", "direct:usb1")

    data class Spec(
        val songCount: Int,
        val sourceCount: Int,
        val albumCount: Int,
        val artistCount: Int,
        val genreCount: Int,
        val folderCount: Int,
        val playlistCount: Int,
        val seed: Long = SEED,
        val schemaVersion: Int = SCHEMA_VERSION,
    )

    data class Song(
        val stableUid: String,
        val sourceKey: String,
        val uri: String,
        val displayPath: String,
        val title: String,
        val artist: String,
        val album: String,
        val genre: String,
        val durationMs: Long,
        val dateAddedMs: Long,
    )

    fun spec(songCount: Int): Spec {
        require(songCount in supportedSongCounts) { "Unsupported fixture size: $songCount" }
        return Spec(
            songCount = songCount,
            sourceCount = sourceKeys.size,
            albumCount = maxOf(1, songCount / 10),
            artistCount = maxOf(1, songCount / 25),
            genreCount = 20,
            folderCount = maxOf(2, songCount / 50),
            playlistCount = 12,
        )
    }

    fun songs(songCount: Int): Sequence<Song> {
        val fixture = spec(songCount)
        return (0 until fixture.songCount).asSequence().map { index -> song(index, fixture) }
    }

    fun song(index: Int, fixture: Spec): Song {
        require(index in 0 until fixture.songCount)
        val sourceIndex = index % fixture.sourceCount
        val sourceKey = sourceKeys[sourceIndex]
        val volumePath = "/storage/usbdisk$sourceIndex"
        val folderIndex = index % fixture.folderCount
        val albumIndex = index % fixture.albumCount
        val artistIndex = index % fixture.artistCount
        val genreIndex = index % fixture.genreCount
        val fileName = "track-${index.toString().padStart(5, '0')}.flac"
        val displayPath = "$volumePath/Music/folder-$folderIndex/$fileName"
        return Song(
            stableUid = "fixture-${fixture.seed}-$index",
            sourceKey = sourceKey,
            uri = "file://$displayPath",
            displayPath = displayPath,
            title = "Fixture Track ${index.toString().padStart(5, '0')}",
            artist = "Fixture Artist $artistIndex",
            album = "Fixture Album $albumIndex",
            genre = "Fixture Genre $genreIndex",
            durationMs = 120_000L + ((index * 997L) % 240_000L),
            dateAddedMs = 1_700_000_000_000L + index * 1_000L,
        )
    }

    fun checksum(songCount: Int): String {
        val digest = MessageDigest.getInstance("SHA-256")
        songs(songCount).forEach { song ->
            val row =
                listOf(
                        song.stableUid,
                        song.sourceKey,
                        song.uri,
                        song.title,
                        song.artist,
                        song.album,
                        song.genre,
                        song.durationMs.toString(),
                        song.dateAddedMs.toString(),
                    )
                    .joinToString("\u001f")
            digest.update(row.toByteArray(Charsets.UTF_8))
            digest.update('\n'.code.toByte())
        }
        return digest.digest().joinToString("") { byte ->
            String.format(Locale.ROOT, "%02x", byte.toInt() and 0xff)
        }
    }
}
