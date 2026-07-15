package org.oxycblt.auxio.headunit.ts18

import java.io.File
import java.util.Locale
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FastStartDirectFolderBrowserTest {
    private val originalLocale = Locale.getDefault()

    @After
    fun restoreLocale() {
        Locale.setDefault(originalLocale)
    }

    @Test
    fun `rejects traversal raw mounts and sibling prefixes`() = runBlocking {
        val root = createTempDir(prefix = "usb0")
        val browser = browser(root)

        assertNull(browser.resolveCandidate("/storage/usbdisk0/../.."))
        assertNull(browser.resolveCandidate("/storage/usbdisk0/../../etc"))
        assertNull(browser.resolveCandidate("/storage/usbdisk01/Music"))
        assertNull(browser.resolveCandidate("/storage/usbdisk0\\..\\.."))
        assertNull(browser.resolveCandidate("/mnt/media_rw/usbdisk0/Music"))
    }

    @Test
    fun `accepts valid nested directories and reconstructs app path`() = runBlocking {
        val root = createTempDir(prefix = "usb0")
        File(root, "Music/Rock").mkdirs()
        val browser = browser(root)

        val candidate = browser.resolveCandidate("/storage/usbdisk0/Music/Rock")
        assertEquals("/storage/usbdisk0/Music/Rock", candidate?.appPath)
    }

    @Test
    fun `rejects symlink escape when platform supports links`() = runBlocking {
        val root = createTempDir(prefix = "usb0")
        val outside = createTempDir(prefix = "outside")
        val link = File(root, "escape")
        try {
            java.nio.file.Files.createSymbolicLink(link.toPath(), outside.toPath())
        } catch (_: UnsupportedOperationException) {
            return@runBlocking
        } catch (_: SecurityException) {
            return@runBlocking
        }
        val browser = browser(root)

        assertNull(browser.resolveCandidate("/storage/usbdisk0/escape"))
    }

    @Test
    fun `browse is bounded folder first and locale stable`() = runBlocking {
        Locale.setDefault(Locale("tr", "TR"))
        val root = createTempDir(prefix = "usb0")
        File(root, "İndir").mkdirs()
        File(root, "album.mp3").writeText("x")
        File(root, "Beta.flac").writeText("x")
        val browser = browser(root)

        val page = browser.browse("/storage/usbdisk0", limit = 2)

        assertEquals(2, page.entries.size)
        assertTrue(page.entries.first().directory)
        assertTrue(page.truncated)
        assertFalse(page.entries.any { it.path.startsWith("/mnt/media_rw") })
    }

    private fun browser(root: File) =
        FastStartDirectFolderBrowser(mapOf("/storage/usbdisk0" to root.canonicalFile))
}
