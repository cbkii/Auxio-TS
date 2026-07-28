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
        val claimed = requireNotNull(settings.claimPendingConfiguration())
        assertEquals(before + 1L, claimed.generation)
        assertEquals(SourceConfigurationCheckpoint.State.RUNNING, claimed.state)
        assertEquals(
            SourceConfigurationCheckpoint.State.RUNNING,
            settings.sourceConfigurationCheckpoint?.state,
        )

        settings.acknowledgeSourceConfiguration(
            generation = claimed.generation,
            unresolvedSourceKeys = emptySet(),
            outcome = "Success",
        )

        assertEquals(
            SourceConfigurationCheckpoint.State.COMMITTED,
            settings.sourceConfigurationCheckpoint?.state,
        )
    }

    @Test
    fun `running checkpoint remains claimable after process recreation`() {
        settings.forceLocationUpdate()
        val firstClaim = requireNotNull(settings.claimPendingConfiguration())

        val recreated = MusicSettingsImpl(context)
        val reclaimed = requireNotNull(recreated.claimPendingConfiguration())

        assertEquals(firstClaim.generation, reclaimed.generation)
        assertEquals(SourceConfigurationCheckpoint.State.RUNNING, reclaimed.state)
    }

    @Test
    fun `older scan cannot acknowledge a newer configuration`() {
        settings.forceLocationUpdate()
        val oldGeneration = requireNotNull(settings.claimPendingConfiguration()).generation
        settings.forceLocationUpdate()
        val currentGeneration = settings.sourceConfigurationGeneration

        settings.acknowledgeSourceConfiguration(oldGeneration, emptySet(), "Success")

        assertEquals(currentGeneration, settings.sourceConfigurationCheckpoint?.generation)
        assertEquals(
            SourceConfigurationCheckpoint.State.PENDING,
            settings.sourceConfigurationCheckpoint?.state,
        )
    }
}
