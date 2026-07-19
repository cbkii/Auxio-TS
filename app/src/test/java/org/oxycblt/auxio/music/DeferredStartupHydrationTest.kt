/*
 * Copyright (c) 2026 Auxio Project
 * DeferredStartupHydrationTest.kt is part of Auxio.
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
import org.junit.Test

class DeferredStartupHydrationTest {
    @Test
    fun `prior usable cache is preserved without synchronous graph hydration`() = runBlocking {
        var loadAttempts = 0
        val scanRequests = mutableListOf<Boolean>()
        var libraryState: LibraryState? = null
        var startupStatus: StartupLibraryStatus? = null

        val decision =
            StartupLibraryStartup.run(
                hasInMemoryLibrary = false,
                revisionKnown = true,
                priorState = LibraryState.USABLE,
                deferCachedLoad = true,
                lastScanFailed = { false },
                isTopwayCompat = false,
                loadCachedLibrary = {
                    loadAttempts += 1
                    42
                },
                cachedSongCount = { it },
                emitCachedLibrary = {},
                emitCachedLoadFailure = {},
                setLibraryState = { libraryState = it },
                requestIndex = { scanRequests += it },
                setStartupLibraryStatus = { startupStatus = it },
            )

        assertEquals("cached-library-hydration-deferred", decision.reason)
        assertEquals(LibraryState.USABLE, libraryState)
        assertEquals(StartupLibraryStatus.Usable, startupStatus)
        assertEquals(0, loadAttempts)
        assertEquals(emptyList<Boolean>(), scanRequests)
    }

    @Test
    fun `first launch still requests the standard initial scan`() = runBlocking {
        var loadAttempts = 0
        val scanRequests = mutableListOf<Boolean>()

        val decision =
            StartupLibraryStartup.run(
                hasInMemoryLibrary = false,
                revisionKnown = false,
                priorState = LibraryState.NEVER,
                deferCachedLoad = true,
                lastScanFailed = { false },
                isTopwayCompat = false,
                loadCachedLibrary = {
                    loadAttempts += 1
                    0
                },
                cachedSongCount = { it },
                emitCachedLibrary = {},
                emitCachedLoadFailure = {},
                setLibraryState = {},
                requestIndex = { scanRequests += it },
            )

        assertEquals(LibraryState.NEVER, decision.libraryState)
        assertEquals(0, loadAttempts)
        assertEquals(listOf(MusicScanRequestMode.REFRESH_WITH_CACHE), scanRequests)
    }
}
