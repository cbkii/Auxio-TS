/*
 * Copyright (c) 2026 Auxio Project
 * ExploreStepFileClassificationTest.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.oxycblt.musikr.pipeline

import android.net.Uri
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.oxycblt.musikr.fs.AddedMs
import org.oxycblt.musikr.fs.Components
import org.oxycblt.musikr.fs.File
import org.oxycblt.musikr.fs.Path
import org.oxycblt.musikr.fs.Volume

class ExploreStepFileClassificationTest {
    @Test
    fun acceptsKnownAudioMimeTypes() {
        assertTrue(FileClassification.isPotentialMusicFile(testFile("song.mp3", "audio/mpeg")))
        assertTrue(FileClassification.isPotentialMusicFile(testFile("song.ogg", "application/ogg")))
    }

    @Test
    fun acceptsOctetStreamOnlyWhenAudioExtensionIsKnown() {
        assertTrue(
            FileClassification.isPotentialMusicFile(testFile("usb-track.flac", "application/octet-stream"))
        )
        assertTrue(
            FileClassification.isPotentialMusicFile(testFile("usb-track.M4A", "application/octet-stream"))
        )
        assertFalse(
            FileClassification.isPotentialMusicFile(testFile("album-art.jpg", "application/octet-stream"))
        )
        assertFalse(
            FileClassification.isPotentialMusicFile(testFile("readme", "application/octet-stream"))
        )
    }

    @Test
    fun rejectsKnownNonAudioMimeTypesAndPlaylists() {
        assertFalse(FileClassification.isPotentialMusicFile(testFile("cover.jpg", "image/jpeg")))
        assertFalse(FileClassification.isPotentialMusicFile(testFile("playlist.m3u", "audio/x-mpegurl")))
    }

    private fun testFile(name: String, mimeType: String): File {
        val rootUri = Uri.parse("file:///storage/usbdisk0/Music")
        return File(
            uri = Uri.parse("$rootUri/$name"),
            path = Path(Volume.ThirdParty(rootUri), Components.root().child(name)),
            addedMs =
                object : AddedMs {
                    override suspend fun resolve(): Long? = null
                },
            modifiedMs = 0L,
            mimeType = mimeType,
            size = 1L,
            parent = null,
        )
    }
}
