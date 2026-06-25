package com.eckom.xtlibrary.p020b.p037f.p041d;

import com.eckom.xtlibrary.p020b.p037f.p039b.C0578e;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0580g;
import com.eckom.xtlibrary.p020b.p037f.p043f.C0643h;
import java.io.File;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.f.d.l */
/* JADX INFO: compiled from: MusicID3Model.java */
/* JADX INFO: loaded from: classes3.dex */
class C0620l implements C0643h.a {
    final /* synthetic */ C0622n this$1;

    C0620l(C0622n c0622n) {
        this.this$1 = c0622n;
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p043f.C0643h.a
    /* JADX INFO: renamed from: S */
    public void mo455S() {
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p043f.C0643h.a
    /* JADX INFO: renamed from: a */
    public void mo456a(C0580g c0580g, String str) {
        C0628t c0628t = this.this$1.this$0;
        C0578e c0578e = c0628t.f659Yc;
        c0578e.f486Dd = c0580g;
        c0578e.f528ud = c0578e.f486Dd;
        c0628t.m702ea(c0578e.f482Ad);
        if (this.this$1.this$0.isPlaying() || C0628t.f638jd.getService() != 3) {
            return;
        }
        String str2 = this.this$1.this$0.f659Yc.f514_j;
        if (str2 != null && new File(str2).canRead() && !this.this$1.this$0.isPlaying()) {
            C0628t c0628t2 = this.this$1.this$0;
            if (c0628t2.m686zb(c0628t2.f659Yc.f514_j) == 0 && !this.this$1.this$0.isPlaying()) {
                C0628t c0628t3 = this.this$1.this$0;
                c0628t3.seekTo(c0628t3.f659Yc.f521md);
                this.this$1.this$0.mo529Va();
            }
        }
        this.this$1.this$0.m644L(false);
    }
}
