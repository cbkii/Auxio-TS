/* Copyright (c) 2026 Auxio Project */
package org.oxycblt.musikr.fs.direct

import java.io.File
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.oxycblt.musikr.fs.direct.DirectFS.Companion.isAllowedRoot

class DirectFsRootPolicyTest {
    @Test
    fun rejectsProtectedRoots() {
        assertFalse(isAllowedRoot(File("/")))
        assertFalse(isAllowedRoot(File("/system")))
        assertFalse(isAllowedRoot(File("/vendor")))
        assertFalse(isAllowedRoot(File("/data")))
    }

    @Test
    fun allowsAppFacingPreparedAndRawBackingRoots() {
        assertTrue(isAllowedRoot(File("/storage/usbdisk0")))
        assertTrue(isAllowedRoot(File("/storage/auxio-root/usbdisk0")))
        assertTrue(isAllowedRoot(File("/mnt/media_rw/usbdisk0")))
    }
}
