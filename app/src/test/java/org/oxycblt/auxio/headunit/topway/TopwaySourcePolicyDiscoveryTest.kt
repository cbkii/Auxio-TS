/*
 * Copyright (c) 2026 Auxio Project
 * TopwaySourcePolicyDiscoveryTest.kt is part of Auxio.
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

package org.oxycblt.auxio.headunit.topway

import java.io.File
import java.nio.file.Files
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TopwaySourcePolicyDiscoveryTest {
    @Test
    fun fixedUsbDiskZeroIsOnlyAnExampleSeedNotTheDiscoveryLimit() {
        @Suppress("DEPRECATION")
        assertTrue(TopwaySourcePolicy.TS18_USB_CANDIDATES.contains("/storage/usbdisk0"))
        assertTrue(TopwaySourcePolicy.TS18_USB_EXAMPLE_CANDIDATES.contains("/storage/usbdisk0"))
        assertFalse(TopwaySourcePolicy.TS18_USB_EXAMPLE_CANDIDATES.contains("/storage/usbdisk1"))
    }

    @Test
    fun discoversMultipleReadableUsbDisksWithoutFixedMaximum() {
        val tempRoot = Files.createTempDirectory("topway-source-policy").toFile()
        try {
            val storage = File(tempRoot, "storage")
            val mediaRw = File(tempRoot, "media-rw")
            assertTrue(storage.mkdir())
            assertTrue(mediaRw.mkdir())

            File(storage, "usbdisk0").mkdir()
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
                    File(storage, "usbdisk0").absolutePath,
                    File(storage, "usbdisk1").absolutePath,
                    File(storage, "usbdisk2").absolutePath,
                    File(mediaRw, "usbdisk3").absolutePath,
                ),
                roots,
            )
        } finally {
            tempRoot.deleteRecursively()
        }
    }

    @Test
    fun allowsSharedStorageAndUsbSourceCandidatesButRejectsProtectedPaths() {
        assertTrue(TopwaySourcePolicy.isAllowedSourceCandidate("/storage/emulated/0"))
        assertTrue(TopwaySourcePolicy.isAllowedSourceCandidate("/storage/emulated/0/Music"))
        assertTrue(TopwaySourcePolicy.isAllowedSourceCandidate("/sdcard"))
        assertTrue(TopwaySourcePolicy.isAllowedSourceCandidate("/sdcard/Music"))
        assertTrue(TopwaySourcePolicy.isAllowedSourceCandidate("/storage/usbdisk1/Music"))
        assertTrue(TopwaySourcePolicy.isAllowedSourceCandidate("/mnt/media_rw/usbdisk2/Music"))

        assertFalse(TopwaySourcePolicy.isAllowedSourceCandidate("/"))
        assertFalse(TopwaySourcePolicy.isAllowedSourceCandidate("/system"))
        assertFalse(TopwaySourcePolicy.isAllowedSourceCandidate("/vendor"))
        assertFalse(TopwaySourcePolicy.isAllowedSourceCandidate("/data"))
        assertFalse(TopwaySourcePolicy.isAllowedSourceCandidate("/proc"))
        assertFalse(TopwaySourcePolicy.isAllowedSourceCandidate("/sys"))
        assertFalse(TopwaySourcePolicy.isAllowedSourceCandidate("/dev"))
        assertFalse(TopwaySourcePolicy.isAllowedSourceCandidate("/storage/emulated/0/../data"))
    }

    @Test
    fun systemSourceFilterStillAcceptsUsbDiskOneMusicPaths() {
        assertTrue(
            TopwaySourcePolicy.matchesSystemSourceFilter("/storage/usbdisk1/My Music/song.flac")
        )
    }
}
