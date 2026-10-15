/*
 * Copyright (c) 2026 Auxio Project
 * BridgeCallerPolicyTest.kt is part of Auxio.
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

package com.tw.music

import android.os.Process
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BridgeCallerPolicyTest {
    @Test
    fun onlySystemUidCanIssuePrivateBridgeCommands() {
        assertTrue(BridgeCallerPolicy.isTrustedUid(Process.SYSTEM_UID))
        assertFalse(BridgeCallerPolicy.isTrustedUid(Process.FIRST_APPLICATION_UID))
        assertFalse(BridgeCallerPolicy.isTrustedUid(Process.ROOT_UID))
    }
}
