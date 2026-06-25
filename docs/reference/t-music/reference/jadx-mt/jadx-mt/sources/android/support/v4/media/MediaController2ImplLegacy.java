package android.support.v4.media;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.support.annotation.GuardedBy;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.mediacompat.Rating2;
import android.support.v4.app.BundleCompat;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaController2;
import android.support.v4.media.MediaSession2;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import java.util.List;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes3.dex */
@TargetApi(16)
class MediaController2ImplLegacy implements MediaController2.SupportLibraryImpl {

    @GuardedBy("mLock")
    private SessionCommandGroup2 mAllowedCommands;

    @GuardedBy("mLock")
    private MediaBrowserCompat mBrowserCompat;

    @GuardedBy("mLock")
    private int mBufferingState;
    private final MediaController2.ControllerCallback mCallback;
    private final Executor mCallbackExecutor;

    @GuardedBy("mLock")
    private volatile boolean mConnected;
    private final Context mContext;

    @GuardedBy("mLock")
    private MediaControllerCompat mControllerCompat;

    @GuardedBy("mLock")
    private ControllerCompatCallback mControllerCompatCallback;

    @GuardedBy("mLock")
    private MediaItem2 mCurrentMediaItem;
    private final Handler mHandler;
    private MediaController2 mInstance;

    @GuardedBy("mLock")
    private boolean mIsReleased;

    @GuardedBy("mLock")
    private MediaMetadataCompat mMediaMetadataCompat;

    @GuardedBy("mLock")
    private MediaController2.PlaybackInfo mPlaybackInfo;

    @GuardedBy("mLock")
    private PlaybackStateCompat mPlaybackStateCompat;

    @GuardedBy("mLock")
    private int mPlayerState;

    @GuardedBy("mLock")
    private List<MediaItem2> mPlaylist;

    @GuardedBy("mLock")
    private MediaMetadata2 mPlaylistMetadata;

    @GuardedBy("mLock")
    private int mRepeatMode;

    @GuardedBy("mLock")
    private int mShuffleMode;
    private final SessionToken2 mToken;
    private static final String TAG = "MC2ImplLegacy";
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    static final Bundle sDefaultRootExtras = new Bundle();
    final Object mLock = new Object();
    private final HandlerThread mHandlerThread = new HandlerThread("MediaController2_Thread");

    private class ConnectionCallback extends MediaBrowserCompat.ConnectionCallback {
        private ConnectionCallback() {
        }

        @Override // android.support.v4.media.MediaBrowserCompat.ConnectionCallback
        public void onConnected() {
            MediaBrowserCompat browserCompat = MediaController2ImplLegacy.this.getBrowserCompat();
            if (browserCompat != null) {
                MediaController2ImplLegacy.this.connectToSession(browserCompat.getSessionToken());
            } else if (MediaController2ImplLegacy.DEBUG) {
                Log.d(MediaController2ImplLegacy.TAG, "Controller is closed prematually", new IllegalStateException());
            }
        }

        @Override // android.support.v4.media.MediaBrowserCompat.ConnectionCallback
        public void onConnectionFailed() {
            MediaController2ImplLegacy.this.close();
        }

        @Override // android.support.v4.media.MediaBrowserCompat.ConnectionCallback
        public void onConnectionSuspended() {
            MediaController2ImplLegacy.this.close();
        }
    }

    private final class ControllerCompatCallback extends MediaControllerCompat.Callback {
        private ControllerCompatCallback() {
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.Callback
        public void onMetadataChanged(MediaMetadataCompat mediaMetadataCompat) {
            synchronized (MediaController2ImplLegacy.this.mLock) {
                MediaController2ImplLegacy.this.mMediaMetadataCompat = mediaMetadataCompat;
            }
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.Callback
        public void onPlaybackStateChanged(PlaybackStateCompat playbackStateCompat) {
            synchronized (MediaController2ImplLegacy.this.mLock) {
                MediaController2ImplLegacy.this.mPlaybackStateCompat = playbackStateCompat;
            }
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.Callback
        public void onSessionDestroyed() {
            MediaController2ImplLegacy.this.close();
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.Callback
        public void onSessionEvent(String str, Bundle bundle) {
            if (bundle != null) {
                bundle.setClassLoader(MediaSession2.class.getClassLoader());
            }
            switch (str) {
                case "android.support.v4.media.session.event.ON_ALLOWED_COMMANDS_CHANGED":
                    final SessionCommandGroup2 sessionCommandGroup2FromBundle = SessionCommandGroup2.fromBundle(bundle.getBundle("android.support.v4.media.argument.ALLOWED_COMMANDS"));
                    synchronized (MediaController2ImplLegacy.this.mLock) {
                        MediaController2ImplLegacy.this.mAllowedCommands = sessionCommandGroup2FromBundle;
                        break;
                    }
                    MediaController2ImplLegacy.this.mCallbackExecutor.execute(new Runnable() { // from class: android.support.v4.media.MediaController2ImplLegacy.ControllerCompatCallback.2
                        @Override // java.lang.Runnable
                        public void run() {
                            MediaController2ImplLegacy.this.mCallback.onAllowedCommandsChanged(MediaController2ImplLegacy.this.mInstance, sessionCommandGroup2FromBundle);
                        }
                    });
                    return;
                case "android.support.v4.media.session.event.ON_PLAYER_STATE_CHANGED":
                    final int i = bundle.getInt("android.support.v4.media.argument.PLAYER_STATE");
                    PlaybackStateCompat playbackStateCompat = (PlaybackStateCompat) bundle.getParcelable("android.support.v4.media.argument.PLAYBACK_STATE_COMPAT");
                    if (playbackStateCompat == null) {
                        return;
                    }
                    synchronized (MediaController2ImplLegacy.this.mLock) {
                        MediaController2ImplLegacy.this.mPlayerState = i;
                        MediaController2ImplLegacy.this.mPlaybackStateCompat = playbackStateCompat;
                        break;
                    }
                    MediaController2ImplLegacy.this.mCallbackExecutor.execute(new Runnable() { // from class: android.support.v4.media.MediaController2ImplLegacy.ControllerCompatCallback.3
                        @Override // java.lang.Runnable
                        public void run() {
                            MediaController2ImplLegacy.this.mCallback.onPlayerStateChanged(MediaController2ImplLegacy.this.mInstance, i);
                        }
                    });
                    return;
                case "android.support.v4.media.session.event.ON_CURRENT_MEDIA_ITEM_CHANGED":
                    final MediaItem2 mediaItem2FromBundle = MediaItem2.fromBundle(bundle.getBundle("android.support.v4.media.argument.MEDIA_ITEM"));
                    synchronized (MediaController2ImplLegacy.this.mLock) {
                        MediaController2ImplLegacy.this.mCurrentMediaItem = mediaItem2FromBundle;
                        break;
                    }
                    MediaController2ImplLegacy.this.mCallbackExecutor.execute(new Runnable() { // from class: android.support.v4.media.MediaController2ImplLegacy.ControllerCompatCallback.4
                        @Override // java.lang.Runnable
                        public void run() {
                            MediaController2ImplLegacy.this.mCallback.onCurrentMediaItemChanged(MediaController2ImplLegacy.this.mInstance, mediaItem2FromBundle);
                        }
                    });
                    return;
                case "android.support.v4.media.session.event.ON_ERROR":
                    final int i2 = bundle.getInt("android.support.v4.media.argument.ERROR_CODE");
                    final Bundle bundle2 = bundle.getBundle("android.support.v4.media.argument.EXTRAS");
                    MediaController2ImplLegacy.this.mCallbackExecutor.execute(new Runnable() { // from class: android.support.v4.media.MediaController2ImplLegacy.ControllerCompatCallback.5
                        @Override // java.lang.Runnable
                        public void run() {
                            MediaController2ImplLegacy.this.mCallback.onError(MediaController2ImplLegacy.this.mInstance, i2, bundle2);
                        }
                    });
                    return;
                case "android.support.v4.media.session.event.ON_ROUTES_INFO_CHANGED":
                    final List<Bundle> listConvertToBundleList = MediaUtils2.convertToBundleList(bundle.getParcelableArray("android.support.v4.media.argument.ROUTE_BUNDLE"));
                    MediaController2ImplLegacy.this.mCallbackExecutor.execute(new Runnable() { // from class: android.support.v4.media.MediaController2ImplLegacy.ControllerCompatCallback.6
                        @Override // java.lang.Runnable
                        public void run() {
                            MediaController2ImplLegacy.this.mCallback.onRoutesInfoChanged(MediaController2ImplLegacy.this.mInstance, listConvertToBundleList);
                        }
                    });
                    return;
                case "android.support.v4.media.session.event.ON_PLAYLIST_CHANGED":
                    final MediaMetadata2 mediaMetadata2FromBundle = MediaMetadata2.fromBundle(bundle.getBundle("android.support.v4.media.argument.PLAYLIST_METADATA"));
                    final List<MediaItem2> listConvertToMediaItem2List = MediaUtils2.convertToMediaItem2List(bundle.getParcelableArray("android.support.v4.media.argument.PLAYLIST"));
                    synchronized (MediaController2ImplLegacy.this.mLock) {
                        MediaController2ImplLegacy.this.mPlaylist = listConvertToMediaItem2List;
                        MediaController2ImplLegacy.this.mPlaylistMetadata = mediaMetadata2FromBundle;
                        break;
                    }
                    MediaController2ImplLegacy.this.mCallbackExecutor.execute(new Runnable() { // from class: android.support.v4.media.MediaController2ImplLegacy.ControllerCompatCallback.7
                        @Override // java.lang.Runnable
                        public void run() {
                            MediaController2ImplLegacy.this.mCallback.onPlaylistChanged(MediaController2ImplLegacy.this.mInstance, listConvertToMediaItem2List, mediaMetadata2FromBundle);
                        }
                    });
                    return;
                case "android.support.v4.media.session.event.ON_PLAYLIST_METADATA_CHANGED":
                    final MediaMetadata2 mediaMetadata2FromBundle2 = MediaMetadata2.fromBundle(bundle.getBundle("android.support.v4.media.argument.PLAYLIST_METADATA"));
                    synchronized (MediaController2ImplLegacy.this.mLock) {
                        MediaController2ImplLegacy.this.mPlaylistMetadata = mediaMetadata2FromBundle2;
                        break;
                    }
                    MediaController2ImplLegacy.this.mCallbackExecutor.execute(new Runnable() { // from class: android.support.v4.media.MediaController2ImplLegacy.ControllerCompatCallback.8
                        @Override // java.lang.Runnable
                        public void run() {
                            MediaController2ImplLegacy.this.mCallback.onPlaylistMetadataChanged(MediaController2ImplLegacy.this.mInstance, mediaMetadata2FromBundle2);
                        }
                    });
                    return;
                case "android.support.v4.media.session.event.ON_REPEAT_MODE_CHANGED":
                    final int i3 = bundle.getInt("android.support.v4.media.argument.REPEAT_MODE");
                    synchronized (MediaController2ImplLegacy.this.mLock) {
                        MediaController2ImplLegacy.this.mRepeatMode = i3;
                        break;
                    }
                    MediaController2ImplLegacy.this.mCallbackExecutor.execute(new Runnable() { // from class: android.support.v4.media.MediaController2ImplLegacy.ControllerCompatCallback.9
                        @Override // java.lang.Runnable
                        public void run() {
                            MediaController2ImplLegacy.this.mCallback.onRepeatModeChanged(MediaController2ImplLegacy.this.mInstance, i3);
                        }
                    });
                    return;
                case "android.support.v4.media.session.event.ON_SHUFFLE_MODE_CHANGED":
                    final int i4 = bundle.getInt("android.support.v4.media.argument.SHUFFLE_MODE");
                    synchronized (MediaController2ImplLegacy.this.mLock) {
                        MediaController2ImplLegacy.this.mShuffleMode = i4;
                        break;
                    }
                    MediaController2ImplLegacy.this.mCallbackExecutor.execute(new Runnable() { // from class: android.support.v4.media.MediaController2ImplLegacy.ControllerCompatCallback.10
                        @Override // java.lang.Runnable
                        public void run() {
                            MediaController2ImplLegacy.this.mCallback.onShuffleModeChanged(MediaController2ImplLegacy.this.mInstance, i4);
                        }
                    });
                    return;
                case "android.support.v4.media.session.event.SEND_CUSTOM_COMMAND":
                    Bundle bundle3 = bundle.getBundle("android.support.v4.media.argument.CUSTOM_COMMAND");
                    if (bundle3 == null) {
                        return;
                    }
                    final SessionCommand2 sessionCommand2FromBundle = SessionCommand2.fromBundle(bundle3);
                    final Bundle bundle4 = bundle.getBundle("android.support.v4.media.argument.ARGUMENTS");
                    final ResultReceiver resultReceiver = (ResultReceiver) bundle.getParcelable("android.support.v4.media.argument.RESULT_RECEIVER");
                    MediaController2ImplLegacy.this.mCallbackExecutor.execute(new Runnable() { // from class: android.support.v4.media.MediaController2ImplLegacy.ControllerCompatCallback.11
                        @Override // java.lang.Runnable
                        public void run() {
                            MediaController2ImplLegacy.this.mCallback.onCustomCommand(MediaController2ImplLegacy.this.mInstance, sessionCommand2FromBundle, bundle4, resultReceiver);
                        }
                    });
                    return;
                case "android.support.v4.media.session.event.SET_CUSTOM_LAYOUT":
                    final List<MediaSession2.CommandButton> listConvertToCommandButtonList = MediaUtils2.convertToCommandButtonList(bundle.getParcelableArray("android.support.v4.media.argument.COMMAND_BUTTONS"));
                    if (listConvertToCommandButtonList == null) {
                        return;
                    }
                    MediaController2ImplLegacy.this.mCallbackExecutor.execute(new Runnable() { // from class: android.support.v4.media.MediaController2ImplLegacy.ControllerCompatCallback.12
                        @Override // java.lang.Runnable
                        public void run() {
                            MediaController2ImplLegacy.this.mCallback.onCustomLayoutChanged(MediaController2ImplLegacy.this.mInstance, listConvertToCommandButtonList);
                        }
                    });
                    return;
                case "android.support.v4.media.session.event.ON_PLAYBACK_INFO_CHANGED":
                    final MediaController2.PlaybackInfo playbackInfoFromBundle = MediaController2.PlaybackInfo.fromBundle(bundle.getBundle("android.support.v4.media.argument.PLAYBACK_INFO"));
                    if (playbackInfoFromBundle == null) {
                        return;
                    }
                    synchronized (MediaController2ImplLegacy.this.mLock) {
                        MediaController2ImplLegacy.this.mPlaybackInfo = playbackInfoFromBundle;
                        break;
                    }
                    MediaController2ImplLegacy.this.mCallbackExecutor.execute(new Runnable() { // from class: android.support.v4.media.MediaController2ImplLegacy.ControllerCompatCallback.13
                        @Override // java.lang.Runnable
                        public void run() {
                            MediaController2ImplLegacy.this.mCallback.onPlaybackInfoChanged(MediaController2ImplLegacy.this.mInstance, playbackInfoFromBundle);
                        }
                    });
                    return;
                case "android.support.v4.media.session.event.ON_PLAYBACK_SPEED_CHANGED":
                    final PlaybackStateCompat playbackStateCompat2 = (PlaybackStateCompat) bundle.getParcelable("android.support.v4.media.argument.PLAYBACK_STATE_COMPAT");
                    if (playbackStateCompat2 == null) {
                        return;
                    }
                    synchronized (MediaController2ImplLegacy.this.mLock) {
                        MediaController2ImplLegacy.this.mPlaybackStateCompat = playbackStateCompat2;
                        break;
                    }
                    MediaController2ImplLegacy.this.mCallbackExecutor.execute(new Runnable() { // from class: android.support.v4.media.MediaController2ImplLegacy.ControllerCompatCallback.14
                        @Override // java.lang.Runnable
                        public void run() {
                            MediaController2ImplLegacy.this.mCallback.onPlaybackSpeedChanged(MediaController2ImplLegacy.this.mInstance, playbackStateCompat2.getPlaybackSpeed());
                        }
                    });
                    return;
                case "android.support.v4.media.session.event.ON_BUFFERING_STATE_CHANGED":
                    final MediaItem2 mediaItem2FromBundle2 = MediaItem2.fromBundle(bundle.getBundle("android.support.v4.media.argument.MEDIA_ITEM"));
                    final int i5 = bundle.getInt("android.support.v4.media.argument.BUFFERING_STATE");
                    PlaybackStateCompat playbackStateCompat3 = (PlaybackStateCompat) bundle.getParcelable("android.support.v4.media.argument.PLAYBACK_STATE_COMPAT");
                    if (mediaItem2FromBundle2 == null || playbackStateCompat3 == null) {
                        return;
                    }
                    synchronized (MediaController2ImplLegacy.this.mLock) {
                        MediaController2ImplLegacy.this.mBufferingState = i5;
                        MediaController2ImplLegacy.this.mPlaybackStateCompat = playbackStateCompat3;
                        break;
                    }
                    MediaController2ImplLegacy.this.mCallbackExecutor.execute(new Runnable() { // from class: android.support.v4.media.MediaController2ImplLegacy.ControllerCompatCallback.15
                        @Override // java.lang.Runnable
                        public void run() {
                            MediaController2ImplLegacy.this.mCallback.onBufferingStateChanged(MediaController2ImplLegacy.this.mInstance, mediaItem2FromBundle2, i5);
                        }
                    });
                    return;
                case "android.support.v4.media.session.event.ON_SEEK_COMPLETED":
                    final long j = bundle.getLong("android.support.v4.media.argument.SEEK_POSITION");
                    PlaybackStateCompat playbackStateCompat4 = (PlaybackStateCompat) bundle.getParcelable("android.support.v4.media.argument.PLAYBACK_STATE_COMPAT");
                    if (playbackStateCompat4 == null) {
                        return;
                    }
                    synchronized (MediaController2ImplLegacy.this.mLock) {
                        MediaController2ImplLegacy.this.mPlaybackStateCompat = playbackStateCompat4;
                        break;
                    }
                    MediaController2ImplLegacy.this.mCallbackExecutor.execute(new Runnable() { // from class: android.support.v4.media.MediaController2ImplLegacy.ControllerCompatCallback.16
                        @Override // java.lang.Runnable
                        public void run() {
                            MediaController2ImplLegacy.this.mCallback.onSeekCompleted(MediaController2ImplLegacy.this.mInstance, j);
                        }
                    });
                    return;
                default:
                    return;
            }
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.Callback
        public void onSessionReady() {
            MediaController2ImplLegacy mediaController2ImplLegacy = MediaController2ImplLegacy.this;
            mediaController2ImplLegacy.sendCommand("android.support.v4.media.controller.command.CONNECT", new ResultReceiver(mediaController2ImplLegacy.mHandler) { // from class: android.support.v4.media.MediaController2ImplLegacy.ControllerCompatCallback.1
                @Override // android.os.ResultReceiver
                protected void onReceiveResult(int i, Bundle bundle) {
                    if (MediaController2ImplLegacy.this.mHandlerThread.isAlive()) {
                        if (i == -1) {
                            MediaController2ImplLegacy.this.mCallbackExecutor.execute(new Runnable() { // from class: android.support.v4.media.MediaController2ImplLegacy.ControllerCompatCallback.1.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    MediaController2ImplLegacy.this.mCallback.onDisconnected(MediaController2ImplLegacy.this.mInstance);
                                }
                            });
                            MediaController2ImplLegacy.this.close();
                        } else {
                            if (i != 0) {
                                return;
                            }
                            MediaController2ImplLegacy.this.onConnectedNotLocked(bundle);
                        }
                    }
                }
            });
        }
    }

    static {
        sDefaultRootExtras.putBoolean("android.support.v4.media.root_default_root", true);
    }

    MediaController2ImplLegacy(@NonNull Context context, @NonNull MediaController2 mediaController2, @NonNull SessionToken2 sessionToken2, @NonNull Executor executor, @NonNull MediaController2.ControllerCallback controllerCallback) {
        this.mContext = context;
        this.mInstance = mediaController2;
        this.mHandlerThread.start();
        this.mHandler = new Handler(this.mHandlerThread.getLooper());
        this.mToken = sessionToken2;
        this.mCallback = controllerCallback;
        this.mCallbackExecutor = executor;
        if (this.mToken.getType() != 0) {
            connectToService();
            return;
        }
        synchronized (this.mLock) {
            this.mBrowserCompat = null;
        }
        connectToSession((MediaSessionCompat.Token) this.mToken.getBinder());
    }

    private void connectToService() {
        this.mCallbackExecutor.execute(new Runnable() { // from class: android.support.v4.media.MediaController2ImplLegacy.4
            @Override // java.lang.Runnable
            public void run() {
                synchronized (MediaController2ImplLegacy.this.mLock) {
                    MediaController2ImplLegacy.this.mBrowserCompat = new MediaBrowserCompat(MediaController2ImplLegacy.this.mContext, MediaController2ImplLegacy.this.mToken.getComponentName(), new ConnectionCallback(), MediaController2ImplLegacy.sDefaultRootExtras);
                    MediaController2ImplLegacy.this.mBrowserCompat.connect();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void connectToSession(MediaSessionCompat.Token token) {
        MediaControllerCompat mediaControllerCompat;
        try {
            mediaControllerCompat = new MediaControllerCompat(this.mContext, token);
        } catch (RemoteException e) {
            e.printStackTrace();
            mediaControllerCompat = null;
        }
        synchronized (this.mLock) {
            this.mControllerCompat = mediaControllerCompat;
            this.mControllerCompatCallback = new ControllerCompatCallback();
            this.mControllerCompat.registerCallback(this.mControllerCompatCallback, this.mHandler);
        }
        if (mediaControllerCompat.isSessionReady()) {
            sendCommand("android.support.v4.media.controller.command.CONNECT", new ResultReceiver(this.mHandler) { // from class: android.support.v4.media.MediaController2ImplLegacy.3
                @Override // android.os.ResultReceiver
                protected void onReceiveResult(int i, Bundle bundle) {
                    if (MediaController2ImplLegacy.this.mHandlerThread.isAlive()) {
                        if (i == -1) {
                            MediaController2ImplLegacy.this.mCallbackExecutor.execute(new Runnable() { // from class: android.support.v4.media.MediaController2ImplLegacy.3.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    MediaController2ImplLegacy.this.mCallback.onDisconnected(MediaController2ImplLegacy.this.mInstance);
                                }
                            });
                            MediaController2ImplLegacy.this.close();
                        } else {
                            if (i != 0) {
                                return;
                            }
                            MediaController2ImplLegacy.this.onConnectedNotLocked(bundle);
                        }
                    }
                }
            });
        }
    }

    private void sendCommand(int i) {
        sendCommand(i, (Bundle) null);
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    public void addPlaylistItem(int i, @NonNull MediaItem2 mediaItem2) {
        Bundle bundle = new Bundle();
        bundle.putInt("android.support.v4.media.argument.PLAYLIST_INDEX", i);
        bundle.putBundle("android.support.v4.media.argument.MEDIA_ITEM", mediaItem2.toBundle());
        sendCommand(15, bundle);
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    public void adjustVolume(int i, int i2) {
        synchronized (this.mLock) {
            if (!this.mConnected) {
                Log.w(TAG, "Session isn't active", new IllegalStateException());
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putInt("android.support.v4.media.argument.VOLUME_DIRECTION", i);
            bundle.putInt("android.support.v4.media.argument.VOLUME_FLAGS", i2);
            sendCommand(11, bundle);
        }
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        if (DEBUG) {
            Log.d(TAG, "release from " + this.mToken);
        }
        synchronized (this.mLock) {
            if (this.mIsReleased) {
                return;
            }
            this.mHandler.removeCallbacksAndMessages(null);
            if (Build.VERSION.SDK_INT >= 18) {
                this.mHandlerThread.quitSafely();
            } else {
                this.mHandlerThread.quit();
            }
            this.mIsReleased = true;
            sendCommand("android.support.v4.media.controller.command.DISCONNECT");
            if (this.mControllerCompat != null) {
                this.mControllerCompat.unregisterCallback(this.mControllerCompatCallback);
            }
            if (this.mBrowserCompat != null) {
                this.mBrowserCompat.disconnect();
                this.mBrowserCompat = null;
            }
            if (this.mControllerCompat != null) {
                this.mControllerCompat.unregisterCallback(this.mControllerCompatCallback);
                this.mControllerCompat = null;
            }
            this.mConnected = false;
            this.mCallbackExecutor.execute(new Runnable() { // from class: android.support.v4.media.MediaController2ImplLegacy.1
                @Override // java.lang.Runnable
                public void run() {
                    MediaController2ImplLegacy.this.mCallback.onDisconnected(MediaController2ImplLegacy.this.mInstance);
                }
            });
        }
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    public void fastForward() {
        synchronized (this.mLock) {
            if (this.mConnected) {
                sendCommand(7);
            } else {
                Log.w(TAG, "Session isn't active", new IllegalStateException());
            }
        }
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    @Nullable
    public MediaBrowserCompat getBrowserCompat() {
        MediaBrowserCompat mediaBrowserCompat;
        synchronized (this.mLock) {
            mediaBrowserCompat = this.mBrowserCompat;
        }
        return mediaBrowserCompat;
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    public long getBufferedPosition() {
        synchronized (this.mLock) {
            long bufferedPosition = -1;
            if (!this.mConnected) {
                Log.w(TAG, "Session isn't active", new IllegalStateException());
                return -1L;
            }
            if (this.mPlaybackStateCompat != null) {
                bufferedPosition = this.mPlaybackStateCompat.getBufferedPosition();
            }
            return bufferedPosition;
        }
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    public int getBufferingState() {
        synchronized (this.mLock) {
            if (this.mConnected) {
                return this.mBufferingState;
            }
            Log.w(TAG, "Session isn't active", new IllegalStateException());
            return 0;
        }
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    @NonNull
    public MediaController2.ControllerCallback getCallback() {
        return this.mCallback;
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    @NonNull
    public Executor getCallbackExecutor() {
        return this.mCallbackExecutor;
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    @NonNull
    public Context getContext() {
        return this.mContext;
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    public MediaItem2 getCurrentMediaItem() {
        MediaItem2 mediaItem2;
        synchronized (this.mLock) {
            mediaItem2 = this.mCurrentMediaItem;
        }
        return mediaItem2;
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    public long getCurrentPosition() {
        synchronized (this.mLock) {
            if (!this.mConnected) {
                Log.w(TAG, "Session isn't active", new IllegalStateException());
                return -1L;
            }
            if (this.mPlaybackStateCompat != null) {
                return Math.max(0L, this.mPlaybackStateCompat.getPosition() + ((long) (this.mPlaybackStateCompat.getPlaybackSpeed() * (this.mInstance.mTimeDiff != null ? this.mInstance.mTimeDiff.longValue() : SystemClock.elapsedRealtime() - this.mPlaybackStateCompat.getLastPositionUpdateTime()))));
            }
            return -1L;
        }
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    public long getDuration() {
        synchronized (this.mLock) {
            if (this.mMediaMetadataCompat == null || !this.mMediaMetadataCompat.containsKey("android.media.metadata.DURATION")) {
                return -1L;
            }
            return this.mMediaMetadataCompat.getLong("android.media.metadata.DURATION");
        }
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    @NonNull
    public MediaController2 getInstance() {
        return this.mInstance;
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    @Nullable
    public MediaController2.PlaybackInfo getPlaybackInfo() {
        MediaController2.PlaybackInfo playbackInfo;
        synchronized (this.mLock) {
            playbackInfo = this.mPlaybackInfo;
        }
        return playbackInfo;
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    public float getPlaybackSpeed() {
        synchronized (this.mLock) {
            float playbackSpeed = 0.0f;
            if (!this.mConnected) {
                Log.w(TAG, "Session isn't active", new IllegalStateException());
                return 0.0f;
            }
            if (this.mPlaybackStateCompat != null) {
                playbackSpeed = this.mPlaybackStateCompat.getPlaybackSpeed();
            }
            return playbackSpeed;
        }
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    public int getPlayerState() {
        int i;
        synchronized (this.mLock) {
            i = this.mPlayerState;
        }
        return i;
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    @Nullable
    public List<MediaItem2> getPlaylist() {
        List<MediaItem2> list;
        synchronized (this.mLock) {
            list = this.mPlaylist;
        }
        return list;
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    @Nullable
    public MediaMetadata2 getPlaylistMetadata() {
        MediaMetadata2 mediaMetadata2;
        synchronized (this.mLock) {
            mediaMetadata2 = this.mPlaylistMetadata;
        }
        return mediaMetadata2;
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    public int getRepeatMode() {
        int i;
        synchronized (this.mLock) {
            i = this.mRepeatMode;
        }
        return i;
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    @Nullable
    public PendingIntent getSessionActivity() {
        synchronized (this.mLock) {
            if (this.mConnected) {
                return this.mControllerCompat.getSessionActivity();
            }
            Log.w(TAG, "Session isn't active", new IllegalStateException());
            return null;
        }
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    @NonNull
    public SessionToken2 getSessionToken() {
        return this.mToken;
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    public int getShuffleMode() {
        int i;
        synchronized (this.mLock) {
            i = this.mShuffleMode;
        }
        return i;
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    public boolean isConnected() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mConnected;
        }
        return z;
    }

    void onConnectedNotLocked(Bundle bundle) {
        bundle.setClassLoader(MediaSession2.class.getClassLoader());
        final SessionCommandGroup2 sessionCommandGroup2FromBundle = SessionCommandGroup2.fromBundle(bundle.getBundle("android.support.v4.media.argument.ALLOWED_COMMANDS"));
        int i = bundle.getInt("android.support.v4.media.argument.PLAYER_STATE");
        MediaItem2 mediaItem2FromBundle = MediaItem2.fromBundle(bundle.getBundle("android.support.v4.media.argument.MEDIA_ITEM"));
        int i2 = bundle.getInt("android.support.v4.media.argument.BUFFERING_STATE");
        PlaybackStateCompat playbackStateCompat = (PlaybackStateCompat) bundle.getParcelable("android.support.v4.media.argument.PLAYBACK_STATE_COMPAT");
        int i3 = bundle.getInt("android.support.v4.media.argument.REPEAT_MODE");
        int i4 = bundle.getInt("android.support.v4.media.argument.SHUFFLE_MODE");
        List<MediaItem2> listConvertToMediaItem2List = MediaUtils2.convertToMediaItem2List(bundle.getParcelableArray("android.support.v4.media.argument.PLAYLIST"));
        MediaController2.PlaybackInfo playbackInfoFromBundle = MediaController2.PlaybackInfo.fromBundle(bundle.getBundle("android.support.v4.media.argument.PLAYBACK_INFO"));
        MediaMetadata2 mediaMetadata2FromBundle = MediaMetadata2.fromBundle(bundle.getBundle("android.support.v4.media.argument.PLAYLIST_METADATA"));
        if (DEBUG) {
            Log.d(TAG, "onConnectedNotLocked token=" + this.mToken + ", allowedCommands=" + sessionCommandGroup2FromBundle);
        }
        try {
            synchronized (this.mLock) {
                try {
                    if (this.mIsReleased) {
                        return;
                    }
                    if (this.mConnected) {
                        Log.e(TAG, "Cannot be notified about the connection result many times. Probably a bug or malicious app.");
                        try {
                            close();
                            return;
                        } catch (Throwable th) {
                            th = th;
                            throw th;
                        }
                    }
                    this.mAllowedCommands = sessionCommandGroup2FromBundle;
                    this.mPlayerState = i;
                    this.mCurrentMediaItem = mediaItem2FromBundle;
                    this.mBufferingState = i2;
                    this.mPlaybackStateCompat = playbackStateCompat;
                    this.mRepeatMode = i3;
                    this.mShuffleMode = i4;
                    this.mPlaylist = listConvertToMediaItem2List;
                    this.mPlaylistMetadata = mediaMetadata2FromBundle;
                    this.mConnected = true;
                    this.mPlaybackInfo = playbackInfoFromBundle;
                    this.mCallbackExecutor.execute(new Runnable() { // from class: android.support.v4.media.MediaController2ImplLegacy.2
                        @Override // java.lang.Runnable
                        public void run() {
                            MediaController2ImplLegacy.this.mCallback.onConnected(MediaController2ImplLegacy.this.mInstance, sessionCommandGroup2FromBundle);
                        }
                    });
                } catch (Throwable th2) {
                    th = th2;
                }
            }
        } catch (Throwable th3) {
            if (0 != 0) {
                close();
            }
            throw th3;
        }
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    public void pause() {
        synchronized (this.mLock) {
            if (this.mConnected) {
                sendCommand(2);
            } else {
                Log.w(TAG, "Session isn't active", new IllegalStateException());
            }
        }
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    public void play() {
        synchronized (this.mLock) {
            if (this.mConnected) {
                sendCommand(1);
            } else {
                Log.w(TAG, "Session isn't active", new IllegalStateException());
            }
        }
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    public void playFromMediaId(@NonNull String str, @Nullable Bundle bundle) {
        synchronized (this.mLock) {
            if (!this.mConnected) {
                Log.w(TAG, "Session isn't active", new IllegalStateException());
                return;
            }
            Bundle bundle2 = new Bundle();
            bundle2.putString("android.support.v4.media.argument.MEDIA_ID", str);
            bundle2.putBundle("android.support.v4.media.argument.EXTRAS", bundle);
            sendCommand(22, bundle2);
        }
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    public void playFromSearch(@NonNull String str, @Nullable Bundle bundle) {
        synchronized (this.mLock) {
            if (!this.mConnected) {
                Log.w(TAG, "Session isn't active", new IllegalStateException());
                return;
            }
            Bundle bundle2 = new Bundle();
            bundle2.putString("android.support.v4.media.argument.QUERY", str);
            bundle2.putBundle("android.support.v4.media.argument.EXTRAS", bundle);
            sendCommand(24, bundle2);
        }
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    public void playFromUri(@NonNull Uri uri, @Nullable Bundle bundle) {
        synchronized (this.mLock) {
            if (!this.mConnected) {
                Log.w(TAG, "Session isn't active", new IllegalStateException());
                return;
            }
            Bundle bundle2 = new Bundle();
            bundle2.putParcelable("android.support.v4.media.argument.URI", uri);
            bundle2.putBundle("android.support.v4.media.argument.EXTRAS", bundle);
            sendCommand(23, bundle2);
        }
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    public void prepare() {
        synchronized (this.mLock) {
            if (this.mConnected) {
                sendCommand(6);
            } else {
                Log.w(TAG, "Session isn't active", new IllegalStateException());
            }
        }
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    public void prepareFromMediaId(@NonNull String str, @Nullable Bundle bundle) {
        synchronized (this.mLock) {
            if (!this.mConnected) {
                Log.w(TAG, "Session isn't active", new IllegalStateException());
                return;
            }
            Bundle bundle2 = new Bundle();
            bundle2.putString("android.support.v4.media.argument.MEDIA_ID", str);
            bundle2.putBundle("android.support.v4.media.argument.EXTRAS", bundle);
            sendCommand(25, bundle2);
        }
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    public void prepareFromSearch(@NonNull String str, @Nullable Bundle bundle) {
        synchronized (this.mLock) {
            if (!this.mConnected) {
                Log.w(TAG, "Session isn't active", new IllegalStateException());
                return;
            }
            Bundle bundle2 = new Bundle();
            bundle2.putString("android.support.v4.media.argument.QUERY", str);
            bundle2.putBundle("android.support.v4.media.argument.EXTRAS", bundle);
            sendCommand(27, bundle2);
        }
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    public void prepareFromUri(@NonNull Uri uri, @Nullable Bundle bundle) {
        synchronized (this.mLock) {
            if (!this.mConnected) {
                Log.w(TAG, "Session isn't active", new IllegalStateException());
                return;
            }
            Bundle bundle2 = new Bundle();
            bundle2.putParcelable("android.support.v4.media.argument.URI", uri);
            bundle2.putBundle("android.support.v4.media.argument.EXTRAS", bundle);
            sendCommand(26, bundle2);
        }
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    public void removePlaylistItem(@NonNull MediaItem2 mediaItem2) {
        Bundle bundle = new Bundle();
        bundle.putBundle("android.support.v4.media.argument.MEDIA_ITEM", mediaItem2.toBundle());
        sendCommand(16, bundle);
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    public void replacePlaylistItem(int i, @NonNull MediaItem2 mediaItem2) {
        Bundle bundle = new Bundle();
        bundle.putInt("android.support.v4.media.argument.PLAYLIST_INDEX", i);
        bundle.putBundle("android.support.v4.media.argument.MEDIA_ITEM", mediaItem2.toBundle());
        sendCommand(17, bundle);
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    public void reset() {
        synchronized (this.mLock) {
            if (this.mConnected) {
                sendCommand(3);
            } else {
                Log.w(TAG, "Session isn't active", new IllegalStateException());
            }
        }
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    public void rewind() {
        synchronized (this.mLock) {
            if (this.mConnected) {
                sendCommand(8);
            } else {
                Log.w(TAG, "Session isn't active", new IllegalStateException());
            }
        }
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    public void seekTo(long j) {
        synchronized (this.mLock) {
            if (!this.mConnected) {
                Log.w(TAG, "Session isn't active", new IllegalStateException());
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putLong("android.support.v4.media.argument.SEEK_POSITION", j);
            sendCommand(9, bundle);
        }
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    public void selectRoute(@NonNull Bundle bundle) {
        Bundle bundle2 = new Bundle();
        bundle2.putBundle("android.support.v4.media.argument.ROUTE_BUNDLE", bundle);
        sendCommand(38, bundle2);
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    public void sendCustomCommand(@NonNull SessionCommand2 sessionCommand2, @Nullable Bundle bundle, @Nullable ResultReceiver resultReceiver) {
        synchronized (this.mLock) {
            if (!this.mConnected) {
                Log.w(TAG, "Session isn't active", new IllegalStateException());
                return;
            }
            Bundle bundle2 = new Bundle();
            bundle2.putBundle("android.support.v4.media.argument.CUSTOM_COMMAND", sessionCommand2.toBundle());
            bundle2.putBundle("android.support.v4.media.argument.ARGUMENTS", bundle);
            sendCommand("android.support.v4.media.controller.command.BY_CUSTOM_COMMAND", bundle2, resultReceiver);
        }
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    public void setPlaybackSpeed(float f) {
        synchronized (this.mLock) {
            if (!this.mConnected) {
                Log.w(TAG, "Session isn't active", new IllegalStateException());
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putFloat("android.support.v4.media.argument.PLAYBACK_SPEED", f);
            sendCommand(39, bundle);
        }
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    public void setPlaylist(@NonNull List<MediaItem2> list, @Nullable MediaMetadata2 mediaMetadata2) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArray("android.support.v4.media.argument.PLAYLIST", MediaUtils2.convertMediaItem2ListToParcelableArray(list));
        bundle.putBundle("android.support.v4.media.argument.PLAYLIST_METADATA", mediaMetadata2 == null ? null : mediaMetadata2.toBundle());
        sendCommand(19, bundle);
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    public void setRating(@NonNull String str, @NonNull Rating2 rating2) {
        synchronized (this.mLock) {
            if (!this.mConnected) {
                Log.w(TAG, "Session isn't active", new IllegalStateException());
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putString("android.support.v4.media.argument.MEDIA_ID", str);
            bundle.putBundle("android.support.v4.media.argument.RATING", rating2.toBundle());
            sendCommand(28, bundle);
        }
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    public void setRepeatMode(int i) {
        Bundle bundle = new Bundle();
        bundle.putInt("android.support.v4.media.argument.REPEAT_MODE", i);
        sendCommand(14, bundle);
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    public void setShuffleMode(int i) {
        Bundle bundle = new Bundle();
        bundle.putInt("android.support.v4.media.argument.SHUFFLE_MODE", i);
        sendCommand(13, bundle);
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    public void setVolumeTo(int i, int i2) {
        synchronized (this.mLock) {
            if (!this.mConnected) {
                Log.w(TAG, "Session isn't active", new IllegalStateException());
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putInt("android.support.v4.media.argument.VOLUME", i);
            bundle.putInt("android.support.v4.media.argument.VOLUME_FLAGS", i2);
            sendCommand(10, bundle);
        }
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    public void skipBackward() {
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    public void skipForward() {
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    public void skipToNextItem() {
        sendCommand(4);
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    public void skipToPlaylistItem(@NonNull MediaItem2 mediaItem2) {
        Bundle bundle = new Bundle();
        bundle.putBundle("android.support.v4.media.argument.MEDIA_ITEM", mediaItem2.toBundle());
        sendCommand(12, bundle);
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    public void skipToPreviousItem() {
        sendCommand(5);
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    public void subscribeRoutesInfo() {
        sendCommand(36);
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    public void unsubscribeRoutesInfo() {
        sendCommand(37);
    }

    @Override // android.support.v4.media.MediaController2.SupportLibraryImpl
    public void updatePlaylistMetadata(@Nullable MediaMetadata2 mediaMetadata2) {
        Bundle bundle = new Bundle();
        bundle.putBundle("android.support.v4.media.argument.PLAYLIST_METADATA", mediaMetadata2 == null ? null : mediaMetadata2.toBundle());
        sendCommand(21, bundle);
    }

    private void sendCommand(int i, Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putInt("android.support.v4.media.argument.COMMAND_CODE", i);
        sendCommand("android.support.v4.media.controller.command.BY_COMMAND_CODE", bundle, null);
    }

    private void sendCommand(String str) {
        sendCommand(str, null, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendCommand(String str, ResultReceiver resultReceiver) {
        sendCommand(str, null, resultReceiver);
    }

    private void sendCommand(String str, Bundle bundle, ResultReceiver resultReceiver) {
        MediaControllerCompat mediaControllerCompat;
        ControllerCompatCallback controllerCompatCallback;
        if (bundle == null) {
            bundle = new Bundle();
        }
        synchronized (this.mLock) {
            mediaControllerCompat = this.mControllerCompat;
            controllerCompatCallback = this.mControllerCompatCallback;
        }
        BundleCompat.putBinder(bundle, "android.support.v4.media.argument.ICONTROLLER_CALLBACK", controllerCompatCallback.getIControllerCallback().asBinder());
        bundle.putString("android.support.v4.media.argument.PACKAGE_NAME", this.mContext.getPackageName());
        bundle.putInt("android.support.v4.media.argument.UID", Process.myUid());
        bundle.putInt("android.support.v4.media.argument.PID", Process.myPid());
        mediaControllerCompat.sendCommand(str, bundle, resultReceiver);
    }
}
