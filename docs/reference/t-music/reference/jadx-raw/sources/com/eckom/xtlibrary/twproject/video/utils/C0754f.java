package com.eckom.xtlibrary.twproject.video.utils;

import android.media.MediaPlayer;

/* compiled from: MediaView.java */
/* renamed from: com.eckom.xtlibrary.twproject.video.utils.f */
/* loaded from: classes3.dex */
class C0754f implements MediaPlayer.OnInfoListener {
    final /* synthetic */ MediaView this$0;

    C0754f(MediaView mediaView) {
        this.this$0 = mediaView;
    }

    @Override // android.media.MediaPlayer.OnInfoListener
    public boolean onInfo(MediaPlayer mediaPlayer, int i, int i2) {
        MediaPlayer.OnInfoListener onInfoListener;
        MediaPlayer.OnInfoListener onInfoListener2;
        onInfoListener = this.this$0.mOnInfoListener;
        if (onInfoListener == null) {
            return true;
        }
        onInfoListener2 = this.this$0.mOnInfoListener;
        onInfoListener2.onInfo(mediaPlayer, i, i2);
        return true;
    }
}
