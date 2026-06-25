package com.eckom.xtlibrary.p020b.p037f.p041d;

import com.eckom.xtlibrary.p020b.p037f.p039b.C0578e;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0580g;
import com.eckom.xtlibrary.p020b.p037f.p043f.C0643h;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.f.d.d */
/* JADX INFO: compiled from: MusicID3Model.java */
/* JADX INFO: loaded from: classes3.dex */
class C0612d implements C0643h.a {

    /* JADX INFO: renamed from: sk */
    final /* synthetic */ String f634sk;
    final /* synthetic */ C0628t this$0;

    C0612d(C0628t c0628t, String str) {
        this.this$0 = c0628t;
        this.f634sk = str;
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p043f.C0643h.a
    /* JADX INFO: renamed from: S */
    public void mo455S() {
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p043f.C0643h.a
    /* JADX INFO: renamed from: a */
    public void mo456a(C0580g c0580g, String str) {
        this.this$0.f659Yc.f528ud.m451e(c0580g);
        C0578e c0578e = this.this$0.f659Yc;
        c0578e.f528ud = c0580g;
        C0580g c0580g2 = c0578e.f528ud;
        c0580g2.mIndex = 0;
        c0578e.f486Dd = c0580g2;
        String strSubstring = this.f634sk.substring(c0578e.f514_j.lastIndexOf("/") + 1, this.this$0.f659Yc.f514_j.lastIndexOf("."));
        int i = 0;
        int i2 = 0;
        while (true) {
            C0628t c0628t = this.this$0;
            C0578e c0578e2 = c0628t.f659Yc;
            C0580g c0580g3 = c0578e2.f528ud;
            if (i >= c0580g3.f545kk) {
                c0578e2.f482Ad = i2;
                c0628t.m702ea(i2);
                this.this$0.m666c(0, false);
                C0628t c0628t2 = this.this$0;
                c0628t2.m672f(c0628t2.f659Yc.f528ud);
                return;
            }
            if (strSubstring.equals(c0580g3.f544jk[i].mName)) {
                i2 = i;
            }
            i++;
        }
    }
}
