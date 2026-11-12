/*
 * Copyright (c) 2026 Auxio Project
 * PlaybackViewModelShuffleTest.kt is part of Auxio.
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

import android.os.Looper
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Job
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.oxycblt.auxio.playback.state.PlaybackCommand
import org.oxycblt.auxio.playback.state.PlaybackStateManager
import org.oxycblt.auxio.playback.state.Progression
import org.oxycblt.auxio.playback.state.RawPlaybackMetadata
import org.oxycblt.auxio.playback.state.RepeatMode
import org.oxycblt.auxio.playback.state.RestoreOutcome
import org.oxycblt.auxio.playback.state.ShuffleMode
import org.oxycblt.auxio.playback.state.ShuffleScope
import org.oxycblt.musikr.Genre
import org.oxycblt.musikr.Music
import org.oxycblt.musikr.MusicParent
import org.oxycblt.musikr.Playlist
import org.oxycblt.musikr.Song
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.LooperMode
import org.robolectric.util.ReflectionHelpers

/**
 * Drives the real [PlaybackViewModel] shuffle cycle against a recording playback manager. Every
 * transition must keep the decoding item: no new playback command, prepare or seek is permitted.
 */
@RunWith(RobolectricTestRunner::class)
@LooperMode(LooperMode.Mode.PAUSED)
class PlaybackViewModelShuffleTest {
    @Test
    fun primitiveFastResumeEnablesWholeQueueShuffleInPlace() {
        val manager = RecordingManager(raw = rawMetadata())
        val model = newModel(manager)

        assertTrue(model.isShuffleControlEnabled())
        model.cycleShuffleScope()

        assertEquals(listOf("shuffled(true)"), manager.calls)
    }

    @Test
    fun restoredGenreShuffleWaitsForHydrationBeforeLeavingGenre() {
        val manager = RecordingManager(raw = rawMetadata(), scope = ShuffleScope.GENRE)
        val model = newModel(manager)

        assertFalse(model.isShuffleControlEnabled())
        model.cycleShuffleScope()
        assertTrue(manager.calls.isEmpty())
    }

    @Test
    fun allToGenreReplacesMembershipAroundTheCurrentSong() {
        val genre = genre()
        val current = song(1, genre)
        val others = (2..5).map { song(it, genre) }
        genre.songs += listOf(current) + others
        val manager = RecordingManager(song = current, scope = ShuffleScope.ALL)
        val model = newModel(manager)

        assertTrue(model.isShuffleControlEnabled())
        model.cycleShuffleScope()
        drainUntil { manager.calls.isNotEmpty() }

        assertEquals(listOf("replace(shuffled=true,scope=GENRE)"), manager.calls)
        assertEquals(current.uid, manager.replacedQueue.first().uid)
        assertEquals(5, manager.replacedQueue.size)
    }

    @Test
    fun staleGenreSelectionIsDiscardedAfterTheScopeChanges() {
        val genre = genre()
        val current = song(1, genre)
        genre.songs += listOf(current, song(2, genre))
        val manager = RecordingManager(song = current, scope = ShuffleScope.ALL)
        val model = newModel(manager)

        // A MediaSession shuffle (or new playback) lands while the selection is being computed.
        // Changing the scope from inside the computation makes the race deterministic whatever
        // thread the selection resumes on.
        genre.onSongsRead = { manager.scope = ShuffleScope.OFF }

        model.cycleShuffleScope()
        drainUntil {
            ReflectionHelpers.getField<Job?>(model, "genreShuffleJob")?.isCompleted != false
        }

        assertEquals(ShuffleScope.OFF, manager.scope)
        assertTrue("Stale selection was applied: ${manager.calls}", manager.calls.isEmpty())
    }

    @Test
    fun genreToOffRestoresThePlaylistContextInPlace() {
        val genre = genre()
        val current = song(1, genre)
        val playlistSongs = listOf(song(7, genre), current, song(8, genre))
        val playlist = playlist()
        val manager =
            RecordingManager(song = current, scope = ShuffleScope.GENRE, parent = playlist)
        val factory = RecordingFactory(playlistQueue = playlistSongs)
        val model = newModel(manager, factory)

        model.cycleShuffleScope()

        assertEquals(listOf("songFromPlaylist(OFF)"), factory.calls)
        assertEquals(listOf("replace(shuffled=false,scope=OFF)"), manager.calls)
        assertEquals(playlistSongs.map { it.uid }, manager.replacedQueue.map { it.uid })
    }

    @Test
    fun unresolvableContextNeverFallsBackToAllSongs() {
        val genre = genre()
        val current = song(1, genre)
        val manager =
            RecordingManager(
                song = current,
                scope = ShuffleScope.GENRE,
                parent = playlist(),
                acceptReplace = false,
            )
        val factory = RecordingFactory(playlistQueue = listOf(song(7, genre)))
        val model = newModel(manager, factory)

        model.cycleShuffleScope()

        assertFalse(factory.calls.contains("songFromAll(OFF)"))
        assertEquals(listOf("shuffled(false)"), manager.calls)
    }

    @Test
    fun refusedContextRestoreKeepsGenreInsteadOfDroppingTheContext() {
        val genre = genre()
        val current = song(1, genre)
        val playlistSongs = listOf(song(7, genre), current)
        val manager =
            RecordingManager(
                song = current,
                scope = ShuffleScope.GENRE,
                parent = playlist(),
                acceptReplace = false,
            )
        val model = newModel(manager, RecordingFactory(playlistQueue = playlistSongs))

        model.cycleShuffleScope()

        // Only the seamless install was attempted: no unshuffle that would discard the context.
        assertEquals(listOf("replace(shuffled=false,scope=OFF)"), manager.calls)
    }

    @Test
    fun newPlaybackOfTheSameSongDiscardsAPendingGenreSelection() {
        val genre = genre()
        val current = song(1, genre)
        genre.songs += listOf(current, song(2, genre), song(3, genre))
        val manager = RecordingManager(song = current, scope = ShuffleScope.ALL)
        val model = newModel(manager)
        // New playback of the same song, still in ALL, lands while the selection is computed.
        genre.onSongsRead = { model.onNewPlayback(null, listOf(current), 0, true) }

        model.cycleShuffleScope()
        drainUntil {
            ReflectionHelpers.getField<Job?>(model, "genreShuffleJob")?.isCompleted != false
        }

        assertTrue("Stale selection was applied: ${manager.calls}", manager.calls.isEmpty())
    }

    @Test
    fun songWithoutGenresSkipsGenreRatherThanStickingInAll() {
        val manager = RecordingManager(song = song(1), scope = ShuffleScope.ALL)
        val model = newModel(manager)

        model.cycleShuffleScope()

        assertEquals(listOf("shuffled(false)"), manager.calls)
    }

    private fun drainUntil(done: () -> Boolean) {
        val deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(5)
        while (!done() && System.nanoTime() < deadline) {
            shadowOf(Looper.getMainLooper()).idle()
            Thread.yield()
        }
        assertTrue("ViewModel did not finish within test deadline", done())
    }

    private fun newModel(
        manager: RecordingManager,
        factory: RecordingFactory = RecordingFactory(),
    ) =
        PlaybackViewModel(
            playbackManager = manager.proxy,
            playbackSettings =
                proxy { method, _ ->
                    if (method.name == "getBarAction") ActionMode.NEXT else defaultValue(method)
                },
            commandFactory = factory.proxy,
            listSettings = proxy(),
        )

    private class RecordingManager(
        val song: Song? = null,
        val raw: RawPlaybackMetadata? = null,
        @Volatile var scope: ShuffleScope = ShuffleScope.OFF,
        val parent: MusicParent? = null,
        val acceptReplace: Boolean = true,
    ) {
        val calls = java.util.Collections.synchronizedList(mutableListOf<String>())
        var replacedQueue: List<Song> = emptyList()

        val proxy: PlaybackStateManager = proxy { method, args ->
            when (method.name) {
                "getProgression" ->
                    Progression.from(isPlaying = true, isAdvancing = true, positionMs = 4000)
                "getRepeatMode" -> RepeatMode.NONE
                "getParent" -> parent
                "getCurrentSong" -> song
                "getRawPlaybackMetadata" -> raw
                "getRestoreOutcome" -> RestoreOutcome.NOT_REQUESTED
                "getQueue" -> listOfNotNull(song)
                "getIndex" -> 0
                "isShuffled" -> scope != ShuffleScope.OFF
                "getShuffleScope" -> scope
                "shuffled" -> {
                    calls += "shuffled(${args!![0]})"
                    null
                }
                "replaceQueuePreservingCurrent" -> {
                    @Suppress("UNCHECKED_CAST")
                    replacedQueue = args!![0] as List<Song>
                    calls += "replace(shuffled=${args[1]},scope=${args[2]})"
                    acceptReplace
                }
                // Any new playback, seek or deferred playback would restart the current source.
                "play",
                "seekTo",
                "playDeferred" -> {
                    calls += method.name
                    null
                }
                else -> defaultValue(method)
            }
        }
    }

    private class RecordingFactory(val playlistQueue: List<Song> = emptyList()) {
        val calls = mutableListOf<String>()
        val proxy: PlaybackCommand.Factory = proxy { method, args ->
            when (method.name) {
                "songFromPlaylist" -> {
                    calls += "songFromPlaylist(${args!![2]})"
                    command(playlistQueue, args[1] as MusicParent)
                }
                "songFromAll" -> {
                    calls += "songFromAll(${args!![1] as ShuffleMode})"
                    null
                }
                else -> defaultValue(method)
            }
        }

        private fun command(queue: List<Song>, parent: MusicParent): PlaybackCommand =
            proxy { method, _ ->
                when (method.name) {
                    "getQueue" -> queue
                    "getParent" -> parent
                    else -> defaultValue(method)
                }
            }
    }

    private class FakeGenre {
        val songs = mutableListOf<Song>()
        @Volatile var onSongsRead: () -> Unit = {}
        val proxy: Genre = proxy { method, _ ->
            when (method.name) {
                "getSongs" -> songs.also { onSongsRead() }
                else -> defaultValue(method)
            }
        }
    }

    private companion object {
        fun genre(): GenreHandle = GenreHandle(FakeGenre())

        class GenreHandle(private val fake: FakeGenre) {
            val proxy: Genre
                get() = fake.proxy

            val songs: MutableList<Song>
                get() = fake.songs

            var onSongsRead: () -> Unit
                get() = fake.onSongsRead
                set(value) {
                    fake.onSongsRead = value
                }
        }

        fun song(id: Int, genre: GenreHandle? = null): Song {
            val uid = uid('s', id)
            val songGenres = listOfNotNull(genre?.proxy)
            return proxy { method, _ ->
                when (method.name) {
                    "getUid" -> uid
                    "getGenres" -> songGenres
                    else -> defaultValue(method)
                }
            }
        }

        fun playlist(): Playlist {
            val uid = uid('p', 1)
            return proxy { method, _ ->
                when (method.name) {
                    "getUid" -> uid
                    else -> defaultValue(method)
                }
            }
        }

        fun uid(item: Char, id: Int): Music.UID =
            requireNotNull(
                Music.UID.fromString("ua${item}00000000-0000-0000-0000-${"%012d".format(id)}")
            )

        fun rawMetadata() =
            RawPlaybackMetadata(
                title = "Fast song",
                artist = "Fast artist",
                album = "Fast album",
                uriString = "file:///storage/usbdisk0/fast.flac",
                path = "/storage/usbdisk0/fast.flac",
                durationMs = 180_000L,
                positionMs = 4000L,
                isPlaying = true,
                savedAtMs = 1L,
            )

        @Suppress("UNCHECKED_CAST")
        inline fun <reified T> proxy(
            noinline invocation: (Method, Array<out Any?>?) -> Any? = { method, _ ->
                defaultValue(method)
            }
        ): T =
            Proxy.newProxyInstance(T::class.java.classLoader, arrayOf(T::class.java)) {
                instance,
                method,
                args ->
                when (method.name) {
                    "toString" -> "${T::class.java.simpleName}TestProxy"
                    "hashCode" -> System.identityHashCode(instance)
                    "equals" -> instance === args?.firstOrNull()
                    else -> invocation(method, args)
                }
            } as T

        fun defaultValue(method: Method): Any? =
            when {
                method.returnType == java.lang.Boolean.TYPE -> false
                method.returnType == java.lang.Integer.TYPE -> 0
                method.returnType == java.lang.Long.TYPE -> 0L
                method.returnType == java.lang.Float.TYPE -> 0f
                method.returnType == java.lang.Double.TYPE -> 0.0
                method.returnType == java.lang.Void.TYPE -> null
                method.returnType.isEnum -> method.returnType.enumConstants?.firstOrNull()
                List::class.java.isAssignableFrom(method.returnType) -> emptyList<Any>()
                else -> null
            }
    }
}
