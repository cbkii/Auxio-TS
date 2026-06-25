package com.eckom.xtlibrary.twproject.video.utils;

import android.media.MediaPlayer;
import android.util.Log;

/* compiled from: MediaView.java */
/* renamed from: com.eckom.xtlibrary.twproject.video.utils.g */
/* loaded from: classes3.dex */
class C0755g implements MediaPlayer.OnErrorListener {
    final /* synthetic */ MediaView this$0;

    C0755g(MediaView mediaView) {
        this.this$0 = mediaView;
    }

    @Override // android.media.MediaPlayer.OnErrorListener
    public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
        String str;
        MediaPlayer.OnErrorListener onErrorListener;
        MediaPlayer.OnErrorListener onErrorListener2;
        MediaPlayer mediaPlayer2;
        str = this.this$0.TAG;
        Log.d(str, "Error: " + i + "," + i2);
        this.this$0.mCurrentState = -1;
        this.this$0.mTargetState = -1;
        onErrorListener = this.this$0.mOnErrorListener;
        if (onErrorListener != null) {
            onErrorListener2 = this.this$0.mOnErrorListener;
            mediaPlayer2 = this.this$0.mMediaPlayer;
            if (onErrorListener2.onError(mediaPlayer2, i, i2)) {
            }
        }
        return true;
    }
}
