package org.oxycblt.auxio.playback.service

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.oxycblt.auxio.playback.dsToMs
import org.oxycblt.auxio.playback.msToDs
import org.oxycblt.auxio.playback.msToSecs
import org.oxycblt.auxio.playback.dsToSecs
import org.oxycblt.auxio.playback.formatDurationMs
import org.oxycblt.auxio.playback.formatDurationDs
import org.oxycblt.auxio.playback.formatDurationSecs

@RunWith(RobolectricTestRunner::class)
class PlaybackUtilTest {
    @Test
    fun `test formatDurationSecs`() {
        assertEquals("--:--", 0L.formatDurationSecs(false))
        assertEquals("0:00", 0L.formatDurationSecs(true))
        assertEquals("0:01", 1L.formatDurationSecs(false))
    }
}
