/*
 * Copyright (c) 2026 Auxio Project
 * LibraryGridSettingsTest.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.oxycblt.auxio.home.list

import android.content.Context
import androidx.preference.PreferenceManager
import androidx.test.core.app.ApplicationProvider
import kotlin.test.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.oxycblt.auxio.R
import org.oxycblt.auxio.home.HomeSettings
import org.oxycblt.auxio.home.HomeSettingsImpl
import org.oxycblt.auxio.list.ListSettings
import org.oxycblt.auxio.list.ListSettingsImpl
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
class LibraryGridSettingsTest {
    private lateinit var context: Context
    private lateinit var homeSettings: HomeSettingsImpl
    private lateinit var listSettings: ListSettingsImpl

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        PreferenceManager.getDefaultSharedPreferences(context).edit().clear().commit()
        homeSettings = HomeSettingsImpl(context)
        listSettings = ListSettingsImpl(context)
    }

    @Test
    fun defaultsAndWritesAreNormalised() {
        assertEquals(LibraryGridPolicy.TWO_COLUMNS, homeSettings.defaultSpanCount)
        assertEquals(LibraryGridPolicy.INHERIT, listSettings.songSpanCount)

        homeSettings.defaultSpanCount = LibraryGridPolicy.THREE_COLUMNS
        listSettings.songSpanCount = LibraryGridPolicy.THREE_COLUMNS
        assertEquals(LibraryGridPolicy.THREE_COLUMNS, homeSettings.defaultSpanCount)
        assertEquals(LibraryGridPolicy.THREE_COLUMNS, listSettings.songSpanCount)

        homeSettings.defaultSpanCount = 99
        listSettings.songSpanCount = 1
        assertEquals(LibraryGridPolicy.TWO_COLUMNS, homeSettings.defaultSpanCount)
        assertEquals(LibraryGridPolicy.INHERIT, listSettings.songSpanCount)
    }

    @Test
    fun resourceBackedKeysDispatchOnlyTheExpectedListener() {
        val homeListener = RecordingHomeListener()
        val listListener = RecordingListListener()
        homeSettings.registerListener(homeListener)
        listSettings.registerListener(listListener)

        homeSettings.onSharedPreferenceChanged(
            PreferenceManager.getDefaultSharedPreferences(context),
            context.getString(R.string.set_key_default_span_count),
        )
        listSettings.onSharedPreferenceChanged(
            PreferenceManager.getDefaultSharedPreferences(context),
            context.getString(R.string.set_key_album_span_count),
        )

        assertEquals(1, homeListener.defaultChanges)
        assertEquals(1, listListener.spanChanges)
    }

    private class RecordingHomeListener : HomeSettings.Listener {
        var defaultChanges = 0

        override fun onDefaultSpanCountChanged() {
            defaultChanges++
        }
    }

    private class RecordingListListener : ListSettings.Listener {
        var spanChanges = 0

        override fun onSpanCountChanged() {
            spanChanges++
        }
    }
}
