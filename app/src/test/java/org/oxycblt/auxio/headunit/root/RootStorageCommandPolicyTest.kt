package org.oxycblt.auxio.headunit.root

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RootStorageCommandPolicyTest {
    @Test
    fun acceptsOnlyStorageBackingsAndPreparedAliases() {
        assertTrue(RootStorageCommandPolicy.isAllowedStorageRoot("/storage/usbdisk0/Music"))
        assertTrue(RootStorageCommandPolicy.isAllowedStorageRoot("/mnt/media_rw/usbdisk1"))
        assertTrue(RootStorageCommandPolicy.isAllowedStorageRoot("/storage/auxio-root/usbdisk1"))
        assertFalse(RootStorageCommandPolicy.isAllowedStorageRoot("/data/local/tmp"))
        assertFalse(RootStorageCommandPolicy.isAllowedStorageRoot("/storage/usbdisk0/../data"))
    }

    @Test
    fun commandIsOneBoundedRecursiveSnapshot() {
        val command =
            RootStorageCommandPolicy.buildSnapshotCommand(
                "/mnt/media_rw/usbdisk0/Music's",
                maxDepth = 8,
            )
        assertTrue(command.contains("find \"\$root\" -xdev"))
        assertTrue(command.contains("-maxdepth 8"))
        assertTrue(command.contains("Music'\"'\"'s"))
        assertFalse(command.contains("pm disable-user"))
    }
}
