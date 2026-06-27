package com.eckom.xtlibrary.twproject.video.model;

import android.os.SystemClock;
import com.eckom.xtlibrary.p066b.p071k.p057c.InterfaceC0708b;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/* compiled from: VideoIjkModel.java */
/* renamed from: com.eckom.xtlibrary.twproject.video.model.h */
/* loaded from: classes3.dex */
class C0730h implements IMediaPlayer.OnErrorListener {
    final /* synthetic */ C0735m this$0;

    C0730h(C0735m c0735m) {
        this.this$0 = c0735m;
    }

    @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnErrorListener
    public boolean onError(IMediaPlayer iMediaPlayer, int i, int i2) {
        InterfaceC0708b interfaceC0708b;
        InterfaceC0708b interfaceC0708b2;
        long[] jArr;
        long[] jArr2;
        long[] jArr3;
        long[] jArr4;
        long[] jArr5;
        long[] jArr6;
        int i3;
        long[] jArr7;
        long[] jArr8;
        long[] jArr9;
        long[] jArr10;
        long[] jArr11;
        long[] jArr12;
        int i4;
        interfaceC0708b = C0735m.f912dj;
        interfaceC0708b.mo1054Y();
        interfaceC0708b2 = C0735m.f912dj;
        interfaceC0708b2.mo1058c(false);
        if (i == 1) {
            this.this$0.mo1156ic();
        } else {
            this.this$0.mMediaPlayer.stopPlayback();
        }
        jArr = this.this$0.mHints;
        jArr2 = this.this$0.mHints;
        if (jArr[jArr2.length - 1] > 0) {
            long uptimeMillis = SystemClock.uptimeMillis();
            jArr5 = this.this$0.mHints;
            jArr6 = this.this$0.mHints;
            if (uptimeMillis - jArr5[jArr6.length - 1] <= 700) {
                jArr7 = this.this$0.mHints;
                jArr8 = this.this$0.mHints;
                jArr9 = this.this$0.mHints;
                System.arraycopy(jArr7, 1, jArr8, 0, jArr9.length - 1);
                jArr10 = this.this$0.mHints;
                jArr11 = this.this$0.mHints;
                jArr10[jArr11.length - 1] = SystemClock.uptimeMillis();
                long uptimeMillis2 = SystemClock.uptimeMillis();
                jArr12 = this.this$0.mHints;
                if (uptimeMillis2 - jArr12[0] <= 5000) {
                    C0735m c0735m = this.this$0;
                    i4 = c0735m.f928Vi;
                    c0735m.mHints = new long[i4];
                    this.this$0.f921Qh = true;
                    this.this$0.mo1156ic();
                }
            } else {
                C0735m c0735m2 = this.this$0;
                i3 = c0735m2.f928Vi;
                c0735m2.mHints = new long[i3];
            }
        } else {
            jArr3 = this.this$0.mHints;
            jArr4 = this.this$0.mHints;
            jArr3[jArr4.length - 1] = SystemClock.uptimeMillis();
        }
        return true;
    }
}
