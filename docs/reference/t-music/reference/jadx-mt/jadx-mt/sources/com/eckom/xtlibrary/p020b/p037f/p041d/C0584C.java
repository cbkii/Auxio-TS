package com.eckom.xtlibrary.p020b.p037f.p041d;

import com.eckom.xtlibrary.p020b.p037f.p039b.C0579f;
import com.eckom.xtlibrary.p020b.p037f.p043f.C0636a;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.f.d.C */
/* JADX INFO: compiled from: MusicIjkID3Model.java */
/* JADX INFO: loaded from: classes3.dex */
class C0584C implements IMediaPlayer.OnErrorListener {
    final /* synthetic */ C0593L this$0;

    C0584C(C0593L c0593l) {
        this.this$0 = c0593l;
    }

    @Override // tv.danmaku.ijk.media.player.IMediaPlayer.OnErrorListener
    public boolean onError(IMediaPlayer iMediaPlayer, int i, int i2) {
        if (this.this$0.m518Lb() != null && !C0636a.m743a(null, this.this$0.m518Lb(), this.this$0.f574Yc.f488Ed) && i == -1010) {
            C0593L c0593l = this.this$0;
            c0593l.f574Yc.f488Ed.add(new C0579f(c0593l.getFileName(), this.this$0.m518Lb()));
        }
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
