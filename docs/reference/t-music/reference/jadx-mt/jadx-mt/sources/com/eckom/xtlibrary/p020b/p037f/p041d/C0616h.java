package com.eckom.xtlibrary.p020b.p037f.p041d;

import android.content.Context;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0578e;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0579f;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0580g;
import com.eckom.xtlibrary.p020b.p037f.p043f.C0643h;
import java.util.ArrayList;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.f.d.h */
/* JADX INFO: compiled from: MusicID3Model.java */
/* JADX INFO: loaded from: classes3.dex */
class C0616h implements C0643h.a {
    final /* synthetic */ C0628t this$0;

    C0616h(C0628t c0628t) {
        this.this$0 = c0628t;
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p043f.C0643h.a
    /* JADX INFO: renamed from: S */
    public void mo455S() {
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p043f.C0643h.a
    /* JADX INFO: renamed from: a */
    public void mo456a(C0580g c0580g, String str) {
        if (c0580g.mLength > 0) {
            this.this$0.f659Yc.f506Tc = new ArrayList<>();
            for (C0579f c0579f : c0580g.f544jk) {
                this.this$0.f659Yc.f506Tc.add(c0579f);
            }
        }
        C0578e c0578e = this.this$0.f659Yc;
        c0578e.f491Fd = c0580g;
        if (c0578e.f528ud.mIndex == 4) {
            c0578e.f486Dd.m450c(c0578e.f491Fd);
            this.this$0.mo505Ab();
        }
        if (this.this$0.f659Yc.f515ck.startsWith("/data/tw/.like")) {
            C0578e c0578e2 = this.this$0.f659Yc;
            c0578e2.f486Dd.m450c(c0578e2.f491Fd);
            this.this$0.m648Te();
        } else if (this.this$0.f659Yc.f515ck.startsWith("/data/tw/")) {
            C0628t c0628t = this.this$0;
            C0578e c0578e3 = c0628t.f659Yc;
            C0643h.m757b(c0578e3.f486Dd, c0578e3.f515ck, c0628t.isForward, new C0609b(this));
        } else {
            Context context = this.this$0.mContext;
            C0628t c0628t2 = this.this$0;
            C0578e c0578e4 = c0628t2.f659Yc;
            C0643h.m750a(context, c0578e4.f486Dd, c0578e4.f515ck, c0578e4.f506Tc, c0628t2.isForward, new C0611c(this));
        }
    }
}
