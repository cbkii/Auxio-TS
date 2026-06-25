package com.eckom.xtlibrary.p020b.p037f.p041d;

import com.eckom.xtlibrary.p020b.p037f.p039b.C0578e;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0580g;
import com.eckom.xtlibrary.p020b.p037f.p043f.C0643h;
import java.io.File;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.f.d.D */
/* JADX INFO: compiled from: MusicIjkID3Model.java */
/* JADX INFO: loaded from: classes3.dex */
class C0585D implements C0643h.a {
    final /* synthetic */ C0587F this$1;

    C0585D(C0587F c0587f) {
        this.this$1 = c0587f;
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p043f.C0643h.a
    /* JADX INFO: renamed from: S */
    public void mo455S() {
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p043f.C0643h.a
    /* JADX INFO: renamed from: a */
    public void mo456a(C0580g c0580g, String str) {
        C0578e c0578e = this.this$1.this$0.f574Yc;
        c0578e.f491Fd = c0580g;
        c0578e.f486Dd.m450c(c0578e.f491Fd);
        C0593L c0593l = this.this$1.this$0;
        C0578e c0578e2 = c0593l.f574Yc;
        c0578e2.f528ud = c0578e2.f486Dd;
        c0593l.m535ea(c0578e2.f482Ad);
        if (!this.this$1.this$0.isPlaying() && C0593L.f553jd.getService() == 3) {
            String str2 = this.this$1.this$0.f574Yc.f514_j;
            if (str2 != null && new File(str2).canRead() && !this.this$1.this$0.isPlaying()) {
                C0593L c0593l2 = this.this$1.this$0;
                c0593l2.mMediaPlayer.setMPPath(c0593l2.f574Yc.f514_j);
                C0593L c0593l3 = this.this$1.this$0;
                c0593l3.seekTo(c0593l3.f574Yc.f521md);
                this.this$1.this$0.mo529Va();
            }
            this.this$1.this$0.m460L(false);
        }
        C0593L c0593l4 = this.this$1.this$0;
        c0593l4.m491f(c0593l4.f574Yc.f528ud);
    }
}
