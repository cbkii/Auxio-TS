package tv.danmaku.ijk.media.player.p069tw;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.SystemProperties;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
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
import tv.danmaku.ijk.media.player.IjkTimedText;
import tv.danmaku.ijk.media.player.p069tw.IRenderView;

/* loaded from: classes4.dex */
public class TWMediaPlayerView extends FrameLayout {
    public static final int PV_PLAYER__AndroidMediaPlayer = 1;
    public static final int PV_PLAYER__Auto = 0;
    public static final int PV_PLAYER__IjkExoMediaPlayer = 3;
    public static final int PV_PLAYER__IjkMediaPlayer = 2;
    public static final int RENDER_NONE = 0;
    public static final int RENDER_SURFACE_VIEW = 1;
    public static final int RENDER_TEXTURE_VIEW = 2;
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
    private String TAG;
    private IMediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener;
    private IMediaPlayer.OnCompletionListener mCompletionListener;
    private int mCurrentBufferPercentage;
    private int mCurrentState;
    private IMediaPlayer.OnErrorListener mErrorListener;
    private Map<String, String> mHeaders;
    private IMediaPlayer.OnInfoListener mInfoListener;
    private IMediaPlayer mMediaPlayer;
    private IMediaPlayer.OnCompletionListener mOnCompletionListener;
    private IMediaPlayer.OnErrorListener mOnErrorListener;
    private IMediaPlayer.OnInfoListener mOnInfoListener;
    private IMediaPlayer.OnPreparedListener mOnPreparedListener;
    private IMediaPlayer.OnTimedTextListener mOnTimedTextListener;
    private int mPlayerType;
    private long mPrepareEndTime;
    private long mPrepareStartTime;
    IMediaPlayer.OnPreparedListener mPreparedListener;
    private IRenderView mRenderView;
    IRenderView.IRenderCallback mSHCallback;
    private IMediaPlayer.OnSeekCompleteListener mSeekCompleteListener;
    private long mSeekEndTime;
    private long mSeekStartTime;
    private int mSeekWhenPrepared;
    IMediaPlayer.OnVideoSizeChangedListener mSizeChangedListener;
    private int mSurfaceHeight;
    private IRenderView.ISurfaceHolder mSurfaceHolder;
    private int mSurfaceWidth;
    private int mTargetState;
    private Uri mUri;
    private int mVideoHeight;
    private int mVideoRotationDegree;
    private int mVideoSarDen;
    private int mVideoSarNum;
    private int mVideoWidth;
    protected TextView subtitleDisplay;
    private static final ArrayList<String> mVideoList = readMediaList2("ijk.video");
    private static final ArrayList<String> mAndroidVideoList = readMediaList2("android.video");
    private static final ArrayList<String> mExoVideoList = readMediaList2("exo.video");

    public TWMediaPlayerView(Context context) {
        super(context);
        this.TAG = "TWMediaPlayerView";
        this.mCurrentState = 0;
        this.mTargetState = 0;
        this.mSurfaceHolder = null;
        this.mMediaPlayer = null;
        this.mRenderView = null;
        this.mPrepareStartTime = 0L;
        this.mPrepareEndTime = 0L;
        this.mSeekStartTime = 0L;
        this.mSeekEndTime = 0L;
        this.subtitleDisplay = null;
        this.mSizeChangedListener = new IMediaPlayer.OnVideoSizeChangedListener() { // from class: tv.danmaku.ijk.media.player.tw.TWMediaPlayerView.1
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnVideoSizeChangedListener
            public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i2, int i3, int i4) {
                TWMediaPlayerView.this.mVideoWidth = iMediaPlayer.getVideoWidth();
                TWMediaPlayerView.this.mVideoHeight = iMediaPlayer.getVideoHeight();
                TWMediaPlayerView.this.mVideoSarNum = iMediaPlayer.getVideoSarNum();
                TWMediaPlayerView.this.mVideoSarDen = iMediaPlayer.getVideoSarDen();
                if (TWMediaPlayerView.this.mVideoWidth == 0 || TWMediaPlayerView.this.mVideoHeight == 0) {
                    return;
                }
                if (TWMediaPlayerView.this.mRenderView != null) {
                    TWMediaPlayerView.this.mRenderView.setVideoSize(TWMediaPlayerView.this.mVideoWidth, TWMediaPlayerView.this.mVideoHeight);
                    TWMediaPlayerView.this.mRenderView.setVideoSampleAspectRatio(TWMediaPlayerView.this.mVideoSarNum, TWMediaPlayerView.this.mVideoSarDen);
                }
                TWMediaPlayerView.this.requestLayout();
            }
        };
        this.mPreparedListener = new IMediaPlayer.OnPreparedListener() { // from class: tv.danmaku.ijk.media.player.tw.TWMediaPlayerView.2
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnPreparedListener
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                TWMediaPlayerView.this.mPrepareEndTime = System.currentTimeMillis();
                Log.d(TWMediaPlayerView.this.TAG, "LoadCost=" + (TWMediaPlayerView.this.mPrepareEndTime - TWMediaPlayerView.this.mPrepareStartTime));
                TWMediaPlayerView.this.mCurrentState = 2;
                if (TWMediaPlayerView.this.mOnPreparedListener != null) {
                    TWMediaPlayerView.this.mOnPreparedListener.onPrepared(TWMediaPlayerView.this.mMediaPlayer);
                }
                TWMediaPlayerView.this.mVideoWidth = iMediaPlayer.getVideoWidth();
                TWMediaPlayerView.this.mVideoHeight = iMediaPlayer.getVideoHeight();
                int i = TWMediaPlayerView.this.mSeekWhenPrepared;
                if (i != 0) {
                    TWMediaPlayerView.this.seekTo(i);
                }
                if (TWMediaPlayerView.this.mVideoWidth == 0 || TWMediaPlayerView.this.mVideoHeight == 0) {
                    if (TWMediaPlayerView.this.mTargetState == 3) {
                        TWMediaPlayerView.this.start();
                        return;
                    }
                    return;
                }
                if (TWMediaPlayerView.this.mRenderView != null) {
                    TWMediaPlayerView.this.mRenderView.setVideoSize(TWMediaPlayerView.this.mVideoWidth, TWMediaPlayerView.this.mVideoHeight);
                    TWMediaPlayerView.this.mRenderView.setVideoSampleAspectRatio(TWMediaPlayerView.this.mVideoSarNum, TWMediaPlayerView.this.mVideoSarDen);
                    if (!TWMediaPlayerView.this.mRenderView.shouldWaitForResize() || (TWMediaPlayerView.this.mSurfaceWidth == TWMediaPlayerView.this.mVideoWidth && TWMediaPlayerView.this.mSurfaceHeight == TWMediaPlayerView.this.mVideoHeight)) {
                        if (TWMediaPlayerView.this.mTargetState == 3) {
                            TWMediaPlayerView.this.start();
                        } else {
                            if (TWMediaPlayerView.this.isPlaying() || i != 0) {
                                return;
                            }
                            TWMediaPlayerView.this.getCurrentPosition();
                        }
                    }
                }
            }
        };
        this.mCompletionListener = new IMediaPlayer.OnCompletionListener() { // from class: tv.danmaku.ijk.media.player.tw.TWMediaPlayerView.3
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnCompletionListener
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                TWMediaPlayerView.this.mCurrentState = 5;
                TWMediaPlayerView.this.mTargetState = 5;
                if (TWMediaPlayerView.this.mOnCompletionListener != null) {
                    TWMediaPlayerView.this.mOnCompletionListener.onCompletion(TWMediaPlayerView.this.mMediaPlayer);
                }
            }
        };
        this.mInfoListener = new IMediaPlayer.OnInfoListener() { // from class: tv.danmaku.ijk.media.player.tw.TWMediaPlayerView.4
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnInfoListener
            public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i2) {
                if (TWMediaPlayerView.this.mOnInfoListener != null) {
                    TWMediaPlayerView.this.mOnInfoListener.onInfo(iMediaPlayer, i, i2);
                }
                if (i == 3) {
                    Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_VIDEO_RENDERING_START:");
                    return true;
                }
                if (i == 901) {
                    Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_UNSUPPORTED_SUBTITLE:");
                    return true;
                }
                if (i == 902) {
                    Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_SUBTITLE_TIMED_OUT:");
                    return true;
                }
                if (i == 10001) {
                    TWMediaPlayerView.this.mVideoRotationDegree = i2;
                    Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_VIDEO_ROTATION_CHANGED: " + i2);
                    if (TWMediaPlayerView.this.mRenderView == null) {
                        return true;
                    }
                    TWMediaPlayerView.this.mRenderView.setVideoRotation(i2);
                    return true;
                }
                if (i == 10002) {
                    Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_AUDIO_RENDERING_START:");
                    return true;
                }
                switch (i) {
                    case 700:
                        Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_VIDEO_TRACK_LAGGING:");
                        break;
                    case 701:
                        Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_BUFFERING_START:");
                        break;
                    case 702:
                        Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_BUFFERING_END:");
                        break;
                    case 703:
                        Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_NETWORK_BANDWIDTH: " + i2);
                        break;
                    default:
                        switch (i) {
                            case 800:
                                Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_BAD_INTERLEAVING:");
                                break;
                            case 801:
                                Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_NOT_SEEKABLE:");
                                break;
                            case 802:
                                Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_METADATA_UPDATE:");
                                break;
                        }
                }
                return true;
            }
        };
        this.mErrorListener = new IMediaPlayer.OnErrorListener() { // from class: tv.danmaku.ijk.media.player.tw.TWMediaPlayerView.5
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnErrorListener
            public boolean onError(IMediaPlayer iMediaPlayer, int i, int i2) {
                Log.d(TWMediaPlayerView.this.TAG, "Error: " + i + "," + i2);
                TWMediaPlayerView.this.mCurrentState = -1;
                TWMediaPlayerView.this.mTargetState = -1;
                if (TWMediaPlayerView.this.mOnErrorListener == null || TWMediaPlayerView.this.mOnErrorListener.onError(TWMediaPlayerView.this.mMediaPlayer, i, i2)) {
                }
                return true;
            }
        };
        this.mBufferingUpdateListener = new IMediaPlayer.OnBufferingUpdateListener() { // from class: tv.danmaku.ijk.media.player.tw.TWMediaPlayerView.6
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnBufferingUpdateListener
            public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {
                TWMediaPlayerView.this.mCurrentBufferPercentage = i;
            }
        };
        this.mSeekCompleteListener = new IMediaPlayer.OnSeekCompleteListener() { // from class: tv.danmaku.ijk.media.player.tw.TWMediaPlayerView.7
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnSeekCompleteListener
            public void onSeekComplete(IMediaPlayer iMediaPlayer) {
                TWMediaPlayerView.this.mSeekEndTime = System.currentTimeMillis();
                Log.d(TWMediaPlayerView.this.TAG, "SeekCost=" + (TWMediaPlayerView.this.mSeekEndTime - TWMediaPlayerView.this.mSeekStartTime));
            }
        };
        this.mOnTimedTextListener = new IMediaPlayer.OnTimedTextListener() { // from class: tv.danmaku.ijk.media.player.tw.TWMediaPlayerView.8
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnTimedTextListener
            public void onTimedText(IMediaPlayer iMediaPlayer, IjkTimedText ijkTimedText) {
                TextView textView;
                if (ijkTimedText == null || (textView = TWMediaPlayerView.this.subtitleDisplay) == null) {
                    return;
                }
                textView.setText(ijkTimedText.getText());
            }
        };
        this.mSHCallback = new IRenderView.IRenderCallback() { // from class: tv.danmaku.ijk.media.player.tw.TWMediaPlayerView.9
            @Override // tv.danmaku.ijk.media.player.tw.IRenderView.IRenderCallback
            public void onSurfaceChanged(@NonNull IRenderView.ISurfaceHolder iSurfaceHolder, int i, int i2, int i3) {
                if (iSurfaceHolder.getRenderView() != TWMediaPlayerView.this.mRenderView) {
                    Log.e(TWMediaPlayerView.this.TAG, "onSurfaceChanged: unmatched render callback\n");
                    return;
                }
                TWMediaPlayerView.this.mSurfaceWidth = i2;
                TWMediaPlayerView.this.mSurfaceHeight = i3;
                boolean z = true;
                boolean z2 = TWMediaPlayerView.this.mTargetState == 3;
                if (TWMediaPlayerView.this.mRenderView.shouldWaitForResize() && (TWMediaPlayerView.this.mVideoWidth != i2 || TWMediaPlayerView.this.mVideoHeight != i3)) {
                    z = false;
                }
                if (TWMediaPlayerView.this.mMediaPlayer != null && z2 && z) {
                    if (TWMediaPlayerView.this.mSeekWhenPrepared != 0) {
                        TWMediaPlayerView tWMediaPlayerView = TWMediaPlayerView.this;
                        tWMediaPlayerView.seekTo(tWMediaPlayerView.mSeekWhenPrepared);
                    }
                    TWMediaPlayerView.this.start();
                }
            }

            @Override // tv.danmaku.ijk.media.player.tw.IRenderView.IRenderCallback
            public void onSurfaceCreated(@NonNull IRenderView.ISurfaceHolder iSurfaceHolder, int i, int i2) {
                if (iSurfaceHolder.getRenderView() != TWMediaPlayerView.this.mRenderView) {
                    Log.e(TWMediaPlayerView.this.TAG, "onSurfaceCreated: unmatched render callback\n");
                    return;
                }
                TWMediaPlayerView.this.mSurfaceHolder = iSurfaceHolder;
                if (TWMediaPlayerView.this.mMediaPlayer != null) {
                    TWMediaPlayerView tWMediaPlayerView = TWMediaPlayerView.this;
                    tWMediaPlayerView.bindSurfaceHolder(tWMediaPlayerView.mMediaPlayer, iSurfaceHolder);
                }
            }

            @Override // tv.danmaku.ijk.media.player.tw.IRenderView.IRenderCallback
            public void onSurfaceDestroyed(@NonNull IRenderView.ISurfaceHolder iSurfaceHolder) {
                if (iSurfaceHolder.getRenderView() != TWMediaPlayerView.this.mRenderView) {
                    Log.e(TWMediaPlayerView.this.TAG, "onSurfaceDestroyed: unmatched render callback\n");
                } else {
                    TWMediaPlayerView.this.mSurfaceHolder = null;
                    TWMediaPlayerView.this.releaseWithoutStop();
                }
            }
        };
        this.mPlayerType = 0;
        initMP();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void bindSurfaceHolder(IMediaPlayer iMediaPlayer, IRenderView.ISurfaceHolder iSurfaceHolder) {
        if (iMediaPlayer == null) {
            return;
        }
        try {
            if (iSurfaceHolder == null) {
                iMediaPlayer.setDisplay(null);
            } else {
                iSurfaceHolder.bindToMediaPlayer(iMediaPlayer);
            }
        } catch (Exception unused) {
        }
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
        this.mVideoWidth = 0;
        this.mVideoHeight = 0;
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
        this.mCurrentState = 0;
        this.mTargetState = 0;
        initSubtitleDisplay();
    }

    private boolean isInPlaybackState() {
        int i;
        return (this.mMediaPlayer == null || (i = this.mCurrentState) == -1 || i == 0 || i == 1) ? false : true;
    }

    public static boolean isVideo(String str) {
        ArrayList<String> arrayList = mVideoList;
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
        if (isAndroidVideo(this.mUri.toString().toUpperCase(Locale.ENGLISH))) {
            this.mPlayerType = 1;
        } else {
            this.mPlayerType = 2;
        }
    }

    private static ArrayList<String> readMediaList(String str) {
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
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    bufferedReader.close();
                    return arrayList;
                }
                arrayList.add(readLine);
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

    public int currentAspectRatio() {
        return 0;
    }

    public int currentRender() {
        return 1;
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

    protected void initSubtitleDisplay() {
        this.subtitleDisplay = new TextView(getContext());
        this.subtitleDisplay.setTextSize(24.0f);
        this.subtitleDisplay.setGravity(17);
        addView(this.subtitleDisplay, new FrameLayout.LayoutParams(-1, -2, 80));
    }

    public boolean isAndroidVideo(String str) {
        ArrayList<String> arrayList = mAndroidVideoList;
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

    public boolean isExoVideo(String str) {
        ArrayList<String> arrayList = mExoVideoList;
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
            setRender(currentRender());
            makePlayerType(i);
            this.mMediaPlayer = createPlayer();
            this.mMediaPlayer.setOnPreparedListener(this.mPreparedListener);
            this.mMediaPlayer.setOnVideoSizeChangedListener(this.mSizeChangedListener);
            this.mMediaPlayer.setOnCompletionListener(this.mCompletionListener);
            this.mMediaPlayer.setOnErrorListener(this.mErrorListener);
            this.mMediaPlayer.setOnInfoListener(this.mInfoListener);
            this.mMediaPlayer.setOnBufferingUpdateListener(this.mBufferingUpdateListener);
            this.mMediaPlayer.setOnSeekCompleteListener(this.mSeekCompleteListener);
            this.mMediaPlayer.setOnTimedTextListener(this.mOnTimedTextListener);
            this.mCurrentBufferPercentage = 0;
            this.mMediaPlayer.setDataSource(getContext().getApplicationContext(), this.mUri, this.mHeaders);
            if (this.mSurfaceHolder != null) {
                bindSurfaceHolder(this.mMediaPlayer, this.mSurfaceHolder);
            }
            this.mMediaPlayer.setAudioStreamType(3);
            this.mPrepareStartTime = System.currentTimeMillis();
            this.mMediaPlayer.prepareAsync();
            this.mCurrentState = 1;
        } catch (IOException e) {
            Log.w(this.TAG, "Unable to open content: " + this.mUri, e);
            this.mCurrentState = -1;
            this.mTargetState = -1;
            this.mErrorListener.onError(this.mMediaPlayer, -1004, 0);
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
        if (this.mMediaPlayer != null) {
            setRenderView(null);
            this.mMediaPlayer.reset();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
            this.mCurrentState = 0;
            if (z) {
                this.mTargetState = 0;
            }
        }
    }

    public void releaseWithoutStop() {
        IMediaPlayer iMediaPlayer = this.mMediaPlayer;
        if (iMediaPlayer != null) {
            iMediaPlayer.setDisplay(null);
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

    public void setRender(int i) {
        if (i == 0) {
            setRenderView(null);
            return;
        }
        if (i == 1) {
            setRenderView(new SurfaceRenderView(getContext()));
            return;
        }
        if (i != 2) {
            Log.e(this.TAG, String.format(Locale.getDefault(), "invalid render %d\n", Integer.valueOf(i)));
            return;
        }
        TextureRenderView textureRenderView = new TextureRenderView(getContext());
        if (this.mMediaPlayer != null) {
            textureRenderView.getSurfaceHolder().bindToMediaPlayer(this.mMediaPlayer);
            textureRenderView.setVideoSize(this.mMediaPlayer.getVideoWidth(), this.mMediaPlayer.getVideoHeight());
            textureRenderView.setVideoSampleAspectRatio(this.mMediaPlayer.getVideoSarNum(), this.mMediaPlayer.getVideoSarDen());
            textureRenderView.setAspectRatio(currentAspectRatio());
        }
        setRenderView(textureRenderView);
    }

    public void setRenderView(IRenderView iRenderView) {
        int i;
        int i2;
        if (this.mRenderView != null) {
            IMediaPlayer iMediaPlayer = this.mMediaPlayer;
            if (iMediaPlayer != null) {
                iMediaPlayer.setDisplay(null);
            }
            View view = this.mRenderView.getView();
            this.mRenderView.removeRenderCallback(this.mSHCallback);
            this.mRenderView = null;
            removeView(view);
        }
        if (iRenderView == null) {
            return;
        }
        this.mRenderView = iRenderView;
        iRenderView.setAspectRatio(currentAspectRatio());
        int i3 = this.mVideoWidth;
        if (i3 > 0 && (i2 = this.mVideoHeight) > 0) {
            iRenderView.setVideoSize(i3, i2);
        }
        int i4 = this.mVideoSarNum;
        if (i4 > 0 && (i = this.mVideoSarDen) > 0) {
            iRenderView.setVideoSampleAspectRatio(i4, i);
        }
        View view2 = this.mRenderView.getView();
        view2.setLayoutParams(new FrameLayout.LayoutParams(-2, -2, 17));
        addView(view2);
        this.mRenderView.addRenderCallback(this.mSHCallback);
        this.mRenderView.setVideoRotation(this.mVideoRotationDegree);
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
        if (this.mMediaPlayer != null) {
            setRenderView(null);
            this.mMediaPlayer.stop();
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
        requestLayout();
        invalidate();
    }

    public TWMediaPlayerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.TAG = "TWMediaPlayerView";
        this.mCurrentState = 0;
        this.mTargetState = 0;
        this.mSurfaceHolder = null;
        this.mMediaPlayer = null;
        this.mRenderView = null;
        this.mPrepareStartTime = 0L;
        this.mPrepareEndTime = 0L;
        this.mSeekStartTime = 0L;
        this.mSeekEndTime = 0L;
        this.subtitleDisplay = null;
        this.mSizeChangedListener = new IMediaPlayer.OnVideoSizeChangedListener() { // from class: tv.danmaku.ijk.media.player.tw.TWMediaPlayerView.1
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnVideoSizeChangedListener
            public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i2, int i3, int i4) {
                TWMediaPlayerView.this.mVideoWidth = iMediaPlayer.getVideoWidth();
                TWMediaPlayerView.this.mVideoHeight = iMediaPlayer.getVideoHeight();
                TWMediaPlayerView.this.mVideoSarNum = iMediaPlayer.getVideoSarNum();
                TWMediaPlayerView.this.mVideoSarDen = iMediaPlayer.getVideoSarDen();
                if (TWMediaPlayerView.this.mVideoWidth == 0 || TWMediaPlayerView.this.mVideoHeight == 0) {
                    return;
                }
                if (TWMediaPlayerView.this.mRenderView != null) {
                    TWMediaPlayerView.this.mRenderView.setVideoSize(TWMediaPlayerView.this.mVideoWidth, TWMediaPlayerView.this.mVideoHeight);
                    TWMediaPlayerView.this.mRenderView.setVideoSampleAspectRatio(TWMediaPlayerView.this.mVideoSarNum, TWMediaPlayerView.this.mVideoSarDen);
                }
                TWMediaPlayerView.this.requestLayout();
            }
        };
        this.mPreparedListener = new IMediaPlayer.OnPreparedListener() { // from class: tv.danmaku.ijk.media.player.tw.TWMediaPlayerView.2
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnPreparedListener
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                TWMediaPlayerView.this.mPrepareEndTime = System.currentTimeMillis();
                Log.d(TWMediaPlayerView.this.TAG, "LoadCost=" + (TWMediaPlayerView.this.mPrepareEndTime - TWMediaPlayerView.this.mPrepareStartTime));
                TWMediaPlayerView.this.mCurrentState = 2;
                if (TWMediaPlayerView.this.mOnPreparedListener != null) {
                    TWMediaPlayerView.this.mOnPreparedListener.onPrepared(TWMediaPlayerView.this.mMediaPlayer);
                }
                TWMediaPlayerView.this.mVideoWidth = iMediaPlayer.getVideoWidth();
                TWMediaPlayerView.this.mVideoHeight = iMediaPlayer.getVideoHeight();
                int i = TWMediaPlayerView.this.mSeekWhenPrepared;
                if (i != 0) {
                    TWMediaPlayerView.this.seekTo(i);
                }
                if (TWMediaPlayerView.this.mVideoWidth == 0 || TWMediaPlayerView.this.mVideoHeight == 0) {
                    if (TWMediaPlayerView.this.mTargetState == 3) {
                        TWMediaPlayerView.this.start();
                        return;
                    }
                    return;
                }
                if (TWMediaPlayerView.this.mRenderView != null) {
                    TWMediaPlayerView.this.mRenderView.setVideoSize(TWMediaPlayerView.this.mVideoWidth, TWMediaPlayerView.this.mVideoHeight);
                    TWMediaPlayerView.this.mRenderView.setVideoSampleAspectRatio(TWMediaPlayerView.this.mVideoSarNum, TWMediaPlayerView.this.mVideoSarDen);
                    if (!TWMediaPlayerView.this.mRenderView.shouldWaitForResize() || (TWMediaPlayerView.this.mSurfaceWidth == TWMediaPlayerView.this.mVideoWidth && TWMediaPlayerView.this.mSurfaceHeight == TWMediaPlayerView.this.mVideoHeight)) {
                        if (TWMediaPlayerView.this.mTargetState == 3) {
                            TWMediaPlayerView.this.start();
                        } else {
                            if (TWMediaPlayerView.this.isPlaying() || i != 0) {
                                return;
                            }
                            TWMediaPlayerView.this.getCurrentPosition();
                        }
                    }
                }
            }
        };
        this.mCompletionListener = new IMediaPlayer.OnCompletionListener() { // from class: tv.danmaku.ijk.media.player.tw.TWMediaPlayerView.3
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnCompletionListener
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                TWMediaPlayerView.this.mCurrentState = 5;
                TWMediaPlayerView.this.mTargetState = 5;
                if (TWMediaPlayerView.this.mOnCompletionListener != null) {
                    TWMediaPlayerView.this.mOnCompletionListener.onCompletion(TWMediaPlayerView.this.mMediaPlayer);
                }
            }
        };
        this.mInfoListener = new IMediaPlayer.OnInfoListener() { // from class: tv.danmaku.ijk.media.player.tw.TWMediaPlayerView.4
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnInfoListener
            public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i2) {
                if (TWMediaPlayerView.this.mOnInfoListener != null) {
                    TWMediaPlayerView.this.mOnInfoListener.onInfo(iMediaPlayer, i, i2);
                }
                if (i == 3) {
                    Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_VIDEO_RENDERING_START:");
                    return true;
                }
                if (i == 901) {
                    Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_UNSUPPORTED_SUBTITLE:");
                    return true;
                }
                if (i == 902) {
                    Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_SUBTITLE_TIMED_OUT:");
                    return true;
                }
                if (i == 10001) {
                    TWMediaPlayerView.this.mVideoRotationDegree = i2;
                    Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_VIDEO_ROTATION_CHANGED: " + i2);
                    if (TWMediaPlayerView.this.mRenderView == null) {
                        return true;
                    }
                    TWMediaPlayerView.this.mRenderView.setVideoRotation(i2);
                    return true;
                }
                if (i == 10002) {
                    Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_AUDIO_RENDERING_START:");
                    return true;
                }
                switch (i) {
                    case 700:
                        Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_VIDEO_TRACK_LAGGING:");
                        break;
                    case 701:
                        Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_BUFFERING_START:");
                        break;
                    case 702:
                        Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_BUFFERING_END:");
                        break;
                    case 703:
                        Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_NETWORK_BANDWIDTH: " + i2);
                        break;
                    default:
                        switch (i) {
                            case 800:
                                Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_BAD_INTERLEAVING:");
                                break;
                            case 801:
                                Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_NOT_SEEKABLE:");
                                break;
                            case 802:
                                Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_METADATA_UPDATE:");
                                break;
                        }
                }
                return true;
            }
        };
        this.mErrorListener = new IMediaPlayer.OnErrorListener() { // from class: tv.danmaku.ijk.media.player.tw.TWMediaPlayerView.5
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnErrorListener
            public boolean onError(IMediaPlayer iMediaPlayer, int i, int i2) {
                Log.d(TWMediaPlayerView.this.TAG, "Error: " + i + "," + i2);
                TWMediaPlayerView.this.mCurrentState = -1;
                TWMediaPlayerView.this.mTargetState = -1;
                if (TWMediaPlayerView.this.mOnErrorListener == null || TWMediaPlayerView.this.mOnErrorListener.onError(TWMediaPlayerView.this.mMediaPlayer, i, i2)) {
                }
                return true;
            }
        };
        this.mBufferingUpdateListener = new IMediaPlayer.OnBufferingUpdateListener() { // from class: tv.danmaku.ijk.media.player.tw.TWMediaPlayerView.6
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnBufferingUpdateListener
            public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {
                TWMediaPlayerView.this.mCurrentBufferPercentage = i;
            }
        };
        this.mSeekCompleteListener = new IMediaPlayer.OnSeekCompleteListener() { // from class: tv.danmaku.ijk.media.player.tw.TWMediaPlayerView.7
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnSeekCompleteListener
            public void onSeekComplete(IMediaPlayer iMediaPlayer) {
                TWMediaPlayerView.this.mSeekEndTime = System.currentTimeMillis();
                Log.d(TWMediaPlayerView.this.TAG, "SeekCost=" + (TWMediaPlayerView.this.mSeekEndTime - TWMediaPlayerView.this.mSeekStartTime));
            }
        };
        this.mOnTimedTextListener = new IMediaPlayer.OnTimedTextListener() { // from class: tv.danmaku.ijk.media.player.tw.TWMediaPlayerView.8
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnTimedTextListener
            public void onTimedText(IMediaPlayer iMediaPlayer, IjkTimedText ijkTimedText) {
                TextView textView;
                if (ijkTimedText == null || (textView = TWMediaPlayerView.this.subtitleDisplay) == null) {
                    return;
                }
                textView.setText(ijkTimedText.getText());
            }
        };
        this.mSHCallback = new IRenderView.IRenderCallback() { // from class: tv.danmaku.ijk.media.player.tw.TWMediaPlayerView.9
            @Override // tv.danmaku.ijk.media.player.tw.IRenderView.IRenderCallback
            public void onSurfaceChanged(@NonNull IRenderView.ISurfaceHolder iSurfaceHolder, int i, int i2, int i3) {
                if (iSurfaceHolder.getRenderView() != TWMediaPlayerView.this.mRenderView) {
                    Log.e(TWMediaPlayerView.this.TAG, "onSurfaceChanged: unmatched render callback\n");
                    return;
                }
                TWMediaPlayerView.this.mSurfaceWidth = i2;
                TWMediaPlayerView.this.mSurfaceHeight = i3;
                boolean z = true;
                boolean z2 = TWMediaPlayerView.this.mTargetState == 3;
                if (TWMediaPlayerView.this.mRenderView.shouldWaitForResize() && (TWMediaPlayerView.this.mVideoWidth != i2 || TWMediaPlayerView.this.mVideoHeight != i3)) {
                    z = false;
                }
                if (TWMediaPlayerView.this.mMediaPlayer != null && z2 && z) {
                    if (TWMediaPlayerView.this.mSeekWhenPrepared != 0) {
                        TWMediaPlayerView tWMediaPlayerView = TWMediaPlayerView.this;
                        tWMediaPlayerView.seekTo(tWMediaPlayerView.mSeekWhenPrepared);
                    }
                    TWMediaPlayerView.this.start();
                }
            }

            @Override // tv.danmaku.ijk.media.player.tw.IRenderView.IRenderCallback
            public void onSurfaceCreated(@NonNull IRenderView.ISurfaceHolder iSurfaceHolder, int i, int i2) {
                if (iSurfaceHolder.getRenderView() != TWMediaPlayerView.this.mRenderView) {
                    Log.e(TWMediaPlayerView.this.TAG, "onSurfaceCreated: unmatched render callback\n");
                    return;
                }
                TWMediaPlayerView.this.mSurfaceHolder = iSurfaceHolder;
                if (TWMediaPlayerView.this.mMediaPlayer != null) {
                    TWMediaPlayerView tWMediaPlayerView = TWMediaPlayerView.this;
                    tWMediaPlayerView.bindSurfaceHolder(tWMediaPlayerView.mMediaPlayer, iSurfaceHolder);
                }
            }

            @Override // tv.danmaku.ijk.media.player.tw.IRenderView.IRenderCallback
            public void onSurfaceDestroyed(@NonNull IRenderView.ISurfaceHolder iSurfaceHolder) {
                if (iSurfaceHolder.getRenderView() != TWMediaPlayerView.this.mRenderView) {
                    Log.e(TWMediaPlayerView.this.TAG, "onSurfaceDestroyed: unmatched render callback\n");
                } else {
                    TWMediaPlayerView.this.mSurfaceHolder = null;
                    TWMediaPlayerView.this.releaseWithoutStop();
                }
            }
        };
        this.mPlayerType = 0;
        initMP();
    }

    public TWMediaPlayerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.TAG = "TWMediaPlayerView";
        this.mCurrentState = 0;
        this.mTargetState = 0;
        this.mSurfaceHolder = null;
        this.mMediaPlayer = null;
        this.mRenderView = null;
        this.mPrepareStartTime = 0L;
        this.mPrepareEndTime = 0L;
        this.mSeekStartTime = 0L;
        this.mSeekEndTime = 0L;
        this.subtitleDisplay = null;
        this.mSizeChangedListener = new IMediaPlayer.OnVideoSizeChangedListener() { // from class: tv.danmaku.ijk.media.player.tw.TWMediaPlayerView.1
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnVideoSizeChangedListener
            public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i2, int i22, int i3, int i4) {
                TWMediaPlayerView.this.mVideoWidth = iMediaPlayer.getVideoWidth();
                TWMediaPlayerView.this.mVideoHeight = iMediaPlayer.getVideoHeight();
                TWMediaPlayerView.this.mVideoSarNum = iMediaPlayer.getVideoSarNum();
                TWMediaPlayerView.this.mVideoSarDen = iMediaPlayer.getVideoSarDen();
                if (TWMediaPlayerView.this.mVideoWidth == 0 || TWMediaPlayerView.this.mVideoHeight == 0) {
                    return;
                }
                if (TWMediaPlayerView.this.mRenderView != null) {
                    TWMediaPlayerView.this.mRenderView.setVideoSize(TWMediaPlayerView.this.mVideoWidth, TWMediaPlayerView.this.mVideoHeight);
                    TWMediaPlayerView.this.mRenderView.setVideoSampleAspectRatio(TWMediaPlayerView.this.mVideoSarNum, TWMediaPlayerView.this.mVideoSarDen);
                }
                TWMediaPlayerView.this.requestLayout();
            }
        };
        this.mPreparedListener = new IMediaPlayer.OnPreparedListener() { // from class: tv.danmaku.ijk.media.player.tw.TWMediaPlayerView.2
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnPreparedListener
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                TWMediaPlayerView.this.mPrepareEndTime = System.currentTimeMillis();
                Log.d(TWMediaPlayerView.this.TAG, "LoadCost=" + (TWMediaPlayerView.this.mPrepareEndTime - TWMediaPlayerView.this.mPrepareStartTime));
                TWMediaPlayerView.this.mCurrentState = 2;
                if (TWMediaPlayerView.this.mOnPreparedListener != null) {
                    TWMediaPlayerView.this.mOnPreparedListener.onPrepared(TWMediaPlayerView.this.mMediaPlayer);
                }
                TWMediaPlayerView.this.mVideoWidth = iMediaPlayer.getVideoWidth();
                TWMediaPlayerView.this.mVideoHeight = iMediaPlayer.getVideoHeight();
                int i2 = TWMediaPlayerView.this.mSeekWhenPrepared;
                if (i2 != 0) {
                    TWMediaPlayerView.this.seekTo(i2);
                }
                if (TWMediaPlayerView.this.mVideoWidth == 0 || TWMediaPlayerView.this.mVideoHeight == 0) {
                    if (TWMediaPlayerView.this.mTargetState == 3) {
                        TWMediaPlayerView.this.start();
                        return;
                    }
                    return;
                }
                if (TWMediaPlayerView.this.mRenderView != null) {
                    TWMediaPlayerView.this.mRenderView.setVideoSize(TWMediaPlayerView.this.mVideoWidth, TWMediaPlayerView.this.mVideoHeight);
                    TWMediaPlayerView.this.mRenderView.setVideoSampleAspectRatio(TWMediaPlayerView.this.mVideoSarNum, TWMediaPlayerView.this.mVideoSarDen);
                    if (!TWMediaPlayerView.this.mRenderView.shouldWaitForResize() || (TWMediaPlayerView.this.mSurfaceWidth == TWMediaPlayerView.this.mVideoWidth && TWMediaPlayerView.this.mSurfaceHeight == TWMediaPlayerView.this.mVideoHeight)) {
                        if (TWMediaPlayerView.this.mTargetState == 3) {
                            TWMediaPlayerView.this.start();
                        } else {
                            if (TWMediaPlayerView.this.isPlaying() || i2 != 0) {
                                return;
                            }
                            TWMediaPlayerView.this.getCurrentPosition();
                        }
                    }
                }
            }
        };
        this.mCompletionListener = new IMediaPlayer.OnCompletionListener() { // from class: tv.danmaku.ijk.media.player.tw.TWMediaPlayerView.3
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnCompletionListener
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                TWMediaPlayerView.this.mCurrentState = 5;
                TWMediaPlayerView.this.mTargetState = 5;
                if (TWMediaPlayerView.this.mOnCompletionListener != null) {
                    TWMediaPlayerView.this.mOnCompletionListener.onCompletion(TWMediaPlayerView.this.mMediaPlayer);
                }
            }
        };
        this.mInfoListener = new IMediaPlayer.OnInfoListener() { // from class: tv.danmaku.ijk.media.player.tw.TWMediaPlayerView.4
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnInfoListener
            public boolean onInfo(IMediaPlayer iMediaPlayer, int i2, int i22) {
                if (TWMediaPlayerView.this.mOnInfoListener != null) {
                    TWMediaPlayerView.this.mOnInfoListener.onInfo(iMediaPlayer, i2, i22);
                }
                if (i2 == 3) {
                    Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_VIDEO_RENDERING_START:");
                    return true;
                }
                if (i2 == 901) {
                    Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_UNSUPPORTED_SUBTITLE:");
                    return true;
                }
                if (i2 == 902) {
                    Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_SUBTITLE_TIMED_OUT:");
                    return true;
                }
                if (i2 == 10001) {
                    TWMediaPlayerView.this.mVideoRotationDegree = i22;
                    Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_VIDEO_ROTATION_CHANGED: " + i22);
                    if (TWMediaPlayerView.this.mRenderView == null) {
                        return true;
                    }
                    TWMediaPlayerView.this.mRenderView.setVideoRotation(i22);
                    return true;
                }
                if (i2 == 10002) {
                    Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_AUDIO_RENDERING_START:");
                    return true;
                }
                switch (i2) {
                    case 700:
                        Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_VIDEO_TRACK_LAGGING:");
                        break;
                    case 701:
                        Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_BUFFERING_START:");
                        break;
                    case 702:
                        Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_BUFFERING_END:");
                        break;
                    case 703:
                        Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_NETWORK_BANDWIDTH: " + i22);
                        break;
                    default:
                        switch (i2) {
                            case 800:
                                Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_BAD_INTERLEAVING:");
                                break;
                            case 801:
                                Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_NOT_SEEKABLE:");
                                break;
                            case 802:
                                Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_METADATA_UPDATE:");
                                break;
                        }
                }
                return true;
            }
        };
        this.mErrorListener = new IMediaPlayer.OnErrorListener() { // from class: tv.danmaku.ijk.media.player.tw.TWMediaPlayerView.5
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnErrorListener
            public boolean onError(IMediaPlayer iMediaPlayer, int i2, int i22) {
                Log.d(TWMediaPlayerView.this.TAG, "Error: " + i2 + "," + i22);
                TWMediaPlayerView.this.mCurrentState = -1;
                TWMediaPlayerView.this.mTargetState = -1;
                if (TWMediaPlayerView.this.mOnErrorListener == null || TWMediaPlayerView.this.mOnErrorListener.onError(TWMediaPlayerView.this.mMediaPlayer, i2, i22)) {
                }
                return true;
            }
        };
        this.mBufferingUpdateListener = new IMediaPlayer.OnBufferingUpdateListener() { // from class: tv.danmaku.ijk.media.player.tw.TWMediaPlayerView.6
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnBufferingUpdateListener
            public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i2) {
                TWMediaPlayerView.this.mCurrentBufferPercentage = i2;
            }
        };
        this.mSeekCompleteListener = new IMediaPlayer.OnSeekCompleteListener() { // from class: tv.danmaku.ijk.media.player.tw.TWMediaPlayerView.7
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnSeekCompleteListener
            public void onSeekComplete(IMediaPlayer iMediaPlayer) {
                TWMediaPlayerView.this.mSeekEndTime = System.currentTimeMillis();
                Log.d(TWMediaPlayerView.this.TAG, "SeekCost=" + (TWMediaPlayerView.this.mSeekEndTime - TWMediaPlayerView.this.mSeekStartTime));
            }
        };
        this.mOnTimedTextListener = new IMediaPlayer.OnTimedTextListener() { // from class: tv.danmaku.ijk.media.player.tw.TWMediaPlayerView.8
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnTimedTextListener
            public void onTimedText(IMediaPlayer iMediaPlayer, IjkTimedText ijkTimedText) {
                TextView textView;
                if (ijkTimedText == null || (textView = TWMediaPlayerView.this.subtitleDisplay) == null) {
                    return;
                }
                textView.setText(ijkTimedText.getText());
            }
        };
        this.mSHCallback = new IRenderView.IRenderCallback() { // from class: tv.danmaku.ijk.media.player.tw.TWMediaPlayerView.9
            @Override // tv.danmaku.ijk.media.player.tw.IRenderView.IRenderCallback
            public void onSurfaceChanged(@NonNull IRenderView.ISurfaceHolder iSurfaceHolder, int i2, int i22, int i3) {
                if (iSurfaceHolder.getRenderView() != TWMediaPlayerView.this.mRenderView) {
                    Log.e(TWMediaPlayerView.this.TAG, "onSurfaceChanged: unmatched render callback\n");
                    return;
                }
                TWMediaPlayerView.this.mSurfaceWidth = i22;
                TWMediaPlayerView.this.mSurfaceHeight = i3;
                boolean z = true;
                boolean z2 = TWMediaPlayerView.this.mTargetState == 3;
                if (TWMediaPlayerView.this.mRenderView.shouldWaitForResize() && (TWMediaPlayerView.this.mVideoWidth != i22 || TWMediaPlayerView.this.mVideoHeight != i3)) {
                    z = false;
                }
                if (TWMediaPlayerView.this.mMediaPlayer != null && z2 && z) {
                    if (TWMediaPlayerView.this.mSeekWhenPrepared != 0) {
                        TWMediaPlayerView tWMediaPlayerView = TWMediaPlayerView.this;
                        tWMediaPlayerView.seekTo(tWMediaPlayerView.mSeekWhenPrepared);
                    }
                    TWMediaPlayerView.this.start();
                }
            }

            @Override // tv.danmaku.ijk.media.player.tw.IRenderView.IRenderCallback
            public void onSurfaceCreated(@NonNull IRenderView.ISurfaceHolder iSurfaceHolder, int i2, int i22) {
                if (iSurfaceHolder.getRenderView() != TWMediaPlayerView.this.mRenderView) {
                    Log.e(TWMediaPlayerView.this.TAG, "onSurfaceCreated: unmatched render callback\n");
                    return;
                }
                TWMediaPlayerView.this.mSurfaceHolder = iSurfaceHolder;
                if (TWMediaPlayerView.this.mMediaPlayer != null) {
                    TWMediaPlayerView tWMediaPlayerView = TWMediaPlayerView.this;
                    tWMediaPlayerView.bindSurfaceHolder(tWMediaPlayerView.mMediaPlayer, iSurfaceHolder);
                }
            }

            @Override // tv.danmaku.ijk.media.player.tw.IRenderView.IRenderCallback
            public void onSurfaceDestroyed(@NonNull IRenderView.ISurfaceHolder iSurfaceHolder) {
                if (iSurfaceHolder.getRenderView() != TWMediaPlayerView.this.mRenderView) {
                    Log.e(TWMediaPlayerView.this.TAG, "onSurfaceDestroyed: unmatched render callback\n");
                } else {
                    TWMediaPlayerView.this.mSurfaceHolder = null;
                    TWMediaPlayerView.this.releaseWithoutStop();
                }
            }
        };
        this.mPlayerType = 0;
        initMP();
    }

    @TargetApi(21)
    public TWMediaPlayerView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.TAG = "TWMediaPlayerView";
        this.mCurrentState = 0;
        this.mTargetState = 0;
        this.mSurfaceHolder = null;
        this.mMediaPlayer = null;
        this.mRenderView = null;
        this.mPrepareStartTime = 0L;
        this.mPrepareEndTime = 0L;
        this.mSeekStartTime = 0L;
        this.mSeekEndTime = 0L;
        this.subtitleDisplay = null;
        this.mSizeChangedListener = new IMediaPlayer.OnVideoSizeChangedListener() { // from class: tv.danmaku.ijk.media.player.tw.TWMediaPlayerView.1
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnVideoSizeChangedListener
            public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i22, int i222, int i3, int i4) {
                TWMediaPlayerView.this.mVideoWidth = iMediaPlayer.getVideoWidth();
                TWMediaPlayerView.this.mVideoHeight = iMediaPlayer.getVideoHeight();
                TWMediaPlayerView.this.mVideoSarNum = iMediaPlayer.getVideoSarNum();
                TWMediaPlayerView.this.mVideoSarDen = iMediaPlayer.getVideoSarDen();
                if (TWMediaPlayerView.this.mVideoWidth == 0 || TWMediaPlayerView.this.mVideoHeight == 0) {
                    return;
                }
                if (TWMediaPlayerView.this.mRenderView != null) {
                    TWMediaPlayerView.this.mRenderView.setVideoSize(TWMediaPlayerView.this.mVideoWidth, TWMediaPlayerView.this.mVideoHeight);
                    TWMediaPlayerView.this.mRenderView.setVideoSampleAspectRatio(TWMediaPlayerView.this.mVideoSarNum, TWMediaPlayerView.this.mVideoSarDen);
                }
                TWMediaPlayerView.this.requestLayout();
            }
        };
        this.mPreparedListener = new IMediaPlayer.OnPreparedListener() { // from class: tv.danmaku.ijk.media.player.tw.TWMediaPlayerView.2
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnPreparedListener
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                TWMediaPlayerView.this.mPrepareEndTime = System.currentTimeMillis();
                Log.d(TWMediaPlayerView.this.TAG, "LoadCost=" + (TWMediaPlayerView.this.mPrepareEndTime - TWMediaPlayerView.this.mPrepareStartTime));
                TWMediaPlayerView.this.mCurrentState = 2;
                if (TWMediaPlayerView.this.mOnPreparedListener != null) {
                    TWMediaPlayerView.this.mOnPreparedListener.onPrepared(TWMediaPlayerView.this.mMediaPlayer);
                }
                TWMediaPlayerView.this.mVideoWidth = iMediaPlayer.getVideoWidth();
                TWMediaPlayerView.this.mVideoHeight = iMediaPlayer.getVideoHeight();
                int i22 = TWMediaPlayerView.this.mSeekWhenPrepared;
                if (i22 != 0) {
                    TWMediaPlayerView.this.seekTo(i22);
                }
                if (TWMediaPlayerView.this.mVideoWidth == 0 || TWMediaPlayerView.this.mVideoHeight == 0) {
                    if (TWMediaPlayerView.this.mTargetState == 3) {
                        TWMediaPlayerView.this.start();
                        return;
                    }
                    return;
                }
                if (TWMediaPlayerView.this.mRenderView != null) {
                    TWMediaPlayerView.this.mRenderView.setVideoSize(TWMediaPlayerView.this.mVideoWidth, TWMediaPlayerView.this.mVideoHeight);
                    TWMediaPlayerView.this.mRenderView.setVideoSampleAspectRatio(TWMediaPlayerView.this.mVideoSarNum, TWMediaPlayerView.this.mVideoSarDen);
                    if (!TWMediaPlayerView.this.mRenderView.shouldWaitForResize() || (TWMediaPlayerView.this.mSurfaceWidth == TWMediaPlayerView.this.mVideoWidth && TWMediaPlayerView.this.mSurfaceHeight == TWMediaPlayerView.this.mVideoHeight)) {
                        if (TWMediaPlayerView.this.mTargetState == 3) {
                            TWMediaPlayerView.this.start();
                        } else {
                            if (TWMediaPlayerView.this.isPlaying() || i22 != 0) {
                                return;
                            }
                            TWMediaPlayerView.this.getCurrentPosition();
                        }
                    }
                }
            }
        };
        this.mCompletionListener = new IMediaPlayer.OnCompletionListener() { // from class: tv.danmaku.ijk.media.player.tw.TWMediaPlayerView.3
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnCompletionListener
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                TWMediaPlayerView.this.mCurrentState = 5;
                TWMediaPlayerView.this.mTargetState = 5;
                if (TWMediaPlayerView.this.mOnCompletionListener != null) {
                    TWMediaPlayerView.this.mOnCompletionListener.onCompletion(TWMediaPlayerView.this.mMediaPlayer);
                }
            }
        };
        this.mInfoListener = new IMediaPlayer.OnInfoListener() { // from class: tv.danmaku.ijk.media.player.tw.TWMediaPlayerView.4
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnInfoListener
            public boolean onInfo(IMediaPlayer iMediaPlayer, int i22, int i222) {
                if (TWMediaPlayerView.this.mOnInfoListener != null) {
                    TWMediaPlayerView.this.mOnInfoListener.onInfo(iMediaPlayer, i22, i222);
                }
                if (i22 == 3) {
                    Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_VIDEO_RENDERING_START:");
                    return true;
                }
                if (i22 == 901) {
                    Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_UNSUPPORTED_SUBTITLE:");
                    return true;
                }
                if (i22 == 902) {
                    Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_SUBTITLE_TIMED_OUT:");
                    return true;
                }
                if (i22 == 10001) {
                    TWMediaPlayerView.this.mVideoRotationDegree = i222;
                    Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_VIDEO_ROTATION_CHANGED: " + i222);
                    if (TWMediaPlayerView.this.mRenderView == null) {
                        return true;
                    }
                    TWMediaPlayerView.this.mRenderView.setVideoRotation(i222);
                    return true;
                }
                if (i22 == 10002) {
                    Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_AUDIO_RENDERING_START:");
                    return true;
                }
                switch (i22) {
                    case 700:
                        Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_VIDEO_TRACK_LAGGING:");
                        break;
                    case 701:
                        Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_BUFFERING_START:");
                        break;
                    case 702:
                        Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_BUFFERING_END:");
                        break;
                    case 703:
                        Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_NETWORK_BANDWIDTH: " + i222);
                        break;
                    default:
                        switch (i22) {
                            case 800:
                                Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_BAD_INTERLEAVING:");
                                break;
                            case 801:
                                Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_NOT_SEEKABLE:");
                                break;
                            case 802:
                                Log.d(TWMediaPlayerView.this.TAG, "MEDIA_INFO_METADATA_UPDATE:");
                                break;
                        }
                }
                return true;
            }
        };
        this.mErrorListener = new IMediaPlayer.OnErrorListener() { // from class: tv.danmaku.ijk.media.player.tw.TWMediaPlayerView.5
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnErrorListener
            public boolean onError(IMediaPlayer iMediaPlayer, int i22, int i222) {
                Log.d(TWMediaPlayerView.this.TAG, "Error: " + i22 + "," + i222);
                TWMediaPlayerView.this.mCurrentState = -1;
                TWMediaPlayerView.this.mTargetState = -1;
                if (TWMediaPlayerView.this.mOnErrorListener == null || TWMediaPlayerView.this.mOnErrorListener.onError(TWMediaPlayerView.this.mMediaPlayer, i22, i222)) {
                }
                return true;
            }
        };
        this.mBufferingUpdateListener = new IMediaPlayer.OnBufferingUpdateListener() { // from class: tv.danmaku.ijk.media.player.tw.TWMediaPlayerView.6
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnBufferingUpdateListener
            public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i22) {
                TWMediaPlayerView.this.mCurrentBufferPercentage = i22;
            }
        };
        this.mSeekCompleteListener = new IMediaPlayer.OnSeekCompleteListener() { // from class: tv.danmaku.ijk.media.player.tw.TWMediaPlayerView.7
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnSeekCompleteListener
            public void onSeekComplete(IMediaPlayer iMediaPlayer) {
                TWMediaPlayerView.this.mSeekEndTime = System.currentTimeMillis();
                Log.d(TWMediaPlayerView.this.TAG, "SeekCost=" + (TWMediaPlayerView.this.mSeekEndTime - TWMediaPlayerView.this.mSeekStartTime));
            }
        };
        this.mOnTimedTextListener = new IMediaPlayer.OnTimedTextListener() { // from class: tv.danmaku.ijk.media.player.tw.TWMediaPlayerView.8
            @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnTimedTextListener
            public void onTimedText(IMediaPlayer iMediaPlayer, IjkTimedText ijkTimedText) {
                TextView textView;
                if (ijkTimedText == null || (textView = TWMediaPlayerView.this.subtitleDisplay) == null) {
                    return;
                }
                textView.setText(ijkTimedText.getText());
            }
        };
        this.mSHCallback = new IRenderView.IRenderCallback() { // from class: tv.danmaku.ijk.media.player.tw.TWMediaPlayerView.9
            @Override // tv.danmaku.ijk.media.player.tw.IRenderView.IRenderCallback
            public void onSurfaceChanged(@NonNull IRenderView.ISurfaceHolder iSurfaceHolder, int i22, int i222, int i3) {
                if (iSurfaceHolder.getRenderView() != TWMediaPlayerView.this.mRenderView) {
                    Log.e(TWMediaPlayerView.this.TAG, "onSurfaceChanged: unmatched render callback\n");
                    return;
                }
                TWMediaPlayerView.this.mSurfaceWidth = i222;
                TWMediaPlayerView.this.mSurfaceHeight = i3;
                boolean z = true;
                boolean z2 = TWMediaPlayerView.this.mTargetState == 3;
                if (TWMediaPlayerView.this.mRenderView.shouldWaitForResize() && (TWMediaPlayerView.this.mVideoWidth != i222 || TWMediaPlayerView.this.mVideoHeight != i3)) {
                    z = false;
                }
                if (TWMediaPlayerView.this.mMediaPlayer != null && z2 && z) {
                    if (TWMediaPlayerView.this.mSeekWhenPrepared != 0) {
                        TWMediaPlayerView tWMediaPlayerView = TWMediaPlayerView.this;
                        tWMediaPlayerView.seekTo(tWMediaPlayerView.mSeekWhenPrepared);
                    }
                    TWMediaPlayerView.this.start();
                }
            }

            @Override // tv.danmaku.ijk.media.player.tw.IRenderView.IRenderCallback
            public void onSurfaceCreated(@NonNull IRenderView.ISurfaceHolder iSurfaceHolder, int i22, int i222) {
                if (iSurfaceHolder.getRenderView() != TWMediaPlayerView.this.mRenderView) {
                    Log.e(TWMediaPlayerView.this.TAG, "onSurfaceCreated: unmatched render callback\n");
                    return;
                }
                TWMediaPlayerView.this.mSurfaceHolder = iSurfaceHolder;
                if (TWMediaPlayerView.this.mMediaPlayer != null) {
                    TWMediaPlayerView tWMediaPlayerView = TWMediaPlayerView.this;
                    tWMediaPlayerView.bindSurfaceHolder(tWMediaPlayerView.mMediaPlayer, iSurfaceHolder);
                }
            }

            @Override // tv.danmaku.ijk.media.player.tw.IRenderView.IRenderCallback
            public void onSurfaceDestroyed(@NonNull IRenderView.ISurfaceHolder iSurfaceHolder) {
                if (iSurfaceHolder.getRenderView() != TWMediaPlayerView.this.mRenderView) {
                    Log.e(TWMediaPlayerView.this.TAG, "onSurfaceDestroyed: unmatched render callback\n");
                } else {
                    TWMediaPlayerView.this.mSurfaceHolder = null;
                    TWMediaPlayerView.this.releaseWithoutStop();
                }
            }
        };
        this.mPlayerType = 0;
        initMP();
    }
}
