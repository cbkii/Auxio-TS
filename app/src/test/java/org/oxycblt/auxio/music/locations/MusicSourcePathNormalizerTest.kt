/*
 * Copyright (c) 2026 Auxio Project
 * MusicSourcePathNormalizerTest.kt is part of Auxio.
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

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
class MusicSourcePathNormalizerTest {
    @Test
    fun directFsKeepsManualUsbPathAsFileUri() {
        assertEquals(
            "file:///storage/usbdisk0/Music",
            MusicSourcePathNormalizer.normalizePersistedLocation("/storage/usbdisk0/Music", true),
        )
    }

    @Test
    fun directFsKeepsManualUsbDiskOnePathAsFileUri() {
        assertEquals(
            "file:///storage/usbdisk1/Music",
            MusicSourcePathNormalizer.normalizePersistedLocation("/storage/usbdisk1/Music", true),
        )
    }

    @Test
    fun directFsMigratesRawUsbBackingPathToAppFacingStorage() {
        assertEquals(
            "file:///storage/usbdisk3/Music",
            MusicSourcePathNormalizer.normalizePersistedLocation(
                "/mnt/media_rw/usbdisk3/Music",
                true,
            ),
        )
    }

    @Test
    fun directFsMigratesRawUuidBackingPathToAppFacingStorage() {
        assertEquals(
            "file:///storage/ABCD-1234/Music",
            MusicSourcePathNormalizer.normalizePersistedLocation(
                "/mnt/media_rw/ABCD-1234/Music",
                true,
            ),
        )
    }

    @Test
    fun directFsKeepsInternalSharedStorageRoot() {
        assertEquals(
            "file:///storage/emulated/0",
            MusicSourcePathNormalizer.normalizePersistedLocation("/storage/emulated/0", true),
        )
    }

    @Test
    fun directFsKeepsArbitraryInternalSharedStorageChild() {
        assertEquals(
            "file:///storage/emulated/0/My%20Audio%20Archive",
            MusicSourcePathNormalizer.normalizePersistedLocation(
                "/storage/emulated/0/My Audio Archive",
                true,
            ),
        )
    }

    @Test
    fun directFsNormalisesSdcardAliasToInternalSharedStorage() {
        assertEquals(
            "file:///storage/emulated/0/Recordings",
            MusicSourcePathNormalizer.normalizePersistedLocation("/sdcard/Recordings", true),
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
    fun directFsConvertsPrimaryExternalStorageRootTreeUri() {
        assertEquals(
            "file:///storage/emulated/0",
            MusicSourcePathNormalizer.normalizePersistedLocation(
                "content://com.android.externalstorage.documents/tree/primary%3A",
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
    fun safCanonicalisesEncodedDecodedAndEquivalentDocumentTreeForms() {
        val expected =
            "content://com.android.externalstorage.documents/tree/primary%3AMusic%2FAlbums"
        listOf(
                "content://com.android.externalstorage.documents/tree/PRIMARY:Music/Albums/",
                "content://com.android.externalstorage.documents/tree/primary%3AMusic%2FAlbums",
                "content://com.android.externalstorage.documents/tree/primary%3AMusic%2FAlbums/" +
                    "document/primary%3AMusic%2FAlbums",
            )
            .forEach {
                assertEquals(
                    expected,
                    MusicSourcePathNormalizer.normalizePersistedLocation(it, false),
                )
            }
    }

    @Test
    fun safRejectsMalformedExternalStorageTreeIds() {
        listOf(
                "content://com.android.externalstorage.documents/tree/primary",
                "content://com.android.externalstorage.documents/tree/primary%3AMusic%2F..%2Fdata",
            )
            .forEach { assertNull(MusicSourcePathNormalizer.normalizePersistedLocation(it, false)) }
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
    fun directFsRejectsDotSegmentTraversalBeforePrefixAllowList() {
        assertNull(
            MusicSourcePathNormalizer.normalizePersistedLocation("file:///storage/../data", true)
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

    @Test
    fun duplicatedDynamicUsbPathIsRepairedWhenSafe() {
        assertEquals(
            "file:///storage/usbdisk7/Music",
            MusicSourcePathNormalizer.normalizePersistedLocation(
                "file:///storage/usbdisk7/storage/usbdisk7/Music",
                true,
            ),
        )
    }

    @Test
    fun duplicatedPersistedUsbDiskOnePathIsRepairedWhenSafe() {
        assertEquals(
            "file:///storage/usbdisk1/Music",
            MusicSourcePathNormalizer.normalizePersistedLocation(
                "file:///storage/usbdisk1/storage/usbdisk1/Music",
                true,
            ),
        )
    }

    @Test
    fun duplicatedContentTreePathIsRepairedAfterDirectFsConversion() {
        assertEquals(
            "file:///storage/emulated/0/Music",
            MusicSourcePathNormalizer.normalizePersistedLocation(
                "content://com.android.externalstorage.documents/tree/primary%3Astorage%2Femulated%2F0%2FMusic",
                true,
            ),
        )
    }
}
