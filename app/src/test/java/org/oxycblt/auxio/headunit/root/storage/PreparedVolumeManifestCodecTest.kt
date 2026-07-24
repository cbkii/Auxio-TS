package org.oxycblt.auxio.headunit.root.storage

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PreparedVolumeManifestCodecTest {
    @Test
    fun parsesPreparedAliasRecord() {
        val parsed =
            PreparedVolumeManifestCodec.parse(
                "1\t123\tusbdisk0\t/mnt/media_rw/usbdisk0\t/storage/usbdisk0\t/storage/auxio-root/usbdisk0\t/storage/auxio-root/usbdisk0\talias_candidate\t/storage/auxio-root/usbdisk0/Music/a.flac\n"
            )
        requireNotNull(parsed)
        assertEquals("usbdisk0", parsed.single().volumeId)
        assertEquals("/storage/auxio-root/usbdisk0", parsed.single().selectedPath)
    }

    @Test
    fun rejectsMismatchedStateAndDuplicateVolumeIdentity() {
        assertNull(
            PreparedVolumeManifestCodec.parse(
                "1\t123\tusbdisk0\t/mnt/media_rw/usbdisk0\t/storage/usbdisk0\t/storage/auxio-root/usbdisk0\t/storage/auxio-root/usbdisk0\tapp_candidate\t-\n"
            )
        )
        val row =
            "1\t123\tusbdisk0\t/mnt/media_rw/usbdisk0\t/storage/usbdisk0\t/storage/auxio-root/usbdisk0\t-\traw_only\t-\n"
        assertNull(PreparedVolumeManifestCodec.parse(row + row))
    }

    @Test
    fun rejectsUnsafeOrEscapedPaths() {
        assertNull(
            PreparedVolumeManifestCodec.parse(
                "1\t123\tusbdisk0\t/data/local/tmp\t/storage/usbdisk0\t/storage/auxio-root/usbdisk0\t-\traw_only\t-\n"
            )
        )
        assertNull(
            PreparedVolumeManifestCodec.parse(
                "1\t123\tusbdisk0\t/mnt/media_rw/usbdisk0\t/storage/usbdisk0\t/storage/auxio-root/usbdisk0\t/storage/usbdisk0\tapp_candidate\t/data/escape.mp3\n"
            )
        )
    }
}
