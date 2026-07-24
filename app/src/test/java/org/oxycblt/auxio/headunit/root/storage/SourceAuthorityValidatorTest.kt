package org.oxycblt.auxio.headunit.root.storage

import java.nio.file.Files
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SourceAuthorityValidatorTest {
    @Test
    fun opensRepresentativeAudioAsAppUid() {
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
                SourceAuthorityValidator.classifyDirect(
                    missing.absolutePath,
                    preparedAlias = false,
                )
            )
        } finally {
            parent.deleteRecursively()
        }
    }
}
