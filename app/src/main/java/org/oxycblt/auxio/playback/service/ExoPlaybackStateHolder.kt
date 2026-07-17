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
import android.net.Uri
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
import androidx.media3.common.MediaMetadata
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
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import org.oxycblt.auxio.headunit.ts18.RawFastResumeItem
import org.oxycblt.auxio.headunit.ts18.RawFastResumeValidator
import org.oxycblt.auxio.headunit.ts18.Ts18FirstAudioLatency
import org.oxycblt.auxio.image.ImageSettings
import org.oxycblt.auxio.music.MusicRepository
import org.oxycblt.auxio.music.StartupReadinessController
import org.oxycblt.auxio.music.StartupReadinessState
import org.oxycblt.auxio.music.resolve
import org.oxycblt.auxio.music.resolveNames
import org.oxycblt.auxio.playback.PlaybackSettings
import org.oxycblt.auxio.playback.persist.FastResumeSnapshot
import org.oxycblt.auxio.playback.persist.PersistenceRepository
import org.oxycblt.auxio.playback.persist.QueueItemRef
import org.oxycblt.auxio.playback.persist.QueueWindow
import org.oxycblt.auxio.playback.persist.QueueWindowPolicy
import org.oxycblt.auxio.playback.replaygain.ReplayGainAudioProcessor
import org.oxycblt.auxio.playback.state.DeferredPlayback
import org.oxycblt.auxio.playback.state.PlaybackCommand
import org.oxycblt.auxio.playback.state.PlaybackStateHolder
import org.oxycblt.auxio.playback.state.PlaybackStateManager
import org.oxycblt.auxio.playback.state.Progression
import org.oxycblt.auxio.playback.state.RawPlaybackMetadata
import org.oxycblt.auxio.playback.state.RawQueue
import org.oxycblt.auxio.playback.state.RepeatMode
import org.oxycblt.auxio.playback.state.RestoreOutcome
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
    private val startupReadinessController: StartupReadinessController,
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
    private var currentRestoreJob: Job? = null
    private var restoreGeneration = 0L
    private var openAudioEffectSession = false
    private val audioManager = context.getSystemService(AudioManager::class.java)
    private val mainHandler = Handler(Looper.getMainLooper())
    private var audioFocusState = AudioFocusPolicy.State()
    private var hasAudioFocus = false
    private var pauseFromAudioFocus = false
    private var rawFastResumeItem: RawFastResumeItem? = null
    private var activePrimitiveWindow: QueueWindow? = null
    private var primitiveNavigationJob: Job? = null
    private var primitivePrefetchJob: Job? = null
    private val primitiveMutationMutex = Mutex()
    private var pendingPrimitiveTarget: Int? = null
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

    override val primitiveQueueWindow: QueueWindow?
        get() = activePrimitiveWindow

    fun attach() {
        playbackManager.registerStateHolder(this)
        musicRepository.addUpdateListener(this)
        player.addListener(this)
        replayGainProcessor.attach()
        playbackSettings.registerListener(this)
        imageSettings.registerListener(this)
    }

    fun release() {
        cancelActiveRestore("holder-release", notify = false)
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
        activePrimitiveWindow
            ?.currentItem
            ?.durationMs
            ?.takeIf { it > 0L }
            ?.let {
                return it
            }
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
        get() {
            rawFastResumeItem?.let {
                return it.toRawPlaybackMetadata(
                    positionMs = progression.calculateElapsedPositionMs().coerceAtLeast(0L),
                    playing = progression.isPlaying,
                )
            }
            val window = activePrimitiveWindow ?: return null
            val item = window.currentItem ?: return null
            return RawPlaybackMetadata(
                title = item.titleFallback,
                artist = item.artistFallback,
                album = item.albumFallback,
                uriString = item.uri.orEmpty(),
                path = item.pathFallback,
                durationMs = item.durationMs,
                positionMs = progression.calculateElapsedPositionMs().coerceAtLeast(0L),
                isPlaying = progression.isPlaying,
                savedAtMs = window.descriptor.updatedAtMs,
            )
        }

    override fun resolveQueue(): RawQueue {
        if (activePrimitiveWindow != null) return RawQueue.nil()
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
            startPrimitiveQueueRestore(action)
            return true
        }

        val library = musicRepository.library?.takeIf { !it.empty() }
        if (library == null) {
            if (action is DeferredPlayback.Open) {
                startDirectOpen(action)
                return true
            }
            return false
        }

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

    private fun startDirectOpen(action: DeferredPlayback.Open) {
        cancelActiveRestore("direct-open", notify = false)
        val generation = ++restoreGeneration
        currentRestoreJob =
            restoreScope.launch {
                try {
                    val uri = action.uri
                    val snapshot =
                        FastResumeSnapshot(
                            uri = uri.toString(),
                            path =
                                uri.path.takeIf {
                                    uri.scheme.isNullOrBlank() || uri.scheme == "file"
                                },
                            title = uri.lastPathSegment,
                            artist = null,
                            album = null,
                            durationMs = 0L,
                            positionMs = 0L,
                            playing = true,
                            savedAtMs = System.currentTimeMillis(),
                        )
                    val validation = RawFastResumeValidator.validate(context, snapshot)
                    withContext(Dispatchers.Main) {
                        if (generation != restoreGeneration) return@withContext
                        when (validation) {
                            is RawFastResumeValidator.Result.Valid -> {
                                startRawFastResume(validation.item, play = true)
                                completeRestore(generation, RestoreOutcome.RAW_FAST_RESUME_ACTIVE)
                            }
                            is RawFastResumeValidator.Result.Invalid -> {
                                L.w(
                                    "Ignoring invalid Fast Start media ${action.uri}: " +
                                        "${validation.reason} ${validation.detail}"
                                )
                                completeRestore(generation, RestoreOutcome.FAILED)
                            }
                        }
                    }
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    L.w(e, "Unable to open Fast Start media ${action.uri}")
                    withContext(Dispatchers.Main) {
                        if (generation == restoreGeneration) {
                            completeRestore(generation, RestoreOutcome.FAILED)
                        }
                    }
                } finally {
                    if (generation == restoreGeneration) currentRestoreJob = null
                }
            }
    }

    private fun startPrimitiveQueueRestore(action: DeferredPlayback.RestoreState) {
        playbackManager.notifyRestoreOutcome(RestoreOutcome.WAITING_FOR_PLAYER)
        currentRestoreJob?.cancel()
        val generation = ++restoreGeneration
        currentRestoreJob =
            restoreScope.launch {
                try {
                    Ts18FirstAudioLatency.mark("primitive_session_read_start")
                    val descriptor = persistenceRepository.readQueueDescriptor()
                    Ts18FirstAudioLatency.mark("primitive_session_read_end")
                    if (descriptor == null) {
                        startupReadinessController.publishCapability(
                            StartupReadinessState.QueueReady
                        )
                        withContext(Dispatchers.Main) { startRawFallback(action, generation) }
                        return@launch
                    }

                    var window = persistenceRepository.readQueueWindowAround(descriptor)
                    var current = window?.currentItem
                    if (current?.hasPlayableReference != true) {
                        val snapshot = persistenceRepository.readFastResumeSnapshot()
                        if (snapshot != null) {
                            persistenceRepository.enrichQueueItem(
                                descriptor,
                                descriptor.currentLogicalPosition,
                                snapshot,
                            )
                            window = persistenceRepository.readQueueWindowAround(descriptor)
                            current = window?.currentItem
                        }
                    }
                    val playableWindow = window?.contiguousPlayableWindow()
                    if (playableWindow == null || current?.hasPlayableReference != true) {
                        startupReadinessController.publishCapability(
                            StartupReadinessState.QueueReady
                        )
                        withContext(Dispatchers.Main) { startRawFallback(action, generation) }
                        return@launch
                    }

                    withContext(Dispatchers.Main) {
                        if (generation != restoreGeneration) return@withContext
                        attachPrimitiveWindow(
                            window = playableWindow,
                            targetLogicalPosition = descriptor.currentLogicalPosition,
                            positionMs = descriptor.positionMs,
                            play = shouldPlayImmediately(action.play),
                        )
                        startupReadinessController.publishCapability(
                            StartupReadinessState.QueueReady
                        )
                        completeRestore(generation, RestoreOutcome.RESTORED_EXISTING_SESSION)
                    }
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    L.w(e, "Unable to restore primitive playback queue")
                    startupReadinessController.publishCapability(StartupReadinessState.QueueReady)
                    withContext(Dispatchers.Main) {
                        if (generation == restoreGeneration) {
                            startRawFallback(action, generation)
                        }
                    }
                } finally {
                    if (generation == restoreGeneration) currentRestoreJob = null
                }
            }
    }

    private fun startRawFallback(action: DeferredPlayback.RestoreState, generation: Long) {
        if (generation != restoreGeneration) return
        playbackManager.notifyRestoreOutcome(RestoreOutcome.WAITING_FOR_LIBRARY)
        tryStartRawFastResume(action, generation)
    }

    private fun QueueWindow.contiguousPlayableWindow(): QueueWindow? {
        val current = currentLocalPosition
        if (current !in items.indices || !items[current].hasPlayableReference) return null
        var start = current
        while (start > 0 && items[start - 1].hasPlayableReference) start--
        var end = current + 1
        while (end < items.size && items[end].hasPlayableReference) end++
        val subset = items.subList(start, end)
        return QueueWindow(
            descriptor = descriptor,
            startLogicalPosition = startLogicalPosition + start,
            items = subset,
        )
    }

    private fun attachPrimitiveWindow(
        window: QueueWindow,
        targetLogicalPosition: Int,
        positionMs: Long,
        play: Boolean,
    ) {
        val localTarget = window.globalToLocal(targetLogicalPosition) ?: return
        val mediaItems = window.items.mapNotNull { it.buildPrimitiveMediaItemOrNull() }
        if (mediaItems.size != window.items.size) {
            L.w("Primitive queue window contains unresolved media references")
            return
        }
        val descriptor =
            window.descriptor.copy(
                currentLogicalPosition = targetLogicalPosition,
                positionMs = positionMs.coerceAtLeast(0L),
                updatedAtMs = System.currentTimeMillis(),
            )
        activePrimitiveWindow = window.copy(descriptor = descriptor)
        rawFastResumeItem = null
        pendingLibraryRestoreAfterRawFailure = null
        parent = null
        player.shuffleModeEnabled = false
        player.setMediaItems(mediaItems)
        player.repeatMode =
            when (descriptor.repeatMode) {
                RepeatMode.NONE -> Player.REPEAT_MODE_OFF
                RepeatMode.ALL -> Player.REPEAT_MODE_ALL
                RepeatMode.TRACK -> Player.REPEAT_MODE_ONE
            }
        player.seekTo(localTarget, positionMs.coerceAtLeast(0L))
        player.prepare()
        sessionOngoing = true
        if (play) playing(true) else player.playWhenReady = false
        playbackManager.ack(this, StateAck.QueueWindowChanged)
        playbackManager.ack(this, StateAck.ProgressionChanged)
        deferSave()
        maybePrefetchPrimitiveWindow()
    }

    private fun QueueItemRef.buildPrimitiveMediaItemOrNull(): MediaItem? {
        val parsed =
            uri?.takeIf { it.isNotBlank() }?.let(Uri::parse)
                ?: pathFallback
                    ?.takeIf { RawFastResumeValidator.isAllowedDirectPath(it) }
                    ?.let { Uri.fromFile(java.io.File(it)) }
                ?: return null
        val scheme = parsed.scheme?.lowercase()
        if (scheme !in setOf("content", "file")) return null
        val metadata =
            MediaMetadata.Builder()
                .setTitle(displayTitle)
                .setArtist(artistFallback)
                .setAlbumTitle(albumFallback)
                .build()
        return MediaItem.Builder().setUri(parsed).setMediaMetadata(metadata).setTag(this).build()
    }

    private fun navigatePrimitive(targetLogicalPosition: Int, play: Boolean) {
        val window = activePrimitiveWindow ?: return
        val safeTarget = targetLogicalPosition.coerceIn(0, window.descriptor.totalCount - 1)
        val local = window.globalToLocal(safeTarget)
        if (local != null && window.items[local].hasPlayableReference) {
            activePrimitiveWindow =
                window.copy(
                    descriptor =
                        window.descriptor.copy(
                            currentLogicalPosition = safeTarget,
                            positionMs = 0L,
                            updatedAtMs = System.currentTimeMillis(),
                        )
                )
            player.seekTo(local, C.TIME_UNSET)
            if (play) player.play() else player.pause()
            playbackManager.ack(this, StateAck.QueueWindowChanged)
            playbackManager.ack(this, StateAck.ProgressionChanged)
            deferSave()
            maybePrefetchPrimitiveWindow()
            return
        }

        synchronized(this) {
            pendingPrimitiveTarget = safeTarget
            if (primitiveNavigationJob?.isActive == true) return
            primitiveNavigationJob =
                restoreScope.launch {
                    try {
                        while (true) {
                            val target =
                                synchronized(this@ExoPlaybackStateHolder) {
                                    pendingPrimitiveTarget?.also { pendingPrimitiveTarget = null }
                                } ?: break
                            val descriptor = activePrimitiveWindow?.descriptor ?: break
                            val requested = descriptor.copy(currentLogicalPosition = target)
                            val loaded =
                                persistenceRepository
                                    .readQueueWindowAround(requested, target)
                                    ?.contiguousPlayableWindow()
                            withContext(Dispatchers.Main) {
                                val currentDescriptor = activePrimitiveWindow?.descriptor
                                if (
                                    loaded != null &&
                                        currentDescriptor?.sessionId == descriptor.sessionId &&
                                        currentDescriptor.revision == descriptor.revision
                                ) {
                                    attachPrimitiveWindow(
                                        window = loaded,
                                        targetLogicalPosition = target,
                                        positionMs = 0L,
                                        play = play,
                                    )
                                } else {
                                    L.w("Unable to load primitive queue target $target")
                                }
                            }
                        }
                    } finally {
                        val pending =
                            synchronized(this@ExoPlaybackStateHolder) {
                                primitiveNavigationJob = null
                                pendingPrimitiveTarget?.also { pendingPrimitiveTarget = null }
                            }
                        if (pending != null) {
                            withContext(Dispatchers.Main) { navigatePrimitive(pending, play) }
                        }
                    }
                }
        }
    }

    private fun maybePrefetchPrimitiveWindow() {
        val window = activePrimitiveWindow ?: return
        val current = window.descriptor.currentLogicalPosition
        if (
            !QueueWindowPolicy.shouldPrefetchBefore(window, current) &&
                !QueueWindowPolicy.shouldPrefetchAfter(window, current)
        ) {
            return
        }
        if (window.items.size >= QueueWindowPolicy.MAX_LOADED_ITEMS) return
        if (primitivePrefetchJob?.isActive == true) return
        primitivePrefetchJob =
            restoreScope.launch {
                val descriptor = window.descriptor
                val range =
                    QueueWindowPolicy.around(
                        descriptor.totalCount,
                        current,
                        QueueWindowPolicy.MAX_LOADED_ITEMS / 2,
                    )
                val expanded =
                    persistenceRepository
                        .readQueueWindow(descriptor, range.startInclusive, range.endExclusive)
                        ?.contiguousPlayableWindow()
                if (expanded == null || expanded.items.size <= window.items.size) return@launch
                withContext(Dispatchers.Main) {
                    val active = activePrimitiveWindow ?: return@withContext
                    if (
                        active.descriptor.sessionId != descriptor.sessionId ||
                            active.descriptor.revision != descriptor.revision ||
                            active.descriptor.currentLogicalPosition != current
                    ) {
                        return@withContext
                    }
                    val expandedWithPosition =
                        expanded.copy(
                            descriptor =
                                expanded.descriptor.copy(
                                    currentLogicalPosition = current,
                                    positionMs = player.currentPosition.coerceAtLeast(0L),
                                    updatedAtMs = System.currentTimeMillis(),
                                )
                        )
                    if (!expandPrimitiveWindowInPlace(expandedWithPosition)) {
                        attachPrimitiveWindow(
                            expandedWithPosition,
                            current,
                            player.currentPosition.coerceAtLeast(0L),
                            player.playWhenReady,
                        )
                    }
                }
            }
    }

    private fun expandPrimitiveWindowInPlace(expanded: QueueWindow): Boolean {
        val active = activePrimitiveWindow ?: return false
        if (
            expanded.descriptor.sessionId != active.descriptor.sessionId ||
                expanded.startLogicalPosition > active.startLogicalPosition ||
                expanded.endLogicalPositionExclusive < active.endLogicalPositionExclusive
        ) {
            return false
        }
        val mediaItems = expanded.items.mapNotNull { it.buildPrimitiveMediaItemOrNull() }
        if (mediaItems.size != expanded.items.size) return false
        val prependCount = active.startLogicalPosition - expanded.startLogicalPosition
        val appendStart = prependCount + active.items.size
        if (prependCount > 0) {
            player.addMediaItems(0, mediaItems.subList(0, prependCount))
        }
        if (appendStart < mediaItems.size) {
            player.addMediaItems(mediaItems.subList(appendStart, mediaItems.size))
        }
        mediaItems.forEachIndexed { index, mediaItem ->
            if (index < player.mediaItemCount) player.replaceMediaItem(index, mediaItem)
        }
        activePrimitiveWindow = expanded
        playbackManager.ack(this, StateAck.QueueWindowChanged)
        return true
    }

    private fun synchronizePrimitivePositionFromPlayer(): QueueWindow? {
        val window = activePrimitiveWindow ?: return null
        val global = window.localToGlobal(player.currentMediaItemIndex) ?: return window
        val updated =
            window.copy(
                descriptor =
                    window.descriptor.copy(
                        currentLogicalPosition = global,
                        positionMs = player.currentPosition.coerceAtLeast(0L),
                        updatedAtMs = System.currentTimeMillis(),
                    )
            )
        activePrimitiveWindow = updated
        return updated
    }

    private fun clearPrimitiveQueueState() {
        primitiveNavigationJob?.cancel()
        primitiveNavigationJob = null
        primitivePrefetchJob?.cancel()
        primitivePrefetchJob = null
        pendingPrimitiveTarget = null
        activePrimitiveWindow = null
    }

    private fun launchPrimitiveMutation(
        operation: String,
        preservePosition: Boolean = true,
        mutate:
            suspend (
                org.oxycblt.auxio.playback.persist.QueueDescriptor
            ) -> org.oxycblt.auxio.playback.persist.QueueDescriptor?,
    ) {
        val initial = activePrimitiveWindow ?: return
        val wasPlaying = player.playWhenReady || player.isPlaying
        val positionMs = if (preservePosition) player.currentPosition.coerceAtLeast(0L) else 0L
        restoreScope.launch {
            primitiveMutationMutex.withLock {
                val active =
                    withContext(Dispatchers.Main) { activePrimitiveWindow } ?: return@withLock
                if (active.descriptor.sessionId != initial.descriptor.sessionId) return@withLock
                val descriptor = mutate(active.descriptor)
                if (descriptor == null) {
                    if (operation == "remove" && active.descriptor.totalCount == 1) {
                        withContext(Dispatchers.Main) {
                            clearPrimitiveQueueState()
                            player.clearMediaItems()
                            sessionOngoing = false
                            playbackManager.ack(this@ExoPlaybackStateHolder, StateAck.SessionEnded)
                        }
                    } else {
                        L.w("Primitive queue $operation did not commit")
                    }
                    return@withLock
                }
                val refreshed =
                    persistenceRepository
                        .readQueueWindowAround(descriptor)
                        ?.contiguousPlayableWindow()
                if (refreshed == null) {
                    L.w("Primitive queue $operation committed but current window is unresolved")
                    return@withLock
                }
                withContext(Dispatchers.Main) {
                    val current = activePrimitiveWindow ?: return@withContext
                    if (current.descriptor.sessionId != descriptor.sessionId) return@withContext
                    attachPrimitiveWindow(
                        window = refreshed,
                        targetLogicalPosition = descriptor.currentLogicalPosition,
                        positionMs = positionMs,
                        play = wasPlaying,
                    )
                }
            }
        }
    }

    private fun Song.toPrimitiveQueueItem() =
        QueueItemRef(
            logicalPosition = 0,
            canonicalPosition = 0,
            stableSongUid = uid,
            uri = uri.toString(),
            pathFallback = path.toString(),
            titleFallback = name.raw,
            artistFallback = artists.resolveNames(context),
            albumFallback = album.name.resolve(context),
            durationMs = durationMs,
        )

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

    private fun completeRestore(generation: Long, outcome: RestoreOutcome) {
        if (generation != restoreGeneration) return
        currentRestoreJob = null
        playbackManager.notifyRestoreOutcome(outcome)
    }

    private fun cancelActiveRestore(reason: String, notify: Boolean = true) {
        val job = currentRestoreJob
        val outcome = playbackManager.restoreOutcome
        val jobActive = job?.isActive == true
        val transientOutcome =
            outcome == RestoreOutcome.WAITING_FOR_PLAYER ||
                outcome == RestoreOutcome.WAITING_FOR_LIBRARY ||
                outcome == RestoreOutcome.RAW_FAST_RESUME_ACTIVE
        if (!jobActive && !transientOutcome) return

        L.i("Cancelling pending playback restore [reason=$reason outcome=$outcome]")
        restoreGeneration += 1
        currentRestoreJob = null
        pendingLibraryRestoreAfterRawFailure = null
        job?.cancel()
        if (notify && (jobActive || transientOutcome)) {
            playbackManager.notifyRestoreOutcome(RestoreOutcome.CANCELLED)
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
        cancelActiveRestore("new-playback")
        rawFastResumeItem = null
        clearPrimitiveQueueState()
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
        cancelActiveRestore("queue-reordered")
        activePrimitiveWindow?.let { window ->
            launchPrimitiveMutation("reorder") { descriptor ->
                val all = persistenceRepository.readAllQueueItems(descriptor)
                if (all.size != descriptor.totalCount) return@launchPrimitiveMutation null
                val currentCanonical =
                    all.firstOrNull { it.logicalPosition == descriptor.currentLogicalPosition }
                        ?.canonicalPosition ?: return@launchPrimitiveMutation null
                val canonicalOrder =
                    if (shuffled) {
                        val others =
                            all.map { it.canonicalPosition }
                                .filter { it != currentCanonical }
                                .shuffled()
                        others.toMutableList().also {
                            it.add(
                                descriptor.currentLogicalPosition.coerceIn(0, it.size),
                                currentCanonical,
                            )
                        }
                    } else {
                        all.sortedBy { it.canonicalPosition }.map { it.canonicalPosition }
                    }
                persistenceRepository.reorderQueue(
                    descriptor,
                    canonicalOrder,
                    if (shuffled) org.oxycblt.auxio.playback.state.ShuffleScope.ALL
                    else org.oxycblt.auxio.playback.state.ShuffleScope.OFF,
                )
            }
            return
        }
        player.setShuffleModeEnabled(shuffled)
        if (player.shuffleModeEnabled) {
            player.setShuffleOrder(
                BetterShuffleOrder(player.mediaItemCount, player.currentMediaItemIndex)
            )
        }
        playbackManager.ack(this, StateAck.QueueReordered)
        deferSave()
    }

    override fun next() {
        cancelActiveRestore("next")
        activePrimitiveWindow?.let { window ->
            val current = pendingPrimitiveTarget ?: window.descriptor.currentLogicalPosition
            val target =
                if (current + 1 < window.descriptor.totalCount) {
                    current + 1
                } else {
                    0
                }
            val shouldContinue =
                current + 1 < window.descriptor.totalCount ||
                    player.repeatMode == Player.REPEAT_MODE_ALL
            navigatePrimitive(target, shouldContinue && !playbackSettings.rememberPause)
            return
        }
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
        cancelActiveRestore("previous")
        activePrimitiveWindow?.let { window ->
            if (playbackSettings.rewindWithPrev && player.currentPosition > 3000L) {
                player.seekTo(0L)
                playbackManager.ack(this, StateAck.ProgressionChanged)
                deferSave()
                return
            }
            val current = pendingPrimitiveTarget ?: window.descriptor.currentLogicalPosition
            val target = (current - 1).coerceAtLeast(0)
            navigatePrimitive(target, !playbackSettings.rememberPause)
            return
        }
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
        cancelActiveRestore("queue-index")
        activePrimitiveWindow?.let { window ->
            if (index !in 0 until window.descriptor.totalCount) {
                L.w("Ignoring primitive goto with out-of-bounds index $index")
                return
            }
            navigatePrimitive(index, !playbackSettings.rememberPause)
            return
        }
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
        cancelActiveRestore("play-next")
        activePrimitiveWindow?.let { window ->
            val insertion = window.descriptor.currentLogicalPosition + 1
            val refs = songs.map { it.toPrimitiveQueueItem() }
            launchPrimitiveMutation("play-next") { descriptor ->
                persistenceRepository.insertQueueItems(descriptor, insertion, refs)
            }
            return
        }
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
        cancelActiveRestore("add-to-queue")
        activePrimitiveWindow?.let { window ->
            val refs = songs.map { it.toPrimitiveQueueItem() }
            launchPrimitiveMutation("append") { descriptor ->
                persistenceRepository.insertQueueItems(descriptor, descriptor.totalCount, refs)
            }
            return
        }
        player.addMediaItems(songs.map { it.buildMediaItem() })
        playbackManager.ack(this, ack)
        deferSave()
    }

    override fun move(from: Int, to: Int, ack: StateAck.Move) {
        cancelActiveRestore("move-queue-item")
        activePrimitiveWindow?.let {
            launchPrimitiveMutation("move") { descriptor ->
                persistenceRepository.moveQueueItem(descriptor, from, to)
            }
            return
        }
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
        cancelActiveRestore("remove-queue-item")
        activePrimitiveWindow?.let { window ->
            val removingCurrent = at == window.descriptor.currentLogicalPosition
            launchPrimitiveMutation("remove", preservePosition = !removingCurrent) { descriptor ->
                persistenceRepository.removeQueueItem(descriptor, at)
            }
            return
        }
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
        clearPrimitiveQueueState()
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
        cancelActiveRestore("reset")
        rawFastResumeItem = null
        clearPrimitiveQueueState()
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

    override fun onAudioSessionIdChanged(audioSessionId: Int) {
        super.onAudioSessionIdChanged(audioSessionId)
        L.d("Audio session ID changed to $audioSessionId")
        playbackManager.ack(this, StateAck.AudioSessionIdChanged(audioSessionId))
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        super.onMediaItemTransition(mediaItem, reason)

        if (activePrimitiveWindow != null) {
            synchronizePrimitivePositionFromPlayer()
            playbackManager.ack(this, StateAck.QueueWindowChanged)
            deferSave()
            maybePrefetchPrimitiveWindow()
        } else if (reason == Player.MEDIA_ITEM_TRANSITION_REASON_AUTO) {
            playbackManager.ack(this, StateAck.IndexMoved)
            deferSave()
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
                hasCurrentSong =
                    playbackManager.currentSong != null ||
                        activePrimitiveWindow?.currentItem != null ||
                        rawFastResumeItem != null,
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
            activePrimitiveWindow?.let {
                L.d("Library obtained while primitive queue is active; enriching loaded range")
                reconcilePrimitiveWindow(library)
                return
            }
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
            L.d("Library obtained, requesting action")
            playbackManager.requestAction(this)
        }
    }

    private fun reconcilePrimitiveWindow(library: Library) {
        val initial = activePrimitiveWindow ?: return
        restoreScope.launch {
            val requested =
                persistenceRepository.readQueueWindowAround(
                    initial.descriptor,
                    initial.descriptor.currentLogicalPosition,
                ) ?: return@launch
            requested.items.forEach { item ->
                val uid = item.stableSongUid ?: return@forEach
                val song = library.findSong(uid) ?: return@forEach
                persistenceRepository.enrichQueueItem(
                    requested.descriptor,
                    item.logicalPosition,
                    FastResumeSnapshot(
                        uri = song.uri.toString(),
                        path = song.path.toString(),
                        title = song.name.raw,
                        artist = song.artists.resolveNames(context),
                        album = song.album.name.resolve(context),
                        durationMs = song.durationMs,
                        positionMs = 0L,
                        playing = false,
                        savedAtMs = System.currentTimeMillis(),
                    ),
                )
            }
            val enriched =
                persistenceRepository
                    .readQueueWindowAround(
                        requested.descriptor,
                        requested.descriptor.currentLogicalPosition,
                    )
                    ?.contiguousPlayableWindow() ?: return@launch
            withContext(Dispatchers.Main) {
                val active = activePrimitiveWindow ?: return@withContext
                if (
                    active.descriptor.sessionId != enriched.descriptor.sessionId ||
                        active.descriptor.revision != enriched.descriptor.revision
                ) {
                    return@withContext
                }
                val updated =
                    enriched.copy(
                        descriptor =
                            enriched.descriptor.copy(
                                currentLogicalPosition = active.descriptor.currentLogicalPosition,
                                positionMs = player.currentPosition.coerceAtLeast(0L),
                                updatedAtMs = System.currentTimeMillis(),
                            )
                    )
                if (!expandPrimitiveWindowInPlace(updated)) {
                    L.w("Unable to expand primitive window in place during metadata reconciliation")
                    return@withContext
                }
                playbackManager.ack(this@ExoPlaybackStateHolder, StateAck.ProgressionChanged)
                maybePrefetchPrimitiveWindow()
            }
        }
    }

    // --- PLAYBACKSETTINGS OVERRIDES ---
    private fun tryStartRawFastResume(action: DeferredPlayback.RestoreState, generation: Long) {
        pendingLibraryRestoreAfterRawFailure = action
        restoreScope.launch {
            Ts18FirstAudioLatency.mark("snapshot_read_start")
            val snapshot = persistenceRepository.readFastResumeSnapshot()
            Ts18FirstAudioLatency.mark("snapshot_read_end")
            if (snapshot == null) {
                L.d("No TS18 fast-resume snapshot available")
                withContext(Dispatchers.Main) {
                    if (generation == restoreGeneration && musicRepository.library != null) {
                        pendingLibraryRestoreAfterRawFailure = null
                        if (action.fallback != null) {
                            completeRestore(generation, RestoreOutcome.FALLBACK_QUEUE_CREATED)
                            playbackManager.playDeferred(action.fallback)
                        } else {
                            completeRestore(generation, RestoreOutcome.NO_SAVED_SESSION)
                        }
                    }
                }
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
                        playbackManager.notifyRestoreOutcome(RestoreOutcome.RAW_FAST_RESUME_ACTIVE)
                    }
                    is RawFastResumeValidator.Result.Invalid -> {
                        L.w(
                            "Ignoring invalid TS18 fast-resume snapshot: " +
                                validation.reason +
                                " " +
                                validation.detail
                        )
                        if (generation == restoreGeneration && musicRepository.library != null) {
                            pendingLibraryRestoreAfterRawFailure = null
                            if (action.fallback != null) {
                                completeRestore(generation, RestoreOutcome.FALLBACK_QUEUE_CREATED)
                                playbackManager.playDeferred(action.fallback)
                            } else {
                                completeRestore(generation, RestoreOutcome.NO_SAVED_SESSION)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun startRawFastResume(item: RawFastResumeItem, play: Boolean) {
        Ts18FirstAudioLatency.mark("raw_media_item_set")
        clearPrimitiveQueueState()
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
                playbackManager.notifyRestoreOutcome(RestoreOutcome.RESTORED_EXISTING_SESSION)
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
            val primitive =
                withContext(Dispatchers.Main) { synchronizePrimitivePositionFromPlayer() }
            val primitiveItem = primitive?.currentItem
            val snapshot =
                when {
                    raw != null ->
                        raw.toSnapshot(
                            positionMs = progression.calculateElapsedPositionMs().coerceAtLeast(0L),
                            playing = progression.isPlaying,
                        )
                    primitiveItem != null && primitiveItem.hasPlayableReference ->
                        FastResumeSnapshot(
                            uri =
                                primitiveItem.uri?.takeIf { it.isNotBlank() }
                                    ?: primitiveItem.pathFallback
                                        ?.let { Uri.fromFile(java.io.File(it)).toString() }
                                        .orEmpty(),
                            path = primitiveItem.pathFallback,
                            title = primitiveItem.titleFallback,
                            artist = primitiveItem.artistFallback,
                            album = primitiveItem.albumFallback,
                            durationMs = primitiveItem.durationMs,
                            positionMs = progression.calculateElapsedPositionMs().coerceAtLeast(0L),
                            playing = progression.isPlaying,
                            savedAtMs = System.currentTimeMillis(),
                        )
                    else -> null
                }
            if (!persistenceRepository.saveFastResumeSnapshot(snapshot)) {
                L.w("Unable to persist pre-library fast-resume snapshot")
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

    private suspend fun savePlaybackState() {
        val primitiveState =
            withContext(Dispatchers.Main) {
                synchronizePrimitivePositionFromPlayer()?.let { it to repeatMode }
            }
        if (primitiveState != null) {
            val (primitive, primitiveRepeatMode) = primitiveState
            persistenceRepository.updateQueuePosition(
                descriptor = primitive.descriptor,
                logicalPosition = primitive.descriptor.currentLogicalPosition,
                positionMs = primitive.descriptor.positionMs,
                repeatMode = primitiveRepeatMode,
            )
        } else {
            persistenceRepository.saveState(playbackManager.toSavedState())
        }
    }

    private fun save(cb: () -> Unit) {
        saveJob {
            if (sessionOngoing) {
                saveFastResumeSnapshot()
                savePlaybackState()
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
                savePlaybackState()
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
        private val startupReadinessController: StartupReadinessController,
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
                startupReadinessController,
            )
        }
    }

    private companion object {
        const val SAVE_BUFFER = 5000L
    }
}
