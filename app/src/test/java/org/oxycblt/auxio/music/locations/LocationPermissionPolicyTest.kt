/*
 * Copyright (c) 2026 Auxio Project
 * LocationPermissionPolicyTest.kt is part of Auxio.
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

package org.oxycblt.auxio.music.locations

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LocationPermissionPolicyTest {
    @Test
    fun onlyMediaStoreDisablesSourceUiWithoutPermission() {
        assertFalse(
            LocationPermissionPolicy.isSourceUiEnabled(
                LocationMode.MEDIA_STORE,
                hasStoragePermission = false,
            )
        )
        assertTrue(
            LocationPermissionPolicy.isSourceUiEnabled(
                LocationMode.SAF,
                hasStoragePermission = false,
            )
        )
        assertTrue(
            LocationPermissionPolicy.isSourceUiEnabled(
                LocationMode.DIRECT_FS,
                hasStoragePermission = false,
            )
        )
    }

    @Test
    fun internalSharedStorageRequiresAndroidStoragePermission() {
        assertTrue(
            LocationPermissionPolicy.requiresStoragePermission(
                LocationMode.DIRECT_FS,
                "/storage/emulated/0/My Audio",
            )
        )
        assertTrue(
            LocationPermissionPolicy.requiresStoragePermission(
                LocationMode.DIRECT_FS,
                "/sdcard/Recordings",
            )
        )
    }

    @Test
    fun appReadableUsbAndPreparedAliasesDoNotRequireBlanketPermission() {
        assertFalse(
            LocationPermissionPolicy.requiresStoragePermission(
                LocationMode.DIRECT_FS,
                "/storage/usbdisk0/Music",
            )
        )
        assertFalse(
            LocationPermissionPolicy.requiresStoragePermission(
                LocationMode.DIRECT_FS,
                "/storage/auxio-root/usbdisk1/Music",
            )
        )
        assertFalse(
            LocationPermissionPolicy.requiresStoragePermission(
                LocationMode.DIRECT_FS,
                "/storage/ABCD-1234/Music",
            )
        )
    }
}
