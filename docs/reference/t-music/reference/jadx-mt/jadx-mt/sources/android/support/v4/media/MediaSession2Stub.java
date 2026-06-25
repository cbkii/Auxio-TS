package android.support.v4.media;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Context;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.support.annotation.GuardedBy;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.mediacompat.Rating2;
import android.support.v4.media.IMediaSession2;
import android.support.v4.media.MediaController2;
import android.support.v4.media.MediaLibraryService2;
import android.support.v4.media.MediaSession2;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/* JADX INFO: loaded from: classes3.dex */
@TargetApi(19)
class MediaSession2Stub extends IMediaSession2.Stub {
    private static final boolean DEBUG = true;
    private static final String TAG = "MediaSession2Stub";
    private static final SparseArray<SessionCommand2> sCommandsForOnCommandRequest = new SparseArray<>();
    final Context mContext;
    final MediaSession2.SupportLibraryImpl mSession;
    private final Object mLock = new Object();

    @GuardedBy("mLock")
    private final ArrayMap<IBinder, MediaSession2.ControllerInfo> mControllers = new ArrayMap<>();

    @GuardedBy("mLock")
    private final Set<IBinder> mConnectingControllers = new HashSet();

    @GuardedBy("mLock")
    private final ArrayMap<MediaSession2.ControllerInfo, SessionCommandGroup2> mAllowedCommandGroupMap = new ArrayMap<>();

    static final class Controller2Cb extends MediaSession2.ControllerCb {
        private final IMediaController2 mIControllerCallback;

        Controller2Cb(@NonNull IMediaController2 iMediaController2) {
            this.mIControllerCallback = iMediaController2;
        }

        @Override // android.support.v4.media.MediaSession2.ControllerCb
        @NonNull
        IBinder getId() {
            return this.mIControllerCallback.asBinder();
        }

        @Override // android.support.v4.media.MediaSession2.ControllerCb
        void onAllowedCommandsChanged(SessionCommandGroup2 sessionCommandGroup2) {
            this.mIControllerCallback.onAllowedCommandsChanged(sessionCommandGroup2.toBundle());
        }

        @Override // android.support.v4.media.MediaSession2.ControllerCb
        void onBufferingStateChanged(MediaItem2 mediaItem2, int i, long j) {
            this.mIControllerCallback.onBufferingStateChanged(mediaItem2 == null ? null : mediaItem2.toBundle(), i, j);
        }

        @Override // android.support.v4.media.MediaSession2.ControllerCb
        void onChildrenChanged(String str, int i, Bundle bundle) {
            this.mIControllerCallback.onChildrenChanged(str, i, bundle);
        }

        @Override // android.support.v4.media.MediaSession2.ControllerCb
        void onCurrentMediaItemChanged(MediaItem2 mediaItem2) {
            this.mIControllerCallback.onCurrentMediaItemChanged(mediaItem2 == null ? null : mediaItem2.toBundle());
        }

        @Override // android.support.v4.media.MediaSession2.ControllerCb
        void onCustomCommand(SessionCommand2 sessionCommand2, Bundle bundle, ResultReceiver resultReceiver) {
            this.mIControllerCallback.onCustomCommand(sessionCommand2.toBundle(), bundle, resultReceiver);
        }

        @Override // android.support.v4.media.MediaSession2.ControllerCb
        void onCustomLayoutChanged(List<MediaSession2.CommandButton> list) {
            this.mIControllerCallback.onCustomLayoutChanged(MediaUtils2.convertCommandButtonListToBundleList(list));
        }

        @Override // android.support.v4.media.MediaSession2.ControllerCb
        void onDisconnected() {
            this.mIControllerCallback.onDisconnected();
        }

        @Override // android.support.v4.media.MediaSession2.ControllerCb
        void onError(int i, Bundle bundle) {
            this.mIControllerCallback.onError(i, bundle);
        }

        @Override // android.support.v4.media.MediaSession2.ControllerCb
        void onGetChildrenDone(String str, int i, int i2, List<MediaItem2> list, Bundle bundle) {
            this.mIControllerCallback.onGetChildrenDone(str, i, i2, MediaUtils2.convertMediaItem2ListToBundleList(list), bundle);
        }

        @Override // android.support.v4.media.MediaSession2.ControllerCb
        void onGetItemDone(String str, MediaItem2 mediaItem2) {
            this.mIControllerCallback.onGetItemDone(str, mediaItem2 == null ? null : mediaItem2.toBundle());
        }

        @Override // android.support.v4.media.MediaSession2.ControllerCb
        void onGetLibraryRootDone(Bundle bundle, String str, Bundle bundle2) {
            this.mIControllerCallback.onGetLibraryRootDone(bundle, str, bundle2);
        }

        @Override // android.support.v4.media.MediaSession2.ControllerCb
        void onGetSearchResultDone(String str, int i, int i2, List<MediaItem2> list, Bundle bundle) {
            this.mIControllerCallback.onGetSearchResultDone(str, i, i2, MediaUtils2.convertMediaItem2ListToBundleList(list), bundle);
        }

        @Override // android.support.v4.media.MediaSession2.ControllerCb
        void onPlaybackInfoChanged(MediaController2.PlaybackInfo playbackInfo) {
            this.mIControllerCallback.onPlaybackInfoChanged(playbackInfo.toBundle());
        }

        @Override // android.support.v4.media.MediaSession2.ControllerCb
        void onPlaybackSpeedChanged(long j, long j2, float f) {
            this.mIControllerCallback.onPlaybackSpeedChanged(j, j2, f);
        }

        @Override // android.support.v4.media.MediaSession2.ControllerCb
        void onPlayerStateChanged(long j, long j2, int i) {
            this.mIControllerCallback.onPlayerStateChanged(j, j2, i);
        }

        @Override // android.support.v4.media.MediaSession2.ControllerCb
        void onPlaylistChanged(List<MediaItem2> list, MediaMetadata2 mediaMetadata2) {
            this.mIControllerCallback.onPlaylistChanged(MediaUtils2.convertMediaItem2ListToBundleList(list), mediaMetadata2 == null ? null : mediaMetadata2.toBundle());
        }

        @Override // android.support.v4.media.MediaSession2.ControllerCb
        void onPlaylistMetadataChanged(MediaMetadata2 mediaMetadata2) {
            this.mIControllerCallback.onPlaylistMetadataChanged(mediaMetadata2.toBundle());
        }

        @Override // android.support.v4.media.MediaSession2.ControllerCb
        void onRepeatModeChanged(int i) {
            this.mIControllerCallback.onRepeatModeChanged(i);
        }

        @Override // android.support.v4.media.MediaSession2.ControllerCb
        void onRoutesInfoChanged(List<Bundle> list) {
            this.mIControllerCallback.onRoutesInfoChanged(list);
        }

        @Override // android.support.v4.media.MediaSession2.ControllerCb
        void onSearchResultChanged(String str, int i, Bundle bundle) {
            this.mIControllerCallback.onSearchResultChanged(str, i, bundle);
        }

        @Override // android.support.v4.media.MediaSession2.ControllerCb
        void onSeekCompleted(long j, long j2, long j3) {
            this.mIControllerCallback.onSeekCompleted(j, j2, j3);
        }

        @Override // android.support.v4.media.MediaSession2.ControllerCb
        void onShuffleModeChanged(int i) {
            this.mIControllerCallback.onShuffleModeChanged(i);
        }
    }

    @FunctionalInterface
    private interface SessionRunnable {
        void run(MediaSession2.ControllerInfo controllerInfo);
    }

    static {
        SessionCommandGroup2 sessionCommandGroup2 = new SessionCommandGroup2();
        sessionCommandGroup2.addAllPlaybackCommands();
        sessionCommandGroup2.addAllPlaylistCommands();
        sessionCommandGroup2.addAllVolumeCommands();
        for (SessionCommand2 sessionCommand2 : sessionCommandGroup2.getCommands()) {
            sCommandsForOnCommandRequest.append(sessionCommand2.getCommandCode(), sessionCommand2);
        }
    }

    MediaSession2Stub(MediaSession2.SupportLibraryImpl supportLibraryImpl) {
        this.mSession = supportLibraryImpl;
        this.mContext = this.mSession.getContext();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public MediaLibraryService2.MediaLibrarySession.SupportLibraryImpl getLibrarySession() {
        MediaSession2.SupportLibraryImpl supportLibraryImpl = this.mSession;
        if (supportLibraryImpl instanceof MediaLibraryService2.MediaLibrarySession.SupportLibraryImpl) {
            return (MediaLibraryService2.MediaLibrarySession.SupportLibraryImpl) supportLibraryImpl;
        }
        throw new RuntimeException("Session cannot be casted to library session");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isAllowedCommand(MediaSession2.ControllerInfo controllerInfo, SessionCommand2 sessionCommand2) {
        SessionCommandGroup2 sessionCommandGroup2;
        synchronized (this.mLock) {
            sessionCommandGroup2 = this.mAllowedCommandGroupMap.get(controllerInfo);
        }
        if (sessionCommandGroup2 == null || !sessionCommandGroup2.hasCommand(sessionCommand2)) {
            return false;
        }
        return DEBUG;
    }

    private void onBrowserCommand(@NonNull IMediaController2 iMediaController2, int i, @NonNull SessionRunnable sessionRunnable) {
        if (!(this.mSession instanceof MediaLibraryService2.MediaLibrarySession.SupportLibraryImpl)) {
            throw new RuntimeException("MediaSession2 cannot handle MediaLibrarySession command");
        }
        onSessionCommandInternal(iMediaController2, null, i, sessionRunnable);
    }

    private void onSessionCommand(@NonNull IMediaController2 iMediaController2, int i, @NonNull SessionRunnable sessionRunnable) {
        onSessionCommandInternal(iMediaController2, null, i, sessionRunnable);
    }

    private void onSessionCommandInternal(@NonNull IMediaController2 iMediaController2, @Nullable final SessionCommand2 sessionCommand2, final int i, @NonNull final SessionRunnable sessionRunnable) {
        final MediaSession2.ControllerInfo controllerInfo;
        synchronized (this.mLock) {
            controllerInfo = iMediaController2 == null ? null : this.mControllers.get(iMediaController2.asBinder());
        }
        if (this.mSession.isClosed() || controllerInfo == null) {
            return;
        }
        this.mSession.getCallbackExecutor().execute(new Runnable() { // from class: android.support.v4.media.MediaSession2Stub.1
            @Override // java.lang.Runnable
            public void run() {
                SessionCommand2 sessionCommand22;
                synchronized (MediaSession2Stub.this.mLock) {
                    if (MediaSession2Stub.this.mControllers.containsValue(controllerInfo)) {
                        SessionCommand2 sessionCommand23 = sessionCommand2;
                        if (sessionCommand23 != null) {
                            if (!MediaSession2Stub.this.isAllowedCommand(controllerInfo, sessionCommand23)) {
                                return;
                            } else {
                                sessionCommand22 = (SessionCommand2) MediaSession2Stub.sCommandsForOnCommandRequest.get(sessionCommand2.getCommandCode());
                            }
                        } else if (!MediaSession2Stub.this.isAllowedCommand(controllerInfo, i)) {
                            return;
                        } else {
                            sessionCommand22 = (SessionCommand2) MediaSession2Stub.sCommandsForOnCommandRequest.get(i);
                        }
                        if (sessionCommand22 == null || MediaSession2Stub.this.mSession.getCallback().onCommandRequest(MediaSession2Stub.this.mSession.getInstance(), controllerInfo, sessionCommand22)) {
                            try {
                                sessionRunnable.run(controllerInfo);
                                return;
                            } catch (RemoteException e) {
                                Log.w(MediaSession2Stub.TAG, "Exception in " + controllerInfo.toString(), e);
                                return;
                            }
                        }
                        Log.d(MediaSession2Stub.TAG, "Command (" + sessionCommand22 + ") from " + controllerInfo + " was rejected by " + MediaSession2Stub.this.mSession);
                    }
                }
            }
        });
    }

    private void releaseController(IMediaController2 iMediaController2) {
        final MediaSession2.ControllerInfo controllerInfoRemove;
        synchronized (this.mLock) {
            controllerInfoRemove = this.mControllers.remove(iMediaController2.asBinder());
            Log.d(TAG, "releasing " + controllerInfoRemove);
        }
        if (this.mSession.isClosed() || controllerInfoRemove == null) {
            return;
        }
        this.mSession.getCallbackExecutor().execute(new Runnable() { // from class: android.support.v4.media.MediaSession2Stub.2
            @Override // java.lang.Runnable
            public void run() {
                MediaSession2Stub.this.mSession.getCallback().onDisconnected(MediaSession2Stub.this.mSession.getInstance(), controllerInfoRemove);
            }
        });
    }

    @Override // android.support.v4.media.IMediaSession2
    public void addPlaylistItem(IMediaController2 iMediaController2, final int i, final Bundle bundle) {
        onSessionCommand(iMediaController2, 15, new SessionRunnable() { // from class: android.support.v4.media.MediaSession2Stub.24
            @Override // android.support.v4.media.MediaSession2Stub.SessionRunnable
            public void run(MediaSession2.ControllerInfo controllerInfo) {
                MediaSession2Stub.this.mSession.getInstance().addPlaylistItem(i, MediaItem2.fromBundle(bundle, null));
            }
        });
    }

    @Override // android.support.v4.media.IMediaSession2
    public void adjustVolume(IMediaController2 iMediaController2, final int i, final int i2) {
        onSessionCommand(iMediaController2, 11, new SessionRunnable() { // from class: android.support.v4.media.MediaSession2Stub.5
            @Override // android.support.v4.media.MediaSession2Stub.SessionRunnable
            public void run(MediaSession2.ControllerInfo controllerInfo) {
                VolumeProviderCompat volumeProvider = MediaSession2Stub.this.mSession.getVolumeProvider();
                if (volumeProvider != null) {
                    volumeProvider.onAdjustVolume(i);
                    return;
                }
                MediaSessionCompat sessionCompat = MediaSession2Stub.this.mSession.getSessionCompat();
                if (sessionCompat != null) {
                    sessionCompat.getController().adjustVolume(i, i2);
                }
            }
        });
    }

    @Override // android.support.v4.media.IMediaSession2
    public void connect(final IMediaController2 iMediaController2, String str) {
        final MediaSession2.ControllerInfo controllerInfo = new MediaSession2.ControllerInfo(str, Binder.getCallingPid(), Binder.getCallingUid(), new Controller2Cb(iMediaController2));
        this.mSession.getCallbackExecutor().execute(new Runnable() { // from class: android.support.v4.media.MediaSession2Stub.3
            @Override // java.lang.Runnable
            public void run() {
                if (MediaSession2Stub.this.mSession.isClosed()) {
                    return;
                }
                synchronized (MediaSession2Stub.this.mLock) {
                    MediaSession2Stub.this.mConnectingControllers.add(controllerInfo.getId());
                }
                SessionCommandGroup2 sessionCommandGroup2OnConnect = MediaSession2Stub.this.mSession.getCallback().onConnect(MediaSession2Stub.this.mSession.getInstance(), controllerInfo);
                try {
                    if ((sessionCommandGroup2OnConnect != null || controllerInfo.isTrusted()) ? MediaSession2Stub.DEBUG : false) {
                        Log.d(MediaSession2Stub.TAG, "Accepting connection, controllerInfo=" + controllerInfo + " allowedCommands=" + sessionCommandGroup2OnConnect);
                        if (sessionCommandGroup2OnConnect == null) {
                            sessionCommandGroup2OnConnect = new SessionCommandGroup2();
                        }
                        synchronized (MediaSession2Stub.this.mLock) {
                            MediaSession2Stub.this.mConnectingControllers.remove(controllerInfo.getId());
                            MediaSession2Stub.this.mControllers.put(controllerInfo.getId(), controllerInfo);
                            MediaSession2Stub.this.mAllowedCommandGroupMap.put(controllerInfo, sessionCommandGroup2OnConnect);
                        }
                        int playerState = MediaSession2Stub.this.mSession.getPlayerState();
                        Bundle bundle = MediaSession2Stub.this.mSession.getCurrentMediaItem() == null ? null : MediaSession2Stub.this.mSession.getCurrentMediaItem().toBundle();
                        long jElapsedRealtime = SystemClock.elapsedRealtime();
                        long currentPosition = MediaSession2Stub.this.mSession.getCurrentPosition();
                        float playbackSpeed = MediaSession2Stub.this.mSession.getPlaybackSpeed();
                        long bufferedPosition = MediaSession2Stub.this.mSession.getBufferedPosition();
                        Bundle bundle2 = MediaSession2Stub.this.mSession.getPlaybackInfo().toBundle();
                        int repeatMode = MediaSession2Stub.this.mSession.getRepeatMode();
                        int shuffleMode = MediaSession2Stub.this.mSession.getShuffleMode();
                        PendingIntent sessionActivity = MediaSession2Stub.this.mSession.getSessionActivity();
                        List<Bundle> listConvertMediaItem2ListToBundleList = MediaUtils2.convertMediaItem2ListToBundleList(sessionCommandGroup2OnConnect.hasCommand(18) ? MediaSession2Stub.this.mSession.getPlaylist() : null);
                        if (MediaSession2Stub.this.mSession.isClosed()) {
                        } else {
                            iMediaController2.onConnected(MediaSession2Stub.this, sessionCommandGroup2OnConnect.toBundle(), playerState, bundle, jElapsedRealtime, currentPosition, playbackSpeed, bufferedPosition, bundle2, repeatMode, shuffleMode, listConvertMediaItem2ListToBundleList, sessionActivity);
                        }
                    } else {
                        synchronized (MediaSession2Stub.this.mLock) {
                            MediaSession2Stub.this.mConnectingControllers.remove(controllerInfo.getId());
                        }
                        Log.d(MediaSession2Stub.TAG, "Rejecting connection, controllerInfo=" + controllerInfo);
                        iMediaController2.onDisconnected();
                    }
                } catch (RemoteException unused) {
                }
            }
        });
    }

    @Override // android.support.v4.media.IMediaSession2
    public void fastForward(IMediaController2 iMediaController2) {
        onSessionCommand(iMediaController2, 7, new SessionRunnable() { // from class: android.support.v4.media.MediaSession2Stub.10
            @Override // android.support.v4.media.MediaSession2Stub.SessionRunnable
            public void run(MediaSession2.ControllerInfo controllerInfo) {
                MediaSession2Stub.this.mSession.getCallback().onFastForward(MediaSession2Stub.this.mSession.getInstance(), controllerInfo);
            }
        });
    }

    @Override // android.support.v4.media.IMediaSession2
    public void getChildren(IMediaController2 iMediaController2, final String str, final int i, final int i2, final Bundle bundle) {
        onBrowserCommand(iMediaController2, 29, new SessionRunnable() { // from class: android.support.v4.media.MediaSession2Stub.37
            @Override // android.support.v4.media.MediaSession2Stub.SessionRunnable
            public void run(MediaSession2.ControllerInfo controllerInfo) {
                if (str == null) {
                    Log.w(MediaSession2Stub.TAG, "getChildren(): Ignoring null parentId from " + controllerInfo);
                    return;
                }
                if (i >= 1 && i2 >= 1) {
                    MediaSession2Stub.this.getLibrarySession().onGetChildrenOnExecutor(controllerInfo, str, i, i2, bundle);
                    return;
                }
                Log.w(MediaSession2Stub.TAG, "getChildren(): Ignoring page nor pageSize less than 1 from " + controllerInfo);
            }
        });
    }

    List<MediaSession2.ControllerInfo> getConnectedControllers() {
        ArrayList arrayList = new ArrayList();
        synchronized (this.mLock) {
            for (int i = 0; i < this.mControllers.size(); i++) {
                arrayList.add(this.mControllers.valueAt(i));
            }
        }
        return arrayList;
    }

    @Override // android.support.v4.media.IMediaSession2
    public void getItem(IMediaController2 iMediaController2, final String str) {
        onBrowserCommand(iMediaController2, 30, new SessionRunnable() { // from class: android.support.v4.media.MediaSession2Stub.36
            @Override // android.support.v4.media.MediaSession2Stub.SessionRunnable
            public void run(MediaSession2.ControllerInfo controllerInfo) {
                if (str != null) {
                    MediaSession2Stub.this.getLibrarySession().onGetItemOnExecutor(controllerInfo, str);
                    return;
                }
                Log.w(MediaSession2Stub.TAG, "getItem(): Ignoring null mediaId from " + controllerInfo);
            }
        });
    }

    @Override // android.support.v4.media.IMediaSession2
    public void getLibraryRoot(IMediaController2 iMediaController2, final Bundle bundle) {
        onBrowserCommand(iMediaController2, 31, new SessionRunnable() { // from class: android.support.v4.media.MediaSession2Stub.35
            @Override // android.support.v4.media.MediaSession2Stub.SessionRunnable
            public void run(MediaSession2.ControllerInfo controllerInfo) {
                MediaSession2Stub.this.getLibrarySession().onGetLibraryRootOnExecutor(controllerInfo, bundle);
            }
        });
    }

    @Override // android.support.v4.media.IMediaSession2
    public void getSearchResult(IMediaController2 iMediaController2, final String str, final int i, final int i2, final Bundle bundle) {
        onBrowserCommand(iMediaController2, 32, new SessionRunnable() { // from class: android.support.v4.media.MediaSession2Stub.39
            @Override // android.support.v4.media.MediaSession2Stub.SessionRunnable
            public void run(MediaSession2.ControllerInfo controllerInfo) {
                if (TextUtils.isEmpty(str)) {
                    Log.w(MediaSession2Stub.TAG, "getSearchResult(): Ignoring empty query from " + controllerInfo);
                    return;
                }
                if (i >= 1 && i2 >= 1) {
                    MediaSession2Stub.this.getLibrarySession().onGetSearchResultOnExecutor(controllerInfo, str, i, i2, bundle);
                    return;
                }
                Log.w(MediaSession2Stub.TAG, "getSearchResult(): Ignoring page nor pageSize less than 1  from " + controllerInfo);
            }
        });
    }

    @Override // android.support.v4.media.IMediaSession2
    public void pause(IMediaController2 iMediaController2) {
        onSessionCommand(iMediaController2, 2, new SessionRunnable() { // from class: android.support.v4.media.MediaSession2Stub.7
            @Override // android.support.v4.media.MediaSession2Stub.SessionRunnable
            public void run(MediaSession2.ControllerInfo controllerInfo) {
                MediaSession2Stub.this.mSession.pause();
            }
        });
    }

    @Override // android.support.v4.media.IMediaSession2
    public void play(IMediaController2 iMediaController2) {
        onSessionCommand(iMediaController2, 1, new SessionRunnable() { // from class: android.support.v4.media.MediaSession2Stub.6
            @Override // android.support.v4.media.MediaSession2Stub.SessionRunnable
            public void run(MediaSession2.ControllerInfo controllerInfo) {
                MediaSession2Stub.this.mSession.play();
            }
        });
    }

    @Override // android.support.v4.media.IMediaSession2
    public void playFromMediaId(IMediaController2 iMediaController2, final String str, final Bundle bundle) {
        onSessionCommand(iMediaController2, 22, new SessionRunnable() { // from class: android.support.v4.media.MediaSession2Stub.19
            @Override // android.support.v4.media.MediaSession2Stub.SessionRunnable
            public void run(MediaSession2.ControllerInfo controllerInfo) {
                if (str != null) {
                    MediaSession2Stub.this.mSession.getCallback().onPlayFromMediaId(MediaSession2Stub.this.mSession.getInstance(), controllerInfo, str, bundle);
                    return;
                }
                Log.w(MediaSession2Stub.TAG, "playFromMediaId(): Ignoring null mediaId from " + controllerInfo);
            }
        });
    }

    @Override // android.support.v4.media.IMediaSession2
    public void playFromSearch(IMediaController2 iMediaController2, final String str, final Bundle bundle) {
        onSessionCommand(iMediaController2, 24, new SessionRunnable() { // from class: android.support.v4.media.MediaSession2Stub.18
            @Override // android.support.v4.media.MediaSession2Stub.SessionRunnable
            public void run(MediaSession2.ControllerInfo controllerInfo) {
                if (!TextUtils.isEmpty(str)) {
                    MediaSession2Stub.this.mSession.getCallback().onPlayFromSearch(MediaSession2Stub.this.mSession.getInstance(), controllerInfo, str, bundle);
                    return;
                }
                Log.w(MediaSession2Stub.TAG, "playFromSearch(): Ignoring empty query from " + controllerInfo);
            }
        });
    }

    @Override // android.support.v4.media.IMediaSession2
    public void playFromUri(IMediaController2 iMediaController2, final Uri uri, final Bundle bundle) {
        onSessionCommand(iMediaController2, 23, new SessionRunnable() { // from class: android.support.v4.media.MediaSession2Stub.17
            @Override // android.support.v4.media.MediaSession2Stub.SessionRunnable
            public void run(MediaSession2.ControllerInfo controllerInfo) {
                if (uri != null) {
                    MediaSession2Stub.this.mSession.getCallback().onPlayFromUri(MediaSession2Stub.this.mSession.getInstance(), controllerInfo, uri, bundle);
                    return;
                }
                Log.w(MediaSession2Stub.TAG, "playFromUri(): Ignoring null uri from " + controllerInfo);
            }
        });
    }

    @Override // android.support.v4.media.IMediaSession2
    public void prepare(IMediaController2 iMediaController2) {
        onSessionCommand(iMediaController2, 6, new SessionRunnable() { // from class: android.support.v4.media.MediaSession2Stub.9
            @Override // android.support.v4.media.MediaSession2Stub.SessionRunnable
            public void run(MediaSession2.ControllerInfo controllerInfo) {
                MediaSession2Stub.this.mSession.prepare();
            }
        });
    }

    @Override // android.support.v4.media.IMediaSession2
    public void prepareFromMediaId(IMediaController2 iMediaController2, final String str, final Bundle bundle) {
        onSessionCommand(iMediaController2, 25, new SessionRunnable() { // from class: android.support.v4.media.MediaSession2Stub.16
            @Override // android.support.v4.media.MediaSession2Stub.SessionRunnable
            public void run(MediaSession2.ControllerInfo controllerInfo) {
                if (str != null) {
                    MediaSession2Stub.this.mSession.getCallback().onPrepareFromMediaId(MediaSession2Stub.this.mSession.getInstance(), controllerInfo, str, bundle);
                    return;
                }
                Log.w(MediaSession2Stub.TAG, "prepareFromMediaId(): Ignoring null mediaId from " + controllerInfo);
            }
        });
    }

    @Override // android.support.v4.media.IMediaSession2
    public void prepareFromSearch(IMediaController2 iMediaController2, final String str, final Bundle bundle) {
        onSessionCommand(iMediaController2, 27, new SessionRunnable() { // from class: android.support.v4.media.MediaSession2Stub.15
            @Override // android.support.v4.media.MediaSession2Stub.SessionRunnable
            public void run(MediaSession2.ControllerInfo controllerInfo) {
                if (!TextUtils.isEmpty(str)) {
                    MediaSession2Stub.this.mSession.getCallback().onPrepareFromSearch(MediaSession2Stub.this.mSession.getInstance(), controllerInfo, str, bundle);
                    return;
                }
                Log.w(MediaSession2Stub.TAG, "prepareFromSearch(): Ignoring empty query from " + controllerInfo);
            }
        });
    }

    @Override // android.support.v4.media.IMediaSession2
    public void prepareFromUri(IMediaController2 iMediaController2, final Uri uri, final Bundle bundle) {
        onSessionCommand(iMediaController2, 26, new SessionRunnable() { // from class: android.support.v4.media.MediaSession2Stub.14
            @Override // android.support.v4.media.MediaSession2Stub.SessionRunnable
            public void run(MediaSession2.ControllerInfo controllerInfo) {
                if (uri != null) {
                    MediaSession2Stub.this.mSession.getCallback().onPrepareFromUri(MediaSession2Stub.this.mSession.getInstance(), controllerInfo, uri, bundle);
                    return;
                }
                Log.w(MediaSession2Stub.TAG, "prepareFromUri(): Ignoring null uri from " + controllerInfo);
            }
        });
    }

    @Override // android.support.v4.media.IMediaSession2
    public void release(IMediaController2 iMediaController2) {
        releaseController(iMediaController2);
    }

    void removeControllerInfo(MediaSession2.ControllerInfo controllerInfo) {
        synchronized (this.mLock) {
            Log.d(TAG, "releasing " + this.mControllers.remove(controllerInfo.getId()));
        }
    }

    @Override // android.support.v4.media.IMediaSession2
    public void removePlaylistItem(IMediaController2 iMediaController2, final Bundle bundle) {
        onSessionCommand(iMediaController2, 16, new SessionRunnable() { // from class: android.support.v4.media.MediaSession2Stub.25
            @Override // android.support.v4.media.MediaSession2Stub.SessionRunnable
            public void run(MediaSession2.ControllerInfo controllerInfo) {
                MediaSession2Stub.this.mSession.getInstance().removePlaylistItem(MediaItem2.fromBundle(bundle));
            }
        });
    }

    @Override // android.support.v4.media.IMediaSession2
    public void replacePlaylistItem(IMediaController2 iMediaController2, final int i, final Bundle bundle) {
        onSessionCommand(iMediaController2, 17, new SessionRunnable() { // from class: android.support.v4.media.MediaSession2Stub.26
            @Override // android.support.v4.media.MediaSession2Stub.SessionRunnable
            public void run(MediaSession2.ControllerInfo controllerInfo) {
                MediaSession2Stub.this.mSession.getInstance().replacePlaylistItem(i, MediaItem2.fromBundle(bundle, null));
            }
        });
    }

    @Override // android.support.v4.media.IMediaSession2
    public void reset(IMediaController2 iMediaController2) {
        onSessionCommand(iMediaController2, 3, new SessionRunnable() { // from class: android.support.v4.media.MediaSession2Stub.8
            @Override // android.support.v4.media.MediaSession2Stub.SessionRunnable
            public void run(MediaSession2.ControllerInfo controllerInfo) {
                MediaSession2Stub.this.mSession.reset();
            }
        });
    }

    @Override // android.support.v4.media.IMediaSession2
    public void rewind(IMediaController2 iMediaController2) {
        onSessionCommand(iMediaController2, 8, new SessionRunnable() { // from class: android.support.v4.media.MediaSession2Stub.11
            @Override // android.support.v4.media.MediaSession2Stub.SessionRunnable
            public void run(MediaSession2.ControllerInfo controllerInfo) {
                MediaSession2Stub.this.mSession.getCallback().onRewind(MediaSession2Stub.this.mSession.getInstance(), controllerInfo);
            }
        });
    }

    @Override // android.support.v4.media.IMediaSession2
    public void search(IMediaController2 iMediaController2, final String str, final Bundle bundle) {
        onBrowserCommand(iMediaController2, 33, new SessionRunnable() { // from class: android.support.v4.media.MediaSession2Stub.38
            @Override // android.support.v4.media.MediaSession2Stub.SessionRunnable
            public void run(MediaSession2.ControllerInfo controllerInfo) {
                if (!TextUtils.isEmpty(str)) {
                    MediaSession2Stub.this.getLibrarySession().onSearchOnExecutor(controllerInfo, str, bundle);
                    return;
                }
                Log.w(MediaSession2Stub.TAG, "search(): Ignoring empty query from " + controllerInfo);
            }
        });
    }

    @Override // android.support.v4.media.IMediaSession2
    public void seekTo(IMediaController2 iMediaController2, final long j) {
        onSessionCommand(iMediaController2, 9, new SessionRunnable() { // from class: android.support.v4.media.MediaSession2Stub.12
            @Override // android.support.v4.media.MediaSession2Stub.SessionRunnable
            public void run(MediaSession2.ControllerInfo controllerInfo) {
                MediaSession2Stub.this.mSession.seekTo(j);
            }
        });
    }

    @Override // android.support.v4.media.IMediaSession2
    public void selectRoute(IMediaController2 iMediaController2, final Bundle bundle) {
        onSessionCommand(iMediaController2, 37, new SessionRunnable() { // from class: android.support.v4.media.MediaSession2Stub.34
            @Override // android.support.v4.media.MediaSession2Stub.SessionRunnable
            public void run(MediaSession2.ControllerInfo controllerInfo) {
                MediaSession2Stub.this.mSession.getCallback().onSelectRoute(MediaSession2Stub.this.mSession.getInstance(), controllerInfo, bundle);
            }
        });
    }

    @Override // android.support.v4.media.IMediaSession2
    public void sendCustomCommand(IMediaController2 iMediaController2, Bundle bundle, final Bundle bundle2, final ResultReceiver resultReceiver) {
        final SessionCommand2 sessionCommand2FromBundle = SessionCommand2.fromBundle(bundle);
        onSessionCommand(iMediaController2, SessionCommand2.fromBundle(bundle), new SessionRunnable() { // from class: android.support.v4.media.MediaSession2Stub.13
            @Override // android.support.v4.media.MediaSession2Stub.SessionRunnable
            public void run(MediaSession2.ControllerInfo controllerInfo) {
                MediaSession2Stub.this.mSession.getCallback().onCustomCommand(MediaSession2Stub.this.mSession.getInstance(), controllerInfo, sessionCommand2FromBundle, bundle2, resultReceiver);
            }
        });
    }

    void setAllowedCommands(MediaSession2.ControllerInfo controllerInfo, SessionCommandGroup2 sessionCommandGroup2) {
        synchronized (this.mLock) {
            this.mAllowedCommandGroupMap.put(controllerInfo, sessionCommandGroup2);
        }
    }

    @Override // android.support.v4.media.IMediaSession2
    public void setPlaybackSpeed(IMediaController2 iMediaController2, final float f) {
        onSessionCommand(iMediaController2, 39, new SessionRunnable() { // from class: android.support.v4.media.MediaSession2Stub.21
            @Override // android.support.v4.media.MediaSession2Stub.SessionRunnable
            public void run(MediaSession2.ControllerInfo controllerInfo) {
                MediaSession2Stub.this.mSession.getInstance().setPlaybackSpeed(f);
            }
        });
    }

    @Override // android.support.v4.media.IMediaSession2
    public void setPlaylist(IMediaController2 iMediaController2, final List<Bundle> list, final Bundle bundle) {
        onSessionCommand(iMediaController2, 19, new SessionRunnable() { // from class: android.support.v4.media.MediaSession2Stub.22
            @Override // android.support.v4.media.MediaSession2Stub.SessionRunnable
            public void run(MediaSession2.ControllerInfo controllerInfo) {
                if (list != null) {
                    MediaSession2Stub.this.mSession.getInstance().setPlaylist(MediaUtils2.convertBundleListToMediaItem2List(list), MediaMetadata2.fromBundle(bundle));
                    return;
                }
                Log.w(MediaSession2Stub.TAG, "setPlaylist(): Ignoring null playlist from " + controllerInfo);
            }
        });
    }

    @Override // android.support.v4.media.IMediaSession2
    public void setRating(IMediaController2 iMediaController2, final String str, final Bundle bundle) {
        onSessionCommand(iMediaController2, 28, new SessionRunnable() { // from class: android.support.v4.media.MediaSession2Stub.20
            @Override // android.support.v4.media.MediaSession2Stub.SessionRunnable
            public void run(MediaSession2.ControllerInfo controllerInfo) {
                if (str == null) {
                    Log.w(MediaSession2Stub.TAG, "setRating(): Ignoring null mediaId from " + controllerInfo);
                    return;
                }
                Bundle bundle2 = bundle;
                if (bundle2 == null) {
                    Log.w(MediaSession2Stub.TAG, "setRating(): Ignoring null ratingBundle from " + controllerInfo);
                    return;
                }
                Rating2 rating2FromBundle = Rating2.fromBundle(bundle2);
                if (rating2FromBundle != null) {
                    MediaSession2Stub.this.mSession.getCallback().onSetRating(MediaSession2Stub.this.mSession.getInstance(), controllerInfo, str, rating2FromBundle);
                } else if (bundle == null) {
                    Log.w(MediaSession2Stub.TAG, "setRating(): Ignoring null rating from " + controllerInfo);
                }
            }
        });
    }

    @Override // android.support.v4.media.IMediaSession2
    public void setRepeatMode(IMediaController2 iMediaController2, final int i) {
        onSessionCommand(iMediaController2, 14, new SessionRunnable() { // from class: android.support.v4.media.MediaSession2Stub.30
            @Override // android.support.v4.media.MediaSession2Stub.SessionRunnable
            public void run(MediaSession2.ControllerInfo controllerInfo) {
                MediaSession2Stub.this.mSession.getInstance().setRepeatMode(i);
            }
        });
    }

    @Override // android.support.v4.media.IMediaSession2
    public void setShuffleMode(IMediaController2 iMediaController2, final int i) {
        onSessionCommand(iMediaController2, 13, new SessionRunnable() { // from class: android.support.v4.media.MediaSession2Stub.31
            @Override // android.support.v4.media.MediaSession2Stub.SessionRunnable
            public void run(MediaSession2.ControllerInfo controllerInfo) {
                MediaSession2Stub.this.mSession.getInstance().setShuffleMode(i);
            }
        });
    }

    @Override // android.support.v4.media.IMediaSession2
    public void setVolumeTo(IMediaController2 iMediaController2, final int i, final int i2) {
        onSessionCommand(iMediaController2, 10, new SessionRunnable() { // from class: android.support.v4.media.MediaSession2Stub.4
            @Override // android.support.v4.media.MediaSession2Stub.SessionRunnable
            public void run(MediaSession2.ControllerInfo controllerInfo) {
                VolumeProviderCompat volumeProvider = MediaSession2Stub.this.mSession.getVolumeProvider();
                if (volumeProvider != null) {
                    volumeProvider.onSetVolumeTo(i);
                    return;
                }
                MediaSessionCompat sessionCompat = MediaSession2Stub.this.mSession.getSessionCompat();
                if (sessionCompat != null) {
                    sessionCompat.getController().setVolumeTo(i, i2);
                }
            }
        });
    }

    @Override // android.support.v4.media.IMediaSession2
    public void skipToNextItem(IMediaController2 iMediaController2) {
        onSessionCommand(iMediaController2, 4, new SessionRunnable() { // from class: android.support.v4.media.MediaSession2Stub.29
            @Override // android.support.v4.media.MediaSession2Stub.SessionRunnable
            public void run(MediaSession2.ControllerInfo controllerInfo) {
                MediaSession2Stub.this.mSession.getInstance().skipToNextItem();
            }
        });
    }

    @Override // android.support.v4.media.IMediaSession2
    public void skipToPlaylistItem(IMediaController2 iMediaController2, final Bundle bundle) {
        onSessionCommand(iMediaController2, 12, new SessionRunnable() { // from class: android.support.v4.media.MediaSession2Stub.27
            @Override // android.support.v4.media.MediaSession2Stub.SessionRunnable
            public void run(MediaSession2.ControllerInfo controllerInfo) {
                if (bundle == null) {
                    Log.w(MediaSession2Stub.TAG, "skipToPlaylistItem(): Ignoring null mediaItem from " + controllerInfo);
                }
                MediaSession2Stub.this.mSession.getInstance().skipToPlaylistItem(MediaItem2.fromBundle(bundle));
            }
        });
    }

    @Override // android.support.v4.media.IMediaSession2
    public void skipToPreviousItem(IMediaController2 iMediaController2) {
        onSessionCommand(iMediaController2, 5, new SessionRunnable() { // from class: android.support.v4.media.MediaSession2Stub.28
            @Override // android.support.v4.media.MediaSession2Stub.SessionRunnable
            public void run(MediaSession2.ControllerInfo controllerInfo) {
                MediaSession2Stub.this.mSession.getInstance().skipToPreviousItem();
            }
        });
    }

    @Override // android.support.v4.media.IMediaSession2
    public void subscribe(IMediaController2 iMediaController2, final String str, final Bundle bundle) {
        onBrowserCommand(iMediaController2, 34, new SessionRunnable() { // from class: android.support.v4.media.MediaSession2Stub.40
            @Override // android.support.v4.media.MediaSession2Stub.SessionRunnable
            public void run(MediaSession2.ControllerInfo controllerInfo) {
                if (str != null) {
                    MediaSession2Stub.this.getLibrarySession().onSubscribeOnExecutor(controllerInfo, str, bundle);
                    return;
                }
                Log.w(MediaSession2Stub.TAG, "subscribe(): Ignoring null parentId from " + controllerInfo);
            }
        });
    }

    @Override // android.support.v4.media.IMediaSession2
    public void subscribeRoutesInfo(IMediaController2 iMediaController2) {
        onSessionCommand(iMediaController2, 36, new SessionRunnable() { // from class: android.support.v4.media.MediaSession2Stub.32
            @Override // android.support.v4.media.MediaSession2Stub.SessionRunnable
            public void run(MediaSession2.ControllerInfo controllerInfo) {
                MediaSession2Stub.this.mSession.getCallback().onSubscribeRoutesInfo(MediaSession2Stub.this.mSession.getInstance(), controllerInfo);
            }
        });
    }

    @Override // android.support.v4.media.IMediaSession2
    public void unsubscribe(IMediaController2 iMediaController2, final String str) {
        onBrowserCommand(iMediaController2, 35, new SessionRunnable() { // from class: android.support.v4.media.MediaSession2Stub.41
            @Override // android.support.v4.media.MediaSession2Stub.SessionRunnable
            public void run(MediaSession2.ControllerInfo controllerInfo) {
                if (str != null) {
                    MediaSession2Stub.this.getLibrarySession().onUnsubscribeOnExecutor(controllerInfo, str);
                    return;
                }
                Log.w(MediaSession2Stub.TAG, "unsubscribe(): Ignoring null parentId from " + controllerInfo);
            }
        });
    }

    @Override // android.support.v4.media.IMediaSession2
    public void unsubscribeRoutesInfo(IMediaController2 iMediaController2) {
        onSessionCommand(iMediaController2, 37, new SessionRunnable() { // from class: android.support.v4.media.MediaSession2Stub.33
            @Override // android.support.v4.media.MediaSession2Stub.SessionRunnable
            public void run(MediaSession2.ControllerInfo controllerInfo) {
                MediaSession2Stub.this.mSession.getCallback().onUnsubscribeRoutesInfo(MediaSession2Stub.this.mSession.getInstance(), controllerInfo);
            }
        });
    }

    @Override // android.support.v4.media.IMediaSession2
    public void updatePlaylistMetadata(IMediaController2 iMediaController2, final Bundle bundle) {
        onSessionCommand(iMediaController2, 21, new SessionRunnable() { // from class: android.support.v4.media.MediaSession2Stub.23
            @Override // android.support.v4.media.MediaSession2Stub.SessionRunnable
            public void run(MediaSession2.ControllerInfo controllerInfo) {
                MediaSession2Stub.this.mSession.getInstance().updatePlaylistMetadata(MediaMetadata2.fromBundle(bundle));
            }
        });
    }

    private void onSessionCommand(@NonNull IMediaController2 iMediaController2, @NonNull SessionCommand2 sessionCommand2, @NonNull SessionRunnable sessionRunnable) {
        onSessionCommandInternal(iMediaController2, sessionCommand2, 0, sessionRunnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isAllowedCommand(MediaSession2.ControllerInfo controllerInfo, int i) {
        SessionCommandGroup2 sessionCommandGroup2;
        synchronized (this.mLock) {
            sessionCommandGroup2 = this.mAllowedCommandGroupMap.get(controllerInfo);
        }
        if (sessionCommandGroup2 == null || !sessionCommandGroup2.hasCommand(i)) {
            return false;
        }
        return DEBUG;
    }
}
