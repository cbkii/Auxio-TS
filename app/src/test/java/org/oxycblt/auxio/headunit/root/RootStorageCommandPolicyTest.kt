/*
 * Copyright (c) 2026 Auxio Project
 * RootStorageCommandPolicyTest.kt is part of Auxio.
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

package org.oxycblt.auxio.headunit.root

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RootStorageCommandPolicyTest {
    @Test
    fun acceptsOnlyStorageBackingsAndPreparedAliases() {
        assertTrue(RootStorageCommandPolicy.isAllowedStorageRoot("/storage/usbdisk0/Music"))
        assertTrue(RootStorageCommandPolicy.isAllowedStorageRoot("/mnt/media_rw/usbdisk1"))
        assertTrue(RootStorageCommandPolicy.isAllowedStorageRoot("/storage/auxio-root/usbdisk1"))
        assertFalse(RootStorageCommandPolicy.isAllowedStorageRoot("/data/local/tmp"))
        assertFalse(RootStorageCommandPolicy.isAllowedStorageRoot("/storage/usbdisk0/../data"))
        assertFalse(RootStorageCommandPolicy.isAllowedStorageRoot("/storage/usbdisk0/a\tb"))
        assertFalse(RootStorageCommandPolicy.isAllowedStorageRoot("/storage/usbdisk0/a\nb"))
    }

    @Test
    fun commandIsOneBoundedRecursiveSnapshot() {
        val command =
            RootStorageCommandPolicy.buildSnapshotCommand(
                "\\mnt\\media_rw\\usbdisk0\\Music's",
                maxDepth = 8,
            )
        assertTrue(command.contains("find \"\$root\" -xdev"))
        assertTrue(command.contains("-maxdepth 8"))
        assertTrue(command.contains("-exec sh -c"))
        assertTrue(command.contains("{} +"))
        assertTrue(command.contains("case \"\$rel\" in *[[:cntrl:]]*)"))
        assertTrue(command.contains("/mnt/media_rw/usbdisk0/Music'\"'\"'s"))
        assertFalse(command.contains("find \"\$root\" -print"))
        assertFalse(command.contains("while IFS= read -r p"))
        assertFalse(command.contains("grep -q"))
        assertFalse(command.contains("pm disable-user"))
    }
}
