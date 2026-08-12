/*
 * Copyright (c) 2021 Auxio Project
 * MediaSessionHolder.kt is part of Auxio.
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

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.annotation.DrawableRes
import androidx.car.app.mediaextensions.MetadataExtras
import androidx.core.app.NotificationCompat
import androidx.media.app.NotificationCompat.MediaStyle
import androidx.media.session.MediaButtonReceiver as AndroidXMediaButtonReceiver
import androidx.preference.PreferenceManager
import coil3.size.Size
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject
import org.oxycblt.auxio.AuxioService
import org.oxycblt.auxio.BuildConfig
import org.oxycblt.auxio.ForegroundListener
import org.oxycblt.auxio.ForegroundServiceNotification
import org.oxycblt.auxio.IntegerTable
import org.oxycblt.auxio.R
import org.oxycblt.auxio.diagnostics.DiagnosticJournal
import org.oxycblt.auxio.headunit.compat.HeadUnitMetadataPolicy
import org.oxycblt.auxio.headunit.topway.LauncherIntegrationTelemetry
import org.oxycblt.auxio.headunit.topway.TopwayLauncherIntegrationCoordinator
import org.oxycblt.auxio.headunit.topway.TopwayServiceBridge
import org.oxycblt.auxio.headunit.topway.Ts18LauncherIntegrationMode
import org.oxycblt.auxio.image.BitmapProvider
import org.oxycblt.auxio.image.CoverProvider
import org.oxycblt.auxio.image.ImageSettings
import org.oxycblt.auxio.music.resolve
import org.oxycblt.auxio.music.resolveNames
import org.oxycblt.auxio.music.service.MediaSessionUID
import org.oxycblt.auxio.music.service.toMediaDescription
import org.oxycblt.auxio.playback.state.PlaybackStateManager
import org.oxycblt.auxio.playback.state.Progression
import org.oxycblt.auxio.playback.state.QueueChange
import org.oxycblt.auxio.playback.state.RawPlaybackMetadata
import org.oxycblt.auxio.playback.state.RepeatMode
import org.oxycblt.auxio.util.NotificationBitmapSafety
import org.oxycblt.auxio.util.PerfTimer
import org.oxycblt.auxio.util.newBroadcastPendingIntent
import org.oxycblt.auxio.util.newNowPlayingPendingIntent
import org.oxycblt.musikr.MusicParent
import org.oxycblt.musikr.Song
import timber.log.Timber as L

/**
 * A component that mirrors the current playback state into the [MediaSessionCompat] and
 * [PlaybackNotification].
 *
 * @author Alexander Capehart (OxygenCobalt)
 */
class MediaSessionHolder
private constructor(
    private val context: Context,
    private val foregroundListener: ForegroundListener,
    private val playbackManager: PlaybackStateManager,
    private val bitmapProvider: BitmapProvider,
    private val imageSettings: ImageSettings,
    private val mediaSessionInterface: MediaSessionInterface,
    private val launcherCoordinator: TopwayLauncherIntegrationCoordinator,
    private val launcherTelemetry: LauncherIntegrationTelemetry,
) : PlaybackStateManager.Listener, ImageSettings.Listener {

    class Factory
    @Inject
    constructor(
        private val playbackManager: PlaybackStateManager,
        private val bitmapProvider: BitmapProvider,
        private val imageSettings: ImageSettings,
        private val mediaSessionInterface: MediaSessionInterface,
        private val launcherCoordinator: TopwayLauncherIntegrationCoordinator,
        private val launcherTelemetry: LauncherIntegrationTelemetry,
    ) {
        fun create(context: Context, foregroundListener: ForegroundListener) =
            MediaSessionHolder(
                context,
                foregroundListener,
                playbackManager,
                bitmapProvider,
                imageSettings,
                mediaSessionInterface,
                launcherCoordinator,
                launcherTelemetry,
            )
    }

    private val mainHandler = Handler(Looper.getMainLooper())
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    private val canonicalServiceClass =
        TopwayServiceBridge.resolveCompatServiceClass(AuxioService::class.java)
    private val mediaButtonReceiver = MediaButtonIntentFactory.receiverComponent(context)
    private val mediaButtonReceiverIntent =
        PendingIntent.getBroadcast(
            context,
            0,
            Intent(Intent.ACTION_MEDIA_BUTTON).setComponent(mediaButtonReceiver),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )
    private val mediaSession =
        MediaSessionCompat(
            context,
            context.packageName,
            mediaButtonReceiver,
            mediaButtonReceiverIntent,
        )
    val token: MediaSessionCompat.Token
        get() = mediaSession.sessionToken

    private val artworkRequestToken = AtomicLong()

    private val _notification =
        PlaybackNotification(context, mediaSession.sessionToken, canonicalServiceClass) {
            DofunMediaCompatPolicy.notificationProfile(
                launcherCoordinator.mode,
                BuildConfig.TOPWAY_COMPAT_ENABLED,
            )
        }
    val notification: ForegroundServiceNotification
        get() = _notification

    private var attached = false
    private var lastReportedSessionActive: Boolean? = null
    private var lastLauncherMode = launcherCoordinator.mode
    private val modePreferenceListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key != Ts18LauncherIntegrationMode.PREF_KEY) return@OnSharedPreferenceChangeListener
            mainHandler.post {
                if (!attached) return@post
                val previousMode = lastLauncherMode
                val newMode = launcherCoordinator.mode
                lastLauncherMode = newMode
                _notification.refreshProfile()
                launcherCoordinator.refreshWidgetControls("mode-preference-change")
                if (
                    DofunMediaCompatPolicy.shouldRepublishLegacyAndroidMediaBroadcasts(
                        previousMode,
                        newMode,
                    )
                ) {
                    republishLegacyAndroidMediaState()
                }
                foregroundListener.updateForeground(ForegroundListener.Change.MEDIA_SESSION)
            }
        }

    fun attach() {
        mediaSession.apply {
            setFlags(MediaSessionInitializationPolicy.FLAGS)
            setCallback(mediaSessionInterface)
            setPlaybackState(MediaSessionInitializationPolicy.initialPlaybackState())
            if (BuildConfig.TOPWAY_COMPAT_ENABLED) {
                setSessionActivity(
                    android.app.PendingIntent.getActivity(
                        context,
                        0,
                        Intent().apply {
                            component =
                                android.content.ComponentName(
                                    context.packageName,
                                    "com.tw.music.MusicActivity",
                                )
                            action = Intent.ACTION_MAIN
                            addCategory(Intent.CATEGORY_LAUNCHER)
                        },
                        android.app.PendingIntent.FLAG_IMMUTABLE or
                            android.app.PendingIntent.FLAG_UPDATE_CURRENT,
                    )
                )
            } else {
                setSessionActivity(context.newNowPlayingPendingIntent())
            }
            setQueueTitle(context.getString(R.string.lbl_queue))
        }
        lastLauncherMode = launcherCoordinator.mode
        attached = true
        prefs.registerOnSharedPreferenceChangeListener(modePreferenceListener)
        playbackManager.addListener(this)
        imageSettings.registerListener(this)
        // addListener() synchronously publishes the current queue/progression. The state
        // invalidation path activates the session only when there is something usable to expose.
        // An empty library therefore never appears externally as active + STATE_NONE.
        synchronizeSessionActivation()
    }

    fun tryMediaButtonIntent(intent: Intent): Boolean =
        AndroidXMediaButtonReceiver.handleIntent(mediaSession, intent) != null

    /**
     * Release this instance, closing the [MediaSessionCompat] and preventing any further updates to
     * the [PlaybackNotification].
     */
    fun release() {
        attached = false
        prefs.unregisterOnSharedPreferenceChangeListener(modePreferenceListener)
        mainHandler.removeCallbacksAndMessages(null)
        // Clear published state before shutdown so external controllers do not keep stale metadata.
        artworkRequestToken.incrementAndGet()
        mediaSession.setMetadata(emptyMetadata)
        _notification.updateMetadata(emptyMetadata)
        bitmapProvider.release()
        playbackManager.removeListener(this)
        imageSettings.unregisterListener(this)
        setSessionActive(false, "release")
        mediaSession.release()
    }

    // --- PLAYBACKSTATEMANAGER OVERRIDES ---

    override fun onIndexMoved(index: Int) {
        updateMediaMetadata(playbackManager.currentSong, playbackManager.parent)
        invalidateSessionState()
    }

    override fun onQueueChanged(queue: List<Song>, index: Int, change: QueueChange) {
        updateQueue(queue)
        if (queue.isEmpty()) {
            updateMediaMetadata(null, null)
            invalidateSessionState()
            return
        }
        when (change.type) {
            // Nothing special to do with mapping changes.
            QueueChange.Type.MAPPING -> {}
            // Index changed, ensure playback state's index changes.
            QueueChange.Type.INDEX -> invalidateSessionState()
            // Song changed, ensure metadata changes.
            QueueChange.Type.SONG ->
                updateMediaMetadata(playbackManager.currentSong, playbackManager.parent)
        }
    }

    override fun onQueueReordered(queue: List<Song>, index: Int, isShuffled: Boolean) {
        updateQueue(queue)
        invalidateSessionState()
        mediaSession.setShuffleMode(
            if (isShuffled) {
                PlaybackStateCompat.SHUFFLE_MODE_ALL
            } else {
                PlaybackStateCompat.SHUFFLE_MODE_NONE
            }
        )
        invalidateNotificationActions()
    }

    override fun onNewPlayback(
        parent: MusicParent?,
        queue: List<Song>,
        index: Int,
        isShuffled: Boolean,
    ) {
        val rawMetadata = playbackManager.rawPlaybackMetadata
        if (queue.isEmpty() && rawMetadata != null) {
            updateRawMediaMetadata(rawMetadata)
        } else {
            updateMediaMetadata(playbackManager.currentSong, parent)
        }
        updateQueue(queue)
        invalidateSessionState()
    }

    override fun onProgressionChanged(progression: Progression) {
        if (playbackManager.currentSong == null) {
            playbackManager.rawPlaybackMetadata?.let(::updateRawMediaMetadata)
        }
        invalidateSessionState()
        _notification.updatePlaying(playbackManager.progression.isPlaying)
        broadcastLegacyPlaybackChanged()
        if (!bitmapProvider.isBusy) {
            foregroundListener.updateForeground(ForegroundListener.Change.MEDIA_SESSION)
        }
    }

    override fun onRawPlaybackMetadataChanged(metadata: RawPlaybackMetadata?) {
        if (playbackManager.currentSong == null && metadata != null) {
            updateRawMediaMetadata(metadata)
        } else if (playbackManager.currentSong == null) {
            updateMediaMetadata(null, null)
        }
        invalidateSessionState()
        foregroundListener.updateForeground(ForegroundListener.Change.MEDIA_SESSION)
    }

    override fun onRepeatModeChanged(repeatMode: RepeatMode) {
        mediaSession.setRepeatMode(
            when (repeatMode) {
                RepeatMode.NONE -> PlaybackStateCompat.REPEAT_MODE_NONE
                RepeatMode.TRACK -> PlaybackStateCompat.REPEAT_MODE_ONE
                RepeatMode.ALL -> PlaybackStateCompat.REPEAT_MODE_ALL
            }
        )

        invalidateNotificationActions()
    }

    // --- SETTINGS OVERRIDES ---

    override fun onImageSettingsChanged() {
        // Need to reload the metadata cover.
        updateMediaMetadata(playbackManager.currentSong, playbackManager.parent)
    }

    // --- MEDIASESSION OVERRIDES ---

    // --- INTERNAL ---

    /**
     * Upload a new [MediaMetadataCompat] based on the current playback state to the
     * [MediaSessionCompat] and [PlaybackNotification].
     *
     * @param song The current [Song] to create the [MediaMetadataCompat] from, or null if no [Song]
     *   is currently playing.
     * @param parent The current [MusicParent] to create the [MediaMetadataCompat] from, or null if
     *   playback is currently occuring from all songs.
     */
    private fun updateMediaMetadata(song: Song?, parent: MusicParent?) {
        PerfTimer.trace("MediaSessionHolder.updateMediaMetadata") {
            L.d("Updating media metadata to $song with $parent")
            val requestToken = artworkRequestToken.incrementAndGet()
            if (song == null) {
                // Nothing playing, reset the MediaSession and close the notification.
                L.d("Nothing playing, resetting media session")
                mediaSession.setMetadata(emptyMetadata)
                _notification.updateMetadata(emptyMetadata)
                bitmapProvider.release()
                return
            }

            // Populate MediaMetadataCompat. For efficiency, cache some fields that are re-used
            // several times.
            val title = song.name.resolve(context)
            val artist = song.artists.resolveNames(context)
            val albumArtist = song.album.artists.resolveNames(context)
            val album = song.album.name.resolve(context)
            val metadataSnapshot =
                HeadUnitMetadataPolicy.fromRaw(
                    title = title,
                    artist = artist,
                    albumArtist = albumArtist,
                    albumTitle = album,
                    durationMs = song.durationMs,
                    mediaId = song.uid.toString(),
                    mediaUri = song.uri.toString(),
                    artworkUri =
                        song.cover?.let {
                            Uri.withAppendedPath(CoverProvider.CONTENT_URI, it.id).toString()
                        },
                    hasArtwork = song.cover != null,
                )
                    ?: run {
                        mediaSession.setMetadata(emptyMetadata)
                        _notification.updateMetadata(emptyMetadata)
                        return
                    }
            val builder =
                MediaMetadataCompat.Builder()
                    .putText(MediaMetadataCompat.METADATA_KEY_TITLE, metadataSnapshot.displayTitle)
                    .putText(MediaMetadataCompat.METADATA_KEY_ALBUM, metadataSnapshot.albumTitle)
                    .putText(MediaMetadataCompat.METADATA_KEY_ARTIST, metadataSnapshot.artist)
                    .putText(
                        MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST,
                        metadataSnapshot.albumArtist,
                    )
                    .putText(MediaMetadataCompat.METADATA_KEY_AUTHOR, artist)
                    .putText(MediaMetadataCompat.METADATA_KEY_COMPOSER, artist)
                    .putText(MediaMetadataCompat.METADATA_KEY_WRITER, artist)
                    .putText(
                        MediaMetadataCompat.METADATA_KEY_GENRE,
                        song.genres.resolveNames(context),
                    )
                    .putText(
                        MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE,
                        metadataSnapshot.displayTitle,
                    )
                    .putText(
                        MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE,
                        metadataSnapshot.displaySubtitle,
                    )
                    .putText(
                        MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION,
                        metadataSnapshot.displayDescription,
                    )
                    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, metadataSnapshot.mediaId)
                    .putString(
                        MediaMetadataCompat.METADATA_KEY_MEDIA_URI,
                        metadataSnapshot.mediaUri,
                    )
                    .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, metadataSnapshot.durationMs)
                    .putText(
                        PlaybackNotification.KEY_PARENT,
                        parent?.name?.resolve(context) ?: context.getString(R.string.lbl_all_songs),
                    )
                    .putText(
                        MetadataExtras.KEY_SUBTITLE_LINK_MEDIA_ID,
                        MediaSessionUID.SingleItem(song.artists[0].uid).toString(),
                    )
                    .putText(
                        MetadataExtras.KEY_DESCRIPTION_LINK_MEDIA_ID,
                        MediaSessionUID.SingleItem(song.album.uid).toString(),
                    )
            song.track?.let {
                L.d("Adding track information")
                builder.putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, it.toLong())
            }
            song.disc?.let {
                L.d("Adding disc information")
                builder.putLong(MediaMetadataCompat.METADATA_KEY_DISC_NUMBER, it.number.toLong())
            }
            song.date?.let {
                L.d("Adding date information")
                builder.putString(MediaMetadataCompat.METADATA_KEY_DATE, it.toString())
                builder.putLong(MediaMetadataCompat.METADATA_KEY_YEAR, it.year.toLong())
            }

            val initialMetadata = builder.build()
            mediaSession.setMetadata(initialMetadata)
            _notification.updateMetadata(initialMetadata)
            broadcastLegacyMetadataChanged(
                title = metadataSnapshot.displayTitle,
                artist = metadataSnapshot.artist,
                album = metadataSnapshot.albumTitle,
                durationMs = metadataSnapshot.durationMs,
            )
            foregroundListener.updateForeground(ForegroundListener.Change.MEDIA_SESSION)

            bitmapProvider.load(
                song,
                object : BitmapProvider.Target {
                    override fun onCompleted(bitmap: Bitmap?) {
                        if (requestToken != artworkRequestToken.get()) {
                            L.d("Artwork loaded for stale request; ignoring")
                            return
                        }

                        L.d("Bitmap loaded, applying media session and posting notification")
                        if (bitmap != null) {
                            builder.putBitmap(MediaMetadataCompat.METADATA_KEY_ART, bitmap)
                            builder.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, bitmap)
                        }
                        song.cover?.let {
                            val artworkUri = Uri.withAppendedPath(CoverProvider.CONTENT_URI, it.id)
                            builder.putString(
                                MediaMetadataCompat.METADATA_KEY_ART_URI,
                                artworkUri.toString(),
                            )
                            builder.putString(
                                MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI,
                                artworkUri.toString(),
                            )
                            builder.putString(
                                MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI,
                                artworkUri.toString(),
                            )
                        }
                        val metadata = builder.build()
                        mediaSession.setMetadata(metadata)
                        _notification.updateMetadata(metadata)
                        foregroundListener.updateForeground(ForegroundListener.Change.MEDIA_SESSION)
                    }
                },
                Size(
                    NotificationBitmapSafety.MAX_ICON_SIZE_PX,
                    NotificationBitmapSafety.MAX_ICON_SIZE_PX,
                ),
            )
        }
    }

    private fun updateRawMediaMetadata(metadata: RawPlaybackMetadata) {
        PerfTimer.trace("MediaSessionHolder.updateRawMediaMetadata") {
            L.d("Updating raw TS18 media metadata to $metadata")
            artworkRequestToken.incrementAndGet()
            val metadataSnapshot =
                HeadUnitMetadataPolicy.fromRaw(
                    title = metadata.displayTitle,
                    artist = metadata.displayArtist,
                    albumArtist = metadata.displayArtist,
                    albumTitle = metadata.album,
                    durationMs = metadata.durationMs,
                    mediaId = metadata.uriString,
                    mediaUri = metadata.uriString,
                    artworkUri = null,
                    hasArtwork = false,
                )
                    ?: run {
                        mediaSession.setMetadata(emptyMetadata)
                        _notification.updateMetadata(emptyMetadata)
                        return
                    }
            val rawSessionMetadata =
                MediaMetadataCompat.Builder()
                    .putText(MediaMetadataCompat.METADATA_KEY_TITLE, metadataSnapshot.displayTitle)
                    .putText(MediaMetadataCompat.METADATA_KEY_ALBUM, metadataSnapshot.albumTitle)
                    .putText(MediaMetadataCompat.METADATA_KEY_ARTIST, metadataSnapshot.artist)
                    .putText(
                        MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST,
                        metadataSnapshot.albumArtist,
                    )
                    .putText(MediaMetadataCompat.METADATA_KEY_AUTHOR, metadataSnapshot.artist)
                    .putText(
                        MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE,
                        metadataSnapshot.displayTitle,
                    )
                    .putText(
                        MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE,
                        metadataSnapshot.displaySubtitle,
                    )
                    .putText(
                        MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION,
                        metadataSnapshot.displayDescription,
                    )
                    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, metadataSnapshot.mediaId)
                    .putString(
                        MediaMetadataCompat.METADATA_KEY_MEDIA_URI,
                        metadataSnapshot.mediaUri,
                    )
                    .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, metadataSnapshot.durationMs)
                    .putText(
                        PlaybackNotification.KEY_PARENT,
                        context.getString(R.string.lbl_all_songs),
                    )
                    .build()
            mediaSession.setMetadata(rawSessionMetadata)
            _notification.updateMetadata(rawSessionMetadata)
            broadcastLegacyMetadataChanged(
                title = metadataSnapshot.displayTitle,
                artist = metadataSnapshot.artist,
                album = metadataSnapshot.albumTitle,
                durationMs = metadataSnapshot.durationMs,
            )
        }
    }

    private fun updateQueue(queue: List<Song>) {
        PerfTimer.trace("MediaSessionHolder.updateQueue(${queue.size})") {
            val queueItems =
                queue.mapIndexed { i, song ->
                    val description =
                        song.toMediaDescription(
                            context,
                            { putInt(MediaSessionInterface.KEY_QUEUE_POS, i) },
                        )
                    MediaSessionCompat.QueueItem(description, i.toLong())
                }
            L.d("Uploading ${queueItems.size} songs to MediaSession queue")
            mediaSession.setQueue(queueItems)
        }
    }

    private fun invalidateSessionState() {
        L.d("Updating media session playback state")
        if (!hasPlayableSessionState()) {
            mediaSession.setPlaybackState(MediaSessionInitializationPolicy.emptyPlaybackState())
            setSessionActive(false, "state-empty")
            return
        }
        setSessionActive(true, "state-playable")
        val state =
            playbackManager.progression
                .intoPlaybackState(PlaybackStateCompat.Builder())
                .setActions(MediaSessionInterface.ACTIONS)
                .setActiveQueueItemId(playbackManager.index.toLong())
        val repeatAction =
            PlaybackStateCompat.CustomAction.Builder(
                    PlaybackActions.ACTION_INC_REPEAT_MODE,
                    context.getString(R.string.desc_change_repeat),
                    playbackManager.repeatMode.icon,
                )
                .build()
        state.addCustomAction(repeatAction)
        val shuffleAction =
            PlaybackStateCompat.CustomAction.Builder(
                    PlaybackActions.ACTION_INVERT_SHUFFLE,
                    context.getString(R.string.desc_shuffle),
                    if (playbackManager.isShuffled) {
                        R.drawable.ic_shuffle_on_24
                    } else {
                        R.drawable.ic_shuffle_off_24
                    },
                )
                .build()
        state.addCustomAction(shuffleAction)
        mediaSession.setPlaybackState(state.build())
    }

    private fun synchronizeSessionActivation() {
        if (hasPlayableSessionState()) {
            invalidateSessionState()
        } else {
            mediaSession.setPlaybackState(MediaSessionInitializationPolicy.emptyPlaybackState())
            setSessionActive(false, "attach-empty")
        }
    }

    private fun hasPlayableSessionState(): Boolean =
        playbackManager.currentSong != null ||
            playbackManager.rawPlaybackMetadata != null ||
            playbackManager.queue.isNotEmpty() ||
            playbackManager.queueWindow != null

    private fun setSessionActive(active: Boolean, reason: String) {
        mediaSession.isActive = active
        if (lastReportedSessionActive == active) return
        lastReportedSessionActive = active
        launcherTelemetry.log(
            category = DiagnosticJournal.CAT_PLAYBACK,
            event = "MediaSession activation",
            origin = "MediaSessionHolder",
            command = if (active) "ACTIVATE" else "DEACTIVATE",
            result = "APPLIED",
            detail =
                "reason=$reason currentSong=${playbackManager.currentSong != null} " +
                    "raw=${playbackManager.rawPlaybackMetadata != null} queue=${playbackManager.queue.size} " +
                    "queueWindow=${playbackManager.queueWindow != null}",
        )
    }

    private fun republishLegacyAndroidMediaState() {
        if (!hasPlayableSessionState()) return
        val song = playbackManager.currentSong
        val rawMetadata = playbackManager.rawPlaybackMetadata
        val metadataSnapshot =
            when {
                song != null ->
                    HeadUnitMetadataPolicy.fromRaw(
                        title = song.name.resolve(context),
                        artist = song.artists.resolveNames(context),
                        albumArtist = song.album.artists.resolveNames(context),
                        albumTitle = song.album.name.resolve(context),
                        durationMs = song.durationMs,
                        mediaId = song.uid.toString(),
                        mediaUri = song.uri.toString(),
                        artworkUri = null,
                        hasArtwork = false,
                    )
                rawMetadata != null ->
                    HeadUnitMetadataPolicy.fromRaw(
                        title = rawMetadata.displayTitle,
                        artist = rawMetadata.displayArtist,
                        albumArtist = rawMetadata.displayArtist,
                        albumTitle = rawMetadata.album,
                        durationMs = rawMetadata.durationMs,
                        mediaId = rawMetadata.uriString,
                        mediaUri = rawMetadata.uriString,
                        artworkUri = null,
                        hasArtwork = false,
                    )
                else -> null
            }
        if (metadataSnapshot != null) {
            broadcastLegacyMetadataChanged(
                title = metadataSnapshot.displayTitle,
                artist = metadataSnapshot.artist,
                album = metadataSnapshot.albumTitle,
                durationMs = metadataSnapshot.durationMs,
            )
        }
        broadcastLegacyPlaybackChanged()
    }

    private fun broadcastLegacyMetadataChanged(
        title: CharSequence?,
        artist: CharSequence?,
        album: CharSequence?,
        durationMs: Long,
    ) {
        if (
            !BuildConfig.TOPWAY_COMPAT_ENABLED ||
                !launcherCoordinator.mode.publishesLegacyAndroidMediaBroadcasts
        ) {
            return
        }
        try {
            context.sendBroadcast(
                Intent(ACTION_LEGACY_META_CHANGED)
                    .putExtra("track", title?.toString().orEmpty())
                    .putExtra("artist", artist?.toString().orEmpty())
                    .putExtra("album", album?.toString().orEmpty())
                    .putExtra("duration", durationMs)
                    .putExtra("playing", playbackManager.progression.isPlaying)
                    .putExtra("package", context.packageName)
            )
            launcherTelemetry.log(
                category = DiagnosticJournal.CAT_PLAYBACK,
                event = "Legacy Android media broadcast",
                origin = "MediaSessionHolder",
                command = ACTION_LEGACY_META_CHANGED,
                result = "PUBLISHED",
                detail = "titleLen=${title?.length ?: 0} durationMs=$durationMs",
            )
        } catch (e: RuntimeException) {
            L.w(e, "Unable to broadcast legacy metadata change")
            launcherTelemetry.log(
                category = DiagnosticJournal.CAT_PLAYBACK,
                event = "Legacy Android media broadcast",
                origin = "MediaSessionHolder",
                command = ACTION_LEGACY_META_CHANGED,
                result = "FAILED",
                detail = e.javaClass.simpleName,
            )
        }
    }

    private fun broadcastLegacyPlaybackChanged() {
        if (
            !BuildConfig.TOPWAY_COMPAT_ENABLED ||
                !launcherCoordinator.mode.publishesLegacyAndroidMediaBroadcasts
        ) {
            return
        }
        try {
            context.sendBroadcast(
                Intent(ACTION_LEGACY_PLAYSTATE_CHANGED)
                    .putExtra("playing", playbackManager.progression.isPlaying)
                    .putExtra("package", context.packageName)
            )
            launcherTelemetry.log(
                category = DiagnosticJournal.CAT_PLAYBACK,
                event = "Legacy Android media broadcast",
                origin = "MediaSessionHolder",
                command = ACTION_LEGACY_PLAYSTATE_CHANGED,
                result = "PUBLISHED",
                detail = "playing=${playbackManager.progression.isPlaying}",
            )
        } catch (e: RuntimeException) {
            L.w(e, "Unable to broadcast legacy playback state change")
            launcherTelemetry.log(
                category = DiagnosticJournal.CAT_PLAYBACK,
                event = "Legacy Android media broadcast",
                origin = "MediaSessionHolder",
                command = ACTION_LEGACY_PLAYSTATE_CHANGED,
                result = "FAILED",
                detail = e.javaClass.simpleName,
            )
        }
    }

    private fun invalidateNotificationActions() {
        L.d("Invalidating notification actions")
        invalidateSessionState()
        _notification.updateRepeatMode(playbackManager.repeatMode)
        _notification.updateShuffled(playbackManager.isShuffled)
        if (!bitmapProvider.isBusy) {
            L.d("Not loading a bitmap, post the notification")
            foregroundListener.updateForeground(ForegroundListener.Change.MEDIA_SESSION)
        }
    }

    companion object {
        private const val ACTION_LEGACY_META_CHANGED = "com.android.music.metachanged"
        private const val ACTION_LEGACY_PLAYSTATE_CHANGED = "com.android.music.playstatechanged"
        internal val emptyMetadata =
            MediaMetadataCompat.Builder()
                .putText(MediaMetadataCompat.METADATA_KEY_TITLE, "")
                .putText(MediaMetadataCompat.METADATA_KEY_ARTIST, "")
                .putText(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, "")
                .putText(MediaMetadataCompat.METADATA_KEY_ALBUM, "")
                .putText(MediaMetadataCompat.METADATA_KEY_AUTHOR, "")
                .putText(MediaMetadataCompat.METADATA_KEY_COMPOSER, "")
                .putText(MediaMetadataCompat.METADATA_KEY_WRITER, "")
                .putText(MediaMetadataCompat.METADATA_KEY_GENRE, "")
                .putText(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, "")
                .putText(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, "")
                .putText(MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION, "")
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, "")
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, "")
                .putString(MediaMetadataCompat.METADATA_KEY_DATE, "")
                .putString(MediaMetadataCompat.METADATA_KEY_ART_URI, "")
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, "")
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI, "")
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, 0L)
                .putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, 0L)
                .putLong(MediaMetadataCompat.METADATA_KEY_DISC_NUMBER, 0L)
                .putLong(MediaMetadataCompat.METADATA_KEY_YEAR, 0L)
                .putText(PlaybackNotification.KEY_PARENT, "")
                .putText(MetadataExtras.KEY_SUBTITLE_LINK_MEDIA_ID, "")
                .putText(MetadataExtras.KEY_DESCRIPTION_LINK_MEDIA_ID, "")
                .build()
    }
}

@SuppressLint("RestrictedApi")
private class PlaybackNotification(
    private val context: Context,
    sessionToken: MediaSessionCompat.Token,
    private val canonicalServiceClass: Class<*>,
    private val profileProvider: () -> PlaybackNotificationProfile,
) : ForegroundServiceNotification(context, CHANNEL_INFO) {
    private val sessionToken = sessionToken
    private var isPlaying = false
    private var repeatMode = RepeatMode.NONE
    private var isShuffled = false

    init {
        setSmallIcon(R.drawable.ic_auxio_24)
        setCategory(NotificationCompat.CATEGORY_TRANSPORT)
        setShowWhen(false)
        setSilent(true)
        setOnlyAlertOnce(true)
        setAutoCancel(false)
        setContentIntent(context.newNowPlayingPendingIntent())
        setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        rebuildActions()
    }

    override val code: Int
        get() = IntegerTable.PLAYBACK_NOTIFICATION_CODE

    fun updateMetadata(metadata: MediaMetadataCompat) {
        L.d("Updating shown metadata")
        val albumArt =
            NotificationBitmapSafety.sanitize(
                metadata.getBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART)
            )
        if (albumArt != null) {
            setLargeIcon(albumArt)
        } else {
            setLargeIcon(NotificationBitmapSafety.fallbackBitmap())
        }
        setContentTitle(
            metadata.getString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE)?.takeIf {
                it.isNotBlank()
            } ?: context.getString(R.string.info_app_name)
        )
        setContentText(
            metadata.getText(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE)?.takeIf {
                it.isNotBlank()
            } ?: context.getString(R.string.lbl_unknown)
        )
        setSubText(
            metadata.getText(MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION)?.takeIf {
                it.isNotBlank()
            }
        )
    }

    fun updatePlaying(isPlaying: Boolean) {
        L.d("Updating playing state: $isPlaying")
        this.isPlaying = isPlaying
        rebuildActions()
    }

    fun updateRepeatMode(repeatMode: RepeatMode) {
        L.d("Applying repeat mode action: $repeatMode")
        this.repeatMode = repeatMode
        rebuildActions()
    }

    fun updateShuffled(isShuffled: Boolean) {
        L.d("Applying shuffle action: $isShuffled")
        this.isShuffled = isShuffled
        rebuildActions()
    }

    fun refreshProfile() {
        L.i("Refreshing playback notification profile")
        rebuildActions()
    }

    private fun rebuildActions() {
        mActions.clear()
        when (profileProvider()) {
            PlaybackNotificationProfile.GenericDofun -> rebuildGenericActions()
            PlaybackNotificationProfile.RichAuxio -> rebuildRichActions()
        }
    }

    private fun rebuildGenericActions() {
        val state = DofunMediaCompatPolicy.genericNotificationState(isPlaying)
        val keys = state.actionKeyCodes
        addAction(
            buildMediaButtonAction(
                keys[0],
                R.drawable.ic_skip_prev_24,
                context.getString(R.string.desc_skip_prev),
            )
        )
        addAction(
            buildMediaButtonAction(
                keys[1],
                if (isPlaying) R.drawable.ic_pause_24 else R.drawable.ic_play_24,
                context.getString(R.string.desc_play_pause),
            )
        )
        addAction(
            buildMediaButtonAction(
                keys[2],
                R.drawable.ic_skip_next_24,
                context.getString(R.string.desc_skip_next),
            )
        )
        val stopIntent = buildMediaButtonPendingIntent(state.deleteKeyCode)
        setDeleteIntent(stopIntent)
        setOngoing(state.ongoing)
        setStyle(
            MediaStyle(this)
                .setMediaSession(sessionToken)
                .setShowActionsInCompactView(*DofunMediaCompatPolicy.compactActionIndices)
                .setShowCancelButton(true)
                .setCancelButtonIntent(stopIntent)
        )
    }

    private fun rebuildRichActions() {
        addAction(buildRepeatAction(context, repeatMode))
        addAction(
            buildAction(
                context,
                PlaybackActions.ACTION_SKIP_PREV,
                R.drawable.ic_skip_prev_24,
                context.getString(R.string.desc_skip_prev),
            )
        )
        addAction(buildPlayPauseAction(context, isPlaying))
        addAction(
            buildAction(
                context,
                PlaybackActions.ACTION_SKIP_NEXT,
                R.drawable.ic_skip_next_24,
                context.getString(R.string.desc_skip_next),
            )
        )
        addAction(buildShuffleAction(context, isShuffled))
        setDeleteIntent(null)
        setOngoing(false)
        setStyle(
            MediaStyle(this)
                .setMediaSession(sessionToken)
                .setShowActionsInCompactView(1, 2, 3)
                .setShowCancelButton(false)
        )
    }

    private fun buildMediaButtonAction(
        keyCode: Int,
        @DrawableRes iconRes: Int,
        title: String,
    ): NotificationCompat.Action =
        NotificationCompat.Action.Builder(iconRes, title, buildMediaButtonPendingIntent(keyCode))
            .build()

    private fun buildMediaButtonPendingIntent(keyCode: Int): PendingIntent =
        PendingIntent.getService(
            context,
            keyCode,
            MediaButtonIntentFactory.serviceIntent(context, canonicalServiceClass, keyCode),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )

    private fun buildPlayPauseAction(
        context: Context,
        isPlaying: Boolean,
    ): NotificationCompat.Action {
        val drawableRes = if (isPlaying) R.drawable.ic_pause_24 else R.drawable.ic_play_24
        return buildAction(
            context,
            PlaybackActions.ACTION_PLAY_PAUSE,
            drawableRes,
            context.getString(R.string.desc_play_pause),
        )
    }

    private fun buildRepeatAction(
        context: Context,
        repeatMode: RepeatMode,
    ): NotificationCompat.Action =
        buildAction(
            context,
            PlaybackActions.ACTION_INC_REPEAT_MODE,
            repeatMode.icon,
            context.getString(R.string.desc_change_repeat),
        )

    private fun buildShuffleAction(
        context: Context,
        isShuffled: Boolean,
    ): NotificationCompat.Action {
        val drawableRes =
            if (isShuffled) R.drawable.ic_shuffle_on_24 else R.drawable.ic_shuffle_off_24
        return buildAction(
            context,
            PlaybackActions.ACTION_INVERT_SHUFFLE,
            drawableRes,
            context.getString(R.string.desc_shuffle),
        )
    }

    private fun buildAction(
        context: Context,
        actionName: String,
        @DrawableRes iconRes: Int,
        title: String,
    ) =
        NotificationCompat.Action.Builder(
                iconRes,
                title,
                context.newBroadcastPendingIntent(actionName),
            )
            .build()

    companion object {
        const val KEY_PARENT = BuildConfig.APPLICATION_ID + ".metadata.PARENT"

        private val CHANNEL_INFO =
            ChannelInfo(id = PlaybackNotificationChannel.id, nameRes = R.string.lbl_playback)
    }
}

