/*
 * Copyright (c) 2026 Auxio Project
 * SourceIdentityTest.kt is part of Auxio.
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
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
class SourceIdentityTest {
    @Test
    fun `configured roots on one volume retain independent ledger keys`() {
        val music = Uri.parse("file:///storage/emulated/0/Music")
        val audio = Uri.parse("file:///storage/emulated/0/Audio")

        assertNotEquals(
            SourceIdentity.forConfiguredRoot("DIRECT_FS", music),
            SourceIdentity.forConfiguredRoot("DIRECT_FS", audio),
        )
        assertNotEquals(
            SourceIdentity.forConfiguredRoot("SAF", music),
            SourceIdentity.forConfiguredRoot("DIRECT_FS", music),
        )
        assertEquals(
            "direct_fs:${SourceIdentity.canonicalKeyForUri(music)}",
            SourceIdentity.forConfiguredRoot("DIRECT_FS", music),
        )
    }

    @Test
    fun `emitted file uses exact source key before legacy volume fallback`() {
        val explicit = "direct_fs:path:/storage/emulated/0/Music"
        val file =
            File(
                uri = Uri.parse("file:///storage/emulated/0/Music/a.mp3"),
                path =
                    Path(
                        Volume.ThirdParty(Uri.parse("file:///storage/emulated/0")),
                        Components.root(),
                    ),
                addedMs =
                    object : AddedMs {
                        override suspend fun resolve() = null
                    },
                modifiedMs = 0L,
                mimeType = "audio/mpeg",
                size = 1L,
                parent = null,
                sourceKey = explicit,
            )
        assertEquals(explicit, SourceIdentity.forFile(file))
    }
}
