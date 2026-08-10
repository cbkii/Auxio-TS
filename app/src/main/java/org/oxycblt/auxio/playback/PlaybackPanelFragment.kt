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
import org.oxycblt.auxio.playback.queue.QueueDisplayItem
import org.oxycblt.auxio.playback.queue.QueueViewModel
import org.oxycblt.auxio.playback.state.RawPlaybackMetadata
import org.oxycblt.auxio.playback.state.RepeatMode
import org.oxycblt.auxio.playback.state.ShuffleScope
import org.oxycblt.auxio.playback.ui.StyledSeekBar
import org.oxycblt.auxio.playback.ui.stepper.Direction
import org.oxycblt.auxio.playback.ui.stepper.StepperOverlay
import org.oxycblt.auxio.playback.ui.swiper.CarouselTransformer
import org.oxycblt.auxio.playback.ui.swiper.CoverPagerAdapter
import org.oxycblt.auxio.playback.ui.swiper.PlaybackPagerProjection
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
    private var visualizerCoordinator: VisualizerCoordinator? = null
    private val visualizerPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            visualizerCoordinator?.onPermissionResult(isGranted)
        }

    override fun onCreateBinding(inflater: LayoutInflater) =
        FragmentPlaybackPanelBinding.inflate(inflater)

    override fun onBindingCreated(
        binding: FragmentPlaybackPanelBinding,
        savedInstanceState: Bundle?,
    ) {
        super.onBindingCreated(binding, savedInstanceState)

        val currentVisualizerCoordinator =
            VisualizerCoordinator(
                requireContext(),
                playbackModel.isPlaying,
                playbackModel.currentAudioSessionId,
                uiSettings,
            )
        visualizerCoordinator = currentVisualizerCoordinator
        viewLifecycleOwner.lifecycle.addObserver(currentVisualizerCoordinator)

        val currentCoverPagerAdapter =
            CoverPagerAdapter(
                this,
                currentVisualizerCoordinator.state,
                uiSettings,
                viewLifecycleOwner,
            )
        coverPagerAdapter = currentCoverPagerAdapter

        uiSettings.registerListener(this)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                currentVisualizerCoordinator.state.collect { state ->
                    if (
                        state is VisualizerState.PermissionRequired &&
                            currentVisualizerCoordinator.claimPermissionRequest()
                    ) {
                        visualizerPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
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
                UserAwarePagerCallback(this) { adapterIndex ->
                        // Capture the queue authority's global target before asynchronous primitive
                        // prefetch can prepend/trim the bounded display window.
                        val targetGlobalPosition = queueModel.globalPositionAt(adapterIndex)
                        currentCoverPagerAdapter.setActivePosition(adapterIndex)
                        queueModel.requestAdjacentRange(adapterIndex, adapterIndex)
                        // Posting the queue goto command prevents the seekbar pos from desyncing
                        // from the item's duration, which creates a visual flicker in the seekbar.
                        if (targetGlobalPosition != null) {
                            post { queueModel.gotoGlobalPosition(targetGlobalPosition) }
                        }
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
        collectImmediately(playbackModel.song, playbackModel.rawPlaybackMetadata) { song, raw ->
            updatePlaybackMetadata(song, raw)
        }
        collectImmediately(playbackModel.parent, ::updateParent)
        collectImmediately(playbackModel.positionDs, ::updatePosition)
        collectImmediately(playbackModel.repeatMode, ::updateRepeat)
        collectImmediately(playbackModel.isPlaying, ::updatePlaying)
        collectImmediately(playbackModel.shuffleScope, ::updateShuffleScope)
        collectImmediately(
            queueModel.queue,
            queueModel.index,
            playbackModel.rawPlaybackMetadata,
        ) { queue, index, raw ->
            updatePager(queue, index, raw)
        }
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
        visualizerCoordinator = null
        binding.playbackRepeat.clearPendingIcon()
        binding.playbackSong.isSelected = false
        binding.playbackArtist.isSelected = false
        binding.playbackAlbum?.isSelected = false
        binding.playbackToolbar.setOnMenuItemClickListener(null)
        userAwarePagerCallback?.release()
        userAwarePagerCallback = null
        binding.playbackPager.adapter = null
        coverPagerAdapter = null
        // Do not let a stale volatile UI command survive binding recreation.
        queueModel.queueInstructions.consume()
        queueModel.scrollTo.consume()
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

    private fun updatePlaybackMetadata(song: Song?, raw: RawPlaybackMetadata?) {
        val binding = requireBinding()
        val hasRichSong = song != null
        val playable = hasRichSong || raw != null
        binding.playbackMore?.isEnabled = hasRichSong
        binding.playbackSong.isClickable = hasRichSong
        binding.playbackArtist.isClickable = hasRichSong
        binding.playbackAlbum?.isClickable = hasRichSong
        binding.playbackShuffle.isEnabled = hasRichSong
        binding.playbackRepeat.isEnabled = playable
        binding.playbackPlayPause.isEnabled = playable

        when {
            song != null -> {
                val context = requireContext()
                L.d("Updating rich song display: $song")
                binding.playbackSong.text = song.name.resolve(context)
                binding.playbackArtist.text = song.artists.resolveNames(context)
                binding.playbackAlbum?.text = song.album.name.resolve(context)
                binding.playbackSeekBar?.durationDs = song.durationMs.msToDs()
            }
            raw != null -> {
                L.d("Updating primitive/raw Now Playing display")
                binding.playbackSong.text = raw.displayTitle
                binding.playbackArtist.text = raw.displayArtist
                binding.playbackAlbum?.text = raw.album.orEmpty()
                binding.playbackSeekBar?.durationDs = raw.durationMs.msToDs()
            }
            else -> {
                binding.playbackSong.text = ""
                binding.playbackArtist.text = ""
                binding.playbackAlbum?.text = ""
                binding.playbackSeekBar?.durationDs = 0L
            }
        }

        // Capability transitions can occur without a corresponding repeat/play/shuffle StateFlow
        // value change, so reconcile checked state whenever Rich/Raw/Idle presentation changes.
        updateRepeat(playbackModel.repeatMode.value)
        updatePlaying(playbackModel.isPlaying.value)
        updateShuffleScope(playbackModel.shuffleScope.value)
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
        repeatButton.isChecked = repeatButton.isEnabled && repeatMode != RepeatMode.NONE
        repeatButton.setIconResource(repeatMode.icon)
    }

    private fun updatePlaying(isPlaying: Boolean) {
        val binding = requireBinding()
        binding.playbackPlayPause.isChecked = binding.playbackPlayPause.isEnabled && isPlaying
        binding.playbackSeekBar?.setWaveEnabled(isPlaying)
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
                    isChecked = isEnabled
                    setIconResource(R.drawable.sel_shuffle_state_24)
                    contentDescription = context.getString(R.string.desc_shuffle_all_songs)
                }
                ShuffleScope.GENRE -> {
                    isChecked = isEnabled
                    setIconResource(R.drawable.ic_shuffle_genre_state_24)
                    contentDescription = context.getString(R.string.desc_shuffle_current_genre)
                }
            }
        }
    }

    private fun updatePager(
        queue: List<QueueDisplayItem>,
        queueIndex: Int,
        rawMetadata: RawPlaybackMetadata?,
    ) {
        val binding = requireBinding()
        val adapter =
            checkNotNull(coverPagerAdapter) {
                "CoverPagerAdapter must exist while the playback-panel binding is active"
            }
        val presentation = PlaybackPagerProjection.project(queue, queueIndex, rawMetadata)
        val queueInstruction = queueModel.queueInstructions.consume()

        adapter.setActivePosition(presentation.activeIndex)
        adapter.update(
            presentation.items,
            // QueueViewModel instructions are authoritative only for queue-backed pages. Raw is a
            // single display fallback and uses ordinary diffing.
            queueInstruction.takeIf { queue.isNotEmpty() },
        )

        val queueCanNavigate = queue.isNotEmpty()
        binding.playbackSkipPrev.isEnabled = queueCanNavigate
        binding.playbackSkipNext.isEnabled = queueCanNavigate

        if (!presentation.hasPlayablePage) {
            queueModel.scrollTo.consume()
            return
        }

        val requestedScroll =
            if (queue.isNotEmpty()) queueModel.scrollTo.consume() else null
        val target = requestedScroll ?: presentation.activeIndex
        if (target !in presentation.items.indices) return

        val delta = binding.playbackPager.currentItem - target
        if (delta == 0) return
        binding.playbackPager.setCurrentItem(
            target,
            queueInstruction == null && abs(delta) == 1,
        )
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
