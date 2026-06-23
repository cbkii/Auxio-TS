/*
 * Copyright (c) 2026 Auxio Project
 * DirectFsRootPolicyTest.kt is part of Auxio.
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

package org.oxycblt.musikr.fs.direct

import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.oxycblt.musikr.fs.direct.DirectFS.Companion.isAllowedRoot
import org.oxycblt.musikr.fs.direct.DirectFS.Companion.shellQuote

class DirectFsRootPolicyTest {
    @Test
    fun testRejectsProtectedRoots() {
        assertFalse(isAllowedRoot(File("/")))
        assertFalse(isAllowedRoot(File("/system")))
        assertFalse(isAllowedRoot(File("/vendor")))
        assertFalse(isAllowedRoot(File("/data")))
    }

    @Test
    fun testAllowsTs18StorageRoots() {
        assertTrue(isAllowedRoot(File("/storage/usbdisk0")))
        assertTrue(isAllowedRoot(File("/mnt/media_rw/usbdisk0")))
    }

    @Test
    fun testShellQuoteEscapesSingleQuotes() {
        assertEquals("'/storage/usbdisk0/Music'\"'\"'s'", shellQuote("/storage/usbdisk0/Music's"))
    }

    @Test
    fun testShellQuoteContainsShellMetacharacters() {
        val path = "/storage/usbdisk0/Music \$(rm -rf /); `id`\nnext"
        assertEquals("'$path'", shellQuote(path))
    }
}
