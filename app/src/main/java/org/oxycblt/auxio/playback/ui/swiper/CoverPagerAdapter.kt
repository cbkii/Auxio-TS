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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
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
 * A [FlexibleListAdapter] that hosts [CoverViewHolder]s containing a [Song]'s cover and step
 * gesture overlays.
 *
 * Visualizer state is collected once at adapter scope. Only the currently visible ViewPager item
 * receives live FFT frames; attached off-screen holders are explicitly reset to
 * [VisualizerState.Hidden]. A low-frequency freshness check restores artwork if the last live frame
 * is no longer current.
 *
 * @param listener The [StepperOverlay.Listener] that step gesture events will be forwarded to
 * @author Alexander Capehart (OxygenCobalt)
 */
class CoverPagerAdapter(
    private val listener: StepperOverlay.Listener,
    playbackModel: PlaybackViewModel,
    private val uiSettings: UISettings,
    lifecycleOwner: LifecycleOwner,
) : FlexibleListAdapter<Song, CoverViewHolder>(CoverViewHolder.DIFF_CALLBACK) {

    private var recyclerView: RecyclerView? = null
    private var activePosition = 0
    private var latestVisualizerState: VisualizerState = VisualizerState.Hidden

    private val scrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    updateActivePositionAndDispatch()
                }
            }
        }

    init {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    playbackModel.visualizerState.collect { state ->
                        latestVisualizerState = state
                        updateActivePositionAndDispatch()
                    }
                }
                launch {
                    while (isActive) {
                        delay(FRESHNESS_CHECK_INTERVAL_MS)
                        val state = latestVisualizerState
                        if (
                            state is VisualizerState.Live &&
                                SystemClock.uptimeMillis() - state.receivedAtUptimeMs >
                                    LIVE_FRAME_FRESHNESS_MS
                        ) {
                            latestVisualizerState =
                                VisualizerState.Failed("Visualizer frame freshness expired")
                            updateActivePositionAndDispatch()
                        }
                    }
                }
            }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
        recyclerView.addOnScrollListener(scrollListener)
        recyclerView.post { updateActivePositionAndDispatch() }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        recyclerView.removeOnScrollListener(scrollListener)
        if (this.recyclerView === recyclerView) {
            this.recyclerView = null
        }
        super.onDetachedFromRecyclerView(recyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, pos: Int) = CoverViewHolder.from(parent)

    override fun onBindViewHolder(viewHolder: CoverViewHolder, pos: Int) {
        val song = currentList[pos]
        viewHolder.bind(song, listener)
        viewHolder.updateVisualizerState(
            if (pos == activePosition) latestVisualizerState else VisualizerState.Hidden,
            uiSettings.visualizerMode,
            hasArtwork = song.cover != null,
        )
    }

    override fun onViewRecycled(viewHolder: CoverViewHolder) {
        viewHolder.onViewRecycled()
        super.onViewRecycled(viewHolder)
    }

    private fun updateActivePositionAndDispatch() {
        val recycler = recyclerView ?: return
        val layoutManager = recycler.layoutManager as? LinearLayoutManager
        val resolvedPosition =
            layoutManager?.findFirstCompletelyVisibleItemPosition()?.takeIf {
                it != RecyclerView.NO_POSITION
            }
                ?: layoutManager?.findFirstVisibleItemPosition()?.takeIf {
                    it != RecyclerView.NO_POSITION
                }
        if (resolvedPosition != null) {
            activePosition = resolvedPosition
        }

        for (index in 0 until recycler.childCount) {
            val child = recycler.getChildAt(index)
            val holder = recycler.getChildViewHolder(child) as? CoverViewHolder ?: continue
            val position = holder.bindingAdapterPosition
            val song = currentList.getOrNull(position) ?: continue
            holder.updateVisualizerState(
                if (position == activePosition) latestVisualizerState else VisualizerState.Hidden,
                uiSettings.visualizerMode,
                hasArtwork = song.cover != null,
            )
        }
    }

    companion object {
        private const val LIVE_FRAME_FRESHNESS_MS = 1_500L
        private const val FRESHNESS_CHECK_INTERVAL_MS = 500L
    }
}

/**
 * A [RecyclerView.ViewHolder] that displays a [Song]'s cover and step gesture overlays.
 *
 * @author Alexander Capehart (OxygenCobalt)
 */
class CoverViewHolder private constructor(private val binding: ItemCoverBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onViewRecycled() {
        updateVisualizerState(
            VisualizerState.Hidden,
            UISettings.VisualizerMode.OFF,
            hasArtwork = true,
        )
    }

    /** Bind song artwork and fast-seek interaction without starting a per-holder coroutine. */
    fun bind(song: Song, listener: StepperOverlay.Listener) {
        binding.cover.bind(song)
        binding.coverFastSeekOverlay.listener = listener
    }

    fun updateVisualizerState(
        state: VisualizerState,
        visualizerMode: UISettings.VisualizerMode,
        hasArtwork: Boolean,
    ) {
        val shouldShow =
            state is VisualizerState.Live &&
                (visualizerMode == UISettings.VisualizerMode.ALWAYS ||
                    (visualizerMode == UISettings.VisualizerMode.FALLBACK && !hasArtwork))

        binding.coverVisualizer.updateState(if (shouldShow) state else VisualizerState.Hidden)
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
