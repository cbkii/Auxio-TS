package org.oxycblt.musikr.fs

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class RootTreeSnapshotCodecTest {
    @Test
    fun parsesBoundedTypedEntries() {
        val snapshot =
            RootTreeSnapshotCodec.parse(
                "/mnt/media_rw/usbdisk0",
                "d\t10\t0\tMusic\nf\t11\t4\tMusic/track.flac\nl\t12\t1\tMusic/link\n",
            )
        requireNotNull(snapshot)
        assertEquals(3, snapshot.entries.size)
        assertTrue(snapshot.entries[0].isDirectory)
        assertEquals(11_000L, snapshot.entries[1].modifiedMs)
        assertTrue(snapshot.entries[2].isSymlink)
    }

    @Test
    fun rejectsTraversalAndEntryOverflow() {
        assertNull(RootTreeSnapshotCodec.parse("/storage/usbdisk0", "f\t1\t1\t../escape.mp3\n"))
        assertNull(
            RootTreeSnapshotCodec.parse(
                "/storage/usbdisk0",
                "f\t1\t1\ta.mp3\nf\t1\t1\tb.mp3\n",
                maxEntries = 1,
            )
        )
    }
}
