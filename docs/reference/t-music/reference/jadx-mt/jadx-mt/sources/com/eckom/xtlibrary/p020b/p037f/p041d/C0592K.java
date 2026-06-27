package com.eckom.xtlibrary.p020b.p037f.p041d;

import com.eckom.xtlibrary.p020b.p037f.p039b.C0578e;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0580g;
import com.eckom.xtlibrary.p020b.p037f.p043f.C0643h;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.f.d.K */
/* JADX INFO: compiled from: MusicIjkID3Model.java */
/* JADX INFO: loaded from: classes3.dex */
class C0592K implements C0643h.a {

    /* JADX INFO: renamed from: sk */
    final /* synthetic */ String f551sk;
    final /* synthetic */ C0593L this$0;

    C0592K(C0593L c0593l, String str) {
        this.this$0 = c0593l;
        this.f551sk = str;
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p043f.C0643h.a
    /* JADX INFO: renamed from: S */
    public void mo455S() {
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p043f.C0643h.a
    /* JADX INFO: renamed from: a */
    public void mo456a(C0580g c0580g, String str) {
        this.this$0.f574Yc.f528ud.m451e(c0580g);
        C0578e c0578e = this.this$0.f574Yc;
        c0578e.f528ud = c0580g;
        C0580g c0580g2 = c0578e.f528ud;
        c0580g2.mIndex = 0;
        c0578e.f486Dd = c0580g2;
        String strSubstring = this.f551sk.substring(c0578e.f514_j.lastIndexOf("/") + 1, this.this$0.f574Yc.f514_j.lastIndexOf("."));
        int i = 0;
        int i2 = 0;
        while (true) {
            C0593L c0593l = this.this$0;
            C0578e c0578e2 = c0593l.f574Yc;
            C0580g c0580g3 = c0578e2.f528ud;
            if (i >= c0580g3.f545kk) {
                c0578e2.f482Ad = i2;
                c0593l.m535ea(i2);
                this.this$0.m485c(0, false);
                C0593L c0593l2 = this.this$0;
                c0593l2.m491f(c0593l2.f574Yc.f528ud);
                return;
            }
            if (strSubstring.equals(c0580g3.f544jk[i].mName)) {
                i2 = i;
            }
            i++;
        }
    }
}
