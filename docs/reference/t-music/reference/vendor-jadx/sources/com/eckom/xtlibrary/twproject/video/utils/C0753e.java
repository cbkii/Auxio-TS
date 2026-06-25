package com.eckom.xtlibrary.twproject.video.utils;

import android.media.MediaPlayer;

/* compiled from: MediaView.java */
/* renamed from: com.eckom.xtlibrary.twproject.video.utils.e */
/* loaded from: classes3.dex */
class C0753e implements MediaPlayer.OnCompletionListener {
    final /* synthetic */ MediaView this$0;

    C0753e(MediaView mediaView) {
        this.this$0 = mediaView;
    }

    @Override // android.media.MediaPlayer.OnCompletionListener
    public void onCompletion(MediaPlayer mediaPlayer) {
        MediaPlayer.OnCompletionListener onCompletionListener;
        MediaPlayer.OnCompletionListener onCompletionListener2;
        MediaPlayer mediaPlayer2;
        this.this$0.mCurrentState = 5;
        this.this$0.mTargetState = 5;
        onCompletionListener = this.this$0.mOnCompletionListener;
        if (onCompletionListener != null) {
            onCompletionListener2 = this.this$0.mOnCompletionListener;
            mediaPlayer2 = this.this$0.mMediaPlayer;
            onCompletionListener2.onCompletion(mediaPlayer2);
        }
    }
}
