/*
 * Copyright (c) 2026 Auxio Project
 * DirectPathFilterTest.kt is part of Auxio.
 */

package org.oxycblt.musikr.fs.direct

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DirectPathFilterTest {
    @Test
    fun ts18DefaultIncludesLikelyAudioDirsCaseInsensitively() {
        val filter = DirectPathFilter.ts18Default()

        assertTrue(filter.shouldInclude("/storage/usbdisk0/Music/song.mp3"))
        assertTrue(filter.shouldInclude("/storage/usbdisk0/downloads/song.mp3"))
        assertTrue(filter.shouldInclude("/storage/emulated/0/My Music Archive/song.mp3"))
        assertTrue(filter.shouldInclude("/storage/emulated/0/Test/Media/song.mp3"))
    }

    @Test
    fun ts18DefaultExcludesUnrelatedRootClutter() {
        val filter = DirectPathFilter.ts18Default()

        assertFalse(filter.shouldInclude("/storage/usbdisk0/random-root-clutter/song.mp3"))
        assertFalse(filter.shouldDescend("/storage/usbdisk0/Android/data"))
        assertFalse(filter.shouldDescend("/storage/usbdisk0/.thumbnails"))
        assertFalse(filter.shouldDescend("/storage/usbdisk0/random-root-clutter"))
    }

    @Test
    fun disabledFilterAllowsBroadScanningWhenDeliberatelySelected() {
        val filter = DirectPathFilter.disabled()

        assertTrue(filter.shouldInclude("/storage/usbdisk0/random-root-clutter/song.mp3"))
        assertTrue(filter.shouldDescend("/storage/usbdisk0/Android/data"))
    }
}
