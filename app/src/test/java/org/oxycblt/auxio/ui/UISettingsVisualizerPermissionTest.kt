/*
 * Copyright (c) 2026 Auxio Project
 * UISettingsVisualizerPermissionTest.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.oxycblt.auxio.ui

import android.content.Context
import androidx.preference.PreferenceManager
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class UISettingsVisualizerPermissionTest {
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
    fun explicitDenialPersistsAcrossSettingsInstancesAndCanBeCleared() {
        val first = UISettingsImpl(context)
        assertFalse(first.visualizerPermissionDenied)

        first.visualizerPermissionDenied = true
        assertTrue(UISettingsImpl(context).visualizerPermissionDenied)

        UISettingsImpl(context).visualizerPermissionDenied = false
        assertFalse(UISettingsImpl(context).visualizerPermissionDenied)
    }
}
