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
import android.os.Handler
import android.os.Looper
import android.provider.OpenableColumns
import androidx.annotation.OptIn
import androidx.media.AudioAttributesCompat
import androidx.media.AudioFocusRequestCompat
import androidx.media.AudioManagerCompat
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.decoder.ffmpeg.FfmpegAudioRenderer
import androidx.media3.exoplayer.BaseRenderer
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.RenderersFactory
import androidx.media3.exoplayer.audio.DefaultAudioSink
import androidx.media3.exoplayer.audio.MediaCodecAudioRenderer
import androidx.media3.exoplayer.mediacodec.MediaCodecSelector
import androidx.media3.exoplayer.source.MediaSource
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.math.abs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import org.oxycblt.auxio.headunit.ts18.RawFastResumeItem
import org.oxycblt.auxio.headunit.ts18.RawFastResumeValidator
import org.oxycblt.auxio.headunit.ts18.Ts18FirstAudioLatency
import org.oxycblt.auxio.image.ImageSettings
import org.oxycblt.auxio.music.MusicRepository
import org.oxycblt.auxio.music.resolve
import org.oxycblt.auxio.music.resolveNames
import org.oxycblt.auxio.playback.PlaybackSettings
import org.oxycblt.auxio.playback.persist.FastResumeSnapshot
import org.oxycblt.auxio.playback.persist.PersistenceRepository
import org.oxycblt.auxio.playback.replaygain.ReplayGainAudioProcessor
import org.oxycblt.auxio.playback.state.DeferredPlayback
import org.oxycblt.auxio.playback.state.PlaybackCommand
import org.oxycblt.auxio.playback.state.PlaybackStateHolder
import org.oxycblt.auxio.playback.state.PlaybackStateManager
import org.oxycblt.auxio.playback.state.Progression
import org.oxycblt.auxio.playback.state.RawPlaybackMetadata
import org.oxycblt.auxio.playback.state.RawQueue
import org.oxycblt.auxio.playback.state.RepeatMode
import org.oxycblt.auxio.playback.state.ShuffleMode
import org.oxycblt.auxio.playback.state.StateAck
import org.oxycblt.musikr.Library
import org.oxycblt.musikr.MusicParent
import org.oxycblt.musikr.Song
import timber.log.Timber as L

@OptIn(UnstableApi::class)
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
    PlaybackStateHolder,
    Player.Listener,
    MusicRepository.UpdateListener,
    PlaybackSettings.Listener,
    ImageSettings.Listener {
    private val saveJob = Job()
    private val saveScope = CoroutineScope(Dispatchers.IO + saveJob)
    private val restoreScope = CoroutineScope(Dispatchers.IO + saveJob)
    private var currentSaveJob: Job? = null
    private var openAudioEffectSession = false
    private val audioManager = context.getSystemService(AudioManager::class.java)
    private val mainHandler = Handler(Looper.getMainLooper())
    private var audioFocusState = AudioFocusPolicy.State()
    private var hasAudioFocus = false
    private var pauseFromAudioFocus = false
    private var rawFastResumeItem: RawFastResumeItem? = null
    private var pendingLibraryRestoreAfterRawFailure: DeferredPlayback.RestoreState? = null
    private var markedFirstPlaying = false
    private val focusChangeListener = AudioManager.OnAudioFocusChangeListener(::onAudioFocusChanged)
    private val focusRequest: AudioFocusRequestCompat =
        AudioFocusRequestCompat.Builder(AudioManagerCompat.AUDIOFOCUS_GAIN)
            .setAudioAttributes(
                AudioAttributesCompat.Builder()
                    .setUsage(AudioAttributesCompat.USAGE_MEDIA)
                    .setContentType(AudioAttributesCompat.CONTENT_TYPE_MUSIC)
                    .build()
            )
            .setOnAudioFocusChangeListener(focusChangeListener, mainHandler)
            .setWillPauseWhenDucked(false)
            .build()

    var sessionOngoing = false
        private set

    val hasRawFastResume: Boolean
        get() = rawFastResumeItem != null

    val rawFastResumeDurationMs: Long?
        get() = rawFastResumeItem?.durationMs?.takeIf { it > 0L }

    override val isAudioFocusHeld: Boolean
        get() = hasAudioFocus

    fun attach() {
        playbackManager.registerStateHolder(this)
        musicRepository.addUpdateListener(this)
        player.addListener(this)
        replayGainProcessor.attach()
        playbackSettings.registerListener(this)
        imageSettings.registerListener(this)
    }

    fun release() {
        saveJob.cancel()
        playbackManager.unregisterStateHolder(this)
        musicRepository.removeUpdateListener(this)
        player.removeListener(this)
        replayGainProcessor.release()
        imageSettings.unregisterListener(this)
        playbackSettings.unregisterListener(this)
        abandonAudioFocus()
        player.release()
    }

    override var parent: MusicParent? = null
        private set

    override val progression: Progression
        get() {
            if (player.currentMediaItem == null) return Progression.nil()
            val duration = activeDurationLimitMs()
            val clampedPosition = player.currentPosition.coerceAtLeast(0).coerceAtMost(duration)
            return Progression.from(player.playWhenReady, player.isPlaying, clampedPosition)
        }

    private fun activeDurationLimitMs(): Long {
        rawFastResumeItem
            ?.durationMs
            ?.takeIf { it > 0L }
            ?.let {
                return it
            }
        player.currentMediaItem
            ?.song
            ?.durationMs
            ?.takeIf { it > 0L }
            ?.let {
                return it
            }
        return Long.MAX_VALUE
    }

    override val repeatMode
        get() =
            when (val repeatMode = player.repeatMode) {
                Player.REPEAT_MODE_OFF -> RepeatMode.NONE
                Player.REPEAT_MODE_ONE -> RepeatMode.TRACK
                Player.REPEAT_MODE_ALL -> RepeatMode.ALL
                else -> throw IllegalStateException("Unknown repeat mode: $repeatMode")
            }

    override val audioSessionId: Int
        get() = player.audioSessionId

    override val rawPlaybackMetadata: RawPlaybackMetadata?
        get() =
            rawFastResumeItem?.toRawPlaybackMetadata(
                positionMs = progression.calculateElapsedPositionMs().coerceAtLeast(0L),
                playing = progression.isPlaying,
            )

    override fun resolveQueue(): RawQueue {
        val library =
            musicRepository.library
                // No library, cannot do anything.
                ?: return RawQueue(emptyList(), emptyList(), 0)
        val heap = (0 until player.mediaItemCount).map { player.getMediaItemAt(it) }
        val shuffledMapping =
            if (player.shuffleModeEnabled) {
                player.unscrambleQueueIndices()
            } else {
                emptyList()
            }
        return RawQueue(heap.mapNotNull { it.song }, shuffledMapping, player.currentMediaItemIndex)
    }

    override fun handleDeferred(action: DeferredPlayback): Boolean {
        if (action is DeferredPlayback.RestoreState) {
            Ts18FirstAudioLatency.mark("restore_request_received")
            val library = musicRepository.library?.takeIf { !it.empty() }
            if (library == null) {
                L.d("Cached library not ready; attempting TS18 raw fast-resume snapshot")
                return tryStartRawFastResume(action)
            }
            rawFastResumeItem?.let {
                L.d("Library available after raw fast resume; attempting reconciliation")
                reconcileRawFastResume(library)
                return true
            }
            L.d("Restoring playback state from cached/loaded library")
            restoreScope.launch {
                val state = persistenceRepository.readState()
                withContext(Dispatchers.Main) {
                    if (state != null) {
                        // Apply the saved state on the main thread to prevent code expecting
                        // state updates on the main thread from crashing.
                        playbackManager.applySavedState(state, false)
                        val shouldPlay = shouldPlayImmediately(action.play)
                        if (shouldPlay) {
                            playbackManager.playing(true)
                        }
                    } else if (action.fallback != null) {
                        playbackManager.playDeferred(action.fallback)
                    }
                }
            }
            return true
        }

        val library =
            musicRepository.library?.takeIf { !it.empty() }
                // No library, cannot do anything.
                ?: return false

        when (action) {
            // Restore state is handled above so it can remain pending until the cached library
            // exists.
            is DeferredPlayback.RestoreState -> return false
            // Shuffle all -> Start new playback from all songs
            is DeferredPlayback.ShuffleAll -> {
                L.d("Shuffling all tracks")
                playbackManager.play(
                    requireNotNull(commandFactory.all(ShuffleMode.ON)) {
                        "Invalid playback parameters"
                    },
                    shouldPlayImmediately(action.play),
                )
            }
            // Open -> Try to find the Song for the given file and then play it from all songs
            is DeferredPlayback.Open -> {
                L.d("Opening specified file")
                restoreScope.launch {
                    val song = findDeferredOpenSong(action, library)
                    if (song == null) {
                        L.w("Unable to resolve opened file ${action.uri}")
                        return@launch
                    }
                    val command =
                        requireNotNull(commandFactory.songFromAll(song, ShuffleMode.IMPLICIT)) {
                            "Invalid playback command"
                        }
                    withContext(Dispatchers.Main) { playbackManager.play(command) }
                }
            }
        }

        return true
    }

    private suspend fun findDeferredOpenSong(
        action: DeferredPlayback.Open,
        library: Library,
    ): Song? =
        withContext(Dispatchers.IO) {
            try {
                context.applicationContext.contentResolver
                    .query(
                        action.uri,
                        arrayOf(OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE),
                        null,
                        null,
                        null,
                    )
                    ?.use { cursor ->
                        val displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                        if (displayNameIndex == -1 || sizeIndex == -1 || !cursor.moveToFirst()) {
                            return@use null
                        }
                        val displayName = cursor.getString(displayNameIndex) ?: return@use null
                        val size = cursor.getLong(sizeIndex)
                        library.songs.find { it.path.name == displayName && it.size == size }
                    }
            } catch (e: SecurityException) {
                L.w(e, "No permission to resolve opened file ${action.uri}")
                null
            } catch (e: Exception) {
                L.w(e, "Unable to resolve opened file ${action.uri}")
                null
            }
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
                        } else {
                            false
                        }
                )
            if (!pauseFromAudioFocus) {
                abandonAudioFocus()
            }
        }
    }

    override fun seekTo(positionMs: Long) {
        player.seekTo(positionMs)
        deferSave()
        // Ack handled w/ExoPlayer events
    }

    override fun repeatMode(repeatMode: RepeatMode) {
        player.repeatMode =
            when (repeatMode) {
                RepeatMode.NONE -> Player.REPEAT_MODE_OFF
                RepeatMode.ALL -> Player.REPEAT_MODE_ALL
                RepeatMode.TRACK -> Player.REPEAT_MODE_ONE
            }
        updatePauseOnRepeat()
        playbackManager.ack(this, StateAck.RepeatModeChanged)
        deferSave()
    }

    override fun newPlayback(command: PlaybackCommand, play: Boolean) {
        rawFastResumeItem = null
        pendingLibraryRestoreAfterRawFailure = null
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
        val target = startIndex ?: player.currentTimeline.getFirstWindowIndex(command.shuffled)
        player.seekTo(target, C.TIME_UNSET)
        player.prepare()
        if (play) {
            player.play()
        } else {
            player.pause()
        }
        playbackManager.ack(this, StateAck.NewPlayback)
        deferSave()
    }

    override fun shuffled(shuffled: Boolean) {
        player.setShuffleModeEnabled(shuffled)
        if (player.shuffleModeEnabled) {
            // Have to manually refresh the shuffle seed and anchor it to the new current songs
            player.setShuffleOrder(
                BetterShuffleOrder(player.mediaItemCount, player.currentMediaItemIndex)
            )
        }
        playbackManager.ack(this, StateAck.QueueReordered)
        deferSave()
    }

    override fun next() {
        if (rawFastResumeItem != null) {
            L.i("Ignoring next on single-item TS18 raw fast-resume playback")
            player.pause()
            playbackManager.ack(this, StateAck.ProgressionChanged)
            deferSave()
            return
        }
        // Replicate the old pseudo-circular queue behavior when no repeat option is implemented.
        // Basically, you can't skip back and wrap around the queue, but you can skip forward and
        // wrap around the queue, albeit playback will be paused.
        if (player.repeatMode == Player.REPEAT_MODE_ALL || player.hasNextMediaItem()) {
            player.seekToNext()
            if (!playbackSettings.rememberPause) {
                player.play()
            }
        } else {
            player.seekTo(
                player.currentTimeline.getFirstWindowIndex(player.shuffleModeEnabled),
                C.TIME_UNSET,
            )
            // TODO: Dislike the UX implications of this, I feel should I bite the bullet
            //  and switch to dynamic skip enable/disable?
            if (!playbackSettings.rememberPause) {
                player.pause()
            }
        }
        playbackManager.ack(this, StateAck.IndexMoved)
        deferSave()
    }

    override fun prev() {
        if (rawFastResumeItem != null) {
            player.seekTo(0)
            if (!playbackSettings.rememberPause) {
                player.play()
            }
            playbackManager.ack(this, StateAck.ProgressionChanged)
            deferSave()
            return
        }
        if (playbackSettings.rewindWithPrev) {
            player.seekToPrevious()
        } else if (player.hasPreviousMediaItem()) {
            player.seekToPreviousMediaItem()
        } else {
            player.seekTo(0)
        }
        if (!playbackSettings.rememberPause) {
            player.play()
        }
        playbackManager.ack(this, StateAck.IndexMoved)
        deferSave()
    }

    override fun goto(index: Int) {
        val indices = player.unscrambleQueueIndices()
        if (index !in indices.indices) {
            L.w("Ignoring goto with out-of-bounds index $index for ${indices.size} items")
            return
        }

        val trueIndex = indices[index]
        player.seekTo(trueIndex, C.TIME_UNSET) // Handles remaining custom logic
        if (!playbackSettings.rememberPause) {
            player.play()
        }
        playbackManager.ack(this, StateAck.IndexMoved)
        deferSave()
    }

    override fun playNext(songs: List<Song>, ack: StateAck.PlayNext) {
        val currTimeline = player.currentTimeline
        val nextIndex =
            if (currTimeline.isEmpty) {
                C.INDEX_UNSET
            } else {
                currTimeline.getNextWindowIndex(
                    player.currentMediaItemIndex,
                    Player.REPEAT_MODE_OFF,
                    player.shuffleModeEnabled,
                )
            }

        if (nextIndex == C.INDEX_UNSET) {
            player.addMediaItems(songs.map { it.buildMediaItem() })
        } else {
            player.addMediaItems(nextIndex, songs.map { it.buildMediaItem() })
        }
        playbackManager.ack(this, ack)
        deferSave()
    }

    override fun addToQueue(songs: List<Song>, ack: StateAck.AddToQueue) {
        player.addMediaItems(songs.map { it.buildMediaItem() })
        playbackManager.ack(this, ack)
        deferSave()
    }

    override fun move(from: Int, to: Int, ack: StateAck.Move) {
        val indices = player.unscrambleQueueIndices()
        if (from !in indices.indices || to !in indices.indices) {
            L.w("Ignoring move with out-of-bounds indices [$from, $to] for ${indices.size} items")
            return
        }

        val trueFrom = indices[from]
        val trueTo = indices[to]
        // ExoPlayer does not actually update it's ShuffleOrder when moving items. Retain a
        // semblance of "normalcy" by doing a weird no-op swap that actually moves the item.
        when {
            trueFrom > trueTo -> {
                player.moveMediaItem(trueFrom, trueTo)
                player.moveMediaItem(trueTo + 1, trueFrom)
            }
            trueTo > trueFrom -> {
                player.moveMediaItem(trueFrom, trueTo)
                player.moveMediaItem(trueTo - 1, trueFrom)
            }
        }
        playbackManager.ack(this, ack)
        deferSave()
    }

    override fun remove(at: Int, ack: StateAck.Remove) {
        val indices = player.unscrambleQueueIndices()
        if (at !in indices.indices) {
            L.w("Ignoring remove with out-of-bounds index $at for ${indices.size} items")
            return
        }

        val trueIndex = indices[at]
        val songWillChange = player.currentMediaItemIndex == trueIndex
        player.removeMediaItem(trueIndex)
        if (songWillChange && !playbackSettings.rememberPause) {
            player.play()
        }
        playbackManager.ack(this, ack)
        deferSave()
    }

    override fun applySavedState(
        parent: MusicParent?,
        rawQueue: RawQueue,
        positionMs: Long,
        repeatMode: RepeatMode,
        ack: StateAck.NewPlayback?,
    ) {
        rawFastResumeItem = null
        pendingLibraryRestoreAfterRawFailure = null
        var sendNewPlaybackEvent = false
        var shouldSeek = false
        if (this.parent != parent) {
            this.parent = parent
            sendNewPlaybackEvent = true
        }
        if (rawQueue != resolveQueue()) {
            val wasPlaying = player.playWhenReady
            player.setMediaItems(rawQueue.heap.map { it.buildMediaItem() })
            if (rawQueue.isShuffled) {
                player.shuffleModeEnabled = true
                player.setShuffleOrder(BetterShuffleOrder(rawQueue.shuffledMapping.toIntArray()))
            } else {
                player.shuffleModeEnabled = false
            }
            player.seekTo(rawQueue.heapIndex, C.TIME_UNSET)
            player.prepare()
            if (wasPlaying) {
                player.play()
            } else {
                player.pause()
            }
            sendNewPlaybackEvent = true
            shouldSeek = true
        }

        repeatMode(repeatMode)
        // See if we differ by more than a second. This allows us to avoid a meaningless seek
        // in the case of a "tight restore" (i.e music was reloaded).
        // In the case that this is a false positive, it's not very percievable (at least compared
        // to skipping when updating the library).
        // TODO: Introduce a better state management system rather than do something finicky like
        // this.
        if (shouldSeek || abs(player.currentPosition - positionMs) > 1000L) {
            player.seekTo(positionMs)
        }

        if (sendNewPlaybackEvent) {
            ack?.let { playbackManager.ack(this, it) }
        }
    }

    override fun endSession() {
        // This session has ended, so we need to reset this flag for when the next
        // session starts.
        playbackManager.playing(false)
        abandonAudioFocus()
        save {
            // User could feasibly start playing again if they were fast enough, so
            // we need to avoid stopping the foreground state if that's the case.
            if (!playbackManager.progression.isPlaying) {
                sessionOngoing = false
                playbackManager.ack(this, StateAck.SessionEnded)
            }
        }
    }

    override fun reset(ack: StateAck.NewPlayback) {
        rawFastResumeItem = null
        pendingLibraryRestoreAfterRawFailure = null
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
            // Mark that we have started playing so that the notification can now be posted.
            L.d("Player has started playing")
            sessionOngoing = true
            if (!openAudioEffectSession) {
                // Convention to start an audioeffect session on play/pause rather than
                // start/stop
                L.d("Opening audio effect session")
                broadcastAudioEffectAction(AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION)
                openAudioEffectSession = true
            }
        } else if (openAudioEffectSession) {
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
                        } else {
                            false
                        }
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
        if (player.isPlaying && !markedFirstPlaying) {
            markedFirstPlaying = true
            Ts18FirstAudioLatency.mark("first_playing_state")
        }
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
        L.e("Player error occurred")
        L.e(error.stackTraceToString())
        if (rawFastResumeItem != null) {
            L.w("TS18 raw fast-resume item failed; clearing raw playback without unsafe next()")
            rawFastResumeItem = null
            player.setMediaItems(emptyList())
            player.pause()
            saveScope.launch { persistenceRepository.saveFastResumeSnapshot(null) }
            playbackManager.ack(this, StateAck.ProgressionChanged)
            return
        }
        // TODO: Replace with no skipping and a notification instead
        // If there's any issue in normal library playback, keep the existing next-song behaviour.
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
        val library = musicRepository.library?.takeIf { !it.empty() }
        if (changes.deviceLibrary && library != null) {
            rawFastResumeItem?.let {
                L.d("Library obtained while raw fast-resume is active; reconciling")
                reconcileRawFastResume(library)
                return
            }
            pendingLibraryRestoreAfterRawFailure?.let { pending ->
                L.d("Library obtained after raw fast-resume miss; replaying saved-state restore")
                pendingLibraryRestoreAfterRawFailure = null
                playbackManager.playDeferred(pending)
                return
            }
            // We now have a library, see if we have anything we need to do.
            L.d("Library obtained, requesting action")
            playbackManager.requestAction(this)
        }
    }

    // --- PLAYBACKSETTINGS OVERRIDES ---
    private fun tryStartRawFastResume(action: DeferredPlayback.RestoreState): Boolean {
        pendingLibraryRestoreAfterRawFailure = action
        restoreScope.launch {
            Ts18FirstAudioLatency.mark("snapshot_read_start")
            val snapshot = persistenceRepository.readFastResumeSnapshot()
            Ts18FirstAudioLatency.mark("snapshot_read_end")
            if (snapshot == null) {
                L.d("No TS18 fast-resume snapshot available")
                return@launch
            }
            Ts18FirstAudioLatency.mark("raw_media_validation_start")
            val validation = RawFastResumeValidator.validate(context, snapshot)
            Ts18FirstAudioLatency.mark("raw_media_validation_end")
            withContext(Dispatchers.Main) {
                when (validation) {
                    is RawFastResumeValidator.Result.Valid -> {
                        if (pendingLibraryRestoreAfterRawFailure !== action) {
                            L.d(
                                "Skipping late TS18 raw fast-resume result; restore was already consumed"
                            )
                            return@withContext
                        }
                        pendingLibraryRestoreAfterRawFailure = null
                        val shouldPlay = shouldPlayImmediately(action.play)
                        startRawFastResume(validation.item, shouldPlay)
                    }
                    is RawFastResumeValidator.Result.Invalid -> {
                        L.w(
                            "Ignoring invalid TS18 fast-resume snapshot: " +
                                validation.reason +
                                " " +
                                validation.detail
                        )
                    }
                }
            }
        }
        return true
    }

    private fun startRawFastResume(item: RawFastResumeItem, play: Boolean) {
        Ts18FirstAudioLatency.mark("raw_media_item_set")
        rawFastResumeItem = item
        parent = null
        player.shuffleModeEnabled = false
        player.setMediaItems(listOf(item.buildMediaItem()))
        player.seekTo(0, item.positionMs)
        Ts18FirstAudioLatency.mark("raw_seek")
        player.prepare()
        Ts18FirstAudioLatency.mark("raw_prepare")
        sessionOngoing = true
        if (play) {
            playing(true)
        } else {
            player.playWhenReady = false
        }
        playbackManager.ack(this, StateAck.NewPlayback)
        playbackManager.ack(this, StateAck.ProgressionChanged)
        deferSave()
    }

    private fun reconcileRawFastResume(library: Library) {
        val raw = rawFastResumeItem ?: return
        Ts18FirstAudioLatency.mark("reconciliation_start")
        restoreScope.launch {
            val song = findSongForRawFastResume(raw, library)
            withContext(Dispatchers.Main) {
                if (rawFastResumeItem !== raw) {
                    L.d("Skipping stale TS18 raw reconciliation result")
                    return@withContext
                }
                if (song == null) {
                    L.i(
                        "Unable to reconcile raw TS18 fast-resume item yet; leaving raw playback active"
                    )
                    Ts18FirstAudioLatency.mark("reconciliation_end_unmatched")
                    return@withContext
                }
                val command = commandFactory.songFromAll(song, ShuffleMode.IMPLICIT)
                if (command == null) {
                    L.w(
                        "Unable to build reconciliation command for ${song.uri}; leaving raw playback active"
                    )
                    Ts18FirstAudioLatency.mark("reconciliation_end_no_command")
                    return@withContext
                }
                val wasPlaying = player.playWhenReady || player.isPlaying
                val positionMs = progression.calculateElapsedPositionMs().coerceAtLeast(0L)
                rawFastResumeItem = null
                pendingLibraryRestoreAfterRawFailure = null
                playbackManager.play(command)
                playbackManager.seekTo(positionMs.coerceAtMost(song.durationMs.coerceAtLeast(0L)))
                playbackManager.playing(wasPlaying)
                Ts18FirstAudioLatency.mark("reconciliation_end_matched")
            }
        }
    }

    private fun findSongForRawFastResume(raw: RawFastResumeItem, library: Library): Song? {
        library.songs
            .firstOrNull { it.uri.toString() == raw.uriString }
            ?.let {
                return it
            }
        val rawPath = raw.path?.takeIf { it.isNotBlank() }
        if (rawPath != null) {
            val appContext = context.applicationContext
            library.songs
                .firstOrNull { song ->
                    try {
                        song.path.resolve(appContext) == rawPath
                    } catch (e: Exception) {
                        false
                    }
                }
                ?.let {
                    return it
                }
        }
        val rawTitle = raw.title?.trim()?.lowercase()?.takeIf { it.isNotEmpty() }
        if (rawTitle != null && raw.durationMs > 0L) {
            val appContext = context.applicationContext
            library.songs
                .firstOrNull { song ->
                    val title =
                        try {
                            song.name.resolve(appContext).trim().lowercase()
                        } catch (e: Exception) {
                            ""
                        }
                    title == rawTitle && kotlin.math.abs(song.durationMs - raw.durationMs) <= 1000L
                }
                ?.let {
                    return it
                }
        }
        return null
    }

    override fun onPauseOnRepeatChanged() {
        super.onPauseOnRepeatChanged()
        updatePauseOnRepeat()
    }

    private fun updatePauseOnRepeat() {
        player.pauseAtEndOfMediaItems =
            player.repeatMode == Player.REPEAT_MODE_ONE && playbackSettings.pauseOnRepeat
    }

    private suspend fun saveFastResumeSnapshot() {
        val (song, progression, raw) =
            withContext(Dispatchers.Main) {
                Triple(playbackManager.currentSong, playbackManager.progression, rawFastResumeItem)
            }

        if (song == null) {
            if (raw != null) {
                val rawSnapshot =
                    raw.toSnapshot(
                        positionMs = progression.calculateElapsedPositionMs().coerceAtLeast(0L),
                        playing = progression.isPlaying,
                    )
                if (!persistenceRepository.saveFastResumeSnapshot(rawSnapshot)) {
                    L.w("Unable to persist raw TS18 fast-resume snapshot")
                }
            } else if (!persistenceRepository.saveFastResumeSnapshot(null)) {
                L.w("Unable to clear TS18 fast-resume snapshot")
            }
            return
        }

        val appContext = context.applicationContext
        val snapshot =
            FastResumeSnapshot(
                uri = song.uri.toString(),
                path =
                    try {
                        song.path.resolve(appContext)
                    } catch (e: Exception) {
                        L.w(e, "Unable to resolve path for TS18 fast-resume snapshot")
                        null
                    },
                title =
                    try {
                        song.name.resolve(appContext)
                    } catch (e: Exception) {
                        L.w(e, "Unable to resolve title for TS18 fast-resume snapshot")
                        null
                    },
                artist =
                    try {
                        song.artists.resolveNames(appContext)
                    } catch (e: Exception) {
                        L.w(e, "Unable to resolve artist for TS18 fast-resume snapshot")
                        null
                    },
                album =
                    try {
                        song.album.name.resolve(appContext)
                    } catch (e: Exception) {
                        L.w(e, "Unable to resolve album for TS18 fast-resume snapshot")
                        null
                    },
                durationMs = song.durationMs,
                positionMs = progression.calculateElapsedPositionMs().coerceAtLeast(0L),
                playing = progression.isPlaying,
                savedAtMs = System.currentTimeMillis(),
            )

        if (!persistenceRepository.saveFastResumeSnapshot(snapshot)) {
            L.w("Unable to persist TS18 fast-resume snapshot")
        }
    }

    private fun save(cb: () -> Unit) {
        saveJob {
            if (sessionOngoing) {
                saveFastResumeSnapshot()
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
                saveFastResumeSnapshot()
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

    private fun shouldPlayImmediately(playActionRequested: Boolean): Boolean {
        return playActionRequested ||
            (playbackSettings.autoplayOnLaunch && playbackSettings.alwaysPlayImmediately)
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
