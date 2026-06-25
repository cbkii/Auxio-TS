package tv.danmaku.ijk.media.player.p069tw;

import android.content.Context;
import android.net.Uri;
import android.os.SystemProperties;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import tv.danmaku.ijk.media.player.AndroidMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/* JADX INFO: loaded from: classes4.dex */
public class TWMediaPlayer {
    public static final int PV_PLAYER__AndroidMediaPlayer = 1;
    public static final int PV_PLAYER__Auto = 0;
    public static final int PV_PLAYER__IjkExoMediaPlayer = 3;
    public static final int PV_PLAYER__IjkMediaPlayer = 2;
    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PAUSED = 4;
    private static final int STATE_PLAYBACK_COMPLETED = 5;
    private static final int STATE_PLAYING = 3;
    private static final int STATE_PREPARED = 2;
    private static final int STATE_PREPARING = 1;
    private static final String SYSTEM_ETC = "/system/etc/";
    private static final String SYSTEM_TW_ETC = "/system_tw/etc/";
    public static final int TWIjkNoError_ERROR = 1;
    public static final int TWIjkNoError_NOERROR = 0;
    public static final int TWIjkOpenSLES_OFF = 0;
    public static final int TWIjkOpenSLES_ON = 1;
    public static final int TWIjk_Android = 3;
    public static final int TWIjk_AndroidIjk = 0;
    public static final int TWIjk_Exo = 4;
    public static final int TWIjk_ExoIjk = 1;
    public static final int TWIjk_Ijk = 2;
    private Context mContext;
    private int mCurrentBufferPercentage;
    private Map<String, String> mHeaders;
    private IMediaPlayer.OnCompletionListener mOnCompletionListener;
    private IMediaPlayer.OnErrorListener mOnErrorListener;
    private IMediaPlayer.OnInfoListener mOnInfoListener;
    private IMediaPlayer.OnPreparedListener mOnPreparedListener;
    private int mSeekWhenPrepared;
    private Uri mUri;
    private static final ArrayList<String> mAudioList = readMediaList2("ijk.audio");
    private static final ArrayList<String> mAndroidAudioList = readMediaList2("android.audio");
    private static final ArrayList<String> mExoAudioList = readMediaList2("exo.audio");
    private String TAG = "TWMediaPlayer";
    private int mCurrentState = 0;
    private int mTargetState = 0;
    private IMediaPlayer mMediaPlayer = null;
    private long mPrepareStartTime = 0;
    private long mPrepareEndTime = 0;
    private long mSeekStartTime = 0;
    private long mSeekEndTime = 0;
    IMediaPlayer.OnPreparedListener mPreparedListener = new IMediaPlayer.OnPreparedListener() { // from class: tv.danmaku.ijk.media.player.tw.TWMediaPlayer.1
        @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnPreparedListener
        public void onPrepared(IMediaPlayer iMediaPlayer) {
            TWMediaPlayer.this.mPrepareEndTime = System.currentTimeMillis();
            Log.d(TWMediaPlayer.this.TAG, "LoadCost=" + (TWMediaPlayer.this.mPrepareEndTime - TWMediaPlayer.this.mPrepareStartTime));
            TWMediaPlayer.this.mCurrentState = 2;
            if (TWMediaPlayer.this.mOnPreparedListener != null) {
                TWMediaPlayer.this.mOnPreparedListener.onPrepared(TWMediaPlayer.this.mMediaPlayer);
            }
            int i = TWMediaPlayer.this.mSeekWhenPrepared;
            if (i != 0) {
                TWMediaPlayer.this.seekTo(i);
            }
            if (TWMediaPlayer.this.mTargetState == 3) {
                TWMediaPlayer.this.start();
            }
        }
    };
    private IMediaPlayer.OnCompletionListener mCompletionListener = new IMediaPlayer.OnCompletionListener() { // from class: tv.danmaku.ijk.media.player.tw.TWMediaPlayer.2
        @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnCompletionListener
        public void onCompletion(IMediaPlayer iMediaPlayer) {
            TWMediaPlayer.this.mCurrentState = 5;
            TWMediaPlayer.this.mTargetState = 5;
            if (TWMediaPlayer.this.mOnCompletionListener != null) {
                TWMediaPlayer.this.mOnCompletionListener.onCompletion(TWMediaPlayer.this.mMediaPlayer);
            }
        }
    };
    private IMediaPlayer.OnInfoListener mInfoListener = new IMediaPlayer.OnInfoListener() { // from class: tv.danmaku.ijk.media.player.tw.TWMediaPlayer.3
        @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnInfoListener
        public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i2) {
            if (TWMediaPlayer.this.mOnInfoListener != null) {
                TWMediaPlayer.this.mOnInfoListener.onInfo(iMediaPlayer, i, i2);
            }
            if (i == 3) {
                Log.d(TWMediaPlayer.this.TAG, "MEDIA_INFO_VIDEO_RENDERING_START:");
                return true;
            }
            if (i == 901) {
                Log.d(TWMediaPlayer.this.TAG, "MEDIA_INFO_UNSUPPORTED_SUBTITLE:");
                return true;
            }
            if (i == 902) {
                Log.d(TWMediaPlayer.this.TAG, "MEDIA_INFO_SUBTITLE_TIMED_OUT:");
                return true;
            }
            if (i == 10001) {
                Log.d(TWMediaPlayer.this.TAG, "MEDIA_INFO_VIDEO_ROTATION_CHANGED: " + i2);
                return true;
            }
            if (i == 10002) {
                Log.d(TWMediaPlayer.this.TAG, "MEDIA_INFO_AUDIO_RENDERING_START:");
                return true;
            }
            switch (i) {
                case 700:
                    Log.d(TWMediaPlayer.this.TAG, "MEDIA_INFO_VIDEO_TRACK_LAGGING:");
                    break;
                case 701:
                    Log.d(TWMediaPlayer.this.TAG, "MEDIA_INFO_BUFFERING_START:");
                    break;
                case 702:
                    Log.d(TWMediaPlayer.this.TAG, "MEDIA_INFO_BUFFERING_END:");
                    break;
                case 703:
                    Log.d(TWMediaPlayer.this.TAG, "MEDIA_INFO_NETWORK_BANDWIDTH: " + i2);
                    break;
                default:
                    switch (i) {
                        case 800:
                            Log.d(TWMediaPlayer.this.TAG, "MEDIA_INFO_BAD_INTERLEAVING:");
                            break;
                        case 801:
                            Log.d(TWMediaPlayer.this.TAG, "MEDIA_INFO_NOT_SEEKABLE:");
                            break;
                        case 802:
                            Log.d(TWMediaPlayer.this.TAG, "MEDIA_INFO_METADATA_UPDATE:");
                            break;
                    }
                    break;
            }
            return true;
        }
    };
    private IMediaPlayer.OnErrorListener mErrorListener = new IMediaPlayer.OnErrorListener() { // from class: tv.danmaku.ijk.media.player.tw.TWMediaPlayer.4
        @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnErrorListener
        public boolean onError(IMediaPlayer iMediaPlayer, int i, int i2) {
            Log.d(TWMediaPlayer.this.TAG, "Error: " + i + "," + i2);
            TWMediaPlayer.this.mCurrentState = -1;
            TWMediaPlayer.this.mTargetState = -1;
            if (TWMediaPlayer.this.mOnErrorListener == null || TWMediaPlayer.this.mOnErrorListener.onError(TWMediaPlayer.this.mMediaPlayer, i, i2)) {
            }
            return true;
        }
    };
    private IMediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener = new IMediaPlayer.OnBufferingUpdateListener() { // from class: tv.danmaku.ijk.media.player.tw.TWMediaPlayer.5
        @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnBufferingUpdateListener
        public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {
            TWMediaPlayer.this.mCurrentBufferPercentage = i;
        }
    };
    private IMediaPlayer.OnSeekCompleteListener mSeekCompleteListener = new IMediaPlayer.OnSeekCompleteListener() { // from class: tv.danmaku.ijk.media.player.tw.TWMediaPlayer.6
        @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnSeekCompleteListener
        public void onSeekComplete(IMediaPlayer iMediaPlayer) {
            TWMediaPlayer.this.mSeekEndTime = System.currentTimeMillis();
            Log.d(TWMediaPlayer.this.TAG, "SeekCost=" + (TWMediaPlayer.this.mSeekEndTime - TWMediaPlayer.this.mSeekStartTime));
        }
    };
    private int mPlayerType = 0;

    public TWMediaPlayer(Context context) {
        this.mContext = context;
        initMP();
    }

    private IMediaPlayer createPlayer() {
        if (this.mPlayerType == 1) {
            return new AndroidMediaPlayer();
        }
        if (this.mUri == null) {
            return null;
        }
        IjkMediaPlayer ijkMediaPlayer = new IjkMediaPlayer();
        IjkMediaPlayer.native_setLogLevel(8);
        ijkMediaPlayer.setOption(4, "mediacodec", 0L);
        ijkMediaPlayer.setOption(4, "opensles", getTWIjkOpenSLES() == 0 ? 0L : 1L);
        ijkMediaPlayer.setOption(4, "overlay-format", 842225234L);
        ijkMediaPlayer.setOption(4, "framedrop", 1L);
        ijkMediaPlayer.setOption(4, "start-on-prepared", 0L);
        ijkMediaPlayer.setOption(1, "http-detect-range-support", 0L);
        ijkMediaPlayer.setOption(2, "skip_loop_filter", 48L);
        return ijkMediaPlayer;
    }

    private void initMP() {
        this.mCurrentState = 0;
        this.mTargetState = 0;
    }

    public static boolean isAudio(String str) {
        ArrayList<String> arrayList = mAudioList;
        if (arrayList == null) {
            return false;
        }
        Iterator<String> it = arrayList.iterator();
        while (it.hasNext()) {
            if (str.endsWith(it.next())) {
                return true;
            }
        }
        return false;
    }

    private boolean isInPlaybackState() {
        int i;
        return (this.mMediaPlayer == null || (i = this.mCurrentState) == -1 || i == 0 || i == 1) ? false : true;
    }

    private void makePlayerType(int i) {
        if (i != 0) {
            this.mPlayerType = i;
            return;
        }
        int tWIjk = getTWIjk();
        if (tWIjk != 0) {
            if (tWIjk != 3) {
                this.mPlayerType = 2;
                return;
            } else {
                this.mPlayerType = 1;
                return;
            }
        }
        if (isAndroidAudio(this.mUri.toString().toUpperCase(Locale.ENGLISH))) {
            this.mPlayerType = 1;
        } else {
            this.mPlayerType = 2;
        }
    }

    private static ArrayList<String> readMediaList(String str) throws Throwable {
        BufferedReader bufferedReader;
        try {
            try {
                bufferedReader = new BufferedReader(new FileReader(str));
            } catch (Exception unused) {
                return null;
            }
        } catch (Exception unused2) {
            bufferedReader = null;
        } catch (Throwable th) {
            th = th;
            bufferedReader = null;
        }
        try {
            ArrayList<String> arrayList = new ArrayList<>();
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    bufferedReader.close();
                    return arrayList;
                }
                arrayList.add(line);
            }
        } catch (Exception unused3) {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            return null;
        } catch (Throwable th2) {
            th = th2;
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            throw th;
        }
    }

    private static ArrayList<String> readMediaList2(String str) {
        if (new File(SYSTEM_ETC + str).canRead()) {
            return readMediaList(SYSTEM_ETC + str);
        }
        if (!new File(SYSTEM_TW_ETC + str).canRead()) {
            return null;
        }
        return readMediaList(SYSTEM_TW_ETC + str);
    }

    public boolean canPause() {
        return true;
    }

    public boolean canSeekBackward() {
        return true;
    }

    public boolean canSeekForward() {
        return true;
    }

    public int getAudioSessionId() {
        IMediaPlayer iMediaPlayer = this.mMediaPlayer;
        if (iMediaPlayer != null) {
            return iMediaPlayer.getAudioSessionId();
        }
        return 0;
    }

    public int getBufferPercentage() {
        if (this.mMediaPlayer != null) {
            return this.mCurrentBufferPercentage;
        }
        return 0;
    }

    public int getCurrentPosition() {
        if (isInPlaybackState()) {
            return (int) this.mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public int getDuration() {
        if (isInPlaybackState()) {
            return (int) this.mMediaPlayer.getDuration();
        }
        return -1;
    }

    public int getPlayerType() {
        return this.mPlayerType;
    }

    public int getTWIjk() {
        return SystemProperties.getInt("persist.tw.ijk", 0);
    }

    public int getTWIjkNoError() {
        return SystemProperties.getInt("persist.tw.ijk.noerror", 0);
    }

    public int getTWIjkOpenSLES() {
        return SystemProperties.getInt("persist.tw.ijk.opensles", 0);
    }

    public boolean isAndroidAudio(String str) {
        ArrayList<String> arrayList = mAndroidAudioList;
        if (arrayList == null) {
            return false;
        }
        Iterator<String> it = arrayList.iterator();
        while (it.hasNext()) {
            if (str.endsWith(it.next())) {
                return true;
            }
        }
        return false;
    }

    public boolean isExoAudio(String str) {
        ArrayList<String> arrayList = mExoAudioList;
        if (arrayList == null) {
            return false;
        }
        Iterator<String> it = arrayList.iterator();
        while (it.hasNext()) {
            if (str.endsWith(it.next())) {
                return true;
            }
        }
        return false;
    }

    public boolean isPlaying() {
        return isInPlaybackState() && this.mMediaPlayer.isPlaying();
    }

    public boolean noError(IMediaPlayer iMediaPlayer, int i, int i2) {
        if (i == -1004 && i2 == 0) {
            return false;
        }
        int tWIjk = getTWIjk();
        if ((tWIjk != 0 && tWIjk != 1) || getTWIjkNoError() != 0 || getPlayerType() == 2) {
            return false;
        }
        openMP(2);
        return true;
    }

    public void openMP(int i) {
        if (this.mUri == null) {
            return;
        }
        release(false);
        try {
            try {
                makePlayerType(i);
                this.mMediaPlayer = createPlayer();
                this.mMediaPlayer.setOnPreparedListener(this.mPreparedListener);
                this.mMediaPlayer.setOnCompletionListener(this.mCompletionListener);
                this.mMediaPlayer.setOnErrorListener(this.mErrorListener);
                this.mMediaPlayer.setOnInfoListener(this.mInfoListener);
                this.mMediaPlayer.setOnBufferingUpdateListener(this.mBufferingUpdateListener);
                this.mMediaPlayer.setOnSeekCompleteListener(this.mSeekCompleteListener);
                this.mCurrentBufferPercentage = 0;
                this.mMediaPlayer.setDataSource(this.mContext.getApplicationContext(), this.mUri, this.mHeaders);
                this.mMediaPlayer.setAudioStreamType(3);
                this.mPrepareStartTime = System.currentTimeMillis();
                this.mMediaPlayer.prepareAsync();
                this.mCurrentState = 1;
            } catch (IOException e) {
                Log.w(this.TAG, "Unable to open content: " + this.mUri, e);
                this.mCurrentState = -1;
                this.mTargetState = -1;
                this.mErrorListener.onError(this.mMediaPlayer, -1004, 0);
            }
        } catch (IllegalArgumentException e2) {
            Log.w(this.TAG, "Unable to open content: " + this.mUri, e2);
            this.mCurrentState = -1;
            this.mTargetState = -1;
            this.mErrorListener.onError(this.mMediaPlayer, -1004, 0);
        }
    }

    public void pause() {
        if (isInPlaybackState() && this.mMediaPlayer.isPlaying()) {
            this.mMediaPlayer.pause();
            this.mCurrentState = 4;
        }
        this.mTargetState = 4;
    }

    public void release(boolean z) {
        IMediaPlayer iMediaPlayer = this.mMediaPlayer;
        if (iMediaPlayer != null) {
            iMediaPlayer.reset();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
            this.mCurrentState = 0;
            if (z) {
                this.mTargetState = 0;
            }
        }
    }

    public void resume() {
        openMP(this.mPlayerType);
    }

    public void seekTo(int i) {
        if (!isInPlaybackState()) {
            this.mSeekWhenPrepared = i;
            return;
        }
        this.mSeekStartTime = System.currentTimeMillis();
        this.mMediaPlayer.seekTo(i);
        this.mSeekWhenPrepared = 0;
    }

    public void setMPPath(String str) {
        setMPURI(Uri.parse(str));
    }

    public void setMPURI(Uri uri) {
        setMPURI(uri, null);
    }

    public void setOnCompletionListener(IMediaPlayer.OnCompletionListener onCompletionListener) {
        this.mOnCompletionListener = onCompletionListener;
    }

    public void setOnErrorListener(IMediaPlayer.OnErrorListener onErrorListener) {
        this.mOnErrorListener = onErrorListener;
    }

    public void setOnInfoListener(IMediaPlayer.OnInfoListener onInfoListener) {
        this.mOnInfoListener = onInfoListener;
    }

    public void setOnPreparedListener(IMediaPlayer.OnPreparedListener onPreparedListener) {
        this.mOnPreparedListener = onPreparedListener;
    }

    public void setVolume(float f, float f2) {
        if (isInPlaybackState()) {
            this.mMediaPlayer.setVolume(f, f2);
        }
    }

    public void start() {
        if (isInPlaybackState()) {
            this.mMediaPlayer.start();
            this.mCurrentState = 3;
        }
        this.mTargetState = 3;
    }

    public void stopPlayback() {
        IMediaPlayer iMediaPlayer = this.mMediaPlayer;
        if (iMediaPlayer != null) {
            iMediaPlayer.stop();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
            this.mCurrentState = 0;
            this.mTargetState = 0;
        }
    }

    public void suspend() {
        release(false);
    }

    private void setMPURI(Uri uri, Map<String, String> map) {
        this.mUri = uri;
        this.mHeaders = map;
        this.mSeekWhenPrepared = 0;
        openMP(0);
    }
}
