package com.eckom.xtlibrary.p020b.p037f.p041d;

import com.eckom.xtlibrary.p020b.p037f.p039b.C0578e;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0579f;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0580g;
import com.eckom.xtlibrary.p020b.p037f.p043f.C0643h;
import java.util.ArrayList;

/* compiled from: MusicIjkID3Model.java */
/* renamed from: com.eckom.xtlibrary.b.f.d.w */
/* loaded from: classes3.dex */
class C0631w implements C0643h.a {
    final /* synthetic */ C0593L this$0;

    C0631w(C0593L c0593l) {
        this.this$0 = c0593l;
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p043f.C0643h.a
    /* renamed from: S */
    public void mo455S() {
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p043f.C0643h.a
    /* renamed from: a */
    public void mo456a(C0580g c0580g, String str) {
        if (c0580g.mLength > 0) {
            this.this$0.f574Yc.f506Tc = new ArrayList<>();
            for (C0579f c0579f : c0580g.f544jk) {
                this.this$0.f574Yc.f506Tc.add(c0579f);
            }
        }
        C0578e c0578e = this.this$0.f574Yc;
        c0578e.f491Fd = c0580g;
        if (c0578e.f528ud.mIndex == 4) {
            c0578e.f486Dd.m450c(c0578e.f491Fd);
        }
    }
}
