/*
 * Copyright (c) 2026 Auxio Project
 * CoverProviderTest.kt is part of Auxio.
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

package org.oxycblt.auxio.image

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CoverProviderTest {
    @Test
    fun `copyBounded copies payloads within limit`() {
        val payload = ByteArray(4096) { (it % 251).toByte() }
        val output = ByteArrayOutputStream()

        assertTrue(
            CoverProvider.copyBounded(ByteArrayInputStream(payload), output, payload.size.toLong())
        )
        assertArrayEquals(payload, output.toByteArray())
    }

    @Test
    fun `copyBounded rejects payloads beyond limit`() {
        val payload = ByteArray(4096) { 7 }
        val output = ByteArrayOutputStream()

        assertFalse(CoverProvider.copyBounded(ByteArrayInputStream(payload), output, 1024))
        assertTrue(output.size() <= 1024)
    }
}
