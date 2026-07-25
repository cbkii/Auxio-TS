/*
 * Copyright (c) 2026 Auxio Project
 * PreparedVolumeManifestCodecTest.kt is part of Auxio.
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
