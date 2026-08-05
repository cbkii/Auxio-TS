/*
 * Copyright (c) 2026 Auxio Project
 * RemovableStorageEventPolicyTest.kt is part of Auxio.
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

package org.oxycblt.auxio.music.service

import android.net.Uri
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.oxycblt.auxio.music.ConfiguredSourceSpec
import org.oxycblt.auxio.music.locations.LocationMode
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RemovableStorageEventPolicyTest {
    @Test
    fun `mount root maps to configured direct child only`() {
        val specs =
            listOf(
                spec("/storage/usbdisk0/Music", "usb0", LocationMode.DIRECT_FS),
                spec("/storage/usbdisk1/Music", "usb1", LocationMode.DIRECT_FS),
                spec("/storage/usbdisk0/Other", "saf", LocationMode.SAF),
            )

        assertEquals(
            setOf("usb0"),
            RemovableStorageEventPolicy.matchingSourceKeys("/storage/usbdisk0", specs),
        )
    }

    @Test
    fun `unrelated and malformed broadcasts do not create work`() {
        val specs = listOf(spec("/storage/usbdisk0/Music", "usb0", LocationMode.DIRECT_FS))

        assertTrue(
            RemovableStorageEventPolicy.matchingSourceKeys("/storage/usbdisk1", specs).isEmpty()
        )
        assertTrue(RemovableStorageEventPolicy.matchingSourceKeys(null, specs).isEmpty())
    }

    private fun spec(path: String, key: String, mode: LocationMode) =
        ConfiguredSourceSpec(
            normalizedUri = Uri.parse("file://$path"),
            sourceKey = key,
            canonicalKey = "path:$path",
            mode = mode,
            displayPath = path,
            accessState = ConfiguredSourceSpec.AccessState.AVAILABLE,
        )
}
