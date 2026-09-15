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
import android.content.SharedPreferences
import android.support.v4.media.session.MediaSessionCompat
import androidx.preference.PreferenceManager
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import org.oxycblt.auxio.AuxioService.Companion.INTENT_KEY_START_ID
import org.oxycblt.auxio.ForegroundListener
import org.oxycblt.auxio.ForegroundServiceNotification
import org.oxycblt.auxio.IntegerTable
import org.oxycblt.auxio.headunit.compat.HeadUnitMetadataPolicy
import org.oxycblt.auxio.headunit.topway.TopwayLauncherIntegrationCoordinator
import org.oxycblt.auxio.headunit.topway.TopwayStartCallbacks
import org.oxycblt.auxio.headunit.topway.Ts18LauncherIntegrationMode
import org.oxycblt.auxio.headunit.ts18.Ts18FirstAudioLatency
import org.oxycblt.auxio.music.PlaybackReadinessState
import org.oxycblt.auxio.music.StartupReadinessController
import org.oxycblt.auxio.music.StartupReadinessState
import org.oxycblt.auxio.music.resolve
import org.oxycblt.auxio.music.resolveNames
import org.oxycblt.auxio.playback.PlaybackSettings
import org.oxycblt.auxio.playback.StartupPlaybackPolicy
import org.oxycblt.auxio.playback.state.DeferredPlayback
import org.oxycblt.auxio.playback.state.PlaybackStateManager
import org.oxycblt.auxio.playback.state.Progression
import org.oxycblt.auxio.playback.state.QueueChange
import org.oxycblt.auxio.playback.state.RestoreOutcome
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
    private val startupReadinessController: StartupReadinessController,
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
        private val startupReadinessController: StartupReadinessController,
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
                startupReadinessController,
            )
    }

    private val waitJob = Job()
    private val scope = CoroutineScope(Dispatchers.Main + waitJob)
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    private var autoStopJob: Job? = null
    private var restoreWatchdogJob: Job? = null
    private val restoreWatchdogGeneration = RestoreWatchdogGeneration()
    private var lastTopwayIsPlaying: Boolean? = null
    private var topwayProgressTickerJob: Job? = null
    private val launcherModePreferenceListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key != Ts18LauncherIntegrationMode.PREF_KEY) return@OnSharedPreferenceChangeListener
            scope.launch { reconcileTopwayProgressTicker() }
        }
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
        if (playbackManager.currentSong != null || playbackManager.rawPlaybackMetadata != null)
            return
        L.i("Requesting cached saved-state restore on playback service attach")
        playbackManager.playDeferred(DeferredPlayback.RestoreState(play = false))
        scheduleRestoreWatchdog()
    }

    private fun scheduleRestoreWatchdog() {
        restoreWatchdogJob?.cancel()
        val generation = restoreWatchdogGeneration.next()
        restoreWatchdogJob =
            scope.launch {
                try {
                    delay(RESTORE_STARTUP_TIMEOUT_MS)
                    coroutineContext.ensureActive()
                    if (!restoreWatchdogGeneration.isCurrent(generation)) return@launch
                    val outcome = playbackManager.restoreOutcome
                    if (
                        outcome == RestoreOutcome.WAITING_FOR_PLAYER ||
                            outcome == RestoreOutcome.WAITING_FOR_LIBRARY
                    ) {
                        L.w(
                            "Playback restore remained transient for ${RESTORE_STARTUP_TIMEOUT_MS}ms; " +
                                "releasing startup readiness without blocking the library"
                        )
                        playbackManager.cancelDeferredRestore()
                        startupReadinessController.publishCapability(
                            StartupReadinessState.QueueReady
                        )
                    }
                } finally {
                    if (restoreWatchdogGeneration.isCurrent(generation)) {
                        restoreWatchdogJob = null
                    }
                }
            }
    }

    private fun cancelRestoreWatchdog() {
        restoreWatchdogGeneration.invalidate()
        restoreWatchdogJob?.cancel()
        restoreWatchdogJob = null
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
        if (
            PlaybackResidencyPolicy.shouldScheduleIdleStop(
                keepPlaybackReady = playbackSettings.keepPlaybackReady,
                isPlaying = isPlaying,
                sessionOngoing = exoHolder.sessionOngoing,
            )
        ) {
            scheduleAutoStop()
        } else {
            cancelAutoStop()
        }
    }

    private fun restoreWithSkip(delta: Int, origin: String) {
        L.i("$origin received with no ready queue; restoring persisted playback with skip=$delta")
        playbackManager.playDeferred(
            DeferredPlayback.RestoreState(play = true, fallback = DeferredPlayback.ShuffleAll())
        )
        if (delta > 0) {
            repeat(delta) { playbackManager.next() }
        } else {
            repeat(-delta) { playbackManager.prev() }
        }
        scheduleRestoreWatchdog()
    }

    // --- MEDIASESSION CALLBACKS ---

    fun attach(): MediaSessionCompat.Token {
        Ts18FirstAudioLatency.mark("playback_fragment_attach")
        exoHolder.attach()
        sessionHolder.attach()
        widgetComponent.attach()
        systemReceiver.attach()
        playbackManager.addListener(this)
        prefs.registerOnSharedPreferenceChangeListener(launcherModePreferenceListener)
        publishTopwayState("service-attach", force = true)
        reconcileTopwayProgressTicker()
        startupReadinessController.publishCapability(StartupReadinessState.PlaybackServiceReady)
        startupReadinessController.publishPlaybackReadiness(PlaybackReadinessState.SERVICE_READY)
        restoreCachedPlaybackStateIfIdle()
        updateAutoStopTimer(playbackManager.progression.isPlaying)
        return sessionHolder.token
    }

    fun handleTaskRemoved() {
        if (
            PlaybackResidencyPolicy.shouldEndSessionOnTaskRemoved(
                keepPlaybackReady = playbackSettings.keepPlaybackReady,
                isPlaying = playbackManager.progression.isPlaying,
                exitOnTaskRemoval = playbackSettings.exitOnTaskRemoval,
            )
        ) {
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
                        DeferredPlayback.RestoreState(
                            play = true,
                            fallback = DeferredPlayback.ShuffleAll(),
                        )
                    } else {
                        null
                    }
                }
                IntegerTable.START_ID_TOPWAY -> null
                IntegerTable.START_ID_BOOT ->
                    StartupPlaybackPolicy.restoreActionForBoot(playbackSettings.autoplayOnLaunch)
                IntegerTable.START_ID_BLUETOOTH ->
                    DeferredPlayback.RestoreState(
                        play = playbackSettings.headsetAutoplay,
                        fallback = DeferredPlayback.ShuffleAll(),
                    )
                else -> {
                    L.d("Handling non-native start.")
                    if (intent != null && sessionHolder.tryMediaButtonIntent(intent)) return
                    DeferredPlayback.RestoreState(play = false)
                }
            }
        if (action != null) {
            L.d("Initing service fragment using action $action")
            playbackManager.playDeferred(action)
            if (action is DeferredPlayback.RestoreState) scheduleRestoreWatchdog()
        }
    }

    private fun handleTopwayStartIntent(intent: Intent?): Boolean {
        return topwayCoordinator.handle(
            intent,
            object : TopwayStartCallbacks {
                override val hasCurrentSong: Boolean
                    get() =
                        playbackManager.currentSong != null ||
                            playbackManager.rawPlaybackMetadata != null

                override val currentDurationMs: Long?
                    get() =
                        playbackManager.currentSong?.durationMs
                            ?: playbackManager.rawPlaybackMetadata?.durationMs

                override fun previous() {
                    if (
                        playbackManager.currentSong != null ||
                            playbackManager.rawPlaybackMetadata != null
                    ) {
                        playbackManager.prev()
                    } else {
                        restoreWithSkip(-1, "Topway previous")
                    }
                }

                override fun next() {
                    if (
                        playbackManager.currentSong != null ||
                            playbackManager.rawPlaybackMetadata != null
                    ) {
                        playbackManager.next()
                    } else {
                        restoreWithSkip(1, "Topway next")
                    }
                }

                override fun playPause() {
                    if (
                        playbackManager.currentSong != null ||
                            playbackManager.rawPlaybackMetadata != null
                    ) {
                        playbackManager.playing(!playbackManager.progression.isPlaying)
                    } else {
                        L.i(
                            "Topway play/pause received with no current media; restoring saved playback"
                        )
                        playbackManager.playDeferred(
                            DeferredPlayback.RestoreState(
                                play = true,
                                fallback = DeferredPlayback.ShuffleAll(),
                            )
                        )
                        scheduleRestoreWatchdog()
                    }
                }

                override fun widgetUpdate() {
                    if (
                        playbackManager.currentSong == null &&
                            playbackManager.rawPlaybackMetadata == null
                    ) {
                        L.i(
                            "Topway update received with no current media; requesting state restore"
                        )
                        playbackManager.playDeferred(DeferredPlayback.RestoreState(play = false))
                        scheduleRestoreWatchdog()
                    }
                    publishTopwayState("cmd-update", force = true)
                    widgetComponent.update(force = true)
                }

                override fun seekTo(positionMs: Long) {
                    if (
                        playbackManager.currentSong == null &&
                            playbackManager.rawPlaybackMetadata == null
                    ) {
                        playbackManager.playDeferred(DeferredPlayback.RestoreState(play = false))
                    }
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
        cancelRestoreWatchdog()
        prefs.unregisterOnSharedPreferenceChangeListener(launcherModePreferenceListener)
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
        updateAutoStopTimer(progression.isPlaying)
        val playStateChanged = lastTopwayIsPlaying != progression.isPlaying
        lastTopwayIsPlaying = progression.isPlaying
        publishTopwayProgress("progression", force = playStateChanged)
    }

    override fun onRestoreOutcomeChanged(outcome: RestoreOutcome) {
        if (
            outcome != RestoreOutcome.WAITING_FOR_PLAYER &&
                outcome != RestoreOutcome.WAITING_FOR_LIBRARY
        ) {
            cancelRestoreWatchdog()
            startupReadinessController.publishCapability(StartupReadinessState.QueueReady)
            foregroundListener.updateForeground(ForegroundListener.Change.MEDIA_SESSION)
        }
    }

    override fun onRawPlaybackMetadataChanged(
        metadata: org.oxycblt.auxio.playback.state.RawPlaybackMetadata?
    ) = publishTopwayState("raw-metadata", force = true)

    override fun onSessionEnded() {
        topwayCoordinator.clear("session-ended")
        foregroundListener.updateForeground(ForegroundListener.Change.MEDIA_SESSION)
    }

    private fun reconcileTopwayProgressTicker() {
        val running = topwayProgressTickerJob?.isActive == true
        when (TopwayProgressTickerPolicy.directive(topwayCoordinator.mode, running)) {
            TopwayProgressTickerDirective.START -> {
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
            TopwayProgressTickerDirective.STOP -> {
                topwayProgressTickerJob?.cancel()
                topwayProgressTickerJob = null
            }
            TopwayProgressTickerDirective.KEEP -> Unit
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
        private const val RESTORE_STARTUP_TIMEOUT_MS = 8_000L
        private const val TOPWAY_PROGRESS_TICK_MS = 1000L
    }
}

internal enum class TopwayProgressTickerDirective {
    START,
    STOP,
    KEEP,
}

/** Pure lifecycle policy for the optional periodic Topway progress publisher. */
internal object TopwayProgressTickerPolicy {
    fun directive(
        mode: Ts18LauncherIntegrationMode,
        running: Boolean,
    ): TopwayProgressTickerDirective =
        when {
            mode.sendsTopwayBroadcasts && !running -> TopwayProgressTickerDirective.START
            !mode.sendsTopwayBroadcasts && running -> TopwayProgressTickerDirective.STOP
            else -> TopwayProgressTickerDirective.KEEP
        }
}

internal class RestoreWatchdogGeneration {
    private val current = AtomicLong()

    fun next(): Long = current.incrementAndGet()

    fun invalidate() {
        current.incrementAndGet()
    }

    fun isCurrent(generation: Long): Boolean = current.get() == generation
}
