/*
 * Copyright (c) 2026 Auxio Project
 * StartupScanAuthorityPolicyTest.kt is part of Auxio.
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

package org.oxycblt.auxio.music

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.oxycblt.auxio.music.service.StartupScanAuthorityPolicy
import org.oxycblt.auxio.music.service.StartupScanOrigin

class StartupScanAuthorityPolicyTest {
    @Test
    fun currentProductVisibleStartMayRecoverAnAuthorisedSource() {
        assertTrue(
            StartupScanAuthorityPolicy.allowAutomaticScan(
                topwayProduct = true,
                origin = StartupScanOrigin.USER_VISIBLE,
                sourceAuthority = true,
            )
        )
    }

    @Test
    fun currentProductBackgroundStartNeverStartsARecoveryScan() {
        assertFalse(
            StartupScanAuthorityPolicy.allowAutomaticScan(
                topwayProduct = true,
                origin = StartupScanOrigin.BACKGROUND,
                sourceAuthority = true,
            )
        )
    }

    @Test
    fun androidFallbackPolicyRetainsBackgroundStartupAuthority() {
        assertTrue(
            StartupScanAuthorityPolicy.allowAutomaticScan(
                topwayProduct = false,
                origin = StartupScanOrigin.BACKGROUND,
                sourceAuthority = true,
            )
        )
    }

    @Test
    fun noProductModeScansWithoutCurrentSourceAuthority() {
        assertFalse(
            StartupScanAuthorityPolicy.allowAutomaticScan(
                topwayProduct = false,
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
