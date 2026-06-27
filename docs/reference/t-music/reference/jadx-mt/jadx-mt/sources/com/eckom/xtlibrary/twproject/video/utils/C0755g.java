package com.eckom.xtlibrary.twproject.video.utils;

import android.media.MediaPlayer;
import android.util.Log;

/* JADX INFO: renamed from: com.eckom.xtlibrary.twproject.video.utils.g */
/* JADX INFO: compiled from: MediaView.java */
/* JADX INFO: loaded from: classes3.dex */
class C0755g implements MediaPlayer.OnErrorListener {
    final /* synthetic */ MediaView this$0;

    C0755g(MediaView mediaView) {
        this.this$0 = mediaView;
    }

    @Override // android.media.MediaPlayer.OnErrorListener
    public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
        Log.d(this.this$0.TAG, "Error: " + i + "," + i2);
        this.this$0.mCurrentState = -1;
        this.this$0.mTargetState = -1;
        if (this.this$0.mOnErrorListener == null || this.this$0.mOnErrorListener.onError(this.this$0.mMediaPlayer, i, i2)) {
        }
        return true;
    }
}
