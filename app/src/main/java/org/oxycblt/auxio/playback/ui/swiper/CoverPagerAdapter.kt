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
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.oxycblt.auxio.databinding.ItemCoverBinding
import org.oxycblt.auxio.list.adapter.FlexibleListAdapter
import org.oxycblt.auxio.list.adapter.SimpleDiffCallback
import org.oxycblt.auxio.playback.PlaybackViewModel
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
class CoverPagerAdapter(
    private val listener: StepperOverlay.Listener,
    private val playbackModel: PlaybackViewModel,
    private val uiSettings: UISettings,
) : FlexibleListAdapter<Song, CoverViewHolder>(CoverViewHolder.DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, pos: Int) = CoverViewHolder.from(parent)

    override fun onBindViewHolder(viewHolder: CoverViewHolder, pos: Int) {
        viewHolder.bind(currentList[pos], listener, playbackModel, uiSettings)
    }
}

/**
 * A [RecyclerView.ViewHolder] that displays a [Song]'s cover and step gesture overlays.
 *
 * @author Alexander Capehart (OxygenCobalt)
 */
class CoverViewHolder private constructor(private val binding: ItemCoverBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private var fftJob: Job? = null

    /**
     * Bind new data to this instance.
     *
     * @param song The new [Song] to bind.
     * @param listener An [StepperOverlay.Listener] to bind fast seek interactions to.
     */
    fun onViewRecycled() {
        fftJob?.cancel()
        fftJob = null
        binding.coverVisualizer.updateFft(null)
    }

    fun bind(
        song: Song,
        listener: StepperOverlay.Listener,
        playbackModel: PlaybackViewModel,
        uiSettings: UISettings,
    ) {
        binding.cover.bind(song)
        binding.coverFastSeekOverlay.listener = listener

        fftJob?.cancel()

        val lifecycleOwner = itemView.findViewTreeLifecycleOwner()
        if (lifecycleOwner != null) {
            val visualizerMode = uiSettings.visualizerMode
            val hasArtwork = song.cover != null
            val hasPermission = androidx.core.content.ContextCompat.checkSelfPermission(binding.root.context, android.Manifest.permission.RECORD_AUDIO) == android.content.pm.PackageManager.PERMISSION_GRANTED
            val shouldShowVisualizer = hasPermission && (
                visualizerMode == UISettings.VisualizerMode.ALWAYS ||
                    (visualizerMode == UISettings.VisualizerMode.FALLBACK && !hasArtwork))

            if (shouldShowVisualizer) {
                binding.coverVisualizer.visibility = View.VISIBLE
                binding.cover.visibility = View.INVISIBLE
                fftJob =
                    lifecycleOwner.lifecycleScope.launch {
                        playbackModel.visualizerFft.collect { bytes ->
                            binding.coverVisualizer.updateFft(bytes)
                        }
                    }
            } else {
                binding.coverVisualizer.updateFft(null)
                binding.coverVisualizer.visibility = View.GONE
                binding.cover.visibility = View.VISIBLE
            }
        }
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
