package org.oxycblt.auxio.headunit.ts18

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

class FastStartDirectFolderBrowserTest {
    @Test
    fun `rejects paths outside app facing usb roots`() = runBlocking {
        val browser = FastStartDirectFolderBrowser()
        assertTrue(browser.browse("/mnt/media_rw/usbdisk0").isEmpty())
        assertTrue(browser.browse("/sdcard/Music").isEmpty())
    }
}
