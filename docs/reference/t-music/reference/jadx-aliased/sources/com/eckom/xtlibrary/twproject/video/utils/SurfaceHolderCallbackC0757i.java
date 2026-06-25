package com.eckom.xtlibrary.twproject.video.utils;

import android.media.MediaPlayer;
import android.view.SurfaceHolder;

/* compiled from: MediaView.java */
/* renamed from: com.eckom.xtlibrary.twproject.video.utils.i */
/* loaded from: classes3.dex */
class SurfaceHolderCallbackC0757i implements SurfaceHolder.Callback {
    final /* synthetic */ MediaView this$0;

    SurfaceHolderCallbackC0757i(MediaView mediaView) {
        this.this$0 = mediaView;
    }

    /* JADX WARN: Code restructure failed: missing block: B:7:0x0026, code lost:
    
        if (r5 == r6) goto L12;
     */
    @Override // android.view.SurfaceHolder.Callback
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        int i4;
        int i5;
        MediaPlayer mediaPlayer;
        int i6;
        int i7;
        int i8;
        this.this$0.mSurfaceWidth = i2;
        this.this$0.mSurfaceHeight = i3;
        i4 = this.this$0.mTargetState;
        boolean z = true;
        boolean z2 = i4 == 3;
        i5 = this.this$0.mVideoWidth;
        if (i5 == i2) {
            i8 = this.this$0.mVideoHeight;
        }
        z = false;
        mediaPlayer = this.this$0.mMediaPlayer;
        if (mediaPlayer != null && z2 && z) {
            i6 = this.this$0.mSeekWhenPrepared;
            if (i6 != 0) {
                MediaView mediaView = this.this$0;
                i7 = mediaView.mSeekWhenPrepared;
                mediaView.seekTo(i7);
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
