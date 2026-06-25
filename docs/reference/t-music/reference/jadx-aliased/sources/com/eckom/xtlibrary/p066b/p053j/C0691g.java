package com.eckom.xtlibrary.p066b.p053j;

import com.eckom.xtlibrary.p066b.p069f.p039b.C0578e;
import com.eckom.xtlibrary.p066b.p069f.p039b.C0580g;
import com.eckom.xtlibrary.p066b.p069f.p043f.C0643h;

/* compiled from: MediaScanMedia.java */
/* renamed from: com.eckom.xtlibrary.b.j.g */
/* loaded from: classes3.dex */
class C0691g implements C0643h.a {

    /* renamed from: hm */
    final /* synthetic */ C0578e f826hm;

    /* renamed from: im */
    final /* synthetic */ C0643h.f f827im;
    final /* synthetic */ C0697m this$0;

    C0691g(C0697m c0697m, C0578e c0578e, C0643h.f fVar) {
        this.this$0 = c0697m;
        this.f826hm = c0578e;
        this.f827im = fVar;
    }

    @Override // com.eckom.xtlibrary.p066b.p069f.p043f.C0643h.a
    /* renamed from: S */
    public void mo455S() {
    }

    @Override // com.eckom.xtlibrary.p066b.p069f.p043f.C0643h.a
    /* renamed from: a */
    public void mo456a(C0580g c0580g, String str) {
        if (c0580g.mLength > 0) {
            this.f826hm.f535xj.add(c0580g);
        }
        if (this.f826hm.f535xj.size() > 1) {
            C0578e c0578e = this.f826hm;
            c0578e.f527tj = c0578e.f535xj.get(c0578e.f536yd);
        } else if (this.f826hm.f535xj.size() > 0) {
            C0578e c0578e2 = this.f826hm;
            c0578e2.f527tj = c0578e2.f535xj.get(0);
        }
        this.f827im.mo641ia(str);
    }
}
