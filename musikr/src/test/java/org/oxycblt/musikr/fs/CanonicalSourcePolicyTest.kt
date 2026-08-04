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

import android.net.Uri
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
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
    fun externalStorageTreeIdentityIsStructural() {
        val expected =
            "content://com.android.externalstorage.documents/tree/primary%3AMusic%2FAlbums"
        val aliases =
            listOf(
                "content://com.android.externalstorage.documents/tree/primary%3AMusic%2FAlbums",
                "content://com.android.externalstorage.documents/tree/PRIMARY%3AMusic%2FAlbums/",
                "content://com.android.externalstorage.documents/tree/primary:Music/Albums",
                "content://com.android.externalstorage.documents/tree/primary%253AMusic%252FAlbums",
                "content://com.android.externalstorage.documents/tree/primary%3AMusic%2FAlbums/" +
                    "document/primary%3AMusic%2FAlbums",
            )

        assertEquals(
            setOf(expected),
            aliases.mapNotNull(CanonicalSourcePolicy::canonicalUriString).toSet(),
        )
        assertEquals(1, aliases.map(CanonicalSourcePolicy::identityForUriString).toSet().size)
        assertEquals(
            "/storage/emulated/0/Music/Albums",
            CanonicalSourcePolicy.externalStorageTreePath(Uri.parse(expected)),
        )
    }

    @Test
    fun externalStorageVolumeTokensAreCanonicalButContentsRemainCaseSensitive() {
        assertEquals(
            "content://com.android.externalstorage.documents/tree/ABCD-1234%3AMusic",
            CanonicalSourcePolicy.canonicalUriString(
                "content://com.android.externalstorage.documents/tree/abcd-1234%3AMusic"
            ),
        )
        assertEquals(
            "content://com.android.externalstorage.documents/tree/usbdisk2%3AMusic",
            CanonicalSourcePolicy.canonicalUriString(
                "content://com.android.externalstorage.documents/tree/USBDISK2%3AMusic"
            ),
        )
        assertFalse(
            CanonicalSourcePolicy.identityForUriString(
                "content://com.android.externalstorage.documents/tree/primary%3AMusic"
            ) ==
                CanonicalSourcePolicy.identityForUriString(
                    "content://com.android.externalstorage.documents/tree/primary%3Amusic"
                )
        )
    }

    @Test
    fun distinctDocumentBelowTreeRetainsDistinctIdentity() {
        val tree = "content://com.android.externalstorage.documents/tree/primary%3AMusic"
        val child = "$tree/document/primary%3AMusic%2FAlbums"

        assertFalse(
            CanonicalSourcePolicy.identityForUriString(tree) ==
                CanonicalSourcePolicy.identityForUriString(child)
        )
        assertNull(CanonicalSourcePolicy.externalStorageTreePath(Uri.parse(child)))
    }

    @Test
    fun malformedAndTraversalLikeExternalStorageIdsAreRejected() {
        listOf(
                "content://com.android.externalstorage.documents/tree/primary",
                "content://com.android.externalstorage.documents/tree/primary%3AMusic%2F..%2Fdata",
                "content://com.android.externalstorage.documents/tree/primary%253AMusic%252F..%252Fdata",
                "content://com.android.externalstorage.documents/tree/%3AMusic",
                "content://com.android.externalstorage.documents/tree/primary%3AMusic?unexpected=1",
            )
            .forEach { assertNull(it, CanonicalSourcePolicy.canonicalUriString(it)) }
    }

    @Test
    fun unrelatedProvidersRemainOpaque() {
        val plain = "content://example.provider/tree/primary%3AMusic"
        val trailing = "$plain/"

        assertEquals(plain, CanonicalSourcePolicy.canonicalUriString(plain))
        assertNull(CanonicalSourcePolicy.externalStorageTreePath(Uri.parse(plain)))
        assertFalse(
            CanonicalSourcePolicy.identityForUriString(plain) ==
                CanonicalSourcePolicy.identityForUriString(trailing)
        )
    }

    @Test
    fun legacyWholeVolumeOriginsAreConservativeAndDeterministic() {
        assertEquals(
            CanonicalSourcePolicy.Origin.WHOLE_VOLUME_FALLBACK,
            CanonicalSourcePolicy.legacyOriginForPath("/storage/usbdisk0"),
        )
        assertEquals(
            CanonicalSourcePolicy.Origin.EXPLICIT,
            CanonicalSourcePolicy.legacyOriginForPath("/storage/usbdisk0/Music"),
        )
    }
}
