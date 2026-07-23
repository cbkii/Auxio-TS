/*
 * Copyright (c) 2026 Auxio Project
 * LibraryRecoveryPolicyTest.kt is part of Auxio.
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

package org.oxycblt.auxio.home.list

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.oxycblt.auxio.headunit.root.RootStateHolder
import org.oxycblt.auxio.music.IndexingState
import org.oxycblt.auxio.music.StartupLibraryStatus
import org.oxycblt.auxio.music.StartupReadinessState
import org.oxycblt.auxio.music.locations.LocationMode
import org.oxycblt.musikr.IndexingProgress

class LibraryRecoveryPolicyTest {
    @Test
    fun usableLibraryHidesRecoveryPanel() {
        val state = resolve(empty = false, libraryStatus = StartupLibraryStatus.Usable)

        assertFalse(state.visible)
        assertEquals(LibraryRecoveryPolicy.Kind.HIDDEN, state.kind)
    }

    @Test
    fun missingMediaStorePermissionShowsGrantAndSourceActions() {
        val state =
            resolve(
                locationMode = LocationMode.MEDIA_STORE,
                storagePermissionGranted = false,
                sourceConfigured = true,
            )

        assertEquals(LibraryRecoveryPolicy.Kind.PERMISSION_REQUIRED, state.kind)
        assertFalse(state.showProgress)
        assertEquals(LibraryRecoveryPolicy.Action.GRANT_PERMISSION, state.primary?.action)
        assertEquals(LibraryRecoveryPolicy.Action.CHOOSE_SOURCE, state.secondary?.action)
        assertEquals(null, state.tertiary)
    }

    @Test
    fun missingDirectPermissionAlsoOffersExplicitRootActionWhenSupported() {
        val state =
            resolve(
                locationMode = LocationMode.DIRECT_FS,
                storagePermissionGranted = false,
                sourceConfigured = true,
                rootSupported = true,
                rootEnabled = false,
            )

        assertEquals(LibraryRecoveryPolicy.Kind.PERMISSION_REQUIRED, state.kind)
        assertFalse(state.showProgress)
        assertEquals(LibraryRecoveryPolicy.Action.GRANT_PERMISSION, state.primary?.action)
        assertEquals(LibraryRecoveryPolicy.Action.CHOOSE_SOURCE, state.secondary?.action)
        assertEquals(LibraryRecoveryPolicy.Action.ENABLE_ROOT, state.tertiary?.action)
    }

    @Test
    fun directSourceWithoutRootOffersExplicitRootAction() {
        val state =
            resolve(
                locationMode = LocationMode.DIRECT_FS,
                sourceConfigured = false,
                rootSupported = true,
                rootEnabled = false,
            )

        assertEquals(LibraryRecoveryPolicy.Kind.SOURCE_REQUIRED, state.kind)
        assertFalse(state.showProgress)
        assertEquals(LibraryRecoveryPolicy.Action.CHOOSE_SOURCE, state.primary?.action)
        assertEquals(LibraryRecoveryPolicy.Action.ENABLE_ROOT, state.secondary?.action)
    }

    @Test
    fun ordinaryDirectSourceDoesNotRequireRootWhenAlreadyEnabledOrUnsupported() {
        val enabled =
            resolve(
                locationMode = LocationMode.DIRECT_FS,
                sourceConfigured = false,
                rootSupported = true,
                rootEnabled = true,
            )
        val unsupported =
            resolve(
                locationMode = LocationMode.DIRECT_FS,
                sourceConfigured = false,
                rootSupported = false,
                rootEnabled = false,
            )

        assertEquals(null, enabled.secondary)
        assertEquals(null, unsupported.secondary)
    }

    @Test
    fun sourceUnavailableShowsRetrySourceAndOptionalRootActions() {
        val state =
            resolve(
                libraryStatus = StartupLibraryStatus.SourceUnavailable,
                locationMode = LocationMode.DIRECT_FS,
                rootSupported = true,
                rootEnabled = false,
            )

        assertEquals(LibraryRecoveryPolicy.Kind.SOURCE_UNAVAILABLE, state.kind)
        assertFalse(state.showProgress)
        assertEquals(LibraryRecoveryPolicy.Action.REFRESH, state.primary?.action)
        assertEquals(LibraryRecoveryPolicy.Action.CHOOSE_SOURCE, state.secondary?.action)
        assertEquals(LibraryRecoveryPolicy.Action.ENABLE_ROOT, state.tertiary?.action)
    }

    @Test
    fun cacheUnavailableShowsRefreshRescanAndSourceActions() {
        val state = resolve(libraryStatus = StartupLibraryStatus.CacheUnavailable)

        assertEquals(LibraryRecoveryPolicy.Kind.CACHE_UNAVAILABLE, state.kind)
        assertFalse(state.showProgress)
        assertEquals(LibraryRecoveryPolicy.Action.REFRESH, state.primary?.action)
        assertEquals(LibraryRecoveryPolicy.Action.RESCAN, state.secondary?.action)
        assertEquals(LibraryRecoveryPolicy.Action.CHOOSE_SOURCE, state.tertiary?.action)
    }

    @Test
    fun confirmedEmptyLibraryShowsRefreshRescanAndSourceActions() {
        val state = resolve(libraryStatus = StartupLibraryStatus.Empty)

        assertEquals(LibraryRecoveryPolicy.Kind.EMPTY, state.kind)
        assertFalse(state.showProgress)
        assertEquals(LibraryRecoveryPolicy.Action.REFRESH, state.primary?.action)
        assertEquals(LibraryRecoveryPolicy.Action.RESCAN, state.secondary?.action)
        assertEquals(LibraryRecoveryPolicy.Action.CHOOSE_SOURCE, state.tertiary?.action)
    }

    @Test
    fun usableStatusWithEmptyVisibleLibraryRecoversAsMissingCache() {
        val state = resolve(empty = true, libraryStatus = StartupLibraryStatus.Usable)

        assertEquals(LibraryRecoveryPolicy.Kind.CACHE_UNAVAILABLE, state.kind)
        assertFalse(state.showProgress)
        assertEquals(LibraryRecoveryPolicy.Action.REFRESH, state.primary?.action)
        assertEquals(LibraryRecoveryPolicy.Action.RESCAN, state.secondary?.action)
        assertEquals(LibraryRecoveryPolicy.Action.CHOOSE_SOURCE, state.tertiary?.action)
    }

    @Test
    fun scanFailureDoesNotLeaveSpinnerOnlyState() {
        val state =
            resolve(
                indexingState = IndexingState.Completed(Exception("failed")),
                lastScanFailed = true,
            )

        assertEquals(LibraryRecoveryPolicy.Kind.FAILED, state.kind)
        assertFalse(state.showProgress)
        assertEquals(LibraryRecoveryPolicy.Action.REFRESH, state.primary?.action)
        assertEquals(LibraryRecoveryPolicy.Action.RESCAN, state.secondary?.action)
        assertEquals(LibraryRecoveryPolicy.Action.CHOOSE_SOURCE, state.tertiary?.action)
    }

    @Test
    fun indexingKeepsChooseSourceAvailable() {
        val state =
            resolve(
                indexingState = IndexingState.Indexing(IndexingProgress.Indeterminate),
                libraryStatus = StartupLibraryStatus.CacheUnavailable,
            )

        assertEquals(LibraryRecoveryPolicy.Kind.INDEXING, state.kind)
        assertTrue(state.showProgress)
        assertEquals(LibraryRecoveryPolicy.Action.CHOOSE_SOURCE, state.primary?.action)
    }

    @Test
    fun earlyUnknownStartupStillExposesRecoveryAction() {
        val state =
            resolve(
                startupState = StartupReadinessState.ProcessVisible,
                libraryStatus = StartupLibraryStatus.Unknown,
            )

        assertEquals(LibraryRecoveryPolicy.Kind.WAITING, state.kind)
        assertTrue(state.showProgress)
        assertEquals(LibraryRecoveryPolicy.Action.REFRESH, state.primary?.action)
    }

    private fun resolve(
        empty: Boolean = true,
        indexingState: IndexingState? = null,
        startupState: StartupReadinessState = StartupReadinessState.FastBrowseReady,
        libraryStatus: StartupLibraryStatus = StartupLibraryStatus.NeedsMusicSource,
        locationMode: LocationMode = LocationMode.SAF,
        sourceConfigured: Boolean = true,
        storagePermissionGranted: Boolean = true,
        rootSupported: Boolean = false,
        rootEnabled: Boolean = false,
        lastScanFailed: Boolean = false,
    ): LibraryRecoveryPolicy.State =
        LibraryRecoveryPolicy.resolve(
            LibraryRecoveryPolicy.Input(
                empty = empty,
                indexingState = indexingState,
                startupState = startupState,
                libraryStatus = libraryStatus,
                sourceConfigured = sourceConfigured,
                storagePermissionRequired = locationMode == LocationMode.MEDIA_STORE,
                storagePermissionGranted = storagePermissionGranted,
                rootSupported = rootSupported,
                rootRequired = locationMode == LocationMode.DIRECT_FS && !rootEnabled,
                rootState =
                    if (rootEnabled) RootStateHolder.State.Available
                    else RootStateHolder.State.DisabledByUser,
                lastScanFailed = lastScanFailed,
            )
        )
}