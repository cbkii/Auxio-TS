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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.oxycblt.musikr.fs.direct.DirectFS.Companion.isAllowedRoot
import org.oxycblt.musikr.fs.direct.DirectFS.Companion.isExpectedRestrictedSharedStorageChild
import org.oxycblt.musikr.fs.direct.DirectFS.Companion.shouldDescendIntoDirectory

class DirectFsRootPolicyTest {
    @Test
    fun rejectsProtectedRootsAndDescendants() {
        listOf("/", "/system", "/system/app", "/vendor", "/vendor/etc", "/data", "/data/media")
            .forEach { path -> assertFalse(path, isAllowedRoot(File(path))) }
    }

    @Test
    fun allowsInternalAppFacingPreparedAndRawBackingRoots() {
        assertTrue(isAllowedRoot(File("/storage/emulated/0")))
        assertTrue(isAllowedRoot(File("/storage/emulated/0/My Audio")))
        assertTrue(isAllowedRoot(File("/storage/usbdisk0")))
        assertTrue(isAllowedRoot(File("/storage/auxio-root/usbdisk0")))
        assertTrue(isAllowedRoot(File("/mnt/media_rw/usbdisk0")))
    }

    @Test
    fun onlyTreatsPlatformRestrictedChildrenAsIgnorableFromSharedStorageRoot() {
        val sharedRoot = File("/storage/emulated/0")
        assertTrue(
            isExpectedRestrictedSharedStorageChild(
                File("/storage/emulated/0/Android/data"),
                sharedRoot,
            )
        )
        assertTrue(
            isExpectedRestrictedSharedStorageChild(
                File("/storage/emulated/0/Android/obb/example"),
                sharedRoot,
            )
        )
        assertFalse(
            isExpectedRestrictedSharedStorageChild(
                File("/storage/emulated/0/Download/data"),
                sharedRoot,
            )
        )
        assertFalse(
            isExpectedRestrictedSharedStorageChild(
                File("/storage/emulated/0/Android/data/example"),
                File("/storage/emulated/0/Android/data"),
            )
        )
    }

    @Test
    fun traversalBudgetsRemainBoundedForAccidentalWholeVolumeSelections() {
        assertTrue(DirectFS.MAX_VISITED_FILES in 1_000..100_000)
        assertTrue(DirectFS.MAX_VISITED_DIRECTORIES in 1_000..100_000)
    }

    @Test
    fun wholeVolumeTraversalSkipsKnownNonMusicAndHiddenTrees() {
        listOf("Android", "Download", "DCIM", "Pictures", "Movies", ".cache").forEach {
            assertFalse(it, shouldDescendIntoDirectory(it))
        }
        assertTrue(shouldDescendIntoDirectory("Music"))
        assertTrue(shouldDescendIntoDirectory("My Audio"))
    }
}
