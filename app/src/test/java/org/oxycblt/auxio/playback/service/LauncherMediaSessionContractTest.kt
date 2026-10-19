/*
 * Copyright (c) 2026 Auxio Project
 * LauncherMediaSessionContractTest.kt is part of Auxio.
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

package org.oxycblt.auxio.playback.service

import android.content.Context
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import androidx.test.core.app.ApplicationProvider
import coil3.ImageLoader
import java.lang.reflect.Proxy
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.oxycblt.auxio.ForegroundListener
import org.oxycblt.auxio.diagnostics.DiagnosticJournal
import org.oxycblt.auxio.headunit.topway.LauncherIntegrationTelemetry
import org.oxycblt.auxio.headunit.topway.TopwayLauncherIntegrationCoordinator
import org.oxycblt.auxio.image.BitmapProvider
import org.oxycblt.auxio.image.ImageSettingsImpl
import org.oxycblt.auxio.music.MusicRepository
import org.oxycblt.auxio.playback.state.DeferredPlayback
import org.oxycblt.auxio.playback.state.PlaybackCommand
import org.oxycblt.auxio.playback.state.PlaybackStateManager
import org.oxycblt.auxio.playback.state.Progression
import org.oxycblt.auxio.playback.state.RawPlaybackMetadata
import org.oxycblt.auxio.playback.state.RepeatMode
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/** Exercise the public transport callbacks and actual published session metadata used by ts-theme. */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
class LauncherMediaSessionContractTest {
    private lateinit var context: Context
    private lateinit var imageLoader: ImageLoader
    private lateinit var holder: MediaSessionHolder
    private lateinit var callback: MediaSessionInterface
    private lateinit var controller: MediaControllerCompat
    private var raw: RawPlaybackMetadata? = null
    private val commands = mutableListOf<Pair<String, Any?>>()

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        val journal = DiagnosticJournal()
        val telemetry = LauncherIntegrationTelemetry(context, journal)
        val manager =
            proxy<PlaybackStateManager> { method, args ->
                when (method) {
                    "getCurrentSong", "getParent", "getQueueWindow" -> null
                    "getRawPlaybackMetadata" -> raw
                    "getProgression" -> Progression.nil()
                    "getQueue" -> emptyList<Any>()
                    "getIndex" -> 0
                    "getRepeatMode" -> RepeatMode.NONE
                    "isShuffled" -> false
                    "playDeferred", "playing", "next", "prev", "seekTo" -> {
                        commands.add(method to args?.firstOrNull())
                        null
                    }
                    else -> null
                }
            }
        callback =
            MediaSessionInterface(
                context,
                manager,
                proxy<PlaybackCommand.Factory>(),
                proxy<MusicRepository>(),
                telemetry,
            )
        imageLoader = ImageLoader.Builder(context).build()
        holder =
            MediaSessionHolder.Factory(
                    manager,
                    BitmapProvider(context, imageLoader),
                    ImageSettingsImpl(context),
                    callback,
                    TopwayLauncherIntegrationCoordinator(context, journal),
                    telemetry,
                )
                .create(context, proxy<ForegroundListener>())
        controller = MediaControllerCompat(context, holder.token)
    }

    @After
    fun tearDown() {
        holder.release()
        imageLoader.shutdown()
    }

    @Test
    fun `cold prepare never plays and one Play requests one restore`() {
        callback.onPrepare()
        callback.onPlay()
        assertEquals(
            listOf(
                "playDeferred" to DeferredPlayback.RestoreState(play = false),
                "playDeferred" to
                    DeferredPlayback.RestoreState(
                        play = true,
                        fallback = DeferredPlayback.ShuffleAll(play = true),
                    ),
            ),
            commands,
        )
    }

    @Test
    fun `prepared raw controls use existing playback without another queue restore`() {
        raw = rawTrack()
        callback.onPrepare()
        callback.onPlay()
        callback.onPause()
        callback.onSkipToNext()
        callback.onSkipToPrevious()
        callback.onSeekTo(1200L)
        assertEquals(
            listOf(
                "playing" to true,
                "playing" to false,
                "next" to null,
                "prev" to null,
                "seekTo" to 1200L,
            ),
            commands,
        )
    }

    @Test
    fun `artwork settings retain raw title artist and identity until explicit clear`() {
        raw = rawTrack()
        holder.onRawPlaybackMetadataChanged(raw)
        val initial = controller.metadata
        assertEquals("Cold track", initial?.getString(MediaMetadataCompat.METADATA_KEY_TITLE))
        holder.onImageSettingsChanged()
        val refreshed = controller.metadata
        val keys =
            listOf(
                MediaMetadataCompat.METADATA_KEY_TITLE,
                MediaMetadataCompat.METADATA_KEY_ARTIST,
                MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE,
                MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE,
                MediaMetadataCompat.METADATA_KEY_MEDIA_ID,
            )
        for (key in keys) {
            assertEquals(key, initial?.getString(key), refreshed?.getString(key))
        }
        raw = null
        holder.onRawPlaybackMetadataChanged(null)
        assertEquals("", controller.metadata?.getString(MediaMetadataCompat.METADATA_KEY_TITLE))
    }

    private fun rawTrack() =
        RawPlaybackMetadata(
            title = "Cold track",
            artist = "Cold artist",
            album = "Cold album",
            uriString = "file:///storage/usbdisk0/track.flac",
            path = "/storage/usbdisk0/track.flac",
            durationMs = 10000L,
            positionMs = 800L,
            isPlaying = false,
            savedAtMs = 1L,
        )

    @Suppress("UNCHECKED_CAST")
    private inline fun <reified T> proxy(
        noinline invoke: (String, Array<out Any?>?) -> Any? = { _, _ -> null }
    ): T =
        Proxy.newProxyInstance(T::class.java.classLoader, arrayOf(T::class.java)) {
            instance, method, args ->
            when (method.name) {
                "hashCode" -> System.identityHashCode(instance)
                "equals" -> instance === args?.firstOrNull()
                "toString" -> T::class.java.simpleName
                else -> invoke(method.name, args)
            }
        } as T
}
