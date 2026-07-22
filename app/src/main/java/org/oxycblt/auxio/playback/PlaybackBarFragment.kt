/*
 * Copyright (c) 2022 Auxio Project
 * PlaybackBarFragment.kt is part of Auxio.
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

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import org.oxycblt.auxio.R
import org.oxycblt.auxio.databinding.FragmentPlaybackBarBinding
import org.oxycblt.auxio.detail.DetailViewModel
import org.oxycblt.auxio.headunit.HeadUnitUiAdapter
import org.oxycblt.auxio.music.resolve
import org.oxycblt.auxio.music.resolveNames
import org.oxycblt.auxio.playback.state.RepeatMode
import org.oxycblt.auxio.playback.state.ShuffleScope
import org.oxycblt.auxio.ui.UISettings
import org.oxycblt.auxio.ui.ViewBindingFragment
import org.oxycblt.auxio.util.collectImmediately
import org.oxycblt.auxio.util.showToast
import timber.log.Timber as L

/**
 * A [ViewBindingFragment] that shows the current playback state in a compact manner.
 *
 * @author Alexander Capehart (OxygenCobalt)
 */
@AndroidEntryPoint
class PlaybackBarFragment : ViewBindingFragment<FragmentPlaybackBarBinding>() {
    private val playbackModel: PlaybackViewModel by activityViewModels()
    private val detailModel: DetailViewModel by activityViewModels()
    @Inject lateinit var uiSettings: UISettings

    private var currentBannerState: BannerState = BannerState.Idle

    override fun onCreateBinding(inflater: LayoutInflater) =
        FragmentPlaybackBarBinding.inflate(inflater)

    override fun onBindingCreated(
        binding: FragmentPlaybackBarBinding,
        savedInstanceState: Bundle?,
    ) {
        super.onBindingCreated(binding, savedInstanceState)
        val context = requireContext()

        binding.playbackSong.isSelected = true
        binding.playbackInfo.isSelected = true

        binding.playbackRepeat.setOnClickListener { playbackModel.toggleRepeatMode() }
        binding.playbackSkipPrev.setOnClickListener {
            playbackModel.prev()
            context.showToast(R.string.msg_playback_previous)
        }
        binding.playbackPlayPause.setOnClickListener { playbackModel.togglePlaying() }
        binding.playbackSkipNext.setOnClickListener {
            playbackModel.next()
            context.showToast(R.string.msg_playback_next)
        }
        binding.playbackShuffle.setOnClickListener { playbackModel.cycleShuffleScope() }

        val useLargeControls =
            uiSettings.largeHeadUnitControls ||
                resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
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
            banner = true,
            primaryButton = binding.playbackPlayPause,
        )
        HeadUnitUiAdapter.applyLargePlaybackText(
            resources,
            useLargeControls,
            binding.playbackSong,
            binding.playbackInfo,
        )
        applyDriverSideLayout(binding)

        collectImmediately(playbackModel.bannerState, ::updateBannerState)
        collectImmediately(playbackModel.isPlaying, ::updatePlaying)
        collectImmediately(playbackModel.positionDs, ::updatePosition)
        collectImmediately(playbackModel.repeatMode, ::updateRepeat)
        collectImmediately(playbackModel.shuffleScope, ::updateShuffleScope)
    }

    override fun onDestroyBinding(binding: FragmentPlaybackBarBinding) {
        binding.playbackRepeat.clearPendingIcon()
        binding.playbackSong.isSelected = false
        binding.playbackInfo.isSelected = false
        currentBannerState = BannerState.Idle
        super.onDestroyBinding(binding)
    }

    private fun updateBannerState(state: BannerState) {
        currentBannerState = state
        val context = requireContext()
        val binding = requireBinding()

        binding.root.setOnClickListener {
            if (state.playable) playbackModel.openPlayback()
        }
        binding.root.setOnLongClickListener {
            if (state is BannerState.Rich) {
                detailModel.showAlbum(state.song)
                true
            } else {
                false
            }
        }

        binding.playbackPlayPause.isEnabled = state.playable
        binding.playbackSkipNext.isEnabled = state.richQueueCommandsAvailable
        binding.playbackSkipPrev.isEnabled = state.richQueueCommandsAvailable
        binding.playbackRepeat.isEnabled = state.richQueueCommandsAvailable
        binding.playbackShuffle.isEnabled = state.richQueueCommandsAvailable

        when (state) {
            is BannerState.Rich -> {
                binding.playbackCover.bind(state.song)
                binding.playbackSong.text = state.song.name.resolve(context)
                binding.playbackInfo.text = state.song.artists.resolveNames(context)
                binding.playbackProgressBar.max = state.song.durationMs.msToDs().toInt()
            }
            is BannerState.Raw -> {
                binding.playbackCover.clear()
                binding.playbackSong.text = state.metadata.displayTitle
                binding.playbackInfo.text = state.metadata.displayArtist
                binding.playbackProgressBar.max = state.metadata.durationMs.msToDs().toInt()
            }
            BannerState.Restoring -> {
                clearMediaState(binding)
                binding.playbackSong.setText(R.string.lbl_playback_restoring)
            }
            BannerState.Idle -> {
                clearMediaState(binding)
                binding.playbackSong.setText(R.string.lbl_playback_idle)
            }
            is BannerState.Unavailable -> {
                L.w("Playback banner unavailable: ${state.reason}")
                clearMediaState(binding)
                binding.playbackSong.setText(R.string.lbl_playback_unavailable)
                binding.playbackInfo.setText(R.string.msg_playback_restore_failed)
            }
        }
        updatePlaying(playbackModel.isPlaying.value)
        updatePosition(playbackModel.positionDs.value)
    }

    private fun clearMediaState(binding: FragmentPlaybackBarBinding) {
        binding.playbackCover.clear()
        binding.playbackInfo.text = ""
        binding.playbackProgressBar.progress = 0
        binding.playbackProgressBar.max = 1
    }

    private fun updatePlaying(isPlaying: Boolean) {
        requireBinding().playbackPlayPause.isChecked = isPlaying && currentBannerState.playable
    }

    private fun updatePosition(positionDs: Long) {
        requireBinding().playbackProgressBar.progress =
            if (currentBannerState.playable) positionDs.toInt() else 0
    }

    private fun updateRepeat(repeatMode: RepeatMode) {
        requireBinding().playbackRepeat.apply {
            isChecked = currentBannerState.richQueueCommandsAvailable && repeatMode != RepeatMode.NONE
            setIconResource(repeatMode.icon)
        }
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
                    isChecked = currentBannerState.richQueueCommandsAvailable
                    setIconResource(R.drawable.sel_shuffle_state_24)
                    contentDescription = context.getString(R.string.desc_shuffle_all_songs)
                }
                ShuffleScope.GENRE -> {
                    isChecked = currentBannerState.richQueueCommandsAvailable
                    setIconResource(R.drawable.ic_shuffle_genre_state_24)
                    contentDescription = context.getString(R.string.desc_shuffle_current_genre)
                }
            }
        }
    }

    private fun applyDriverSideLayout(binding: FragmentPlaybackBarBinding) {
        if (uiSettings.driverSide != UISettings.DriverSide.LEFT) return
        val root = binding.root
        ConstraintSet().apply {
            clone(root)
            clear(R.id.playback_cover, ConstraintSet.START)
            connect(
                R.id.playback_cover,
                ConstraintSet.END,
                ConstraintSet.PARENT_ID,
                ConstraintSet.END,
            )

            clear(R.id.playback_controls_wrapper, ConstraintSet.END)
            connect(
                R.id.playback_controls_wrapper,
                ConstraintSet.START,
                ConstraintSet.PARENT_ID,
                ConstraintSet.START,
            )

            clear(R.id.playback_song, ConstraintSet.START)
            clear(R.id.playback_song, ConstraintSet.END)
            connect(
                R.id.playback_song,
                ConstraintSet.START,
                R.id.playback_controls_wrapper,
                ConstraintSet.END,
            )
            connect(R.id.playback_song, ConstraintSet.END, R.id.playback_cover, ConstraintSet.START)

            clear(R.id.playback_info, ConstraintSet.START)
            clear(R.id.playback_info, ConstraintSet.END)
            connect(
                R.id.playback_info,
                ConstraintSet.START,
                R.id.playback_controls_wrapper,
                ConstraintSet.END,
            )
            connect(R.id.playback_info, ConstraintSet.END, R.id.playback_cover, ConstraintSet.START)
            applyTo(root)
        }
    }
}
