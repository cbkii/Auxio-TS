/*
 * Copyright (c) 2021 Auxio Project
 * PlaybackPanelFragment.kt is part of Auxio.
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

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.media.audiofx.Visualizer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.core.view.updatePaddingRelative
import androidx.dynamicanimation.animation.SpringForce
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.abs
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.oxycblt.auxio.R
import org.oxycblt.auxio.databinding.FragmentPlaybackPanelBinding
import org.oxycblt.auxio.detail.DetailViewModel
import org.oxycblt.auxio.headunit.HeadUnitUiAdapter
import org.oxycblt.auxio.headunit.topway.TopwayEqualizerLauncher
import org.oxycblt.auxio.home.HomeFragmentDirections
import org.oxycblt.auxio.list.menu.Menu
import org.oxycblt.auxio.music.resolve
import org.oxycblt.auxio.music.resolveNames
import org.oxycblt.auxio.playback.queue.QueueViewModel
import org.oxycblt.auxio.playback.state.RepeatMode
import org.oxycblt.auxio.playback.state.ShuffleScope
import org.oxycblt.auxio.playback.ui.StyledSeekBar
import org.oxycblt.auxio.playback.ui.stepper.Direction
import org.oxycblt.auxio.playback.ui.stepper.StepperOverlay
import org.oxycblt.auxio.playback.ui.swiper.CarouselTransformer
import org.oxycblt.auxio.playback.ui.swiper.CoverPagerAdapter
import org.oxycblt.auxio.playback.ui.swiper.UserAwarePagerCallback
import org.oxycblt.auxio.playback.ui.visualizer.VisualizerState
import org.oxycblt.auxio.ui.UISettings
import org.oxycblt.auxio.ui.ViewBindingFragment
import org.oxycblt.auxio.util.collectImmediately
import org.oxycblt.auxio.util.dampen
import org.oxycblt.auxio.util.navigateSafe
import org.oxycblt.auxio.util.recycler
import org.oxycblt.auxio.util.showToast
import org.oxycblt.auxio.util.systemBarInsetsCompat
import org.oxycblt.musikr.MusicParent
import org.oxycblt.musikr.Song
import timber.log.Timber as L

/**
 * A [ViewBindingFragment] more information about the currently playing song, alongside all
 * available controls.
 *
 * @author Alexander Capehart (OxygenCobalt)
 *
 * TODO: Improve flickering situation on play button
 */
@AndroidEntryPoint
class PlaybackPanelFragment :
    ViewBindingFragment<FragmentPlaybackPanelBinding>(),
    Toolbar.OnMenuItemClickListener,
    StyledSeekBar.Listener,
    StepperOverlay.Listener,
    UISettings.Listener {
    private val coverPagerAdapter by lazy {
        CoverPagerAdapter(this, playbackModel, uiSettings, viewLifecycleOwner)
    }
    private val playbackModel: PlaybackViewModel by activityViewModels()
    private val detailModel: DetailViewModel by activityViewModels()
    @Inject lateinit var uiSettings: UISettings
    private val queueModel: QueueViewModel by viewModels()
    private var userAwarePagerCallback: UserAwarePagerCallback? = null
    private var visualizer: Visualizer? = null
    private var visualizerSessionId: Int? = null
    private var visualizerPermissionLauncher:
        androidx.activity.result.ActivityResultLauncher<String>? = null
    private var visualizerWatchdogJob: Job? = null
    private var visualizerGeneration = 0
    private var visualizerRetryCount = 0

    override fun onCreateBinding(inflater: LayoutInflater) =
        FragmentPlaybackPanelBinding.inflate(inflater)

    override fun onBindingCreated(
        binding: FragmentPlaybackPanelBinding,
        savedInstanceState: Bundle?,
    ) {
        super.onBindingCreated(binding, savedInstanceState)

        visualizerPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    visualizerRetryCount = 0
                    updateVisualizerState(forceRestart = true)
                } else {
                    playbackModel.updateVisualizerState(
                        VisualizerState.Failed("RECORD_AUDIO permission denied")
                    )
                }
            }
        uiSettings.registerListener(this)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                playbackModel.currentAudioSessionId.collect { updateVisualizerState() }
            }
        }

        // --- UI SETUP ---
        binding.root.setOnApplyWindowInsetsListener { view, insets ->
            val bars = insets.systemBarInsetsCompat
            view.updatePadding(bottom = bars.bottom)
            insets
        }

        binding.playbackToolbar.apply {
            setNavigationOnClickListener { playbackModel.openMain() }
            setOnMenuItemClickListener(this@PlaybackPanelFragment)
        }

        binding.playbackPager?.apply {
            adapter = coverPagerAdapter
            userAwarePagerCallback =
                UserAwarePagerCallback(this) {
                        coverPagerAdapter.setActivePosition(it)
                        // Posting the queue goto command prevents the seekbar pos from desyncing
                        // from the song's duration, which creates a visual flicker in the seekbar.
                        post { queueModel.goto(it) }
                    }
                    .also { it.attach() }
            setPageTransformer(CarouselTransformer())
            recycler()?.apply {
                // Make it possible to collapse the bottom sheet from the ViewPager's touch area.
                isNestedScrollingEnabled = false
                // Visual effect consistency
                // TODO: Custom overscroll?
                overScrollMode = View.OVER_SCROLL_NEVER
            }
            // Make it easier to collapse the bottom sheet
            dampen()
            offscreenPageLimit = 1
        }

        // Set up fast seek overlay
        binding.playbackSong.apply {
            isSelected = true
            setOnClickListener { navigateToCurrentSong() }
        }
        binding.playbackArtist.apply {
            isSelected = true
            setOnClickListener { navigateToCurrentArtist() }
        }
        binding.playbackAlbum?.apply {
            isSelected = true
            setOnClickListener { navigateToCurrentAlbum() }
        }

        binding.playbackSeekBar?.listener = this
        val spacingSmall = resources.getDimensionPixelSize(R.dimen.spacing_small)
        val spacingMedium = resources.getDimensionPixelSize(R.dimen.spacing_medium)
        // The TS18 Now Playing controls have one deterministic automotive size. The removed
        // preference was ineffective because this surface is always landscape on the target unit.
        val useLargeControls = true
        binding.playbackSeekBar?.setLargeTouchMode(useLargeControls)
        val playbackInfoVerticalPadding =
            when {
                !uiSettings.showHeadUnitAlbumArt -> spacingMedium
                uiSettings.largeHeadUnitControls -> spacingSmall
                else -> null
            }
        if (!uiSettings.showHeadUnitAlbumArt) {
            binding.playbackPager?.visibility = View.GONE
            binding.playbackSong.maxLines = 3
            binding.playbackArtist.maxLines = 2
            binding.playbackControlsWrapper?.updatePadding(top = spacingSmall)
        }
        playbackInfoVerticalPadding?.let {
            binding.playbackInfoContainer.updatePadding(top = it, bottom = it)
        }
        HeadUnitUiAdapter.applyUniformMediaControls(
            resources,
            useLargeControls,
            listOf(
                binding.playbackRepeat,
                binding.playbackSkipPrev,
                binding.playbackPlayPause,
                binding.playbackSkipNext,
                binding.playbackShuffle,
            ),
            primaryButton = binding.playbackPlayPause,
        )
        HeadUnitUiAdapter.applyLargePlaybackText(
            resources,
            useLargeControls,
            binding.playbackSong,
            binding.playbackArtist,
        )
        if (useLargeControls) {
            binding.playbackControlsWrapper?.updatePaddingRelative(start = 0, end = 0)
            HeadUnitUiAdapter.applyUniformMediaControls(
                resources,
                largeControls = true,
                buttons = listOfNotNull(binding.playbackMore),
                compact = true,
            )
            applyLargeToolbarTargets(binding.playbackToolbar)
        }
        applyDriverSideLayout(binding)

        // Set up actions
        binding.playbackRepeat.apply {
            contentDescription = getString(R.string.desc_change_repeat)
            setOnClickListener { playbackModel.toggleRepeatMode() }
        }
        binding.playbackSkipPrev.apply {
            contentDescription = getString(R.string.desc_playback_previous)
            setOnClickListener {
                playbackModel.prev()
                requireContext().showToast(R.string.msg_playback_previous)
            }
        }
        binding.playbackPlayPause.apply {
            contentDescription = getString(R.string.desc_play_pause)
            @SuppressLint("RestrictedApi")
            setCornerSpringForce(
                SpringForce().apply {
                    stiffness = 700f
                    dampingRatio = 0.9f
                }
            )
            setOnClickListener { playbackModel.togglePlaying() }
        }
        binding.playbackSkipNext.apply {
            contentDescription = getString(R.string.desc_playback_next)
            setOnClickListener {
                playbackModel.next()
                requireContext().showToast(R.string.msg_playback_next)
            }
        }
        binding.playbackShuffle.apply {
            contentDescription = getString(R.string.desc_shuffle)
            setOnClickListener { playbackModel.cycleShuffleScope() }
        }
        binding.playbackMore?.setOnClickListener {
            val song = playbackModel.song.value
            if (song == null) {
                L.w("playbackMore clicked but no current song")
                return@setOnClickListener
            }
            val parcel = Menu.ForSong(R.menu.playback_song, song, PlaySong.ByItself).parcel
            val innerNavController =
                requireParentFragment()
                    .requireView()
                    .findViewById<View>(R.id.explore_nav_host)
                    ?.findNavController()
            innerNavController?.navigateSafe(
                HomeFragmentDirections.actionGlobalOpenSongMenu(parcel)
            )
        }

        // --- VIEWMODEL SETUP --
        collectImmediately(playbackModel.song, ::updateSong)
        collectImmediately(playbackModel.parent, ::updateParent)
        collectImmediately(playbackModel.positionDs, ::updatePosition)
        collectImmediately(playbackModel.repeatMode, ::updateRepeat)
        collectImmediately(playbackModel.isPlaying, ::updatePlaying)
        collectImmediately(playbackModel.shuffleScope, ::updateShuffleScope)
        collectImmediately(playbackModel.pagerQueue, ::updatePager)
    }

    private fun applyLargeToolbarTargets(toolbar: ViewGroup) {
        val size = resources.getDimensionPixelSize(R.dimen.size_touchable_head_unit)
        toolbar.minimumHeight = size
        toolbar.post {
            for (i in 0 until toolbar.childCount) {
                val child = toolbar.getChildAt(i)
                if (child.contentDescription != null) {
                    child.minimumWidth = size
                    child.minimumHeight = size
                    child.updateLayoutParams {
                        width = maxOf(width, size)
                        height = maxOf(height, size)
                    }
                }
            }
        }
    }

    override fun onDestroyBinding(binding: FragmentPlaybackPanelBinding) {
        uiSettings.unregisterListener(this)
        visualizerPermissionLauncher = null
        releaseVisualizer()
        binding.playbackRepeat.clearPendingIcon()
        binding.playbackSong.isSelected = false
        binding.playbackArtist.isSelected = false
        binding.playbackAlbum?.isSelected = false
        binding.playbackToolbar.setOnMenuItemClickListener(null)
        userAwarePagerCallback?.release()
        binding.playbackPager?.adapter = null
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_open_equalizer) {
            L.d("Launching TS18/native equalizer with Android fallback")
            val candidates =
                TopwayEqualizerLauncher.resolveIntents(
                    requireContext(),
                    playbackModel.currentAudioSessionId.value,
                )
            var launched = false
            for (candidate in candidates) {
                try {
                    startActivity(candidate)
                    launched = true
                    break
                } catch (e: android.content.ActivityNotFoundException) {
                    L.w(e, "EQ/DSP candidate disappeared before launch: $candidate")
                } catch (e: SecurityException) {
                    L.w(e, "EQ/DSP candidate was denied: $candidate")
                } catch (e: RuntimeException) {
                    L.w(e, "EQ/DSP candidate failed at launch: $candidate")
                }
            }
            if (!launched) requireContext().showToast(R.string.err_no_equalizer_app)
            return true
        }

        return false
    }

    override fun onSeekConfirmed(positionDs: Long) {
        playbackModel.seekTo(positionDs)
    }

    private fun updateSong(song: Song?) {
        val binding = requireBinding()
        // Disable the more button when there is no current song to prevent dead taps.
        binding.playbackMore?.isEnabled = song != null

        if (song == null) {
            // Nothing to do.
            return
        }

        val context = requireContext()
        L.d("Updating song display: $song")
        binding.playbackSong.text = song.name.resolve(context)
        binding.playbackArtist.text = song.artists.resolveNames(context)
        binding.playbackAlbum?.text = song.album.name.resolve(context)
        binding.playbackSeekBar?.durationDs = song.durationMs.msToDs()
        updateVisualizerState()
    }

    private fun updateParent(parent: MusicParent?) {
        val binding = requireBinding()
        val context = requireContext()
        binding.playbackToolbar.subtitle =
            parent?.run { name.resolve(context) } ?: context.getString(R.string.lbl_all_songs)
    }

    private fun updatePosition(positionDs: Long) {
        requireBinding().playbackSeekBar?.positionDs = positionDs
    }

    private fun updateRepeat(repeatMode: RepeatMode) {
        val repeatButton = requireBinding().playbackRepeat
        repeatButton.isChecked = repeatMode != RepeatMode.NONE
        repeatButton.setIconResource(repeatMode.icon)
    }

    override fun onResume() {
        super.onResume()
        if (!shouldUseVisualizerForCurrentState()) {
            releaseVisualizer()
            return
        }
        val hasPermission =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) ==
                PackageManager.PERMISSION_GRANTED
        if (!hasPermission) {
            visualizerPermissionLauncher?.launch(Manifest.permission.RECORD_AUDIO)
        } else {
            updateVisualizerState()
        }
    }

    override fun onPause() {
        super.onPause()
        releaseVisualizer()
    }

    private fun releaseVisualizer(publishHidden: Boolean = true) {
        visualizerWatchdogJob?.cancel()
        visualizerWatchdogJob = null
        visualizerGeneration++
        val activeVisualizer = visualizer
        visualizer = null
        visualizerSessionId = null
        if (activeVisualizer != null) {
            try {
                if (activeVisualizer.enabled) {
                    activeVisualizer.enabled = false
                }
            } catch (e: RuntimeException) {
                L.d(e, "Visualizer disable during release failed")
            }
            try {
                activeVisualizer.setDataCaptureListener(null, 0, false, false)
            } catch (e: RuntimeException) {
                L.d(e, "Visualizer listener cleanup failed")
            }
            try {
                activeVisualizer.release()
            } catch (e: RuntimeException) {
                L.d(e, "Visualizer native release failed")
            }
        }
        if (publishHidden) playbackModel.updateVisualizerState(VisualizerState.Hidden)
    }

    private fun shouldUseVisualizerForCurrentState(): Boolean {
        val sessionId = playbackModel.currentAudioSessionId.value ?: return false
        if (sessionId == 0 || !playbackModel.isPlaying.value) return false

        return when (uiSettings.visualizerMode) {
            UISettings.VisualizerMode.OFF -> false
            UISettings.VisualizerMode.ALWAYS -> true
            UISettings.VisualizerMode.FALLBACK -> playbackModel.song.value?.cover == null
        }
    }

    private fun updateVisualizerState(forceRestart: Boolean = false) {
        if (!shouldUseVisualizerForCurrentState()) {
            visualizerRetryCount = 0
            releaseVisualizer()
            return
        }

        val sessionId =
            playbackModel.currentAudioSessionId.value?.takeIf { it > 0 }
                ?: run {
                    releaseVisualizer(publishHidden = false)
                    playbackModel.updateVisualizerState(
                        VisualizerState.Failed("Waiting for a non-zero audio session")
                    )
                    return
                }
        val hasPermission =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) ==
                PackageManager.PERMISSION_GRANTED
        if (!hasPermission) {
            releaseVisualizer(publishHidden = false)
            playbackModel.updateVisualizerState(
                VisualizerState.Failed("RECORD_AUDIO permission denied")
            )
            return
        }

        if (forceRestart || (visualizer != null && visualizerSessionId != sessionId)) {
            visualizerRetryCount = 0
            releaseVisualizer(publishHidden = false)
        }
        if (visualizer != null) return

        startVisualizer(sessionId)
    }

    private fun startVisualizer(sessionId: Int) {
        val generation = ++visualizerGeneration
        playbackModel.updateVisualizerState(VisualizerState.WaitingForFrames)
        try {
            val captureRange = Visualizer.getCaptureSizeRange()
            require(captureRange.size >= 2 && captureRange[0] > 0 && captureRange[1] >= captureRange[0]) {
                "Invalid Visualizer capture-size range"
            }

            val candidate = Visualizer(sessionId)
            val targetSize = 512.coerceIn(captureRange[0], captureRange[1])
            candidate.captureSize = targetSize
            val maxCaptureRate = Visualizer.getMaxCaptureRate()
            val targetRate = minOf(maxCaptureRate, 30_000).coerceAtLeast(1)
            val scalingMode =
                if (visualizerRetryCount == 0) Visualizer.SCALING_MODE_NORMALIZED
                else Visualizer.SCALING_MODE_AS_PLAYED
            try {
                candidate.scalingMode = scalingMode
            } catch (e: RuntimeException) {
                L.d(e, "Requested Visualizer scaling mode unavailable; continuing with default")
            }

            val listenerStatus =
                candidate.setDataCaptureListener(
                    object : Visualizer.OnDataCaptureListener {
                        private var fftCallbacks = 0
                        private var waveformCallbacks = 0
                        private var lastUsableFftAtMs = 0L

                        override fun onWaveFormDataCapture(
                            visualizer: Visualizer,
                            waveform: ByteArray,
                            samplingRate: Int,
                        ) {
                            if (generation != visualizerGeneration) return
                            waveformCallbacks++
                            if (!hasUsableWaveform(waveform)) return
                            val now = android.os.SystemClock.uptimeMillis()
                            if (now - lastUsableFftAtMs <= FFT_PREFERENCE_WINDOW_MS) return
                            playbackModel.updateVisualizerState(
                                VisualizerState.Live(
                                    waveform.copyOf(),
                                    samplingRate,
                                    now,
                                    VisualizerState.FrameSource.WAVEFORM,
                                )
                            )
                            if (waveformCallbacks == 1) {
                                L.i("Visualizer waveform fallback live for session $sessionId")
                            }
                        }

                        override fun onFftDataCapture(
                            visualizer: Visualizer,
                            fft: ByteArray,
                            samplingRate: Int,
                        ) {
                            if (generation != visualizerGeneration) return
                            fftCallbacks++
                            if (!hasUsableFft(fft)) return
                            val now = android.os.SystemClock.uptimeMillis()
                            lastUsableFftAtMs = now
                            playbackModel.updateVisualizerState(
                                VisualizerState.Live(
                                    fft.copyOf(),
                                    samplingRate,
                                    now,
                                    VisualizerState.FrameSource.FFT,
                                )
                            )
                            if (fftCallbacks == 1) {
                                L.i(
                                    "Visualizer FFT live for session $sessionId " +
                                        "samplingRate=${samplingRate}mHz"
                                )
                            }
                        }
                    },
                    targetRate,
                    true,
                    true,
                )

            if (listenerStatus != Visualizer.SUCCESS) {
                playbackModel.updateVisualizerState(
                    VisualizerState.Failed("Listener registration failed: $listenerStatus")
                )
                try {
                    candidate.release()
                } catch (e: RuntimeException) {
                    L.d(e, "Visualizer native release failed during partial initialization")
                }
                return
            }

            candidate.enabled = true
            visualizer = candidate
            visualizerSessionId = sessionId
            L.i(
                "Visualizer started session=$sessionId captureSize=$targetSize " +
                    "captureRate=${targetRate}mHz scalingMode=$scalingMode " +
                    "attempt=$visualizerRetryCount"
            )
            scheduleVisualizerWatchdog(sessionId, generation)
        } catch (e: RuntimeException) {
            val message =
                when (e) {
                    is SecurityException -> "Visualizer construction denied"
                    is IllegalArgumentException -> "Visualizer rejected session/capture configuration"
                    is IllegalStateException -> "Visualizer entered invalid state"
                    is UnsupportedOperationException -> "Visualizer unsupported on this device"
                    else -> "Visualizer construction failed"
                }
            L.w(e, "$message for session $sessionId")
            visualizer = null
            visualizerSessionId = null
            playbackModel.updateVisualizerState(VisualizerState.Failed(message))
        }
    }

    private fun scheduleVisualizerWatchdog(sessionId: Int, generation: Int) {
        visualizerWatchdogJob?.cancel()
        visualizerWatchdogJob =
            viewLifecycleOwner.lifecycleScope.launch {
                while (true) {
                    delay(VISUALIZER_WATCHDOG_INTERVAL_MS)
                    if (generation != visualizerGeneration || visualizerSessionId != sessionId) return@launch
                    val state = playbackModel.visualizerState.value
                    val now = android.os.SystemClock.uptimeMillis()
                    val hasFreshFrame =
                        state is VisualizerState.Live &&
                            now - state.receivedAtUptimeMs <= VISUALIZER_STALE_AFTER_MS
                    if (hasFreshFrame) continue

                    if (visualizerRetryCount < MAX_VISUALIZER_RETRIES) {
                        visualizerRetryCount++
                        L.w(
                            "Visualizer produced no recent usable frame; retrying session=$sessionId " +
                                "attempt=$visualizerRetryCount"
                        )
                        visualizerWatchdogJob = null
                        releaseVisualizer(publishHidden = false)
                        if (shouldUseVisualizerForCurrentState()) startVisualizer(sessionId)
                    } else {
                        playbackModel.updateVisualizerState(
                            VisualizerState.Failed("No usable FFT or waveform frames")
                        )
                    }
                    return@launch
                }
            }
    }

    private fun hasUsableFft(fft: ByteArray): Boolean {
        for (index in 2 until fft.size) {
            if (fft[index] != 0.toByte()) return true
        }
        return false
    }

    private fun hasUsableWaveform(waveform: ByteArray): Boolean {
        if (waveform.size < 16) return false
        var min = 255
        var max = 0
        for (sample in waveform) {
            val unsigned = sample.toInt() and 0xFF
            if (unsigned < min) min = unsigned
            if (unsigned > max) max = unsigned
        }
        return max - min >= MIN_WAVEFORM_RANGE
    }

    private fun updatePlaying(isPlaying: Boolean) {
        requireBinding().playbackPlayPause.isChecked = isPlaying
        requireBinding().playbackSeekBar?.setWaveEnabled(isPlaying)
        updateVisualizerState()
    }

    override fun onVisualizerModeChanged() {
        visualizerRetryCount = 0
        coverPagerAdapter.refreshVisualizerMode()
        updateVisualizerState(forceRestart = true)
    }

    private fun updateShuffleScope(scope: ShuffleScope) {
        requireBinding().playbackShuffle.apply {
            when (scope) {
                ShuffleScope.OFF -> {
                    isChecked = false
                    setIconResource(R.drawable.sel_shuffle_state_24)
                    contentDescription = context.getString(R.string.desc_shuffle_off)
                }
                ShuffleScope.ALL -> {
                    isChecked = true
                    setIconResource(R.drawable.sel_shuffle_state_24)
                    contentDescription = context.getString(R.string.desc_shuffle_all_songs)
                }
                ShuffleScope.GENRE -> {
                    isChecked = true
                    setIconResource(R.drawable.ic_shuffle_genre_state_24)
                    contentDescription = context.getString(R.string.desc_shuffle_current_genre)
                }
            }
        }
    }

    private fun updatePager(queue: PagerQueue) {
        val binding = requireBinding()
        coverPagerAdapter.setActivePosition(queue.index)

        val command = playbackModel.pagerCommand.consume()
        if (command == null) {
            // This probably shouldn't happen in practice, as QueueViewModel directly
            // attaches to PlaybackStateManager and will basically always initialize
            // with a command as a result.
            //
            // If it does happen we should just make sure the UI state is aligned. Don't
            // want broken UI.
            coverPagerAdapter.update(queue.queue, null)
            binding.playbackPager.setCurrentItem(queue.index, false)
            return
        }

        if (command.update != null) {
            // queue needs to be updated.
            coverPagerAdapter.update(queue.queue, command.update)
        }

        if (command.scroll != null) {
            // we need to scroll, however the smooth scroll only really looks best
            // when we are only doing next/prev due to various factors. better to
            // just not animate on outright gotos or queue updates
            val delta = binding.playbackPager.currentItem - command.scroll
            if (delta == 0) {
                // user scroll, carry on
                return
            }
            binding.playbackPager.setCurrentItem(
                command.scroll,
                command.update == null && abs(delta) == 1,
            )
        }
    }

    private fun navigateToCurrentSong() {
        playbackModel.song.value?.let(detailModel::showAlbum)
    }

    private fun navigateToCurrentArtist() {
        playbackModel.song.value?.let(detailModel::showArtist)
    }

    private fun navigateToCurrentAlbum() {
        playbackModel.song.value?.let { detailModel.showAlbum(it.album) }
    }

    override fun seek(direction: Direction) {
        when (direction) {
            Direction.FORWARDS -> playbackModel.stepForward()
            Direction.BACKWARDS -> playbackModel.stepBackwards()
        }
    }

    private companion object {
        const val FFT_PREFERENCE_WINDOW_MS = 300L
        const val VISUALIZER_WATCHDOG_INTERVAL_MS = 1_500L
        const val VISUALIZER_STALE_AFTER_MS = 2_000L
        const val MAX_VISUALIZER_RETRIES = 1
        const val MIN_WAVEFORM_RANGE = 4
    }

    private fun applyDriverSideLayout(binding: FragmentPlaybackPanelBinding) {
        if (uiSettings.driverSide != UISettings.DriverSide.LEFT) {
            return
        }
        val root = binding.root
        ConstraintSet().apply {
            clone(root)
            clear(R.id.playback_pager, ConstraintSet.START)
            connect(
                R.id.playback_pager,
                ConstraintSet.END,
                ConstraintSet.PARENT_ID,
                ConstraintSet.END,
            )

            clear(R.id.playback_info_container, ConstraintSet.START)
            clear(R.id.playback_info_container, ConstraintSet.END)
            connect(
                R.id.playback_info_container,
                ConstraintSet.START,
                ConstraintSet.PARENT_ID,
                ConstraintSet.START,
            )
            connect(
                R.id.playback_info_container,
                ConstraintSet.END,
                R.id.playback_pager,
                ConstraintSet.START,
            )

            clear(R.id.playback_controls_wrapper, ConstraintSet.START)
            clear(R.id.playback_controls_wrapper, ConstraintSet.END)
            connect(
                R.id.playback_controls_wrapper,
                ConstraintSet.START,
                ConstraintSet.PARENT_ID,
                ConstraintSet.START,
            )
            connect(
                R.id.playback_controls_wrapper,
                ConstraintSet.END,
                R.id.playback_pager,
                ConstraintSet.START,
            )
            applyTo(root)
        }
    }
}
