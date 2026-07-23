#!/usr/bin/env python3
"""Apply PR #195's generic DoFun media compatibility implementation.

This is a temporary, branch-local development helper. The applying workflow and this
script are removed after the generated implementation commit is verified.
"""

from __future__ import annotations

from pathlib import Path
import re
import sys

ROOT = Path(__file__).resolve().parents[1]


def read(path: str) -> str:
    return (ROOT / path).read_text(encoding="utf-8")


def write(path: str, content: str) -> None:
    target = ROOT / path
    target.parent.mkdir(parents=True, exist_ok=True)
    target.write_text(content, encoding="utf-8")


def replace_once(path: str, old: str, new: str) -> None:
    content = read(path)
    count = content.count(old)
    if count != 1:
        raise RuntimeError(f"{path}: expected one match, found {count}: {old[:120]!r}")
    write(path, content.replace(old, new, 1))


def regex_replace_once(path: str, pattern: str, replacement: str, flags: int = 0) -> None:
    content = read(path)
    updated, count = re.subn(pattern, replacement, content, count=1, flags=flags)
    if count != 1:
        raise RuntimeError(f"{path}: expected one regex match, found {count}: {pattern[:120]!r}")
    write(path, updated)


write(
    "app/src/main/java/org/oxycblt/auxio/playback/service/DofunMediaCompatPolicy.kt",
    '''/*
 * Copyright (c) 2026 Auxio Project
 * DofunMediaCompatPolicy.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.oxycblt.auxio.playback.service

import android.view.KeyEvent
import org.oxycblt.auxio.headunit.topway.Ts18LauncherIntegrationMode

/** Notification layouts available to the one canonical Auxio playback notification. */
enum class PlaybackNotificationProfile {
    RichAuxio,
    GenericDofun,
}

/** Pure policy for the Android-standard DoFun compatibility lane. */
object DofunMediaCompatPolicy {
    val compactActionIndices: IntArray = intArrayOf(0, 1, 2)

    fun notificationProfile(
        mode: Ts18LauncherIntegrationMode,
        topwayCompatFlavor: Boolean,
    ): PlaybackNotificationProfile =
        if (topwayCompatFlavor && mode.usesGenericMediaNotification) {
            PlaybackNotificationProfile.GenericDofun
        } else {
            PlaybackNotificationProfile.RichAuxio
        }

    fun genericActionKeyCodes(isPlaying: Boolean): IntArray =
        intArrayOf(
            KeyEvent.KEYCODE_MEDIA_PREVIOUS,
            if (isPlaying) KeyEvent.KEYCODE_MEDIA_PAUSE else KeyEvent.KEYCODE_MEDIA_PLAY,
            KeyEvent.KEYCODE_MEDIA_NEXT,
        )

    fun isColdStartPlayKey(keyCode: Int): Boolean =
        keyCode == KeyEvent.KEYCODE_MEDIA_PLAY ||
            keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE ||
            keyCode == KeyEvent.KEYCODE_HEADSETHOOK
}
''',
)

write(
    "app/src/main/java/org/oxycblt/auxio/playback/service/PlaybackNotificationChannel.kt",
    '''/*
 * Copyright (c) 2026 Auxio Project
 * PlaybackNotificationChannel.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.oxycblt.auxio.playback.service

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat
import java.util.concurrent.atomic.AtomicBoolean
import org.oxycblt.auxio.BuildConfig

/** User-controlled playback notification channel state. */
enum class PlaybackChannelState {
    Usable,
    NotCreated,
    Blocked,
}

data class PlaybackChannelSnapshot(
    val state: PlaybackChannelState,
    val packageNotificationsEnabled: Boolean,
    val importance: Int?,
    val publicationRequestedThisProcess: Boolean,
)

/**
 * Read-only status and settings routing for the playback channel.
 *
 * Android preserves channel importance across app updates. Auxio reports a blocked channel and
 * opens system settings; it never attempts to silently override the user's channel choice.
 */
object PlaybackNotificationChannel {
    val id: String = BuildConfig.APPLICATION_ID + ".channel.PLAYBACK"

    private val publicationRequested = AtomicBoolean(false)

    fun markPublicationRequested() {
        publicationRequested.set(true)
    }

    fun inspect(context: Context): PlaybackChannelSnapshot {
        val packageEnabled = NotificationManagerCompat.from(context).areNotificationsEnabled()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return PlaybackChannelSnapshot(
                state = classify(packageEnabled, channelExists = true, importance = 1),
                packageNotificationsEnabled = packageEnabled,
                importance = null,
                publicationRequestedThisProcess = publicationRequested.get(),
            )
        }

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        val channel = manager?.getNotificationChannel(id)
        return PlaybackChannelSnapshot(
            state =
                classify(
                    notificationsEnabled = packageEnabled,
                    channelExists = channel != null,
                    importance = channel?.importance,
                ),
            packageNotificationsEnabled = packageEnabled,
            importance = channel?.importance,
            publicationRequestedThisProcess = publicationRequested.get(),
        )
    }

    fun settingsIntent(context: Context): Intent {
        val hasChannel =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val manager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
                manager?.getNotificationChannel(id) != null
            } else {
                false
            }
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && hasChannel) {
            Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
                .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                .putExtra(Settings.EXTRA_CHANNEL_ID, id)
        } else {
            Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                } else {
                    putExtra("app_package", context.packageName)
                    putExtra("app_uid", context.applicationInfo.uid)
                }
            }
        }
    }

    internal fun classify(
        notificationsEnabled: Boolean,
        channelExists: Boolean,
        importance: Int?,
    ): PlaybackChannelState =
        when {
            !notificationsEnabled -> PlaybackChannelState.Blocked
            !channelExists -> PlaybackChannelState.NotCreated
            importance == NotificationManager.IMPORTANCE_NONE -> PlaybackChannelState.Blocked
            else -> PlaybackChannelState.Usable
        }
}
''',
)

# The new profile is standards-first. Existing broadcast/CommandService lanes remain explicit.
replace_once(
    "app/src/main/java/org/oxycblt/auxio/headunit/topway/Ts18LauncherIntegrationMode.kt",
    '''enum class Ts18LauncherIntegrationMode {
    Disabled,
    AndroidMediaSessionOnly,
    TopwayBroadcastOnly,
    TopwayCommandOnly,
    TopwayBroadcastAndCommand,
    AutoAllSafePaths,
    DiagnosticsOnly;
''',
    '''enum class Ts18LauncherIntegrationMode {
    Disabled,
    AndroidMediaSessionOnly,
    GenericDofunMedia,
    TopwayBroadcastOnly,
    TopwayCommandOnly,
    TopwayBroadcastAndCommand,
    AutoAllSafePaths,
    DiagnosticsOnly;
''',
)
replace_once(
    "app/src/main/java/org/oxycblt/auxio/headunit/topway/Ts18LauncherIntegrationMode.kt",
    '''    val diagnosticsOnly: Boolean
        get() = this == DiagnosticsOnly

    companion object {
''',
    '''    val diagnosticsOnly: Boolean
        get() = this == DiagnosticsOnly

    val usesGenericMediaNotification: Boolean
        get() = this == GenericDofunMedia

    val bindsTopwayCommandService: Boolean
        get() = handlesTopwayCommands || diagnosticsOnly

    companion object {
''',
)
replace_once(
    "app/src/main/java/org/oxycblt/auxio/headunit/topway/Ts18LauncherIntegrationMode.kt",
    '''        fun default(): Ts18LauncherIntegrationMode =
            if (BuildConfig.TOPWAY_COMPAT_FLAVOR) AutoAllSafePaths else AndroidMediaSessionOnly
''',
    '''        fun default(): Ts18LauncherIntegrationMode =
            if (BuildConfig.TOPWAY_COMPAT_FLAVOR) GenericDofunMedia else AndroidMediaSessionOnly
''',
)

# Remove the focus requirement only for cold/resume play-like commands. Other stale global keys
# remain rejected when Auxio does not hold focus.
replace_once(
    "app/src/main/java/org/oxycblt/auxio/playback/service/MediaButtonActionMapper.kt",
    '''        if (!isFocusHeld) {
            return false
        }
        if (!hasCurrentSong && isPauseOrStop(keyCode)) {
            return false
        }
        return isSupportedMediaKey(keyCode)
''',
    '''        if (!isSupportedMediaKey(keyCode)) {
            return false
        }
        if (!isFocusHeld) {
            return DofunMediaCompatPolicy.isColdStartPlayKey(keyCode)
        }
        if (!hasCurrentSong && !DofunMediaCompatPolicy.isColdStartPlayKey(keyCode)) {
            return false
        }
        return true
''',
)
regex_replace_once(
    "app/src/main/java/org/oxycblt/auxio/playback/service/MediaButtonActionMapper.kt",
    r'''\n    private fun isPauseOrStop\(keyCode: Int\): Boolean =\n        keyCode == KeyEvent\.KEYCODE_MEDIA_PAUSE \|\| keyCode == KeyEvent\.KEYCODE_MEDIA_STOP\n''',
    "\n",
)

# Feed the mapper the actual focus state. The mapper owns the narrow cold-play exception.
replace_once(
    "app/src/main/java/org/oxycblt/auxio/playback/service/MediaButtonReceiver.kt",
    '''                isFocusHeld =
                    AudioFocusPolicy.shouldHandleMediaButton(
                        isFocusHeld = isFocusHeld,
                        hasCurrentSong = hasCurrentSong,
                        sessionOngoing = hasCurrentSong,
                    ),
''',
    '''                isFocusHeld = isFocusHeld,
''',
)

# A direct MediaController.play() must restore saved state just like the verified Topway callback
# path. PlaybackStateManager remains the only queue/player/audio-focus authority.
replace_once(
    "app/src/main/java/org/oxycblt/auxio/playback/service/MediaSessionInterface.kt",
    '''import org.oxycblt.auxio.playback.state.PlaybackCommand
import org.oxycblt.auxio.playback.state.PlaybackStateManager
''',
    '''import org.oxycblt.auxio.playback.state.DeferredPlayback
import org.oxycblt.auxio.playback.state.PlaybackCommand
import org.oxycblt.auxio.playback.state.PlaybackStateManager
''',
)
replace_once(
    "app/src/main/java/org/oxycblt/auxio/playback/service/MediaSessionInterface.kt",
    '''    override fun onPlay() {
        playbackManager.playing(true)
    }
''',
    '''    override fun onPlay() {
        if (playbackManager.currentSong != null) {
            playbackManager.playing(true)
        } else {
            playbackManager.playDeferred(
                DeferredPlayback.RestoreState(
                    play = true,
                    fallback = DeferredPlayback.ShuffleAll(),
                )
            )
        }
    }
''',
)

# MediaSession construction and initialization: explicit receiver, initial actions/callback, then
# activation. The notification dynamically follows the selected profile while remaining the same
# single builder/foreground notification authority.
media_holder = "app/src/main/java/org/oxycblt/auxio/playback/service/MediaSessionHolder.kt"
replace_once(media_holder, "import android.annotation.SuppressLint\n", "import android.annotation.SuppressLint\nimport android.app.PendingIntent\nimport android.content.ComponentName\n")
replace_once(
    media_holder,
    "import androidx.media.session.MediaButtonReceiver\n",
    "import androidx.media.session.MediaButtonReceiver as AndroidXMediaButtonReceiver\n",
)
replace_once(
    media_holder,
    '''import org.oxycblt.auxio.headunit.compat.HeadUnitMetadataPolicy
''',
    '''import org.oxycblt.auxio.headunit.compat.HeadUnitMetadataPolicy
import org.oxycblt.auxio.headunit.topway.TopwayLauncherIntegrationCoordinator
''',
)
replace_once(
    media_holder,
    '''    private val imageSettings: ImageSettings,
    private val mediaSessionInterface: MediaSessionInterface,
) : PlaybackStateManager.Listener, ImageSettings.Listener {
''',
    '''    private val imageSettings: ImageSettings,
    private val mediaSessionInterface: MediaSessionInterface,
    private val launcherCoordinator: TopwayLauncherIntegrationCoordinator,
) : PlaybackStateManager.Listener, ImageSettings.Listener {
''',
)
replace_once(
    media_holder,
    '''        private val imageSettings: ImageSettings,
        private val mediaSessionInterface: MediaSessionInterface,
    ) {
''',
    '''        private val imageSettings: ImageSettings,
        private val mediaSessionInterface: MediaSessionInterface,
        private val launcherCoordinator: TopwayLauncherIntegrationCoordinator,
    ) {
''',
)
replace_once(
    media_holder,
    '''                imageSettings,
                mediaSessionInterface,
            )
''',
    '''                imageSettings,
                mediaSessionInterface,
                launcherCoordinator,
            )
''',
)
replace_once(
    media_holder,
    '''    private val mediaSession = MediaSessionCompat(context, context.packageName)
    val token: MediaSessionCompat.Token
        get() = mediaSession.sessionToken

    private val artworkRequestToken = AtomicLong()

    private val _notification = PlaybackNotification(context, mediaSession.sessionToken)
''',
    '''    private val mediaButtonReceiver =
        ComponentName(context, org.oxycblt.auxio.playback.service.MediaButtonReceiver::class.java)
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
        PlaybackNotification(context, mediaSession.sessionToken) {
            DofunMediaCompatPolicy.notificationProfile(
                launcherCoordinator.mode,
                BuildConfig.TOPWAY_COMPAT_FLAVOR,
            )
        }
''',
)
replace_once(
    media_holder,
    '''        mediaSession.apply {
            isActive = true
            if (BuildConfig.TOPWAY_COMPAT_FLAVOR) {
''',
    '''        mediaSession.apply {
            setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or
                    MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS or
                    MediaSessionCompat.FLAG_HANDLES_QUEUE_COMMANDS
            )
            setCallback(mediaSessionInterface)
            setPlaybackState(
                PlaybackStateCompat.Builder()
                    .setActions(MediaSessionInterface.ACTIONS)
                    .setState(PlaybackStateCompat.STATE_NONE, 0L, 0f)
                    .build()
            )
            if (BuildConfig.TOPWAY_COMPAT_FLAVOR) {
''',
)
replace_once(
    media_holder,
    '''            setQueueTitle(context.getString(R.string.lbl_queue))
            setCallback(mediaSessionInterface)
        }
''',
    '''            setQueueTitle(context.getString(R.string.lbl_queue))
            isActive = true
        }
''',
)
replace_once(
    media_holder,
    "MediaButtonReceiver.handleIntent(mediaSession, intent) != null",
    "AndroidXMediaButtonReceiver.handleIntent(mediaSession, intent) != null",
)
replace_once(
    media_holder,
    '''        if (!BuildConfig.TOPWAY_COMPAT_FLAVOR) return
''',
    '''        if (
            !BuildConfig.TOPWAY_COMPAT_FLAVOR ||
                !launcherCoordinator.mode.sendsTopwayBroadcasts
        ) {
            return
        }
''',
)
replace_once(
    media_holder,
    '''        if (!BuildConfig.TOPWAY_COMPAT_FLAVOR) return
''',
    '''        if (
            !BuildConfig.TOPWAY_COMPAT_FLAVOR ||
                !launcherCoordinator.mode.sendsTopwayBroadcasts
        ) {
            return
        }
''',
)

new_notification = r'''@SuppressLint("RestrictedApi")
private class PlaybackNotification(
    private val context: Context,
    sessionToken: MediaSessionCompat.Token,
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
            // TS18/DoFun SystemUI crashes when it crops a 1x1 transparent placeholder.
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

    private fun rebuildActions() {
        mActions.clear()
        when (profileProvider()) {
            PlaybackNotificationProfile.GenericDofun -> rebuildGenericActions()
            PlaybackNotificationProfile.RichAuxio -> rebuildRichActions()
        }
    }

    private fun rebuildGenericActions() {
        val keys = DofunMediaCompatPolicy.genericActionKeyCodes(isPlaying)
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
        val stopIntent =
            AndroidXMediaButtonReceiver.buildMediaButtonPendingIntent(
                context,
                android.view.KeyEvent.KEYCODE_MEDIA_STOP,
            )
        setDeleteIntent(stopIntent)
        setOngoing(isPlaying)
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
        NotificationCompat.Action.Builder(
                iconRes,
                title,
                AndroidXMediaButtonReceiver.buildMediaButtonPendingIntent(context, keyCode),
            )
            .build()

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
            ChannelInfo(
                id = PlaybackNotificationChannel.id,
                nameRes = R.string.lbl_playback,
            )
    }
}
'''
regex_replace_once(
    media_holder,
    r'@SuppressLint\("RestrictedApi"\)\nprivate class PlaybackNotification\([\s\S]*\Z',
    new_notification,
)

# Mark actual publication attempts so the user-facing status distinguishes channel state from an
# idle session. This is normal runtime state, not a probe/capture framework.
replace_once(
    "app/src/main/java/org/oxycblt/auxio/AuxioService.kt",
    '''import org.oxycblt.auxio.playback.service.PlaybackServiceFragment
''',
    '''import org.oxycblt.auxio.playback.service.PlaybackNotificationChannel
import org.oxycblt.auxio.playback.service.PlaybackServiceFragment
''',
)
replace_once(
    "app/src/main/java/org/oxycblt/auxio/AuxioService.kt",
    '''            if (change == ForegroundListener.Change.MEDIA_SESSION) {
                startForeground(mediaNotification.code, mediaNotification.build())
            }
''',
    '''            if (change == ForegroundListener.Change.MEDIA_SESSION) {
                PlaybackNotificationChannel.markPublicationRequested()
                startForeground(mediaNotification.code, mediaNotification.build())
            }
''',
)

# Bind the private Topway callback adapter only when the explicitly selected fallback requires it,
# including live preference changes without leaking its worker/binding.
command_client = "app/src/main/java/org/oxycblt/auxio/headunit/topway/TopwayCommandServiceClient.kt"
replace_once(command_client, "import android.content.Context\n", "import android.content.Context\nimport android.content.SharedPreferences\n")
replace_once(command_client, "import androidx.core.content.ContextCompat\n", "import androidx.core.content.ContextCompat\nimport androidx.preference.PreferenceManager\n")
replace_once(
    command_client,
    '''    private val mainHandler = Handler(Looper.getMainLooper())
    private var workerThread: HandlerThread? = null
''',
    '''    private val mainHandler = Handler(Looper.getMainLooper())
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    private var requestedServiceClass: Class<out AuxioService>? = null
    private var preferenceListenerRegistered = false
    private val modePreferenceListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == Ts18LauncherIntegrationMode.PREF_KEY) {
                mainHandler.post(::reconcileMode)
            }
        }
    private var workerThread: HandlerThread? = null
''',
)
regex_replace_once(
    command_client,
    r'''    /\*\* Starts a bounded, idempotent bind for the concrete Auxio service component in use\. \*/\n    @Synchronized\n    fun attach\(serviceClass: Class<out AuxioService>\) \{[\s\S]*?\n    @Synchronized\n    private fun startAttach''',
    '''    /** Tracks the service owner and binds only for an explicitly selected fallback mode. */
    @Synchronized
    fun attach(serviceClass: Class<out AuxioService>) {
        if (!BuildConfig.TOPWAY_COMPAT_FLAVOR) return
        requestedServiceClass = serviceClass
        if (!preferenceListenerRegistered) {
            prefs.registerOnSharedPreferenceChangeListener(modePreferenceListener)
            preferenceListenerRegistered = true
        }
        reconcileMode()
    }

    /** Releases the optional adapter without touching Auxio's canonical Android media stack. */
    @Synchronized
    fun release() {
        if (!BuildConfig.TOPWAY_COMPAT_FLAVOR) return
        requestedServiceClass = null
        pendingServiceClass = null
        if (preferenceListenerRegistered) {
            prefs.unregisterOnSharedPreferenceChangeListener(modePreferenceListener)
            preferenceListenerRegistered = false
        }
        if (attached) {
            beginRelease()
        } else if (!releaseInProgress) {
            stopWorker()
        }
    }

    @Synchronized
    private fun reconcileMode() {
        val serviceClass = requestedServiceClass ?: return
        val mode = coordinator.mode
        if (!mode.bindsTopwayCommandService) {
            pendingServiceClass = null
            if (attached) {
                log("Bind disabled by profile", mode.name)
                beginRelease()
            } else {
                log("Bind suppressed", mode.name)
            }
            return
        }
        if (attached) {
            if (ownerServiceClass != serviceClass) {
                log(
                    "Attach ignored",
                    "active=${ownerServiceClass?.name}; requested=${serviceClass.name}",
                )
            }
            return
        }
        if (releaseInProgress) {
            pendingServiceClass = serviceClass
            log("Attach deferred", serviceClass.name)
            return
        }
        startAttach(serviceClass)
    }

    @Synchronized
    private fun beginRelease() {
        if (!attached) return
        attached = false
        ownerServiceClass = null
        releaseInProgress = true
        mainHandler.removeCallbacks(retryRunnable)
        retryScheduled = false
        log("Release binding")

        val worker = workerHandler
        if (
            worker == null ||
                !worker.post {
                    clearRemote(unregister = true)
                    mainHandler.post(::finishRelease)
                }
        ) {
            mainHandler.post(::finishRelease)
        }
    }

    @Synchronized
    private fun startAttach''',
)
replace_once(
    command_client,
    '''        val pending = pendingServiceClass
        pendingServiceClass = null
        if (pending != null) {
            startAttach(pending)
        } else {
            stopWorker()
        }
''',
    '''        val pending =
            pendingServiceClass?.takeIf {
                requestedServiceClass != null && coordinator.mode.bindsTopwayCommandService
            }
        pendingServiceClass = null
        if (pending != null) {
            startAttach(pending)
        } else {
            stopWorker()
        }
''',
)

# Settings: make the generic profile the default, retain legacy lanes explicitly, and provide a
# functional channel access/status row rather than reviving the abandoned diagnostics UI.
settings = "app/src/main/res/values/settings.xml"
replace_once(
    settings,
    '''    <string name="set_key_topway_seek_unit_policy" translatable="false">auxio_ts18_launcher_seek_unit_policy</string>
''',
    '''    <string name="set_key_topway_seek_unit_policy" translatable="false">auxio_ts18_launcher_seek_unit_policy</string>
    <string name="set_key_playback_notification_access" translatable="false">auxio_playback_notification_access</string>
''',
)
replace_once(
    settings,
    '''    <string-array name="entries_ts18_launcher_integration_mode">
        <item>@string/set_ts18_launcher_integration_mode_auto_all_safe_paths</item>
        <item>@string/set_ts18_launcher_integration_mode_android_media_session_only</item>
''',
    '''    <string-array name="entries_ts18_launcher_integration_mode">
        <item>@string/set_ts18_launcher_integration_mode_generic_dofun_media</item>
        <item>@string/set_ts18_launcher_integration_mode_android_media_session_only</item>
        <item>@string/set_ts18_launcher_integration_mode_auto_all_safe_paths</item>
''',
)
replace_once(
    settings,
    '''    <string-array name="values_ts18_launcher_integration_mode" translatable="false">
        <item>AutoAllSafePaths</item>
        <item>AndroidMediaSessionOnly</item>
''',
    '''    <string-array name="values_ts18_launcher_integration_mode" translatable="false">
        <item>GenericDofunMedia</item>
        <item>AndroidMediaSessionOnly</item>
        <item>AutoAllSafePaths</item>
''',
)

strings = "app/src/main/res/values/strings.xml"
replace_once(
    strings,
    '''    <string name="set_ts18_launcher_integration_mode_desc">Controls safe in-app Android, Topway broadcast, and Topway command paths for DoFun/Topway launchers.</string>
    <string name="set_ts18_launcher_integration_mode_auto_all_safe_paths">Auto / all safe paths</string>
    <string name="set_ts18_launcher_integration_mode_android_media_session_only">Android media session only</string>
''',
    '''    <string name="set_ts18_launcher_integration_mode_desc">Choose the primary Android media path or an explicit legacy Topway fallback. Generic DoFun media is recommended.</string>
    <string name="set_ts18_launcher_integration_mode_generic_dofun_media">Generic DoFun media (recommended)</string>
    <string name="set_ts18_launcher_integration_mode_auto_all_safe_paths">Legacy: all safe paths</string>
    <string name="set_ts18_launcher_integration_mode_android_media_session_only">Android media session only</string>
''',
)
replace_once(
    strings,
    '''    <string name="set_launcher_integration_summary">Automatic — recommended</string>
''',
    '''    <string name="set_launcher_integration_summary">Generic Android media — recommended</string>
    <string name="set_playback_notification_access">Playback notification access</string>
    <string name="set_playback_notification_access_desc">Review the exact playback channel used by DoFun and other media controllers</string>
    <string name="set_playback_notification_access_summary">Package: %1$s\nNotifications: %2$s\nPlayback channel: %3$s\nPublication requested this process: %4$s</string>
    <string name="set_playback_channel_usable">usable (importance %1$s)</string>
    <string name="set_playback_channel_blocked">blocked (importance %1$s)</string>
    <string name="set_playback_channel_not_created">not created yet</string>
    <string name="set_playback_channel_unknown_importance">unknown</string>
    <string name="set_status_yes">yes</string>
    <string name="set_status_no">no</string>
''',
)

prefs = "app/src/topwayCompat/res/xml/preferences_car.xml"
replace_once(
    prefs,
    '''    <PreferenceCategory app:title="@string/set_launcher_integration">
        <Preference app:key="@string/set_key_launcher_integration" app:summary="@string/set_launcher_integration_summary" app:title="@string/set_launcher_integration" />
    </PreferenceCategory>
''',
    '''    <PreferenceCategory app:title="@string/set_launcher_integration">
        <Preference app:key="@string/set_key_launcher_integration" app:summary="@string/set_launcher_integration_summary" app:title="@string/set_launcher_integration" />
        <Preference app:key="@string/set_key_playback_notification_access" app:summary="@string/set_playback_notification_access_desc" app:title="@string/set_playback_notification_access" />
    </PreferenceCategory>
''',
)
replace_once(prefs, 'app:defaultValue="AutoAllSafePaths"', 'app:defaultValue="GenericDofunMedia"')

car_fragment = "app/src/topwayCompat/java/org/oxycblt/auxio/settings/categories/CarPreferenceFragment.kt"
replace_once(
    car_fragment,
    '''import org.oxycblt.auxio.headunit.overlay.CarOverlayContract
''',
    '''import org.oxycblt.auxio.headunit.overlay.CarOverlayContract
import org.oxycblt.auxio.playback.service.PlaybackChannelState
import org.oxycblt.auxio.playback.service.PlaybackNotificationChannel
''',
)
replace_once(
    car_fragment,
    '''            getString(R.string.set_key_launcher_integration) -> setupLauncherIntegration(preference)
''',
    '''            getString(R.string.set_key_launcher_integration) -> setupLauncherIntegration(preference)
            getString(R.string.set_key_playback_notification_access) ->
                setupPlaybackNotificationAccess(preference)
''',
)
replace_once(
    car_fragment,
    '''        findPreference<Preference>(KEY_CAR_OVERLAY_ENABLED)?.let(::setupCarOverlayEnabled)
''',
    '''        findPreference<Preference>(KEY_CAR_OVERLAY_ENABLED)?.let(::setupCarOverlayEnabled)
        findPreference<Preference>(getString(R.string.set_key_playback_notification_access))
            ?.let(::setupPlaybackNotificationAccess)
''',
)
replace_once(
    car_fragment,
    '''    private fun setupLauncherIntegration(preference: Preference) {
        preference.setOnPreferenceClickListener {
            findNavController()
                .navigateSafe(CarPreferenceFragmentDirections.diagnosticsPreferences())
            true
        }
    }

''',
    '''    private fun setupLauncherIntegration(preference: Preference) {
        preference.setOnPreferenceClickListener {
            findNavController()
                .navigateSafe(CarPreferenceFragmentDirections.diagnosticsPreferences())
            true
        }
    }

    private fun setupPlaybackNotificationAccess(preference: Preference) {
        val snapshot = PlaybackNotificationChannel.inspect(requireContext())
        val importance =
            snapshot.importance?.toString()
                ?: getString(R.string.set_playback_channel_unknown_importance)
        val channelSummary =
            when (snapshot.state) {
                PlaybackChannelState.Usable ->
                    getString(R.string.set_playback_channel_usable, importance)
                PlaybackChannelState.Blocked ->
                    getString(R.string.set_playback_channel_blocked, importance)
                PlaybackChannelState.NotCreated ->
                    getString(R.string.set_playback_channel_not_created)
            }
        preference.summary =
            getString(
                R.string.set_playback_notification_access_summary,
                BuildConfig.APPLICATION_ID,
                statusSummary(snapshot.packageNotificationsEnabled),
                channelSummary,
                if (snapshot.publicationRequestedThisProcess) {
                    getString(R.string.set_status_yes)
                } else {
                    getString(R.string.set_status_no)
                },
            )
        preference.setOnPreferenceClickListener {
            startActivity(PlaybackNotificationChannel.settingsIntent(requireContext()))
            true
        }
    }

''',
)

# Unit coverage for profile defaults, exact action order and cold-play policy.
replace_once(
    "app/src/test/java/org/oxycblt/auxio/headunit/topway/Ts18LauncherIntegrationModeTest.kt",
    '''                Ts18LauncherIntegrationMode.AutoAllSafePaths
''',
    '''                Ts18LauncherIntegrationMode.GenericDofunMedia
''',
)
replace_once(
    "app/src/test/java/org/oxycblt/auxio/headunit/topway/Ts18LauncherIntegrationModeTest.kt",
    '''        assertFalse(Ts18LauncherIntegrationMode.Disabled.sendsTopwayBroadcasts)
''',
    '''        assertTrue(Ts18LauncherIntegrationMode.GenericDofunMedia.usesGenericMediaNotification)
        assertFalse(Ts18LauncherIntegrationMode.GenericDofunMedia.sendsTopwayBroadcasts)
        assertFalse(Ts18LauncherIntegrationMode.GenericDofunMedia.handlesTopwayCommands)
        assertFalse(Ts18LauncherIntegrationMode.GenericDofunMedia.bindsTopwayCommandService)
        assertFalse(Ts18LauncherIntegrationMode.Disabled.sendsTopwayBroadcasts)
''',
)
replace_once(
    "app/src/test/java/org/oxycblt/auxio/headunit/topway/Ts18LauncherIntegrationModeTest.kt",
    '''        assertTrue(Ts18LauncherIntegrationMode.AutoAllSafePaths.handlesTopwayCommands)
''',
    '''        assertTrue(Ts18LauncherIntegrationMode.AutoAllSafePaths.handlesTopwayCommands)
        assertTrue(Ts18LauncherIntegrationMode.AutoAllSafePaths.bindsTopwayCommandService)
        assertTrue(Ts18LauncherIntegrationMode.DiagnosticsOnly.bindsTopwayCommandService)
''',
)
replace_once(
    "app/src/test/java/org/oxycblt/auxio/playback/service/MediaButtonActionMapperTest.kt",
    '''    @Test
    fun `rejects media keys when focus is not held`() {
        assertFalse(
            MediaButtonActionMapper.shouldForward(
                action = KeyEvent.ACTION_DOWN,
                keyCode = KeyEvent.KEYCODE_MEDIA_NEXT,
                repeatCount = 0,
                hasCurrentSong = true,
                isFocusHeld = false,
            )
        )
    }
''',
    '''    @Test
    fun `allows only play-like cold start keys when focus is not held`() {
        assertTrue(
            MediaButtonActionMapper.shouldForward(
                action = KeyEvent.ACTION_DOWN,
                keyCode = KeyEvent.KEYCODE_MEDIA_PLAY,
                repeatCount = 0,
                hasCurrentSong = false,
                isFocusHeld = false,
            )
        )
        assertTrue(
            MediaButtonActionMapper.shouldForward(
                action = KeyEvent.ACTION_DOWN,
                keyCode = KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE,
                repeatCount = 0,
                hasCurrentSong = true,
                isFocusHeld = false,
            )
        )
        assertFalse(
            MediaButtonActionMapper.shouldForward(
                action = KeyEvent.ACTION_DOWN,
                keyCode = KeyEvent.KEYCODE_MEDIA_NEXT,
                repeatCount = 0,
                hasCurrentSong = true,
                isFocusHeld = false,
            )
        )
        assertFalse(
            MediaButtonActionMapper.shouldForward(
                action = KeyEvent.ACTION_DOWN,
                keyCode = KeyEvent.KEYCODE_MEDIA_PAUSE,
                repeatCount = 0,
                hasCurrentSong = false,
                isFocusHeld = false,
            )
        )
    }
''',
)

write(
    "app/src/test/java/org/oxycblt/auxio/playback/service/DofunMediaCompatPolicyTest.kt",
    '''/*
 * Copyright (c) 2026 Auxio Project
 * DofunMediaCompatPolicyTest.kt is part of Auxio.
 */

package org.oxycblt.auxio.playback.service

import android.app.NotificationManager
import android.view.KeyEvent
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test
import org.oxycblt.auxio.headunit.topway.Ts18LauncherIntegrationMode

class DofunMediaCompatPolicyTest {
    @Test
    fun `generic profile is isolated to topway generic mode`() {
        assertEquals(
            PlaybackNotificationProfile.GenericDofun,
            DofunMediaCompatPolicy.notificationProfile(
                Ts18LauncherIntegrationMode.GenericDofunMedia,
                topwayCompatFlavor = true,
            ),
        )
        assertEquals(
            PlaybackNotificationProfile.RichAuxio,
            DofunMediaCompatPolicy.notificationProfile(
                Ts18LauncherIntegrationMode.GenericDofunMedia,
                topwayCompatFlavor = false,
            ),
        )
        assertEquals(
            PlaybackNotificationProfile.RichAuxio,
            DofunMediaCompatPolicy.notificationProfile(
                Ts18LauncherIntegrationMode.AutoAllSafePaths,
                topwayCompatFlavor = true,
            ),
        )
    }

    @Test
    fun `generic actions are conventional previous play pause next`() {
        assertArrayEquals(intArrayOf(0, 1, 2), DofunMediaCompatPolicy.compactActionIndices)
        assertArrayEquals(
            intArrayOf(
                KeyEvent.KEYCODE_MEDIA_PREVIOUS,
                KeyEvent.KEYCODE_MEDIA_PLAY,
                KeyEvent.KEYCODE_MEDIA_NEXT,
            ),
            DofunMediaCompatPolicy.genericActionKeyCodes(isPlaying = false),
        )
        assertArrayEquals(
            intArrayOf(
                KeyEvent.KEYCODE_MEDIA_PREVIOUS,
                KeyEvent.KEYCODE_MEDIA_PAUSE,
                KeyEvent.KEYCODE_MEDIA_NEXT,
            ),
            DofunMediaCompatPolicy.genericActionKeyCodes(isPlaying = true),
        )
    }

    @Test
    fun `playback channel classification fails closed`() {
        assertEquals(
            PlaybackChannelState.Blocked,
            PlaybackNotificationChannel.classify(
                notificationsEnabled = false,
                channelExists = true,
                importance = NotificationManager.IMPORTANCE_LOW,
            ),
        )
        assertEquals(
            PlaybackChannelState.NotCreated,
            PlaybackNotificationChannel.classify(
                notificationsEnabled = true,
                channelExists = false,
                importance = null,
            ),
        )
        assertEquals(
            PlaybackChannelState.Blocked,
            PlaybackNotificationChannel.classify(
                notificationsEnabled = true,
                channelExists = true,
                importance = NotificationManager.IMPORTANCE_NONE,
            ),
        )
        assertEquals(
            PlaybackChannelState.Usable,
            PlaybackNotificationChannel.classify(
                notificationsEnabled = true,
                channelExists = true,
                importance = NotificationManager.IMPORTANCE_LOW,
            ),
        )
    }
}
''',
)

# Extend the maintained compatibility guard rather than weakening it.
replace_once(
    "scripts/check-dofun-topway-compat.sh",
    '''require_file_contains "$mode_file" "AutoAllSafePaths" "launcher integration auto/all-safe-paths mode"
''',
    '''require_file_contains "$mode_file" "GenericDofunMedia" "launcher integration generic DoFun media mode"
require_file_contains "$mode_file" "usesGenericMediaNotification" "launcher integration generic notification gate"
require_file_contains "$mode_file" "bindsTopwayCommandService" "launcher integration command-service bind gate"
require_file_contains "$mode_file" "AutoAllSafePaths" "launcher integration legacy all-safe-paths mode"
''',
)
replace_once(
    "scripts/check-dofun-topway-compat.sh",
    '''require_file_contains "$mode_file" "handlesTopwayCommands" "launcher integration incoming mode flag"
''',
    '''require_file_contains "$mode_file" "handlesTopwayCommands" "launcher integration incoming mode flag"
require_file_contains "app/src/main/java/org/oxycblt/auxio/playback/service/MediaSessionHolder.kt" "DofunMediaCompatPolicy.compactActionIndices" "generic DoFun compact notification actions"
require_file_contains "app/src/main/java/org/oxycblt/auxio/playback/service/MediaSessionHolder.kt" "AndroidXMediaButtonReceiver.buildMediaButtonPendingIntent" "generic DoFun AndroidX media-button pending intents"
require_file_contains "app/src/main/java/org/oxycblt/auxio/playback/service/PlaybackNotificationChannel.kt" "ACTION_CHANNEL_NOTIFICATION_SETTINGS" "playback channel settings recovery path"
''',
)

# Documentation language: user-visible channel status is functional recovery, not the abandoned
# in-app capture framework.
spec = "docs/ts18/launcher-integration/DOFUN_GENERIC_MEDIA_COMPAT_IMPLEMENTATION.md"
replace_once(
    spec,
    "Add a bounded, user-visible diagnostic that reports:",
    "Add a bounded, user-visible playback-notification status row that reports:",
)

# Basic patch integrity checks before the workflow commits anything.
required = {
    "app/src/main/java/org/oxycblt/auxio/playback/service/MediaSessionHolder.kt": [
        "PlaybackNotificationProfile.GenericDofun",
        "FLAG_HANDLES_MEDIA_BUTTONS",
        "AndroidXMediaButtonReceiver.buildMediaButtonPendingIntent",
    ],
    "app/src/main/java/org/oxycblt/auxio/headunit/topway/TopwayCommandServiceClient.kt": [
        "Bind suppressed",
        "bindsTopwayCommandService",
        "registerOnSharedPreferenceChangeListener",
    ],
    "app/src/topwayCompat/res/xml/preferences_car.xml": [
        "GenericDofunMedia",
        "set_key_playback_notification_access",
    ],
}
for path, needles in required.items():
    content = read(path)
    for needle in needles:
        if needle not in content:
            raise RuntimeError(f"{path}: missing required generated marker {needle!r}")

print("PR #195 generic media compatibility patch applied successfully")
'''
