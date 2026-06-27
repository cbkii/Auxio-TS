package com.eckom.xtlibrary.twproject.video.utils;

import android.media.MediaPlayer;

/* JADX INFO: renamed from: com.eckom.xtlibrary.twproject.video.utils.f */
/* JADX INFO: compiled from: MediaView.java */
/* JADX INFO: loaded from: classes3.dex */
class C0754f implements MediaPlayer.OnInfoListener {
    final /* synthetic */ MediaView this$0;

    C0754f(MediaView mediaView) {
        this.this$0 = mediaView;
    }

    @Override // android.media.MediaPlayer.OnInfoListener
    public boolean onInfo(MediaPlayer mediaPlayer, int i, int i2) {
        if (this.this$0.mOnInfoListener == null) {
            return true;
        }
        this.this$0.mOnInfoListener.onInfo(mediaPlayer, i, i2);
        return true;
    }
}
