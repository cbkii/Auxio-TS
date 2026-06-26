/*
 * Copyright (c) 2026 Auxio Project
 * Ts18RawFastResumePolicyTest.kt is part of Auxio.
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

package org.oxycblt.auxio.headunit.ts18

import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.oxycblt.auxio.playback.state.RawPlaybackMetadata

class Ts18RawFastResumePolicyTest {
    @Test
    fun usbdisk0AndUsbdisk1PathsAreAllowed() {
        assertTrue(RawFastResumeValidator.isAllowedDirectPath("/storage/usbdisk0/Music/a.mp3"))
        assertTrue(RawFastResumeValidator.isAllowedDirectPath("/storage/usbdisk1/Music/a.flac"))
    }

    @Test
    fun unsafePathsAreRejected() {
        assertFalse(
            RawFastResumeValidator.isAllowedDirectPath("/mnt/media_rw/usbdisk0/Music/a.mp3")
        )
        assertFalse(RawFastResumeValidator.isAllowedDirectPath("/data/local/tmp/a.mp3"))
        assertFalse(RawFastResumeValidator.isAllowedDirectPath("/storage/usbdisk0/../secret/a.mp3"))
    }

    @Test
    fun audioExtensionPolicyIsConservative() {
        assertTrue(RawFastResumeValidator.hasAudioExtension("/storage/usbdisk0/Music/a.mp3"))
        assertTrue(RawFastResumeValidator.hasAudioExtension("/storage/usbdisk1/Music/a.FLAC"))
        assertFalse(RawFastResumeValidator.hasAudioExtension("/storage/usbdisk1/Music/a.txt"))
    }

    @Test
    fun sourcePolicySummarisesMixedVolumeState() {
        val states =
            listOf(
                Ts18SourceRepairStatePolicy.SourceState(
                    "/storage/usbdisk0",
                    Ts18SourceRepairStatePolicy.Kind.ALL_SOURCES_READY,
                    "ready",
                    Ts18SourceRepairStatePolicy.Action.NONE,
                ),
                Ts18SourceRepairStatePolicy.SourceState(
                    "/storage/usbdisk1",
                    Ts18SourceRepairStatePolicy.Kind.MOUNT_MISSING,
                    "missing",
                    Ts18SourceRepairStatePolicy.Action.REINSERT_USB,
                ),
            )
        assertTrue(
            Ts18SourceRepairStatePolicy.summarise(states) ==
                Ts18SourceRepairStatePolicy.Kind.MIXED_MULTIPLE_VOLUME_STATE
        )
    }

    @Test
    fun rawPlaybackMetadataUsesSafeFallbackDisplayValues() {
        val metadata =
            RawPlaybackMetadata(
                title = null,
                artist = null,
                album = null,
                uriString = "file:///storage/usbdisk0/Music/example.mp3",
                path = "/storage/usbdisk0/Music/example.mp3",
                durationMs = 120000L,
                positionMs = 1000L,
                isPlaying = true,
                savedAtMs = 1L,
            )

        assertEquals("example.mp3", metadata.displayTitle)
        assertEquals("USB audio", metadata.displayArtist)
    }

    @Test
    fun nestedAudioProbeFindsMusicInSubdirectories() {
        val root = Files.createTempDirectory("ts18-source-probe").toFile()
        try {
            val nested = root.resolve("Music/Artist")
            assertTrue(nested.mkdirs())
            val audio = nested.resolve("track.flac")
            assertTrue(audio.createNewFile())

            assertTrue(Ts18SourceRepairStatePolicy.hasAudioLikeWithinBoundedProbe(root))
        } finally {
            root.deleteRecursively()
        }
    }

    @Test
    fun fileUriPathUsesPathTitleFallback() {
        val item =
            RawFastResumeItem(
                uri = android.net.Uri.parse("file:///storage/usbdisk0/Music/example.mp3"),
                uriString = "file:///storage/usbdisk0/Music/example.mp3",
                path = "/storage/usbdisk0/Music/example.mp3",
                title = null,
                artist = null,
                album = null,
                durationMs = 120000L,
                positionMs = 1000L,
                savedAtMs = 1L,
            )

        assertEquals("example.mp3", item.toRawPlaybackMetadata(1000L, true).displayTitle)
    }

}
