/*
 * Copyright (c) 2026 Auxio Project
 * StartupScanAuthorityPolicyTest.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.oxycblt.auxio.music

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.oxycblt.auxio.music.service.StartupScanAuthorityPolicy
import org.oxycblt.auxio.music.service.StartupScanOrigin

class StartupScanAuthorityPolicyTest {
    @Test
    fun topwayVisibleStartMayRecoverAnAuthorisedSource() {
        assertTrue(
            StartupScanAuthorityPolicy.allowAutomaticScan(
                topwayCompatFlavor = true,
                origin = StartupScanOrigin.USER_VISIBLE,
                sourceAuthority = true,
            )
        )
    }

    @Test
    fun topwayBackgroundStartNeverStartsARecoveryScan() {
        assertFalse(
            StartupScanAuthorityPolicy.allowAutomaticScan(
                topwayCompatFlavor = true,
                origin = StartupScanOrigin.BACKGROUND,
                sourceAuthority = true,
            )
        )
    }

    @Test
    fun standardVariantRetainsBackgroundStartupAuthority() {
        assertTrue(
            StartupScanAuthorityPolicy.allowAutomaticScan(
                topwayCompatFlavor = false,
                origin = StartupScanOrigin.BACKGROUND,
                sourceAuthority = true,
            )
        )
    }

    @Test
    fun noVariantScansWithoutCurrentSourceAuthority() {
        assertFalse(
            StartupScanAuthorityPolicy.allowAutomaticScan(
                topwayCompatFlavor = false,
                origin = StartupScanOrigin.USER_VISIBLE,
                sourceAuthority = false,
            )
        )
    }

    @Test
    fun trustedVisibleNonceIsSingleUse() {
        val nonce = StartupScanAuthorityPolicy.issueTrustedUserVisibleStart()

        assertFalse(StartupScanAuthorityPolicy.consumeTrustedUserVisibleStart("wrong"))
        assertTrue(StartupScanAuthorityPolicy.consumeTrustedUserVisibleStart(nonce))
        assertFalse(StartupScanAuthorityPolicy.consumeTrustedUserVisibleStart(nonce))
    }
}
