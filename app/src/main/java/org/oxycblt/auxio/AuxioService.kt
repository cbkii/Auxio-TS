/*
 * Copyright (c) 2024 Auxio Project
 * AuxioService.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.oxycblt.auxio

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.BadParcelableException
import android.os.Bundle
import android.os.IBinder
import android.support.v4.media.MediaBrowserCompat.MediaItem
import androidx.annotation.StringRes
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat
import androidx.media.MediaBrowserServiceCompat
import androidx.media.utils.MediaConstants
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import org.oxycblt.auxio.diagnostics.DiagnosticJournal
import org.oxycblt.auxio.headunit.topway.TopwayCommandServiceClient
import org.oxycblt.auxio.headunit.ts18.Ts18FirstAudioLatency
import org.oxycblt.auxio.music.service.MusicServiceFragment
import org.oxycblt.auxio.playback.service.PlaybackServiceFragment
import org.oxycblt.auxio.util.PerfTimer
import timber.log.Timber

@AndroidEntryPoint
open class AuxioService :
    MediaBrowserServiceCompat(), ForegroundListener, MusicServiceFragment.Invalidator {
    @Inject lateinit var playbackFragmentFactory: PlaybackServiceFragment.Factory
    private lateinit var playbackFragment: PlaybackServiceFragment

    @Inject lateinit var musicFragmentFactory: MusicServiceFragment.Factory
    private lateinit var musicFragment: MusicServiceFragment

    @Inject lateinit var journal: DiagnosticJournal
    @Inject lateinit var topwayCommandServiceClient: TopwayCommandServiceClient

    @SuppressLint("WrongConstant")
    override fun onCreate() {
        PerfTimer.trace("AuxioService.onCreate") {
            Ts18FirstAudioLatency.mark("service_on_create")
            super.onCreate()
            isForeground = false
            playbackFragment = playbackFragmentFactory.create(this, this)
            musicFragment = musicFragmentFactory.create(this, this, this)
            sessionToken = playbackFragment.attach()
            musicFragment.attach()
            topwayCommandServiceClient.attach(this::class.java)
            Timber.d("Service Created")
            journal.log(DiagnosticJournal.CAT_LIFECYCLE, "AuxioService onCreate")
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        PerfTimer.trace("AuxioService.onStartCommand") {
            Ts18FirstAudioLatency.mark("service_on_start_command")
            // TODO: Start command occurring from a foreign service basically implies a detached
            // service, we might need more handling here.
            super.onStartCommand(intent, flags, startId)
            onHandleForeground(intent)
            journal.log(
                DiagnosticJournal.CAT_LIFECYCLE,
                "AuxioService onStartCommand",
                "Action: ${intent?.action}, StartId: $startId",
            )
            // Playback services are expected to survive process churn when possible so that
            // MediaSession/controller interactions continue to route to the same service endpoint.
            // Keep this service sticky and let playback/session state restoration decide whether
            // playback should resume.
            return START_STICKY
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        val binder = super.onBind(intent)
        onHandleForeground(intent)
        return binder
    }

    private fun onHandleForeground(intent: Intent?) {
        // TS18 fast-resume priority: handle playback/launcher commands before any heavy
        // music indexing path. This keeps raw snapshot restore independent from library readiness.
        playbackFragment.start(intent)
        musicFragment.start()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        playbackFragment.handleTaskRemoved()
    }

    override fun onDestroy() {
        isForeground = false
        topwayCommandServiceClient.release()
        super.onDestroy()
        musicFragment.release()
        playbackFragment.release()
        journal.log(DiagnosticJournal.CAT_LIFECYCLE, "AuxioService onDestroy")
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?,
    ): BrowserRoot? {
        if (
            clientUid < 0 ||
                clientPackageName.isBlank() ||
                clientPackageName.length > MAX_CLIENT_PACKAGE_LENGTH ||
                clientPackageName.any(Char::isISOControl)
        ) {
            Timber.w("Rejecting malformed MediaBrowser client identity")
            return null
        }
        return musicFragment.getRoot()
    }

    override fun onLoadItem(itemId: String, result: Result<MediaItem>) {
        if (!isValidMediaId(itemId)) {
            Timber.w("Rejecting malformed MediaBrowser item id")
            result.sendResult(null)
            return
        }
        musicFragment.getItem(itemId, result)
    }

    override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaItem>>) {
        if (!isValidMediaId(parentId)) {
            Timber.w("Rejecting malformed MediaBrowser parent id")
            result.sendResult(mutableListOf())
            return
        }
        musicFragment.getChildren(parentId, getRootChildrenLimit(), result)
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaItem>>,
        options: Bundle,
    ) {
        if (!isValidMediaId(parentId)) {
            Timber.w("Rejecting malformed paged MediaBrowser parent id")
            result.sendResult(mutableListOf())
            return
        }
        musicFragment.getChildren(parentId, getRootChildrenLimit(), result)
    }

    override fun onSearch(query: String, extras: Bundle?, result: Result<MutableList<MediaItem>>) {
        if (query.length > MAX_SEARCH_QUERY_LENGTH || query.any(Char::isISOControl)) {
            Timber.w("Rejecting malformed MediaBrowser search query")
            result.sendResult(mutableListOf())
            return
        }
        musicFragment.search(query, result)
    }

    private fun getRootChildrenLimit(): Int =
        try {
            (browserRootHints?.getInt(
                MediaConstants.BROWSER_ROOT_HINTS_KEY_ROOT_CHILDREN_LIMIT,
                DEFAULT_ROOT_CHILDREN_LIMIT,
            ) ?: DEFAULT_ROOT_CHILDREN_LIMIT)
                .coerceIn(1, MAX_ROOT_CHILDREN_LIMIT)
        } catch (e: BadParcelableException) {
            Timber.w(e, "Ignoring malformed MediaBrowser root hints")
            DEFAULT_ROOT_CHILDREN_LIMIT
        } catch (e: RuntimeException) {
            Timber.w(e, "Ignoring unreadable MediaBrowser root hints")
            DEFAULT_ROOT_CHILDREN_LIMIT
        }

    private fun isValidMediaId(value: String): Boolean =
        value.isNotBlank() &&
            value.length <= MAX_MEDIA_ID_LENGTH &&
            value.none(Char::isISOControl)

    override fun updateForeground(change: ForegroundListener.Change) {
        val mediaNotification = playbackFragment.notification
        if (mediaNotification != null) {
            if (change == ForegroundListener.Change.MEDIA_SESSION) {
                startForeground(mediaNotification.code, mediaNotification.build())
            }
            // Nothing changed, but don't show anything music related since we can always
            // index during playback.
            isForeground = true
        } else {
            musicFragment.createNotification {
                if (it != null) {
                    startForeground(it.code, it.build())
                    isForeground = true
                } else {
                    ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
                    isForeground = false
                }
            }
        }
    }

    override fun invalidateMusic(mediaId: String) {
        if (isValidMediaId(mediaId)) notifyChildrenChanged(mediaId)
    }

    companion object {
        const val ACTION_START = BuildConfig.APPLICATION_ID + ".service.START"

        @Volatile
        var isForeground = false
            private set

        // This is only meant for Auxio to internally ensure that it's state management will work.
        const val INTENT_KEY_START_ID = BuildConfig.APPLICATION_ID + ".service.START_ID"

        private const val MAX_CLIENT_PACKAGE_LENGTH = 255
        private const val MAX_MEDIA_ID_LENGTH = 1024
        private const val MAX_SEARCH_QUERY_LENGTH = 256
        private const val DEFAULT_ROOT_CHILDREN_LIMIT = 4
        private const val MAX_ROOT_CHILDREN_LIMIT = 100
    }
}

interface ForegroundListener {
    fun updateForeground(change: Change)

    enum class Change {
        MEDIA_SESSION,
        INDEXER,
    }
}

/**
 * Wrapper around [NotificationCompat.Builder] intended for use for [NotificationCompat]s that
 * signal a Service's ongoing foreground state.
 *
 * @author Alexander Capehart (OxygenCobalt)
 */
abstract class ForegroundServiceNotification(context: Context, info: ChannelInfo) :
    NotificationCompat.Builder(context, info.id) {
    private val notificationManager = NotificationManagerCompat.from(context)

    init {
        // Set up the notification channel. Foreground notifications are non-substantial, and
        // thus make no sense to have lights, vibration, or lead to a notification badge.
        val channel =
            NotificationChannelCompat.Builder(info.id, NotificationManagerCompat.IMPORTANCE_LOW)
                .setName(context.getString(info.nameRes))
                .setLightsEnabled(false)
                .setVibrationEnabled(false)
                .setShowBadge(false)
                .build()
        notificationManager.createNotificationChannel(channel)
    }

    /**
     * The code used to identify this notification.
     *
     * @see NotificationManagerCompat.notify
     */
    abstract val code: Int

    /**
     * Reduced representation of a [NotificationChannelCompat].
     *
     * @param id The ID of the channel.
     * @param nameRes A string resource ID corresponding to the human-readable name.
     */
    data class ChannelInfo(val id: String, @StringRes val nameRes: Int)
}
