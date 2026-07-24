/*
 * Copyright (c) 2026 Auxio Project
 * RootStorageAccelerationPolicyTest.kt is part of Auxio.
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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RootStorageAccelerationPolicyTest {
    @Test
    fun cachedMetadataLeadsWithoutStartingRoot() {
        assertEquals(
            RootStorageResolutionOrder.CACHED_ROOT_METADATA_FIRST,
            RootStorageAccelerationPolicy.choose(
                requestedPath = "/storage/usbdisk0/Music",
                rootEnabled = true,
                rootAvailable = false,
                hasCachedRecord = true,
            ),
        )
    }

    @Test
    fun grantedRootLeadsOnlyForRawOrPreparedPaths() {
        listOf("/mnt/media_rw/usbdisk0/Music", "/storage/auxio-root/usbdisk0/Music").forEach { path
            ->
            assertEquals(
                RootStorageResolutionOrder.REFRESHED_ROOT_METADATA_FIRST,
                RootStorageAccelerationPolicy.choose(path, true, true, false),
            )
            assertTrue(RootStorageAccelerationPolicy.requiresRootPreparation(path))
        }
        assertEquals(
            RootStorageResolutionOrder.DIRECT_FIRST,
            RootStorageAccelerationPolicy.choose("/storage/usbdisk0/Music", true, true, false),
        )
    }

    @Test
    fun traversalAndProtectedPathsNeverGainRootAcceleration() {
        listOf("/storage/usbdisk0/../data", "/mnt/media_rw/usbdisk0/../../data", "/data/local/tmp")
            .forEach { path ->
                assertFalse(RootStorageAccelerationPolicy.isRemovablePath(path))
                assertFalse(RootStorageAccelerationPolicy.requiresRootPreparation(path))
                assertEquals(
                    RootStorageResolutionOrder.DIRECT_FIRST,
                    RootStorageAccelerationPolicy.choose(path, true, true, true),
                )
            }
    }

    @Test
    fun disabledRootAlwaysUsesDirectAuthority() {
        assertEquals(
            RootStorageResolutionOrder.DIRECT_FIRST,
            RootStorageAccelerationPolicy.choose(
                requestedPath = "/mnt/media_rw/usbdisk0",
                rootEnabled = false,
                rootAvailable = true,
                hasCachedRecord = true,
            ),
        )
    }

    @Test
    fun removableClassificationCoversTs18AndUuidVolumes() {
        assertTrue(RootStorageAccelerationPolicy.isRemovablePath("/storage/usbdisk1"))
        assertTrue(RootStorageAccelerationPolicy.isRemovablePath("/mnt/media_rw/usbdisk0"))
        assertTrue(RootStorageAccelerationPolicy.isRemovablePath("/storage/auxio-root/usbdisk2"))
        assertTrue(RootStorageAccelerationPolicy.isRemovablePath("/storage/12AB-34CD/Music"))
        assertFalse(RootStorageAccelerationPolicy.isRemovablePath("/storage/emulated/0/Music"))
    }
}
