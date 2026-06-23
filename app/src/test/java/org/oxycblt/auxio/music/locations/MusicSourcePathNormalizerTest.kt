/*
 * Copyright (c) 2026 Auxio Project
 * MusicSourcePathNormalizerTest.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.oxycblt.auxio.music.locations

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MusicSourcePathNormalizerTest {
    @Test
    fun directFsKeepsManualUsbPathAsFileUri() {
        assertEquals(
            "file:///storage/usbdisk0/Music",
            MusicSourcePathNormalizer.normalizePersistedLocation(
                "/storage/usbdisk0/Music",
                true,
            ),
        )
    }

    @Test
    fun directFsKeepsFileUriWithoutDuplicatingPath() {
        assertEquals(
            "file:///storage/usbdisk0/Music",
            MusicSourcePathNormalizer.normalizePersistedLocation(
                "file:///storage/usbdisk0/Music",
                true,
            ),
        )
    }

    @Test
    fun directFsConvertsPrimaryExternalStorageTreeUri() {
        assertEquals(
            "file:///storage/emulated/0/Music",
            MusicSourcePathNormalizer.normalizePersistedLocation(
                "content://com.android.externalstorage.documents/tree/primary%3AMusic",
                true,
            ),
        )
    }

    @Test
    fun safPreservesPrimaryExternalStorageTreeUri() {
        val uri = "content://com.android.externalstorage.documents/tree/primary%3AMusic"
        assertEquals(uri, MusicSourcePathNormalizer.normalizePersistedLocation(uri, false))
    }

    @Test
    fun directFsRejectsUnknownThirdPartyContentUri() {
        assertNull(
            MusicSourcePathNormalizer.normalizePersistedLocation(
                "content://example.provider/tree/music",
                true,
            )
        )
    }

    @Test
    fun duplicatedPersistedStoragePathIsRepairedWhenSafe() {
        assertEquals(
            "file:///storage/usbdisk0/Music",
            MusicSourcePathNormalizer.normalizePersistedLocation(
                "file:///storage/usbdisk0/storage/usbdisk0/Music",
                true,
            ),
        )
    }
}
