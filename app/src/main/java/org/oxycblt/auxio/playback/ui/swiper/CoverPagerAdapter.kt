/*
 * Copyright (c) 2026 Auxio Project
 * CoverPagerAdapter.kt is part of Auxio.
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

package org.oxycblt.auxio.playback.ui.swiper

import android.os.SystemClock
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import org.oxycblt.auxio.databinding.ItemCoverBinding
import org.oxycblt.auxio.list.adapter.FlexibleListAdapter
import org.oxycblt.auxio.list.adapter.SimpleDiffCallback
import org.oxycblt.auxio.playback.PlaybackViewModel
import org.oxycblt.auxio.playback.ui.stepper.StepperOverlay
import org.oxycblt.auxio.playback.ui.visualizer.VisualizerState
import org.oxycblt.auxio.ui.UISettings
import org.oxycblt.auxio.util.inflater
import org.oxycblt.musikr.Song

/**
 * Hosts cover pages while keeping visualizer collection at adapter scope.
 *
 * Only the current page receives live frames. Attached off-screen holders are reset to artwork, and
 * replayed StateFlow frames are rejected before they can briefly replace artwork after recreation.
 */
class CoverPagerAdapter(
    private val listener: StepperOverlay.Listener,
    private val visualizerStateFlow: kotlinx.coroutines.flow.StateFlow<VisualizerState>,
    private val uiSettings: UISettings,
    lifecycleOwner: LifecycleOwner,
) : FlexibleListAdapter<Song, CoverViewHolder>(CoverViewHolder.DIFF_CALLBACK) {

    private val attachedHolders = linkedSetOf<CoverViewHolder>()
    private var activePosition = RecyclerView.NO_POSITION
    private var latestState: VisualizerState = VisualizerState.Disabled

    init {
        lifecycleOwner.lifecycleScope.launch {
            visualizerStateFlow.collect { state ->
                latestState = sanitize(state)
                dispatchVisualizerState()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, pos: Int) = CoverViewHolder.from(parent)

    override fun onBindViewHolder(viewHolder: CoverViewHolder, pos: Int) {
        viewHolder.bind(currentList[pos], listener)
        attachedHolders += viewHolder
        updateHolder(viewHolder)
    }

    override fun onViewAttachedToWindow(holder: CoverViewHolder) {
        super.onViewAttachedToWindow(holder)
        attachedHolders += holder
        updateHolder(holder)
    }

    override fun onViewDetachedFromWindow(holder: CoverViewHolder) {
        holder.updateVisualizerState(VisualizerState.Disabled, uiSettings.visualizerMode)
        attachedHolders -= holder
        super.onViewDetachedFromWindow(holder)
    }

    override fun onViewRecycled(viewHolder: CoverViewHolder) {
        attachedHolders -= viewHolder
        viewHolder.onViewRecycled()
        super.onViewRecycled(viewHolder)
    }

    fun setActivePosition(position: Int) {
        if (activePosition == position) return
        activePosition = position
        dispatchVisualizerState()
    }

    fun refreshVisualizerMode() {
        dispatchVisualizerState()
    }

    private fun dispatchVisualizerState() {
        attachedHolders.toList().forEach(::updateHolder)
    }

    private fun updateHolder(holder: CoverViewHolder) {
        val state =
            if (holder.bindingAdapterPosition == activePosition) latestState
            else VisualizerState.Disabled
        holder.updateVisualizerState(state, uiSettings.visualizerMode)
    }

    private fun sanitize(state: VisualizerState): VisualizerState {
        if (state !is VisualizerState.Live) return state
        val ageMs = SystemClock.uptimeMillis() - state.receivedAtUptimeMs
        return if (ageMs in 0..LIVE_FRAME_FRESHNESS_MS) {
            state
        } else {
            VisualizerState.Unavailable("Stale visualizer frame")
        }
    }

    private companion object {
        const val LIVE_FRAME_FRESHNESS_MS = 1_500L
    }
}

/** A ViewHolder containing artwork, the visualizer surface, and fast-seek gestures. */
class CoverViewHolder private constructor(private val binding: ItemCoverBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private var song: Song? = null

    fun onViewRecycled() {
        song = null
        binding.coverVisualizer.updateState(VisualizerState.Disabled)
        binding.coverVisualizer.visibility = View.GONE
        binding.cover.visibility = View.VISIBLE
    }

    fun bind(song: Song, listener: StepperOverlay.Listener) {
        this.song = song
        binding.cover.bind(song)
        binding.coverFastSeekOverlay.listener = listener
        updateVisualizerState(VisualizerState.Disabled, UISettings.VisualizerMode.OFF)
    }

    fun updateVisualizerState(state: VisualizerState, mode: UISettings.VisualizerMode) {
        binding.coverVisualizer.updateState(state)
        val hasArtwork = song?.cover != null
        val shouldShow =
            state is VisualizerState.Live &&
                (mode == UISettings.VisualizerMode.ALWAYS ||
                    (mode == UISettings.VisualizerMode.FALLBACK && !hasArtwork))

        binding.coverVisualizer.visibility = if (shouldShow) View.VISIBLE else View.GONE
        binding.cover.visibility = if (shouldShow) View.INVISIBLE else View.VISIBLE
    }

    companion object {
        fun from(parent: ViewGroup) =
            CoverViewHolder(ItemCoverBinding.inflate(parent.context.inflater, parent, false))

        val DIFF_CALLBACK =
            object : SimpleDiffCallback<Song>() {
                override fun areContentsTheSame(oldItem: Song, newItem: Song) =
                    oldItem.cover == newItem.cover
            }
    }
}
