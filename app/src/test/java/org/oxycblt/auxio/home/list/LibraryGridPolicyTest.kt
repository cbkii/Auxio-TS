/*
 * Copyright (c) 2026 Auxio Project
 * LibraryGridPolicyTest.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.oxycblt.auxio.home.list

import kotlin.test.Test
import kotlin.test.assertEquals

class LibraryGridPolicyTest {
    @Test
    fun twoColumnsIsTheDefault() {
        assertEquals(2, LibraryGridPolicy.effective(defaultValue = 0, overrideValue = 0))
        assertEquals(2, LibraryGridPolicy.effective(defaultValue = 2, overrideValue = 0))
    }

    @Test
    fun threeColumnGlobalDefaultIsInherited() {
        assertEquals(3, LibraryGridPolicy.effective(defaultValue = 3, overrideValue = 0))
    }

    @Test
    fun perTabOverrideWins() {
        assertEquals(3, LibraryGridPolicy.effective(defaultValue = 2, overrideValue = 3))
        assertEquals(2, LibraryGridPolicy.effective(defaultValue = 3, overrideValue = 2))
    }

    @Test
    fun unsupportedValuesCannotCreateUnsafeLayouts() {
        assertEquals(2, LibraryGridPolicy.effective(defaultValue = 99, overrideValue = -4))
        assertEquals(2, LibraryGridPolicy.normalizeDefault(4))
        assertEquals(0, LibraryGridPolicy.normalizeOverride(1))
    }
}
