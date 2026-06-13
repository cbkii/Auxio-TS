package org.oxycblt.auxio.headunit.topway

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class TopwaySourcePolicyTest {

    @Test
    fun testCandidateRootsIncludesSafeFallbacks() {
        val fallbacks = TopwaySourcePolicy.SAFE_GENERIC_FALLBACKS
        val roots = TopwaySourcePolicy.CANDIDATE_ROOTS

        assertTrue(roots.containsAll(fallbacks))
    }

    @Test
    fun testNoisyDirsContainsKnownProblematicFolders() {
        assertTrue(TopwaySourcePolicy.NOISY_DIRS.contains("Android"))
        assertTrue(TopwaySourcePolicy.NOISY_DIRS.contains(".Tcfg"))
    }

    @Test
    fun testMatchesSystemSourceFilter() {
        assertTrue(TopwaySourcePolicy.matchesSystemSourceFilter("/storage/usbdisk0/Music/song.mp3"))
        assertTrue(TopwaySourcePolicy.matchesSystemSourceFilter("/storage/emulated/0/Download/song.mp3"))
        assertTrue(TopwaySourcePolicy.matchesSystemSourceFilter("/mnt/media_rw/USB/Media/song.mp3"))
        assertTrue(!TopwaySourcePolicy.matchesSystemSourceFilter("/storage/usbdisk0/DCIM/photo.jpg"))
    }
}
