/*
 * Copyright (c) 2026 Auxio Project
 * MetadataExtractorFailureClassificationTest.kt is part of Auxio.
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

package org.oxycblt.musikr.metadata

import android.content.ContentProviderClient
import android.content.ContentResolver
import android.content.Context
import android.database.MatrixCursor
import android.net.Uri
import io.mockk.every
import io.mockk.mockk
import java.io.FileNotFoundException
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.oxycblt.musikr.fs.AddedMs
import org.oxycblt.musikr.fs.Components
import org.oxycblt.musikr.fs.File
import org.oxycblt.musikr.fs.Path
import org.oxycblt.musikr.fs.Volume
import org.oxycblt.musikr.library.MetadataProfile
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
class MetadataExtractorFailureClassificationTest {
    private val uri = Uri.parse("content://media/external/audio/media/42")
    private val resolver = mockk<ContentResolver>()
    private val context = mockk<Context> { every { contentResolver } returns resolver }

    @Test
    fun `lean extractor treats missing provider as provider failure`() = runBlocking {
        val failure = FileNotFoundException("No content provider: $uri")
        every { resolver.openAssetFileDescriptor(uri, "r") } throws failure
        every { resolver.acquireUnstableContentProviderClient(uri) } returns null

        val result = MetadataExtractor.from(context, MetadataProfile.LEAN).extract(mediaFile())

        assertTrue(result is MetadataResult.ProviderFailed)
        result as MetadataResult.ProviderFailed
        assertEquals(FileNotFoundException::class.java.name, result.failureClass)
        assertEquals(failure.message, result.failureMessage)
    }

    @Test
    fun `full extractor treats missing provider as provider failure`() = runBlocking {
        val failure = FileNotFoundException("No content provider: $uri")
        every { resolver.openFileDescriptor(uri, "r") } throws failure
        every { resolver.acquireUnstableContentProviderClient(uri) } returns null

        val result = MetadataExtractor.from(context, MetadataProfile.FULL).extract(mediaFile())

        assertTrue(result is MetadataResult.ProviderFailed)
    }

    @Test
    fun `reachable provider with absent exact row confirms item disappearance`() = runBlocking {
        val client = mockk<ContentProviderClient>(relaxed = true)
        val cursor = MatrixCursor(arrayOf("_id"))
        every { resolver.openAssetFileDescriptor(uri, "r") } throws
            FileNotFoundException(uri.toString())
        every { resolver.acquireUnstableContentProviderClient(uri) } returns client
        every { client.query(uri, null, null, null, null) } returns cursor

        val result = MetadataExtractor.from(context, MetadataProfile.LEAN).extract(mediaFile())

        assertSame(MetadataResult.ItemUnavailable, result)
    }

    @Test
    fun `descriptor failure with row still present fails provider instead of deleting item`() =
        runBlocking {
            val client = mockk<ContentProviderClient>(relaxed = true)
            val cursor = MatrixCursor(arrayOf("_id")).apply { addRow(arrayOf(42L)) }
            every { resolver.openAssetFileDescriptor(uri, "r") } throws
                FileNotFoundException(uri.toString())
            every { resolver.acquireUnstableContentProviderClient(uri) } returns client
            every { client.query(uri, null, null, null, null) } returns cursor

            val result = MetadataExtractor.from(context, MetadataProfile.LEAN).extract(mediaFile())

            assertTrue(result is MetadataResult.ProviderFailed)
        }

    private fun mediaFile() =
        File(
            uri = uri,
            path =
                Path(
                    Volume.ThirdParty(Uri.parse("content://media/external")),
                    Components.parseUnix("42"),
                ),
            addedMs =
                object : AddedMs {
                    override suspend fun resolve(): Long? = 1L
                },
            modifiedMs = 1L,
            mimeType = "audio/mpeg",
            size = 1L,
            parent = null,
            sourceKey = "internal:external",
        )
}
