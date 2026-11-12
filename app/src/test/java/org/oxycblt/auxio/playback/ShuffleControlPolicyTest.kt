/*
 * Copyright (c) 2026 Auxio Project
 * ShuffleControlPolicyTest.kt is part of Auxio.
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

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.oxycblt.auxio.playback.ShuffleControlPolicy.Action
import org.oxycblt.auxio.playback.state.ShuffleScope

class ShuffleControlPolicyTest {
    private val idle =
        ShuffleControlPolicy.capability(
            hasHydratedSong = false,
            hasPrimitivePlayback = false,
            hydratedSongHasGenres = false,
        )
    private val primitive =
        ShuffleControlPolicy.capability(
            hasHydratedSong = false,
            hasPrimitivePlayback = true,
            hydratedSongHasGenres = false,
        )
    private val hydrated =
        ShuffleControlPolicy.capability(
            hasHydratedSong = true,
            hasPrimitivePlayback = false,
            hydratedSongHasGenres = true,
        )
    private val hydratedNoGenre =
        ShuffleControlPolicy.capability(
            hasHydratedSong = true,
            hasPrimitivePlayback = false,
            hydratedSongHasGenres = false,
        )

    @Test
    fun primitiveFastResumeCanShuffleTheWholePersistedQueue() {
        assertTrue(ShuffleControlPolicy.isEnabled(ShuffleScope.OFF, primitive))
        assertEquals(Action.SHUFFLE_QUEUE, ShuffleControlPolicy.next(ShuffleScope.OFF, primitive))
        assertEquals(Action.UNSHUFFLE_QUEUE, ShuffleControlPolicy.next(ShuffleScope.ALL, primitive))
    }

    @Test
    fun genreTransitionsWaitForTheHydratedSong() {
        assertFalse(ShuffleControlPolicy.isEnabled(ShuffleScope.GENRE, primitive))
        assertEquals(Action.NONE, ShuffleControlPolicy.next(ShuffleScope.GENRE, primitive))
        assertEquals(
            Action.RESTORE_CONTEXT,
            ShuffleControlPolicy.next(ShuffleScope.GENRE, hydrated),
        )
    }

    @Test
    fun hydratedPlaybackKeepsTheOffAllGenreCycle() {
        assertEquals(Action.SHUFFLE_QUEUE, ShuffleControlPolicy.next(ShuffleScope.OFF, hydrated))
        assertEquals(Action.GENRE_SHUFFLE, ShuffleControlPolicy.next(ShuffleScope.ALL, hydrated))
        assertEquals(
            Action.RESTORE_CONTEXT,
            ShuffleControlPolicy.next(ShuffleScope.GENRE, hydrated),
        )
        for (scope in ShuffleScope.values()) {
            assertTrue(ShuffleControlPolicy.isEnabled(scope, hydrated))
        }
    }

    @Test
    fun missingGenreSkipsGenreInsteadOfStickingInAll() {
        assertEquals(
            Action.UNSHUFFLE_QUEUE,
            ShuffleControlPolicy.next(ShuffleScope.ALL, hydratedNoGenre),
        )
    }

    @Test
    fun noPlaybackDisablesTheControl() {
        for (scope in ShuffleScope.values()) {
            assertFalse(ShuffleControlPolicy.isEnabled(scope, idle))
        }
    }
}
