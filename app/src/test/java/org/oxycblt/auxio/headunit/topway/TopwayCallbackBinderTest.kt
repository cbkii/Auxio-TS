/*
 * Copyright (c) 2026 Auxio Project
 * TopwayCallbackBinderTest.kt is part of Auxio.
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
import android.os.Parcel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
class TopwayCallbackBinderTest {
    @Test
    fun `music callback binder dispatches all four playback controls`() {
        val controls = mutableListOf<TopwayMusicControl>()
        val binder =
            TopwayMusicCallbackBinder(
                onControl = controls::add,
                onMode = {},
                onExtended = {},
            )

        for (code in 1..4) {
            transact(
                binder = binder,
                descriptor = TopwayCommandServiceContract.MUSIC_CALLBACK_DESCRIPTOR,
                code = code,
            )
        }

        assertEquals(
            listOf(
                TopwayMusicControl.NEXT,
                TopwayMusicControl.PREVIOUS,
                TopwayMusicControl.PLAY,
                TopwayMusicControl.PAUSE,
            ),
            controls,
        )
    }

    @Test
    fun `music callback binder unmarshals mode and extended bundle`() {
        var mode: Int? = null
        var extended: Bundle? = null
        val binder =
            TopwayMusicCallbackBinder(
                onControl = {},
                onMode = { mode = it },
                onExtended = { extended = it },
            )

        transact(
            binder = binder,
            descriptor = TopwayCommandServiceContract.MUSIC_CALLBACK_DESCRIPTOR,
            code = TopwayCommandServiceContract.MusicCallbackTransaction.MODE,
        ) { writeInt(7) }
        transact(
            binder = binder,
            descriptor = TopwayCommandServiceContract.MUSIC_CALLBACK_DESCRIPTOR,
            code = TopwayCommandServiceContract.MusicCallbackTransaction.EXTENDED_INTERFACE,
        ) {
            writeInt(1)
            Bundle().apply { putString("probe", "value") }.writeToParcel(this, 0)
        }

        assertEquals(7, mode)
        assertEquals("value", extended?.getString("probe"))
    }

    @Test
    fun `command callback binder unmarshals source response`() {
        var extended: Bundle? = null
        val binder =
            TopwayCommandCallbackBinder(
                onStatus = { _, _ -> },
                onExtended = { extended = it },
            )
        val sourceBundle =
            Bundle().apply {
                putString(
                    TopwayCommandServiceContract.EXTRA_ACTION,
                    TopwayCommandServiceContract.ACTION_SOURCE_RECEIVE,
                )
                putInt(
                    TopwayCommandServiceContract.EXTRA_SOURCE_VALUE,
                    TopwaySourceState.SOURCE_LOCAL_MUSIC,
                )
            }

        transact(
            binder = binder,
            descriptor = TopwayCommandServiceContract.COMMAND_CALLBACK_DESCRIPTOR,
            code = TopwayCommandServiceContract.CommandCallbackTransaction.EXTENDED_INTERFACE,
        ) {
            writeInt(1)
            sourceBundle.writeToParcel(this, 0)
        }

        val source = TopwayCommandServiceContract.parseSource(extended)
        assertEquals(TopwaySourceState.SOURCE_LOCAL_MUSIC, source?.value)
        assertEquals(TopwaySourceState.Kind.LOCAL_MUSIC, source?.kind)
    }

    private fun transact(
        binder: android.os.IBinder,
        descriptor: String,
        code: Int,
        writeArguments: Parcel.() -> Unit = {},
    ) {
        val data = Parcel.obtain()
        val reply = Parcel.obtain()
        try {
            data.writeInterfaceToken(descriptor)
            data.writeArguments()
            assertTrue(binder.transact(code, data, reply, 0))
            reply.readException()
        } finally {
            reply.recycle()
            data.recycle()
        }
    }
}
