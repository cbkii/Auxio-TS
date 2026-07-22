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
import org.oxycblt.auxio.playback.ui.stepper.StepperOverlay
import org.oxycblt.auxio.playback.ui.visualizer.VisualizerDisplayPolicy
import org.oxycblt.auxio.playback.ui.visualizer.VisualizerState
import org.oxycblt.auxio.ui.UISettings
import org.oxycblt.auxio.util.inflater
import org.oxycblt.musikr.Song

/**
 * Hosts cover pages while keeping visualiser collection at adapter scope.
 *
 * Only the current page receives state. Attached off-screen holders are reset to artwork, and stale
 * replayed frames are converted to an explicit unavailable state before display.
 */
class CoverPagerAdapter(
    private val listener: StepperOverlay.Listener,
    visualizerStateFlow: kotlinx.coroutines.flow.StateFlow<VisualizerState>,
    private val uiSettings: UISettings,
    lifecycleOwner: LifecycleOwner,
) : FlexibleListAdapter<Song, CoverViewHolder>(CoverViewHolder.DIFF_CALLBACK) {

    private val attachedHolders = linkedSetOf<CoverViewHolder>()
    private var activePosition = RecyclerView.NO_POSITION
    private var latestState: VisualizerState = VisualizerState.Disabled

    init {
        lifecycleOwner.lifecycleScope.launch {
            visualizerStateFlow.collect { state ->
                latestState =
                    VisualizerDisplayPolicy.sanitizeLiveFrame(state, SystemClock.uptimeMillis())
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
}

/** A ViewHolder containing artwork, the visualiser surface, and fast-seek gestures. */
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
        val shouldShow =
            VisualizerDisplayPolicy.shouldShow(
                state = state,
                mode = mode,
                hasArtwork = song?.cover != null,
            )
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
