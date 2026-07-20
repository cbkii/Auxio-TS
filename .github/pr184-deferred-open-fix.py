#!/usr/bin/env python3
from __future__ import annotations

from pathlib import Path
import re

SOURCE = Path(
    "app/src/main/java/org/oxycblt/auxio/playback/service/ExoPlaybackStateHolder.kt"
)
TEST = Path(
    "app/src/test/java/org/oxycblt/auxio/playback/service/DeferredOpenIdentityTest.kt"
)

replacement = r'''    private suspend fun findDeferredOpenSong(
        action: DeferredPlayback.Open,
        library: Library,
    ): Song? =
        withContext(Dispatchers.IO) {
            library.songs
                .firstOrNull { song ->
                    deferredOpenIdentityMatches(action.uri, song.uri, song.path.toString())
                }
                ?.let { return@withContext it }

            val scheme = action.uri.scheme?.lowercase()
            if (scheme.isNullOrBlank() || scheme == "file") return@withContext null

            try {
                context.applicationContext.contentResolver
                    .query(
                        action.uri,
                        arrayOf(OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE),
                        null,
                        null,
                        null,
                    )
                    ?.use { cursor ->
                        val displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                        if (displayNameIndex == -1 || sizeIndex == -1 || !cursor.moveToFirst()) {
                            return@use null
                        }
                        val displayName = cursor.getString(displayNameIndex) ?: return@use null
                        val size = cursor.getLong(sizeIndex)
                        library.songs.find { it.path.name == displayName && it.size == size }
                    }
            } catch (e: SecurityException) {
                L.w(e, "No permission to resolve opened file ${action.uri}")
                null
            } catch (e: Exception) {
                L.w(e, "Unable to resolve opened file ${action.uri}")
                null
            }
        }
'''

helper = r'''

/** Matches a deferred direct-open request to an already hydrated library song. */
internal fun deferredOpenIdentityMatches(actionUri: Uri, songUri: Uri, songPath: String): Boolean {
    if (songUri == actionUri) return true

    val actionScheme = actionUri.scheme?.lowercase()
    if (!actionScheme.isNullOrBlank() && actionScheme != "file") return false

    val actionPath = actionUri.path?.takeIf { it.isNotBlank() } ?: actionUri.toString()
    val songScheme = songUri.scheme?.lowercase()
    val directSongUri = songScheme.isNullOrBlank() || songScheme == "file"
    return (directSongUri && songUri.path == actionPath) || songPath == actionPath
}
'''

test_content = r'''/*
 * Copyright (c) 2026 Auxio Project
 * DeferredOpenIdentityTest.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.oxycblt.auxio.playback.service

import android.net.Uri
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DeferredOpenIdentityTest {
    @Test
    fun `exact content uri resolves without provider metadata`() {
        val uri = Uri.parse("content://fixture.audio/songs/10")

        assertTrue(deferredOpenIdentityMatches(uri, uri, "/storage/usbdisk0/song.wav"))
    }

    @Test
    fun `benchmark file uri resolves by exact identity`() {
        val uri = Uri.parse("file:/data/user/0/org.oxycblt.auxio/files/benchmark-tone-0.wav")

        assertTrue(deferredOpenIdentityMatches(uri, uri, "/storage/usbdisk0/benchmark-tone-0.wav"))
    }

    @Test
    fun `plain direct path resolves against file uri and display path`() {
        val action = Uri.parse("/storage/usbdisk0/Music/song.flac")
        val songUri = Uri.parse("file:///storage/usbdisk0/Music/song.flac")

        assertTrue(
            deferredOpenIdentityMatches(
                action,
                songUri,
                "/storage/usbdisk0/Music/song.flac",
            )
        )
    }

    @Test
    fun `different content uri is not matched only by provider path`() {
        val action = Uri.parse("content://provider-a/audio/10")
        val songUri = Uri.parse("content://provider-b/audio/10")

        assertFalse(deferredOpenIdentityMatches(action, songUri, "/audio/10"))
    }
}
'''

text = SOURCE.read_text(encoding="utf-8")
pattern = re.compile(
    r"    private suspend fun findDeferredOpenSong\(.*?\n"
    r"(?=    private fun completeRestore\()",
    re.DOTALL,
)
text, count = pattern.subn(replacement + "\n", text, count=1)
if count != 1:
    raise SystemExit(f"Expected one findDeferredOpenSong block, replaced {count}")

if "internal fun deferredOpenIdentityMatches(" not in text:
    text = text.rstrip() + helper + "\n"

SOURCE.write_text(text, encoding="utf-8", newline="\n")
TEST.parent.mkdir(parents=True, exist_ok=True)
TEST.write_text(test_content, encoding="utf-8", newline="\n")
