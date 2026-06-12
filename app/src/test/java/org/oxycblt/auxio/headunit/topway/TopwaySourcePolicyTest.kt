package org.oxycblt.auxio.headunit.topway

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TopwaySourcePolicyTest {

    @Test
    fun testSystemSourceFilter() {
        assertTrue(TopwaySourcePolicy.matchesSystemSourceFilter("/storage/usbdisk0/MUSIC/song.mp3"))
        assertTrue(TopwaySourcePolicy.matchesSystemSourceFilter("/storage/usbdisk0/Download/song.mp3"))
        assertTrue(TopwaySourcePolicy.matchesSystemSourceFilter("/storage/usbdisk0/MEDIA/song.mp3"))
        
        assertFalse(TopwaySourcePolicy.matchesSystemSourceFilter("/storage/usbdisk0/Random/song.mp3"))
        assertFalse(TopwaySourcePolicy.matchesSystemSourceFilter("/storage/usbdisk0/Documents/song.mp3"))
    }
}
