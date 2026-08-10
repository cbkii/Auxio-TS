/*
 * Copyright (c) 2026 Auxio Project
 * PlaybackPagerItem.kt is part of Auxio.
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

import org.oxycblt.auxio.playback.persist.QueueItemRef
import org.oxycblt.auxio.playback.queue.QueueDisplayItem
import org.oxycblt.auxio.playback.state.RawPlaybackMetadata
import org.oxycblt.musikr.Song

/**
 * Display-only item for the Now Playing pager.
 *
 * Raw and primitive entries deliberately remain primitive. They are never promoted into fake
 * [Song] instances or a second playback queue authority.
 */
sealed interface PlaybackPagerItem {
    /** Global logical queue position when this item is backed by the canonical queue. */
    val globalPosition: Int?

    /** Duration used only to configure the existing seek/visualizer presentation. */
    val durationMs: Long

    /** Rich song when Musikr hydration has completed. */
    val song: Song?

    /** Stable public identities that can bridge primitive/raw -> rich hydration. */
    val identityKeys: Set<String>

    /** Deterministic key for the visualizer's per-track render state. */
    val visualizerTrackKey: String
        get() = identityKeys.firstOrNull() ?: "pager:${globalPosition ?: -1}:$durationMs"

    data class Rich(override val globalPosition: Int, override val song: Song) : PlaybackPagerItem {
        override val durationMs: Long
            get() = song.durationMs

        override val identityKeys: Set<String>
            get() =
                buildSet {
                    add("uid:${song.uid}")
                    add("uri:${song.uri}")
                }
    }

    data class Primitive(
        override val globalPosition: Int,
        val item: QueueItemRef,
    ) : PlaybackPagerItem {
        override val durationMs: Long
            get() = item.durationMs
        override val song: Song? = null
        override val identityKeys: Set<String>
            get() =
                buildSet {
                    item.stableSongUid?.let { add("uid:$it") }
                    item.uri?.takeIf(String::isNotBlank)?.let { add("uri:$it") }
                    item.pathFallback?.takeIf(String::isNotBlank)?.let { add("path:$it") }
                    if (isEmpty()) add("primitive:$globalPosition:${item.durationMs}")
                }
    }

    data class Raw(val metadata: RawPlaybackMetadata) : PlaybackPagerItem {
        override val globalPosition: Int? = null
        override val durationMs: Long
            get() = metadata.durationMs
        override val song: Song? = null
        override val identityKeys: Set<String>
            get() =
                buildSet {
                    metadata.uriString.takeIf(String::isNotBlank)?.let { add("uri:$it") }
                    metadata.path?.takeIf(String::isNotBlank)?.let { add("path:$it") }
                    if (isEmpty()) {
                        add("raw:${metadata.displayTitle}:${metadata.durationMs}")
                    }
                }
    }

    companion object {
        /**
         * Identity comparison used by the pager diff. Queue positions disambiguate duplicate queue
         * entries while shared UID/URI/path identity lets an early item hydrate in place.
         */
        fun sameLogicalItem(old: PlaybackPagerItem, new: PlaybackPagerItem): Boolean {
            val oldPosition = old.globalPosition
            val newPosition = new.globalPosition
            if (oldPosition != null && newPosition != null && oldPosition != newPosition) {
                return false
            }
            return old.identityKeys.any(new.identityKeys::contains)
        }
    }
}

/** Pure projection from the established queue UI model plus the raw single-item fallback. */
object PlaybackPagerProjection {
    data class State(val items: List<PlaybackPagerItem>, val activeIndex: Int) {
        val hasPlayablePage: Boolean
            get() = activeIndex in items.indices
    }

    fun project(
        queue: List<QueueDisplayItem>,
        queueIndex: Int,
        rawMetadata: RawPlaybackMetadata?,
    ): State {
        if (queue.isNotEmpty()) {
            val activeGlobalPosition = queue.getOrNull(queueIndex)?.globalPosition
            val items =
                queue.mapNotNull { display ->
                    when {
                        display.song != null ->
                            PlaybackPagerItem.Rich(display.globalPosition, display.song)
                        display.primitive != null ->
                            PlaybackPagerItem.Primitive(display.globalPosition, display.primitive)
                        else -> null
                    }
                }
            val activeIndex =
                activeGlobalPosition
                    ?.let { global -> items.indexOfFirst { it.globalPosition == global } }
                    ?.takeIf { it >= 0 }
                    ?: items.indices.firstOrNull()
                    ?: -1
            return State(items, activeIndex)
        }

        if (rawMetadata != null) {
            return State(listOf(PlaybackPagerItem.Raw(rawMetadata)), 0)
        }

        return State(emptyList(), -1)
    }
}
