package com.eckom.xtlibrary.p020b.p053j;

import com.eckom.xtlibrary.p020b.p037f.p039b.C0578e;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0580g;
import com.eckom.xtlibrary.p020b.p037f.p043f.C0643h;

/* compiled from: MediaScanMedia.java */
/* renamed from: com.eckom.xtlibrary.b.j.h */
/* loaded from: classes3.dex */
class C0692h implements C0643h.a {

    /* renamed from: hm */
    final /* synthetic */ C0578e f828hm;

    /* renamed from: im */
    final /* synthetic */ C0643h.f f829im;
    final /* synthetic */ C0697m this$0;

    C0692h(C0697m c0697m, C0578e c0578e, C0643h.f fVar) {
        this.this$0 = c0697m;
        this.f828hm = c0578e;
        this.f829im = fVar;
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p043f.C0643h.a
    /* renamed from: S */
    public void mo455S() {
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p043f.C0643h.a
    /* renamed from: a */
    public void mo456a(C0580g c0580g, String str) {
        if (c0580g.mLength > 0) {
            this.f828hm.f533wj.add(c0580g);
        }
        if (this.f828hm.f533wj.size() > 1) {
            C0578e c0578e = this.f828hm;
            c0578e.f529uj = c0578e.f533wj.get(c0578e.f536yd);
        } else if (this.f828hm.f533wj.size() > 0) {
            C0578e c0578e2 = this.f828hm;
            c0578e2.f529uj = c0578e2.f533wj.get(0);
        }
        this.f829im.mo641ia(str);
    }
}
