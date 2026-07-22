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
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintSet
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
import kotlinx.coroutines.launch
import org.oxycblt.auxio.BuildConfig
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
import org.oxycblt.auxio.playback.ui.visualizer.VisualizerCoordinator
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
    private var coverPagerAdapter: CoverPagerAdapter? = null
    private val playbackModel: PlaybackViewModel by activityViewModels()
    private val detailModel: DetailViewModel by activityViewModels()
    @Inject lateinit var uiSettings: UISettings
    private val queueModel: QueueViewModel by viewModels()
    private var userAwarePagerCallback: UserAwarePagerCallback? = null
    private lateinit var visualizerCoordinator: VisualizerCoordinator
    private var visualizerPermissionLauncher:
        androidx.activity.result.ActivityResultLauncher<String>? =
        null

    override fun onCreateBinding(inflater: LayoutInflater) =
        FragmentPlaybackPanelBinding.inflate(inflater)

    override fun onBindingCreated(
        binding: FragmentPlaybackPanelBinding,
        savedInstanceState: Bundle?,
    ) {
        super.onBindingCreated(binding, savedInstanceState)

        visualizerCoordinator =
            VisualizerCoordinator(
                requireContext(),
                playbackModel.isPlaying,
                playbackModel.currentAudioSessionId,
                uiSettings,
            )
        viewLifecycleOwner.lifecycle.addObserver(visualizerCoordinator)

        val currentCoverPagerAdapter =
            CoverPagerAdapter(this, visualizerCoordinator.state, uiSettings, viewLifecycleOwner)
        coverPagerAdapter = currentCoverPagerAdapter

        visualizerPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                visualizerCoordinator.onPermissionResult(isGranted)
            }
        uiSettings.registerListener(this)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                visualizerCoordinator.state.collect { state ->
                    if (
                        state is VisualizerState.PermissionRequired &&
                            visualizerCoordinator.claimPermissionRequest()
                    ) {
                        visualizerPermissionLauncher?.launch(Manifest.permission.RECORD_AUDIO)
                    }
                }
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

        binding.playbackPager.apply {
            adapter = currentCoverPagerAdapter
            userAwarePagerCallback =
                UserAwarePagerCallback(this) {
                        currentCoverPagerAdapter.setActivePosition(it)
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
        val forceLargeLandscapeControls =
            resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        val useLargeControls =
            if (BuildConfig.TOPWAY_COMPAT_FLAVOR) {
                true
            } else {
                uiSettings.largeHeadUnitControls || forceLargeLandscapeControls
            }
        binding.playbackSeekBar?.setLargeTouchMode(useLargeControls)
        val playbackInfoVerticalPadding =
            when {
                !uiSettings.showHeadUnitAlbumArt -> spacingMedium
                useLargeControls -> spacingSmall
                else -> null
            }
        if (!uiSettings.showHeadUnitAlbumArt) {
            binding.playbackPager.visibility = View.GONE
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
        binding.playbackRepeat.clearPendingIcon()
        binding.playbackSong.isSelected = false
        binding.playbackArtist.isSelected = false
        binding.playbackAlbum?.isSelected = false
        binding.playbackToolbar.setOnMenuItemClickListener(null)
        userAwarePagerCallback?.release()
        userAwarePagerCallback = null
        binding.playbackPager.adapter = null
        coverPagerAdapter = null
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_open_equalizer) {
            L.d("Launching TS18/native equalizer with Android fallback")
            val candidates =
                TopwayEqualizerLauncher.resolveCandidates(
                    requireContext(),
                    playbackModel.currentAudioSessionId.value,
                )
            val launched =
                TopwayEqualizerLauncher.launchFirstWorkingCandidate(
                    candidates = candidates,
                    launch = { requireContext().startActivity(it) },
                    onFailure = { candidate, error ->
                        when (error) {
                            is android.content.ActivityNotFoundException ->
                                L.w(
                                    error,
                                    "EQ/DSP candidate not found after resolution: ${candidate.label}",
                                )
                            is SecurityException ->
                                L.w(error, "EQ/DSP candidate denied: ${candidate.label}")
                            else ->
                                L.w(error, "EQ/DSP candidate failed at launch: ${candidate.label}")
                        }
                    },
                )
            if (launched != null) {
                L.i("Launched EQ/DSP candidate ${launched.label} (${launched.kind})")
            } else {
                requireContext().showToast(R.string.err_no_equalizer_app)
            }
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

    private fun updatePlaying(isPlaying: Boolean) {
        requireBinding().playbackPlayPause.isChecked = isPlaying
        requireBinding().playbackSeekBar?.setWaveEnabled(isPlaying)
    }

    override fun onVisualizerModeChanged() {
        coverPagerAdapter?.refreshVisualizerMode()
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
        val adapter =
            checkNotNull(coverPagerAdapter) {
                "CoverPagerAdapter must exist while the playback-panel binding is active"
            }
        adapter.setActivePosition(queue.index)

        val command = playbackModel.pagerCommand.consume()
        if (command == null) {
            // This probably shouldn't happen in practice, as QueueViewModel directly
            // attaches to PlaybackStateManager and will basically always initialize
            // with a command as a result.
            //
            // If it does happen we should just make sure the UI state is aligned. Don't
            // want broken UI.
            adapter.update(queue.queue, null)
            binding.playbackPager.setCurrentItem(queue.index, false)
            return
        }

        if (command.update != null) {
            // queue needs to be updated.
            adapter.update(queue.queue, command.update)
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
