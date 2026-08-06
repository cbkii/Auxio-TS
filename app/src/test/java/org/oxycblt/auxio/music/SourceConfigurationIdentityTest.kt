/*
 * Copyright (c) 2026 Auxio Project
 * SourceConfigurationIdentityTest.kt is part of Auxio.
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
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import androidx.test.core.app.ApplicationProvider
import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.oxycblt.auxio.IntegerTable
import org.oxycblt.auxio.R
import org.oxycblt.auxio.music.locations.LocationMode
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * The source configuration revision decides whether every committed source generation is discarded,
 * so it must key on the effective traversal contract and on nothing else.
 *
 * Preferences are written directly rather than through the query setters so each assertion isolates
 * exactly one field of the identity material.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
class SourceConfigurationIdentityTest {
    private lateinit var context: Context
    private lateinit var settings: MusicSettingsImpl

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        PreferenceManager.getDefaultSharedPreferences(context).edit().clear().commit()
        settings = MusicSettingsImpl(context)
        settings.locationMode = LocationMode.DIRECT_FS
    }

    @Test
    fun `the indexing and browse lanes share one definition`() {
        seedSources("/storage/emulated/0/Music", "/storage/usbdisk0/Audio")

        assertEquals(
            SourceConfigurationIdentity.revision(settings),
            ConfiguredSourcePolicy(settings).snapshot().configurationRevision,
        )
    }

    @Test
    fun `identity is independent of source order and alias spelling`() {
        seedSources("/storage/emulated/0/Music", "/storage/usbdisk0/Audio")
        val baseline = SourceConfigurationIdentity.revision(settings)

        seedSources("/storage/usbdisk0/Audio", "/storage/emulated/0/Music")
        assertEquals(baseline, SourceConfigurationIdentity.revision(settings))

        seedSources("/mnt/media_rw/usbdisk0/Audio", "/sdcard/Music/")
        assertEquals(baseline, SourceConfigurationIdentity.revision(settings))

        // A duplicate entry of an already configured folder is the same effective configuration.
        seedSources("/storage/emulated/0/Music", "/storage/usbdisk0/Audio", "/sdcard/Music")
        assertEquals(baseline, SourceConfigurationIdentity.revision(settings))
    }

    @Test
    fun `traversal-affecting material produces a new identity`() {
        seedSources("/storage/emulated/0/Music")
        val baseline = SourceConfigurationIdentity.revision(settings)

        seedSources("/storage/emulated/0/Music", "/storage/usbdisk0/Audio")
        assertNotEquals(baseline, SourceConfigurationIdentity.revision(settings))

        seedSources("/storage/emulated/0/Music")
        assertEquals(baseline, SourceConfigurationIdentity.revision(settings))

        putBoolean(R.string.set_key_with_hidden, true)
        assertNotEquals(baseline, SourceConfigurationIdentity.revision(settings))
        putBoolean(R.string.set_key_with_hidden, false)

        seedExclusions("/storage/emulated/0/Music/Podcasts")
        assertNotEquals(baseline, SourceConfigurationIdentity.revision(settings))
    }

    @Test
    fun `switching backend mode produces a new identity`() {
        seedSources("/storage/emulated/0/Music")
        val directFs = SourceConfigurationIdentity.revision(settings)

        settings.locationMode = LocationMode.SAF
        assertNotEquals(directFs, SourceConfigurationIdentity.revision(settings))

        settings.locationMode = LocationMode.MEDIA_STORE
        assertNotEquals(directFs, SourceConfigurationIdentity.revision(settings))
    }

    @Test
    fun `tag interpretation settings never invalidate filesystem authority`() {
        seedSources("/storage/emulated/0/Music")
        val baseline = SourceConfigurationIdentity.revision(settings)

        // Cached rows hold raw tags; separators and sorting are applied while the library is built,
        // so changing them must refresh the library without re-enumerating any source.
        settings.separators = ";/"
        assertEquals(baseline, SourceConfigurationIdentity.revision(settings))

        putBoolean(R.string.set_key_auto_sort_names, false)
        assertEquals(baseline, SourceConfigurationIdentity.revision(settings))
    }

    @Test
    fun `resource and scheduling settings never invalidate filesystem authority`() {
        seedSources("/storage/emulated/0/Music")
        val baseline = SourceConfigurationIdentity.revision(settings)

        putBoolean(R.string.set_key_saf_multithread, false)
        assertEquals(baseline, SourceConfigurationIdentity.revision(settings))

        putString(R.string.set_key_scan_priority, ScanPriority.FAST_SCAN.name)
        assertEquals(baseline, SourceConfigurationIdentity.revision(settings))

        putString(R.string.set_key_observation_mode, ObservationMode.CONTINUOUS.name)
        assertEquals(baseline, SourceConfigurationIdentity.revision(settings))

        putBoolean(R.string.set_key_generated_playlists, true)
        assertEquals(baseline, SourceConfigurationIdentity.revision(settings))
    }

    @Test
    fun `mode-scoped material only counts inside its own mode`() {
        seedSources("/storage/emulated/0/Music")
        val directFsBaseline = SourceConfigurationIdentity.revision(settings)

        // DirectFS traversal never consults the provider filter or the TS18 path filter.
        putBoolean(R.string.set_key_exclude_non_music, false)
        assertEquals(directFsBaseline, SourceConfigurationIdentity.revision(settings))
        settings.ts18SystemSourceFilter = !settings.ts18SystemSourceFilter
        assertEquals(directFsBaseline, SourceConfigurationIdentity.revision(settings))

        // Root preparation only ever produces DirectFS roots, so it counts here and nowhere else.
        putString(R.string.set_key_root_access_policy, RootAccessPolicy.OFF.name)
        assertNotEquals(directFsBaseline, SourceConfigurationIdentity.revision(settings))

        settings.locationMode = LocationMode.MEDIA_STORE
        val mediaStoreBaseline = SourceConfigurationIdentity.revision(settings)
        putString(R.string.set_key_root_access_policy, RootAccessPolicy.ON_DEMAND.name)
        assertEquals(mediaStoreBaseline, SourceConfigurationIdentity.revision(settings))

        putBoolean(R.string.set_key_exclude_non_music, true)
        assertNotEquals(mediaStoreBaseline, SourceConfigurationIdentity.revision(settings))
    }

    @Test
    fun `provider filter material counts in MediaStore mode`() {
        settings.locationMode = LocationMode.MEDIA_STORE
        val baseline = SourceConfigurationIdentity.revision(settings)

        putInt(R.string.set_key_filter_mode, IntegerTable.FILTER_MODE_INCLUDE)
        assertNotEquals(baseline, SourceConfigurationIdentity.revision(settings))
        putInt(R.string.set_key_filter_mode, IntegerTable.FILTER_MODE_EXCLUDE)
        assertEquals(baseline, SourceConfigurationIdentity.revision(settings))

        putString(
            R.string.set_key_filtered_locations,
            Uri.fromFile(File("/storage/emulated/0/Podcasts")).toString(),
        )
        assertNotEquals(baseline, SourceConfigurationIdentity.revision(settings))

        // The TS18 path filter narrows the provider heuristic, so it is authoritative here.
        putString(R.string.set_key_filtered_locations, "")
        settings.ts18SystemSourceFilter = !settings.ts18SystemSourceFilter
        assertNotEquals(baseline, SourceConfigurationIdentity.revision(settings))
    }

    private fun seedSources(vararg paths: String) =
        putString(R.string.set_key_music_locations, serialize(paths))

    private fun seedExclusions(vararg paths: String) =
        putString(R.string.set_key_excluded_locations, serialize(paths))

    private fun serialize(paths: Array<out String>) =
        paths.joinToString(";") { Uri.fromFile(File(it)).toString() }

    private fun putString(key: Int, value: String) {
        PreferenceManager.getDefaultSharedPreferences(context).edit(commit = true) {
            putString(context.getString(key), value)
        }
    }

    private fun putBoolean(key: Int, value: Boolean) {
        PreferenceManager.getDefaultSharedPreferences(context).edit(commit = true) {
            putBoolean(context.getString(key), value)
        }
    }

    private fun putInt(key: Int, value: Int) {
        PreferenceManager.getDefaultSharedPreferences(context).edit(commit = true) {
            putInt(context.getString(key), value)
        }
    }
}
