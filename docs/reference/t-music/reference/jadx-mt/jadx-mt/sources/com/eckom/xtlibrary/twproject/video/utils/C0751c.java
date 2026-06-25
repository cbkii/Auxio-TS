package com.eckom.xtlibrary.twproject.video.utils;

import android.media.MediaPlayer;

/* JADX INFO: renamed from: com.eckom.xtlibrary.twproject.video.utils.c */
/* JADX INFO: compiled from: MediaView.java */
/* JADX INFO: loaded from: classes3.dex */
class C0751c implements MediaPlayer.OnVideoSizeChangedListener {
    final /* synthetic */ MediaView this$0;

    C0751c(MediaView mediaView) {
        this.this$0 = mediaView;
    }

    @Override // android.media.MediaPlayer.OnVideoSizeChangedListener
    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i2) {
        this.this$0.mVideoWidth = mediaPlayer.getVideoWidth();
        this.this$0.mVideoHeight = mediaPlayer.getVideoHeight();
        if (this.this$0.mVideoWidth == 0 || this.this$0.mVideoHeight == 0) {
            return;
        }
        this.this$0.getHolder().setFixedSize(this.this$0.mVideoWidth, this.this$0.mVideoHeight);
        this.this$0.requestLayout();
    }
}
