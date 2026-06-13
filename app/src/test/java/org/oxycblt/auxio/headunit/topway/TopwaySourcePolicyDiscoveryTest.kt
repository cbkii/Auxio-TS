package org.oxycblt.auxio.headunit.topway

import java.io.File
import java.nio.file.Files
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TopwaySourcePolicyDiscoveryTest {
    @Test fun fixedUsbDiskZeroIsOnlyASeedNotTheDiscoveryLimit() {
        assertTrue(TopwaySourcePolicy.TS18_USB_CANDIDATES.contains("/storage/usbdisk0"))
        assertFalse(TopwaySourcePolicy.TS18_USB_CANDIDATES.contains("/storage/usbdisk1"))
    }

    @Test fun discoversMultipleReadableUsbDisksWithoutFixedMaximum() {
        val storage = Files.createTempDirectory("storage").toFile()
        val mediaRw = Files.createTempDirectory("media-rw").toFile()
        File(storage, "usbdisk2").mkdir()
        File(storage, "usbdisk1").mkdir()
        File(storage, "emulated").mkdir()
        File(storage, "self").mkdir()
        File(mediaRw, "usbdisk3").mkdir()
        File(mediaRw, "not-usb").mkdir()

        val roots =
            TopwaySourcePolicy.discoverCandidateRoots(
                storageRoot = storage,
                mediaRwRoot = mediaRw,
            )

        assertEquals(
            listOf(
                File(storage, "usbdisk1").absolutePath,
                File(storage, "usbdisk2").absolutePath,
                File(mediaRw, "usbdisk3").absolutePath,
            ),
            roots,
        )
    }

    @Test fun systemSourceFilterStillAcceptsUsbDiskOneMusicPaths() {
        assertTrue(TopwaySourcePolicy.matchesSystemSourceFilter("/storage/usbdisk1/My Music/song.flac"))
    }
}
