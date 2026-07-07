/*
 * Copyright (c) 2024 Auxio Project
 * PlaybackServiceFragment.kt is part of Auxio.
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

package org.oxycblt.auxio.playback.service

import android.content.Context
import android.content.Intent
import android.support.v4.media.session.MediaSessionCompat
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.oxycblt.auxio.AuxioService.Companion.INTENT_KEY_START_ID
import org.oxycblt.auxio.ForegroundListener
import org.oxycblt.auxio.ForegroundServiceNotification
import org.oxycblt.auxio.IntegerTable
import org.oxycblt.auxio.headunit.compat.HeadUnitMetadataPolicy
import org.oxycblt.auxio.headunit.topway.TopwayLauncherIntegrationCoordinator
import org.oxycblt.auxio.headunit.topway.TopwayStartCallbacks
import org.oxycblt.auxio.headunit.ts18.Ts18FirstAudioLatency
import org.oxycblt.auxio.music.resolve
import org.oxycblt.auxio.music.resolveNames
import org.oxycblt.auxio.playback.PlaybackSettings
import org.oxycblt.auxio.playback.StartupPlaybackPolicy
import org.oxycblt.auxio.playback.state.DeferredPlayback
import org.oxycblt.auxio.playback.state.PlaybackStateManager
import org.oxycblt.auxio.playback.state.Progression
import org.oxycblt.auxio.playback.state.QueueChange
import org.oxycblt.auxio.widgets.WidgetComponent
import org.oxycblt.musikr.MusicParent
import org.oxycblt.musikr.Song
import timber.log.Timber as L

class PlaybackServiceFragment
private constructor(
    private val context: Context,
    private val foregroundListener: ForegroundListener,
    private val playbackManager: PlaybackStateManager,
    private val playbackSettings: PlaybackSettings,
    exoHolderFactory: ExoPlaybackStateHolder.Factory,
    sessionHolderFactory: MediaSessionHolder.Factory,
    widgetComponentFactory: WidgetComponent.Factory,
    systemReceiverFactory: SystemPlaybackReceiver.Factory,
    private val topwayCoordinator: TopwayLauncherIntegrationCoordinator,
) : PlaybackStateManager.Listener {
    class Factory
    @Inject
    constructor(
        private val playbackManager: PlaybackStateManager,
        private val playbackSettings: PlaybackSettings,
        private val exoHolderFactory: ExoPlaybackStateHolder.Factory,
        private val sessionHolderFactory: MediaSessionHolder.Factory,
        private val widgetComponentFactory: WidgetComponent.Factory,
        private val systemReceiverFactory: SystemPlaybackReceiver.Factory,
        private val topwayCoordinator: TopwayLauncherIntegrationCoordinator,
    ) {
        fun create(context: Context, foregroundListener: ForegroundListener) =
            PlaybackServiceFragment(
                context,
                foregroundListener,
                playbackManager,
                playbackSettings,
                exoHolderFactory,
                sessionHolderFactory,
                widgetComponentFactory,
                systemReceiverFactory,
                topwayCoordinator,
            )
    }

    private val waitJob = Job()
    private val scope = CoroutineScope(Dispatchers.Main + waitJob)
    private var autoStopJob: Job? = null
    private var lastTopwayIsPlaying: Boolean? = null
    private var topwayProgressTickerJob: Job? = null
    private val exoHolder = exoHolderFactory.create()
    private val sessionHolder = sessionHolderFactory.create(context, foregroundListener)
    private val widgetComponent = widgetComponentFactory.create(context)
    private val systemReceiver =
        systemReceiverFactory.create(
            context,
            widgetComponent,
            onExitRequested = { playbackManager.endSession() },
        )

    private fun restoreCachedPlaybackStateIfIdle() {
        if (playbackManager.currentSong != null) return
        L.i("Requesting cached saved-state restore on playback service attach")
        playbackManager.playDeferred(DeferredPlayback.RestoreState(play = false))
    }

    private fun scheduleAutoStop() {
        autoStopJob?.cancel()
        autoStopJob =
            scope.launch {
                delay(AUTO_STOP_DELAY_MS)
                L.d(
                    "Auto-stop timer expired after ${AUTO_STOP_DELAY_MS / 60000} minutes of inactivity"
                )
                playbackManager.endSession()
            }
    }

    private fun cancelAutoStop() {
        autoStopJob?.cancel()
        autoStopJob = null
    }

    private fun updateAutoStopTimer(isPlaying: Boolean) {
        if (isPlaying) {
            cancelAutoStop()
        } else if (exoHolder.sessionOngoing) {
            scheduleAutoStop()
        }
    }

    // --- MEDIASESSION CALLBACKS ---

    fun attach(): MediaSessionCompat.Token {
        Ts18FirstAudioLatency.mark("playback_fragment_attach")
        exoHolder.attach()
        sessionHolder.attach()
        widgetComponent.attach()
        systemReceiver.attach()
        playbackManager.addListener(this)
        publishTopwayState("service-attach", force = true)
        startTopwayProgressTicker()
        restoreCachedPlaybackStateIfIdle()
        updateAutoStopTimer(playbackManager.progression.isPlaying)
        return sessionHolder.token
    }

    fun handleTaskRemoved() {
        if (!playbackManager.progression.isPlaying || playbackSettings.exitOnTaskRemoval) {
            playbackManager.endSession()
        }
    }

    fun start(intent: Intent?) {
        Ts18FirstAudioLatency.mark("playback_fragment_start")
        // Handle Topway intents regardless of startId for better robustness with external
        // launcher/widget commands. Intent can be null on service restart.
        if (intent != null && handleTopwayStartIntent(intent)) {
            return
        }

        // At minimum we want to ensure an active playback state.
        // TODO: Possibly also force to go foreground?
        val startId = intent?.getIntExtra(INTENT_KEY_START_ID, -1)
        val action =
            when (startId) {
                IntegerTable.START_ID_ACTIVITY -> null
                IntegerTable.START_ID_TASKER ->
                    DeferredPlayback.RestoreState(
                        play = true,
                        fallback = DeferredPlayback.ShuffleAll(),
                    )
                IntegerTable.START_ID_MEDIA_BUTTON -> {
                    if (!sessionHolder.tryMediaButtonIntent(intent)) {
                        // Malformed intent, need to restore state immediately
                        DeferredPlayback.RestoreState(
                            play = true,
                            fallback = DeferredPlayback.ShuffleAll(),
                        )
                    } else {
                        null
                    }
                }
                IntegerTable.START_ID_TOPWAY -> {
                    // Topway intents are handled early above. This branch remains as a
                    // defensive no-op for any future fall-through from non-action intents
                    // that still carry the Topway start ID.
                    null
                }
                IntegerTable.START_ID_BOOT -> {
                    // Boot receiver started the service (Activity launch was blocked).
                    // Respect the autoplay setting for the restore action.
                    StartupPlaybackPolicy.restoreActionForBoot(playbackSettings.autoplayOnLaunch)
                }
                IntegerTable.START_ID_BLUETOOTH -> {
                    DeferredPlayback.RestoreState(
                        play = playbackSettings.headsetAutoplay,
                        fallback = DeferredPlayback.ShuffleAll(),
                    )
                }
                else -> {
                    L.d("Handling non-native start.")
                    if (intent != null && sessionHolder.tryMediaButtonIntent(intent)) {
                        // Just a media button intent, move on.
                        return
                    }
                    // External services using Auxio better know what they are doing.
                    DeferredPlayback.RestoreState(play = false)
                }
            }
        if (action != null) {
            L.d("Initing service fragment using action $action")
            playbackManager.playDeferred(action)
        }
    }

    private fun handleTopwayStartIntent(intent: Intent?): Boolean {
        return topwayCoordinator.handle(
            intent,
            object : TopwayStartCallbacks {
                override val hasCurrentSong: Boolean
                    get() = playbackManager.currentSong != null || exoHolder.hasRawFastResume

                override val currentDurationMs: Long?
                    get() =
                        playbackManager.currentSong?.durationMs ?: exoHolder.rawFastResumeDurationMs

                override fun previous() = playbackManager.prev()

                override fun next() = playbackManager.next()

                override fun playPause() {
                    val currentSong = playbackManager.currentSong
                    if (currentSong != null || exoHolder.hasRawFastResume) {
                        playbackManager.playing(!playbackManager.progression.isPlaying)
                    } else {
                        L.i(
                            "Topway play/pause received with no current song; restoring saved playback"
                        )
                        playbackManager.playDeferred(
                            DeferredPlayback.RestoreState(
                                play = true,
                                fallback = DeferredPlayback.ShuffleAll(),
                            )
                        )
                    }
                }

                override fun widgetUpdate() {
                    if (playbackManager.currentSong == null) {
                        L.i("Topway update received with no current song; requesting state restore")
                        playbackManager.playDeferred(DeferredPlayback.RestoreState(play = false))
                    }
                    publishTopwayState("cmd-update", force = true)
                    widgetComponent.update(force = true)
                }

                override fun seekTo(positionMs: Long) {
                    playbackManager.seekTo(positionMs)
                    publishTopwayProgress("launcher-seek", force = true)
                }

                override fun ignore() = L.d("Ignoring unsupported or unsafe Topway start intent")
            },
        )
    }

    val notification: ForegroundServiceNotification?
        get() = if (exoHolder.sessionOngoing) sessionHolder.notification else null

    fun release() {
        autoStopJob?.cancel()
        topwayProgressTickerJob?.cancel()
        topwayProgressTickerJob = null
        waitJob.cancel()
        playbackManager.removeListener(this)
        systemReceiver.release()
        topwayCoordinator.clear("service-release")
        widgetComponent.release()
        sessionHolder.release()
        exoHolder.release()
    }

    override fun onIndexMoved(index: Int) = publishTopwayState("index-moved", force = true)

    override fun onQueueChanged(queue: List<Song>, index: Int, change: QueueChange) {
        when {
            queue.isEmpty() -> topwayCoordinator.clear("queue-empty")
            change.type == QueueChange.Type.SONG || change.type == QueueChange.Type.INDEX ->
                publishTopwayState("queue-${change.type.name.lowercase()}", force = true)
        }
    }

    override fun onNewPlayback(
        parent: MusicParent?,
        queue: List<Song>,
        index: Int,
        isShuffled: Boolean,
    ) {
        cancelAutoStop()
        publishTopwayState("new-playback", force = true)
    }

    override fun onProgressionChanged(progression: Progression) {
        // Update timer whenever play/pause state changes
        updateAutoStopTimer(progression.isPlaying)
        val playStateChanged = lastTopwayIsPlaying != progression.isPlaying
        lastTopwayIsPlaying = progression.isPlaying
        publishTopwayProgress("progression", force = playStateChanged)
    }

    override fun onRawPlaybackMetadataChanged(
        metadata: org.oxycblt.auxio.playback.state.RawPlaybackMetadata?
    ) = publishTopwayState("raw-metadata", force = true)

    override fun onSessionEnded() {
        topwayCoordinator.clear("session-ended")
        foregroundListener.updateForeground(ForegroundListener.Change.MEDIA_SESSION)
    }

    private fun startTopwayProgressTicker() {
        topwayProgressTickerJob?.cancel()
        topwayProgressTickerJob =
            scope.launch {
                while (true) {
                    if (playbackManager.progression.isPlaying) {
                        publishTopwayProgress("periodic", force = false)
                    }
                    delay(TOPWAY_PROGRESS_TICK_MS)
                }
            }
    }

    private fun publishTopwayState(reason: String, force: Boolean) {
        val song = playbackManager.currentSong
        if (song != null) {
            val snapshot =
                HeadUnitMetadataPolicy.fromRaw(
                    title = song.name.resolve(context),
                    artist = song.artists.resolveNames(context),
                    albumArtist = song.album.artists.resolveNames(context),
                    albumTitle = song.album.name.resolve(context),
                    durationMs = song.durationMs,
                    mediaId = song.uid.toString(),
                    mediaUri = song.uri.toString(),
                    artworkUri = null,
                    hasArtwork = false,
                )
            topwayCoordinator.publishMetadata(snapshot, reason = reason, force = force)
            topwayCoordinator.publishProgress(
                playbackManager.progression.calculateElapsedPositionMs(),
                song.durationMs,
                reason = reason,
                force = force,
            )
            return
        }
        val rawMetadata = playbackManager.rawPlaybackMetadata
        if (rawMetadata != null) {
            val snapshot =
                HeadUnitMetadataPolicy.fromRaw(
                    title = rawMetadata.displayTitle,
                    artist = rawMetadata.displayArtist,
                    albumArtist = rawMetadata.displayArtist,
                    albumTitle = rawMetadata.album,
                    durationMs = rawMetadata.durationMs,
                    mediaId = rawMetadata.uriString,
                    mediaUri = rawMetadata.uriString,
                    artworkUri = null,
                    hasArtwork = false,
                )
            topwayCoordinator.publishMetadata(snapshot, reason = reason, force = force)
            topwayCoordinator.publishProgress(
                playbackManager.progression.calculateElapsedPositionMs(),
                rawMetadata.durationMs,
                reason = reason,
                force = force,
            )
            return
        }
        topwayCoordinator.clear(reason)
    }

    private fun publishTopwayProgress(reason: String, force: Boolean) {
        val duration =
            playbackManager.currentSong?.durationMs
                ?: playbackManager.rawPlaybackMetadata?.durationMs
                ?: 0L
        topwayCoordinator.publishProgress(
            playbackManager.progression.calculateElapsedPositionMs(),
            duration,
            reason = reason,
            force = force,
        )
    }

    private companion object {
        private const val AUTO_STOP_DELAY_MS = 30L * 60L * 1000L
        private const val TOPWAY_PROGRESS_TICK_MS = 1000L
    }
}
