/*
 * Copyright (c) 2026 Auxio Project
 * ShuffleControlPolicy.kt is part of Auxio.
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

import org.oxycblt.auxio.playback.state.ShuffleScope

/**
 * Capability-based Now Playing shuffle control (`OFF → ALL → GENRE → OFF`).
 *
 * Whole-queue shuffle reorders the *existing* queue in place, so it needs only a queue owner: the
 * hydrated queue, the persisted primitive Fast Resume queue, or raw Fast Resume (which defers the
 * reorder to its hydration). Genre shuffle and leaving genre shuffle change queue membership, so
 * they need the hydrated current song and library.
 */
internal object ShuffleControlPolicy {
    enum class Action {
        /** Shuffle the existing queue in place (scope ALL). */
        SHUFFLE_QUEUE,
        /** Replace membership with the current song's genre overlap, around the current song. */
        GENRE_SHUFFLE,
        /** Restore the existing queue's unshuffled order in place (scope OFF). */
        UNSHUFFLE_QUEUE,
        /** Restore the playback context's unshuffled membership around the current song. */
        RESTORE_CONTEXT,
        /** No safe transition is available yet. */
        NONE,
    }

    data class Capability(
        /** A queue owner exists whose order can be changed without replacing the current item. */
        val canReorderQueue: Boolean,
        /** The hydrated current song has genre identity. */
        val canGenreShuffle: Boolean,
        /** The hydrated current song and library exist to rebuild the playback context. */
        val canRestoreContext: Boolean,
    )

    fun capability(
        hasHydratedSong: Boolean,
        hasPrimitivePlayback: Boolean,
        hydratedSongHasGenres: Boolean,
    ) =
        Capability(
            canReorderQueue = hasHydratedSong || hasPrimitivePlayback,
            canGenreShuffle = hasHydratedSong && hydratedSongHasGenres,
            canRestoreContext = hasHydratedSong,
        )

    fun next(scope: ShuffleScope, capability: Capability): Action =
        when (scope) {
            ShuffleScope.OFF ->
                if (capability.canReorderQueue) Action.SHUFFLE_QUEUE else Action.NONE
            ShuffleScope.ALL ->
                when {
                    capability.canGenreShuffle -> Action.GENRE_SHUFFLE
                    // Without genre data the cycle skips GENRE rather than leaving the control
                    // stuck in ALL.
                    capability.canReorderQueue -> Action.UNSHUFFLE_QUEUE
                    else -> Action.NONE
                }
            ShuffleScope.GENRE ->
                if (capability.canRestoreContext) Action.RESTORE_CONTEXT else Action.NONE
        }

    fun isEnabled(scope: ShuffleScope, capability: Capability) =
        next(scope, capability) != Action.NONE
}
