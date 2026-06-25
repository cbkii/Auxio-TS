package com.eckom.xtlibrary.p020b.p053j;

import com.eckom.xtlibrary.p020b.p037f.p039b.C0578e;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0580g;
import com.eckom.xtlibrary.p020b.p037f.p043f.C0643h;

/* compiled from: MediaScanMedia.java */
/* renamed from: com.eckom.xtlibrary.b.j.e */
/* loaded from: classes3.dex */
class C0689e implements C0643h.a {

    /* renamed from: hm */
    final /* synthetic */ C0578e f824hm;
    final /* synthetic */ C0697m this$0;

    C0689e(C0697m c0697m, C0578e c0578e) {
        this.this$0 = c0697m;
        this.f824hm = c0578e;
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p043f.C0643h.a
    /* renamed from: S */
    public void mo455S() {
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p043f.C0643h.a
    /* renamed from: a */
    public void mo456a(C0580g c0580g, String str) {
        if (c0580g.mLength > 0) {
            this.f824hm.f496Jj.add(c0580g);
        }
        if (this.f824hm.f496Jj.size() > 1) {
            C0578e c0578e = this.f824hm;
            c0578e.f493Gj = c0578e.f496Jj.get(c0578e.f534xd);
        } else if (this.f824hm.f496Jj.size() > 0) {
            C0578e c0578e2 = this.f824hm;
            c0578e2.f493Gj = c0578e2.f496Jj.get(0);
        }
    }
}
