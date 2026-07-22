/*
 * Copyright (c) 2026 Auxio Project
 * TopwayMusicBroadcastBridgeTest.kt is part of Auxio.
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

package org.oxycblt.auxio.headunit.topway

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.runner.RunWith
import org.oxycblt.auxio.playback.state.PlaybackStateManager
import org.oxycblt.auxio.playback.state.Progression
import org.oxycblt.auxio.playback.state.RepeatMode
import org.oxycblt.auxio.playback.state.ShuffleMode
import org.oxycblt.auxio.playback.state.ShuffleScope
import org.oxycblt.auxio.ui.UISettings
import org.oxycblt.auxio.ui.accent.Accent
import org.oxycblt.musikr.Music
import org.oxycblt.musikr.Song
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TopwayMusicBroadcastBridgeTest {
    private lateinit var context: RecordingContext

    @Before
    fun setUp() {
        context = RecordingContext(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun progressRecoversAfterStateClearAndNewPlaybackStarts() = runTest {
        val stateManager = FakePlaybackStateManager()
        val bridge =
            TopwayMusicBroadcastBridge(
                context,
                FakeUiSettings(headUnitLandscapeMode = true),
                stateManager,
            )
        bridge.start()

        stateManager.emitProgression(positionMs = 1_000, durationMs = 5_000)
        stateManager.emitClear()
        stateManager.emitProgression(positionMs = 2_500, durationMs = 5_000)

        val progressBroadcasts =
            context.broadcasts.filter { it.action == TopwayMusicContract.ACTION_PROGRESS_DURATION }

        // Should have 3 broadcasts: Active(1000/5000), Clear(0/0), Active(2500/5000)
        assertEquals(3, progressBroadcasts.size, "Should have 3 broadcasts including the recovery")

        assertEquals(
            1_000,
            progressBroadcasts[0].getIntExtra(TopwayMusicContract.EXTRA_PROGRESS, -1),
        )
        assertEquals(0, progressBroadcasts[1].getIntExtra(TopwayMusicContract.EXTRA_PROGRESS, -1))
        assertEquals(
            2_500,
            progressBroadcasts[2].getIntExtra(TopwayMusicContract.EXTRA_PROGRESS, -1),
        )
    }

    private class RecordingContext(base: Context) : ContextWrapper(base) {
        val broadcasts = mutableListOf<Intent>()

        override fun sendBroadcast(intent: Intent) {
            broadcasts += Intent(intent)
        }
    }

    private class FakeUiSettings(override val headUnitLandscapeMode: Boolean) : UISettings {
        override val theme: Int = 0
        override val useBlackTheme: Boolean = false
        override var accent: Accent = Accent.from(Accent.DEFAULT)
        override val roundMode: Boolean = true
        override val driverSide: UISettings.DriverSide = UISettings.DriverSide.RIGHT
        override val largeHeadUnitControls: Boolean = true
        override val showHeadUnitAlbumArt: Boolean = true
        override val showHeadUnitDashboardQuickAccess: Boolean = true
        override val headUnitCompatStatusSummary: String = "test"
        override val visualizerMode: UISettings.VisualizerMode = UISettings.VisualizerMode.OFF
        override var visualizerPermissionDenied: Boolean = false

        override fun registerListener(listener: UISettings.Listener) = Unit

        override fun unregisterListener(listener: UISettings.Listener) = Unit
    }

    private class FakePlaybackStateManager : PlaybackStateManager {
        override val currentSong: Song? = null
        override val rawPlaybackMetadata = null
        override val restoreOutcome = org.oxycblt.auxio.playback.state.RestoreOutcome.NONE
        override val isPlaying = false
        override val currentAudioSessionId = null
        override val queueWindow = null
        override val parent = null
        override val progression: Progression? = null
        override val repeatMode = RepeatMode.NONE
        override val shuffleMode = ShuffleMode.OFF
        override val shuffleScope = ShuffleScope.OFF
        override val currentIndex = -1
        override val queue: List<Song> = emptyList()
        override val listeners = CopyOnWriteArrayList<PlaybackStateManager.Listener>()

        override fun addListener(listener: PlaybackStateManager.Listener) {
            listeners.add(listener)
        }

        override fun removeListener(listener: PlaybackStateManager.Listener) {
            listeners.remove(listener)
        }

        override fun emitCurrentState(listener: PlaybackStateManager.Listener) = Unit

        override fun emitCurrentProgression(listener: PlaybackStateManager.Listener) = Unit

        override fun emitCurrentAudioSession(listener: PlaybackStateManager.Listener) = Unit

        override fun registerStoredPlaylist(handle: org.oxycblt.musikr.storage.StoredPlaylistHandle) = Unit

        override fun start() = Unit

        override fun end() = Unit

        override fun endSession() = Unit

        override fun play(song: Song) = Unit

        override fun play(parent: org.oxycblt.musikr.MusicParent) = Unit

        override fun play(parent: org.oxycblt.musikr.MusicParent, song: Song) = Unit

        override fun play(songs: List<Song>, song: Song?) = Unit

        override fun playOrPause() = Unit

        override fun pause() = Unit

        override fun seekTo(positionMs: Long) = Unit

        override fun next() = Unit

        override fun prev() = Unit

        override fun goTo(index: Int) = Unit

        override fun setRepeatMode(mode: RepeatMode) = Unit

        override fun setShuffleMode(mode: ShuffleMode) = Unit

        override fun setShuffleScope(scope: ShuffleScope) = Unit

        override fun move(from: Int, to: Int) = Unit

        override fun remove(index: Int) = Unit

        override fun resolve(raw: Music.Raw, music: Music) = Unit

        override fun requestRestore() = Unit

        override fun requestRawFastResume() = Unit

        fun emitProgression(positionMs: Long, durationMs: Long) {
            val progression =
                Progression(
                    positionMs = positionMs,
                    durationMs = durationMs,
                    isPlaying = true,
                )
            listeners.forEach { it.onProgressionChanged(progression) }
        }

        fun emitClear() {
            listeners.forEach { it.onStateChanged(null, null) }
        }
    }
}
