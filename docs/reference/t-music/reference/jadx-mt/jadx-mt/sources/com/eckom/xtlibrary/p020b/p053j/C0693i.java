package com.eckom.xtlibrary.p020b.p053j;

import com.eckom.xtlibrary.p020b.p037f.p039b.C0578e;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0580g;
import com.eckom.xtlibrary.p020b.p037f.p043f.C0643h;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.j.i */
/* JADX INFO: compiled from: MediaScanMedia.java */
/* JADX INFO: loaded from: classes3.dex */
class C0693i implements C0643h.a {

    /* JADX INFO: renamed from: hm */
    final /* synthetic */ C0578e f830hm;

    /* JADX INFO: renamed from: im */
    final /* synthetic */ C0643h.f f831im;
    final /* synthetic */ C0697m this$0;

    C0693i(C0697m c0697m, C0578e c0578e, C0643h.f fVar) {
        this.this$0 = c0697m;
        this.f830hm = c0578e;
        this.f831im = fVar;
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p043f.C0643h.a
    /* JADX INFO: renamed from: S */
    public void mo455S() {
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p043f.C0643h.a
    /* JADX INFO: renamed from: a */
    public void mo456a(C0580g c0580g, String str) {
        if (c0580g.mLength > 0) {
            this.f830hm.f531vj.add(c0580g);
        }
        if (this.f830hm.f531vj.size() > 1) {
            C0578e c0578e = this.f830hm;
            c0578e.f526sj = c0578e.f531vj.get(c0578e.f536yd);
        } else if (this.f830hm.f531vj.size() > 0) {
            C0578e c0578e2 = this.f830hm;
            c0578e2.f526sj = c0578e2.f531vj.get(0);
        }
        this.f831im.mo641ia(str);
    }
}
