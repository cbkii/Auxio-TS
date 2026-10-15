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
import org.junit.Assert.assertTrue
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
    fun ordinaryReadableRootDoesNotRequireShallowRepresentativeAudio() {
        val root = Files.createTempDirectory("auxio-source-deep").toFile()
        try {
            var directory = root
            repeat(6) { depth -> directory = directory.resolve("d$depth").apply { mkdirs() } }
            directory.resolve("deep.flac").writeBytes(byteArrayOf(7))

            assertEquals(
                SourceAuthority.APP_READABLE,
                SourceAuthorityValidator.classifyDirect(root.absolutePath, preparedAlias = false),
            )
            assertNull(
                SourceAuthorityValidator.classifyDirect(root.absolutePath, preparedAlias = true)
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
            assertEquals(
                SourceAuthority.PREPARED_ALIAS,
                SourceAuthorityValidator.classifyDirect(
                    path = root.absolutePath,
                    preparedAlias = true,
                    representativePath = media.absolutePath,
                ),
            )
        } finally {
            root.deleteRecursively()
        }
    }

    @Test
    fun escapedPreparedRepresentativeHintIsRejected() {
        val root = Files.createTempDirectory("auxio-source-contained").toFile()
        val outside = Files.createTempFile("auxio-source-outside", ".flac").toFile()
        try {
            outside.writeBytes(byteArrayOf(1))
            assertNull(
                SourceAuthorityValidator.classifyDirect(
                    path = root.absolutePath,
                    preparedAlias = true,
                    representativePath = outside.absolutePath,
                )
            )
        } finally {
            root.deleteRecursively()
            outside.delete()
        }
    }

    @Test
    fun emptyOrdinaryDirectoryIsValidButEmptyPreparedAliasIsNot() {
        val root = Files.createTempDirectory("auxio-source-empty").toFile()
        try {
            root.resolve("notes.txt").writeText("not audio")
            assertEquals(
                SourceAuthority.APP_READABLE,
                SourceAuthorityValidator.classifyDirect(root.absolutePath, preparedAlias = false),
            )
            assertNull(
                SourceAuthorityValidator.classifyDirect(root.absolutePath, preparedAlias = true)
            )
        } finally {
            root.deleteRecursively()
        }
    }

    @Test
    fun preparedAliasContinuesPastUnreadableAudioCandidates() {
        val root = Files.createTempDirectory("auxio-source-mixed-access").toFile()
        try {
            root.resolve("blocked.flac").writeBytes(byteArrayOf(1))
            root.resolve("playable.mp3").writeBytes(byteArrayOf(2))
            val inspected = mutableSetOf<String>()
            val authority =
                SourceAuthorityValidator.classifyDirectWithOpener(
                    path = root.absolutePath,
                    preparedAlias = true,
                    canOpen = { file ->
                        inspected += file.name
                        file.name == "playable.mp3"
                    },
                )
            assertEquals(SourceAuthority.PREPARED_ALIAS, authority)
            assertTrue(inspected.contains("playable.mp3"))
        } finally {
            root.deleteRecursively()
        }
    }

    @Test
    fun preparedAliasRejectsWhenEveryAudioCandidateIsUnreadable() {
        val root = Files.createTempDirectory("auxio-source-all-blocked").toFile()
        try {
            root.resolve("one.flac").writeBytes(byteArrayOf(1))
            root.resolve("two.mp3").writeBytes(byteArrayOf(2))
            assertNull(
                SourceAuthorityValidator.classifyDirectWithOpener(
                    path = root.absolutePath,
                    preparedAlias = true,
                    canOpen = { false },
                )
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
