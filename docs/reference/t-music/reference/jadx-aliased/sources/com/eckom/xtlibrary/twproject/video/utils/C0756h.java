package com.eckom.xtlibrary.twproject.video.utils;

import android.media.MediaPlayer;

/* compiled from: MediaView.java */
/* renamed from: com.eckom.xtlibrary.twproject.video.utils.h */
/* loaded from: classes3.dex */
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
