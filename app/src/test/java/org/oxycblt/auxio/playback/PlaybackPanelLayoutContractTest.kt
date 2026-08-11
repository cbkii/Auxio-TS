/*
 * Copyright (c) 2026 Auxio Project
 * PlaybackPanelLayoutContractTest.kt is part of Auxio.
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

package org.oxycblt.auxio.playback

import java.io.File
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** Source contract for qualifier-specific Now Playing metadata edge spacing. */
class PlaybackPanelLayoutContractTest {
    @Test
    fun everyPlaybackPanelQualifierKeepsLogicalMediumMetadataInset() {
        val resDir = resolveAppResDir()
        assertTrue("Missing app resource directory: ${resDir.path}", resDir.isDirectory)

        val layoutFiles =
            resDir
                .listFiles()
                ?.asSequence()
                ?.filter { directory ->
                    directory.isDirectory &&
                        (directory.name == "layout" || directory.name.startsWith("layout-"))
                }
                ?.map { directory -> File(directory, PLAYBACK_PANEL_LAYOUT) }
                ?.filter(File::isFile)
                ?.sortedBy { it.parentFile?.name.orEmpty() }
                ?.toList()
                .orEmpty()
        assertTrue(
            "No playback-panel layout variants found under ${resDir.path}",
            layoutFiles.isNotEmpty(),
        )

        layoutFiles.forEach { file ->
            val relativePath = file.relativeTo(resDir).path
            val xml = file.readText()
            val marker = "android:id=\"@+id/playback_info_container\""
            val markerIndex = xml.indexOf(marker)
            assertTrue("Missing playback_info_container in $relativePath", markerIndex >= 0)
            val blockStart = xml.lastIndexOf('<', markerIndex)
            val blockEnd = xml.indexOf('>', markerIndex)
            assertTrue("Malformed playback_info_container in $relativePath", blockEnd > blockStart)
            val openingTag = xml.substring(blockStart, blockEnd + 1)

            assertTrue(
                "$relativePath must keep logical start metadata padding",
                "android:paddingStart=\"@dimen/spacing_medium\"" in openingTag,
            )
            assertTrue(
                "$relativePath must keep logical end metadata padding",
                "android:paddingEnd=\"@dimen/spacing_medium\"" in openingTag,
            )
            assertFalse(
                "$relativePath must not double-count the metadata inset as a start margin",
                "android:layout_marginStart=" in openingTag,
            )
            assertFalse(
                "$relativePath must not double-count the metadata inset as an end margin",
                "android:layout_marginEnd=" in openingTag,
            )
        }
    }

    private fun resolveAppResDir(): File {
        val workingDir = File(System.getProperty("user.dir").orEmpty())
        val moduleRelative = File(workingDir, "src/main/res")
        if (moduleRelative.isDirectory) return moduleRelative
        return File(workingDir, "app/src/main/res")
    }

    private companion object {
        const val PLAYBACK_PANEL_LAYOUT = "fragment_playback_panel.xml"
    }
}
