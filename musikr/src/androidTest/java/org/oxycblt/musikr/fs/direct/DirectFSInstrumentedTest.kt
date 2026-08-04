/*
 * Copyright (c) 2026 Auxio Project
 * DirectFSInstrumentedTest.kt is part of Auxio.
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

package org.oxycblt.musikr.fs.direct

import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import java.io.File as JavaFile
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.oxycblt.musikr.fs.File as MusicFile
import org.oxycblt.musikr.fs.Location

@RunWith(AndroidJUnit4::class)
class DirectFSInstrumentedTest {
    @Test
    fun deterministicTraversalVisitsEveryDirectoryExactlyOnce() = runBlocking {
        withTimeout(TEST_TIMEOUT_MS) {
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            val parent = requireNotNull(context.getExternalFilesDir(null))
            val root = JavaFile(parent, "directfs-queue-${System.nanoTime()}")
            assertTrue(root.mkdirs())
            val directoryCount = DIRECTORY_COUNT

            try {
                repeat(directoryCount) { index ->
                    val directory = JavaFile(root, "album-${index.toString().padStart(5, '0')}")
                    assertTrue(directory.mkdir())
                    assertTrue(JavaFile(directory, "track-$index.mp3").createNewFile())
                }

                val rootUri = Uri.fromFile(root)
                val location =
                    requireNotNull(Location.Unopened.from(context, rootUri).open(context))
                val output = Channel<MusicFile>(Channel.RENDEZVOUS)
                val directFs = DirectFS(listOf(location))
                val explorationCall = async { directFs.explore(output) }

                // Hold the consumer briefly so the rendezvous channel applies real
                // back-pressure to the coordinator before anything is drained.
                delay(250)
                val discovered = mutableListOf<MusicFile>()
                for (file in output) discovered += file

                val exploration = explorationCall.await()
                assertTrue(exploration.await().isSuccess)
                assertEquals(directoryCount, discovered.size)
                assertTrue(directFs.drainSourceFailures().isEmpty())

                val metrics = requireNotNull(directFs.lastTraversalMetrics())
                assertEquals(directoryCount + 1, metrics.directoriesVisited)
                assertEquals(directoryCount, metrics.filesEmitted)
                assertEquals(0, metrics.duplicateDirectoriesSuppressed)
                assertEquals(0, metrics.activeEnumerators)
                assertEquals(
                    listOf(SourceCompletion.COMPLETED),
                    metrics.results.map { it.completion },
                )
            } finally {
                root.deleteRecursively()
            }
        }
    }

    private companion object {
        const val TEST_TIMEOUT_MS = 60_000L
        const val DIRECTORY_COUNT = 512
    }
}
