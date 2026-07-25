/*
 * Copyright (c) 2026 Auxio Project
 * ConfiguredSourcePolicyPathTest.kt is part of Auxio.
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

package org.oxycblt.auxio.music

import android.net.Uri
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ConfiguredSourcePolicyPathTest {
    @Test
    fun recognisesTopwayPreparedAndUuidRemovableRoots() {
        assertTrue(ConfiguredSourcePolicy.isUsbUri(Uri.parse("file:///storage/usbdisk0/Music")))
        assertTrue(
            ConfiguredSourcePolicy.isUsbUri(
                Uri.parse("file:///storage/auxio-root/usbdisk2/Music")
            )
        )
        assertTrue(ConfiguredSourcePolicy.isUsbUri(Uri.parse("file:///storage/ABCD-1234/Music")))
        assertTrue(
            ConfiguredSourcePolicy.isUsbUri(
                Uri.parse("content://com.android.externalstorage.documents/tree/ABCD-1234%3AMusic")
            )
        )
        assertFalse(ConfiguredSourcePolicy.isUsbUri(Uri.parse("file:///storage/emulated/0/Music")))
    }

    @Test
    fun directRootsAreCanonicalAppFacingAndRejectUnsafePaths() {
        assertEquals(
            "/storage/usbdisk0/Music",
            ConfiguredSourcePolicy.normaliseConfiguredRoot("file:///mnt/media_rw/usbdisk0/Music")
                ?.absolutePath,
        )
        assertEquals(
            "/storage/emulated/0/Music",
            ConfiguredSourcePolicy.normaliseConfiguredRoot("file:///storage/emulated/0/Music")
                ?.absolutePath,
        )
        assertEquals(
            "/storage/emulated/0/Recordings",
            ConfiguredSourcePolicy.normaliseConfiguredRoot("file:///sdcard/Recordings")
                ?.absolutePath,
        )
        assertEquals(
            "/storage/auxio-root/usbdisk2/Music",
            ConfiguredSourcePolicy.normaliseConfiguredRoot(
                    "file:///storage/auxio-root/usbdisk2/Music"
                )
                ?.absolutePath,
        )
        assertNull(
            ConfiguredSourcePolicy.normaliseConfiguredRoot("/storage/usbdisk0/Music/../Private")
        )
        assertNull(ConfiguredSourcePolicy.normaliseConfiguredRoot("/data/local/tmp"))
    }
}
