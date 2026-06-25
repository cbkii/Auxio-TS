package com.eckom.xtlibrary.p020b.p037f.p041d;

import com.eckom.xtlibrary.p020b.p037f.p039b.C0578e;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0580g;
import com.eckom.xtlibrary.p020b.p037f.p043f.C0643h;
import com.eckom.xtlibrary.p020b.p037f.p043f.C0655t;
import java.io.File;

/* compiled from: MusicIjkID3Model.java */
/* renamed from: com.eckom.xtlibrary.b.f.d.D */
/* loaded from: classes3.dex */
class C0585D implements C0643h.a {
    final /* synthetic */ C0587F this$1;

    C0585D(C0587F c0587f) {
        this.this$1 = c0587f;
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p043f.C0643h.a
    /* renamed from: S */
    public void mo455S() {
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p043f.C0643h.a
    /* renamed from: a */
    public void mo456a(C0580g c0580g, String str) {
        boolean isPlaying;
        C0655t c0655t;
        boolean isPlaying2;
        C0578e c0578e = this.this$1.this$0.f574Yc;
        c0578e.f491Fd = c0580g;
        c0578e.f486Dd.m450c(c0578e.f491Fd);
        C0593L c0593l = this.this$1.this$0;
        C0578e c0578e2 = c0593l.f574Yc;
        c0578e2.f528ud = c0578e2.f486Dd;
        c0593l.m535ea(c0578e2.f482Ad);
        isPlaying = this.this$1.this$0.isPlaying();
        if (!isPlaying) {
            c0655t = C0593L.f553jd;
            if (c0655t.getService() == 3) {
                String str2 = this.this$1.this$0.f574Yc.f514_j;
                if (str2 != null && new File(str2).canRead()) {
                    isPlaying2 = this.this$1.this$0.isPlaying();
                    if (!isPlaying2) {
                        C0593L c0593l2 = this.this$1.this$0;
                        c0593l2.mMediaPlayer.setMPPath(c0593l2.f574Yc.f514_j);
                        C0593L c0593l3 = this.this$1.this$0;
                        c0593l3.seekTo(c0593l3.f574Yc.f521md);
                        this.this$1.this$0.mo529Va();
                    }
                }
                this.this$1.this$0.m460L(false);
            }
        }
        C0593L c0593l4 = this.this$1.this$0;
        c0593l4.m491f(c0593l4.f574Yc.f528ud);
    }
}
