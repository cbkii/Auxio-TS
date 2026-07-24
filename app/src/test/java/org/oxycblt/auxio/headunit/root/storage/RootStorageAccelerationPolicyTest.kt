package org.oxycblt.auxio.headunit.root.storage

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RootStorageAccelerationPolicyTest {
    @Test
    fun cachedPreparedMetadataLeadsWhenRootFeatureIsEnabled() {
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
    fun alreadyGrantedRootLeadsForRawOrPreparedPaths() {
        listOf(
                "/mnt/media_rw/usbdisk0/Music",
                "/storage/auxio-root/usbdisk0/Music",
            )
            .forEach { path ->
                assertEquals(
                    RootStorageResolutionOrder.REFRESHED_ROOT_METADATA_FIRST,
                    RootStorageAccelerationPolicy.choose(
                        requestedPath = path,
                        rootEnabled = true,
                        rootAvailable = true,
                        hasCachedRecord = false,
                    ),
                )
            }
    }

    @Test
    fun ordinaryStorageDoesNotPayForRootWithoutAccelerationEvidence() {
        assertEquals(
            RootStorageResolutionOrder.DIRECT_FIRST,
            RootStorageAccelerationPolicy.choose(
                requestedPath = "/storage/usbdisk0/Music",
                rootEnabled = true,
                rootAvailable = true,
                hasCachedRecord = false,
            ),
        )
        assertEquals(
            RootStorageResolutionOrder.DIRECT_FIRST,
            RootStorageAccelerationPolicy.choose(
                requestedPath = "/storage/emulated/0/Music",
                rootEnabled = true,
                rootAvailable = true,
                hasCachedRecord = false,
            ),
        )
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
