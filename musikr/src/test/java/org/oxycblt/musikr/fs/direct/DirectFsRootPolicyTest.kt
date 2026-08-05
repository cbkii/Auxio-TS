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

import android.content.Context
import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.oxycblt.musikr.fs.CanonicalSourcePolicy
import org.oxycblt.musikr.fs.Components
import org.oxycblt.musikr.fs.Path
import org.oxycblt.musikr.fs.Volume
import org.oxycblt.musikr.fs.direct.DirectFS.Companion.isAllowedRoot
import org.oxycblt.musikr.fs.direct.DirectFS.Companion.isExpectedRestrictedSharedStorageChild
import org.oxycblt.musikr.fs.direct.DirectFS.Companion.shouldDescendIntoDirectory
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
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
        assertTrue(WHOLE_VOLUME_MAX_DIRECTORIES in 1_000..DirectFS.MAX_VISITED_DIRECTORIES)
    }

    @Test
    fun wholeVolumeTraversalSkipsKnownNonMusicAndHiddenTrees() {
        listOf("Android", "Download", "DCIM", "Pictures", "Movies", ".cache").forEach {
            assertFalse(it, shouldDescendIntoDirectory(it))
        }
        assertTrue(shouldDescendIntoDirectory("Music"))
        assertTrue(shouldDescendIntoDirectory("My Audio"))
    }

    @Test
    fun explicitlySelectedFoldersKeepOrdinaryChildrenWithGenericNames() {
        // A folder the user picked is scanned as asked: "Download" inside "/…/My Audio" is
        // ordinary content, not one of the platform's own media trees.
        listOf("Android", "Download", "DCIM", "Pictures", "Movies", "Music").forEach {
            assertTrue(it, shouldDescendIntoDirectory(it, CanonicalSourcePolicy.Scope.EXPLICIT))
        }
    }

    @Test
    fun everyScopeStillRefusesHiddenAndRelativeDirectories() {
        CanonicalSourcePolicy.Scope.entries.forEach { scope ->
            listOf(".cache", ".", "..", " ").forEach {
                assertFalse("$scope/$it", shouldDescendIntoDirectory(it, scope))
            }
        }
    }

    @Test
    fun configuredHiddenPolicyAllowsHiddenNamesButNeverRelativeSegments() {
        CanonicalSourcePolicy.Scope.entries.forEach { scope ->
            assertTrue(shouldDescendIntoDirectory(".archive", scope, withHidden = true))
            listOf(".", "..", " ").forEach {
                assertFalse(shouldDescendIntoDirectory(it, scope, withHidden = true))
            }
        }
    }

    @Test
    fun backendSuppressesOnlyBroadFallbackOverlappingExplicitSource() {
        val volume = prepared("usbdisk0", CanonicalSourcePolicy.Origin.WHOLE_VOLUME_FALLBACK)
        val explicit = prepared("usbdisk0/Music", CanonicalSourcePolicy.Origin.EXPLICIT)
        val automatic =
            prepared("usbdisk0/Podcasts", CanonicalSourcePolicy.Origin.AUTOMATIC_SUGGESTION)

        assertEquals(
            listOf(explicit, automatic),
            DirectFS.applyOverlapPolicy(listOf(explicit, automatic, volume)),
        )
    }

    @Test
    fun backendRetainsDeliberateExplicitOverlapsAndDistinctVolumes() {
        val explicitVolume = prepared("usbdisk0", CanonicalSourcePolicy.Origin.EXPLICIT)
        val nested = prepared("usbdisk0/Music", CanonicalSourcePolicy.Origin.EXPLICIT)
        val otherFallback = prepared("usbdisk1", CanonicalSourcePolicy.Origin.WHOLE_VOLUME_FALLBACK)

        assertEquals(
            listOf(nested, explicitVolume, otherFallback),
            DirectFS.applyOverlapPolicy(listOf(nested, explicitVolume, otherFallback)),
        )
    }

    private fun prepared(path: String, origin: CanonicalSourcePolicy.Origin): PreparedRoot {
        val canonicalPath = "/storage/$path"
        return PreparedRoot(
            sourceKey = canonicalPath,
            directory = File(canonicalPath),
            canonicalPath = canonicalPath,
            relativePath = Path(TestVolume, Components.root()),
            scope = CanonicalSourcePolicy.scopeOf(canonicalPath),
            origin = origin,
        )
    }

    private object TestVolume : Volume.Internal {
        override val mediaStoreName: String? = null
        override val components = org.oxycblt.musikr.fs.Components.root()

        override fun resolveName(context: Context) = "test"

        override fun isAccessible() = true
    }
}
