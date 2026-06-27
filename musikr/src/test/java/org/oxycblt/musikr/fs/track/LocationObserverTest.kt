/*
 * Copyright (c) 2026 Auxio Project
 * LocationObserverTest.kt is part of Auxio.
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

package org.oxycblt.musikr.fs.track

import android.net.Uri
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class LocationObserverTest {
    @Test
    fun rejectsBlankAuthorityContentUri() {
        assertFalse(LocationObserver.isObservableContentUri(Uri.parse("content:///music")))
    }

    @Test
    fun rejectsEmptyAndMalformedSourceValues() {
        assertFalse(LocationObserver.isObservableContentUri(Uri.EMPTY))
        assertFalse(LocationObserver.isObservableContentUri(Uri.parse("/storage/usbdisk0/Music")))
    }

    @Test
    fun rejectsFileAndDirectFsPaths() {
        assertFalse(
            LocationObserver.isObservableContentUri(
                Uri.parse("file:///storage/usbdisk1/Music/song.mp3")
            )
        )
        assertFalse(
            LocationObserver.isObservableContentUri(Uri.parse("/storage/usbdisk1/Music/song.mp3"))
        )
    }

    @Test
    fun acceptsUnknownProviderForRegistrationAttempt() {
        assertTrue(
            LocationObserver.isObservableContentUri(
                Uri.parse("content://missing.example.provider/tree/primary%3AMusic")
            )
        )
    }

    @Test
    fun constructingObserverForMissingProviderDoesNotCrashOrInvokeCallback() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        var callbacks = 0

        val observer =
            LocationObserver(context, Uri.parse("content://missing.example.provider/tree/stale")) {
                callbacks++
            }

        observer.release()
        assertEquals(0, callbacks)
    }

    @Test
    fun acceptsMediaProviderUri() {
        assertTrue(
            LocationObserver.isObservableContentUri(
                Uri.parse("content://media/external/audio/media")
            )
        )
    }
}
