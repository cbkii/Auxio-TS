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
import org.oxycblt.auxio.music.ConfiguredSourcePolicy
import org.oxycblt.auxio.music.MusicRepository
import org.oxycblt.auxio.music.StartupOptionalWorkGate
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
    private val configuredSourcePolicy: ConfiguredSourcePolicy,
    private val optionalWorkGate: StartupOptionalWorkGate,
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
    @Volatile private var currentRestoreJob: Job? = null
    @Volatile private var restoreGeneration = 0L
    private val restoreIntentArbiter = RestoreIntentArbiter()
    private var openAudioEffectSession = false
    private val audioManager = context.getSystemService(AudioManager::class.java)
    private val mainHandler = Handler(Looper.getMainLooper())
    private var audioFocusState = AudioFocusPolicy.State()
    private var hasAudioFocus = false
    private var pauseFromAudioFocus = false
    private var rawFastResumeItem: RawFastResumeItem? = null
    private var activePrimitiveWindow: QueueWindow? = null
    private var canonicalCurrentSourceLease: CanonicalCurrentSourceLease? = null
    private val primitiveHandoffGate = PrimitiveQueueHandoffGate()
    private var primitivePromotionPreparationJob: Job? = null
    private var primitivePromotionPreparationKey: PrimitiveQueueHandoffGate.Key? = null
    private var preparedPrimitivePromotion: PreparedPrimitivePromotion? = null
    private val pendingPrimitivePromotionActions = mutableListOf<() -> Unit>()
    private var primitiveNavigationJob: Job? = null
    private var primitivePrefetchJob: Job? = null
    private val primitiveMutationMutex = Mutex()
    private var pendingPrimitiveTarget: Int? = null
    private var pendingLibraryRestoreAfterRawFailure: DeferredPlayback.RestoreState? = null
    private var markedFirstPlaying = false

    private data class CanonicalCurrentSourceLease(val song: Song)

    private data class PreparedPrimitivePromotion(
        val key: PrimitiveQueueHandoffGate.Key,
        val songs: List<Song>,
        val currentHeapIndex: Int,
        val shuffledMapping: List<Int>,
        val parent: MusicParent?,
        val droppedCount: Int,
    )

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
        canonicalCurrentSourceLease
            ?.song
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
        musicRepository.library ?: return RawQueue(emptyList(), emptyList(), 0)
        val heap = (0 until player.mediaItemCount).map { player.getMediaItemAt(it) }
        val currentPlayerIndex = player.currentMediaItemIndex
        val leasedSong = canonicalCurrentSourceLease?.song
        val songs =
            heap.mapIndexed { index, item ->
                item.song ?: if (index == currentPlayerIndex) leasedSong else null
            }
        if (songs.any { it == null }) {
            L.w("Canonical queue contains unresolved player items after handoff")
            return RawQueue.nil()
        }
        val shuffledMapping =
            if (player.shuffleModeEnabled) {
                player.unscrambleQueueIndices()
            } else {
                emptyList()
            }
        return RawQueue(songs.filterNotNull(), shuffledMapping, currentPlayerIndex)
    }

    override fun handleDeferred(action: DeferredPlayback): Boolean {
        if (action is DeferredPlayback.RestoreState) {
            Ts18FirstAudioLatency.mark("restore_requested")
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
            is DeferredPlayback.RestoreState -> return false
            is DeferredPlayback.ShuffleAll -> {
                L.d("Shuffling all tracks")
                playbackManager.play(
                    requireNotNull(commandFactory.all(ShuffleMode.ON)) {
                        "Invalid playback parameters"
                    },
                    shouldPlayImmediately(action.play),
                )
            }
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
                    val validation =
                        RawFastResumeValidator.validate(context, snapshot, configuredSourcePolicy)
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
        val newlyStarted = restoreIntentArbiter.begin(action)
        if (!newlyStarted && currentRestoreJob?.isActive == true) {
            Ts18FirstAudioLatency.mark("restore_coalesced")
            L.d("Coalescing saved-state restore into the active restore generation")
            return
        }
        currentRestoreJob?.cancel()
        optionalWorkGate.onRestoreStarted()
        playbackManager.notifyRestoreOutcome(RestoreOutcome.WAITING_FOR_PLAYER)
        val generation = ++restoreGeneration
        currentRestoreJob =
            restoreScope.launch {
                try {
                    Ts18FirstAudioLatency.mark("queue_descriptor_read_start")
                    val descriptor = persistenceRepository.readQueueDescriptor()
                    Ts18FirstAudioLatency.mark("queue_descriptor_read_end")
                    if (descriptor == null) {
                        if (!publishQueueReadyIfCurrent(generation)) return@launch
                        startRawFallback(generation)
                        return@launch
                    }

                    var attempts = 0
                    while (generation == restoreGeneration) {
                        val intent = restoreIntentArbiter.snapshot()
                        val target =
                            (descriptor.currentLogicalPosition.toLong() + intent.skipDelta)
                                .coerceIn(0L, (descriptor.totalCount - 1).coerceAtLeast(0).toLong())
                                .toInt()
                        val requested = descriptor.copy(currentLogicalPosition = target)
                        Ts18FirstAudioLatency.mark("queue_window_read_start")
                        var window = persistenceRepository.readQueueWindowAround(requested, target)
                        Ts18FirstAudioLatency.mark("queue_window_read_end")
                        var current = window?.currentItem
                        if (current?.hasPlayableReference != true) {
                            val snapshot = persistenceRepository.readFastResumeSnapshot()
                            if (snapshot != null) {
                                persistenceRepository.enrichQueueItem(requested, target, snapshot)
                                window =
                                    persistenceRepository.readQueueWindowAround(requested, target)
                                current = window?.currentItem
                            }
                        }
                        val playableWindow = window?.contiguousPlayableWindow()
                        if (playableWindow == null || current?.hasPlayableReference != true) {
                            if (!publishQueueReadyIfCurrent(generation)) return@launch
                            startRawFallback(generation)
                            return@launch
                        }

                        val attached =
                            withContext(Dispatchers.Main) {
                                if (generation != restoreGeneration) return@withContext true
                                val latest = restoreIntentArbiter.snapshot()
                                if (latest.version != intent.version && attempts < 3) {
                                    return@withContext false
                                }
                                val latestTarget =
                                    (descriptor.currentLogicalPosition.toLong() + latest.skipDelta)
                                        .coerceIn(
                                            0L,
                                            (descriptor.totalCount - 1).coerceAtLeast(0).toLong(),
                                        )
                                        .toInt()
                                if (
                                    latestTarget != target &&
                                        playableWindow.globalToLocal(latestTarget) == null
                                ) {
                                    return@withContext false
                                }
                                val finalIntent = restoreIntentArbiter.finish()
                                val position =
                                    finalIntent.seekPositionMs
                                        ?: if (finalIntent.skipDelta != 0) 0L
                                        else descriptor.positionMs
                                attachPrimitiveWindow(
                                    window = playableWindow,
                                    targetLogicalPosition = latestTarget,
                                    positionMs = position,
                                    play = shouldPlayImmediately(finalIntent.play),
                                )
                                Ts18FirstAudioLatency.mark("primitive_window_attached")
                                if (!publishQueueReadyIfCurrent(generation)) return@withContext true
                                completeRestore(
                                    generation,
                                    RestoreOutcome.RESTORED_EXISTING_SESSION,
                                )
                                true
                            }
                        if (attached) return@launch
                        attempts += 1
                    }
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    L.w(e, "Unable to restore primitive playback queue")
                    if (publishQueueReadyIfCurrent(generation)) {
                        startRawFallback(generation)
                    }
                } finally {
                    if (generation == restoreGeneration) currentRestoreJob = null
                }
            }
    }

    private fun publishQueueReadyIfCurrent(generation: Long): Boolean {
        if (generation != restoreGeneration) return false
        startupReadinessController.publishCapability(StartupReadinessState.QueueReady)
        return true
    }

    private suspend fun startRawFallback(generation: Long) {
        if (generation != restoreGeneration) return
        playbackManager.notifyRestoreOutcome(RestoreOutcome.WAITING_FOR_LIBRARY)
        tryStartRawFastResume(generation)
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
        canonicalCurrentSourceLease = null
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
        Ts18FirstAudioLatency.mark("player_prepare")
        player.prepare()
        sessionOngoing = true
        if (play) playing(true) else player.playWhenReady = false
        playbackManager.ack(this, StateAck.QueueWindowChanged)
        playbackManager.ack(this, StateAck.ProgressionChanged)
        deferSave()
        maybePrefetchPrimitiveWindow()
        musicRepository.library?.takeIf { !it.empty() }?.let { library ->
            primitiveHandoffGate.onLibraryChanged(activePrimitiveWindow!!.promotionKey())
            preparePrimitivePromotion(library, force = true)
        }
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
        val currentPlayerIndex = player.currentMediaItemIndex
        mediaItems.forEachIndexed { index, mediaItem ->
            if (index < player.mediaItemCount && index != currentPlayerIndex) {
                player.replaceMediaItem(index, mediaItem)
            }
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

    private fun clearPrimitiveQueueState(clearPromotion: Boolean = true) {
        primitiveNavigationJob?.cancel()
        primitiveNavigationJob = null
        primitivePrefetchJob?.cancel()
        primitivePrefetchJob = null
        pendingPrimitiveTarget = null
        activePrimitiveWindow = null
        if (clearPromotion) {
            canonicalCurrentSourceLease = null
            clearPrimitivePromotionState("primitive-queue-cleared")
        }
    }

    private fun clearPrimitivePromotionState(reason: String) {
        if (
            preparedPrimitivePromotion != null ||
                primitivePromotionPreparationJob?.isActive == true ||
                pendingPrimitivePromotionActions.isNotEmpty()
        ) {
            L.d("Clearing Fast Resume canonical handoff state [reason=$reason]")
        }
        primitivePromotionPreparationJob?.cancel()
        primitivePromotionPreparationJob = null
        primitivePromotionPreparationKey = null
        preparedPrimitivePromotion = null
        pendingPrimitivePromotionActions.clear()
        primitiveHandoffGate.clear()
    }

    private fun QueueWindow.promotionKey() =
        PrimitiveQueueHandoffGate.Key(descriptor.sessionId, descriptor.revision)

    private fun preparePrimitivePromotion(library: Library, force: Boolean = false) {
        val active = activePrimitiveWindow ?: return
        val descriptor = active.descriptor
        val key = active.promotionKey()
        if (!force && preparedPrimitivePromotion?.key == key) return
        if (
            !force &&
                primitivePromotionPreparationJob?.isActive == true &&
                primitivePromotionPreparationKey == key
        ) {
            return
        }

        primitivePromotionPreparationJob?.cancel()
        primitivePromotionPreparationKey = key
        preparedPrimitivePromotion = null
        Ts18FirstAudioLatency.mark("canonical_prepare_start")
        primitivePromotionPreparationJob =
            restoreScope.launch {
                val allItems = persistenceRepository.readAllQueueItems(descriptor)
                val layout = PrimitiveQueuePromotionPolicy.layout(descriptor, allItems)
                val canonicalItems = layout?.itemsByCanonicalPosition.orEmpty()
                val songsByUid =
                    canonicalItems.map { item -> item.stableSongUid?.let(library::findSong) }
                val songsByCanonicalPosition: List<Song?> =
                    if (songsByUid.all { it != null }) {
                        songsByUid
                    } else {
                        val songsByUri =
                            PrimitiveQueuePromotionIdentityIndex.uniqueBy(library.songs) {
                                it.uri.toString()
                            }
                        val songsByPath =
                            PrimitiveQueuePromotionIdentityIndex.uniqueBy(library.songs) {
                                it.path.toString()
                            }
                        canonicalItems.mapIndexed { index, item ->
                            songsByUid[index]
                                ?: item.uri?.let(songsByUri::get)
                                ?: item.pathFallback?.let(songsByPath::get)
                        }
                    }
                val resolvedHeapIndices =
                    songsByCanonicalPosition.mapIndexedNotNull { index, song ->
                        index.takeIf { song != null }
                    }.toSet()
                val hydrated =
                    layout?.let {
                        PrimitiveQueuePromotionPolicy.hydratedLayout(
                            it,
                            descriptor.currentLogicalPosition,
                            resolvedHeapIndices,
                        )
                    }
                val hydratedSongs =
                    hydrated?.keptHeapIndices?.mapNotNull { songsByCanonicalPosition[it] }.orEmpty()
                val restoredParent =
                    if (hydrated != null && hydrated.droppedCount == 0) {
                        resolvePreparedParent(hydratedSongs, hydrated.shuffledMapping)
                    } else {
                        null
                    }

                withContext(Dispatchers.Main) {
                    if (primitivePromotionPreparationKey != key) return@withContext
                    primitivePromotionPreparationJob = null
                    primitivePromotionPreparationKey = null
                    val current = activePrimitiveWindow ?: return@withContext
                    val currentKey = current.promotionKey()
                    if (currentKey != key) {
                        L.d(
                            "Discarding stale Fast Resume canonical preparation " +
                                "[prepared=$key current=$currentKey]"
                        )
                        preparePrimitivePromotion(library, force = true)
                        return@withContext
                    }

                    if (
                        layout == null ||
                            hydrated == null ||
                            hydratedSongs.size != hydrated.keptHeapIndices.size
                    ) {
                        val unresolved = songsByCanonicalPosition.count { it == null }
                        preparedPrimitivePromotion = null
                        primitiveHandoffGate.onFailed(key)
                        L.w(
                            "Unable to hydrate current Fast Resume item; keeping primitive " +
                                "authority [session=${key.sessionId} revision=${key.revision} " +
                                "items=${allItems.size}/${descriptor.totalCount} unresolved=$unresolved]"
                        )
                        Ts18FirstAudioLatency.mark("canonical_fail_open_current_unresolved")
                        drainPendingPrimitivePromotionActions()
                        return@withContext
                    }

                    preparedPrimitivePromotion =
                        PreparedPrimitivePromotion(
                            key = key,
                            songs = hydratedSongs,
                            currentHeapIndex = hydrated.currentHeapIndex,
                            shuffledMapping = hydrated.shuffledMapping,
                            parent = restoredParent,
                            droppedCount = hydrated.droppedCount,
                        )
                    primitiveHandoffGate.onPrepared(key)
                    Ts18FirstAudioLatency.mark("canonical_prepare_complete")
                    L.i(
                        "Fast Resume canonical queue prepared for automatic handoff " +
                            "[session=${key.sessionId} revision=${key.revision} " +
                            "count=${hydratedSongs.size} dropped=${hydrated.droppedCount}]"
                    )
                    if (!promotePreparedPrimitiveQueue("library-ready")) {
                        primitiveHandoffGate.onFailed(key)
                    }
                    drainPendingPrimitivePromotionActions()
                }
            }
    }

    private suspend fun resolvePreparedParent(
        songs: List<Song>,
        shuffledMapping: List<Int>,
    ): MusicParent? {
        val saved = persistenceRepository.readState() ?: return null
        if (saved.heap.size != songs.size) return null
        if (saved.heap.map { it?.uid } != songs.map { it.uid }) return null
        if (saved.shuffledMapping != shuffledMapping) return null
        return saved.parent
    }

    private fun deferPrimitiveQueueInteractionUntilPromotion(
        reason: String,
        replay: () -> Unit,
    ): Boolean {
        val active = activePrimitiveWindow ?: return false
        val library = musicRepository.library?.takeIf { !it.empty() } ?: return false
        val key = active.promotionKey()
        return when (primitiveHandoffGate.requestHandoff(key, libraryReady = true)) {
            PrimitiveQueueHandoffGate.Decision.BYPASS -> false
            PrimitiveQueueHandoffGate.Decision.PROMOTE -> {
                if (!promotePreparedPrimitiveQueue(reason)) {
                    primitiveHandoffGate.onFailed(key)
                }
                false
            }
            PrimitiveQueueHandoffGate.Decision.PREPARE -> {
                L.i(
                    "Deferring Fast Resume queue interaction until automatic canonical handoff " +
                        "[reason=$reason session=${key.sessionId} revision=${key.revision}]"
                )
                pendingPrimitivePromotionActions.add(replay)
                preparePrimitivePromotion(library)
                true
            }
        }
    }

    private fun promotePreparedPrimitiveQueue(reason: String): Boolean {
        val active = activePrimitiveWindow ?: return false
        val key = active.promotionKey()
        val prepared = preparedPrimitivePromotion ?: return false
        if (prepared.key != key || !primitiveHandoffGate.isPrepared(key)) return false
        val currentSong = prepared.songs.getOrNull(prepared.currentHeapIndex) ?: return false
        val currentPositionBefore = player.currentPosition.coerceAtLeast(0L)
        val playWhenReadyBefore = player.playWhenReady
        val isPlayingBefore = player.isPlaying
        val playbackStateBefore = player.playbackState
        val audioSessionBefore = player.audioSessionId
        val currentUriBefore = player.currentMediaItem?.localConfiguration?.uri

        Ts18FirstAudioLatency.mark("canonical_commit_start")
        if (
            !installCanonicalQueueAroundCurrentSource(
                songs = prepared.songs,
                currentHeapIndex = prepared.currentHeapIndex,
                shuffleModeEnabled = prepared.shuffledMapping.isNotEmpty(),
                shuffledMapping = prepared.shuffledMapping,
            )
        ) {
            return false
        }

        currentSaveJob?.cancel()
        currentSaveJob = null
        primitivePromotionPreparationJob?.cancel()
        primitivePromotionPreparationJob = null
        primitivePromotionPreparationKey = null
        clearPrimitiveQueueState(clearPromotion = false)
        preparedPrimitivePromotion = null
        primitiveHandoffGate.clear()
        rawFastResumeItem = null
        pendingLibraryRestoreAfterRawFailure = null
        parent = prepared.parent
        sessionOngoing = true
        playbackManager.notifyRestoreOutcome(RestoreOutcome.RESTORED_EXISTING_SESSION)
        playbackManager.ack(this, StateAck.NewPlayback)
        playbackManager.ack(this, StateAck.ProgressionChanged)
        Ts18FirstAudioLatency.mark("primitive_queue_promoted")
        Ts18FirstAudioLatency.mark("canonical_active")
        deferSave()
        L.i(
            "Promoted Fast Resume queue to canonical library authority without current-source " +
                "reset [reason=$reason count=${prepared.songs.size} dropped=${prepared.droppedCount} " +
                "heap=${prepared.currentHeapIndex} positionBefore=$currentPositionBefore " +
                "positionAfter=${player.currentPosition.coerceAtLeast(0L)} " +
                "playWhenReadyBefore=$playWhenReadyBefore playWhenReadyAfter=${player.playWhenReady} " +
                "isPlayingBefore=$isPlayingBefore isPlayingAfter=${player.isPlaying} " +
                "stateBefore=$playbackStateBefore stateAfter=${player.playbackState} " +
                "uriStable=${currentUriBefore == player.currentMediaItem?.localConfiguration?.uri} " +
                "audioSessionBefore=$audioSessionBefore audioSessionAfter=${player.audioSessionId} " +
                "currentUid=${currentSong.uid}]"
        )
        return true
    }

    private fun installCanonicalQueueAroundCurrentSource(
        songs: List<Song>,
        currentHeapIndex: Int,
        shuffleModeEnabled: Boolean,
        shuffledMapping: List<Int>,
    ): Boolean {
        val currentMediaItem = player.currentMediaItem ?: return false
        val originalItemCount = player.mediaItemCount
        val originalCurrentIndex = player.currentMediaItemIndex
        val plan =
            SeamlessQueueHandoffPolicy.plan(
                originalItemCount = originalItemCount,
                originalCurrentIndex = originalCurrentIndex,
                canonicalItemCount = songs.size,
                targetCurrentIndex = currentHeapIndex,
            ) ?: return false
        val sourceUri = currentMediaItem.localConfiguration?.uri
        canonicalCurrentSourceLease = CanonicalCurrentSourceLease(songs[currentHeapIndex])

        if (plan.originalCurrentIndex + 1 < plan.originalItemCount) {
            player.removeMediaItems(plan.originalCurrentIndex + 1, plan.originalItemCount)
        }
        if (plan.originalCurrentIndex > 0) {
            player.removeMediaItems(0, plan.originalCurrentIndex)
        }
        if (plan.prependCount > 0) {
            player.addMediaItems(0, songs.subList(0, currentHeapIndex).map { it.buildMediaItem() })
        }
        if (plan.appendCount > 0) {
            player.addMediaItems(songs.subList(currentHeapIndex + 1, songs.size).map { it.buildMediaItem() })
        }

        val currentUriAfter = player.currentMediaItem?.localConfiguration?.uri
        if (player.currentMediaItemIndex != currentHeapIndex || currentUriAfter != sourceUri) {
            L.e(
                "Canonical handoff did not preserve the current source " +
                    "[expectedIndex=$currentHeapIndex actualIndex=${player.currentMediaItemIndex} " +
                    "uriStable=${currentUriAfter == sourceUri}]"
            )
            // Playlist edits have already committed. Returning to primitive authority here would
            // desynchronise logical queue state from ExoPlayer; do not reset or re-seek live audio.
            if (player.currentMediaItem?.song != null) {
                canonicalCurrentSourceLease = null
            }
        }

        if (shuffleModeEnabled) {
            val order =
                if (shuffledMapping.isNotEmpty()) {
                    BetterShuffleOrder(shuffledMapping.toIntArray())
                } else {
                    BetterShuffleOrder(songs.size, currentHeapIndex)
                }
            player.setShuffleOrder(order)
            player.shuffleModeEnabled = true
        } else {
            player.shuffleModeEnabled = false
        }
        return true
    }

    private fun drainPendingPrimitivePromotionActions() {
        if (pendingPrimitivePromotionActions.isEmpty()) return
        val actions = pendingPrimitivePromotionActions.toList()
        pendingPrimitivePromotionActions.clear()
        actions.forEach { action -> action() }
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
        optionalWorkGate.onRestoreFinished()
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
        restoreIntentArbiter.cancel()
        job?.cancel()
        optionalWorkGate.onRestoreFinished()
        if (notify && (jobActive || transientOutcome)) {
            playbackManager.notifyRestoreOutcome(RestoreOutcome.CANCELLED)
        }
    }

    override fun playing(playing: Boolean) {
        if (restoreIntentArbiter.updatePlay(playing)) {
            Ts18FirstAudioLatency.mark("restore_play_state_coalesced")
            return
        }
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
        if (restoreIntentArbiter.updateSeek(positionMs)) {
            Ts18FirstAudioLatency.mark("restore_seek_coalesced")
            return
        }
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
        if (deferPrimitiveQueueInteractionUntilPromotion("shuffle") { shuffled(shuffled) }) return
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
        if (restoreIntentArbiter.addSkip(1)) {
            Ts18FirstAudioLatency.mark("restore_skip_coalesced")
            return
        }
        cancelActiveRestore("next")
        if (deferPrimitiveQueueInteractionUntilPromotion("next") { next() }) return
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
            if (!playbackSettings.rememberPause) {
                player.pause()
            }
        }
        playbackManager.ack(this, StateAck.IndexMoved)
        deferSave()
    }

    override fun prev() {
        if (restoreIntentArbiter.addSkip(-1)) {
            Ts18FirstAudioLatency.mark("restore_skip_coalesced")
            return
        }
        cancelActiveRestore("previous")
        if (deferPrimitiveQueueInteractionUntilPromotion("previous") { prev() }) return
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
        if (deferPrimitiveQueueInteractionUntilPromotion("goto") { goto(index) }) return
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
        player.seekTo(trueIndex, C.TIME_UNSET)
        if (!playbackSettings.rememberPause) {
            player.play()
        }
        playbackManager.ack(this, StateAck.IndexMoved)
        deferSave()
    }

    override fun playNext(songs: List<Song>, ack: StateAck.PlayNext) {
        cancelActiveRestore("play-next")
        if (deferPrimitiveQueueInteractionUntilPromotion("play-next") { playNext(songs, ack) }) {
            return
        }
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
        if (
            deferPrimitiveQueueInteractionUntilPromotion("add-to-queue") { addToQueue(songs, ack) }
        ) {
            return
        }
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
        if (deferPrimitiveQueueInteractionUntilPromotion("move") { move(from, to, ack) }) return
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
        if (deferPrimitiveQueueInteractionUntilPromotion("remove") { remove(at, ack) }) return
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
        if (shouldSeek || abs(player.currentPosition - positionMs) > 1000L) {
            player.seekTo(positionMs)
        }

        if (sendNewPlaybackEvent) {
            ack?.let { playbackManager.ack(this, it) }
        }
    }

    override fun endSession() {
        playbackManager.playing(false)
        abandonAudioFocus()
        save {
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

    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
        super.onPlayWhenReadyChanged(playWhenReady, reason)

        if (player.playWhenReady) {
            if (!requestAudioFocus()) {
                L.w("Cannot continue playback: audio focus request denied")
                player.pause()
                return
            }
            L.d("Player has started playing")
            sessionOngoing = true
            if (!openAudioEffectSession) {
                L.d("Opening audio effect session")
                broadcastAudioEffectAction(AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION)
                openAudioEffectSession = true
            }
        } else if (openAudioEffectSession) {
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
            musicRepository.library?.takeIf { !it.empty() }?.let { library ->
                mainHandler.post { preparePrimitivePromotion(library, force = true) }
            }
        } else {
            if (canonicalCurrentSourceLease != null && mediaItem?.song != null) {
                L.i("Canonical current-source lease completed on media transition")
                canonicalCurrentSourceLease = null
            }
            if (reason == Player.MEDIA_ITEM_TRANSITION_REASON_AUTO) {
                playbackManager.ack(this, StateAck.IndexMoved)
                deferSave()
            }
        }
    }

    override fun onEvents(player: Player, events: Player.Events) {
        super.onEvents(player, events)

        if (player.isPlaying && !markedFirstPlaying) {
            markedFirstPlaying = true
            Ts18FirstAudioLatency.mark("first_audio")
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

    override fun onMusicChanges(changes: MusicRepository.Changes) {
        val library = musicRepository.library?.takeIf { !it.empty() }
        if (changes.deviceLibrary && library != null) {
            activePrimitiveWindow?.let { window ->
                val key = window.promotionKey()
                primitiveHandoffGate.onLibraryChanged(key)
                L.d(
                    "Library obtained while primitive queue is active; starting automatic " +
                        "canonical handoff"
                )
                Ts18FirstAudioLatency.mark("library_ready")
                preparePrimitivePromotion(library, force = true)
                return
            }
            rawFastResumeItem?.let {
                L.d("Library obtained while raw fast-resume is active; reconciling")
                reconcileRawFastResume(library)
                return
            }
            pendingLibraryRestoreAfterRawFailure?.let {
                L.d("Library obtained after raw fast-resume miss; replaying saved-state restore")
                pendingLibraryRestoreAfterRawFailure = null
                playbackManager.playDeferred(restoreIntentArbiter.snapshot().toRestoreState())
                return
            }
            L.d("Library obtained, requesting action")
            playbackManager.requestAction(this)
        }
    }

    private suspend fun tryStartRawFastResume(generation: Long) {
        pendingLibraryRestoreAfterRawFailure = restoreIntentArbiter.snapshot().toRestoreState()
        Ts18FirstAudioLatency.mark("snapshot_read_start")
        val snapshot = persistenceRepository.readFastResumeSnapshot()
        Ts18FirstAudioLatency.mark("snapshot_read_end")
        if (snapshot == null) {
            L.d("No TS18 fast-resume snapshot available")
            optionalWorkGate.onNoSavedSession()
            withContext(Dispatchers.Main) {
                if (generation == restoreGeneration && musicRepository.library != null) {
                    pendingLibraryRestoreAfterRawFailure = null
                    val finalIntent = restoreIntentArbiter.finish()
                    if (finalIntent.fallback != null) {
                        completeRestore(generation, RestoreOutcome.FALLBACK_QUEUE_CREATED)
                        playbackManager.playDeferred(finalIntent.fallback)
                    } else {
                        completeRestore(generation, RestoreOutcome.NO_SAVED_SESSION)
                    }
                }
            }
            return
        }
        Ts18FirstAudioLatency.mark("raw_media_validation_start")
        val validation = RawFastResumeValidator.validate(context, snapshot, configuredSourcePolicy)
        Ts18FirstAudioLatency.mark("raw_media_validation_end")
        withContext(Dispatchers.Main) {
            when (validation) {
                is RawFastResumeValidator.Result.Valid -> {
                    if (generation != restoreGeneration) {
                        L.d(
                            "Skipping late TS18 raw fast-resume result; restore was already consumed"
                        )
                        return@withContext
                    }
                    pendingLibraryRestoreAfterRawFailure = null
                    val finalIntent = restoreIntentArbiter.finish()
                    val shouldPlay = shouldPlayImmediately(finalIntent.play)
                    startRawFastResume(validation.item, shouldPlay)
                    finalIntent.seekPositionMs?.let(player::seekTo)
                    optionalWorkGate.onRestoreFinished()
                    playbackManager.notifyRestoreOutcome(RestoreOutcome.RAW_FAST_RESUME_ACTIVE)
                }
                is RawFastResumeValidator.Result.Invalid -> {
                    optionalWorkGate.onNoSavedSession()
                    L.w(
                        "Ignoring invalid TS18 fast-resume snapshot: " +
                            validation.reason +
                            " " +
                            validation.detail
                    )
                    if (generation == restoreGeneration && musicRepository.library != null) {
                        pendingLibraryRestoreAfterRawFailure = null
                        val finalIntent = restoreIntentArbiter.finish()
                        if (finalIntent.fallback != null) {
                            completeRestore(generation, RestoreOutcome.FALLBACK_QUEUE_CREATED)
                            playbackManager.playDeferred(finalIntent.fallback)
                        } else {
                            completeRestore(generation, RestoreOutcome.NO_SAVED_SESSION)
                        }
                    }
                }
            }
        }
    }

    private fun RestoreIntentArbiter.Snapshot.toRestoreState() =
        DeferredPlayback.RestoreState(play = play, fallback = fallback)

    override fun cancelDeferredRestore() {
        cancelActiveRestore("external-cancel")
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
        Ts18FirstAudioLatency.mark("player_prepare")
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
                val currentHeapIndex = command.queue.indexOf(song)
                if (currentHeapIndex < 0) {
                    L.w("Raw Fast Resume reconciliation command omitted the current song")
                    return@withContext
                }
                val positionBefore = player.currentPosition.coerceAtLeast(0L)
                val audioSessionBefore = player.audioSessionId
                if (
                    !installCanonicalQueueAroundCurrentSource(
                        songs = command.queue,
                        currentHeapIndex = currentHeapIndex,
                        shuffleModeEnabled = command.shuffled,
                        shuffledMapping = emptyList(),
                    )
                ) {
                    L.w("Unable to perform seamless raw Fast Resume canonical handoff")
                    return@withContext
                }
                currentSaveJob?.cancel()
                currentSaveJob = null
                rawFastResumeItem = null
                pendingLibraryRestoreAfterRawFailure = null
                parent = command.parent
                playbackManager.notifyRestoreOutcome(RestoreOutcome.RESTORED_EXISTING_SESSION)
                playbackManager.ack(this@ExoPlaybackStateHolder, StateAck.NewPlayback)
                playbackManager.ack(this@ExoPlaybackStateHolder, StateAck.ProgressionChanged)
                deferSave()
                L.i(
                    "Reconciled raw Fast Resume without current-source reset " +
                        "[positionBefore=$positionBefore positionAfter=${player.currentPosition} " +
                        "audioSessionBefore=$audioSessionBefore audioSessionAfter=${player.audioSessionId}]"
                )
                Ts18FirstAudioLatency.mark("reconciliation_end_matched")
            }
        }
    }

    private fun findSongForRawFastResume(raw: RawFastResumeItem, library: Library): Song? {
        PrimitiveQueuePromotionIdentityIndex.uniqueBy(library.songs) { it.uri.toString() }[raw.uriString]
            ?.let {
                return it
            }
        val rawPath = raw.path?.takeIf { it.isNotBlank() }
        if (rawPath != null) {
            val appContext = context.applicationContext
            PrimitiveQueuePromotionIdentityIndex.uniqueBy(library.songs) { song ->
                try {
                    song.path.resolve(appContext)
                } catch (e: Exception) {
                    ""
                }
            }[rawPath]
                ?.let {
                    return it
                }
        }
        val rawTitle = raw.title?.trim()?.lowercase()?.takeIf { it.isNotEmpty() }
        if (rawTitle != null && raw.durationMs > 0L) {
            val appContext = context.applicationContext
            return library.songs
                .filter { song ->
                    val title =
                        try {
                            song.name.resolve(appContext).trim().lowercase()
                        } catch (e: Exception) {
                            ""
                        }
                    title == rawTitle && kotlin.math.abs(song.durationMs - raw.durationMs) <= 1000L
                }
                .singleOrNull()
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
        val queue = ArrayDeque<Int>()
        val currentMediaItemIndex = currentMediaItemIndex
        queue.add(currentMediaItemIndex)
        var firstMediaItemIndex = currentMediaItemIndex
        var lastMediaItemIndex = currentMediaItemIndex
        val shuffleModeEnabled = shuffleModeEnabled
        while ((firstMediaItemIndex != C.INDEX_UNSET || lastMediaItemIndex != C.INDEX_UNSET)) {
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
        private val configuredSourcePolicy: ConfiguredSourcePolicy,
        private val optionalWorkGate: StartupOptionalWorkGate,
    ) {
        fun create(): ExoPlaybackStateHolder {
            val audioRenderer = RenderersFactory { handler, _, audioListener, _, _ ->
                val platformRenderer =
                    MediaCodecAudioRenderer(
                        context,
                        MediaCodecSelector.DEFAULT,
                        handler,
                        audioListener,
                        DefaultAudioSink.Builder(context)
                            .setAudioProcessors(arrayOf(replayGainProcessor))
                            .build(),
                    )
                arrayOf<BaseRenderer>(
                    platformRenderer,
                    FfmpegAudioRenderer(handler, audioListener, replayGainProcessor),
                )
            }

            val exoPlayer =
                ExoPlayer.Builder(context, audioRenderer)
                    .setMediaSourceFactory(mediaSourceFactory)
                    .setWakeMode(C.WAKE_MODE_LOCAL)
                    .setAudioAttributes(
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
                configuredSourcePolicy,
                optionalWorkGate,
            )
        }
    }

    private companion object {
        const val SAVE_BUFFER = 5000L
    }
}
