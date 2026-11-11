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
import android.media.MediaMetadata
import android.media.session.MediaSession
import android.media.session.PlaybackState
import android.os.Bundle
import android.os.SystemClock
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
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
import org.oxycblt.auxio.list.adapter.UpdateInstructions
import org.oxycblt.auxio.music.MusicRepository
import org.oxycblt.auxio.playback.state.DeferredPlayback
import org.oxycblt.auxio.playback.state.PlaybackCommand
import org.oxycblt.auxio.playback.state.PlaybackStateManager
import org.oxycblt.auxio.playback.state.Progression
import org.oxycblt.auxio.playback.state.QueueChange
import org.oxycblt.auxio.playback.state.RawPlaybackMetadata
import org.oxycblt.auxio.playback.state.RepeatMode
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.Implementation
import org.robolectric.annotation.Implements
import org.robolectric.shadow.api.Shadow
import org.robolectric.shadows.ShadowMediaSession
import org.robolectric.util.ReflectionHelpers

/**
 * Exercise the public transport callbacks and actual published session metadata used by ts-theme.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29], shadows = [MetadataPublishingSession::class])
class LauncherMediaSessionContractTest {
    private lateinit var context: Context
    private lateinit var imageLoader: ImageLoader
    private lateinit var holder: MediaSessionHolder
    private lateinit var callback: MediaSessionInterface
    private var raw: RawPlaybackMetadata? = null
    private var shuffled = false
    private var index = 6
    private val commands = mutableListOf<Pair<String, Any?>>()

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        val journal = DiagnosticJournal()
        val telemetry = LauncherIntegrationTelemetry(context, journal)
        val manager =
            proxy<PlaybackStateManager> { method, args ->
                when (method) {
                    "getCurrentSong",
                    "getParent",
                    "getQueueWindow" -> null
                    "getRawPlaybackMetadata" -> raw
                    "getProgression" -> Progression.nil()
                    "getQueue" -> emptyList<Any>()
                    "getIndex" -> index
                    "getRepeatMode" -> RepeatMode.NONE
                    "isShuffled" -> shuffled
                    "playDeferred",
                    "playing",
                    "next",
                    "prev",
                    "seekTo" -> {
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
        holder.attach()
    }

    @After
    fun tearDown() {
        holder.release()
        imageLoader.shutdown()
    }

    @Test
    fun `bounded HOME Play dispatches one cold restore with the original expiry`() {
        val deadline = SystemClock.elapsedRealtime() + MediaSessionInterface.HOME_PLAY_MAX_WINDOW_MS
        callback.onCustomAction(
            "org.oxycblt.auxio.action.HOME_PLAY_V1",
            Bundle().apply {
                putInt("protocol", 1)
                putLong("deadline_elapsed_ms", deadline)
            },
        )
        assertEquals(
            listOf(
                "playDeferred" to
                    DeferredPlayback.RestoreState(
                        play = true,
                        fallback = DeferredPlayback.ShuffleAll(play = true),
                        deadlineElapsedMs = deadline,
                    )
            ),
            commands,
        )
    }

    @Test
    fun `expired malformed and oversized HOME Play cannot resume a prepared track`() {
        raw = rawTrack()
        val now = SystemClock.elapsedRealtime()
        for (remaining in listOf(-1L, 0L, 6_100L, 7_700L)) {
            callback.onCustomAction(
                "org.oxycblt.auxio.action.HOME_PLAY_V1",
                Bundle().apply {
                    putInt("protocol", 1)
                    putLong("deadline_elapsed_ms", now + remaining)
                },
            )
        }
        callback.onCustomAction("org.oxycblt.auxio.action.HOME_PLAY_V1", null)
        assertEquals(emptyList<Pair<String, Any?>>(), commands)
        callback.onPlay()
        callback.onPause()
        assertEquals(listOf("playing" to true, "playing" to false), commands)
    }

    @Test
    fun `primitive shuffle notification updates the standard session mode`() {
        val session = ReflectionHelpers.getField<MediaSessionCompat>(holder, "mediaSession")
        shuffled = true
        holder.onQueueWindowChanged(null)
        assertEquals(
            android.support.v4.media.session.PlaybackStateCompat.SHUFFLE_MODE_ALL,
            session.controller.shuffleMode,
        )
        shuffled = false
        holder.onQueueWindowChanged(null)
        assertEquals(
            android.support.v4.media.session.PlaybackStateCompat.SHUFFLE_MODE_NONE,
            session.controller.shuffleMode,
        )
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
    fun `raw restore and natural next publish an actual current queue item without library hydration`() {
        raw = rawTrack()
        holder.onNewPlayback(null, emptyList(), index, false)
        assertRawQueue()
        // The primitive queue moves before the rich Song library is available.
        index = 7
        raw = rawTrack().copy(uriString = "file:///storage/usbdisk0/next.flac", title = "Next")
        holder.onRawPlaybackMetadataChanged(raw)
        assertRawQueue()
        holder.onQueueWindowChanged(null)
        assertRawQueue()
        holder.onQueueChanged(
            emptyList(),
            index,
            QueueChange(QueueChange.Type.MAPPING, UpdateInstructions.Diff),
        )
        assertRawQueue()
        index = 6
        raw = rawTrack()
        holder.onRawPlaybackMetadataChanged(raw)
        assertRawQueue()
        // Losing current identity may not advertise a phantom queue index.
        raw = null
        holder.onRawPlaybackMetadataChanged(null)
        val publication = publication()
        assertEquals(0, publication.publishedQueue?.size ?: 0)
        assertEquals(
            MediaSession.QueueItem.UNKNOWN_ID.toLong(),
            publication.publishedState?.activeQueueItemId,
        )
    }

    private fun assertRawQueue() {
        val publication = publication()
        val queue = requireNotNull(publication.publishedQueue)
        assertEquals(1, queue.size)
        assertEquals(index.toLong(), queue.single().queueId)
        assertEquals(index.toLong(), publication.publishedState?.activeQueueItemId)
        assertEquals(raw?.uriString, queue.single().description.mediaId)
        assertEquals(
            raw?.uriString,
            publishedMetadata().getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID),
        )
    }

    private fun publication(): MetadataPublishingSession {
        val compat = ReflectionHelpers.getField<MediaSessionCompat>(holder, "mediaSession")
        return Shadow.extract(compat.mediaSession as MediaSession)
    }

    @Test
    fun `artwork settings retain raw title artist and identity until explicit clear`() {
        raw = rawTrack()
        holder.onRawPlaybackMetadataChanged(raw)
        val initial = publishedMetadata()
        assertEquals("Cold track", initial.getString(MediaMetadataCompat.METADATA_KEY_TITLE))
        holder.onImageSettingsChanged()
        val refreshed = publishedMetadata()
        val keys =
            listOf(
                MediaMetadataCompat.METADATA_KEY_TITLE,
                MediaMetadataCompat.METADATA_KEY_ARTIST,
                MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE,
                MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE,
                MediaMetadataCompat.METADATA_KEY_MEDIA_ID,
            )
        for (key in keys) {
            assertEquals(key, initial.getString(key), refreshed.getString(key))
        }
        raw = null
        holder.onRawPlaybackMetadataChanged(null)
        assertEquals("", publishedMetadata().getString(MediaMetadataCompat.METADATA_KEY_TITLE))
    }

    private fun publishedMetadata(): MediaMetadataCompat {
        val compat = ReflectionHelpers.getField<MediaSessionCompat>(holder, "mediaSession")
        val framework = compat.mediaSession as MediaSession
        val publishingSession = Shadow.extract<MetadataPublishingSession>(framework)
        return requireNotNull(
            MediaMetadataCompat.fromMediaMetadata(publishingSession.publishedMetadata)
        )
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
            instance,
            method,
            args ->
            when (method.name) {
                "hashCode" -> System.identityHashCode(instance)
                "equals" -> instance === args?.firstOrNull()
                "toString" -> T::class.java.simpleName
                else -> invoke(method.name, args)
            }
        } as T
}

/**
 * Capture the outbound framework publication; Robolectric controllers do not mirror session IPC.
 */
@Implements(MediaSession::class)
class MetadataPublishingSession : ShadowMediaSession() {
    var publishedMetadata: MediaMetadata? = null
        private set

    var publishedQueue: List<MediaSession.QueueItem>? = null
        private set

    var publishedState: PlaybackState? = null
        private set

    @Implementation
    protected fun setQueue(queue: List<MediaSession.QueueItem>?) {
        publishedQueue = queue
    }

    @Implementation
    protected fun setPlaybackState(state: PlaybackState?) {
        publishedState = state
    }

    @Implementation
    protected fun setMetadata(metadata: MediaMetadata?) {
        publishedMetadata = metadata
    }
}
