/*
 * Copyright (c) 2026 Auxio Project
 * PerformanceCapturePreferenceRestoreTest.kt is part of Auxio.
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
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.oxycblt.auxio.R
import org.oxycblt.auxio.util.PerfTimer
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
class PerformanceCapturePreferenceRestoreTest {
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        PreferenceManager.getDefaultSharedPreferences(context).edit().clear().commit()
        PerfTimer.configure(false)
    }

    @After
    fun tearDown() {
        PreferenceManager.getDefaultSharedPreferences(context).edit().clear().commit()
        PerfTimer.configure(false)
    }

    @Test
    fun `settings construction restores persisted performance capture preference`() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val key = context.getString(R.string.set_key_performance_capture)
        preferences.edit().putBoolean(key, true).commit()

        MusicSettingsImpl(context)
        assertTrue(PerfTimer.isExplicitlyConfigured())

        preferences.edit().putBoolean(key, false).commit()
        PerfTimer.configure(true)
        MusicSettingsImpl(context)
        assertFalse(PerfTimer.isExplicitlyConfigured())
    }
}
