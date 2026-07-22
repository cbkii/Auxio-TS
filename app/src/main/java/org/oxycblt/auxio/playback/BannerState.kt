/*
 * Copyright (c) 2026 Auxio Project
 * BannerState.kt is part of Auxio.
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

import org.oxycblt.auxio.playback.state.RawPlaybackMetadata
import org.oxycblt.musikr.Song

/** Represents the persistent UI state of the playback banner. */
sealed interface BannerState {
    /** Whether the banner has a playable item without inventing playback state. */
    val playable: Boolean
        get() = this is Rich || this is Raw

    /** Whether queue-wide commands are known to be backed by a hydrated rich queue. */
    val richQueueCommandsAvailable: Boolean
        get() = this is Rich

    /** Rich library playback with a fully hydrated [Song]. */
    data class Rich(val song: Song) : BannerState

    /** Raw primitive playback active before library hydration, such as TS18 fast resume. */
    data class Raw(val metadata: RawPlaybackMetadata) : BannerState

    /** A previous playback session is currently being restored. */
    data object Restoring : BannerState

    /** No playback is active or queued. */
    data object Idle : BannerState

    /** Playback is unavailable. [reason] is diagnostic and must not be rendered directly. */
    data class Unavailable(val reason: String) : BannerState
}
