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

/* JADX INFO: loaded from: classes3.dex */
@TargetApi(19)
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
        int size2 = i;
        for (int i3 = 0; i3 < size; i3++) {
            size2 += i2;
            if (size2 < 0 || size2 >= this.mPlaylist.size()) {
                if (this.mRepeatMode == 0) {
                    if (i3 == size - 1) {
                        return null;
                    }
                    return this.mEopPlayItem;
                }
                size2 = size2 < 0 ? this.mPlaylist.size() - 1 : 0;
            }
            DataSourceDesc dataSourceDescRetrieveDataSourceDescLocked = retrieveDataSourceDescLocked(this.mShuffledList.get(size2));
            if (dataSourceDescRetrieveDataSourceDescLocked != null) {
                return new PlayItem(size2, dataSourceDescRetrieveDataSourceDescLocked);
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
        DataSourceDesc dataSourceDescOnDataSourceMissing = this.mItemDsdMap.get(mediaItem2);
        if (dataSourceDescOnDataSourceMissing != null) {
            return dataSourceDescOnDataSourceMissing;
        }
        MediaSession2.OnDataSourceMissingHelper onDataSourceMissingHelper = this.mDsmHelper;
        if (onDataSourceMissingHelper != null && (dataSourceDescOnDataSourceMissing = onDataSourceMissingHelper.onDataSourceMissing(this.mSession.getInstance(), mediaItem2)) != null) {
            this.mItemDsdMap.put(mediaItem2, dataSourceDescOnDataSourceMissing);
        }
        return dataSourceDescOnDataSourceMissing;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCurrentIfNeededLocked() {
        if (!hasValidItem() || this.mCurrent.isValid()) {
            return;
        }
        int iIndexOf = this.mShuffledList.indexOf(this.mCurrent.mediaItem);
        if (iIndexOf >= 0) {
            this.mCurrent.shuffledIdx = iIndexOf;
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
            int iClamp = clamp(i, this.mPlaylist.size());
            this.mPlaylist.add(iClamp, mediaItem2);
            if (this.mShuffleMode == 0) {
                this.mShuffledList.add(iClamp, mediaItem2);
            } else {
                this.mShuffledList.add((int) (Math.random() * ((double) (this.mShuffledList.size() + 1))), mediaItem2);
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
        List<MediaItem2> listUnmodifiableList;
        synchronized (this.mLock) {
            listUnmodifiableList = Collections.unmodifiableList(this.mPlaylist);
        }
        return listUnmodifiableList;
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
            int iClamp = clamp(i, this.mPlaylist.size() - 1);
            int iIndexOf = this.mShuffledList.indexOf(this.mPlaylist.get(iClamp));
            this.mItemDsdMap.remove(this.mShuffledList.get(iIndexOf));
            this.mShuffledList.set(iIndexOf, mediaItem2);
            this.mPlaylist.set(iClamp, mediaItem2);
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
            if (i == 0) {
                this.mPlayer.loopCurrent(false);
            } else if (i != 1) {
                if (i == 2 || i == 3) {
                    if (this.mCurrent == this.mEopPlayItem) {
                        this.mCurrent = getNextValidPlayItemLocked(-1, 1);
                        updatePlayerDataSourceLocked();
                    }
                    this.mPlayer.loopCurrent(false);
                }
            } else if (this.mCurrent != null && this.mCurrent != this.mEopPlayItem) {
                this.mPlayer.loopCurrent(true);
            }
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
                int iIndexOf = this.mShuffledList.indexOf(mediaItem2);
                if (iIndexOf < 0) {
                    return;
                }
                this.mCurrent = new PlayItem(this, iIndexOf);
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
