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

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DirectFsRootPolicyTest {
    @Test
    fun `root listing only allows explicit storage descendants`() {
        assertTrue(DirectFsRootPolicy.isAllowedRootPath("/storage/usbdisk0"))
        assertTrue(DirectFsRootPolicy.isAllowedRootPath("/storage/usbdisk0/Music"))

        assertFalse(DirectFsRootPolicy.isAllowedRootPath(""))
        assertFalse(DirectFsRootPolicy.isAllowedRootPath("/"))
        assertFalse(DirectFsRootPolicy.isAllowedRootPath("/storage/"))
        assertFalse(DirectFsRootPolicy.isAllowedRootPath("/data/data/org.oxycblt.auxio"))
        assertFalse(DirectFsRootPolicy.isAllowedRootPath("/storage/usbdisk0/../emulated"))
        assertFalse(DirectFsRootPolicy.isAllowedRootPath("/storage/usbdisk0\n/system"))
    }

    @Test
    fun `root listing command shell-quotes unsafe path characters`() {
        val command =
            DirectFsRootPolicy.buildRootListCommand("/storage/usbdisk0/Music/a'b;\$(reboot)")
                .getOrThrow()

        assertTrue(command.contains("'/storage/usbdisk0/Music/a'\"'\"'b;\$(reboot)'"))
    }
}
