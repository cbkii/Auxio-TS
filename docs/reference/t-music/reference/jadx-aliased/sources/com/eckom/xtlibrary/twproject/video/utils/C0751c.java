package com.eckom.xtlibrary.twproject.video.utils;

import android.media.MediaPlayer;
import android.view.SurfaceHolder;

/* compiled from: MediaView.java */
/* renamed from: com.eckom.xtlibrary.twproject.video.utils.c */
/* loaded from: classes3.dex */
class C0751c implements MediaPlayer.OnVideoSizeChangedListener {
    final /* synthetic */ MediaView this$0;

    C0751c(MediaView mediaView) {
        this.this$0 = mediaView;
    }

    @Override // android.media.MediaPlayer.OnVideoSizeChangedListener
    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i2) {
        int i3;
        int i4;
        int i5;
        int i6;
        this.this$0.mVideoWidth = mediaPlayer.getVideoWidth();
        this.this$0.mVideoHeight = mediaPlayer.getVideoHeight();
        i3 = this.this$0.mVideoWidth;
        if (i3 != 0) {
            i4 = this.this$0.mVideoHeight;
            if (i4 != 0) {
                SurfaceHolder holder = this.this$0.getHolder();
                i5 = this.this$0.mVideoWidth;
                i6 = this.this$0.mVideoHeight;
                holder.setFixedSize(i5, i6);
                this.this$0.requestLayout();
            }
        }
    }
}
