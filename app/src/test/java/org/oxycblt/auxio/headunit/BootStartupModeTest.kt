/*
 * Copyright (c) 2026 Auxio Project
 * BootStartupModeTest.kt is part of Auxio.
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

import android.content.Context
import androidx.preference.PreferenceManager
import androidx.test.core.app.ApplicationProvider
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.junit.runner.RunWith
import org.oxycblt.auxio.R
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
class BootStartupModeTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    private val startupKey = context.getString(R.string.set_key_head_unit_startup_mode)
    private val bootKey = context.getString(R.string.set_key_autostart_on_boot)
    private val floatingKey = context.getString(R.string.set_key_autostart_floating_only)

    @Test
    fun `legacy full-player choice migrates once and stays idempotent`() {
        prefs.edit().clear().putBoolean(bootKey, true).putBoolean(floatingKey, false).commit()

        assertEquals(BootStartupMode.FULL_PLAYER, BootStartupMode.resolve(prefs, context))
        assertEquals("open_auxio", prefs.getString(startupKey, null))
        assertEquals(BootStartupMode.FULL_PLAYER, BootStartupMode.resolve(prefs, context))
    }

    @Test
    fun `explicit background ready persists mirrored legacy flags`() {
        prefs.edit().clear().commit()

        BootStartupMode.persist(prefs, context, BootStartupMode.BACKGROUND_READY)

        assertEquals(BootStartupMode.BACKGROUND_READY, BootStartupMode.resolve(prefs, context))
        assertTrue(prefs.getBoolean(bootKey, false))
        assertFalse(prefs.getBoolean(floatingKey, true))
    }

    @Test
    fun `legacy floating-only choice is preserved`() {
        prefs.edit().clear().putBoolean(bootKey, true).putBoolean(floatingKey, true).commit()

        assertEquals(BootStartupMode.FLOATING_CONTROLS_ONLY, BootStartupMode.resolve(prefs, context))
        assertEquals("floating_only", prefs.getString(startupKey, null))
    }
}
