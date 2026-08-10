/*
 * Copyright (c) 2026 Auxio Project
 * ImageSettingsTest.kt is part of Auxio.
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

package org.oxycblt.auxio.image

import android.content.Context
import androidx.preference.PreferenceManager
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.oxycblt.auxio.IntegerTable
import org.oxycblt.auxio.R
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
class ImageSettingsTest {
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        PreferenceManager.getDefaultSharedPreferences(context).edit().clear().commit()
    }

    @After
    fun tearDown() {
        PreferenceManager.getDefaultSharedPreferences(context).edit().clear().commit()
    }

    @Test
    fun `explicit legacy hide covers migrates to off`() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs.edit().putBoolean("KEY_SHOW_COVERS", false).commit()

        val settings = ImageSettingsImpl(context)
        settings.migrate()

        assertEquals(CoverMode.OFF, settings.coverMode)
    }

    @Test
    fun `unknown current value fails open to optimised`() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs.edit().putInt(context.getString(R.string.set_key_cover_mode), 123456789).commit()

        val settings = ImageSettingsImpl(context)
        settings.migrate()

        assertEquals(CoverMode.OPTIMISED, settings.coverMode)
    }

    @Test
    fun `legacy high quality remains artwork enabled`() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs.edit().putInt("auxio_cover_mode", IntegerTable.COVER_MODE_HIGH_QUALITY).commit()

        val settings = ImageSettingsImpl(context)
        settings.migrate()

        assertEquals(CoverMode.OPTIMISED, settings.coverMode)
    }

    @Test
    fun `explicit legacy cover mode off remains off`() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs.edit().putInt("auxio_cover_mode", IntegerTable.COVER_MODE_OFF).commit()

        val settings = ImageSettingsImpl(context)
        settings.migrate()

        assertEquals(CoverMode.OFF, settings.coverMode)
    }

    @Test
    fun `explicit current off remains off`() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs
            .edit()
            .putInt(context.getString(R.string.set_key_cover_mode), IntegerTable.COVER_MODE_OFF)
            .commit()

        val settings = ImageSettingsImpl(context)
        settings.migrate()

        assertEquals(CoverMode.OFF, settings.coverMode)
    }
}
