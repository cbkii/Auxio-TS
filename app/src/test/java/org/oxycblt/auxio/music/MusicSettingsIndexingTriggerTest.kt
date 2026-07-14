/*
 * Copyright (c) 2026 Auxio Project
 * MusicSettingsIndexingTriggerTest.kt is part of Auxio.
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
import androidx.preference.PreferenceManager
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.oxycblt.auxio.R
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Regression coverage proving that the diagnostics-only performance-capture preference never
 * requests or starts a library reindex, while genuine scan-affecting settings still do.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
class MusicSettingsIndexingTriggerTest {
    private lateinit var context: Context
    private lateinit var settings: MusicSettingsImpl
    private lateinit var listener: RecordingListener

    private class RecordingListener : MusicSettings.Listener {
        var indexingSettingChanges = 0
        var locationChanges = 0
        var observingChanges = 0

        override fun onIndexingSettingChanged() {
            indexingSettingChanges++
        }

        override fun onMusicLocationsChanged() {
            locationChanges++
        }

        override fun onObservingChanged() {
            observingChanges++
        }
    }

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        settings = MusicSettingsImpl(context)
        listener = RecordingListener()
        settings.registerListener(listener)
    }

    private fun dispatch(key: String) {
        settings.onSharedPreferenceChanged(
            PreferenceManager.getDefaultSharedPreferences(context),
            key,
        )
    }

    @Test
    fun `performance capture toggle does not initiate a scan`() {
        dispatch(context.getString(R.string.set_key_performance_capture))
        assertEquals(0, listener.indexingSettingChanges)
        assertEquals(0, listener.locationChanges)
        assertEquals(0, listener.observingChanges)
    }

    @Test
    fun `scan-affecting settings still request a reindex`() {
        dispatch(context.getString(R.string.set_key_scan_priority))
        dispatch(context.getString(R.string.set_key_root_access_policy))
        dispatch(context.getString(R.string.set_key_separators))
        assertEquals(3, listener.indexingSettingChanges)
    }

    @Test
    fun `observation settings dispatch observing changes only`() {
        dispatch(context.getString(R.string.set_key_observation_mode))
        assertEquals(0, listener.indexingSettingChanges)
        assertEquals(1, listener.observingChanges)
    }
}
