/*
 * Copyright (c) 2026 Auxio Project
 * PassivePreparationPolicyTest.kt is part of Auxio.
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

package org.oxycblt.auxio.playback.state

import org.junit.Test

class PassivePreparationPolicyTest {
    @Test
    fun idleStartupMayRestoreButAPendingUserCommandOwnsTheNextAction() {
        org.junit.Assert.assertTrue(PassivePreparationPolicy.mayRestore(false, false, false))
        org.junit.Assert.assertFalse(PassivePreparationPolicy.mayRestore(true, false, false))
    }

    @Test
    fun warmPlaybackAndAnExistingRestoreAreNeverReplacedByHomePreparation() {
        org.junit.Assert.assertFalse(PassivePreparationPolicy.mayRestore(false, true, false))
        org.junit.Assert.assertFalse(PassivePreparationPolicy.mayRestore(false, false, true))
    }
}
