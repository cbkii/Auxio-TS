package org.oxycblt.auxio.headunit.topway

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TopwaySourcePolicyDiscoveryTest {
    @Test fun fixedUsbDiskZeroIsOnlyASeedNotTheDiscoveryLimit() {
        assertTrue(TopwaySourcePolicy.TS18_USB_CANDIDATES.contains("/storage/usbdisk0"))
        assertFalse(TopwaySourcePolicy.TS18_USB_CANDIDATES.contains("/storage/usbdisk1"))
    }
    @Test fun systemSourceFilterStillAcceptsUsbDiskOneMusicPaths() {
        assertTrue(TopwaySourcePolicy.matchesSystemSourceFilter("/storage/usbdisk1/My Music/song.flac"))
    }
}
