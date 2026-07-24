/*
 * Copyright (c) 2026 Auxio Project
 * RootTreeSnapshotCodecTest.kt is part of Auxio.
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

package org.oxycblt.musikr.fs

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class RootTreeSnapshotCodecTest {
    @Test
    fun parsesBoundedTypedEntriesIncludingWhitespaceNames() {
        val snapshot =
            RootTreeSnapshotCodec.parse(
                "/mnt/media_rw/usbdisk0",
                "d\t10\t0\tMusic\nf\t11\t4\tMusic/track.flac\nl\t12\t1\tMusic/link\nf\t13\t1\t   \n",
            )
        requireNotNull(snapshot)
        assertEquals(4, snapshot.entries.size)
        assertTrue(snapshot.entries[0].isDirectory)
        assertEquals(11_000L, snapshot.entries[1].modifiedMs)
        assertTrue(snapshot.entries[2].isSymlink)
        assertEquals("   ", snapshot.entries[3].relativePath)
    }

    @Test
    fun acceptsMaximumConvertibleTimestamp() {
        val maxSeconds = Long.MAX_VALUE / 1000L
        val snapshot =
            RootTreeSnapshotCodec.parse("/storage/usbdisk0", "f\t$maxSeconds\t1\ttrack.flac\n")
        requireNotNull(snapshot)
        assertEquals(maxSeconds * 1000L, snapshot.entries.single().modifiedMs)
    }

    @Test
    fun rejectsMalformedTraversalControlCharactersAndOverflow() {
        assertNull(RootTreeSnapshotCodec.parse("/storage/usbdisk0", "x\t1\t1\tbad.mp3\n"))
        assertNull(RootTreeSnapshotCodec.parse("/storage/usbdisk0", "f\t1\t1\t../escape.mp3\n"))
        assertNull(RootTreeSnapshotCodec.parse("/storage/usbdisk0", "f\t1\t1\tbad\u001b.mp3\n"))
        assertNull(
            RootTreeSnapshotCodec.parse(
                "/storage/usbdisk0",
                "f\t${Long.MAX_VALUE / 1000L + 1L}\t1\toverflow.mp3\n",
            )
        )
        assertNull(
            RootTreeSnapshotCodec.parse(
                "/storage/usbdisk0",
                "f\t1\t1\ta.mp3\nf\t1\t1\tb.mp3\n",
                maxEntries = 1,
            )
        )
    }
}
