package com.eckom.xtlibrary.p020b.p053j;

import com.eckom.xtlibrary.p020b.p037f.p039b.C0578e;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0580g;
import com.eckom.xtlibrary.p020b.p037f.p043f.C0643h;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.j.d */
/* JADX INFO: compiled from: MediaScanMedia.java */
/* JADX INFO: loaded from: classes3.dex */
class C0688d implements C0643h.a {

    /* JADX INFO: renamed from: hm */
    final /* synthetic */ C0578e f823hm;
    final /* synthetic */ C0697m this$0;

    C0688d(C0697m c0697m, C0578e c0578e) {
        this.this$0 = c0697m;
        this.f823hm = c0578e;
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p043f.C0643h.a
    /* JADX INFO: renamed from: S */
    public void mo455S() {
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p043f.C0643h.a
    /* JADX INFO: renamed from: a */
    public void mo456a(C0580g c0580g, String str) {
        if (c0580g.mLength > 0) {
            this.f823hm.f495Ij.add(c0580g);
        }
        if (this.f823hm.f495Ij.size() > 1) {
            C0578e c0578e = this.f823hm;
            c0578e.f492Fj = c0578e.f495Ij.get(c0578e.f534xd);
        } else if (this.f823hm.f495Ij.size() > 0) {
            C0578e c0578e2 = this.f823hm;
            c0578e2.f492Fj = c0578e2.f495Ij.get(0);
        }
    }
}
