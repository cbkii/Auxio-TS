package com.eckom.xtlibrary.twproject.video.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.io.File;
import java.io.IOException;

/* JADX INFO: loaded from: classes3.dex */
public class MediaView extends SurfaceView {
    private String TAG;

    /* JADX INFO: renamed from: Td */
    private int f974Td;

    /* JADX INFO: renamed from: Ud */
    private boolean f975Ud;

    /* JADX INFO: renamed from: Vd */
    private boolean f976Vd;

    /* JADX INFO: renamed from: Wd */
    private boolean f977Wd;
    private MediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener;
    private MediaPlayer.OnCompletionListener mCompletionListener;
    private int mCurrentBufferPercentage;
    private int mCurrentState;
    private MediaPlayer.OnErrorListener mErrorListener;
    private MediaPlayer.OnInfoListener mInfoListener;
    private MediaPlayer mMediaPlayer;
    private MediaPlayer.OnCompletionListener mOnCompletionListener;
    private MediaPlayer.OnErrorListener mOnErrorListener;
    private MediaPlayer.OnInfoListener mOnInfoListener;
    private MediaPlayer.OnPreparedListener mOnPreparedListener;
    private String mPath;
    MediaPlayer.OnPreparedListener mPreparedListener;
    SurfaceHolder.Callback mSHCallback;
    private int mSeekWhenPrepared;
    MediaPlayer.OnVideoSizeChangedListener mSizeChangedListener;
    private int mSurfaceHeight;
    private SurfaceHolder mSurfaceHolder;
    private int mSurfaceWidth;
    private int mTargetState;
    private int mVideoHeight;
    private int mVideoWidth;

    public MediaView(Context context) {
        super(context);
        this.TAG = "MediaView";
        this.mCurrentState = 0;
        this.mTargetState = 0;
        this.mSurfaceHolder = null;
        this.mMediaPlayer = null;
        this.mSizeChangedListener = new C0751c(this);
        this.mPreparedListener = new C0752d(this);
        this.mCompletionListener = new C0753e(this);
        this.mInfoListener = new C0754f(this);
        this.mErrorListener = new C0755g(this);
        this.mBufferingUpdateListener = new C0756h(this);
        this.mSHCallback = new SurfaceHolderCallbackC0757i(this);
        m1266Ae();
    }

    /* JADX INFO: renamed from: Ae */
    private void m1266Ae() {
        this.mVideoWidth = 0;
        this.mVideoHeight = 0;
        getHolder().addCallback(this.mSHCallback);
        getHolder().setType(3);
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
        this.mCurrentState = 0;
        this.mTargetState = 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: Be */
    public void m1267Be() {
        String str = this.mPath;
        if (str == null || this.mSurfaceHolder == null || !new File(str).exists()) {
            return;
        }
        release(false);
        try {
            this.mMediaPlayer = new MediaPlayer();
            if (this.f974Td != 0) {
                this.mMediaPlayer.setAudioSessionId(this.f974Td);
            } else {
                this.f974Td = this.mMediaPlayer.getAudioSessionId();
            }
            this.mMediaPlayer.setOnPreparedListener(this.mPreparedListener);
            this.mMediaPlayer.setOnVideoSizeChangedListener(this.mSizeChangedListener);
            this.mMediaPlayer.setOnCompletionListener(this.mCompletionListener);
            this.mMediaPlayer.setOnErrorListener(this.mErrorListener);
            this.mMediaPlayer.setOnInfoListener(this.mInfoListener);
            this.mMediaPlayer.setOnBufferingUpdateListener(this.mBufferingUpdateListener);
            this.mCurrentBufferPercentage = 0;
            this.mMediaPlayer.setDataSource(this.mPath);
            this.mMediaPlayer.setDisplay(this.mSurfaceHolder);
            this.mMediaPlayer.setAudioStreamType(3);
            this.mMediaPlayer.setScreenOnWhilePlaying(true);
            this.mMediaPlayer.prepareAsync();
            this.mCurrentState = 1;
        } catch (IOException e) {
            Log.w(this.TAG, "Unable to open content: " + this.mPath, e);
            this.mCurrentState = -1;
            this.mTargetState = -1;
            this.mErrorListener.onError(this.mMediaPlayer, 1, 0);
        } catch (IllegalArgumentException e2) {
            Log.w(this.TAG, "Unable to open content: " + this.mPath, e2);
            this.mCurrentState = -1;
            this.mTargetState = -1;
            this.mErrorListener.onError(this.mMediaPlayer, 1, 0);
        } catch (IllegalStateException e3) {
            Log.w(this.TAG, "Unable to open content: " + this.mPath, e3);
            this.mCurrentState = -1;
            this.mTargetState = -1;
            this.mErrorListener.onError(this.mMediaPlayer, 1, 0);
        }
    }

    private boolean isInPlaybackState() {
        int i;
        return (this.mMediaPlayer == null || (i = this.mCurrentState) == -1 || i == 0 || i == 1) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void release(boolean z) {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
            this.mCurrentState = 0;
            if (z) {
                this.mTargetState = 0;
            }
        }
    }

    public int getAudioSessionId() {
        if (this.f974Td == 0) {
            MediaPlayer mediaPlayer = new MediaPlayer();
            this.f974Td = mediaPlayer.getAudioSessionId();
            mediaPlayer.release();
        }
        return this.f974Td;
    }

    public int getBufferPercentage() {
        if (this.mMediaPlayer != null) {
            return this.mCurrentBufferPercentage;
        }
        return 0;
    }

    public int getCurrentPosition() {
        return isInPlaybackState() ? this.mMediaPlayer.getCurrentPosition() : this.mSeekWhenPrepared;
    }

    public int getDuration() {
        if (isInPlaybackState()) {
            return this.mMediaPlayer.getDuration();
        }
        return -1;
    }

    public boolean isPlaying() {
        return isInPlaybackState() && this.mMediaPlayer.isPlaying();
    }

    public void pause() {
        if (isInPlaybackState() && this.mMediaPlayer.isPlaying()) {
            this.mMediaPlayer.pause();
            this.mCurrentState = 4;
        }
        this.mTargetState = 4;
    }

    public void seekTo(int i) {
        if (!isInPlaybackState()) {
            this.mSeekWhenPrepared = i;
        } else {
            this.mMediaPlayer.seekTo(i);
            this.mSeekWhenPrepared = 0;
        }
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener onCompletionListener) {
        this.mOnCompletionListener = onCompletionListener;
    }

    public void setOnErrorListener(MediaPlayer.OnErrorListener onErrorListener) {
        this.mOnErrorListener = onErrorListener;
    }

    public void setOnInfoListener(MediaPlayer.OnInfoListener onInfoListener) {
        this.mOnInfoListener = onInfoListener;
    }

    public void setOnPreparedListener(MediaPlayer.OnPreparedListener onPreparedListener) {
        this.mOnPreparedListener = onPreparedListener;
    }

    public void setVideoPath(String str) {
        this.mPath = str;
        this.mSeekWhenPrepared = 0;
        m1267Be();
        requestLayout();
        invalidate();
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
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
            this.mCurrentState = 0;
            this.mTargetState = 0;
        }
    }

    public MediaView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.TAG = "MediaView";
        this.mCurrentState = 0;
        this.mTargetState = 0;
        this.mSurfaceHolder = null;
        this.mMediaPlayer = null;
        this.mSizeChangedListener = new C0751c(this);
        this.mPreparedListener = new C0752d(this);
        this.mCompletionListener = new C0753e(this);
        this.mInfoListener = new C0754f(this);
        this.mErrorListener = new C0755g(this);
        this.mBufferingUpdateListener = new C0756h(this);
        this.mSHCallback = new SurfaceHolderCallbackC0757i(this);
        m1266Ae();
    }

    public MediaView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.TAG = "MediaView";
        this.mCurrentState = 0;
        this.mTargetState = 0;
        this.mSurfaceHolder = null;
        this.mMediaPlayer = null;
        this.mSizeChangedListener = new C0751c(this);
        this.mPreparedListener = new C0752d(this);
        this.mCompletionListener = new C0753e(this);
        this.mInfoListener = new C0754f(this);
        this.mErrorListener = new C0755g(this);
        this.mBufferingUpdateListener = new C0756h(this);
        this.mSHCallback = new SurfaceHolderCallbackC0757i(this);
        m1266Ae();
    }
}
