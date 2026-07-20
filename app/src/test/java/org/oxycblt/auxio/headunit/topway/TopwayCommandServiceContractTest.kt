/*
 * Copyright (c) 2026 Auxio Project
 * TopwayCommandServiceContractTest.kt is part of Auxio.
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

package org.oxycblt.auxio.headunit.topway

import android.os.Bundle
import android.view.KeyEvent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
class TopwayCommandServiceContractTest {
    @Test
    fun `verified command transactions remain exact`() {
        assertEquals(1, TopwayCommandServiceContract.CommandTransaction.REGISTER_COMMAND_CALLBACK)
        assertEquals(2, TopwayCommandServiceContract.CommandTransaction.UNREGISTER_COMMAND_CALLBACK)
        assertEquals(5, TopwayCommandServiceContract.CommandTransaction.REGISTER_MUSIC_CALLBACK)
        assertEquals(6, TopwayCommandServiceContract.CommandTransaction.UNREGISTER_MUSIC_CALLBACK)
        assertEquals(67, TopwayCommandServiceContract.CommandTransaction.EXTENDED_INTERFACE)
    }

    @Test
    fun `verified music callback transactions map to standard media keys`() {
        assertEquals(
            KeyEvent.KEYCODE_MEDIA_NEXT,
            TopwayMusicControl.fromTransaction(1)?.mediaKeyCode,
        )
        assertEquals(
            KeyEvent.KEYCODE_MEDIA_PREVIOUS,
            TopwayMusicControl.fromTransaction(2)?.mediaKeyCode,
        )
        assertEquals(
            KeyEvent.KEYCODE_MEDIA_PLAY,
            TopwayMusicControl.fromTransaction(3)?.mediaKeyCode,
        )
        assertEquals(
            KeyEvent.KEYCODE_MEDIA_PAUSE,
            TopwayMusicControl.fromTransaction(4)?.mediaKeyCode,
        )
        assertNull(TopwayMusicControl.fromTransaction(5))
    }

    @Test
    fun `source request preserves exact misspelled vendor response contract`() {
        val request = TopwayCommandServiceContract.sourceRequest()
        assertEquals(
            TopwayCommandServiceContract.PROJECT_SYSTEM,
            request.getString(TopwayCommandServiceContract.EXTRA_PROJECT),
        )
        assertEquals(
            TopwayCommandServiceContract.ACTION_SOURCE_REQUEST,
            request.getString(TopwayCommandServiceContract.EXTRA_ACTION),
        )
        assertEquals("Source_recieve", TopwayCommandServiceContract.ACTION_SOURCE_RECEIVE)
    }

    @Test
    fun `source response is classified without changing vendor state`() {
        fun source(value: Int) =
            TopwayCommandServiceContract.parseSource(
                Bundle().apply {
                    putString(
                        TopwayCommandServiceContract.EXTRA_ACTION,
                        TopwayCommandServiceContract.ACTION_SOURCE_RECEIVE,
                    )
                    putInt(TopwayCommandServiceContract.EXTRA_SOURCE_VALUE, value)
                }
            )

        assertEquals(TopwaySourceState.Kind.RADIO, source(1)?.kind)
        assertEquals(TopwaySourceState.Kind.LOCAL_MUSIC, source(3)?.kind)
        assertEquals(TopwaySourceState.Kind.BLUETOOTH, source(8)?.kind)
        assertEquals(TopwaySourceState.Kind.VIDEO, source(9)?.kind)
        assertEquals(TopwaySourceState.Kind.OTHER, source(42)?.kind)
        assertNull(TopwayCommandServiceContract.parseSource(Bundle()))
    }
}
