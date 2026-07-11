/*
 * Copyright (c) 2026 Auxio Project
 * StartupPlaybackPolicyTest.kt is part of Auxio.
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

package org.oxycblt.auxio.playback

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.oxycblt.auxio.playback.state.DeferredPlayback

class StartupPlaybackPolicyTest {

    @Test
    fun `restoreActionForLaunch with autoplay disabled returns play false with paused ShuffleAll fallback`() {
        val action = StartupPlaybackPolicy.restoreActionForLaunch(autoplayOnLaunch = false)
        assertFalse(action.play)
        assertEquals(DeferredPlayback.ShuffleAll(play = false), action.fallback)
    }

    @Test
    fun `restoreActionForLaunch with autoplay enabled returns play true with ShuffleAll fallback`() {
        val action = StartupPlaybackPolicy.restoreActionForLaunch(autoplayOnLaunch = true)
        assertTrue(action.play)
        assertEquals(DeferredPlayback.ShuffleAll(play = true), action.fallback)
    }

    @Test
    fun `restoreActionForBoot with autoplay disabled returns play false with paused ShuffleAll fallback`() {
        val action = StartupPlaybackPolicy.restoreActionForBoot(autoplayOnLaunch = false)
        assertFalse(action.play)
        assertEquals(DeferredPlayback.ShuffleAll(play = false), action.fallback)
    }

    @Test
    fun `restoreActionForBoot with autoplay enabled returns play true with ShuffleAll fallback`() {
        val action = StartupPlaybackPolicy.restoreActionForBoot(autoplayOnLaunch = true)
        assertTrue(action.play)
        assertEquals(DeferredPlayback.ShuffleAll(play = true), action.fallback)
    }

    @Test
    fun shouldOpenPanelOnLaunch_falseWhenLibraryNull() {
        val result = StartupPlaybackPolicy.shouldOpenPanelOnLaunch(null)
        assertFalse(result)
    }

    @Test
    fun shouldOpenPanelOnLaunch_falseWhenLibraryEmpty() {
        val emptyLibrary =
            object : org.oxycblt.musikr.Library {
                override val songs = emptyList<org.oxycblt.musikr.Song>()
                override val albums = emptyList<org.oxycblt.musikr.Album>()
                override val artists = emptyList<org.oxycblt.musikr.Artist>()
                override val genres = emptyList<org.oxycblt.musikr.Genre>()
                override val playlists = emptyList<org.oxycblt.musikr.Playlist>()

                override fun empty() = true

                override fun findSong(uid: org.oxycblt.musikr.Music.UID) = null

                override fun findSongByPath(path: org.oxycblt.musikr.fs.Path) = null

                override fun findAlbum(uid: org.oxycblt.musikr.Music.UID) = null

                override fun findArtist(uid: org.oxycblt.musikr.Music.UID) = null

                override fun findGenre(uid: org.oxycblt.musikr.Music.UID) = null

                override fun findPlaylist(uid: org.oxycblt.musikr.Music.UID) = null

                override fun findPlaylistByName(name: String) = null
            }
        val result = StartupPlaybackPolicy.shouldOpenPanelOnLaunch(emptyLibrary)
        assertFalse(result)
    }
    @Test
    fun `startupRoute waits for restored song on Topway cold launch`() {
        val decision =
            StartupPlaybackPolicy.startupRoute(
                StartupPanelInput(
                    coldLaunch = true,
                    restoredTask = false,
                    topwayCompatFlavor = true,
                    headUnitLandscapeMode = true,
                    libraryState = StartupLibraryRouteState.READY_OR_UNKNOWN,
                    hasNormalSong = false,
                    rawFastResumeActive = false,
                )
            )
        val request = decision as StartupPanelDecision.RequestRoute
        assertEquals(OpenPanel.PLAYBACK, request.destination)
        assertTrue(request.waitForSong)
    }

    @Test
    fun `startupRoute opens restored paused or playing song independently of autoplay`() {
        val decision =
            StartupPlaybackPolicy.startupRoute(
                StartupPanelInput(
                    coldLaunch = true,
                    restoredTask = false,
                    topwayCompatFlavor = true,
                    headUnitLandscapeMode = true,
                    libraryState = StartupLibraryRouteState.READY_OR_UNKNOWN,
                    hasNormalSong = true,
                    rawFastResumeActive = false,
                )
            )
        val request = decision as StartupPanelDecision.RequestRoute
        assertEquals(OpenPanel.PLAYBACK, request.destination)
        assertFalse(request.waitForSong)
    }

    @Test
    fun `startupRoute never routes first setup empty or recovery states`() {
        listOf(
                StartupLibraryRouteState.NEEDS_SOURCE,
                StartupLibraryRouteState.EMPTY,
                StartupLibraryRouteState.RECOVERY,
            )
            .forEach { state ->
                val decision =
                    StartupPlaybackPolicy.startupRoute(
                        StartupPanelInput(
                            coldLaunch = true,
                            restoredTask = false,
                            topwayCompatFlavor = true,
                            headUnitLandscapeMode = true,
                            libraryState = state,
                            hasNormalSong = false,
                            rawFastResumeActive = false,
                        )
                    )
                assertTrue(decision is StartupPanelDecision.KeepCurrent)
            }
    }

    @Test
    fun `startupRoute retains raw fast resume until normal song reconciliation`() {
        val decision =
            StartupPlaybackPolicy.startupRoute(
                StartupPanelInput(
                    coldLaunch = true,
                    restoredTask = false,
                    topwayCompatFlavor = true,
                    headUnitLandscapeMode = true,
                    libraryState = StartupLibraryRouteState.CHECKING,
                    hasNormalSong = false,
                    rawFastResumeActive = true,
                )
            )
        val request = decision as StartupPanelDecision.RequestRoute
        assertEquals("raw-fast-resume-awaiting-reconciliation", request.reason)
        assertTrue(request.waitForSong)
    }

    @Test
    fun `startupRoute explicit queue supersedes generic startup playback`() {
        val decision =
            StartupPlaybackPolicy.startupRoute(
                StartupPanelInput(
                    coldLaunch = true,
                    restoredTask = false,
                    topwayCompatFlavor = true,
                    headUnitLandscapeMode = true,
                    libraryState = StartupLibraryRouteState.READY_OR_UNKNOWN,
                    hasNormalSong = true,
                    rawFastResumeActive = false,
                    explicitDestination = OpenPanel.PLAYBACK_QUEUE,
                )
            )
        val request = decision as StartupPanelDecision.RequestRoute
        assertEquals(OpenPanel.PLAYBACK_QUEUE, request.destination)
        assertEquals(PanelRoutePriority.EXPLICIT, request.priority)
    }

    @Test
    fun `startupRoute does not reopen on restored task warm return or user cancellation`() {
        val base =
            StartupPanelInput(
                coldLaunch = true,
                restoredTask = true,
                topwayCompatFlavor = true,
                headUnitLandscapeMode = true,
                libraryState = StartupLibraryRouteState.READY_OR_UNKNOWN,
                hasNormalSong = true,
                rawFastResumeActive = false,
            )
        assertTrue(StartupPlaybackPolicy.startupRoute(base) is StartupPanelDecision.KeepCurrent)
        assertTrue(
            StartupPlaybackPolicy.startupRoute(base.copy(restoredTask = false, userCancelled = true))
                is StartupPanelDecision.KeepCurrent
        )
    }

}
