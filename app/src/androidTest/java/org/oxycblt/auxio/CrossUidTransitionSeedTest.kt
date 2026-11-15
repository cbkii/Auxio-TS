/*
 * Copyright (c) 2026 Auxio Project
 * CrossUidTransitionSeedTest.kt is part of Auxio.
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

package org.oxycblt.auxio

import android.os.Build
import androidx.room.withTransaction
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.oxycblt.auxio.playback.persist.PersistenceRoomModule
import org.oxycblt.auxio.playback.persist.QueueItemRefEntity
import org.oxycblt.auxio.playback.persist.QueueSessionEntity
import org.oxycblt.auxio.playback.state.RepeatMode
import org.oxycblt.auxio.playback.state.ShuffleScope

/** Seeds only a fresh API29 emulator app; the real service/player performs every restore/skip. */
@RunWith(AndroidJUnit4::class)
class CrossUidTransitionSeedTest {
    @Test
    fun seedPrimitiveQueueForCrossUidProbe() = runBlocking {
        val arguments = InstrumentationRegistry.getArguments()
        org.junit.Assume.assumeTrue(arguments.getString("pair_seed") == "1")
        assertEquals(arguments.getString("expected_source_sha"), BuildConfig.BUILD_COMMIT)
        assertEquals(29, Build.VERSION.SDK_INT)
        assertTrue("Emulator required", Build.HARDWARE == "ranchu" || Build.HARDWARE == "goldfish")
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        assertTrue("Debug peer required", BuildConfig.DEBUG)
        val database = PersistenceRoomModule().database(context)
        try {
            val dao = database.queueDao()
            assertNull(
                "Use a fresh emulator installation; existing queues are protected",
                dao.getQueueSession(),
            )
            val rows =
                (0..2).map { index ->
                    val file = File(context.filesDir, "pair-transition-$index.wav")
                    val samples = 8000 * 20
                    val data = ByteBuffer.allocate(44 + samples * 2).order(ByteOrder.LITTLE_ENDIAN)
                    data
                        .put("RIFF".toByteArray())
                        .putInt(36 + samples * 2)
                        .put("WAVEfmt ".toByteArray())
                    data
                        .putInt(16)
                        .putShort(1)
                        .putShort(1)
                        .putInt(8000)
                        .putInt(16000)
                        .putShort(2)
                        .putShort(16)
                    data.put("data".toByteArray()).putInt(samples * 2)
                    repeat(samples) { data.putShort(if (it % (16 + index * 2) < 8) 800 else -800) }
                    file.writeBytes(data.array())
                    QueueItemRefEntity(
                        987L,
                        index,
                        index,
                        null,
                        android.net.Uri.fromFile(file).toString(),
                        file.path,
                        "Pair item $index",
                        "Pair fixture",
                        null,
                        20000L,
                    )
                }
            database.withTransaction {
                dao.insertQueueSession(
                    QueueSessionEntity(
                        987L,
                        0,
                        0L,
                        RepeatMode.NONE,
                        ShuffleScope.OFF,
                        rows.size,
                        1L,
                        1L,
                    )
                )
                dao.insertQueueItemRefs(rows)
            }
            assertEquals(3, dao.getAllQueueItems(987L).size)
        } finally {
            database.close()
        }
    }
}
