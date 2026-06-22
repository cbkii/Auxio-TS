/*
 * Copyright (c) 2024 Auxio Project
 * ExoPlaybackStateHolder.kt is part of Auxio.
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
import android.media.AudioManager
import android.media.audiofx.AudioEffect
import androidx.media3.common.AudioAttributes
import androidx.media3.common.BaseRenderer
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultAudioSink
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.RenderersFactory
import androidx.media3.exoplayer.audio.MediaCodecAudioRenderer
import androidx.media3.exoplayer.audio.ReplayGainAudioProcessor
import androidx.media3.exoplayer.mediacodec.MediaCodecSelector
import androidx.media3.exoplayer.source.MediaSource
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.ArrayDeque
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import org.oxycblt.auxio.image.ImageSettings
import org.oxycblt.auxio.media.BetterShuffleOrder
import org.oxycblt.auxio.media.FfmpegAudioRenderer
import org.oxycblt.auxio.music.MusicRepository
import org.oxycblt.auxio.playback.PlaybackCommand
import org.oxycblt.auxio.playback.PlaybackSettings
import org.oxycblt.auxio.playback.persistence.PersistenceRepository
import org.oxycblt.auxio.playback.state.AudioFocusPolicy
import org.oxycblt.auxio.playback.state.AudioFocusState
import org.oxycblt.auxio.playback.state.PlaybackStateManager
import org.oxycblt.auxio.playback.state.Progression
import org.oxycblt.auxio.playback.state.RepeatMode
import org.oxycblt.auxio.playback.state.StateAck
import org.oxycblt.auxio.util.AudioManagerCompat
import org.oxycblt.musikr.MusicParent
import org.oxycblt.musikr.RawQueue
import org.oxycblt.musikr.Song
import timber.log.Timber as L

/**
 * An implementation of [PlaybackStateManager.PlaybackStateHolder] for the ExoPlayer-based playback
 * system.
 *
 * @author Alexander Capehart (OxygenCobalt)
 */
@UnstableApi
class ExoPlaybackStateHolder(
    private val context: Context,
    private val player: ExoPlayer,
    private val playbackManager: PlaybackStateManager,
    private val persistenceRepository: PersistenceRepository,
    private val playbackSettings: PlaybackSettings,
    private val commandFactory: PlaybackCommand.Factory,
    private val replayGainProcessor: ReplayGainAudioProcessor,
    private val musicRepository: MusicRepository,
    private val imageSettings: ImageSettings,
) :
    PlaybackStateManager.PlaybackStateHolder,
    Player.Listener,
    MusicRepository.UpdateListener,
    PlaybackSettings.Listener {

    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val focusRequest = AudioManagerCompat.focusRequest(::onAudioFocusChanged)
    private var hasAudioFocus = false
    private var audioFocusState = AudioFocusState()
    private var pauseFromAudioFocus = false
    private var sessionOngoing = false

    private val saveJob = Job()
    private val saveScope = CoroutineScope(saveJob + Dispatchers.IO)
    private var currentSaveJob: Job? = null

    private var audioSessionId = C.AUDIO_SESSION_ID_UNSET
    private var openAudioEffectSession = false

    private var parent: MusicParent? = null

    init {
        playbackSettings.registerListener(this)
        musicRepository.addUpdateListener(this)
        player.addListener(this)
    }

    override val progression: Progression
        get() {
            val mediaItem = player.currentMediaItem ?: return Progression.nil()
            val duration = mediaItem.mediaMetadata.extras?.getLong("durationMs") ?: Long.MAX_VALUE
            val clampedPosition = player.currentPosition.coerceAtLeast(0).coerceAtMost(duration)
            return Progression.from(player.playWhenReady, player.isPlaying, clampedPosition)
        }

    override val repeatMode
        get() =
            when (val repeatMode = player.repeatMode) {
                Player.REPEAT_MODE_OFF -> RepeatMode.NONE
                Player.REPEAT_MODE_ALL -> RepeatMode.ALL
                Player.REPEAT_MODE_ONE -> RepeatMode.TRACK
                else -> throw IllegalStateException("Invalid ExoPlayer repeat mode: $repeatMode")
            }

    override val isShuffled
        get() = player.shuffleModeEnabled

    override fun release() {
        playbackSettings.unregisterListener(this)
        musicRepository.removeUpdateListener(this)
        player.removeListener(this)
        player.release()
        abandonAudioFocus()
    }

    override fun playing(playing: Boolean) {
        if (playing && !requestAudioFocus()) {
            L.w("Cannot start playback: audio focus request denied")
            player.playWhenReady = false
            return
        }
        player.playWhenReady = playing
        if (!playing) {
            player.volume = 1f
            audioFocusState =
                audioFocusState.copy(
                    wasPlayingBeforeTransientLoss =
                        if (pauseFromAudioFocus) {
                            audioFocusState.wasPlayingBeforeTransientLoss
                        } else false
                )
        }
    }

    override fun seekTo(positionMs: Long) {
        player.seekTo(positionMs)
        deferSave()
    }

    override fun repeatMode(repeatMode: RepeatMode) {
        player.repeatMode =
            when (repeatMode) {
                RepeatMode.NONE -> Player.REPEAT_MODE_OFF
                RepeatMode.ALL -> Player.REPEAT_MODE_ALL
                RepeatMode.TRACK -> Player.REPEAT_MODE_ONE
            }
        updatePauseOnRepeat()
        deferSave()
    }

    override fun shuffle(shuffled: Boolean) {
        if (shuffled) {
            player.shuffleModeEnabled = true
            player.setShuffleOrder(BetterShuffleOrder(player.mediaItemCount))
        } else {
            player.shuffleModeEnabled = false
        }
        deferSave()
    }

    override fun goto(index: Int) {
        player.seekTo(index, C.TIME_UNSET)
        playbackManager.ack(this, StateAck.IndexMoved)
        deferSave()
    }

    override fun move(from: Int, to: Int) {
        player.moveMediaItem(from, to)
        deferSave()
    }

    override fun remove(index: Int) {
        player.removeMediaItem(index)
        deferSave()
    }

    override fun add(index: Int, song: Song) {
        player.addMediaItem(index, song.buildMediaItem())
        deferSave()
    }

    override fun add(index: Int, songs: List<Song>) {
        player.addMediaItems(index, songs.map { it.buildMediaItem() })
        deferSave()
    }

    override fun addAll(songs: List<Song>) {
        player.addMediaItems(songs.map { it.buildMediaItem() })
        deferSave()
    }

    override fun clear() {
        player.clearMediaItems()
        deferSave()
    }

    override fun resolveQueue(): RawQueue {
        val indices = player.unscrambleQueueIndices()
        val songs = (0 until player.mediaItemCount).map { i -> player.getMediaItemAt(i).song }
        return RawQueue(songs, player.currentMediaItemIndex, player.shuffleModeEnabled, indices)
    }

    override fun startSession() {
        sessionOngoing = true
    }

    override fun newPlayback(command: PlaybackCommand) {
        parent = command.parent
        player.shuffleModeEnabled = command.shuffled
        player.setMediaItems(command.queue.map { it.buildMediaItem() })
        val startIndex =
            command.song
                ?.let { command.queue.indexOf(it) }
                .also { check(it != -1) { "Start song not in queue" } }
        if (command.shuffled) {
            player.setShuffleOrder(BetterShuffleOrder(command.queue.size, startIndex ?: -1))
        }
        player.seekTo(startIndex ?: 0, C.TIME_UNSET)
        player.prepare()
        playing(true)
        playbackManager.ack(this, StateAck.NewPlayback(command.song))
        save {}
    }

    override fun applySavedState(
        parent: MusicParent?,
        rawQueue: RawQueue,
        positionMs: Long,
        repeatMode: RepeatMode,
        ack: StateAck.NewPlayback?,
    ) {
        var sendNewPlaybackEvent = false
        var shouldSeek = false
        if (this.parent != parent) {
            this.parent = parent
            sendNewPlaybackEvent = true
        }
        if (rawQueue != resolveQueue()) {
            val playWhenReady = player.playWhenReady
            player.setMediaItems(rawQueue.heap.map { it.buildMediaItem() })
            if (rawQueue.isShuffled) {
                player.shuffleModeEnabled = true
                player.setShuffleOrder(BetterShuffleOrder(rawQueue.shuffledMapping.toIntArray()))
            } else {
                player.shuffleModeEnabled = false
            }
            player.seekTo(rawQueue.heapIndex, C.TIME_UNSET)
            player.prepare()
            player.playWhenReady = playWhenReady
            sendNewPlaybackEvent = true
            shouldSeek = true
        }

        repeatMode(repeatMode)

        if (ack != null && (sendNewPlaybackEvent || shouldSeek)) {
            if (shouldSeek) {
                player.seekTo(positionMs)
            }
            playbackManager.ack(this, ack)
        }
    }

    override fun reset(ack: StateAck.NewPlayback) {
        player.setMediaItems(listOf())
        abandonAudioFocus()
        playbackManager.ack(this, ack)
        deferSave()
    }

    // --- PLAYER OVERRIDES ---

    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
        super.onPlayWhenReadyChanged(playWhenReady, reason)

        if (player.playWhenReady) {
            if (!requestAudioFocus()) {
                L.w("Cannot continue playback: audio focus request denied")
                player.pause()
                return
            }

            if (audioSessionId != player.audioSessionId) {
                audioSessionId = player.audioSessionId
                L.d("Sending AudioEffect broadcast for session $audioSessionId")
                broadcastAudioEffectAction(AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION)
                openAudioEffectSession = true
            }
        } else {
            // Make sure to close the audio session when we stop playback.
            L.d("Closing audio effect session")
            broadcastAudioEffectAction(AudioEffect.ACTION_CLOSE_AUDIO_EFFECT_CONTROL_SESSION)
            openAudioEffectSession = false
        }
        if (!player.playWhenReady) {
            player.volume = 1f
            audioFocusState =
                audioFocusState.copy(
                    wasPlayingBeforeTransientLoss =
                        if (pauseFromAudioFocus) {
                            audioFocusState.wasPlayingBeforeTransientLoss
                        } else false
                )
        }
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)

        if (playbackState == Player.STATE_ENDED && player.repeatMode == Player.REPEAT_MODE_OFF) {
            goto(0)
            player.pause()
        }
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        super.onMediaItemTransition(mediaItem, reason)

        if (reason == Player.MEDIA_ITEM_TRANSITION_REASON_AUTO) {
            playbackManager.ack(this, StateAck.IndexMoved)
        }
    }

    override fun onEvents(player: Player, events: Player.Events) {
        super.onEvents(player, events)

        // So many actions trigger progression changes that it becomes easier just to handle it
        // in an ExoPlayer callback anyway. This doesn't really cause issues anywhere.
        if (
            events.containsAny(
                Player.EVENT_PLAY_WHEN_READY_CHANGED,
                Player.EVENT_IS_PLAYING_CHANGED,
                Player.EVENT_POSITION_DISCONTINUITY,
            )
        ) {
            L.d("Player state changed, must synchronize state")
            playbackManager.ack(this, StateAck.ProgressionChanged)
        }
    }

    override fun onPlayerError(error: PlaybackException) {
        // TODO: Replace with no skipping and a notification instead
        // If there's any issue, just go to the next song.
        L.e("Player error occurred")
        L.e(error.stackTraceToString())
        player.prepare()
        playbackManager.next()
    }

    private fun broadcastAudioEffectAction(event: String) {
        L.d("Broadcasting AudioEffect event: $event")
        context.sendBroadcast(
            Intent(event)
                .putExtra(AudioEffect.EXTRA_PACKAGE_NAME, context.packageName)
                .putExtra(AudioEffect.EXTRA_AUDIO_SESSION, audioSessionId)
                .putExtra(AudioEffect.EXTRA_CONTENT_TYPE, AudioEffect.CONTENT_TYPE_MUSIC)
        )
    }

    private fun requestAudioFocus(): Boolean {
        if (hasAudioFocus) return true
        val result = AudioManagerCompat.requestAudioFocus(audioManager, focusRequest)
        hasAudioFocus = result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
        return hasAudioFocus
    }

    private fun abandonAudioFocus() {
        if (!hasAudioFocus) return
        AudioManagerCompat.abandonAudioFocusRequest(audioManager, focusRequest)
        hasAudioFocus = false
    }

    private fun onAudioFocusChanged(focusChange: Int) {
        val event =
            when (focusChange) {
                AudioManager.AUDIOFOCUS_LOSS -> AudioFocusPolicy.Event.LOSS
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> AudioFocusPolicy.Event.LOSS_TRANSIENT
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK ->
                    AudioFocusPolicy.Event.LOSS_TRANSIENT_CAN_DUCK
                AudioManager.AUDIOFOCUS_GAIN -> AudioFocusPolicy.Event.GAIN
                else -> return
            }

        val decision = AudioFocusPolicy.decide(event, audioFocusState, player.isPlaying)
        decision.rememberTransientPlayback?.let { remember ->
            audioFocusState = audioFocusState.copy(wasPlayingBeforeTransientLoss = remember)
        }
        if (decision.pause) {
            pauseFromAudioFocus = true
            try {
                playbackManager.playing(false)
            } finally {
                pauseFromAudioFocus = false
            }
        }
        player.volume = decision.volume
        if (
            AudioFocusPolicy.shouldResumePlayback(
                decision = decision,
                playWhenReady = player.playWhenReady,
                sessionOngoing = sessionOngoing,
                hasCurrentSong = playbackManager.currentSong != null,
            )
        ) {
            playbackManager.playing(true)
        }
        if (event == AudioFocusPolicy.Event.LOSS) {
            abandonAudioFocus()
        }
    }

    // --- MUSICREPOSITORY METHODS ---

    override fun onMusicChanges(changes: MusicRepository.Changes) {
        if (changes.deviceLibrary && musicRepository.library?.takeIf { !it.empty() } != null) {
            // We now have a library, see if we have anything we need to do.
            L.d("Library obtained, requesting action")
            playbackManager.requestAction(this)
        }
    }

    // --- PLAYBACKSETTINGS OVERRIDES ---

    override fun onPauseOnRepeatChanged() {
        super.onPauseOnRepeatChanged()
        updatePauseOnRepeat()
    }

    private fun updatePauseOnRepeat() {
        player.pauseAtEndOfMediaItems =
            player.repeatMode == Player.REPEAT_MODE_ONE && playbackSettings.pauseOnRepeat
    }

    private fun save(cb: () -> Unit) {
        saveJob {
            if (sessionOngoing) {
                persistenceRepository.saveState(playbackManager.toSavedState())
            }
            withContext(Dispatchers.Main) { cb() }
        }
    }

    private fun deferSave() {
        saveJob {
            L.d("Waiting for save buffer")
            delay(SAVE_BUFFER)
            yield()
            L.d("Committing saved state")
            if (sessionOngoing) {
                persistenceRepository.saveState(playbackManager.toSavedState())
            }
        }
    }

    private fun saveJob(block: suspend () -> Unit) {
        currentSaveJob?.let {
            L.d("Discarding prior save job")
            it.cancel()
        }
        currentSaveJob = saveScope.launch { block() }
    }

    private fun Song.buildMediaItem() = MediaItem.Builder().setUri(uri).setTag(this).build()

    private val MediaItem.song: Song?
        get() = this.localConfiguration?.tag as? Song?

    private fun Player.unscrambleQueueIndices(): List<Int> {
        val timeline = currentTimeline
        if (timeline.isEmpty) {
            return emptyList()
        }
        // Use a deque: prepending to an ArrayList in the loop below would shift the whole
        // backing array each time, going quadratic on long shuffled queues.
        val queue = ArrayDeque<Int>()

        // Add the active queue item.
        val currentMediaItemIndex = currentMediaItemIndex
        queue.add(currentMediaItemIndex)

        // Fill queue alternating with next and/or previous queue items.
        var firstMediaItemIndex = currentMediaItemIndex
        var lastMediaItemIndex = currentMediaItemIndex
        val shuffleModeEnabled = shuffleModeEnabled
        while ((firstMediaItemIndex != C.INDEX_UNSET || lastMediaItemIndex != C.INDEX_UNSET)) {
            // Begin with next to have a longer tail than head if an even sized queue needs to be
            // trimmed.
            if (lastMediaItemIndex != C.INDEX_UNSET) {
                lastMediaItemIndex =
                    timeline.getNextWindowIndex(
                        lastMediaItemIndex,
                        Player.REPEAT_MODE_OFF,
                        shuffleModeEnabled,
                    )
                if (lastMediaItemIndex != C.INDEX_UNSET) {
                    queue.add(lastMediaItemIndex)
                }
            }
            if (firstMediaItemIndex != C.INDEX_UNSET) {
                firstMediaItemIndex =
                    timeline.getPreviousWindowIndex(
                        firstMediaItemIndex,
                        Player.REPEAT_MODE_OFF,
                        shuffleModeEnabled,
                    )
                if (firstMediaItemIndex != C.INDEX_UNSET) {
                    queue.addFirst(firstMediaItemIndex)
                }
            }
        }

        return queue
    }

    class Factory
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
        private val playbackManager: PlaybackStateManager,
        private val persistenceRepository: PersistenceRepository,
        private val playbackSettings: PlaybackSettings,
        private val commandFactory: PlaybackCommand.Factory,
        private val mediaSourceFactory: MediaSource.Factory,
        private val replayGainProcessor: ReplayGainAudioProcessor,
        private val musicRepository: MusicRepository,
        private val imageSettings: ImageSettings,
    ) {
        fun create(): ExoPlaybackStateHolder {
            // Since Auxio is a music player, only specify an audio renderer to save
            // battery/apk size/cache size]
            val audioRenderer = RenderersFactory { handler, _, audioListener, _, _ ->
                arrayOf<BaseRenderer>(
                    FfmpegAudioRenderer(handler, audioListener, replayGainProcessor),
                    MediaCodecAudioRenderer(
                        context,
                        MediaCodecSelector.DEFAULT,
                        handler,
                        audioListener,
                        DefaultAudioSink.Builder(context)
                            .setAudioProcessors(arrayOf(replayGainProcessor))
                            .build(),
                    ),
                )
            }

            val exoPlayer =
                ExoPlayer.Builder(context, audioRenderer)
                    .setMediaSourceFactory(mediaSourceFactory)
                    // Enable automatic WakeLock support
                    .setWakeMode(C.WAKE_MODE_LOCAL)
                    .setAudioAttributes(
                        // Signal that we are a music player.
                        AudioAttributes.Builder()
                            .setUsage(C.USAGE_MEDIA)
                            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                            .build(),
                        false,
                    )
                    .build()

            return ExoPlaybackStateHolder(
                context,
                exoPlayer,
                playbackManager,
                persistenceRepository,
                playbackSettings,
                commandFactory,
                replayGainProcessor,
                musicRepository,
                imageSettings,
            )
        }
    }

    private companion object {
        const val SAVE_BUFFER = 5000L
    }
}
