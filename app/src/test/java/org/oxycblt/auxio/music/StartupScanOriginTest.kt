/*
 * Copyright (c) 2026 Auxio Project
 * StartupScanOriginTest.kt is part of Auxio.
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

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class StartupScanOriginTest {
    @Test
    fun userVisibleOriginOverridesBackgroundOrigin() {
        assertEquals(
            StartupScanOrigin.USER_VISIBLE,
            StartupScanOrigin.merge(
                StartupScanOrigin.BACKGROUND,
                StartupScanOrigin.USER_VISIBLE,
            ),
        )
        assertEquals(
            StartupScanOrigin.USER_VISIBLE,
            StartupScanOrigin.merge(
                StartupScanOrigin.USER_VISIBLE,
                StartupScanOrigin.EARLY_PRESTART,
            ),
        )
    }

    @Test
    fun topwayUserVisibleFirstStartMayRequestOneScan() = runBlocking {
        var requests = 0
        val decision =
            StartupLibraryStartup.run(
                hasInMemoryLibrary = false,
                revisionKnown = false,
                priorState = LibraryState.NEVER,
                deferCachedLoad = true,
                lastScanFailed = { false },
                isTopwayCompat = true,
                loadCachedLibrary = { Unit },
                cachedSongCount = { 0 },
                emitCachedLibrary = {},
                emitCachedLoadFailure = {},
                setLibraryState = {},
                requestIndex = { requests++ },
                sourceConfigured = true,
                allowAutomaticScan = StartupScanOrigin.USER_VISIBLE.allowAutomaticScan,
            )

        assertTrue(decision.requestScan)
        assertEquals(1, requests)
    }

    @Test
    fun topwayBackgroundAndEarlyPrestartDoNotRequestScans() = runBlocking {
        for (origin in listOf(StartupScanOrigin.BACKGROUND, StartupScanOrigin.EARLY_PRESTART)) {
            var requests = 0
            val decision =
                StartupLibraryStartup.run(
                    hasInMemoryLibrary = false,
                    revisionKnown = false,
                    priorState = LibraryState.NEVER,
                    deferCachedLoad = true,
                    lastScanFailed = { false },
                    isTopwayCompat = true,
                    loadCachedLibrary = { Unit },
                    cachedSongCount = { 0 },
                    emitCachedLibrary = {},
                    emitCachedLoadFailure = {},
                    setLibraryState = {},
                    requestIndex = { requests++ },
                    sourceConfigured = true,
                    allowAutomaticScan = origin.allowAutomaticScan,
                )

            assertFalse(decision.requestScan)
            assertEquals(0, requests)
        }
    }

    @Test
    fun noConfiguredSourceNeverRequestsScan() = runBlocking {
        var requests = 0
        val decision =
            StartupLibraryStartup.run(
                hasInMemoryLibrary = false,
                revisionKnown = false,
                priorState = LibraryState.NEVER,
                deferCachedLoad = true,
                lastScanFailed = { false },
                isTopwayCompat = true,
                loadCachedLibrary = { Unit },
                cachedSongCount = { 0 },
                emitCachedLibrary = {},
                emitCachedLoadFailure = {},
                setLibraryState = {},
                requestIndex = { requests++ },
                sourceConfigured = false,
                allowAutomaticScan = true,
            )

        assertFalse(decision.requestScan)
        assertEquals(0, requests)
    }
}
