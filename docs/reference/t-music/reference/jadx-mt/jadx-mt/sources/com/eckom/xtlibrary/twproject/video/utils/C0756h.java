package com.eckom.xtlibrary.twproject.video.utils;

import android.media.MediaPlayer;

/* JADX INFO: renamed from: com.eckom.xtlibrary.twproject.video.utils.h */
/* JADX INFO: compiled from: MediaView.java */
/* JADX INFO: loaded from: classes3.dex */
class C0756h implements MediaPlayer.OnBufferingUpdateListener {
    final /* synthetic */ MediaView this$0;

    C0756h(MediaView mediaView) {
        this.this$0 = mediaView;
    }

    @Override // android.media.MediaPlayer.OnBufferingUpdateListener
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
        this.this$0.mCurrentBufferPercentage = i;
    }
}
