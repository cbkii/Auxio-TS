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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import org.oxycblt.auxio.databinding.ItemCoverBinding
import org.oxycblt.auxio.list.adapter.FlexibleListAdapter
import org.oxycblt.auxio.playback.ui.stepper.StepperOverlay
import org.oxycblt.auxio.playback.ui.visualizer.VisualizerDisplayPolicy
import org.oxycblt.auxio.playback.ui.visualizer.VisualizerRecoveryPolicy
import org.oxycblt.auxio.playback.ui.visualizer.VisualizerState
import org.oxycblt.auxio.ui.UISettings
import org.oxycblt.auxio.util.inflater
import org.oxycblt.musikr.tag.Name

/**
 * Hosts rich, primitive and raw Now Playing pages while keeping visualizer collection at adapter
 * scope.
 *
 * Frequent live frames are delivered only to the current page. Attached off-screen holders are
 * reset only when attachment, position, or mode state changes, and replayed stale frames are
 * rejected before they can briefly replace artwork after recreation.
 */
class CoverPagerAdapter(
    private val listener: StepperOverlay.Listener,
    private val visualizerStateFlow: kotlinx.coroutines.flow.StateFlow<VisualizerState>,
    private val uiSettings: UISettings,
    lifecycleOwner: LifecycleOwner,
) : FlexibleListAdapter<PlaybackPagerItem, CoverViewHolder>(CoverViewHolder.DIFF_CALLBACK) {

    private val attachedHolders = linkedSetOf<CoverViewHolder>()
    private var activePosition = RecyclerView.NO_POSITION
    private var latestState: VisualizerState = VisualizerState.Disabled
    private var visualizerMode = uiSettings.visualizerMode

    init {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                visualizerStateFlow.collect { state ->
                    latestState = sanitize(state)
                    dispatchActiveVisualizerState(latestState)
                }
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
        holder.updateVisualizerState(VisualizerState.Disabled, visualizerMode)
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
        dispatchAllVisualizerState()
    }

    fun refreshVisualizerMode() {
        val newMode = uiSettings.visualizerMode
        if (visualizerMode == newMode) return
        visualizerMode = newMode
        dispatchAllVisualizerState()
    }

    private fun dispatchActiveVisualizerState(state: VisualizerState) {
        for (holder in attachedHolders) {
            if (holder.bindingAdapterPosition == activePosition) {
                holder.updateVisualizerState(state, visualizerMode)
            }
        }
    }

    private fun dispatchAllVisualizerState() {
        for (holder in attachedHolders) updateHolder(holder)
    }

    private fun updateHolder(holder: CoverViewHolder) {
        val state =
            if (holder.bindingAdapterPosition == activePosition) sanitize(latestState)
            else VisualizerState.Disabled
        holder.updateVisualizerState(state, visualizerMode)
    }

    private fun sanitize(state: VisualizerState): VisualizerState =
        VisualizerRecoveryPolicy.sanitizeCachedState(state, SystemClock.uptimeMillis())
}

/** A ViewHolder containing artwork/fallback, the visualizer surface, and fast-seek gestures. */
class CoverViewHolder private constructor(private val binding: ItemCoverBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private var item: PlaybackPagerItem? = null
    private var visualizerShown = false

    fun onViewRecycled() {
        item = null
        binding.coverVisualizer.updateState(VisualizerState.Disabled)
        visualizerShown = false
        binding.coverVisualizer.visibility = View.GONE
        binding.cover.visibility = View.VISIBLE
        binding.cover.clear()
    }

    fun bind(item: PlaybackPagerItem, listener: StepperOverlay.Listener) {
        this.item = item
        val song = item.song
        if (song != null) {
            binding.cover.bind(song)
        } else {
            // Primitive/raw Fast Resume deliberately has no fake Song or artwork authority.
            binding.cover.clear()
        }
        binding.coverVisualizer.configureTrack(item.visualizerTrackKey, item.durationMs)
        binding.coverFastSeekOverlay.listener = listener
        updateVisualizerState(VisualizerState.Disabled, UISettings.VisualizerMode.OFF)
    }

    fun updateVisualizerState(state: VisualizerState, mode: UISettings.VisualizerMode) {
        val shouldShow =
            VisualizerDisplayPolicy.shouldShowVisualizer(
                state = state,
                mode = mode,
                hasArtwork = item?.song?.cover != null,
            )

        if (shouldShow) {
            binding.coverVisualizer.updateState(state)
        } else if (visualizerShown) {
            // Reset once when leaving the visible state; hidden live frames need no mapper work.
            binding.coverVisualizer.updateState(VisualizerState.Disabled)
        }

        if (visualizerShown == shouldShow) return
        visualizerShown = shouldShow
        binding.coverVisualizer.visibility = if (shouldShow) View.VISIBLE else View.GONE
        binding.cover.visibility = if (shouldShow) View.INVISIBLE else View.VISIBLE
    }

    companion object {
        fun from(parent: ViewGroup) =
            CoverViewHolder(ItemCoverBinding.inflate(parent.context.inflater, parent, false))

        val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<PlaybackPagerItem>() {
                override fun areItemsTheSame(
                    oldItem: PlaybackPagerItem,
                    newItem: PlaybackPagerItem,
                ) = PlaybackPagerItem.sameLogicalItem(oldItem, newItem)

                override fun areContentsTheSame(
                    oldItem: PlaybackPagerItem,
                    newItem: PlaybackPagerItem,
                ): Boolean =
                    when {
                        oldItem is PlaybackPagerItem.Rich && newItem is PlaybackPagerItem.Rich ->
                            oldItem.song.cover == newItem.song.cover &&
                                sameDisplayName(oldItem.song.album.name, newItem.song.album.name) &&
                                oldItem.durationMs == newItem.durationMs
                        oldItem is PlaybackPagerItem.Primitive &&
                            newItem is PlaybackPagerItem.Primitive -> oldItem.item == newItem.item
                        oldItem is PlaybackPagerItem.Raw && newItem is PlaybackPagerItem.Raw ->
                            oldItem.metadata.title == newItem.metadata.title &&
                                oldItem.metadata.artist == newItem.metadata.artist &&
                                oldItem.metadata.album == newItem.metadata.album &&
                                oldItem.metadata.uriString == newItem.metadata.uriString &&
                                oldItem.metadata.path == newItem.metadata.path &&
                                oldItem.metadata.durationMs == newItem.metadata.durationMs
                        else -> false
                    }
            }

        private fun sameDisplayName(oldName: Name, newName: Name): Boolean =
            when {
                oldName is Name.Known && newName is Name.Known -> oldName.raw == newName.raw
                oldName is Name.Unknown && newName is Name.Unknown ->
                    oldName.placeholder == newName.placeholder
                else -> false
            }
    }
}
