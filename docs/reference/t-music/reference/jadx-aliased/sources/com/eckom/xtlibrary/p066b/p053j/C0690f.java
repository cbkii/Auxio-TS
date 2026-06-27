package com.eckom.xtlibrary.p066b.p053j;

import com.eckom.xtlibrary.p066b.p069f.p039b.C0578e;
import com.eckom.xtlibrary.p066b.p069f.p039b.C0580g;
import com.eckom.xtlibrary.p066b.p069f.p043f.C0643h;

/* compiled from: MediaScanMedia.java */
/* renamed from: com.eckom.xtlibrary.b.j.f */
/* loaded from: classes3.dex */
class C0690f implements C0643h.a {

    /* renamed from: hm */
    final /* synthetic */ C0578e f825hm;
    final /* synthetic */ C0697m this$0;

    C0690f(C0697m c0697m, C0578e c0578e) {
        this.this$0 = c0697m;
        this.f825hm = c0578e;
    }

    @Override // com.eckom.xtlibrary.p066b.p069f.p043f.C0643h.a
    /* renamed from: S */
    public void mo455S() {
    }

    @Override // com.eckom.xtlibrary.p066b.p069f.p043f.C0643h.a
    /* renamed from: a */
    public void mo456a(C0580g c0580g, String str) {
        if (c0580g.mLength > 0) {
            this.f825hm.f494Hj.add(c0580g);
        }
        if (this.f825hm.f494Hj.size() > 1) {
            C0578e c0578e = this.f825hm;
            c0578e.f489Ej = c0578e.f494Hj.get(c0578e.f534xd);
        } else if (this.f825hm.f494Hj.size() > 0) {
            C0578e c0578e2 = this.f825hm;
            c0578e2.f489Ej = c0578e2.f494Hj.get(0);
        }
    }
}
