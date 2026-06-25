package com.eckom.xtlibrary.p020b.p037f.p041d;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/* compiled from: MusicIjkModel.java */
/* renamed from: com.eckom.xtlibrary.b.f.d.N */
/* loaded from: classes3.dex */
class C0595N implements IMediaPlayer.OnErrorListener {
    final /* synthetic */ C0601U this$0;

    C0595N(C0601U c0601u) {
        this.this$0 = c0601u;
    }

    @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnErrorListener
    public boolean onError(IMediaPlayer iMediaPlayer, int i, int i2) {
        try {
            if (!this.this$0.mMediaPlayer.noError(iMediaPlayer, i, i2)) {
                return true;
            }
            this.this$0.mo529Va();
            return true;
        } catch (Exception unused) {
            return true;
        }
    }
}
