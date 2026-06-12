package org.oxycblt.musikr.fs

import org.junit.Assert.assertEquals
import org.junit.Test

class StoragePathAliasPolicyTest {

    @Test
    fun testDeduplicatePaths() {
        val paths = listOf(
            "/mnt/media_rw/usbdisk0/Music/A.flac",
            "/storage/usbdisk0/Music/A.flac",
            "/sdcard/Music/B.flac",
            "/storage/emulated/0/Music/B.flac"
        )
        
        val deduped = StoragePathAliasPolicy.deduplicatePaths(paths)
        assertEquals(2, deduped.size)
    }

    @Test
    fun testDeduplicateFiles() {
        data class MockFile(val path: String, val size: Long, val modified: Long)
        
        val files = listOf(
            MockFile("/mnt/media_rw/usbdisk0/Music/A.flac", 100L, 1000L),
            MockFile("/storage/usbdisk0/Music/A.flac", 100L, 2000L),
            MockFile("/storage/usbdisk0/Music/A.flac", 200L, 2000L)
        )
        
        val deduped = StoragePathAliasPolicy.deduplicateFiles(
            files,
            { it.path },
            { it.size },
            { it.modified }
        )
        
        assertEquals(2, deduped.size)
    }
}
