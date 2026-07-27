/*
 * Copyright (c) 2026 Auxio Project
 * MusicSourceConfigurationTest.kt is part of Auxio.
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

package org.oxycblt.auxio.music

import android.content.Context
import android.net.Uri
import androidx.preference.PreferenceManager
import androidx.test.core.app.ApplicationProvider
import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.oxycblt.auxio.music.locations.LocationMode
import org.oxycblt.musikr.fs.Location
import org.oxycblt.musikr.fs.saf.SAF
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
class MusicSourceConfigurationTest {
    private lateinit var context: Context
    private lateinit var settings: MusicSettingsImpl

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        PreferenceManager.getDefaultSharedPreferences(context).edit().clear().commit()
        settings = MusicSettingsImpl(context)
    }

    @Test
    fun `source configuration is durable and queues one initial scan`() {
        settings.lastScanFailed = true
        val source =
            requireNotNull(
                Location.Unopened.from(context, Uri.fromFile(File("/storage/emulated/0/Music")))
                    .open(context)
            )
        val query =
            SAF.Query(
                source = listOf(source),
                exclude = emptyList(),
                withHidden = false,
                multithread = true,
            )
        val before = settings.sourceConfigurationGeneration

        assertTrue(
            settings.applySourceConfiguration(
                LocationMode.DIRECT_FS,
                query,
                settings.mediaStoreQuery,
            )
        )
        assertEquals(LocationMode.DIRECT_FS, settings.locationMode)
        assertEquals(1, settings.configuredSourceCount)
        assertEquals(before + 1L, settings.sourceConfigurationGeneration)
        assertFalse(settings.lastScanFailed)
        assertEquals(before + 1L, settings.consumePendingInitialScan())
        assertNull(settings.consumePendingInitialScan())
    }
}
