package com.eckom.xtlibrary.p066b.p069f.p041d;

import com.eckom.xtlibrary.p066b.p069f.p039b.C0578e;
import com.eckom.xtlibrary.p066b.p069f.p039b.C0580g;
import com.eckom.xtlibrary.p066b.p069f.p043f.C0643h;
import com.eckom.xtlibrary.p066b.p069f.p043f.C0655t;
import java.io.File;

/* compiled from: MusicID3Model.java */
/* renamed from: com.eckom.xtlibrary.b.f.d.m */
/* loaded from: classes3.dex */
class C0621m implements C0643h.a {
    final /* synthetic */ C0622n this$1;

    C0621m(C0622n c0622n) {
        this.this$1 = c0622n;
    }

    @Override // com.eckom.xtlibrary.p066b.p069f.p043f.C0643h.a
    /* renamed from: S */
    public void mo455S() {
    }

    @Override // com.eckom.xtlibrary.p066b.p069f.p043f.C0643h.a
    /* renamed from: a */
    public void mo456a(C0580g c0580g, String str) {
        boolean isPlaying;
        C0655t c0655t;
        boolean isPlaying2;
        int m686zb;
        boolean isPlaying3;
        C0628t c0628t = this.this$1.this$0;
        C0578e c0578e = c0628t.f659Yc;
        c0578e.f486Dd = c0580g;
        c0578e.f528ud = c0578e.f486Dd;
        c0628t.m702ea(c0578e.f482Ad);
        isPlaying = this.this$1.this$0.isPlaying();
        if (isPlaying) {
            return;
        }
        c0655t = C0628t.f638jd;
        if (c0655t.getService() == 3) {
            String str2 = this.this$1.this$0.f659Yc.f514_j;
            if (str2 != null && new File(str2).canRead()) {
                isPlaying2 = this.this$1.this$0.isPlaying();
                if (!isPlaying2) {
                    C0628t c0628t2 = this.this$1.this$0;
                    m686zb = c0628t2.m686zb(c0628t2.f659Yc.f514_j);
                    if (m686zb == 0) {
                        isPlaying3 = this.this$1.this$0.isPlaying();
                        if (!isPlaying3) {
                            C0628t c0628t3 = this.this$1.this$0;
                            c0628t3.seekTo(c0628t3.f659Yc.f521md);
                            this.this$1.this$0.mo529Va();
                        }
                    }
                }
            }
            this.this$1.this$0.m644L(false);
        }
    }
}
