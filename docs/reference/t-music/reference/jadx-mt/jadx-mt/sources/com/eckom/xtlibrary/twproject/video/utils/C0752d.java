package com.eckom.xtlibrary.twproject.video.utils;

import android.media.MediaPlayer;

/* JADX INFO: renamed from: com.eckom.xtlibrary.twproject.video.utils.d */
/* JADX INFO: compiled from: MediaView.java */
/* JADX INFO: loaded from: classes3.dex */
class C0752d implements MediaPlayer.OnPreparedListener {
    final /* synthetic */ MediaView this$0;

    C0752d(MediaView mediaView) {
        this.this$0 = mediaView;
    }

    @Override // android.media.MediaPlayer.OnPreparedListener
    public void onPrepared(MediaPlayer mediaPlayer) {
        this.this$0.mCurrentState = 2;
        MediaView mediaView = this.this$0;
        mediaView.f977Wd = true;
        mediaView.f976Vd = true;
        mediaView.f975Ud = true;
        if (this.this$0.mOnPreparedListener != null) {
            this.this$0.mOnPreparedListener.onPrepared(this.this$0.mMediaPlayer);
        }
        this.this$0.mVideoWidth = mediaPlayer.getVideoWidth();
        this.this$0.mVideoHeight = mediaPlayer.getVideoHeight();
        int i = this.this$0.mSeekWhenPrepared;
        if (i != 0) {
            this.this$0.seekTo(i);
        }
        if (this.this$0.mVideoWidth == 0 || this.this$0.mVideoHeight == 0) {
            if (this.this$0.mTargetState == 3) {
                this.this$0.start();
                return;
            }
            return;
        }
        this.this$0.getHolder().setFixedSize(this.this$0.mVideoWidth, this.this$0.mVideoHeight);
        if (this.this$0.mSurfaceWidth == this.this$0.mVideoWidth && this.this$0.mSurfaceHeight == this.this$0.mVideoHeight) {
            if (this.this$0.mTargetState == 3) {
                this.this$0.start();
            } else {
                if (this.this$0.isPlaying() || i != 0) {
                    return;
                }
                this.this$0.getCurrentPosition();
            }
        }
    }
}
