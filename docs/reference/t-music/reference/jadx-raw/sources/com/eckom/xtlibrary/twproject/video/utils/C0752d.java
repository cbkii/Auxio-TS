package com.eckom.xtlibrary.twproject.video.utils;

import android.media.MediaPlayer;
import android.view.SurfaceHolder;

/* compiled from: MediaView.java */
/* renamed from: com.eckom.xtlibrary.twproject.video.utils.d */
/* loaded from: classes3.dex */
class C0752d implements MediaPlayer.OnPreparedListener {
    final /* synthetic */ MediaView this$0;

    C0752d(MediaView mediaView) {
        this.this$0 = mediaView;
    }

    @Override // android.media.MediaPlayer.OnPreparedListener
    public void onPrepared(MediaPlayer mediaPlayer) {
        MediaPlayer.OnPreparedListener onPreparedListener;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        MediaPlayer.OnPreparedListener onPreparedListener2;
        MediaPlayer mediaPlayer2;
        this.this$0.mCurrentState = 2;
        MediaView mediaView = this.this$0;
        mediaView.f977Wd = true;
        mediaView.f976Vd = true;
        mediaView.f975Ud = true;
        onPreparedListener = this.this$0.mOnPreparedListener;
        if (onPreparedListener != null) {
            onPreparedListener2 = this.this$0.mOnPreparedListener;
            mediaPlayer2 = this.this$0.mMediaPlayer;
            onPreparedListener2.onPrepared(mediaPlayer2);
        }
        this.this$0.mVideoWidth = mediaPlayer.getVideoWidth();
        this.this$0.mVideoHeight = mediaPlayer.getVideoHeight();
        i = this.this$0.mSeekWhenPrepared;
        if (i != 0) {
            this.this$0.seekTo(i);
        }
        i2 = this.this$0.mVideoWidth;
        if (i2 != 0) {
            i4 = this.this$0.mVideoHeight;
            if (i4 != 0) {
                SurfaceHolder holder = this.this$0.getHolder();
                i5 = this.this$0.mVideoWidth;
                i6 = this.this$0.mVideoHeight;
                holder.setFixedSize(i5, i6);
                i7 = this.this$0.mSurfaceWidth;
                i8 = this.this$0.mVideoWidth;
                if (i7 == i8) {
                    i9 = this.this$0.mSurfaceHeight;
                    i10 = this.this$0.mVideoHeight;
                    if (i9 == i10) {
                        i11 = this.this$0.mTargetState;
                        if (i11 == 3) {
                            this.this$0.start();
                            return;
                        } else {
                            if (this.this$0.isPlaying() || i != 0) {
                                return;
                            }
                            this.this$0.getCurrentPosition();
                            return;
                        }
                    }
                    return;
                }
                return;
            }
        }
        i3 = this.this$0.mTargetState;
        if (i3 == 3) {
            this.this$0.start();
        }
    }
}
