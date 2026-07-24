#!/usr/bin/env python3
"""Apply the final scoped PR #195 launcher-media hardening edits."""

from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]


def replace_once(path: str, old: str, new: str) -> None:
    target = ROOT / path
    content = target.read_text(encoding="utf-8")
    count = content.count(old)
    if count != 1:
        raise RuntimeError(f"{path}: expected one match, found {count}: {old[:120]!r}")
    target.write_text(content.replace(old, new, 1), encoding="utf-8")


holder = "app/src/main/java/org/oxycblt/auxio/playback/service/MediaSessionHolder.kt"
replace_once(
    holder,
    "import android.content.Intent\n",
    "import android.content.Intent\nimport android.content.SharedPreferences\n",
)
replace_once(
    holder,
    "import android.net.Uri\n",
    "import android.net.Uri\nimport android.os.Handler\nimport android.os.Looper\n",
)
replace_once(
    holder,
    "import androidx.media.session.MediaButtonReceiver as AndroidXMediaButtonReceiver\n",
    "import androidx.media.session.MediaButtonReceiver as AndroidXMediaButtonReceiver\nimport androidx.preference.PreferenceManager\n",
)
replace_once(
    holder,
    "import org.oxycblt.auxio.BuildConfig\n",
    "import org.oxycblt.auxio.AuxioService\nimport org.oxycblt.auxio.BuildConfig\n",
)
replace_once(
    holder,
    "import org.oxycblt.auxio.headunit.topway.TopwayLauncherIntegrationCoordinator\n",
    "import org.oxycblt.auxio.headunit.topway.TopwayLauncherIntegrationCoordinator\n"
    "import org.oxycblt.auxio.headunit.topway.TopwayServiceBridge\n"
    "import org.oxycblt.auxio.headunit.topway.Ts18LauncherIntegrationMode\n",
)
replace_once(
    holder,
    """    private val mediaButtonReceiver = MediaButtonIntentFactory.receiverComponent(context)
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
        PlaybackNotification(context, mediaSession.sessionToken, mediaButtonReceiver) {
            DofunMediaCompatPolicy.notificationProfile(
                launcherCoordinator.mode,
                BuildConfig.TOPWAY_COMPAT_FLAVOR,
            )
        }
    val notification: ForegroundServiceNotification
        get() = _notification
""",
    """    private val mainHandler = Handler(Looper.getMainLooper())
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
                BuildConfig.TOPWAY_COMPAT_FLAVOR,
            )
        }
    val notification: ForegroundServiceNotification
        get() = _notification

    private var attached = false
    private val modePreferenceListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key != Ts18LauncherIntegrationMode.PREF_KEY) return@OnSharedPreferenceChangeListener
            mainHandler.post {
                if (!attached) return@post
                _notification.refreshProfile()
                launcherCoordinator.refreshWidgetControls("mode-preference-change")
                foregroundListener.updateForeground(ForegroundListener.Change.MEDIA_SESSION)
            }
        }
""",
)
replace_once(
    holder,
    """    fun attach() {
        playbackManager.addListener(this)
        imageSettings.registerListener(this)
        mediaSession.apply {
            setFlags(MediaSessionInitializationPolicy.FLAGS)
            setCallback(mediaSessionInterface)
            setPlaybackState(MediaSessionInitializationPolicy.initialPlaybackState())
            if (BuildConfig.TOPWAY_COMPAT_FLAVOR) {
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
            isActive = true
        }
    }
""",
    """    fun attach() {
        mediaSession.apply {
            setFlags(MediaSessionInitializationPolicy.FLAGS)
            setCallback(mediaSessionInterface)
            setPlaybackState(MediaSessionInitializationPolicy.initialPlaybackState())
            if (BuildConfig.TOPWAY_COMPAT_FLAVOR) {
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
        attached = true
        prefs.registerOnSharedPreferenceChangeListener(modePreferenceListener)
        playbackManager.addListener(this)
        imageSettings.registerListener(this)
        mediaSession.isActive = true
    }
""",
)
replace_once(
    holder,
    """    fun release() {
        // Clear published state before shutdown so external controllers do not keep stale metadata.
        artworkRequestToken.incrementAndGet()
        mediaSession.setMetadata(emptyMetadata)
        _notification.updateMetadata(emptyMetadata)
        bitmapProvider.release()
        playbackManager.removeListener(this)
        imageSettings.unregisterListener(this)
        mediaSession.apply {
            isActive = false
            release()
        }
    }
""",
    """    fun release() {
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
        mediaSession.apply {
            isActive = false
            release()
        }
    }
""",
)
replace_once(
    holder,
    """private class PlaybackNotification(
    private val context: Context,
    sessionToken: MediaSessionCompat.Token,
    private val mediaButtonReceiver: ComponentName,
    private val profileProvider: () -> PlaybackNotificationProfile,
) : ForegroundServiceNotification(context, CHANNEL_INFO) {
""",
    """private class PlaybackNotification(
    private val context: Context,
    sessionToken: MediaSessionCompat.Token,
    private val canonicalServiceClass: Class<*>,
    private val profileProvider: () -> PlaybackNotificationProfile,
) : ForegroundServiceNotification(context, CHANNEL_INFO) {
""",
)
replace_once(
    holder,
    """    fun updateShuffled(isShuffled: Boolean) {
        L.d("Applying shuffle action: $isShuffled")
        this.isShuffled = isShuffled
        rebuildActions()
    }

    private fun rebuildActions() {
""",
    """    fun updateShuffled(isShuffled: Boolean) {
        L.d("Applying shuffle action: $isShuffled")
        this.isShuffled = isShuffled
        rebuildActions()
    }

    fun refreshProfile() {
        L.i("Refreshing playback notification profile")
        rebuildActions()
    }

    private fun rebuildActions() {
""",
)
replace_once(
    holder,
    """    private fun buildMediaButtonPendingIntent(keyCode: Int): PendingIntent =
        PendingIntent.getBroadcast(
            context,
            keyCode,
            MediaButtonIntentFactory.receiverIntent(context, keyCode),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )
""",
    """    private fun buildMediaButtonPendingIntent(keyCode: Int): PendingIntent =
        PendingIntent.getService(
            context,
            keyCode,
            MediaButtonIntentFactory.serviceIntent(context, canonicalServiceClass, keyCode),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )
""",
)

widget = "app/src/topwayCompat/java/com/tw/music/view/MusicWidgetProvider.kt"
replace_once(
    widget,
    """    private fun currentIntegrationMode(context: Context): Ts18LauncherIntegrationMode {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return Ts18LauncherIntegrationMode.fromPreference(
            prefs.getString(Ts18LauncherIntegrationMode.PREF_KEY, null)
        )
    }
""",
    """    private fun currentIntegrationMode(context: Context): Ts18LauncherIntegrationMode {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return Ts18LauncherIntegrationMode.resolveAndPersist(
                prefs = prefs,
                topwayCompatFlavor = true,
            )
            .mode
    }
""",
)

for path, markers in {
    holder: [
        "setPlaybackState(MediaSessionInitializationPolicy.initialPlaybackState())",
        "prefs.registerOnSharedPreferenceChangeListener(modePreferenceListener)",
        "_notification.refreshProfile()",
        "PendingIntent.getService(",
        "MediaButtonIntentFactory.serviceIntent(context, canonicalServiceClass, keyCode)",
    ],
    widget: ["Ts18LauncherIntegrationMode.resolveAndPersist("],
}.items():
    content = (ROOT / path).read_text(encoding="utf-8")
    for marker in markers:
        if marker not in content:
            raise RuntimeError(f"{path}: missing hardened marker {marker!r}")

print("PR #195 launcher-media hardening edits applied")
