/*
 * Copyright (c) 2026 Auxio Project
 * ImmediateLaneArchitectureTest.kt is part of Auxio.
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

package org.oxycblt.auxio.music

import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.readText
import kotlin.test.Test
import kotlin.test.assertFalse

class ImmediateLaneArchitectureTest {
    private val root =
        Path.of(System.getProperty("user.dir")).let { cwd ->
            if (Files.exists(cwd.resolve("settings.gradle"))) cwd else cwd.parent
        }

    @Test
    fun `fast interaction consumers never materialise the complete graph`() {
        val paths =
            listOf(
                "app/src/main/java/org/oxycblt/auxio/home/HomeViewModel.kt",
                "app/src/main/java/org/oxycblt/auxio/search/SearchViewModel.kt",
                "app/src/main/java/org/oxycblt/auxio/music/service/MusicBrowser.kt",
                "app/src/main/java/org/oxycblt/auxio/headunit/ts18/FastStartDirectFolderBrowser.kt",
            )
        val forbidden =
            listOf(
                "DBCache.snapshot",
                "Musikr.loadCached",
                "MusicGraph",
                "LibraryFactory",
                "selectAllSongs",
            )
        for (path in paths) {
            val source = root.resolve(path).readText()
            forbidden.forEach { symbol ->
                assertFalse(source.contains(symbol), "$path uses $symbol")
            }
        }
    }
}
