/*
 * Copyright (c) 2026 Auxio Project
 * CarPreferencesContractTest.kt is part of Auxio.
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

package org.oxycblt.auxio.headunit

import java.io.File
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CarPreferencesContractTest {
    @Test
    fun `keep playback ready lives in advanced car preferences only`() {
        val resDir = resolveAppResDir()
        assertTrue("Missing app resource directory: ${resDir.path}", resDir.isDirectory)

        val carPreferences = File(resDir, "xml/preferences_car.xml")
        val audioPreferences = File(resDir, "xml/preferences_audio.xml")
        assertTrue("Missing car preferences XML", carPreferences.isFile)
        assertTrue("Missing audio preferences XML", audioPreferences.isFile)

        val keyMarker = "app:key=\"auxio_keep_playback_ready\""
        val carXml = carPreferences.readText()
        val audioXml = audioPreferences.readText()

        assertTrue(
            "Car settings should expose keepPlaybackReady after the move",
            keyMarker in carXml,
        )
        assertTrue(
            "keepPlaybackReady should stay in the advanced car section",
            "<PreferenceCategory app:title=\"@string/set_advanced\">" in carXml &&
                carXml.indexOf("<PreferenceCategory app:title=\"@string/set_advanced\">") <
                    carXml.indexOf(keyMarker),
        )
        assertFalse(
            "Audio settings should no longer expose keepPlaybackReady",
            keyMarker in audioXml,
        )
    }

    private fun resolveAppResDir(): File {
        val workingDir = File(System.getProperty("user.dir").orEmpty())
        val moduleRelative = File(workingDir, "src/main/res")
        if (moduleRelative.isDirectory) return moduleRelative
        return File(workingDir, "app/src/main/res")
    }
}
