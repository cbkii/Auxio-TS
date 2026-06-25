package android.support.v4.media;

import android.annotation.TargetApi;
import android.support.annotation.GuardedBy;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.media.BaseMediaPlayer;
import android.support.v4.media.MediaSession2;
import android.support.v4.util.ArrayMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@TargetApi(19)
/* loaded from: classes3.dex */
class SessionPlaylistAgentImplBase extends MediaPlaylistAgent {

    @VisibleForTesting
    static final int END_OF_PLAYLIST = -1;

    @VisibleForTesting
    static final int NO_VALID_ITEMS = -2;

    @GuardedBy("mLock")
    private PlayItem mCurrent;

    @GuardedBy("mLock")
    private MediaSession2.OnDataSourceMissingHelper mDsmHelper;

    @GuardedBy("mLock")
    private MediaMetadata2 mMetadata;

    @GuardedBy("mLock")
    private BaseMediaPlayer mPlayer;
    private final MyPlayerEventCallback mPlayerCallback;

    @GuardedBy("mLock")
    private int mRepeatMode;
    private final MediaSession2ImplBase mSession;

    @GuardedBy("mLock")
    private int mShuffleMode;
    private final PlayItem mEopPlayItem = new PlayItem(-1, null);
    private final Object mLock = new Object();

    @GuardedBy("mLock")
    private ArrayList<MediaItem2> mPlaylist = new ArrayList<>();

    @GuardedBy("mLock")
    private ArrayList<MediaItem2> mShuffledList = new ArrayList<>();

    @GuardedBy("mLock")
    private Map<MediaItem2, DataSourceDesc> mItemDsdMap = new ArrayMap();

    private class MyPlayerEventCallback extends BaseMediaPlayer.PlayerEventCallback {
        private MyPlayerEventCallback() {
        }

        @Override // android.support.v4.media.BaseMediaPlayer.PlayerEventCallback
        public void onCurrentDataSourceChanged(@NonNull BaseMediaPlayer baseMediaPlayer, @Nullable DataSourceDesc dataSourceDesc) {
            synchronized (SessionPlaylistAgentImplBase.this.mLock) {
                if (SessionPlaylistAgentImplBase.this.mPlayer != baseMediaPlayer) {
                    return;
                }
                if (dataSourceDesc == null && SessionPlaylistAgentImplBase.this.mCurrent != null) {
                    SessionPlaylistAgentImplBase.this.mCurrent = SessionPlaylistAgentImplBase.this.getNextValidPlayItemLocked(SessionPlaylistAgentImplBase.this.mCurrent.shuffledIdx, 1);
                    SessionPlaylistAgentImplBase.this.updateCurrentIfNeededLocked();
                }
            }
        }
    }

    private class PlayItem {
        public DataSourceDesc dsd;
        public MediaItem2 mediaItem;
        public int shuffledIdx;

        PlayItem(SessionPlaylistAgentImplBase sessionPlaylistAgentImplBase, int i) {
            this(i, null);
        }

        boolean isValid() {
            if (this == SessionPlaylistAgentImplBase.this.mEopPlayItem) {
                return true;
            }
            MediaItem2 mediaItem2 = this.mediaItem;
            if (mediaItem2 == null || this.dsd == null) {
                return false;
            }
            if (mediaItem2.getDataSourceDesc() != null && !this.mediaItem.getDataSourceDesc().equals(this.dsd)) {
                return false;
            }
            synchronized (SessionPlaylistAgentImplBase.this.mLock) {
                if (this.shuffledIdx >= SessionPlaylistAgentImplBase.this.mShuffledList.size()) {
                    return false;
                }
                return this.mediaItem == SessionPlaylistAgentImplBase.this.mShuffledList.get(this.shuffledIdx);
            }
        }

        PlayItem(int i, DataSourceDesc dataSourceDesc) {
            this.shuffledIdx = i;
            if (i >= 0) {
                this.mediaItem = (MediaItem2) SessionPlaylistAgentImplBase.this.mShuffledList.get(i);
                if (dataSourceDesc != null) {
                    this.dsd = dataSourceDesc;
                    return;
                }
                synchronized (SessionPlaylistAgentImplBase.this.mLock) {
                    this.dsd = SessionPlaylistAgentImplBase.this.retrieveDataSourceDescLocked(this.mediaItem);
                }
            }
        }
    }

    SessionPlaylistAgentImplBase(@NonNull MediaSession2ImplBase mediaSession2ImplBase, @NonNull BaseMediaPlayer baseMediaPlayer) {
        if (mediaSession2ImplBase == null) {
            throw new IllegalArgumentException("sessionImpl shouldn't be null");
        }
        if (baseMediaPlayer == null) {
            throw new IllegalArgumentException("player shouldn't be null");
        }
        this.mSession = mediaSession2ImplBase;
        this.mPlayer = baseMediaPlayer;
        this.mPlayerCallback = new MyPlayerEventCallback();
        this.mPlayer.registerPlayerEventCallback(this.mSession.getCallbackExecutor(), this.mPlayerCallback);
    }

    private void applyShuffleModeLocked() {
        this.mShuffledList.clear();
        this.mShuffledList.addAll(this.mPlaylist);
        int i = this.mShuffleMode;
        if (i == 1 || i == 2) {
            Collections.shuffle(this.mShuffledList);
        }
    }

    private static int clamp(int i, int i2) {
        if (i < 0) {
            return 0;
        }
        return i > i2 ? i2 : i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public PlayItem getNextValidPlayItemLocked(int i, int i2) {
        int size = this.mPlaylist.size();
        if (i == -1) {
            i = i2 > 0 ? -1 : size;
        }
        int i3 = i;
        for (int i4 = 0; i4 < size; i4++) {
            i3 += i2;
            if (i3 < 0 || i3 >= this.mPlaylist.size()) {
                if (this.mRepeatMode == 0) {
                    if (i4 == size - 1) {
                        return null;
                    }
                    return this.mEopPlayItem;
                }
                i3 = i3 < 0 ? this.mPlaylist.size() - 1 : 0;
            }
            DataSourceDesc retrieveDataSourceDescLocked = retrieveDataSourceDescLocked(this.mShuffledList.get(i3));
            if (retrieveDataSourceDescLocked != null) {
                return new PlayItem(i3, retrieveDataSourceDescLocked);
            }
        }
        return null;
    }

    private boolean hasValidItem() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mCurrent != null;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public DataSourceDesc retrieveDataSourceDescLocked(MediaItem2 mediaItem2) {
        DataSourceDesc dataSourceDesc = mediaItem2.getDataSourceDesc();
        if (dataSourceDesc != null) {
            this.mItemDsdMap.put(mediaItem2, dataSourceDesc);
            return dataSourceDesc;
        }
        DataSourceDesc dataSourceDesc2 = this.mItemDsdMap.get(mediaItem2);
        if (dataSourceDesc2 != null) {
            return dataSourceDesc2;
        }
        MediaSession2.OnDataSourceMissingHelper onDataSourceMissingHelper = this.mDsmHelper;
        if (onDataSourceMissingHelper != null && (dataSourceDesc2 = onDataSourceMissingHelper.onDataSourceMissing(this.mSession.getInstance(), mediaItem2)) != null) {
            this.mItemDsdMap.put(mediaItem2, dataSourceDesc2);
        }
        return dataSourceDesc2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCurrentIfNeededLocked() {
        if (!hasValidItem() || this.mCurrent.isValid()) {
            return;
        }
        int indexOf = this.mShuffledList.indexOf(this.mCurrent.mediaItem);
        if (indexOf >= 0) {
            this.mCurrent.shuffledIdx = indexOf;
            return;
        }
        if (this.mCurrent.shuffledIdx >= this.mShuffledList.size()) {
            this.mCurrent = getNextValidPlayItemLocked(this.mShuffledList.size() - 1, 1);
        } else {
            PlayItem playItem = this.mCurrent;
            playItem.mediaItem = this.mShuffledList.get(playItem.shuffledIdx);
            if (retrieveDataSourceDescLocked(this.mCurrent.mediaItem) == null) {
                this.mCurrent = getNextValidPlayItemLocked(this.mCurrent.shuffledIdx, 1);
            }
        }
        updatePlayerDataSourceLocked();
    }

    private void updatePlayerDataSourceLocked() {
        PlayItem playItem = this.mCurrent;
        if (playItem == null || playItem == this.mEopPlayItem) {
            return;
        }
        DataSourceDesc currentDataSource = this.mPlayer.getCurrentDataSource();
        DataSourceDesc dataSourceDesc = this.mCurrent.dsd;
        if (currentDataSource != dataSourceDesc) {
            this.mPlayer.setDataSource(dataSourceDesc);
            this.mPlayer.loopCurrent(this.mRepeatMode == 1);
        }
    }

    @Override // android.support.v4.media.MediaPlaylistAgent
    public void addPlaylistItem(int i, @NonNull MediaItem2 mediaItem2) {
        if (mediaItem2 == null) {
            throw new IllegalArgumentException("item shouldn't be null");
        }
        synchronized (this.mLock) {
            int clamp = clamp(i, this.mPlaylist.size());
            this.mPlaylist.add(clamp, mediaItem2);
            if (this.mShuffleMode == 0) {
                this.mShuffledList.add(clamp, mediaItem2);
            } else {
                this.mShuffledList.add((int) (Math.random() * (this.mShuffledList.size() + 1)), mediaItem2);
            }
            if (hasValidItem()) {
                updateCurrentIfNeededLocked();
            } else {
                this.mCurrent = getNextValidPlayItemLocked(-1, 1);
                updatePlayerDataSourceLocked();
            }
        }
        notifyPlaylistChanged();
    }

    public void clearOnDataSourceMissingHelper() {
        synchronized (this.mLock) {
            this.mDsmHelper = null;
        }
    }

    @VisibleForTesting
    int getCurShuffledIndex() {
        int i;
        synchronized (this.mLock) {
            i = hasValidItem() ? this.mCurrent.shuffledIdx : -2;
        }
        return i;
    }

    @Override // android.support.v4.media.MediaPlaylistAgent
    public MediaItem2 getCurrentMediaItem() {
        MediaItem2 mediaItem2;
        synchronized (this.mLock) {
            mediaItem2 = this.mCurrent == null ? null : this.mCurrent.mediaItem;
        }
        return mediaItem2;
    }

    @Override // android.support.v4.media.MediaPlaylistAgent
    public MediaItem2 getMediaItem(DataSourceDesc dataSourceDesc) {
        return null;
    }

    @Override // android.support.v4.media.MediaPlaylistAgent
    @Nullable
    public List<MediaItem2> getPlaylist() {
        List<MediaItem2> unmodifiableList;
        synchronized (this.mLock) {
            unmodifiableList = Collections.unmodifiableList(this.mPlaylist);
        }
        return unmodifiableList;
    }

    @Override // android.support.v4.media.MediaPlaylistAgent
    @Nullable
    public MediaMetadata2 getPlaylistMetadata() {
        MediaMetadata2 mediaMetadata2;
        synchronized (this.mLock) {
            mediaMetadata2 = this.mMetadata;
        }
        return mediaMetadata2;
    }

    @Override // android.support.v4.media.MediaPlaylistAgent
    public int getRepeatMode() {
        int i;
        synchronized (this.mLock) {
            i = this.mRepeatMode;
        }
        return i;
    }

    @Override // android.support.v4.media.MediaPlaylistAgent
    public int getShuffleMode() {
        int i;
        synchronized (this.mLock) {
            i = this.mShuffleMode;
        }
        return i;
    }

    @Override // android.support.v4.media.MediaPlaylistAgent
    public void removePlaylistItem(@NonNull MediaItem2 mediaItem2) {
        if (mediaItem2 == null) {
            throw new IllegalArgumentException("item shouldn't be null");
        }
        synchronized (this.mLock) {
            if (this.mPlaylist.remove(mediaItem2)) {
                this.mShuffledList.remove(mediaItem2);
                this.mItemDsdMap.remove(mediaItem2);
                updateCurrentIfNeededLocked();
                notifyPlaylistChanged();
            }
        }
    }

    @Override // android.support.v4.media.MediaPlaylistAgent
    public void replacePlaylistItem(int i, @NonNull MediaItem2 mediaItem2) {
        if (mediaItem2 == null) {
            throw new IllegalArgumentException("item shouldn't be null");
        }
        synchronized (this.mLock) {
            if (this.mPlaylist.size() <= 0) {
                return;
            }
            int clamp = clamp(i, this.mPlaylist.size() - 1);
            int indexOf = this.mShuffledList.indexOf(this.mPlaylist.get(clamp));
            this.mItemDsdMap.remove(this.mShuffledList.get(indexOf));
            this.mShuffledList.set(indexOf, mediaItem2);
            this.mPlaylist.set(clamp, mediaItem2);
            if (hasValidItem()) {
                updateCurrentIfNeededLocked();
            } else {
                this.mCurrent = getNextValidPlayItemLocked(-1, 1);
                updatePlayerDataSourceLocked();
            }
            notifyPlaylistChanged();
        }
    }

    public void setOnDataSourceMissingHelper(MediaSession2.OnDataSourceMissingHelper onDataSourceMissingHelper) {
        synchronized (this.mLock) {
            this.mDsmHelper = onDataSourceMissingHelper;
        }
    }

    public void setPlayer(@NonNull BaseMediaPlayer baseMediaPlayer) {
        if (baseMediaPlayer == null) {
            throw new IllegalArgumentException("player shouldn't be null");
        }
        synchronized (this.mLock) {
            if (baseMediaPlayer == this.mPlayer) {
                return;
            }
            this.mPlayer.unregisterPlayerEventCallback(this.mPlayerCallback);
            this.mPlayer = baseMediaPlayer;
            this.mPlayer.registerPlayerEventCallback(this.mSession.getCallbackExecutor(), this.mPlayerCallback);
            updatePlayerDataSourceLocked();
        }
    }

    @Override // android.support.v4.media.MediaPlaylistAgent
    public void setPlaylist(@NonNull List<MediaItem2> list, @Nullable MediaMetadata2 mediaMetadata2) {
        if (list == null) {
            throw new IllegalArgumentException("list shouldn't be null");
        }
        synchronized (this.mLock) {
            this.mItemDsdMap.clear();
            this.mPlaylist.clear();
            this.mPlaylist.addAll(list);
            applyShuffleModeLocked();
            this.mMetadata = mediaMetadata2;
            this.mCurrent = getNextValidPlayItemLocked(-1, 1);
            updatePlayerDataSourceLocked();
        }
        notifyPlaylistChanged();
    }

    @Override // android.support.v4.media.MediaPlaylistAgent
    public void setRepeatMode(int i) {
        if (i < 0 || i > 3) {
            return;
        }
        synchronized (this.mLock) {
            if (this.mRepeatMode == i) {
                return;
            }
            this.mRepeatMode = i;
            if (i != 0) {
                if (i != 1) {
                    if (i == 2 || i == 3) {
                        if (this.mCurrent == this.mEopPlayItem) {
                            this.mCurrent = getNextValidPlayItemLocked(-1, 1);
                            updatePlayerDataSourceLocked();
                        }
                    }
                } else if (this.mCurrent != null && this.mCurrent != this.mEopPlayItem) {
                    this.mPlayer.loopCurrent(true);
                }
                notifyRepeatModeChanged();
            }
            this.mPlayer.loopCurrent(false);
            notifyRepeatModeChanged();
        }
    }

    @Override // android.support.v4.media.MediaPlaylistAgent
    public void setShuffleMode(int i) {
        if (i < 0 || i > 2) {
            return;
        }
        synchronized (this.mLock) {
            if (this.mShuffleMode == i) {
                return;
            }
            this.mShuffleMode = i;
            applyShuffleModeLocked();
            updateCurrentIfNeededLocked();
            notifyShuffleModeChanged();
        }
    }

    @Override // android.support.v4.media.MediaPlaylistAgent
    public void skipToNextItem() {
        synchronized (this.mLock) {
            if (hasValidItem() && this.mCurrent != this.mEopPlayItem) {
                PlayItem nextValidPlayItemLocked = getNextValidPlayItemLocked(this.mCurrent.shuffledIdx, 1);
                if (nextValidPlayItemLocked != this.mEopPlayItem) {
                    this.mCurrent = nextValidPlayItemLocked;
                }
                updateCurrentIfNeededLocked();
            }
        }
    }

    @Override // android.support.v4.media.MediaPlaylistAgent
    public void skipToPlaylistItem(@NonNull MediaItem2 mediaItem2) {
        if (mediaItem2 == null) {
            throw new IllegalArgumentException("item shouldn't be null");
        }
        synchronized (this.mLock) {
            if (hasValidItem() && !mediaItem2.equals(this.mCurrent.mediaItem)) {
                int indexOf = this.mShuffledList.indexOf(mediaItem2);
                if (indexOf < 0) {
                    return;
                }
                this.mCurrent = new PlayItem(this, indexOf);
                updateCurrentIfNeededLocked();
            }
        }
    }

    @Override // android.support.v4.media.MediaPlaylistAgent
    public void skipToPreviousItem() {
        synchronized (this.mLock) {
            if (hasValidItem()) {
                PlayItem nextValidPlayItemLocked = getNextValidPlayItemLocked(this.mCurrent.shuffledIdx, -1);
                if (nextValidPlayItemLocked != this.mEopPlayItem) {
                    this.mCurrent = nextValidPlayItemLocked;
                }
                updateCurrentIfNeededLocked();
            }
        }
    }

    @Override // android.support.v4.media.MediaPlaylistAgent
    public void updatePlaylistMetadata(@Nullable MediaMetadata2 mediaMetadata2) {
        synchronized (this.mLock) {
            if (mediaMetadata2 == this.mMetadata) {
                return;
            }
            this.mMetadata = mediaMetadata2;
            notifyPlaylistMetadataChanged();
        }
    }
}
