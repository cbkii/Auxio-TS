/*
 * Copyright (c) 2026 Auxio Project
 * MusicSourceCanonicalizationTest.kt is part of Auxio.
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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.oxycblt.auxio.R
import org.oxycblt.auxio.music.locations.LocationAdapter
import org.oxycblt.auxio.music.locations.LocationMode
import org.oxycblt.auxio.music.locations.MusicSourceCanonicalizer
import org.oxycblt.musikr.fs.CanonicalSourcePolicy
import org.oxycblt.musikr.fs.Location
import org.oxycblt.musikr.fs.mediastore.MediaStore
import org.oxycblt.musikr.fs.saf.SAF
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Canonical-source proofs for the persistence and picker boundaries.
 *
 * The physical TS18 campaign that motivated this work ended with `configuredSourceCount=2` where
 * both entries were `/storage/emulated/0/Music`, so these tests assert on the effective unique
 * configuration rather than on whatever happened to be serialised.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
class MusicSourceCanonicalizationTest {
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
    fun `duplicate sources are never persisted`() {
        val music = location("/storage/emulated/0/Music")

        assertTrue(apply(listOf(music, music)))

        assertEquals(1, settings.configuredSourceCount)
        assertEquals(1, settings.safQuery.source.size)
        assertEquals(1, settings.configuredSourceSpecs.size)
    }

    @Test
    fun `aliases of one folder are one persisted source`() {
        assertTrue(
            apply(
                listOf(
                    location("/storage/emulated/0/Music"),
                    location("/sdcard/Music"),
                    location("/storage/emulated/0/Music/"),
                )
            )
        )

        assertEquals(1, settings.configuredSourceCount)
        assertEquals(
            "/storage/emulated/0/Music",
            settings.configuredSourceSpecs.single().displayPath,
        )
    }

    @Test
    fun `re-selecting the same folders is not a configuration change`() {
        val music = location("/storage/emulated/0/Music")
        assertTrue(apply(listOf(music)))
        val generation = settings.sourceConfigurationGeneration

        assertFalse(apply(listOf(music, music)))
        assertFalse(apply(listOf(location("/sdcard/Music"))))

        assertEquals(generation, settings.sourceConfigurationGeneration)
    }

    @Test
    fun `persisted duplicates are migrated once without a new generation`() {
        seedRawSources("/storage/emulated/0/Music", "/storage/emulated/0/Music")
        val generation = settings.sourceConfigurationGeneration

        assertEquals(1, settings.configuredSourceCount)
        assertEquals(generation, settings.sourceConfigurationGeneration)
        assertEquals("file:///storage/emulated/0/Music", rawSources())

        // Idempotent: reading again neither changes the stored value nor the generation.
        assertEquals(1, settings.configuredSourceCount)
        assertEquals(1, settings.safQuery.source.size)
        assertEquals(1, settings.configuredSourceSpecs.size)
        assertEquals(generation, settings.sourceConfigurationGeneration)
        assertEquals("file:///storage/emulated/0/Music", rawSources())
    }

    @Test
    fun `persisted aliases of one folder are migrated to one source`() {
        seedRawSources("/sdcard/Music", "/storage/emulated/0/Music/")

        assertEquals(1, settings.configuredSourceCount)
        assertEquals("file:///storage/emulated/0/Music", rawSources())
    }

    @Test
    fun `lone persisted alias is repaired idempotently without a new generation`() {
        seedRawSources("/sdcard/Music")
        val generation = settings.sourceConfigurationGeneration

        assertEquals(1, settings.configuredSourceCount)
        assertEquals("file:///storage/emulated/0/Music", rawSources())
        assertEquals(generation, settings.sourceConfigurationGeneration)

        assertEquals(1, settings.configuredSourceCount)
        assertEquals("file:///storage/emulated/0/Music", rawSources())
        assertEquals(generation, settings.sourceConfigurationGeneration)
    }

    @Test
    fun `direct saf setter stores canonical sources exclusions and origin metadata`() {
        val canonicalKey = "path:/storage/emulated/0/Music"
        settings.safQuery =
            SAF.Query(
                source = listOf(location("/sdcard/Music"), location("/storage/emulated/0/Music/")),
                exclude =
                    listOf(
                        unopened("/mnt/media_rw/usbdisk0/Podcasts"),
                        unopened("/storage/USBDISK0/Podcasts/"),
                    ),
                withHidden = true,
                multithread = false,
                sourceOrigins =
                    mapOf(canonicalKey to CanonicalSourcePolicy.Origin.AUTOMATIC_SUGGESTION),
            )

        assertEquals("file:///storage/emulated/0/Music", rawSources())
        assertEquals("file:///storage/usbdisk0/Podcasts", rawExclusions())
        val query = settings.safQuery
        assertEquals(listOf("/storage/emulated/0/Music"), query.source.map { it.uri.path })
        assertEquals(listOf("/storage/usbdisk0/Podcasts"), query.exclude.map { it.uri.path })
        assertEquals(
            CanonicalSourcePolicy.Origin.AUTOMATIC_SUGGESTION,
            query.sourceOrigins[canonicalKey],
        )
        assertTrue(query.withHidden)
    }

    @Test
    fun `explicit volume origin remains distinct from legacy fallback`() {
        val volume = location("/storage/usbdisk0")
        val key = "path:/storage/usbdisk0"
        settings.safQuery =
            query(listOf(volume))
                .copy(sourceOrigins = mapOf(key to CanonicalSourcePolicy.Origin.EXPLICIT))

        assertEquals(CanonicalSourcePolicy.Origin.EXPLICIT, settings.safQuery.sourceOrigins[key])

        PreferenceManager.getDefaultSharedPreferences(context).edit(commit = true) {
            remove("auxio_source_origins")
        }
        assertEquals(
            CanonicalSourcePolicy.Origin.WHOLE_VOLUME_FALLBACK,
            settings.safQuery.sourceOrigins[key],
        )
    }

    @Test
    fun `canonical setter preserves first selection order`() {
        settings.safQuery =
            query(
                listOf(
                    location("/mnt/media_rw/usbdisk0/Audio"),
                    location("/sdcard/Music"),
                    location("/storage/usbdisk0/Audio/"),
                )
            )

        assertEquals(
            "file:///storage/usbdisk0/Audio;file:///storage/emulated/0/Music",
            rawSources(),
        )
    }

    @Test
    fun `distinct sources survive migration in selection order`() {
        seedRawSources(
            "/storage/emulated/0/Music",
            "/storage/usbdisk0/Audio",
            "/storage/emulated/0/Music",
        )

        assertEquals(2, settings.configuredSourceCount)
        assertEquals(
            listOf("/storage/emulated/0/Music", "/storage/usbdisk0/Audio"),
            settings.configuredSourceSpecs.map { it.displayPath },
        )
    }

    @Test
    fun `source key selection cannot expand back into duplicate roots`() {
        seedRawSources("/storage/emulated/0/Music", "/sdcard/Music")

        val specs = settings.configuredSourceSpecs
        assertEquals(1, specs.size)
        assertEquals(1, specs.map { it.sourceKey }.toSet().size)
        assertEquals(1, specs.map { it.canonicalKey }.toSet().size)
    }

    @Test
    fun `configuration revision is stable across semantically equal aliases`() {
        val policy = ConfiguredSourcePolicy(settings)

        seedRawSources("/storage/emulated/0/Music", "/storage/usbdisk0/Audio")
        val first = policy.snapshot().configurationRevision

        seedRawSources("/sdcard/Music/", "/mnt/media_rw/usbdisk0/Audio")
        val aliased = policy.snapshot().configurationRevision

        seedRawSources("/storage/emulated/0")
        val different = policy.snapshot().configurationRevision

        assertEquals(first, aliased)
        assertNotEquals(first, different)
    }

    @Test
    fun `configuration revision changes only with effective traversal policy`() {
        val music = location("/storage/emulated/0/Music")
        val excluded = unopened("/storage/emulated/0/Music/Podcasts")
        val policy = ConfiguredSourcePolicy(settings)
        settings.safQuery = query(listOf(music))
        val baseline = policy.snapshot().configurationRevision

        settings.safQuery = query(listOf(location("/sdcard/Music"), music))
        assertEquals(baseline, policy.snapshot().configurationRevision)

        settings.safQuery = query(listOf(music)).copy(multithread = true)
        assertEquals(baseline, policy.snapshot().configurationRevision)

        settings.safQuery = query(listOf(music), exclude = listOf(excluded))
        val excludedRevision = policy.snapshot().configurationRevision
        assertNotEquals(baseline, excludedRevision)

        settings.safQuery = query(listOf(music), exclude = listOf(excluded), withHidden = true)
        assertNotEquals(excludedRevision, policy.snapshot().configurationRevision)
    }

    @Test
    fun `the picker refuses to add a duplicate of a configured source`() {
        val adapter = LocationAdapter<Location.Opened>(NoopListener)
        val music = location("/storage/emulated/0/Music")

        assertTrue(adapter.add(music))
        assertFalse(adapter.add(music))
        assertFalse(adapter.add(location("/sdcard/Music")))
        assertFalse(adapter.add(location("/storage/emulated/0/Music/")))

        assertEquals(1, adapter.locations.size)
    }

    @Test
    fun `restoring a saved list into the picker collapses duplicates`() {
        val adapter = LocationAdapter<Location.Opened>(NoopListener)

        adapter.addAll(
            listOf(
                location("/storage/emulated/0/Music"),
                location("/sdcard/Music"),
                location("/storage/usbdisk0/Audio"),
            )
        )

        assertEquals(
            listOf("/storage/emulated/0/Music", "/storage/usbdisk0/Audio"),
            adapter.locations.map { it.uri.path },
        )
    }

    @Test
    fun `overlapping roots are recognised without being silently dropped`() {
        val volume = location("/storage/emulated/0")
        val music = location("/storage/emulated/0/Music")

        assertEquals(volume.uri, MusicSourceCanonicalizer.ancestorOf(listOf(volume), music)?.uri)
        assertTrue(MusicSourceCanonicalizer.descendantsOf(listOf(music), volume).isNotEmpty())
        assertTrue(MusicSourceCanonicalizer.isWholeVolume(volume))
        assertFalse(MusicSourceCanonicalizer.isWholeVolume(music))
        assertTrue(
            MusicSourceCanonicalizer.hasNarrowerSourceOn(
                listOf("/storage/emulated/0/Music"),
                "/storage/emulated/0",
            )
        )

        assertTrue(apply(listOf(volume, music)))
        assertEquals(2, settings.configuredSourceCount)
    }

    private fun apply(sources: List<Location.Opened>) =
        settings.applySourceConfiguration(
            LocationMode.DIRECT_FS,
            SAF.Query(
                source = sources,
                exclude = emptyList(),
                withHidden = false,
                multithread = true,
            ),
            MediaStore.Query(
                mode = MediaStore.FilterMode.EXCLUDE,
                filtered = emptyList(),
                excludeNonMusic = true,
            ),
        )

    private fun query(
        sources: List<Location.Opened>,
        exclude: List<Location.Unopened> = emptyList(),
        withHidden: Boolean = false,
    ) = SAF.Query(source = sources, exclude = exclude, withHidden = withHidden, multithread = false)

    private fun location(path: String): Location.Opened =
        requireNotNull(
            Location.Unopened.from(context, Uri.fromFile(File(path))).open(context),
            { "could not open $path" },
        )

    private fun unopened(path: String): Location.Unopened =
        Location.Unopened.from(context, Uri.fromFile(File(path)))

    private fun seedRawSources(vararg paths: String) {
        PreferenceManager.getDefaultSharedPreferences(context).edit(commit = true) {
            putString(
                context.getString(R.string.set_key_music_locations),
                paths.joinToString(";") { Uri.fromFile(File(it)).toString() },
            )
        }
    }

    private fun rawSources(): String =
        requireNotNull(
            PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.set_key_music_locations), "")
        )

    private fun rawExclusions(): String =
        requireNotNull(
            PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.set_key_excluded_locations), "")
        )

    private object NoopListener : LocationAdapter.Listener {
        override fun onRemoveLocation(location: Location) = Unit
    }
}
