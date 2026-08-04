/*
 * Copyright (c) 2026 Auxio Project
 * CanonicalSourcePolicyTest.kt is part of Auxio.
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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CanonicalSourcePolicyTest {
    @Test
    fun collapsesPrimarySharedStorageAliases() {
        assertEquals(
            "/storage/emulated/0/Music",
            CanonicalSourcePolicy.normalizePath("/sdcard/Music"),
        )
        assertEquals(
            "/storage/emulated/0/Music",
            CanonicalSourcePolicy.normalizePath("/storage/emulated/0/Music/"),
        )
        assertEquals(
            "/storage/emulated/0/Music",
            CanonicalSourcePolicy.normalizePath("/storage/emulated//0//Music"),
        )
        assertEquals("/storage/emulated/0", CanonicalSourcePolicy.normalizePath("/sdcard"))
    }

    @Test
    fun collapsesRemovableBackingAliasesOntoAppFacingPaths() {
        assertEquals(
            "/storage/usbdisk0/Music",
            CanonicalSourcePolicy.normalizePath("/mnt/media_rw/usbdisk0/Music"),
        )
        assertEquals(
            "/storage/ABCD-1234/Music",
            CanonicalSourcePolicy.normalizePath("/mnt/media_rw/abcd-1234/Music"),
        )
        assertEquals(
            "/storage/ABCD-1234/Music",
            CanonicalSourcePolicy.normalizePath("/storage/abcd-1234/Music"),
        )
        assertEquals(
            "/storage/usbdisk1/Music",
            CanonicalSourcePolicy.normalizePath("/storage/USBDISK1/Music"),
        )
    }

    @Test
    fun keepsVolumeContentsCaseSensitive() {
        assertEquals(
            "/storage/emulated/0/music",
            CanonicalSourcePolicy.normalizePath("/storage/emulated/0/music"),
        )
    }

    @Test
    fun repairsDuplicatedPersistedPrefixes() {
        assertEquals(
            "/storage/emulated/0/Music",
            CanonicalSourcePolicy.normalizePath("/storage/emulated/0/storage/emulated/0/Music"),
        )
        assertEquals(
            "/storage/usbdisk0/Music",
            CanonicalSourcePolicy.normalizePath("/storage/usbdisk0/storage/usbdisk0/Music"),
        )
    }

    @Test
    fun rejectsTraversalProtectedAndRootOnlyPaths() {
        assertNull(CanonicalSourcePolicy.normalizePath("/storage/../data"))
        assertNull(CanonicalSourcePolicy.normalizePath("/storage/emulated/0/./Music"))
        assertNull(CanonicalSourcePolicy.normalizePath("/"))
        assertNull(CanonicalSourcePolicy.normalizePath("/data/media/0/Music"))
        assertNull(CanonicalSourcePolicy.normalizePath("/system"))
        assertNull(CanonicalSourcePolicy.normalizePath("/proc/self"))
        // A privileged mount without an app-facing alias is never an ordinary playback source.
        assertNull(CanonicalSourcePolicy.normalizePath("/mnt/media_rw/unknownmount/Music"))
        assertNull(CanonicalSourcePolicy.normalizePath("relative/Music"))
    }

    @Test
    fun identifiesVolumeRootsAndScopes() {
        assertTrue(CanonicalSourcePolicy.isVolumeRoot("/storage/emulated/0"))
        assertTrue(CanonicalSourcePolicy.isVolumeRoot("/sdcard"))
        assertTrue(CanonicalSourcePolicy.isVolumeRoot("/storage/usbdisk0"))
        assertTrue(CanonicalSourcePolicy.isVolumeRoot("/mnt/media_rw/usbdisk0"))
        assertTrue(CanonicalSourcePolicy.isVolumeRoot("/storage/ABCD-1234"))
        assertFalse(CanonicalSourcePolicy.isVolumeRoot("/storage/emulated/0/Music"))
        assertEquals(
            CanonicalSourcePolicy.Scope.WHOLE_VOLUME,
            CanonicalSourcePolicy.scopeOf("/storage/emulated/0"),
        )
        assertEquals(
            CanonicalSourcePolicy.Scope.EXPLICIT,
            CanonicalSourcePolicy.scopeOf("/storage/emulated/0/Music"),
        )
    }

    @Test
    fun detectsAncestryAcrossAliases() {
        assertTrue(CanonicalSourcePolicy.isAncestorOf("/sdcard", "/storage/emulated/0/Music"))
        assertTrue(
            CanonicalSourcePolicy.isAncestorOf("/storage/emulated/0/", "/sdcard/Music/Album")
        )
        assertFalse(
            CanonicalSourcePolicy.isAncestorOf(
                "/storage/emulated/0/Music",
                "/storage/emulated/0/Music",
            )
        )
        assertFalse(
            CanonicalSourcePolicy.isAncestorOf(
                "/storage/emulated/0/Music",
                "/storage/emulated/0/Musicals",
            )
        )
    }

    @Test
    fun producesOneIdentityForEveryAliasOfOneFolder() {
        val identities =
            listOf(
                    "/sdcard/Music",
                    "/storage/emulated/0/Music",
                    "/storage/emulated/0/Music/",
                    "/storage/emulated//0/Music",
                    "/storage/emulated/0/storage/emulated/0/Music",
                )
                .map(CanonicalSourcePolicy::identityForPath)
                .toSet()
        assertEquals(1, identities.size)
        assertEquals("path:/storage/emulated/0/Music", identities.first())
    }

    @Test
    fun collapsesDuplicatesPreservingFirstSelection() {
        val collapsed =
            CanonicalSourcePolicy.collapseDuplicates(
                listOf(
                    "/storage/emulated/0/Music",
                    "/sdcard/Music",
                    "/storage/usbdisk0/Audio",
                    "/storage/emulated/0/Music/",
                ),
                CanonicalSourcePolicy::identityForPath,
            )
        assertEquals(listOf("/storage/emulated/0/Music", "/storage/usbdisk0/Audio"), collapsed)
    }

    @Test
    fun retainsUnidentifiableItemsWhenCollapsing() {
        val collapsed =
            CanonicalSourcePolicy.collapseDuplicates(
                listOf("content://tree/a", "content://tree/b"),
                CanonicalSourcePolicy::identityForPath,
            )
        assertEquals(2, collapsed.size)
    }

    @Test
    fun ordersNarrowRootsBeforeWholeVolumeRoots() {
        val ordered =
            CanonicalSourcePolicy.traversalOrder(
                listOf(
                    "/storage/emulated/0",
                    "/storage/emulated/0/Music",
                    "/storage/emulated/0/Music/Live/Sets",
                    "/storage/usbdisk0",
                ),
                { it },
            )
        assertEquals(
            listOf(
                "/storage/emulated/0/Music/Live/Sets",
                "/storage/emulated/0/Music",
                "/storage/emulated/0",
                "/storage/usbdisk0",
            ),
            ordered,
        )
    }

    @Test
    fun uriIdentityIgnoresTrailingSeparators() {
        assertEquals(
            CanonicalSourcePolicy.identityForUriString("content://x/tree/primary%3AMusic"),
            CanonicalSourcePolicy.identityForUriString("content://x/tree/primary%3AMusic/"),
        )
    }
}
