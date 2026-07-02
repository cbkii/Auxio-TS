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

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.oxycblt.auxio.databinding.ItemCoverBinding
import org.oxycblt.auxio.list.adapter.FlexibleListAdapter
import org.oxycblt.auxio.list.adapter.SimpleDiffCallback
import org.oxycblt.auxio.playback.ui.stepper.StepperOverlay
import org.oxycblt.auxio.ui.UISettings
import org.oxycblt.auxio.util.inflater
import org.oxycblt.musikr.Song

/**
 * A [FlexibleListAdapter] that hosts [CoverViewHolder]s containing a [Song]'s cover and step
 * gesture overlays.
 *
 * @param listener The [StepperOverlay.Listener] that step gesture events will be forwarded to
 * @author Alexander Capehart (OxygenCobalt)
 */
class CoverPagerAdapter(private val listener: StepperOverlay.Listener) :
    FlexibleListAdapter<Song, CoverViewHolder>(CoverViewHolder.DIFF_CALLBACK) {

    var visualizerMode: UISettings.VisualizerMode = UISettings.VisualizerMode.OFF
        set(value) {
            val old = field
            field = value
            if (old != value) {
                notifyDataSetChanged()
            }
        }

    var audioSessionId: Int? = null
        set(value) {
            val old = field
            field = value
            if (old != value) {
                notifyDataSetChanged()
            }
        }

    var isVisualizerActive: Boolean = true
        set(value) {
            // We just update the children directly if possible, or trigger a rebind.
            val old = field
            field = value
            if (old != value) {
                notifyDataSetChanged()
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, pos: Int) = CoverViewHolder.from(parent)

    override fun onBindViewHolder(viewHolder: CoverViewHolder, pos: Int) {
        viewHolder.bind(
            currentList[pos],
            listener,
            visualizerMode,
            audioSessionId,
            isVisualizerActive,
        )
    }

    override fun onViewRecycled(holder: CoverViewHolder) {
        super.onViewRecycled(holder)
        holder.releaseVisualizer()
    }
}

/**
 * A [RecyclerView.ViewHolder] that displays a [Song]'s cover and step gesture overlays.
 *
 * @author Alexander Capehart (OxygenCobalt)
 */
class CoverViewHolder private constructor(private val binding: ItemCoverBinding) :
    RecyclerView.ViewHolder(binding.root) {
    /**
     * Bind new data to this instance.
     *
     * @param song The new [Song] to bind.
     * @param listener An [StepperOverlay.Listener] to bind fast seek interactions to.
     */
    fun bind(
        song: Song,
        listener: StepperOverlay.Listener,
        visualizerMode: UISettings.VisualizerMode,
        audioSessionId: Int?,
        isVisualizerActive: Boolean,
    ) {
        binding.coverFastSeekOverlay.listener = listener

        val showVisualizer =
            when (visualizerMode) {
                UISettings.VisualizerMode.OFF -> false
                UISettings.VisualizerMode.ALWAYS -> true
                UISettings.VisualizerMode.FALLBACK -> song.cover == null
            }

        if (showVisualizer) {
            binding.cover.visibility = View.GONE
            binding.visualizer.visibility = View.VISIBLE
            binding.visualizer.setAudioSessionId(audioSessionId ?: 0)
            binding.visualizer.enableVisualizer(isVisualizerActive)
        } else {
            binding.cover.visibility = View.VISIBLE
            binding.visualizer.visibility = View.GONE
            binding.visualizer.enableVisualizer(false)
            binding.cover.bind(song)
        }
    }

    fun releaseVisualizer() {
        binding.visualizer.release()
    }

    companion object {
        /**
         * Create a new instance.
         *
         * @param parent The parent to inflate this instance from.
         * @return A new instance.
         */
        fun from(parent: ViewGroup) =
            CoverViewHolder(ItemCoverBinding.inflate(parent.context.inflater, parent, false))

        /** A comparator that can be used with DiffUtil. */
        val DIFF_CALLBACK =
            object : SimpleDiffCallback<Song>() {
                override fun areContentsTheSame(oldItem: Song, newItem: Song) =
                    oldItem.cover == newItem.cover
            }
    }
}
