/*
 * Copyright (c) 2026 Auxio Project
 * MediaBrowserClientPolicyTest.kt is part of Auxio.
 */

package org.oxycblt.auxio.music.service

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MediaBrowserClientPolicyTest {
    @Test
    fun rejectsPackageUidMismatchEvenForKnownOemPackage() {
        assertFalse(
            MediaBrowserClientPolicy.classify(
                packageMatchesUid = false,
                sameAppUid = false,
                systemUid = false,
                hasMediaControlPermission = false,
                knownOemPackage = true,
            )
        )
    }

    @Test
    fun trustsSameApplicationUid() {
        assertTrue(
            MediaBrowserClientPolicy.classify(
                packageMatchesUid = true,
                sameAppUid = true,
                systemUid = false,
                hasMediaControlPermission = false,
                knownOemPackage = false,
            )
        )
    }

    @Test
    fun trustsSystemAndPrivilegedMediaClients() {
        assertTrue(
            MediaBrowserClientPolicy.classify(
                packageMatchesUid = true,
                sameAppUid = false,
                systemUid = true,
                hasMediaControlPermission = false,
                knownOemPackage = false,
            )
        )
        assertTrue(
            MediaBrowserClientPolicy.classify(
                packageMatchesUid = true,
                sameAppUid = false,
                systemUid = false,
                hasMediaControlPermission = true,
                knownOemPackage = false,
            )
        )
    }

    @Test
    fun trustsEvidencedDofunAndStockPackagesOnlyAfterUidMatch() {
        assertTrue(
            MediaBrowserClientPolicy.classify(
                packageMatchesUid = true,
                sameAppUid = false,
                systemUid = false,
                hasMediaControlPermission = false,
                knownOemPackage = true,
            )
        )
    }

    @Test
    fun unrelatedApplicationGetsNoBrowseAuthority() {
        assertFalse(
            MediaBrowserClientPolicy.classify(
                packageMatchesUid = true,
                sameAppUid = false,
                systemUid = false,
                hasMediaControlPermission = false,
                knownOemPackage = false,
            )
        )
    }
}
