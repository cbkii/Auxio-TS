package com.eckom.xtlibrary.twproject.video.utils;

import android.view.SurfaceHolder;

/* JADX INFO: renamed from: com.eckom.xtlibrary.twproject.video.utils.i */
/* JADX INFO: compiled from: MediaView.java */
/* JADX INFO: loaded from: classes3.dex */
class SurfaceHolderCallbackC0757i implements SurfaceHolder.Callback {
    final /* synthetic */ MediaView this$0;

    SurfaceHolderCallbackC0757i(MediaView mediaView) {
        this.this$0 = mediaView;
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        this.this$0.mSurfaceWidth = i2;
        this.this$0.mSurfaceHeight = i3;
        boolean z = this.this$0.mTargetState == 3;
        boolean z2 = this.this$0.mVideoWidth == i2 && this.this$0.mVideoHeight == i3;
        if (this.this$0.mMediaPlayer != null && z && z2) {
            if (this.this$0.mSeekWhenPrepared != 0) {
                MediaView mediaView = this.this$0;
                mediaView.seekTo(mediaView.mSeekWhenPrepared);
            }
            this.this$0.start();
        }
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.this$0.mSurfaceHolder = surfaceHolder;
        this.this$0.m1267Be();
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        this.this$0.mSurfaceHolder = null;
        this.this$0.release(true);
    }
}
