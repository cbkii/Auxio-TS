/*
 * Copyright (c) 2026 Auxio Project
 * FileUriStoragePolicyTest.kt is part of Auxio.
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

package org.oxycblt.musikr.fs.path

import android.content.Context
import android.net.Uri
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Test
import org.junit.runner.RunWith
import org.oxycblt.musikr.fs.Components
import org.oxycblt.musikr.fs.Volume
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
class FileUriStoragePolicyTest {
    @Test
    fun `primary shared-storage root maps to internal root`() {
        assertEquals("", FileUriStoragePolicy.relativeToPrimarySharedStorage("/storage/emulated/0"))
    }

    @Test
    fun `primary shared-storage descendants retain relative path`() {
        assertEquals(
            "/Music/Album/song.flac",
            FileUriStoragePolicy.relativeToPrimarySharedStorage(
                "/storage/emulated/0/Music/Album/song.flac"
            ),
        )
    }

    @Test
    fun `similarly prefixed and removable paths do not map to primary storage`() {
        assertNull(
            FileUriStoragePolicy.relativeToPrimarySharedStorage("/storage/emulated/01/Music")
        )
        assertNull(FileUriStoragePolicy.relativeToPrimarySharedStorage("/storage/usbdisk0/Music"))
    }

    @Test
    fun `file URI primary root uses internal volume`() {
        val path =
            requireNotNull(pathFactory().unpackFileUri(Uri.parse("file:///storage/emulated/0")))

        assertSame(InternalVolume, path.volume)
        assertEquals("", path.components.unixString)
    }

    @Test
    fun `file URI primary descendant uses internal volume and relative components`() {
        val path =
            requireNotNull(
                pathFactory()
                    .unpackFileUri(Uri.parse("file:///storage/emulated/0/Music/Album/song.flac"))
            )

        assertSame(InternalVolume, path.volume)
        assertEquals("Music/Album/song.flac", path.components.unixString)
    }

    private fun pathFactory(): DocumentPathFactory {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val volumeManager =
            object : VolumeManager {
                override fun getInternalVolume() = InternalVolume

                override fun getVolumes(): List<Volume> = emptyList()
            }
        return DocumentPathFactory.create(context, volumeManager)
    }

    private object InternalVolume : Volume.Internal {
        override val mediaStoreName = "external_primary"
        override val components = Components.parseUnix("/storage/emulated/0")

        override fun resolveName(context: Context) = "Internal storage"

        override fun isAccessible() = true
    }
}
