/*
 * Copyright (c) 2026 Auxio Project
 * StoragePathAliasPolicyTest.kt is part of Auxio.
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

package org.oxycblt.musikr.fs

import org.junit.Assert.assertEquals
import org.junit.Test

class StoragePathAliasPolicyTest {

    @Test
    fun testDeduplicatePaths() {
        val paths =
            listOf(
                "/mnt/media_rw/usbdisk0/Music/A.flac",
                "/storage/usbdisk0/Music/A.flac",
                "/sdcard/Music/B.flac",
                "/storage/emulated/0/Music/B.flac",
            )

        val deduped = StoragePathAliasPolicy.deduplicatePaths(paths)
        assertEquals(2, deduped.size)
    }

    @Test
    fun testDeduplicateFiles() {
        data class MockFile(val path: String, val size: Long, val modified: Long)

        val files =
            listOf(
                MockFile("/mnt/media_rw/usbdisk0/Music/A.flac", 100L, 1000L),
                MockFile("/storage/usbdisk0/Music/A.flac", 100L, 2000L),
                MockFile("/storage/usbdisk0/Music/A.flac", 200L, 2000L),
            )

        val deduped =
            StoragePathAliasPolicy.deduplicateFiles(
                files,
                { it.path },
                { it.size },
                { it.modified },
            )

        assertEquals(2, deduped.size)
    }
}
