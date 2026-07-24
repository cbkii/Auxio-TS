/*
 * Copyright (c) 2026 Auxio Project
 * SourceAuthorityValidatorTest.kt is part of Auxio.
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

package org.oxycblt.auxio.headunit.root.storage

import java.nio.file.Files
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SourceAuthorityValidatorTest {
    @Test
    fun classifiesReadableRepresentativeAudioInCurrentProcess() {
        val root = Files.createTempDirectory("auxio-source-authority").toFile()
        try {
            val music = root.resolve("Music").apply { mkdirs() }
            music.resolve("track.flac").writeBytes(byteArrayOf(1, 2, 3))
            assertEquals(
                SourceAuthority.APP_READABLE,
                SourceAuthorityValidator.classifyDirect(root.absolutePath, preparedAlias = false),
            )
            assertEquals(
                SourceAuthority.PREPARED_ALIAS,
                SourceAuthorityValidator.classifyDirect(root.absolutePath, preparedAlias = true),
            )
        } finally {
            root.deleteRecursively()
        }
    }

    @Test
    fun preparedRepresentativeBypassesBoundedWalkWithoutBypassingProcessOpen() {
        val root = Files.createTempDirectory("auxio-source-hint").toFile()
        try {
            var directory = root
            repeat(6) { depth -> directory = directory.resolve("d$depth").apply { mkdirs() } }
            val media = directory.resolve("deep.flac").apply { writeBytes(byteArrayOf(7)) }
            assertNull(
                SourceAuthorityValidator.classifyDirect(root.absolutePath, preparedAlias = false)
            )
            assertEquals(
                SourceAuthority.APP_READABLE,
                SourceAuthorityValidator.classifyDirect(
                    path = root.absolutePath,
                    preparedAlias = false,
                    representativePath = media.absolutePath,
                ),
            )
        } finally {
            root.deleteRecursively()
        }
    }

    @Test
    fun escapedRepresentativeHintIsRejected() {
        val root = Files.createTempDirectory("auxio-source-contained").toFile()
        val outside = Files.createTempFile("auxio-source-outside", ".flac").toFile()
        try {
            outside.writeBytes(byteArrayOf(1))
            assertNull(
                SourceAuthorityValidator.classifyDirect(
                    path = root.absolutePath,
                    preparedAlias = false,
                    representativePath = outside.absolutePath,
                )
            )
        } finally {
            root.deleteRecursively()
            outside.delete()
        }
    }

    @Test
    fun rejectsDirectoryWithoutRepresentativeAudio() {
        val root = Files.createTempDirectory("auxio-source-empty").toFile()
        try {
            root.resolve("notes.txt").writeText("not audio")
            assertNull(
                SourceAuthorityValidator.classifyDirect(root.absolutePath, preparedAlias = false)
            )
        } finally {
            root.deleteRecursively()
        }
    }

    @Test
    fun rejectsTestOwnedMissingDirectory() {
        val parent = Files.createTempDirectory("auxio-source-missing").toFile()
        try {
            val missing = parent.resolve("not-created")
            assertNull(
                SourceAuthorityValidator.classifyDirect(missing.absolutePath, preparedAlias = false)
            )
        } finally {
            parent.deleteRecursively()
        }
    }
}
