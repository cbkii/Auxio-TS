package com.eckom.xtlibrary.twproject.video.model;

import android.os.SystemClock;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/* JADX INFO: renamed from: com.eckom.xtlibrary.twproject.video.model.h */
/* JADX INFO: compiled from: VideoIjkModel.java */
/* JADX INFO: loaded from: classes3.dex */
class C0730h implements IMediaPlayer.OnErrorListener {
    final /* synthetic */ C0735m this$0;

    C0730h(C0735m c0735m) {
        this.this$0 = c0735m;
    }

    @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnErrorListener
    public boolean onError(IMediaPlayer iMediaPlayer, int i, int i2) {
        C0735m.f912dj.mo1054Y();
        C0735m.f912dj.mo1058c(false);
        if (i == 1) {
            this.this$0.mo1156ic();
        } else {
            this.this$0.mMediaPlayer.stopPlayback();
        }
        if (this.this$0.mHints[this.this$0.mHints.length - 1] <= 0) {
            this.this$0.mHints[this.this$0.mHints.length - 1] = SystemClock.uptimeMillis();
        } else if (SystemClock.uptimeMillis() - this.this$0.mHints[this.this$0.mHints.length - 1] <= 700) {
            System.arraycopy(this.this$0.mHints, 1, this.this$0.mHints, 0, this.this$0.mHints.length - 1);
            this.this$0.mHints[this.this$0.mHints.length - 1] = SystemClock.uptimeMillis();
            if (SystemClock.uptimeMillis() - this.this$0.mHints[0] <= 5000) {
                C0735m c0735m = this.this$0;
                c0735m.mHints = new long[c0735m.f928Vi];
                this.this$0.f921Qh = true;
                this.this$0.mo1156ic();
            }
        } else {
            C0735m c0735m2 = this.this$0;
            c0735m2.mHints = new long[c0735m2.f928Vi];
        }
        return true;
    }
}
