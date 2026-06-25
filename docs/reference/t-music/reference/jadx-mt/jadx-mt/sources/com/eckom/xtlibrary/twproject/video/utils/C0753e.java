package com.eckom.xtlibrary.twproject.video.utils;

import android.media.MediaPlayer;

/* JADX INFO: renamed from: com.eckom.xtlibrary.twproject.video.utils.e */
/* JADX INFO: compiled from: MediaView.java */
/* JADX INFO: loaded from: classes3.dex */
class C0753e implements MediaPlayer.OnCompletionListener {
    final /* synthetic */ MediaView this$0;

    C0753e(MediaView mediaView) {
        this.this$0 = mediaView;
    }

    @Override // android.media.MediaPlayer.OnCompletionListener
    public void onCompletion(MediaPlayer mediaPlayer) {
        this.this$0.mCurrentState = 5;
        this.this$0.mTargetState = 5;
        if (this.this$0.mOnCompletionListener != null) {
            this.this$0.mOnCompletionListener.onCompletion(this.this$0.mMediaPlayer);
        }
    }
}
