package com.eckom.xtlibrary.twproject.video.model;

import android.media.MediaPlayer;
import android.os.SystemClock;

/* JADX INFO: renamed from: com.eckom.xtlibrary.twproject.video.model.t */
/* JADX INFO: compiled from: VideoModel.java */
/* JADX INFO: loaded from: classes3.dex */
class C0742t implements MediaPlayer.OnErrorListener {
    final /* synthetic */ C0748z this$0;

    C0742t(C0748z c0748z) {
        this.this$0 = c0748z;
    }

    @Override // android.media.MediaPlayer.OnErrorListener
    public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
        C0748z.f944dj.mo1054Y();
        C0748z.f944dj.mo1058c(false);
        if (i == 1 || i == 100) {
            this.this$0.mo1156ic();
        } else {
            C0748z.mMediaPlayer.stopPlayback();
        }
        if (this.this$0.mHints[this.this$0.mHints.length - 1] <= 0) {
            this.this$0.mHints[this.this$0.mHints.length - 1] = SystemClock.uptimeMillis();
        } else if (SystemClock.uptimeMillis() - this.this$0.mHints[this.this$0.mHints.length - 1] <= 700) {
            System.arraycopy(this.this$0.mHints, 1, this.this$0.mHints, 0, this.this$0.mHints.length - 1);
            this.this$0.mHints[this.this$0.mHints.length - 1] = SystemClock.uptimeMillis();
            if (SystemClock.uptimeMillis() - this.this$0.mHints[0] <= 5000) {
                C0748z c0748z = this.this$0;
                c0748z.mHints = new long[c0748z.f960Vi];
                this.this$0.f953Qh = true;
                this.this$0.mo1156ic();
            }
        } else {
            C0748z c0748z2 = this.this$0;
            c0748z2.mHints = new long[c0748z2.f960Vi];
        }
        return true;
    }
}
